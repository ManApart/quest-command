package core.conditional

class WeatherStringsGenerated : WeatherStringsCollection {
    override val values: List<ConditionalString> = listOf(resources.weatherOptions.WeatherOptions()).flatMap { it.values }
}