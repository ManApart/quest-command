package core.gameState

import combat.TargetDirection
import combat.slash.SlashEvent
import system.EventManager

class AI(val name: String, val creature: Creature) {

    fun takeAction() {
        //TODO - replace hardcoding with script informed / generic
        if (GameState.battle != null) {
            EventManager.postEvent(SlashEvent(creature, creature.body.getPart("Body"), GameState.player.creature, TargetDirection.MIDDLE))
        }

    }

}