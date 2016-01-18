package de.ubt.svs.braun.neuberger.DiffieHellmann;

import java.math.BigInteger;
import java.util.*;

/**
 * Created on 15.01.2016.
 *
 * @author Julian Neuberger
 */
public class DiffieHellmann {

    public static final int MAX_TRIES_FOR_ROOT = 150;
    public static final Random RAND = new Random();

    public static void main(String[] args) {
        long randomPrime = findRandomPrimeWithDigits(10);
        System.out.println("Random prime is " + randomPrime);
        System.out.println("Factorization of phi(prime): " + primeFactorize(randomPrime - 1));
        System.out.println("Primitive root: " + findPrimitiveRootForPrime(randomPrime, 3));
    }

    /**
     * finds a prime with 1 - (1/2)^10 % certainty
     * @param digits the number of digits in base 10
     * @return a number which is prime with 1 - (1/2)^10 % certainty
     */
    public static long findRandomPrimeWithDigits(int digits) {
        long min = (long) Math.pow(10, digits - 1);
        BigInteger minBig = BigInteger.valueOf(min);
        BigInteger prime = BigInteger.probablePrime(minBig.bitLength() + 1, RAND);
        while(!prime.isProbablePrime(10) || prime.bitLength() > 64 || prime.compareTo(minBig) < 0) {
            prime = BigInteger.probablePrime(minBig.bitLength(), RAND);
        }
        return prime.longValue();
    }

    private static boolean checkPrimitiveRoot(long g, long p,
                                              long n, Set<Long> factors) {
        // Run g^(n / "each factor) mod p
        // If the is 1 mod p then g is not a primitive root
        for (Long factor : factors) {
            if (fastExponentiation(BigInteger.valueOf(g), BigInteger.valueOf(n).divide(BigInteger.valueOf(factor)), BigInteger.valueOf(p))
                    .equals(BigInteger.ONE)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param number needs to be prime
     * @return a primitve root with a minimum of minDigits digits
     */
    public static long findPrimitiveRootForPrime(long number, int minDigits) {
        long min = (long) Math.pow(10, minDigits - 1);
        long primitiveCandidate = RAND.nextInt(899) + min;
        while(!checkPrimitiveRoot(primitiveCandidate, number, number - 1, primeFactorize(number))) {
            System.out.println("Testing candidate " + primitiveCandidate);
            primitiveCandidate = RAND.nextInt(899) + min;
        }
        return primitiveCandidate;
    }

    private static Set<Long> primeFactorize(long number) {
        Set<Long> ret = new HashSet<>();
        long curNumber = number;
        for(long i = 2; i <= number / 2; i++) {
            if(curNumber % i == 0) {
                ret.add(i);
                curNumber /= i;
                --i;
            }
        }

        return ret;
    }

    // shamelessly stolen from https://github.com/bhepburn/CS789/blob/master/src/functions/FastExponentiation.java
    public static BigInteger fastExponentiation(BigInteger base,
                                                BigInteger exponent, BigInteger modulus) {
        return recurseFastExponentiation(base, exponent, modulus,
                BigInteger.ONE);
    }

    private static BigInteger recurseFastExponentiation(BigInteger base,
                                                        BigInteger exponent, BigInteger modulus, BigInteger result) {

        if (exponent.equals(BigInteger.ZERO)) {
            return result;
        } else if (exponent.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
            return recurseFastExponentiation(base,
                    exponent.subtract(BigInteger.ONE), modulus,
                    base.multiply(result).mod(modulus));
        } else {
            return recurseFastExponentiation(base.multiply(base).mod(modulus),
                    exponent.divide(BigInteger.valueOf(2)), modulus, result);
        }
    }

}
