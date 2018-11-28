package status.journal

import core.events.Event
import core.gameState.Creature

class ViewJournalEvent(val justActive: Boolean = false) : Event