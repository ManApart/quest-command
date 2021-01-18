package core.reflection

import core.commands.Command
import core.events.EventListener
import core.events.eventParsers.EventParser
import magic.spellCommands.SpellCommand
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File
import java.lang.reflect.Modifier


object ReflectionTools {
    private const val fileName = "./src/main/kotlin/core/reflection/GeneratedReflections.kt"
    private val topLevelPackages = getPrefixes()
    private val reflections = Reflections(topLevelPackages, SubTypesScanner(false))

    private fun getPrefixes(): List<String> {
        return File("./src/main/kotlin").listFiles()!!.filter { it.isDirectory }.map { it.name }.sorted()
    }

    fun generateFiles() {
//        generateBigFile()
        generateFile(Command::class.java)
        generateFile(SpellCommand::class.java)
        generateFile(EventParser::class.java)
        generateFile(EventListener::class.java)
    }

    private fun generateFile(clazz: Class<*>) {
        val allClasses = reflections.getSubTypesOf(clazz).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }
        println("Saving ${allClasses.size} classes for ${clazz.name}")
        val isTyped = clazz.typeParameters.isNotEmpty()
        val typeSuffix = if(isTyped){
            "<*>"
        } else {
            ""
        }

        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }

        writeInterfaceFile(clazz, typeSuffix)
        writeGeneratedFile(clazz, typeSuffix, classes)
        writeMockedFile(clazz, typeSuffix)
    }

    private fun writeInterfaceFile(clazz: Class<*>, typeSuffix: String) {
        File("./src/main/kotlin/core/reflection/${clazz.simpleName}sCollection.kt").printWriter().use {
            it.print(
                """
                package core.reflection
    
                import ${clazz.name}
                
                interface ${clazz.simpleName}sCollection {
                    val values: List<${clazz.simpleName}$typeSuffix>
                }
            """.trimIndent()
            )
        }
    }

    private fun writeGeneratedFile(clazz: Class<*>, typeSuffix: String, classes: String) {
        File("./src/main/kotlin/core/reflection/Generated${clazz.simpleName}s.kt").printWriter().use {
            it.print(
                    """
                package core.reflection
    
                class Generated${clazz.simpleName}s : ${clazz.simpleName}sCollection {
                    override val values: List<${clazz.name}$typeSuffix> = listOf($classes)
                }
            """.trimIndent()
            )
        }
    }

    private fun writeMockedFile(clazz: Class<*>, typeSuffix: String) {
        File("./src/test/kotlin/core/utility/reflection/Mock${clazz.simpleName}s.kt").printWriter().use {
            it.print(
                    """
                package core.utility.reflection

                import ${clazz.name}
                import core.reflection.${clazz.simpleName}sCollection
                
                class Mock${clazz.simpleName}s(override val values: List<${clazz.simpleName}$typeSuffix> = listOf()) : ${clazz.simpleName}sCollection
            """.trimIndent()
            )
        }
    }

    private fun generateBigFile() {
        val variables = listOf(
                getInstanceList("core.commands.Command"),
                getInstanceList("magic.spellCommands.SpellCommand"),
                getInstanceList("core.events.eventParsers.EventParser"),
                getInstanceList("core.events.EventListener<*>")
        )

        File(fileName).printWriter().use { out ->
            out.println("""
                package core.reflection
    
                import core.commands.Command
                import core.events.EventListener
                import core.events.eventParsers.EventParser
                import magic.spellCommands.SpellCommand
                
                class GeneratedReflections : Reflections {
                    override fun getCommands(): List<Command> {
                        return commands
                    }
                
                    override fun getSpellCommands(): List<SpellCommand> {
                        return spellCommands
                    }
                    
                    override fun getEventParsers(): List<EventParser> {
                        return eventParsers
                    }
                
                    override fun getEventListeners(): List<EventListener<*>> {
                        return eventListeners
                    }
                }
                
                    """.trimIndent())
            variables.forEach { out.println(it) }
        }
    }

    private fun getInstanceList(classPackageName: String): String {
        val regex = Regex("[^A-Za-z.]")
        val cleanedPackageName = regex.replace(classPackageName, "")

        val kClass = Class.forName(cleanedPackageName) as Class<*>
        val allClasses = reflections.getSubTypesOf(kClass).filter { !Modifier.isAbstract(it.modifiers) }.sortedBy { it.name }
        println("Saving ${allClasses.size} classes for $classPackageName")
        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val variableName = cleanedPackageName.substringAfterLast(".").decapitalize() + "s"
        return "private val $variableName: List<$classPackageName> = listOf($classes)\n"
    }

}