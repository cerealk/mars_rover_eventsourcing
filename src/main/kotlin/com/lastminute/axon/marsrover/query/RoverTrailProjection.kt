package com.lastminute.axon.marsrover.query

import com.lastminute.axon.marsrover.Position
import com.lastminute.axon.marsrover.RoverLandedEvent

class RoverTrailProjection {

    //TODO: this is a good candidate to be moved to a repository
    private val roversTrail = mutableMapOf<String, MutableList<Position>>()

    fun on(c: RoverLandedEvent) {
        val t = roversTrail.getOrPut(c.rover) { mutableListOf() }
        t.add(c.position)
    }

    fun trailFor(roverId: String): Trail {
        return Trail(roverId, roversTrail.getOrDefault(roverId, mutableListOf()))
    }


}


data class Trail(val roverId: String,  val path: List<Position>)