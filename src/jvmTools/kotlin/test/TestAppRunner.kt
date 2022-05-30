package test

import main

const val initialCommand: String = "w && n && w && debug recipe && recipe dagger && bag chest && exa forge && mv to chest && take tinder && take all from chest && mv to forge && debug stat smithing 2 && use tinder on forge && craft dagger"

fun main() {
    main(arrayOf(initialCommand))
}