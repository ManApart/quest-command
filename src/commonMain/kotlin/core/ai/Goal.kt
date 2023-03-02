package core.ai

import core.GameState
import core.ai.action.AIAction
import core.ai.agenda.Agenda
import core.events.EventManager
import core.history.displayGlobal
import core.thing.Thing
import core.utility.Named
import system.debug.DebugType
import kotlin.math.max

data class Goal(
    override val name: String,
    val priority: Int,
    var progress: Int,
    val steps: List<AIAction>,
) : Named {
    constructor(agenda: Agenda, priority: Int) : this(agenda.name, priority, 0, agenda.steps.flatMap { it.getActions() })

    private var aborted = false

    suspend fun step(owner: Thing) {
        val step = steps[progress]
        if (step.shouldSkip(owner) != true) {
            val events = try {
                step.createEvents(owner)
            } catch (e: Exception) {
                println("Failed to create event actions for ${owner.name}. This shouldn't happen!")
                println(e.message)
                e.printStackTrace()
                null
            }
            if (events == null) {
                aborted = true
            }
            if (GameState.getDebugBoolean(DebugType.AI_UPDATES)) displayGlobal("${owner.name} does ${step.name}, producing events ${events?.joinToString { it.toString() }}.")
            events?.forEach { EventManager.postEvent(it) }
        }
        progress++
    }

    fun canContinue(): Boolean {
        return !aborted && progress < steps.size
    }
}

