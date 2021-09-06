package status.conditions

class ConditionsGenerated : ConditionsCollection {
    override val values = listOf<ConditionResource>(resources.status.CommonConditions()).flatMap { it.values }
}