package core.body

class BodyPartsGenerated : BodyPartsCollection {
    override val values by lazy { listOf<BodyPartResource>(resources.body.CommonBodyParts()).flatMap { it.values }}
}