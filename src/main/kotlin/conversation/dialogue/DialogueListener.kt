package conversation.dialogue

import core.events.EventListener
import magic.Element
import status.conditions.Condition
import status.effects.EffectManager


class DialogueListener : EventListener<DialogueEvent>() {
    override fun execute(event: DialogueEvent) {
        val conversation = event.conversation

        val condition = Condition("Talking", Element.AIR, 20, listOf(EffectManager.getEffect("Talking", 20, 2)), silent = true)
        event.speaker.soul.addNewCondition(condition)
        conversation.history.add(event)
        conversation.getLatestListener().ai.hear(event)
    }
}