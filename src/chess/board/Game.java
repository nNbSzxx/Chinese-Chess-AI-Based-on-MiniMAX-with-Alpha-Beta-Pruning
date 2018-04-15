package chess.board;
import chess.util.Step;

public interface Game {
	public static enum State {FIRSTMOVEWIN, SECONDMOVEWIN, DRAW, UNFINISH};
	
	// 判断棋子移动是否合法
	abstract public boolean isMoveLegal(Step step);
	// 移动棋子，如果移动成功，返回true， 失败则返回false
	abstract public boolean update(Step step);
	// 检查是否终局 
	abstract public State isEnd();
	// 重新开局，再战！
	// abstract public void restart();
}
