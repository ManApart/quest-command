package resources.ai.desire

import core.ai.desire.Desire
import core.ai.desire.DesireResource

class CommonGoals : DesireResource {
    override val values = listOf(
        Desire("PredatorAttack", listOf({ s, c -> c.thing("target", s) != null }), 10, "FindAndAttack")
    )
}