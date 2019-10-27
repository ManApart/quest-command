package interact.magic.spellCommands.air

import combat.battle.position.TargetAim
import core.commands.Args
import core.gameState.GameState
import core.gameState.Vector
import core.gameState.stat.AIR_MAGIC
import interact.magic.StartCastSpellEvent
import interact.magic.spellCommands.SpellCommand
import interact.magic.spells.MoveTargetSpell
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager

class Push : SpellCommand() {
    override val name = "Push"

    override fun getDescription(): String {
        return "Cast Push:\n\tPush targets away from you."
    }

    override fun getManual(): String {
        return "\n\tCast Push <distance> on *<targets> - Push the targets a set distance away from you."
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
    }

    override fun execute(args: Args, targets: List<TargetAim>) {
        //TODO - response request instead of hard coded default
        val distance = args.getNumber() ?: 1
        val hitCount = targets.count()
        val totalCost = distance * hitCount
        val levelRequirement = distance / 2

        executeWithWarns(AIR_MAGIC, levelRequirement, totalCost, targets) {
            targets.forEach { target ->
                val parts = target.target.body.getParts()
                val effects = listOf(EffectManager.getEffect("Air Blasted", 0, 0, parts))
                val condition = Condition("Air Blasted", Element.AIR, distance, effects)
                val vector = target.target.position.further(GameState.player.position, distance)
                val spell = MoveTargetSpell("Push", vector, condition, distance, AIR_MAGIC, levelRequirement)
                EventManager.postEvent(StartCastSpellEvent(GameState.player, target.target, spell))
            }
        }
    }

}