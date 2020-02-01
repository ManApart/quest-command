package traveling.location.weather

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class WeatherJsonParser : WeatherParser {
    private fun parseWeather(path: String): List<Weather> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))
    override fun loadWeather(): NameSearchableList<Weather> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/location/weather", ::parseWeather))
    }
}