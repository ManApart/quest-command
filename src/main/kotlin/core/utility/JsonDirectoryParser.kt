package core.utility

object JsonDirectoryParser {

    fun <E> parseDirectory(directoryPath: String, parser: (path: String) -> List<E>): List<E> {
        return ResourceHelper.getResourceFiles(directoryPath).map {
            parser("$it")
        }.flatten()
    }



}