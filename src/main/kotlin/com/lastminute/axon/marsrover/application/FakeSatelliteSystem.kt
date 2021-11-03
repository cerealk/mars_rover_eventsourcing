package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.domain.command.Position
import com.lastminute.axon.marsrover.domain.service.SatelliteSystem

class FakeSatelliteSystem:SatelliteSystem {
    override fun probe(): List<Position> = listOf(Position(10,10), Position(10,9), Position(4,5))
}