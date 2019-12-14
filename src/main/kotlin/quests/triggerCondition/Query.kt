package quests.triggerCondition

import com.fasterxml.jackson.annotation.JsonProperty
import core.utility.apply

class Query(private val property: String, val params: List<String> = listOf(), @JsonProperty("operator") operatorName: String, val value: String) {
    private val operator: Operator = Operator.getOperator(operatorName)

    fun evaluate(params: Map<String, String> = mapOf()) : Boolean {
        val propertyVal = GameStateQuery.getValue(this.property.apply(params), this.params.apply(params))
        return operator.evaluate(propertyVal, value)
    }
}
