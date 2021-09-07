package resources.weatherOptions

import traveling.location.weather.Weather
import traveling.location.weather.WeatherResource

class OutsideWeather : WeatherResource {
    override val values = listOf(
        Weather("Soft Wind", "A soft wind blows gently against you."),
        Weather("Windy", "Wind tugs and pulls at you."),
        Weather("Strong Wind", "Wind whips and push, almost as if it is angry with you."),
        Weather("Gale", "Mighty blasts of air assail you."),
        Weather("Dry Air", "The air feels feels empty and harsh; it lacks all moisture."),
        Weather("Damp Air", "The air feels damp and wet."),
        Weather("Humid Air", "The air feels thick and it's harder to catch your breath."),
        Weather("Light Fog", "The light fog gives everything a grey hue."),
        Weather("Fog", "Fog obscures the distance."),
        Weather("Heavy Fog", "The heavy fog hems you in, obscuring all but a small area around you."),
        Weather("Gentle Rain", "Fat drops of water lazily splash against everything around you.", listOf("Rain Wet")),
        Weather("Rain", "Rain falls at a steady, measured clip."),
        Weather("Heavy Rain", "Rain pelts like an army of small soldiers, charging, flanking, rushing down."),
        Weather("Thunderstorm", "Rain falls angrily down. Occasionally everything flashes brilliant white as the sky breaks and roars."),
        Weather("Ankle Deep Water", "Your feet are covered in water."),
        Weather("Waist Deep Water", "You are in water up to your waist."),
        Weather("Submerged", "You are completely under water."),
    )
}