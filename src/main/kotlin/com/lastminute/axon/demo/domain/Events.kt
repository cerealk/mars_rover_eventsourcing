package com.lastminute.axon.demo

data class RoverLandedEvent(val rover: String, val position: Position, val orientation: Orientation)
data class RoverMovedEvent(val position: Position, val direction: Direction)
data class RoverTurnedEvent(val newOrientation: Orientation, val rotation: Rotation)
data class ObstacleFoundEvent(val position:Position)