package core.gameState

import combat.DamageType
import combat.attack.StartAttackEvent
import combat.battle.position.TargetPosition
import system.EventManager

class AI(val name: String, val creature: Target) {

    fun takeAction() {
        //TODO - replace hardcoding with script informed / generic
        if (GameState.battle != null) {
            EventManager.postEvent(StartAttackEvent(creature, creature.body.getPart("Small Claws"), GameState.player, TargetPosition(), DamageType.SLASH))
        }
    }

}