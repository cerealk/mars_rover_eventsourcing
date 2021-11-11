package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Coordinates
import com.lastminute.axon.marsrover.domain.command.Direction
import com.lastminute.axon.marsrover.domain.command.Orientation
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMapQuery
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMappedEvent
import com.lastminute.axon.marsrover.domain.coreapi.RoverLandedEvent
import com.lastminute.axon.marsrover.domain.coreapi.RoverMovedEvent
import com.lastminute.axon.marsrover.domain.query.PlanetMapProjection
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PlanetaryMapTest {

    val planetaryAtlas = PlanetMapProjection()

    @Test
    internal fun `once a planet is probed by a satellite system it's cartography is persisted`() {
        planetaryAtlas.on(PlanetMappedEvent("Mars", PlanetMap("Mars", listOf(Coordinates(1,1)))))

        planetaryAtlas.mapOf(PlanetMapQuery("Mars")) shouldBe PlanetMap("Mars", listOf(Coordinates(1,1)))
    }

    @Test
    internal fun `the planetary cartography keeps track of rover positions as obstacle`() {
        planetaryAtlas.on(PlanetMappedEvent("Mars", PlanetMap("Mars", listOf(Coordinates(1,1)))))
        planetaryAtlas.on(RoverLandedEvent("Mars1", "Mars", Coordinates(1,4), Orientation.N))
        planetaryAtlas.on(RoverMovedEvent("Mars1", Coordinates(1,5), Direction.F))

        planetaryAtlas.mapOf( PlanetMapQuery("Mars")) shouldBe PlanetMap("Mars", listOf(Coordinates(1,1), Coordinates(1,5)))
    }
}