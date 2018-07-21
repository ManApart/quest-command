package status

import core.commands.Command
import core.gameState.GameState

class StatusCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Status")
    }

    override fun getDescription(): String {
        return "Status:\n\tGet information about your or something else's status"
    }

    override fun getManual(): String {
        return "\n\tStatus - Get your current status" +
                "\n\tStatus <target> - Get the status of a target."
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override fun execute(keyword: String, args: List<String>) {
        getPlayerStatus()
    }

    private fun getPlayerStatus() {
        val soul = GameState.player.creature.soul
        println("You have ${soul.getCurrent("Health")}/${soul.getTotal("Health")} HP and ${soul.getCurrent("Stamina")}/${soul.getTotal("Stamina")} SP.")
    }
}