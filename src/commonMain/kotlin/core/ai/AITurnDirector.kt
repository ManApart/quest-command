package core.ai

import core.GameState
import core.events.EventManager
import time.gameTick.GameTickEvent
/**
 Make sure everyone with an AI is doing something
 Keep the game ticking so long as all creatures have stuff to do
 If a player doesn't have an action, pause the loop
 */
suspend fun directAI() {
    val creatures = GameState.players.values.flatMap { it.thing.location.getLocation().getCreatures(it.thing) }.toSet()

    creatures.map { it.mind.ai }.filter { it.actions.isEmpty() }.forEach {
        it.creature.body.blockHelper.resetStance()
        it.chooseAction()
    }

    //In multiplayer we don't want to force all players to wait on a slow player
    if (creatures.filter { it.isPlayer() }.any { it.mind.ai.actions.isEmpty() }){
        //Give player a chance to take an action
    } else{
        EventManager.postEvent(GameTickEvent())
    }

}
