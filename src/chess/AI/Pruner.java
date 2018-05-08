package chess.AI;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author nNbS
 *   
 *   This class is used to help prune
 */

public final class Pruner {
	// 当前处理的局面
	private Position position;
	// 当前搜索深度
	private int depth;
	// 置换表中是否已经有当前局面，而且足够深，以至于可以直接返回
	private boolean isDeeplySearched = false;
	// 置换表是否命中
	private boolean isSearched = false;
	// 置换表中存储的记录
	private TranspositionRecord record;
	// 存储当前棋步生成状态
	public static enum GenState {NO_STEP, DONE_TRANS, DONE_SORT, DONE_HIS};
	private GenState state;
	// 所有棋步
	private List<Integer> steps = null;
	// 所有棋步迭代器
	private Iterator<Integer> iterator = null;
	
	public Pruner(Position position, int depth) {
		this.position = position;
		this.depth = depth;
		state = GenState.NO_STEP;
		record = TranspositionTable.getRecord(position);
		// 置换表命中，则要考虑置换表招法，否则跳过置换表，直接考虑历史表招法
		if (record != null) {
			isSearched = true;
			if (record.getBestMove() == Search.NO_LEGAL_MOVE) {
				state = GenState.DONE_TRANS;
			}
			if (record.getDepth() >= this.depth) {
				isDeeplySearched = true;
				state = GenState.DONE_TRANS;
			}
		} else {
			state = GenState.DONE_TRANS;
		}
	}
	
	/* 试图得到当前最优的招法，这样做：
	 * 1.使得对手的局面尽量差，这样下一个节点的兄弟节点们的beta值就会尽可能的大，利于裁剪
	 * 2.使自己的局面尽量的好，这样更可能触发本节点的beta截断
	 */
	public int getAStep() {
		int move = Search.NO_LEGAL_MOVE;
		switch (state) {
		case NO_STEP:
			move = record.getBestMove();
			state = GenState.DONE_TRANS;
//			System.out.println("In Chess.AI.Pruner.getAMove: move in TransTable!");
			break;
		case DONE_TRANS:
			sortStep();
			state = GenState.DONE_SORT;
			// 此处故意不写break
		case DONE_SORT:
			if (iterator.hasNext()) {
				move = iterator.next();
//				System.out.println("In Chess.AI.Pruner.getAMove : move in Move Generator!");
				break;
			} else {
				state = GenState.DONE_HIS;
				// 此处故意不写break
			}
		case DONE_HIS:
			move = Search.NO_LEGAL_MOVE;
			break;
			
		default:
			assert false;
			break;
		}
		
		return move;
	}
	
	public void saveRecord(TranspositionRecord.NodeType type, int value, int bestMove) {
		TranspositionRecord record = new TranspositionRecord(type, position.getZobrist().getLock(),
											value, depth, bestMove);
		TranspositionTable.saveRecord(position, record);
		HistoryTable.saveRecord(bestMove, depth);
	}
	
	private void sortStep() {
		steps = MoveGenerator.getAllMove(this.position);
		steps.sort(new SortedByHistoryTable());
		iterator = steps.iterator();
	}
	
	// 按照历史表分值降序排列
	private class SortedByHistoryTable implements Comparator<Integer> {
		@Override
		public int compare(Integer o1, Integer o2) {
			return - HistoryTable.getRecord(o1) + HistoryTable.getRecord(o2);
		}
	}
	
	// getter
	public int getStepCount() {
		return steps.size();
	}
	public boolean isDeeplySearched() {
		return isDeeplySearched;
	}
	public boolean isSearched() {
		return isSearched;
	}
	public TranspositionRecord getRecord() {
		return record;
	}
	
}
