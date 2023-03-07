import java.io.File

sealed class Instruction {
    data class AddX(val value: Int) : Instruction()
    object Noop : Instruction()
}

fun main() {
    val instructions = File("data/day10_input.txt")
        .readLines()
        .map { line ->
            if (line.startsWith("noop")) {
                Instruction.Noop
            } else {
                val parts = line.split(" ")
                Instruction.AddX(parts[1].toInt())
            }
        }

    var sum = 0

    runInstructions(instructions) { cycle: Int, xRegister: Int ->
        if ((cycle - 20) % 40 == 0) {
            sum += cycle * xRegister
        }
    }

    println(sum)
}

fun runInstructions(instructions: List<Instruction>, callback: (cycle: Int, xRegister: Int) -> Unit) {
    var cycle = 1
    var xRegister = 1

    for (instruction in instructions) {
        when (instruction) {
            is Instruction.Noop -> {
                callback(cycle, xRegister)
                cycle++
            }
            is Instruction.AddX -> {
                repeat(2) {
                    callback(cycle, xRegister)
                    cycle++
                }
                xRegister += instruction.value
            }
        }
    }
}


