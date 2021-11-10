package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.domain.command.Orientation.N
import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Coordinates
import com.lastminute.axon.marsrover.domain.coreapi.DropRoverCommand
import com.lastminute.axon.marsrover.domain.coreapi.FollowPathCommand
import com.lastminute.axon.marsrover.domain.coreapi.PlanetMapQuery
import com.lastminute.axon.marsrover.domain.coreapi.TrailQuery
import com.lastminute.axon.marsrover.domain.query.Trail
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


//TODO: improve api :D
@RestController
class RoverController {

    @Autowired
    lateinit var commandGateway: CommandGateway

    @Autowired
    lateinit var queryGateway: QueryGateway

    val commandParser = CommandParser()

    @PostMapping("/{planetName}/{roverName}")
    fun drop(@PathVariable("planetName") planet: String, @PathVariable("roverName")rover: String){
        queryGateway.query(PlanetMapQuery(planet), PlanetMap::class.java).thenApply {
            commandGateway.send<Any>(DropRoverCommand(rover, Coordinates(1, 1), N, it))
        }
    }


    @PostMapping("/{planetName}/{roverName}/{moves}")
    fun move(@PathVariable("planetName")planet: String, @PathVariable("roverName") rover: String, @PathVariable("moves") moves: String){
        val commands = commandParser.parseCommands(moves)
        queryGateway.query(PlanetMapQuery(planet), PlanetMap::class.java).thenApply {planetMap ->  commandGateway.send<Any>(FollowPathCommand(rover, commands, planetMap))}
    }

    @GetMapping("/{planetName}/{roverName}/trail")
    fun trailFor(@PathVariable("roverName") rover: String) = queryGateway.query(TrailQuery(rover), Trail::class.java)
}