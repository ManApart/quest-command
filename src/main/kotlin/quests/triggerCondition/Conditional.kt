package quests.triggerCondition

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class Conditional<ParametrizedOption : ConditionalOption<ResultType>, ResultType>(val options: List<ParametrizedOption> = listOf(), val params: Map<String, String> = mapOf()) {
    constructor(defaultOption: ParametrizedOption) : this(listOf(defaultOption))
    constructor(defaultOption: ResultType) : this() {
        throw IllegalArgumentException("Conditional was given a string. You probably need to surround the string with something like {option: <string>}: $defaultOption")
    }

    open fun apply(params: Map<String, String>) : Conditional<ParametrizedOption, ResultType> {
        return Conditional(options, params)
    }

    @JsonIgnore
    fun getOption(): ResultType? {
        return options.firstOrNull { it.condition(params) }?.apply(params)
    }

}
