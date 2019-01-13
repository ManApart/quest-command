package system.activator

import core.gameState.Activator
import core.gameState.Item
import core.utility.NameSearchableList

interface ActivatorParser {
    fun loadActivators(): NameSearchableList<Activator>
}