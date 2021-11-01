package com.lastminute.axon.marsrover.application

import com.lastminute.axon.marsrover.domain.coreapi.*

class CommandParser() {

    fun parseCommands(commandSequence: String) =
        commandSequence.toCharArray().map { c -> parseSingleCommand(c)}

    private fun parseSingleCommand(command: Char) : Command = when(command) {
        'F' -> MoveForwardCommand
        'B' -> MoveBackwardCommand
        'R' -> RotateRightCommand
        'L' -> RotateLeftCommand
        else -> throw IllegalArgumentException("CommandNotSupported")
    }

}