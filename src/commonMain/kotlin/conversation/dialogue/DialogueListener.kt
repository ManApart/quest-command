package conversation.dialogue

import core.events.EventListener
import explore.listen.addSoundEffect
import explore.listen.soundCondition
import magic.Element
import status.conditions.Condition
import status.effects.EffectManager


class DialogueListener : EventListener<DialogueEvent>() {
    override fun execute(event: DialogueEvent) {
        val conversation = event.conversation

        event.speaker.addSoundEffect("Talking", "the sound of voices", 20)
        conversation.history.add(event)
        conversation.getLatestListener().mind.ai.hear(event)
    }
}