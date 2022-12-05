import java.io.File

enum class Shape(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    val beats: Shape
        get() = when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }
}

enum class Outcome(val score: Int) {
    WIN(6),
    DRAW(3),
    LOSS(0)
}

class Round(input: String, firstStrategy: Boolean = true) {

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

    private val outcomeMapping = mutableMapOf(
        "X" to Outcome.LOSS,
        "Y" to Outcome.DRAW,
        "Z" to Outcome.WIN
    )

    private val opponentShape: Shape
    private val myShape: Shape
    private val outcome: Outcome

    val score
        get() = outcome.score + myShape.score

    init {
        val (firstColumn, secondColumn) = input.split(" ")
        opponentShape = opponentMapping[firstColumn]!!

        if (firstStrategy) {
            myShape = myMapping[secondColumn]!!

            outcome = when {
                myShape.beats == opponentShape -> Outcome.WIN
                myShape == opponentShape -> Outcome.DRAW
                else -> Outcome.LOSS
            }
        } else {
            outcome = outcomeMapping[secondColumn]!!

            myShape = when (outcome) {
                Outcome.WIN -> Shape.values().first { it.beats == opponentShape }
                Outcome.DRAW -> opponentShape
                Outcome.LOSS -> opponentShape.beats
            }
        }
    }
}

fun main() {
    File("data/day02_input.txt")
        .readLines()
        .also { rounds ->
            println(rounds.sumOf { Round(it, firstStrategy = true).score }) // Part 1
            println(rounds.sumOf { Round(it, firstStrategy = false).score }) // Part 2
        }
}