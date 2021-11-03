package com.lastminute.axon.marsrover.domain.service

import com.lastminute.axon.marsrover.domain.command.Position

interface SatelliteSystem {
    fun probe(): List<Position>
}