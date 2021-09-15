package core.ai

import conversation.dialogue.DialogueEvent
import core.target.Target

const val DUMB_AI_ID = "No AI"

class DumbAI : AI(DUMB_AI_ID) {
    override fun hear(event: DialogueEvent) {}
    override fun takeAction() {}
    override fun equals(other: Any?): Boolean {
        return other is DumbAI
    }
}