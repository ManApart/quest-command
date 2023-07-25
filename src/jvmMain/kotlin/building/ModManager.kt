package building

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

/*
TODO
Do loaded classes have the ability to call each other?
Resolve load order conflicts

 */

actual object ModManager {
    actual fun applyMods() {
        val manifestFile = File("./mods/manifest.txt")
        val manifest = if (manifestFile.exists()){
            manifestFile.readLines()
        } else listOf()
        println("Found ${manifest.size} mods.")

        manifest.mapNotNull { fileName ->
            val jarFile = File("./mods/$fileName.jar")
            if (jarFile.exists()) jarFile else null
        }.forEach { loadJar(it) }

        if (manifest.isNotEmpty()) println("Loaded Mods")
    }

    private fun loadJar(jarFile: File) {
        val jar = JarFile(jarFile)

        val e = jar.entries()
        val urls = arrayOf(URL("jar:file:" + jarFile.absolutePath + "!/"))
        val cl = URLClassLoader.newInstance(urls)

        while (e.hasMoreElements()) {
            val je = e.nextElement() as JarEntry

            if (je.isDirectory || !je.name.endsWith(".class")) {
                continue
            }

            // -6 because of .class
            val className = je.name.substring(0, je.name.length - 6).replace('/', '.')

            val c: Class<*> = cl.loadClass(className)
        }
    }
}