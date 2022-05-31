package core.utility

fun String.wrapNonEmpty(prefix: String, suffix: String): String {
    return if (isBlank()) {
        this
    } else {
        prefix + this + suffix
    }
}

fun String.apply(params: Map<String, String>): String {
    var modified = this
    params.keys.sortedBy { -it.length }
        .forEach {
            modified = modified.replace("$$it", params[it]!!, true)
        }
    return modified
}

fun String.repeat(times: Int): String {
    var result = ""
    for (i in 0 until times) {
        result += this
    }
    return result
}

fun String.capitalize2(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

fun <T> List<T>.joinToStringAnd(transform: ((T) -> CharSequence)? = null): String {
    return joinToStringWithDifferentLast("and", transform)
}

fun <T> List<T>.joinToStringOr(transform: ((T) -> CharSequence)? = null): String {
    return joinToStringWithDifferentLast("or", transform)
}

fun <T> List<T>.joinToStringWithDifferentLast(lastDelimiter: String, transform: ((T) -> CharSequence)? = null): String {
    val stringList = if (transform == null) this else {
        this.map { transform(it) }
    }
    return if (size > 1) {
        val subList = stringList.subList(0, size - 1)
        val last = if (transform == null) last().toString() else {
            transform(last())
        }
        subList.joinToString(", ") + " $lastDelimiter " + last
    } else {
        stringList.joinToString(", ")
    }
}

fun String.highlightCurrent(current: String): String {
    return if (lowercase() == current.lowercase()) "*$this" else this
}