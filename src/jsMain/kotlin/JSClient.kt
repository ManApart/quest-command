@file:OptIn(DelicateCoroutinesApi::class)

import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import core.history.InputOutput
import core.history.TerminalPrinter.optionsToPrintIfTheyExist
import core.utility.capitalize2
import core.utility.minOverlap
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import system.connection.WebClient
import system.help.getCommandGroups
import system.persistance.createDB

private lateinit var outputDiv: Element
private lateinit var prompt: HTMLInputElement
private lateinit var suggestionsDiv: Element
private var suggestions = listOf<String>()
private var historyStart = 0

fun main() {
    window.onload = {
        createDB()
        document.startClient()
    }
}

fun Document.startClient() {
    outputDiv = getElementById("output-block")!!
    prompt = getElementById("prompt")!! as HTMLInputElement
    suggestionsDiv = getElementById("suggestions")!!

    CoroutineScope(GlobalScope.coroutineContext).launch {
        GameManager.newOrLoadGame()
        EventManager.processEvents()
        CommandParsers.parseInitialCommand(GameState.player)
        updateOutput()
        window.onkeyup = { keyboardEvent ->
            CoroutineScope(GlobalScope.coroutineContext).launch {
//        println("Pressed " + keyboardEvent.key)
                when (keyboardEvent.key) {
                    "Enter" -> {
                        submitCommand(prompt.value)
                    }

                    "Tab" -> tabComplete()
                    else -> tabHint()
                }
            }
        }
        document.onclick = { e ->
            CoroutineScope(GlobalScope.coroutineContext).launch {
                if (e.target is HTMLButtonElement) {
                    clickSuggestion(e.target as HTMLButtonElement)
                }
            }
        }
        tabHint()
        scrollToBottom()
    }
}

private suspend fun submitCommand(input: String) {
    prompt.value = ""
    suggestionsDiv.innerHTML = ""
    if (input.lowercase() == "clear") {
        clearScreen()
    } else {
        CommandParsers.parseCommand(GameState.player, input)
        updateOutput()
    }
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
    //TODO - implement response options if connected to server
    if (!isConnectedToServer()) {
        val suggestions = CommandParsers.getParser(GameState.player).getResponseRequest()?.getOptions() ?: getCommandGroups().map { "commands $it" }
        updateSuggestions(null, suggestions)
    }
    scrollToBottom()
}

private fun InputOutput.toHtml(): String {
    return "<br/>> $input<br/>${outPut.joinToString("<br/>") { it.replace("\n", "<br/>") }}"
}

private fun scrollToBottom() {
    window.scrollTo(0.0, document.body?.scrollHeight?.toDouble() ?: 100.0)
    (document.getElementById("prompt") as HTMLElement).focus()
}

private fun clearScreen() {
    historyStart = GameLogger.getMainHistory().history.size
    updateOutput()
}

private suspend fun tabComplete() {
    prompt.focus()
    val input = prompt.value.split(" ").lastOrNull()
    if (!input.isNullOrBlank()) {
        val prePrompt = prompt.value.substring(0, prompt.value.indexOf(input))
        val overlap = suggestions.minOverlap()
        println("Suggestions: ${suggestions.joinToString()}")
        when {
            suggestions.size == 1 -> prompt.value = prePrompt + suggestions.first()
            suggestions.size > 1 && overlap.length > input.length -> prompt.value = prePrompt + overlap
        }
        tabHint()
    }
}

private suspend fun clickSuggestion(button: HTMLButtonElement) = submitCommand(button.getAttribute("command") ?: "")

private suspend fun tabHint() {
    val input = prompt.value.lowercase()
    val lastInput = input.split(" ").lastOrNull()
    if (input.isNotBlank()) {
        if (isConnectedToServer()) {
            updateSuggestions(lastInput, WebClient.getSuggestions(input))
        } else {
            updateSuggestions(lastInput, CommandParsers.suggestions(GameState.player, input))
        }
    } else {
        updateSuggestions(lastInput, getCommandGroups().map { "commands $it" })
    }
}

private fun isConnectedToServer() = CommandParsers.getParser(GameState.player).commandInterceptor != null

private fun updateSuggestions(lastInput: String?, allSuggestions: List<String>) {
    suggestions = if (lastInput.isNullOrBlank()) {
        allSuggestions
    } else {
        allSuggestions.filter {
            val option = it.lowercase()
            option.startsWith(lastInput)
        }
    }
    suggestionsDiv.innerHTML = suggestions.joinToString(" ") {
        val display = if (it.startsWith("commands ")) it.replace("commands ", "") else it
        """<button class="suggestion-button" command="$it">${display.capitalize2()}</button>"""
    }
}