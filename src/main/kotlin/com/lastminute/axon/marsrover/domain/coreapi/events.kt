package com.lastminute.axon.marsrover.domain.coreapi

import com.lastminute.axon.marsrover.domain.command.*

interface RoverEvent

data class RoverLandedEvent(
    val rover: String,
    val planet: String,
    val position: Coordinates,
    val orientation: Orientation
) : RoverEvent
data class RoverExplodedEvent(val rover: String, val impactLocation: Coordinates) :RoverEvent
data class RoverMovedEvent(val rover: String, val position: Coordinates, val direction: Direction) : RoverEvent
data class RoverTurnedEvent(val newOrientation: Orientation, val rotation: Rotation) : RoverEvent
data class ObstacleFoundEvent(val position: Coordinates) : RoverEvent

data class PlanetMappedEvent(val planetName: String, val planetMap: PlanetMap)