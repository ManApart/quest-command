package core.utility

import java.text.NumberFormat
import java.util.*

actual class PoorMansInstrumenter actual constructor(private val divisionFactor: Int) {
    private var lastMeasure = System.nanoTime()

    actual fun printElapsed(message: String?) {

        val elapsed = NumberFormat.getNumberInstance(Locale.US).format((System.nanoTime() - lastMeasure)/ divisionFactor)
        lastMeasure = System.nanoTime()
        if (message != null) {
            println("$elapsed time elapsed: $message")
        } else {
            println("$elapsed time elapsed.")
        }
    }
}