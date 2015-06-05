package Panes;

import Objects.EcWithPathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Prog.DataProcessor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

public class LCAPanes extends JPanel{
	private static final long serialVersionUID = 1L; 
	Project activeProj_; // This is the active project
	ArrayList<PathwayWithEc> pwList_; // List of pathways to which something is mapped
	//ArrayList<EcWithPathway> ecList_; // list of ec which map to some pathway
	DataProcessor proc_; // the data processor object which controlls the parsing of the input files allowing important information to be cleaned for analysis
	JPanel showPanel_; 
	JPanel optionsPanel_; // The options panel
	JButton pathwaySort; // A button to choose to g to the PathwayEcMat panel
	JButton ecSort_; // A button to go to the ActMatrixPane panel
	//PathwayEcMat pwEcMat; 
	ActMatrixPane actMat_; 
	int mode_; 
	int xsize; 
	int yOffset_; 
	public JButton backButton_; // Button to go back to Analysis Options
	Timer timer; 

	public LCAPanes(Project activeProj, DataProcessor proc, Dimension dim) {
		this.activeProj_ = activeProj;
		this.pwList_ = proc.getPathwayList_();
		//this.ecList_ = DataProcessor.ecList_;
		this.proc_ = proc;
		this.xsize = (4000 + Project.samples_.size() * 300);
		setSize(dim);
		if (Project.getBackColor_() == null) {
			Project.setBackColor_(Color.orange);
		}
		setBackground(Project.getBackColor_());
		setLayout(new BorderLayout());
		setVisible(true);
		this.backButton_ = new JButton(" < Back to the Analysis Options");
		initMainPanels();

		addOptions();
		showActivity();
	}

	private void initMainPanels() {
		this.optionsPanel_ = new JPanel();

		this.optionsPanel_
				.setPreferredSize(new Dimension(getWidth() - 50, 100));
		this.optionsPanel_.setBackground(Project.getBackColor_());
		this.optionsPanel_.setVisible(true);
		this.optionsPanel_.setLayout(null);
		add(this.optionsPanel_, "First");

		this.showPanel_ = new JPanel();
		this.showPanel_.setPreferredSize(new Dimension(getWidth() - 70,
				getHeight() - 150));
		this.showPanel_.setBackground(Project.getBackColor_());
		this.showPanel_.setLayout(new BorderLayout());
		this.showPanel_.setVisible(true);
		add(this.showPanel_, "Center");
	}

	private void addOptions() {
		this.optionsPanel_.removeAll();

		this.backButton_.setBounds(10, 10, 300, 25);
		this.optionsPanel_.add(this.backButton_);
	}

	private void showActivity() {
		this.showPanel_.removeAll();

		switchmode();
		switch (this.mode_) {
		case 0:
			if ((this.actMat_ == null) || (Project.dataChanged)) {
				this.actMat_ = new ActMatrixPane(this.activeProj_,this.proc_,this.showPanel_.getSize());
			}
			this.showPanel_.add(this.actMat_);
			invalidate();
			validate();
			repaint();
			break;
		case 1:
//			if ((this.pwEcMat == null) || (Project.dataChanged)) {
//				this.pwEcMat = new PathwayEcMat(this.pwList_, this.activeProj_,
//						this.proc_, this.showPanel_.getSize());
//			}
//			this.showPanel_.add(this.pwEcMat);
//			invalidate();
//			validate();
//			repaint();
		}
	}

	private void switchmode() {
	}
}
