package inventory

import system.persistance.clean
import system.persistance.clearFolder
import system.persistance.getFiles

suspend fun persist(dataObject: Inventory, path: String) {
    clearFolder(path)
    dataObject.getItems().forEach { core.thing.persist(it, path) }
}

suspend fun load(path: String): Inventory {
    return getFiles(clean(path)).map { core.thing.load(it.path) }.let { Inventory(it) }
}
