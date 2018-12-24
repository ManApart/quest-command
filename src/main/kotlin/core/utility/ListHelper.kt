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

fun List<String>.apply(params: Map<String, String>): List<String> {
    return map {
        it.apply(params)
    }
}