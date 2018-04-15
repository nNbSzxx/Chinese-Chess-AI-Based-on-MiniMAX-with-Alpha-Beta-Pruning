package chess.board;

import java.util.ArrayList;
import java.util.List;
import chess.AI.ChessEngine;
import chess.AI.Searchable;
import chess.piece.Advisor;
import chess.piece.Bking;
import chess.piece.Cannon;
import chess.piece.ChessPieces;
import chess.piece.Elephant;
import chess.piece.Horse;
import chess.piece.Rking;
import chess.piece.Rook;
import chess.piece.Soldier;
import chess.util.Step;

public class ChessBoard extends Searchable{
	// 定义当前棋局状态
	public static enum State {REDWIN, BLACKWIN, DRAW, UNFINISH};
	State state;
	// 不论用户是红方还是黑方，我们总是定义[0, 0]处为黑车，用户红黑方绘图不同的问题交给ChessView类去解决
	private ChessPieces[][] board;
	
	// AI对象
	private ChessEngine AI;
	// 标记当前行祺方
	private boolean isRedMove;
	
	// 棋盘最大行数
	static public final int MAXROW = 10;
	// 棋盘最大列数
	static public final int MAXCOL = 9;
	// 棋盘上最多棋子数
	static public final int MAXPIECE = 32;
	
	
	// 初始化棋盘，将棋盘状态重置为开局时状态
	private void initialize() {
		initPieces();
		this.isRedMove = true;
		this.state = State.UNFINISH;
//		this.isUserRed = isUserRed;
//		this.isAIRed = !isUserRed;
//		this.isRedBeChecked = false;
//		this.isBlackBeChecked = false;
	}
	
	public ChessBoard(ChessPieces [][] CurBoard,boolean flag) {
		// TODO Auto-generated constructor stub
		board = new ChessPieces[MAXROW][MAXCOL];
		//AI = new ChessEngine();
		// isAIRed = iniIsAIRed;
		this.state = State.UNFINISH;
		board = CurBoard;
		isRedMove = flag;
	}

	
	public ChessBoard() {
		// TODO Auto-generated constructor stub
		board = new ChessPieces[MAXROW][MAXCOL];
		AI = new ChessEngine();
		// isAIRed = iniIsAIRed;
		initialize();
	}
	
	// 以下是对象棋游戏主要功能实现
	// 判断棋子移动是否合法，通过调用ChessPieces类的函数实现
	public boolean isMoveLegal(Step step) {
		boolean isLegal = false;
		System.out.print("In chess.board.ChessBoard.isMoveLegal: ");
		if (checkLegalIndexRange(step)) {
			System.out.print("legal index range  ");
			if (board[step.getFromX()][step.getFromY()] != null) {
				System.out.print("pick " + board[step.getFromX()][step.getFromY()].getName() + " ");
				isLegal = board[step.getFromX()][step.getFromY()].isLegalMove(this, step.getToX(), step.getToY());
			} else {
				System.out.print("pick null ");
			}
			System.out.println("");
		} else {
			System.out.println("Chess Board Index Out Of Range in isMoveLegal!");
			System.out.printf("Index: %d %d %d %d", 
					step.getFromX(), step.getFromY(),
					step.getToX(),   step.getToY() );
			System.exit(-1);
		}
		System.out.println("Legal move is" + isLegal);
		return isLegal;
	}
	
	// 移动棋子，如果移动成功，返回true， 失败则返回false， 规定坐标范围为 0 to MAX - 1
	public boolean update(Step step) {
		System.out.print("In chess.board.ChessBoard.update: ");
		System.out.println("from x:" + step.getFromX() + " from y:" + step.getFromY() + " to x:"
				+ step.getToX() + " to y:" + step.getToY() + " ");
		if (isMoveLegal(step)) {
			// 移动棋子
			move(step);
			// 更新当前行祺方
			isRedMove = !isRedMove;
			// 更新AI棋盘状态
			AI.applyAMove(step);
			// 判断当前是否将军  
			/*
			if (board[step.getToX()][step.getToY()].isCheck(this)) {
				if (isRedMove()) {
					isRedBeChecked = true;
				} else {
					isBlackBeChecked = true;
				}
				// 检查是否终局
				isEnd();
			}
			*/
			
			
			return true;
		} else {
			return false;
		}
	}
	
	// 检查是否终局
	public State isEnd() {
		return state;
	}
	// 象棋游戏主要功能实现完毕	
	
	// 以下是与AI相关的功能
	// 向AI询问下一步如何走
	public Step AIMove() {
		Step step = AI.generateAMove();
		boolean isLegal = update(step);
		if (!isLegal) {
			System.out.println("AI gives an unlegal move!!!!!");
			System.out.println(step.getFromX() + " " + step.getFromY() + " " + step.getToX() + " " + step.getToY());
			System.exit(-1);
		}
		return step;
	}
	
	// 对手求和，AI是否答应
	/*
	public boolean askForPeace() {
		return AI.askForPeace(this);
	}
	*/
	// 生成所有合法的下一步走棋棋步，当然是生成当前待走棋方的
	public List<Step> generateAllNextMove() {
		List<Step> list = new ArrayList<>();
		for (int i = 0; i < MAXROW; i ++) {
			for (int j = 0; j < MAXCOL; j ++) {
				if (board[i][j] != null && 
						board[i][j].isRed == isRedMove && 
						board[i][j].isAlive()) {
					//System.out.println(board[i][j].getName());
					List<Step> list2 = board[i][j].getNextMove(this);
					//System.out.println(i + " " + j);
					for (Step step : list2) {
						//System.out.println(step.getFromX() + " " + step.getFromY() + " " + step.getToX() + " " + step.getToY());
						if (step.getFromX() == 0 && step.getFromY() == 0 && step.getToX() == 0 && step.getToY() == 0){
							//System.out.println("0000error" + board[i][j].getName());
							//System.exit(-1);
						}
					}
					list.addAll(list2);
				}
			}
		}
		return list;
	}
	
	// 返回棋盘副本
	public ChessPieces [][] getClone(){
		return board.clone();
	}
	
	// 返回当前局面总分
	public int getValue() {
		return evaluate(board,this);
	}
	
	// AI相关功能实现完毕
	
	public static boolean checkLegalIndexRange(Step step) {
		return (step.getFromX() >= 0 && step.getFromX() < MAXROW 
				&& 
				step.getFromY() >= 0 && step.getFromY() < MAXCOL); 
	}
	
	// 以下是为public方法提供便利的一些方法
	
	// 把位于from坐标的棋子移动到to坐标
	private void move(Step step) {
		// 如果有吃子，则更新被吃棋子是否存活的信息
		if (board[step.getToX()][step.getToY()] != null) {
			board[step.getToX()][step.getToY()].setAlive(false);
			if (board[step.getToX()][step.getToY()].getName() == "B_King") {
				state = State.REDWIN;
			} else if (board[step.getToX()][step.getToY()].getName() == "R_King") {
				state = State.BLACKWIN;
			}
		}
		// 移动棋子并更新其对应数据域的坐标信息
		board[step.getFromX()][step.getFromY()].
				move(step.getToX(), step.getToY());
		board[step.getToX()][step.getToY()] = 
				board[step.getFromX()][step.getFromY()];
		// 清空原位置
		board[step.getFromX()][step.getFromY()] = null;
	}
		
	// 将棋子摆成开局时的状态 unfinished
	private void initPieces() {
		// initialize the chessBoard
		board[0][0] = new Rook(0, 0, false, true, "B_Rook");
		board[0][8] = new Rook(0, 8, false, true, "B_Rook");
		board[0][1] = new Horse(0, 1, false, true, "B_Horse");
		board[0][7] = new Horse(0, 7, false, true, "B_Horse");
		board[0][2] = new Elephant(0, 2, false, true, "B_Elephant");
		board[0][6] = new Elephant(0, 6, false, true, "B_Elephant");
		board[0][3] = new Advisor(0, 3, false, true, "B_Advisor");
		board[0][5] = new Advisor(0, 5, false, true, "B_Advisor");
		board[0][4] = new Bking(0, 4, false, true, "B_King");
		board[2][1] = new Cannon(2, 1, false, true, "B_Cannon");
		board[2][7] = new Cannon(2, 7, false, true, "B_Cannon");
		for (int i = 0; i < ChessBoard.MAXCOL; i += 2) {
			board[3][i] = new Soldier(3, i, false, true, "B_Soldier");
		}
		
		board[9][0] = new Rook(9, 0, true, true, "R_Rook");
		board[9][8] = new Rook(9, 8, true, true, "R_Rook");
		board[9][1] = new Horse(9, 1, true, true, "R_Horse");
		board[9][7] = new Horse(9, 7, true, true, "R_Horse");
		board[9][2] = new Elephant(9, 2, true, true, "R_Elephant");
		board[9][6] = new Elephant(9, 6, true, true, "R_Elephant");
		board[9][3] = new Advisor(9, 3, true, true, "R_Advisor");
		board[9][5] = new Advisor(9, 5, true, true, "R_Advisor");
		board[9][4] = new Rking(9, 4, true, true, "R_King");
		board[7][1] = new Cannon(7, 1, true, true, "R_Cannon");
		board[7][7] = new Cannon(7, 7, true, true, "R_Cannon");
		for (int i = 0; i < ChessBoard.MAXCOL; i += 2) {
			board[6][i] = new Soldier(6, i, true, true, "R_Soldier");
		}
		
	}
	
	// getter & setter
	
	// 根据棋盘坐标，返回棋子，若没有棋子，则返回null
	public ChessPieces getPiece(int x, int y) {
		/*
		if (x >= 0 && x < ChessBoard.MAXROW && y >= 0 && y < ChessBoard.MAXCOL) {
			return board[x][y];
		} else {
			System.out.println("Chess Board Index Out Of Range in getPiece!");
			System.out.printf("Index: %d %d", x, y);
			System.exit(-1);
			return null;
		}*/
		return board[x][y];
	}
	
	public boolean isRedMove() {
		return isRedMove;
	}

	
	
	// 以下是被弃用的方法和数据成员
	
	// 实现上存在麻烦
	// 红方是否被将军
//		private boolean isRedBeChecked;
	// 黑方是否被将军
//		private boolean isBlackBeChecked;
	
	// 存储当前棋子，和board指向的是同一对象，只是提供另一种遍历方式，似乎并不需要
//		private ChessPieces[] pieces;
	// 这两个变量放在ChessView中似乎更为妥当
	// 标记用户是否是红方，若用户是红方，则把棋盘画在下面，用户是黑方，则把黑方画在上面
//		private boolean isUserRed;
	// 标记AI是红方还是黑方
//		private boolean isAIRed; // 似乎应该放到Searchable接口里面，或者更恰当的说说AIable接口
	
	
//	public boolean isRedBeChecked() {
//		return isRedBeChecked;
//	}
//
//	public boolean isBlackBeChecked() {
//		return isBlackBeChecked;
//	}
//
//	public boolean isUserRed() {
//		return isUserRed;
//	}
	
	// 启动应该放在ChessView中	
	// 启动象棋游戏 unfinished
//	public void startHumanVsAI() {
//		initialize();
//		while (!isEnd()) {
//			if (isRedMove) {
//				if (isAIRed) {
//					
//				}
//			}
//		}
//	}
	
	// getPiece方法更加安全
//	// 返回当前的棋盘状态
//	public ChessPieces[][] getNowBoard() {
//		return board.clone();
//	}
	
	// 放在ChessPiece中更好
//	// 判断两个棋子是否来自同一方
//	public boolean isSameSide(ChessPieces pieces1, ChessPieces pieces2) {
//		return pieces1.isRed == pieces2.isRed;
//	}
	
	// 重新开局只需要ChessView重新new一个ChessBoard对象就可以了
	// 重新开局，再战！
//		public void restart(boolean isUserRed) {
//			initialize();
//		}
}
