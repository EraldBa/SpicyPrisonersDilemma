package agents

/**
 * the [Action] enum holds all possible actions when the agents interact
 * with each other.
 */
enum class Action {
    COOPERATE,
    DEFECT;

    operator fun not(): Action {
        return when (this) {
            COOPERATE -> DEFECT
            DEFECT -> COOPERATE
        }
    }
}
