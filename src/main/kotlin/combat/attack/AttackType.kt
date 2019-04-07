package combat.attack

enum class AttackType {
    CHOP,
    CRUSH,
    SLASH,
    STAB;

    val verb get() = name.toLowerCase() + "es"
    val damage get() = name.toLowerCase() + "Damage"
    val health get() = name.toLowerCase() + "Health"
    val defense get() = name.toLowerCase() + "Defense"
}