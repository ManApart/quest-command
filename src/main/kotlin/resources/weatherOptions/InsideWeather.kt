package resources.weatherOptions

import traveling.location.weather.WeatherResource
import traveling.location.weather.build
import traveling.location.weather.weathers

class InsideWeather : WeatherResource {
    override val values = weathers {
        weather("Stifled Air", "The air is stale and stifled.")
        weather("Calm", "The air is warm and still.")
        weather("Distant Rain", "You can hear the rain falling outside.")
        weather("Distant Storm", "You can hear the cracks of thunder outside.")
        weather("Claustrophobic", "The air is tightly packed and feels dense.")
        weather("Dusty", "You can see motes of dust crowding the air.")
        weather("Earthy", "You smell earthy tones and taste grit against your lips.")
        weather("Rocky", "You smell earthy tones and taste grit against your lips.")
        weather("Overgrown", "Spores, pollen and plant life surge into your nostrils.")
    }.build()
}