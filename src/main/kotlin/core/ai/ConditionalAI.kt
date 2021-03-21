package core.ai

import combat.DamageType
import combat.attack.StartAttackEvent
import conversation.dialogue.DialogueEvent
import traveling.position.TargetAim
import core.GameState
import core.target.Target
import core.events.EventManager
import quests.ConditionalEvents
import core.history.display

class ConditionalAI(name: String, creature: Target, val actions: List<ConditionalEvents<*>>) : AI(name, creature) {

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

    override fun hear(event: DialogueEvent) {
        display(event.line)

    }

}