import core.GameState
import core.commands.CommandParsers
import core.history.GameLogger
import core.history.InputOutput
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.div
import kotlinx.html.dom.append
import org.w3c.dom.Element
import org.w3c.dom.Node

fun main() {
    window.onload = { document.body?.startClient() }
}

fun Node.sayHello() {
    append {
        div {
            +"Hello from JS"
        }
    }
}

fun Node.startClient() {
    val text = sampleText.split("\n").joinToString("<br/>")
    val outputDiv = document.getElementById("output-block")!!
    val prompt = document.getElementById("prompt")!!
    /*
    TODO
    - Read input and send to command parser
    - Print output to history (format inputs with leading >
    - Autocomplete
     */
    outputDiv.innerHTML = text + text + text + text

//    EventManager.registerListeners()
//    GameManager.newOrLoadGame()
//    EventManager.executeEvents()
//    CommandParsers.parseInitialCommand(GameState.player)
//    print(outputDiv)
    window.onkeypress = { keyboardEvent ->
        println(keyboardEvent.key)
        if (keyboardEvent.key == "Enter") {
            println("Submitting: " + prompt.textContent)
//            CommandParsers.parseCommand(GameState.player, prompt.textContent ?: "")
//            print(outputDiv)
        }
    }
    scrollToBottom()
}

private fun print(outputDiv: Element) {
    outputDiv.innerHTML = GameLogger.getMainHistory().history.joinToString("<br>") { it.toHtml() }
    scrollToBottom()
}

private fun InputOutput.toHtml(): String {
    return "> $input<br/>${outPut.joinToString("<br/>")}"
}

private fun scrollToBottom() {
    window.scrollTo(0.0, document.body?.scrollHeight?.toDouble() ?: 100.0)
}

private val sampleText = """
    You are at An Open Field (X: -100 to 150 and Y: -100 to 100).
    It contains Wheat Field (10, 0).
    > ls
    You are at An Open Field (X: -100 to 150 and Y: -100 to 100).
    It contains Wheat Field (10, 0).
    > exa
    You are at An Open Field (X: -100 to 150 and Y: -100 to 100).
    The waist high grasses stretch into the distance. They don't obscure your view, but as they drift in the wind they give you the sensation of floating on a calm sea.
    It is 8 light and 2 hot.
    It contains Wheat Field (10, 0).
    > n
    You leave An Open Field travelling towards Apple Tree.
    You travel to Apple Tree.
    It is neighbored by An Open Field (SOUTH), Apple Tree Branches (ABOVE).
    The journey decreases Your Stamina by 10 (90/100).
""".trimIndent()