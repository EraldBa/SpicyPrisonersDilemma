package agents

/** the [Agent] class is the base class of all agents */
abstract class Agent {
    /** each agent has a unique identifier */
    private val _id: UInt = idTracker++

    /** each agent starts off with the same amount of points */
    var points: Long = 5_000
        private set

    /**
     * [getActionFor] gets the action of the agent for the agent
     * it interacts with according to the agent's identifier.
     * Preferably, it should not change the state of the class.
     */
    protected abstract fun getActionFor(otherId: UInt): Action

    /**
     * [reactTo] gets called after the encounter with the other agent
     * has finished with the other agent's id and action.
     * All reaction logic should be placed in this method.
     */
    protected abstract fun reactTo(otherId: UInt, action: Action)

    /** [survives] determines whether the agent survives or not */
    fun survives() = points > 0

    /** [wearOut] wears out the agent by a pre-determined amount */
    fun wearOut() {
        points -= 5
    }

    /**
     * [interactWith] holds all the interaction logic for
     * the base [Agent] class. This is the method that's called every
     * time an agent interacts with another agent.
     */
    fun interactWith(other: Agent) {
        val action = getActionFor(other._id)
        val otherAction = other.getActionFor(_id)

        val (myPoints, otherPoints) = when (action) {
            Action.COOPERATE -> cooperateAnd(otherAction)
            Action.DEFECT -> defectAnd(otherAction)
        }

        points += myPoints
        other.points += otherPoints

        reactTo(other._id, otherAction)
        other.reactTo(_id, action)
    }

    /**
     * all 'static' member variables and logic are held in
     * this private companion object.
     */
    private companion object {
        /** tracks the last id of a created agent */
        var idTracker: UInt = 0U

        /** The point pairs for all possible interaction outcomes */
        val BOTH_COOPERATE_POINTS = Pair(3, 3)
        val BOTH_DEFECT_POINTS = Pair(1, 1)
        val SELF_COOP_OTHER_DEFECTS_POINTS = Pair(0, 5)
        val SELF_DEFECT_OTHER_COOPS_POINTS = Pair(5, 0)

        /**
         * [cooperateAnd] determines the interaction points based on
         * the assumption that the agent cooperated with the other.
         */
        fun cooperateAnd(otherAction: Action): Pair<Int, Int> {
            return when (otherAction) {
                Action.COOPERATE -> BOTH_COOPERATE_POINTS
                Action.DEFECT -> SELF_COOP_OTHER_DEFECTS_POINTS
            }
        }

        /**
         * [defectAnd] determines the interaction points based on
         * the assumption that the agent defected on the other.
         */
        fun defectAnd(otherAction: Action): Pair<Int, Int> {
            return when (otherAction) {
                Action.COOPERATE -> SELF_DEFECT_OTHER_COOPS_POINTS
                Action.DEFECT -> BOTH_DEFECT_POINTS
            }
        }
    }
}
