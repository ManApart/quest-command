package core.ai.desire

import core.conditional.Context
import core.thing.Thing
import core.utility.Named

data class Desire(
    override val name: String,
    val criteria: List<(Thing, Context) -> Boolean?>,
    val priority: Int,
    val agenda: String
) : Named