package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Position
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMapQuery
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMappedEvent
import com.lastminute.axon.marsrover.domain.query.PlanetMapProjection
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PlanetaryMapTest {

    val planetaryAtlas = PlanetMapProjection()

    @Test
    internal fun `once a planet is probed by a satellite system it's cartography is persisted`() {
        planetaryAtlas.on(PlanetMappedEvent("Mars", PlanetMap(listOf(Position(1,1)))))

        planetaryAtlas.mapOf("Mars", PlanetMapQuery("Mars")) shouldBe PlanetMap(listOf(Position(1,1)))
    }
}