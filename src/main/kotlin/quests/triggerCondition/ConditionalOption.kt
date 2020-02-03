package quests.triggerCondition

interface ConditionalOption<T> {
    val option: T
    val condition: Condition
    fun apply(params: Map<String, String>) : T
}
