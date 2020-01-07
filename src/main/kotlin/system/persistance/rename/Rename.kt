package system.persistance.rename

import core.events.EventListener
import core.history.display

class Rename : EventListener<RenameEvent>() {
    override fun execute(event: RenameEvent) {
        val oldName = event.target.getDisplayName()
        event.target.givenName = event.newName
        display(oldName + " renamed to " + event.target.getDisplayName())
    }
}