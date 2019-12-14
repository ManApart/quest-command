package core.reflection

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File
import java.lang.reflect.Modifier


object ReflectionTools {
    private const val fileName = "./src/main/kotlin/core/reflection/GeneratedReflections.kt"
    private val reflections = Reflections(SubTypesScanner(false))

    fun generateFile() {
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
        val allClasses = reflections.getSubTypesOf(kClass).filter {!Modifier.isAbstract(it.modifiers) }
        println("Saving ${allClasses.size} classes for $classPackageName")
        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val variableName = cleanedPackageName.substringAfterLast(".").decapitalize() + "s"
        return "private val $variableName: List<$classPackageName> = listOf($classes)\n"
    }

}