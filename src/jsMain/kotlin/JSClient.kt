import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import core.history.InputOutput
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement

private lateinit var outputDiv: Element
private lateinit var prompt: HTMLInputElement
private lateinit var suggestionsDiv: Element
private var suggestions = listOf<String>()
private var historyStart = 0

fun main() {
    window.onload = { document.startClient() }
}

fun Document.startClient() {
    outputDiv = getElementById("output-block")!!
    prompt = getElementById("prompt")!! as HTMLInputElement
    suggestionsDiv = getElementById("suggestions")!!

    GameManager.newOrLoadGame()
    EventManager.executeEvents()
    CommandParsers.parseInitialCommand(GameState.player)
    updateOutput()
    window.onkeyup = { keyboardEvent ->
        println("Pressed " + keyboardEvent.key)
        when (keyboardEvent.key) {
            "Enter" -> {
                val input = prompt.value
                prompt.value = ""
                if (input.lowercase() == "clear") {
                    clearScreen()
                } else {
                    CommandParsers.parseCommand(GameState.player, input)
                    updateOutput()
                }
            }
            "Tab" -> tabComplete()
            else -> tabHint()
        }
    }
    scrollToBottom()
}

fun addHistoryMessageAfterCallback(message: String) {
    /*
        The history was closed while we were doing the callback, so we need to add to the previous history
        Then refresh the page with the output
     */
    val histories = GameLogger.getMainHistory().history
    histories[histories.size - 1].outPut.add(message)
    updateOutput()
}

fun updateOutput() {
    val history = GameLogger.getMainHistory().history
    outputDiv.innerHTML = history.subList(historyStart, history.size).joinToString("<br>") { it.toHtml() }
    scrollToBottom()
}

private fun InputOutput.toHtml(): String {
    return "<br/>> $input<br/>${outPut.joinToString("<br/>") { it.replace("\n", "<br/>") }}"
}

private fun scrollToBottom() {
    window.scrollTo(0.0, document.body?.scrollHeight?.toDouble() ?: 100.0)
}

private fun clearScreen() {
    historyStart = GameLogger.getMainHistory().history.size
    updateOutput()
}

private fun tabHint() {
    val input = prompt.value.lowercase().split(" ").lastOrNull()
    if (!input.isNullOrBlank()) {
        suggestions = listOf("Option", "Option2", "Suggestion", "Idea").filter {
            val option = it.lowercase()
            option.startsWith(input)
        }
        suggestionsDiv.innerHTML = suggestions.joinToString(" ")
    }
}

private fun tabComplete() {
    prompt.focus()
    val input = prompt.value.split(" ").lastOrNull()
    if (!input.isNullOrBlank()) {
        val prePrompt = prompt.value.substring(0, prompt.value.indexOf(input))
        val overlap = suggestions.minOverlap()
        when {
            suggestions.size == 1 -> prompt.value = prePrompt + suggestions.first()
            suggestions.size > 1 && overlap.length > input.length -> prompt.value = prePrompt + overlap
            else -> {}
        }
        tabHint()
    }
}

private fun List<String>.minOverlap(): String {
    if (isEmpty()) return ""
    val example = first().toCharArray()
    val overlap = minOf { it.toCharArray().mapIndexed { i, c -> if (example[i] == c) 1 else 0 }.count() }
    return first().substring(0, overlap)
}