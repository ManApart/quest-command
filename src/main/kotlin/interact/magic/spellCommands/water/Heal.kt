package interact.magic.spellCommands.water

import combat.battle.position.TargetAim
import core.commands.Args
import core.gameState.GameState
import core.gameState.Target
import core.gameState.stat.WATER_MAGIC
import interact.magic.spells.Spell
import interact.magic.StartCastSpellEvent
import interact.magic.getTargetedParts
import interact.magic.spellCommands.SpellCommand
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager

class Heal : SpellCommand() {
    override val name = "Heal"

    override fun getDescription(): String {
        return "Cast Heal:\n\tHeal yourself or others."
    }

    override fun getManual(): String {
        return "\n\tCast Heal <amount> on *<targets> - Heals damage taken over time."
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>) {
        //TODO - response request instead of hard coded default
        val amount = args.getNumber() ?: 1
        val hitCount = targets.count()
        val totalCost = amount * hitCount
//        val levelRequirement = amount*2 +  duration/2
        val levelRequirement = amount / 2

        executeWithWarns(WATER_MAGIC, levelRequirement, totalCost, targets) {
            targets.forEach { target ->
                val parts = getTargetedParts(target)
                val effects = listOf(
                        EffectManager.getEffect("Heal", amount, 3, parts),
                        EffectManager.getEffect("Wet", 0, 5, parts)
                )

                val condition = Condition("Healing", Element.WATER, amount, effects)
                val spell = Spell("Heal", condition, amount, WATER_MAGIC, levelRequirement)
                EventManager.postEvent(StartCastSpellEvent(source, target, spell))
            }
        }
    }

}