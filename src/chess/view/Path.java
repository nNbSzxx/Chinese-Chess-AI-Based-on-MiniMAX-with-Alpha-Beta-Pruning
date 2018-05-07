package chess.view;

public class Path {
	private ChessPiece from;
	private ChessPiece to;
	public Path(ChessPiece from,ChessPiece to){		
		this.from = from;
		this.to = to;
		
	}
	public ChessPiece getFrom() {
		return this.from;
	}
	public void setFrom(ChessPiece from) {
		
		this.from = from;
	}
	public ChessPiece getTo() {
		return this.to;
	}
	public void setTo(ChessPiece to) {
		this.to = to;
	}

}
