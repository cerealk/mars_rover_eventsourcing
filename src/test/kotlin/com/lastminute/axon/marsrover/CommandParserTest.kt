package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.application.CommandParser
import com.lastminute.axon.marsrover.domain.command.MoveBackwardCommand
import com.lastminute.axon.marsrover.domain.command.MoveForwardCommand
import com.lastminute.axon.marsrover.domain.command.RotateLeftCommand
import com.lastminute.axon.marsrover.domain.command.RotateRightCommand
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainInOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CommandParserTest {

    private val parser: CommandParser = CommandParser()

    companion object {
        val moveForward = MoveForwardCommand
        val moveBackward = MoveBackwardCommand
        val turnRight = RotateRightCommand
        val turnLeft = RotateLeftCommand
    }

    @Test
    internal fun `F stands for forward`() {
        parser.parseCommands("F") shouldContainExactly listOf(moveForward)
    }

    @Test
    internal fun `B stands for backwards`() {
        parser.parseCommands("B") shouldContainExactly listOf(moveBackward)
    }

    @Test
    internal fun `R stands for Turn Right`() {
        parser.parseCommands("R") shouldContainExactly listOf(turnRight)
    }

    @Test
    internal fun `L stands for rotate left`() {
        parser.parseCommands("L") shouldContainExactly listOf(turnLeft)
    }

    @Test
    internal fun `L, R, B and F are the only admissibleCommands`() {
        val NOT_ONE_OF_THE_ALLOWED_COMMANDS = "G"
        assertThrows<IllegalArgumentException> {
            parser.parseCommands(NOT_ONE_OF_THE_ALLOWED_COMMANDS)
        }
    }

    @Test
    internal fun `can parse a sequence of commands`() {
        parser.parseCommands("FFRLB") shouldContainInOrder listOf(
            moveForward,
            moveForward,
            turnRight,
            turnLeft,
            moveBackward
        )

    }
}

