package core.ai.packages


data class AIPackageTemplate(val name: String, val ideas: List<Idea>, val subPackages: List<String>, val dropped: List<String>) {

    fun flatten(reference: Map<String, AIPackageTemplate>, flattenedReference: MutableMap<String, AIPackage>): AIPackage {
        return flattenedReference[name] ?: let {
            val subIdeas = subPackages.asSequence().mapNotNull { reference[it] }
                .flatMap { subPackage ->
                    flattenedReference[subPackage.name]?.ideas?.values?.flatten()
                        ?: subPackage.flatten(reference, flattenedReference).also { flattenedReference[subPackage.name] = it }.ideas.values.flatten()
                }.filter { !dropped.contains(it.name) }.toList()
            AIPackage(name, ideas + subIdeas).also { flattenedReference[name] = it }
        }
    }
}
