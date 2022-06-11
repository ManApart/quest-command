package wiki

import java.io.File

private val newLineAtLineStart = Regex("^\n")

internal fun getExistingText(filePath: String, matchText: String): String {
    val fullExisting = File(filePath).readText()

    val i = fullExisting.indexOf(matchText)

    return fullExisting.substring(0, i + matchText.length) + "\n"
}

internal fun prepareDescription(description: String): String {
    return prepareLines(description)
}

internal fun prepareManual(manual: String): String {
    return manual.split("\n").joinToString("<br/>") { line ->
        val dashes = line.split("-")
        if (dashes.size == 1) {
            line
        } else {
            val command = "`" + dashes[0].trim() + "`"
            val explanation = prepareLines(dashes[1])
            "$command - $explanation"
        }
    }

}

private fun prepareLines(lines: String): String {
    return lines
        .replace(newLineAtLineStart, "")
        .replace("<", "\\<")
        .replace("\n", "<br/>")
        .replace("\t", "")
        .trim()
}