package test

import main

const val initialCommand: String = "w && n && sw && rest 10 && w && rs 10 && sw && rs 10 && cl"

fun main() {
    main(arrayOf(initialCommand))
}