package agents

class Traitor : Agent() {
    private val _memory = mutableMapOf<UInt, ActionMemory>()

    /**
     * The [Traitor] agent's strategy is based off of maximization
     * i.e. aiming for the maximum results out of every agent
     * the default for new encounters is to cooperate.
     */
    override fun getActionFor(otherId: UInt): Action {
        val actionMemory = _memory[otherId]
            ?: return Action.COOPERATE

        /**
         *  if agent did not cooperate last time and agent's defects
         *  are below the defect count threshold, cooperate.
         */
        if (actionMemory.action != Action.COOPERATE && actionMemory.defectCounter < MAX_DEFECTS_FROM_OTHER) {
            return Action.COOPERATE
        }

        /**
         * if agent cooperated the previous round or if the other agent
         * has not cooperated after the amount of rounds
         * specified by the [MAX_DEFECTS_FROM_OTHER] value, the [Traitor]
         * agent defects to maximize results or because it assumes further
         * cooperation is futile, respectively.
         */
        return Action.DEFECT
    }

    /** log the action of the other agent from the encounter */
    override fun reactTo(otherId: UInt, action: Action) {
        _memory.getOrPut(otherId) { ActionMemory() }.action = action
    }

    /**
     * the private companion object holds the 'static'
     * member variables and/or methods of the class.
     */
    private companion object {
        const val MAX_DEFECTS_FROM_OTHER: UInt = 2U
    }
}

/**
 * [ActionMemory] holds the agent's action as well as
 * how many times the agent has defected in a row.
 */
private class ActionMemory {
    var action: Action? = null
        set(value) {
            field = value
            /**
             *  if the set [action] is [Action.COOPERATE], the [defectCounter]
             *  gets reset, otherwise it get incremented.
             */
            when (value) {
                Action.COOPERATE -> defectCounter = 0U
                Action.DEFECT -> defectCounter++
                null -> Unit
            }
        }

    /**
     * the [defectCounter] can only be set inside the class,
     * since it gets automatically updated in the [action]'s setter.
     */
    var defectCounter: UInt = 0U
        private set
}
