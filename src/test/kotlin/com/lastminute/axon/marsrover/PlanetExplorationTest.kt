package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Position
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.Test

class PlanetExplorationTest {

    private val fixture: FixtureConfiguration<PlanetCartography> = AggregateTestFixture(PlanetCartography::class.java)


    @Test
    internal fun `can probe the surface of a planet`() {
        fixture
            .registerInjectableResource(TestSatelliteProbeService(listOf(Position(3, 7))))
            .given()
            .`when`(ProbePlanetCommand("Mars"))
            .expectSuccessfulHandlerExecution()
            .expectEvents(PlanetMappedEvent("Mars", PlanetMap(listOf(Position(3, 7)))))
    }
}

@Aggregate
class PlanetCartography {

    @AggregateIdentifier
    private lateinit var planetName: String

    @CommandHandler
    constructor(command: ProbePlanetCommand, externalService: TestSatelliteProbeService) {
        AggregateLifecycle.apply(PlanetMappedEvent(command.planetName, PlanetMap(externalService.probe())))
    }

    @EventSourcingHandler
    fun on(event: PlanetMappedEvent) {
        planetName = event.planetName
    }
}

data class ProbePlanetCommand(val planetName: String)
data class PlanetMappedEvent(val planetName: String, val planetMap: PlanetMap)


class TestSatelliteProbeService(private val obstaclesCoordinates: List<Position>) {
    fun probe() = obstaclesCoordinates
}