package core.ai.desire

import core.ai.action.DesireTree

interface DesireResource {
    val values: List<DesireTree>
}