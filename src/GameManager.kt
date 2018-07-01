import processing.MapManager
import processing.SystemManager
import processing.TravelManager

object GameManager {
    val commandParser = CommandParser()
    val travelManager = TravelManager()
    val mapManager = MapManager()
    val systemManager = SystemManager()
}