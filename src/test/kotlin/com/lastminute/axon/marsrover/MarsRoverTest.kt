package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.domain.command.Direction.B
import com.lastminute.axon.marsrover.domain.command.Direction.F
import com.lastminute.axon.marsrover.domain.command.*
import com.lastminute.axon.marsrover.domain.command.Orientation.*
import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Coordinates
import com.lastminute.axon.marsrover.domain.command.Rotation.*
import com.lastminute.axon.marsrover.domain.command.Rover
import com.lastminute.axon.marsrover.domain.coreapi.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.Test

class MarsRoverTest {

    private val  fixture: FixtureConfiguration<Rover> = AggregateTestFixture(Rover::class.java)

    private val roverName = "MarsRover1"

    @Test
    fun theRoverCanLand(){

        val landingSpot = Coordinates(1,2)
        val landingOrientation = N
        val dropRoverCommand = DropRoverCommand(roverName, landingSpot, landingOrientation)

        fixture
            .givenNoPriorActivity()
            .`when`(dropRoverCommand).expectSuccessfulHandlerExecution().expectEvents(
            RoverLandedEvent(
                roverName,
            landingSpot,
            landingOrientation
        )
        )
    }

    @Test
    internal fun `a rover explode if it lands on an obstacle`() {

        val landingSpot = Coordinates(1,2)
        val landingOrientation = Orientation.N
        val planetMap = PlanetMap(listOf(landingSpot))

        fixture.givenNoPriorActivity()
            .`when`(DropRoverCommand(roverName, landingSpot, landingOrientation, planetMap))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverExplodedEvent(roverName, landingSpot))
    }

    @Test
    internal fun `a crashed rover cannot move`() {
        fixture.given(RoverExplodedEvent(roverName, Coordinates(1,2)))
            .`when`(FollowPathCommand(roverName, listOf(MoveForwardCommand)))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
    }

    @Test
    fun theRoverCanMoveForward(){

        fixture.given(RoverLandedEvent(roverName, Coordinates(1, 2), N))
            .`when`(FollowPathCommand(roverName, listOf(MoveForwardCommand))).
                expectSuccessfulHandlerExecution().
                expectEvents(RoverMovedEvent(roverName, Coordinates(1, 3), F))
    }

    @Test
    fun theRoverCanMoveBackward(){

        val newPosition = Coordinates(1,1)
        val direction = B
        fixture.given(RoverLandedEvent(roverName, Coordinates(1, 2), N))
            .`when`(FollowPathCommand(roverName, listOf(MoveBackwardCommand))).
            expectSuccessfulHandlerExecution().
            expectEvents(RoverMovedEvent(roverName, newPosition, direction))
    }

    @Test
    fun `the rover can rotate left`() {

        fixture.given(RoverLandedEvent(roverName, Coordinates(1, 2), N))
            .`when`(FollowPathCommand(roverName, listOf(RotateLeftCommand)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverTurnedEvent(W, L))

    }

    @Test
    fun `the rover can rotate right`() {

        fixture.given(RoverLandedEvent(roverName, Coordinates(1, 2), N))
            .`when`(FollowPathCommand(roverName, listOf(RotateRightCommand)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverTurnedEvent(E, R))

    }

    @Test
    fun `after a rotation the rover move accordingly`(){
        fixture.given(RoverLandedEvent(roverName, Coordinates(1, 2), N))
            .andGiven(RoverTurnedEvent(W,L))
            .`when`(FollowPathCommand(roverName, listOf(MoveForwardCommand)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(RoverMovedEvent(roverName, Coordinates(0,2), F))
    }

    @Test
    internal fun `the rover can follow a path`() {
        fixture.given(RoverLandedEvent(roverName, Coordinates(1,2), N))
            .`when`(
                FollowPathCommand(
                    roverName,
                listOf(MoveForwardCommand, MoveForwardCommand, RotateRightCommand, MoveForwardCommand)
            )
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                RoverMovedEvent(roverName, Coordinates(1,3), F),
                RoverMovedEvent(roverName, Coordinates(1,4), F),
                RoverTurnedEvent(E, R),
                RoverMovedEvent(roverName, Coordinates(2,4), F),
            )
    }

    @Test
    internal fun `the rover cannot overcome obstacles in front of it`() {
        fixture.given(RoverLandedEvent(roverName, Coordinates(1,2), N))
            .`when`(FollowPathCommand(roverName, listOf(MoveForwardCommand), PlanetMap(listOf(Coordinates(1,3)))))
            .expectSuccessfulHandlerExecution()
            .expectEvents(ObstacleFoundEvent(Coordinates(1,3)))

    }

    @Test
    internal fun `the rover cannot overcome obstacles in behind it`() {
        fixture.given(RoverLandedEvent(roverName, Coordinates(1,2), N))
            .`when`(FollowPathCommand(roverName, listOf(MoveBackwardCommand), PlanetMap(listOf(Coordinates(1,1)))))
            .expectSuccessfulHandlerExecution()
            .expectEvents(ObstacleFoundEvent(Coordinates(1,1)))

    }

    @Test
    internal fun `when an obstacle is found the rover stops`() {
        val planet = PlanetMap(listOf(Coordinates(1, 4)))
        fixture.given(RoverLandedEvent(roverName, Coordinates(1,2), N))
            .`when`(
                FollowPathCommand(
                    roverName,
                listOf(MoveForwardCommand, MoveForwardCommand, RotateRightCommand, MoveForwardCommand)
            , planet
            )
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                RoverMovedEvent(roverName, Coordinates(1,3), F),
                ObstacleFoundEvent(Coordinates(1,4))
            )
    }
}




