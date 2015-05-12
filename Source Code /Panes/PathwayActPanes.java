package Panes;

import Objects.EcWithPathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Prog.DataProcessor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

// The Panel above the PathwayActivitymatrixPane

public class PathwayActPanes extends JPanel {
	private static final long serialVersionUID = 1L;
	Project activeProj_;
	ArrayList<PathwayWithEc> pwList_;
	ArrayList<EcWithPathway> ecList_;
	DataProcessor proc_;
	JCheckBox includeWeights;
	PathwayActivitymatrixPane actMat_;
	JPanel showPanel_;
	JPanel optionsPanel_;
	public JButton backButton_;
	int xwidth;

	public PathwayActPanes(Project activeProj, DataProcessor proc, Dimension dim) {
		this.activeProj_ = activeProj;
		this.pwList_ = proc.getPathwayList_();
		this.ecList_ = DataProcessor.ecList_;
		this.proc_ = proc;
		setSize(dim);
		setBackground(Project.standard);
		setLayout(new BorderLayout());
		setVisible(true);
		this.backButton_ = new JButton("< Back to the Analysis Options");

		prepaint();
	}

	private void initMainPanels() {
		this.optionsPanel_ = new JPanel();
		this.optionsPanel_.setPreferredSize(new Dimension(getWidth() - 50, 50));
		this.optionsPanel_.setBackground(Project.getBackColor_().darker());
		this.optionsPanel_.setBackground(Project.getBackColor_());
		this.optionsPanel_.setVisible(true);
		this.optionsPanel_.setLayout(null);
		add(this.optionsPanel_, "First");

		this.backButton_.setBounds(10, 10, 300, 25);
		this.optionsPanel_.add(this.backButton_);

		this.showPanel_ = new JPanel();
		this.showPanel_.setPreferredSize(new Dimension(getWidth() - 70,
				getHeight() - 150));
		this.showPanel_.setBackground(Project.getBackColor_().brighter());
		this.showPanel_.setLayout(new BorderLayout());
		this.showPanel_.setVisible(true);
		add(this.showPanel_, "Center");
	}

	private void showActivity() {
		if ((this.actMat_ == null) || (Project.dataChanged)) {
			this.actMat_ = new PathwayActivitymatrixPane(this.proc_,
					this.activeProj_, this.showPanel_.getSize());
			this.showPanel_.add(this.actMat_);
		} else if (this.actMat_.changed_) {
			this.actMat_.showMatrix();
		}
	}

	private void prepaint() {
		initMainPanels();
		showActivity();
		invalidate();
		validate();
		repaint();
	}
}
