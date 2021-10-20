package com.lastminute.axon.demo

import com.lastminute.axon.demo.Direction.B
import com.lastminute.axon.demo.Direction.F
import com.lastminute.axon.demo.Orientation.*
import com.lastminute.axon.demo.Rotation.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.Test

class MarsRoverTest {

    private val  fixture: AggregateTestFixture<Rover> = AggregateTestFixture(Rover::class.java)


    @Test
    fun theRoverCanLand(){

        val landingSpot = Position(1,2)
        val landingOrientation = N
        val dropLanderCommand = DropLanderCommand("Mars", landingSpot, landingOrientation)

        fixture.`when`(dropLanderCommand).expectSuccessfulHandlerExecution().expectEvents(RoverLandedEvent(
            "Mars",
            landingSpot,
            landingOrientation
        ))
    }

    @Test
    fun theRoverCanMoveForward(){

        val newPosition = Position(1,3)
        val direction = F
        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .`when`(MoveForwardCommand("Mars")).
                expectSuccessfulHandlerExecution().
                expectEvents(RoverMovedEvent(newPosition, direction))
    }

    @Test
    fun theRoverCanMoveBackward(){

        val newPosition = Position(1,1)
        val direction = B
        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .`when`(MoveBackwardCommand("Mars")).
            expectSuccessfulHandlerExecution().
            expectEvents(RoverMovedEvent(newPosition, direction))
    }

    @Test
    fun `the rover can rotate left`() {

        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .`when`(RotateLeftCommand("Mars"))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverTurnedEvent(W, L))

    }

    @Test
    fun `the rover can rotate right`() {

        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .`when`(RotateRightCommand("Mars"))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverTurnedEvent(E, R))

    }

    @Test
    fun `after a rotation the rover move accordingly`(){
        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .andGiven(RoverTurnedEvent(W,L))
            .`when`(MoveForwardCommand("Mars"))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverMovedEvent(Position(0,2), F))
    }

}

@Aggregate
class Rover {
    constructor() {}


    @AggregateIdentifier
    private val roverName= "Mars"

    private lateinit var currentRoverPosition:Position
    private lateinit var currentRoverOrientation: Orientation

    @CommandHandler
    constructor(command: DropLanderCommand){
        AggregateLifecycle.apply(RoverLandedEvent(command.rover, command.position, command.orientation))
    }

    @EventHandler
    fun landing(event: RoverLandedEvent){
        currentRoverPosition = event.position
        currentRoverOrientation = event.orientation
    }

    @CommandHandler
    fun moveForward(command: MoveForwardCommand) {
        val roverMovedEvent = RoverMovedEvent(move(F), F)
        AggregateLifecycle.apply(roverMovedEvent)
    }

    @CommandHandler
    fun moveBackward(command: MoveBackwardCommand) {
        val roverMovedEvent = RoverMovedEvent(move(B), B)
        AggregateLifecycle.apply(roverMovedEvent)
    }

    private fun move(direction: Direction): Position {
        val delta = when (direction) {
            F -> 1
            B -> -1
        }
        return when (currentRoverOrientation) {
            N -> currentRoverPosition.copy(y = currentRoverPosition.y + delta)
            S -> currentRoverPosition.copy(y = currentRoverPosition.y - delta)
            E -> currentRoverPosition.copy(x = currentRoverPosition.x + delta)
            W -> currentRoverPosition.copy(x = currentRoverPosition.x - delta)
        }
    }

    @CommandHandler
    fun rotateLeft(command:RotateLeftCommand){
        val newDirection = rotateLeft()
        AggregateLifecycle.apply(RoverTurnedEvent(newDirection, L))
    }

    @CommandHandler
    fun rotateRight(command:RotateRightCommand){
        val newDirection = rotateRight()
        AggregateLifecycle.apply(RoverTurnedEvent(newDirection, R))
    }

    private fun rotateLeft(): Orientation = when (currentRoverOrientation){
        N -> W
        S -> E
        W -> S
        E -> N
    }

    private fun rotateRight(): Orientation = when (currentRoverOrientation){
        N -> E
        S -> W
        W -> N
        E -> S
    }

    @EventHandler
    fun handleRoverRotation(event: RoverTurnedEvent){
        currentRoverOrientation = event.newOrientation
    }

}

data class Position(val x:Int, val y:Int)

enum class Orientation {N,S,W,E}
enum class Direction {F,B}
enum class Rotation {L, R}

data class DropLanderCommand(@TargetAggregateIdentifier val rover: String, val position: Position, val orientation:Orientation)
data class MoveForwardCommand (@TargetAggregateIdentifier val planet:String)
data class MoveBackwardCommand (@TargetAggregateIdentifier val Planet:String)
data class RotateLeftCommand (@TargetAggregateIdentifier val Planet:String)
data class RotateRightCommand (@TargetAggregateIdentifier val Planet:String)


data class RoverLandedEvent(val rover: String, val position: Position, val orientation: Orientation)
data class RoverMovedEvent(val position: Position, val direction: Direction)
data class RoverTurnedEvent(val newOrientation: Orientation, val rotation:Rotation )

