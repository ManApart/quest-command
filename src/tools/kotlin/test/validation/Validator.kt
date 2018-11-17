package test.validation

/*
This tool should validate things the compiler doesn't know to check, like unique names in data etc.

 */
fun main(args: Array<String>) {
    val warnings = CommandValidator().validate() +
            ActivatorValidator().validate() +
            LocationValidator().validate()
    println("Completed with $warnings warnings")
}

