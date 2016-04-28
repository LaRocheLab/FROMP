package Panes;

import Objects.EcNr;
import Objects.Line;
import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import Prog.DataProcessor;
import Prog.Exporter;
import Prog.PathButt;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//The Pathway Activity Matrix Pane. All things you see happening in the Pathway Activity part of the gui are controlled here.

public class PathwayActivitymatrixPane extends JPanel {
	private static final long serialVersionUID = 1L; //
	DataProcessor proc_; // DataProcessor object to do the parsing and computations nessesairy to build the activity matrix
	Project actProj_; // The active project
	Exporter exp_; // Used to export the matrix to a file
	boolean includeWeights_; // Boolean to say whether or not to to include the weights
	JButton export_; // Button used in the GUI to export the activity matrix
	boolean changed_; 
	JCheckBox includeWeights; // Checkboxes for sorting, normalizing and including the weights of the matrix
	JCheckBox normalizeMax_; 
	JCheckBox normalizeSum_; 
	JCheckBox sortByLine_; 
	int numOfpaths; // Number of paths
	int numOfSamples; // Number of samples
	ArrayList<PathwayWithEc> paths_; // ArrayList of the pathways which have ecs mapped
	ArrayList<Line> lineMatrix_; // An arraylist of line objects used to build the activity matrix
	MouseOverFrame infoFrame; // A mouse over frame used to display information about what you are mousing over of the main fromp frame
	private JPanel optionsPanel_; // The options panel at the top of the display for this frame
	private JPanel displayP_; // The main display panel where the matrix is displayed
	/*
	 * A scroll pane which houses the display panel such that if the display panel exceeded the
	 * alloted space of the frame it is fit to the display size and the user is able
	 * to scroll through the content
	 */
	private JScrollPane showJPanel_; 
	private JLabel mouseOverDisp; // A label which holds the same purpose as the MouseOverFrame
	private JPanel mouseOverP; 
	private JCheckBox useCsf_; 

	public PathwayActivitymatrixPane(DataProcessor proc, Project proj,
			Dimension dim) {
		System.out.println("Pathway Activity matrix");
		this.proc_ = proc;
		this.actProj_ = proj;

		setSize(dim);
		setVisible(true);
		setLayout(new BorderLayout());
		setBackground(Project.getBackColor_());
		setPreferredSize(dim);
		this.includeWeights_ = false;

		prepPaths(); // Prepares the Pathwaylist this.paths_
		this.changed_ = true; // Notes that the data has been changed
		initMainPanels(); // Initiates the options panel, the display panel, and the scroll panel
		addOptions(); // Adds checkboxes, buttons, and labels to the options panel
		createMatrix(); // Builds the activity matrix by generating the arraylist of line objects which will be used to display the matrix
		showMatrix(); // Generates the onscreen activity matrix
		invalidate(); // Rebuilds the backpanel to refresh what is seen on screen
		validate(); 
		repaint(); 
	}

	private void initMainPanels() {
		this.optionsPanel_ = new JPanel();
		this.optionsPanel_.setPreferredSize(new Dimension(getWidth(), 100));
		this.optionsPanel_.setLocation(0, 0);
		this.optionsPanel_.setBackground(Project.standard);
		this.optionsPanel_.setVisible(true);
		this.optionsPanel_.setLayout(null);
		add(this.optionsPanel_, "First");

		this.displayP_ = new JPanel();
		this.displayP_.setLocation(0, 0);
		this.displayP_.setPreferredSize(new Dimension(getWidth()
				+ Project.samples_.size() * 300, 8000));
		this.displayP_.setSize(getPreferredSize());
		this.displayP_.setBackground(Project.getBackColor_());
		this.displayP_.setVisible(true);
		this.displayP_.setLayout(null);

		this.showJPanel_ = new JScrollPane(this.displayP_);
		this.showJPanel_.setVisible(true);
		this.showJPanel_.setVerticalScrollBarPolicy(20);
		this.showJPanel_.setHorizontalScrollBarPolicy(30);

		add("Center", this.showJPanel_);
	}

	private void prepPaths() {
		this.paths_ = new ArrayList<PathwayWithEc>();
		for (int pathCnt = 0; pathCnt < this.proc_.getPathwayList_().size(); pathCnt++) {
			if (((PathwayWithEc) this.proc_.getPathwayList_().get(pathCnt))
					.isSelected()) {
				this.paths_.add(new PathwayWithEc((Pathway) this.proc_
						.getPathwayList_().get(pathCnt)));
			}
		}
	}

	private void addOptions() {
		this.includeWeights = new JCheckBox("Include Weights");
		this.includeWeights.setBounds(300, 5, 200, 20);
		this.includeWeights.setSelected(false);
		this.includeWeights.setBackground(this.optionsPanel_.getBackground());
		this.includeWeights.setVisible(true);
		this.includeWeights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayActivitymatrixPane.this
						.setIncludeWeights_(PathwayActivitymatrixPane.this.includeWeights
								.isSelected());
				PathwayActivitymatrixPane.this.changed_ = true;
			}
		});
		this.optionsPanel_.add(this.includeWeights);

		this.normalizeMax_ = new JCheckBox("Normalize by highest");
		this.normalizeMax_.setBounds(300, 25, 200, 20);
		this.normalizeMax_.setSelected(false);
		this.normalizeMax_.setBackground(this.optionsPanel_.getBackground());
		this.normalizeMax_.setVisible(true);
		this.normalizeMax_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayActivitymatrixPane.this.changed_ = true;
				if (PathwayActivitymatrixPane.this.normalizeSum_.isSelected()) {
					PathwayActivitymatrixPane.this.normalizeSum_
							.setSelected(false);
				}
			}
		});
		this.optionsPanel_.add(this.normalizeMax_);

		this.normalizeSum_ = new JCheckBox("Normalize by Col-sum");
		this.normalizeSum_.setBounds(500, 25, 200, 20);
		this.normalizeSum_.setSelected(false);
		this.normalizeSum_.setBackground(this.optionsPanel_.getBackground());
		this.normalizeSum_.setVisible(true);
		this.normalizeSum_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayActivitymatrixPane.this.changed_ = true;
				if (PathwayActivitymatrixPane.this.normalizeMax_.isSelected()) {
					PathwayActivitymatrixPane.this.normalizeMax_
							.setSelected(false);
				}
			}
		});
		this.optionsPanel_.add(this.normalizeSum_);

		this.sortByLine_ = new JCheckBox("Sort by Linesum");
		this.sortByLine_.setBounds(500, 5, 200, 20);
		this.sortByLine_.setSelected(false);
		this.sortByLine_.setBackground(this.optionsPanel_.getBackground());
		this.sortByLine_.setVisible(true);
		this.sortByLine_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayActivitymatrixPane.this.changed_ = true;
			}
		});
		this.optionsPanel_.add(this.sortByLine_);
		this.useCsf_ = new JCheckBox("CSF");
		this.useCsf_.setVisible(true);
		this.useCsf_.setLayout(null);
		this.useCsf_.setBackground(this.optionsPanel_.getBackground());
		this.useCsf_.setForeground(Project.getFontColor_());
		this.useCsf_.setBounds(10, 44, 100, 15);
		this.optionsPanel_.add(this.useCsf_);

		this.export_ = new JButton("Write to file");
		this.export_.setBounds(10, 10, 150, 20);
		this.export_.setVisible(true);
		this.export_.setLayout(null);
		this.export_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayActivitymatrixPane.this.exportMat(true, "");
			}
		});
		this.optionsPanel_.add(this.export_);

		JButton applyOptions = new JButton("Apply");
		applyOptions.setBounds(450, 50, 100, 20);
		applyOptions.setVisible(true);
		applyOptions.setLayout(null);
		applyOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayActivitymatrixPane.this.showMatrix();
				PathwayActivitymatrixPane.this.repaint();
				PathwayActivitymatrixPane.this.showMatrix();
				PathwayActivitymatrixPane.this.repaint();
			}
		});
		this.optionsPanel_.add(applyOptions);

		JButton rebuild = new JButton("Rebuild");
		rebuild.setBounds(450, 75, 100, 20);
		rebuild.setVisible(true);
		rebuild.setLayout(null);
		rebuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayActivitymatrixPane.this.changed_ = true;
				PathwayActivitymatrixPane.this.createMatrix();
				PathwayActivitymatrixPane.this.showMatrix();
				PathwayActivitymatrixPane.this.invalidate();
				PathwayActivitymatrixPane.this.validate();
				PathwayActivitymatrixPane.this.repaint();

				PathwayActivitymatrixPane.this.showMatrix();
				PathwayActivitymatrixPane.this.repaint();
				for (int i = 0; i < Project.samples_.size(); i++) {
					Project.samples_.get(i).onoff = true;
				}
			}
		});
		this.optionsPanel_.add(rebuild);

		this.mouseOverP = new JPanel();
		this.mouseOverP.setBackground(Project.getBackColor_());
		this.mouseOverP.setBounds(700, 10, 500, 60);
		this.optionsPanel_.add(this.mouseOverP);

		this.mouseOverDisp = new JLabel("Additional Pathway-information");

		this.mouseOverDisp.setBounds(0, 0, 500, 60);
		this.mouseOverP.add(this.mouseOverDisp);
	}

	private void sortByLineSum() {
		Loadingframe lframe = new Loadingframe();
		lframe.bigStep("Sorting ecs");
		quicksortByLineSum(0, this.lineMatrix_.size() - 1);
		reverseMatrix();
		Loadingframe.close();
	}

	private void quicksortByLineSum(int low, int high) {
		int i = low, j = high;
		// if(i>=j){return;}
		double pivot = this.lineMatrix_.get(low + (high - low) / 2).sum_;
		while (i <= j) {
			while (this.lineMatrix_.get(i).sum_ < pivot) {
				i++;
			}
			while (this.lineMatrix_.get(j).sum_ > pivot) {
				j--;
			}
			if (i <= j) {
				switchLines(i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quicksortByLineSum(low, j);
		if (i < high)
			quicksortByLineSum(i, high);
	}

	private void switchLines(int index1, int index2) {
		Line line1 = (Line) this.lineMatrix_.get(index1);
		Line line2 = (Line) this.lineMatrix_.get(index2);

		this.lineMatrix_.set(index1, line2);
		this.lineMatrix_.set(index2, line1);
	}

	private void reverseMatrix() {
		int j = 0;
		for (int i = lineMatrix_.size() - 1; i > j; i--) {
			switchLines(i, j);
			j++;
		}
	}

	private void normalizeMax() {
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			normalizeRowByMaxEntry(smpCnt);
		}
	}

	private void normalizeRowsByAllEntries() {
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			normalizeRowByAllEntries(smpCnt);
		}
	}

	private void normalizeRowByMaxEntry(int index) {
		double highestEntry = 0.0D;
		for (int pathCnt = 0; pathCnt < this.numOfpaths; pathCnt++) {
			if (highestEntry < ((Line) this.lineMatrix_.get(pathCnt))
					.getEntry(index)) {
				highestEntry = ((Line) this.lineMatrix_.get(pathCnt))
						.getEntry(index);
			}
		}
		double newVal = 0.0D;
		for (int pathCnt = 0; pathCnt < this.numOfpaths; pathCnt++) {
			newVal = ((Line) this.lineMatrix_.get(pathCnt)).getEntry(index)
					/ highestEntry * 100.0D;
			((Line) this.lineMatrix_.get(pathCnt)).setEntry(index, newVal);
		}
	}

	private void normalizeRowByAllEntries(int index) {
		double sum = 0.0D;
		for (int pathCnt = 0; pathCnt < this.numOfpaths; pathCnt++) {
			sum += ((Line) this.lineMatrix_.get(pathCnt)).getEntry(index);
		}
		double newVal = 0.0D;
		for (int pathCnt = 0; pathCnt < this.numOfpaths; pathCnt++) {
			newVal = ((Line) this.lineMatrix_.get(pathCnt)).getEntry(index)
					/ sum * 100.0D;
			((Line) this.lineMatrix_.get(pathCnt)).setEntry(index, newVal);
		}
	}

	private void createMatrix() {
		this.numOfpaths = 0;
		this.lineMatrix_ = new ArrayList<Line>();
		for (int j = 0; j < this.proc_.getPathwayList_().size(); j++) {
			if (((PathwayWithEc) this.proc_.getPathwayList_().get(j))
					.isSelected()) {
				this.numOfpaths += 1;
			}
		}
		this.numOfSamples = Project.samples_.size();

		int sampleNr = Project.samples_.size();

		double[] tmpLine = new double[sampleNr];
		for (int pathCnt = 0; pathCnt < this.paths_.size(); pathCnt++) {
			tmpLine = new double[sampleNr];
			if (this.proc_.getPathway(
					((PathwayWithEc) this.paths_.get(pathCnt)).id_)
					.isSelected()) {
				for (int sampleCnt = 0; sampleCnt < sampleNr; sampleCnt++) {
					if (Project.samples_.get(sampleCnt).onoff) {
						PathwayWithEc tmpPath = (PathwayWithEc) ((Sample) Project.samples_
								.get(sampleCnt)).pathways_.get(pathCnt);
						double activity = 0.0D;
						double tmpScore;
						// double tmpScore;
						if (this.includeWeights_) {
							for (int ecCnt = 0; ecCnt < tmpPath.ecNrs_.size(); ecCnt++) {
								activity += ((EcNr) tmpPath.ecNrs_.get(ecCnt)).amount_
										* ((EcNr) tmpPath.ecNrs_.get(ecCnt)).weight_;
							}
							tmpScore = activity;
						} else {
							for (int ecCnt = 0; ecCnt < tmpPath.ecNrs_.size(); ecCnt++) {
								activity += ((EcNr) tmpPath.ecNrs_.get(ecCnt)).amount_;
							}
							tmpScore = activity;
						}
						tmpLine[sampleCnt] = tmpScore;
					}
				}
				this.lineMatrix_.add(new Line((PathwayWithEc) this.paths_
						.get(pathCnt), tmpLine));
			}
		}
		System.out.println("endOfcreate");
		this.changed_ = false;
	}

	public void showMatrix() {
		if (this.changed_) {
			createMatrix();
		}
		if (this.normalizeMax_.isSelected()) {
			normalizeMax();
		}
		if (this.normalizeSum_.isSelected()) {
			normalizeRowsByAllEntries();
		}
		if (this.sortByLine_.isSelected()) {
			sortByLineSum();
		}
		Loadingframe lframe = new Loadingframe();
		lframe.bigStep("repainting");
		lframe.step("removeAll");
		this.displayP_.removeAll();

		int col = 1;
		int coldist = 40;
		int xDist = 60;
		int namesWidth = 275;

		int scoreWidth = 150;
		int scoreLength = 6;

		int counter = 0;
		for (int i = 0; i < this.lineMatrix_.size(); i++) {
			if (((Line) this.lineMatrix_.get(i)).getPath_().isSelected()) {
				PathwayWithEc tmpPath = Project.overall_
						.getPath(((Line) this.lineMatrix_.get(i)).getPath_().id_);
				lframe.step(tmpPath.id_);
				PathButt pathNames = new PathButt(Project.samples_,
						Project.overall_, tmpPath, Project.getBackColor_(),
						Project.workpath_, 1);
				pathNames.setBounds(xDist - 5, col * coldist + coldist
						* counter, namesWidth, coldist);

				final PathwayWithEc fpath = new PathwayWithEc(tmpPath, false);
				pathNames.addMouseListener(new MouseListener() {
					public void mouseReleased(MouseEvent e) {
					}

					public void mousePressed(MouseEvent e) {
					}

					public void mouseExited(MouseEvent e) {
						PathwayActivitymatrixPane.this.mouseOverDisp
								.setVisible(false);

						PathwayActivitymatrixPane.this.mouseOverDisp
								.setText("Additional Pathway-information");
					}

					public void mouseEntered(MouseEvent e) {
						PathwayActivitymatrixPane.this.mouseOverDisp
								.setVisible(true);

						PathwayActivitymatrixPane.this.setAdditionalInfo(fpath);
					}

					public void mouseClicked(MouseEvent e) {
					}
				});
				pathNames.addMouseListener(new MouseListener() {
					public void mouseReleased(MouseEvent e) {
					}

					public void mousePressed(MouseEvent e) {
					}

					public void mouseExited(MouseEvent e) {
						if (PathwayActivitymatrixPane.this.infoFrame != null) {
							PathwayActivitymatrixPane.this.infoFrame
									.setText("Additional Pathway-information");
						}
						PathwayActivitymatrixPane.this.repaint();
					}

					public void mouseEntered(MouseEvent e) {
						PathwayActivitymatrixPane.this.setAdditionalInfo(fpath);
						PathwayActivitymatrixPane.this.repaint();
					}

					public void mouseClicked(MouseEvent e) {
					}
				});
				this.displayP_.add(pathNames);
				counter++;
			}
		}
		int sampleNr = Project.samples_.size();
		int pathNr = this.lineMatrix_.size();
		int xREAL = 0;
		for (int sampleCnt = 0; sampleCnt < sampleNr; sampleCnt++) {
			if (Project.samples_.get(sampleCnt).onoff) {
				final JLabel fullName = new JLabel(
						((Sample) Project.samples_.get(sampleCnt)).name_);
				int x = xDist + namesWidth + scoreWidth * (xREAL + 1);
				fullName.setBounds(x, (col - 1) * coldist - 5, 500, coldist);
				fullName.setForeground(((Sample) Project.samples_
						.get(sampleCnt)).sampleCol_);
				fullName.setVisible(false);
				this.displayP_.add(fullName);

				final JLabel smpName = new JLabel(
						((Sample) Project.samples_.get(sampleCnt)).name_);
				smpName.setBounds(0, 0, scoreWidth, 20);
				smpName.setForeground(((Sample) Project.samples_.get(sampleCnt)).sampleCol_);
				smpName.setVisible(true);

				final int smpCnt = sampleCnt;

				JPanel mouseOver = new JPanel();
				mouseOver.setBounds(x, (col - 1) * coldist - 5 + 25,
						scoreWidth, 20);
				mouseOver.setBackground(Project.getBackColor_());
				mouseOver.setLayout(null);
				mouseOver.addMouseListener(new MouseListener() {
					public void mouseReleased(MouseEvent e) {
					}

					public void mousePressed(MouseEvent e) {
					}

					public void mouseExited(MouseEvent e) {
						fullName.setVisible(false);
						smpName.setVisible(true);
					}

					public void mouseEntered(MouseEvent e) {
						fullName.setVisible(true);
						smpName.setVisible(false);
					}

					public void mouseClicked(MouseEvent e) {
						Project.samples_.get(smpCnt).onoff = false;
					}
				});
				this.displayP_.add(mouseOver);
				mouseOver.add(smpName);

				counter = 0;
				for (int pathCnt = 0; pathCnt < pathNr; pathCnt++) {
					if (((PathwayWithEc) ((Sample) Project.samples_
							.get(sampleCnt)).pathways_.get(pathCnt))
							.isSelected()) {
						String tmpString = String
								.valueOf(((Line) this.lineMatrix_.get(pathCnt))
										.getEntry(sampleCnt));
						if (tmpString.length() > scoreLength) {
							tmpString = tmpString.substring(0, scoreLength);
							lframe.step(tmpString);
						}
						JLabel scores = new JLabel(tmpString);
						scores.setForeground(((Sample) Project.samples_
								.get(sampleCnt)).sampleCol_);
						x = xDist + namesWidth + scoreWidth * (xREAL + 1);
						int y = col * coldist + coldist * counter;
						scores.setBounds(x, y, scoreWidth, coldist);
						this.displayP_.add(scores);
						counter++;
					}
				}
				xREAL++;
			}
		}
		for (int pathCnt = 0; pathCnt < pathNr; pathCnt++) {
			if (((Line) this.lineMatrix_.get(pathCnt)).sum_ == 0) {
				((Line) this.lineMatrix_.get(pathCnt)).setSum();
			}
			String tmpString = String.valueOf(((Line) this.lineMatrix_
					.get(pathCnt)).getSum_());
			if (tmpString.length() > scoreLength) {
				tmpString = tmpString.substring(0, scoreLength);
				lframe.step(tmpString);
			}
			JLabel scores = new JLabel(tmpString);
			scores.setForeground(Color.black);
			int x = xDist + namesWidth + scoreWidth * (xREAL + 1);
			int y = col * coldist + coldist * pathCnt;
			scores.setBounds(x, y, scoreWidth, coldist);
			this.displayP_.add(scores);
		}
		invalidate();
		validate();
		repaint();
		Loadingframe.close();
	}

	public void exportMat(boolean dialog, String path) {
		this.exp_ = new Exporter(this);
		double[][] matrix = new double[this.numOfSamples][this.numOfpaths];

		ArrayList<String> yDesc = new ArrayList<String>();
		for (int pathCnt = 0; pathCnt < this.lineMatrix_.size(); pathCnt++) {
			
			//remove special signal for using at Stamp.
			String newName=PathwayMatrix.removeSpecialsignal(this.lineMatrix_.get(pathCnt).getPath_().name_);
			
			yDesc.add(((Line) this.lineMatrix_.get(pathCnt)).getPath_().id_ + "-" + newName);
		}
		ArrayList<String> xDesc = this.exp_
				.getNameListFromSample(Project.samples_);
		for (int pathcnt = 0; pathcnt < this.lineMatrix_.size(); pathcnt++) {
			for (int sampleCnt = 0; sampleCnt < this.numOfSamples; sampleCnt++) {
				matrix[sampleCnt][pathcnt] = ((Line) this.lineMatrix_
						.get(pathcnt)).getEntry(sampleCnt);
			}
		}
		if (dialog) {
			if (this.exp_.saveDialog()) {
				this.exp_.exportDoubleMatrix(xDesc, yDesc, matrix,
						this.useCsf_.isSelected());
			}
		} else {
			File f = new File(path);
			f.mkdirs();
			if (!path.endsWith(".txt")) {
				Calendar cal = Calendar.getInstance();

				int day = cal.get(5);
				int month = cal.get(2) + 1;
				int year = cal.get(1);

				String date = day + "." + month + "." + year;
				path = path + File.separator + "pwActivity" + "."
						+ Project.workpath_ + "." + date;
			}
			String tmpPath = path + ".txt";
			File file1 = new File(tmpPath);
			if (file1.exists() && !file1.isDirectory()) {
				int i = 1;
				while ("Pigs" != "Fly") {// loop forever
					tmpPath = path + "-" + i + ".txt";
					File file2 = new File(tmpPath);
					if (file2.exists() && !file2.isDirectory()) {
						i++;
						continue;
					} else {
						path = path + "-" + i;
						break;
					}
				}
			}
			path = path + ".txt";
			this.exp_.path_ = path;
			this.exp_.exportDoubleMatrix(xDesc, yDesc, matrix,
					this.useCsf_.isSelected());
		}
	}

	private void setAdditionalInfo(PathwayWithEc path) {
		this.mouseOverDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_
				+ "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_
				+ "</html>");
		if (this.infoFrame != null) {
			this.infoFrame.setAdditionalInfo(path);
		}
	}

	public boolean isIncludeWeights_() {
		return this.includeWeights_;
	}

	public void setIncludeWeights_(boolean includeWeights_) {
		this.includeWeights_ = includeWeights_;
	}
}
