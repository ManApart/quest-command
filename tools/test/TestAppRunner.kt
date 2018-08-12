package test

import main

const val burnApple = "w && use tinder on tree && use apple on tree"
const val pickupAndBurnApple = "w && use tinder on tree && bag && pickup apple && bag && use apple on tree"
const val fightRat = "travel an open field barren patch && equip hatchet && chop rat && slash rat"



val initialCommand = fightRat


fun main(args: Array<String>) {
    main(arrayOf(initialCommand))
}