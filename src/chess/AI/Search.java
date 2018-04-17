package chess.AI;

public final class Search {
	// 无合法棋步时返回的编号
	public static final int NO_LEGAL_MOVE = 0;
	// 依层数搜索时，朴素alpha-beta搜索的最大深度
	public static final int NAIVE_ALPHABETA_DEPTH = 5;
	// 按照时间长短搜索时，最长思考时间
	public static final int THINKING_TIME = 3;
	// 记录找出一步祺一共搜索了多少节点
	private static long count = 0;
	// 记录找出一步祺一共用了多长时间
	private static long timeCost = 0;
	
	// 迭代加深
	public static int mainSearch(Position position) {
		count = 0;
		// 发现清空历史表比全部保留或者衰减原有历史表的值效果都要好
		HistoryTable.clear();
//		HistoryTable.shrink();
		long beginTime = System.currentTimeMillis();
		int bestStep = NO_LEGAL_MOVE;
		int dep;
		for (dep = 1; ; ++ dep) {
			bestStep = searchRoot(dep, -Evaluator.WIN_VALUE, Evaluator.WIN_VALUE, position);
			timeCost = System.currentTimeMillis() - beginTime;
			if (timeCost >= THINKING_TIME * 1000) {
				break;
			}
		}
		System.out.print("In chess.AI.Search.mainSearch: deepest depth " + dep + " Total nodes searched: " + count);
		System.out.println(" Time: " + (1.0 * timeCost / 1000.0));
		return bestStep;
	}
	
	// 返回的是最好的棋步
	private static int searchRoot(int depth, int alpha, int beta, Position position) {
		// 判断到棋局终局，而又轮到己方行棋，说明已输
		++ count;
		if (position.isEnd()) {
			return NO_LEGAL_MOVE;
		}
		int bestVal = -Evaluator.WIN_VALUE;
		int bestMove = NO_LEGAL_MOVE;
		StepDealer dealer = new StepDealer(position);
//		System.out.println("In chess.AI.Search.searchRoot: steps " + dealer.getStepCount());
		for (int step = dealer.getAStep(); step != NO_LEGAL_MOVE; step = dealer.getAStep()) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			// 对脸则跳过这一步
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position, 1);
			position.undoMove();
			// beta是胜利状态的价值，因此不会发生beta截断
			if (val > bestVal) {
				bestVal = val;
				bestMove = step;
				if (val > alpha) {
					alpha = val;
				}
			}
		}
		// 将棋步记入历史表
		if (bestVal >= alpha) {
			HistoryTable.record(bestMove, depth);
		}
		return bestMove;
	}
	
	// unfinished
	@SuppressWarnings("unused")
	private static int PVS(int depth, int alpha, int beta) {
		return 0;
	}
	
	// 超出上下界的alpha-beta搜索，curDepth表示当前距离根节点的距离
	private static int naiveAlphaBeta(int depth, int alpha, int beta, Position position, int curDepth) {
		++ count;
		// 判断到棋局终局，而又轮到己方行棋，说明已输
		// 为了能搜索到最少步数杀棋棋步，避免长将，要在评估上体现出从根节点到此节点一共走了多少步
		if (position.isEnd()) {
			return -Evaluator.WIN_VALUE + curDepth;
		}
		if (depth == 0) {
			return position.evaluate();
		}
		int bestVal = -Evaluator.WIN_VALUE;
		int bestMove = NO_LEGAL_MOVE;
		StepDealer dealer = new StepDealer(position);
		for (int step = dealer.getAStep(); step != NO_LEGAL_MOVE; step = dealer.getAStep()) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position, curDepth + 1);
			position.undoMove();
			// 发生beta截断时，将该棋步记入历史表
			if (val >= beta) {
				HistoryTable.record(step, depth);
				return val;
			}
			if (val > bestVal) {
				bestVal = val;
				bestMove = step;
				if (val > alpha) {
					alpha = val;
				}
			}
		}
		// 将最好棋步记入历史表
		if (bestVal >= alpha) {
			HistoryTable.record(bestMove, depth);
		}
		return bestVal;
	}
	
	private Search() {}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		assert false;
	}

}
