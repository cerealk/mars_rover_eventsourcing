package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Position
import com.lastminute.axon.marsrover.domain.command.Rover
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.eventsourcing.GenericAggregateFactory
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory
import org.axonframework.spring.stereotype.Aggregate
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.Test

class PlanetExplorationTest {

    private val  fixture: AggregateTestFixture<Planet> = AggregateTestFixture(Planet::class.java)

    @Test
    internal fun `can probe the surface of a planet`() {
        fixture.`when`(ProbePlanetCommand("Mars"))
            .expectSuccessfulHandlerExecution()
            .expectEvents(PlanetMappedEvent("Mars", PlanetMap(listOf(Position(3,7)))))
    }
}

@Aggregate
class Planet {

    @AggregateIdentifier
    val planetName:String

    @CommandHandler
    constructor(command: ProbePlanetCommand){
        planetName = command.planetName
    }
}


//class PlanetAggregateFactory(): SpringPrototypeAggregateFactory<Planet>(){}


data class ProbePlanetCommand(val planetName:String)
data class PlanetMappedEvent(val planetName:String, val planetMap:PlanetMap)