package core.ai

import core.ai.action.AIAction
import core.ai.agenda.Agenda
import core.utility.Named

data class Goal(
    override val name: String,
    val priority: Int,
    val progress: Int,
    val steps: List<AIAction>
) : Named {
    constructor(agenda: Agenda, priority: Int): this(agenda.name, priority, 0, agenda.steps.flatMap { it.actions() })

}

