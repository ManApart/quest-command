package resources.weatherOptions

import traveling.location.weather.WeatherResource
import traveling.location.weather.build
import traveling.location.weather.weathers

class InsideWeather : WeatherResource {
    override val values = weathers {
        weather("Stifled Air", "The air is stale and stifled.")
        weather("Calm", "The air is warm and still.")
        
        weather("Distant Rain" ){
            description("You can hear the rain falling outside.")
            sound(3, "the percussion of rain outside mixed with sloshing, streaming and splashing")
        }

        weather("Distant Storm" ){
            description("You can hear the cracks of thunder outside.")
            sound(4, "the distant and yet heavy slap of rain deepened by rolling rumbles and loud claps")
        }

        weather("Claustrophobic", "The air is tightly packed and feels dense.")
        weather("Dusty", "You can see motes of dust crowding the air.")
        weather("Earthy", "You smell earthy tones and taste grit against your lips.")
        weather("Rocky", "You smell earthy tones and taste grit against your lips.")
        weather("Overgrown", "Spores, pollen and plant life surge into your nostrils.")
    }.build()
}