fun main() {
    fun part1(input: String): Int {
        val (pattern, commands) = input.split("\n\n")
        val grid = Grid(pattern.lines())

        var cur = grid.find('@')
        for (cmd in commands.replace("\n", "")) {
            val dir = cmd.parseDir()
            val firstNext = grid.next(cur, dir)
            var freeNext = firstNext
            while (grid[freeNext] == 'O') freeNext = grid.next(freeNext, dir)
            if (grid[freeNext] == '.') {
                if (freeNext != firstNext) {
                    grid.swap(firstNext, freeNext)
                }
                grid.swap(cur, firstNext)
                cur = firstNext
            }
        }
        var res = 0
        grid.forEachIndexed { x, y, value ->
            if (value == 'O') res += 100 * y + x
        }
        return res
    }

    fun part2(input: String): Int {
        val (pattern, commands) = input.split("\n\n")
        val extended = pattern.lines().map { line ->
            line.map {
                when (it) {
                    '#' -> "##"
                    'O' -> "[]"
                    '.' -> ".."
                    '@' -> "@."
                    else -> throw IllegalArgumentException()
                }
            }.joinToString("")
        }
        val grid = Grid(extended)

        var cur = grid.find('@')
        for (cmd in commands.replace("\n", "")) {
            val dir = cmd.parseDir()
            val firstNext = grid.next(cur, dir)
            if (grid[firstNext] == '.') {
                grid.swap(cur, firstNext)
                cur = firstNext
            } else if (grid[firstNext] in "[]") {
                val visited = HashSet<Vec2>()
                fun dfs(v: Vec2) {
                    if (v in visited) return
                    visited += v
                    if (grid[v] == '[') dfs(Vec2(v.x + 1, v.y))
                    if (grid[v] == ']') dfs(Vec2(v.x - 1, v.y))
                    grid.next(v, dir).run {
                        if (grid[this] in "[]") dfs(this)
                    }
                }
                dfs(firstNext)

                val blocked = visited.any { grid[grid.next(it, dir)] == '#' }
                if (!blocked) {
                    val backup = visited.associateBy({ it }, { grid[it] })
                    visited.forEach { grid[it] = '.' }
                    visited.forEach { grid[grid.next(it, dir)] = backup[it]!! }
                    grid.swap(cur, firstNext)
                    cur = firstNext
                }
            }
        }
        var res = 0
        grid.forEachIndexed { x, y, value ->
            if (value == '[') res += 100 * y + x
        }
        return res
    }

    val input = readInputText("Day15")
    val testInput0 = readInputText("Day15_test0")
    val testInput = readInputText("Day15_test")

    check(part1(testInput0), 2028)
    check(part1(testInput), 10092)
    println(part1(input))

    check(part2(testInput), 9021)
    println(part2(input))
}
