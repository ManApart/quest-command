package traveling.location.weather

import core.properties.Properties
import core.utility.Named

class Weather(override val name: String, val description: String, val conditionNames: List<String> = listOf(), val properties: Properties = Properties()) : Named