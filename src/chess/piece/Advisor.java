package chess.piece;

import java.util.ArrayList;
import java.util.List;

import chess.board.ChessBoard;
import chess.util.Step;

public class Advisor extends ChessPieces{

	public Advisor(int iniX, int iniY, boolean iniIsRed, boolean iniIsAlive, String iniName) {
		super(iniX, iniY, iniIsRed, iniIsAlive, iniName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isLegalMove(ChessBoard board, int toX, int toY) {
		// TODO Auto-generated method stub
		//首先判断是否为本方走棋
		if (!isMyTurn(board)) {
			return false;
			}
		else if (toX == this.getX() && toY == this.getY()) {
			// 目的地横纵坐标与当前横纵坐标都相同，相当于没有移动，亦是非法的
			return false;
		}else if (!isDestinationLegal(board, toX, toY)) {
			return false;
		}else {
			if(!this.isRed) //黑士
			{
					if(toX > 2 || toY > 5||toY < 3) //士出九宫
					{
						return false;
					}
					if(Math.abs(toX - this.getX())!= 1 ||Math.abs(toY - this.getY()) != 1) 
					{
						return false;
					}
			}else //红士
			{
				if(toX < 7||toY < 3||toY > 5 ) //士出九宫
				{
					return false;
				}
				if(Math.abs(toX - this.getX())!= 1 ||Math.abs(toY - this.getY()) != 1) 
				{
					return false;
				}
			}
		}
		return true;
		
		
		
		/*boolean mm = true;
		if (!isMyTurn(board)) {
			mm =  false;
			}
		if (toX == this.getX() && toY == this.getY()) {
			// 目的地横纵坐标与当前横纵坐标都相同，相当于没有移动，亦是非法的
				mm = false;
			}
		if(isSameSide(board.getPiece(toX, toY), this)) 
		{
			mm = false;
		}
		if(!this.isRed) //黑士
		{
				if(toX > 2 || toY > 5||toY < 3) //士出九宫
				{
					mm = false;
				}
				if(Math.abs(toX - this.getX())!= 1 ||Math.abs(toY)-this.getY() != 1) 
				{
					mm = false;
				}
		}else //红士
		{
			if(toX < 7||toY < 3||toY > 5 ) //士出九宫
			{
				mm = false;
			}
			if(Math.abs(toX - this.getX())!= 1 ||Math.abs(toY)-this.getY() != 1) 
			{
				mm = false;
			}
		}
		return mm;*/
	}

	@Override
	public List<Step> getNextMove(ChessBoard board) {
		// TODO Auto-generated method stub
		
		List<Step> list = new ArrayList<Step>();
		//每一种颜色的士只有五个地方可以走
		if(isRed) 
		{
			if(this.isLegalMove(board,7, 3)) 
				list.add(new Step(this.getX(), this.getY(), 7, 3));
			if(this.isLegalMove(board,7,5)) 
				list.add(new Step(this.getX(), this.getY(), 7, 5));
			if(this.isLegalMove(board,8, 4)) 
				list.add(new Step(this.getX(), this.getY(), 8, 4));
			if(this.isLegalMove(board,9, 3)) 
				list.add(new Step(this.getX(), this.getY(), 9, 3));
			if(this.isLegalMove(board,9, 5)) 
				list.add(new Step(this.getX(), this.getY(), 9, 5));
		}else 
		{
			if(this.isLegalMove(board,0, 3)) 
				list.add(new Step(this.getX(), this.getY(), 0, 3));
			if(this.isLegalMove(board,0,5)) 
				list.add(new Step(this.getX(), this.getY(), 0, 5));
			if(this.isLegalMove(board,1, 4)) 
				list.add(new Step(this.getX(), this.getY(), 1, 4));
			if(this.isLegalMove(board,2, 3)) 
				list.add(new Step(this.getX(), this.getY(), 2, 3));
			if(this.isLegalMove(board,2, 5)) 
				list.add(new Step(this.getX(), this.getY(), 2, 5));
		}
		
		//this.addNextStep(board, list);
		return list;
	}

	
	public String getImagePath() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
