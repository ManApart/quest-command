package status.conditions

import core.utility.NameSearchableList
import core.utility.Named
import magic.Element
import status.effects.EffectRecipe

class ConditionRecipe(
        override val name: String,
        val element: Element = Element.NONE,
        var elementStrength: Int = 1,
        effects: List<EffectRecipe> = listOf(),
        criticalEffects: List<EffectRecipe> = listOf(),
        val permanent: Boolean = false
) : Named {
    val effects = NameSearchableList(effects)
    val criticalEffects = NameSearchableList(criticalEffects)
}