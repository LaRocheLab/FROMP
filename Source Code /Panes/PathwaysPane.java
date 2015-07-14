package Panes;

import Objects.EcWithPathway;
import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import Prog.DataProcessor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// The class which switches between all three analysies in the pathway completeness section

public class PathwaysPane extends JPanel {
	private static final long serialVersionUID = 1L; 
	SampleScorePane ssp; // The Show pathway-score tab
	PathwayMatrix pwMat; // the Show score-matrix tab
	Color buttonDefault_; 
	Project activeProj_; // the active project
	ArrayList<PathwayWithEc> pwList_; // Array list of pathways
	ArrayList<EcWithPathway> ecList_; // Array list of ecs mapped to pathways
	DataProcessor proc_; // the data processor used to parse and compute what we need from the raw input files
	JPanel showPanel_; 
	int yOffset_; 
	double minVisScore_ = 0; 
	int xCol2 = 350; 
	int xCol3 = 500; 
	int mode = 0; 
	int yLine1 = 30; 
	JPanel optionsPanel_; // the options panel where buttons, and check boxes specific to this tab are
	JButton pwScores_; // button to switch to the Show pathway-score pane
	JButton pwMatrix_; // button to switch to the Show score-matrix pane
	JButton actMatrix_; 
	JButton pwPlot_; // button to switch to the Show score-plot pane
	JTextField maxVisfield; 
	JCheckBox chainCheck_; // the checkbox for chaining mode 1
	JCheckBox chainCheck2_; // the checkbox for chaining mode 2
	JCheckBox listCheck_; // the sort my score checkbox
	JCheckBox listCheck_2;//sort by name
	int activeChainingMode = 0; 
	int xwidth; 
	boolean checked; 
	boolean checked2;
	public JButton backButton_; // Button to take the user back to Analysis options

	public PathwaysPane(Project activeProj, DataProcessor proc, Dimension dim) {
		this.activeProj_ = activeProj;
		this.pwList_ = proc.getPathwayList_();
		this.ecList_ = DataProcessor.ecList_;
		this.proc_ = proc;
		this.minVisScore_ = Project.minVisScore_;
		this.xwidth = (4000 + Project.samples_.size() * 300);
		setSize(dim);
		setBackground(Project.getBackColor_().darker());
		setLayout(new BorderLayout());
		setVisible(true);

		checked = false;
		checked2= false;

		this.backButton_ = new JButton("< Back to the Analysis Options");
		initMainPanels();
		initChainChecks();
		addOptions();
		showScores();
	}

	private void initMainPanels() {// initiates the show and options panels
		this.optionsPanel_ = new JPanel();
		this.optionsPanel_
				.setPreferredSize(new Dimension(getWidth() - 50, 100));
		this.optionsPanel_.setBackground(Project.getBackColor_().darker());
		this.optionsPanel_.setBackground(Project.getBackColor_());
		this.optionsPanel_.setVisible(true);
		this.optionsPanel_.setLayout(null);
		add(this.optionsPanel_, "First");

		this.showPanel_ = new JPanel();
		this.showPanel_.setPreferredSize(new Dimension(getWidth() - 70,
				getHeight() - 150));
		this.showPanel_.setBackground(Project.getBackColor_().brighter());
		this.showPanel_.setLayout(new BorderLayout());
		this.showPanel_.setVisible(true);
		add(this.showPanel_, "Center");
	}

	private void addOptions() {
		this.optionsPanel_.removeAll();
		addButtons();
		showMaxVis();
		showListMode();
		addChaincheck();
		invalidate();
		validate();
		repaint();
	}

	private void initChainChecks() {// builds the chaining mode checkboxes
		JLabel chain = new JLabel("Use Chaining Mode 1");
		if (this.chainCheck_ == null) {
			this.chainCheck_ = new JCheckBox();
			this.chainCheck_.setSelected(false);
		}
		this.chainCheck_.setBackground(this.optionsPanel_.getBackground());
		chain.setBounds(this.xCol3 + 20, this.yLine1, 230, 20);
		this.optionsPanel_.add(chain);

		this.chainCheck_.setBounds(this.xCol3, this.yLine1, 20, 20);
		this.chainCheck_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Project.chaining = PathwaysPane.this.chainCheck_.isSelected();
				if (PathwaysPane.this.chainCheck2_.isSelected()) {
					PathwaysPane.this.chainCheck2_.setSelected(false);
				} else {
					PathwaysPane.this.activeChainingMode = 0;
				}
				if (PathwaysPane.this.chainCheck_.isSelected()) {
					PathwaysPane.this.activeChainingMode = 1;
				}
				PathwaysPane.this.proc_
						.getAllscores(PathwaysPane.this.activeChainingMode);
				PathwaysPane.this.redo();
			}
		});
		this.optionsPanel_.add(this.chainCheck_);

		JLabel chain2 = new JLabel("Use Chaining Mode 2");
		if (this.chainCheck2_ == null) {
			this.chainCheck2_ = new JCheckBox();

			this.chainCheck2_.setSelected(false);
		}
		this.chainCheck2_.setSelected(false);
		chain2.setBounds(this.xCol3 + 220, this.yLine1, 230, 20);
		this.optionsPanel_.add(chain2);
		this.chainCheck2_.setBounds(this.xCol3 + 200, this.yLine1, 20, 20);
		this.chainCheck2_.setBackground(this.optionsPanel_.getBackground());
		this.chainCheck2_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Project.chaining = PathwaysPane.this.chainCheck2_.isSelected();
				if (PathwaysPane.this.chainCheck_.isSelected()) {
					PathwaysPane.this.chainCheck_.setSelected(false);
				} else {
					PathwaysPane.this.activeChainingMode = 0;
				}
				if (PathwaysPane.this.chainCheck2_.isSelected()) {
					PathwaysPane.this.activeChainingMode = 2;
				}
				PathwaysPane.this.proc_
						.getAllscores(PathwaysPane.this.activeChainingMode);
				PathwaysPane.this.redo();
			}
		});
		this.optionsPanel_.add(this.chainCheck2_);
	}

	private void addChaincheck() {// adds the chaining mode checkboxes to the
									// options panel
		JLabel chain = new JLabel("Use Chaining Mode 1");
		chain.setBounds(this.xCol3 + 20, this.yLine1, 230, 20);
		this.optionsPanel_.add(chain);
		this.optionsPanel_.add(this.chainCheck_);

		JLabel chain2 = new JLabel("Use Chaining Mode 2");
		chain2.setBounds(this.xCol3 + 220, this.yLine1, 230, 20);
		this.optionsPanel_.add(chain2);
		this.optionsPanel_.add(this.chainCheck2_);
	}

	public void showListMode() {
		JLabel listl_ = new JLabel("Sort by Score ");

		this.listCheck_ = new JCheckBox();

		listl_.setBounds(this.xCol2 + 20, this.yLine1, 150, 20);
		this.optionsPanel_.add(listl_);
		this.listCheck_.setBounds(this.xCol2, this.yLine1, 20, 20);
		/*
		 * this.listCheck_.setBackground(this.optionsPanel_.getBackground()); if
		 * (this.activeProj_ != null) {
		 * this.listCheck_.setSelected(Project.listMode_); }
		 */
		listCheck_.setSelected(checked);
		this.listCheck_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Project.listMode_ =
				// PathwaysPane.this.listCheck_.isSelected();

				if (checked) {
					checked = false;
				} else {
					checked = true;
				}

				for (int i = 0; i < Project.samples_.size(); i++) {
					if (PathwaysPane.this.listCheck_.isSelected()) {
						PathwaysPane.this
								.sortPathwaysByScore(((Sample) Project.samples_
										.get(i)).pathways_);
					} else {
						PathwaysPane.this
								.sortPathsById(((Sample) Project.samples_
										.get(i)).pathways_);
					}
				}
				if (PathwaysPane.this.listCheck_.isSelected()) {
					PathwaysPane.this
							.sortPathwaysByScore(Project.overall_.pathways_);
				} else {
					PathwaysPane.this.sortPathsById(Project.overall_.pathways_);
				}

				PathwaysPane.this.redo();
			}
		});
		this.optionsPanel_.add(this.listCheck_);
		
		JLabel listl_2 = new JLabel("Sort by Name ");

		this.listCheck_2 = new JCheckBox();

		listl_2.setBounds(this.xCol2 + 20, this.yLine1 + 20, 150, 20);
		this.optionsPanel_.add(listl_2);
		this.listCheck_2.setBounds(this.xCol2, this.yLine1 + 20, 20, 20);
		
		listCheck_2.setSelected(checked2);
		this.listCheck_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (checked2) {
					checked2 = false;
				} else {
					checked2 = true;
				}

				for (int i = 0; i < Project.samples_.size(); i++) {
					if (PathwaysPane.this.listCheck_2.isSelected()) {
						PathwaysPane.this.sortPathwaysByName(((Sample) Project.samples_.get(i)).pathways_);
					} else {
						PathwaysPane.this.sortPathsById(((Sample) Project.samples_.get(i)).pathways_);
					}
				}
				if (PathwaysPane.this.listCheck_2.isSelected()) {
					PathwaysPane.this.sortPathwaysByName(Project.overall_.pathways_);
				} else {
					PathwaysPane.this.sortPathsById(Project.overall_.pathways_);
				}

				PathwaysPane.this.redo();
			}
		});
		this.optionsPanel_.add(this.listCheck_2);
		
	}

	private void showMaxVis() {
		JLabel maxvisl_ = new JLabel("Minimum shown Pathway Score: ");
		if (this.maxVisfield == null) {
			this.maxVisfield = new JTextField((int)this.minVisScore_);
		}
		maxvisl_.setBounds(10, 10, 400, 20);
		this.optionsPanel_.add(maxvisl_);
		this.maxVisfield.setBounds(10, 30, 200, 20);
		this.maxVisfield.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaysPane.this.getMaxVis();
				PathwaysPane.this.redo();
			}
		});
		this.optionsPanel_.add(this.maxVisfield);
	}
	// removes everything from the panel, chooses what mode the user has selected, and rebuilds the panel based off that.
	private void redo() {
		this.optionsPanel_.removeAll();
		addOptions();
		this.showPanel_.removeAll();
		System.out.println("redo");
		switch (this.mode) {
		case 0:
			showScores();
			break;
		case 1:
			showMatrix();
			break;
		case 3:
			showPlot();
		}
		invalidate();
		validate();
		repaint();
	}

	private void getMaxVis() {
		if (this.maxVisfield != null && isNumber(this.maxVisfield.getText())) {
			double tmp = Double.parseDouble(this.maxVisfield.getText());
			if (this.minVisScore_ != tmp && tmp > 0) {
				this.minVisScore_ = tmp;
			}
		}
		Project.minVisScore_ = this.minVisScore_;
	}
	
	public static boolean isNumber(String tmp){
	  return tmp.matches("-?\\d+(\\.\\d+)?");
	}
	
	// Adds the buttons to select between the three pathway analysies: Show pathway-scores, Show score-matrix, and Show score-plot
	private void addButtons() {
		this.pwScores_ = new JButton("Show pathway-scores");
		this.pwScores_.setBounds(0, 80, 200, 20);
		this.pwScores_.setVisible(true);
		if (this.mode == 0) {
			this.pwScores_.setBackground(Project.standard);
		}
		this.pwScores_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaysPane.this.showScores();
				PathwaysPane.this.mode = 0;
				PathwaysPane.this.changeMode();
			}
		});
		this.optionsPanel_.add(this.pwScores_);
		this.buttonDefault_ = this.optionsPanel_.getBackground();

		this.pwMatrix_ = new JButton("Show score-matrix");
		this.pwMatrix_.setBounds(201, 80, 200, 20);
		this.pwMatrix_.setVisible(true);
		if (this.mode == 1) {
			this.pwMatrix_.setBackground(Project.standard);
		}
		this.pwMatrix_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaysPane.this.showMatrix();
				PathwaysPane.this.mode = 1;
				PathwaysPane.this.changeMode();
			}
		});
		this.optionsPanel_.add(this.pwMatrix_);

		this.pwPlot_ = new JButton("Show score-plot");
		this.pwPlot_.setBounds(402, 80, 200, 20);
		this.pwPlot_.setVisible(true);
		if (this.mode == 3) {
			this.pwPlot_.setBackground(Project.standard);
		}
		this.pwPlot_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaysPane.this.showPlot();
				PathwaysPane.this.mode = 3;
				PathwaysPane.this.changeMode();
			}
		});
		this.optionsPanel_.add(this.pwPlot_);

		this.backButton_.setBounds(10, 52, 300, 25);
		this.optionsPanel_.add(this.backButton_);
	}

	private void showScores() {// show the "Show pathway-scores" tab
		clear();
		this.ssp = new SampleScorePane(Project.overall_, this.activeProj_,
				this.proc_, new Dimension(getWidth(), getHeight()));
		this.showPanel_.add("Center", this.ssp);
		invalidate();
		validate();
		repaint();
	}

	private void showMatrix() {// show the "Show score-matrix" tab
		clear();

		this.pwMat = new PathwayMatrix(Project.samples_, Project.overall_,
				this.pwList_, this.activeProj_);

		this.showPanel_.add("Center", this.pwMat);
		invalidate();
		validate();
		repaint();
	}

	private void showPlot() {// show the "Show score-plot" tab
		clear();

		PathwayPlot pwPlot = new PathwayPlot(this.activeProj_, this.proc_);

		this.showPanel_.add("Center", pwPlot);
		invalidate();
		validate();
		repaint();
	}

	private void clear() {
		this.showPanel_.removeAll();
	}
	//sorts the pathways by Pathways.score_
	private void sortPathwaysByScore(ArrayList<PathwayWithEc> pathways) {
		int tmpCnt = 0;
		for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++) {
			tmpCnt = pathCnt;
			for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
				if (((PathwayWithEc) pathways.get(tmpCnt)).score_ < ((PathwayWithEc) pathways
						.get(pathCnt2)).score_) {
					tmpCnt = pathCnt2;
				}
			}
			pathways.add(pathCnt, (PathwayWithEc) pathways.get(tmpCnt));
			pathways.remove(tmpCnt + 1);
		}
	}

	public int getyOffset_() {
		return this.yOffset_;
	}

	public void setyOffset_(int yOffset_) {
		this.yOffset_ = yOffset_;
	}

	private void sortPathsById(ArrayList<PathwayWithEc> pathways) {

		int tmpCnt = 0;
		for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++) {
			tmpCnt = pathCnt;
			for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
				if (((PathwayWithEc) pathways.get(tmpCnt))
						.idBiggerId2(((PathwayWithEc) pathways.get(pathCnt2)))) {
					tmpCnt = pathCnt2;
				}
			}
			pathways.add(pathCnt, (PathwayWithEc) pathways.get(tmpCnt));
			pathways.remove(tmpCnt + 1);
		}
	}
	
	private void sortPathwaysByName(ArrayList<PathwayWithEc> pathways) {
		for (int pathCnt = 0; pathCnt < pathways.size(); pathCnt++) {
			if ((pathways.get(pathCnt).score_ <= 0)) {
				pathways.remove(pathCnt);
			}
		}
		Collections.sort(pathways, new Comparator<PathwayWithEc>() {
	        @Override public int compare(PathwayWithEc p1, PathwayWithEc p2) {
	        	String name1 = p1.getName().toLowerCase();
	        	String name2 = p2.getName().toLowerCase();
	            return name1.compareTo(name2);
	        }
		});
	}

	private void quicksortPathsById(ArrayList<PathwayWithEc> path, int low,
			int high) {
		int i = low, j = high;
		// if(i>=j){return;}
		PathwayWithEc pivot = path.get(low + (high - low) / 2);
		while (i <= j) {
			while (path.get(i).idSmallerId2(pivot)) {
				i++;
			}
			while (path.get(j).idBiggerId2(pivot)) {
				j--;
			}
			if (i <= j) {
				switchPaths(path, i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quicksortPathsById(path, low, j);
		if (i < high)
			quicksortPathsById(path, i, high);
	}

	private void switchPaths(ArrayList<PathwayWithEc> path, int i, int j) {
		PathwayWithEc tmp = path.get(i);

		path.set(i, path.get(j));
		path.set(j, tmp);
	}

	public int convertStringtoInt(String in) {
		int ret = 0;
		int ccti = 0;
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			ccti = convertCharToInt(c);
			if (ccti == -1) {
				return ret;
			}
			ret *= 10;
			ret += ccti;
		}
		return ret;
	}

	public int convertCharToInt(char c) {
		switch (c) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		}
		return -1;
	}

	private void changeMode() {
		redo();
	}

	public void newSize(Dimension dim) {
	}
}
