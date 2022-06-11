package core.utility

import core.DependencyInjector

object JsonDirectoryParser {

    fun <E> parseDirectory(directoryPath: String, parser: (path: String) -> List<E>): List<E> {
        val helper = DependencyInjector.getImplementation(ResourceHelper::class)
        return helper.getResourceFiles(directoryPath).map {
            parser(it)
        }.flatten()
    }



}