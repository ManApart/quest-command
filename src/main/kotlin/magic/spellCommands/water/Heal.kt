package magic.spellCommands.water

import core.Player
import core.commands.Args
import core.commands.responseHelper
import core.events.EventManager
import magic.Element
import magic.castSpell.StartCastSpellEvent
import magic.castSpell.getThingedPartsOrRootPart
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import status.stat.WATER_MAGIC
import traveling.position.Distances
import traveling.position.ThingAim
import traveling.position.toCommandString

class Heal : SpellCommand() {
    override val name = "Heal"

    override fun getDescription(): String {
        return "Heal yourself or others."
    }

    override fun getManual(): String {
        return """
	Cast Heal <amount> for <duration> on *<things> - Heals damage taken over time."""
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override fun execute(source: Player, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialAmount = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")
        val options = listOf("1", "3", "5", "10", "50", "#")

        val responseHelper = source.responseHelper {
            respond("amount"){
                message("Heal how much?")
                options(options)
                command { "cast heal $it for ${initialDuration.toString()} on ${things.toCommandString()}"  }
                value(initialAmount)
                defaultValue(5)
            }
            respond("duration"){
                message("Heal for how long?")
                options(options)
                command {"cast heal ${initialAmount.toString()} for $it on ${things.toCommandString()}" }
                value(initialDuration)
                defaultValue(1)
            }
        }

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val amount = responseHelper.getIntValue("amount")
            val duration = responseHelper.getIntValue("duration")
            val hitCount = things.count()
            val totalCost = amount * hitCount
            val levelRequirement = amount * 2 + duration / 2

            executeWithWarns(source, WATER_MAGIC, levelRequirement, totalCost, things) {
                things.forEach { thing ->
                    val parts = getThingedPartsOrRootPart(thing)
                    val effects = listOf(
                            EffectManager.getEffect("Heal", amount, duration, parts),
                            EffectManager.getEffect("Wet", 0, duration + 1, parts)
                    )

                    val condition = Condition("Healing", Element.WATER, amount, effects)
                    val spell = Spell("Heal", condition, amount, WATER_MAGIC, levelRequirement, range = Distances.SWORD_RANGE)
                    EventManager.postEvent(StartCastSpellEvent(source.thing, thing, spell))
                }
            }
        }
    }

}