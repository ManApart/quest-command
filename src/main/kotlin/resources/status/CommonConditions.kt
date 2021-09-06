package resources.status

import magic.Element
import status.conditions.ConditionRecipe
import status.conditions.ConditionResource
import status.effects.EffectRecipe

class CommonConditions : ConditionResource {
    override val values = listOf(
        ConditionRecipe("Rain Wet", Element.WATER, 1, listOf(EffectRecipe("Wet", 1, 5))),
        )
}