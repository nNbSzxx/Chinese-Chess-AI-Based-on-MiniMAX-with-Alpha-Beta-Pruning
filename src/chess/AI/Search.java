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
	public static final int NULL_MOVE_MARGIN = 400;
	
	// 以下是监测变量
	// 记录找出一步祺一共搜索了多少节点
	private static long count = 0;
	// 记录找出一步祺一共用了多长时间
	private static long timeCost = 0;
	// 记录尝试空着裁剪总次数
	private static long nullCutTries = 0;
	// 记录尝试空着裁剪成功总次数
	private static long nullCutSuccess = 0;
	// 记录尝试PV裁剪总次数
	private static long PVCutTries = 0;
	// 记录尝试PV裁剪失败总次数 
	private static long PVCutfailure = 0;
	// 根节点最佳棋步所对应价值
	private static long rootBestValue = 0;
	
	public static void setThinkingTime(int mils) {
		thinkingTime = mils;
	}
	
	// 迭代加深
	public static int mainSearch(Position position) {
		// 初始化监测变量
		initWatchVar();
		TranspositionTable.initWatchVar();
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
		display(dep);
		TranspositionTable.display();
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
		Pruner pruner = new Pruner(position, depth, 0, alpha, beta);
		// 尝试置换表裁剪，检查置换表是否存在满足当前深度的记录，若有直接返回
		if (pruner.isDeeplySearched()) {
			assert (pruner.getRecordDepth() > 0);
			bestMove = pruner.getBestMove();
			rootBestValue = pruner.getValue();
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
			// 在根节点尝试主要变例搜索
			// 如果我们已经找到了PV节点，假设我们的招法排序足够好，那么我们只需要证明剩下的招法不如PV招法
			// 如果证明并非如此，则要重新搜索全部节点
			int val;
			// 当前还没找到PV节点，做正常的alpha-beta搜索
			if (bestVal <= alpha) {
				val = -PVSearch(depth - 1, -beta, -alpha, position, 1);
			}
			// 否则，做检查，当前招法是否比PV招法好
			else {
				// 检查是否存在比alpha好的招法（即使得对方局面更坏的招法），此时bestVal=alpha
				// 只关心是不是存在好的，不在乎好多少，由此可以节省很多时间
				// 就是在检测其他节点是否能产生beta截断
				++ PVCutTries;
				val = -limitWindowSearch(depth - 1, -alpha, position, 1, true);
				// 检查到当前招法比PV招法更好，需要重新完全搜索该节点
				if (val > alpha) {
					++ PVCutfailure;
					val = -PVSearch(depth - 1, -beta, -alpha, position, 1);
				}
			}
			position.undoMove();
			// 发生beta截断时，记录该局面
			if (val >= beta) {
				pruner.saveRecord(TranspositionRecord.NodeType.BETA, val, step);
				rootBestValue = val;
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
			pruner.saveRecord(TranspositionRecord.NodeType.PVS, bestVal, bestMove);
		} else {
			pruner.saveRecord(TranspositionRecord.NodeType.ALPHA, bestVal, bestMove);
		}
		rootBestValue = bestVal;
		return bestMove;
	}

	private static int PVSearch(int depth, int alpha, int beta, Position position, int curDepth) {
		++ count;
		// 判断到棋局终局，而又轮到己方行棋，说明已输
		// 为了能搜索到最少步数杀棋棋步，避免长将，要在评估上体现出从根节点到此节点一共走了多少步
		if (position.isEnd()) {
			return -Evaluator.WIN_VALUE + curDepth;
		}
		Pruner pruner = new Pruner(position, depth, curDepth, alpha, beta);
		// 尝试置换表裁剪，检查置换表是否存在满足当前深度的记录，若有直接返回
		if (pruner.isDeeplySearched()) {
			assert (pruner.getRecordDepth() > 0);
			return pruner.getValue();
		}

		if (depth == 0) {
			return position.evaluate();
		}
		int bestVal = -Evaluator.WIN_VALUE;
		int bestMove = NO_LEGAL_MOVE;
		// 尝试空着裁剪
		if (beta != Evaluator.WIN_VALUE && doAllowNullMove(position)) {
			++ nullCutTries;
			position.makeNullMove();
			// 执行一个空着实际上只把当前局面距离根节点的深度加了1
			// 核心思想是如果当前我不下棋依然能得到beta截断，那么说明这个局面非常好
			// 不过此时没有最佳招法
			int val = -limitWindowSearch(depth - 1 - NULL_MOVE_SKIP, -beta + 1, position, curDepth + 1, false);
			position.undoNullMove();
			if (val >= beta) {
				// 裁剪成功
				++ nullCutSuccess;
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
			// 主要变例搜索
			// 如果我们已经找到了PV节点，假设我们的招法排序足够好，那么我们只需要证明剩下的招法不如PV招法
			// 如果证明并非如此，则要重新搜索全部节点
			int val;
			// 当前还没找到PV节点，做正常的alpha-beta搜索
			if (bestVal <= alpha) {
				val = -PVSearch(depth - 1, -beta, -alpha, position, curDepth + 1);
			}
			// 否则，做检查，当前招法是否比PV招法好
			else {
				// 检查是否存在比alpha好的招法（即使得对方局面更坏的招法），此时bestVal=alpha
				// 只关心是不是存在好的，不在乎好多少，由此可以节省很多时间
				// 就是在检测其他节点是否能产生beta截断
				++ PVCutTries;
				val = -limitWindowSearch(depth - 1, -alpha, position, curDepth + 1, true);
				// 检查到当前招法比PV招法更好，需要重新完全搜索该节点
				if (val > alpha) {
					val = -PVSearch(depth - 1, -beta, -alpha, position, curDepth + 1);
					++ PVCutfailure;
				}
			}
			position.undoMove();
			// 发生beta截断时，记录该局面
			if (val >= beta) {
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
			pruner.saveRecord(TranspositionRecord.NodeType.PVS, bestVal, bestMove);
		} else {
			pruner.saveRecord(TranspositionRecord.NodeType.ALPHA, bestVal, bestMove);
		}
		return bestVal;
	}
	
	/*
	 *  零窗口搜索，用于空着裁剪，主要变例搜索，用于检验某种招法是否存在，该搜索的alpha值即为beta-1
	 *  
	 *  对于空着裁剪，此处的beta为原搜索的-beta+1，此处alpha为原搜索的-beta，
	 *  用来检测是否即使让对手一招，也会发生beta裁剪
	 *  我们只关心是否存在不足以达到beta裁剪的局面，不关心局面的具体价值，因此只要找到高出-beta+1的局面就可以截断返回了
	 *  
	 *  对于主要变例搜索，此处的beta为原搜索的-alpha，此处alpha为原搜索的-alpha-1，
	 *  用来检测是否存在局面比当前PV招法导致的局面更好（我们希望是没有的）
	 *  我们只关心是否存在比当前更好的局面，不关心局面的具体价值，因此只要找出高于-alpha的局面就可以截断返回了
	 */
	public static int limitWindowSearch(int depth, int beta, Position position, int curDepth, boolean allowNullCut) {
		++ count;
		// 判断到棋局终局，而又轮到己方行棋，说明已输
		// 为了能搜索到最少步数杀棋棋步，避免长将，要在评估上体现出从根节点到此节点一共走了多少步
		if (position.isEnd()) {
			return -Evaluator.WIN_VALUE + curDepth;
		}
		Pruner pruner = new Pruner(position, depth, curDepth, beta - 1, beta);
		// 尝试置换表裁剪，检查置换表是否存在满足当前深度的记录，若有直接返回
		if (pruner.isDeeplySearched()) {
			assert (pruner.getRecordDepth() > 0);
			return pruner.getValue();
		}
		if (depth <= 0) {
			return position.evaluate();
		}
		int bestVal = -Evaluator.WIN_VALUE;
		int bestMove = NO_LEGAL_MOVE;
		// 尝试空着裁剪
		if (allowNullCut && beta != Evaluator.WIN_VALUE && doAllowNullMove(position) && !position.isChecked()) {
			position.makeNullMove();
			++ nullCutTries;
			// 执行一个空着实际上只把当前局面距离根节点的深度加了1
			// 禁止连续的空着裁剪，否则搜索将退化成直接使用静态评估
			int val = -limitWindowSearch(depth - 1 - NULL_MOVE_SKIP, -beta + 1, position, curDepth + 1, false);
			position.undoNullMove();
			if (val >= beta) {
				// 裁剪成功
				++ nullCutSuccess;
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
			int val = -limitWindowSearch(depth - 1, -beta + 1, position, curDepth + 1, true);
			position.undoMove();
			// 发生beta截断时，记录该局面
			if (val >= beta) {
				pruner.saveRecord(TranspositionRecord.NodeType.BETA, val, step);
				return val;
			}
			if (val > bestVal) {
				bestVal = val;
				bestMove = step;
			}
		}
		// 记录当前局面
		pruner.saveRecord(TranspositionRecord.NodeType.ALPHA, bestVal, bestMove);
		return bestVal;
	}
	
	// 超出上下界的alpha-beta搜索，curDepth表示当前距离根节点的距离
	@Deprecated
	private static int failSoftAlphaBeta(int depth, int alpha, int beta, Position position, int curDepth) {
		++ count;
		// 判断到棋局终局，而又轮到己方行棋，说明已输
		// 为了能搜索到最少步数杀棋棋步，避免长将，要在评估上体现出从根节点到此节点一共走了多少步
		if (position.isEnd()) {
			return -Evaluator.WIN_VALUE + curDepth;
		}
		Pruner pruner = new Pruner(position, depth, curDepth, alpha, beta);
		// 尝试置换表裁剪，检查置换表是否存在满足当前深度的记录，若有直接返回
		if (pruner.isDeeplySearched()) {
			assert (pruner.getRecordDepth() > 0);
//			if ((pruner.getValue() >= Evaluator.WIN_VALUE - 200 && pruner.getValue() <= Evaluator.WIN_VALUE - 100) || 
//					(pruner.getValue() >= -Evaluator.WIN_VALUE + 100 && pruner.getValue() <= -Evaluator.WIN_VALUE + 200)) {
//				System.out.println("In failSoftAlphaBeta: trans cut, value: " + pruner.getValue());
//			}
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
	@Deprecated
	private static int nullMovePruning(int depth, int alpha, int beta, Position position, int curDepth) {
		++ count;
		if (position.isEnd()) {
			return curDepth - Evaluator.WIN_VALUE;
		}
		Pruner pruner = new Pruner(position, depth, curDepth, alpha, beta);
		// 尝试置换表裁剪，检查置换表是否存在满足当前深度的记录，若有直接返回
		if (pruner.isDeeplySearched()) {
			assert (pruner.getRecordDepth() > 0);
//			if ((pruner.getValue() >= Evaluator.WIN_VALUE - 200 && pruner.getValue() <= Evaluator.WIN_VALUE - 100) || 
//					(pruner.getValue() >= -Evaluator.WIN_VALUE + 100 && pruner.getValue() <= -Evaluator.WIN_VALUE + 200)) {
//				System.out.println("In nullMovePruning: trans cut, value: " + pruner.getValue());
//			}
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
	
	public static void initWatchVar() {
		count = 0;
		nullCutTries = 0;
		nullCutSuccess = 0;
		PVCutTries = 0;
		PVCutfailure = 0;
	}
	
	private static void display(int dep) {
		System.out.println("In chess.AI.Search.mainSearch: ");
		System.out.println("  deepest depth " + dep);
		System.out.println("  Total nodes searched: " + count);
		System.out.println("  Time: " + (1.0 * timeCost / 1000.0));
		System.out.println("  PV cut tries: " + PVCutTries);
		System.out.println("  PV cut succeeds: " + (PVCutTries - PVCutfailure));
		System.out.println("  PV cut success ratio: " + (1.0 * (PVCutTries - PVCutfailure) / PVCutTries));
		System.out.println("  Null cut tries: " + nullCutTries);
		System.out.println("  Null cut succeeds: " + nullCutSuccess);
		System.out.println("  Null cut seccess ratio: " + (1.0 * nullCutSuccess / nullCutTries));
		System.out.println("  Best move value: " + rootBestValue);
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
