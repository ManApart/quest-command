package use.eat

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
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

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
        val allFood = source.currentLocation().getItemsIncludingPlayerInventory(source).filter { it.properties.tags.has("food") }
        val pickedFood = source.currentLocation().getItemsIncludingPlayerInventory(argsString, source)
        val topChoice = pickedFood.firstOrNull { it.name.lowercase() == argsString }

        when {
            args.isEmpty() -> eatWhat(allFood)
            pickedFood.isEmpty() -> source.displayToMe("Couldn't find $argsString")
            topChoice != null -> eatFood(source, topChoice)
            pickedFood.size > 1 -> eatWhat(pickedFood)
            else -> eatFood(source, pickedFood.first())
        }
    }

    private fun eatWhat(food: List<Thing>) {
        val message = "Eat what?\n\t${food.joinToString(", ")}"
        val response = ResponseRequest(message, food.associate { it.name to "eat ${it.name}" })
         CommandParsers.setResponseRequest(response)
    }

    private fun eatFood(source: Thing, food: Thing) {
        if (food.properties.tags.has("food")) {
            EventManager.postEvent(StartUseEvent(source, food, source))
        } else {
            source.displayToMe("${food.name} is inedible.")
        }
    }

}