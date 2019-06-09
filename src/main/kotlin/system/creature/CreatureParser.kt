package system.creature

import core.gameState.Target

interface CreatureParser {
    fun loadCreatures(): List<Target>
}