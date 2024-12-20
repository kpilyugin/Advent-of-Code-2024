fun main() {
    fun part1(input: List<String>): Long {
        val flatten = ArrayList<Int>()
        var id = 0
        var space = false
        input.single().forEach {
            val count = it.digitToInt()
            repeat(count) {
                flatten.add(if (space) -1 else id)
            }
            space = !space
            if (!space) id++
        }

        var start = flatten.indexOfFirst { it == -1 }
        var end = flatten.indexOfLast { it != -1 }
        while (start < end) {
            if (flatten[start] == -1) {
                flatten[start] = flatten[end]
                flatten[end] = -1
                end--
            }
            while (flatten[end] == -1) end--
            start++
        }

        return flatten.withIndex().sumOf { (idx, value) ->
            if (value == -1) 0 else value.toLong() * idx
        }
    }

    data class Group(val size: Int, val id: Int, var empty: Boolean) {
        fun score(idx: Int): Long {
            return (0 until size).sumOf { (idx + it).toLong() * id }
        }
    }

    fun part2(input: List<String>): Long {
        val groups = ArrayList<Group>()
        var id = 0
        var empty = false
        input.single().forEach {
            val count = it.digitToInt()
            groups += Group(count, if (empty) -1 else id, empty)
            empty = !empty
            if (!empty) id++
        }

        val toMove = groups.filter { !it.empty }.map { it.copy() }.reversed()
        toMove.forEach { cur ->
            val curIndex = groups.indexOfFirst { it.id == cur.id }
            val spaceIdx = groups.indexOfFirst { it.empty && it.size >= cur.size }
            if (spaceIdx != -1 && spaceIdx < curIndex) {
                val space = groups[spaceIdx]
                groups.removeAt(spaceIdx)
                groups.first { it.id == cur.id }.empty = true
                groups.add(spaceIdx, cur)
                val remain = space.size - cur.size
                if (remain > 0) {
                    groups.add(spaceIdx + 1, Group(remain, -1, true))
                }
            }
        }

        var idx = 0
        var score = 0L
        groups.forEach {
            if (!it.empty) {
                score += it.score(idx)
            }
            idx += it.size
        }
        return score
    }

    val input = readInput("Day09")
    val testInput = readInput("Day09_test")

    check(part1(testInput), 1928L)
    println(part1(input))

    check(part2(testInput), 2858L)
    println(part2(input))
}
