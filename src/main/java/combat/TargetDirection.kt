package combat

import core.utility.getRandomRange

enum class TargetDirection(private val aliases: List<String>) {
    TOP(listOf("Top", "t")),
    TOP_LEFT(listOf("Top Left", "tl")),
    TOP_RIGHT(listOf("Top Right", "tr")),
    MIDDLE(listOf("Middle", "m")),
    MIDDLE_LEFT(listOf("Middle Left", "ml")),
    MIDDLE_RIGHT(listOf("Middle Right", "mr")),
    BOTTOM(listOf("Bottom", "b")),
    BOTTOM_LEFT(listOf("Bottom Left", "bl")),
    BOTTOM_RIGHT(listOf("Bottom Right", "br"));

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