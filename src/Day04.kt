fun main() {
    fun part1(input: List<String>): Long {
        val n = input.size
        val m = input[0].length

        data class Path(val sx: Int, val sy: Int, val dx: Int, val dy: Int) {
            fun score() = path().score()

            private fun path(): String {
                var res = ""
                var i = 0
                while (true) {
                    val x = sx + dx * i
                    val y = sy + dy * i
                    if (x in 0 until m && y in 0 until m) {
                        res += input[y][x]
                        i++
                    } else {
                        return res
                    }
                }
            }

            private fun String.score(): Int {
                return "XMAS".toRegex().findAll(this).toList().size
            }
        }

        val paths = (0 until n).flatMap {
            buildList {
                for ((dx, dy) in listOf(-1 to 0, 1 to 0, -1 to -1, 1 to 1, 1 to -1, -1 to 1)) {
                    add(Path(0, it, dx, dy))
                    add(Path(m - 1, it, dx, dy))
                }
                for ((dx, dy) in listOf(0 to 1, 0 to -1, -1 to -1, 1 to 1, 1 to -1, -1 to 1)) {
                    add(Path(it, 0, dx, dy))
                    add(Path(it, n - 1, dx, dy))
                }
            }
        }
        return paths.toSet()
            .sumOf { it.score().toLong() }
    }

    fun part2(input: List<String>): Int {
        fun matches(pattern: List<String>): Int {
            var count = 0
            for (x in 0 until input.size - 2) {
                for (y in 0 until input[0].length - 2) {
                    var good = true
                    for (i in 0..2) {
                        for (j in 0..2) {
                            if (pattern[i][j] != '.' && pattern[i][j] != input[x + i][y + j]) {
                                good = false
                            }
                        }
                    }
                    if (good) {
                        count++
                    }
                }
            }
            return count
        }

        fun rotate(pattern: List<String>): List<String> {
            val res = List(3) { CharArray(3) }
            for (i in 0..2) {
                for (j in 0..2) {
                    res[i][j] = pattern[j][2 - i]
                }
            }
            return res.map { String(it) }
        }

        var pattern = listOf(
            "M.S",
            ".A.",
            "M.S"
        )
        var res = 0
        repeat(4) {
            res += matches(pattern)
            pattern = rotate(pattern)
        }
        return res
    }

    val input = readInput("Day04")
    val testInput = readInput("Day04_test")

    check(part1(testInput), 18L)
    println(part1(input))

    val testInput2 = readInput("Day04_test2")
    check(part2(testInput2), 9)
    println(part2(input))
}
