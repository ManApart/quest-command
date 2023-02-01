package core.thing.creature

class CreaturesGenerated : CreaturesCollection {
    override suspend fun values() = listOf<CreatureResource>(resources.thing.creature.CommonCreatures()).flatMap { it.values() }
}