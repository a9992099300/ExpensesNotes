package utils

fun Int.addZeroForTime() = if (toString().length == 1) "0$this" else this

fun Long.formatPrice(): String {
    val value = this.toString().toMutableList()
    val countSpace = (value.size - 1) / 3
    var startIndex = 1
    if (value.size >= 4) {
        (1..countSpace).forEach { index ->
            value.add(value.size - (index * 3) - index + 1, ' ')
            startIndex += 1
        }
    }
    return "${value.toCharArray().concatToString()} Р"
}