package com.lastminute.axon.marsrover.query

import com.lastminute.axon.marsrover.Position
import com.lastminute.axon.marsrover.RoverLandedEvent

class RoverTrailProjection {

    fun on(roverLandedEvent: RoverLandedEvent) {
        TODO("Not yet implemented")
    }

    fun trailFor(roverId: String): Trail {
        TODO("Not yet implemented")
    }


}


data class Trail(val roverId: String,  val path: List<Position>)