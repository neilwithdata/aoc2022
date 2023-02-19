import java.io.File

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

    fun isVisible(rowIndex: Int, colIndex: Int): Boolean {
        val height = grid[rowIndex][colIndex]

        // returns true if all trees in the given direction are lower than height
        fun checkDirection(dx: Int = 0, dy: Int = 0): Boolean {
            var currRow = rowIndex + dy
            var currCol = colIndex + dx

            while (currRow in 0 until rows && currCol in 0 until cols) {
                if (grid[currRow][currCol] >= height) {
                    return false
                }

                currRow += dy
                currCol += dx
            }

            return true
        }

        // edges are always visible
        if (rowIndex == 0 ||
            colIndex == 0 ||
            rowIndex == rows - 1 ||
            colIndex == cols - 1
        ) {
            return true
        }

        // if visible from any direction
        if (checkDirection(dx = -1) ||
            checkDirection(dx = +1) ||
            checkDirection(dy = -1) ||
            checkDirection(dy = +1)
        ) {
            return true
        }

        return false
    }
}

fun main() {
    val input = File("data/day08_input.txt")
        .readLines()

    val grid = Grid(input)

    var visibleCount = 0
    for (row in 0 until grid.rows) {
        for (col in 0 until grid.cols) {
            if (grid.isVisible(row, col)) {
                visibleCount++
            }
        }
    }

    println(visibleCount)
}