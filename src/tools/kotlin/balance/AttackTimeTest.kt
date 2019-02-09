package balance

import combat.AttackType
import combat.StartAttackEvent
import combat.battle.position.TargetPosition
import core.gameState.Creature
import core.gameState.Item
import core.gameState.Properties
import core.gameState.Tags
import core.gameState.body.BodyPart
import core.gameState.stat.AGILITY
import core.gameState.stat.STRENGTH


fun main(args: Array<String>) {
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
    val creature = Creature("Creature")
    creature.soul.addStat(AGILITY, agility)
    creature.soul.addStat(STRENGTH, strength)

    val part = BodyPart("hand", slots = listOf("hand"))
    val weapon = Item("Weapon", weight = weaponWeight, properties = Properties(tags = Tags(listOf("Weapon", weaponSize))))
    creature.inventory.add(weapon)
    creature.inventory.add(Item("Dead weight", weight = otherWeight))
    part.equipItem("hand", weapon)

    val event = StartAttackEvent(creature, part, Creature("Target"), TargetPosition(), AttackType.SLASH)

    println("\t${creature.soul.getCurrent(AGILITY)} \t\t${creature.soul.getCurrent(STRENGTH)} \t\t\t${creature.getEncumbrance()} \t\t\t$weaponSize \t\t\t$weaponWeight \t\t\t\t${event.timeLeft}")
}