package interact.magic

import core.events.Event
import core.gameState.Target
import interact.magic.spells.Spell

class CastSpellEvent(val source: Target, val target: Target, val spell: Spell) : Event