import java.io.File

fun main() {
    val regex = Regex("""(\d+)-(\d+),(\d+)-(\d+)""")

    val res = File("data/day04_input.txt")
        .readLines()
        .count { line ->
            val match = requireNotNull(regex.find(line))
            val (min1, max1, min2, max2) = match.groupValues.drop(1).map { it.toInt() }

            return@count ((min1 <= min2) && (max1 >= max2)) ||
                    ((min2 <= min1) && (max2 >= max1))
        }

    println(res)
}