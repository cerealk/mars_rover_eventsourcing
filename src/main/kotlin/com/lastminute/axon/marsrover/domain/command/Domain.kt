package com.lastminute.axon.marsrover.domain.command

import com.lastminute.axon.marsrover.domain.command.Direction.B
import com.lastminute.axon.marsrover.domain.command.Direction.F
import com.lastminute.axon.marsrover.domain.command.Orientation.*
import com.lastminute.axon.marsrover.domain.command.Rotation.*
import com.lastminute.axon.marsrover.domain.coreapi.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
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

    @EventSourcingHandler
    fun landing(event: RoverLandedEvent) {
        roverName = event.rover
        currentRoverPosition = event.position
        orientation = event.orientation
    }


    @CommandHandler
    fun movePath(command: FollowPathCommand) {

        command.commands.fold(true) { clearPath, cmd ->
            if (!clearPath)
                false
            else {
                val event = when (cmd) {
                    is MoveForwardCommand -> move(command.rover, F, command.planetMap)
                    is MoveBackwardCommand -> move(command.rover, B, command.planetMap)
                    is RotateLeftCommand -> RoverTurnedEvent(orientation.left(), L)
                    is RotateRightCommand -> RoverTurnedEvent(orientation.right(), R)
                    else ->  throw IllegalArgumentException()
                }

                AggregateLifecycle.apply(event)

                event !is ObstacleFoundEvent

            }

        }
    }

    private fun move(
        rover: String,
        direction: Direction,
        planetMap: PlanetMap
    ): RoverEvent {
        val targetPosition = nextClick(direction)
        val canMove = planetMap.probe(targetPosition)
        return if (canMove) RoverMovedEvent(rover, targetPosition, direction) else ObstacleFoundEvent(targetPosition)
    }

    private fun nextClick(direction: Direction): Position = when (orientation) {
        N -> currentRoverPosition.copy(y = currentRoverPosition.y + direction.delta)
        S -> currentRoverPosition.copy(y = currentRoverPosition.y - direction.delta)
        E -> currentRoverPosition.copy(x = currentRoverPosition.x + direction.delta)
        W -> currentRoverPosition.copy(x = currentRoverPosition.x - direction.delta)
    }

    @EventSourcingHandler
    fun handleRoverRotation(event: RoverTurnedEvent) {
        orientation = event.newOrientation
    }

    @EventSourcingHandler
    fun handleRoverMovement(event: RoverMovedEvent) {
        currentRoverPosition = event.position
    }
}

interface Command
object MoveForwardCommand : Command
object MoveBackwardCommand : Command
object RotateLeftCommand : Command
object RotateRightCommand : Command

//TODO: find where to put these datastructures
data class PlanetMap(val obstacles: List<Position> = emptyList()) {
    fun probe(newPosition: Position): Boolean = !obstacles.contains(newPosition)
}

data class Position(val x: Int, val y: Int)

enum class Orientation {
    N, S, W, E;

    fun left() = when (this) {
        N -> W
        S -> E
        W -> S
        E -> N
    }

    fun right(): Orientation = when (this) {
        N -> E
        S -> W
        W -> N
        E -> S
    }
}

enum class Direction(val delta: Int) { F(1), B(-1); }
enum class Rotation { L, R }