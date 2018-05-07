package chess.board;

import chess.piece.ChessPieces;

public class StepTrack {
	private ChessPieces from;
	private ChessPieces to;
	private int fx,fy;
	private int tx,ty;
	public StepTrack(ChessPieces from,ChessPieces to,int fx,int fy,int tx,int ty){
		this.from = from;
		this.to = to;
		this.fx = fx;
		this.fy = fy;
		this.tx = tx;
		this.ty = ty;
	}
	
	public ChessPieces getFrom() {
		return from;
	}
	public void setFrom(ChessPieces from) {
		this.from = from;
	}
	public ChessPieces getTo() {
		return to;
	}
	public void setTo(ChessPieces to) {
		this.to = to;
	}

	public int getFx() {
		return fx;
	}

	public void setFx(int fx) {
		this.fx = fx;
	}

	public int getFy() {
		return fy;
	}

	public void setFy(int fy) {
		this.fy = fy;
	}

	public int getTx() {
		return tx;
	}

	public void setTx(int tx) {
		this.tx = tx;
	}

	public int getTy() {
		return ty;
	}

	public void setTy(int ty) {
		this.ty = ty;
	}

}
