package Panes;

import Objects.EcWithPathway;
import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import Prog.PathButt;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
// pop out window when click ec# at ec activity
public class PwInfoFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public PwInfoFrame(EcWithPathway ec, Project actProj, Sample samp) {
		int lineDis = 40;
		Sample samp_ = samp;
		setBounds(100, 100, 400, 50 + lineDis * ec.pathways_.size());
		setVisible(true);
		setLayout(null);
	
		if ((ec.amount_ <= 1) && (samp_ != null)) {
			
			ec.amount_ = samp_.getEc(ec.name_).amount_;
		}
		setTitle(ec.name_ + "(" + ec.bioName_ + ")" + " * " + ec.amount_);

		JPanel back = new JPanel();
		back.setBackground(Color.white);
		back.setBounds(0, 0, getWidth(), getHeight());
		back.setVisible(true);
		back.setLayout(null);
		add(back);
		for (int pwCnt = 0; pwCnt < ec.pathways_.size(); pwCnt++) {
			for (int pwCnt2 = 0; pwCnt2 < samp_.pathways_.size(); pwCnt2++) {
				if (((Pathway) ec.pathways_.get(pwCnt)).id_
						.contentEquals(((PathwayWithEc) samp_.pathways_
								.get(pwCnt2)).id_)) {
					if (actProj == null) {
						System.out.println("actProj");
					}
					if (samp_.pathways_.get(pwCnt2) == null) {
						System.out.println("samp_.pathways_.get(pwCnt2)");
					}
					PathButt label = new PathButt(Project.samples_, samp_,
							(PathwayWithEc) samp_.pathways_.get(pwCnt2),
							Color.orange, "", 0);
					label.setBounds(0, lineDis * pwCnt, 400, lineDis);
					label.setVisible(true);
					label.setLayout(null);
					back.add(label);
				}
			}
		}
		repaint();
	}
}
