package status.stat

import core.thing.Thing
import kotlin.math.max

fun canSmith(crafter: Thing, ingredient: Thing, difficultyScale: Float): Boolean {
    val needed = listOf("Metal", "Ingot")
    val levels = listOf("Bronze", "Iron", "Steel", "Blackened Steel", "Adamant", "Mithril")
    return canCraft(crafter, ingredient, SMITHING, difficultyScale, needed, levels)
}

fun canCraft(crafter: Thing, ingredient: Thing, skill: String, difficultyScale: Float, neededTags: List<String>, levels: List<String>): Boolean {
    val ingredientTags = ingredient.properties.tags
    val currentLevel = crafter.soul.getCurrent(skill)

    if (!ingredientTags.hasAll(neededTags)) return false

    val level = max(0, ingredientTags.getAll().maxOf { levels.indexOf(it) })
    val required = (difficultyScale * level).toInt()

    return currentLevel >= required
}