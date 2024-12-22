fun main() {
    fun solve(input: List<String>, numRobots: Int): Long {
        val digits = listOf(
            "789",
            "456",
            "123",
            "-0A"
        )
        val dirs = listOf(
            "-^A",
            "<v>"
        )

        fun pathsFrom(keyboard: List<String>, cur: Char, target: Char): List<String> {
            if (cur == target) return listOf("A")
            val grid = Grid(keyboard)
            val start = grid.find(cur)
            val end = grid.find(target)
            var paths = setOf("" to start)
            while (true) {
                paths = paths.flatMap { (curPath, curPos) ->
                    val next = listOf(
                        curPath + '>' to Vec2(curPos.x + 1, curPos.y),
                        curPath + '<' to Vec2(curPos.x - 1, curPos.y),
                        curPath + 'v' to Vec2(curPos.x, curPos.y + 1),
                        curPath + '^' to Vec2(curPos.x, curPos.y - 1),
                    )
                    next.filter {
                        val value = grid.safeGet(it.second)
                        value != null && value != '-'
                    }
                }.toSet()
                val ends = paths.filter { it.second == end }
                if (ends.isNotEmpty()) {
                    val result = ends.map { it.first }.map { it + "A" }
                    return result
                }
            }
        }

        data class State(val step: Int, val command: String)
        val memo = HashMap<State, Long>()

        fun recursive(step: Int, command: String): Long {
            if (step == numRobots + 1) {
                return command.length.toLong()
            }
            val state = State(step, command)
            memo[state]?.let { return it }

            var cur = 'A'
            var result = 0L
            val keyboard = if (step == 0) digits else dirs
            for (ch in command) {
                val next = pathsFrom(keyboard, cur, ch)
                result += next.minOf { recursive(step + 1, it) }
                cur = ch
            }
            memo[state] = result
            return result
        }

        return input.sumOf { line ->
            val result = recursive(0, line)
            val goal = line.dropLast(1).toInt()
            goal * result
        }
    }

    fun part1(input: List<String>) = solve(input, 2)

    fun part2(input: List<String>) = solve(input, 25)

    val input = readInput("Day21")
    val testInput = readInput("Day21_test")

    check(part1(testInput), 126384L)
    println(part1(input))

    println(part2(input))
}
