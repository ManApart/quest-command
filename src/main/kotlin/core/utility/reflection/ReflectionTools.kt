package core.utility.reflection

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File


object ReflectionTools {
    private const val fileName = "./src/main/kotlin/core/utility/reflection/GeneratedReflections.kt"
    private val reflections = Reflections(SubTypesScanner(false))


    fun generateFile() {
        val variables = listOf(
                getInstanceList("core.commands.Command"),
                getInstanceList("interact.magic.spellCommands.SpellCommand"),
                getInstanceList("core.events.EventListener<*>")
        )

        File(fileName).printWriter().use { out ->
            out.println("""
            package core.utility.reflection

            import core.commands.Command
            import core.events.EventListener
            import interact.magic.spellCommands.SpellCommand
            
            class GeneratedReflections : Reflections {
                override fun getCommands(): List<Command> {
                    return commands
                }
            
                override fun getSpellCommands(): List<SpellCommand> {
                    return spellCommands
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
        val allClasses = reflections.getSubTypesOf(kClass)
        println("Saving ${allClasses.size} classes for $classPackageName")
        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }
        val variableName = cleanedPackageName.substringAfterLast(".").decapitalize() + "s"
        return "private val $variableName: List<$classPackageName> = listOf($classes)\n"
    }

}