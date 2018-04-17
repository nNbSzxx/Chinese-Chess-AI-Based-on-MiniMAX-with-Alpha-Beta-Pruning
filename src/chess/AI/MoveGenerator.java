package chess.AI;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author nNbS
 *   This class is used to generate legal moves according to
 * a specific Chinese Chess position.
 */
public final class MoveGenerator {
	// 注意：本类中我们使用一个int描述一个招法，低8位表示目的地坐标，
	// 高8位表示起点坐标，最高的16位为0
	// 从而节约搜索过程中递归调用产生的空间开销
	// 同时在历史表中也使用了这种方法记录招法
	
	public static final int FROMLOC_SHIFT = 8;
	// 取得起始坐标的掩码
	public static final int FROMLOC_MASK = 0xff00;
	// 取得终点坐标的掩码
	public static final int TOLOC_MASK = 0x00ff;
	// 用于存储当前吃子招法
	private static List<Integer> capMoves = new ArrayList<>(16);
	// 用于存储当前非吃子招法
	private static List<Integer> nonCapMoves = new ArrayList<>(64);
	
	// 从step中取得from与to坐标
	public static int getFromLoc(int step) {
		return ((step & FROMLOC_MASK) >> FROMLOC_SHIFT);
	}
	public static int getToLoc(int step) {
		return (step & TOLOC_MASK);
	}
	
	public static List<Integer> getAllMove(Position position) {
		List<Integer> list = new ArrayList<>(80);
		generateCapMove(position, true);
		generateNonCapMove(position, true);
		list.addAll(capMoves);
		list.addAll(nonCapMoves);
		return list;
	}
	
	// 注意！以下两个方法返回的都是类变量而不是类变量的副本！
	// 获得所有吃子招法，并根据参数决定是否将非吃子招法缓存
	public static List<Integer> getCapMove(Position position, boolean doCacheNonCapMove) {
		generateCapMove(position, doCacheNonCapMove);
		return capMoves;
	}
	// 获得所有非吃子招法，并根据参数决定是否清空nonCapMoves
	// 注意！只有时间上最近的一次调用是getCapMove，并且doCacheNonCapMove=true，不清空nonCapMoves才是正确的！
	public static List<Integer> getNonCapMove(Position position, boolean doUseNonCapCache) {
		generateNonCapMove(position, doUseNonCapCache);
		return nonCapMoves;
	}
	
	// 产生所有吃子招法，确保吃子都是吃的对方的子，但不确保走棋之后不被将军，将帅对脸留待搜索时检查
	private static void generateCapMove(Position position, boolean doCacheNonCapMove) {
		// 每次生成吃子招法时都要清空，否则会包含上次查询的结果
		capMoves.clear();
		// 如果要缓存非吃子招法，那么也要清空非吃子招法的list
		if (doCacheNonCapMove) {
			nonCapMoves.clear();
		}
		assert (capMoves.size() == 0);
		assert (!doCacheNonCapMove || nonCapMoves.size() == 0);
		int side = (position.isRedMove() ? Board.RED_SIDE : Board.BLACK_SIDE);
		
		for (int i = 0; i < Board.PIECE_ARRAY[side].length; ++ i) {
			int piece = Board.PIECE_ARRAY[side][i];
			assert (Board.getPieceSide(piece) == side);
			int from = position.getPieceLoc(piece);
			if (from == 0) {
				continue;
			}
			int pieceType = Board.getPieceType(piece);
			
			switch (pieceType) {
			
			case Board.KING_MASK:
				generateKingCapMove(piece, from, position, doCacheNonCapMove);
				break;
				
			case Board.ROOK_MASK:
				generateRookCapMove(piece, from, position);
				break;
				
			case Board.CANNON_MASK:
				generateCannonCapMove(piece, from, position);
				break;
				
			case Board.KNIGHT_MASK:
				generateKnightCapMove(piece, from, position, doCacheNonCapMove);
				break;
				
			case Board.PAWN_MASK:
				generatePawnCapMove(side, piece, from, position, doCacheNonCapMove);
				break;
				
			case Board.BISHOP_MASK:
				generateBishopCapMove(piece, from, position, doCacheNonCapMove);
				break;
			
			case Board.ADVISOR_MASK:
				generateAdvisorCapMove(piece, from, position, doCacheNonCapMove);
				break;
			default:
				assert false;
				break;
			}
		}
	}
	
	// 产生所有非吃子招法，但不确保走棋之后不被将军，将帅对脸留待搜索时检查
	private static void generateNonCapMove(Position position, boolean doUseNonCapCache) {
		if (!doUseNonCapCache) {
			nonCapMoves.clear();
		}
		assert (doUseNonCapCache || nonCapMoves.size() == 0);
		int side = (position.isRedMove() ? Board.RED_SIDE : Board.BLACK_SIDE);
		
		for (int i = 0; i < Board.PIECE_ARRAY[side].length; ++ i) {
			int piece = Board.PIECE_ARRAY[side][i];
			assert (Board.getPieceSide(piece) == side);
			int from = position.getPieceLoc(piece);
			if (from == 0) {
				continue;
			}
			int pieceType = Board.getPieceType(piece);
			
			switch (pieceType) {
			
			case Board.KING_MASK:
				if (!doUseNonCapCache) {
					generateKingNonCapMove(piece, from, position);
				}
				break;
				
			case Board.KNIGHT_MASK:
				if (!doUseNonCapCache) {
					generateKnightNonCapMove(piece, from, position);
				}
				break;
				
			case Board.PAWN_MASK:
				if (!doUseNonCapCache) {
					generatePawnNonCapMove(side, piece, from, position);
				}
				break;
				
			case Board.BISHOP_MASK:
				if (!doUseNonCapCache) {
					generateBishopNonCapMove(piece, from, position);
				}
				break;
			
			case Board.ADVISOR_MASK:
				if (!doUseNonCapCache) {
					generateAdvisorNonCapMove(piece, from, position);
				}
				break;
			
			// 无论是否使用缓存，车炮的招法都是要生成的
			case Board.ROOK_MASK:
			case Board.CANNON_MASK:
				generateStraightNonCapMove(piece, from, position);
				break;	
			
			default:
				assert false;
				break;
			}
		}		
	}
	
	// 产生各个棋子吃子招法
	private static void generateKingCapMove(int piece, int from, Position position, boolean doCacheNonCapMove) {
		assert (Board.getPieceType(piece) == Board.KING_MASK);
		assert (Board.inFort(from));
		assert (Board.isPieceInOwnHalf(piece, from));
		for (int j = 0; MoveTable.kingMoveTable[from][j] != 0; j ++) {
			int to = MoveTable.kingMoveTable[from][j];
			assert (Board.inFort(to));
			assert (Board.isPieceInOwnHalf(piece, to));
			if (position.getPiece(to) != 0) {
				if (!Board.isPiecesSameSide(piece, position.getPiece(to))) {
					capMoves.add(makeStep(from, to));
				}
			} else if (doCacheNonCapMove) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
	}
	private static void generateRookCapMove(int piece, int from, Position position) {
		assert (Board.getPieceType(piece) == Board.ROOK_MASK);
		assert (Board.inBoard(from));
		// 行吃子
		int to = MoveTable.getRankSmallestRookCap(from, 
					position.getRankBit(Board.getRank(from)));
		assert (Board.inBoard(to));
		assert (Board.getRank(to) == Board.getRank(from));
		if (position.getPiece(to) != 0 &&
				!Board.isPiecesSameSide(piece, position.getPiece(to))) {
			capMoves.add(makeStep(from, to));
		}
		to = MoveTable.getRankBiggestRookCap(from, 
				position.getRankBit(Board.getRank(from)));
		assert (Board.inBoard(to));
		assert (Board.getRank(to) == Board.getRank(from));
		if (position.getPiece(to) != 0 &&
				!Board.isPiecesSameSide(piece, position.getPiece(to))) {
			capMoves.add(makeStep(from, to));
		}
		// 列吃子
		to = MoveTable.getFileSmallestRookCap(from, 
				position.getFileBit(Board.getFile(from)));
		assert (Board.inBoard(to));
		assert (Board.getFile(to) == Board.getFile(from));
		if (position.getPiece(to) != 0 &&
				!Board.isPiecesSameSide(piece, position.getPiece(to))) {
			capMoves.add(makeStep(from, to));
		}
		to = MoveTable.getFileBiggestRookCap(from, 
				position.getFileBit(Board.getFile(from)));
		assert (Board.inBoard(to));
		assert (Board.getFile(to) == Board.getFile(from));
		if (position.getPiece(to) != 0 &&
				!Board.isPiecesSameSide(piece, position.getPiece(to))) {
			capMoves.add(makeStep(from, to));
		}
	}
	private static void generateCannonCapMove(int piece, int from, Position position) {
		assert (Board.getPieceType(piece) == Board.CANNON_MASK);
		assert (Board.inBoard(from));
		// 行吃子
		int to = MoveTable.getRankSmallestCannonCap(from, 
				position.getRankBit(Board.getRank(from)));
		assert (Board.inBoard(to));
		assert (Board.getRank(to) == Board.getRank(from));
		if (position.getPiece(to) != 0 &&
				!Board.isPiecesSameSide(piece, position.getPiece(to))) {
			capMoves.add(makeStep(from, to));
		}
		to = MoveTable.getRankBiggestCannonCap(from, 
				position.getRankBit(Board.getRank(from)));
		assert (Board.inBoard(to));
		assert (Board.getRank(to) == Board.getRank(from));
		if (position.getPiece(to) != 0 &&
				!Board.isPiecesSameSide(piece, position.getPiece(to))) {
			capMoves.add(makeStep(from, to));
		}
		// 列吃子
		to = MoveTable.getFileSmallestCannonCap(from, 
				position.getFileBit(Board.getFile(from)));
		assert (Board.inBoard(to));
		assert (Board.getFile(to) == Board.getFile(from));
		if (position.getPiece(to) != 0 &&
				!Board.isPiecesSameSide(piece, position.getPiece(to))) {
			capMoves.add(makeStep(from, to));
		}
		to = MoveTable.getFileBiggestCannonCap(from, 
				position.getFileBit(Board.getFile(from)));
		assert (Board.inBoard(to));
		assert (Board.getFile(to) == Board.getFile(from));
		if (position.getPiece(to) != 0 &&
				!Board.isPiecesSameSide(piece, position.getPiece(to))) {
			capMoves.add(makeStep(from, to));
		}
	}
	private static void generateKnightCapMove(int piece, int from, Position position, boolean doCacheNonCapMove) {
		assert (Board.getPieceType(piece) == Board.KNIGHT_MASK);
		assert (Board.inBoard(from));
		for (int j = 0; MoveTable.knightMoveTable[from][j] != 0; j ++) {
			int to = MoveTable.knightMoveTable[from][j];
			int pin = MoveTable.knightPinTable[from][j];
			assert (Board.inBoard(to));
			assert (Board.inBoard(pin));
			if (position.getPiece(pin) == 0) {
				if (position.getPiece(to) != 0) {
					if (!Board.isPiecesSameSide(piece, position.getPiece(to))) {
						capMoves.add(makeStep(from, to));
					}
				} else if (doCacheNonCapMove) {
					nonCapMoves.add(makeStep(from, to));
				}
			}
		}
	}
	private static void generatePawnCapMove(int side, int piece, int from, Position position, boolean doCacheNonCapMove) {
		assert (Board.getPieceType(piece) == Board.PAWN_MASK);
		assert (side == Board.RED_SIDE || side == Board.BLACK_SIDE);
//		System.out.println("In chess.AI.MoveGenerator.generatePawnCapMove:");
//		System.out.println("The from loc is: " + from);
		assert (Board.inBoard(from));
		for (int j = 0; MoveTable.pawnMoveTable[side][from][j] != 0; j ++) {
			int to = MoveTable.pawnMoveTable[side][from][j];
			assert (Board.inBoard(to));
			// 黑卒，移动后行号不会减少
			assert (side == Board.RED_SIDE ||   (Board.getRank(from) <= Board.getRank(to)));
			// 红兵，移动后行号不会增加
			assert (side == Board.BLACK_SIDE || (Board.getRank(from) >= Board.getRank(to)));
			// 多于一步说明必然已经过河
			assert (j == 0 || !Board.isPieceInOwnHalf(piece, from));
			if (position.getPiece(to) != 0) {
				if (!Board.isPiecesSameSide(piece, position.getPiece(to))) {
					capMoves.add(makeStep(from, to));
				}
			} else if (doCacheNonCapMove) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
	}
	private static void generateBishopCapMove(int piece, int from, Position position, boolean doCacheNonCapMove) {
		assert (Board.getPieceType(piece) == Board.BISHOP_MASK);
		assert (Board.inBoard(from));
		assert (Board.isPieceInOwnHalf(piece, from));
		for (int j = 0; MoveTable.bishopMoveTable[from][j] != 0; j ++) {
			int to = MoveTable.bishopMoveTable[from][j];
			int pin = MoveTable.bishopPinTable[from][j];
			assert Board.inBoard(to);
			assert Board.inBoard(pin);
			assert Board.isPieceInOwnHalf(piece, to);
			if (position.getPiece(pin) == 0) {
				if (position.getPiece(to) != 0) {
					if (!Board.isPiecesSameSide(piece, position.getPiece(to))) {
						capMoves.add(makeStep(from, to));
					}
				} else if (doCacheNonCapMove) {
					nonCapMoves.add(makeStep(from, to));
				}
			}					
		}
	}
	private static void generateAdvisorCapMove(int piece, int from, Position position, boolean doCacheNonCapMove) {
		assert Board.getPieceType(piece) == Board.ADVISOR_MASK;
		assert Board.inFort(from);
		assert Board.isPieceInOwnHalf(piece, from);
		for (int j = 0; MoveTable.advisorMoveTable[from][j] != 0; j ++) {
			int to = MoveTable.advisorMoveTable[from][j];
			assert Board.inFort(to);
			assert Board.isPieceInOwnHalf(piece, to);
			if (position.getPiece(to) != 0) {
				if (!Board.isPiecesSameSide(piece, position.getPiece(to))) {
					capMoves.add(makeStep(from, to));
				}
			} else if (doCacheNonCapMove) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
	}
	
	// 产生各个棋子的非吃子招法
	private static void generateKingNonCapMove(int piece, int from, Position position) {
		assert Board.getPieceType(piece) == Board.KING_MASK;
		assert Board.inFort(from);
		assert Board.isPieceInOwnHalf(piece, from);
		for (int j = 0; MoveTable.kingMoveTable[from][j] != 0; j ++) {
			int to = MoveTable.kingMoveTable[from][j];
			assert (Board.inFort(to));
			assert (Board.isPieceInOwnHalf(piece, to));
			if (position.getPiece(to) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
	}
	private static void generateStraightNonCapMove(int piece, int from, Position position) {
		assert (Board.getPieceType(piece) == Board.ROOK_MASK || 
				Board.getPieceType(piece) == Board.CANNON_MASK);
		assert (Board.inBoard(from));
		// 行移动
		int to;
		for (to = from + 1; 
				to <= MoveTable.getRankBiggestNonCap(from, 
						position.getRankBit(Board.getRank(from))); to ++) {
			assert (Board.inBoard(to));
			assert (Board.getRank(to) == Board.getRank(from));
			if (position.getPiece(to) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
		for (to = from - 1;
				to >= MoveTable.getRankSmallestNonCap(from, 
						position.getRankBit(Board.getRank(from))); to --) {
			assert (Board.inBoard(to));
			assert (Board.getRank(to) == Board.getRank(from));
			if (position.getPiece(to) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
		// 列移动
		for (to = from + Board.rankDis(1);
				to <= MoveTable.getFileBiggestNonCap(from, 
						position.getFileBit(Board.getFile(from))); 
				to += Board.rankDis(1)) {
			assert (Board.inBoard(to));
			assert (Board.getFile(from) == Board.getFile(to));
			if (position.getPiece(to) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
		assert (to - Board.rankDis(1) == MoveTable.getFileBiggestNonCap(from, 
						position.getFileBit(Board.getFile(from))));
		for (to = from - Board.rankDis(1); 
				to >= MoveTable.getFileSmallestNonCap(from, 
						position.getFileBit(Board.getFile(from))); 
				to -= Board.rankDis(1)) {
			assert (Board.inBoard(to));
			assert (Board.getFile(from) == Board.getFile(to));
			if (position.getPiece(to) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
		assert (to + Board.rankDis(1) == MoveTable.getFileSmallestNonCap(from, 
						position.getFileBit(Board.getFile(from))));	
	}
	private static void generateKnightNonCapMove(int piece, int from, Position position) {
		assert (Board.getPieceType(piece) == Board.KNIGHT_MASK);
		assert (Board.inBoard(from));
		for (int j = 0; MoveTable.knightMoveTable[from][j] != 0; j ++) {
			int to = MoveTable.knightMoveTable[from][j];
			int pin = MoveTable.knightPinTable[from][j];
			assert (Board.inBoard(to));
			assert (Board.inBoard(pin));
			if (position.getPiece(to) == 0 &&
					position.getPiece(pin) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
	}
	private static void generatePawnNonCapMove(int side, int piece, int from, Position position) {
		assert (Board.getPieceType(piece) == Board.PAWN_MASK);
		assert (side == Board.RED_SIDE || side == Board.BLACK_SIDE);
		assert (Board.inBoard(from));
		for (int j = 0; MoveTable.pawnMoveTable[side][from][j] != 0; j ++) {
			int to = MoveTable.pawnMoveTable[side][from][j];
			assert (Board.inBoard(to));
			// 黑卒，移动后行号不会减少
			assert (side == Board.RED_SIDE ||   (Board.getRank(from) <= Board.getRank(to)));
			// 红兵，移动后行号不会增加
			assert (side == Board.BLACK_SIDE || (Board.getRank(from) >= Board.getRank(to)));
			// 多于一步说明必然已经过河
			assert (j == 0 || !Board.isPieceInOwnHalf(piece, from));
			if (position.getPiece(to) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
	}
	private static void generateBishopNonCapMove(int piece, int from, Position position) {
		assert (Board.getPieceType(piece) == Board.BISHOP_MASK);
		assert (Board.inBoard(from));
		assert (Board.isPieceInOwnHalf(piece, from));
		for (int j = 0; MoveTable.bishopMoveTable[from][j] != 0; j ++) {
			int to = MoveTable.bishopMoveTable[from][j];
			int pin = MoveTable.bishopPinTable[from][j];
			assert Board.inBoard(to);
			assert Board.inBoard(pin);
			assert Board.isPieceInOwnHalf(piece, to);
			if (position.getPiece(to) == 0 &&
					position.getPiece(pin) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
	}
	private static void generateAdvisorNonCapMove(int piece, int from, Position position) {
		assert Board.getPieceType(piece) == Board.ADVISOR_MASK;
		assert Board.inFort(from);
		assert Board.isPieceInOwnHalf(piece, from);
		for (int j = 0; MoveTable.advisorMoveTable[from][j] != 0; j ++) {
			int to = MoveTable.advisorMoveTable[from][j];
			assert Board.inFort(to);
			assert Board.isPieceInOwnHalf(piece, to);
			if (position.getPiece(to) == 0) {
				nonCapMoves.add(makeStep(from, to));
			}
		}
	}
	
	public static int makeStep(int from, int to) {
		return ((from << FROMLOC_SHIFT) | to);
	}
	
	private MoveGenerator() {}
	
	// test
	public static void main(String[] args) {
//		assert false;
	}
}
