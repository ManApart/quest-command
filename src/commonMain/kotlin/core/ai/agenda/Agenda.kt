package core.ai.agenda

import core.ai.action.AIAction
import core.utility.Named

interface GoalStep {
    fun actions(): List<AIAction>
}


data class Agenda(
    override val name: String,
    val steps: List<GoalStep>
) : Named, GoalStep {

    override fun actions(): List<AIAction> {
        return steps.flatMap { it.actions() }
    }
}
fun agenda(name: String, steps: List<AIAction>) = Agenda(name,  listOf(Plan(steps)))

data class Plan(private val steps: List<AIAction>) : GoalStep {
    override fun actions() = steps
}