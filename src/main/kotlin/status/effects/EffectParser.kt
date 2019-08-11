package status.effects

import core.utility.NameSearchableList

interface EffectParser {
    fun loadEffects(): NameSearchableList<EffectBase>
}