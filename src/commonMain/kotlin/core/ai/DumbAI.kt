package core.ai

import conversation.dialogue.DialogueEvent

class DumbAI : AI() {
    override fun toString() = "Dumb AI for ${creature.name}"
    override fun copy() = DumbAI()
    override suspend fun hear(event: DialogueEvent) {}
    override suspend fun takeAction() = true
    override fun equals(other: Any?) = other is DumbAI
    override fun hashCode() = this::class.hashCode()
}
