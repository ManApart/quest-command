package interact.magic

import status.effects.Condition

class Spell(
        val name: String,
        val condition: Condition,
        val cost: Int,
        val statRequired: String,
        val levelRequired: Int,
        val range: Int =1,
        val castTime: Int = cost,
        val isHostile: Boolean = true
)