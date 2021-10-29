package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.Orientation.E
import com.lastminute.axon.marsrover.query.RoverTrailProjection
import com.lastminute.axon.marsrover.query.Trail
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RoverTrailTest {


    @Test
    internal fun `a rover just landed has only one position`() {
        val roverTrailProjection = RoverTrailProjection()

        roverTrailProjection.on(RoverLandedEvent("Mars", Position(1,2), E))

        roverTrailProjection.trailFor("Mars") shouldBe Trail("Mars", listOf(Position(1,2)))
    }
}

