fun main() {
    fun part1(input: List<String>): Int {
        val connections = HashMap<String, HashSet<String>>()
        input.forEach { line ->
            val (u, v) = line.split("-")
            connections.getOrPut(u) { HashSet() }.add(v)
            connections.getOrPut(v) { HashSet() }.add(u)
        }

        val result = HashSet<Set<String>>()
        for ((u, nextU) in connections) {
            for ((v, nextV) in connections) {
                if (v in nextU) {
                    val options = nextU.intersect(nextV)
                        .map { setOf(u, v, it) }
                        .filter { triple -> triple.any { it.startsWith("t") } }
                    result.addAll(options)
                }
            }
        }
        return result.size
    }

    fun part2(input: List<String>): String {
        val connections = HashMap<String, HashSet<String>>()
        input.forEach { line ->
            val (u, v) = line.split("-")
            connections.getOrPut(u) { HashSet() }.add(v)
            connections.getOrPut(v) { HashSet() }.add(u)
        }

        fun cliqueFrom(v: String, next: Set<String>): List<String> {
            var common: MutableSet<String> = HashSet<String>(next + v).toMutableSet()
            for (u in next) {
                val uNext = HashSet<String>(connections[u]!! + u)
                val newCommon = common.intersect(uNext)
                if (newCommon.size < common.size - 1) {
                    common -= u
                } else {
                    common = newCommon.toMutableSet()
                }
            }
            return common.toList().sorted()
        }

        return connections
            .map { cliqueFrom(it.key, it.value) }
            .maxBy { it.size }
            .joinToString(",")
    }

    val input = readInput("Day23")
    val testInput = readInput("Day23_test")

    check(part1(testInput), 7)
    println(part1(input))

    check(part2(testInput), "co,de,ka,ta")
    println(part2(input))
}
