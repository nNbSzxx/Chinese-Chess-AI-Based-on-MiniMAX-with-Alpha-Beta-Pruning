package chess.piece;

import java.util.*;

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
		List<Step> list = new ArrayList<Step>();
		this.addNextStep(board, list);
//		System.out.println(this.getName());
//		System.out.println(this.getX() + " " + this.getY());
//		for (Step step : list) {
//			System.out.println(step.getFromX() + " " + step.getFromY() + " " + step.getToX() + " " + step.getToY());
//			if (step.getFromX() == 0 && step.getFromY() == 0 && step.getToX() == 0 && step.getToY() == 0){
//				System.out.println("0000error");
//				System.exit(-1);
//			}
//		}
		return list;
	}

	private void addNextStep(ChessBoard board, List<Step> list) {
		for (int i = this.getX() - 1; i >= 0; i --) {
			if (board.getPiece(i, this.getY()) == null) {
				list.add(new Step(this.getX(), this.getY(), i, this.getY()));
			} else {
				if (!isSameSide(this, board.getPiece(i, this.getY()))) {
					list.add(new Step(this.getX(), this.getY(), i, this.getY()));
				}
				break;
			}
		}
	
		for (int i = this.getX() + 1; i < ChessBoard.MAXROW; i ++) {
			if (board.getPiece(i, this.getY()) == null) {
				list.add(new Step(this.getX(), this.getY(), i, this.getY()));
			} else {
				if (!isSameSide(this, board.getPiece(i, this.getY()))) {
					list.add(new Step(this.getX(), this.getY(), i, this.getY()));
				}
				break;
			}
		}
		for (int i = this.getY() - 1; i >= 0; i --) {
			if (board.getPiece(this.getX(), i) == null) {
				list.add(new Step(this.getX(), this.getY(), this.getX(), i));
			} else {
				if (!isSameSide(this, board.getPiece(this.getX(), i))) {
					list.add(new Step(this.getX(), this.getY(), this.getX(), i));
				}
				break;
			}
		}
		for (int i = this.getY() + 1; i < ChessBoard.MAXCOL; i ++) {
			if (board.getPiece(this.getX(), i) == null) {
				list.add(new Step(this.getX(), this.getY(), this.getX(), i));
			} else {
				if (!isSameSide(this, board.getPiece(this.getX(), i))) {
					list.add(new Step(this.getX(), this.getY(), this.getX(), i));
				}
				break;
			}
		}
	
	}

	@Override
	public String getImagePath() {
		// TODO Auto-generated method stub
		return null;
	}
}
