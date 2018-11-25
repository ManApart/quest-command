package core.gameState.dataParsing

import com.fasterxml.jackson.annotation.JsonProperty

class Query(private val property: String, val params: List<String> = listOf(), @JsonProperty("operator") operatorName: String, val value: String) {
    private val operator: Operator = Operator.getOperator(operatorName)

    fun evaluate() : Boolean {
        val propertyVal = GamestateQuery.getValue(property, params)
        return operator.evaluate(propertyVal, value)
    }
}
