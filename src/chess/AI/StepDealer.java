package chess.AI;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author nNbS
 *   
 *   This class is used to sort steps
 */

public final class StepDealer {
	private Position position;
	private List<Integer> steps;
	private Iterator<Integer> iterator;
	
	public StepDealer(Position position) {
		this.position = position;
		steps = MoveGenerator.getAllMove(this.position);
		sortStep();
		iterator = steps.iterator();
	}
	public void sortStep() {
		steps.sort(new SortedByHistoryTable());
	}
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
