package rationals

import com.sun.org.apache.xpath.internal.operations.Bool
import java.math.BigInteger


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

data class Rational(val numerator: BigInteger, val denominator: BigInteger = BigInteger.ONE) {

    val hasFullDivision: Boolean = false;


    private fun rationalToStringConverter(numerat: BigInteger, denumerat: BigInteger): String {

        if (denumerat == BigInteger.ONE) return "$numerat";

        var numeratAbs: BigInteger = numerat.abs();
        var denumeratAbs: BigInteger = denumerat.abs();

        val hasMinusSign: Boolean = (numerat.signum() < 0 && denumerat.signum() >= 0) || (denumerat.signum() < 0 && numerat.signum() >= 0);

        if (numeratAbs == denumeratAbs) return if (hasMinusSign) "-1" else "1";

        when (BigInteger.ZERO) {
            denumerat % numerat -> {

                numeratAbs /= numerat;
                denumeratAbs /= numerat;

                return rationalToStringConverter(numeratAbs, denumeratAbs);
            }

            numerat % denumerat -> {
                numeratAbs /= denumerat;
                denumeratAbs /= denumerat;

                return rationalToStringConverter(numeratAbs, denumeratAbs);
            }

            else -> {
                val compareIter = if (numeratAbs > denumeratAbs) denumeratAbs else numeratAbs;

                var iter: BigInteger = BigInteger.valueOf(2);

                while (iter <= compareIter) {
                    if (numeratAbs % iter == BigInteger.ZERO && denumeratAbs % iter == BigInteger.ZERO) {

                        numeratAbs /= iter;
                        denumeratAbs /= iter;

                        return rationalToStringConverter(numeratAbs, denumeratAbs);
                    }

                    iter = iter.add(BigInteger.ONE);
                }
            }
        }

        return if (hasMinusSign) "-$numeratAbs/$denumeratAbs" else "$numeratAbs/$denumeratAbs";
    }

    operator fun plus(rational: Rational): Rational {
        val denominator = this.denominator * rational.denominator;
        val numerator = denominator / this.denominator * this.numerator + denominator / rational.denominator * rational.numerator;

        return Rational(numerator, denominator);
    }

    operator fun minus(rational: Rational): Rational {
        val denominator = this.denominator * rational.denominator;
        val numerator = denominator / this.denominator * this.numerator - denominator / rational.denominator * rational.numerator;

        return Rational(numerator, denominator);
    }

    operator fun times(rational: Rational): Rational {
        val denominator = this.denominator * rational.denominator;
        val numerator = this.numerator * rational.numerator;

        return Rational(numerator, denominator);
    }

    operator fun div(rational: Rational): Rational {
        val denominator = this.denominator * rational.numerator;
        val numerator = this.numerator * rational.denominator;

        return Rational(numerator, denominator);
    }

    operator fun unaryMinus(): Rational {
        val denominator = this.denominator;
        val numerator = -this.numerator;

        return Rational(numerator, denominator);
    }

    operator fun compareTo(rational: Rational): Int {
        if (this.denominator == rational.denominator) {
            return if (this.numerator < rational.numerator) -1 else 1;
        }

        val a = this.numerator * rational.denominator;
        val b = this.denominator * rational.numerator;

        return if (a < b) -1 else 1;
    }

    operator fun rangeTo(rational: Rational): Pair<Rational, Rational> {
        return Pair(this, rational);
    }

    override fun toString(): String = rationalToStringConverter(numerator, denominator)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true;

        return !(other?.javaClass != javaClass || this.toString() != other.toString());
    }
}

operator fun Pair<Rational, Rational>.contains(rational: Rational): Boolean {
    return rational > this.first && rational < this.second;
}

infix fun Number.divBy(i: Number): Rational {
    return Rational(BigInteger.valueOf(this.toLong()), BigInteger.valueOf(i.toLong()));
}

fun String.toRational(): Rational {
    val ratElArr = this.split("/");

    if (ratElArr[1].toBigInteger() == BigInteger.ZERO)
        throw IllegalArgumentException("Denominator must not equal ZERO");

    return Rational(ratElArr[0].toBigInteger(), ratElArr[1].toBigInteger());
}
