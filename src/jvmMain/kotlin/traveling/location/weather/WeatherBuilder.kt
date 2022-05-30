package traveling.location.weather

import core.properties.Properties
import core.properties.PropsBuilder
import explore.listen.SOUND_DESCRIPTION
import explore.listen.SOUND_LEVEL
import explore.listen.SOUND_LEVEL_DEFAULT
import traveling.scope.LIGHT

class WeatherBuilder(private val name: String) {
    private var description: String = ""
    private var propsBuilder = PropsBuilder()
    private var conditionNames = mutableListOf<String>()

    fun build(): Weather {
        val props = propsBuilder.build()
        return Weather(name, description, conditionNames.toList(), props)
    }

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun props(properties: Properties) {
        propsBuilder.props(properties)
    }

    fun condition(vararg conditionName: String) {
        conditionNames.addAll(conditionName)
    }

    fun description(description: String) {
        this.description = description
    }

    fun light(level: Int) {
        propsBuilder.value(LIGHT, level)
    }

    fun sound(description: String) {
        sound(SOUND_LEVEL_DEFAULT, description)
    }

    fun sound(level: Int, description: String) {
        propsBuilder.value(SOUND_DESCRIPTION, description)
        propsBuilder.value(SOUND_LEVEL, level)
    }

}

fun weather(name: String, description: String, initializer: WeatherBuilder.() -> Unit): WeatherBuilder {
    return WeatherBuilder(name).apply { description(description) }.apply(initializer)
}