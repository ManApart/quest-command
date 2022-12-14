package core.ai

import core.ai.action.AIAction
import core.ai.agenda.Agenda
import core.ai.desire.Desire
import core.utility.Named

data class Goal(
    override val name: String,
    val priority: Int,
    val progress: Int,
    val steps: List<AIAction>
) : Named {
    constructor(desire: Desire, agenda: Agenda): this(desire.name, desire.priority, 0, agenda.steps.flatMap { it.actions() })

}

