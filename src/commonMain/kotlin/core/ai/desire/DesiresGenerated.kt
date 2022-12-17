package core.ai.desire

class DesiresGenerated : DesiresCollection {
    override val values by lazy { listOf<DesireResource>(resources.ai.desire.CommonDesires()).flatMap { it.values }}
}