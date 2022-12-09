import java.io.File

fun main() {
    println(File("data/day03_input.txt")
        .readLines()
        .sumOf { line ->
            val firstHalf = line.substring(0, line.length / 2)
            val secondHalf = line.substring(line.length / 2)

            val common = firstHalf.toSet().intersect(secondHalf.toSet()).first()

            if (common.isLowerCase()) {
                1 + common.code - 'a'.code
            } else {
                27 + common.code - 'A'.code
            }
        })
}