package Panes;

import Objects.EcNr;
import Objects.EcWithPathway;
import Objects.Line;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import Prog.DataProcessor;
import Prog.PathButt;
import Panes.ActMatrixPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

public class PathwayEcMat extends JPanel {
	private static final long serialVersionUID = 1L;
	//An array list of array lists of line objects. each array list of line objects is a matrix so it is an arrayList of matrices 
	private ArrayList<ArrayList<Line>> arrays_; 
	private double[] pwSums_; // An array or doubles which serves as the last online of the matrix. Each element is the sum of its corresponding line
	ArrayList<PathwayWithEc> origPaths_; // ArrayList of pathways
	Project actProj_; // The active project
	JCheckBox sortPathesBySum_; // Check box to sort paths by sum... I have no idea why paths has an 'e' in it though... I didn't write that variable
	JCheckBox sortEcsBySum_; // Check box to sort Ec's by sum
	boolean dataChanged_; // Boolean to state whether or not the data has been changed
	int activesamps_; // The number of active samples
	DataProcessor proc_; // DataProcessor object to parse through the sample files and do the heavy computations required to build this matrix
	ActMatrixPane actMat_;
	boolean allset = true; 
	int xWidth; 
	int yOffset_; 
	int linecnt_ = 0; 
	private JPanel optionsPanel_; // The top panel which contains all the options for formatting the matrix
	private JPanel displayP_; // The display panel where the matrix is portrayed
	/*
	 * Scroll panel that the display panel sits on. If the display panel
	 * becomes to large for the space it is allotted this allows the user
	 * to scroll through the matrix
	 */
	private JScrollPane showJPanel_; 
	private JLabel mouseOverDisp; // A label which displays more information about what the user is mousing over
	private JPanel mouseOverP; 
	private JCheckBox useCsf_; 
	JPopupMenu ecMenuPopup;
	boolean firstTime = true; 
	private JButton rebuild; // A button added to the options panel used to fully rebuild this matrix as well as the options
	int origCnt1;
	public PathwayEcMat(ArrayList<PathwayWithEc> origPaths, Project actProj,
			DataProcessor proc, Dimension dim) {
		System.out.println("pathwayecmat"); 
		this.dataChanged_ = false; 
									
		this.origPaths_ = origPaths; 
		this.actProj_ = actProj; 
		this.proc_ = proc; 
							
		this.xWidth = (4000 + Project.samples_.size() * 300); 
																	
		createArrays(); // Creates the array list of array lists. Does not fill the inner arrayLists with line objects
		fillArrays(); // Builds the line objects and fills the array lists with line objects
										
		setSize(dim); 
		setVisible(true); 
		setLayout(new BorderLayout()); 
		setBackground(Project.getBackColor_()); 
												
		this.sortPathesBySum_ = new JCheckBox("Sort paths by the sum of ECs"); 
		this.sortEcsBySum_ = new JCheckBox("Sort ECs by sum"); 
		this.sortPathesBySum_.setSelected(false); 
		this.sortEcsBySum_.setSelected(false); 
												
		prepaint(); // Removes everything from the back panel, initiates the new panels and draws the Ec matrix on screen

		System.out.println("finished 0");
		this.allset = true;
	}
	
	

	public ActMatrixPane getActMat_() {
		return actMat_;
	}



	public void setActMat_(ActMatrixPane actMat_) {
		this.actMat_ = actMat_;
	}



	private void prepaint() {
		removeAll(); // Removes everything from the back panel
		initMainPanels(); // Initiates the options panel, the display panel, and the scroll panel
		addOptions(); // Adds buttons, checkboxes, and labels to the options panel
		drawArr(); // Draws the matrix to the screen
		invalidate(); // Rebuilds the backpanel
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
				+ Project.samples_.size() * 300, 4000 + 12 * this.linecnt_));
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

	private void createArrays() {
		Loadingframe lframe = new Loadingframe();
		lframe.bigStep("creating Arrays");
		this.activesamps_ = 0;
		for (int x = 0; x < Project.samples_.size(); x++) {
			if ((((Sample) Project.samples_.get(x)).inUse)
					&& (Project.samples_.get(x).onoff)) {
				this.activesamps_ += 1;
			}
		}
		//pathway list
		this.arrays_ = new ArrayList<ArrayList<Line>>();
		//Ec # lines in each pathway.
		ArrayList<Line> array = new ArrayList<Line>();
		//creat empty pathway list
		for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++) {
			array = new ArrayList<Line>();
			this.arrays_.add(array);
		}
		Loadingframe.close();
	}

	private void fillArrays() {
		this.linecnt_ = 0;
		Loadingframe lframe = new Loadingframe();
		lframe.bigStep("creating Arrays");

		this.pwSums_ = new double[this.arrays_.size()];
		//pathway 
		for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++) {
			int activCnt = 0;
			//ec line
			for (int ecCnt = 0; ecCnt < ((PathwayWithEc) this.origPaths_
					.get(origCnt)).ecNrs_.size(); ecCnt++) {
				EcNr ecNr = (EcNr) ((PathwayWithEc) this.origPaths_
						.get(origCnt)).ecNrs_.get(ecCnt);
				double[] arr = new double[this.activesamps_];
				int xREAL = 0;
				for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
					if ((((Sample) Project.samples_.get(smpCnt)).inUse)
							&& (Project.samples_.get(smpCnt).onoff)) {
						EcWithPathway ecwp = ((Sample) Project.samples_
								.get(smpCnt)).getEc(ecNr.name_);
						if (ecwp != null) {
							arr[xREAL] = ecwp.amount_;
							activCnt += ecwp.amount_;
						}
						ecwp = null;
						xREAL++;
					}
				}
				Line line = new Line(ecNr, arr);
				((ArrayList) this.arrays_.get(origCnt)).add(line);
				this.linecnt_ += 1;
				line.setSum();
				ecNr = null;
			}
			this.pwSums_[origCnt] = activCnt;
		}
		Loadingframe.close();
	}

	private void sortEcsBySum() {
		// boolean changed = true;

		Loadingframe lframe = new Loadingframe();

		lframe.bigStep("sorting ecs");
		
		for (int pthCnt = 0; pthCnt < this.arrays_.size(); pthCnt++) {
			ArrayList<Line> arr1 = this.arrays_.get(pthCnt);

			// changed = true;

			lframe.step("Sorting path:"
					+ ((PathwayWithEc) this.origPaths_.get(pthCnt)).id_);
			quicksortEcsBySum(arr1, 0, arr1.size() - 1);
			int j = 0;
			for (int i = arr1.size() - 1; i > j; i--) {
				switchLines(arr1, i, j);
				j++;
			}
		}

		Loadingframe.close();
		System.out.println("sortedbysum");
	}

	private void quicksortEcsBySum(ArrayList<Line> arr, int low, int high) {
		int i = low, j = high;
		if (i >= j) {
			return;
		}
		double pivot = (arr.get(low + (high - low) / 2)).sum_;
		while (i <= j) {
			while (arr.get(i).sum_ < pivot) {
				i++;
			}
			while (arr.get(j).sum_ > pivot) {
				j--;
			}
			if (i <= j) {
				// lframe.step("Swaping: "+arr.get(i).sum_+" and "+arr.get(j).sum_);
				switchLines(arr, i, j);
				i++;
				j--;
			}
			// lframe.step();
		}
		if (low < j)
			quicksortEcsBySum(arr, low, j);
		if (i < high)
			quicksortEcsBySum(arr, i, high);
	}

	private void sortEcsByName() {// this method was implemented using bubble
									// sort, I have since modified it to work as
									// a quicksort method instead
		boolean changed = true;

		Loadingframe lframe = new Loadingframe();

		lframe.bigStep("sorting ecs");
		for (int pthCnt = 0; pthCnt < this.arrays_.size(); pthCnt++) {
			ArrayList<Line> arr1 = (ArrayList) this.arrays_.get(pthCnt);
			// changed = true;
			lframe.step("Sorting path:"
					+ ((PathwayWithEc) this.origPaths_.get(pthCnt)).id_);
			quicksortEcsByName(arr1, 0, arr1.size() - 1);
		}
		Loadingframe.close();
		System.out.println("sortbyName");
	}

	private void quicksortEcsByName(ArrayList<Line> arr, int low, int high) {
		int i = low, j = high;
		if (i >= j) {
			return;
		}
		String pivot = arr.get(high - 1).getEcNr_().name_;
		while (i <= j) {
			while (arr.get(i).getEcNr_().name_.compareTo(pivot) < 0) {
				i++;
			}
			while (arr.get(j).getEcNr_().name_.compareTo(pivot) > 0) {
				j--;
			}
			if (i <= j) {
				// lframe.step("Swaping: "+arr.get(i).sum_+" and "+arr.get(j).sum_);
				switchLines(arr, i, j);
				i++;
				j--;
			}
			// lframe.step();
		}
		if (low < j)
			quicksortEcsByName(arr, low, j);
		if (i < high)
			quicksortEcsByName(arr, i, high);
	}

	private void sortPathsBySum() {
		Loadingframe lframe = new Loadingframe();
		boolean changed = true;
		double sum1 = 0.0D;
		double sum2 = 0.0D;
		lframe.bigStep("sorting paths");
		setsums();

		QuickSortPathsBySum(0, this.pwSums_.length - 1);
		int j = 0;
		for (int i = pwSums_.length - 1; i > j; i--) {
			switchPaths(i, j);
			j++;
		}
		Loadingframe.close();
	}

	private void QuickSortPathsBySum(int low, int high) {
		int i = low, j = high;
		double pivot = pwSums_[high - 1];

		while (i <= j) {
			while (pwSums_[i] < pivot) {
				i++;
			}
			while (pwSums_[j] > pivot) {
				j--;
			}
			if (i <= j) {
				switchPaths(i, j);
				i++;
				j--;
			}
		}
		if (low < j) {
			QuickSortPathsBySum(low, j);
		}
		if (i < high) {
			QuickSortPathsBySum(i, high);
		}
	}

	private void setsums() {
		double sum = 0.0D;
		for (int pathCnt = 0; pathCnt < this.arrays_.size(); pathCnt++) {
			sum = 0.0D;
			for (int ecCnt = 0; ecCnt < ((ArrayList) this.arrays_.get(pathCnt))
					.size(); ecCnt++) {
				sum += ((Line) ((ArrayList) this.arrays_.get(pathCnt))
						.get(ecCnt)).sum_;
			}
			this.pwSums_[pathCnt] = sum;
		}
	}

	private void sortPathsByName() {
		quicksortPathsByName(0, origPaths_.size() - 1);
	}

	private void quicksortPathsByName(int low, int high) {
		int i = low, j = high;
		if (i >= j) {
			return;
		}
		String pivot = this.origPaths_.get(low + (high - low) / 2).id_;
		while (i <= j) {
			while (this.origPaths_.get(i).id_.compareTo(pivot) < 0) {
				i++;
			}
			while (this.origPaths_.get(j).id_.compareTo(pivot) > 0) {
				j--;
			}
			if (i <= j) {
				switchPaths(i, j);
				i++;
				j--;
			}
		}
		if (low < j) {
			quicksortPathsByName(low, j);
		}
		if (i < high) {
			quicksortPathsByName(i, high);
		}
	}

	private void switchPaths(int index1, int index2) {
		double tmp = this.pwSums_[index1];
		this.pwSums_[index1] = this.pwSums_[index2];
		this.pwSums_[index2] = tmp;

		PathwayWithEc origPaths1 = (PathwayWithEc) this.origPaths_.get(index1);
		PathwayWithEc origPaths2 = (PathwayWithEc) this.origPaths_.get(index2);

		this.origPaths_.set(index1, origPaths2);
		this.origPaths_.set(index2, origPaths1);

		ArrayList<Line> line1 = this.arrays_.get(index1);
		ArrayList<Line> line2 = this.arrays_.get(index2);

		this.arrays_.set(index1, line2);
		this.arrays_.set(index2, line1);
	}

	private void writeOutAllArr(String path, boolean inCsf) {
		String seperator = "\t";
		if (inCsf) {
			seperator = ",";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++) {
				out.write(((PathwayWithEc) this.origPaths_.get(origCnt)).id_
						+ seperator);
				out.newLine();
				for (int oriEcCnt = 0; oriEcCnt < ((PathwayWithEc) this.origPaths_
						.get(origCnt)).ecNrs_.size(); oriEcCnt++) {
					out.write(((EcNr) ((PathwayWithEc) this.origPaths_
							.get(origCnt)).ecNrs_.get(oriEcCnt)).name_
							+ ((EcNr) ((PathwayWithEc) this.origPaths_
									.get(origCnt)).ecNrs_.get(oriEcCnt))
									.nameSuppl() + seperator);
					for (int smpCnt = 0; smpCnt < this.activesamps_; smpCnt++) {
						if (((Line) ((ArrayList) this.arrays_.get(origCnt))
								.get(oriEcCnt)).getEntry(smpCnt) == 0.0D) {
							out.write("0" + seperator);
						} else {
							out.write(((Line) ((ArrayList) this.arrays_
									.get(origCnt)).get(oriEcCnt))
									.getEntry(smpCnt)
									+ seperator);
						}
					}
					
					out.newLine();
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeOutSelectedArr(String path, boolean inCsf) {
		String seperator = "\t";
		if (inCsf) {
			seperator = ",";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++) {
				if (this.origPaths_.get(origCnt).selected == true){
					
					out.write(((PathwayWithEc) this.origPaths_.get(origCnt)).id_
							+ seperator);
					out.newLine();
					for (int oriEcCnt = 0; oriEcCnt < ((PathwayWithEc) this.origPaths_
							.get(origCnt)).ecNrs_.size(); oriEcCnt++) {
						out.write(((EcNr) ((PathwayWithEc) this.origPaths_
								.get(origCnt)).ecNrs_.get(oriEcCnt)).name_
								+ ((EcNr) ((PathwayWithEc) this.origPaths_
										.get(origCnt)).ecNrs_.get(oriEcCnt))
										.nameSuppl() + seperator);
						for (int smpCnt = 0; smpCnt < this.activesamps_; smpCnt++) {
							if (((Line) ((ArrayList) this.arrays_.get(origCnt))
									.get(oriEcCnt)).getEntry(smpCnt) == 0.0D) {
								out.write("0" + seperator);
							} 
							else {
								out.write(((Line) ((ArrayList) this.arrays_
										.get(origCnt)).get(oriEcCnt))
										.getEntry(smpCnt)
										+ seperator);
							}
						}
						out.newLine();
						
					}	
					out.newLine();
					//this.origPaths_.get(origCnt).selected = false;
				}	
			}
			out.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addOptions() {
		if (this.useCsf_ == null) {
			this.useCsf_ = new JCheckBox("CSF");
		}
		this.useCsf_.setVisible(true);
		this.useCsf_.setLayout(null);
		this.useCsf_.setBackground(this.optionsPanel_.getBackground());
		this.useCsf_.setForeground(Project.getFontColor_());
		this.useCsf_.setBounds(230, 50, 100, 20);
		this.optionsPanel_.add(this.useCsf_);
		//"Export ALL values" button at Pathway orientated section
		JButton export = new JButton("Export ALL values");
		export.setBounds(10, 10, 210, 20);
		export.setVisible(true);
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
							
						}
						PathwayEcMat.this.writeOutAllArr(path,
								PathwayEcMat.this.useCsf_.isSelected());
						System.out.println("File saved at: "+path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		//"Export SELECTED values" button at Pathway orientated section
		JButton export2 = new JButton("Export SELECTED values");
		export2.setBounds(10, 40, 210, 20);
		export2.setVisible(true);
		export2.addActionListener(new ActionListener() {
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
							
						}
						PathwayEcMat.this.writeOutSelectedArr(path,
								PathwayEcMat.this.useCsf_.isSelected());
						System.out.println("File saved at: "+path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		this.optionsPanel_.add(export);
		this.optionsPanel_.add(export2);
		this.sortPathesBySum_.setBounds(230, 10, 250, 20);
		this.sortPathesBySum_.setBackground(this.optionsPanel_.getBackground());

		this.optionsPanel_.add(this.sortPathesBySum_);

		this.sortEcsBySum_.setBounds(230, 30, 200, 20);
		this.sortEcsBySum_.setBackground(this.optionsPanel_.getBackground());

		this.optionsPanel_.add(this.sortEcsBySum_);

		JButton resort = new JButton("Resort");
		resort.setBounds(550, 10, 100, 25);
		resort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayEcMat.this.prepaint();
			}
		});
		this.optionsPanel_.add(resort);

		this.mouseOverP = new JPanel();
		this.mouseOverP.setBackground(Project.getBackColor_());
		this.mouseOverP.setBounds(700, 10, 500, 60);
		this.optionsPanel_.add(this.mouseOverP);

		this.mouseOverDisp = new JLabel("Additional Pathway-information");

		this.mouseOverDisp.setBounds(0, 0, 500, 60);
		this.mouseOverP.add(this.mouseOverDisp);

		this.rebuild = new JButton("Rebuild");
		this.rebuild.setBounds(550, 50, 100, 25);
		this.rebuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayEcMat.this.createArrays();
				PathwayEcMat.this.fillArrays();
				PathwayEcMat.this.prepaint();
				for (int i = 0; i < Project.samples_.size(); i++) {
					Project.samples_.get(i).onoff = true;
				}
			}
		});
		this.optionsPanel_.add(this.rebuild);

		System.out.println("finished 1");
	}
	//Ec activity -> Pathway orientated -> main chart
	public void drawArr() {
		double time = System.currentTimeMillis();
		System.out.println("drawArr");

		int xdist = 100;
		if (this.sortPathesBySum_.isSelected()) {
			sortPathsBySum();
			this.dataChanged_ = true;
		} 
		
		else {
			sortPathsByName();
		}
		this.allset = false;
		if (this.sortEcsBySum_.isSelected()) {
			sortEcsBySum();
		} 
		else if (this.firstTime) {
			this.firstTime = false;
		} 
		else {
			sortEcsByName();
		}
		Loadingframe lframe = new Loadingframe();
		lframe.bigStep("painting values");
		lframe.step("removing old vals");

		int lineCounter = 0;
		int arrCounter = 0;
		int arrH = 50;
		int smpNameSpace = 25;
		int smpSpaceCnt = 0;
		int lineH = 12;

		String line = "";

		lineCounter = 0;
		
		System.out.println("starting " + (System.currentTimeMillis() - time));
		for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++) {
			lframe.bigStep("drawing "
					+ ((PathwayWithEc) this.origPaths_.get(origCnt)).id_);
			if (((PathwayWithEc) this.origPaths_.get(origCnt)).isSelected()) {
				final PathwayWithEc path = Project.overall_
						.getPath(((PathwayWithEc) this.origPaths_.get(origCnt)).id_);
				PathButt pathButt = new PathButt(Project.samples_,
						Project.overall_, path, Project.getBackColor_(),
						Project.workpath_, 0);
				pathButt.setBounds(50, 10 + lineH * lineCounter + arrH
						* arrCounter + smpNameSpace * smpSpaceCnt, 400, 50);
				pathButt.setVisible(true);
				pathButt.setLayout(null);
				pathButt.addMouseListener(new MouseListener() {
					public void mouseReleased(MouseEvent e) {
					}

					public void mousePressed(MouseEvent e) {
					}

					public void mouseExited(MouseEvent e) {
						PathwayEcMat.this.mouseOverDisp.setVisible(false);

						PathwayEcMat.this.mouseOverDisp
								.setText("Additional Pathway-information");
					}

					public void mouseEntered(MouseEvent e) {
						PathwayEcMat.this.mouseOverDisp.setVisible(true);

						PathwayEcMat.this.setAdditionalInfo(path);
					}

					public void mouseClicked(MouseEvent e) {

					}
				});
				//pathway select box for exporting
				final JCheckBox tmpBox = new JCheckBox ();
				tmpBox.setBounds(30, 10 + lineH * lineCounter + arrH
						* arrCounter + smpNameSpace * smpSpaceCnt,20,20);
				tmpBox.setVisible(true);
				final int origCnt2=origCnt;
				tmpBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (tmpBox.isSelected()){
							origPaths_.get(origCnt2).selected = true;
						}
						else{
							origPaths_.get(origCnt2).selected = false;
						}
					}
				});
				this.displayP_.add(tmpBox);
				this.displayP_.add(pathButt);
				lineCounter++;
				line = "";
				smpSpaceCnt++;
				int smpNme = 0;
				for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
					if ((((Sample) Project.samples_.get(smpCnt)).inUse)
							&& (Project.samples_.get(smpCnt).onoff)) {
						final int sampleCount = smpCnt;
						final int sampleName = smpNme;
						lframe.step("drawing "
								+ ((Sample) Project.samples_.get(smpCnt)).name_);
						line = ((Sample) Project.samples_.get(smpCnt)).name_;
						final JLabel fullName = new JLabel(line);
						fullName.setBounds(20 + xdist + xdist * sampleName, 50
								+ lineH * lineCounter + arrH * arrCounter
								+ smpNameSpace * smpSpaceCnt - lineH, 500,
								lineH);
						fullName.setForeground(((Sample) Project.samples_
								.get(smpCnt)).sampleCol_);
						fullName.setVisible(false);
						fullName.setLayout(null);
						this.displayP_.add(fullName);

						final JLabel smpName = new JLabel(line);

						smpName.setBounds(0, 0, xdist, lineH);
						smpName.setForeground(((Sample) Project.samples_
								.get(smpCnt)).sampleCol_);
						smpName.setVisible(true);
						smpName.setLayout(null);

						JPanel mouseOver = new JPanel();
						mouseOver.setBounds(20 + xdist + xdist * sampleName, 50
								+ lineH * lineCounter + arrH * arrCounter
								+ smpNameSpace * smpSpaceCnt, xdist, lineH);
						mouseOver.setBackground(Project.getBackColor_());
						mouseOver.setVisible(true);
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
								Project.samples_.get(sampleCount).onoff = false;
							}
						});
						this.displayP_.add(mouseOver);
						mouseOver.add(smpName);
						smpNme++;
					}
				}
				lineCounter++;
				//
				for (int oriEcCnt = 0; oriEcCnt < ((PathwayWithEc) this.origPaths_
						.get(origCnt)).ecNrs_.size(); oriEcCnt++) {
					if (((Line) (this.arrays_.get(origCnt))
							.get(oriEcCnt)).getSum_() == 0.0D) {
						((Line) (this.arrays_.get(origCnt))
								.get(oriEcCnt)).setSum();
					}
					if (((Line) (this.arrays_.get(origCnt))
							.get(oriEcCnt)).sum_ > 0) {
						final int ecIndex = oriEcCnt;
						final int pathIndex = origCnt;

						line = this.proc_.findEcWPath(
								((Line) (this.arrays_.get(origCnt))
										.get(oriEcCnt)).getEcNr_())
								.getFullName();
						lframe.step("drawing " + line);
						JButton ecButt = new JButton(line);
						ecButt.setText(line);
						ecButt.setBounds(10, 50 + lineH * lineCounter + arrH
								* arrCounter + smpNameSpace * smpSpaceCnt,
								xdist, lineH);

						ecButt.setVisible(true);
						ecButt.setLayout(null);
						ecButt.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								
								
//								//it will get bug when sorting ecs by sum.
//								EcWithPathway ecWp = PathwayEcMat.this.proc_.getEc(PathwayEcMat.this.origPaths_
//												.get(pathIndex).ecNrs_
//												.get(ecIndex).name_);
								
								EcWithPathway ecWp = PathwayEcMat.this.proc_.getEc(proc_.findEcWPath(
										((Line) (arrays_.get(pathIndex))
												.get(ecIndex)).getEcNr_()).name_);
								
								if (ecWp != null) {
									new PwInfoFrame(ecWp,
											PathwayEcMat.this.actProj_,
											Project.overall_);
								} 
								
								
								else {
									System.out.println("no paths");
								}
							}
						});
						
						ecButt.addMouseListener(new MouseListener(){
							//ability to export seqence files and file lca from the pathway ec matrix
							public void mouseClicked(MouseEvent e) {
								if (SwingUtilities.isRightMouseButton(e)|| e.isControlDown()) {
									if (actMat_.includeRepseq_.isSelected()) {
										actMat_.ecMenuPopup.show(e.getComponent(), e.getX(),e.getY());
										JButton  button1 = (JButton) e.getComponent();
										System.out.println(button1.getText());
										actMat_.buttonName = button1.getText();
									}
								}
							}
							
							public void mousePressed(MouseEvent paramMouseEvent) {
							}
							
							public void mouseReleased(MouseEvent paramMouseEvent) {
							}
							
							public void mouseEntered(MouseEvent paramMouseEvent) {	
							}
							
							public void mouseExited(MouseEvent paramMouseEvent) {	
							}
						});
						this.displayP_.add(ecButt);
						for (int smpCnt = 0; smpCnt < this.activesamps_; smpCnt++) {
							if (((Line) ((ArrayList) this.arrays_.get(origCnt))
									.get(oriEcCnt)).getEntry(smpCnt) == 0.0D) {
								line = "0";
							} else {
								line = String
										.valueOf(((Line) ((ArrayList) this.arrays_
												.get(origCnt)).get(oriEcCnt))
												.getEntry(smpCnt));
							}
							JLabel value = new JLabel(line);
							value.setBounds(20 + xdist + xdist * smpCnt, 50
									+ lineH * lineCounter + arrH * arrCounter
									+ smpNameSpace * smpSpaceCnt, xdist, lineH);

							value.setForeground(Color.black);
							value.setVisible(true);
							value.setLayout(null);
							this.displayP_.add(value);
						}
						line = String.valueOf(((Line) ((ArrayList) this.arrays_
								.get(origCnt)).get(oriEcCnt)).getSum_());
						JLabel value = new JLabel(line);
						value.setBounds(20 + xdist + xdist * this.activesamps_,
								50 + lineH * lineCounter + arrH * arrCounter
										+ smpNameSpace * smpSpaceCnt, xdist,
								lineH);

						value.setForeground(Color.black);
						value.setVisible(true);
						value.setLayout(null);
						this.displayP_.add(value);
						lineCounter++;
					}
				}
				arrCounter++;
			}
		}
		System.out.println("finished2 " + (System.currentTimeMillis() - time));
		Loadingframe.close();
	}

	private void switchLines(ArrayList<Line> arr, int index1, int index2) {
		if (index2 < index1) {
			int tmp = index2;
			index2 = index1;
			index1 = tmp;
		}
		Line line1 = (Line) arr.get(index1);
		Line line2 = (Line) arr.get(index2);

		arr.set(index1, line2);
		arr.set(index2, line1);
	}

	private boolean id1Bigger(String id1, String id2) {
		if (id1.contentEquals("-1")) {
			return true;
		}
		if (id2.contentEquals("-1")) {
			return false;
		}
		for (int charCnt = 0; (charCnt < id1.length())
				&& (charCnt < id2.length()); charCnt++) {
			if (id1.charAt(charCnt) > id2.charAt(charCnt)) {
				return true;
			}
			if (id1.charAt(charCnt) < id2.charAt(charCnt)) {
				return false;
			}
		}
		return false;
	}

	public void setyOffset_(int yOffset_) {
		this.yOffset_ = yOffset_;
	}

	private void setAdditionalInfo(PathwayWithEc path) {
		this.mouseOverDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_
				+ "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_
				+ "</html>");
	}
}
