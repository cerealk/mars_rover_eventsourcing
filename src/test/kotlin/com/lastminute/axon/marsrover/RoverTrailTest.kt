package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.domain.command.Direction.F
import com.lastminute.axon.marsrover.domain.command.Orientation.E
import com.lastminute.axon.marsrover.domain.command.Coordinates
import com.lastminute.axon.marsrover.domain.coreapi.RoverLandedEvent
import com.lastminute.axon.marsrover.domain.coreapi.RoverMovedEvent
import com.lastminute.axon.marsrover.domain.coreapi.TrailQuery
import com.lastminute.axon.marsrover.domain.query.RoverTrailProjection
import com.lastminute.axon.marsrover.domain.query.Trail
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RoverTrailTest {


    @Test
    internal fun `a rover just landed has only one position`() {
        val roverTrailProjection = RoverTrailProjection()

        roverTrailProjection.on(RoverLandedEvent("MarsRover", "Mars", Coordinates(1,2), E))

        roverTrailProjection.trailFor(TrailQuery("MarsRover")) shouldBe Trail("MarsRover", listOf(Coordinates(1,2)))
    }


    @Test
    internal fun `the trail tracks when a rover moves`() {
        val roverTrailProjection = RoverTrailProjection()

        roverTrailProjection.on(RoverLandedEvent("MarsRover", "Mars", Coordinates(1,2), E))
        roverTrailProjection.on(RoverMovedEvent("MarsRover", Coordinates(2,2), F))


        roverTrailProjection.trailFor(TrailQuery("MarsRover")) shouldBe Trail("MarsRover", listOf(Coordinates(1,2), Coordinates(2,2)))
    }
}

