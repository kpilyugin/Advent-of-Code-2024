fun main() {
    fun part1(input: List<String>): Long {
        val regex = "mul\\((\\d+),(\\d+)\\)".toRegex()
        return input.sumOf { line ->
            regex.findAll(line, 0).sumOf {
                it.groupValues[1].toLong() * it.groupValues[2].toLong()
            }
        }
    }

    fun part2(input: List<String>): Long {
        val regex = "mul\\((\\d+),(\\d+)\\)".toRegex()

        val line = input.joinToString("")
        val matches = regex.findAll(line, 0).toList()
        val dontMatches = "don't".toRegex().findAll(line, 0).toList()
        val doMatches = "do".toRegex().findAll(line, 0).toList().filter { cur ->
            dontMatches.none { it.range.first == cur.range.first }
        }
        val allMatches = (matches + dontMatches + doMatches).sortedBy { it.range.first }
        var enabled = true
        var sum = 0L
        allMatches.forEach {
            val match = it.groupValues[0]
            when {
                "mul" in match -> if (enabled) sum += it.groupValues[1].toLong() * it.groupValues[2].toLong()
                match == "do" -> enabled = true
                match == "don't" -> enabled = false
            }
        }
        return sum
    }

    val input = readInput("Day03")
    val testInput = readInput("Day03_test")

    check(part1(testInput) == 161L)
    println(part1(input))

    val testInput2 = readInput("Day03_test2")
    check(part2(testInput2) == 48L)
    println(part2(input))
}
