import java.io.File

fun main() {
    val fullyContained = pairCount { min1, max1, min2, max2 ->
        ((min1 <= min2) && (max1 >= max2)) || ((min2 <= min1) && (max2 >= max1))
    }

    // Part 1
    println(fullyContained)

    val overlapping = pairCount { min1, max1, min2, max2 ->
        (min1..max1 intersect min2..max2).isNotEmpty()
    }

    // Part 2
    println(overlapping)
}

fun pairCount(predicate: (min1: Int, max1: Int, min2: Int, max2: Int) -> Boolean): Int {
    val regex = Regex("""(\d+)-(\d+),(\d+)-(\d+)""")

    return File("data/day04_input.txt")
        .readLines()
        .count { line ->
            val match = requireNotNull(regex.find(line))
            val (min1, max1, min2, max2) = match.groupValues.drop(1).map { it.toInt() }

            predicate(min1, max1, min2, max2)
        }
}