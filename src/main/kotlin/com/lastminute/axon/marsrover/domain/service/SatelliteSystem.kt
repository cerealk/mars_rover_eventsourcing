package com.lastminute.axon.marsrover.domain.service

import com.lastminute.axon.marsrover.domain.command.Coordinates

interface SatelliteSystem {
    fun probe(): List<Coordinates>
}