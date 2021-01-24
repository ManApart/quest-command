package validation

import core.conditional.ConditionalManager
import core.conditional.ConditionalString
import core.conditional.ConditionalStringType
import traveling.location.weather.WeatherManager

class ConditionalStringValidator {

    fun validate(): Int {
        return allEnumTypesPresent() +
                allConditionsHaveAtLeastOneOption() +
                noDuplicateKeys() +
                allWeathersExist()
    }

    private fun allEnumTypesPresent(): Int {
        return ConditionalStringType.values()
                .filter { it != ConditionalStringType.DEFAULT }
                .map { type ->
                    when {
                        ConditionalManager.getTypeMap()[type] == null -> {
                            println("WARN: $type is not represented in the Conditional Manager.")
                            1
                        }
                        ConditionalManager.getTypeMap()[type]?.isEmpty() ?: false -> {
                            println("WARN: $type is has no values in the Conditional Manager.")
                            1
                        }
                        else -> {
                            0
                        }
                    }
                }.sum()
    }

    private fun allConditionsHaveAtLeastOneOption(): Int {
        return ConditionalStringType.values()
                .filter { it != ConditionalStringType.DEFAULT }
                .map { eachConditionHasAtLeastOneOption(ConditionalManager.getTypeMap()[it]!!) }.sum()
    }

    private fun eachConditionHasAtLeastOneOption(conditions: List<ConditionalString>): Int {
        return conditions.map { condition ->
            if (condition.options.isEmpty()) {
                println("WARN: ${condition.name} doesn't have at least one option.")
                1
            } else {
                0
            }
        }.sum()
    }

    private fun noDuplicateKeys(): Int {
        return ConditionalStringType.values().map { noDuplicateKeys(it) }.sum()
    }

    private fun noDuplicateKeys(type: ConditionalStringType): Int {
        val conditions = ConditionalManager.getTypeMap()[type]!!
        val keys = mutableListOf<String>()

        return conditions.map { condition ->
            if (keys.contains(condition.name)) {
                println("WARN: $type has multiple conditions for '${condition.name}'.")
                1
            } else {
                keys.add(condition.name)
                0
            }
        }.sum()
    }

    private fun allWeathersExist(): Int {
        val weathers = ConditionalManager.getTypeMap()[ConditionalStringType.WEATHER]!!
        return weathers.flatMap { weather ->
            weather.options.map { option ->
                if (WeatherManager.weatherExists(option.option)) {
                    0
                } else {
                    println("WARN: Weather Option ${weather.name} references weather ${option.option} that does not exist.")
                    1
                }
            }
        }.sum()
    }
}