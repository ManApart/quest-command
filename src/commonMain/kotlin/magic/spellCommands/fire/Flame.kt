package magic.spellCommands.fire

import core.Player
import core.commands.Args
import core.commands.clarify
import core.events.EventManager
import core.thing.Thing
import magic.Element
import magic.castSpell.CastSpellEvent
import magic.castSpell.getThingedPartsOrAll
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import status.stat.FIRE_MAGIC
import traveling.position.Distances
import traveling.position.ThingAim
import traveling.position.toCommandString
import traveling.scope.LIT_LIGHT
import kotlin.math.max

class Flame : SpellCommand() {
    override val name = "Flame"

    override fun getDescription(): String {
        return "High damage short range attack that leaves the thing burning."
    }

    override fun getManual(): String {
        return """
	Cast Flame <power> on *<things> - High damage short range attack that leaves the thing burning. Burns caster a proportional amount (minus immunity)."""
    }

    override fun getCategory(): List<String> {
        return listOf("Fire")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("1", "5", "10")
            args.size == 1 && args.last().toIntOrNull() != null -> listOf("on")
            args.last() == "on" -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        val initialPower = args.getBaseNumber()

        val clarifier = source.clarify {
            respond("power") {
                message("Cast Flame with what power?")
                options("1", "3", "5", "10", "50", "#")
                command { "cast flame $it on ${things.toCommandString()}" }
                value(initialPower)
                defaultValue(1)
            }
        }

        if (!clarifier.hasAllValues()) {
            clarifier.requestAResponse()
        } else {
            val damageAmount = clarifier.getIntValue("power")
            val cost = damageAmount / 2
            val totalCost = cost * things.count()
            val levelRequirement = damageAmount / 2

            executeWithWarns(source, FIRE_MAGIC, levelRequirement, totalCost, things) {
                EventManager.postEvent(postSpell(source.thing, ThingAim(source.thing, source.body.getParts()), max(1, damageAmount / 3), 0, levelRequirement, cost))

                //Only the first effect has a cast time, the rest are applied immediately after the first one
                things.forEach { thing ->
                    EventManager.postEvent(postSpell(source.thing, thing, damageAmount, cost, levelRequirement, 0))
                }
            }
        }
    }

    private suspend fun postSpell(source: Thing, thing: ThingAim, damageAmount: Int, cost: Int, levelRequirement: Int, castTime: Int): CastSpellEvent {
        val parts = getThingedPartsOrAll(thing, 3)
        val litLevel = max(damageAmount, thing.thing.properties.values.getInt(LIT_LIGHT, 1))
        val effects = listOf(
            EffectManager.getEffect("Burning", damageAmount, 3, parts),
            EffectManager.getEffect("On Fire", damageAmount, 3, parts),
            EffectManager.getEffect("Lit", litLevel, 3, parts),
        )

        val condition = Condition("Fire Blasted", Element.FIRE, cost, effects)
        val spell = Spell("Flame", condition, cost, FIRE_MAGIC, levelRequirement, range = Distances.SPEAR_RANGE, castTime = castTime)
        return CastSpellEvent(source, thing, spell)
    }

}