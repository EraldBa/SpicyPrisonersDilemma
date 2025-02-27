package agents

class Sucker : Agent() {
    /** always cooperate */
    override fun getActionFor(otherId: UInt) = Action.COOPERATE

    /** no reaction, since action always stays the same */
    override fun reactTo(otherId: UInt, action: Action) = Unit
}
