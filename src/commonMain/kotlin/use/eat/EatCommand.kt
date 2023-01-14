package use.eat

import core.Player
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import traveling.location.weather.WeatherManager
import use.StartUseEvent

class EatCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Eat")
    }

    override fun getDescription(): String {
        return "Eat an item"
    }

    override fun getManual(): String {
        return """
	Eat <item> - Eat an item"""
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> source.inventory.getAllItems().map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
        val allFood = source.thing.currentLocation().getItemsIncludingPlayerInventory(source.thing).filter { it.properties.tags.has("food") }
        val pickedFood = source.thing.currentLocation().getItemsIncludingPlayerInventory(argsString, source.thing)
        val topChoice = pickedFood.firstOrNull { it.name.lowercase() == argsString }

        when {
            args.isEmpty() -> eatWhat(source, allFood)
            pickedFood.isEmpty() -> source.displayToMe("Couldn't find $argsString")
            topChoice != null -> eatFood(source.thing, topChoice)
            pickedFood.size > 1 -> eatWhat(source, pickedFood)
            else -> eatFood(source.thing, pickedFood.first())
        }
    }

    private fun eatWhat(source: Player, food: List<Thing>) {
        source.respond("There is nothing you can eat.") {
            message("Eat what?")
            optionsNamed(food)
            command { "eat $it" }
        }
    }

    private fun eatFood(source: Thing, food: Thing) {
        if (food.properties.tags.has("food")) {
            EventManager.postEvent(StartUseEvent(source, food, source))
        } else {
            source.displayToMe("${food.name} is inedible.")
        }
    }

}