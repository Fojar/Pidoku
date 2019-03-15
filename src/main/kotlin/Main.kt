import java.lang.IllegalStateException

fun main() {

    val grid = Grid(
        listOf(
            "3  154  1 95",
            " 1  3    136",
            "  4  3 8  2 ",
            "5  1  925  1",
            " 9  5  5    ",
            "581  9  3 6 ",
            " 5 8  2  553",
            "    5  6  1 ",
            "2  515  5  9",
            " 6  4 1  3  ",
            "151    5  5 ",
            "55 4  316  8"
        )
    )

    val blocks = listOf(
        "AAABBBBBBCCC",
        "AAABBBBBBCCC",
        "AADDDDEEEECC",
        "AADDDDEEEECC",
        "AADDDDEEEECC",
        "FFFGGHHIIJJJ",
        "FFFGGHHIIJJJ",
        "FFFGGHHIIJJJ",
        "FFFGGHHIIJJJ",
        "KKKGGHHIILLL",
        "KKKGGHHIILLL",
        "KKKKKKLLLLLL"
    )


    // A region is a set of cells that constrain each other.
    val regions: MutableList<List<Cell>> = mutableListOf()

    // Add rows and columns as regions.
    for (i in 0 until 12) {

        val row = mutableListOf<Cell>()
        val col = mutableListOf<Cell>()

        for (j in 0 until 12) {
            col.add(grid[i, j])
            row.add(grid[j, i])
        }

        regions.add(row)
        regions.add(col)
    }

    // Calculate and add blocks as regions.
    val blockCells = mutableMapOf<Char, MutableList<Cell>>()

    for (y in 0 until 12) {
        for (x in 0 until 12) {
            val c = blocks[y][x]
            blockCells
                .getOrPut(c) { mutableListOf<Cell>() }
                .add(grid[x, y])
        }
    }

    regions.addAll(blockCells.values)

    println("Initial state:")
    grid.printStats()

    val start = System.currentTimeMillis()

    var steps = 0
    while (grid.fixedCellCount < 144) {

        regions.forEach(::eliminateFixedCandidates)
        steps++

        regions.forEach(::identifyUniqueCandidates)
        steps++

        regions.forEach(::identifyDeterminedSubsets)
        steps++

        grid.printStats()
    }

    val duration = System.currentTimeMillis() - start

    regions.forEach(::verifyRegionIsValid)

    println("Puzzle solved in $steps steps, $duration ms.")

    grid.print()
}

fun verifyRegionIsValid(region: List<Cell>) {

    Value.values().forEach { value ->
        if (value.repeats != region.count { it.value == value }) throw IllegalStateException()
    }
}

fun eliminateFixedCandidates(region: List<Cell>) {

    region.asSequence()
        .filter { it.isFixed }
        .forEach { fixedCell ->
            val fixedValue = fixedCell.value!!
            val count = region.count { it.value == fixedValue }
            if (count == fixedValue.repeats) {
                region.forEach {
                    it.exclude(fixedCell.value!!)
                }
            }
        }
}

fun identifyUniqueCandidates(region: List<Cell>) {

    Value.values().forEach { value ->
        val cellsWithCandidate = region.filter { it.candidates.contains(value) }

        if (cellsWithCandidate.size == value.repeats) {
            cellsWithCandidate.forEach { it.setTo(value) }
            region.forEach { it.exclude(value) }
        }
    }
}

fun identifyDeterminedSubsets(region: List<Cell>) {

    val (fixedCells, unfixedCells) = region.partition { it.isFixed }

    val unfixedCellsByCandidates = unfixedCells.groupBy { it.candidates }

    for ((candidateSet, cells) in unfixedCellsByCandidates) {

        // Determine total number of instances of all values that are not yet fixed in the region.
        val totalAvailableValueInstances = candidateSet.sumBy { value ->
            value.repeats - fixedCells.count { it.value == value }
        }

        if (totalAvailableValueInstances == cells.size) {
            // All other unfixed cells in the region can't have any of these candidates!
            unfixedCells.asSequence()
                .filter { it !in cells }
                .forEach { cell ->
                    candidateSet.forEach { cell.exclude(it) }
                }
        }
    }
}