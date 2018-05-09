package chess.AI;

/**
 * 
 * @author nNbS
 *
 *   Transposition table tries to save the position we have searched before.
 *
 */

class TranspositionRecord {
	public static enum NodeType {ALPHA, BETA, PVS};
	NodeType type;
	private long lock = 0;
	private int value = -Evaluator.WIN_VALUE;
	private int depth = -1;
	private int bestMove = Search.NO_LEGAL_MOVE;
	
	public TranspositionRecord(NodeType type, long lock, int value, int depth, int bestMove) {
		this.type = type;
		this.lock = lock;
		this.value = value;
		this.depth = depth;
		this.bestMove = bestMove;
	}
	
	public TranspositionRecord() {}
	
	public NodeType getType() {
		return type;
	}
	public void setType(NodeType type) {
		this.type = type;
	}
	public long getLock() {
		return lock;
	}
	public void setLock(long lock) {
		this.lock = lock;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getBestMove() {
		return bestMove;
	}
	public void setBestMove(int bestMove) {
		this.bestMove = bestMove;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
}

public final class TranspositionTable {
	public static final int MAX_SIZE = (1 << 20) - 1;
	private static final TranspositionRecord[] records = new TranspositionRecord[MAX_SIZE + 1];
	// 置换表监测变量
	// 置换表已有记录的个数
	private static int usedLocNum = 0;
	// 查询了多少次
	private static int queryNum = 0;
	// 命中了多少次
	private static int hitNum = 0;
	
	static {
		for (int i = 0; i < MAX_SIZE + 1; i ++) {
			records[i] = new TranspositionRecord();
		}
	}
	
	// 记录
	public static void saveRecord(Position position, TranspositionRecord record) {
		int index = (int)(position.getZobrist().getKey()) & MAX_SIZE;
		if (isReplace(records[index], record)) {
			if (records[index].getDepth() == -1) {
				++ usedLocNum;
			}
			records[index] = record;
		}
	}
	
	// 取出记录，如果记录存在则返回记录，否则返回null
	public static TranspositionRecord getRecord(Position position) {
		++ queryNum;
		int index = (int)(position.getZobrist().getKey()) & MAX_SIZE;
		assert (records[index] != null);
		if (position.getZobrist().getLock() == records[index].getLock()) {
			++ hitNum;
			return records[index];
		} else {
			return null;
		}
	}
	
	// 置换表替换策略
	private static boolean isReplace(TranspositionRecord oldRecord, TranspositionRecord newRecord) {
//		return newRecord.getDepth() > oldRecord.getDepth();
		return true;
	}
	
	// 输出置换表的状况
	public static void display() {
		System.out.println("In chess.AI.TranpositionTable.display: Sum of records : " 
								+ usedLocNum + " , hitNum : " + hitNum + ", queryNum : " + queryNum + 
								", hit ratio: " + (1.0 * hitNum / queryNum));
	}
	private TranspositionTable() {}

	
	public static void main(String[] args) {
		
	}

}
