package system

import core.gameState.Target
import core.utility.NameSearchableList
import system.activator.ActivatorParser

class ActivatorFakeParser(activators: NameSearchableList<Target> = NameSearchableList()) : ActivatorParser {
    private val activators: NameSearchableList<Target> = activators.onEach { it.properties.tags.add("Activator") }
    override fun loadActivators(): NameSearchableList<Target> {
        return activators
    }

}