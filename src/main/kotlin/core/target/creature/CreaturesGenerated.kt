package core.target.creature

class CreaturesGenerated : CreaturesCollection {
    override val values = listOf<CreatureResource>(resources.target.creature.CommonCreatures()).flatMap { it.values }
}