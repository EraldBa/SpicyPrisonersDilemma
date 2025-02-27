package agents

class Pavlov : Agent() {
    private val _actionMemory = mutableMapOf<UInt, Action>()

    /**
     *  if the corresponding action for the agent are contained in history,
     *  return the action, else cooperate by default.
     */
    override fun getActionFor(otherId: UInt) = _actionMemory[otherId] ?: Action.COOPERATE

    /**
     * The [Pavlov] agent aims for a payoff that's considered adequate when interacting
     * with other agents. We assume that [Pavlov] desires a payoff that
     * is sizable and not insignificant, none or negative. Therefore, we can conclude
     * that whenever the other agent defects, the payoff is always bad, while
     * when it cooperates with [Pavlov], the payoff is always good
     * (see base [Agent] class's point values as reference).
     */
    override fun reactTo(otherId: UInt, action: Action) {
        val myAction = _actionMemory.getOrPut(otherId) { Action.COOPERATE }

        /**
         *  When the other agent defects, the payoff is always bad,
         *  so [Pavlov] will keep alternating strategies until
         *  the payoff is good, i.e. when the other cooperates.
         */
        if (action == Action.DEFECT) {
            _actionMemory[otherId] = !myAction
        }
    }
}
