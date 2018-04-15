package chess.piece;

import java.util.ArrayList;
import java.util.List;


import chess.board.ChessBoard;
import chess.util.Step;

public class Soldier extends ChessPieces{

	public Soldier(int iniX, int iniY, boolean iniIsRed, boolean iniIsAlive, String iniName) {
		super(iniX, iniY, iniIsRed, iniIsAlive, iniName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isLegalMove(ChessBoard board, int toX, int toY) {
		// TODO Auto-generated method stub
		if (!isMyTurn(board)) {
			return false;
		}else if (toX == this.getX() && toY == this.getY()) {
			return false;
		}else if (!isDestinationLegal(board, toX, toY)) {
			return false;
		}else {
			if (!isRed) {
				if(this.getX() > toX)
					return false;
				if(this.getX() <= 4 && this.getX()==toX)
					return false;
				if(toX - this.getX() + Math.abs(toY - this.getY()) != 1)
					return false;
			}else {
				if(this.getX() < toX)
					return false;
				if (this.getX() >= 5 && this.getX() == toX) 
					return false;
				if(this.getX() - toX + Math.abs(toY - this.getY())!= 1)
					return false;
			}
		}
		return true;
	}

	@Override
	public List<Step> getNextMove(ChessBoard board) {
		// TODO Auto-generated method stub
		List<Step> list = new ArrayList<Step>();
		//产生黑卒的走法
		if(!isRed) 
		{
			//如果没有过河，那么就只能向前走，只需要前面的位置是空或者不是己方棋子就可以
			if(this.getX() <= 4) 
			{
				if( isDestinationLegal(board, this.getX()+1, this.getY())) 
				{
					list.add(new Step(this.getX(),this.getY(), this.getX()+1, this.getY()));
				}
			}else {//过河之后有前方一个和左右两个位置可以走，但是向前走要注意是否超过棋盘的边界
				if(this.getX() < 10 && this.getY()+1 < 9 && isDestinationLegal(board, this.getX(), this.getY()+1))
					list.add(new Step(this.getX(), this.getY(),this.getX(), this.getY() + 1));
				if(this.getX() < 10 && this.getY()-1 >= 0 && isDestinationLegal(board, this.getX(), this.getY()-1))
					list.add(new Step(this.getX(), this.getY(),this.getX(), this.getY() - 1));
				if(this.getX() < 10 && isDestinationLegal(board, this.getX()+1, this.getY()))
					list.add(new Step(this.getX(), this.getY(),this.getX()+1, this.getY()));
			}
		}else {
			//如果没有过河，那么就只能向前走，只需要前面的位置是空或者不是己方棋子就可以
			if(this.getX() >= 5) 
			{
				if(isDestinationLegal(board, this.getX()-1, this.getY()))
					list.add(new Step(this.getX(), this.getY(),this.getX()-1, this.getY()));
			}else {
				//过河之后有前方一个和左右两个位置可以走，但是向前走要注意是否超过棋盘的边界
				if(this.getX() >= 0 && this.getY()+1 < 9 && isDestinationLegal(board, this.getX(), this.getY()+1))
					list.add(new Step(this.getX(), this.getY(),this.getX(), this.getY() + 1));
				if(this.getX() >= 0 && this.getY() - 1 >= 0 && isDestinationLegal(board, this.getX(), this.getY()-1))
					list.add(new Step(this.getX(), this.getY(),this.getX(), this.getY() - 1));
				if(this.getX() >= 0 && isDestinationLegal(board, this.getX()-1, this.getY()))
					list.add(new Step(this.getX(), this.getY(),this.getX()-1, this.getY()));
			}
		}
		return list;
	}

	
	public String getImagePath() {
		// TODO Auto-generated method stub
		return null;
	}

}
