package com.lastminute.axon.marsrover.domain.coreapi

import com.lastminute.axon.marsrover.domain.command.Position

data class TrailQuery(val roverId:String)
data class Trail(val roverId: String,  val path: List<Position>)

data class PlanetMapQuery(val planet:String)
