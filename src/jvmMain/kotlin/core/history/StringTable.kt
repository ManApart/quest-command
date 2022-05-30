package core.history

class StringTable(private val grid: List<List<String>>, private val indentSpaces: Int = 0, private val delimiter: String = "", rightPadding: Int = 0) {
    private val widestInEachColumn = getWidestColumn(rightPadding)

    fun getString(): String {
        var lines = ""

        grid.forEach { row ->
            var line = "".padStart(indentSpaces)
            for (column in row.indices) {
                line += row[column].padEnd(widestInEachColumn[column]) + delimiter
            }
            lines += line + "\n"
        }

        return lines
    }

    private fun getWidestColumn(additionalPadding: Int): List<Int> {
        val gridSize = grid.maxByOrNull { it.size }?.size ?: 0
        val largest = IntArray(gridSize) { 0 }
        for (row in grid.indices) {
            for (column in grid[row].indices) {
                val size = grid[row][column].length
                if (size > largest[column]) {
                    largest[column] = size
                }
            }
        }
        return largest.map { it + additionalPadding }
    }

}