package core.target.activator

import core.target.Target
import core.utility.NameSearchableList

interface ActivatorParser {
    fun loadActivators(): NameSearchableList<Target>
}