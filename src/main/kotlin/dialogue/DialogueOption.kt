package dialogue

import core.gameState.dataParsing.Condition

data class DialogueOption(val response: String, val condition: Condition = Condition())