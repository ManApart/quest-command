package core.ai.packages

class AIPackageTemplatesGenerated : AIPackageTemplatesCollection {
    override val values by lazy { listOf<AIPackageTemplateResource>(resources.ai.packages.CommonPackages(), resources.ai.packages.PeasantPackage(), resources.ai.packages.CreaturePackage()).flatMap { it.values }}
}
