import java.io.File

fun Char.priority(): Int =
    if (this.isLowerCase()) {
        1 + this.code - 'a'.code
    } else {
        27 + this.code - 'A'.code
    }


fun main() {
    val input = File("data/day03_input.txt")
        .readLines()

    // Part 1
    println(input.sumOf { line ->
        val firstHalf = line.substring(0, line.length / 2)
        val secondHalf = line.substring(line.length / 2)

        findCommonChars(firstHalf, secondHalf).first().priority()
    })

    // Part 2
    println(input.chunked(3) { group ->
        findCommonChars(*group.toTypedArray()).first().priority()
    }.sum())
}

fun findCommonChars(vararg strings: String): Set<Char> {
    var common = strings.first().toSet()

    for (string in strings.drop(1)) {
        common = common intersect string.toSet()
    }

    return common
}