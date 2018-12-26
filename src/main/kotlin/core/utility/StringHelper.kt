package core.utility

fun String.wrapNonEmpty(prefix: String, suffix: String) : String {
    return if (isBlank()) {
        this
    } else {
        prefix + this + suffix
    }
}

fun String.apply(params: Map<String, String>): String {
    var modified = this
    params.forEach {
        modified = modified.replace("$${it.key}", it.value, true)
    }
    return modified
}