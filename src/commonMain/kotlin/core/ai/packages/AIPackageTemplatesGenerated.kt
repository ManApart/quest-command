package core.ai.packages

class AIPackageTemplatesGenerated : AIPackageTemplatesCollection {
    override val values by lazy { listOf<AIPackageTemplateResource>(resources.ai.packages.CommonPackages()).flatMap { it.values }}
}