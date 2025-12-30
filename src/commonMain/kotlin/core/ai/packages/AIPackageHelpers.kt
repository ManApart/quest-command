package core.ai.packages

import combat.DamageType
import combat.attack.AttackEvent
import combat.attack.startAttack
import core.GameState
import core.ai.knowledge.DiscoverFactEvent
import core.ai.knowledge.Fact
import core.ai.knowledge.ForgetFactEvent
import core.ai.knowledge.HowToUse
import core.ai.knowledge.Subject
import core.properties.Properties
import core.properties.ValueKey
import core.properties.Values
import core.thing.Thing
import core.utility.RandomManager
import traveling.location.RouteFinder
import traveling.location.network.LocationNode
import traveling.position.ThingAim
import traveling.routes.FindRouteEvent


suspend fun Thing.hasAggroTarget() = mind.getAggroTarget() != null
suspend fun Thing.hasUseTarget() = mind.getUseTargetThing() != null

suspend fun Thing.canReachGoal(howToUse: HowToUse) = canReachGoal(howToUse.name)
suspend fun Thing.canReachGoal(howToUse: String): Boolean {
    val useTarget = mind.getUseTarget()
    return useTarget != null && useTarget.props.values.getString(ValueKey.GOAL) == howToUse && useTarget.source.getThing()?.position?.let { pos -> canReach(pos) } ?: false
}

suspend fun clawAttack(target: Thing, creature: Thing): AttackEvent {
    val enemyBody = target.body
    val possibleParts = listOf(
        enemyBody.getPart("Right Foot"),
        enemyBody.getPart("Left Foot")
    )
    val thingPart = listOf(RandomManager.getRandom(possibleParts))
    val partToAttackWith = if (creature.body.hasPart("Small Claws")) {
        creature.body.getPart("Small Claws")
    } else {
        creature.body.getRootPart()
    }
    return startAttack(creature, partToAttackWith, ThingAim(GameState.player.thing, thingPart), DamageType.SLASH)
}

suspend fun Thing.perceivedCreatures(): List<Thing> {
    return location.getLocation().getCreatures(perceivedBy = this).filter { it != this }
}

suspend fun Thing.perceivedActivators(): List<Thing> {
    return location.getLocation().getActivators(perceivedBy = this).filter { it != this }
}

suspend fun Thing.perceivedItems(): List<Thing> {
    return location.getLocation().getItems(perceivedBy = this).filter { it != this }
}

suspend fun Thing.perceivedItemsAndInventory(): List<Thing> {
    return (inventory.getItems() + location.getLocation().getItems(perceivedBy = this)).filter { it != this }
}

fun plotRouteAndStartTravel(s: Thing, goal: LocationNode): FindRouteEvent {
    val dest = RouteFinder(s.location, goal).getRoute().getNextStep(s.location).destination.location

    return FindRouteEvent(s, s.location, dest, startImmediately = true)
}
