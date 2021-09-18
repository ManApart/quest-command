package balance

import combat.DamageType
import combat.attack.StartAttackEvent
import traveling.position.TargetAim
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.properties.Values
import status.stat.AGILITY
import status.stat.STRENGTH
import traveling.location.location.Location
import traveling.location.network.LocationNode
import traveling.location.location.LocationRecipe


fun main() {
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

private fun testAttackTime(agility: Int, strength: Int, weaponSize: String, weaponWeight: Int, otherWeight: Int) {
    val creature = Target("Creature")
    creature.soul.addStat(AGILITY, agility)
    creature.soul.addStat(STRENGTH, strength)

    val recipe = LocationRecipe("hand", slots = listOf("hand"))
    val node = LocationNode("hand")
    val part = Location(node, recipe = recipe)
    val weapon = Target("Weapon", properties = Properties(Values(mapOf("weight" to weaponWeight.toString())), Tags("Weapon", weaponSize)))
    creature.inventory.add(weapon)
    creature.inventory.add(Target("Dead weight", properties = Properties(Values(mapOf("weight" to otherWeight.toString())))))
    part.equipItem("hand", weapon)

    val event = StartAttackEvent(creature, part, TargetAim(Target("Target")), DamageType.SLASH)

    println("\t${creature.soul.getCurrent(AGILITY)} \t\t${creature.soul.getCurrent(STRENGTH)} \t\t\t${creature.getEncumbrance()} \t\t\t$weaponSize \t\t\t$weaponWeight \t\t\t\t${event.timeLeft}")
}