package building

import conversation.dsl.DialogueTreeResource
import core.ai.agenda.AgendaResource
import core.ai.behavior.BehaviorResource
import core.body.BodyPartResource
import core.body.BodyResource
import core.thing.activator.dsl.ActivatorResource
import core.thing.creature.CreatureResource
import core.thing.item.ItemResource
import crafting.RecipeResource
import crafting.material.MaterialResource
import kotlinx.coroutines.runBlocking
import quests.StoryEventResource
import status.conditions.ConditionResource
import status.effects.EffectResource
import traveling.location.location.LocationResource
import traveling.location.network.NetworkResource
import traveling.location.weather.WeatherResource
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile

fun loadMods() {
    ModManager.reset()
    val manifestFile = File("./mods/manifest.txt")
    val manifest = if (manifestFile.exists()) {
        manifestFile.readLines()
    } else listOf()
    println("Found ${manifest.size} mods.")

    runBlocking {
        manifest.mapNotNull { fileName ->
            val jarFile = File("./mods/$fileName.jar")
            if (jarFile.exists()) jarFile else null
        }.forEach { loadJar(it) }
    }
    if (manifest.isNotEmpty()) println("Loaded Mods")
}

@Suppress("UNCHECKED_CAST")
private suspend fun loadJar(jarFile: File) {
    val jar = JarFile(jarFile)

    val e = jar.entries()
    val urls = arrayOf(URL("jar:file:" + jarFile.absolutePath + "!/"))
    val cl = URLClassLoader.newInstance(urls)

    while (e.hasMoreElements()) {
        val je = e.nextElement() as JarEntry

        if (je.isDirectory || !je.name.endsWith(".class")) continue
        val className = je.name.substring(0, je.name.length - ".class".length).replace('/', '.')
        val c: Class<*> = cl.loadClass(className)

        when {
            c.interfaces.contains(ActivatorResource::class.java) -> processActivator(c as Class<ActivatorResource>)
            c.interfaces.contains(AgendaResource::class.java) -> processAgenda(c as Class<AgendaResource>)
            c.interfaces.contains(BehaviorResource::class.java) -> processBehavior(c as Class<BehaviorResource>)
            c.interfaces.contains(BodyResource::class.java) -> processBody(c as Class<BodyResource>)
            c.interfaces.contains(BodyPartResource::class.java) -> processBodyPart(c as Class<BodyPartResource>)
            c.interfaces.contains(CreatureResource::class.java) -> processCreature(c as Class<CreatureResource>)
            c.interfaces.contains(ConditionResource::class.java) -> processCondition(c as Class<ConditionResource>)
            c.interfaces.contains(DialogueTreeResource::class.java) -> processConversation(c as Class<DialogueTreeResource>)
            c.interfaces.contains(EffectResource::class.java) -> processEffect(c as Class<EffectResource>)
            c.interfaces.contains(ItemResource::class.java) -> processItem(c as Class<ItemResource>)
            c.interfaces.contains(LocationResource::class.java) -> processLocation(c as Class<LocationResource>)
            c.interfaces.contains(NetworkResource::class.java) -> processNetwork(c as Class<NetworkResource>)
            c.interfaces.contains(MaterialResource::class.java) -> processMaterial(c as Class<MaterialResource>)
            c.interfaces.contains(RecipeResource::class.java) -> processRecipe(c as Class<RecipeResource>)
            c.interfaces.contains(StoryEventResource::class.java) -> processQuest(c as Class<StoryEventResource>)
            c.interfaces.contains(WeatherResource::class.java) -> processWeather(c as Class<WeatherResource>)
        }
    }
}

private suspend fun processActivator(c: Class<ActivatorResource>) = ModManager.activators.addAll(c.getDeclaredConstructor().newInstance().values())
private fun processAgenda(c: Class<AgendaResource>) = ModManager.agendas.addAll(c.getDeclaredConstructor().newInstance().values)
private fun processBehavior(c: Class<BehaviorResource>) = ModManager.behaviors.addAll(c.getDeclaredConstructor().newInstance().values)
private fun processBody(c: Class<BodyResource>) = ModManager.bodies.addAll(c.getDeclaredConstructor().newInstance().values)
private fun processBodyPart(c: Class<BodyPartResource>) = ModManager.bodyParts.addAll(c.getDeclaredConstructor().newInstance().values)
private suspend fun processCreature(c: Class<CreatureResource>) = ModManager.creatures.addAll(c.getDeclaredConstructor().newInstance().values())
private fun processCondition(c: Class<ConditionResource>) = ModManager.conditions.addAll(c.getDeclaredConstructor().newInstance().values)
private suspend fun processConversation(c: Class<DialogueTreeResource>) = ModManager.conversations.addAll(c.getDeclaredConstructor().newInstance().values())
private fun processEffect(c: Class<EffectResource>) = ModManager.effects.addAll(c.getDeclaredConstructor().newInstance().values)
private suspend fun processItem(c: Class<ItemResource>) = ModManager.items.addAll(c.getDeclaredConstructor().newInstance().values())
private fun processLocation(c: Class<LocationResource>) = ModManager.locations.addAll(c.getDeclaredConstructor().newInstance().values)
private fun processNetwork(c: Class<NetworkResource>) = ModManager.networks.addAll(c.getDeclaredConstructor().newInstance().values)
private fun processMaterial(c: Class<MaterialResource>) = ModManager.materials.addAll(c.getDeclaredConstructor().newInstance().values)
private suspend fun processRecipe(c: Class<RecipeResource>) = ModManager.recipes.addAll(c.getDeclaredConstructor().newInstance().values())
private fun processQuest(c: Class<StoryEventResource>) = ModManager.quests.addAll(c.getDeclaredConstructor().newInstance().values)
private fun processWeather(c: Class<WeatherResource>) = ModManager.weather.addAll(c.getDeclaredConstructor().newInstance().values)