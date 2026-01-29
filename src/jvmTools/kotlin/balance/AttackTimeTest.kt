package balance

import combat.DamageType
import combat.attack.startAttack
import core.properties.Properties
import core.properties.Tags
import core.properties.Values
import core.thing.Thing
import kotlinx.coroutines.runBlocking
import status.stat.Attributes.AGILITY
import status.stat.Attributes.STRENGTH
import traveling.location.location.Location
import traveling.location.location.LocationRecipe
import traveling.location.network.LocationNode
import traveling.position.ThingAim


fun main() {
    runBlocking {
        println("\nIncrease Agility")
        println("Agility \tStrength \tEncumbrance \tWeapon size \tWeapon weight \tTime needed")
        testAttackTime(1, 1, "Small", 1, 0)
        testAttackTime(2, 1, "Small", 1, 0)
        testAttackTime(5, 1, "Small", 1, 0)
        testAttackTime(10, 1, "Small", 1, 0)

        println("\nIncrease Encumbrance")
        println("Agility \tStrength \tEncumbrance \tWeapon size \tWeapon weight \tTime needed")
        testAttackTime(10, 1, "Small", 1, 0)
        testAttackTime(10, 1, "Small", 1, 4)
        testAttackTime(10, 1, "Small", 1, 9)

        println("\nIncrease Weapon Weight")
        println("Agility \tStrength \tEncumbrance \tWeapon size \tWeapon weight \tTime needed")
        testAttackTime(1, 1, "Small", 1, 0)
        testAttackTime(1, 1, "Small", 5, 0)
        testAttackTime(1, 1, "Small", 10, 0)

        println("\nIncrease Weapon Size")
        println("Agility \tStrength \tEncumbrance \tWeapon size \tWeapon weight \tTime needed")
        testAttackTime(1, 1, "Small", 1, 0)
        testAttackTime(1, 1, "Medium", 1, 0)
        testAttackTime(1, 1, "Large", 1, 0)

        println("\nMixed")
        println("Agility \tStrength \tEncumbrance \tWeapon size \tWeapon weight \tTime needed")
        testAttackTime(1, 1, "Small", 1, 0)
        testAttackTime(10, 1, "Medium", 2, 5)
    }
}

private suspend fun testAttackTime(agility: Int, strength: Int, weaponSize: String, weaponWeight: Int, otherWeight: Int) {
    val creature = Thing("Creature")
    creature.soul.addStat(AGILITY, agility)
    creature.soul.addStat(STRENGTH, strength)

    val recipe = LocationRecipe("hand", slots = listOf("hand"))
    val node = LocationNode("hand")
    val part = Location(node, recipe = recipe)
    val weapon = Thing("Weapon", properties = Properties(Values("weight" to weaponWeight.toString()), Tags("Weapon", weaponSize)))
    creature.inventory.add(weapon)
    creature.inventory.add(Thing("Dead weight", properties = Properties(Values("weight" to otherWeight.toString()))))
    part.equipItem("hand", weapon)

    val event = startAttack(creature, part, ThingAim(Thing("Thing")), DamageType.SLASH)

    println("\t${creature.soul.getCurrent(AGILITY)} \t\t${creature.soul.getCurrent(STRENGTH)} \t\t\t${creature.getEncumbrance()} \t\t\t$weaponSize \t\t\t$weaponWeight \t\t\t\t${event.timeLeft}")
}