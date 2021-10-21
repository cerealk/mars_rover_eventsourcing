package com.lastminute.axon.demo

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class DropLanderCommand(@TargetAggregateIdentifier val rover: String, val position: Position, val orientation: Orientation)
data class MoveForwardCommand (@TargetAggregateIdentifier val rover:String)
data class MoveBackwardCommand (@TargetAggregateIdentifier val rover:String)
data class RotateLeftCommand (@TargetAggregateIdentifier val rover:String)
data class RotateRightCommand (@TargetAggregateIdentifier val rover:String)