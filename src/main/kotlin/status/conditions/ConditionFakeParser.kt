package status.conditions

import core.utility.NameSearchableList

class ConditionFakeParser(effects: List<ConditionRecipe> = listOf())  : ConditionParser{
    val effects = NameSearchableList(effects)

    override fun loadConditions(): NameSearchableList<ConditionRecipe> {
        return effects
    }
}