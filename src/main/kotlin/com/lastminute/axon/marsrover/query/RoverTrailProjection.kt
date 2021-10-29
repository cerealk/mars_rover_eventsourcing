package com.lastminute.axon.marsrover.query

import com.lastminute.axon.marsrover.Position
import com.lastminute.axon.marsrover.RoverLandedEvent
import com.lastminute.axon.marsrover.RoverMovedEvent
import org.axonframework.eventhandling.EventHandler

class RoverTrailProjection {

    //TODO: this is a good candidate to be moved to a repository
    private val roversTrail = mutableMapOf<String, MutableList<Position>>()

    @EventHandler
    fun on(event: RoverLandedEvent) {
        val t = roversTrail.getOrPut(event.rover) { mutableListOf() }
        t.add(event.position)
    }

    fun trailFor(roverId: String): Trail {
        return Trail(roverId, roversTrail.getOrDefault(roverId, mutableListOf()))
    }

    @EventHandler
    fun on(event: RoverMovedEvent) {
        val t = roversTrail.getOrPut(event.rover) { mutableListOf() }
        t.add(event.position)
    }


}


data class Trail(val roverId: String,  val path: List<Position>)