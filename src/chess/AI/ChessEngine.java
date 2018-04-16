package chess.AI;

import chess.board.ChessBoard;
import chess.util.Step;

public class ChessEngine {
	
	Position position;
	// 初始化函数
	public ChessEngine() {
		position = new Position();
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


/* package chess.AI;

import java.util.Iterator;
import java.util.List;

import chess.board.ChessBoard;
import chess.piece.ChessPieces;
import chess.util.Step;

public class ChessEngine {
	Step best_Step = new Step();
	final int max_depth = 4;   // 最大搜索深度
	final int INF = 18888;    // 自定义最大值
	ChessPieces [][] Curboard = new ChessPieces[10][9]; // 内部棋盘
	ChessBoard board;
	
	
	// 初始化函数
	public ChessEngine() {
			
	}
	
	public ChessEngine(ChessBoard board) {
		this.Curboard = board.getClone();
		this.board = board;
	}
	
	
	// AI走棋函数
	public Step generateAMove(ChessBoard board) {
		this.Curboard = board.getClone();
		this.board = board;

		AlphaBeta(max_depth,-INF,INF,best_Step,board.isRedMove());
		//System.out.println("best= " + best_Step.getFromX() + " " + best_Step.getFromY() + " " + best_Step.getToX() + " " + best_Step.getToY());
		return best_Step;
	}
	
	// 模拟走棋函数
	void MakeMove(Step t) {
		ChessPieces to = Curboard[t.getFromX()][t.getFromY()];
		Curboard[t.getToX()][t.getToY()] = to;
		Curboard[t.getFromX()][t.getFromY()] = null;
	}
	
	void UnMakeMove(ChessPieces p , Step t) {
		Curboard[t.getFromX()][t.getFromY()] = Curboard[t.getToX()][t.getToY()];
		Curboard[t.getToX()][t.getToY()] = p;
	}
	
	
	// 搜索算法
	int AlphaBeta(int depth, int alpha, int beta,Step best,boolean flag) {
		ChessBoard newone = new ChessBoard(Curboard,flag);
		if(depth == 0) {
			return newone.getValue();
		}
		List<Step> t;
		t = newone.generateAllNextMove();
		Iterator<Step> it = t.iterator();
		while(it.hasNext()) {
			Step now = it.next();
			ChessPieces now_p = Curboard[now.getToX()][now.getToY()];
			MakeMove(now);
			flag = !flag;
			int val = -AlphaBeta(depth-1,-beta,-alpha,best,flag);
			UnMakeMove(now_p,now);
			flag = !flag;
			if(val >= beta) return beta;
			if(val > alpha) {
				alpha = val;
				if(depth == max_depth) {
					//System.out.println("0000error2222");
					//System.out.println(now.getFromX() + " " + now.getFromY() + " " + now.getToX() + " " + now.getToY());
					best_Step = now;
				}
			}
		}
		return alpha;
	}

}
*/
