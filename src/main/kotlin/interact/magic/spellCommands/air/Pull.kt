package interact.magic.spellCommands.air

import combat.battle.position.TargetAim
import core.commands.Args
import core.commands.parseDirection
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.Target
import core.gameState.Vector
import core.gameState.stat.AIR_MAGIC
import interact.magic.StartCastSpellEvent
import interact.magic.spellCommands.SpellCommand
import interact.magic.spells.MoveTargetSpell
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager

class Pull : SpellCommand() {
    override val name = "Pull"

    override fun getDescription(): String {
        return "Cast Pull:\n\tPull targets closer to you."
    }

    override fun getManual(): String {
        return "\n\tCast Pull <distance> on *<targets> - Pull the targets a set distance closer to you." +
                "\n\tCast Pull <direction> <distance> on *<targets> - Pull the targets a set distance in the given direction."
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>) {
        //TODO - response request instead of hard coded default
        val distance = args.getNumber() ?: 1
        val hitCount = targets.count()
        //TODO - eventually take into account weight of targets
        val totalCost = distance * hitCount
        val levelRequirement = distance / 2
        val direction = parseDirection(args.args)

        executeWithWarns(AIR_MAGIC, levelRequirement, totalCost, targets) {
            targets.forEach { target ->
                val parts = target.target.body.getParts()
                val effects = listOf(EffectManager.getEffect("Air Blasted", 0, 0, parts))
                val condition = Condition("Air Blasted", Element.AIR, distance, effects)
                val vector = if (direction != Direction.NONE) {
                    target.target.position.closer(direction.vector + source.position, distance)
                } else {
                    target.target.position.closer(source.position, distance)
                }
                val spell = MoveTargetSpell("Push", vector, condition, distance, AIR_MAGIC, levelRequirement)
                EventManager.postEvent(StartCastSpellEvent(source, target, spell))
            }
        }
    }

}