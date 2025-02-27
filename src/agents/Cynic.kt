package agents

class Cynic : Agent() {
    private val _trusted = hashSetOf<UInt>()

    /** if the agent is trusted, cooperate, else defect */
    override fun getActionFor(otherId: UInt): Action {
        return if (_trusted.contains(otherId))
            Action.COOPERATE
        else
            Action.DEFECT
    }

    /** trust the agent if it cooperated at least once */
    override fun reactTo(otherId: UInt, action: Action) {
        if (action == Action.COOPERATE) {
            _trusted.add(otherId)
        }
    }
}
