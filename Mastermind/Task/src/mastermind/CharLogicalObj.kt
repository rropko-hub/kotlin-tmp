package mastermind

import kotlin.math.min

class CharLogicalObj {
    var difSecretCount: Int = 0;
    var difGuessCount: Int = 0;

    var coCount: Int = 0;

    fun getDifCount(): Int {
        return min(difGuessCount, difSecretCount);
    }
}
