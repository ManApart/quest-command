package building

import core.commands.Command
import core.reflection.ReflectionTools
import java.io.File


fun main() {
    val classes = ReflectionTools.getClasses(Command::class.java)

    classes.forEach { clean(it, it.declaredConstructors.first().newInstance() as Command) }
//    classes.filter { it.simpleName == "AttackCommand"}.forEach { clean(it, it.declaredConstructors.first().newInstance() as Command) }
//    clean(classes.first(), classes.first().declaredConstructors.first().newInstance() as Command)

}

fun clean(clazz: Class<*>, command: Command) {
    val name = clazz.simpleName.replace("Command", "")
    val classFile = File("./src/main/kotlin/" + clazz.name.replace(".", "/") + ".kt")
    val classText = classFile.readText().split("\n").toMutableList()

    if (command.getDescription().startsWith("$name:\n\t")) {
        val descriptionSignature = classText.lineOf("override fun getDescription(): String {")

        classText[descriptionSignature+1] = classText[descriptionSignature+1].replace("$name:\\n\\t", "")
    }

    val manSignature = classText.lineOf("override fun getManual(): String {")
    if (classText[manSignature+1].contains("return = \"\n\t")) {
        val manClose = classText.lineOf("}", manSignature)

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

//    println(classText.joinToString("\n"))
    classFile.printWriter().use {
        it.print(classText.joinToString("\n"))
    }

}

private fun List<String>.lineOf(line: String, start: Int = 0) : Int {
    val match = subList(start, size).firstOrNull { it.trim() == line }
    return if (match != null){
        subList(start, size).indexOf(match) + start
    } else {
        -1
    }
}
