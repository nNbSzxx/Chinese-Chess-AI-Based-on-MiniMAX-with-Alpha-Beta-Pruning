package chess.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import chess.AI.ChessEngine.Level;

public class Menu extends JPanel implements ActionListener,ItemListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JButton reset;
	private JButton back;
	private JButton check;
	private Icon icon1;
	private Icon icon2;
	private Icon icon3;
	
	private Icon m1;
	private Icon m2;
	private Icon m3;
	
	private int count = 0;	//由于每次选择下拉列表的时候都会被出发两次，所以用一个计数器来选择最后一次被选中的项
	private int lastSelectItem = 0;	//下拉列表中上一个被选中的项
	private boolean cancel = false;	//由于一些原因，需要判定对话框中玩家是否点击了取消按钮以便后续的处理
	
	private JComboBox<Icon> levelList;
	
	private Vector<Icon> levelListPic;
	
	private ChessView view;
	
	public Menu(ChessView view){
		this.view = view;
	
		this.setBackground(new Color(205,133,63));
		
		icon1 = new ImageIcon("pic/reset.jpg");
		icon2 = new ImageIcon("pic/back.jpg");
		icon3 = new ImageIcon("pic/check.jpg");
		
		m1 = new ImageIcon("pic/menu11.jpg");
		m2 = new ImageIcon("pic/menu22.jpg");
		m3 = new ImageIcon("pic/menu33.jpg");
		
		levelListPic = new Vector<Icon>(3);
		levelListPic.add(m1);
		levelListPic.add(m2);
		levelListPic.add(m3);
		
		reset = new JButton(icon1);
		back = new JButton(icon2);
		check = new JButton(icon3);

		reset.setBorder(null);
		reset.setBorderPainted(false);
		
		back.setBorder(null);
		back.setBorderPainted(false);
		
		check.setBorder(null);
		check.setBorderPainted(false);
		
		
		reset.addActionListener(this);
		back.addActionListener(this);
		check.addActionListener(this);

		
		levelList = new JComboBox<Icon>(levelListPic);
		levelList.setBackground(new Color(205,133,63));
		levelList.addItemListener(this);
		
		
		
		
		this.setLayout(new FlowLayout());
		
		this.add(reset);
		this.add(back);
		
		this.add(levelList);
		
		this.add(check);
		this.setSize(100, 700);
	}
		
	public void setUnable(){
		this.back.removeActionListener(this);
		this.levelList.removeItemListener(this);
		
	}
	public void setAble(){
		this.back.addActionListener(this);
		this.levelList.addItemListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==reset){
			view.restart();
		}else if(e.getSource()==back){
			if(view.isRedTurn)
				view.back();
		}else if(e.getSource()==check){
			view.check();
		}
		
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		int selectItem;
		int currentSelectItem = levelList.getSelectedIndex();
		if (e.getItem().equals(m1)) {
			System.out.println(e.getItem());
			this.count = (this.count + 1) % 2;
			if (this.count == 0 && !this.cancel) {
				selectItem = JOptionPane.showConfirmDialog(null, "修改难度将会重新开局，确认要修改?", "修改提示",
						JOptionPane.OK_CANCEL_OPTION);
				if (selectItem == 0) {
					view.setLevel(Level.ONE);
					this.lastSelectItem = currentSelectItem;
				} else {
					this.cancel = true;
					levelList.setSelectedIndex(this.lastSelectItem);

				}
			} else if (this.count == 0 && this.cancel) {
				this.cancel = false;
			}
		} else if (e.getItem().equals(m2)) {
			System.out.println(e.getItem());
			this.count = (this.count + 1) % 2;

			if (this.count == 0 && !this.cancel) {
				selectItem = JOptionPane.showConfirmDialog(null, "修改难度将会重新开局，确认要修改?", "修改提示",
						JOptionPane.OK_CANCEL_OPTION);
				if (selectItem == 0) {
					view.setLevel(Level.TWO);
					this.lastSelectItem = currentSelectItem;
				} else {
					this.cancel = true;
					levelList.setSelectedIndex(this.lastSelectItem);

				}
			} else if (this.count == 0 && this.cancel) {
				this.cancel = false;
			}
		} else if (e.getItem().equals(m3)) {
			System.out.println(e.getItem());
			this.count = (this.count + 1) % 2;
			if (this.count == 0 && !this.cancel) {
				selectItem = JOptionPane.showConfirmDialog(null, "修改难度将会重新开局，确认要修改?", "修改提示",
						JOptionPane.OK_CANCEL_OPTION);
				if (selectItem == 0) {
					view.setLevel(Level.THREE);
					this.lastSelectItem = currentSelectItem;
				} else {
					this.cancel = true;
					levelList.setSelectedIndex(this.lastSelectItem);

				}
			} else if (this.count == 0 && this.cancel) {
				this.cancel = false;
			}
		}

	}

}
