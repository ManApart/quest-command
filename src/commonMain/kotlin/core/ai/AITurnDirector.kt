package core.ai

import core.GameState

/**
Make sure everyone with an AI is doing something
Keep the game ticking so long as all creatures have stuff to do
If a player doesn't have an action, pause the loop
 Returns true if it's a players turn and we need to pause
 */
suspend fun directAI(): Boolean {
    val creatureAIs = GameState.players.values.flatMap { it.thing.location.getLocation().getCreatures(it.thing) }.toSet().map { it.mind.ai }

    creatureAIs.filter { it.actions.isEmpty() }.forEach {
        it.creature.body.blockHelper.resetStance()
        it.chooseAction()
    }

    //In multiplayer we don't want to force all players to wait on a slow player
    //Give player a chance to take an action
   return creatureAIs.filter { it.creature.isPlayer() }.all { it.actions.isEmpty() }
}
