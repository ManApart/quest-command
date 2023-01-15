package core.ai.desire

interface DesireResource {
    suspend fun values(): List<DesireTree>
}