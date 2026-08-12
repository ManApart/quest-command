package core.body

typealias Layer = String

class EquipTarget(val layer: String, val parts: List<String>) {
    override fun toString(): String {
        return "to $layer on ${parts.joinToString(", ")}"
    }
}
