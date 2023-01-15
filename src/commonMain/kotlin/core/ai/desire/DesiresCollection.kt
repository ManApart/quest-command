package core.ai.desire
import core.ai.desire.DesireTree

interface DesiresCollection {
    suspend fun values(): List<DesireTree>
}