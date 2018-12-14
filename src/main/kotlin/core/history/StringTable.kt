package core.history

class StringTable(private val grid: List<List<String>>, private val indentSpaces: Int = 0, private val delimiter: String = "", rightPadding: Int = 0) {
    private val widestInEachColumn = getWidestColumn(rightPadding)

    fun getString(): String {
        var lines = ""

        grid.forEach { row ->
            var line = "".padStart(indentSpaces)
            for (column in 0 until row.size) {
                line += row[column].padEnd(widestInEachColumn[column]) + delimiter
            }
            lines += line + "\n"
        }

        return lines
    }

    private fun getWidestColumn(additionalPadding: Int): List<Int> {
        val gridSize = grid.maxBy { it.size }?.size ?: 0
        val largest = IntArray(gridSize) { _ -> 0 }
        for (row in 0 until grid.size) {
            for (column in 0 until grid[row].size) {
                val size = grid[row][column].length
                if (size > largest[column]) {
                    largest[column] = size
                }
            }
        }
        return largest.map { it + additionalPadding }
    }

}