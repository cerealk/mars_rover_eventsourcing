package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.domain.command.PlanetCartography
import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Position
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMappedEvent
import com.lastminute.axon.marsrover.domain.coreapi.ProbePlanetCommand
import com.lastminute.axon.marsrover.domain.service.SatelliteSystem
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.Test

class PlanetExplorationTest {

    private val fixture: FixtureConfiguration<PlanetCartography> = AggregateTestFixture(PlanetCartography::class.java)


    @Test
    internal fun `can probe the surface of a planet`() {
        fixture
            .registerInjectableResource(TestSatelliteProbeService(listOf(Position(3, 7))))
            .givenNoPriorActivity()
            .`when`(ProbePlanetCommand("Mars"))
            .expectSuccessfulHandlerExecution()
            .expectEvents(PlanetMappedEvent("Mars", PlanetMap(listOf(Position(3, 7)))))
    }
}


class TestSatelliteProbeService(private val obstaclesCoordinates: List<Position>): SatelliteSystem {
    override fun probe() = obstaclesCoordinates
}