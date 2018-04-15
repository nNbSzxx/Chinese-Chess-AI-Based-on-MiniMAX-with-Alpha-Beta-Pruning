/*
package chess.AI;

import java.util.List;

import chess.board.ChessBoard;
import chess.piece.ChessPieces;
import chess.util.Step;

public class Rook extends ChessPieces {

	public Rook(int iniX, int iniY, boolean iniIsRed, boolean iniIsAlive, String iniName) {
		super(iniX, iniY, iniIsRed, iniIsAlive, iniName);
		// TODO Auto-generated constructor stub
	}
	
	// 传入的坐标已经确保是在棋盘之内的
	@Override
	public boolean isLegalMove(ChessBoard board, int toX, int toY) {
		// TODO Auto-generated method stub
		// 首先检查是否是本方行祺
		if (!isMyTurn(board)) {
			return false;
		}
		// 判断：（1）从当前位置到目的地（不包含目的地）途中是否有其他棋子 
		//	         （2）目的地位置是否有棋子，若有棋子，是否是己方棋子
		if (toX != this.getX() && toY != this.getY()) {
			// 目的地横纵坐标与当前横纵坐标都不相同，必然非法
			return false;
		} else if (toX == this.getX() && toY == this.getY()) {
			// 目的地横纵坐标与当前横纵坐标都相同，相当于没有移动，亦是非法的
			return false;
		} else {
			// 从当前位置到目的地（不包含目的地）途中是否有其他棋子，有则非法 
			int pieceInWay = this.piecesInStraightWay(board, toX, toY);
			if (pieceInWay > 0) {
				return false;
			} else {
				// 目的地位置是否有棋子，若有棋子，是否是己方棋子
				return isDestinationLegal(board, toX, toY);
			}
		}
	}

	@Override
	public List<Step> getNextMove(ChessBoard board) {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public boolean isCheck(ChessBoard board) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public String getImagePath() {
		// TODO Auto-generated method stub
		return null;
	}

}
*/
