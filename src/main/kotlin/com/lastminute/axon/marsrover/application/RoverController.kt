package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.domain.command.Orientation
import com.lastminute.axon.marsrover.domain.command.Position
import com.lastminute.axon.marsrover.domain.coreapi.DropLanderCommand
import com.lastminute.axon.marsrover.domain.coreapi.FollowPathCommand
import com.lastminute.axon.marsrover.domain.query.Trail
import com.lastminute.axon.marsrover.domain.query.TrailQuery
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RoverController {

    @Autowired
    lateinit var commandGateway: CommandGateway

    @Autowired
    lateinit var queryGateway: QueryGateway

    val commandParser = CommandParser()

    @RequestMapping("rover/{moves}")
    fun move(@PathVariable("moves") moves: String): Trail {
        val commands = commandParser.parseCommands(moves)

        //a command creating an aggregate returns the Id of the aggreegate itself (?)
        val res = commandGateway.sendAndWait<Any>(
            DropLanderCommand("Mars1", Position(1,2), Orientation.N)
        )

        //TODO: here the return value is null
        commandGateway.sendAndWait<Any>(FollowPathCommand("Mars1", commands))

        return queryGateway.query(TrailQuery("Mars1"), Trail::class.java).get()
    }
}