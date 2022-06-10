import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import core.history.InputOutput
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement

fun main() {
    window.onload = { document.startClient() }
}

fun Document.startClient() {
    val outputDiv = getElementById("output-block")!!
    val prompt = getElementById("prompt")!! as HTMLInputElement
    // TODO Autocomplete
    outputDiv.innerHTML = "Loading..."

    GameManager.newOrLoadGame()
    EventManager.executeEvents()
    CommandParsers.parseInitialCommand(GameState.player)
    print(outputDiv)
    window.onkeypress = { keyboardEvent ->
        println(keyboardEvent.key)
        if (keyboardEvent.key == "Enter") {
            CommandParsers.parseCommand(GameState.player, prompt.value)
            prompt.value = ""
            print(outputDiv)
        }
    }
    //Poll log for updates from callbacks
    GlobalScope.launch {
        while (true){
            print(outputDiv)
            delay(500)
        }
    }
    scrollToBottom()
}

private fun print(outputDiv: Element) {
    outputDiv.innerHTML = GameLogger.getMainHistory().history.joinToString("<br>") { it.toHtml() }
    scrollToBottom()
}

private fun InputOutput.toHtml(): String {
    return "<br/>> $input<br/>${outPut.joinToString("<br/>") {it.replace("\n", "<br/>")}}"
}

private fun scrollToBottom() {
    window.scrollTo(0.0, document.body?.scrollHeight?.toDouble() ?: 100.0)
}