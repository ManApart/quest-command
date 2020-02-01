package traveling.location.weather

import core.DependencyInjector

val DEFAULT_WEATHER = Weather("Still", "The air is completely still.")

object WeatherManager {
    private var parser = DependencyInjector.getImplementation(WeatherParser::class.java)
    private var weather = parser.loadWeather()

    fun reset() {
        parser = DependencyInjector.getImplementation(WeatherParser::class.java)
        weather = parser.loadWeather()
    }

    fun weatherExists(name: String): Boolean {
        return weather.exists(name)
    }

    fun getWeather(name: String): Weather {
        return weather.getOrNull(name) ?: DEFAULT_WEATHER
    }

}