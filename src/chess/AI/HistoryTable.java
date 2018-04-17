package chess.AI;

/**
 * 
 * @author nNbS
 * 
 *   This class records how many times a step has been searched.
 */

public final class HistoryTable {
	public static final int STEP_UPPER_BOUND = 64 * 64;
	private static int[] table = new int[STEP_UPPER_BOUND];
	
	public static void clear() {
		for (int i = 0; i < table.length; i ++) {
			table[i] = 0;
		}
	}
	
	public static void record(int from, int to) {
		int step = MoveGenerator.makeStep(from, to);
		++ table[step];
	}
	
	public static int getRecord(int step) {
		return table[step];
	}
	
	private HistoryTable() {}

}
