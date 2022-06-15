import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.event.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.border.EmptyBorder

val background = Color(34, 34, 34)
val text = Color(238, 238, 238)
val mid = Color(65, 65, 65)
val highlight = Color.gray

fun runInGui() {
    TerminalGui()
}

class TerminalGui : JFrame(){
    init {
        title = "Quest Command"
        iconImage = Toolkit.getDefaultToolkit().getImage(this::class.java.getResource("favicon.png"))
        setSize(1000, 500)
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

        val sampleText = (1..100).joinToString { "This is some text" }
        val output = JTextArea(sampleText).apply {
            isEditable = false
            lineWrap = true
            background = Color.black
            foreground = Color.white
            border = EmptyBorder(10, 10, 10, 10)
        }
        val outputScroll = JScrollPane(output).apply {
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
            preferredSize = Dimension(400, 400)
            border = null
            verticalScrollBar.setUI(CustomScrollBarUI())
        }
        contentPane.add(outputScroll, BorderLayout.CENTER)

        val bottomPart = JPanel().apply { layout = BorderLayout() }

        val prompt = JTextField("Inital").apply {
            background = Color.BLACK
            foreground = Color.WHITE
            border = EmptyBorder(10, 10, 10, 10)
            addFocusListener(object : FocusListener{
                override fun focusGained(e: FocusEvent?) {
                    println("Focus gained")
                }

                override fun focusLost(e: FocusEvent?) {
                    this@apply.grabFocus()
                }

            })
            addActionListener {
                println("action performend $it")
                output.append(text)
                text = ""
            }
        }
        bottomPart.add(prompt, BorderLayout.CENTER)

        val suggestions = JTextArea("option option option").apply {
            isEditable = false
            lineWrap = true
            background = Color.black
            foreground = Color.white
            border = EmptyBorder(10, 10, 10, 10)
        }
        bottomPart.add(suggestions, BorderLayout.SOUTH)

        contentPane.add(bottomPart, BorderLayout.SOUTH)


        pack()
    }
}