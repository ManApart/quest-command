package core.ai

import combat.DamageType
import combat.attack.StartAttackEvent
import traveling.position.TargetAim
import core.GameState
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import core.events.EventManager

class ConditionalAI(name: String, creature: Target, val actions: List<TriggeredEvent>) : AI(name, creature) {

    override fun takeAction() {
        //TODO - replace hardcoding with script informed / generic
        if (actions.isEmpty()) {
            defaultHardCodedAction()
        } else {
            //TODO - evaluate triggered events
        }
    }

    private fun defaultHardCodedAction() {
        if (aggroTarget != null) {
            val playerBody = GameState.player.body
            val possibleParts = listOf(
                    playerBody.getPart("Right Leg"),
                    playerBody.getPart("Right Foot"),
                    playerBody.getPart("Left Leg"),
                    playerBody.getPart("Left Leg")
            )
            val targetPart = listOf(possibleParts.random())
            val defaultPart = if (creature.body.hasPart("Small Claws")) {
                creature.body.getPart("Small Claws")
            } else {
                creature.body.getRootPart()
            }
            EventManager.postEvent(StartAttackEvent(creature, defaultPart, TargetAim(GameState.player, targetPart), DamageType.SLASH))
        }
    }

}