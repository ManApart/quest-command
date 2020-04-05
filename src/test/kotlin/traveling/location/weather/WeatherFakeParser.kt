package traveling.location.weather

import core.utility.NameSearchableList
import core.utility.toNameSearchableList

class WeatherFakeParser(weathers: List<Weather> = listOf()) : WeatherParser {
    private val weathers = weathers.toNameSearchableList()

    override fun loadWeather(): NameSearchableList<Weather> {
        return weathers
    }
}