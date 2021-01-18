package core.reflection

import core.ai.behavior.Behavior
import core.ai.behavior.BehaviorResource
import core.commands.Command
import core.events.EventListener
import core.events.eventParsers.EventParser
import magic.spellCommands.SpellCommand
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File
import java.lang.reflect.Modifier


object ReflectionTools {
    private val topLevelPackages = getPrefixes()
    private val reflections = Reflections(topLevelPackages, SubTypesScanner(false))

    private fun getPrefixes(): List<String> {
        return File("./src/main/kotlin").listFiles()!!.filter { it.isDirectory }.map { it.name }.sorted()
    }

    fun generateFiles() {
//        generateCollectionsFile(Command::class.java)
//        generateCollectionsFile(SpellCommand::class.java)
//        generateCollectionsFile(EventParser::class.java)
//        generateCollectionsFile(EventListener::class.java)
//        generateResourcesFile(BehaviorResource::class.java, Behavior::class.java)
    }

//    private fun generateCollectionsFile(clazz: Class<*>) {
//        val allClasses = reflections.getSubTypesOf(clazz).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }
//        println("Saving ${allClasses.size} classes for ${clazz.name}")
//        val isTyped = clazz.typeParameters.isNotEmpty()
//        val typeSuffix = if(isTyped){
//            "<*>"
//        } else {
//            ""
//        }
//
//        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
//
//        writeInterfaceFile(clazz, typeSuffix)
//        writeGeneratedFile(clazz, typeSuffix, classes)
//        writeMockedFile(clazz, typeSuffix)
//    }
//
//    private fun generateResourcesFile(clazz: Class<*>, resourceClass: Class<*>) {
//        val allClasses= reflections.getSubTypesOf(clazz).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }
//        println("Saving ${allClasses.size} classes for ${clazz.name}")
//        val isTyped = resourceClass.typeParameters.isNotEmpty()
//        val typeSuffix = if(isTyped){
//            "<*>"
//        } else {
//            ""
//        }
//        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
//
//        writeInterfaceFile(resourceClass, typeSuffix)
//        writeGeneratedResourceFile(resourceClass, typeSuffix, classes)
//        writeMockedFile(resourceClass, typeSuffix)
//    }
//
//    private fun writeInterfaceFile(clazz: Class<*>, typeSuffix: String) {
//        val packageName = clazz.packageName.replace(".", "/")
//        File("./src/main/kotlin/$packageName/${clazz.simpleName}sCollection.kt").printWriter().use {
//            it.print(
//                """
//                package ${clazz.packageName}
//
//                interface ${clazz.simpleName}sCollection {
//                    val values: List<${clazz.simpleName}$typeSuffix>
//                }
//            """.trimIndent()
//            )
//        }
//    }
//
//    private fun writeGeneratedFile(clazz: Class<*>, typeSuffix: String, classes: String) {
//        val packageName = clazz.packageName.replace(".", "/")
//        File("./src/main/kotlin/$packageName/${clazz.simpleName}sGenerated.kt").printWriter().use {
//            it.print(
//                    """
//                package ${clazz.packageName}
//
//                class ${clazz.simpleName}sGenerated : ${clazz.simpleName}sCollection {
//                    override val values: List<${clazz.name}$typeSuffix> = listOf($classes)
//                }
//            """.trimIndent()
//            )
//        }
//    }
//
//    private fun writeGeneratedResourceFile(clazz: Class<*>, typeSuffix: String, classes: String) {
//        val packageName = clazz.packageName.replace(".", "/")
//
//        File("./src/main/kotlin/$packageName/${clazz.simpleName}sGenerated.kt").printWriter().use {
//            it.print(
//                    """
//                package ${clazz.packageName}
//
//                class ${clazz.simpleName}sGenerated : ${clazz.simpleName}sCollection {
//                    override val values: List<${clazz.simpleName}$typeSuffix> = listOf($classes).flatMap { it.values }
//                }
//            """.trimIndent()
//            )
//        }
//    }
//
//    private fun writeMockedFile(clazz: Class<*>, typeSuffix: String) {
//        val packageName = clazz.packageName.replace(".", "/")
//        File("./src/test/kotlin/$packageName/${clazz.simpleName}sMock.kt").printWriter().use {
//            it.print(
//                    """
//                package ${clazz.packageName}
//
//                class ${clazz.simpleName}sMock(override val values: List<${clazz.simpleName}$typeSuffix> = listOf()) : ${clazz.simpleName}sCollection
//            """.trimIndent()
//            )
//        }
//    }

}