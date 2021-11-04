package com.lastminute.axon.marsrover.domain.query

import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMapQuery
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMappedEvent
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class PlanetMapProjection {

    private val repo = mutableMapOf<String, PlanetMap>()

    @EventHandler
    fun on(planetMappedEvent: PlanetMappedEvent) {
        repo[planetMappedEvent.planetName] = planetMappedEvent.planetMap
    }

    fun mapOf(planetName: String, q: PlanetMapQuery): PlanetMap = repo[planetName]?:throw IllegalArgumentException("planetNotFound!!!")
}