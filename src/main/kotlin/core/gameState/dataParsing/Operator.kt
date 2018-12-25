package core.gameState.dataParsing

/**
 * NOTE: Equal and not equal work with Int and String. Contains only works on strings. All other operators only work on Int values
 */
enum class Operator(val symbol: String) {
    EQUALS("=") {
        override fun compareString(left: String, right: String): Boolean {
            return left.toLowerCase() == right.toLowerCase()
        }

        override fun compareInt(left: Int, right: Int): Boolean {
            return left == right
        }
    },
    NOT_EQUALS("!=") {
        override fun compareString(left: String, right: String): Boolean {
            return left.toLowerCase() != right.toLowerCase()
        }

        override fun compareInt(left: Int, right: Int): Boolean {
            return left != right
        }
    },
    GREATER_THAN(">") {
        override fun compareString(left: String, right: String): Boolean {
            return false
        }

        override fun compareInt(left: Int, right: Int): Boolean {
            return left > right
        }
    },
    LESS_THAN("<") {
        override fun compareString(left: String, right: String): Boolean {
            return false
        }

        override fun compareInt(left: Int, right: Int): Boolean {
            return left < right
        }
    },
    GREATER_THAN_EQUALS_TO(">=") {
        override fun compareString(left: String, right: String): Boolean {
            return false
        }

        override fun compareInt(left: Int, right: Int): Boolean {
            return left >= right
        }
    },
    LESS_THAN_EQUALS_TO("<=") {
        override fun compareString(left: String, right: String): Boolean {
            return false
        }

        override fun compareInt(left: Int, right: Int): Boolean {
            return left <= right
        }
    },
    CONTAINS("contains"){
        override fun compareString(left: String, right: String): Boolean {
            return left.contains(right)
        }

        override fun compareInt(left: Int, right: Int): Boolean {
            return false
        }
    };

    fun evaluate(left: String, right: String) : Boolean {
        return if (left.toIntOrNull() != null && right.toIntOrNull() != null){
            compareInt(left.toInt(), right.toInt())
        } else {
            compareString(left, right)
        }
    }
    protected abstract fun compareString(left: String, right: String) : Boolean
    protected abstract fun compareInt(left: Int, right: Int) : Boolean

    companion object {
        fun getOperator(value: String) : Operator {
            val cleaned = value.toLowerCase().trim()
            return Operator.values().firstOrNull {
                cleaned == it.name.toLowerCase() || cleaned == it.symbol.toLowerCase()
            } ?: Operator.EQUALS
        }
    }
}