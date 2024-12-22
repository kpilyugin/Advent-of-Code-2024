fun main() {
    fun solve(input: String): Pair<Int, Long> {
        val (sPatterns, designs) = input.split("\n\n")

        val patterns = sPatterns.split(", ").toSet()

        val memo = HashMap<String, Long>()
        fun countPossible(design: String): Long {
            memo[design]?.let { return it }
            if (design.isEmpty()) return 1

            var res = 0L
            for (p in patterns) {
                if (design.startsWith(p)) {
                    res += countPossible(design.substringAfter(p))
                }
            }
            memo[design] = res
            return res
        }
        return designs.lines().count { countPossible(it) > 0 } to
                designs.lines().sumOf { countPossible(it) }
    }

    val input = readInputText("Day19")
    val testInput = readInputText("Day19_test")

    check(solve(testInput), 6 to 16L)
    println(solve(input))
}