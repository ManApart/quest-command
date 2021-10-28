package resources.status

import magic.Element
import status.conditions.ConditionResource
import status.conditions.conditions
import status.effects.EffectRecipe

class CommonConditions : ConditionResource {
    override val values = conditions {
        conditions(Element.WATER) {
            condition("Rain Wet") {
                elementStrength(1)
                effect(EffectRecipe("Wet", 1, 5))
            }
        }
    }
}