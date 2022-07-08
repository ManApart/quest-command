package core.ai

import conversation.dialogue.DialogueEvent

class DumbAI : AI() {
    override fun hear(event: DialogueEvent) {}
    override fun takeAction() {}
    override fun equals(other: Any?): Boolean {
        return other is DumbAI
    }
    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}