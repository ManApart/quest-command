package status.conditions

class ConditionsGenerated : ConditionsCollection {
    override val values by lazy { listOf<ConditionResource>(resources.status.CommonConditions()).flatMap { it.values }}
}