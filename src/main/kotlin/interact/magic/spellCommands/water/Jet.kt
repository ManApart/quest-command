package interact.magic.spellCommands.water

import combat.battle.position.TargetAim
import core.commands.Args
import core.gameState.GameState
import core.gameState.stat.WATER_MAGIC
import interact.magic.Spell
import interact.magic.StartCastSpellEvent
import interact.magic.spellCommands.SpellCommand
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager

class Jet : SpellCommand() {
    override val name = "Jet"

    override fun getDescription(): String {
        return "Cast Jet:\n\tBurst of water that does one time damage to one or more targets."
    }

    override fun getManual(): String {
        return "\n\tCast Jet <damage amount> on *<targets> - Burst of water that does one time damage to one or more targets."
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override fun execute(args: Args, targets: List<TargetAim>) {
        //TODO - response request instead of hard coded default
        val damageAmount = args.getNumber() ?: 1
        val hitCount = targets.sumBy { it.target.body.getParts().size }
        val totalCost = damageAmount * hitCount
        val levelRequirement = damageAmount / 2

        executeWithWarns(WATER_MAGIC, levelRequirement, totalCost, targets) {
            targets.forEach { target ->
                val parts = target.target.body.getParts()
                val effects = listOf(
                        EffectManager.getEffect("Water Slice", damageAmount, 1, parts),
                        EffectManager.getEffect("Wet", 0, 5, parts)
                )

                val cost = damageAmount * target.target.body.getParts().size
                val condition = Condition("Water Blasted", Element.WATER, cost, effects)
                val spell = Spell("Jet", condition, cost, WATER_MAGIC, levelRequirement)
                EventManager.postEvent(StartCastSpellEvent(GameState.player, target.target, spell))
            }
        }
    }

}