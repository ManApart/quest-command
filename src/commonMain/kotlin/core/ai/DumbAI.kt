package core.ai

import conversation.dialogue.DialogueEvent

class DumbAI : AI() {
    override fun toString(): String {
        return "Dumb AI for ${creature.name}"
    }
    override suspend fun hear(event: DialogueEvent) {}
    override suspend fun takeAction() {}
    override fun equals(other: Any?): Boolean {
        return other is DumbAI
    }
    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}