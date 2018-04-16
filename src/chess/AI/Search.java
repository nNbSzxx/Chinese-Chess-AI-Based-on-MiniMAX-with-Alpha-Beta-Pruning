package chess.AI;

import java.util.List;

public final class Search {
	// 无合法棋步时返回的编号
	public static final int NO_LEGAL_MOVE = 0;
	// 依层数搜索时，朴素alpha-beta搜索的最大深度
	public static final int NAIVE_ALPHABETA_DEPTH = 5;
	// 按照时间长短搜索时，最长思考时间
	public static final int THINKING_TIME = 3;
	// 记录一共搜索了多少节点
	private static long count = 0;
	private static long timeCost = 0;
	
	// 迭代加深
	public static int mainSearch(Position position) {
		count = 0;
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
		int bestMove = NO_LEGAL_MOVE;
		List<Integer> list = MoveGenerator.getCapMove(position);
		for (int step : list) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position, 1);
			position.undoMove();
			if (val > alpha) {
				alpha = val;
				bestMove = step;
			}
		}
		list = MoveGenerator.getNonCapMove(position);
		for (int step : list) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position, 1);
			position.undoMove();
			if (val > alpha) {
				alpha = val;
				bestMove = step;
			}
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
		// 为了能搜索到最少步数杀棋棋步，避免长将
		if (position.isEnd()) {
			return -Evaluator.WIN_VALUE + curDepth;
		}
		if (depth == 0) {
			return position.evaluate();
		}
		int bestVal = -Evaluator.WIN_VALUE;
		List<Integer> list = MoveGenerator.getCapMove(position);
		for (int step : list) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position, curDepth + 1);
			position.undoMove();
			if (val >= beta) {
				return val;
			}
			bestVal = (bestVal < val)? val : bestVal;
			if (val > alpha) {
				alpha = val;
			}
		}
		list = MoveGenerator.getNonCapMove(position);
		for (int step : list) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			if (position.doKingFaceKing()) {
				position.undoMove();
				continue;
			}
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position, curDepth + 1);
			position.undoMove();
			if (val >= beta) {
				return val;
			}
			bestVal = (bestVal < val)? val : bestVal;
			if (val > alpha) {
				alpha = val;
			}
		}
		return bestVal;
	}
	
	private Search() {}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		assert false;
	}

}
