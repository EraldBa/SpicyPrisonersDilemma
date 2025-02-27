/** [SimOptions] holds the data necessary for the simulation to run */
data class SimOptions(
    val includeSuckers: Boolean = true,
    val includeCheaters: Boolean = true,
    val includeGrudgers: Boolean = true,
    val includeTitForTats: Boolean = true,
    val includeTraitors: Boolean = true,
    val includeCynics: Boolean = true,
    val includePavlovs: Boolean = true,
    val populationCount: UInt = DEFAULT_POPULATION_COUNT,
    val generations: UInt = DEFAULT_GENERATIONS,
) {
    /**
     * the [Parser] companion object holds all the data and
     * logic necessary for correct SimOption parsing from an Array<String>.
     */
    companion object Parser {
        private const val DEFAULT_GENERATIONS: UInt = 10_000U
        private const val DEFAULT_POPULATION_COUNT: UInt = 150U

        const val MIN_GENERATIONS: UInt = 1_000U

        /**
         * [parse] parses the SimOption data from an Array<String>
         * and returns the SimOption instance with the parsed data.
         */
        fun parse(arr: Array<String>) = SimOptions(
            includeSuckers = "-sucker" in arr,
            includeCheaters = "-cheater" in arr,
            includeGrudgers = "-grudger" in arr,
            includeTitForTats = "-tit_for_tat" in arr,
            includeTraitors = "-traitor" in arr,
            includeCynics = "-cynic" in arr,
            includePavlovs = "-pavlov" in arr,
            populationCount = parsePopulationCountFrom(arr),
            generations = parseGenerationsFrom(arr),
        )


        /**
         * [parseGenerationsFrom] parses the generations option from the
         * provided Array<String> and returns the parsed argument or
         * the [DEFAULT_GENERATIONS] if not found.
         */
        private fun parseGenerationsFrom(arr: Array<String>): UInt {
            val generationsIndex = arr.indexOf("-g")
            if (generationsIndex == -1) {
                return DEFAULT_GENERATIONS
            }

            val generations = arr.getOrNull(generationsIndex + 1)?.toUIntOrNull()
                ?: throw IllegalArgumentException("Generations count value is not a valid number")

            require(generations >= MIN_GENERATIONS) {
                "Generations number is below minimum: $MIN_GENERATIONS"
            }

            return generations
        }

        /**
         * [parsePopulationCountFrom] parses the population count options from
         * the provided Array<String> and returns the parsed argument or
         * the [DEFAULT_POPULATION_COUNT] if not found.
         */
        private fun parsePopulationCountFrom(arr: Array<String>): UInt {
            val populationIndex = arr.indexOf("-p")
            if (populationIndex == -1) {
                return DEFAULT_POPULATION_COUNT
            }

            val populationCount = arr.getOrNull(populationIndex + 1)?.toUIntOrNull()
                ?: throw IllegalArgumentException("Population count value is not a valid number")

            require(populationCount > 0U) {
                "Population count must be at least 1"
            }

            return populationCount
        }
    }
}
