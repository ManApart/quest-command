package quests

fun getPersisted(dataObject: Quest): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["stage"] = dataObject.stage
    data["complete"] = dataObject.complete
    data["active"] = dataObject.active
    //TODO - persist if each stage is complete
    data["journalEntries"] = dataObject.getAllJournalEntries()

    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>) {
    val name = data["name"] as String
    val quest = QuestManager.quests.getOrNull(name)
    if (quest != null) {
        quest.stage = data["stage"] as Int
        quest.complete = data["complete"] as Boolean
        quest.active = data["active"] as Boolean
        quest.addAllEntries(data["journalEntries"] as List<String>)
    }
}

@kotlinx.serialization.Serializable
data class QuestP(
    val name: String,
    val stage: Int,
    val complete: Boolean,
    val active: Boolean,
    val journalEntries: List<String>
) {
    constructor(b: Quest) : this(b.name, b.stage, b.complete, b.active, b.getAllJournalEntries())

    fun updateQuestManager() {
        val quest = QuestManager.quests.getOrNull(name)
        if (quest != null) {
            quest.stage = stage
            quest.complete = complete
            quest.active = active
            quest.addAllEntries(journalEntries)
        }
    }
}