package Panes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static BufferedImage image_;

	public ImagePanel(BufferedImage image) {
		image_ = image;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(image_, 0, 0, null);
		repaint();
	}

	public static void showImage(BufferedImage image, String title) {
		JFrame f = new JFrame(title);
		f.add(new ImagePanel(image));
		f.setLocation(100, 100);
		f.setSize(image.getWidth(), image.getHeight() + 30);
		f.setVisible(true);
	}
	
	public static void showImage(BufferedImage image, String title, ArrayList<JLabel> labels) {
		JFrame f = new JFrame(title);
		for(int i = 0; i < labels.size(); i++){
			f.add(labels.get(i));
		}
		f.add(new ImagePanel(image));
		f.setLocation(100, 100);
		f.setSize(image.getWidth(), image.getHeight() + 30);
		f.setVisible(true);
	}
}
