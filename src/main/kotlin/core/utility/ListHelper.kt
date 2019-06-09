package core.utility

fun List<String>.matches(other: List<String>): Boolean {
    if (size != other.size) {
        return false
    }
    val listLower = map { it.toLowerCase() }
    val otherLower = other.map { it.toLowerCase() }

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

