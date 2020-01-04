package dialogue

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import core.utility.apply

@JsonIgnoreProperties(ignoreUnknown = true)
class DialogueOptions(val options: List<DialogueOption> = listOf(), val params: Map<String, String> = mapOf()) {
    constructor(defaultOption: String) : this(listOf(DialogueOption(defaultOption)))

    fun apply(params: Map<String, String>) : DialogueOptions {
        return DialogueOptions(options, params)
    }

    @JsonIgnore
    fun getDialogue(): String {
        return options.firstOrNull { it.condition.matches(params) }?.response?.apply(params) ?: ""
    }
}