package conversation.dialogue

import core.events.EventListener
import explore.listen.addSoundEffect


class DialogueListener : EventListener<DialogueEvent>() {
    override suspend fun execute(event: DialogueEvent) {
        val conversation = event.conversation

        event.speaker.addSoundEffect("Talking", "the sound of voices", 20)
        conversation.history.add(event)
        conversation.getLatestListener().mind.ai.hear(event)
    }
}