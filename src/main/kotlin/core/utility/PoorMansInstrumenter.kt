package core.utility

import java.text.NumberFormat
import java.util.*

class PoorMansInstrumenter(private val divisionFactor: Int = 100) {
    private var lastMeasure = System.nanoTime()

    fun printElapsed(message: String? = null) {

        val elapsed = NumberFormat.getNumberInstance(Locale.US).format((System.nanoTime() - lastMeasure)/ divisionFactor)
        lastMeasure = System.nanoTime()
        if (message != null) {
            println("$elapsed time elapsed: $message")
        } else {
            println("$elapsed time elapsed.")
        }
    }
}