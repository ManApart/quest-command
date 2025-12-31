package core.ai

import building.ModManager
import core.DependencyInjector
import core.ai.packages.AIPackage
import core.ai.packages.AIPackageTemplatesCollection
import core.startupLog
import core.utility.lazyM

object AIPackageManager {
    var aiPackages by lazyM { loadAIPackages() }
        private set

    private fun loadAIPackages(): Map<String, AIPackage> {
        startupLog("Loading AI Packages.")
        val templates = (DependencyInjector.getImplementation(AIPackageTemplatesCollection::class).values + ModManager.ai2).associateBy { it.name }
        val flattenedReference = mutableMapOf<String, AIPackage>()
        return templates.values.map { it.flatten(templates, flattenedReference) }.associateBy { it.name }
    }

    fun reset() {
        aiPackages = loadAIPackages()
    }
}
