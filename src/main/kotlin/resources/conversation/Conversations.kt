package resources.conversation

import conversation.input.EvaluationBuilder2
import conversation.input.convo
import core.GameState

val conversations = convo({ GameState.player.isPlayer() }) {
    result = "top level comment"

    convo({ GameState.player.isSafe() }) {
        result = "Player is safe"
    }

    convo({ !GameState.player.isSafe() }) {
        result = "Player is safe not safe"
    }

}.build()

val conversations2 = EvaluationBuilder2(
    { GameState.player.isPlayer() }, listOf(
        EvaluationBuilder2({ GameState.player.isSafe() }, result = "Hello Player"),
    )

).build()