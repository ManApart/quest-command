package system.persistance.rename

import core.events.Event
import core.target.Target

class RenameEvent(val target: Target, val newName: String) : Event