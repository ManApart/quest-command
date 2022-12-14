package core.ai.agenda

class AgendasGenerated : AgendasCollection {
    override val values by lazy { listOf<AgendaResource>().flatMap { it.values }}
}