fun main() {
    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val visited = hashSetOf<Vec2>()

        fun region(start: Vec2): Int {
            var area = 0
            var perimeter = 0

            fun dfs(pos: Vec2) {
                area++
                grid.adjacentIncludeBorder(pos).forEach { next ->
                    val value = grid.safeGet(next)
                    if (value == null || value != grid[pos]) {
                        perimeter++
                    } else if (next !in visited) {
                        visited += next
                        dfs(next)
                    }
                }
            }
            visited += start
            dfs(start)
            return area * perimeter
        }

        return grid.positions().sumOf {
            if (it !in visited) region(it) else 0
        }
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val visited = hashSetOf<Vec2>()

        class SidePart(val x: Int, val y: Int, val dir: Dir)

        fun region(start: Vec2): Int {
            var area = 0

            val perimeter = mutableListOf<SidePart>()
            fun dfs(pos: Vec2) {
                area++
                for (dir in Dir.entries) {
                    val next = grid.next(pos, dir)
                    if (grid.safeGet(next) == grid[pos]) {
                        if (next !in visited) {
                            visited += next
                            dfs(next)
                        }
                    } else {
                        perimeter += SidePart(pos.x, pos.y, dir)
                    }
                }
            }
            visited += start
            dfs(start)

            var sides = 0
            perimeter.groupBy { it.dir }.forEach { (dir, parts) ->
                val vertical = dir == Dir.LEFT || dir == Dir.RIGHT
                val lines = parts.groupBy { if (vertical) it.x else it.y }
                lines.forEach { (_, line) ->
                    val values = line.map { if (vertical) it.y else it.x }.sorted()
                    sides += 1 + values.zipWithNext().count { (i, j) -> j > i + 1 }
                }
            }
            return area * sides
        }

        return grid.positions().sumOf {
            if (it !in visited) region(it) else 0
        }
    }

    val input = readInput("Day12")
    val testInput = readInput("Day12_test")

    check(part1(testInput), 1930)
    println(part1(input))

    check(part2(testInput), 1206)
    println(part2(input))
}
