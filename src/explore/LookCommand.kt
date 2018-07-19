package explore

import core.commands.Command
import core.gameState.GameState
import interact.ScopeManager
import core.gameState.targetsToString
import travel.climb.ClimbJourney

class LookCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Look", "ls", "Examine", "Exa")
    }

    override fun getDescription(): String {
        return "Look:\n\tExamine your surroundings"
    }

    override fun getManual(): String {
        return "\n\tLook - View the objects you can interact with." +
                "\n\tLook <target> - Look at a specific target."
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (args.isEmpty()) {
            if (GameState.journey != null && GameState.journey is ClimbJourney){
                describeClimbJourney()
            } else {
                describeLocation()
            }
        } else if (ScopeManager.targetExists(args)) {
            val target = ScopeManager.getTarget(args)
            println(target.description)
        } else {
            println("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun describeClimbJourney() {
        val journey = GameState.journey as ClimbJourney
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

    private fun describeLocation() {
        println(GameState.player.location.getDescription())
        if (ScopeManager.getTargets().size > 1) {
            val targetList = targetsToString(ScopeManager.getTargets().filterNot { it == GameState.player })
            println("You find yourself surrounded by $targetList.")
        } else {
            println("You don't see anything of use.")
        }
    }
}