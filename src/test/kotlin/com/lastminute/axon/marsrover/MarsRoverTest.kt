package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.domain.command.Direction.B
import com.lastminute.axon.marsrover.domain.command.Direction.F
import com.lastminute.axon.marsrover.domain.command.Orientation.*
import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Position
import com.lastminute.axon.marsrover.domain.command.Rotation.*
import com.lastminute.axon.marsrover.domain.command.Rover
import com.lastminute.axon.marsrover.domain.coreapi.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.Test

class MarsRoverTest {

    private val  fixture: AggregateTestFixture<Rover> = AggregateTestFixture(Rover::class.java)


    @Test
    fun theRoverCanLand(){

        val landingSpot = Position(1,2)
        val landingOrientation = N
        val dropLanderCommand = DropLanderCommand("Mars", landingSpot, landingOrientation)

        fixture.`when`(dropLanderCommand).expectSuccessfulHandlerExecution().expectEvents(
            RoverLandedEvent(
            "Mars",
            landingSpot,
            landingOrientation
        )
        )
    }

    @Test
    fun theRoverCanMoveForward(){

        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .`when`(FollowPathCommand("Mars", listOf(MoveForwardCommand))).
                expectSuccessfulHandlerExecution().
                expectEvents(RoverMovedEvent("Mars", Position(1, 3), F))
    }

    @Test
    fun theRoverCanMoveBackward(){

        val newPosition = Position(1,1)
        val direction = B
        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .`when`(FollowPathCommand("Mars", listOf(MoveBackwardCommand))).
            expectSuccessfulHandlerExecution().
            expectEvents(RoverMovedEvent("Mars", newPosition, direction))
    }

    @Test
    fun `the rover can rotate left`() {

        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .`when`(FollowPathCommand("Mars", listOf(RotateLeftCommand)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverTurnedEvent(W, L))

    }

    @Test
    fun `the rover can rotate right`() {

        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .`when`(FollowPathCommand("Mars", listOf(RotateRightCommand)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverTurnedEvent(E, R))

    }

    @Test
    fun `after a rotation the rover move accordingly`(){
        fixture.given(RoverLandedEvent("Mars", Position(1, 2), N))
            .andGiven(RoverTurnedEvent(W,L))
            .`when`(FollowPathCommand("Mars", listOf(MoveForwardCommand)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverMovedEvent("Mars", Position(0,2), F))
    }

    @Test
    internal fun `the rover can follow a path`() {
        fixture.given(RoverLandedEvent("Mars", Position(1,2), N))
            .`when`(
                FollowPathCommand("Mars",
                listOf(MoveForwardCommand, MoveForwardCommand, RotateRightCommand, MoveForwardCommand)
            )
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                RoverMovedEvent("Mars", Position(1,3), F),
                RoverMovedEvent("Mars", Position(1,4), F),
                RoverTurnedEvent(E, R),
                RoverMovedEvent("Mars", Position(2,4), F),
            )
    }

    @Test
    internal fun `the rover cannot overcome obstacles in front of it`() {
        fixture.given(RoverLandedEvent("Mars", Position(1,2), N))
            .`when`(FollowPathCommand("Mars", listOf(MoveForwardCommand), PlanetMap(listOf(Position(1,3)))))
            .expectSuccessfulHandlerExecution()
            .expectEvents(ObstacleFoundEvent(Position(1,3)))

    }

    @Test
    internal fun `the rover cannot overcome obstacles in behind it`() {
        fixture.given(RoverLandedEvent("Mars", Position(1,2), N))
            .`when`(FollowPathCommand("Mars", listOf(MoveBackwardCommand), PlanetMap(listOf(Position(1,1)))))
            .expectSuccessfulHandlerExecution()
            .expectEvents(ObstacleFoundEvent(Position(1,1)))

    }

    @Test
    internal fun `when an obstacle is found the rover stops`() {
        val planet = PlanetMap(listOf(Position(1, 4)))
        fixture.given(RoverLandedEvent("Mars", Position(1,2), N))
            .`when`(
                FollowPathCommand("Mars",
                listOf(MoveForwardCommand, MoveForwardCommand, RotateRightCommand, MoveForwardCommand)
            , planet
            )
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                RoverMovedEvent("Mars", Position(1,3), F),
                ObstacleFoundEvent(Position(1,4))
            )
    }
}



