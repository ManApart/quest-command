package status

import core.events.EventListener
import core.gameState.isPlayer
import core.gameState.stat.HEALTH
import core.gameState.stat.STAMINA
import core.history.display
import core.utility.StringFormatter

class Status : EventListener<StatusEvent>() {

    override fun execute(event: StatusEvent) {
        printImportantStats(event)
        printOtherStats(event)
    }

    private fun printImportantStats(event: StatusEvent) {
        val soul = event.creature.soul
        if (soul.hasStat("Health") || soul.hasStat("Stamina")) {
            val youHave = StringFormatter.format(event.creature.isPlayer(), "You have", "${event.creature.name} has")
            val youAre= StringFormatter.format(event.creature.isPlayer(), "You are", "${event.creature.name} is")
            display("$youHave ${soul.getCurrent("Health")}/${soul.getTotal("Health")} HP and ${soul.getCurrent("Stamina")}/${soul.getTotal("Stamina")} Stamina. $youAre ${event.creature.inventory.getWeight()}/${event.creature.getTotalCapacity()} encumbered.")
        }
    }

    private fun printOtherStats(event: StatusEvent) {
        val soul = event.creature.soul
        val subject = StringFormatter.getSubjectPossessive(event.creature)
        val statString = soul.getStats().asSequence().filter { it != soul.getStatOrNull(HEALTH) && it != soul.getStatOrNull(STAMINA) }.joinToString("\n\t") {
            "${it.name.capitalize()}: ${it.current}/${it.max} (${it.getCurrentXP().toInt()}/${it.getNextLevelXP().toInt()}xp)"
        }
        display("$subject stats are:\n\t$statString")
    }

}