package com.hack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
		/*Integer one = 1;
		Field field = Integer.class.getDeclaredField( "value" );
		field.setAccessible( true );
		field.set( one, 2 );
		System.out.println( "1 + 1 = " + plus( 1, 1 ) );*/
		/*double n = 1_183_439;
		int x = (int) Math.ceil( Math.sqrt( n ) );
		double r = Math.pow( x, 2 ) - n;
		System.out.println( "x: " + x );
		System.out.println( "r: " + r );
		while ( !isQuad( r ) ) {
			r = r + 2 * x + 1;
			++x;
			System.out.println( "x: " + x );
			System.out.println( "r: " + r );
		}
		System.out.println( "x: " + x );
		System.out.println( "r: " + r );
		int y = (int) Math.sqrt( r );
		int a = x + y;
		int b = x - y;
		System.out.println( "a: " + a );
		System.out.println( "b: " + b );
		if ( !isPrime( a ) || !isPrime( b ) ) {
			throw new RuntimeException( "something went terribly wrong" );
		}*/
		List<Integer> stuff = new ArrayList<>();
		stuff.addAll( Arrays.asList( new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} ) );
		int base = 6;
		int index = 1;
		while(!stuff.isEmpty()) {
			int tmp = ((int)Math.pow( base, index++ )) % 11;
			stuff.remove( (Object) (Integer) (tmp) );
			System.out.println("6^"+index+" mod 11="+tmp + "; Restliche Reste: " + stuff);
		}

		System.out.println("Terminated");
	}

	public static boolean isQuad(double val) {
		double sqrt = Math.sqrt( val );
		double sqrtFloor = Math.floor( sqrt );
		return val == sqrtFloor * sqrtFloor;
	}

	public static boolean isPrime(int val) {
		for ( int i = 2; i < val; ++i ) {
			if ( val % i == 0 ) {
				return false;
			}
		}
		return true;
	}

	public static int plus(Integer x, int y) {
		return x + y;
	}

}