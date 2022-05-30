package core.body

class BodysGenerated : BodysCollection {
    override val values by lazy { listOf<BodyResource>(resources.body.CommonBodies()).flatMap { it.values }}
}