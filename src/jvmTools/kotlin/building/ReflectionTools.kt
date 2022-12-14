package building

import conversation.dsl.Dialogue
import conversation.dsl.DialogueResource
import core.ai.action.AIActionTree
import core.ai.action.dsl.AIActionResource
import core.ai.agenda.Agenda
import core.ai.behavior.Behavior
import core.ai.behavior.BehaviorResource
import core.ai.agenda.AgendaResource
import core.ai.desire.DesireResource
import core.ai.desire.DesireTree
import core.body.BodyPartResource
import core.body.BodyResource
import core.commands.Command
import core.events.Event
import core.events.EventListener
import core.thing.ThingBuilder
import core.thing.activator.dsl.ActivatorResource
import core.thing.creature.CreatureResource
import core.thing.item.ItemResource
import crafting.RecipeBuilder
import crafting.RecipeResource
import magic.spellCommands.SpellCommand
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import quests.StoryEvent
import quests.StoryEventResource
import status.conditions.ConditionRecipe
import status.conditions.ConditionResource
import status.effects.EffectBase
import status.effects.EffectResource
import traveling.location.location.LocationRecipeBuilder
import traveling.location.location.LocationResource
import traveling.location.network.NetworkBuilder
import traveling.location.network.NetworkResource
import traveling.location.weather.Weather
import traveling.location.weather.WeatherResource
import java.io.File
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes

private const val srcRoot = "./src/commonMain/kotlin"
private const val testRoot = "./src/jvmTest/kotlin"

object ReflectionTools {
    private val topLevelPackages = getPrefixes()
    private val reflections = Reflections(topLevelPackages, SubTypesScanner(false))

    private fun getPrefixes(): List<String> {
        return File(srcRoot).listFiles()!!.filter { it.isDirectory }.map { it.name }.sorted()
    }

    fun generateFiles() {
        generateCollectionsFile(Command::class)
        generateCollectionsFile(SpellCommand::class)
        generateListenerMapFile()

        generateResourcesFile(AIActionResource::class, AIActionTree::class)
        generateResourcesFile(ActivatorResource::class, ThingBuilder::class)
        generateResourcesFile(AgendaResource::class, Agenda::class)
        generateResourcesFile(ConditionResource::class, ConditionRecipe::class)
        generateResourcesFile(BodyResource::class, NetworkBuilder::class)
        generateResourcesFile(BodyPartResource::class, LocationRecipeBuilder::class)
        generateResourcesFile(CreatureResource::class, ThingBuilder::class)
        generateResourcesFile(DesireResource::class, DesireTree::class)
        generateResourcesFile(ItemResource::class, ThingBuilder::class)
        generateResourcesFile(BehaviorResource::class, Behavior::class)
        generateResourcesFile(DialogueResource::class, Dialogue::class)
        generateResourcesFile(EffectResource::class, EffectBase::class)
        generateResourcesFile(LocationResource::class, LocationRecipeBuilder::class)
        generateResourcesFile(NetworkResource::class, NetworkBuilder::class)
        generateResourcesFile(RecipeResource::class, RecipeBuilder::class)
        generateResourcesFile(StoryEventResource::class, StoryEvent::class)
        generateResourcesFile(WeatherResource::class, Weather::class)
    }

    fun getClasses(superClass: KClass<*>): List<KClass<*>> {
        return reflections.getSubTypesOf(superClass.java).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }.map { it.kotlin }
    }

    /**
     * Find all classes that extend the collected class interface (Command) and dump them into a list in the generated class (CommandsGenerated)
     */
    private fun generateCollectionsFile(collectedClass: KClass<*>) {
        val allClasses = getClasses(collectedClass)
        println("Saving ${allClasses.size} classes for ${collectedClass.simpleName}")
        val isTyped = collectedClass.typeParameters.isNotEmpty()
        val typeSuffix = if (isTyped) {
            "<*>"
        } else {
            ""
        }

        val classes = allClasses.joinToString(", ") { "${it.qualifiedName}()".replace("$", ".") }
        val newClassName = collectedClass.simpleName!!

        writeInterfaceFile(collectedClass, collectedClass, typeSuffix, newClassName)
        writeGeneratedFile(collectedClass, typeSuffix, classes)
        writeMockedFile(collectedClass, collectedClass, typeSuffix, newClassName)
    }

    /**
     * Takes Two Classes
     * 1) The Resource interface to look for
     * 2) The implementation or collected class
     * Find all classes that extend the resource interface (WeatherStringResource) and combines their values into a list in the generated class (WeatherStringsGenerated)
     */
    private fun generateResourcesFile(resourceInterface: KClass<*>, collectedClass: KClass<*>) {
        val allClasses = reflections.getSubTypesOf(resourceInterface.java).filter { !Modifier.isAbstract(it.modifiers) }
            .sortedBy { it.name }.map { it.kotlin }
        println("Saving ${allClasses.size} classes for ${resourceInterface.simpleName}")
        val isTyped = collectedClass.typeParameters.isNotEmpty()
        val typeSuffix = if (isTyped) {
            "<*>"
        } else {
            ""
        }
        val classes = allClasses.joinToString(", ") { "${it.qualifiedName}()".replace("$", ".") }
        val newClassName = resourceInterface.simpleName!!.replace("Resource", "")

        writeInterfaceFile(resourceInterface, collectedClass, typeSuffix, newClassName)
        writeGeneratedResourceFile(resourceInterface, classes, newClassName)
        writeMockedFile(resourceInterface, collectedClass, typeSuffix, newClassName)
    }

    private fun writeInterfaceFile(
        resourceInterface: KClass<*>,
        collectedClass: KClass<*>,
        typeSuffix: String,
        newClassName: String
    ) {
        //TODO - use qualified name instead of package name?
        //resourceInterface.qualifiedName
        val packageName = resourceInterface.java.packageName.replace(".", "/")
        File("$srcRoot/$packageName/${newClassName}sCollection.kt").printWriter().use {
            it.print(
                """
                package ${resourceInterface.java.packageName}
                import ${collectedClass.qualifiedName}

                interface ${newClassName}sCollection {
                    val values: List<${collectedClass.simpleName}$typeSuffix>
                }
            """.trimIndent()
            )
        }
    }

    private fun writeGeneratedFile(collectedClass: KClass<*>, typeSuffix: String, classes: String) {
        val packageName = collectedClass.java.packageName.replace(".", "/")
        File("$srcRoot/$packageName/${collectedClass.simpleName}sGenerated.kt").printWriter().use {
            it.print(
                """
                package ${collectedClass.java.packageName}

                class ${collectedClass.simpleName}sGenerated : ${collectedClass.simpleName}sCollection {
                    override val values: List<${collectedClass.qualifiedName}$typeSuffix> = listOf($classes)
                }
            """.trimIndent()
            )
        }
    }

    private fun writeGeneratedResourceFile(resourceInterface: KClass<*>, classes: String, newClassName: String) {
        val packageName = resourceInterface.java.packageName.replace(".", "/")

        File("$srcRoot/$packageName/${newClassName}sGenerated.kt").printWriter().use {
            it.print(
                """
                package ${resourceInterface.java.packageName}

                class ${newClassName}sGenerated : ${newClassName}sCollection {
                    override val values by lazy { listOf<${resourceInterface.simpleName}>($classes).flatMap { it.values }}
                }
            """.trimIndent()
            )
        }
    }

    private fun writeMockedFile(
        resourceInterface: KClass<*>,
        collectedClass: KClass<*>,
        typeSuffix: String,
        newClassName: String
    ) {
        val packageName = resourceInterface.java.packageName.replace(".", "/")
        val file = File("$testRoot/$packageName/${newClassName}sMock.kt")
        file.parentFile.mkdirs()
        //Only generate an initial sketch, but let the user update it and keep their changes
        if (!file.exists()) {
            file.printWriter().use {
                it.print(
                    """
                package ${resourceInterface.java.packageName}
                import ${collectedClass.simpleName}

                class ${newClassName}sMock(override val values: List<${collectedClass.simpleName}$typeSuffix> = listOf()) : ${newClassName}sCollection
            """.trimIndent()
                )
            }
        }
    }

    private fun generateListenerMapFile() {
        val allListeners = getClasses(EventListener::class)
        println("Creating listener map for ${allListeners.size} listeners.")

        assertNoDuplicateEventNames()

        val classes = allListeners.joinToString(", ") { "\"${it.qualifiedName}\" to ${it.qualifiedName}()".replace("$", ".") }
        val eventMapString = buildEventMap(allListeners).entries.joinToString(", ") { (eventName, listenerList) ->
            val valueString = listenerList.joinToString(",") { "listenerMap[\"$it\"]!!" }
            "\"$eventName\" to listOf($valueString)".replace("$", ".")
        }

        val file = File("$srcRoot/core/events/EventListenerMapGenerated.kt")
        file.parentFile.mkdirs()
        file.printWriter().use {
            it.print(
                """
                package core.events

                class EventListenerMapGenerated : EventListenerMapCollection {
                    private val listenerMap: Map<String, EventListener<*>> = mapOf($classes)
                    
                    override val values: Map<String, List<EventListener<*>>> = mapOf($eventMapString)
                }
                """.trimIndent()
            )
        }
    }

    //This can be removed when JS can use qualified name
    private fun assertNoDuplicateEventNames(){
        val allEvents = getClasses(Event::class)
        val existing = mutableMapOf<String, String>()
        allEvents.forEach { event ->
            val simpleName = event.simpleName!!
            val qualifiedName = event.qualifiedName!!
            if (existing.containsKey(simpleName)){
                throw IllegalArgumentException("Found Duplicate Event. Both ${existing[simpleName]} and $qualifiedName have the same simple name!")
            }
            existing[simpleName] = qualifiedName
        }

    }

    private fun buildEventMap(allListeners: List<KClass<*>>): Map<String, List<String>> {
        val result = mutableMapOf<String, MutableList<String>>()

        allListeners.forEach { listener ->
            val eventName = getListenedForClassName(listener)
            result.putIfAbsent(eventName, mutableListOf())
            result[eventName]?.add(listener.qualifiedName!!)
        }

        return result
    }

    //I'd prefer to use qualified name but the JS api doesn't support that yet. This has a higher chance of name collisions, so it should be updated to use qualified name once supported
    private fun getListenedForClassName(listener: KClass<*>): String {
        val clazz = listener.allSupertypes.first { it.classifier == EventListener::class }.arguments.first().type!!.classifier as KClass<*>
        return clazz.simpleName!!
    }

    private fun getListenedForClass(listener: EventListener<*>): KClass<*> {
        return listener::class.allSupertypes.first { it.classifier == EventListener::class }.arguments.first().type!!.classifier as KClass<*>
    }


}