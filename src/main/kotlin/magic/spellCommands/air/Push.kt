package magic.spellCommands.air

import core.commands.Args
import core.commands.ResponseRequest
import core.commands.parseDirection
import core.events.EventManager
import core.thing.Thing
import magic.Element
import magic.castSpell.StartCastSpellEvent
import magic.spellCommands.SpellCommand
import magic.spells.MoveThingSpell
import status.conditions.Condition
import status.effects.EffectManager
import status.stat.AIR_MAGIC
import traveling.direction.Direction
import traveling.position.ThingAim
import traveling.position.toCommandString

class Push : SpellCommand() {
    override val name = "Push"

    override fun getDescription(): String {
        return "Push things away from you."
    }

    override fun getManual(): String {
        return """
	Cast Push <power> on *<things> - Push the things away from you. The higher the power, the further the thing will be pushed. Lighter things are pushed further.
	Cast Push <power> towards <direction> on *<things> - Push the things a set distance in the given direction."""
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
    }

    override fun execute(source: Thing, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        val power = args.getNumber()
        val argsWithTowards = Args(args.args, delimiters = listOf("towards"))
        val direction = parseDirection(argsWithTowards.getGroup("towards"))


        if (power == null) {
            val message = "Push ${things.toCommandString()} how hard?"
            val options = listOf("1", "3", "5", "10", "50")
            val response = ResponseRequest(message,
                options.associateWith { "push $it towards ${direction.name} on ${things.toCommandString()}}" })
             CommandParsers.setResponseRequest(response)
        } else {
            val hitCount = things.count()
            val perThingCost = power / 10
            val totalCost = perThingCost * hitCount
            val levelRequirement = power / 2

            executeWithWarns(source, AIR_MAGIC, levelRequirement, totalCost, things) {
                things.forEach { thing ->
                    val parts = thing.thing.body.getParts()
                    val effects = listOf(EffectManager.getEffect("Air Blasted", 0, 0, parts))
                    val condition = Condition("Air Blasted", Element.AIR, power, effects)
                    val distance = calcDistance(thing.thing, power)

                    val vector = if (direction == Direction.NONE) {
                       val directionGoal = source.position.further(thing.thing.position, distance)
                        thing.thing.position.getVectorInDirection(directionGoal, distance)
                    } else {
                        thing.thing.position.getVectorInDirection((direction.vector * distance) + thing.thing.position, distance)
                    }
                    val spell = MoveThingSpell("Push", vector, condition, perThingCost, AIR_MAGIC, levelRequirement)
                    EventManager.postEvent(StartCastSpellEvent(source, thing, spell))
                }
            }
        }
    }

    private fun calcDistance(thing: Thing, power: Int): Int {
        val weight = thing.getWeight()
        return if (weight > power) {
            0
        } else {
            power / weight
        }
    }

}