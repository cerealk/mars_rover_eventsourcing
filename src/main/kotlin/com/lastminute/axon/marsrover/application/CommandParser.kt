package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.domain.command.*

class CommandParser() {

    fun parseCommands(commandSequence: String) =
        commandSequence.toCharArray().map { c -> parseSingleCommand(c)}

    private fun parseSingleCommand(command: Char) : RoverCommand = when(command) {
        'F' -> MoveForward
        'B' -> MoveBackward
        'R' -> RotateRight
        'L' -> RotateLeft
        else -> throw IllegalArgumentException("CommandNotSupported")
    }

}