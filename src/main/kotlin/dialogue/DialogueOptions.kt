package dialogue

import quests.triggerCondition.Conditional

class DialogueOptions(options: List<DialogueOption> = listOf(), params: Map<String, String> = mapOf()) : Conditional<DialogueOption, String>(options, params) {
    constructor(defaultOption: String) : this(listOf(DialogueOption(defaultOption)))

    override fun apply(params: Map<String, String>) : DialogueOptions {
        return DialogueOptions(options, params)
    }
}