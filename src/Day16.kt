import java.util.*

fun main() {
    class Path(val cost: Int, val totalTiles: Int)

    fun solve(input: List<String>): Path {
        val grid = Grid(input)
        val start = grid.find('S')

        data class PosDir(val pos: Vec2, val dir: Dir)
        data class State(val pos: Vec2, val dir: Dir, val cost: Int) : Comparable<State> {
            override fun compareTo(other: State): Int {
                return cost - other.cost
            }

            fun posDir() = PosDir(pos, dir)
        }
        val visited = HashMap<PosDir, Int>()
        val sources = HashMap<PosDir, MutableList<PosDir>>()
        val q = PriorityQueue<State>()

        fun add(cur: State?, newState: State) {
            val prevCost = visited[newState.posDir()] ?: Int.MAX_VALUE
            if (newState.cost <= prevCost) {
                if (newState.cost < prevCost) {
                    q.add(newState)
                    visited[newState.posDir()] = newState.cost
                    sources[newState.posDir()]?.clear()
                }
                if (cur != null) {
                    sources.getOrPut(newState.posDir()) { mutableListOf() }.add(cur.posDir())
                }
            }
        }

        fun fillPath(v: PosDir, path: HashSet<PosDir>): HashSet<PosDir> {
            path += v
            sources[v]?.let { src ->
                src.forEach {
                    if (it !in path) {
                        fillPath(it, path)
                    }
                }
            }
            return path
        }

        add(null, State(start, Dir.RIGHT, 0))
        while (q.isNotEmpty()) {
            val cur = q.poll()
            if (grid[cur.pos] == 'E') {
                val path = fillPath(cur.posDir(), HashSet()).map { it.pos }.toSet()
                return Path(cur.cost, path.size)
            }
            val next = grid.next(cur.pos, cur.dir)
            if (grid[next] != '#') {
                add(cur, State(next, cur.dir, cur.cost + 1))
            }
            add(cur, State(cur.pos, cur.dir.rotateLeft(), cur.cost + 1000))
            add(cur, State(cur.pos, cur.dir.rotateRight(), cur.cost + 1000))
        }
        throw IllegalStateException()
    }

    fun part1(input: List<String>): Int = solve(input).cost

    fun part2(input: List<String>): Int = solve(input).totalTiles

    val input = readInput("Day16")
    val testInput = readInput("Day16_test")
    val testInput2 = readInput("Day16_test2")

    check(part1(testInput), 11048)
    println(part1(input))

    check(part2(testInput), 64)
    check(part2(testInput2), 45)
    println(part2(input))
}
