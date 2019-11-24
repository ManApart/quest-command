package interact.magic.spellCommands.air

import combat.battle.position.TargetAim
import core.commands.Args
import core.commands.parseDirection
import core.gameState.Direction
import core.gameState.Target
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
        return "Cast Pull:\n\tPull targets closer to you. The higher the power, the further the target will be pulled. Lighter targets are pulled further."
    }

    override fun getManual(): String {
        return "\n\tCast Pull <power> on *<targets> - Pull the targets a set distance closer to you." +
                "\n\tCast Pull <power> towards <direction> on *<targets> - Pull the targets a set distance in the given direction."
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>) {
        //TODO - response request instead of hard coded default
        val power = args.getNumber() ?: 1
        val hitCount = targets.count()
        val perTargetCost = power/10
        val totalCost = perTargetCost * hitCount
        val levelRequirement = power / 2
        val direction = parseDirection(args.getGroup("towards"))

        executeWithWarns(source, AIR_MAGIC, levelRequirement, totalCost, targets) {
            targets.forEach { target ->
                val parts = target.target.body.getParts()
                val effects = listOf(EffectManager.getEffect("Air Blasted", 0, 0, parts))
                val condition = Condition("Air Blasted", Element.AIR, power, effects)
                val distance = calcDistance(target.target, power)
                val vector = if (direction != Direction.NONE) {
                    target.target.position.closer(direction.vector + source.position, distance)
                } else {
                    target.target.position.closer(source.position, distance)
                }
                val spell = MoveTargetSpell("Push", vector, condition, perTargetCost, AIR_MAGIC, levelRequirement)
                EventManager.postEvent(StartCastSpellEvent(source, target, spell))
            }
        }
    }

    private fun calcDistance(target: Target, power: Int) : Int{
        val weight = target.getWeight()
        return if (weight > power){
            0
        } else {
            power/weight
        }
    }

}