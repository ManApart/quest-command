package core.utility

expect class PoorMansInstrumenter(divisionFactor: Int = 100) {
    fun printElapsed(message: String? = null)
}