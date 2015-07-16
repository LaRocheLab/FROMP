package Panes;

import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import Prog.DataProcessor;
import Prog.PathButt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

// The "Show pathway-scores" tab of the pathway completeness analysis

public class SampleScorePane extends JPanel {
	private static final long serialVersionUID = 1L; 
	private DataProcessor proc_; // the data processor object used to parse through input files
	private static int counter = 0; 
	private int yoffset = 100; 
	private int linCnt = 0; 
	private int colCnt = 0; 
	private int linDis = 40; 
	private int colDis = 275; 
	private int lines = 31;
	private double maxVisScore; 
	private JLabel mouseOverDisp; 
	private JPanel mouseOverP; 
	private JPanel optionsPanel_; // the options panel upon which the options, ie buttons and chectboxes, are displayed
	private JPanel displayP_; // the display panel upon which the pathway scores will be displayed
	private JScrollPane showJPanel_; 
	private JCheckBox useCsf_; 
	private static MouseOverFrame infoFrame; 
	int xWidth; 

	public SampleScorePane(Sample overAll, Project actProj, DataProcessor proc,
			Dimension dim) {
		System.out.println("SampleScorePane");
		this.maxVisScore = Project.minVisScore_;
		this.proc_ = proc;
		this.xWidth = ((Project.samples_.size() + 3) * this.colDis);

		setLayout(new BorderLayout());
		setVisible(true);
		setBackground(Project.getBackColor_());
		setSize(dim);
		initMainPanels();

		addscrollOptions();

		addOptions();
		showMat();

		repaint();
	}

	private void initMainPanels() {
		this.optionsPanel_ = new JPanel();

		this.optionsPanel_
				.setPreferredSize(new Dimension(getWidth() - 20, 100));
		this.optionsPanel_.setLocation(0, 0);
		this.optionsPanel_.setBackground(Project.standard);
		this.optionsPanel_.setVisible(true);
		this.optionsPanel_.setLayout(null);
		add("First", this.optionsPanel_);

		this.displayP_ = new JPanel();
		this.displayP_.setLocation(0, 0);
		this.displayP_.setPreferredSize(new Dimension(2000, 2000));
		this.displayP_.setSize(new Dimension(2000, 2000));
		this.displayP_.setBackground(Project.getBackColor_());
		this.displayP_.setVisible(true);
		this.displayP_.setLayout(null);

		this.showJPanel_ = new JScrollPane(this.displayP_);
		this.showJPanel_.setVisible(true);
		this.showJPanel_.setVerticalScrollBarPolicy(20);
		this.showJPanel_.setHorizontalScrollBarPolicy(30);

		add("Center", this.showJPanel_);
	}

	private void addscrollOptions() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				e.getWheelRotation();
			}
		});
	}

	private void addOptions() {
		JButton left = new JButton("Previous Sample");
		left.setBounds(10, 10, 200, 25);
		left.setVisible(true);
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleScorePane.counter -= 1;
				if ((SampleScorePane.counter < 0)
						|| (SampleScorePane.counter > Project.samples_.size())) {
					SampleScorePane.counter = Project.samples_.size();
				}
				SampleScorePane.this.displayP_.removeAll();
				SampleScorePane.this.showMat();

				SampleScorePane.this.optionsPanel_.removeAll();
				SampleScorePane.this.addOptions();

				SampleScorePane.this.invalidate();
				SampleScorePane.this.validate();
				SampleScorePane.this.repaint();
			}
		});
		this.optionsPanel_.add(left);

		JButton right = new JButton("Next Sample");
		right.setBounds(10, 40, 200, 25);
		right.setVisible(true);
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleScorePane.counter += 1;
				if (SampleScorePane.counter > Project.samples_.size()) {
					SampleScorePane.counter = 0;
				}
				SampleScorePane.this.displayP_.removeAll();
				SampleScorePane.this.showMat();

				SampleScorePane.this.optionsPanel_.removeAll();
				SampleScorePane.this.addOptions();

				SampleScorePane.this.invalidate();
				SampleScorePane.this.validate();
				SampleScorePane.this.repaint();
			}
		});
		this.optionsPanel_.add(right);
		if (this.useCsf_ == null) {
			this.useCsf_ = new JCheckBox("CSF");
		}
		this.useCsf_.setVisible(true);
		this.useCsf_.setLayout(null);
		this.useCsf_.setBackground(this.optionsPanel_.getBackground());
		this.useCsf_.setForeground(Project.getFontColor_());
		this.useCsf_.setBounds(250, 50, 80, 15);
		this.optionsPanel_.add(this.useCsf_);

		JButton export = new JButton("Write to file");
		export.setBounds(250, 10, 130, 25);
		export.setVisible(true);
		export.setLayout(null);
		export.setForeground(Project.getFontColor_());
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fChoose_ = new JFileChooser(Project.workpath_);
				fChoose_.setFileSelectionMode(0);
				fChoose_.setBounds(100, 100, 200, 20);
				fChoose_.setVisible(true);
				File file = new File(Project.workpath_);
				fChoose_.setSelectedFile(file);
				fChoose_.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if ((f.isDirectory())
								|| (f.getName().toLowerCase().endsWith(".txt"))) {
							return true;
						}
						return false;
					}

					public String getDescription() {
						return ".txt";
					}
				});
				if (fChoose_.showSaveDialog(null) == 0) {
					try {
						String path = fChoose_.getSelectedFile()
								.getCanonicalPath();
						if (!path.endsWith(".txt")) {
							path = path + ".txt";
							System.out.println(".txt");
						}
						SampleScorePane.this.exportMat(path,
								SampleScorePane.this.useCsf_.isSelected());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		this.optionsPanel_.add(export);

		JButton openDetailFr = new JButton("Pathway-Info-Window");
		openDetailFr.setBounds(420, 10, 200, 25);
		openDetailFr.setVisible(true);
		openDetailFr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (SampleScorePane.infoFrame == null) {
					SampleScorePane.this.addMouseOverFrame();
				} else {
					SampleScorePane.infoFrame.closeFrame();
					SampleScorePane.infoFrame = null;
				}
			}
		});
		this.optionsPanel_.add(openDetailFr);

		Sample tmpSample = null;
		JLabel label;
		// JLabel label;
		if (counter == Project.samples_.size()) {
			tmpSample = Project.overall_;
			label = new JLabel("Sample 0: " + tmpSample.name_);
		} else {
			tmpSample = (Sample) Project.samples_.get(counter);
			label = new JLabel("Sample " + (counter + 1) + ": "
					+ tmpSample.name_);
		}
		label.setBounds(10, 70, 500, 20);
		this.optionsPanel_.add(label);

		this.mouseOverP = new JPanel();
		this.mouseOverP.setBackground(Project.getBackColor_());
		this.mouseOverP.setBounds(660, 10, 500, 60);
		this.optionsPanel_.add(this.mouseOverP);

		this.mouseOverDisp = new JLabel("Additional Pathway-information");

		this.mouseOverDisp.setBounds(0, 0, 500, 60);
		this.mouseOverP.add(this.mouseOverDisp);
	}

	private void showMat() {
		String tmpWeight = "";
		String tmpScore = "";

		Sample tmpSample = null;
		PathwayWithEc tmpPath = null;

		JButton pathButt = null;
		if (counter == Project.samples_.size()) {
			tmpSample = Project.overall_;
		} else {
			tmpSample = (Sample) Project.samples_.get(counter);
		}
		int listCount = 0;
//		if (Project.listMode_) {
//			tmpSample.pathways_ = sortPathwaysByScore(tmpSample.pathways_);
//		} else {
//			sortPathsById(tmpSample.pathways_);
//		}
		for (int pathCnt = 0; pathCnt < tmpSample.pathways_.size(); pathCnt++) {
			if (this.proc_.getPathway(((PathwayWithEc) tmpSample.pathways_.get(pathCnt)).id_).isSelected()) {
				tmpPath = (PathwayWithEc) tmpSample.pathways_.get(pathCnt);
				if (tmpPath.name_.contentEquals("testPath")) {
					System.err.println("PathButt");
				}
				tmpWeight = String.valueOf(tmpPath.weight_);
				if (((PathwayWithEc) tmpSample.pathways_.get(pathCnt))
						.isSelected()) {
					//hides any pathway score that is zero or below  tmpPath.score_ > 0
					if(tmpPath.score_ >= this.maxVisScore && tmpPath.score_ > 0) {
						if (tmpWeight.length() > 3) {
							tmpWeight = tmpWeight.substring(0, 3);
						}
						tmpScore = String.valueOf(tmpPath.score_);
						if (tmpScore.length() > 3) {
							tmpScore = tmpScore.substring(0, 3);
						}
						pathButt = new PathButt(Project.samples_, tmpSample,
								tmpPath, Project.getBackColor_(),
								Project.workpath_, 1);
						final PathwayWithEc fpath = new PathwayWithEc(tmpPath,
								false);
						pathButt.addMouseListener(new MouseListener() {
							public void mouseReleased(MouseEvent e) {
							}

							public void mousePressed(MouseEvent e) {
							}

							public void mouseExited(MouseEvent e) {
								SampleScorePane.this.mouseOverDisp
										.setVisible(false);

								SampleScorePane.this.mouseOverDisp
										.setText("Additional Pathway-information");
							}

							public void mouseEntered(MouseEvent e) {
								SampleScorePane.this.mouseOverDisp
										.setVisible(true);

								SampleScorePane.this.setAdditionalInfo(fpath);
							}

							public void mouseClicked(MouseEvent e) {
							}
						});
						if (Project.listMode_) {
							this.linCnt = (listCount % this.lines);
							this.colCnt = (listCount / this.lines);
						} else {
							this.linCnt = (pathCnt % this.lines);
							this.colCnt = (pathCnt / this.lines);
						}
						pathButt.setBounds(this.colCnt * this.colDis,
								this.linCnt * this.linDis + this.yoffset,
								this.colDis, this.linDis);
						this.displayP_.add(pathButt);
						listCount++;
					}
				}
			}
		}
	}

//	private ArrayList<PathwayWithEc> sortPathwaysByScore(ArrayList<PathwayWithEc> pathways) {
//		System.out.println("Sort by Score Sample");
////		int tmpCnt = 0;
////		for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++) {
////			tmpCnt = pathCnt;
////			for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
////				if (((PathwayWithEc) pathways.get(tmpCnt)).score_ < ((PathwayWithEc) pathways
////						.get(pathCnt2)).score_) {
////					tmpCnt = pathCnt2;
////				}
////			}
////			pathways.add(pathCnt, (PathwayWithEc) pathways.get(tmpCnt));
////			pathways.remove(tmpCnt + 1);
////		}
//		Collections.sort(pathways, new Comparator<PathwayWithEc>() {
//	        @Override public int compare(PathwayWithEc p1, PathwayWithEc p2) {
//	        	 if (p1.getScore() > p2.getScore())
//	                 return 1;
//	             if (p1.getScore() < p2.getScore())
//	                 return -1;
//	             return 0;
//	        }
//		});
//		Collections.reverse(pathways);
//		return pathways;
//	}
//
//	private void sortPathsById(ArrayList<PathwayWithEc> pathways) {
//		boolean changed = true;
//		Pathway path1 = null;
//		Pathway path2 = null;
//		int pathCnt = 0;
//		// for (; changed; pathCnt < pathways.size())
//		while (changed && (pathCnt < pathways.size())) {
//			changed = false;
//
//			// pathCnt = 0; continue;
//			path1 = (Pathway) pathways.get(pathCnt);
//			path2 = null;
//			for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
//				path2 = (Pathway) pathways.get(pathCnt2);
//				if (!path1.idBiggerId2(path2)) {
//					break;
//				}
//				PathwayWithEc origPaths1 = (PathwayWithEc) pathways
//						.get(pathCnt);
//				PathwayWithEc origPaths2 = (PathwayWithEc) pathways
//						.get(pathCnt2);
//
//				pathways.remove(pathCnt2);
//				pathways.remove(pathCnt);
//
//				pathways.add(pathCnt, origPaths2);
//				pathways.add(pathCnt2, origPaths1);
//
//				pathCnt++;
//				pathCnt2++;
//				changed = true;
//			}
//			pathCnt++;
//		}
//	}

	private void setAdditionalInfo(PathwayWithEc path) {
		this.mouseOverDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_
				+ "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_
				+ "</html>");
		if (infoFrame != null) {
			infoFrame.setAdditionalInfo(path);
		}
	}

	private void addMouseOverFrame() {
		if (MouseOverFrame.getFrameCount() <= 0) {
			infoFrame = new MouseOverFrame();
		}
	}

	private void exportMat(String path, boolean inCsf) {
		String seperator = "\t";
		if (inCsf) {
			seperator = ",";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			Sample tmpSample;
			// Sample tmpSample;
			if (counter == Project.samples_.size()) {
				tmpSample = Project.overall_;
			} else {
				tmpSample = (Sample) Project.samples_.get(counter);
			}
			out.write("**** n.s. = 'not selected' ******** n.a. = not above minScore");
			out.newLine();
			out.newLine();
			out.write("Sample Scores " + seperator + tmpSample.name_);
			out.newLine();
			out.newLine();
			ArrayList<PathwayWithEc> pathways = tmpSample.pathways_;
			for (int pathCnt = 0; pathCnt < pathways.size(); pathCnt++) {
				PathwayWithEc pathway = (PathwayWithEc) pathways.get(pathCnt);
				if ((pathway.isSelected())
						&& (pathway.score_ >= this.maxVisScore)&&(pathway.score_ > 0)) {
					out.write(pathway.id_ + seperator + pathway.name_
							+ seperator + pathway.score_);
					out.newLine();
				} else {
					if (!pathway.isSelected()) {
						out.write(pathway.id_ + seperator + pathway.name_
								+ " n.s.");
					} else {
						out.write(pathway.id_ + seperator + pathway.name_
								+ " n.a.");
					}
					out.newLine();
				}
			}
			out.newLine();

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
