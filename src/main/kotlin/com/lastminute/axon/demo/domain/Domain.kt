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
    private lateinit var orientation: Orientation

    @CommandHandler
    constructor(command: DropLanderCommand) {
        AggregateLifecycle.apply(RoverLandedEvent(command.rover, command.position, command.orientation))
    }

    @EventHandler
    fun landing(event: RoverLandedEvent) {
        roverName = event.rover
        currentRoverPosition = event.position
        orientation = event.orientation
    }



    private fun nextClick(direction: Direction): Position {
        val delta = when (direction) {
            F -> 1
            B -> -1
        }
        return when (orientation) {
            N -> currentRoverPosition.copy(y = currentRoverPosition.y + delta)
            S -> currentRoverPosition.copy(y = currentRoverPosition.y - delta)
            E -> currentRoverPosition.copy(x = currentRoverPosition.x + delta)
            W -> currentRoverPosition.copy(x = currentRoverPosition.x - delta)
        }
    }

    @CommandHandler
    fun movePath(command: FollowPathCommand) {

        command.commands.fold(true) { clearPath, cmd ->
            if (!clearPath)
                false
            else {
                when (cmd) {
                    is MoveForwardCommand -> {
                        moveIfFree(F, command.planetMap)
                    }
                    is MoveBackwardCommand -> {
                        moveIfFree(B, command.planetMap)
                    }
                    is RotateLeftCommand -> {
                        AggregateLifecycle.apply(RoverTurnedEvent(orientation.rotateLeft(), L))
                        true
                    }
                    is RotateRightCommand -> {
                        AggregateLifecycle.apply(RoverTurnedEvent(orientation.rotateRight(), R))
                        true
                    }
                    else -> false
                }
            }

        }
    }

    private fun moveIfFree(
        direction: Direction,
        planetMap: PlanetMap
    ): Boolean {
        val targetPosition = nextClick(direction)
        val canMove = planetMap.probe(targetPosition)
        if (canMove) {
            AggregateLifecycle.apply(RoverMovedEvent(targetPosition, direction))
        } else {
            AggregateLifecycle.apply(ObstacleFoundEvent(targetPosition))
        }
        return canMove
    }

    @EventHandler
    fun handleRoverRotation(event: RoverTurnedEvent) {
        orientation = event.newOrientation
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
enum class Orientation {
    N, S, W, E;

    fun rotateLeft() = when(this){
        N -> W
        S -> E
        W -> S
        E -> N
    }

    fun rotateRight(): Orientation = when (this) {
        N -> E
        S -> W
        W -> N
        E -> S
    }
}
enum class Direction { F, B }
enum class Rotation { L, R }