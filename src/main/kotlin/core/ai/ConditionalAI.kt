package core.ai

import combat.DamageType
import combat.attack.StartAttackEvent
import conversation.ConversationManager
import conversation.dialogue.DialogueEvent
import core.GameState
import core.ai.action.AIAction
import core.events.EventManager
import core.history.display
import core.target.Target
import traveling.position.TargetAim

class ConditionalAI(name: String, private val owner: Target, private val actions: List<AIAction>) : AI(name, owner) {

    override fun takeAction() {
        val action = determineAction()
        if (action != null) {
            action.execute(owner)
        } else {
            defaultHardCodedAction()
        }
    }

    private fun determineAction() : AIAction? {
        return actions.firstOrNull { it.canRun(creature) }
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
        val matches = ConversationManager.getMatchingDialogue(event.conversation)
        val response = matches.maxByOrNull { it.priority }!!
        response.result(event.conversation).forEach { EventManager.postEvent(it) }
    }

}