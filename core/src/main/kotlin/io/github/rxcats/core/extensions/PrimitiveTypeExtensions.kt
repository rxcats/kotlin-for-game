package io.github.rxcats.core.extensions

fun Int?.orDefault(v: Int = 0): Int {
    return this ?: v
}

fun Long?.orDefault(v: Long = 0L): Long {
    return this ?: v
}

fun String?.orDefault(v: String = ""): String {
    return this ?: v
}

fun Double?.orDefault(v: Double = 0.0): Double {
    return this ?: v
}

fun Float?.orDefault(v: Float = 0.0f): Float {
    return this ?: v
}

fun Boolean?.orDefault(v: Boolean = false): Boolean {
    return this ?: v
}
