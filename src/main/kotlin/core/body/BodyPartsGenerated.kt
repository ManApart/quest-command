package core.body

class BodyPartsGenerated : BodyPartsCollection {
    override val values = listOf<BodyPartResource>().flatMap { it.values }
}