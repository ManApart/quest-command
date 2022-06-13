import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.GroupLayout
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField

fun runInGui() {
    TerminalGui()
}

class TerminalGui : JFrame() {
    init {
        title = "Quest Command"
        setSize(800, 600)
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

        val sampleText = (1..100).joinToString { "This is some text" }
        val output = JTextArea(sampleText).apply {
            isEditable = false
            lineWrap = true
            background = Color.black
            foreground = Color.white
        }
        val outputScroll = JScrollPane(output).apply {
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
            preferredSize = Dimension(400, 400)
        }
        contentPane.add(outputScroll, BorderLayout.CENTER)

        val prompt = JTextField("Inital").apply {
            background = Color.BLACK
            foreground = Color.WHITE
        }
        contentPane.add(prompt, BorderLayout.SOUTH)


        pack()
    }
}