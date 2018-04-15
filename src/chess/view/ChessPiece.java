package chess.view;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public class ChessPiece extends JLabel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Color color = null;		//棋子的字体颜色
	protected String name = null;	    //棋子的名称
	protected int x,y;			//棋子的坐标
	protected Icon image = null;		//棋子的图片
	protected boolean isPiece;	//标志该位置是否有棋子
	private ChessView view;
	private PlayBoard board;
	
	//构造方法，初始化棋子并设置棋子的参数
	public ChessPiece(String name,Color color,Icon image,int x,int y){
		//初始化棋子的属性
		super(image);
		this.color = color;
		this.name = name;
		this.image = image;
		this.x = x;
		this.y = y;		
		setSize(50,50);
		setVisible(true);
		
		
		//添加事件监听器
		this.addMouseListener(this);
		
	}
	//构造方法，传递这个画面的对象和棋盘的对象以便后面操作
	public ChessPiece(ChessView view, PlayBoard board,int x,int y){
		this.x = x;
		this.y = y;
		this.isPiece = false;
		this.setSize(50,50);
		this.addMouseListener(this);
		this.view = view;
		this.board = board;
		
	}
	//设置棋子的属性
	public void setAttribute(String name,Color color,Icon image,int x,int y){
		this.setIcon(image);
		this.color = color;
		this.name = name;
		this.image = image;
		this.x = x;
		this.y = y;	
		this.isPiece = true;
		this.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (view.isRedTurn) {	//到红方走棋
			//打印棋子的信息
			System.out.println(this.name + " " + this.x + " " + this.y + " " + this.color + " " + this.isPiece);

			if (view.from == null && this.isPiece && this.color.equals(Color.red)) { //选中己方棋子
				view.from = this;
				this.setBorder(BorderFactory.createLineBorder(Color.red));
			} else if (view.from != null && this.equals(view.from)) { //第二次点击取消选中棋子
				view.from = null;
				this.setBorder(null);
			}else if(view.from!=null && this.isPiece && this.color.equals(Color.red)){
				view.from.setBorder(null);
				view.from = this;
				view.from.setBorder(BorderFactory.createLineBorder(Color.red));
			}else if (view.from != null && this != view.from) { //走棋
				
			
				view.to = this;
				//System.out.println("KKKKKK" + view.from.name + view.to.name);
				if (view.isLegal(view.from, view.to)) {
					view.move(view.from, view.to);

				} else {
					System.out.println("不能走棋");
					view.to = null;
				}

			}
		}

	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}



