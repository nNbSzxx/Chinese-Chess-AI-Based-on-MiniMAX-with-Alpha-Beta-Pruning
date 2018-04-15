package chess.view;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import chess.board.ChessBoard;
import chess.util.Step;

public class ChessView extends JFrame implements MouseListener{

	private static final long serialVersionUID = 1L;
	
	private PlayBoard board;
	
	private Container container;
	
	private ChessPiece pieces[][];
	
	protected ChessPiece from,to;	//用于棋子的移动
	protected ChessPiece last1,last2;	//上一次移动的棋子的位置
	
	protected boolean isRedTurn;
	
	protected Step step;
	
	protected ChessBoard chessBoard;
	
	
	public ChessView(){
		super("中国象棋");
		container = getContentPane();
		container.setLayout(null);
		
		board = new PlayBoard();
		
		from = null;
		to = null;
		last1 = null;
		last2 = null;
		
		
		this.addMouseListener(this);
		this.add(board);		
		this.setSize(700,800);
		this.setVisible(true);
		this.setLocation(600, 0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//初始化整个棋盘
	//坐标映射函数(y*64+32,x*62+33,50,50)
	public void initial() {
		pieces = new ChessPiece[11][10];
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 9; j++){
				pieces[i][j] = new ChessPiece(this,board,i,j);
				
			}
		Icon image;

		// 黑车
		image = new ImageIcon("pic/Bche.png");
		pieces[0][0].setAttribute("黑车", Color.black, image, 0, 0);
		board.add(pieces[0][0]);
		pieces[0][0].setBounds(32, 33, 50, 50);

		pieces[0][8].setAttribute("黑车", Color.black, image, 0, 8);
		board.add(pieces[0][8]);
		pieces[0][8].setBounds(544, 33, 50, 50);

		// 黑马
		image = new ImageIcon("pic/Bma.png");
		pieces[0][1].setAttribute("黑马", Color.black, image, 0, 1);
		board.add(pieces[0][1]);
		pieces[0][1].setBounds(96, 33, 50, 50);

		pieces[0][7].setAttribute("黑马", Color.black, image, 0, 7);
		board.add(pieces[0][7]);
		pieces[0][7].setBounds(480, 33, 50, 50);

		// 黑象
		image = new ImageIcon("pic/Bxiang.png");
		pieces[0][2].setAttribute("黑象", Color.black, image, 0, 2);
		board.add(pieces[0][2]);
		pieces[0][2].setBounds(160, 33, 50, 50);

		pieces[0][6].setAttribute("黑象", Color.black, image, 0, 6);
		board.add(pieces[0][6]);
		pieces[0][6].setBounds(416, 33, 50, 50);

		// 黑士
		image = new ImageIcon("pic/Bshi.png");
		pieces[0][3].setAttribute("黑士", Color.black, image, 0, 3);
		board.add(pieces[0][3]);
		pieces[0][3].setBounds(224, 33, 50, 50);

		pieces[0][5].setAttribute("黑士", Color.black, image, 0, 5);
		board.add(pieces[0][5]);
		pieces[0][5].setBounds(352, 33, 50, 50);

		// 将
		image = new ImageIcon("pic/Bjiang.png");
		pieces[0][4].setAttribute("将", Color.black, image, 0, 4);
		board.add(pieces[0][4]);
		pieces[0][4].setBounds(288, 33, 50, 50);
		
		//黑炮
		image = new ImageIcon("pic/Bpao.png");
		pieces[2][1].setAttribute("黑炮", Color.black, image, 2, 1);
		board.add(pieces[2][1]);
		pieces[2][1].setBounds(96, 157, 50, 50);
		
		pieces[2][7].setAttribute("黑炮", Color.black, image, 2,7);
		board.add(pieces[2][7]);
		pieces[2][7].setBounds(480, 157, 50, 50);
		
		//黑卒
		image = new ImageIcon("pic/Bbing.png");
		
		for(int i=0;i<10; i += 2){	
			pieces[3][i].setAttribute("黑卒", Color.black, image, 3, i);
			board.add(pieces[3][i]);
			pieces[3][i].setBounds(i*64+32, 219, 50, 50);
		}
		
		//红车
		image = new ImageIcon("pic/Rche.png");
		pieces[9][0].setAttribute("红车", Color.red, image, 9, 0);
		board.add(pieces[9][0]);
		pieces[9][0].setBounds(32, 591, 50, 50);
		
		pieces[9][8].setAttribute("红车", Color.red, image, 9, 8);
		board.add(pieces[9][8]);
		pieces[9][8].setBounds(544, 591, 50, 50);
		
		//红马
		image = new ImageIcon("pic/Rma.png");
		pieces[9][1].setAttribute("红马", Color.red, image, 9, 1);
		board.add(pieces[9][1]);
		pieces[9][1].setBounds(96, 591, 50, 50);
		
		pieces[9][7].setAttribute("红马", Color.red, image, 9, 7);
		board.add(pieces[9][7]);
		pieces[9][7].setBounds(480, 591, 50, 50);
		
		//红相
		image = new ImageIcon("pic/Rxiang.png");
		pieces[9][2].setAttribute("红相", Color.red, image, 9, 2);
		board.add(pieces[9][2]);
		pieces[9][2].setBounds(160, 591, 50, 50);
		
		pieces[9][6].setAttribute("红相", Color.red, image, 9, 6);
		board.add(pieces[9][6]);
		pieces[9][6].setBounds(416, 591, 50, 50);
		
		//红仕
		image = new ImageIcon("pic/Rshi.png");
		pieces[9][3].setAttribute("红仕", Color.red, image, 9, 3);
		board.add(pieces[9][3]);
		pieces[9][3].setBounds(224,591,50,50);
		
		pieces[9][5].setAttribute("红仕", Color.red, image, 9, 5);
		board.add(pieces[9][5]);
		pieces[9][5].setBounds(352, 591, 50, 50);
		
		//帅
		image = new ImageIcon("pic/Rshuai.png");
		pieces[9][4].setAttribute("帅", Color.red, image, 9, 4);
		board.add(pieces[9][4]);
		pieces[9][4].setBounds(288,591,50,50);
		
		//红炮
		image = new ImageIcon("pic/Rpao.png");
		pieces[7][1].setAttribute("红炮", Color.red, image, 7, 1);
		board.add(pieces[7][1]);
		pieces[7][1].setBounds(96, 467, 50, 50);
		
		pieces[7][7].setAttribute("红炮", Color.red, image, 7, 7);
		board.add(pieces[7][7]);
		pieces[7][7].setBounds(480, 467, 50, 50);
		
		//红兵
		image = new ImageIcon("pic/Rbing.png");
		for(int i=0;i<10;i+=2){
			pieces[6][i].setAttribute("红兵", Color.red, image, 6, i);
			board.add(pieces[6][i]);
			pieces[6][i].setBounds(i*64+32, 405, 50, 50);
		}
		
		//添加空的标签以便做事件的响应
		for(int i=0;i<10;i++)
			for(int j=0;j<9;j++){
				if(!pieces[i][j].isPiece){
					board.add(pieces[i][j]);
					pieces[i][j].setBounds(j*64+32, i*62+33, 50, 50);
				}
			}
	}

	public boolean isLegal(ChessPiece from, ChessPiece to) {
		//System.out.println(from.name + from.x + "  " + from.y + "  " + to.name + to.x + "  " + to.y);
		this.step = new Step(from.x, from.y, to.x, to.y);

		if (this.chessBoard.isMoveLegal(step)) {

			//更新ChessBoard类中的棋盘
			this.chessBoard.update(this.step);

			return true;
		} else
			return false;
	}
	
	
	//棋子的移动，将坐标转换成相应的形式        传递起点到终点的坐标
	public void move(int fromX,int fromY,int toX,int toY){
		ChessPiece f,t;
		f = pieces[fromX][fromY];
		t = pieces[toX][toY];
		move(f,t);	//调用处理移动的函数
	}
	
	//移动两个点
	public void move(ChessPiece from,ChessPiece to){
		board.remove(from);
		board.remove(to);
		int x1,y1,x2,y2;
		x1 = from.x;
		y1 = from.y;
		x2 = to.x;
		y2 = to.y;
		//将上一次显示的红色边框转为不显示
		if(last1!=null||last2!=null){
			last1.setBorder(null);
			last2.setBorder(null);
		}
		//将移动后的起始位置边框置为红色
		from.setBorder(BorderFactory.createLineBorder(Color.red));
		to.setBorder(BorderFactory.createLineBorder(Color.red));
		ChessPiece p;
		if(!to.isPiece){	//仅仅移动
			
			//交换数组中的两者
			p = pieces[to.x][to.y];
			pieces[x2][y2] = from;
			pieces[x2][y2].x  = x2;
			pieces[x2][y2].y = y2;
			pieces[x1][y1] = p;
			pieces[x1][y1].x = x1;
			pieces[x1][y1].y = y1;
			
			//交换棋盘中的两者	(y*64+32,x*62+33,50,50)
			board.add(pieces[x1][y1]);
			board.add(pieces[x2][y2]);
			pieces[x1][y1].setBounds(y1*64+32, x1*62+33, 50, 50);
			pieces[x2][y2].setBounds(y2*64+32, x2*62+33, 50, 50);
		}
		else{	//吃子
			
			//交换数组中的两者
			p = pieces[to.x][to.y];
			pieces[x2][y2] = from;
			pieces[x2][y2].x  = x2;
			pieces[x2][y2].y = y2;
			pieces[x1][y1] = p;
			pieces[x1][y1].x = x1;
			pieces[x1][y1].y = y1;
			//设置被吃的棋子的属性
			pieces[x1][y1].isPiece = false;
			pieces[x1][y1].setIcon(null);
			pieces[x1][y1].image = null;
			pieces[x1][y1].name = null;
			pieces[x1][y1].color = null;
			
			//交换棋盘中的两者	(y*64+32,x*62+33,50,50)
			board.add(pieces[x1][y1]);
			board.add(pieces[x2][y2]);
			pieces[x1][y1].setBounds(y1*64+32, x1*62+33, 50, 50);
			pieces[x2][y2].setBounds(y2*64+32, x2*62+33, 50, 50);
			
		}
		last1 = pieces[x1][y1];
		last2 = pieces[x2][y2];
		this.from = null;
		this.to = null;
		
		//到对方下棋
		this.isRedTurn = !this.isRedTurn;

	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("xx:" + e.getX() + "yy:"+e.getY());
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String args[]){
		ChessView c = new ChessView();
		c.initial();
		
		c.chessBoard = new ChessBoard();
		
		c.isRedTurn = true;
		
		while(true){	
			System.out.print("");
			if(!c.isRedTurn){
				System.out.println("In chess.view.ChessView: Make AI Move");
				c.step = c.chessBoard.AIMove();
				c.from = c.pieces[c.step.getFromX()][c.step.getFromY()];
				c.to = c.pieces[c.step.getToX()][c.step.getToY()];
				
				c.move(c.from, c.to);
			}	
			
		}
		
	}
	

}
