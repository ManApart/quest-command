package core.ai.action.dsl

import core.ai.action.AIActionTree

interface AIActionResource {
    val values: List<AIActionTree>
}