package core.utility

fun String.wrapNonEmpty(prefix: String, suffix: String) : String {
    return if (isBlank()) {
        this
    } else {
        prefix + this + suffix
    }
}