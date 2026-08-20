package inventory

import system.persistance.clean
import system.persistance.getFiles

suspend fun persist(dataObject: Inventory, path: String) {
    dataObject.getItems().forEach { core.thing.persist(it, path) }
}

suspend fun load(path: String): Inventory {
    //TODO - paths check
    val folderPath = path.removeSuffix(".json")
    val items = getFiles(clean(folderPath, folderPath)).map { core.thing.load(it.path) }
    return Inventory(items)
}
