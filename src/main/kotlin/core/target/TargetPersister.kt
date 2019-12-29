package core.target


import system.persistance.Persistable
import system.persistance.Persister

class TargetPersister : Persistable<Target> {

    override fun getVersion(): Int {
        return 0
    }

    override fun getPersisted(dataObject: Target): Map<String, Any> {
        val data = mutableMapOf<String, Any>()
        data["name"] = dataObject.name
        data["properties"] = Persister.getPersisted(dataObject.properties)
        return data
    }

    @Suppress("UNCHECKED_CAST")
    override fun applyData(data: Map<String, Any>): Target {
        val name = data["name"] as String
        val props = Persister.getProperties(data["properties"] as Map<String, Any>)
        return Target(name, properties = props)
    }

}