package core.ai

import core.ai.action.AIAction
import core.conditional.Context
import core.thing.Thing
import core.utility.Named


interface GoalStep {
    fun actions(): List<AIAction>
}

//TODO - combine with AIActionTree (see wiki)

//Goals have steps and sub goals to be done in order
data class Desire(
    override val name: String,
    val criteria: List<(Thing, Context) -> Boolean?>,
    val priority: Int,
    val agenda: Agenda // Can this be a hard reference or does it need to be by string
) : Named

data class Agenda(
    override val name: String,
    val steps: List<GoalStep>
) : Named, GoalStep {
    constructor(name: String, steps: List<AIAction>) : this(name, listOf(Plan(steps)))

    override fun actions(): List<AIAction> {
        return steps.flatMap { it.actions() }
    }
}

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