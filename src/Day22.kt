fun main() {
    fun Long.prune() = this % 16777216

    fun Long.mix(other: (Long) -> Long) = this xor other(this)

    fun Long.next(): Long {
        return mix { it * 64 }.prune()
            .mix { it / 32 }.prune()
            .mix { it * 2048 }.prune()
    }

    fun Long.sequenceFrom() = generateSequence(this) { it.next() }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            line.toLong().sequenceFrom().elementAt(2000)
        }
    }

    fun part2(input: List<String>): Int {
        val scores = HashMap<List<Int>, Int>()

        input.forEach { line ->
            val prices = line.toLong().sequenceFrom().take(2001).map { (it % 10).toInt() }.toList()
            val visited = HashSet<List<Int>>()
            prices.zipWithNext { a, b -> b - a }
                .windowed(4)
                .forEachIndexed { i, pattern ->
                    if (pattern !in visited) {
                        scores.compute(pattern) { _, prev -> (prev ?: 0) + prices[i + 4] }
                        visited += pattern
                    }
                }
        }
        return scores.values.max()
    }

    val input = readInput("Day22")
    val testInput = readInput("Day22_test")
    val testInput2 = readInput("Day22_test2")

    check(part1(testInput), 37327623L)
    println(part1(input))

    check(part2(testInput2), 23)
    println(part2(input))
}
