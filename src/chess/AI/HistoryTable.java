package chess.AI;

/**
 * 
 * @author nNbS
 * 
 *   This class records all steps that cause beta pruning or is a PV.
 */

public final class HistoryTable {
	public static final int STEP_UPPER_BOUND = 256 * 256;
	private static int[] table = new int[STEP_UPPER_BOUND];
	
	public static void clear() {
		for (int i = 0; i < table.length; i ++) {
			table[i] = 0;
		}
	}
	
	public static void shrink() {
		for (int i = 0; i < table.length; i ++) {
			table[i] >>= 2;
		}
	}
	
	public static void record(int step, int dep) {
		table[step] += dep * dep;
	}
	
	public static int getRecord(int step) {
		return table[step];
	}
	
	private HistoryTable() {}

}
