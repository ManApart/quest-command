import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import core.history.InputOutput
import core.history.displayToMe
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement

private lateinit var outputDiv: Element

fun main() {
    window.onload = { document.startClient() }
}

fun Document.startClient() {
    outputDiv = getElementById("output-block")!!
    val prompt = getElementById("prompt")!! as HTMLInputElement
    // TODO Autocomplete
    outputDiv.innerHTML = "Loading..."

    GameManager.newOrLoadGame()
    EventManager.executeEvents()
    CommandParsers.parseInitialCommand(GameState.player)
    updateOutput()
    window.onkeypress = { keyboardEvent ->
        if (keyboardEvent.key == "Enter") {
            CommandParsers.parseCommand(GameState.player, prompt.value)
            prompt.value = ""
            updateOutput()
        }
    }
    scrollToBottom()
}

fun addHistoryMessageAfterCallback(message: String){
    /*
        The history was closed while we were doing the callback, so we need to add to the previous history
        Then refresh the page with the output
     */
    val histories = GameLogger.getMainHistory().history
    histories[histories.size-1].outPut.add(message)
    updateOutput()
}

fun updateOutput() {
    outputDiv.innerHTML = GameLogger.getMainHistory().history.joinToString("<br>") { it.toHtml() }
    scrollToBottom()
}

private fun InputOutput.toHtml(): String {
    return "<br/>> $input<br/>${outPut.joinToString("<br/>") {it.replace("\n", "<br/>")}}"
}

private fun scrollToBottom() {
    window.scrollTo(0.0, document.body?.scrollHeight?.toDouble() ?: 100.0)
}