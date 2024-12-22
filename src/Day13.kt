fun main() {
    /*
     ax * a + bx * b = X
     ay * a + by * b = Y

     ax * ay * a + bx * ay * b = X * ay
     ax * ay * a + ax * by * b = Y * ax

     bx * ay * b - ax * by * b = X - Y
     b = (X * ay - Y * ax) / (ay * bx - ax * by)
     a = (X * by - Y * bx) / (by * ax - ay * bx)
     */

    infix fun Long.safeDiv(denom: Long): Long? {
        return if (this % denom == 0L) this / denom else null
    }

    fun solve(input: String, processPrize: (List<Long>) -> List<Long>): Long {
        return input.split("\n\n").sumOf { part ->
            val (a, b, givenPrize) = part.lines().map { line ->
                line.split(" ")
                    .map { it.replace("[^0-9]".toRegex(), "") }
                    .filter { it.isNotEmpty() }
                    .map { it.toLong() }
            }
            val prize = processPrize(givenPrize)

            val coefB = (prize[0] * a[1] - prize[1] * a[0]) safeDiv (a[1] * b[0] - a[0] * b[1])
            val coefA = (prize[0] * b[1] - prize[1] * b[0]) safeDiv (b[1] * a[0] - b[0] * a[1])

            if (coefA != null && coefB != null) {
                coefA * 3 + coefB
            } else 0
        }
    }

    fun part1(input: String) = solve(input) { it }

    fun part2(input: String): Long {
        val P = 10000000000000
        return solve(input) {
            prize -> prize.map { it + P }
        }
    }

    val input = readInputText("Day13")
    val testInput = readInputText("Day13_test")

    check(part1(testInput), 480L)
    println(part1(input))

    check(part2(testInput), 875318608908)
    println(part2(input))
}
