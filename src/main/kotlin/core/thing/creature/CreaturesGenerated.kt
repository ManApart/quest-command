package core.thing.creature

class CreaturesGenerated : CreaturesCollection {
    override val values by lazy { listOf<CreatureResource>(resources.thing.creature.CommonCreatures()).flatMap { it.values }}
}