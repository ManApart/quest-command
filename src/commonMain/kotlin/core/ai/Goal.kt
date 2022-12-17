package core.ai

import core.ai.action.AIAction
import core.ai.agenda.Agenda
import core.conditional.Context
import core.events.EventManager
import core.thing.Thing
import core.utility.Named

data class Goal(
    override val name: String,
    val priority: Int,
    private var progress: Int,
    val steps: List<AIAction>,
    val context: Context = Context(),
) : Named {
    constructor(agenda: Agenda, priority: Int) : this(agenda.name, priority, 0, agenda.steps.flatMap { it.getActions() })

    private var aborted = false

    fun step(owner: Thing) {
        val step = steps[progress]
        val events = try {
            step.createEvents(owner, context)
        } catch (e: Exception) {
            println("Failed to create event actions for ${owner.name}. This shouldn't happen!")
            println(e.message)
            e.printStackTrace()
            null
        }
        if (events == null && !step.optional) {
            aborted = true
        }
        events?.forEach { EventManager.postEvent(it) }
        progress++
    }

    fun canContinue(): Boolean {
        return !aborted && progress < steps.size
    }
}

