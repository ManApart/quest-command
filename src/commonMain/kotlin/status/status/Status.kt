package status.status

import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.history.displayToOthers
import core.properties.ENCUMBRANCE
import core.utility.asSubjectPossessive
import core.utility.capitalize2
import core.utility.then
import status.stat.FOCUS
import status.stat.HEALTH
import status.stat.STAMINA

class Status : EventListener<StatusEvent>() {

    override suspend fun execute(event: StatusEvent) {
        printImportantStats(event)
        printOtherStats(event)
        printOtherConditions(event)
        event.source.displayToOthers("${event.source.name} examines ${event.creature.name}'s stats.")
    }

    private fun printImportantStats(event: StatusEvent) {
        val soul = event.creature.soul
        if (soul.hasStat(HEALTH) || soul.hasStat(STAMINA) || soul.hasStat(FOCUS)) {
            val youHave =
                (event.creature == event.source.thing).then("You (${event.creature.name}) have", "${event.creature.name} has")
            val youAre = (event.creature == event.source.thing).then("You are", "${event.creature.name} is")
            val encumbrancePercent = (event.creature.getEncumbrance() * 100).toInt()
            val additionalEncumbrancePercent = event.creature.properties.values.getInt(ENCUMBRANCE, 0)
            val encumberedStats =
                "${event.creature.inventory.getWeight()}/${event.creature.getTotalCapacity()} + $additionalEncumbrancePercent% additional encumbrance"
            event.source.displayToMe(
                "$youHave ${soul.getCurrent(HEALTH)}/${soul.getTotal(HEALTH)} HP, ${
                    soul.getCurrent(
                        FOCUS
                    )
                }/${soul.getTotal(FOCUS)} Focus and ${soul.getCurrent(STAMINA)}/${soul.getTotal(STAMINA)} Stamina. $youAre $encumbrancePercent% encumbered ($encumberedStats)."
            )
        }
    }

    private suspend fun printOtherStats(event: StatusEvent) {
        val soul = event.creature.soul

        val statString = soul.getStats().asSequence()
            .filter {
                it != soul.getStatOrNull(HEALTH) && it != soul.getStatOrNull(FOCUS) && it != soul.getStatOrNull(
                    STAMINA
                )
            }
            .joinToString("\n\t") {
                "${it.name.capitalize2()}: ${it.current}/${it.max} (${it.xp.toInt()}/${it.getNextLevelXP().toInt()}xp)"
            }
        event.creature.display {
            val subject = event.creature.asSubjectPossessive(it)
            "$subject stats:\n\t$statString"
        }
    }

    private suspend fun printOtherConditions(event: StatusEvent) {
        val soul = event.creature.soul

        val conditionString = soul.getConditions().joinToString("\n\t") { condition ->
            "${condition.name} (age: ${condition.age}}):\n\t\t" + condition.getEffects().joinToString("\n\t\t")
        }

        if (soul.getConditions().isNotEmpty()) {
            event.creature.display {
                val subject = event.creature.asSubjectPossessive(it)
                "$subject current conditions:\n\t$conditionString"
            }
        }
    }
}