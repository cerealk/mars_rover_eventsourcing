package com.lastminute.axon.demo

import com.lastminute.axon.demo.Direction.B
import com.lastminute.axon.demo.Direction.F
import com.lastminute.axon.demo.Orientation.*
import com.lastminute.axon.demo.Rotation.*
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


