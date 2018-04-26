package chess.AI;

import java.util.Random;


/**
 * 
 * @author nNbS
 *   Hash method used to get a position in transposition table.
 *   
 */
public final class Zobrist {
	// 存储双方每个棋子的hash键值
	public static final Zobrist[][] Z_TABLE = new Zobrist[2][Board.MAX_PIECE_MASK];
	// 相同局面，需要在hash值上体现出轮到哪方下棋
	public static final Zobrist Z_SIDE = new Zobrist();
	// 随机数发生器
	/* 
	 * THE MEANING OF UNIVERSE BLESS MY HASH FEW CONFLICTS !
	 * --------------------------------------------
	 * -         ---                --------      -
	 * -        ---               ------------    -
	 * -       ---    ---        ---        ---   -
	 * -      ---    ---        ---         ---   -
	 * -     ---    ---                    ---    -
	 * -    ---    ---                    ---     -
	 * -   ---    ---                   ---       -
	 * -  -------------------         ---         -
	 * -  -------------------       ---           -
	 * -         ---              ---             -
	 * -        ---              --------------   -
	 * -       ---              ----------------  -
	 * -                                          -
	 * --------------------------------------------
	 * DEVOTE THE REST OF MY LIFE TO YOU, 42! MERCY, MY LORD!
	 */
	private static Random random = new Random(42);
	
	static {
		Z_SIDE.key = random.nextLong();
		Z_SIDE.lock = random.nextLong();
		for (int side = 0; side < 2; ++ side) {
			assert (side == Board.RED_SIDE || side == Board.BLACK_SIDE);
			for (int pieceType = Board.KING_MASK; pieceType <= Board.PAWN_MASK; ++ pieceType) {
				Z_TABLE[side][pieceType].key = random.nextLong();
				Z_TABLE[side][pieceType].lock = random.nextLong();
			}
		}
	}
	
	private long key = 0;
	private long lock = 0;
	
	public Zobrist() {}
	public void xor(Zobrist rhs) {
		key ^= rhs.key;
		lock ^= rhs.lock;
	}
}
