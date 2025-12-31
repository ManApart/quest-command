package core.ai.packages

class AIPackageTemplatesGenerated : AIPackageTemplatesCollection {
    override val values by lazy { listOf<AIPackageTemplateResource>(resources.ai.packages.CommonAIPackages(), resources.ai.packages.PeasantAIPackage(), resources.ai.packages.CreatureAIPackage()).flatMap { it.values }}
}
