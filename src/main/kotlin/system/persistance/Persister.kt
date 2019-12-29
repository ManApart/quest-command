package system.persistance

import core.properties.Properties
import core.properties.PropertiesPersister
import core.target.Player
import core.target.Target
import core.target.TargetPersister

//TODO - generate through reflection?
//Create dependency injection
object Persister {
    private val propertiesPersister = PropertiesPersister()
    private val targetPersister = TargetPersister()

    fun getPersisted(dataObject: Player) : Map<String, Any> {
        return targetPersister.getPersisted(dataObject)
    }

    fun getPersisted(dataObject: Target) : Map<String, Any> {
        return targetPersister.getPersisted(dataObject)
    }

    fun getPersisted(dataObject: Properties) : Map<String, Any> {
        return propertiesPersister.getPersisted(dataObject)
    }



    fun getProperties(data: Map<String, Any>) : Properties {
        return propertiesPersister.applyData(data)
    }

    fun getTarget(data: Map<String, Any>) : Target {
        return targetPersister.applyData(data)
    }

    fun getPlayer(data: Map<String, Any>) : Player {
        return Player()
    }

}