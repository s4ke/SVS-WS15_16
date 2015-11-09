import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Martin Braun (1249080), Julian Neuberger (1252734)
 */
public class VigenereCracker {

	private static final String TEST_STRING = "RCRHD DBAYG GHVZD DLQOI ZOSYM DVMKL MMPNR HNMYM STRTW HNMKR THQSM SCUXI MMCOX" +
			"YYAYG GHVZD DLAXM STRTM MCUXW BBAOX YBBRD RWURM STRTA NVROW HYFIL VCGFI MMVTH" +
			"RCRHD DBAYG GQVZD DHQKW BBAOX YYAJI ZOSJI LMPNR HNMYM STFOX YYAJI RJVZD DMPNR" +
			"HNMKV AYAAI STRTH DMPNR HNMNS KTEOX YYAYG GFVZD DL";

	public static final String[] FREQUENCIES = "H M M M H M M H H M M M M H H M L H H H M L L L L L".split( " " );
	public static final String[] FREQ_MOD = ("H M M M H M M H H M M M M H H M L H H H M L L L L L " +
			"H M M M H M M H H M M M M H H M L H H H M L L L L L " +
			"H M M M H M M H H M M M M H H M L H H H M L L L L L").split( " " );
	public static final int MOD_BASE = 26;

	public static double calcIC(String input) {
		double ic = 0;
		for ( char cur = 'A'; cur <= 'Z'; ++cur ) {
			double cnt = input.length() - input.replaceAll( String.valueOf( cur ), "" ).length();
			ic += (cnt * (cnt - 1)) / (input.length() * (input.length() - 1));
		}
		return ic;
	}

	public static int countChars(String str, char c) {
		return str.length() - str.replaceAll( String.valueOf( c ), "" ).length();
	}

	public static List<Tuple> getLongestRepetitions(String str, int maxSeqLength) {
		Map<String, Tuple> differences = new HashMap<>();
		for ( int start = 0; start < str.length(); ++start ) {
			for ( int seqLength = 1; seqLength <= str.length() - start; ++seqLength ) {
				//remove this to get all sequences
				if ( seqLength > maxSeqLength ) {
					break;
				}
				String sequence = str.substring( start, start + seqLength );
				int restOffset = 1;
				while ( start + seqLength + restOffset < str.length() ) {
					String rest = str.substring( start + seqLength + restOffset );
					if ( rest.startsWith( sequence ) ) {
						Tuple tuple = new Tuple();
						tuple.start = start;
						tuple.end = start + seqLength + restOffset + seqLength;
						tuple.string = sequence;
						tuple.distance = seqLength + restOffset;

						Tuple existingTuple = differences.get( sequence );
						if ( existingTuple == null || existingTuple.distance > tuple.distance ) {
							differences.put( sequence, tuple );
						}
						break;
					}
					++restOffset;
				}
			}
		}
		List<Tuple> tuples = new ArrayList<>( differences.values() );
		Collections.sort(
				tuples, (first, second) -> (-1) * Integer.compare(
						first.string.length(),
						second.string.length()
				)
		);
		for ( int i = 0; i < tuples.size(); ++i ) {
			List<Tuple> newList = new ArrayList<>();
			newList.addAll( tuples.subList( 0, i + 1 ) );
			Tuple cur = tuples.get( i );
			for ( int j = i + 1; j < tuples.size(); ++j ) {
				Tuple other = tuples.get( j );
				if ( cur.string.contains( other.string ) && (cur.end > other.end || cur.start < other.start) ) {
					// do nothing
				}
				else {
					newList.add( other );
				}
			}
			tuples = newList;
		}
		return tuples;
	}

	public static String d(double d) {
		DecimalFormat decimalFormat = new DecimalFormat( "#.############" );
		return decimalFormat.format( d );
	}

	public static String sanitize(String string) {
		return string.replaceAll( "\\s", "" ).toUpperCase( Locale.ROOT );
	}

	private static class Tuple implements Comparable<Tuple> {
		public String string;
		public int distance;
		public int start;
		public int end;

		@Override
		public int compareTo(Tuple o) {
			return (-1) * Integer.compare( distance, o.distance );
		}

		@Override
		public boolean equals(Object o) {
			if ( !(o instanceof Tuple) ) {
				return false;
			}
			Tuple t = (Tuple) o;
			return Integer.compare( start, t.start ) == 0;
		}

		@Override
		public int hashCode() {
			return Integer.hashCode( start );
		}

		@Override
		public String toString() {
			return "Tuple{" +
					"string='" + string + '\'' +
					", distance=" + distance +
					", start=" + start +
					", end=" + end +
					'}';
		}
	}

	public static List<Integer> primeFactors(int number) {
		int n = number;
		List<Integer> factors = new ArrayList<>();
		for ( int i = 2; i <= n; i++ ) {
			while ( n % i == 0 ) {
				factors.add( i );
				n /= i;
			}
		}
		return factors;
	}

	public static void main(String[] args) {
		String strOrig = TEST_STRING;
		if ( args.length == 0 ) {
			System.out.println( "assuming DEFAULT string" );
		}
		else {
			strOrig = args[0];
		}
		int maxKeyLength = 9;
		if ( args.length <= 1 ) {
			System.out.println( "assuming max-key length of 9" );
		}
		else {
			maxKeyLength = Integer.parseInt( args[1] );
		}
		String str = sanitize( strOrig );
		System.out.println( "Sanitized string:\n" + str );
		System.out.println( "\n" );
		System.out.println( "IC: " + d( calcIC( str ) ) );
		System.out.println( "\n" );
		List<Tuple> repetitions = getLongestRepetitions( str, maxKeyLength );
		Collections.sort( repetitions );
		System.out.println( "repetitions, sorted by delta" );
		for ( Tuple tuple : repetitions ) {
			System.out.print( "sequence: " + tuple.string );
			System.out.print( ", delta: " + tuple.distance );
			System.out.print( ", start: " + tuple.start );
			System.out.print( ", end: " + tuple.end );
			System.out.print( ", factors: " + new HashSet<>( primeFactors( tuple.distance ) ) );
			System.out.println();
		}

		if ( args.length >= 2 ) {
			System.out.println( "\n" );
			int keyLength = Integer.parseInt( args[2] );
			System.out.println( "using actual key length: " + keyLength );
			System.out.println( "split into strings of length " + keyLength + ":" );
			List<String> rows = new ArrayList<>();
			for ( int i = 0; i < Math.ceil( ((double) str.length()) / keyLength ); ++i ) {
				String curRow = str.substring(
						i * keyLength, Math.min(
								i * keyLength + keyLength,
								str.length()
						)
				);
				rows.add( curRow );
				System.out.println(
						curRow
				);
			}

			//calculate columns
			List<String> columns = new ArrayList<>();
			for ( int i = 0; i < keyLength; ++i ) {
				columns.add( "" );
			}
			for ( int i = 0; i < str.length(); ++i ) {
				int rowNum = i % keyLength;
				String oldString = columns.remove( rowNum );
				String newString = oldString + str.charAt( i );
				columns.add( rowNum, newString );
			}
			System.out.println( "\n" );
			for ( int i = 0; i < columns.size(); ++i ) {
				System.out.println( "Column " + i + ", " + columns.get( i ) + ", :" + calcIC( columns.get( i ) ) );
			}

			System.out.println( "\n" );
			int e = ('E' - 'A');
			int n = ('N' - 'A');
			System.out.println( "cccurences in the columns:" );
			int[][] cnts = new int[keyLength]['Z' - 'A' + 1];
			System.out.print( "| col" );
			for ( int i = 0; i < cnts[0].length; ++i ) {
				System.out.printf( "|   " + (char) ('A' + i) );
			}
			System.out.println();
			for ( int i = 0; i < keyLength; ++i ) {
				String column = columns.get( i );
				System.out.printf( "|#%3d", i );
				for ( int j = 0; j < cnts[i].length; ++j ) {
					cnts[i][j] = countChars( column, (char) ('A' + j) );
					System.out.printf( "| %3d", cnts[i][j] );
				}
				List<Integer> maxIndexes = maxIndex( cnts[i] );
				List<Character> maxChars = new ArrayList<>( maxIndexes.size() );
				maxChars.addAll(
						maxIndexes.stream()
								.map( val -> ((char) ('A' + val)) )
								.collect( Collectors.toList() )
				);
				System.out.print( "| max occurences: " + maxChars + " " );

				System.out.print( "| key candidates:" );
				for ( int x = 0; x < maxIndexes.size(); ++x ) {
					char maxChar = ((char) ('A' + maxIndexes.get( x )));
					{
						int keyDiffE = maxChar - 'E';
						if ( keyDiffE < 0 ) {
							keyDiffE = ('Z' - 'A' + 1) + keyDiffE;
						}
						int keyDiffN = maxChar - 'N';
						if ( keyDiffN < 0 ) {
							keyDiffN = ('Z' - 'A' + 1) + keyDiffN;
						}
						System.out.print( " " + (char) ('A' + keyDiffE) + " or " + (char) ('A' + keyDiffN) );
					}
				}
				System.out.println();

				String[] tmp = new String[26];

				//print the shifted frequencies
				for ( int x = 0; x < maxIndexes.size(); ++x ) {
					char maxChar = ((char) ('A' + maxIndexes.get( x )));
					{
						int keyDiffE = maxChar - 'E';
						if ( keyDiffE < 0 ) {
							keyDiffE = ('Z' - 'A' + 1) + keyDiffE;
						}

						System.out.print( "|k: " + (char) ('A' + keyDiffE) );
						int modStartE = MOD_BASE - keyDiffE;
						for ( int k = 0; k < 26; ++k ) {
							System.out.printf( "|   %s", FREQ_MOD[modStartE + k] );
						}

						System.arraycopy(
								FREQ_MOD,
								modStartE,
								tmp,
								0, 26
						);
						System.out.println( "| heuristic: " + heuristicValue( cnts[i], tmp ) + " (was to E) " );

						int keyDiffN = maxChar - 'N';
						if ( keyDiffN < 0 ) {
							keyDiffN = ('Z' - 'A' + 1) + keyDiffN;
						}
						System.out.print( "|k: " + (char) ('A' + keyDiffN) );
						int modStartN = MOD_BASE - keyDiffN;
						for ( int k = 0; k < 26; ++k ) {
							System.out.printf( "|   %s", FREQ_MOD[modStartN + k] );
						}

						System.arraycopy(
								FREQ_MOD,
								modStartN,
								tmp,
								0, 26
						);
						System.out.println( "| heuristic: " + heuristicValue( cnts[i], tmp ) + " (was to N) " );
					}
				}
				System.out.println();
			}
			System.out.print( "|orig" );
			for ( int i = 0; i < FREQUENCIES.length; ++i ) {
				System.out.printf( "|   " + FREQUENCIES[i] );
			}
			System.out.println( "|" );
		}

	}

	private static int heuristicValue(int[] vals, String[] frequencies) {
		int ret = 0;
		for ( int i = 0; i < vals.length; ++i ) {
			if ( frequencies[i].equals( "H" ) ) {
				ret += vals[i];
			}
		}
		return ret;
	}

	private static List<Integer> maxIndex(int[] array) {
		List<Wrapper> wrapper = new ArrayList<>();
		int index = 0;
		for ( int val : array ) {
			Wrapper w = new Wrapper();
			w.index = index++;
			w.val = val;
			wrapper.add( w );
		}
		Collections.sort( wrapper, (first, second) -> (-1) * Integer.compare( first.val, second.val ) );
		//4 maximale werden hier ausgegeben
		return wrapper.subList( 0, 4 ).stream().map( (obj) -> obj.index ).collect( Collectors.toList() );
	}

	private static class Wrapper {
		private int val;
		private int index;

		@Override
		public String toString() {
			return String.valueOf( index );
		}
	}

}
