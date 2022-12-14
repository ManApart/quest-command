package core.ai

import core.ai.action.AIAction
import core.ai.desire.Desire
import core.conditional.Context
import core.thing.Thing
import core.utility.Named


interface GoalStep {
    fun actions(): List<AIAction>
}

//TODO - combine with AIActionTree (see wiki)

//Desires have steps and sub goals to be done in order


data class Agenda(
    override val name: String,
    val steps: List<GoalStep>
) : Named, GoalStep {

    override fun actions(): List<AIAction> {
        return steps.flatMap { it.actions() }
    }
}
fun agenda(name: String, steps: List<AIAction>) = Agenda(name,  listOf(Plan(steps)))

data class Goal(
    override val name: String,
    val priority: Int,
    val progress: Int,
    val steps: List<AIAction>
) : Named {
    constructor(desire: Desire, agenda: Agenda): this(desire.name, desire.priority, 0, agenda.steps.flatMap { it.actions() })

}

data class Plan(private val steps: List<AIAction>) : GoalStep {
    override fun actions() = steps
}