package com.lastminute.axon.marsrover.domain.coreapi

import com.lastminute.axon.marsrover.domain.command.Orientation
import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Position
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface Command

data class DropLanderCommand(@TargetAggregateIdentifier val rover: String, val position: Position, val orientation: Orientation):
    Command
object MoveForwardCommand : Command
object MoveBackwardCommand : Command
object RotateLeftCommand : Command
object RotateRightCommand : Command
data class FollowPathCommand(
    @TargetAggregateIdentifier val rover: String,
    val commands: List<Command>,
    val planetMap: PlanetMap = PlanetMap(emptyList())
)