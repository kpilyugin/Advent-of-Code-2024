fun main() {
    fun solve(bytes: Set<Vec2>, size: Int): Int {
        val grid = Grid(size + 1)
        for (b in bytes) {
            grid[b] = '#'
        }

        val cost = HashMap<Vec2, Int>()
        val q = ArrayDeque<Vec2>()
        val start = Vec2(0, 0)
        cost[start] = 0
        q.add(start)
        while (q.isNotEmpty()) {
            val cur = q.removeFirst()
            grid[cur] = 'O'
            val curCost = cost[cur]!!
            if (cur == Vec2(size, size)) {
                return curCost
            }
            grid.adjacent(cur).forEach {
                if (it !in bytes && it !in cost.keys) {
                    q.addLast(it)
                    cost[it] = curCost + 1
                }
            }
        }
        return -1
    }

    fun part1(input: List<String>, size: Int, numBytes: Int): Int {
        val bytes = input
            .map { line -> line.split(",").map { it.toInt() } }
            .map { Vec2(it[0], it[1]) }
            .take(numBytes)
            .toSet()
        return solve(bytes, size)
    }

    fun part2(input: List<String>, size: Int): String {
        val full = input
            .map { line -> line.split(",").map { it.toInt() } }
            .map { Vec2(it[0], it[1]) }

        val subset = HashSet<Vec2>()
        for (pos in full) {
            subset.add(pos)
            if (solve(subset, size) == -1) {
                return "${pos.x},${pos.y}"
            }
        }
        return "-"
    }

    val input = readInput("Day18")
    val testInput = readInput("Day18_test")

    check(part1(testInput, 6, 12), 22)
    println(part1(input, 70, 1024))

    check(part2(testInput, 6), "6,1")
    println(part2(input, 70))
}