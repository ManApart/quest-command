package core.body

import kotlinx.serialization.Serializable

typealias Layer = String

@Serializable
class EquipTarget(val layer: String, val parts: List<String>) {
    override fun toString(): String {
        return "to $layer on ${parts.joinToString(", ")}"
    }
}
