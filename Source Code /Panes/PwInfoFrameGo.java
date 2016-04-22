package Panes;

import Objects.GONum;
import Objects.Project;
import Objects.Sample;
import Prog.DataProcessor;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
// pop out window when click go# at go activity
public class PwInfoFrameGo extends JFrame {
	private static final long serialVersionUID = 1L;

	public PwInfoFrameGo(GONum go, Project actProj, Sample samp) {
		
		for (int i = 0; i < DataProcessor.goList_.size();i++){
			if (DataProcessor.goList_.get(i).getGoNumber().contentEquals(go.GoNumber)){
				go.setGoTerm(DataProcessor.goList_.get(i).getGoTerm());
				break;
			}
			
		}
		if (go.getGoTerm() == null){
			go.setGoTerm("Name does not found");
		}
		String label = go.getGoTerm();
		int xsize = 0;
		if (label.length()*10>400){
			xsize = label.length()*10;
		}
		else {
			xsize = 400;
		}
		setBounds(100, 100, xsize,150);
		setVisible(true);
		setLayout(null);
	
		setTitle("GO: "+ go.GoNumber);

		JPanel back = new JPanel();
		back.setBackground(Color.white);
		back.setBounds(0, 0, getWidth(), getHeight());
		back.setVisible(true);
		back.setLayout(null);
		add(back);
		
		
		JLabel goTerm = new JLabel(go.getGoTerm(),SwingConstants.CENTER);
		goTerm.setBounds(0, 0, getWidth(), getHeight());
		back.add(goTerm);

		repaint();
	}
}
