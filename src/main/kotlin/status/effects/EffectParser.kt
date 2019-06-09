package status.effects

import core.gameState.Effect
import core.utility.NameSearchableList

interface EffectParser {
    fun loadEffects(): NameSearchableList<Effect>
}