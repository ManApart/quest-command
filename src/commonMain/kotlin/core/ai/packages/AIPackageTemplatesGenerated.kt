package core.ai.packages

class AIPackageTemplatesGenerated : AIPackageTemplatesCollection {
    override val values by lazy { listOf<AIPackageTemplateResource>(resources.ai.packages.CommonAIPackages(), resources.ai.packages.CreatureAIPackage(), resources.ai.packages.PeasantAIPackage()).flatMap { it.values }}
}