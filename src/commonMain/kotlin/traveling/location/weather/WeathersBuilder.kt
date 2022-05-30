package traveling.location.weather

class WeathersBuilder {
    internal val children = mutableListOf<WeatherBuilder>()

    fun weather(item: WeatherBuilder) {
        children.add(item)
    }

    fun weather(name: String, description: String, initializer: WeatherBuilder.() -> Unit = {}) {
        children.add(WeatherBuilder(name).apply { description(description) }.apply(initializer))
    }

    fun weather(name: String, initializer: WeatherBuilder.() -> Unit = {}) {
        children.add(WeatherBuilder(name).apply(initializer))
    }
}

fun weathers(initializer: WeathersBuilder.() -> Unit): List<WeatherBuilder> {
    return WeathersBuilder().apply(initializer).children
}

fun List<WeatherBuilder>.build(): List<Weather> {
    return map { it.build() }
}