package resources.weatherOptions

import traveling.location.weather.WeatherResource
import traveling.location.weather.build
import traveling.location.weather.weathers

class OutsideWeather : WeatherResource {
    override val values = weathers {

        weather("Clear", "The air is clean and fresh.")
        weather("Soft Wind", "A soft wind blows gently against you.")

        weather("Windy") {
            description("Wind tugs and pulls at you.")
            sound(2, "gusts of wind")
        }

        weather("Strong Wind") {
            description("Wind whips and push, almost as if it is angry with you.")
            sound(5, "the heavy slap of air")
        }

        weather("Gale") {
            description("Mighty blasts of air assail you.")
            sound(9, "the roar of wind")
        }

        weather("Dry Air") {
            description("The air feels feels empty and harsh; it lacks all moisture.")
        }

        weather("Damp Air") {
            description("The air feels damp and wet.")
        }

        weather("Humid Air") {
            description("The air feels thick and it's harder to catch your breath.")
        }

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
            sound(2, "the gentle patter of small splashes")
            condition("Rain Wet")
        }

        weather("Rain") {
            description("Rain falls at a steady, measured clip.")
            sound(4, "the rhythmic splatter of rain")
            light(-2)
        }

        weather("Heavy Rain") {
            description("Rain pelts like an army of small soldiers, charging, flanking, rushing down.")
            sound(6, "the percussion of rain mixed with sloshing, streaming and splashing")
            light(-4)
        }

        weather("Thunderstorm") {
            description("Rain falls angrily down. Occasionally everything flashes brilliant white as the sky breaks and roars.")
            sound(8, "the heavy slap of rain deepened by rolling rumbles and loud claps")
            light(-4)
        }

        weather("Ankle Deep Water" ){
            description("Your feet are covered in water.")
            sound(4, "splashes and gurgles")
        }

        weather("Waist Deep Water" ){
            description("You are in water up to your waist.")
            sound("sloshing and gurgling")
        }

        weather("Submerged") {
            description("You are completely under water.")
            sound(7,"muffled swirling and bubbling")
            light(-2)
        }
        weather("Utter Fog") {
            description("You can't see anything.")
            sound(10,"muffled nothings")
            light(-10)
        }
    }.build()
}