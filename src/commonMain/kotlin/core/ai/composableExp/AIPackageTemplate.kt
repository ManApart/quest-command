package core.ai.composableExp


//TODO - we can validate string references before flattening
//Validate
// - package name unique
// - idea name unique
// - string references exist
data class AIPackageTemplate(val name: String, val subPackages: List<String>, val priorityOverride: Map<String, Int>, val ideas: List<Idea>) {
    fun flatten(reference: Map<String, AIPackageTemplate>, flattenedReference: MutableMap<String, AIPackage>): AIPackage {
        return flattenedReference[name] ?: let {
            val subIdeas = subPackages.mapNotNull { reference[it] }.flatMap { subPackage ->
                flattenedReference[subPackage.name]?.ideas?.values?.flatten()
                    ?: subPackage.flatten(reference, flattenedReference).also { flattenedReference[subPackage.name] = it }.ideas.values.flatten()
            }.map { idea ->
                priorityOverride[it.name]?.let { idea.copy(priority = it) } ?: idea
            }
            AIPackage(name, ideas + subIdeas)
        }
    }
}