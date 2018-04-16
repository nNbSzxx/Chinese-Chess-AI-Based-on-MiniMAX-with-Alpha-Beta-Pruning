package chess.AI;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author nNbS
 * 
 *   This class is used to define some important constants
 * that helps describe the Chinese Chess board and pieces.
 * 
 */
public final class Board {
	// 类变量开始
	// 判断棋子是否在棋盘内
	private static final int IN_BOARD[] = {
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};
	// 判断棋子是否在九宫格内
	private static final int IN_FORT[] = {
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};
	
	// 存储棋盘大小
	public static final int BOARD_SIZE = 256; 
	// 实际棋盘大小
	public static final int REAL_BOARD_SIZE = 90; 
	// 存储棋盘列数
	public static final int MAX_FILE = 16; 
	// 存储棋盘行数
	public static final int MAX_RANK = 16;
	// 真实棋盘列数
	public static final int REAL_MAX_FILE = 9;
	// 真实棋盘行数
	public static final int REAL_MAX_RANK = 10;
	// 最大棋子数
	public static final int MAX_PIECE = 32;
	// 棋子最大编号上界:
	public static final int UPPER_BOUND_PIECE_ID = (1 << 7);
	
	// 边界留白信息
	public static final int FILE_LEFT = 3; 
	public static final int FILE_RIGHT = 12;
	public static final int RANK_TOP = 3;
	public static final int RANK_BOTTOM = 13;
	
	// 红黑方编号
	public static final int RED_SIDE = 1;
	public static final int BLACK_SIDE = 0; 
	
	// 掩码定义
	// 红方棋子第4位为1，黑方为0
	public static final int PIECE_SIDE_MASK = 0x08;
	// 红方棋盘半场坐标第8位为1，黑方为0
	public static final int INDEX_SIDE_MASK = 0x80;
	// 棋子类型掩码，低3位
	public static final int PIECE_TYPE_MASK = 0x07;
	// 坐标所在列的掩码
	public static final int FILE_MASK = 0x0f;
	// 坐标行所在掩码
	public static final int RANK_MASK = 0xf0;
	
	// 代表棋子类型的编码，共7种，位于棋子编号低3位
	// 不从0开始，否则将有棋子的编号是0
	public static final int KING_MASK = 1;
	public static final int ADVISOR_MASK = 2;
	public static final int BISHOP_MASK = 3;
	public static final int KNIGHT_MASK = 4;
	public static final int ROOK_MASK = 5;
	public static final int CANNON_MASK = 6;
	public static final int PAWN_MASK = 7;
	
	// 棋子编号数组，第一维表示棋子颜色，0黑 1红，大小 2 * 16
	public static final int[][] PIECE_ARRAY;
	// 棋子编号
	public static final int RED_KING;
	public static final int RED_ADVISOR_1;
	public static final int RED_ADVISOR_2;
	public static final int RED_BISHOP_1;
	public static final int RED_BISHOP_2;
	public static final int RED_KNIGHT_1;
	public static final int RED_KNIGHT_2;
	public static final int RED_ROOK_1;
	public static final int RED_ROOK_2;
	public static final int RED_CANNON_1;
	public static final int RED_CANNON_2;
	public static final int RED_PAWN_1;
	public static final int RED_PAWN_2;
	public static final int RED_PAWN_3;
	public static final int RED_PAWN_4;
	public static final int RED_PAWN_5;
	
	public static final int BLACK_KING;
	public static final int BLACK_ADVISOR_1;
	public static final int BLACK_ADVISOR_2;
	public static final int BLACK_BISHOP_1;
	public static final int BLACK_BISHOP_2;
	public static final int BLACK_KNIGHT_1;
	public static final int BLACK_KNIGHT_2;
	public static final int BLACK_ROOK_1;
	public static final int BLACK_ROOK_2;
	public static final int BLACK_CANNON_1;
	public static final int BLACK_CANNON_2;
	public static final int BLACK_PAWN_1;
	public static final int BLACK_PAWN_2;
	public static final int BLACK_PAWN_3;
	public static final int BLACK_PAWN_4;
	public static final int BLACK_PAWN_5;
	
	static {
		// 棋子常量初始化
		// 棋子编号  该类第几个棋子 | 棋子颜色 | 棋子类型
		//           3位                      1位              3位
		RED_KING = 		((0 << 4) | PIECE_SIDE_MASK | KING_MASK);
		RED_ADVISOR_1 = ((0 << 4) | PIECE_SIDE_MASK | ADVISOR_MASK);
		RED_ADVISOR_2 = ((1 << 4) | PIECE_SIDE_MASK | ADVISOR_MASK);
		RED_BISHOP_1 = 	((0 << 4) | PIECE_SIDE_MASK | BISHOP_MASK);
		RED_BISHOP_2 = 	((1 << 4) | PIECE_SIDE_MASK | BISHOP_MASK);
		RED_KNIGHT_1 = 	((0 << 4) | PIECE_SIDE_MASK | KNIGHT_MASK);
		RED_KNIGHT_2 = 	((1 << 4) | PIECE_SIDE_MASK | KNIGHT_MASK);
		RED_ROOK_1 = 	((0 << 4) | PIECE_SIDE_MASK | ROOK_MASK);
		RED_ROOK_2 = 	((1 << 4) | PIECE_SIDE_MASK | ROOK_MASK);
		RED_CANNON_1 = 	((0 << 4) | PIECE_SIDE_MASK | CANNON_MASK);
		RED_CANNON_2 = 	((1 << 4) | PIECE_SIDE_MASK | CANNON_MASK);
		RED_PAWN_1 = 	((0 << 4) | PIECE_SIDE_MASK | PAWN_MASK);
		RED_PAWN_2 = 	((1 << 4) | PIECE_SIDE_MASK | PAWN_MASK);
		RED_PAWN_3 = 	((2 << 4) | PIECE_SIDE_MASK | PAWN_MASK);
		RED_PAWN_4 = 	((3 << 4) | PIECE_SIDE_MASK | PAWN_MASK);
		RED_PAWN_5 = 	((4 << 4) | PIECE_SIDE_MASK | PAWN_MASK);
		
		BLACK_KING = 		((0 << 4) | KING_MASK);
		BLACK_ADVISOR_1 =	((0 << 4) | ADVISOR_MASK);
		BLACK_ADVISOR_2 = 	((1 << 4) | ADVISOR_MASK);
		BLACK_BISHOP_1 = 	((0 << 4) | BISHOP_MASK);
		BLACK_BISHOP_2 = 	((1 << 4) | BISHOP_MASK);
		BLACK_KNIGHT_1 = 	((0 << 4) | KNIGHT_MASK);
		BLACK_KNIGHT_2 = 	((1 << 4) | KNIGHT_MASK);
		BLACK_ROOK_1 = 		((0 << 4) | ROOK_MASK);
		BLACK_ROOK_2 = 		((1 << 4) | ROOK_MASK);
		BLACK_CANNON_1 = 	((0 << 4) | CANNON_MASK);
		BLACK_CANNON_2 = 	((1 << 4) | CANNON_MASK);
		BLACK_PAWN_1 = 		((0 << 4) | PAWN_MASK);
		BLACK_PAWN_2 = 		((1 << 4) | PAWN_MASK);
		BLACK_PAWN_3 = 		((2 << 4) | PAWN_MASK);
		BLACK_PAWN_4 = 		((3 << 4) | PAWN_MASK);
		BLACK_PAWN_5 = 		((4 << 4) | PAWN_MASK);
		
		PIECE_ARRAY = 
			new int[][]{ 
				{BLACK_KING, BLACK_ADVISOR_1, BLACK_ADVISOR_2,
					BLACK_BISHOP_1, BLACK_BISHOP_2,
					BLACK_KNIGHT_1, BLACK_KNIGHT_2,
					BLACK_ROOK_1, BLACK_ROOK_2,
					BLACK_CANNON_1, BLACK_CANNON_2,
					BLACK_PAWN_1, BLACK_PAWN_2,
				BLACK_PAWN_3, BLACK_PAWN_4, BLACK_PAWN_5},
				{RED_KING, RED_ADVISOR_1, RED_ADVISOR_2, 
					RED_BISHOP_1, RED_BISHOP_2,
					RED_KNIGHT_1, RED_KNIGHT_2,
					RED_ROOK_1, RED_ROOK_2,
					RED_CANNON_1, RED_CANNON_2,
					RED_PAWN_1, RED_PAWN_2,
				RED_PAWN_3, RED_PAWN_4, RED_PAWN_5},
			};
		
			
	}
	
	public static final int KING_STEP[]    = {-0x10, -0x01, +0x01, +0x10};
	public static final int ADVISOR_STEP[] = {-0x11, -0x0f, +0x0f, +0x11};
	public static final int BISHOP_STEP[]  = {-0x22, -0x1e, +0x1e, +0x22};
	public static final int KNIGHT_STEP[]  = {-0x21, -0x1f, -0x12, -0x0e, +0x0e, +0x12, +0x1f, +0x21};
	
	/* the represent has been abandoned
	public static final int RED_KING = 1;
	public static final int RED_ADVISOR = 2;
	public static final int RED_BISHOP = 3;
	public static final int RED_KNIGHT = 4;
	public static final int RED_ROOK = 5;
	public static final int RED_CANNON = 6;
	public static final int RED_PAWN = 7;
	
	public static final int BLACK_KING = 8;
	public static final int BLACK_ADVISOR = 9;
	public static final int BLACK_BISHOP = 10;
	public static final int BLACK_KNIGHT = 11;
	public static final int BLACK_ROOK = 12;
	public static final int BLACK_CANNON = 13;
	public static final int BLACK_PAWN = 14;
	*/
	
	// 类变量结束
	
	// 类方法开始
	// 增加一行相当于增加16格
	public static boolean inBoard(int loc) {
		return (IN_BOARD[loc] == 1);
	}
	public static boolean inFort(int loc) {
		return (IN_FORT[loc] == 1);
	}
	public static int rankDis(int x) {
		return (x << 4);
	}
	public static int getRank(int loc) {
		return (loc >> 4);
	}
	public static int getFile(int loc) {
		return (loc & 0x0f);
	}
	public static int getPieceType(int piece) {
		return (piece & PIECE_TYPE_MASK);
	}
	public static int getPieceSide(int piece) {
		assert (((piece & PIECE_SIDE_MASK) >> 3) == RED_SIDE ||
				((piece & PIECE_SIDE_MASK) >> 3) == BLACK_SIDE);
		return ((piece & PIECE_SIDE_MASK) >> 3);
	}
	// 接收马从from到to的预处理招法下标，返回从to到from的预处理招法下标
	public static int getKnightReverseStepId(int id) {
		return 7 - id;
	}
	public static boolean isIndexSameHalf(int loc1, int loc2) {
		return (((loc1 ^ loc2) & INDEX_SIDE_MASK) == 0);
	}
	public static boolean isPiecesSameSide(int piece1, int piece2) {
		return (((piece1 ^ piece2) & PIECE_SIDE_MASK) == 0);
	}
	public static boolean isRedPiece(int piece) {
		return ((piece & PIECE_SIDE_MASK) != 0);
	}
	public static boolean isPieceInOwnHalf(int piece, int loc) {
		// 棋子掩码第4位，棋盘掩码第8位，注意移位
		return (((piece & PIECE_SIDE_MASK) << 4) ^ (loc & INDEX_SIDE_MASK)) == 0;
	}
	// 类方法结束

	// 用于测试的方法
	private static boolean isAllDiff() {
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < PIECE_ARRAY.length; i ++) {
			for (int j = 0; j < PIECE_ARRAY[i].length; j ++) {
				map.put(PIECE_ARRAY[i][j], 
						map.getOrDefault(PIECE_ARRAY[i][j], 0) + 1);
				if (map.get(PIECE_ARRAY[i][j]) > 1) {
					return false;
				}
			}
		}
		
		int cnt[] = new int[256];
		for (int i = 0; i < PIECE_ARRAY.length; i ++) {
			for (int j = 0; j < PIECE_ARRAY[i].length; j ++) {
				cnt[PIECE_ARRAY[i][j]] ++;
				if (cnt[PIECE_ARRAY[i][j]] > 1) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static boolean isAllLowerThanUpperBound() {
		for (int i = 0; i < PIECE_ARRAY.length; i ++) {
			for (int j = 0; j < PIECE_ARRAY[i].length; j ++) {
				if (PIECE_ARRAY[i][j] >= UPPER_BOUND_PIECE_ID) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static boolean isAllNotZero() {
		for (int i = 0; i < PIECE_ARRAY.length; i ++) {
			for (int j = 0; j < PIECE_ARRAY[i].length; j ++) {
				if (PIECE_ARRAY[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	private Board() {}
	
	public static void main(String args[]) {
//		assert false;
		assert (IN_BOARD.length == BOARD_SIZE);
		assert (IN_FORT.length == BOARD_SIZE);
		assert (PIECE_ARRAY.length == 2);
		assert (PIECE_ARRAY[0].length == (MAX_PIECE >> 1));
		assert (PIECE_ARRAY[1].length == (MAX_PIECE >> 1));
		assert (isAllDiff());
		assert (isAllLowerThanUpperBound());
		assert (isAllNotZero());
		assert (isPieceInOwnHalf(RED_PAWN_1, 0x80));
		System.out.println("BLACK_KING: " + BLACK_KING);
		System.out.println("No Assertions break in Board");
	}
}