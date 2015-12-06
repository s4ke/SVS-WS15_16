package de.ubt.svs.braun.neuberger.HillsCipher;

import Jama.Matrix;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 03.12.2015.
 *
 * @author Julian Neuberger
 */
public class HillsCipher {
    private static final String TEST_MESSAGE = "basketball";
    private static final String ENCRYPTED_TEST_MESSAGE = "tdeuxzvgwxvltxmn";

    private int dimension;
    private Matrix key;

    public HillsCipher(int dimension, Matrix key) {
        if(key.getColumnDimension() != dimension) {
            throw new IllegalArgumentException("key dimension is not equal to block size");
        }
        this.dimension = dimension;
        this.key = key;
    }

    public String encrypt(String message) {
        return this.deOrEncrypt(message, this.key);
    }

    public String decrypt(String message) {
        Matrix hillInverse = this.key.inverse().times(this.key.det());
        hillInverse = this.matrixMod(hillInverse, 26);
        hillInverse = hillInverse.times(this.modularMultiplicativeInverse(correctMod((int) Math.round(this.key.det()), 26), 26));
        hillInverse = this.matrixMod(hillInverse, 26);
        hillInverse = this.matrixRound(hillInverse);

        return this.deOrEncrypt(message, hillInverse);
    }

    public static List<String> splitEqually(String text, int size) {
        List<String> ret;
        ret = new ArrayList<>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }

    private String deOrEncrypt(String message, Matrix key) {
        System.out.println(MessageFormat.format("Using the message {0} with key {1}",
                message,
                Arrays.deepToString(key.getArray())));

        // divide message into blocks with correct length
        List<String> blocks = HillsCipher.splitEqually(message, this.dimension);

        String result = "";

        for(String block : blocks) {
            // create vector out of block
            Matrix blockVector = new Matrix(this.dimension, 1);
            for(int i = 0; i < block.length(); i++) {
                char cur = block.charAt(i);
                blockVector.set(i, 0, (int) cur - (int) 'a');
            }

            // multiply key times vector
            Matrix encr = key.times(blockVector);
            System.out.print(MessageFormat.format("Multiplying: key x {0}={1}, \nwhich is: ",
                    Arrays.deepToString(blockVector.getArray()),
                    Arrays.deepToString(encr.getArray())));

            // read the resulting vector and write it to the result-string
            for(int i = 0; i < block.length(); i++) {
                char curChar = (char)(correctMod((int) Math.round(encr.get(i, 0)), 26) + (int) 'a');
                result += curChar;
                System.out.print(curChar);
            }
            System.out.println();
        }

        return result;
    }

    private Matrix matrixMod(Matrix matrix, int mod) {
        Matrix ret = new Matrix(matrix.getColumnDimension(), matrix.getRowDimension());
        for(int column = 0; column < matrix.getColumnDimension(); column++) {
            for(int row = 0; row < matrix.getRowDimension(); row++) {
                ret.set(row, column, correctMod((int) Math.round(matrix.get(row, column)), 26));
            }
        }
        return ret;
    }

    private Matrix matrixRound(Matrix matrix) {
        Matrix ret = new Matrix(matrix.getColumnDimension(), matrix.getRowDimension());
        for(int column = 0; column < matrix.getColumnDimension(); column++) {
            for(int row = 0; row < matrix.getRowDimension(); row++) {
                ret.set(row, column, Math.round(matrix.get(row, column)));
            }
        }
        return ret;
    }

    private int modularMultiplicativeInverse(int n, int m) {
        n %= m;
        for (int i = 1; i < m; i++) {
            if((n * i) % m == 1) {
                return i;
            }
        }
        return -1;
    }

    private int correctMod(int i, int mod) {
        int tmp = i % mod;
        return i < 0 ? tmp + mod : tmp;
    }

    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            System.out.println("Usage: HillsCipher <key> <d(ecrypt)/e(ncrypt)> <message>");
            System.out.println("Provide key as matrix in form of a,b,c;d,e,f;g,h,i");
            System.out.println(MessageFormat.format("If you provide no message, it will default to '{0}' for encryption " +
                    "or '{1}' for decryption.", TEST_MESSAGE, ENCRYPTED_TEST_MESSAGE));
        } else {
            String[] matrixRows = args[0].split(";");
            int dimension = matrixRows.length;
            Matrix key = new Matrix(dimension, dimension);
            int row = 0;
            for (String matrixRow : matrixRows) {
                String[] cur = matrixRow.split(",");
                if(cur.length != dimension) {
                    System.out.println(MessageFormat.format("Row {0} of the matrix is not well formed, " +
                            "expected {1} elements, found {2}", row, dimension, cur.length));
                }
                int column = 0;
                for (String s : cur) {
                    key.set(row, column, Integer.parseInt(s));
                    column++;
                }
                row++;
            }

            HillsCipher hillsCipher = new HillsCipher(key.getColumnDimension(), key);
            String mode = args[1];
            boolean encrypt = mode.equals("e") || mode.equals("encrypt");
            if(!encrypt && !(mode.equals("d") || mode.equals("decrypt"))) {
                System.out.println("Please specify either d or e as second parameter, aborting now...");
                System.exit(0);
            }
            String message;
            try {
                message = args[2];
                // this may as well be the most inefficient way to fill the missing chars... :D
                while(message.length() % key.getColumnDimension() != 0) {
                    message += "x";
                }
            } catch(ArrayIndexOutOfBoundsException e) {
                message = encrypt ? TEST_MESSAGE : ENCRYPTED_TEST_MESSAGE;
            }
            String res = null;
            if(encrypt) {
                res = hillsCipher.encrypt(message);
            } else {
                res = hillsCipher.decrypt(message);
            }

            System.out.println("Result: " + res);
        }
        System.exit(0);
    }
}
