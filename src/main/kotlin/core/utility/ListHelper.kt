package core.utility

fun List<String>.matches(other: List<String>): Boolean {
    if (size != other.size) {
        return false
    }
    val listLower = map { it.lowercase() }
    val otherLower = other.map { it.lowercase() }

    listLower.forEach {
        if (!otherLower.contains(it)) {
            return false
        }
    }

    otherLower.forEach {
        if (!listLower.contains(it)) {
            return false
        }
    }

    return true
}

fun List<List<String>>.applyNested(params: Map<String, String>): List<List<String>> {
    return map {
        it.apply(params)
    }
}

fun List<String>.apply(params: Map<String, String>): List<String> {
    return map {
        it.apply(params)
    }
}

fun <E : Named> List<E>.filterUniqueByName(): List<E> {
    val unique = mutableListOf<String>()
    val results = mutableListOf<E>()
    forEach {
        if (!unique.contains(it.name)) {
            unique.add(it.name)
            results.add(it)
        }
    }
    return results
}

fun <E> List<E>.random(): E? {
    return if (isNotEmpty()) {
        get(getRandomRange(size))
    } else {
        null
    }
}

fun List<String>.lowercase(): List<String> {
    return this.map { it.lowercase() }
}

fun List<String>.removeFirstItem(): List<String> {
    return if (size > 1) subList(1, size) else listOf()
}

fun Array<String>.removeFirstItem(): Array<String> {
    return if (size > 1) toList().subList(1, size).toTypedArray() else arrayOf()
}

fun <E> List<E>.safeSubList(start: Int = 0, end: Int = this.size): List<E> {

    val cleanEnd = when {
        end < 0 -> 0
        end >= this.size -> this.size
        else -> end
    }

    val cleanStart = when {
        start < 0 -> 0
        start >= this.size -> this.size
        start > cleanEnd -> cleanEnd
        else -> start
    }

    return subList(cleanStart, cleanEnd)
}