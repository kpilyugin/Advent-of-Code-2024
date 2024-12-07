fun main() {
    fun part1(input: String): Int {
        val (sRules, sUpdates) = input.split("\n\n")

        val rules = sRules.lines()
            .map { it.split("|") }
            .map { it[0].toInt() to it[1].toInt() }
            .toSet()

        return sUpdates.lines().sumOf { update ->
            val list = update.split(",").map { it.toInt() }
            var good = true
            for (i in list.indices) {
                for (j in i + 1 until list.size) {
                    if (list[j] to list[i] in rules) good = false
                }
            }
            if (good) list[list.size / 2] else 0
        }
    }

    fun part2(input: String): Int {
        val (sRules, sUpdates) = input.split("\n\n")

        val rules = sRules.lines()
            .map { it.split("|") }
            .map { it[0].toInt() to it[1].toInt() }
            .toSet()

        return sUpdates.lines().sumOf { update ->
            val list = update.split(",").map { it.toInt() }
            var good = true
            for (i in list.indices) {
                for (j in i + 1 until list.size) {
                    if (list[j] to list[i] in rules) good = false
                }
            }
            if (!good) {
                val sorted = list.sortedWith { a, b ->
                    when {
                        a to b in rules -> -1
                        b to a in rules -> 1
                        else -> 0
                    }
                }
                sorted[sorted.size / 2]
            } else 0
        }
    }

    val input = readInputText("Day05")
    val testInput = readInputText("Day05_test")

    check(part1(testInput), 143)
    println(part1(input))

    check(part2(testInput), 123)
    println(part2(input))
}
