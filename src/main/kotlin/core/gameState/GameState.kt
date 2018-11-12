package core.gameState

import combat.battle.Battle
import travel.journey.Journey

object GameState {
//    val world = loadLocations()
    val player = Player()
    var journey: Journey? = null
    var battle: Battle? = null

    fun finishJourney(){
        journey = null
        player.canRest = true
    }

}