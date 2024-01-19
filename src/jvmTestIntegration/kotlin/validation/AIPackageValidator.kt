package validation

import building.ModManager
import core.DependencyInjector
import core.ai.AIManager2
import core.ai.packages.AIPackageTemplatesCollection
import kotlinx.coroutines.runBlocking

//TODO validate all things/minds reference valid package name

class AIPackageValidator {

    private val packages = runBlocking { AIManager2.aiPackages }
    private val templates = (DependencyInjector.getImplementation(AIPackageTemplatesCollection::class).values + ModManager.ai2)

    fun validate(): Int {
        return noDuplicatePackageNames() +
                noDuplicateIdeaNames() +
                subPackageStringReferenceExists()
    }

    private fun noDuplicatePackageNames(): Int {
        var warnings = 0
        val names = mutableListOf<String>()

        templates.forEach { template ->
            if (names.contains(template.name)) {
                println("WARN: AI Package Template '${template.name}' has a duplicate name.")
                warnings++
            } else {
                names.add(template.name)
            }
        }
        return warnings
    }

    private fun noDuplicateIdeaNames(): Int {
        var warnings = 0
        val names = mutableListOf<String>()

        packages.values.flatMap { it.ideas.values.flatten() }.forEach { idea ->
            if (names.contains(idea.name)) {
                println("WARN: Idea '${idea.name}' has a duplicate name.")
                warnings++
            } else {
                names.add(idea.name)
            }
        }
        return warnings
    }

    private fun subPackageStringReferenceExists(): Int {
        var warnings = 0
        val packageRef = templates.map { it.name }.toSet()

        templates.forEach { template ->
            template.subPackages.forEach { reference ->
                if (!packageRef.contains(reference)) {
                    println("WARN: AI Package Template ${template.name} references non existent package template $reference.")
                    warnings++
                }
            }
        }
        return warnings
    }


}