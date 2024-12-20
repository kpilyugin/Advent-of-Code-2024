fun main() {
    fun part1(input: List<String>): Int {
        var score = 0

        val visited = HashSet<Pair<Int, Int>>()
        fun dfs(i: Int, j: Int) {
            val cur = input[i][j].digitToInt()
            if (cur == 9) {
                score++
                return
            }

            for ((di, dj) in listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)) {
                val ni = i + di
                val nj = j + dj
                if (ni in input.indices && nj in input.indices &&
                    input[ni][nj].digitToInt() == cur + 1 && (ni to nj !in visited)) {
                    visited.add(ni to nj)
                    dfs(ni, nj)
                }
            }
        }

        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == '0') {
                    visited.clear()
                    visited.add(i to j)
                    dfs(i, j)
                }
            }
        }
        return score
    }

    fun part2(input: List<String>): Int {
        class State(val pos: Pair<Int, Int>, val count: Int)

        fun bfs(i: Int, j: Int): Int {
            var current = listOf(State(i to j, 1))

            for (step in 1..9) {
                val next = current.flatMap { cur ->
                    listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0).mapNotNull { (di, dj) ->
                        val ni = cur.pos.first + di
                        val nj = cur.pos.second + dj
                        if (ni in input.indices && nj in input.indices && input[ni][nj].digitToInt() == step) {
                            State(ni to nj, cur.count)
                        } else null
                    }
                }

                current = next.groupBy { it.pos }
                    .map { entry -> State(entry.key, entry.value.sumOf { it.count })}
            }
            return current.sumOf { it.count }
        }
        var score = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == '0') {
                    score += bfs(i, j)
                }
            }
        }
        return score
    }

    val input = readInput("Day10")
    val testInput = readInput("Day10_test")

    check(part1(testInput), 36)
    println(part1(input))

    check(part2(testInput), 81)
    println(part2(input))
}
