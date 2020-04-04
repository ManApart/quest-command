package core.body

import system.persistance.clean

fun persist(dataObject: Body, path: String) {
    if (dataObject.name == NONE.name) {
        return
    }
    val prefix = clean(path, dataObject.name)
    traveling.location.persist(dataObject.layout, prefix)
}

@Suppress("UNCHECKED_CAST")
fun load(path: String, name: String): Body {
    val network = traveling.location.load(clean(path, name), name)
    return Body(name, network)
}