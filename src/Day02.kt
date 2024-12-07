fun main() {
    fun List<Int>.safe(): Boolean {
        val diffs = zipWithNext { a, b -> b - a }
        return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
    }

    fun part1(input: List<String>): Int {
        return input.count { line ->
            line.split(" ").map { it.toInt() }.safe()
        }
    }

    fun part2(input: List<String>): Int {
        fun List<Int>.without(i: Int): List<Int> = filterIndexed { index, _ -> index != i }

        return input.count { line ->
            val full = line.split(" ").map { it.toInt() }
            full.safe() || full.indices.any { full.without(it).safe() }
        }
    }

    val input = readInput("Day02")
    val testInput = readInput("Day02_test")

    check(part1(testInput) == 2)
    println(part1(input))

    check(part2(testInput) == 4)
    println(part2(input))
}
