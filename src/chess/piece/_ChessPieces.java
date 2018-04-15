/*
package chess.piece;

import java.util.*;
import chess.board.ChessBoard;
import chess.util.Step;

public abstract class _ChessPieces {
	// 棋子的坐标
	private int x;
	private int y;
	// 棋子是否存活
	private boolean isAlive;
	// 棋子是否是红方的
	public final boolean isRed;
	// 棋子的名字
	private String name;
	
	// 请AI和ChessPiece进行协调，确认是否需要该属性
//	public final int value;
	
	public _ChessPieces(int iniX, int iniY, boolean iniIsRed, boolean iniIsAlive, String iniName) {
		// TODO Auto-generated constructor stub
		x = iniX;
		y = iniY;
//		value = iniValue;
		isRed = iniIsRed;
		isAlive = iniIsAlive;
		name = iniName;
	}

	// 首先检查是不是本方行祺， 然后再判断行祺是否合法
	abstract public boolean isLegalMove(ChessBoard board, int toX, int toY);
	// 查询移动该棋子所产生的所有可能走法
	abstract public List<Step> getNextMove(ChessBoard board);
	// 查询当前棋子是否在将对方的军 实现存在难度，故弃用
//	abstract public boolean isCheck(ChessBoard board);
	// 得到该棋子的外观图片路径  请ChessPiece和ChessView进行协调，确定是否需要该方法
	abstract public String getImagePath();

	// 判断两个棋子是否属于同一方
	public static boolean isSameSide(ChessPieces pieces1, ChessPieces pieces2) {
		return pieces1.isRed == pieces2.isRed;
	}
	
	// 判断当前待走棋棋子的阵营是否是当前走棋方
	protected boolean isMyTurn(ChessBoard board) {
		return board.isRedMove() == this.isRed;
	}
	
	// 判断直线移动过程中路径上有多少棋子
	protected int piecesInStraightWay(ChessBoard board, int toX, int toY) {
		
		if (toX == this.x) {
			return pieceInStraightHerizontalWay(board, toX, toY);
		} else {
			// toY == this.y
			return pieceInStraightVerticalWay(board, toX, toY);
		}
	}
	
	// 目的地位置是否有棋子，若有棋子，是否是己方棋子
	protected boolean isDestinationLegal(ChessBoard board, int toX, int toY) {
		// 防止对null调用成员方法，一定要先判断对象是否为null
		if (board.getPiece(toX, toY) == null ||
				(!isSameSide(board.getPiece(toX, toY), this)) ) 
		{
			return true;
		} else {
			return false;
		}
	}
	
	// private 方法
	// 判断水平移动棋子的路径上有多少个棋子
	private int pieceInStraightHerizontalWay(ChessBoard board, int toX, int toY) {
		int cnt = 0;
		if (toY > this.y) {
			for (int i = y + 1; i < toY; i ++) {
				if (board.getPiece(x, i) != null) {
					++ cnt;
				}
			}
		} else {
			// toY < this.y
			for (int i = y - 1; i > toY; i --) {
				if (board.getPiece(x, i) != null) {
					++ cnt;
				}
			}
		}
		return cnt;
	}
	
	// 判断竖直移动棋子的路径上有多少个棋子
	private int pieceInStraightVerticalWay(ChessBoard board, int toX, int toY) {
		int cnt = 0;
		if (toX > this.x) {
			for (int i = x + 1; i < toX; i ++) {
				if (board.getPiece(i, y) != null) {
					++ cnt;
				}
			}
		} else {
			// toX < this.getX
			for (int i = x - 1; i < toX; i --) {
				if (board.getPiece(i, y) != null) {
					++ cnt;
				}
			}
		}
		return cnt;
	}
	// getter & setter
	// 行祺后更新棋子信息
	public void move(int toX, int toY) {
		x = toX;
		y = toY;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
		
	public boolean isAlive() {
		return isAlive;
	}

	public boolean isRed() {
		return isRed;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public String getName() {
		return name;
	}
	
//	public int getValue() {
//	return value;
//}

}
*/
