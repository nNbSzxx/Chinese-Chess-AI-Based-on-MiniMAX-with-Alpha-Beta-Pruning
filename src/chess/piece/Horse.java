package chess.piece;

import java.util.ArrayList;
import java.util.List;



import chess.board.ChessBoard;
import chess.util.Step;

public class Horse extends ChessPieces{

	public Horse(int iniX, int iniY, boolean iniIsRed, boolean iniIsAlive, String iniName) {
		super(iniX, iniY, iniIsRed, iniIsAlive, iniName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isLegalMove(ChessBoard board, int toX, int toY) {
		// TODO Auto-generated method stub
		if(!isMyTurn(board))
		{
			return false;
		}else if (this.getX() == toX &&this.getY() == toY) {
			return false;
		}else if (!isDestinationLegal(board, toX, toY)) {
			return false;
		}else {
			if (!((Math.abs(toX - this.getX()) == 2 && Math.abs(toY - this.getY()) == 1)||
				  (Math.abs(toX - this.getX()) == 1 && Math.abs(toY - this.getY()) == 2))) {
				return false;
			}
			if (Math.abs(toX - this.getX()) == 2) {
				int hx;
				int hy;
				hx = (toX + this.getX()) / 2;
				hy = this.getY();
				if(board.getPiece(hx, hy)!= null)
					return false;
			}else if(Math.abs(toY - this.getY()) == 2) 
			{
				int hx;
				int hy;
				hx = this.getX();
				hy = (this.getY() + toY) / 2;
				if(board.getPiece(hx, hy)!= null)
					return false;
			}
		}
		return true;
	}

	@Override
	public List<Step> getNextMove(ChessBoard board) {
		// TODO Auto-generated method stub
		List<Step> list = new ArrayList<Step>();
		//同一个马有四个地方是蹩马腿的地方，每一个地方要是没有棋子的话可以产生两种走法，
		//只需检查位置是否过界以及欲走位置是否有己方的棋子
		//对上方蹩马腿的位置进行检查
//		if(isPointLegal(this.getX() - 1, this.getY()) && board.getPiece(this.getX() - 1, this.getY()) == null) 
//		{
//			if(this.getX() - 2 >= 0 && this.getY() + 1 <= 8 && 
//					isDestinationLegal(board, this.getX() -2, this.getY() + 1))
//				list.add(new Step(this.getX(), this.getY(), this.getX() - 2, this.getY()+ 1));
//			if(this.getX() - 2 >= 0 && this.getY() - 1 >= 0 && 
//					isDestinationLegal(board, this.getX() -2, this.getY() - 1))
//				list.add(new Step(this.getX(), this.getY(), this.getX() - 2, this.getY()- 1));
//		}
//		//对下方蹩马腿位置进行检查
//		if(isPointLegal(this.getX() + 1, this.getY()) && board.getPiece(this.getX() + 1, this.getY()) == null) 
//		{
//			if(this.getX() < 10 && this.getY() < 9 && 
//					isDestinationLegal(board, this.getX() + 2, this.getY() + 1))
//				list.add(new Step(this.getX(), this.getY(), this.getX() - 2, this.getY()+ 1));
//			if(this.getX() - 2 >= 0 && this.getY() - 1 >= 0 && 
//					isDestinationLegal(board, this.getX() + 2, this.getY() - 1))
//				list.add(new Step(this.getX(), this.getY(), this.getX() - 2, this.getY()- 1));
//		}
//		//对右侧蹩马腿位置进行检查
//		if(isPointLegal(this.getX(), this.getY()+1) && board.getPiece(this.getX(), this.getY()+1) == null) 
//		{
//			if(this.getX() >= 0&&this.getY() < 9&&isDestinationLegal(board, this.getX() -1, this.getY() +2))
//				list.add(new Step(this.getX(), this.getY(), this.getX() -1, this.getY()+2));
//			if(this.getX() <10&&this.getY()< 9&&isDestinationLegal(board, this.getX() +1, this.getY() +2))
//				list.add(new Step(this.getX(), this.getY(), this.getX() - 1, this.getY()+2));
//		}
//		//对左侧蹩马腿位置进行检查
//		if(isPointLegal(this.getX(), this.getY()-1) && board.getPiece(this.getX(), this.getY()-1) == null) 
//		{
//			if(this.getX() >= 0&&this.getY() >= 0&&isDestinationLegal(board, this.getX() -1, this.getY() -2))
//				list.add(new Step(this.getX(), this.getY(), this.getX() -1, this.getY() - 2));
//			if(this.getX() <10&&this.getY() >=0&&isDestinationLegal(board, this.getX() +1, this.getY() - 2))
//				list.add(new Step(this.getX(), this.getY(), this.getX() +1, this.getY()- 2));
//		}
		
		int[][] dstep = { {-2, +1}, {-1, +2}, {+1, +2}, {+2, +1},
						  {+2, -1}, {+1, -2}, {-1, -2}, {-2, -1}};
		for (int i = 0; i < 8; i ++) {
			int dx = this.getX() + dstep[i][0];
			int dy = this.getY() + dstep[i][1];
			if (this.isPointLegal(dx, dy)) {
				if (this.isLegalMove(board, dx, dy)) {
					list.add(new Step(this.getX(), this.getY(), dx, dy));
				}
			}
		}
		
		
		
		
		return list;
	}

	

	@Override
	public String getImagePath() {
		// TODO Auto-generated method stub
		return null;
	}

}
