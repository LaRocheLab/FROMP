package Panes;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * An inFROMPmational (...see what I did there...) JFrame (ie. popup window)
 * which gives details about the version and developers of the FROMP software.
 * 
 * @author Kevan Lynch, Jennifer Terpstra
 */
public class AboutFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public AboutFrame() {
		setTitle("About Fromp");
		setBounds(500, 400, 300, 300);
		setVisible(true);
		setResizable(false);
		setLayout(null);

		JPanel back = new JPanel();
		back.setBounds(0, 0, 300, 300);
		back.setVisible(true);
		back.setLayout(null);
		back.setBackground(Color.white);
		add(back);

		JLabel fromp = new JLabel("Fromp Ver. 1.0: 04.2015");
		fromp.setBounds(10, 10, 200, 20);
		fromp.setVisible(true);
		back.add(fromp);

		JLabel idea = new JLabel("Developers:");
		idea.setBounds(10, 50, 200, 20);
		idea.setVisible(true);
		back.add(idea);

		idea = new JLabel("Dhwani Desai");
		idea.setBounds(30, 80, 200, 20);
		idea.setVisible(true);
		back.add(idea);

		idea = new JLabel("Harald Schunck");
		idea.setBounds(30, 110, 200, 20);
		idea.setVisible(true);
		back.add(idea);

		idea = new JLabel("Johannes Löser");
		idea.setBounds(30, 140, 200, 20);
		idea.setVisible(true);
		back.add(idea);

		idea = new JLabel("Julie LaRoche");
		idea.setBounds(30, 170, 200, 20);
		idea.setVisible(true);
		back.add(idea);

		idea = new JLabel("Kevan Lynch");
		idea.setBounds(30, 200, 200, 20);
		idea.setVisible(true);
		back.add(idea);
		
		idea = new JLabel("Jennifer Terpstra");
		idea.setBounds(30, 230, 200, 20);
		idea.setVisible(true);
		back.add(idea);
	}
}
