package magic.spellCommands.air

import traveling.position.TargetAim
import traveling.position.toCommandString
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.commands.parseDirection
import traveling.direction.Direction
import core.target.Target
import status.stat.AIR_MAGIC
import magic.castSpell.StartCastSpellEvent
import magic.spellCommands.SpellCommand
import magic.spells.MoveTargetSpell
import status.conditions.Condition
import status.effects.EffectManager
import magic.Element
import core.events.EventManager

class Push : SpellCommand() {
    override val name = "Push"

    override fun getDescription(): String {
        return "Push targets away from you."
    }

    override fun getManual(): String {
        return """
	Cast Push <power> on *<targets> - Push the targets away from you. The higher the power, the further the target will be pushed. Lighter targets are pushed further.
	Cast Push <power> towards <direction> on *<targets> - Push the targets a set distance in the given direction."""
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val power = args.getNumber()
        val argsWithTowards = Args(args.args, delimiters = listOf("towards"))
        val direction = parseDirection(argsWithTowards.getGroup("towards"))


        if (power == null) {
            val message = "Push ${targets.toCommandString()} how hard?"
            val options = listOf("1", "3", "5", "10", "50")
            val response = ResponseRequest(message,
                options.associateWith { "push $it towards ${direction.name} on ${targets.toCommandString()}}" })
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

                    val vector = if (direction == Direction.NONE) {
                       val directionGoal = source.position.further(target.target.position, distance)
                        target.target.position.getVectorInDirection(directionGoal, distance)
                    } else {
                        target.target.position.getVectorInDirection((direction.vector * distance) + target.target.position, distance)
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