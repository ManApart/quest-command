package core.utility.reflection

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File


object ReflectionTools {
    private const val srcPrefix = "./src/main/kotlin/core/utility/reflection/"
    private val reflections = Reflections(SubTypesScanner(false))

    fun saveAllCommands() {
        saveGeneric("core.commands.Command")
    }

    fun saveAllSpellCommands() {
        saveGeneric("interact.magic.spellCommands.SpellCommand")
    }

    fun saveAllEventListeners() {
        saveGeneric("core.events.EventListener<*>")
    }

    private fun saveGeneric(classPackageName: String) {
        val regex = Regex("[^A-Za-z.]")
        val cleanedPackageName = regex.replace(classPackageName, "")

        val kClass = Class.forName(cleanedPackageName) as Class<*>
        val allClasses = reflections.getSubTypesOf(kClass)
        println("Saving ${allClasses.size} classes for $classPackageName")
        val classes = allClasses.joinToString(", ") { "${it.name}()".replace("$", ".") }

        val variableName = cleanedPackageName.substringAfterLast(".").decapitalize() + "s"
        val fileName = variableName.capitalize() + ".kt"
        File(srcPrefix + fileName).printWriter().use { out ->
            out.println("""
            package core.utility.reflection
            val $variableName: List<$classPackageName> = listOf(
                """.trimIndent())
            out.println(classes)
            out.println(")")
        }
    }

}