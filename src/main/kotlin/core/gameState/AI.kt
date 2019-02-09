package core.gameState

import combat.AttackType
import combat.StartAttackEvent
import combat.battle.position.TargetPosition
import system.EventManager

class AI(val name: String, val creature: Creature) {

    fun takeAction() {
        //TODO - replace hardcoding with script informed / generic
        if (GameState.battle != null) {
            EventManager.postEvent(StartAttackEvent(creature, creature.body.getPart("Small Claws"), GameState.player.creature, TargetPosition(), AttackType.SLASH))
        }
    }

}