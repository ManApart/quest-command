package inventory

import core.events.EventListener
import core.history.displayToMe
import core.properties.TagStrings.CONTAINER
import core.thing.Thing

class ViewInventoryFit : EventListener<ViewInventoryFitEvent>() {
    override suspend fun complete(event: ViewInventoryFitEvent) {
        getAllContainers(event.taker)
            .joinToString("\n\t") { explainContainer(it, event.item) }
            .let { event.source.displayToMe("Fit results:\n\t$it") }
    }

    private fun getAllContainers(taker: Thing): List<Thing> {
        return listOf(taker) + taker.inventory.getAllItems().filter { it.hasTag(CONTAINER) }.flatMap { getAllContainers(it) }
    }

    private fun explainContainer(taker: Thing, item: Thing): String {
        val reason = taker.inventory.hasRoomForExplained(taker, item)
        val t = taker.name
        val i = item.name
        return when (reason) {
            FitReason.FITS -> "$i fits in $t."
            FitReason.NO_CAPACITY -> "$t can't hold any more items (${taker.inventory.getUsedCapacity()}/${taker.getCapacity()})."
            FitReason.TAG_TOO_SMALL -> "$i's tagged size is too large for $t. (${item.properties.tags.getSizeTag()}/${taker.properties.tags.getSizeTag()}"
            FitReason.BODY_TOO_SMALL -> "$i doesn't fit within $t's dimensions (${item.getDimensions()}/${taker.getDimensions()})."
        }
    }

}
