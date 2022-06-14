package traveling.location.weather

import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList

val DEFAULT_WEATHER = Weather("Still", "The air is completely still.")

object WeatherManager {
    private var weather by lazyM { loadWeather() }

    private fun loadWeather(): NameSearchableList<Weather> {
        startupLog("Loading Weather")
        val collection = DependencyInjector.getImplementation(WeathersCollection::class)
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

    fun getAllWeather(): List<Weather>{
        return weather.toList()
    }

}