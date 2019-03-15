
// The possible values for a cell, and how often each can be repeated within a region.
enum class Value(val number: Int, val repeats: Int) {

    ONE(1, 2),
    TWO(2, 1),
    THREE(3, 2),
    FOUR(4, 1),
    FIVE(5, 3),
    SIX(6, 1),
    EIGHT(8, 1),
    NINE(9, 1);

    companion object {
        fun fromChar(c: Char) : Value? = when(c) {
            '1' -> ONE
            '2' -> TWO
            '3' -> THREE
            '4' -> FOUR
            '5' -> FIVE
            '6' -> SIX
            '8' -> EIGHT
            '9' -> NINE
            else -> null
        }
    }

}


class Cell(value: Value?) {

    val candidates: MutableSet<Value>

    init {
        when (value) {
            null -> candidates = mutableSetOf(* Value.values())
            else -> candidates = mutableSetOf(value)
        }
    }

    val isFixed get() = candidates.size == 1

    fun exclude(value: Value) {
        if (candidates.size > 1) candidates.remove(value)
    }

    fun setTo(value: Value) {
        candidates.clear()
        candidates.add(value)
    }

    val value get() = if (isFixed) candidates.elementAt(0) else null
}