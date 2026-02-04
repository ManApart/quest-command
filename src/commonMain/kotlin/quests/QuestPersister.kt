package quests

@kotlinx.serialization.Serializable
data class QuestP(
    val name: String,
    val stage: Int,
    val complete: Boolean,
    val active: Boolean,
    val journalEntries: List<String> = emptyList(),
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
