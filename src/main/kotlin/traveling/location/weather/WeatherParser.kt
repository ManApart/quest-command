package traveling.location.weather

import core.utility.NameSearchableList

interface WeatherParser {
    fun loadWeather(): NameSearchableList<Weather>
}