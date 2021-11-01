package com.lastminute.axon.marsrover.domain.coreapi

import com.lastminute.axon.marsrover.domain.command.Direction
import com.lastminute.axon.marsrover.domain.command.Orientation
import com.lastminute.axon.marsrover.domain.command.Position
import com.lastminute.axon.marsrover.domain.command.Rotation

interface Event

data class RoverLandedEvent(val rover: String, val position: Position, val orientation: Orientation) : Event
data class RoverMovedEvent(val rover: String, val position: Position, val direction: Direction) : Event
data class RoverTurnedEvent(val newOrientation: Orientation, val rotation: Rotation) : Event
data class ObstacleFoundEvent(val position: Position) : Event