package resources.weatherOptions

import traveling.location.weather.Weather
import traveling.location.weather.WeatherResource

class InsideWeather : WeatherResource {
    override val values = listOf(
        Weather("Stifled Air", "The air is stale and stifled."),
        Weather("Calm", "The air is warm and still."),
        Weather("Distant Rain", "You can hear the rain falling outside."),
        Weather("Distant Storm", "You can hear the cracks of thunder outside."),
        Weather("Claustrophobic", "The air is tightly packed and feels dense."),
        Weather("Dusty", "You can see motes of dust crowding the air."),
        Weather("Earthy", "You smell earthy tones and taste grit against your lips."),
        Weather("Rocky", "You smell earthy tones and taste grit against your lips."),
        Weather("Overgrown", "Spores, pollen and plant life surge into your nostrils."),
    )
}