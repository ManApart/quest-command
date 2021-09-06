package core.body

class BodysGenerated : BodysCollection {
    override val values = listOf<BodyResource>(resources.body.CommonBodies()).flatMap { it.values }
}