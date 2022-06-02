import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Node
import kotlinx.html.div
import kotlinx.html.dom.append

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