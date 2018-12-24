package explore

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import core.gameState.Target
import core.gameState.targetsToString
import core.history.display
import explore.ClimbLook.describeClimbJourney
import interact.scope.ScopeManager
import travel.climb.ClimbJourney

class Look : EventListener<LookEvent>() {


    override fun execute(event: LookEvent) {
        if (GameState.player.climbJourney != null && GameState.player.climbJourney is ClimbJourney) {
            ClimbLook.describeClimbJourney()
        } else if (event.target != null) {
            display(event.target.description)
            describeStatusEffects(event.target)
        } else {
            describeLocation()
        }
    }

    private fun describeStatusEffects(target: Target) {
        if (target is Activator && target.creature.soul.effects.isNotEmpty()) {
            val effects = target.creature.soul.effects.joinToString(", ") { it.name }
            display("${target.name} is $effects")
        }
    }

    private fun describeLocation() {
        display(GameState.player.creature.location.getDescription())
        if (ScopeManager.getTargets().size > 1) {
            val targetList = targetsToString(ScopeManager.getTargets().filterNot { it == GameState.player })
            display("You find yourself surrounded by $targetList.")
        } else {
            display("You don't see anything of use.")
        }
    }


}