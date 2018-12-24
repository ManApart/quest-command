package system

import core.gameState.Activator
import core.utility.NameSearchableList

class ActivatorFakeParser(private val activators: NameSearchableList<Activator> = NameSearchableList()) : ActivatorParser {
    override fun loadActivators(): NameSearchableList<Activator> {
        return activators
    }

}