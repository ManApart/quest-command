package combat.block

import combat.DamageType
import core.events.EventListener
import core.properties.DEFENSE
import core.thing.Thing
import explore.listen.addSoundEffect
import traveling.location.location.Location

class Block : EventListener<BlockEvent>() {

    override suspend fun complete(event: BlockEvent) {
        val blockHelper = event.creature.body.blockHelper
        blockHelper.blockBodyPart = event.partThatWillShield
        blockHelper.blockedBodyParts.addAll(getBlockedParts(event))
        event.creature.addSoundEffect("Block", "the tightening of straps and sinew", 1)
    }

    private suspend fun getBlockedParts(event: BlockEvent): List<Location> {
        val shield = getShield(event.partThatWillShield)
        val shieldSize = shield?.properties?.values?.getInt("radius") ?: 0
        val partLocation = event.creature.body.layout.findLocation(event.partThatWillBeShielded.name)
        val locations = listOf(partLocation) + partLocation.getNeighbors().filter { (partLocation.getConnection(it)?.source?.vector?.getDistance() ?: 0) <= shieldSize }

        return locations.map { it.getLocation() }
    }

    private fun getShield(partThatWillShield: Location): Thing? {
        val equippedItems = partThatWillShield.getEquippedItems()
        return equippedItems.firstOrNull { it.properties.tags.has("shield") }
                ?: equippedItems.maxByOrNull { getTotalDefense(it) }
    }

    private fun getTotalDefense(apparel: Thing): Int {
        val values = apparel.properties.values
        return values.getInt(DEFENSE) + DamageType.values().sumOf { values.getInt(it.defense) }
    }

}