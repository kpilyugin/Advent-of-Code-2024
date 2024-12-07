fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val (sTarget, rest) = line.split(": ")

            val target = sTarget.toLong()
            val values = rest.split(" ").map { it.toLong() }

            fun can(cur: Long, start: Int): Boolean {
                if (start == values.size) return cur == target
                if (cur > target) return false
                return can(cur + values[start], start + 1) || can(cur * values[start], start + 1)
            }

            if (can(0, 0)) target else 0L
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val (sTarget, rest) = line.split(": ")

            val target = sTarget.toLong()
            val values = rest.split(" ").map { it.toLong() }

            fun can(cur: Long, start: Int): Boolean {
                if (start == values.size) return cur == target
                if (cur > target) return false
                return can(cur + values[start], start + 1)
                        || can(cur * values[start], start + 1)
                        || can((cur.toString() + values[start].toString()).toLong(), start + 1)
            }
            if (can(0, 0)) target else 0L
        }
    }

    val input = readInput("Day07")
    val testInput = readInput("Day07_test")

    check(part1(testInput), 3749L)
    println(part1(input))

    check(part2(testInput), 11387L)
    println(part2(input))
}
