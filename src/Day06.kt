import java.io.File

fun main() {
    val input = File("data/day06_input.txt")
        .readLines()
        .first()

    for (i in 4 until input.lastIndex) {
        val buffer = input.substring(i - 4, i)
        if (buffer.groupingBy { it }.eachCount().all { it.value == 1 }) {
            println(i)
            break
        }
    }
}