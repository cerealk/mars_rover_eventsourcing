package com.lastminute.axon.marsrover

interface Event

data class RoverLandedEvent(val rover: String, val position: Position, val orientation: Orientation) : Event
data class RoverMovedEvent(val rover: String, val position: Position, val direction: Direction) : Event
data class RoverTurnedEvent(val newOrientation: Orientation, val rotation: Rotation) : Event
data class ObstacleFoundEvent(val position: Position) : Event