package core.gameState

import combat.DamageType
import combat.attack.StartAttackEvent
import combat.battle.position.TargetAim
import system.EventManager

class AI(val name: String, val creature: Target) {

    fun takeAction() {
        //TODO - replace hardcoding with script informed / generic
        if (GameState.battle != null) {
            val playerBody = GameState.player.body
            val possibleParts = listOf(
                    playerBody.getPart("Right Leg"),
                    playerBody.getPart("Right Foot"),
                    playerBody.getPart("Left Leg"),
                    playerBody.getPart("Left Leg")
            )
            val targetPart = listOf(possibleParts.random())
            EventManager.postEvent(StartAttackEvent(creature, creature.body.getPart("Small Claws"), TargetAim(GameState.player, targetPart), DamageType.SLASH))
        }
    }

}