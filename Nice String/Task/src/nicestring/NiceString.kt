package nicestring

fun String.isNice(): Boolean {

    if (this.partition { it.isUpperCase() }.first.isNotEmpty())
        throw Exception("Must contains Big letters!!!");

    val vowels = listOf('a','e','i','o','u')

    val doesNotContainsBuBaBe = !this.contains("bu")
            && !this.contains("ba")
            && !this.contains("be")

    val containsAtLeastThreeVowels = this.count { vowels.contains(it) } >= 3

    var containsADoubleLetterFollowingEachOther = false
    this.forEachIndexed { index, element ->
        if(index != 0){
            if(this.get(index - 1) == element) {
                containsADoubleLetterFollowingEachOther = true
            }
        }
    }

    return listOf(doesNotContainsBuBaBe, containsAtLeastThreeVowels, containsADoubleLetterFollowingEachOther)
        .filter { it }
        .count() >= 2
}
