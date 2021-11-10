package com.lastminute.axon.marsrover.domain.query

import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMapQuery
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMappedEvent
import com.lastminute.axon.marsrover.domain.coreapi.RoverMovedEvent
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class PlanetMapProjection {

    private val repo = mutableMapOf<String, PlanetMap>()

    @EventHandler
    fun on(planetMappedEvent: PlanetMappedEvent) {
        repo[planetMappedEvent.planetName] = planetMappedEvent.planetMap
    }

//    @EventHandler
//    fun on(planetMappedEvent: RoverMovedEvent) {
//        repo[]
//    }

    fun mapOf(q: PlanetMapQuery): PlanetMap = repo[q.planet]?:throw IllegalArgumentException("planetNotFound!!!")
}