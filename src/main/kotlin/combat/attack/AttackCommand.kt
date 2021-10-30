package combat.attack

import combat.HandHelper
import core.Player
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import status.stat.HEALTH
import traveling.position.ThingAim
import use.StartUseEvent

class AttackCommand : Command() {
    override fun getAliases(): List<String> {
        val aliases = mutableListOf("Attack")
        AttackType.values().forEach {
            aliases.add(it.name.lowercase())
            aliases.add(it.alias.lowercase())
        }
        return aliases
    }

    override val name = "Chop, Slash, Stab"

    override fun getDescription(): String {
        return "Chop/Stab/Slash/Crush the thing"
    }

    override fun getManual(): String {
        return """
	<attack> <thing> - Chop, crush, slash, or stab the thing with the item in your right hand
	<attack> <thing> with <hand> - Attack the thing with the item in your left/right hand
	<attack> <part> of <thing> - Attack the thing, aiming for a specific body part
	<attack> <thing> with <item> - Attack the thing with the item in your left/right hand
	Attacking a thing damages it based on the chop/stab/slash damage of the item you're holding in that hand, or the damage you do if empty handed"""
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val sourceT = source.thing
        if (keyword.lowercase() == "attack") {
            clarifyAttackType(source, args)
        } else {
            val arguments = Args(args, listOf("with"))
            val attackType = fromString(keyword)
            val handHelper = HandHelper(sourceT, arguments.getString("with"), attackType.damageType.damage.lowercase())
            val weaponName = handHelper.hand.getEquippedWeapon()?.name ?: handHelper.hand.name
            val thing = getThing(keyword, arguments, weaponName, source)

            if (thing == null) {
                clarifyThing(source, keyword, weaponName)
            } else {
                //Go ahead and process a thing that has aimed for body parts or no body parts at all
                if (thing.bodyPartThings.isNotEmpty()) {
                    processAttack(sourceT, arguments, attackType, handHelper, thing)
                } else {
                    //If we got an alias, process with a default value of the body root part
                    if (isAlias(keyword) || thing.thing.body.getParts().size == 1) {
                        processAttack(sourceT, arguments, attackType, handHelper, ThingAim(thing.thing, listOf(thing.thing.body.getRootPart())))
                        //Otherwise clarify body parts.
                    } else {
                        clarifyThingPart(source, keyword, thing, weaponName)
                    }
                }
            }
        }
    }

    private fun getThing(keyword: String, arguments: Args, weaponName: String, source: Player): ThingAim? {
        val things = parseThingsFromLocation(source.thing, arguments.getBaseGroup()) + parseThingsFromInventory(source.thing, arguments.getBaseGroup())

        return when {
            things.size == 1 -> things.first()
            things.isEmpty() && !isAlias(keyword) -> {
                clarifyThing(source, keyword, weaponName)
                null
            }
            things.isEmpty() -> {
                source.ai.aggroThing?.let { ThingAim(it) }
            }
            else -> {
                clarifyThing(source, keyword, weaponName)
                null
            }
        }
    }

    private fun clarifyAttackType(player: Player, args: List<String>) {
        player.respond {
            message("Attack how?")
            options("Chop", "Crush", "Slash", "Stab")
            command { "$it ${args.joinToString(" ")}" }
        }
    }

    private fun clarifyThing(player: Player, keyword: String, weaponName: String) {
        player.respond {
            message("$keyword what with $weaponName?")
            options(player.thing.currentLocation().getThings())
            command { "$keyword $it" }
        }
    }

    private fun clarifyThings(player: Player, keyword: String, options: List<ThingAim>, weaponName: String) {
        player.respond {
            message("$keyword which one with $weaponName?")
            options(options.map { it.thing.name })
            command { "$keyword $it" }
        }
    }

    private fun clarifyThingPart(player: Player, keyword: String, thing: ThingAim, weaponName: String) {
        player.respond {
            message("$keyword what part of ${thing.thing.name} with $weaponName?")
            options(thing.thing.body.getParts())
            command { "$keyword $it of ${thing.thing.name}" }
        }
    }

    private fun processAttack(source: Thing, arguments: Args, attackType: AttackType, handHelper: HandHelper, thing: ThingAim?) {
        when {
            isAttackingActivatorWithWeapon(thing, handHelper) -> EventManager.postEvent(StartUseEvent(source, handHelper.weapon!!, thing!!.thing))
            thing != null && thing.thing == source && handHelper.weapon != null -> EventManager.postEvent(StartUseEvent(source, handHelper.weapon!!, source))
            thing != null && !thing.thing.soul.hasStat(HEALTH) && handHelper.weapon != null -> EventManager.postEvent(StartUseEvent(source, handHelper.weapon!!, thing.thing))
            thing != null -> EventManager.postEvent(StartAttackEvent(source, handHelper.hand, thing, attackType.damageType))
            source.ai.aggroThing != null -> EventManager.postEvent(StartAttackEvent(source, handHelper.hand, ThingAim(source.ai.aggroThing!!), attackType.damageType))
            else -> source.displayToMe("Couldn't find ${arguments.getBaseString()}.")
        }
    }

    private fun isAttackingActivatorWithWeapon(thing: ThingAim?, handHelper: HandHelper) =
        thing != null && handHelper.weapon != null && thing.thing.properties.isActivator()


}