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

    // Part 1
    println(totals.maxOf { it })

    // Part 2
    println(totals.sortedDescending().take(3).sum())
}