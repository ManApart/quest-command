package combat

enum class AttackType {
    CHOP,
    CRUSH,
    SLASH,
    STAB;

    val damage get() = name.toLowerCase() + "Damage"
    val health get() = name.toLowerCase() + "Health"
    val defense get() = name.toLowerCase() + "Defense"
}