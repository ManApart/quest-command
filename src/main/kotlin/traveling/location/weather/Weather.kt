package traveling.location.weather

import core.utility.Named
import dialogue.DialogueOptions

class Weather(override val name: String, val description: String, val conditionNames: List<DialogueOptions> = listOf()) : Named {
}