package status

import core.events.EventListener
import core.gameState.stat.FOCUS

import core.gameState.stat.HEALTH
import core.gameState.stat.STAMINA
import core.history.display
import core.utility.StringFormatter

class Status : EventListener<StatusEvent>() {

    override fun execute(event: StatusEvent) {
        printImportantStats(event)
        printOtherStats(event)
        printOtherConditions(event)
    }

    private fun printImportantStats(event: StatusEvent) {
        val soul = event.creature.soul
        if (soul.hasStat(HEALTH) || soul.hasStat(STAMINA) || soul.hasStat(FOCUS)) {
            val youHave = StringFormatter.format(event.creature.isPlayer(), "You have", "${event.creature.name} has")
            val youAre = StringFormatter.format(event.creature.isPlayer(), "You are", "${event.creature.name} is")
            display("$youHave ${soul.getCurrent(HEALTH)}/${soul.getTotal(HEALTH)} HP, ${soul.getCurrent(FOCUS)}/${soul.getTotal(FOCUS)} Focus and ${soul.getCurrent(STAMINA)}/${soul.getTotal(STAMINA)} Stamina. $youAre ${event.creature.inventory.getWeight()}/${event.creature.getTotalCapacity()} encumbered.")
        }
    }

    private fun printOtherStats(event: StatusEvent) {
        val soul = event.creature.soul
        val subject = StringFormatter.getSubjectPossessive(event.creature)
        val statString = soul.getStats().asSequence()
                .filter { it != soul.getStatOrNull(HEALTH) && it != soul.getStatOrNull(FOCUS) && it != soul.getStatOrNull(STAMINA) }
                .joinToString("\n\t") {
                    "${it.name.capitalize()}: ${it.current}/${it.max} (${it.getCurrentXP().toInt()}/${it.getNextLevelXP().toInt()}xp)"
                }
        display("$subject stats:\n\t$statString")
    }

    private fun printOtherConditions(event: StatusEvent) {
        val soul = event.creature.soul
        val subject = StringFormatter.getSubjectPossessive(event.creature)
        val conditionString = soul.getConditions().joinToString("\n\t") { condition ->
            "${condition.name} (age: ${condition.getAge()}}):\n\t\t" + condition.getEffects().joinToString("\n\t\t")
        }

        if (soul.getConditions().isNotEmpty()) {
            display("$subject current conditions:\n\t$conditionString")
        }
    }

}