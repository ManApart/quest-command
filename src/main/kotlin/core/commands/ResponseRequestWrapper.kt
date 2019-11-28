package core.commands

class ResponseRequestWrapper(private val value: String?, val responseRequest: ResponseRequest, private val useDefault: Boolean = false, private val defaultValue: String? = null) {
    constructor(value: Int?, responseRequest: ResponseRequest, useDefault: Boolean = false, defaultValue: Int? = null) : this(value?.toString(), responseRequest, useDefault, defaultValue?.toString())

    override fun toString(): String {
        return value + ":" + responseRequest.message
    }

    fun hasValue(): Boolean {
        return !value.isNullOrBlank() || (useDefault && defaultValue != null)
    }

    fun getValue(): String? {
        return value ?: defaultValue
    }
}