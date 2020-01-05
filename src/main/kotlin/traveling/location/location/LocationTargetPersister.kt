package traveling.location.location

fun getPersisted(dataObject: LocationTarget): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["position"] = dataObject.position.toMap()


    if (dataObject.location != null) {
        data["location"] = dataObject.location
    }

    return data
}

@Suppress("UNCHECKED_CAST")
fun readLocationTargetFromData(data: Map<String, Any>): LocationTarget {
//    val location = BodyManager.getBody(data["name"] as String)

    return Location()
}