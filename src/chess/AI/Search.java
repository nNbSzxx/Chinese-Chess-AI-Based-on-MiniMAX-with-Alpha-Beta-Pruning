package chess.AI;

public final class Search {
	// 无合法棋步时返回的编号
	public static final int NO_LEGAL_MOVE = 0;
	// 依层数搜索时，朴素alpha-beta搜索的最大深度
	public static final int NAIVE_ALPHABETA_DEPTH = 5;
	// 按照时间长短搜索时，最长思考时间，单位毫秒
	public static int thinkingTime = 1500;
	// 空着裁剪步数
	public static final int NULL_MOVE_SKIP = 2;
	// 允许空着裁剪的最小局面价值，因为残局下执行空着可能会比走棋更好
	public static final int NULL_MOVE_MARGIN = 300;
	// 记录找出一步祺一共搜索了多少节点
	private static long count = 0;
	// 记录找出一步祺一共用了多长时间
	private static long timeCost = 0;
	
	public static void setThinkingTime(int mils) {
		thinkingTime = mils;
	}
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
			if (timeCost >= thinkingTime) {
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
		// 根节点禁止空着裁剪
		Pruner pruner = new Pruner(position, depth, 0);
		// 尝试置换表裁剪，检查置换表是否存在满足当前深度的记录，若有直接返回
		if (pruner.isDeeplySearched()) {
			if ((pruner.getValue() >= Evaluator.WIN_VALUE - 200 && pruner.getValue() <= Evaluator.WIN_VALUE - 100) || 
					(pruner.getValue() >= -Evaluator.WIN_VALUE + 100 && pruner.getValue() <= -Evaluator.WIN_VALUE + 200)) {
				System.out.println("In searchRoot: trans cut, value: " + pruner.getValue());
			}
			bestMove = pruner.getBestMove();
			return bestMove;
		}
//		System.out.println("In chess.AI.Search.searchRoot: steps " + dealer.getStepCount());
		for (int step = pruner.getAStep(); step != NO_LEGAL_MOVE; step = pruner.getAStep()) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			// 对脸则跳过这一步
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			int val = -failSoftAlphaBeta(depth - 1, -beta, -alpha, position, 1);
			position.undoMove();
			// 发生beta截断时，记录该局面
			if (val >= beta) {
//				HistoryTable.record(step, depth);
				pruner.saveRecord(TranspositionRecord.NodeType.BETA, val, step);
				return step;
			}
			if (val > bestVal) {
				bestVal = val;
				bestMove = step;
				if (val > alpha) {
					alpha = val;
				}
			}
		}
		// 记录当前局面
		if (bestVal >= alpha) {
//			HistoryTable.record(bestMove, depth);
			pruner.saveRecord(TranspositionRecord.NodeType.PVS, bestVal, bestMove);
		} else {
			pruner.saveRecord(TranspositionRecord.NodeType.ALPHA, bestVal, bestMove);
		}
		return bestMove;
	}
	
	// unfinished
	@SuppressWarnings("unused")
	private static int PVS(int depth, int alpha, int beta) {
		return 0;
	}
	
	// 超出上下界的alpha-beta搜索，curDepth表示当前距离根节点的距离
	private static int failSoftAlphaBeta(int depth, int alpha, int beta, Position position, int curDepth) {
		++ count;
		// 判断到棋局终局，而又轮到己方行棋，说明已输
		// 为了能搜索到最少步数杀棋棋步，避免长将，要在评估上体现出从根节点到此节点一共走了多少步
		if (position.isEnd()) {
			return -Evaluator.WIN_VALUE + curDepth;
		}
		Pruner pruner = new Pruner(position, depth, curDepth);
		// 尝试置换表裁剪，检查置换表是否存在满足当前深度的记录，若有直接返回
		if (pruner.isDeeplySearched()) {
			if ((pruner.getValue() >= Evaluator.WIN_VALUE - 200 && pruner.getValue() <= Evaluator.WIN_VALUE - 100) || 
					(pruner.getValue() >= -Evaluator.WIN_VALUE + 100 && pruner.getValue() <= -Evaluator.WIN_VALUE + 200)) {
				System.out.println("In failSoftAlphaBeta: trans cut, value: " + pruner.getValue());
			}
			return pruner.getValue();
		}
		if (depth == 0) {
			return position.evaluate();
		}
		int bestVal = -Evaluator.WIN_VALUE;
		int bestMove = NO_LEGAL_MOVE;
		// 尝试空着裁剪
		if (beta != Evaluator.WIN_VALUE && doAllowNullMove(position)) {
			position.makeNullMove();
			// 执行一个空着实际上只把当前局面距离根节点的深度加了1
			int val = -nullMovePruning(depth - 1 - NULL_MOVE_SKIP, -beta, -beta + 1, position, curDepth + 1);
			position.undoNullMove();
			if (val >= beta) {
				// 裁剪成功
				pruner.saveRecord(TranspositionRecord.NodeType.BETA, val, 0);
				return val;
			}
		}
		// 尝试招法
		for (int step = pruner.getAStep(); step != NO_LEGAL_MOVE; step = pruner.getAStep()) {
			assert (step != NO_LEGAL_MOVE);
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			int val = -failSoftAlphaBeta(depth - 1, -beta, -alpha, position, curDepth + 1);
			position.undoMove();
			// 发生beta截断时，记录该局面
			if (val >= beta) {
//				HistoryTable.record(step, depth);
				pruner.saveRecord(TranspositionRecord.NodeType.BETA, val, step);
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
		// 记录当前局面
		if (bestVal >= alpha) {
//			HistoryTable.record(bestMove, depth);
			pruner.saveRecord(TranspositionRecord.NodeType.PVS, bestVal, bestMove);
		} else {
			pruner.saveRecord(TranspositionRecord.NodeType.ALPHA, bestVal, bestMove);
		}
		return bestVal;
	}

	// 空着裁剪
	private static int nullMovePruning(int depth, int alpha, int beta, Position position, int curDepth) {
		++ count;
		if (position.isEnd()) {
			return curDepth - Evaluator.WIN_VALUE;
		}
		Pruner pruner = new Pruner(position, depth, curDepth);
		// 尝试置换表裁剪，检查置换表是否存在满足当前深度的记录，若有直接返回
		if (pruner.isDeeplySearched()) {
			if ((pruner.getValue() >= Evaluator.WIN_VALUE - 200 && pruner.getValue() <= Evaluator.WIN_VALUE - 100) || 
					(pruner.getValue() >= -Evaluator.WIN_VALUE + 100 && pruner.getValue() <= -Evaluator.WIN_VALUE + 200)) {
				System.out.println("In nullMovePruning: trans cut, value: " + pruner.getValue());
			}
			return pruner.getValue();
		}
		// 注意！空着裁剪可能使得深度小于0
		if (depth <= 0) {
			return position.evaluate();
		}
		int bestVal = -Evaluator.WIN_VALUE;
		int bestMove = NO_LEGAL_MOVE;
		
		// 禁止连续空着裁剪
		for (int step = pruner.getAStep(); step != NO_LEGAL_MOVE; step = pruner.getAStep()) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			// 这里我们禁止连续的空着裁剪，因此空着裁剪后是alpha-beta搜索
			int val = -failSoftAlphaBeta(depth - 1, -beta, -alpha, position, curDepth + 1);
			position.undoMove();
			// 发生beta截断时，记录该局面
			if (val >= beta) {
//				HistoryTable.record(step, depth);
				pruner.saveRecord(TranspositionRecord.NodeType.BETA, val, step);
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
		// 记录当前局面
		if (bestVal >= alpha) {
//			HistoryTable.record(bestMove, depth);
			pruner.saveRecord(TranspositionRecord.NodeType.PVS, bestVal, bestMove);
		} else {
			pruner.saveRecord(TranspositionRecord.NodeType.ALPHA, bestVal, bestMove);
		}
		return bestVal;
	}
	
	private static boolean doAllowNullMove(Position position) {
		return (position.isRedMove()? position.getRedVal() : position.getBlackVal())
					>= NULL_MOVE_MARGIN;
	}
	private Search() {}

	public static void main(String[] args) {
//		assert false;
	}

}
