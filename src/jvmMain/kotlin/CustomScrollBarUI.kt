import java.awt.*
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JScrollBar
import javax.swing.plaf.basic.BasicScrollBarUI

private val d = Dimension()
private val decreaseButton = object : JButton() {
    override fun getPreferredSize(): Dimension {
        return d
    }
}

private val increaseButton = object : JButton() {
    override fun getPreferredSize(): Dimension {
        return d
    }
}

class CustomScrollBarUI : BasicScrollBarUI() {
    override fun createDecreaseButton(orientation: Int) = decreaseButton
    override fun createIncreaseButton(orientation: Int) = increaseButton

    override fun paintTrack(g: Graphics, c: JComponent, r: Rectangle) {}
    override fun paintThumb(g: Graphics, c: JComponent, r: Rectangle) {
        val g2 = g.create() as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val sb = c as JScrollBar
        if (!sb.isEnabled || r.width > r.height) return
        val color = when {
            isDragging -> CSS.mid
            isThumbRollover -> CSS.highlight
            else -> CSS.mid
        }

        g2.paint = color
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10)
        g2.dispose()
    }

    override fun setThumbBounds(x: Int, y: Int, width: Int, height: Int) {
        super.setThumbBounds(x, y, width, height)
        scrollbar.repaint()
    }
}