package core.reflection

import conversation.dsl.Dialogue
import conversation.dsl.DialogueResource
import core.ai.AIBase
import core.ai.action.AIAction
import core.ai.action.dsl.AIActionResource
import core.ai.behavior.Behavior
import core.ai.behavior.BehaviorResource
import core.ai.dsl.AIResource
import core.body.BodyPartResource
import core.body.BodyResource
import core.commands.Command
import core.conditional.ConditionalString
import core.events.EventListener
import core.target.Target
import core.target.TargetBuilder
import core.target.activator.dsl.ActivatorResource
import core.target.creature.CreatureResource
import core.target.item.ItemResource
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
import traveling.location.location.BodyBuilder
import traveling.location.location.LocationDescriptionResource
import traveling.location.location.LocationNodeBuilder
import traveling.location.location.LocationRecipeBuilder
import traveling.location.weather.Weather
import traveling.location.weather.WeatherResource
import traveling.location.weather.WeatherStringResource
import java.io.File
import java.lang.reflect.Modifier


object ReflectionTools {
    private val topLevelPackages = getPrefixes()
    private val reflections = Reflections(topLevelPackages, SubTypesScanner(false))

    private fun getPrefixes(): List<String> {
        return File("./src/main/kotlin").listFiles()!!.filter { it.isDirectory }.map { it.name }.sorted()
    }

    fun generateFiles() {
        generateCollectionsFile(Command::class.java)
        generateCollectionsFile(SpellCommand::class.java)
        generateCollectionsFile(EventListener::class.java)

        generateResourcesFile(AIResource::class.java, AIBase::class.java)
        generateResourcesFile(AIActionResource::class.java, AIAction::class.java)
        generateResourcesFile(ActivatorResource::class.java, TargetBuilder::class.java)
        generateResourcesFile(ConditionResource::class.java, ConditionRecipe::class.java)
        generateResourcesFile(BodyResource::class.java, BodyBuilder::class.java)
        generateResourcesFile(BodyPartResource::class.java, LocationRecipeBuilder::class.java)
        generateResourcesFile(CreatureResource::class.java, TargetBuilder::class.java)
        generateResourcesFile(ItemResource::class.java, TargetBuilder::class.java)
        generateResourcesFile(BehaviorResource::class.java, Behavior::class.java)
        generateResourcesFile(DialogueResource::class.java, Dialogue::class.java)
        generateResourcesFile(EffectResource::class.java, EffectBase::class.java)
        generateResourcesFile(LocationDescriptionResource::class.java, ConditionalString::class.java)
        generateResourcesFile(RecipeResource::class.java, RecipeBuilder::class.java)
        generateResourcesFile(StoryEventResource::class.java, StoryEvent::class.java)
        generateResourcesFile(WeatherResource::class.java, Weather::class.java)
        generateResourcesFile(WeatherStringResource::class.java, ConditionalString::class.java)
    }

    fun getClasses(superClass: Class<*>): List<Class<*>> {
        return reflections.getSubTypesOf(superClass).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }
    }

    /**
     * Find all classes that extend the collected class interface (Command) and dump them into a list in the generated class (CommandsGenerated)
     */
    private fun generateCollectionsFile(collectedClass: Class<*>) {
        val allClasses = getClasses(collectedClass)
        println("Saving ${allClasses.size} classes for ${collectedClass.name}")
        val isTyped = collectedClass.typeParameters.isNotEmpty()
        val typeSuffix = if (isTyped) {
            "<*>"
        } else {
            ""
        }

        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val newClassName = collectedClass.simpleName

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
    private fun generateResourcesFile(resourceInterface: Class<*>, collectedClass: Class<*>) {
        val allClasses = reflections.getSubTypesOf(resourceInterface).filter { !Modifier.isAbstract(it.modifiers) }
            .sortedBy { it.name }
        println("Saving ${allClasses.size} classes for ${resourceInterface.name}")
        val isTyped = collectedClass.typeParameters.isNotEmpty()
        val typeSuffix = if (isTyped) {
            "<*>"
        } else {
            ""
        }
        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val newClassName = resourceInterface.simpleName.replace("Resource", "")

        writeInterfaceFile(resourceInterface, collectedClass, typeSuffix, newClassName)
        writeGeneratedResourceFile(resourceInterface, classes, newClassName)
        writeMockedFile(resourceInterface, collectedClass, typeSuffix, newClassName)
    }

    private fun writeInterfaceFile(
        resourceInterface: Class<*>,
        collectedClass: Class<*>,
        typeSuffix: String,
        newClassName: String
    ) {
        val packageName = resourceInterface.packageName.replace(".", "/")
        File("./src/main/kotlin/$packageName/${newClassName}sCollection.kt").printWriter().use {
            it.print(
                """
                package ${resourceInterface.packageName}
                import ${collectedClass.name}

                interface ${newClassName}sCollection {
                    val values: List<${collectedClass.simpleName}$typeSuffix>
                }
            """.trimIndent()
            )
        }
    }

    private fun writeGeneratedFile(collectedClass: Class<*>, typeSuffix: String, classes: String) {
        val packageName = collectedClass.packageName.replace(".", "/")
        File("./src/main/kotlin/$packageName/${collectedClass.simpleName}sGenerated.kt").printWriter().use {
            it.print(
                """
                package ${collectedClass.packageName}

                class ${collectedClass.simpleName}sGenerated : ${collectedClass.simpleName}sCollection {
                    override val values: List<${collectedClass.name}$typeSuffix> = listOf($classes)
                }
            """.trimIndent()
            )
        }
    }

    private fun writeGeneratedResourceFile(resourceInterface: Class<*>, classes: String, newClassName: String) {
        val packageName = resourceInterface.packageName.replace(".", "/")

        File("./src/main/kotlin/$packageName/${newClassName}sGenerated.kt").printWriter().use {
            it.print(
                """
                package ${resourceInterface.packageName}

                class ${newClassName}sGenerated : ${newClassName}sCollection {
                    override val values = listOf<${resourceInterface.simpleName}>($classes).flatMap { it.values }
                }
            """.trimIndent()
            )
        }
    }

    private fun writeMockedFile(
        resourceInterface: Class<*>,
        collectedClass: Class<*>,
        typeSuffix: String,
        newClassName: String
    ) {
        val packageName = resourceInterface.packageName.replace(".", "/")
        val file = File("./src/test/kotlin/$packageName/${newClassName}sMock.kt")
        file.parentFile.mkdirs()
        //Only generate an initial sketch, but let the user update it and keep their changes
        if (!file.exists()) {
            file.printWriter().use {
                it.print(
                    """
                package ${resourceInterface.packageName}
                import ${collectedClass.name}

                class ${newClassName}sMock(override val values: List<${collectedClass.simpleName}$typeSuffix> = listOf()) : ${newClassName}sCollection
            """.trimIndent()
                )
            }
        }
    }


}