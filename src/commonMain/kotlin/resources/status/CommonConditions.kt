package resources.status

import magic.Element
import status.conditions.ConditionResource
import status.conditions.conditions

class CommonConditions : ConditionResource {
    override val values = conditions {
        conditions(Element.WATER) {
            condition("Rain Wet") {
                elementStrength(1)
                effect("Wet", 1, 5)
            }
        }
    }
}