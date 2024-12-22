fun main() {

    fun solve(input: List<String>, maxCheat: Int, cheatAtLeast: Int): Int {
        val grid = Grid(input)
        val start = grid.find('S')
        val end = grid.find('E')

        val distToEnd = HashMap<Vec2, Int>()
        val q = ArrayDeque<Vec2>()

        q.addLast(end)
        distToEnd[end] = 0
        while (q.isNotEmpty()) {
            val cur = q.removeLast()
            grid.adjacent(cur).forEach { next ->
                if (next !in distToEnd && grid[next] != '#') {
                    distToEnd[next] = distToEnd[cur]!! + 1
                    q.addLast(next)
                }
            }
        }

        val total = distToEnd[start]!!
        val cheats = ArrayList<Int>()

        val path = distToEnd.keys
        for (cur in path) {
            val toCur = total - distToEnd[cur]!!
            val options = path.filter { cur.manhattan(it) <= maxCheat }
            for (next in options) {
                val fromNext = distToEnd[next]!!
                val dist = cur.manhattan(next)
                val win = total - (toCur + dist + fromNext)
                if (win > 0) cheats.add(win)
            }
        }

        return cheats.count { it >= cheatAtLeast }
    }

    fun part1(input: List<String>, cheatAtLeast: Int = 100): Int {
        return solve(input, 2, cheatAtLeast)
    }

    fun part2(input: List<String>, cheatAtLeast: Int = 100): Int {
        return solve(input, 20, cheatAtLeast)
    }

    val input = readInput("Day20")
    val testInput = readInput("Day20_test")

    check(part1(testInput, 64) == 1)
    println(part1(input))

    check(part2(testInput, 76) == 3)
    println(part2(input))
}
