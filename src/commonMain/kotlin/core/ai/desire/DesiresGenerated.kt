package core.ai.desire

class DesiresGenerated : DesiresCollection {
    override suspend fun values() = listOf<DesireResource>(resources.ai.desire.CommonDesires()).flatMap { it.values() }
}