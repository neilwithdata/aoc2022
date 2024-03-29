import java.io.File

data class Instruction(
    val source: Int,
    val destination: Int,
    val n: Int,
    val inOrder: Boolean = false
)

fun main() {
    val input = File("data/day05_input.txt")
        .readLines()

    val instructions = readInstructions(input, inOrder = true)
    val stacks = readInitialState(input)

    for (instruction in instructions) {
        if (instruction.inOrder) { // Part 2
            val buffer = mutableListOf<Char>()
            repeat(instruction.n) {
                val crate = stacks[instruction.source].removeLast()
                buffer.add(crate)
            }

            while (buffer.isNotEmpty()) {
                stacks[instruction.destination].add(buffer.removeLast())
            }
        } else {
            repeat(instruction.n) { // Part 1
                val crate = stacks[instruction.source].removeLast()
                stacks[instruction.destination].add(crate)
            }
        }
    }

    val result = stacks.joinToString(separator = "") {
        it.last().toString()
    }

    println(result)
}

fun readInstructions(input: List<String>, inOrder: Boolean): List<Instruction> {
    val instructionRegex = Regex("""move (\d+) from (\d) to (\d)""")

    return instructionRegex.findAll(input.joinToString())
        .map {
            val (n, source, dest) = it.destructured
            Instruction(source.toInt() - 1, dest.toInt() - 1, n.toInt(), inOrder)
        }.toList()
}

fun readInitialState(input: List<String>): List<ArrayDeque<Char>> {
    // Setup the stacks
    val numberLine = requireNotNull(input.find {
        Regex("""(\d\s{3}){2,}""").find(it) != null
    })

    val stackCount = numberLine.split(" ").last { it.isNotBlank() }.toInt()

    val stacks = buildList {
        repeat(stackCount) {
            add(ArrayDeque<Char>())
        }
    }

    // Populate the stacks with the initial crate configuration
    val cargoRegex = Regex("""[A-Z]""")
    for (line in input) {
        val matches = cargoRegex.findAll(line)
        for (match in matches) {
            val stack = (match.range.first - 1) / 4
            stacks[stack].addFirst(match.value[0])
        }
    }

    return stacks
}