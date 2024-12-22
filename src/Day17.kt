import kotlin.math.pow

fun main() {
    class Computer(var a: Long, var b: Long, var c: Long, val opcodes: List<Int>) {
        var pointer = 0

        fun run(debug: Boolean = false): List<Int> {
            val output = ArrayList<Int>()

            fun debug(s: String) {
                if (debug) println(s)
            }

            while (pointer in opcodes.indices) {
                val code = opcodes[pointer]
                var jumped = false
                when (code) {
                    0 -> {
                        a = divA()
                        debug("a = a / 2 ^ ${descCombo()}: $a")
                    }

                    1 -> {
                        b = b xor literal().toLong()
                        debug("b = b xor ${literal()}: $b")
                    }

                    2 -> {
                        b = combo() % 8
                        debug("b = ${descCombo()} % 8: $b")
                    }

                    3 -> {
                        if (a != 0L) {
                            pointer = literal()
                            jumped = true
                            debug("jump ${literal()}")
                        } else {
                            debug("skip jump")
                        }
                    }

                    4 -> {
                        b = b xor c
                        debug("b = b xor c: $b")
                    }

                    5 -> {
                        output += (combo() % 8).toInt()
                        debug("output ${descCombo()} % 8: ${combo() % 8}")
                    }

                    6 -> {
                        b = divA()
                        debug("b = a / 2 ^ ${descCombo()}: $b")
                    }

                    7 -> {
                        c = divA()
                        debug("c = a / 2 ^ ${descCombo()}: $c")
                    }
                }
                if (!jumped) pointer += 2
            }
            return output
        }

        private fun divA() = (a / 2.0.pow(combo().toDouble())).toLong()

        private fun combo(): Long = when (val value = opcodes[pointer + 1]) {
            4 -> a
            5 -> b
            6 -> c
            else -> value.toLong()
        }

        private fun descCombo(): String = when (val value = opcodes[pointer + 1]) {
            4 -> "a"
            5 -> "b"
            6 -> "c"
            else -> value.toString()
        }

        private fun literal(): Int = opcodes[pointer + 1]
    }

    fun part1(input: String): String {
        val (registers, program) = input.split("\n\n")
        val regs = registers.lines().map { it.substringAfter(": ").toLong() }
        val codes = program.substringAfter(": ").split(",").map { it.toInt() }
        val computer = Computer(regs[0], regs[1], regs[2], codes)
        val result = computer.run()
        return result.joinToString(",")
    }

    fun part2(input: String): Long {
        val program = input.split("\n\n")[1]
        val codes = program.substringAfter(": ").split(",").map { it.toInt() }

        fun forward(a: Long): Int {
            var b = a % 8
            b = b xor 2
            val c = a / (2.0.pow(b.toDouble())).toInt()
            b = b xor c
            b = b xor 7
            return (b % 8).toInt()
        }

        var a = 0L
        for (out in codes.reversed()) {
            val next = (0..7)
                .map { a * 8 + it }
                .first { forward(it) == out }
            println(next)
            a = next
        }

        val computer = Computer(a, 0, 0, codes)

        val result = computer.run(true)
        check(codes, result)
        return a
    }

    val input = readInputText("Day17")
    val testInput = readInputText("Day17_test")
//    val testInput2 = readInputText("Day17_test2")

    check(part1(testInput), "4,6,3,5,6,3,5,2,1,0")
    println(part1(input))

    println(part2(input))
}