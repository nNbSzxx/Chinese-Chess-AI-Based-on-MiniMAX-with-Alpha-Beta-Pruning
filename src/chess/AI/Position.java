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
	// Zobrist 键值，用于置换表等
	// 需要在改变棋局信息时修改键值，比如初始化棋盘，添加棋子，移除棋子，变换走棋方
	private Zobrist zobrist = new Zobrist();
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
		// 输出棋盘，以供调试
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
		// 输出结束
	}
	
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
	
	// 走一步空着，在空着裁剪中运用
	public void makeNullMove() {
		changeSide();
	}
	// 撤销一步空着
	public void undoNullMove() {
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
	
	// 判断是否被将军
	public boolean isChecked() {
		int myKing = (isRedMove)? Board.RED_KING: Board.BLACK_KING;
		int myKingLoc = getPieceLoc(myKing);
		assert (Board.inFort(myKingLoc));
		
		// 可能被帅车炮马兵将军，不可能被士象将军
		return isCheckedByKingOrRook(myKing, myKingLoc) || 
			   isCheckedByCannon(myKing, myKingLoc) || 
			   isCheckedByKnight(myKing, myKingLoc) ||
			   isCheckedByPawn(myKing, myKingLoc);
	}
	
	// 判断是否将帅对脸
	public boolean doKingFaceKing() {
		int redKingLoc = getPieceLoc(Board.RED_KING);
		int blackKingLoc = getPieceLoc(Board.BLACK_KING);
		if (redKingLoc == 0 || blackKingLoc == 0) {
			return false;
		}
//			System.out.println(redKingLoc);
		assert (Board.inFort(redKingLoc));
		assert (Board.inFort(blackKingLoc));
		// 判断的思想是把帅看作是车，判断这样被当作车的帅是否能吃到将
		return (Board.getFile(redKingLoc) == Board.getFile(blackKingLoc) &&
				MoveTable.getFileSmallestRookCap
					(redKingLoc, getFileBit(Board.getFile(redKingLoc))) == blackKingLoc);
	}
	
	// 以下是判断将帅被特定棋子将军的方法
	private boolean isCheckedByKingOrRook(int myKing, int myKingLoc) {
		// 把帅看作车，判断能否吃到对方的车或者将
		// 将帅不可能处于同一行
		int rankBiggestCapLoc = 
				MoveTable.getRankBiggestRookCap(myKingLoc, getRankBit(Board.getRank(myKingLoc)));
		assert (Board.inBoard(rankBiggestCapLoc));
		assert (Board.getRank(rankBiggestCapLoc) == Board.getRank(myKingLoc));
		int capedPiece = getPiece(rankBiggestCapLoc);
		assert (capedPiece != 0);
		if (!Board.isPiecesSameSide(capedPiece, myKing)) {
			int pieceType = Board.getPieceType(capedPiece);
			assert (pieceType != Board.KING_MASK);
			if (pieceType == Board.ROOK_MASK) {
				return true;
			}
		}
		int rankSmallestCapLoc = 
				MoveTable.getRankSmallestRookCap(myKingLoc, getRankBit(Board.getRank(myKingLoc)));
		assert (Board.inBoard(rankSmallestCapLoc));
		assert (Board.getRank(rankSmallestCapLoc) == Board.getRank(myKingLoc));
		capedPiece = getPiece(rankSmallestCapLoc);
		assert (capedPiece != 0);
		if (!Board.isPiecesSameSide(capedPiece, myKing)) {
			int pieceType = Board.getPieceType(capedPiece);
			assert (pieceType != Board.KING_MASK);
			if (pieceType == Board.ROOK_MASK) {
				return true;
			}
		}
		int fileBiggestCapLoc = 
				MoveTable.getFileBiggestRookCap(myKingLoc, getFileBit(Board.getFile(myKingLoc)));
		assert (Board.inBoard(fileBiggestCapLoc));
		assert (Board.getFile(fileBiggestCapLoc) == Board.getFile(myKingLoc));
		capedPiece = getPiece(fileBiggestCapLoc);
		assert (capedPiece != 0);
		if (!Board.isPiecesSameSide(capedPiece, myKing)) {
			int pieceType = Board.getPieceType(capedPiece);
			if (pieceType == Board.ROOK_MASK || pieceType == Board.KING_MASK) {
				return true;
			}
		}
		int fileSmallestCapLoc = 
				MoveTable.getFileSmallestRookCap(myKingLoc, getFileBit(Board.getFile(myKingLoc)));
		assert (Board.inBoard(fileSmallestCapLoc));
		assert (Board.getFile(fileSmallestCapLoc) == Board.getFile(myKingLoc));
		capedPiece = getPiece(fileSmallestCapLoc);
		assert (capedPiece != 0);
		if (!Board.isPiecesSameSide(capedPiece, myKing)) {
			int pieceType = Board.getPieceType(capedPiece);
			if (pieceType == Board.ROOK_MASK || pieceType == Board.KING_MASK) {
				return true;
			}
		}
		return false;
	}
	private boolean isCheckedByCannon(int myKing, int myKingLoc) {
		// 把帅看作炮，判断能否吃到对方的炮
		int rankBiggestCapLoc = 
				MoveTable.getRankBiggestCannonCap(myKingLoc, getRankBit(Board.getRank(myKingLoc)));
		assert (Board.inBoard(rankBiggestCapLoc));
		assert (Board.getRank(rankBiggestCapLoc) == Board.getRank(myKingLoc));
		int capedPiece = getPiece(rankBiggestCapLoc);
		assert (capedPiece != 0);
		if (!Board.isPiecesSameSide(capedPiece, myKing)) {
			int pieceType = Board.getPieceType(capedPiece);
			if (pieceType == Board.CANNON_MASK) {
				return true;
			}
		}
		int rankSmallestCapLoc = 
				MoveTable.getRankSmallestCannonCap(myKingLoc, getRankBit(Board.getRank(myKingLoc)));
		assert (Board.inBoard(rankSmallestCapLoc));
		assert (Board.getRank(rankSmallestCapLoc) == Board.getRank(myKingLoc));
		capedPiece = getPiece(rankSmallestCapLoc);
		assert (capedPiece != 0);
		if (!Board.isPiecesSameSide(capedPiece, myKing)) {
			int pieceType = Board.getPieceType(capedPiece);
			if (pieceType == Board.CANNON_MASK) {
				return true;
			}
		}
		int fileBiggestCapLoc = 
				MoveTable.getFileBiggestCannonCap(myKingLoc, getFileBit(Board.getFile(myKingLoc)));
		assert (Board.inBoard(fileBiggestCapLoc));
		assert (Board.getFile(fileBiggestCapLoc) == Board.getFile(myKingLoc));
		capedPiece = getPiece(fileBiggestCapLoc);
		assert (capedPiece != 0);
		if (!Board.isPiecesSameSide(capedPiece, myKing)) {
			int pieceType = Board.getPieceType(capedPiece);
			if (pieceType == Board.CANNON_MASK) {
				return true;
			}
		}
		int fileSmallestCapLoc = 
				MoveTable.getFileSmallestCannonCap(myKingLoc, getFileBit(Board.getFile(myKingLoc)));
		assert (Board.inBoard(fileSmallestCapLoc));
		assert (Board.getFile(fileSmallestCapLoc) == Board.getFile(myKingLoc));
		capedPiece = getPiece(fileSmallestCapLoc);
		assert (capedPiece != 0);
		if (!Board.isPiecesSameSide(capedPiece, myKing)) {
			int pieceType = Board.getPieceType(capedPiece);
			if (pieceType == Board.CANNON_MASK) {
				return true;
			}
		}
		return false;
	}
	private boolean isCheckedByKnight(int myKing, int myKingLoc) {
		// 把帅看作马，检查走日字后的坐标有没有马，若有马且马走到帅不蹩腿，则将军
		for (int id = 0; id < 8; id ++) {
			int capedPieceLoc = myKingLoc + Board.KNIGHT_STEP[id];
			if (!Board.inBoard(capedPieceLoc)) {
				continue;
			}
			int capedPiece = getPiece(capedPieceLoc);
			if (capedPiece != 0 &&
					!Board.isPiecesSameSide(capedPiece, myKing) &&
					Board.getPieceType(capedPiece) == Board.KNIGHT_MASK) {
				assert (Board.getPieceType(capedPiece) == Board.KNIGHT_MASK);
				int knightPin = MoveTable.getKnightPin(capedPieceLoc, myKingLoc);
				assert (knightPin != 0);
//				int reverseId = Board.getKnightReverseStepId(id);
//				if (Board.getPieceType
//							(getPiece(MoveTable.knightMoveTable
//							[capedPieceLoc][reverseId])) != Board.KING_MASK) {
//					System.out.println("knight piece id: " + capedPiece);
//					System.out.println("side: " + Board.getPieceSide(capedPiece));
//					System.out.println("knight loc: " + capedPieceLoc);
//					System.out.println("loc rank: " + (Board.getRank(capedPieceLoc) - Board.RANK_TOP));
//					System.out.println("loc file: " + (Board.getFile(capedPieceLoc) - Board.FILE_LEFT));
//					
//					System.out.println("King piece id:" + myKing);
//					System.out.println("side: " + Board.getPieceSide(myKing));
//					System.out.println("King loc:" + myKingLoc);
//					System.out.println("loc rank: " + (Board.getRank(myKingLoc) - Board.RANK_TOP));
//					System.out.println("loc file: " + (Board.getFile(myKingLoc) - Board.FILE_LEFT));
//					
//					System.out.println("Step id: " + id);
//					System.out.println("Step Shift:" + Board.KNIGHT_STEP[id]);
//					System.out.println("Reverse step id: " + reverseId);
//					System.out.println("Reverse set shift: " + Board.KNIGHT_STEP[reverseId]);
//					
//					System.out.println("Table step to loc: " + MoveTable.knightMoveTable[capedPieceLoc][reverseId]);
//					System.out.println("loc rank: " + (Board.getRank(MoveTable.knightMoveTable
//							[capedPieceLoc][reverseId]) - Board.RANK_TOP));
//					System.out.println("loc file:" + (Board.getFile(MoveTable.knightMoveTable
//							[capedPieceLoc][reverseId]) - Board.FILE_LEFT));
//				}
//				assert (Board.getPieceType
//							(getPiece(MoveTable.knightMoveTable
//							[capedPieceLoc][reverseId])) == Board.KING_MASK);
				if (getPiece(knightPin) != 0) {
					return true;
				}
			}
		}
		return false;
	}
	private boolean isCheckedByPawn(int myKing, int myKingLoc) {
		// 在帅的攻击范围内的卒不是己方中兵就一定是对方过河卒
		int capedPiece;
		int capedPieceLoc;
		if (Board.getPieceSide(myKing) == Board.RED_SIDE) {
			capedPieceLoc = myKingLoc - Board.rankDis(1);
			capedPiece = getPiece(capedPieceLoc);
			assert (Board.inBoard(capedPieceLoc));
		} else {
			capedPieceLoc = myKingLoc + Board.rankDis(1);
			capedPiece = getPiece(capedPieceLoc);
			assert (Board.inBoard(capedPieceLoc));
		}
		if (Board.getPieceType(capedPiece) == Board.PAWN_MASK) {
			assert (capedPiece != 0);
//			if (Board.isPiecesSameSide(myKing, capedPiece)) {
//				System.out.print("In chess.AI.Position.isCheckedByPawn: ");
//				System.out.println("King: " + myKing + " Pawn: " + capedPiece);
//				System.out.print(" KingLoc: x: " + (Board.getRank(myKingLoc) - Board.RANK_TOP) + 
//						" y: " + (Board.getFile(myKingLoc) - Board.FILE_LEFT));
//				System.out.println(" PawnLoc: x: " + (Board.getRank(capedPieceLoc) - Board.RANK_TOP) + 
//						" y: " + (Board.getFile(capedPieceLoc) - Board.FILE_LEFT));
//			}
			assert ((Board.getRank(capedPieceLoc) == 6 + Board.RANK_TOP && Board.getFile(capedPieceLoc) == 4 + Board.FILE_LEFT) || 
					(Board.getRank(capedPieceLoc) == 3 + Board.RANK_TOP && Board.getFile(capedPieceLoc) == 4 + Board.FILE_LEFT) ||
					!Board.isPiecesSameSide(myKing, capedPiece));
			return true;
		}
		for (int i = -1; i <= 1; i += 2) {
			capedPieceLoc = myKingLoc + i;
			capedPiece = getPiece(capedPieceLoc);
			if (Board.getPieceType(capedPiece) == Board.PAWN_MASK) {
				assert (capedPiece != 0);
				assert (!Board.isPiecesSameSide(myKing, capedPiece));
				return true;
			}
		}
		return false;
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
		// 修改Zobrist键值
		zobrist.xor(Zobrist.Z_TABLE[Board.getPieceSide(piece)][Board.getPieceType(piece)][loc]);
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
		// 修改Zobrist键值
		zobrist.xor(Zobrist.Z_TABLE[Board.getPieceSide(piece)][Board.getPieceType(piece)][loc]);
		// 减少评估值
		evaluationVal[Board.getPieceSide(piece)] -=
				Evaluator.getPieceVal(piece, loc);
		assert (evaluationVal[Board.BLACK_SIDE] >= 0);
		assert (evaluationVal[Board.RED_SIDE] >= 0);
	}
	private void changeSide() {
		isRedMove = !isRedMove;
		// 修改Zobrist键值
		zobrist.xor(Zobrist.Z_SIDE);
	}
	
	// getter & setter
	public boolean isRedMove() {
		return isRedMove;
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
	public int getRedVal() {
		return evaluationVal[Board.RED_SIDE];
	}
	public int getBlackVal() {
		return evaluationVal[Board.BLACK_SIDE];
	}
	public Zobrist getZobrist() {
		return zobrist;
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
