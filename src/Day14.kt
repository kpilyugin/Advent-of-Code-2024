fun main() {
    class Robot(val px: Int, val py: Int, val vx: Int, val vy: Int)

    fun parse(input: List<String>): List<Robot> {
        return input.map { line ->
            val (p, v) = line.split(" ")
            val (px, py) = p.drop(2).split(",").map { it.toInt() }
            val (vx, vy) = v.drop(2).split(",").map { it.toInt() }
            Robot(px, py, vx, vy)
        }
    }

    fun normalize(value: Int, max: Int): Int {
        var res = value
        while (res < 0) res += (max + 1)
        while (res > max) res -= (max + 1)
        return res
    }

    fun part1(input: List<String>, maxX: Int, maxY: Int): Int {
        val steps = 100
        return parse(input).mapNotNull { robot ->
            val x = normalize(robot.px + robot.vx * steps, maxX)
            val y = normalize(robot.py + robot.vy * steps, maxY)
            when {
                x < maxX / 2 && y < maxY / 2 -> 0
                x < maxX / 2 && y > maxY / 2 -> 1
                x > maxX / 2 && y < maxY / 2 -> 2
                x > maxX / 2 && y > maxY / 2 -> 3
                else -> null
            }
        }.groupingBy { it }.eachCount().values.also { println(it) }
            .reduce { a, b -> a * b }
    }

    fun part2(input: List<String>, maxX: Int, maxY: Int): Int {
        val robots = parse(input)
        repeat(10000) { step ->
            val positions = robots.map {
                val x = normalize(it.px + it.vx * step, maxX)
                val y = normalize(it.py + it.vy * step, maxY)
                Vec2(x, y)
            }.toSet()
            val groups = ArrayList<Int>()
            val visited = HashSet<Vec2>()
            fun dfs(pos: Vec2): Int {
                var res = 1
                for (dx in -1..1) {
                    for (dy in -1..1) {
                        if (dx != 0 || dy != 0) {
                            val next = Vec2(pos.x + dx, pos.y + dy)
                            if (next in positions && next !in visited) {
                                visited.add(next)
                                res += dfs(next)
                            }
                        }
                    }
                }
                return res
            }

            for (p in positions) {
                if (p !in visited) {
                    visited += p
                    groups += dfs(p)
                }
            }
            if (groups.max() > 100) {
                println("$step: ${groups.sortedDescending()}")
                val visual = List(maxY + 1) { CharArray(maxX + 1) { '.' } }
                for (p in positions) {
                    visual[p.y][p.x] = '*'
                }
                println(visual.joinToString("\n") { it.joinToString("") })
                println()
            }

        }
        return 0
    }

    val input = readInput("Day14")
    val testInput = readInput("Day14_test")

    check(part1(testInput, 10, 6), 12)
    println(part1(input, 100, 102))

    part2(input, 100, 102)
}
