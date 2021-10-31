package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.*
import com.lastminute.axon.marsrover.domain.query.Trail
import com.lastminute.axon.marsrover.domain.query.TrailQuery
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
class RoverController {

    @Autowired
    lateinit var commandGateway: CommandGateway

    @Autowired
    lateinit var queryGateway: QueryGateway

    @Autowired
    lateinit var eventStore: EmbeddedEventStore


    @RequestMapping("rover/{moves}")
    fun move(@PathVariable("moves") moves: String): Trail {
        val commandParser = CommandParser()
        val commands = commandParser.parseCommands(moves)

        commandGateway.sendAndWait<Any>(
            DropLanderCommand("Mars1", Position(1,2), Orientation.N)
        )

        commandGateway.sendAndWait<Any>(FollowPathCommand("Mars1", commands))

        return queryGateway.query(TrailQuery("Mars1"), Trail::class.java).get()
    }
}