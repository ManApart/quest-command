package status.conditions

fun getPersisted(dataObject: Condition): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    return mapOf()
}

fun readFromData(data: Map<String, Any>): Condition {
    return Condition("NONE")
}