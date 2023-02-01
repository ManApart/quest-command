package core.ai.desire

class DesiresMock(val values: List<DesireTree> = listOf()) : DesiresCollection {
    override suspend fun values() = values
}