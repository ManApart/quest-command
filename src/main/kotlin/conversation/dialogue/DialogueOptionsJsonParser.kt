package conversation.dialogue

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser

class DialogueOptionsJsonParser : DialogueOptionsParser {
    private fun parseFile(path: String): List<ConditionalDialogue> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadConversationDialogue(): List<ConditionalDialogue> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/dialogue/conversation", ::parseFile)
    }
}