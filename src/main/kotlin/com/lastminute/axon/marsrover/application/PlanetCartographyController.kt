package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMapQuery
import com.lastminute.axon.marsrover.domain.coreapi.ProbePlanetCommand
import com.lastminute.axon.marsrover.domain.query.PlanetMapProjection
import com.lastminute.axon.marsrover.domain.service.SatelliteSystem
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
class PlanetCartographyController {

    @Autowired
    private lateinit var satelliteSystem: SatelliteSystem

    @Autowired
    lateinit var commandGateway: CommandGateway

    @Autowired
    lateinit var planetMapProjection:PlanetMapProjection

    @PostMapping("/planet/{planetName}")
    fun flyOverNewPlanet(@PathVariable("planetName") planetName: String) {
        commandGateway.sendAndWait<Any>(ProbePlanetCommand(planetName))
    }

    @GetMapping("/planet/{planetName}")
    fun getCartography(@PathVariable("planetName") planetName: String): PlanetMap =
        planetMapProjection.mapOf(planetName, PlanetMapQuery(planetName))


}