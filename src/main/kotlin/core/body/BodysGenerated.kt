package core.body

class BodysGenerated : BodysCollection {
    override val values = listOf<BodyResource>(resources.body.CommonBodies(), resources.body.CommonBodyParts()).flatMap { it.values }
}