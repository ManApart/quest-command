package resources.weatherOptions

import core.conditional.ConditionalString
import core.conditional.StringOption
import core.utility.RandomManager
import traveling.location.weather.WeatherStringResource

class WeatherOptions : WeatherStringResource {
    override val values: List<ConditionalString> = listOf(
            ConditionalString("Inside Weather", listOf(
                    StringOption("Distant Rain") { RandomManager.isSuccess(40) },
                    StringOption("Distant Storm") { RandomManager.isSuccess(10) },
                    StringOption("Calm") { true },
            )),

            ConditionalString("Outside Weather", listOf(
                    StringOption("Windy") { RandomManager.isSuccess(20) },
                    StringOption("Strong Wind") { RandomManager.isSuccess(10) },
                    StringOption("Gale") { RandomManager.isSuccess(5) },
                    StringOption("Light Fog") { RandomManager.isSuccess(20) },
                    StringOption("Fog") { RandomManager.isSuccess(5) },
                    StringOption("Rain") { RandomManager.isSuccess(20) },
                    StringOption("Heavy Rain") { RandomManager.isSuccess(30) },
                    StringOption("Thunderstorm") { RandomManager.isSuccess(5) },
                    StringOption("Soft Wind") { true },
            )),

    )

}