package Panes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OverAllMapPane extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel image_;
	final String picPath_;
	private BufferedImage pathMap_;

	public OverAllMapPane() {
		this.picPath_ = ("pics" + File.separator + "ec01100-50pro.png");
		try {
			this.pathMap_ = ImageIO.read(new File(this.picPath_));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBounds(0, 0, this.pathMap_.getWidth() + 300,
				this.pathMap_.getHeight() - 50);
		setLayout(null);
		setBackground(Color.white);

		ImageIcon icon = new ImageIcon(this.pathMap_);
		this.image_ = new JLabel(icon);
		this.image_.setLayout(null);
		this.image_.setBounds(0, 0, this.pathMap_.getWidth(),
				this.pathMap_.getHeight());
		this.image_.setVisible(true);
		add(this.image_);
	}
}
