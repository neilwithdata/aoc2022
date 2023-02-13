sealed class FileSystemEntry(
    val name: String,
    var parentDirectory: Directory? = null
) {
    abstract val size: Long
}

class File(
    name: String,
    override val size: Long,
    parentDirectory: Directory?
) : FileSystemEntry(name, parentDirectory)

open class Directory(
    name: String,
    parentDirectory: Directory?
) : FileSystemEntry(name, parentDirectory) {
    override val size: Long
        get() = children.sumOf { it.size }

    private val _children = mutableListOf<FileSystemEntry>()
    val children: List<FileSystemEntry> = _children

    private fun hasChild(name: String): Boolean {
        return children.firstOrNull { it.name == name } != null
    }

    fun addChild(entry: FileSystemEntry) {
        // a child file system entry with this name already exists - do not add
        if (hasChild(entry.name))
            return

        entry.parentDirectory = this
        _children.add(entry)
    }
}

object Root : Directory("/", null)

sealed class Command {
    data class ChangeDirectory(val target: String) : Command()
    object List : Command()
}

fun main() {
    val input = java.io.File("data/day07_input.txt")
        .readLines()

    val root = buildDirectoryTree(input)
    val dirs = getAllSubdirectories(root)

    // Part 1
    println(dirs
        .filter { it.size <= 100000 }
        .sumOf { it.size })

    // Part 2
    // find the smallest directory >= `required`
    val required = 30_000_000 - (70_000_000 - root.size)
    println(dirs
        .sortedBy { it.size }
        .first { it.size >= required }
        .size)

}

fun getAllSubdirectories(directory: Directory): List<Directory> {
    val dirs = mutableListOf<Directory>()
    val stack = ArrayDeque<FileSystemEntry>()
    stack.add(directory)

    while (stack.isNotEmpty()) {
        val curr = stack.removeFirst()
        if (curr is Directory) {
            stack.addAll(curr.children)
            dirs.add(curr)
        }
    }

    return dirs
}

fun buildDirectoryTree(input: List<String>): Directory {
    val iterator = input.iterator()

    var line: String? = iterator.next()

    val root: Directory = Root
    var pwd: Directory = root // We always start in the root directory

    while (line != null) {
        val tokens = line.split(" ")

        val cmd = when {
            tokens[1] == "cd" -> Command.ChangeDirectory(tokens[2])
            tokens[1] == "ls" -> Command.List
            else -> throw IllegalStateException("Unknown command")
        }

        if (cmd is Command.ChangeDirectory) {
            pwd = when (cmd.target) {
                "/" -> Root
                ".." -> requireNotNull(pwd.parentDirectory) { "Cannot navigate up from /" }
                else -> {
                    // does pwd already have a child directory with this name? If not, let's create it
                    val childDir = pwd.children.firstOrNull { it.name == cmd.target } as? Directory
                    childDir ?: Directory(cmd.target, pwd).also {
                        pwd.addChild(it)
                    }
                }
            }

            line = iterator.next()
        } else {
            while (true) {
                if (iterator.hasNext()) {
                    line = iterator.next()

                    if (line.startsWith("$")) {
                        break
                    } else {
                        val outputTokens = line.split(" ")

                        if (outputTokens[0] == "dir") {
                            // Create a new directory within the current directory
                            pwd.addChild(Directory(outputTokens[1], pwd))
                        } else {
                            // Create a new file within the current directory
                            pwd.addChild(File(outputTokens[1], outputTokens[0].toLong(), pwd))
                        }
                    }
                } else {
                    line = null
                    break
                }
            }
        }
    }

    return root
}