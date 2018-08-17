package crafting

import core.commands.Command

class CraftRecipeCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Craft", "Make", "Build")
    }

    override fun getDescription(): String {
        return "Craft:\n\tCraft a recipe you know"
    }

    override fun getManual(): String {
        return "\n\tCraft - View the Recipes that you know. X" +
                "\n\tCraft <Recipe> - Craft a recipe. X"
    }

    override fun getCategory(): List<String> {
        return listOf("Craft")
    }

    override fun execute(keyword: String, args: List<String>) {
        println("You don't know any recipes yet.")
//        when {
//            args.isEmpty() -> EventManager.postEvent(LookEvent())
//            ScopeManager.targetExists(args) -> EventManager.postEvent(LookEvent(ScopeManager.getTarget(args)))
//            else -> println("Couldn't find ${args.joinToString(" ")}.")
//        }
    }

}