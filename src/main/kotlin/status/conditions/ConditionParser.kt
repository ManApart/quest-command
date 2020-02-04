package status.conditions

import core.utility.NameSearchableList

interface ConditionParser {
    fun loadConditions(): NameSearchableList<ConditionRecipe>
}