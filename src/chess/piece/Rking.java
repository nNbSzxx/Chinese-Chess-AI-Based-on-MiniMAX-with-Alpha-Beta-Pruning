package chess.piece;

import java.util.ArrayList;
import java.util.List;

import chess.board.ChessBoard;
import chess.util.Step;

public class Rking extends ChessPieces{

	
	public Rking(int iniX, int iniY, boolean iniIsRed, boolean iniIsAlive, String iniName) {
		super(iniX, iniY, iniIsRed, iniIsAlive, iniName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isLegalMove(ChessBoard board, int toX, int toY) {
		
		// TODO Auto-generated method stub
		if(!isMyTurn(board)) 
		{	
			return false;
		}else if (this.getX() == toX && this.getY() == toY) { 
			return false;
		}else if (!isDestinationLegal(board, toX, toY)) { 
			return false;
		}else if (toX < 7 || toY < 3||toY > 5) {//将出九宫
			return false;
		}else if (Math.abs(toY - this.getY()) + Math.abs(toX- this.getX())  != 1) {
			return false;
		}else {//将帅不能对脸？？
			//需要遍历一下棋盘找到Bking的 位置坐标
			int bx = 0;
			int by = 0;
			for(int i = 0;i < 3;i++) 
			{
				for(int j = 3;j < 6;j++) 
				{
					if(board.getPiece(i, j)!= null){
						if(board.getPiece(i, j).getName() == "B_King") 
							bx = i;by = j;
					}
				}
			}
			if(by == toY) 
			{
//				if(temp.piecesInStraightWay(board, toX, toY) == 0)
//					return false;,
				for(int x = bx + 1 ; x  < toX ; x++){
					if(board.getPiece(x, by) != null) return true;
				}
				return false;
			}
		}
		
		return true;
	}

	@Override
	public List<Step> getNextMove(ChessBoard board) {
		// TODO Auto-generated method stub
		List<Step> list = new ArrayList<Step>();
		for(int i = 7;i< 10;i++) //对九宫格进行遍历，对于满足条件的加到list中
		{
			for(int j = 3;j < 6;j++) 
			{
				if(this.isLegalMove(board, i, j)) {
					list.add(new Step(this.getX(), this.getY(), i, j));
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
