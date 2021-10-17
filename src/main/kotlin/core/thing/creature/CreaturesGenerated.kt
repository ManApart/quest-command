package core.thing.creature

class CreaturesGenerated : CreaturesCollection {
    override val values = listOf<CreatureResource>(resources.thing.creature.CommonCreatures()).flatMap { it.values }
}