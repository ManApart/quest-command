package status.status

import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.history.displayToOthers
import core.properties.ENCUMBRANCE
import core.utility.asSubjectPossessive
import core.utility.capitalize2
import core.utility.then
import status.stat.Attributes.FOCUS
import status.stat.Attributes.HEALTH
import status.stat.Attributes.STAMINA

class Status : EventListener<StatusEvent>() {

    override suspend fun complete(event: StatusEvent) {
        printImportantStats(event)
        printOtherStats(event)
        printOtherConditions(event)
        event.source.displayToOthers("${event.source.name} examines ${event.examined.name}'s stats.")
    }

    private suspend fun printImportantStats(event: StatusEvent) {
        val soul = event.examined.soul
        if (soul.hasStat(HEALTH) || soul.hasStat(STAMINA) || soul.hasStat(FOCUS)) {
            val youHave =
                (event.examined == event.source.thing).then("You (${event.examined.name}) have", "${event.examined.name} has")
            val youAre = (event.examined == event.source.thing).then("You are", "${event.examined.name} is")
            val encumbrancePercent = (event.examined.getEncumbrance() * 100).toInt()
            val additionalEncumbrancePercent = event.examined.properties.values.getInt(ENCUMBRANCE, 0)
            val encumberedStats =
                "${event.examined.inventory.getWeight()}/${event.examined.getTotalCapacity()} + $additionalEncumbrancePercent% additional encumbrance"
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
        val soul = event.examined.soul

        val statString = soul.getStats().asSequence()
            .filter {
                it != soul.getStatOrNull(HEALTH) && it != soul.getStatOrNull(FOCUS) && it != soul.getStatOrNull(
                    STAMINA
                )
            }
            .joinToString("\n\t") {
                "${it.name.capitalize2()}: ${it.current}/${it.max} (${it.xp.toInt()}/${it.getNextLevelXP().toInt()}xp)"
            }
        event.examined.display {
            val subject = event.examined.asSubjectPossessive(it)
            "$subject stats:\n\t$statString"
        }
    }

    private suspend fun printOtherConditions(event: StatusEvent) {
        val soul = event.examined.soul

        val conditionString = soul.getConditions().joinToString("\n\t") { condition ->
            "${condition.name} (age: ${condition.age}}):\n\t\t" + condition.getEffects().joinToString("\n\t\t")
        }

        if (soul.getConditions().isNotEmpty()) {
            event.examined.display {
                val subject = event.examined.asSubjectPossessive(it)
                "$subject current conditions:\n\t$conditionString"
            }
        }
    }
}