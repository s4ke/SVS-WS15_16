package de.ubt.svs.braun.neuberger.SteinsAlgorithmGTT;

import java.text.MessageFormat;

/**
 * Created on 05.11.2015.
 *
 * @author Julian Neuberger
 */
public class SteinsAlgorithm {

    public static int solve(int first, int second) {
        int curFirst = first, curSecond = second, randomC = 1, tmp;
        boolean curFirstEven, curSecondEven;
        System.out.println(MessageFormat.format("Schritt 1 | A:{0}, B:{1}, C:{2}",first, second, randomC));
        int index = 2;
        while(curFirst != curSecond) {
            curFirstEven = curFirst % 2 == 0;
            curSecondEven = curSecond % 2 == 0;
            if(curFirstEven && curSecondEven) {
                curFirst /= 2;
                curSecond /= 2;
                randomC *= 2;
            } else if(curFirstEven && !curSecondEven) {
                curFirst /= 2;
                // curSecond stays the same
                // randomC stays the same
            } else if(!curFirstEven && curSecondEven) {
                // curFirst stays the same
                curSecond /= 2;
                // randomC stays same
            } else {
                tmp = Math.abs(curFirst - curSecond);
                curSecond = Math.min(curSecond, curFirst);
                curFirst = tmp;
                // randomC stays the same
            }

            System.out.println(MessageFormat.format("Schritt {0} | A:{1}, B:{2}, C:{3}", index++, curFirst, curSecond, randomC));
        }
        return (curFirst * randomC);
    }

    public static void main(String[] args) {
        int second = 0, first = 0;
        try {
            first = Integer.parseInt(args[0]);
            second = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: SteinsAlgorithm <a> <b>");
            System.exit(0);
        }
        int sol = SteinsAlgorithm.solve(first, second);
        System.out.println(MessageFormat.format("Solution for {0} and {1} is {2}", first, second, sol));
    }

}
