package Panes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
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
}
