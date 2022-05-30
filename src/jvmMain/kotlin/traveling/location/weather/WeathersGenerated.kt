package traveling.location.weather

class WeathersGenerated : WeathersCollection {
    override val values by lazy { listOf<WeatherResource>(resources.weatherOptions.InsideWeather(), resources.weatherOptions.OutsideWeather()).flatMap { it.values }}
}