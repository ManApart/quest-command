import core.GameState
import core.commands.CommandParsers
import core.history.GameLogger
import core.utility.minOverlap
import kotlinx.coroutines.runBlocking
import system.connection.WebClient
import java.awt.*
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import javax.swing.border.EmptyBorder

object CSS {
    val background = Color(34, 34, 34)
    val text = Color(238, 238, 238)
    val mid = Color(65, 65, 65)
    val highlight = Color.gray
    val font = Font("Monaco", Font.PLAIN, 20)
}

fun runInGui() {
    println("Starting gui")
    TerminalGui()
}

class TerminalGui : JFrame() {
    private val output: JTextArea
    private val suggestionsArea: JTextArea
    private val prompt: JTextField
    private var historyStart = 0
    private var suggestions = listOf<String>()

    init {
        title = "Quest Command"
        iconImage = Toolkit.getDefaultToolkit().getImage(this::class.java.getResource("favicon.png"))
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

        output = JTextArea("Loading...").apply {
            isEditable = false
            lineWrap = true
            background = CSS.background
            foreground = CSS.text
            border = EmptyBorder(10, 10, 10, 10)
            font = CSS.font
        }
        val outputScroll = JScrollPane(output).apply {
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
            preferredSize = Dimension(400, 400)
            border = null
            verticalScrollBar.setUI(CustomScrollBarUI())
            verticalScrollBar.background = CSS.background
        }
        contentPane.add(outputScroll, BorderLayout.CENTER)

        val bottomPart = JPanel().apply {
            background = CSS.background
            layout = BorderLayout()
        }

        suggestionsArea = JTextArea().apply {
            isEditable = false
            lineWrap = true
            background = CSS.background
            foreground = CSS.text
            border = EmptyBorder(10, 10, 10, 10)
            font = CSS.font
        }
        bottomPart.add(suggestionsArea, BorderLayout.SOUTH)

        prompt = JTextField().apply {
            background = CSS.background
            foreground = CSS.text
            border = EmptyBorder(10, 10, 10, 10)
            font = CSS.font
            caretColor = Color.white

            addKeyListener(object : KeyListener {
                override fun keyTyped(e: KeyEvent) {}
                override fun keyPressed(e: KeyEvent) {}
                override fun keyReleased(e: KeyEvent) {
//                    println("released ${e.keyChar}")
                    runBlocking { tabHint() }
                }
            })

            addActionListener {
                runBlocking {
                    CommandParsers.parseCommand(GameState.player, text)
                    text = ""
                    updateOutput()
                    suggestionsArea.text = ""
                }
            }
        }
        bottomPart.add(prompt, BorderLayout.CENTER)

        bottomPart.add(JLabel(">").apply {
            foreground = CSS.text
            border = EmptyBorder(10, 10, 10, 10)
            font = CSS.font
        }, BorderLayout.WEST)

        contentPane.add(bottomPart, BorderLayout.SOUTH)

        suggestionsArea.addFocusListener(object : FocusListener {
            override fun focusGained(e: FocusEvent?) {
                runBlocking {
                    tabComplete()
                    prompt.grabFocus()
                }
            }

            override fun focusLost(e: FocusEvent?) {}
        })

        runBlocking {
            CommandParsers.parseInitialCommand(GameState.player)
            updateOutput()
            prompt.grabFocus()
            pack()
            setSize(1000, 800)
        }
    }

    private fun updateOutput() {
        val history = GameLogger.getMainHistory().history
        output.text = history.subList(historyStart, history.size).joinToString("\n") {
            "\n> ${it.input}\n${it.outPut.joinToString("\n")}"
        }
    }

    private suspend fun tabComplete() {
        val input = prompt.text.split(" ").lastOrNull()
        if (!input.isNullOrBlank()) {
            val prePrompt = prompt.text.substring(0, prompt.text.indexOf(input))
            val overlap = suggestions.minOverlap()
//            println("Suggestions: ${suggestions.joinToString()}")
            when {
                suggestions.size == 1 -> prompt.text = prePrompt + suggestions.first()
                suggestions.size > 1 && overlap.length > input.length -> prompt.text = prePrompt + overlap
            }
            tabHint()
        } else if (suggestions.size == 1) {
            prompt.text = prompt.text + suggestions.first()
        }
    }

    private suspend fun tabHint() {
        val input = prompt.text.lowercase()
        val lastInput = input.split(" ").lastOrNull()
        if (input.isNotBlank()) {

            if (CommandParsers.getParser(GameState.player).commandInterceptor != null) {
                WebClient.getSuggestions(input) { suggestions ->
                    updateSuggestions(lastInput, suggestions)
                }
            } else {
                updateSuggestions(lastInput, CommandParsers.suggestions(GameState.player, input))
            }
        }
    }

    private fun updateSuggestions(lastInput: String?, allSuggestions: List<String>) {
        suggestions = if (lastInput.isNullOrBlank()) {
            allSuggestions
        } else {
            allSuggestions.filter {
                val option = it.lowercase()
                option.startsWith(lastInput)
            }
        }

        suggestionsArea.text = suggestions.joinToString(" ")
    }
}