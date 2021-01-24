package core.reflection

import core.ai.behavior.Behavior
import core.ai.behavior.BehaviorResource
import core.commands.Command
import core.events.EventListener
import core.conditional.ConditionalString
import core.conditional.WeatherStringResource
import magic.spellCommands.SpellCommand
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import quests.StoryEvent
import quests.StoryEventResource
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

        generateResourcesFile(BehaviorResource::class.java, Behavior::class.java)
        generateResourcesFile(WeatherStringResource::class.java, ConditionalString::class.java)
        generateResourcesFile(StoryEventResource::class.java, StoryEvent::class.java)
    }

    /**
     * Find all classes that extend the collected class interface (Command) and dump them into a list in the generated class (CommandsGenerated)
     */
    private fun generateCollectionsFile(collectedClass: Class<*>) {
        val allClasses = reflections.getSubTypesOf(collectedClass).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }
        println("Saving ${allClasses.size} classes for ${collectedClass.name}")
        val isTyped = collectedClass.typeParameters.isNotEmpty()
        val typeSuffix = if(isTyped){
            "<*>"
        } else {
            ""
        }

        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val newClassName = collectedClass.simpleName

        writeInterfaceFile(collectedClass, typeSuffix, newClassName)
        writeGeneratedFile(collectedClass, typeSuffix, classes)
        writeMockedFile(collectedClass, typeSuffix, newClassName)
    }
    /**
     * Takes Two Classes
     * 1) The Resource interface to look for
     * 2) The implementation or collected class
     * Find all classes that extend the resource interface (WeatherStringResource) and combines their values into a list in the generated class (WeatherStringsGenerated)
     */
    private fun generateResourcesFile(resourceInterface: Class<*>, collectedClass: Class<*>) {
        val allClasses= reflections.getSubTypesOf(resourceInterface).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }
        println("Saving ${allClasses.size} classes for ${resourceInterface.name}")
        val isTyped = collectedClass.typeParameters.isNotEmpty()
        val typeSuffix = if(isTyped){
            "<*>"
        } else {
            ""
        }
        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val newClassName = resourceInterface.simpleName.replace("Resource", "")

        writeInterfaceFile(collectedClass, typeSuffix, newClassName)
        writeGeneratedResourceFile(collectedClass, typeSuffix, classes, newClassName)
        writeMockedFile(collectedClass, typeSuffix, newClassName)
    }

    private fun writeInterfaceFile(collectedClass: Class<*>, typeSuffix: String, newClassName: String) {
        val packageName = collectedClass.packageName.replace(".", "/")
        File("./src/main/kotlin/$packageName/${newClassName}sCollection.kt").printWriter().use {
            it.print(
                """
                package ${collectedClass.packageName}

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

    private fun writeGeneratedResourceFile(collectedClass: Class<*>, typeSuffix: String, classes: String, newClassName: String) {
        val packageName = collectedClass.packageName.replace(".", "/")

        File("./src/main/kotlin/$packageName/${newClassName}sGenerated.kt").printWriter().use {
            it.print(
                    """
                package ${collectedClass.packageName}

                class ${newClassName}sGenerated : ${newClassName}sCollection {
                    override val values: List<${collectedClass.simpleName}$typeSuffix> = listOf($classes).flatMap { it.values }
                }
            """.trimIndent()
            )
        }
    }

    private fun writeMockedFile(collectedClass: Class<*>, typeSuffix: String, newClassName: String) {
        val packageName = collectedClass.packageName.replace(".", "/")
        File("./src/test/kotlin/$packageName/${newClassName}sMock.kt").printWriter().use {
            it.print(
                    """
                package ${collectedClass.packageName}

                class ${newClassName}sMock(override val values: List<${collectedClass.simpleName}$typeSuffix> = listOf()) : ${newClassName}sCollection
            """.trimIndent()
            )
        }
    }

}