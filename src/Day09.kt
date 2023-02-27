import java.io.File
import kotlin.math.abs

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
        if (this.y == position.y) { // Same row
            this.x += if (position.x > this.x) 1 else -1
        } else if (this.x == position.x) { // Same column
            this.y += if (position.y > this.y) 1 else -1
        } else { // Diagonal
            if (abs(this.y - position.y) >= 2) {
                this.x = position.x
                this.y += if (position.y > this.y) 1 else -1
            } else {
                this.y = position.y
                this.x += if (position.x > this.x) 1 else -1
            }
        }
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
    val head = Position(0, 0)
    val tail = Position(0, 0)

    val tailVisited = mutableSetOf(tail.copy())

    fun step(direction: Direction) {
        // Move the head one step in direction
        head.move(direction)

        if (!head.isTouching(tail)) {
            // Move the tail to keep up with the head
            tail.snapTo(head)
            tailVisited.add(tail.copy())
        }
    }

    File("data/day09_input.txt")
        .readLines()
        .map { Command.fromString(it) }
        .forEach { command ->
            repeat(command.count) {
                step(command.direction)
            }
        }

    println(tailVisited.size)
}