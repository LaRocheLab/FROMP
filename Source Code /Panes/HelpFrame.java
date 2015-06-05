package Panes;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//The Help frame. Not super exciting maybe, but it is also the project summary window (when it is called by the data processor)

public class HelpFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public static boolean showSummary = true;

	public HelpFrame(String Text) {
		if (!showSummary) {
			return;
		}
		setTitle("Project summary");
		setBounds(500, 400, 300, 450);
		//set to false to try to not get the summary page to show up
		setVisible(false);
		setResizable(false);
		setLayout(null);

		JPanel back = new JPanel();
		back.setBounds(0, 0, 300, 400);
		back.setVisible(true);
		back.setLayout(null);
		back.setBackground(Color.white);
		add(back);

		JLabel fromp = new JLabel(Text);
		fromp.setBounds(0, 0, 300, 400);
		fromp.setVisible(true);
		back.add(fromp);
	}
}
