package chess.AI;

import java.util.HashMap;
import java.util.List;
import chess.board.ChessBoard;
import chess.piece.ChessPieces;
import chess.util.Step;

// Unused class
public class Searchable {
	int INF = 18888;
	//记录此位置的灵活性的数组
	int e_flexibility[][] = new int [10][9];
	
	//记录此位置被保护的信息
	int e_guard[][] = new int [10][9];
	
	//记录此位置被威胁的信息
	int e_attack[][] = new int [10][9];
	
	//记录此位置棋子的总价值
	int e_all[][] = new int[10][9];
	
	HashMap<String,Integer> BasicValues = new HashMap<String, Integer>();
	HashMap<String,Integer> MobilityValues = new HashMap<String, Integer>();
	public Searchable() {
		BasicValues.put("B_Advisor",250);
		BasicValues.put("B_King",0);
		BasicValues.put("B_Cannon",300);
		BasicValues.put("B_Elephant",250);
		BasicValues.put("B_Horse",300);
		BasicValues.put("B_Rook",500);
		BasicValues.put("B_Soldier",80);
		
		BasicValues.put("R_Advisor",250);
		BasicValues.put("R_King",0);
		BasicValues.put("R_Cannon",300);
		BasicValues.put("R_Elephant",250);
		BasicValues.put("R_Horse",300);
		BasicValues.put("R_Rook",500);
		BasicValues.put("R_Soldier",80);
		
		MobilityValues.put("B_Advisor",1);
		MobilityValues.put("B_King",0);
		MobilityValues.put("B_Cannon",6);
		MobilityValues.put("B_Elephant",1);
		MobilityValues.put("B_Horse",12);
		MobilityValues.put("B_Rook",6);
		MobilityValues.put("B_Soldier",15);
		
		MobilityValues.put("R_Advisor",1);
		MobilityValues.put("R_King",0);
		MobilityValues.put("R_Cannon",6);
		MobilityValues.put("R_Elephant",1);
		MobilityValues.put("R_Horse",12);
		MobilityValues.put("R_Rook",6);
		MobilityValues.put("R_Soldier",15);
		
		
		
	}
	
	
	public int evaluate(ChessPieces [][] board,ChessBoard now) {
		ChessPieces ChessType; // 当前棋子 from
		ChessPieces ChessTarget;// 目标棋子  to
		int nPosCnt; // 相关个数
		
		
		//第一次扫描找出棋子的基本价值，找出其威胁与保护的棋子，以及灵活性
		for(int i = 0 ; i < 10 ; i++)
			for(int j = 0 ; j < 9 ; j++) {
				if(board[i][j] != null) {  // 若此位置有棋子
					ChessType = board[i][j]; // 取此棋子的类型
					List<Step> relations = ChessType.getNextMove(now); // 得到与此棋子相关的所有位置
					nPosCnt = relations.size();
					for(int k = 0 ; k < nPosCnt; k++) { // 对每一个位置进行处理
						//System.out.println(ChessType.getName());
						//System.out.println(relations.get(k).getToX() + " " +relations.get(k).getToY());
						ChessTarget = board[relations.get(k).getToX()][relations.get(k).getToY()];//取目标位置类型
						if(ChessTarget == null) { // 若此位置类型为空， 增加灵活性
							e_flexibility[i][j]++;
						}else { 
							if(isSameSide(ChessTarget,ChessType)) {// 若两个棋子为同一方，收到保护
								e_guard[relations.get(k).getToX()][relations.get(k).getToY()]++; 
							}else { // 如果是对方棋子，则造成威胁
								e_attack[relations.get(k).getToX()][relations.get(k).getToY()]++;
								e_flexibility[relations.get(k).getToX()][relations.get(k).getToY()]++;  // 因为能吃掉那个棋子，灵活性增加
								
								//接下来讨论分为 产生将军 与 不产生将军 两种情况
								
								// 将军情况
								if(ChessTarget.getName() == "R_King") { 
									if(!now.isRedMove()) return INF;
								}
								else if(ChessTarget.getName() == "B_King") {
									if(now.isRedMove()) return INF;
								} else {  //非将军情况,根据威胁的棋子的基础价值加上威胁分值(根据两个棋子的价值之差)
									e_attack[relations.get(k).getToX()][relations.get(k).getToY()] +=  // 分值有待设计
											(30 + (BasicValues.get(ChessTarget.getName()) - BasicValues.get(ChessType.getName())) / 10) / 10;
								}		
							}
						}
					}
				}	
			}
		
		//第二次扫描 , 把棋子的灵活性价值与基本价值加到这个位置的总价值上面
		for(int i = 0 ; i < 10 ; i++)
			for(int j = 0 ; j < 9 ; j++) {
				if(board[i][j] != null) {
					ChessType = board[i][j];
					e_all[i][j] += BasicValues.get(ChessType.getName());
					e_all[i][j] += MobilityValues.get(ChessType.getName()) * e_flexibility[i][j];  // 需要在棋子里面添加返回灵活性价值的函数
				}
			}
		
		//第三次扫描， 处理威胁与保护
		
		int v;  // 定义一个变量v, 根据棋子的基本价值来决定v的实际取值
		for(int i = 0 ; i < 10 ; i++)
			for(int j = 0 ; j < 9 ; j++) {
				if(board[i][j] != null) { // 如果此处有棋子
					ChessType = board[i][j];
					v =  BasicValues.get(ChessType.getName()) / 16;// 此处的公式考虑棋子基本价值
					if(ChessType.isRed == true) { // 如果棋子是红方
						if(e_attack[i][j] >= 1) { // 如果受到了威胁
							if(now.isRedMove()) { // 如果轮到红方下棋
								if(ChessType.getName() == "R_King") {// 如果是红将受到威胁
									e_all[i][j] -= 20; // 将军受到威胁需要减的分数
								} else { // 如果是红色方其他的棋子受到威胁
									e_all[i][j] -= 2 * v;
									if(e_guard[i][j] >= 1) { // 如果收到威胁的同时又收到了保护，加分
										e_all[i][j] += v;
									}
								}
							}else { // 如果是黑方下棋，此时是红棋被威胁，黑棋可以走
								if(ChessType.getName() == "R_King") {// 如果将军了，直接结束
									return INF;
								}else { // 如果收到威胁的是其他的棋子
									e_all[i][j] -= v * 10; // 与v有关
									if(e_guard[i][j] >= 1) {// 如果被保护
										e_all[i][j] += v * 9; 
									}
								}
							}
						} else { // 如果没有受到威胁
							if(e_guard[i][j] >= 1) { // 如果收到保护
								e_all[i][j] += 5; // 此处与v和棋子有关
							}
						}
					} else { // 如果棋子是黑棋
						if(e_attack[i][j] >= 1) { // 如果收到威胁
							if(now.isRedMove()) { // 如果轮到黑棋走
								if(ChessType.getName() == "B_King") {//如果是黑将
									e_all[i][j] -= 20;
								} else { // 
									e_all[i][j] -= v * 2;
									if(e_guard[i][j] >= 1) { // 如果收到威胁的同时又收到了保护，加分
										e_all[i][j] += v;
									}
								}
							} else { // 如果轮到红棋走
								if(ChessType.getName() == "B_King") {// 如果是黑将
									return INF;
								} else {// 如果收到威胁的是其他的棋子
									e_all[i][j] -= v * 10;
									if(e_guard[i][j] >= 1) {// 如果被保护
										e_all[i][j] += v * 9; 
									}
								}	
							}
						} else { // 如果没有受到威胁
							if(e_guard[i][j] >= 1) { // 如果收到保护
								e_all[i][j] += 5;
							}	
						}
					}
				}	
			}
		
		
		
		//统计总分
		int R_Val = 0;
		int B_Val = 0;
		for(int i = 0 ; i < 10 ; i++)
			for(int j = 0 ; j < 9 ; j++) {
				ChessType = board[i][j];
				if(ChessType != null) {
					if(ChessType.isRed == true) { // 如果是红棋子
						R_Val += e_all[i][j];
					} else {
						B_Val += e_all[i][j];
					}
				}
			}
		
		// 返回总分
		if(now.isRedMove()) return (R_Val - B_Val);
		else return -(R_Val - B_Val);
	}


	private boolean isSameSide(ChessPieces pieces1, ChessPieces pieces2) {
		// TODO Auto-generated method stub
		return pieces1.isRed == pieces2.isRed;
	}
	
	
//	Step[] generateAllNextState(ChessBoard board);
//	boolean isEndState(ChessBoard board);
}

