package core.ai.packages

import combat.DamageType
import combat.attack.AttackEvent
import combat.attack.startAttack
import core.GameState
import core.ai.knowledge.DiscoverFactEvent
import core.ai.knowledge.Fact
import core.ai.knowledge.ForgetFactEvent
import core.ai.knowledge.Subject
import core.properties.Properties
import core.properties.Values
import core.thing.Thing
import core.utility.RandomManager
import traveling.position.ThingAim


suspend fun Thing.hasUseTarget() = mind.getUseTargetThing() != null

fun Thing.setUseGoal(target: Thing, howToUse: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), "useTarget", Properties(Values(mutableMapOf("goal" to howToUse)))))
}

fun Thing.clearUseGoal(): ForgetFactEvent {
    return ForgetFactEvent(this, kind ="useTarget")
}

suspend fun Thing.canReachGoal(howToUse: String ): Boolean {
    val useTarget = mind.getUseTarget()
    return useTarget != null && useTarget.props.values.getString("goal") == howToUse && useTarget.source.getThing()?.position?.let { pos -> canReach(pos) } ?: false
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