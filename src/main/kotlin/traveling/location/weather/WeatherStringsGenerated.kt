package traveling.location.weather

class WeatherStringsGenerated : WeatherStringsCollection {
    override val values = listOf<WeatherStringResource>(resources.weatherOptions.WeatherOptions()).flatMap { it.values }
}