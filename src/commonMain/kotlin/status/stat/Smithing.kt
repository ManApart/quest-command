package status.stat

import core.thing.Thing

fun canSmith(crafter: Thing, ingredient: Thing, difficultyScale: Float): Boolean {
    return canCraft(crafter, ingredient, SMITHING, difficultyScale)
}

fun canCraft(crafter: Thing, ingredient: Thing, skill: String, difficultyScale: Float): Boolean {
    val currentLevel = crafter.soul.getCurrent(skill)

    val qualityLevel = ingredient.body.material.properties.values.getInt("Quality")
    val required = (difficultyScale * qualityLevel).toInt()

    return currentLevel >= required
}