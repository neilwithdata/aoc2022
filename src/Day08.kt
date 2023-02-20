import java.io.File

enum class VISIBILITY {
    BLOCKED,
    TO_EDGE
}

data class Direction(val dx: Int = 0, val dy: Int = 0)

private val allDirections = listOf(
    Direction(dx = -1),
    Direction(dx = +1),
    Direction(dy = -1),
    Direction(dy = +1)
)


class Grid(input: List<String>) {
    val cols = input[0].length
    val rows = input.size

    private val grid = Array(rows) { IntArray(cols) }

    init {
        for ((iy, row) in input.withIndex()) {
            for ((ix, c) in row.withIndex()) {
                grid[iy][ix] = c.digitToInt()
            }
        }
    }

    // returns the viewing distance from the position in the grid in the given direction
    private fun findViewingDistance(row: Int, col: Int, direction: Direction): Pair<VISIBILITY, Int> {
        val height = grid[row][col]

        var currRow = row + direction.dy
        var currCol = col + direction.dx
        var dist = 0

        while (currRow in 0 until rows && currCol in 0 until cols) {
            dist++

            if (grid[currRow][currCol] >= height) {
                return Pair(VISIBILITY.BLOCKED, dist)
            }

            currRow += direction.dy
            currCol += direction.dx
        }

        return Pair(VISIBILITY.TO_EDGE, dist)
    }

    fun isVisible(row: Int, col: Int): Boolean {
        return allDirections.any { direction ->
            val dist = findViewingDistance(row, col, direction)
            dist.first == VISIBILITY.TO_EDGE
        }
    }

    fun scenicScore(row: Int, col: Int): Int {
        return allDirections.map { direction ->
            findViewingDistance(row, col, direction).second
        }.reduce { acc, d -> acc * d }
    }
}

fun main() {
    val input = File("data/day08_input.txt")
        .readLines()

    val grid = Grid(input)

    var visibleCount = 0
    var highestScenicScore = 0

    for (row in 0 until grid.rows) {
        for (col in 0 until grid.cols) {
            // Part 1
            if (grid.isVisible(row, col)) {
                visibleCount++
            }

            // Part 2
            val scenicScore = grid.scenicScore(row, col)
            if (scenicScore > highestScenicScore) {
                highestScenicScore = scenicScore
            }
        }
    }

    println(visibleCount)
    println(highestScenicScore)
}