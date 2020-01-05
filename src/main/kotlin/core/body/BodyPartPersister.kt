package core.body

fun getPersisted(dataObject: BodyPart): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["layout"] = traveling.location.getPersisted(dataObject.layout)
//    data["parts"] = dataObject.layout.getLocations().map { traveling.location.location.getPersisted(it) }

    return data
}

@Suppress("UNCHECKED_CAST")
fun readBodyPartFromData(data: Map<String, Any>): BodyPart {
    val name = data["name"] as String
    val network = traveling.location.readFromData(data["layout"] as Map<String, Any>)
    val body = Body(name, network)
    //TODO Re equip items?
    return body
}