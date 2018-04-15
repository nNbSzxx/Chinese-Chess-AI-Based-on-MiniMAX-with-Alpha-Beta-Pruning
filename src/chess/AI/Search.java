package chess.AI;

import java.util.List;

public final class Search {

	public static final int NO_LEGAL_MOVE = 0;
	public static final int NAIVE_ALPHABETA_DEPTH = 5;
	// unfinished
	public static int mainSearch(Position position) {
		return searchRoot(NAIVE_ALPHABETA_DEPTH, -Evaluator.WIN_VALUE, 
				Evaluator.WIN_VALUE, position);
	}
	
	// 返回的是最好的棋步
	private static int searchRoot(int depth, int alpha, int beta, Position position) {
		// 判断到棋局终局，而又轮到己方行棋，说明已输
		if (position.isEnd()) {
			return -Evaluator.WIN_VALUE;
		}
		int bestMove = NO_LEGAL_MOVE;
		List<Integer> list = MoveGenerator.getCapMove(position);
		for (int step : list) {
			int from = MoveGenerator.getFromLoc(step);
			int to = MoveGenerator.getToLoc(step);
			position.move(from, to);
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position);
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
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position);
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
	
	// 超出上下界的alpha-beta搜索
	private static int naiveAlphaBeta(int depth, int alpha, int beta, Position position) {
		// 判断到棋局终局，而又轮到己方行棋，说明已输
		if (position.isEnd()) {
			return -Evaluator.WIN_VALUE + (NAIVE_ALPHABETA_DEPTH - depth);
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
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position);
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
			int val = -naiveAlphaBeta(depth - 1, -beta, -alpha, position);
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
