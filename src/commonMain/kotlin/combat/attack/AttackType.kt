package combat.attack

import combat.DamageType

enum class AttackType(val alias: String, val damageType: DamageType) {
    CHOP("ch", DamageType.CHOP),
    CRUSH("cr", DamageType.CRUSH),
    SLASH("sl", DamageType.SLASH),
    STAB("sb", DamageType.STAB)
}

fun fromString(keyword: String) : AttackType {
    val cleaned = keyword.lowercase()
    return AttackType.values().first { it.name.lowercase() == cleaned || it.alias == cleaned }
}