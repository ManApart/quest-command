package resources.weatherOptions

import traveling.location.weather.WeatherResource
import traveling.location.weather.build
import traveling.location.weather.weathers

class OutsideWeather : WeatherResource {
    override val values = weathers {

        weather("Soft Wind", "A soft wind blows gently against you.")
        weather("Windy", "Wind tugs and pulls at you.")
        weather("Strong Wind", "Wind whips and push, almost as if it is angry with you.")
        weather("Gale", "Mighty blasts of air assail you.")
        weather("Dry Air", "The air feels feels empty and harsh; it lacks all moisture.")
        weather("Damp Air", "The air feels damp and wet.")
        weather("Humid Air", "The air feels thick and it's harder to catch your breath.")
        weather("Light Fog") {
            description("The light fog gives everything a grey hue.")
            light(-1)
        }
        weather("Fog") {
            description("Fog obscures the distance.")
            light(-2)
        }
        weather("Heavy Fog") {
            description("The heavy fog hems you in, obscuring all but a small area around you.")
            light(-3)
        }
        weather("Gentle Rain") {
            description("Fat drops of water lazily splash against everything around you.")
            light(-2)
            condition("Rain Wet")
        }
        weather("Rain") {
            description("Rain falls at a steady, measured clip.")
            light(-2)
        }
        weather("Heavy Rain") {
            description("Rain pelts like an army of small soldiers, charging, flanking, rushing down.")
            light(-4)
        }
        weather("Thunderstorm") {
            description("Rain falls angrily down. Occasionally everything flashes brilliant white as the sky breaks and roars.")
            light(-4)
        }
        weather("Ankle Deep Water", "Your feet are covered in water.")
        weather("Waist Deep Water", "You are in water up to your waist.")
        weather("Submerged") {
            description("You are completely under water.")
            light(-2)
        }
    }.build()
}