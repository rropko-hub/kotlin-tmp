package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {

    if (secret.length != guess.length) {
        throw Exception("Incorrect length of guess string. must be " + secret.length);
    }

    val secretList = secret.toMutableList();
    val guessList = guess.toMutableList();

    val markedCharMap: MutableMap<Char, CharLogicalObj> = mutableMapOf();

    for (i in 0 until guessList.count()) {
        val sChar = secretList[i];
        val gChar = guessList[i];

        if (sChar == gChar) {
            if (markedCharMap[sChar] != null) {
                markedCharMap[sChar]!!.coCount++;
            } else {
                val charLogicalObj = CharLogicalObj();
                charLogicalObj.coCount++;

                markedCharMap[sChar] = charLogicalObj;
            }
        } else {
            if (markedCharMap[gChar] != null) {
                markedCharMap[gChar]!!.difGuessCount++;
            } else {
                val charLogicalObj = CharLogicalObj();
                charLogicalObj.difGuessCount++;

                markedCharMap[gChar] = charLogicalObj;
            }

            if (markedCharMap[sChar] != null) {
                markedCharMap[sChar]!!.difSecretCount++;
            } else {
                val charLogicalObj = CharLogicalObj();
                charLogicalObj.difSecretCount++;

                markedCharMap[sChar] = charLogicalObj;
            }
        }
    }

    var rightPositionCount = 0;
    var wrongPositionCount = 0;

    markedCharMap.forEach{ obj ->
        rightPositionCount = rightPositionCount.plus(obj.value.coCount);
        wrongPositionCount = wrongPositionCount.plus(obj.value.getDifCount());
    }

    return Evaluation(rightPositionCount, wrongPositionCount);
}
