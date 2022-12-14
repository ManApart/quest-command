package core.ai.desire

class DesiresGenerated : DesiresCollection {
    override val values by lazy { listOf<DesireResource>().flatMap { it.values }}
}