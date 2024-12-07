import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val lists = input.map { it.split("\\s+".toRegex()) }
        val first = lists.map { it[0].toInt() }.sorted()
        val second = lists.map { it[1].toInt() }.sorted()
        return first.indices.sumOf { abs(first[it] - second[it]) }
    }

    fun part2(input: List<String>): Long {
        val lists = input.map { it.split("\\s+".toRegex()) }
        val first = lists.map { it[0].toInt() }.sorted()
        val second = lists.map { it[1].toInt() }
            .groupBy { it }.mapValues { it.value.size }

        return first.sumOf { it.toLong() * (second[it] ?: 0) }
    }
    val input = readInput("Day01")
    val testInput = readInput("Day01_test")

    check(part1(testInput) == 11)
    println(part1(input))

    check(part2(testInput) == 31L)
    println(part2(input))
}
