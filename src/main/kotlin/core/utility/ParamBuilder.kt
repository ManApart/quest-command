package core.utility

interface ParamBuilder {
    val paramsBuilder: MapBuilder
    fun param(vararg values: Pair<String, Any>)
    fun param(key: String, value: String)
    fun param(key: String, value: Int)
}