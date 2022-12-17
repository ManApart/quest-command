package core.ai.agenda

import core.ai.AIManager
import core.ai.action.AIAction2
import core.utility.Named

data class GoalStep(val agendaName: String?, val step: AIAction2?) {
    constructor(agendaName: String) : this(agendaName, null)
    constructor(step: AIAction2) : this(null, step)

    fun getActions(): List<AIAction2> {
        return step?.let { listOf(it) } ?: AIManager.agendas[agendaName]!!.getActions()
    }
}

data class Agenda(
    override val name: String,
    val steps: List<GoalStep>
) : Named {
    fun getActions(): List<AIAction2> {
        return steps.flatMap { it.getActions() }
    }
}
