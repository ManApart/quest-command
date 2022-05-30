package core.properties

import combat.DamageType

object GuardManager {
    private val guards by lazy { buildGuardMap() }

    private fun buildGuardMap(): Map<String, (String, String) -> String> {
        return DamageType.values().associate { it.health.lowercase() to ::isGreaterThanEqualToZero }
    }

    fun getGuardedValue(key: String, current: String, newValue: String): String {
        val lowKey = key.lowercase()
        val guard = guards[lowKey]
        return guard?.invoke(current, newValue) ?: newValue
    }

}