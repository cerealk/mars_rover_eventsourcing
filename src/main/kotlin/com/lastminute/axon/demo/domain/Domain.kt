package com.lastminute.axon.demo

import com.lastminute.axon.demo.Direction.B
import com.lastminute.axon.demo.Direction.F
import com.lastminute.axon.demo.Orientation.*
import com.lastminute.axon.demo.Rotation.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Rover {

    @AggregateIdentifier
    private lateinit var roverName: String

    private lateinit var currentRoverPosition: Position
    private lateinit var currentRoverOrientation: Orientation

    @CommandHandler
    constructor(command: DropLanderCommand) {
        AggregateLifecycle.apply(RoverLandedEvent(command.rover, command.position, command.orientation))
    }

    @EventHandler
    fun landing(event: RoverLandedEvent) {
        roverName = event.rover
        currentRoverPosition = event.position
        currentRoverOrientation = event.orientation
    }

    @CommandHandler
    fun moveForward(command: MoveForwardCommand) {
        val newPosition = move(F)
        if (command.planetMap.probe(newPosition)) {
            val roverMovedEvent = RoverMovedEvent(newPosition, F)
            AggregateLifecycle.apply(roverMovedEvent)
        } else
            AggregateLifecycle.apply(ObstacleFoundEvent(newPosition))
    }

    @CommandHandler
    fun moveBackward(command: MoveBackwardCommand) {
        val newPosition = move(B)
        if (command.planetMap.probe(newPosition)) {
            AggregateLifecycle.apply(RoverMovedEvent(newPosition, B))
        } else {
            AggregateLifecycle.apply(ObstacleFoundEvent(newPosition))
        }
    }

    private fun move(direction: Direction): Position {
        val delta = when (direction) {
            F -> 1
            B -> -1
        }
        return when (currentRoverOrientation) {
            N -> currentRoverPosition.copy(y = currentRoverPosition.y + delta)
            S -> currentRoverPosition.copy(y = currentRoverPosition.y - delta)
            E -> currentRoverPosition.copy(x = currentRoverPosition.x + delta)
            W -> currentRoverPosition.copy(x = currentRoverPosition.x - delta)
        }
    }

    @CommandHandler
    fun rotateLeft(command: RotateLeftCommand) {
        val newDirection = rotateLeft()
        AggregateLifecycle.apply(RoverTurnedEvent(newDirection, L))
    }

    @CommandHandler
    fun rotateRight(command: RotateRightCommand) {
        val newDirection = rotateRight()
        AggregateLifecycle.apply(RoverTurnedEvent(newDirection, R))
    }

    @CommandHandler
    fun movePath(command: FollowPathCommand) {

        command.commands.fold(true) { clearPath, cmd ->
            if (!clearPath)
                false
            else {
                when (cmd) {
                    is MoveForwardCommand -> {
                        val newPosition = move(F)
                        if (cmd.planetMap.probe(newPosition)) {
                            AggregateLifecycle.apply(RoverMovedEvent(newPosition, F))
                            true
                        } else {
                            AggregateLifecycle.apply(ObstacleFoundEvent(newPosition))
                            false
                        }
                    }
                    is MoveBackwardCommand -> {
                        val newPosition = move(B)
                        if (cmd.planetMap.probe(newPosition)) {
                            AggregateLifecycle.apply(RoverMovedEvent(newPosition, B))
                            true
                        } else {
                            AggregateLifecycle.apply(ObstacleFoundEvent(newPosition))
                            false
                        }
                    }
                    is RotateLeftCommand -> {
                        val newDirection = rotateLeft()
                        AggregateLifecycle.apply(RoverTurnedEvent(newDirection, L))
                        true
                    }
                    is RotateRightCommand -> {
                        val newDirection = rotateRight()
                        AggregateLifecycle.apply(RoverTurnedEvent(newDirection, R))
                        true
                    }
                    else -> false
                }
            }

        }
    }

    private fun rotateLeft(): Orientation = when (currentRoverOrientation) {
        N -> W
        S -> E
        W -> S
        E -> N
    }

    private fun rotateRight(): Orientation = when (currentRoverOrientation) {
        N -> E
        S -> W
        W -> N
        E -> S
    }

    @EventHandler
    fun handleRoverRotation(event: RoverTurnedEvent) {
        currentRoverOrientation = event.newOrientation
    }

    @EventHandler
    fun handleRoverMovement(event: RoverMovedEvent) {
        currentRoverPosition = event.position
    }
}

data class PlanetMap(val obstacles: List<Position> = emptyList()) {
    fun probe(newPosition: Position): Boolean = !obstacles.contains(newPosition)
}

data class Position(val x: Int, val y: Int)
enum class Orientation { N, S, W, E }
enum class Direction { F, B }
enum class Rotation { L, R }