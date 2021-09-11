package traveling.location.weather

import core.DependencyInjector
import core.utility.NameSearchableList
import core.utility.toNameSearchableList

val DEFAULT_WEATHER = Weather("Still", "The air is completely still.")

object WeatherManager {
    private var weather = loadWeather()

    private fun loadWeather(): NameSearchableList<Weather> {
        val collection = DependencyInjector.getImplementation(WeathersCollection::class.java)
        return collection.values.toNameSearchableList()
    }

    fun reset() {
        weather = loadWeather()
    }

    fun weatherExists(name: String): Boolean {
        return weather.exists(name)
    }

    fun getWeather(name: String): Weather {
        return weather.getOrNull(name) ?: DEFAULT_WEATHER
    }

}