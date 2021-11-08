package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.domain.command.Coordinates
import com.lastminute.axon.marsrover.domain.service.SatelliteSystem

class FakeSatelliteSystem:SatelliteSystem {
    override fun probe(): List<Coordinates> = listOf(Coordinates(10,10), Coordinates(10,9), Coordinates(4,5))
}