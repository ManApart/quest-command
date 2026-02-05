@file:OptIn(DelicateCoroutinesApi::class)

import core.GameManager
import core.GameState
import core.commands.Command
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import core.history.InputOutput
import core.utility.capitalize2
import core.utility.minOverlap
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLObjectElement
import org.w3c.dom.svg.SVGPathElement
import system.connection.WebClient
import system.help.getCommandGroups
import system.persistance.createDB

private lateinit var outputDiv: Element
private lateinit var prompt: HTMLInputElement
private lateinit var loading: Element
private lateinit var suggestionsDiv: Element
private var suggestions = listOf<String>()
private var historyStart = 0
private var darkTheme = true

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
    loading = getElementById("loading")!!

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
                    val button = (e.target as HTMLButtonElement)
                    if (button.id == "theme-toggle") toggleTheme() else clickSuggestion(button)
                } else if (e.target is HTMLObjectElement && (e.target as HTMLObjectElement).parentElement is HTMLButtonElement) {
                    if (((e.target as HTMLObjectElement).parentElement as HTMLButtonElement).id == "theme-toggle") toggleTheme()
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
        loading.removeClass("hidden")
        prompt.addClass("hidden")
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
    outputDiv.innerHTML = history.subList(historyStart, history.size).filter { it.input.isNotBlank() || it.outPut.isNotEmpty() }.joinToString("<br>") { it.toHtml() }
    //TODO - implement response options if connected to server
    if (!isConnectedToServer()) {
        val suggestions = CommandParsers.getParser(GameState.player).getResponseRequest()?.getOptions()?.associateWith { it } ?: defaultSuggestions(history.last())
        updateSuggestions(suggestions)
    }
    loading.addClass("hidden")
    prompt.removeClass("hidden")
    scrollToBottom()
}

private fun InputOutput.toHtml(): String {
    return "<br/>> $input<br/>${outPut.joinToString("<br/>") { it.replace("<", "`").replace(">", "`").replace("\n", "<br/>") }}"
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
            suggestions.size == 1 -> prompt.value = prePrompt + suggestions.first() + " "
            suggestions.size > 1 && overlap.length > input.length -> prompt.value = prePrompt + overlap
        }
        tabHint()
    }
}

private suspend fun clickSuggestion(button: HTMLButtonElement) = submitCommand(button.getAttribute("command") ?: "")

private suspend fun tabHint() {
    val input = prompt.value.lowercase()
    if (input.isNotBlank()) {
        if (isConnectedToServer()) {
            updateSuggestions(WebClient.getSuggestions(input))
        } else {
            updateSuggestions(CommandParsers.suggestions(GameState.player, input))
        }
    } else {
        updateSuggestions(defaultSuggestions())
    }
}

private fun defaultSuggestions(previousCommand: InputOutput? = null): Map<TabDisplay, TabCommand> {
    if (previousCommand?.input == "alias") {
        return previousCommand.outPut.flatMap { it.split("\n") }.drop(1)
            .mapNotNull { line -> line.split(" ").firstOrNull { it.isNotBlank() } }
            .associateWith { it }
    }
    return (listOf("alias" to "alias") + getCommandGroups().map { it to "commands $it" }).toMap()
}

private fun isConnectedToServer() = CommandParsers.getParser(GameState.player).commandInterceptor != null

private typealias TabDisplay=String
private typealias TabCommand=String
private fun updateSuggestions(allSuggestions: List<String>) = updateSuggestions(allSuggestions.associateWith { it })
private fun updateSuggestions(displayToCommand: Map<TabDisplay, TabCommand>) {
    suggestions = displayToCommand.values.toList()
    val previous = GameLogger.getMainHistory().history.last().input

    val back = if (previous.startsWith("commands ") || previous == "alias") {
        """<button class="suggestion-button" command="">Back</button>"""
    } else ""
    suggestionsDiv.innerHTML = back + displayToCommand.entries.joinToString(" ") { (display, cmd) ->
        """<button class="suggestion-button" command="$cmd">${display.capitalize2()}</button>"""
    }
}

private fun toggleTheme() {
    val svg = (document.getElementById("theme-icon") as HTMLObjectElement).getSVGDocument()!!
    with(document.body!!.style) {
        darkTheme = !darkTheme
        if (darkTheme) {
            setProperty("--background", "#222222")
            setProperty("--text", "rgb(238, 238, 238)")
            setProperty("--mid", "rgb(65, 65, 65)")
            setProperty("--prompt", "#151515")
            setProperty("--highlight", "gray")
            svg.toggleColor("circle", "stroke", "#efd79c")
            svg.toggleColor("rays", "fill", "#efd79c")
        } else {
            setProperty("--background", "#efd79c")
            setProperty("--text", "#222222")
            setProperty("--mid", "rgb(65, 65, 65)")
            setProperty("--prompt", "#ceba87")
            setProperty("--highlight", "gray")
            svg.toggleColor("circle", "stroke", "#222222")
            svg.toggleColor("rays", "fill", "#222222")
        }
    }
}

private fun Document.toggleColor(id: String, prop: String, color: String) {
    val el = getElementById(id)!!
    if (el.tagName.equals("path", ignoreCase = true)) {
        el.unsafeCast<SVGPathElement>().style.setProperty(prop, color)
    }
}
