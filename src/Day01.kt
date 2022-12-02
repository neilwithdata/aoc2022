import java.io.File

fun main() {
    val values = File("data/day01_input.txt")
        .readLines()
        .map { it.toIntOrNull() }

    val totals = mutableListOf<Int>()

    var sum = 0
    for (value in values) {
        if (value != null) {
            sum += value
        } else {
            totals.add(sum)
            sum = 0
        }
    }

    totals.add(sum)
    totals.sortDescending()

    // Part 1
    println(totals.first())

    // Part 2
    println(totals.take(3).sum())
}