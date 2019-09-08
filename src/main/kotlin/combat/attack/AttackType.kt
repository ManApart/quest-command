package combat.attack

import combat.DamageType

enum class AttackType(val alias: String, val damageType: DamageType) {
    CHOP("ch", DamageType.CHOP),
    CRUSH("cr", DamageType.CRUSH),
    SLASH("sl", DamageType.SLASH),
    STAB("sb", DamageType.STAB)
}

fun fromString(keyword: String) : AttackType {
    val cleaned = keyword.toLowerCase()
    return AttackType.values().first { it.name.toLowerCase() == cleaned || it.alias == cleaned }
}