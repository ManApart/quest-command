package core.ai.composableExp

class AIPackageTemplatesGenerated : AIPackageTemplatesCollection {
    override val values by lazy { listOf<AIPackageTemplateResource>().flatMap { it.values }}
}