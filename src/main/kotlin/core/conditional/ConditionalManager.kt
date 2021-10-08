package core.conditional

import core.conditional.ConditionalStringType.DEFAULT

object ConditionalManager {
    private var types = buildTypeMap()

    private fun buildTypeMap() : Map<ConditionalStringType, List<ConditionalString>>{
        return mapOf(
                DEFAULT to listOf(),
        )
    }

    fun reset() {
        types = buildTypeMap()
    }

    fun getTypeMap() : Map<ConditionalStringType, List<ConditionalString>> {
        return types
    }

    fun getConditionalString(name: String, type: ConditionalStringType) : ConditionalString {
        return (types[type] ?: error("Unknown Condition Type $type")).first { it.name == name }
    }

    fun getOption(name: String, type: ConditionalStringType) : String {
        return getConditionalString(name, type).getOption()
    }

}