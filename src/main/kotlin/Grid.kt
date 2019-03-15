class Grid(input: List<String>) {

    private val cells: Array<Array<Cell>>

    init {

        cells = Array(12) { y ->
            Array(12) { x ->
                val c = input[y][x]
                val v = Value.fromChar(c)
                Cell(v)
            }
        }
    }

    operator fun get(x: Int, y: Int) = cells[y][x]


    // Prints out the grid, showing candidates and fixed cells.
    fun print() {

        val values = Value.values()

        for (row in cells) {

            for (y in 0 until 3) {

                for (cell in row) {

                    val value = cell.value

                    if (value != null) {
                        print(if (y == 1) "| ${value.number.toString()} | " else "+---+ ")
                    } else {

                        for (x in 0 until 3) {

                            val index = y * 3 + x
                            val c = if (index < values.size) {
                                if (cell.candidates.contains(values[index])) values[index].number.toString()
                                else "."

                            } else " "
                            print("$c ")
                        }

                    }
                    print("  ")
                }
                println()

            }
            println()
            println()
        }
        println("=".repeat(8 * 12))
    }

    val fixedCellCount get() = cells.sumBy { it.count { it.isFixed } }

    val totalCandidates get() = cells.sumBy { row -> row.sumBy { it.candidates.size } }

    fun printStats() {
        println("   Unfixed cells: ${144 - fixedCellCount}")
        println("Extra candidates: ${totalCandidates - 144}")
        println()
    }

}