package resources.conversation

import conversation.input.convo
import core.GameState

val conversations = convo({ GameState.player.isPlayer() }) {
    result = { "top level comment" }

    convo({ it.speaker.isSafe() }) {
        result = { "${it.speaker} is safe" }
    }

    convo({ !GameState.player.isSafe() }) {
        result = { "Player is safe not safe" }
    }

}.build()