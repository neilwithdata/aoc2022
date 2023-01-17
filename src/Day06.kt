import java.io.File

fun main() {
    val input = File("data/day06_input.txt")
        .readLines()
        .first()

    println(findMarker(input, 4)) // Part 1
    println(findMarker(input, 14)) // Part 2
}

fun findMarker(input: String, length: Int): Int {
    for (i in length until input.lastIndex) {
        val buffer = input.substring(i - length, i)
        if (buffer.groupingBy { it }.eachCount().all { it.value == 1 }) {
            return i
        }
    }

    throw IllegalStateException("Marker not found")
}