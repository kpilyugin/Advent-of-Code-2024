fun main() {
    fun findStart(input: List<CharArray>): Pair<Int, Int> {
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == '^') return j to i
            }
        }
        throw IllegalStateException()
    }

    fun part1(input: List<String>): Int {
        var cur = findStart(input.map { it.toCharArray() })
        var dir = 0 to -1

        val visited = HashSet<Pair<Int, Int>>()
        visited.add(cur)
        while (true) {
            val next = cur.first + dir.first to cur.second + dir.second
            if (next.first !in input.indices || next.second !in input[0].indices) {
                return visited.size
            }
            if (input[next.second][next.first] == '#') {
                dir = -dir.second to dir.first
            } else {
                cur = next
                visited.add(next)
            }
        }
    }

    fun part2(input: List<String>): Int {
        val copy = input.map { it.toCharArray() }

        data class State(val x: Int, val y: Int, val dx: Int, val dy: Int)

        fun isLooping(): Boolean {
            var cur = findStart(copy)
            var dir = 0 to -1

            val visited = HashSet<State>()
            visited.add(State(cur.first, cur.second, dir.first, dir.second))
            while (true) {
                val next = cur.first + dir.first to cur.second + dir.second
                if (next.first !in input.indices || next.second !in input[0].indices) {
                    return false
                }
                if (copy[next.second][next.first] == '#') {
                    dir = -dir.second to dir.first
                } else {
                    cur = next
                    val state = State(cur.first, cur.second, dir.first, dir.second)
                    if (state in visited) {
                        return true
                    } else {
                        visited.add(state)
                    }
                }
            }
        }

        var count = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == '.') {
                    copy[i][j] = '#'
                    if (isLooping()) {
                        count++
                    }
                    copy[i][j] = '.'
                }
            }
        }
        return count
    }

    val input = readInput("Day06")
    val testInput = readInput("Day06_test")

    check(part1(testInput), 41)
    println(part1(input))

    check(part2(testInput), 6)
    println(part2(input))
}
