package chess.AI;

import java.util.Stack;

/**
 * 
 * @author nNbS
 * 
 *   This class is used to describe one particular single position
 * in a Chinese Chess game.
 *
 */
public final class Position {
	
	// 最大回滚步数
	public static final int MAX_STEP = 256;
	
	// 用于初始化棋盘的常量
	private static int BK = Board.BLACK_KING;
	private static int BA1 = Board.BLACK_ADVISOR_1;
	private static int BA2 = Board.BLACK_ADVISOR_2;
	private static int BB1 = Board.BLACK_BISHOP_1;
	private static int BB2 = Board.BLACK_BISHOP_2;
	private static int BK1 = Board.BLACK_KNIGHT_1;
	private static int BK2 = Board.BLACK_KNIGHT_2;
	private static int BR1 = Board.BLACK_ROOK_1;
	private static int BR2 = Board.BLACK_ROOK_2;
	private static int BC1 = Board.BLACK_CANNON_1;
	private static int BC2 = Board.BLACK_CANNON_2;
	private static int BP1 = Board.BLACK_PAWN_1;
	private static int BP2 = Board.BLACK_PAWN_2;
	private static int BP3 = Board.BLACK_PAWN_3;
	private static int BP4 = Board.BLACK_PAWN_4;
	private static int BP5 = Board.BLACK_PAWN_5;
	
	private static int RK = Board.RED_KING;
	private static int RA1 = Board.RED_ADVISOR_1;
	private static int RA2 = Board.RED_ADVISOR_2;
	private static int RB1 = Board.RED_BISHOP_1;
	private static int RB2 = Board.RED_BISHOP_2;
	private static int RK1 = Board.RED_KNIGHT_1;
	private static int RK2 = Board.RED_KNIGHT_2;
	private static int RR1 = Board.RED_ROOK_1;
	private static int RR2 = Board.RED_ROOK_2;
	private static int RC1 = Board.RED_CANNON_1;
	private static int RC2 = Board.RED_CANNON_2;
	private static int RP1 = Board.RED_PAWN_1;
	private static int RP2 = Board.RED_PAWN_2;
	private static int RP3 = Board.RED_PAWN_3;
	private static int RP4 = Board.RED_PAWN_4;
	private static int RP5 = Board.RED_PAWN_5;
	
	private static final int INI_BOARD[] = {
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,BR1,BK1,BB1,BA1, BK,BA2,BB2,BK2,BR2,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,  0,BC1,  0,  0,  0,  0,  0,BC2,  0,  0,  0,  0,  0,
	  0,  0,  0,BP1,  0,BP2,  0,BP3,  0,BP4,  0,BP5,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,RP1,  0,RP2,  0,RP3,  0,RP4,  0,RP5,  0,  0,  0,  0,
	  0,  0,  0,  0,RC1,  0,  0,  0,  0,  0,RC2,  0,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,RR1,RK1,RB1,RA1, RK,RA2,RB2,RK2,RR2,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
	};
	// 用于棋局回滚的结构
	private final class RollBackRecord {
		private int from;
		private int to;
		private int capedPiece;
		public RollBackRecord(int from, int to, int capedPiece) {
			this.from = from;
			this.to = to;
			this.capedPiece = capedPiece;
		}
		public int getFrom() {
			return from;
		}
		public int getTo() {
			return to;
		}
		public int getCapedPiece() {
			return capedPiece;
		}
	}
	// 私有数据成员
	// true表示轮到红方行棋
	private boolean isRedMove;
	// 0表示没有棋子，否则存储的是棋子的编号
	private int[] board = new int[Board.BOARD_SIZE];
	// 存储每个棋子在棋盘上的位置，0表示棋子已死，遍历所有棋子应搭配Board.PIECE_ARRAY使用
	private int[] pieceLoc = new int[Board.UPPER_BOUND_PIECE_ID];
	// 压位存储当前行每个格子是否有子，数组下标代表行号
	// 注意低位->高位 代表棋盘 左边（列号小）->右边（列号大）
	private int rankBit[] = new int[Board.MAX_RANK];
	// 压位存储当前列每个格子是否有子，数组下标代表列号
	// 注意低位->高位 代表棋盘 上边边（行号小）->下边（行号大）
	private int fileBit[] = new int[Board.MAX_FILE];   
	// 棋局评估变量，存储当前局面评估值，红方1黑方0
	private int evaluationVal[] = new int[2];
	// 棋局回滚结构，用于撤销一步祺
	private Stack<RollBackRecord> records = new Stack<>();
	// 私有数据成员结束
	
	
	public Position() {
		isRedMove = true;
		for (int i = 0; i < Board.BOARD_SIZE; i ++) {
			int piece = INI_BOARD[i];
			if (piece != 0) {
				addPiece(i, piece);
			}
		}
		assert (evaluationVal[Board.RED_SIDE] == evaluationVal[Board.BLACK_SIDE]);
		// 输出棋盘
		System.out.println("BK: " + BK);
		for (int i = 0; i < Board.BOARD_SIZE; i ++) {
			if (i % 16 == 0) {
				System.out.println("");
			}
			System.out.print(board[i] + "\t");
		}
		System.out.println("\n");
		for (int i = 0; i < Board.MAX_RANK; i ++) {
			for (int j = 0; j < Board.REAL_MAX_FILE; j ++) {
				if ((rankBit[i] & (1 << j)) != 0) {
					System.out.print(1);
				} else {
					System.out.print(0);
				}
			}
			System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < Board.MAX_FILE; i ++) {
			for (int j = 0; j < Board.REAL_MAX_RANK; j ++) {
				if ((fileBit[i] & (1 << j)) != 0) {
					System.out.print(1);
				} else {
					System.out.print(0);
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}
	// unfinished
//	public Position(ChessBoard board) {
//		for (int i = 0; i < board.MAXROW; i ++) {
//			for (int j = 0; j < board.MAXCOL; j ++) {
//				
//			}
//		}
//	}
	
	// 走一步祺
	public void move(int from, int to) {
		records.push(new RollBackRecord(from, to, board[to]));
		movePiece(from, to);
		changeSide();
	}
	
	// 撤销一步祺
	public void undoMove() {
		RollBackRecord record = records.pop();
		int from = record.getFrom();
		int to = record.getTo();
		int capedPiece = record.getCapedPiece();
		undoMovePiece(from, to, capedPiece);
		changeSide();
	}
	
	// 评估当前局面 
	public int evaluate() {
		return isRedMove ? 
					(evaluationVal[Board.RED_SIDE] - evaluationVal[Board.BLACK_SIDE]) :
					(evaluationVal[Board.BLACK_SIDE] - evaluationVal[Board.RED_SIDE]);
	}
	
	public boolean isEnd() {
		return (pieceLoc[Board.RED_KING] == 0 ||
				pieceLoc[Board.BLACK_KING] == 0);
	}
	
	// 移动棋子
	private void movePiece(int from, int to) {
//		System.out.println("In chess.AI.Position.movePiece: ");
//		System.out.println("Pick: " + board[from]);
//		System.out.println("ToLocPiece: " + board[to]);
		if (board[to] != 0) {
			delPiece(to);
		}
		int piece = board[from];
		delPiece(from);
		addPiece(to, piece);
	}
	
	// 撤销移动棋子
	private void undoMovePiece(int from, int to, int capedPiece) {
		assert(board[from] == 0);
		int piece = board[to];
		delPiece(to);
		addPiece(from, piece);
		if (capedPiece != 0) {
			addPiece(to, capedPiece);
		}
	}
	
	// 添加棋子
	private void addPiece(int loc, int piece) {
		assert (Board.inBoard(loc));
		assert (board[loc] == 0);
		board[loc] = piece;
		assert(pieceLoc[piece] == 0);
		pieceLoc[piece] = loc;
		assert ((rankBit[Board.getRank(loc)] & MoveTable.rankMask[loc]) == 0);
		rankBit[Board.getRank(loc)] ^= MoveTable.rankMask[loc];
		assert ((fileBit[Board.getFile(loc)] & MoveTable.fileMask[loc]) == 0);
		fileBit[Board.getFile(loc)] ^= MoveTable.fileMask[loc];
		// 增加评估值
		evaluationVal[Board.getPieceSide(piece)] += 
				Evaluator.getPieceVal(piece, loc);
	}
	// 删除棋子
	private void delPiece(int loc) {
		assert (Board.inBoard(loc));
		int piece = board[loc];
		assert (piece != 0);
		board[loc] = 0;
		assert(pieceLoc[piece] != 0);
		pieceLoc[piece] = 0;
		assert ((rankBit[Board.getRank(loc)] & MoveTable.rankMask[loc]) != 0);
		rankBit[Board.getRank(loc)] ^= MoveTable.rankMask[loc];
		assert ((fileBit[Board.getFile(loc)] & MoveTable.fileMask[loc]) != 0);
		fileBit[Board.getFile(loc)] ^= MoveTable.fileMask[loc];
		// 减少评估值
		evaluationVal[Board.getPieceSide(piece)] -=
				Evaluator.getPieceVal(piece, loc);
		assert (evaluationVal[Board.BLACK_SIDE] >= 0);
		assert (evaluationVal[Board.RED_SIDE] >= 0);
	}
	private void changeSide() {
		isRedMove = !isRedMove;
	}
	
	// getter & setter
	public boolean isRedMove() {
		return isRedMove;
	}
	public void setRedMove(boolean isRedMove) {
		this.isRedMove = isRedMove;
	}
	public int getPiece(int loc) {
		return board[loc];
	}
	public int getPieceLoc(int piece) {
		return pieceLoc[piece];
	}
	public int getRankBit(int rank) {
		return rankBit[rank];
	}
	public int getFileBit(int file) {
		return fileBit[file];
	}
	
	// test
	private static boolean isAllDiff() {
		int cnt[] = new int[256];
		for (int i = 0; i < INI_BOARD.length; i++) {
			++ cnt[INI_BOARD[i]];
			if (INI_BOARD[i] != 0 && cnt[INI_BOARD[i]] > 1)
				return false;
		}
		return true;
	}
	
	public static void main(String args[]) {
//		assert false;
		assert isAllDiff();
		// 棋盘初始化正确
		new Position();
		System.out.println("No assertions break");
	}
}
