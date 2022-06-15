//import java.awt.*
//import javax.swing.JButton
//import javax.swing.JComponent
//import javax.swing.JScrollBar
//import javax.swing.plaf.basic.BasicScrollBarUI
//
//
//class CustomScrollBarUI : BasicScrollBarUI() {
//    private val d = Dimension()
//    override fun createDecreaseButton(orientation: Int): JButton {
//        return object : JButton() {
//            private static
//            val serialVersionUID = -3592643796245558676L
//            override fun getPreferredSize(): Dimension {
//                return d
//            }
//        }
//    }
//
//    override fun createIncreaseButton(orientation: Int): JButton {
//        return object : JButton() {
//            private static
//            val serialVersionUID = 1L
//            override fun getPreferredSize(): Dimension {
//                return d
//            }
//        }
//    }
//
//    override fun paintTrack(g: Graphics, c: JComponent, r: Rectangle) {}
//    override fun paintThumb(g: Graphics, c: JComponent, r: Rectangle) {
//        val g2 = g.create() as Graphics2D
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//        var color: Color? = null
//        val sb = c as JScrollBar
//        color = if (!sb.isEnabled || r.width > r.height) {
//            return
//        } else if (isDragging) {
//            Color.DARK_GRAY // change color
//        } else if (isThumbRollover) {
//            Color.LIGHT_GRAY // change color
//        } else {
//            Color.GRAY // change color
//        }
//        g2.paint = color
//        g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10)
//        g2.paint = Color.WHITE
//        g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10)
//        g2.dispose()
//    }
//
//    override fun setThumbBounds(x: Int, y: Int, width: Int, height: Int) {
//        super.setThumbBounds(x, y, width, height)
//        scrollbar.repaint()
//    }
//}