package test

import main

const val burnApple = "w && e && n && use tinder on tree && use apple on tree"
const val pickupAndBurnApple = "w && use tinder on tree && bag && pickup apple && bag && use apple on tree"
const val climbTree = "move tree && climb tree"
const val fightRat = "travel an open field barren patch && equip hatchet && chop rat && slash rat"
const val cookMeatTest = "move farmer's hut interior && cook Raw Poor Quality Meat on range"
const val useGate = "w && w && w && use gate"


const val initialCommand: String = burnApple


fun main(args: Array<String>) {
    main(arrayOf(initialCommand))
}