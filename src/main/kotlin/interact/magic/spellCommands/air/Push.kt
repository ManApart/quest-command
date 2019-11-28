package interact.magic.spellCommands.air

import combat.battle.position.TargetAim
import combat.battle.position.toCommandString
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
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

class Push : SpellCommand() {
    override val name = "Push"

    override fun getDescription(): String {
        return "Cast Push:\n\tPush targets away from you. The higher the power, the further the target will be pushed. Lighter targets are pushed further."
    }

    override fun getManual(): String {
        return "\n\tCast Push <power> on *<targets> - Push the targets away from you." +
                "\n\tCast Push <power> towards <direction> on *<targets> - Push the targets a set distance in the given direction."
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val power = args.getNumber()
        val direction = parseDirection(args.getGroup("towards"))

        if (power == null) {
            val message = "Push ${targets.toCommandString()} how hard?"
            val options = listOf("1", "3", "5", "10", "50")
            val response = ResponseRequest(message, options.map { it to "push $it towards ${direction.name} on ${targets.toCommandString()}}" }.toMap())
             CommandParser.setResponseRequest(response)
        } else {
            val hitCount = targets.count()
            val perTargetCost = power / 10
            val totalCost = perTargetCost * hitCount
            val levelRequirement = power / 2

            executeWithWarns(source, AIR_MAGIC, levelRequirement, totalCost, targets) {
                targets.forEach { target ->
                    val parts = target.target.body.getParts()
                    val effects = listOf(EffectManager.getEffect("Air Blasted", 0, 0, parts))
                    val condition = Condition("Air Blasted", Element.AIR, power, effects)
                    val distance = calcDistance(target.target, power)

                    val vector = if (direction != Direction.NONE) {
                        target.target.position.further(direction.vector + target.target.position, distance)
                    } else {
                        target.target.position.further(source.position, power)
                    }
                    val spell = MoveTargetSpell("Push", vector, condition, perTargetCost, AIR_MAGIC, levelRequirement)
                    EventManager.postEvent(StartCastSpellEvent(source, target, spell))
                }
            }
        }
    }

    private fun calcDistance(target: Target, power: Int): Int {
        val weight = target.getWeight()
        return if (weight > power) {
            0
        } else {
            power / weight
        }
    }

}