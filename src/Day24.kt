fun main() {
    fun part1(input: String): Long {
        val (initial, sGates) = input.split("\n\n")

        val values = HashMap<String, Int>()
        initial.lines().forEach { line ->
            val (wire, value) = line.split(": ")
            values[wire] = value.toInt()
        }

        val gates = sGates.lines().toMutableList()
        while (gates.isNotEmpty()) {
            gates.removeIf { line ->
                val (inputs, output) = line.split(" -> ")
                val (p0, cmd, p1) = inputs.split(" ")
                val p0Value = values[p0]
                val p1Value = values[p1]
                if (p0Value != null && p1Value != null) {
                    values[output] = when (cmd) {
                        "AND" -> p0Value and p1Value
                        "OR" -> p0Value or p1Value
                        "XOR" -> p0Value xor p1Value
                        else -> throw IllegalStateException()
                    }
                    true
                } else false
            }
        }
        return values.entries
            .filter { it.key.startsWith('z') }
            .sortedByDescending { it.key }
            .joinToString(separator = "") { it.value.toString() }
            .toLong(radix = 2)
    }

    data class Gate(val inputs: Set<String>, val command: String)

    fun part2(input: String): String {
        val gates = input.substringAfter("\n\n").lines().associate { line ->
            val (inputs, output) = line.split(" -> ")
            val (p0, cmd, p1) = inputs.split(" ")
            Gate(setOf(p0, p1), cmd) to output
        }

        val maxBits = 44
        val carry = Array(maxBits + 1) { "" }

        val changes = HashSet<String>()

        fun findGate(p0: String, p1: String, cmd: String): String {
            val exact = gates[Gate(setOf(p0, p1), cmd)]
            if (exact != null) {
                return exact
            } else {
                for ((fix, change) in listOf(p0 to p1, p1 to p0)) {
                    val gate = gates.keys.firstOrNull { fix in it.inputs && cmd == it.command }
                    if (gate != null) {
                        changes += change
                        changes += gate.inputs.single { it != fix }
                        return gates[gate]!!
                    }
                }
            }
            throw IllegalStateException()
        }

        for (i in 0..maxBits) {
            val xi = "x" + (if (i < 10) "0" else "") + i.toString()
            val yi = "y" + (if (i < 10) "0" else "") + i.toString()
            val xor0 = findGate(xi, yi, "XOR")
            val and0 = findGate(xi, yi, "AND")
            if (i == 0) {
                carry[0] = and0
            } else {
                val prevCarry = carry[i - 1]
                findGate(xor0, prevCarry, "XOR")

                val and1 = findGate(xor0, prevCarry, "AND")
                val or = findGate(and0, and1, "OR")
                carry[i] = or
            }
        }

        return changes.sorted().joinToString(",")
    }

    val input = readInputText("Day24")
    val testInput = readInputText("Day24_test")

    check(part1(testInput), 4L)
    println(part1(input))

    println(part2(input))
}

// x0 xor y0 -> val0
// x0 and y0 -> carry0
// (x1 xor y1) -> xor1
// (x1 xor y1) xor carry0 -> val1
// 0 1 1
// (x1 and y1) or (xor1 and carry0)
// (x2 xor y2) xor carry1 -> val2
// ....

// y00 AND x00 -> rhk (rhk = carry0)
// y01 XOR x01 -> vsn (=val1)
// y01 AND x01 -> hct
// rhk AND vsn -> tpp
// tpp OR hct -> mbr