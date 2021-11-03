package com.lastminute.axon.marsrover.domain.coreapi

import com.lastminute.axon.marsrover.domain.command.*

interface RoverEvent

data class RoverLandedEvent(val rover: String, val position: Position, val orientation: Orientation) : RoverEvent
data class RoverMovedEvent(val rover: String, val position: Position, val direction: Direction) : RoverEvent
data class RoverTurnedEvent(val newOrientation: Orientation, val rotation: Rotation) : RoverEvent
data class ObstacleFoundEvent(val position: Position) : RoverEvent

data class PlanetMappedEvent(val planetName: String, val planetMap: PlanetMap)