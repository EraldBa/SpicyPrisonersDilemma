import agents.*

/**
 * The [Simulation] class hold all the data and logic for
 * the prisoner's dilemma simulation to run.
 */
class Simulation(private var simOptions: SimOptions) {
    /** generation tracks the current generation in the whole simulation */
    private var generation: UInt = 0U

    /** the [Agent] population of the whole simulation */
    private var population: MutableList<Agent>

    init {
        population = generatePopulation()
    }

    /**
     * [run] runs the simulation in a loop along with the
     * prompts for the user. The loop breaks when the user decides to.
     */
    fun run() {
        while (true) {
            runTheSimulation()

            print("The simulation is finished.\nWould you like to go again? (y/n): ")
            val goAgain = getUserConfirmation()

            if (!goAgain)
                break

            print("Do you wish to add more agents and/or a different generation count? (y/n): ")
            val addMoreOptions = getUserConfirmation()

            if (addMoreOptions) {
                println("Please give new options:")
                simOptions = SimOptions.parse(getUserOptions())
                population.addAll(generatePopulation())
            }
        }
    }

    /**
     * [getUserConfirmation] gets, validates and returns
     * the user's confirmation option.
     */
    private fun getUserConfirmation(): Boolean {
        var userInput = readlnOrNull()?.trim()
        while (userInput != "y" && userInput != "n") {
            print("\nInvalid input, need (y/n): ")
            userInput = readlnOrNull()?.trim()
        }

        return userInput == "y"
    }

    /**
     * [getUserOptions] gets, validates and returns the simulation
     * options provided by the user.
     */
    private fun getUserOptions(): Array<String> {
        val whitespace = " "
        var userOptions = readlnOrNull()?.trim()?.split(whitespace)

        while (userOptions.isNullOrEmpty() || userOptions.first().isEmpty()) {
            println("\nOptions not provided, please try again:")
            userOptions = readlnOrNull()?.trim()?.split(whitespace)
        }

        return userOptions.toTypedArray()
    }

    /** [runTheSimulation] runs the simulation */
    private fun runTheSimulation() {
        require(population.isNotEmpty()) {
            "No population for the simulation to run"
        }

        printStatsFor(population)

        for (i in 1U..simOptions.generations) {
            generation++

            population.shuffle()

            /** engage the population as long as wear it out */
            for ((first, second) in population.zipWithNext()) {
                first.interactWith(second)
                first.wearOut()
            }
            population.lastOrNull()?.wearOut()

            /** keep the survivors only */
            population.retainAll { it.survives() }

            if (population.isEmpty()) {
                println("\nNo survivors left on generation $generation")
                return
            }

            /** Every [SimOptions.MIN_GENERATIONS] iterations, print stats */
            if (generation % SimOptions.MIN_GENERATIONS == 0U) {
                printStatsFor(population)
            }
        }

        /** Print the last generation if it was not covered above */
        if (simOptions.generations % SimOptions.MIN_GENERATIONS != 0U) {
            printStatsFor(population, simOptions.generations)
        }
    }

    /** [generatePopulation] generates the population according to the [simOptions] */
    private fun generatePopulation(): MutableList<Agent> {
        return sequence {
            listOf(
                simOptions.includeSuckers to ::Sucker,
                simOptions.includeCheaters to ::Cheater,
                simOptions.includeGrudgers to ::Grudger,
                simOptions.includeTitForTats to ::TitForTat,
                simOptions.includeTraitors to ::Traitor,
                simOptions.includeCynics to ::Cynic,
                simOptions.includePavlovs to ::Pavlov,
            ).forEach { (includeAgent, constructor) ->
                if (includeAgent) {
                    for (i in 1U..simOptions.populationCount) {
                        yield(constructor())
                    }
                }
            }
        }.toMutableList()
    }

    /**
     * [printStatsFor] prints the stats for the provided [population] and [currentGen]
     * If [currentGen] is not provided, then the global [generation] is assumed as the value
     * This method could have been more efficient, but it would turn ugly and complicated
     * as a consequence, without providing all that much perf gain in the long run.
     */
    private fun printStatsFor(population: MutableList<Agent>, currentGen: UInt = generation) {
        println("---------AGENT STATS FOR GENERATION $currentGen---------")

        val agentMap = population.groupBy {
            it::class.simpleName?.uppercase() ?: "UNKNOWN"
        }.toSortedMap()

        if (agentMap.isEmpty()) {
            println("\nNo survivors left on generation $currentGen")
            return
        }

        println("┌──────────────┬──────────────┬──────────────┐")
        println("│  Agent Type  │  Population  │ Avg. Points  │")
        println("├──────────────┼──────────────┤──────────────┤")

        agentMap.onEachIndexed { index, (name, agentList) ->
            println("│ %-12s │ %12d │ %12d │".format(name, agentList.size, getAveragePointsFor(agentList)))
            if (index < agentMap.size - 1) {
                println("├──────────────┼──────────────┤──────────────┤")
            }
        }

        println("└──────────────┴──────────────┴──────────────┘\n")
    }

    /** [getAveragePointsFor] gets the average for the provided [population],
     * and returns the average or zero if the population is empty.
     */
    private fun getAveragePointsFor(population: List<Agent>): Long {
        return if (population.isEmpty())
            0L
        else
            population.sumOf { it.points } / population.size
    }
}
