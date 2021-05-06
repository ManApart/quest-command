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

class Pull : SpellCommand() {
    override val name = "Pull"

    override fun getDescription(): String {
        return "Pull targets closer to you."
    }

    override fun getManual(): String {
        return """
	Cast Pull <power> on *<targets> - Pull the targets a set distance closer to you. The higher the power, the further the target will be pulled. Lighter targets are pulled further.
	Cast Pull <power> towards <direction> on *<targets> - Pull the targets a set distance in the given direction."""
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val argsWithTowards = Args(args.args, delimiters = listOf("towards"))
        val direction = parseDirection(argsWithTowards.getGroup("towards"))

        val power = args.getNumber()
        if (power == null) {
            val message =  "Pull ${targets.toCommandString()} how hard?"
            val options = listOf("1", "3", "5", "10", "50")
            val response = ResponseRequest(message,
                options.associateWith { "pull $it towards ${direction.name} on ${targets.toCommandString()}}" })
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
                        target.target.position.closer(source.position, distance)
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