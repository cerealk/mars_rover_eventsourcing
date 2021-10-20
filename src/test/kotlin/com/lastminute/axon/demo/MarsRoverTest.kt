package com.lastminute.axon.demo

import com.lastminute.axon.demo.Direction.B
import com.lastminute.axon.demo.Direction.F
import com.lastminute.axon.demo.Orientation.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.Test

class MarsRoverTest {

    private val  fixture: AggregateTestFixture<PlanetMap> = AggregateTestFixture(PlanetMap::class.java)


    @Test
    fun theRoverCanLand(){

        val landingSpot = Position(1,2)
        val landingOrientation = N
        val dropLanderCommand = DropLanderCommand("Mars", landingSpot, landingOrientation)

        fixture.`when`(dropLanderCommand).expectSuccessfulHandlerExecution().expectEvents(RoverLandedEvent(landingSpot, landingOrientation))
    }

    @Test
    fun theRoverCanMoveForward(){

        val newPosition = Position(1,3)
        val direction = F
        fixture.given(RoverLandedEvent(Position(1, 2), N))
            .`when`(MoveForwardCommand("Mars")).
                expectSuccessfulHandlerExecution().
                expectEvents(RoverMovedEvent(newPosition, direction))
    }

    @Test
    fun theRoverCanMoveBackward(){

        val newPosition = Position(1,1)
        val direction = B
        fixture.given(RoverLandedEvent(Position(1, 2), N))
            .`when`(MoveBackwardCommand("Mars")).
            expectSuccessfulHandlerExecution().
            expectEvents(RoverMovedEvent(newPosition, direction))
    }

}

@Aggregate
class PlanetMap {
    constructor() {}


    @AggregateIdentifier
    private val planet= "Mars"

    private lateinit var currentRoverPosition:Position
    private lateinit var currentRoverOrientation: Orientation

    @CommandHandler
    constructor(command: DropLanderCommand){
        AggregateLifecycle.apply(RoverLandedEvent(command.position, command.orientation))
    }

    @EventHandler
    fun landing(event: RoverLandedEvent){
        currentRoverPosition = event.position
        currentRoverOrientation = event.orientation
    }

    @CommandHandler
    fun moveForward(command: MoveForwardCommand) {
        AggregateLifecycle.apply(RoverMovedEvent(move(F), F))
    }

    @CommandHandler
    fun moveBackward(command: MoveBackwardCommand) {
        AggregateLifecycle.apply(RoverMovedEvent(move(B), B))
    }

    private fun move(direction: Direction): Position {
        val delta = when (direction) {
            F -> 1
            B -> -1
        }
        return when (currentRoverOrientation) {
            N -> currentRoverPosition.copy(y = currentRoverPosition.y + delta)
            S -> currentRoverPosition.copy(y = currentRoverPosition.y + delta)
            E -> currentRoverPosition.copy(x = currentRoverPosition.x + delta)
            W -> currentRoverPosition.copy(y = currentRoverPosition.x + delta)
        }
    }




}

data class Position(val x:Int, val y:Int)

enum class Orientation {N,S,W,E}
enum class Direction {F,B}
data class DropLanderCommand(@TargetAggregateIdentifier val planet: String, val position: Position, val orientation:Orientation)

data class MoveForwardCommand (@TargetAggregateIdentifier val planet:String)
data class MoveBackwardCommand (@TargetAggregateIdentifier val Planet:String)


data class RoverLandedEvent(val position: Position, val orientation: Orientation)
data class RoverMovedEvent(val position: Position, val direction: Direction)

