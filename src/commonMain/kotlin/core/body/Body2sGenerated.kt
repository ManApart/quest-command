package core.body

class Body2sGenerated : Body2sCollection {
    override val values by lazy { listOf<Body2Resource>(resources.body.CommonBodies2()).flatMap { it.values }}
}