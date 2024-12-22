import kotlin.math.abs

class Grid(source: List<String>) {
    private val data: List<CharArray> = source.map { it.toCharArray() }

    constructor(size: Int) : this(List(size) { ".".repeat(size) })

    fun find(c: Char): Vec2 {
        for (y in data.indices) {
            for (x in data[0].indices) {
                if (data[y][x] == c) {
                    return Vec2(x, y)
                }
            }
        }
        throw IllegalStateException()
    }

    fun positions(): List<Vec2> {
        return data.indices.flatMap { y ->
            data[0].indices.map { x -> Vec2(y, x) }
        }
    }

    fun safeGet(pos: Vec2): Char? {
        return if (pos.x in data[0].indices && pos.y in data.indices) {
            data[pos.y][pos.x]
        } else null
    }

    fun swap(p1: Vec2, p2: Vec2) {
        val tmp = data[p1.y][p1.x]
        data[p1.y][p1.x] = data[p2.y][p2.x]
        data[p2.y][p2.x] = tmp
    }

    fun forEachIndexed(action: (Int, Int, Char) -> Unit) {
        for (y in data.indices) {
            for (x in data[0].indices) {
                action(x, y, data[y][x])
            }
        }
    }

    operator fun get(pos: Vec2) = data[pos.y][pos.x]

    operator fun set(pos: Vec2, value: Char) {
        data[pos.y][pos.x] = value
    }

    fun adjacentIncludeBorder(pos: Vec2): List<Vec2> {
        return listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
            .map { (dx, dy) -> Vec2(pos.x + dx, pos.y + dy) }
    }

    fun adjacent(pos: Vec2): List<Vec2> {
        return listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
            .mapNotNull { (dx, dy) ->
                val x = pos.x + dx
                val y = pos.y + dy
                if (x in data.indices && y in data.indices) Vec2(x, y) else null
            }
    }

    fun next(pos: Vec2, side: Dir): Vec2 {
        return when (side) {
            Dir.LEFT -> Vec2(pos.x - 1, pos.y)
            Dir.RIGHT -> Vec2(pos.x + 1, pos.y)
            Dir.UP -> Vec2(pos.x, pos.y - 1)
            Dir.DOWN -> Vec2(pos.x, pos.y + 1)
        }
    }

    override fun toString() = data.joinToString("\n") { it.joinToString("") }
}

enum class Dir {
    LEFT, RIGHT, UP, DOWN;

    fun rotateLeft(): Dir {
        return when (this) {
            LEFT -> DOWN
            RIGHT -> UP
            UP -> LEFT
            DOWN -> RIGHT
        }
    }

    fun rotateRight(): Dir {
        return when (this) {
            LEFT -> UP
            RIGHT -> DOWN
            UP -> RIGHT
            DOWN -> LEFT
        }
    }
}

fun Char.parseDir(): Dir {
    return when (this) {
        '>' -> Dir.RIGHT
        '<' -> Dir.LEFT
        '^' -> Dir.UP
        'v' -> Dir.DOWN
        else -> throw IllegalArgumentException()
    }
}

data class Vec2(val x: Int, val y: Int)

fun Vec2.manhattan(other: Vec2): Int = abs(x - other.x) + abs(y - other.y)