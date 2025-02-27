fun main(args: Array<String>) {
    println("***STARTING THE SIMULATION***\n")

    val simOptions = if (args.isNotEmpty())
        SimOptions.parse(args)
    else
        SimOptions()

    val simulation = Simulation(simOptions)
    simulation.run()

    println("\n***SIMULATION EXITED***")
}
