import java.io.File

class Monkey(
    startItems: List<Int>,
    val operation: (old: Int) -> Int,
    val test: (level: Int) -> Int
) {
    val items = startItems.toMutableList()
    var inspectionCount = 0
}


fun main() {
    val monkeys = File("data/day11_input.txt")
        .readLines()
        .chunked(7) { chunk ->
            parseMonkey(chunk.toList())
        }

    repeat(20) {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                monkey.inspectionCount++

                // 1. Perform operation
                var newWorryLevel = monkey.operation(item)

                // 2. Divide by 3 (rounding down)
                newWorryLevel /= 3

                // 3. Pass to some other monkey
                val passTo = monkey.test(newWorryLevel)
                monkeys[passTo].items.add(newWorryLevel)
            }

            // All items get passed on to
            monkey.items.clear()
        }
    }

    for (monkey in monkeys) {
        println(monkey.items.joinToString(", "))
        println("inspection count: ${monkey.inspectionCount}")
    }

    val result = monkeys
        .map { it.inspectionCount }
        .sortedDescending()
        .take(2)
        .reduce { acc, i -> acc * i }

    println(result)
}

fun parseMonkey(input: List<String>): Monkey {
    val inputItems = input[1].substringAfter(":").split(',')
        .map {
            it.trim().toInt()
        }

    val operation: (Int) -> Int = { old ->
        val (leftOperand, operator, rightOperand) = input[2]
            .substringAfter('=').trim().split(' ')

        val substitute = { operand: String -> if (operand == "old") old else operand.toInt() }

        when (operator) {
            "+" -> substitute(leftOperand) + substitute(rightOperand)
            "*" -> substitute(leftOperand) * substitute(rightOperand)
            else -> throw IllegalStateException("Unsupported operation expression")
        }
    }

    val test: (Int) -> Int = { level ->
        val divisibleBy = input[3].substringAfter("divisible by ").toInt()
        val selectedLine = if (level % divisibleBy == 0) input[4] else input[5]
        selectedLine.split(' ').last().toInt()
    }

    return Monkey(inputItems, operation, test)
}