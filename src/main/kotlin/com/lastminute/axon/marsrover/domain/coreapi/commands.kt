package com.lastminute.axon.marsrover.domain.coreapi

import com.lastminute.axon.marsrover.domain.command.Command
import com.lastminute.axon.marsrover.domain.command.Orientation
import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Coordinates
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ProbePlanetCommand(val planetName: String)

data class DropRoverCommand(
    @TargetAggregateIdentifier val rover: String,
    val position: Coordinates,
    val orientation: Orientation,
    val planetMap: PlanetMap = PlanetMap()
)

data class FollowPathCommand(
    @TargetAggregateIdentifier val rover: String,
    val commands: List<Command>,
    val planetMap: PlanetMap = PlanetMap(emptyList())
)