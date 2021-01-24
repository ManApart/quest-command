package core.conditional

class ConditionalStringPointer(val name: String, private val type: ConditionalStringType) {
    constructor(name: String) : this(name, ConditionalStringType.DEFAULT)

    fun getOption() : String {
        return if (type == ConditionalStringType.DEFAULT){
            name
        } else {
            ConditionalManager.getOption(name, type)
        }
    }
}