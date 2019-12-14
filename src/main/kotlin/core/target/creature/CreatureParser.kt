package core.target.creature

import core.target.Target

interface CreatureParser {
    fun loadCreatures(): List<Target>
}