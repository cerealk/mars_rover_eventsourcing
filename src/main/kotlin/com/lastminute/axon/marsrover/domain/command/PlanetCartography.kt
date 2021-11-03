package com.lastminute.axon.marsrover.domain.command

import com.lastminute.axon.marsrover.domain.coreapi.PlanetMappedEvent
import com.lastminute.axon.marsrover.domain.coreapi.ProbePlanetCommand
import com.lastminute.axon.marsrover.domain.service.SatelliteSystem
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class PlanetCartography {

    @AggregateIdentifier
    private lateinit var planetName: String

    @CommandHandler
    constructor(command: ProbePlanetCommand, externalService: SatelliteSystem) {
        AggregateLifecycle.apply(PlanetMappedEvent(command.planetName, PlanetMap(externalService.probe())))
    }

    @EventSourcingHandler
    fun on(event: PlanetMappedEvent) {
        planetName = event.planetName
    }
}