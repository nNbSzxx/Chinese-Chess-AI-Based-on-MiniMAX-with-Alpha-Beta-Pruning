package chess.piece;

import java.util.ArrayList;
import java.util.List;

import chess.board.ChessBoard;
import chess.util.Step;

public class Cannon extends ChessPieces{

	public Cannon(int iniX, int iniY, boolean iniIsRed, boolean iniIsAlive, String iniName) {
		super(iniX, iniY, iniIsRed, iniIsAlive, iniName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isLegalMove(ChessBoard board, int toX, int toY) {
		// TODO Auto-generated method stub
		if(!isMyTurn(board))
			return false;
		if(this.getX() == toX && this.getY() == toY)
			return false;
		if(this.getX() != toX && this.getY() != toY)
			return false;
		if(!isDestinationLegal(board, toX, toY))
			return false;
		//当炮不吃子的时候，只需判断是否从初始位置到目的位置没有其他字
		if(board.getPiece(toX, toY) == null) 
		{
			if(this.piecesInStraightWay(board, toX, toY)!= 0)
				return false;
		}
		//当炮吃子的时候只需要在初始位置和目的地位置之间只有一个棋子
		if(board.getPiece(toX, toY)!= null) 
		{
			System.out.println("Connon: " + this.piecesInStraightWay(board, toX, toY) + " toX:" + toX + "toY:" + toY);
			if(this.piecesInStraightWay(board, toX, toY) != 1)
				return false;
		}
		return true;
	}

	@Override
	public List<Step> getNextMove(ChessBoard board) {
		// TODO Auto-generated method stub
		List<Step> list = new ArrayList<Step>();
		//炮走直线，在它的位置上，他有上下左右四个方向可以走
		//向上	
		
		//向上的标记量
		int sx ;
		for(sx = this.getX() -1;sx >= 0;sx--) 
		{
			if(board.getPiece(sx, this.getY()) == null) //所到达的位置无棋子
			{
				if(this.piecesInStraightWay(board, sx, this.getY()) == 0){//线路上没有棋子
					list.add(new Step(this.getX(),this.getY(), sx, this.getY()));
					//System.out.println("hehe");
					
				}
					 
				//线路上有棋子
				//无需做任何操作
				//由于对break的使用规则不是很清楚，故即便不可以不再进行循环的时候，仍然在继续循环，知道结束，较为浪费时间
			}else {
				//所到达的位置上有棋子
				//如果在线路上没有棋子或者棋子数不为一，或者该位置的棋子和自己棋子为同一方都是不可以走到的点
				if(this.piecesInStraightWay(board, sx, this.getY()) == 1 &&
						!isSameSide(this, board.getPiece(sx, this.getY())))
					list.add(new Step(this.getX(), this.getY(), sx, this.getY()));
			}
		}
		//向下
		int xx;
		for(xx = this.getX() + 1;xx < 10;xx++) 
		{
			if(board.getPiece(xx, this.getY()) == null) //所到达的位置无棋子
			{
				if(this.piecesInStraightWay(board, xx, this.getY()) == 0){//线路上没有棋子
					list.add(new Step(this.getX(),this.getY(), xx, this.getY()));
					//System.out.println("hehe2");
				}
				//线路上有棋子
				//无需做任何操作
				//由于对break的使用规则不是很清楚，故即便不可以不再进行循环的时候，仍然在继续循环，知道结束，较为浪费时间
			}else {
				//所到达的位置上有棋子
				//如果在线路上没有棋子或者棋子数不为一，或者该位置的棋子和自己棋子为同一方都是不可以走到的点
				if(this.piecesInStraightWay(board, xx, this.getY()) == 1 &&
						!isSameSide(this, board.getPiece(xx, this.getY())))
					list.add(new Step(this.getX(), this.getY(), xx, this.getY()));
			}
		}
		//向左
		int zy;
		for(zy = this.getY() - 1;zy >= 0;zy--) 
		{
			if(board.getPiece(this.getX(), zy) == null) 
			{
				if(this.piecesInStraightWay(board, this.getX(), zy) == 0)
					list.add(new Step(this.getX(), this.getY(), this.getX(), zy));
			}else {
				if(this.piecesInStraightWay(board, this.getX(), zy) == 1 &&
						!isSameSide(this, board.getPiece(this.getX(),zy )))
					list.add(new Step(this.getX(), this.getY(), this.getX(), zy));
			}
		}
		//向右
		int yy;
		for(yy = this.getY() + 1;yy < 9;yy++) 
		{
			if(board.getPiece(this.getX(), yy) == null) 
			{
				if(this.piecesInStraightWay(board, this.getX(), yy) == 0)
					list.add(new Step(this.getX(), this.getY(), this.getX(), yy));
			}else {
				if(this.piecesInStraightWay(board, this.getX(), yy) == 1 &&
						!isSameSide(this, board.getPiece(this.getX(),yy )))
					list.add(new Step(this.getX(), this.getY(), this.getX(), yy));
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
