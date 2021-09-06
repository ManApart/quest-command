package traveling.location.weather

class WeathersGenerated : WeathersCollection {
    override val values = listOf<WeatherResource>(resources.weatherOptions.InsideWeather(), resources.weatherOptions.OutsideWeather()).flatMap { it.values }
}