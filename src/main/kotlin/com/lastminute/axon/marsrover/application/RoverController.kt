package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.domain.command.Orientation.N
import com.lastminute.axon.marsrover.domain.command.Position
import com.lastminute.axon.marsrover.domain.coreapi.DropRoverCommand
import com.lastminute.axon.marsrover.domain.coreapi.FollowPathCommand
import com.lastminute.axon.marsrover.domain.query.Trail
import com.lastminute.axon.marsrover.domain.query.TrailQuery
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
        commandGateway.send<Any>(DropRoverCommand(planet, Position(1,1), N))
    }


    @PostMapping("/{planetName}/{roverName}/{moves}")
    fun move(@PathVariable("roverName") rover: String, @PathVariable("moves") moves: String){

        val commands = commandParser.parseCommands(moves)
        commandGateway.send<Any>(FollowPathCommand(rover, commands))
    }

    @GetMapping("/{planetName}/{roverName}/trail")
    fun trailFor(@PathVariable("roverName") rover: String) = queryGateway.query(TrailQuery(rover), Trail::class.java)
}