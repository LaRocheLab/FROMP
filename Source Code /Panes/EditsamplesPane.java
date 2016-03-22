package Panes;

import Objects.Project;
import Objects.Sample;
import Prog.CmdController1;
import Prog.Controller;
import Prog.DataProcessor;
import Prog.IndexButton;
import Prog.StringReader;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import java.awt.Dimension;

// This is the panel in between the start screen and the pathway selection screen where you are able to select 
// samples you want to work with and whether or not you want to do random sampling.

public class EditsamplesPane extends JPanel {
	private static final long serialVersionUID = 1L; 
	Project activeProj_; // Project object from which to add and remove samples
	Sample tmpSamp_; // A temporary sample object, to be used for adding samples to the project
	IndexButton[] colButton_; // Array of colour buttons for the samples
	IndexButton[] delButt_; // Array of buttons used to remove samples from the project
	IndexButton[] seqButt_; // Array of buttons used to add a sequence file to a sample
	JButton button_; // Select sample button
	JButton newColors_; // Set new colours button
	JButton randomColors_;// Set new random colors
	String lastPath_; // Temporary variable used to keep track of the last file path in the FileChooser
	MyChooser fChoose_; // The afforementioned FileChooser
	File lastFile; // The last file in the Project.samples_ arraylist
	JSlider slider; // Sample Colour Difference Slider
	int colChange; // Difference in colour from sample to sample if not interfiered with
	JLabel colDiff; 
	ArrayList<JCheckBox> checks_; // Checkboxes to the left of each sample. If not checked, sets sample.inuse to false
	public JCheckBox listCheck_; 
	JCheckBox chainCheck_; 
	JLabel listl_; 
	public JCheckBox randCheck_; // CheckBox for random sampling
	JLabel randl_; 
	public boolean dataChanged_; 
	public JButton advance_; 
	private JButton save_; 
	private JButton saveAs_; 
	private JButton loadMat_; 
	public JButton backButton_; 
	public JButton nextButton_; 
	ArrayList<JButton> names_; // ArrayList of buttons which contain the names of the samples
	StringReader reader; 
	int xCol2 = 800; 
	int iprCount = 0;

	public EditsamplesPane(Project activeProj) {
		this.reader = new StringReader();

		this.activeProj_ = activeProj;
		if (Project.samples_.size() > 0) {
			this.lastFile = new File(
					((Sample) Project.samples_.get(Project.samples_.size() - 1)).fullPath_);
		}
		this.lastPath_ = "";
		this.colChange = 15;
		this.colButton_ = new IndexButton[Project.samples_.size()];
		this.delButt_ = new IndexButton[Project.samples_.size()];
		this.seqButt_ = new IndexButton[Project.samples_.size()];
		this.fChoose_ = new MyChooser(this.lastPath_);
		this.checks_ = new ArrayList<JCheckBox>();

		this.backButton_ = new JButton("< Back to Project Menu");
		this.nextButton_ = new JButton("Go to Pathway Selection >");

		setLayout(null);
		setVisible(true);
		setBackground(Project.getBackColor_());
		setPreferredSize(new Dimension(getWidth(),
				(Project.samples_.size() + 2) * 50 + 100));
		setSize(getPreferredSize());
		prepPaint(); // Sets everything up for the EditSamplesPane
	}

	private void addSamples() {// Adds all the samples on screen as buttons
		setBackground(Project.getBackColor_());

		this.names_ = new ArrayList<JButton>();

		this.colButton_ = new IndexButton[Project.samples_.size()];
		this.delButt_ = new IndexButton[Project.samples_.size()];
		this.seqButt_ = new IndexButton[Project.samples_.size()];
		int nameL = 400;
		int colH = 20;
		int colD = 25;

		this.newColors_ = new JButton("Set New Colors");
		this.newColors_.setBounds(this.xCol2, 150, 150, 30);
		this.newColors_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditsamplesPane.this.convertChecks();
				EditsamplesPane.this.setNewColorSet();
				EditsamplesPane.this.prepPaint();
				Project.dataChanged = true;
			}
		});
		add(this.newColors_);
		
		this.randomColors_ = new JButton("Random Colors");
		this.randomColors_.setBounds(this.xCol2, 240, 150, 30);
		
		this.randomColors_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRandomSampleColor();
				EditsamplesPane.this.prepPaint();
				Project.dataChanged = true;
			}
		});
		
		add(this.randomColors_);

		loadSampleButton();
		if (Project.samples_.size() > 0) {
			JLabel label = new JLabel("col");
			label.setBounds(31, 23, 40, 20);
			add(label);

			label = new JLabel("name");
			label.setBounds(200, 23, 40, 20);
			add(label);

			label = new JLabel("valid");
			label.setBounds(61 + nameL, 23, 40, 20);
			add(label);

			label = new JLabel("del");
			label.setBounds(102 + nameL, 23, 40, 20);
			add(label);
			label = new JLabel("seq");
			label.setBounds(156 + nameL, 23, 40, 20);
			add(label);
		}
		for (int sampCnt = 0; sampCnt < Project.samples_.size(); sampCnt++) {
			final int cnt = sampCnt;
			this.tmpSamp_ = ((Sample) Project.samples_.get(sampCnt));

			JButton sampName = new JButton(this.tmpSamp_.name_);
			sampName.setBounds(60, 50 + colD * sampCnt, nameL, colH);
			sampName.setVisible(true);
			sampName.setLayout(null);
			sampName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SampleNameFrame nFrame = new SampleNameFrame(cnt,
							((Sample) Project.samples_.get(cnt)).name_);
					nFrame.addWindowListener(new WindowListener() {
						public void windowOpened(WindowEvent arg0) {
						}

						public void windowIconified(WindowEvent arg0) {
						}

						public void windowDeiconified(WindowEvent arg0) {
						}

						public void windowDeactivated(WindowEvent arg0) {
						}

						public void windowClosing(WindowEvent arg0) {
						}

						public void windowClosed(WindowEvent arg0) {
							EditsamplesPane.this.prepPaint();
						}

						public void windowActivated(WindowEvent arg0) {
						}
					});
				}
			});
			add(sampName);
			this.names_.add(sampName);

			JColorChooser colChoose_ = new JColorChooser();
			colChoose_.setBounds(0, 0, 400, 400);
			colChoose_.setVisible(true);

			this.colButton_[sampCnt] = new IndexButton(sampCnt);
			this.colButton_[sampCnt].setBackground(this.tmpSamp_.sampleCol_);
			this.colButton_[sampCnt].setBounds(30, 50 + colD * sampCnt, 20, 20);
			this.colButton_[sampCnt].setLayout(null);
			this.colButton_[sampCnt].setVisible(true);
			this.colButton_[sampCnt].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((Sample) Project.samples_.get(cnt)).sampleCol_ = JColorChooser
							.showDialog(null, "Choose SampleColor",
									EditsamplesPane.this.tmpSamp_.sampleCol_);
					EditsamplesPane.this.colButton_[cnt]
							.setBackground(((Sample) Project.samples_.get(cnt)).sampleCol_);
					Project.dataChanged = true;
					EditsamplesPane.this.prepPaint();
				}
			});
			add(this.colButton_[sampCnt]);

			this.seqButt_[sampCnt] = new IndexButton(sampCnt);
			//determine there are sequence file or not.
			if (Project.samples_.get(sampCnt).getSequenceFile() != null
					&& !Project.samples_.get(cnt).getSequenceFile().equals("")) {
				this.seqButt_[sampCnt].setText("?");
				this.seqButt_[sampCnt].setBackground(Color.yellow);
				if(CmdController1.checkSeqFileFormat(Project.samples_.get(sampCnt).getSequenceFile())){
					this.seqButt_[sampCnt].setText("Y");
					this.seqButt_[sampCnt].setBackground(Color.green);
				}
				
			} 
			else {
				this.seqButt_[sampCnt].setText("N");
				this.seqButt_[sampCnt].setBackground(Color.red);
			}
			this.seqButt_[sampCnt].setBounds(142 + nameL, 50 + colD * sampCnt, 50, 20);
			this.seqButt_[sampCnt].setVisible(true);
			
//			if (Project.samples_.get(cnt).getSequenceFile() != null
//					&& !Project.samples_.get(cnt).getSequenceFile().equals("")) {
//				this.seqButt_[sampCnt].setBackground(Color.green);
//			} 
//			else {
//				this.seqButt_[sampCnt].setBackground(Color.red);
//			}
			
			this.seqButt_[sampCnt].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Project.dataChanged = true;
					Project.dataChanged = true;
					String path_ = "";
					try {
						path_ = new File("").getCanonicalPath();
					} 
					catch (IOException e1) {
						e1.printStackTrace();
					}
					JFileChooser fChoose_ = new JFileChooser(path_);
					fChoose_.setFileSelectionMode(0);
					fChoose_.setBounds(100, 100, 200, 20);
					fChoose_.setVisible(true);
					File file = new File(path_);
					fChoose_.setSelectedFile(file);
					fChoose_.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							if ((f.isDirectory())
									|| (f.getName().toLowerCase()
											.endsWith(".fasta"))
									|| (f.getName().toLowerCase()
											.endsWith(".fas"))
									|| (f.getName().toLowerCase()
											.endsWith(".faa"))) {
								return true;
							}
							return false;
						}

						public String getDescription() {
							return ".fasta/.fas/.faa";
						}
					});
					// if did not save seq file path.
					if (fChoose_.showSaveDialog(EditsamplesPane.this
							.getParent()) == 0) {
						try {
							String path = fChoose_.getSelectedFile()
									.getCanonicalPath();
							Project.samples_.get(cnt).setSequenceFile(path);
						} 
						catch (IOException e1) {
							e1.printStackTrace();
						}
						EditsamplesPane.this.prepPaint();
					}
				}
			});
			add(this.seqButt_[sampCnt]);

			this.delButt_[sampCnt] = new IndexButton(sampCnt);
			this.delButt_[sampCnt].setText("x");
			this.delButt_[sampCnt].setBounds(86 + nameL, 50 + colD * sampCnt, 50, 20);
			this.delButt_[sampCnt].setVisible(true);
			this.delButt_[sampCnt].setBackground(Project.getBackColor_());
			this.delButt_[sampCnt].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Project.dataChanged = true;
					//if the .ipr file is deleted from the sample list, the user can put new .ipr file in or enter multiple individual samples
					if(Project.samples_.get(EditsamplesPane.this.delButt_[cnt].SampleIndex_).fullPath_.contains("ipr")){
						iprCount = 0;
					}
					
					EditsamplesPane.this.removeSample(EditsamplesPane.this.delButt_[cnt].SampleIndex_);
					EditsamplesPane.this.prepPaint();
				}
			});
			add(this.delButt_[sampCnt]);

			JCheckBox check = new JCheckBox();
			check.setBounds(10, 50 + colD * sampCnt, 17, 17);
			check.setBackground(Project.getBackColor_());
			if (this.tmpSamp_.inUse) {
				check.setSelected(true);
			}
			add(check);
			this.checks_.add(check);

			JPanel legitP = new JPanel();
			if ((this.tmpSamp_.legitSample) || (this.tmpSamp_.imported)) {
				legitP.setBackground(Color.green);
			} 
			else {
				legitP.setBackground(Color.red);
			}
			legitP.setBounds(63 + nameL, 50 + colD * sampCnt, 20, 20);
			add(legitP);
		}
	}

	private void loadSampleButton() {// Builds the Select Sample button
		this.button_ = new JButton();
		this.button_.setBounds(this.xCol2, 50, 150, 30);
		this.button_.setText("Select Sample");
		add(this.button_);

		this.button_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (EditsamplesPane.this.lastFile != null) {
					EditsamplesPane.this.fChoose_.setCurrentDirectory(EditsamplesPane.this.lastFile);
				} 
				else {
					EditsamplesPane.this.lastFile = new File("");
				}
				try {
					EditsamplesPane.this.lastPath_ = EditsamplesPane.this.fChoose_.getCurrentDirectory().getCanonicalPath();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				EditsamplesPane.this.fChoose_.setFileSelectionMode(0);
				EditsamplesPane.this.fChoose_.setVisible(true);
				EditsamplesPane.this.fChoose_.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if ((f.isDirectory())|| (f.getName().toLowerCase().endsWith(".txt"))|| (f.getName().toLowerCase().endsWith(".out"))
								|| (f.getName().toLowerCase().endsWith(".tsv"))||(f.getName().toLowerCase().endsWith(".tsv.cleaned"))
								|| (f.getName().toLowerCase().endsWith(".ipr"))||(f.getName().toLowerCase().endsWith(".txt.cleaned"))) {
							return true;
						}
						return false;
					}

					public String getDescription() {
						return ".txt, .out, .tsv, .tsv.cleaned, .ipr, .txt.cleaned";
					}
				});
				if (EditsamplesPane.this.fChoose_.showOpenDialog(EditsamplesPane.this.getParent()) == 0) {
					try {
						EditsamplesPane.this.lastPath_ = EditsamplesPane.this.fChoose_
								.getCurrentDirectory().toString();
						EditsamplesPane.this.lastFile = EditsamplesPane.this.fChoose_
								.getCurrentDirectory();
						//Checks to see if an .ipr file is already loaded. Only one .ipr file may exist in the sample list or errors will occur
						if(iprCount != 1){
							//Cannot have any other samples present when trying to load .ipr file
							if (Project.samples_.size() > 0 && EditsamplesPane.this.fChoose_.getSelectedFile().getCanonicalPath().contains(".ipr")) {
								openWarning(
										"Warning!",
										"<html><body>In order to load a multiple sample file there must be no "
										+ "other samples present. Please remove the current samples.</body></html>");
							} else {
								//Once .ipr file is added iprCount is made to 1, this stops user from adding anymore samples
								if(EditsamplesPane.this.fChoose_.getSelectedFile().getCanonicalPath().contains(".ipr")){
									iprCount = 1;
								}
								boolean legit = EditsamplesPane.this.testSampleFile(EditsamplesPane.this.fChoose_.getSelectedFile().getCanonicalPath());
								System.out.println(legit);
								Sample sample = new Sample(EditsamplesPane.this.fChoose_.getSelectedFile().getName(),
										EditsamplesPane.this.fChoose_.getSelectedFile().getCanonicalPath(),
										EditsamplesPane.this.getColor(Project.samples_.size()));
								sample.legitSample = legit;
								Project.samples_.add(sample);
								Project.dataChanged = true;
							}
						}
						else{
							openWarning(
									"Warning!",
									"<html><body>Only one multiple sample file can be loaded. Remove sample file "
									+ "if you intend to load samples individually</body></html>");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					EditsamplesPane.this.prepPaint();
				}
			}
		});
		if (Project.samples_.size() > 0 && iprCount == 1) {
			final JButton load_IPR = new JButton();
			load_IPR.setBounds(xCol2 - 200, 50, 150, 30);
			load_IPR.setText("Load IPR");
			load_IPR.setVisible(true);
			add(load_IPR);
			load_IPR.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Controller.loadPathways(true);
					EditsamplesPane.this.prepPaint();
					
				}
			});
		}
	}
	/*
	 * Converts the check boxes next to the samples into data stored about the samples. If the boxes
	 * are checked than inUse for that sample equals true, else false
	 */
	private void convertChecks() {
		System.out.println("Set new Colors - convert");
		for (int i = 0; i < this.checks_.size(); i++) {
			//System.out.println(((JCheckBox) this.checks_.get(i)).isEnabled());
			if (((JCheckBox) this.checks_.get(i)).isSelected()) {
				if ((i < Project.samples_.size())
						&& (!((Sample) Project.samples_.get(i)).inUse)) {
					this.dataChanged_ = true;
					((Sample) Project.samples_.get(i)).inUse = true;
				}
			} else if ((i < Project.samples_.size())
					&& (((Sample) Project.samples_.get(i)).inUse)) {
				((Sample) Project.samples_.get(i)).inUse = false;
			}
		}
		while (this.checks_.size() > 0) {
			this.checks_.remove(0);
		}
		Project.dataChanged = true;
	}
	/*
	 * adds the load EC-Matrix button. If pressed it opens a FileChooser. If a
	 * file is selected it trys Project.loadMat on the path of the selected file
	 * to add to the sample
	 */
	private void addMatrixButton() {
		this.loadMat_ = new JButton("Load EC-matrix");
		this.loadMat_.setBounds(this.xCol2, 85, 150, 30);
		this.loadMat_.setLayout(null);
		this.loadMat_.setVisible(true);
		this.loadMat_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (EditsamplesPane.this.lastFile != null) {
					EditsamplesPane.this.fChoose_
							.setCurrentDirectory(EditsamplesPane.this.lastFile);
				} else {
					EditsamplesPane.this.lastFile = new File("");
				}
				try {
					EditsamplesPane.this.lastPath_ = EditsamplesPane.this.fChoose_
							.getCurrentDirectory().getCanonicalPath();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				EditsamplesPane.this.fChoose_.setFileSelectionMode(0);
				EditsamplesPane.this.fChoose_.setVisible(true);
				EditsamplesPane.this.fChoose_.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if ((f.isDirectory())
								|| (f.getName().toLowerCase().endsWith(".txt"))
								|| (f.getName().toLowerCase().endsWith(".out"))) {
							return true;
						}
						return false;
					}

					public String getDescription() {
						return ".txt";
					}
				});
				if (EditsamplesPane.this.fChoose_
						.showOpenDialog(EditsamplesPane.this.getParent()) == 0) {
					EditsamplesPane.this.lastPath_ = EditsamplesPane.this.fChoose_
							.getCurrentDirectory().toString();
					EditsamplesPane.this.lastFile = EditsamplesPane.this.fChoose_
							.getCurrentDirectory();
					try {
						EditsamplesPane.this.activeProj_.loadMat(
								EditsamplesPane.this.fChoose_.getSelectedFile()
										.getCanonicalPath(),
								EditsamplesPane.this.fChoose_.getSelectedFile()
										.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
					Project.dataChanged = true;
					EditsamplesPane.this.prepPaint();
				}
			}
		});
		add(this.loadMat_);

		JButton help = new JButton("?");
		help.setBounds(this.xCol2 + 150, 85, 50, 30);
		help.setLayout(null);
		help.setVisible(true);
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String Text = "<html><body>Add Ec-Matrix to the programm<br>a line in the input file should be of this form<br>(1.2.1.12,29,15,0)"
						+ "<br>Warning: no empty fields and no blanks</body></html>";
				openWarning("Help", Text);
				//HelpFrame helpF = new HelpFrame(Text);
			}
		});
		add(help);
	}

	private void addSaveButtons() {
		this.save_ = new JButton("Save project");
		this.save_.setBounds(this.xCol2, 500, 150, 30);
		this.save_.setLayout(null);
		this.save_.setVisible(true);
		this.save_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Project.dataChanged = true;
				EditsamplesPane.this.setValues();
				Controller.loadPathways(true);
				Controller.saveProject();
			}
		});
		add(this.save_);

		this.saveAs_ = new JButton("Save project as");
		this.saveAs_.setBounds(this.xCol2, 535, 150, 30);
		this.saveAs_.setLayout(null);
		this.saveAs_.setVisible(true);
		this.saveAs_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Project.dataChanged = true;
				EditsamplesPane.this.setValues();
				Controller.loadPathways(true);
				String path_ = "";
				try {
					path_ = new File("").getCanonicalPath();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				JFileChooser fChoose_ = new JFileChooser(path_ + File.separator
						+ "projects");
				fChoose_.setFileSelectionMode(0);
				fChoose_.setBounds(100, 100, 200, 20);
				fChoose_.setVisible(true);
				File file = new File(path_ + File.separator + "projects");
				fChoose_.setSelectedFile(file);
				fChoose_.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if ((f.isDirectory())
								|| (f.getName().toLowerCase().endsWith(".frp"))) {
							return true;
						}
						return false;
					}

					public String getDescription() {
						return ".frp";
					}
				});
				if (fChoose_.showSaveDialog(EditsamplesPane.this.getParent()) == 0) {
					try {
						String path = fChoose_.getSelectedFile()
								.getCanonicalPath();
						if (!path.endsWith(".frp")) {
							path = path + ".frp";
							System.out.println(".frp");
						}
						Controller.project_.exportProj(path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					EditsamplesPane.this.invalidate();
					EditsamplesPane.this.validate();
					EditsamplesPane.this.repaint();
				}
				System.out.println("Save");
			}
		});
		add(this.saveAs_);
	}

	private void addBackNext() {// Adds the "Back to project Menu" and "Go to pathway selection" buttons
		this.backButton_.setBounds(this.xCol2 - 135, 570, 250, 30);
		add(this.backButton_);

		this.nextButton_.setBounds(this.xCol2 + 125, 570, 250, 30);
		add(this.nextButton_);
	}

	private void removeSample(int sampIndex) {
		Project.removeSample(sampIndex);
	}
	//set random colors for all samples.
	private void setRandomSampleColor(){
		
		for (int sampCnt = 0; sampCnt < Project.samples_.size(); sampCnt++) {
			Project.samples_.get(sampCnt).sampleCol_= new Color((float) Math.random(),(float) Math.random(),(float) Math.random());	
		}
		System.out.println("Set Random Colors - convert");	
	}
	private void setNewColorSet() {
		for (int sampCnt = 0; sampCnt < Project.samples_.size(); sampCnt++) {
			this.tmpSamp_ = ((Sample) Project.samples_.get(sampCnt));
			this.tmpSamp_.sampleCol_ = getColor(sampCnt);
		}
	}

	private void addColDiff() {
		if (this.colDiff == null) {
			this.colDiff = new JLabel("Sample Color Difference");
		}
		this.colDiff.setLayout(null);
		this.colDiff.setBounds(this.xCol2, 180, 200, 30);
		add(this.colDiff);
		if (this.slider == null) {
			this.slider = new JSlider(10, 100, this.colChange);
		}
		this.slider.setLayout(null);
		this.slider.setBackground(Project.getBackColor_());
		this.slider.setBounds(this.xCol2, 210, 150, 30);
		this.slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				EditsamplesPane.this.colChange = ((JSlider) arg0.getSource())
						.getValue();
			}
		});
		add(this.slider);
	}

	private Color getColor(int pos) {
		Color col = new Color(255, 0, 0);
		for (int j = 0; j < pos; j++) {
			if (col.getBlue() <= 255 - this.colChange) {
				col = new Color(col.getRed() - this.colChange, col.getGreen(),
						col.getBlue() + this.colChange);
			} else if (col.getGreen() <= 255 - this.colChange) {
				col = new Color(col.getRed(), col.getGreen() + this.colChange,
						col.getBlue());
			}
		}
		return col;
	}

	public void prepPaint() {
		removeAll(); // Clears the back panel
		addSamples(); // Adds samples if there are any in the current project
		addColDiff(); // Sets up the colour slider
		showRandomSampling(); // Sets up the random sampling button and if it is selected puts the project in to random sampling mode
		addMatrixButton(); // Sets up the Add EC-Matrix button, as well as the help button next to it. If it is pressed the EC-matrix is parsed in this sample object
		addBackNext(); // Adds the back and next buttons
		invalidate(); // After this the method rebuilds the back panel with what has been done
		validate(); 
		repaint(); 
	}

	private void setValues() {
		convertChecks();

		copyNames();
		Project.dataChanged = true;
	}

	private void copyNames() {
		for (int nCnt = 0; nCnt < this.names_.size(); nCnt++) {
			((Sample) Project.samples_.get(nCnt)).name_ = ((JButton) this.names_
					.get(nCnt)).getText();
		}
	}

	private void showRandomSampling() {// Shows the random sampling checkbox. If checked sets ranMode_ in Project object
		if (this.randCheck_ == null) {
			this.randCheck_ = new JCheckBox();
			this.randl_ = new JLabel("Random Sampling:");
			this.randCheck_.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Project.randMode_ = EditsamplesPane.this.randCheck_
							.isSelected();
					EditsamplesPane.this.dataChanged_ = true;
					Project.dataChanged = true;
					if (EditsamplesPane.this.randCheck_.isSelected()) {
						EditsamplesPane.this
								.openWarning(
										"Warning!",
										"<html><body>Be carefull with random sampling and saving afterwards <br> only the reduced set of enzymes will be saved.</body></html>");
					}
				}
			});
		}
		this.randl_.setBounds(this.xCol2, 320, 200, 20);
		add(this.randl_);
		this.randCheck_.setBounds(this.xCol2, 350, 20, 20);
		this.randCheck_.setBackground(Project.getBackColor_());
		if (this.activeProj_ != null) {
			this.randCheck_.setSelected(Project.randMode_);
		}
		add(this.randCheck_);
	}
	//opens a warning frame, with the input title as the title and the input string as the body
	private void openWarning(String title, String string) {
		JOptionPane.showMessageDialog(null,string, 
			    title, JOptionPane.WARNING_MESSAGE);
	}
	/*
	 * Tests the first five lines of a file, if none of them can be read the file
	 * registers as invalid saying "no enzyme in sample line". If all can be read,
	 * registers as valid. Anywhere in between, registers as valid but offers a 
	 * warning that only x of 5 were read
	 */
	private boolean testSampleFile(String samplePath) throws IOException {
		int retries = 5;
		int goodLines = 0;
		boolean IPR = false;
		
		BufferedReader sample = this.reader.readTxt(samplePath);
		DataProcessor tmpProc = new DataProcessor();
		for (int i = 0; i < retries; i++) {
			String zeile = sample.readLine();
			//System.out.println("Line " + zeile);
			if (zeile == null) {
				break;
			}
			//added to ensure that files lines that contain many samples or those that are missing IPR are skipped
			if(zeile.contains(">")){
				IPR = true;
				zeile = sample.readLine();
			}
			if (!zeile.contains("IPR") && IPR == true) {
				while (!zeile.contains("IPR")) {
					zeile = sample.readLine();
				}
			}	
			String[] newEnz = tmpProc.getEnzFromSample(zeile);
			if (!tmpProc.enzReadCorrectly(newEnz)) {
				newEnz = tmpProc.getEnzFromRawSample(zeile);
				
			}
			if (!tmpProc.enzReadCorrectly(newEnz)) {
				newEnz = tmpProc.getEnzFromInterPro(zeile);
				if(newEnz != null){
				}
			}
			if (!tmpProc.enzReadCorrectly(newEnz)) {
				System.err.println("no enzyme in sample line");
				System.err.println(zeile);
			} else {
				goodLines++;
			}
		}
		if (goodLines > 0) {
			if (goodLines < retries) {
				openWarning("Warning!",
						"<html><body>The selected samplefile may not be correct.<br>Out of the first "
								+ retries + " lines " + (retries - goodLines)
								+ " could not be read correctly.<br>"
								+ "Please check your file.</body></html>");
			} 
			return true;
		}
		openWarning(
				"Warning!",
				"<html><body>The selected samplefile could not be read correctly.<br>Please check your file.</body></html>");

		return false;
	}
}
