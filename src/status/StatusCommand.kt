package status

import core.commands.Command
import core.gameState.GameState
import core.gameState.Stat

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

    override fun execute(args: List<String>) {
        getPlayerStatus()
    }

    private fun getPlayerStatus() {
        val soul = GameState.player.soul
        println("You have ${soul.getCurrent(Stat.StatType.HEALTH)}/${soul.getTotal(Stat.StatType.HEALTH)} HP and ${soul.getCurrent(Stat.StatType.STAMINA)}/${soul.getTotal(Stat.StatType.STAMINA)} SP.")
    }
}