package explore

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import core.gameState.Target
import core.gameState.targetsToString
import interact.ScopeManager
import travel.climb.ClimbJourney

class Look : EventListener<LookEvent>() {


    override fun execute(event: LookEvent) {
        if (GameState.player.climbJourney != null && GameState.player.climbJourney is ClimbJourney) {
            describeClimbJourney()
        }else if (event.target != null){
            println(event.target.description)
            describeStatusEffects(event.target)
        } else {
            describeLocation()
        }
    }

    private fun describeClimbJourney() {
        val journey = GameState.player.climbJourney as ClimbJourney
        val abovePaths = if (journey.getHigherSegments().isNotEmpty()) {
            var i = 0
            "Above you are path choices " + journey.getHigherSegments().joinToString(", "){
                i++
                "$i"
            }
        } else {
            if (journey.getCurrentSegment().top){
                "You are at the top of ${journey.target.name}"
            } else {
                "Above you is nothing to climb on"
            }
        }
        val belowPaths = if (journey.getLowerSegments().isNotEmpty()) {
            var i = journey.getHigherSegments().size
            "Below you are path choices " + journey.getLowerSegments().joinToString(","){
                i++
                "$i"
            }
        } else {
            if (journey.getCurrentSegment().bottom){
                "You are at the bottom of ${journey.target.name}"
            } else {
                "Below you is nothing to climb on"
            }
        }
        println("You are ${journey.getCurrentDistance()}ft up. $abovePaths. $belowPaths.")
    }


    private fun describeStatusEffects(target: Target) {
        if (target is Activator && target.creature.soul.effects.isNotEmpty()){
            val effects = target.creature.soul.effects.joinToString(", ") { it.name }
            println("${target.name} is $effects")
        }
    }

    private fun describeLocation() {
        println(GameState.player.creature.location.getDescription())
        if (ScopeManager.getTargets().size > 1) {
            val targetList = targetsToString(ScopeManager.getTargets().filterNot { it == GameState.player })
            println("You find yourself surrounded by $targetList.")
        } else {
            println("You don't see anything of use.")
        }
    }


}