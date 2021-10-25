package com.lastminute.axon.demo

import org.axonframework.modelling.command.TargetAggregateIdentifier

interface Command

data class DropLanderCommand(@TargetAggregateIdentifier val rover: String, val position: Position, val orientation: Orientation): Command
data class MoveForwardCommand (@TargetAggregateIdentifier val rover:String, val planetMap: PlanetMap =PlanetMap()): Command
data class MoveBackwardCommand(@TargetAggregateIdentifier val rover: String, val planetMap: PlanetMap = PlanetMap()): Command
data class RotateLeftCommand (@TargetAggregateIdentifier val rover:String): Command
data class RotateRightCommand (@TargetAggregateIdentifier val rover:String): Command
data class FollowPathCommand(@TargetAggregateIdentifier val rover: String, val commands: List<Command>)