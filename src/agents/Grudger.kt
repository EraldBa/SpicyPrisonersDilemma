package agents

class Grudger : Agent() {
    private val _notTrusted = hashSetOf<UInt>()

    /** if agent is not trusted, defect, else cooperate */
    override fun getActionFor(otherId: UInt): Action {
        return if (_notTrusted.contains(otherId))
            Action.DEFECT
        else
            Action.COOPERATE
    }

    /** if agent defected even once, distrust it */
    override fun reactTo(otherId: UInt, action: Action) {
        if (action == Action.DEFECT) {
            _notTrusted.add(otherId)
        }
    }
}
