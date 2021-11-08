package com.lastminute.axon.marsrover.domain.query

import com.lastminute.axon.marsrover.domain.command.Coordinates
import com.lastminute.axon.marsrover.domain.coreapi.RoverLandedEvent
import com.lastminute.axon.marsrover.domain.coreapi.RoverMovedEvent
import com.lastminute.axon.marsrover.domain.coreapi.TrailQuery
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class RoverTrailProjection {

    //TODO: this is a good candidate to be moved to a repository
    private val roversTrail = mutableMapOf<String, MutableList<Coordinates>>()

    @EventHandler
    fun on(event: RoverLandedEvent) {
        val t = roversTrail.getOrPut(event.rover) { mutableListOf() }
        t.add(event.position)
    }

    @EventHandler
    fun on(event: RoverMovedEvent) {
        val t = roversTrail.getOrPut(event.rover) { mutableListOf() }
        t.add(event.position)
    }

    @QueryHandler
    fun trailFor(query: TrailQuery): Trail {
        val roverId = query.roverId
        return Trail(roverId, roversTrail.getOrDefault(roverId, mutableListOf()))
    }

}


data class Trail(val roverId: String,  val path: List<Coordinates>)