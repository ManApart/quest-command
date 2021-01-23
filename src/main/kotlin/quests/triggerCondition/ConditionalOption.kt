package quests.triggerCondition

interface ConditionalOption<T> {
    val option: T
    val condition: (Map<String, String>) -> Boolean
    fun apply(params: Map<String, String>) : T
}
