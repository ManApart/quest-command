package core.ai.action.dsl
import core.ai.action.AIActionTree

interface AIActionsCollection {
    val values: List<AIActionTree>
}