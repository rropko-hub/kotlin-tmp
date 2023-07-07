package nicestring

fun String.isNice(): Boolean {

    if (this.partition { it.isUpperCase() }.first.isNotEmpty())
        throw Exception("Must contains Big letters!!!");

    val minVowelsCount = 3;
    var currentVowelsCount = 0;

    val revCharList: ArrayList<Char> = arrayListOf('u', 'a', 'e');
    val contCharList: ArrayList<Char> = arrayListOf('a', 'e', 'i', 'o', 'u');

    val arr: CharArray = this.toCharArray();

    var hasCorrectContains: Boolean = true;
    var hasCorrectVowels: Boolean = false;
    var hasDoubleLetters: Boolean = false;

    for (i in arr.indices) {
        val current = arr[i];

        if (contCharList.contains(current) && currentVowelsCount < minVowelsCount)
            currentVowelsCount++;

        if (i + 1 < arr.size) {
            val next = arr[i + 1];

            if (!hasDoubleLetters && current == next)
                hasDoubleLetters = true;

            if (hasCorrectContains && (current == 'b' && revCharList.contains(next)))
                hasCorrectContains = false;
        } else {
            if (currentVowelsCount >= minVowelsCount) {
                hasCorrectVowels = true;
            }
        }
    }

    return (hasCorrectContains && hasCorrectVowels)
            || (hasCorrectContains && hasDoubleLetters)
            || (hasCorrectVowels && hasDoubleLetters);
}
