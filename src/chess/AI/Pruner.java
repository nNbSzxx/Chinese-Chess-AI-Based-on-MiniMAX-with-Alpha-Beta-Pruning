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
	private Position position;
	private List<Integer> steps = null;
	private Iterator<Integer> iterator = null;
	
	public Pruner(Position position) {
		this.position = position;
		steps = MoveGenerator.getAllMove(this.position);
		sortStep();
		iterator = steps.iterator();
	}
	public void sortStep() {
		steps.sort(new SortedByHistoryTable());
	}
	
	/* 试图得到当前最优的招法，这样做：
	 * 1.使得对手的局面尽量差，这样下一个节点的兄弟节点们的beta值就会尽可能的大，利于裁剪
	 * 2.使自己的局面尽量的好，这样更可能触发本节点的beta截断
	 */
	public int getAStep() {
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			return Search.NO_LEGAL_MOVE;
		}
	}
	
	// 按照历史表分值降序排列
	private class SortedByHistoryTable implements Comparator<Integer> {
		@Override
		public int compare(Integer o1, Integer o2) {
			return - HistoryTable.getRecord(o1) + HistoryTable.getRecord(o2);
		}
	}
	
	public int getStepCount() {
		return steps.size();
	}
	
}
