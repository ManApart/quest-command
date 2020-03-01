package traveling.location.weather

import core.properties.Properties
import core.utility.Named
import dialogue.DialogueOptions

class Weather(override val name: String, val description: String, val conditionNames: List<DialogueOptions> = listOf(), val properties: Properties = Properties()) : Named {
}