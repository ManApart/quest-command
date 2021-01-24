package traveling.location.weather
import core.conditional.ConditionalString

class WeatherStringsGenerated : WeatherStringsCollection {
    override val values: List<ConditionalString> = listOf(resources.weatherOptions.WeatherOptions()).flatMap { it.values }
}