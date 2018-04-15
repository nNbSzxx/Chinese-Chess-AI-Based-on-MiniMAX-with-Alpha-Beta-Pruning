package chess.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PlayBoard extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	
	protected Image img;
	protected ImageIcon icon;
	
	
	public PlayBoard(){
		icon = new ImageIcon("pic/qipan.png");
		img = icon.getImage();
		System.out.println(icon.getIconWidth() + "  " + icon.getIconHeight());
		setLayout(null);
		setSize(630,700);
		setVisible(true);	
				
		this.addMouseListener(this);
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(img, 0, 0, 630, 680, this);
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(" x:" +e.getX() + "y:" + e.getY());
				
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
	
}

