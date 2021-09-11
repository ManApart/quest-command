package core.body

class BodyPartsGenerated : BodyPartsCollection {
    override val values = listOf<BodyPartResource>(resources.body.CommonBodyParts()).flatMap { it.values }
}