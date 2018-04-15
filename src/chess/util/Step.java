package chess.util;

public class Step {
	private int fromX, fromY, toX, toY;

	public Step(int iniFromX, int iniFromY, int iniToX, int iniToY) {
		// TODO Auto-generated constructor stub
		fromX = iniFromX;
		fromY = iniFromY;
		toX = iniToX;
		toY = iniToY;
	}
	public Step(){
		
	}
	
	// getter
	public int getFromX() {
		return fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public int getToX() {
		return toX;
	}

	public int getToY() {
		return toY;
	}
	
}
