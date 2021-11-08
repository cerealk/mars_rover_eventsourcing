package com.lastminute.axon.marsrover.domain.coreapi

import com.lastminute.axon.marsrover.domain.command.Coordinates

data class TrailQuery(val roverId:String)
data class Trail(val roverId: String,  val path: List<Coordinates>)

data class PlanetMapQuery(val planet:String)
