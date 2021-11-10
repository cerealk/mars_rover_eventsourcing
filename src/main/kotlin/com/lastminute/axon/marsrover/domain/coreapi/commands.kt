package com.lastminute.axon.marsrover.domain.coreapi

import com.lastminute.axon.marsrover.domain.command.RoverCommand
import com.lastminute.axon.marsrover.domain.command.Orientation
import com.lastminute.axon.marsrover.domain.command.PlanetMap
import com.lastminute.axon.marsrover.domain.command.Coordinates
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ProbePlanetCommand(val planetName: String)

data class DropRoverCommand(
    @TargetAggregateIdentifier val rover: String,
    val position: Coordinates,
    val orientation: Orientation,
    val planetMap: PlanetMap
)

data class FollowPathCommand(
    @TargetAggregateIdentifier val rover: String,
    val commands: List<RoverCommand>,
    val planetMap: PlanetMap
)