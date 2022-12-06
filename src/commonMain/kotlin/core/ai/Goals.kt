package core.ai

import core.ai.action.AIAction
import core.conditional.Context
import core.thing.Thing


interface Goal


//Lists of agendas can be created and then a creature can take combine those lists as its set of desires
data class Personality(val desires: List<Agenda>) {
    constructor(vararg desires: List<Agenda>): this(desires.toList().flatten())
}

//High level goal with a list of tasks and sub goals to accomplish
//TODO - combine with AIActionTree
data class Agenda(val criteria: List<(Thing, Context) -> Boolean?>, val steps: List<Goal>) : Goal

data class Plan(val tasks: List<AIAction>): Goal
