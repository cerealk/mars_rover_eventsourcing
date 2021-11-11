package com.lastminute.axon.marsrover.domain.query

import com.lastminute.axon.marsrover.domain.command.Coordinates
import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMapQuery
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMappedEvent
import com.lastminute.axon.marsrover.domain.coreapi.RoverLandedEvent
import com.lastminute.axon.marsrover.domain.coreapi.RoverMovedEvent
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class PlanetMapProjection {

    private val planetToRovers = mutableMapOf<String, MutableSet<RoverPosition>>()
    private val roverToPlanet = mutableMapOf<String, String>()
    private val planetToObstacles = mutableMapOf<String, PlanetMap>()

    @EventHandler
    fun on(event: PlanetMappedEvent) {
        planetToObstacles[event.planetName] = event.planetMap
    }

    @EventHandler
    fun on(event: RoverLandedEvent) {
        val roversOnPlanet =
            planetToRovers.getOrPut(event.planet) { mutableSetOf() }

        roversOnPlanet.add(RoverPosition(event.rover, event.position))
        roverToPlanet[event.rover] = event.planet
    }


    @EventHandler
    fun on(event: RoverMovedEvent) {

        val planet: String =
            roverToPlanet[event.rover] ?: throw IllegalArgumentException("Rover ${event.rover} never landed")

        val roversOnPlanet =
            planetToRovers[planet] ?: throw IllegalArgumentException("no rover on planet $planet")

        roversOnPlanet.removeIf { it.rover == event.rover }

        roversOnPlanet.add(RoverPosition(event.rover, event.position))

    }

    fun mapOf(q: PlanetMapQuery): PlanetMap {

        val obstacles = planetToObstacles[q.planet] ?: throw IllegalArgumentException("planetNotFound!!!")
        val rovers = planetToRovers[q.planet]?.map {
            it.coordinates
        }?: emptyList()
        return obstacles.copy (obstacles = obstacles.obstacles + rovers)
    }

    private data class RoverPosition(val rover: String, val coordinates: Coordinates)

}