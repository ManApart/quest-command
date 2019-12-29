package core.properties

import system.persistance.Persistable

class PropertiesPersister : Persistable<Properties> {
    override fun getVersion(): Int {
        return 0
    }

    override fun getPersisted(dataObject: Properties): Map<String, Any> {
        val data = mutableMapOf<String, Any>()
        data["values"] = dataObject.values.getAll()
        data["tags"] = dataObject.tags.getAll()
        return data
    }

    override fun applyData(data: Map<String, Any>): Properties {
        val values = Values()
        (data["values"] as Map<*, *>).forEach { values.put(it.key as String, it.value as String) }

        val tags = Tags()
        (data["tags"] as List<*>).forEach { tags.add(it as String) }

        return Properties(values, tags)
    }
}