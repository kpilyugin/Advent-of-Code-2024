fun main() {
    fun solve(input: List<String>, steps: Int): Long {
        var state = input.single().split(" ").map { it.toLong() }.associateWith { 1L }
        repeat(steps) {
            val next = HashMap<Long, Long>()

            for ((value, count) in state) {
                val strValue = value.toString()
                val len = strValue.length

                fun add(newValue: Long) {
                    next.compute(newValue) { _, v -> (v ?: 0L) + count }
                }
                when {
                    value == 0L -> add(1L)
                    len % 2 == 0 -> {
                        add(strValue.take(len / 2).toLong())
                        add(strValue.takeLast(len / 2).toLong())
                    }
                    else -> add(value * 2024)
                }
            }
            state = next
        }
        return state.values.sum()
    }

    fun part1(input: List<String>): Long {
        return solve(input, 25)
    }

    fun part2(input: List<String>): Long {
        return solve(input, 75)
    }

    val input = readInput("Day11")
    val testInput = readInput("Day11_test")

    check(part1(testInput), 55312L)
    println(part1(input))

    println(part2(input))
}
