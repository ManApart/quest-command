package combat.battle.position

import core.utility.getRandomRange

enum class TargetDirection(val position: TargetPosition, private val aliases: List<String>) {
    TOP(TargetPosition(vertical = Vertical.HIGH), listOf("Top", "t")),
    TOP_LEFT(TargetPosition(Horizontal.LEFT, Vertical.HIGH), listOf("Top Left", "High Left", "tl")),
    TOP_RIGHT(TargetPosition(Horizontal.RIGHT, Vertical.HIGH), listOf("Top Right", "High Right", "tr")),
    MIDDLE(TargetPosition(), listOf("Middle", "m", "Center")),
    MIDDLE_LEFT(TargetPosition(Horizontal.LEFT), listOf("Middle Left", "Center Left", "ml")),
    MIDDLE_RIGHT(TargetPosition(Horizontal.RIGHT), listOf("Middle Right", "Center Right", "mr")),
    BOTTOM(TargetPosition(vertical = Vertical.LOW), listOf("Bottom", "Low", "b")),
    BOTTOM_LEFT(TargetPosition(Horizontal.LEFT, Vertical.LOW), listOf("Bottom Left", "Low Left", "bl")),
    BOTTOM_RIGHT(TargetPosition(Horizontal.RIGHT, Vertical.LOW), listOf("Bottom Right", "Low Right", "br"));

    override fun toString(): String {
        return aliases[0]
    }

    companion object {
        fun getPrimaryAliases() : List<String> {
            return TargetDirection.values().map { it.aliases[0] }
        }

        fun getAllAliases() : List<String> {
            return TargetDirection.values().flatMap { it.aliases }
        }

        fun getTargetDirection(line: String) : TargetDirection? {
            val targets = getAllPossibleDirections(line)
            return getBestMatch(targets)
        }

        fun getRandom(): TargetDirection {
            val i = getRandomRange(TargetDirection.values().size)
            return TargetDirection.values()[i]
        }

        private fun getAllPossibleDirections(line: String) : List<TargetDirection> {
            val targets = mutableListOf<TargetDirection>()
            values().forEach { direction ->
                direction.aliases.forEach {
                    val word = if (it.length <= 2) {
                        " $it "
                    } else {
                        it
                    }
                    if (line.contains(word, true)) {
                        targets.add(direction)
                    }
                }
            }
            return targets
        }

        private fun getBestMatch(targets: List<TargetDirection>) : TargetDirection? {
            return if (targets.isNotEmpty()) {
                var target = targets[0]
                var maxLength = target.aliases[0].length
                targets.forEach {
                    if (it.aliases[0].length > maxLength){
                        target = it
                        maxLength = target.aliases[0].length
                    }
                }
                target
            } else {
                null
            }
        }

    }

}