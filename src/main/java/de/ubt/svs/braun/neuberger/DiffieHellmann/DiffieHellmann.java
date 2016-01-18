package de.ubt.svs.braun.neuberger.DiffieHellmann;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created on 15.01.2016.
 *
 * @author Julian Neuberger
 */
public class DiffieHellmann {

    public static final int MAX_TRIES_FOR_ROOT = 150;
    public static final Random RAND = new Random();

    public static void main(String[] args) {
        BigInteger randomPrime = findRandomPrimeWithDigits(100);
        System.out.println("Random prime is " + randomPrime);
        System.out.println(findPrimitiveRootForPrime(randomPrime, 3));
    }

    /**
     * finds a prime with 1 - (1/2)^10 % certainty
     * @param digits the number of digits in base 10
     * @return a number which is prime with 1 - (1/2)^10 % certainty
     */
    public static BigInteger findRandomPrimeWithDigits(int digits) {
        BigInteger min = BigInteger.TEN.pow(digits);
        BigInteger prime = BigInteger.probablePrime(min.bitLength(), RAND);
        while(!prime.isProbablePrime(10)) {
            prime = BigInteger.probablePrime(min.bitLength(), RAND);
        }
        return prime;
    }

    /**
     * @param number needs to be prime
     * @return a primitve root with a minimum of minDigits digits
     */
    public static BigInteger findPrimitiveRootForPrime(BigInteger number, int minDigits) {
        List<BigInteger> remainingNumbersSrc = new ArrayList<>();
        List<BigInteger> remainingNumbers;
        for(BigInteger i = BigInteger.ZERO; i.compareTo(number) == -1; i = i.add(BigInteger.ONE)) {
            remainingNumbersSrc.add(i);
        }

        BigInteger min = BigInteger.TEN.pow(minDigits);
        BigInteger rootCandidate = null;
        boolean found = false;
        return rootCandidate;
    }

    private static boolean testPrimitiveRoot(BigInteger number, BigInteger rootCandidate, List<BigInteger> remainingNumbers) {
        int currentTry = 0;
        while(currentTry < MAX_TRIES_FOR_ROOT) {
            remainingNumbers.remove(rootCandidate.pow(currentTry).mod(number));
            if(remainingNumbers.size()==0) {
                return true;
            }
            ++currentTry;
        }

        return false;
    }
}
