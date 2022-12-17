package core.ai.agenda

class AgendasGenerated : AgendasCollection {
    override val values by lazy { listOf<AgendaResource>(resources.ai.agenda.CommonAgendas()).flatMap { it.values }}
}