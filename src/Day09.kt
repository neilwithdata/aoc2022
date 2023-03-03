import java.io.File
import kotlin.math.abs
import kotlin.math.sign

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT
}

data class Position(var x: Int, var y: Int) {
    fun move(direction: Direction) {
        when (direction) {
            Direction.UP -> y++
            Direction.RIGHT -> x++
            Direction.DOWN -> y--
            Direction.LEFT -> x--
        }
    }

    fun isTouching(position: Position): Boolean {
        return abs(position.x - this.x) <= 1 && abs(position.y - this.y) <= 1
    }

    fun snapTo(position: Position) {
        val xDiff = this.x - position.x
        val yDiff = this.y - position.y

        this.x = position.x + if (abs(xDiff) >= 2) xDiff.sign else 0
        this.y = position.y + if (abs(yDiff) >= 2) yDiff.sign else 0
    }
}


data class Command(val direction: Direction, val count: Int) {
    companion object {
        fun fromString(input: String): Command {
            val (dirString, countString) = input.split(" ")

            val direction = when (dirString) {
                "U" -> Direction.UP
                "R" -> Direction.RIGHT
                "D" -> Direction.DOWN
                "L" -> Direction.LEFT
                else -> throw IllegalArgumentException("Invalid direction")
            }

            return Command(direction, countString.toInt())
        }
    }
}

fun main() {
    val commands = File("data/day09_input.txt")
        .readLines()
        .map { Command.fromString(it) }

    // Part 1
    val shortRope = buildList {
        repeat(2) {
            add(Position(0, 0))
        }
    }

    println("Part 1: ${runCommands(commands, shortRope)}")

    // Part 2
    val longRope = buildList {
        repeat(10) {
            add(Position(0, 0))
        }
    }

    println("Part 2: ${runCommands(commands, longRope)}")
}

fun runCommands(commands: List<Command>, rope: List<Position>): Int {
    val tailVisited = mutableSetOf(rope.last().copy())

    fun step(direction: Direction) {
        // Move the head one step in direction
        rope.first().move(direction)

        // Update each link in the rope to follow the parent
        for (i in 1 until rope.size) {
            val link = rope[i]
            val parent = rope[i - 1]

            if (!link.isTouching(parent)) {
                link.snapTo(parent)
            }
        }

        tailVisited.add(rope.last().copy())
    }

    commands.forEach { command ->
        repeat(command.count) {
            step(command.direction)
        }
    }

    return tailVisited.size
}