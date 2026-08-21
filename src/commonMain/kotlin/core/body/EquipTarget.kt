package core.body

import core.thing.Thing
import core.utility.NameSearchableList
import kotlinx.serialization.Serializable

typealias Layer = String

@Serializable
data class EquipTarget(val layer: String, val parts: List<String>) {
    override fun toString(): String {
        return "to $layer on ${parts.joinToString(", ")}"
    }
}

data class EquippedItem(val item: Thing, val layer: String, val parts: List<BodyPart>) {
    fun toEquipTarget() = EquipTarget(layer, parts.map { it.name })
}

fun Thing.toEquippedItem(target: EquipTarget, parts: NameSearchableList<BodyPart>): EquippedItem {
    return EquippedItem(this, target.layer,  target.parts.mapNotNull { parts.getOrNull(it) })
}
