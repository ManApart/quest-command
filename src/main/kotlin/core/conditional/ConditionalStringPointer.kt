package core.conditional

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ConditionalStringPointer(val name: String, val type: ConditionalStringType) {
    constructor(name: String) : this(name, ConditionalStringType.DEFAULT)

    @JsonIgnore
    fun getOption() : String {
        return if (type == ConditionalStringType.DEFAULT){
            name
        } else {
            ConditionalManager.getOption(name, type)
        }
    }
}