package system

import core.target.Target
import core.utility.NameSearchableList
import core.target.activator.ActivatorParser

class ActivatorFakeParser(activators: NameSearchableList<Target> = NameSearchableList()) : ActivatorParser {
    private val activators: NameSearchableList<Target> = activators.onEach { it.properties.tags.add("Activator") }
    override fun loadActivators(): NameSearchableList<Target> {
        return activators
    }

}