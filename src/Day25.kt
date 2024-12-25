fun main() {
    fun part1(input: String): Int {
        val all = input.split("\n\n").map { it.lines() }

        val locks = all.filter { grid ->
            grid[0].all { it == '#' }
        }
        val keys = all.filter { grid ->
            grid[0].all { it == '.' }
        }

        fun List<String>.heights() = this[0].indices.map { idx ->
            count { it[idx] == '#' } - 1
        }
        fun matches(lock: List<String>, key: List<String>): Boolean {
            return key.heights().zip(lock.heights()).all { (h1, h2) -> h1 + h2 <= 5 }
        }

        return locks.sumOf { lock ->
            keys.count { key ->
                matches(lock, key)
            }
        }
    }

    val input = readInputText("Day25")
    val testInput = readInputText("Day25_test")

    check(part1(testInput), 3)
    println(part1(input))
}
