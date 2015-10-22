import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CaesarCracker {

	private static final String FIRST_STRING = "KEPNOXCSOLOXBYLLOXUVSZZOXCSDJOXCSOLOXBYLLOXCSZZOXNSOCSMRSXNSOBSZZO" +
			"XCDSZZOXLSCCSOFYXNOXUVSZZOXUSZZOX";

	private static final String SECOND_STRING = "MPZJOLYZMYPAGLMPZJOAMYPZJOLMPZJOLMYPZJOLMPZJOLMPZJOAMPZJOLYZMYPAGL";

	public static void main(String[] args) {
		decode( FIRST_STRING );
		decode( SECOND_STRING );
	}

	public static void decode(String string) {
		char[] chars = new char[26];
		for ( int i = 0; i < chars.length; ++i ) {
			chars[i] = (char) (i + 'A');
		}
		List<Wrapper> counts = new ArrayList<>();
		{
			System.out.println( "trying to decode with one of the keys: " + Arrays.toString( chars ) );

			char[] stringAsArray = string.toCharArray();

			int[] count = new int[26];

			for ( int i = 0; i < stringAsArray.length; ++i ) {
				count[stringAsArray[i] - 'A']++;
			}

			System.out.println( "count array: " + Arrays.toString( count ) );

			for ( int i = 0; i < count.length; ++i ) {
				counts.add( new Wrapper( i, (char) ('A' + i), count[i] ) );
			}

			System.out.println( counts );
			Collections.sort( counts );
			System.out.println( counts );
		}

		System.out.println( "decoding word." );
		for ( Wrapper pair : counts ) {
			char[] stringAsArray = string.toCharArray();
			for ( int i = 0; i < stringAsArray.length; ++i ) {
				int diff = Math.abs(pair.index - 'E');
				int newIndex = (stringAsArray[i] + diff) % 26;
				stringAsArray[i] = chars[newIndex];
			}
			System.out.println( "using " + pair + " resulted in: " + String.copyValueOf( stringAsArray ) );
		}
	}

	private static class Wrapper implements Comparable<Wrapper> {
		private final int index;
		private final char character;
		private final int count;

		public Wrapper(int index, char character, int count) {
			this.index = index;
			this.character = character;
			this.count = count;
		}

		public int compareTo(Wrapper o) {
			return (-1) * Integer.compare( this.count, o.count );
		}

		@Override
		public String toString() {
			return "Wrapper{" +
					"index=" + index +
					", character=" + character +
					", count=" + count +
					'}';
		}
	}

}
