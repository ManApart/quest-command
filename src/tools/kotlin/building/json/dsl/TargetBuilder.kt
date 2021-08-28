package building.json.dsl

import core.target.Target

class TargetBuilder(private val name: String) {
    private var propsBuilder = PropsBuilder()
    private val params = mutableMapOf<String, Any>()

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun params(values: Map<String, Any>){
        values.entries.forEach { (key, value)-> this.params[key] = value }
    }

    fun params(key: String, value: String){
        params[key] = value
    }

    fun params(key: String, value: Int){
        params[key] = value
    }

    internal fun build(): Target {
        val props = propsBuilder.build()
        return Target(name)
    }
}

fun target(name:String, initializer: TargetBuilder.() -> Unit): Target {
    return TargetBuilder(name).apply(initializer).build()
}