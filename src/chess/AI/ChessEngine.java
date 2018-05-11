package chess.AI;

import chess.board.ChessBoard;
import chess.util.Step;

public class ChessEngine {
	
	// 难度依次递增
	public static enum Level {ONE, TWO, THREE};
	Position position;
	// 初始化函数
	public ChessEngine() {
		position = new Position();
		Search.setThinkingTime(1000);
	}
	
	public void setLevel(Level level) {
		switch (level) {
		case ONE:
			Search.setThinkingTime(1000);
			break;
		case TWO:
			Search.setThinkingTime(2000);
			break;
		case THREE:
			Search.setThinkingTime(3000);
			break;
		default:
			break;
		}
	}
	
	// AI走棋函数
	public Step generateAMove() {
//		System.out.print("In chess.AI.ChessEngine.generateAMove: ");
//		System.out.println("Now is " + (position.isRedMove()? "Red Turn" : "Black Turn"));
		int bestStep = Search.mainSearch(position);
//		System.out.print("The best step id: " + bestStep + "   ");
		int from = MoveGenerator.getFromLoc(bestStep);
//		System.out.print("Pick: " + position.getPiece(from) + "   ");
		int to = MoveGenerator.getToLoc(bestStep);
//		System.out.print("ToLocPiece: " + position.getPiece(to) + "   ");
		assert (Board.inBoard(from));
		assert (Board.inBoard(to));
		Step step = transformStep(from, to);
//		TranspositionTable.display();
//		System.out.println((Board.getRank(from) - Board.RANK_TOP) + " " + (Board.getFile(from) - Board.FILE_LEFT)
//				   + " " + (Board.getRank(to) - Board.RANK_TOP) + " " + (Board.getFile(to)- Board.FILE_LEFT));
//		System.out.println("fromX: " + step.getFromX() + " fromY: " + step.getFromY() + " toX: "
//							+ step.getToX() + " toY: " + step.getToY());
		return step;
	}
	
	public void applyAMove(Step step) {
		int fromX = step.getFromX();
		int fromY = step.getFromY();
		int toX = step.getToX();
		int toY = step.getToY();
		int from = (((fromX + Board.RANK_TOP) << 4) | (fromY + Board.FILE_LEFT));
		int to = (((toX + Board.RANK_TOP) << 4) | (toY + Board.FILE_LEFT));
		position.move(from, to);
	}
	
	public void regret() {
		position.undoMove();
	}
	
	private static Step transformStep(int from, int to) {
		int fromX = Board.getRank(from) - Board.RANK_TOP;
		int fromY = Board.getFile(from) - Board.FILE_LEFT;
		int toX = Board.getRank(to) - Board.RANK_TOP;
		int toY = Board.getFile(to) - Board.FILE_LEFT;
		assert (fromX >= 0 && fromX < ChessBoard.MAXROW);
		assert (fromY >= 0 && fromY < ChessBoard.MAXCOL);
		assert (toX >= 0 && toX < ChessBoard.MAXROW);
		assert (toY >= 0 && toY < ChessBoard.MAXCOL);
		return new Step(fromX, fromY, toX, toY);
	}
	
	public static void main(String[] args) {
//		assert false;
	}

}