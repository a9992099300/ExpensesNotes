package utils

fun Int.addZeroForTime() = if (toString().length == 1) "0$this" else this