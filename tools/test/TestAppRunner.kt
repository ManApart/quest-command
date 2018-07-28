package test

import main

const val burnApple = "w && use tinder on tree && use apple on tree"
const val pickupAndBurnApple = "w && use tinder on tree && bag && pickup apple && bag && use apple on tree"



val initialCommand = pickupAndBurnApple


fun main(args: Array<String>) {
    main(arrayOf(initialCommand))
}