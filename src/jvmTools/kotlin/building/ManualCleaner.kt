package building

import core.commands.Command
import magic.spellCommands.SpellCommand
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


fun main() {
    val classes = ReflectionTools.getClasses(Command::class)
    val spellClasses = ReflectionTools.getClasses(SpellCommand::class)

    classes.forEach { clean(it, it.primaryConstructor!!.call() as Command) }
    spellClasses.forEach { clean(it, it.primaryConstructor!!.call() as SpellCommand) }
//    spellClasses.filter { it.simpleName == "Adrenaline" }.forEach { clean(it, it.declaredConstructors.first().newInstance() as SpellCommand) }
//    classes.filter { it.simpleName == "SpeakCommand"}.forEach { clean(it, it.declaredConstructors.first().newInstance() as Command) }
//    clean(classes.first(), classes.first().declaredConstructors.first().newInstance() as Command)

}

fun clean(clazz: KClass<*>, command: Command) {
    val description = command.getDescription()
    val name = clazz.simpleName!!.replace("Command", "")
    val aliases = listOf(name) + command.getAliases()
    clean(clazz, description, aliases)
}

fun clean(clazz: KClass<*>, command: SpellCommand) {
    val description = command.getDescription()
    val name = "Cast " + clazz.simpleName
    val aliases = listOf(name)
    clean(clazz, description, aliases)
}

private fun cleanDescription(line: String, description: String, aliases: List<String>): String {
    return if (aliases.any { description.startsWith("$it:\n\t") }) {
        (listOf(line) + aliases).reduce { acc, alias -> acc.replace("$alias:\\n\\t", "") }
    } else {
        line
    }
}

private fun clean(clazz: KClass<*>, description: String, aliases: List<String>) {
    val classFile = File("./src/commonMain/kotlin/" + clazz.qualifiedName!!.replace(".", "/") + ".kt")
    val classText = classFile.readText().split("\n").toMutableList()

    val descriptionSignature = classText.lineOf("override fun getDescription(): String {")
    classText[descriptionSignature + 1] = cleanDescription(classText[descriptionSignature + 1], description, aliases)

    val manSignature = classText.lineOf("override fun getManual(): String {")
    val manClose = classText.lineOf("}", manSignature)
    if (classText[manSignature + 1].contains("return \"\\n\\t")) {

        (manSignature..manClose).forEach { i ->
            classText[i] = classText[i]
                .replace("return \"\\n\\t", "return \"\"\"\n\t")
                .replace("\"\\n\\t", "")
                .replace("\" +", "")
                .replace("+\n", "")
                .replace("                ", "\t")
        }
        classText[manClose - 1] = classText[manClose - 1] + "\"\"\""
    }

    if (classText[manClose - 1].endsWith("\"\"\"\"")) {
        classText[manClose - 1] = classText[manClose - 1].replace("\"\"\"\"", "\"\"\"")
    }


//    println(classText.joinToString("\n"))
    classFile.printWriter().use {
        it.print(classText.joinToString("\n"))
    }

}

private fun List<String>.lineOf(line: String, start: Int = 0): Int {
    val match = subList(start, size).firstOrNull { it.trim() == line }
    return if (match != null) {
        subList(start, size).indexOf(match) + start
    } else {
        -1
    }
}
