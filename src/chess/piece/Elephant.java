package chess.piece;

import java.util.ArrayList;
import java.util.List;

import chess.board.ChessBoard;
import chess.util.Step;

public class Elephant extends ChessPieces{

	public Elephant(int iniX, int iniY, boolean iniIsRed, boolean iniIsAlive, String iniName) {
		super(iniX, iniY, iniIsRed, iniIsAlive, iniName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isLegalMove(ChessBoard board, int toX, int toY) {
		// TODO Auto-generated method stub
		//求象眼坐标
		int mx = (toX + this.getX()) / 2;
		int my = (toY + this.getY()) / 2;
		if (!isMyTurn(board)) {
			return  false;
		}else if (toX == this.getX() && toY == this.getY()) {
			// 目的地横纵坐标与当前横纵坐标都相同，相当于没有移动，亦是非法的
			return false;
		}else if(!isDestinationLegal(board, toX, toY))
		{
			return false;
		}else if(board.getPiece(mx, my) != null){
			return false;
		}else {
			if(!isRed) //黑象
			{
				if(toX > 4)
					return false;
				
				if(Math.abs(this.getX()- toX) != 2 ||Math.abs(toY - this.getY())!= 2)
				{
					return false;
				}
				
			}else 
			{
				if(board.getPiece(mx, my) != null) 
				{
					return false;
				}
				if(toX <5)
					return false;
				if(Math.abs(this.getX()- toX) != 2 ||Math.abs(toY - this.getY())!= 2)
				{
					 return false;
				}
			}	
		}
		/*boolean m = true;
		//首先判断是否为本方走棋	
		if (!isMyTurn(board)) {
				m =  false;
		}
		if (toX == this.getX() && toY == this.getY()) {
			// 目的地横纵坐标与当前横纵坐标都相同，相当于没有移动，亦是非法的
				m = false;
		}
		//象眼位置有棋子，不可以行棋
		if(board.getPiece(mx, my) != null) 
		{
			m = false;
		}
		if(!isRed) //黑象
		{
			if(toX > 4)
				m = false;
			
			if(Math.abs(this.getX()- toX) != 2 ||Math.abs(toY)-this.getY()!= 2)
			{
				m = false;
			}
			
		}else 
		{
			if(board.getPiece(mx, my) != null) 
			{
				m = false;
			}
			if(toX <5)
				m = false;
			if(Math.abs(this.getX()- toX) != 2 ||Math.abs(toY)-this.getY()!= 2)
			{
				m = false;
			}
		}	
		return m;*/
		return true;
	}

	@Override
	public List<Step> getNextMove(ChessBoard board) {
		// TODO Auto-generated method stub
		List<Step> list = new ArrayList<Step>();
		//每个象可以走向四个对角线方向走棋
		int zx;
		int zy;
		//左上方向
		zx = this.getX() - 2;
		zy = this.getY() - 2;
		if(zx >= 0 && zx <= 9&& zy <= 8 && zy >= 0 && this.isLegalMove(board, zx, zy)) 
		{
			list.add(new Step(this.getX(), this.getY(), zx, zy));
		}
		//右上方向
		zx = this.getX() - 2;
		zy = this.getY() + 2;
		if(zx >= 0 && zx <= 9&& zy <= 8 && zy >= 0 && this.isLegalMove(board, zx, zy)) 
		{
			list.add(new Step(this.getX(), this.getY(), zx, zy));
		}
		//左下方向
		zx = this.getX() + 2;
		zy = this.getY() - 2;
		if(zx >= 0 && zx <= 9&& zy <= 8 && zy >= 0 && this.isLegalMove(board, zx, zy)) 
		{
			list.add(new Step(this.getX(), this.getY(), zx, zy));
		}
		//右下方向
		zx = this.getX() + 2;
		zy = this.getY() + 2;
		
		return list;
	}

	@Override
	public String getImagePath() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
