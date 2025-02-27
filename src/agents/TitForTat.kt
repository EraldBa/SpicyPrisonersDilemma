package agents

class TitForTat : Agent() {
    private val _actionMemory = mutableMapOf<UInt, Action>()

    /** if agent's actions are contained in history, return the action, else cooperate by default */
    override fun getActionFor(otherId: UInt) = _actionMemory[otherId] ?: Action.COOPERATE

    /** remember agent's action for next encounter */
    override fun reactTo(otherId: UInt, action: Action) {
        _actionMemory[otherId] = action
    }
}
