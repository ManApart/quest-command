package inventory

import core.events.EventListener
import core.history.displayToMe
import core.history.displayToOthers
import core.utility.asSubject
import core.utility.asSubjectPossessive
import core.utility.ifYouWord
import core.utility.joinToStringSuspend

class ViewEquipped : EventListener<ViewEquippedEvent>() {

    override suspend fun complete(event: ViewEquippedEvent) {

        with(event) {
            val body = target.body
            val items = body.getEquippedItems()
            val subject = target.asSubject(source)
            if (items.isEmpty()) {
                val doesNot = target.ifYouWord(source, "don't", "doesn't")
                source.displayToMe("$subject $doesNot have anything equipped!")
            } else {
                val itemList = items.joinToStringSuspend("\n\t") { "${it.name} equipped to ${it.getEquippedSlot(body).description}" }
                val has = target.ifYouWord(source, "have", "has")
                source.displayToMe("$subject $has the following items equipped:\n\t$itemList")
            }
            source.displayToOthers{"${source.name} looks at ${target.asSubjectPossessive(it)} gear."}
        }
    }

}