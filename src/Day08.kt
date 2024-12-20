fun main() {
    fun solve(input: List<String>, singleStep: Boolean = false): Int {
        val pos = HashSet<Pair<Int, Int>>()
        fun go(sx: Int, sy: Int, dx: Int, dy: Int) {
            var i = if (singleStep) 1 else 0
            while (true) {
                val p = sy + dy * i to sx + dx * i
                if (p.first in input.indices && p.second in input.indices) {
                    pos += p
                    i++
                } else {
                    return
                }
                if (singleStep) return
            }
        }

        for (i in input.indices) {
            for (j in input.indices) {
                for (y in input.indices) {
                    for (x in input.indices) {
                        if (input[i][j] == input[y][x] && input[i][j] != '.' && (i to j != y to x)) {
                            val offY = y - i
                            val offX = x - j
                            go(j, i, -offX, -offY)
                            go(x, y, offX, offY)
                        }
                    }
                }
            }
        }

        return pos.size
    }

    fun part1(input: List<String>): Int = solve(input, true)

    fun part2(input: List<String>): Int = solve(input, false)

    val input = readInput("Day08")
    val testInput = readInput("Day08_test")

    check(part1(testInput), 14)
    println(part1(input))

    check(part2(testInput), 34)
    println(part2(input))
}
