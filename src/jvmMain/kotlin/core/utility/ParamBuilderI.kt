package core.utility

class ParamBuilderI : ParamBuilder{
    override val paramsBuilder = MapBuilder()
    override fun param(vararg values: Pair<String, Any>) = this.paramsBuilder.entry(values.toList())
    override fun param(key: String, value: String) = paramsBuilder.entry(key, value)
    override fun param(key: String, value: Int) = paramsBuilder.entry(key, value)
}