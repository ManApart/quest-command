package core.ai

import conversation.dialogue.DialogueEvent
import core.target.Target

const val DUMB_AI_ID = "No AI"

class DumbAI(creature: Target) : AI(DUMB_AI_ID, creature) {
    override fun hear(event: DialogueEvent) {}
    override fun takeAction() {}
}