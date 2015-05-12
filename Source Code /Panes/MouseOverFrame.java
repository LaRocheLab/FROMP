package Panes;

import Objects.PathwayWithEc;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MouseOverFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static int frameCount;
	private JLabel mouseOverFrDisp;

	public MouseOverFrame() {
		super("Additional Pathway-information");
		if (frameCount > 0) {
			closeFrame();
			return;
		}
		frameCount = 0;

		setVisible(true);
		setLayout(null);
		setBounds(700, 10, 500, 100);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				MouseOverFrame.frameCount -= 1;
				MouseOverFrame.this.closeFrame();
			}
		});
		JPanel mouseOverP = new JPanel();
		mouseOverP.setBackground(Color.white);
		mouseOverP.setBounds(0, 0, 500, 60);
		add(mouseOverP);

		this.mouseOverFrDisp = new JLabel("Additional Pathway-information");
		this.mouseOverFrDisp.setBounds(0, 0, 500, 60);
		mouseOverP.add(this.mouseOverFrDisp);

		setText("Additional Pathway-information");

		frameCount += 1;
		System.out.println("addMouseOverFrame frameCount class" + frameCount);
	}

	public void setText(String text) {
		this.mouseOverFrDisp.setText(text);
		repaint();
	}

	public void setAdditionalInfo(PathwayWithEc path) {
		this.mouseOverFrDisp.setText("<html>" + path.name_ + "<br>ID:"
				+ path.id_ + "<br> Wgt:" + path.weight_ + "| Scr:"
				+ path.score_ + "</html>");
		repaint();
	}

	public void closeFrame() {
		setVisible(false);
		dispose();
		frameCount -= 1;
	}

	public static int getFrameCount() {
		return frameCount;
	}
}
