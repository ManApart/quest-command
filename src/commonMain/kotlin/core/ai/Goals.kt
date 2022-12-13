package core.ai

import core.ai.action.AIAction
import core.conditional.Context
import core.thing.Thing
import core.utility.Named


interface GoalStep {
    fun actions(): List<AIAction>
}


//Goals have steps and sub goals to be done in order
//TODO - combine with AIActionTree
data class Goal(
    override val name: String,
    val criteria: List<(Thing, Context) -> Boolean?>,
    val priority: Int,
    val steps: List<GoalStep>
) : Named, GoalStep {
    constructor(id: String, criteria: List<(Thing, Context) -> Boolean?>, priority: Int, steps: List<AIAction>) : this(id, criteria, priority, listOf(Plan(steps)))

    override fun actions(): List<AIAction> {
        return steps.flatMap { it.actions() }
    }

}

data class Plan(private val steps: List<AIAction>) : GoalStep {
    override fun actions() = steps
}