package agents

class Cheater : Agent() {
    /** always defect */
    override fun getActionFor(otherId: UInt) = Action.DEFECT

    /** no reaction, since action always stays the same */
    override fun reactTo(otherId: UInt, action: Action) = Unit
}
