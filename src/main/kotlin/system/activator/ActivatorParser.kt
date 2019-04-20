package system.activator

import core.gameState.Target
import core.utility.NameSearchableList

interface ActivatorParser {
    fun loadActivators(): NameSearchableList<Target>
}