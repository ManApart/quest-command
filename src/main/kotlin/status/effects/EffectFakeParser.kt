package status.effects

import core.utility.NameSearchableList

class EffectFakeParser(effects: List<EffectBase> = listOf())  : EffectParser{
    val effects = NameSearchableList(effects)

    override fun loadEffects(): NameSearchableList<EffectBase> {
        return effects
    }
}