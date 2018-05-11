package chess.AI;

/**
 * 
 * @author nNbS
 * 
 *   StraightMoveLoc is used to define the basic table type
 * that store information which Rook & Cannon need to pre-generate
 * legal steps.
 * 
 *   MoveTable is the structure stores all pre-generated legal steps.
 *
 */
final class StraightMoveLoc {
	// Attention: 以下存储的都是存储棋盘（16 * 16）中所能到达的行数和列数
	// 车炮不吃子能走到的最大一格/最小一格
	private int biggestNonCap;
	private int smallestNonCap;
	// 车吃子能走到的最大一格/最小一格
	private int biggestRookCap;
	private int smallestRookCap;
	// 炮吃子能走到的最大一格/最小一格
	private int biggestCannonCap;
	private int smallestCannonCap;
	public StraightMoveLoc() {}
	public StraightMoveLoc(int ini) {
		this.biggestNonCap = this.smallestNonCap =
				this.biggestRookCap = this.smallestRookCap = 
				this.biggestCannonCap = this.smallestCannonCap = ini;
	}
	public int getBiggestNonCap() {
		return biggestNonCap;
	}
	public int getSmallestNonCap() {
		return smallestNonCap;
	}
	public int getBiggestRookCap() {
		return biggestRookCap;
	}
	public int getSmallestRookCap() {
		return smallestRookCap;
	}
	public int getBiggestCannonCap() {
		return biggestCannonCap;
	}
	public int getSmallestCannonCap() {
		return smallestCannonCap;
	}
	public void setBiggestNonCap(int biggestNonCap) {
		this.biggestNonCap = biggestNonCap;
	}
	public void setSmallestNonCap(int smallestNonCap) {
		this.smallestNonCap = smallestNonCap;
	}
	public void setBiggestRookCap(int biggestRookCap) {
		this.biggestRookCap = biggestRookCap;
	}
	public void setSmallestRookCap(int smallestRookCap) {
		this.smallestRookCap = smallestRookCap;
	}
	public void setBiggestCannonCap(int biggestCannonCap) {
		this.biggestCannonCap = biggestCannonCap;
	}
	public void setSmallestCannonCap(int smallestCannonCap) {
		this.smallestCannonCap = smallestCannonCap;
	}
	
}; 

public final class MoveTable {
	// Attention: rankTable 中保存列信息，存储在低四位，or rankDis(棋子所在存储行号) 就是棋子目的地格子序号
	// Attention: fileTable 中保存行信息，存储在高四位，or (棋子所在存储列号) 就是棋子目的地格子序号
	
	// Attention: 压位的二进制串，低位表示棋盘左边（列号小），高位表示棋盘右边（列号大）
	// 车炮横向走位表
	private static StraightMoveLoc[][] rankMoveTable = 
			new StraightMoveLoc[Board.REAL_MAX_FILE][(1 << Board.REAL_MAX_FILE)];   
	// 获取车炮预处理招法，loc为棋子所在格子的编号，rankBit为所在行的位串
	public static int getRankSmallestNonCap (int loc, int rankBit) {
		int pieceFile = Board.getFile(loc) - Board.FILE_LEFT;
		return (loc & Board.RANK_MASK) | rankMoveTable[pieceFile][rankBit].getSmallestNonCap();
	}
	public static int getRankBiggestNonCap (int loc, int rankBit) {
		int pieceFile = Board.getFile(loc) - Board.FILE_LEFT;
		return (loc & Board.RANK_MASK) | rankMoveTable[pieceFile][rankBit].getBiggestNonCap();
	}
	public static int getRankSmallestRookCap (int loc, int rankBit) {
		int pieceFile = Board.getFile(loc) - Board.FILE_LEFT;
		return (loc & Board.RANK_MASK) | rankMoveTable[pieceFile][rankBit].getSmallestRookCap();
	}
	public static int getRankBiggestRookCap (int loc, int rankBit) {
		int pieceFile = Board.getFile(loc) - Board.FILE_LEFT;
		return (loc & Board.RANK_MASK) | rankMoveTable[pieceFile][rankBit].getBiggestRookCap();
	}
	public static int getRankSmallestCannonCap (int loc, int rankBit) {
		int pieceFile = Board.getFile(loc) - Board.FILE_LEFT;
		return (loc & Board.RANK_MASK) | rankMoveTable[pieceFile][rankBit].getSmallestCannonCap();
	}
	public static int getRankBiggestCannonCap (int loc, int rankBit) {
		int pieceFile = Board.getFile(loc) - Board.FILE_LEFT;
		return (loc & Board.RANK_MASK) | rankMoveTable[pieceFile][rankBit].getBiggestCannonCap();
	}
	// Attention: 压位的二进制串，低位表示棋盘上边边（行号小），高位表示棋盘下边（行号大）
	// 车炮纵向走位表
	private static StraightMoveLoc[][] fileMoveTable = 
			new StraightMoveLoc[Board.REAL_MAX_RANK][(1 << Board.REAL_MAX_RANK)]; 
	// 获取车炮预处理招法，loc为棋子所在格子的编号，fileBit为所在列的位串
	public static int getFileSmallestNonCap (int loc, int fileBit) {
		int pieceRank = Board.getRank(loc) - Board.RANK_TOP;
		return (loc & Board.FILE_MASK) | fileMoveTable[pieceRank][fileBit].getSmallestNonCap();
	}
	public static int getFileBiggestNonCap (int loc, int fileBit) {
		int pieceRank = Board.getRank(loc) - Board.RANK_TOP;
		return (loc & Board.FILE_MASK) | fileMoveTable[pieceRank][fileBit].getBiggestNonCap();
	}
	public static int getFileSmallestRookCap (int loc, int fileBit) {
		int pieceRank = Board.getRank(loc) - Board.RANK_TOP;
		return (loc & Board.FILE_MASK) | fileMoveTable[pieceRank][fileBit].getSmallestRookCap();
	}
	public static int getFileBiggestRookCap (int loc, int fileBit) {
		int pieceRank = Board.getRank(loc) - Board.RANK_TOP;
		return (loc & Board.FILE_MASK) | fileMoveTable[pieceRank][fileBit].getBiggestRookCap();
	}
	public static int getFileSmallestCannonCap (int loc, int fileBit) {
		int pieceRank = Board.getRank(loc) - Board.RANK_TOP;
		return (loc & Board.FILE_MASK) | fileMoveTable[pieceRank][fileBit].getSmallestCannonCap();
	}
	public static int getFileBiggestCannonCap (int loc, int fileBit) {
		int pieceRank = Board.getRank(loc) - Board.RANK_TOP;
		return (loc & Board.FILE_MASK) | fileMoveTable[pieceRank][fileBit].getBiggestCannonCap();
	}
	
	// Pin表示蹩腿表，以0表示招法表结尾
	public static final int kingMoveTable[][] = new int[Board.BOARD_SIZE][8];
	public static final int advisorMoveTable[][] = new int[Board.BOARD_SIZE][8];
	public static final int bishopMoveTable[][] = new int[Board.BOARD_SIZE][8];
	public static final int bishopPinTable[][] = new int[Board.BOARD_SIZE][4];
	public static final int knightMoveTable[][] = new int[Board.BOARD_SIZE][12];
	public static final int knightPinTable[][] = new int[Board.BOARD_SIZE][8];
	// 注意，第一维为0表示黑方，为1表示红方
	public static final int pawnMoveTable[][][] = new int[2][Board.BOARD_SIZE][4];
	
	// 棋盘上每个格子在当前行所在的列的位置，以位串形式表示
	public static final int rankMask[] = new int[Board.BOARD_SIZE];
	// 棋盘上每个格子在当前列所在的行的位置，以位串形式表示
	public static final int fileMask[] = new int[Board.BOARD_SIZE];
	// 根据from和to坐标给出马腿，马腿表
	private static final int KNIGHT_PIN_TABLE[] = new int[]{
									0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,-16,  0,-16,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0, -1,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0, -1,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0, 16,  0, 16,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0
	};
	static {
		makeStraightPieceTable();
		makeOtherPieceTable();
		makeMaskTable();
//		assert false : "test assertion";
		System.out.println("Move Table has generated");
	}
	
	private static void makeStraightPieceTable() {
		// 首先生成行表
		int limit = (1 << Board.REAL_MAX_FILE);
		
		// 外层循环当前车炮所在该行的位置
		for (int i = 0; i < Board.REAL_MAX_FILE; i ++) {
			// 内层循环该行的棋子状况，压位
		    for (int j = 0; j < limit; j ++) {
		    	// 枚举到该行第i列并没有棋子，跳过
		    	if ((j & (1 << i)) == 0) {
		    		continue;
		    	}
		    	// 初始化， 假设都没有合法棋步，初始化为自己当前位置
		    	StraightMoveLoc moveLoc = new StraightMoveLoc(i + Board.FILE_LEFT);
		    	// 向右移动，填充biggest
		    	// 首先是车炮不吃子和车吃子的招法
		    	int k;
		    	for (k = i + 1; k < Board.REAL_MAX_FILE; k ++) {
		    		if ((j & (1 << k)) != 0) {
		    			moveLoc.setBiggestRookCap(k + Board.FILE_LEFT);
		    			break;
		    		}
		    		moveLoc.setBiggestNonCap(k + Board.FILE_LEFT);
		    	}
		    	// 炮吃子的走法
		    	for (k ++; k < Board.REAL_MAX_FILE; k ++) {
		    		if ((j & (1 << k)) != 0) {
		    			moveLoc.setBiggestCannonCap(k + Board.FILE_LEFT);
		    			break;
		    		}
		    	}
		    	
		    	// 向左移动，填充smallest，原理同上
		    	for (k = i - 1; k >= 0; k --) {
		    		if ((j & (1 << k)) != 0) {
		    			moveLoc.setSmallestRookCap(k + Board.FILE_LEFT);
		    			break;
		    		}
		    		moveLoc.setSmallestNonCap(k + Board.FILE_LEFT);
		    	}
		    	for (k --; k >= 0; k --) {
		    		if ((j & (1 << k)) != 0) {
		    			moveLoc.setSmallestCannonCap(k + Board.FILE_LEFT);
		    			break;
		    		}
		    	}
		        
		    	// 断言
		    	assert (Board.FILE_LEFT <= moveLoc.getBiggestNonCap()) : "out of left bound";
		    	assert (Board.FILE_LEFT <= moveLoc.getBiggestRookCap()) : "out of left bound";
		    	assert (Board.FILE_LEFT <= moveLoc.getBiggestCannonCap()) : "out of left bound";
		    	assert (Board.FILE_LEFT <= moveLoc.getSmallestNonCap()) : "out of left bound";
		    	assert (Board.FILE_LEFT <= moveLoc.getSmallestRookCap()) : "out of left bound";
		    	assert (Board.FILE_LEFT <= moveLoc.getSmallestCannonCap()) : "out of left bound";
		    	assert (moveLoc.getBiggestNonCap() < Board.FILE_RIGHT) : "out of right bound";
		    	assert (moveLoc.getBiggestRookCap() < Board.FILE_RIGHT) : "out of right bound";
		    	assert (moveLoc.getBiggestCannonCap() < Board.FILE_RIGHT) : "out of right bound";
		    	assert (moveLoc.getSmallestNonCap() < Board.FILE_RIGHT) : "out of right bound";
		    	assert (moveLoc.getSmallestRookCap() < Board.FILE_RIGHT) : "out of right bound";
		    	assert (moveLoc.getSmallestCannonCap() < Board.FILE_RIGHT) : "out of right bound";

			    // 拷贝
			    rankMoveTable[i][j] = moveLoc;
		    } // for j
		} // for i
	
	
		// 然后生成列表
		limit = (1 << Board.REAL_MAX_RANK);
		// 外层循环车炮在该行的位置
		for (int i = 0; i < Board.REAL_MAX_RANK; i ++) {
			for (int j = 0; j < limit; j ++) {
				// 枚举到第i行没有棋子，跳过
				if ((j & (1 << i)) == 0) {
					continue;
				}
				// 初始化
				StraightMoveLoc moveLoc = 
						new StraightMoveLoc(Board.rankDis(i + Board.RANK_TOP));
				// 向下移动，填充biggest
				int k;
				for (k = i + 1; k < Board.REAL_MAX_RANK; k ++) {
					if ((j & (1 << k)) != 0) {
						moveLoc.setBiggestRookCap(
								Board.rankDis(k + Board.RANK_TOP));
						break;
					}
					moveLoc.setBiggestNonCap(
							Board.rankDis(k + Board.RANK_TOP));
				}
				for (k ++; k < Board.REAL_MAX_RANK; k ++) {
					if ((j & (1 << k)) != 0) {
						moveLoc.setBiggestCannonCap(
								Board.rankDis(k + Board.RANK_TOP));
						break;
					}
				}
		      
				// 向上移动，填充smallest
				for (k = i - 1; k >= 0; k --) {
					if ((j & (1 << k)) != 0) {
						moveLoc.setSmallestRookCap(
								Board.rankDis(k + Board.RANK_TOP));
						break;
					}
					moveLoc.setSmallestNonCap(
							Board.rankDis(k + Board.RANK_TOP));
				}
				for (k --; k >= 0; k --) {
					if ((j & (1 << k)) != 0) {
						moveLoc.setSmallestCannonCap(
								Board.rankDis(k + Board.RANK_TOP));
						break;
					}
				}
				// 断言
				assert (Board.rankDis(Board.RANK_TOP) <= moveLoc.getBiggestNonCap()) : "out of upper bound";
		    	assert (Board.rankDis(Board.RANK_TOP) <= moveLoc.getBiggestRookCap()) : "out of upper bound";
		    	assert (Board.rankDis(Board.RANK_TOP) <= moveLoc.getBiggestCannonCap()) : "out of upper bound";
		    	assert (Board.rankDis(Board.RANK_TOP) <= moveLoc.getSmallestNonCap()) : "out of upper bound";
		    	assert (Board.rankDis(Board.RANK_TOP) <= moveLoc.getSmallestRookCap()) : "out of upper bound";
		    	assert (Board.rankDis(Board.RANK_TOP) <= moveLoc.getSmallestCannonCap()) : "out of upper bound";
		    	assert (moveLoc.getBiggestNonCap() < Board.rankDis(Board.RANK_BOTTOM)) : "out of lower bound";
		    	assert (moveLoc.getBiggestRookCap() < Board.rankDis(Board.RANK_BOTTOM)) : "out of lower bound";
		    	assert (moveLoc.getBiggestCannonCap() < Board.rankDis(Board.RANK_BOTTOM)) : "out of lower bound";
		    	assert (moveLoc.getSmallestNonCap() < Board.rankDis(Board.RANK_BOTTOM)) : "out of lower bound";
		    	assert (moveLoc.getSmallestRookCap() < Board.rankDis(Board.RANK_BOTTOM)) : "out of lower bound";
		    	assert (moveLoc.getSmallestCannonCap() < Board.rankDis(Board.RANK_BOTTOM)) : "out of lower bound";

				// 拷贝
				fileMoveTable[i][j] = moveLoc;
		    } // for j
		} // for i
	} // makeStraightTable
	
	private static void makeOtherPieceTable() {
		// 接下来生成着法预生成数组，连同将军预判数组
		for (int from = 0; from < 256; from ++) {
		    if (Board.inBoard(from)) {
		    	if (Board.inFort(from)) {
			    	generateKingMove(from);
			    	generateAdvisorMove(from);
				}
		    	generateBishopMove(from);
		    	generateKnightMove(from);
		    	generatePawnMove(from);
		    }
		}
	}
	
	private static void makeMaskTable() {
		for (int i = 0; i < 256; i ++) {
		    if (Board.inBoard(i)) {
		    	rankMask[i] = 1 << (Board.getFile(i) - Board.FILE_LEFT);
		    	fileMask[i] = 1 << (Board.getRank(i) - Board.RANK_TOP);
		    } else {
		    	rankMask[i] = 0;
		    	fileMask[i] = 0;
		    }
		}
	}
	
	// 以下是各个棋子步法generate方法
	private static void generateKingMove(int from) {
		int cnt = 0;
    	for (int i = 0; i < 4; i ++) {
    		int to = from + Board.KING_STEP[i];
    		assert (isDisOne(from, to)) : "Distance More Than One!";
    		if (Board.inFort(to)) {
    			kingMoveTable[from][cnt] = to;
    			cnt ++;
    		}
    	}
    	assert (cnt <= 4) : "King's Available Move More Than 4!";
    	kingMoveTable[from][cnt] = 0;
	}
	private static void generateAdvisorMove(int from) {
		int cnt = 0;
    	for (int i = 0; i < 4; i ++) {
    		int to = from + Board.ADVISOR_STEP[i];
    		assert (isDisOneDia(from, to)) : "Wrong Advisor Move!";
    		if (Board.inFort(to)) {
    			advisorMoveTable[from][cnt] = to;
    			cnt ++;
    		}
    	}
    	assert (cnt <= 4) : "Advisor's Available Move More Than 4!";
    	advisorMoveTable[from][cnt] = 0;
	}
	private static void generateBishopMove(int from) {
		int cnt = 0;
    	for (int i = 0; i < 4; i ++) {
    		int to = from + Board.BISHOP_STEP[i];
    		assert (isDisTwoDia(from, to)) : "Wrong Bishop Move!";
    		if (Board.inBoard(to) && Board.isIndexSameHalf(from, to)) {
    			bishopMoveTable[from][cnt] = to;
    			bishopPinTable[from][cnt] = (from + to) >> 1;
    			assert (isDisOneDia(from, bishopPinTable[from][cnt])) : "Wrong Bishop Pin!";
    			cnt ++;
    		}
    	}
    	assert (cnt <= 4) : "Bishop's Available Move More Than 4!";
    	bishopMoveTable[from][cnt] = 0;
	}
	private static void generateKnightMove(int from) {
		int cnt = 0;
    	for (int i = 0; i < 8; i ++) {
    		int to = from + Board.KNIGHT_STEP[i];
    		assert (isDisKnight(from, to)) : "Wrong Knight Move!";
    		if (Board.inBoard(to)) {
    			knightMoveTable[from][cnt] = to;
    			knightPinTable[from][cnt] = getKnightPin(from, to);
    			assert (isDisOne(from, knightPinTable[from][cnt])) : "Wrong Knight Pin!";
    			cnt ++;
    		}
    	}
    	assert (cnt <= 8) : "Knight's Available Move More Than 8!";
    	knightMoveTable[from][cnt] = 0;
	}
	private static void generatePawnMove(int from) {
		for (int i = 0; i < 2; i ++) {
    		int cnt = 0;
    		int to = from + (i == Board.BLACK_SIDE ? Board.rankDis(1) : -Board.rankDis(1));
    		assert (isDisOne(from, to)) : "Wrong Pawn Move!";
    		if (Board.inBoard(to)) {
    			pawnMoveTable[i][from][cnt] = to;
    			cnt ++;
    		}
    		int piece = (i == Board.RED_SIDE? Board.RED_PAWN_1 : Board.BLACK_PAWN_1);
    		if (!Board.isPieceInOwnHalf(piece, from)) {
    			for (int j = -1; j <= 1; j += 2) {
    				to = from + j;
    				assert (isDisOne(from, to)) : "Wrong Pawn Move!";
    				if (Board.inBoard(to)) {
    					pawnMoveTable[i][from][cnt] = to;
    					cnt ++;
    				}
    			}
    		}
    		assert (cnt <= 3) : "Pawn's Available Move More Than 3!";
    		pawnMoveTable[i][from][cnt] = 0;
    	}
	}
	public static int getKnightPin(int from, int to) {
		int way1 = from + KNIGHT_PIN_TABLE[to - from + Board.BOARD_SIZE];
		int way2;
		if (Math.abs(from - to) == Board.rankDis(2) + 1 || 
				Math.abs(from - to) == Board.rankDis(2) - 1) {
			if (Board.getRank(from) - Board.getRank(to) == 2) {
				way2 = from - Board.rankDis(1);
			} else if (Board.getRank(from) - Board.getRank(to) == -2){
				way2 = from + Board.rankDis(1);
			} else {
				assert false;
				return -1;
			}
		} else if (Math.abs(from - to) == Board.rankDis(1) + 2 ||
				Math.abs(from - to) == Board.rankDis(1) - 2) {
			if (Board.getFile(from) - Board.getFile(to) == 2) {
				way2 =  from - 1;
			} else if (Board.getFile(from) - Board.getFile(to) == -2) {
				way2 =  from + 1;
			} else {
				assert false;
				return -1;
			}
		} else {
			assert (false) : "Wrong In Generate Knight Pin!";
			return -1;
		}
		assert (way1 == way2); 
		return way1;
	}
	// 以上使步法generate方法
	
	// 以下是用于断言检验的方法
	private static boolean isDisOne(int x, int y) {
		return (Math.abs(x - y) == 1 || (Math.abs(x - y) >> 4) == 1); 
	}
	private static boolean isDisOneDia(int x, int y) {
		return (Math.abs(x - y) == 0x0f || Math.abs(x - y) == 0x11);
	}
	private static boolean isDisTwoDia(int x, int y) {
		return (Math.abs(x - y) == 0x22 || Math.abs(x - y) == 0x1e);
	}
	private static boolean isDisKnight(int x, int y) {
		return (Math.abs(x - y) == 0x21 || Math.abs(x - y) == 0x1f ||
				Math.abs(x - y) == 0x12 || Math.abs(x - y) == 0x0e);
	}
	
	
	
	
	
	private MoveTable() {}
	
	
	
	// 测试
	public static void main(String[] args) {
//		MoveTable table = new MoveTable();
//		System.out.println("No Assertion activated");
//		int limit = 1 << Board.REAL_MAX_FILE;
		int rankBit = (1 << 0) + (1 << 7);
		int loc = (11 << 4) | (3);
		int minNonCapStep = getRankSmallestNonCap(loc, rankBit);
		int maxNonCapStep = getRankBiggestNonCap(loc, rankBit);
		int minRookCapStep = getRankSmallestRookCap(loc, rankBit);
		int maxRookCapStep = getRankBiggestRookCap(loc, rankBit);
		
		System.out.println("minNonCap: " + Board.getRank(minNonCapStep) + " " + Board.getFile(minNonCapStep));
		System.out.println("maxNonCap: " + Board.getRank(maxNonCapStep) + " " + Board.getFile(maxNonCapStep));
		System.out.println("minRookCapStep: " + Board.getRank(minRookCapStep) + " " + Board.getFile(minRookCapStep));
		System.out.println("maxRookCapStep: " + Board.getRank(maxRookCapStep) + " " + Board.getFile(maxRookCapStep));
	}
	
}


