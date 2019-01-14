package dialogue

import core.utility.apply

class DialogueOptions(private val options: List<DialogueOption> = listOf(), private val params: Map<String, String> = mapOf()) {
    constructor(defaultOption: String) : this(listOf(DialogueOption(defaultOption)))

    fun apply(params: Map<String, String>) : DialogueOptions {
        return DialogueOptions(options, params)
    }

    fun getDialogue(): String {
        return options.firstOrNull { it.condition.matches(params) }?.response?.apply(params) ?: ""
    }
}