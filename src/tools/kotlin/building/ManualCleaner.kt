package building

import core.commands.Command
import core.reflection.ReflectionTools
import java.io.File


fun main() {
    val classes = ReflectionTools.getClasses(Command::class.java)

//    classes.forEach { clean(it, it.declaredConstructors.first().newInstance() as Command) }
    clean(classes.first(), classes.first().declaredConstructors.first().newInstance() as Command)

}


fun clean(clazz: Class<*>, command: Command) {
    val name = clazz.simpleName.replace("Command", "")
    val classFile = File("./src/main/kotlin/" + clazz.name.replace(".", "/") + ".kt")
    var classText = classFile.readText()

    if (command.getDescription().startsWith("$name:\n\t")) {
        val description = command.getDescription().substring("$name:\n\t".length, command.getDescription().length)
        classText = replace(classText, "override fun getDescription(): String {", "return \"$description\"")
    }
    val man = command.getManual().substring(1, command.getManual().length)
    classText = replace(classText, "override fun getManual(): String {", "return \"\"\"\n${man}\"\"\"")

    println(classText)
    classFile.printWriter().use {
        it.print(classText)
    }

}

private fun replace(fullClass: String, methodSignature: String, newText: String): String {
    val start = fullClass.indexOf(methodSignature)
    val end = fullClass.indexOf("}", start)

    return fullClass.substring(0, start + methodSignature.length) + "\n\t\t" + newText + "\n\t" + fullClass.substring(end, fullClass.length)
}