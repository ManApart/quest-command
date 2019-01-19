package core.gameState

import combat.AttackEvent
import combat.AttackType
import combat.battle.position.TargetPosition
import system.EventManager

class AI(val name: String, val creature: Creature) {

    fun takeAction() {
        //TODO - replace hardcoding with script informed / generic
        if (GameState.battle != null) {
            EventManager.postEvent(AttackEvent(creature, creature.body.getPart("Small Claws"), GameState.player.creature, TargetPosition(), AttackType.SLASH))
        }
    }

}