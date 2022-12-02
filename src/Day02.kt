import java.io.File

enum class Shape(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3)
}

data class Round(val opponentShape: Shape, val myShape: Shape) {
    fun calculateScore(): Int {
        val winLossScore = if (
            (myShape == Shape.ROCK && opponentShape == Shape.SCISSORS) ||
            (myShape == Shape.SCISSORS && opponentShape == Shape.PAPER) ||
            (myShape == Shape.PAPER && opponentShape == Shape.ROCK)
        ) {
            6 // Win
        } else if (myShape == opponentShape) {
            3 // Draw
        } else {
            0
        }

        return winLossScore + myShape.score
    }

    companion object {
        private val opponentMapping = mutableMapOf(
            "A" to Shape.ROCK,
            "B" to Shape.PAPER,
            "C" to Shape.SCISSORS
        )

        private val myMapping = mutableMapOf(
            "X" to Shape.ROCK,
            "Y" to Shape.PAPER,
            "Z" to Shape.SCISSORS
        )

        fun fromInput(str: String): Round {
            val (opponentChoice, myChoice) = str.split(" ")

            return Round(opponentMapping[opponentChoice]!!, myMapping[myChoice]!!)
        }
    }
}

fun main() {
    val scores = File("data/day02_input.txt")
        .readLines()
        .map { Round.fromInput(it).calculateScore() }

    println(scores.sum())
}