package status.conditions
import status.conditions.ConditionRecipe

interface ConditionsCollection {
    val values: List<ConditionRecipe>
}