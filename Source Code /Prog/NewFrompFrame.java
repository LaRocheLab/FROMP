package Prog;

import Objects.Project;
import Panes.AboutFrame;
import Panes.EcActPanes;
import Panes.EditsamplesPane;
import Panes.LCAPanes;
import Panes.PathwayActPanes;
import Panes.PathwaySelectP;
import Panes.PathwaysPane;
import Panes.PwSearchPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.accessibility.AccessibleContext;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import pathwayLayout.PathLayoutGrid;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * Main GUI frame for FROMP. Every part of the GUI FROMP is run through this
 * frame.
 * 
 * @author Jennifer Terpstra, Kevan Lynch
 */
public class NewFrompFrame extends JFrame {
	private static final long serialVersionUID = 7855981267252684730L; 
	private String separator_; // The file seperator used by your OS. ie '/' for Linux/Mac and '\' for Windows
	private String path_; // The canonical path for this folder
	private Color backCol_; // Backgound colour
	private Color sysColor_; // System colour
	private Color overallSampCol; //
	private JMenuBar menuBar_; // Menu bar at the top of Fromp. ie 'File' ,'Project' , 'Analyze' etc.
	private JMenu menu_; 
	private JPanel back_; // The back panel on this JFrame
	public static ArrayList<String> recentProj_; 
	/* The base path of the Fromp software nessesary for all relative paths to function*/
	final String basePath_ = new File(".").getAbsolutePath() + File.separator; 
	final String recentProjPath_ = "recentProj.txt"; // Path to the file which contains the recently opened projects
	private Controller control_; // The controller. Allows user to save, load etc.
	private JScrollPane showJPanel_; // Used only to be able to scroll through samples in the EditSamplesPane

	public NewFrompFrame() {
		this.separator_ = File.separator;

		setVisible(true);
		setLayout(new BorderLayout());
		setBounds(20, 20, 1200, 800);

		setDefaultCloseOperation(0);
		// Adds an action listner to the close button of the the JFrame which
		// prints "Window Closing" when it is closed and
		// opens a warning that you will lose all unsaved data by closing before
		// you close.
		addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent arg0) {
			}

			public void windowIconified(WindowEvent arg0) {
			}

			public void windowDeiconified(WindowEvent arg0) {
			}

			public void windowDeactivated(WindowEvent arg0) {
			}

			public void windowClosing(WindowEvent arg0) {
				NewFrompFrame.this.openClosingFrame();
			}

			public void windowClosed(WindowEvent arg0) {
				System.out.println("Window Closing");
			}

			public void windowActivated(WindowEvent arg0) {
			}
		});
		setTitle("FROMP-Alpha - Fragment Recruitment on Metabolic Pathways");

		this.backCol_ = new Color(233, 233, 233);
		this.sysColor_ = Color.black;
		this.overallSampCol = Color.red;

		this.control_ = new Controller(this.sysColor_);

		addBackPanel();
		try {
			this.path_ = new File("").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		addMenu(); // Adds the drop down menu
		loadRecentProj(); // Adds the list of "Recent Projects"
		showNewProjPanel(); // Shows the statring panel of Fromp. Includes 'Project Options' , and 'Recent Projects'
		invalidate(); // Next three commands in combination reload the JFrame
		validate(); 
		repaint(); 
	}

	private void addMenu() {// adds the drop down menu bar and all of its elements
		this.menuBar_ = new JMenuBar();

		addFileMenu(); // Adds the "File" drop down menu option
		addProjectMenu(); // Adds the "Project" drop down menu option
		addAnalyseMenu(); 
		addSettingsMenu(); 
		addDesignMenu(); 
		addHelpMenu(); 

		setJMenuBar(this.menuBar_);
	}

	private void addFileMenu() {
		this.menu_ = new JMenu("File");

		this.menu_.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		this.menuBar_.add(this.menu_);

		new JButton();
		new JTextField("Enter project name");

		JMenuItem miItem = new JMenuItem("New Project", 78);
		miItem.setAccelerator(KeyStroke.getKeyStroke(78, 8));
		miItem.getAccessibleContext().setAccessibleDescription(
				"Start a new project");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.newProjectName();
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Open Project", 79);
		miItem.setAccelerator(KeyStroke.getKeyStroke(79, 8));
		miItem.getAccessibleContext()
				.setAccessibleDescription("Open a project");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.openProj();
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Save Project", 83);
		miItem.setAccelerator(KeyStroke.getKeyStroke(83, 8));
		miItem.getAccessibleContext().setAccessibleDescription(
				"Save your project");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// NewFrompFrame.this.clearBack();
				Controller.loadPathways(true);
				String path = Controller.saveProject();
				System.out.println("Save");
				NewFrompFrame.addRecentPath(path);
				NewFrompFrame.this.saveRecentProj();
				// NewFrompFrame.this.clearBack();
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Save Project as", 65);
		miItem.setAccelerator(KeyStroke.getKeyStroke(65, 8));
		miItem.getAccessibleContext().setAccessibleDescription(
				"Export Project as");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.saveAs();
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Home", 86);
		miItem.setAccelerator(KeyStroke.getKeyStroke(86, 8));
		miItem.getAccessibleContext().setAccessibleDescription("Home");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.control_ = new Controller(
						NewFrompFrame.this.sysColor_);
				clearBack();
				addMenu();
				loadRecentProj();
				showNewProjPanel();
				invalidate();
				validate();
				repaint();
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Quit", 81);
		miItem.setAccelerator(KeyStroke.getKeyStroke(81, 8));
		miItem.getAccessibleContext().setAccessibleDescription("Quit");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openClosingFrame();
			}
		});
		this.menu_.add(miItem);

		invalidate();
		validate();
		repaint();
	}

	private void addProjectMenu() {
		this.menu_ = new JMenu("Project");

		this.menu_.getAccessibleContext().setAccessibleDescription(
				"Manage your Project");
		this.menuBar_.add(this.menu_);

		JMenuItem mItem = new JMenuItem("Edit Samples", 68);
		mItem.setAccelerator(KeyStroke.getKeyStroke(68, 8));
		mItem.getAccessibleContext().setAccessibleDescription(
				"Add Samples to your project");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.newEditSamples();
			}
		});
		this.menu_.add(mItem);

		mItem = new JMenuItem("Select Pathways", 87);
		mItem.setAccelerator(KeyStroke.getKeyStroke(87, 8));
		mItem.getAccessibleContext().setAccessibleDescription(
				"Select wanted Pathways");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (NewFrompFrame.this.control_.gotSamples()) {
					NewFrompFrame.this.selectPws();
				} else {
					warningFrame();
				}
			}
		});
		this.menu_.add(mItem);

		mItem = new JMenuItem("Search", 88);
		mItem.setAccelerator(KeyStroke.getKeyStroke(88, 8));
		mItem.getAccessibleContext().setAccessibleDescription("Search");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Search Pathway");
				if (NewFrompFrame.this.control_.gotSamples()) {
					Controller.loadPathways(true);
					NewFrompFrame.this.searchPathway();
				}
			}
		});
		this.menu_.add(mItem);
	}

	private void addAnalyseMenu() {
		this.menu_ = new JMenu("Analyze");

		this.menu_.getAccessibleContext().setAccessibleDescription("Analyze");
		this.menuBar_.add(this.menu_);

		JMenuItem mItem = new JMenuItem("Pathway Completeness", 67);
		mItem.setAccelerator(KeyStroke.getKeyStroke(67, 8));
		mItem.getAccessibleContext().setAccessibleDescription(
				"Pathway completeness score");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (NewFrompFrame.this.control_.gotSamples()) {
					NewFrompFrame.this.clearBack();
					Controller.loadPathways(true);
					if (!NewFrompFrame.this.control_.processor_
							.selectedPathways()) {
						warningFrame("No pathways selected!");
						selectPws();
					} else {
						NewFrompFrame.this.showPathwayScorePane();
					}
				} else {
					warningFrame();
				}
				System.out.println("Pathway orientated");

			}
		});
		this.menu_.add(mItem);

		mItem = new JMenuItem("Pathway Activity", 80);
		mItem.setAccelerator(KeyStroke.getKeyStroke(80, 8));
		mItem.getAccessibleContext().setAccessibleDescription(
				"Pathway Activity Analysis");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pathway Activity");
				if (NewFrompFrame.this.control_.gotSamples()) {
					NewFrompFrame.this.clearBack();
					Controller.loadPathways(true);
					if (!NewFrompFrame.this.control_.processor_
							.selectedPathways()) {
						warningFrame("No pathways selected!");
						selectPws();
					} else {
						NewFrompFrame.this.showPathwayActMatrix();
					}
				} else {
					warningFrame();
				}
			}
		});
		this.menu_.add(mItem);

		mItem = new JMenuItem("EC Activity", 69);
		mItem.setAccelerator(KeyStroke.getKeyStroke(69, 8));
		mItem.getAccessibleContext().setAccessibleDescription(
				"EC Activity Matrix");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (NewFrompFrame.this.control_.gotSamples()) {
					NewFrompFrame.this.clearBack();
					Controller.loadPathways(true);
					if (!NewFrompFrame.this.control_.processor_
							.selectedPathways()) {
						warningFrame("No pathways selected!");
						selectPws();
					} else {
						NewFrompFrame.this.showEcActPanes();
					}
				} else {
					warningFrame();
				}
				System.out.println("Compare Samples");
			}
		});
		this.menu_.add(mItem);
	}

	private void warningFrame() {
		JFrame wrngFrame = new JFrame();
		wrngFrame.setBounds(200, 200, 350, 100);
		wrngFrame.setLayout(null);
		wrngFrame.setVisible(true);

		JPanel backP = new JPanel();
		backP.setBounds(0, 0, 350, 100);
		backP.setLayout(null);
		wrngFrame.add(backP);

		JLabel label = new JLabel("Warning! No samples have been selected!");
		label.setBounds(25, 25, 300, 25);
		backP.add(label);
	}

	private void warningFrame(String strIN) {
		JFrame wrngFrame = new JFrame();
		wrngFrame.setBounds(200, 200, 350, 100);
		wrngFrame.setLayout(null);
		wrngFrame.setVisible(true);

		JPanel backP = new JPanel();
		backP.setBounds(0, 0, 350, 100);
		backP.setLayout(null);
		wrngFrame.add(backP);

		JLabel label = new JLabel("Warning! " + strIN);
		label.setBounds(25, 25, 300, 25);
		backP.add(label);
	}

	private void addSettingsMenu() {
		this.menu_ = new JMenu("Settings");

		this.menu_.getAccessibleContext().setAccessibleDescription("Settings");
		this.menuBar_.add(this.menu_);

		JMenuItem mItem = new JMenuItem("Background Color", 66);
		mItem.setAccelerator(KeyStroke.getKeyStroke(66, 8));
		mItem.getAccessibleContext().setAccessibleDescription(
				"Set Background Color");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.backCol_ = JColorChooser.showDialog(
						NewFrompFrame.this.getParent(),
						"Choose BackgroundColor", NewFrompFrame.this.backCol_);
				Project.setBackColor_(NewFrompFrame.this.backCol_);
				NewFrompFrame.this.back_
						.setBackground(NewFrompFrame.this.backCol_);
				// NewFrompFrame.this.clearBack();
				// NewFrompFrame.this.addBackPanel();
				// NewFrompFrame.this.back_.invalidate();
				// NewFrompFrame.this.back_.validate();
				// NewFrompFrame.this.back_.repaint();
				NewFrompFrame.this.invalidate();
				NewFrompFrame.this.validate();
				NewFrompFrame.this.repaint();
			}
		});
		this.menu_.add(mItem);

		mItem = new JMenuItem("System Color", 70);
		mItem.setAccelerator(KeyStroke.getKeyStroke(70, 8));
		mItem.getAccessibleContext().setAccessibleDescription(
				"Set System Color");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.sysColor_ = JColorChooser.showDialog(
						NewFrompFrame.this.getParent(), "Choose System Color",
						NewFrompFrame.this.sysColor_);
				Project.setFontColor_(NewFrompFrame.this.sysColor_);
				NewFrompFrame.this.invalidate();
				NewFrompFrame.this.validate();
				NewFrompFrame.this.repaint();
			}
		});
		this.menu_.add(mItem);

		mItem = new JMenuItem("Overall Sample Color", 67);
		mItem.setAccelerator(KeyStroke.getKeyStroke(67, 8));
		mItem.getAccessibleContext().setAccessibleDescription(
				"Set Overall Sample Color");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.overallSampCol = JColorChooser.showDialog(
						NewFrompFrame.this.getParent(), "Choose Overall Color",
						NewFrompFrame.this.overallSampCol);
				Project.setOverAllColor_(NewFrompFrame.this.overallSampCol);
			}
		});
		this.menu_.add(mItem);
	}

	private void addDesignMenu() {
		this.menu_ = new JMenu("Designer");

		this.menu_.getAccessibleContext().setAccessibleDescription("Designer");
		this.menuBar_.add(this.menu_);

		JMenuItem mItem = new JMenuItem("Open Designer", 71);
		mItem.setAccelerator(KeyStroke.getKeyStroke(71, 8));
		mItem.getAccessibleContext().setAccessibleDescription("About Fromp");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathLayoutGrid Grid = new PathLayoutGrid(10, 10, true);
			}
		});
		this.menu_.add(mItem);
	}

	private void addHelpMenu() {
		this.menu_ = new JMenu("Help");

		this.menu_.getAccessibleContext().setAccessibleDescription("Help");
		this.menuBar_.add(this.menu_);

		JMenuItem mItem = new JMenuItem("Help", 72);
		mItem.setAccelerator(KeyStroke.getKeyStroke(72, 8));
		mItem.getAccessibleContext().setAccessibleDescription("About Fromp");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String file = NewFrompFrame.this.basePath_.substring(0,
						NewFrompFrame.this.basePath_.length() - 2)
						+ "fromp-users-guide.pdf";
				System.out.println(file);
				NewFrompFrame.this.openPDf(file);
			}
		});
		this.menu_.add(mItem);

		mItem = new JMenuItem("About", 84);
		mItem.setAccelerator(KeyStroke.getKeyStroke(84, 8));
		mItem.getAccessibleContext().setAccessibleDescription("About Fromp");
		mItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutFrame about = new AboutFrame();
				System.out.println("About");
			}
		});
		this.menu_.add(mItem);
	}

	private void clearBack() {// updates the FROMP frame
								// System.out.println("ClearBack");
		this.back_.removeAll();
		if (Project.workpath_ != null) {
			setTitle("Project: "
					+ Project.workpath_
					+ " - FROMP-Alpha - Fragment Recruitment on Metabolic Pathways");
		} else {
			setTitle("FROMP-Alpha - Fragment Recruitment on Metabolic Pathways");
		}
		invalidate();
		validate();
		repaint();
	}

	private void addBackPanel() {
		this.back_ = new JPanel();
		this.back_.setVisible(true);
		this.back_.setLayout(new BorderLayout());
		this.back_.setLocation(0, 0);
		add(this.back_, "Center");
		this.back_.setSize(getMaximumSize());
		this.back_.setBackground(this.backCol_);
	}

	private void newEditSamples() {
		clearBack();
		this.control_.clearProcessor();
		Controller.dataChanged = true;
		Project.dataChanged = true;
		EditsamplesPane samplesP_ = new EditsamplesPane(Controller.project_);
		samplesP_.backButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.clearBack();
				NewFrompFrame.this.showNewProjPanel();
			}
		});
		samplesP_.nextButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!NewFrompFrame.this.control_.gotSamples()) {
					warningFrame();
				} else {
					NewFrompFrame.this.clearBack();

					NewFrompFrame.this.selectPws();
				}
			}
		});

		this.showJPanel_ = new JScrollPane(samplesP_);
		this.showJPanel_.setVisible(true);
		this.showJPanel_.setVerticalScrollBarPolicy(20);

		this.back_.add("Center", this.showJPanel_);

		invalidate();
		validate();
		repaint();
	}

	private void selectPws() {// Opens the pathway selection panel
		if (Controller.processor_ == null) {
			Controller.loadPathways(false);
		}
		clearBack();
		final PathwaySelectP selectP = new PathwaySelectP(
				Controller.processor_.getPathwayList_());
		selectP.back_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.clearBack();
				NewFrompFrame.this.newEditSamples();
			}
		});
		selectP.next_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean noPathways = true;
				if (selectP.pathSelected()) {
					noPathways = false;
				}
				if (!noPathways) {
					NewFrompFrame.this.clearBack();
					NewFrompFrame.this.showAnalyseOptions();
				} else {
					warningFrame("No pathways selected!");
				}
			}
		});
		this.back_.add(selectP);
		invalidate();
		validate();
		repaint();
	}

	private void showPathwayScorePane() {// Opens the pathway completeness
											// analysis
		clearBack();
		PathwaysPane pwPanes = new PathwaysPane(Controller.project_,
				Controller.processor_, getSize());
		pwPanes.backButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.clearBack();
				NewFrompFrame.this.showAnalyseOptions();
			}
		});
		this.back_.add(pwPanes);
		invalidate();
		validate();
		repaint();
	}

	private void showPathwayActMatrix() {// Opens the pathway activity analysis
		clearBack();
		PathwayActPanes actP = new PathwayActPanes(Controller.project_,
				Controller.processor_, getSize());
		actP.backButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.clearBack();
				NewFrompFrame.this.showAnalyseOptions();
			}
		});
		this.back_.add(actP);
		invalidate();
		validate();
		repaint();
	}

	private void showEcActPanes() {// Opens the ec activity analysis
		clearBack();
		EcActPanes matrixP_ = new EcActPanes(Controller.project_,
				Controller.processor_, getSize());
		matrixP_.backButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.clearBack();
				NewFrompFrame.this.showAnalyseOptions();
			}
		});
		this.back_.add(matrixP_);
		invalidate();
		validate();
		repaint();
	}
	//NEEDS EDITING
	private void showLCAPanes() {// Opens the ec activity analysis
		clearBack();
		LCAPanes matrixP_ = new LCAPanes(Controller.project_,
				Controller.processor_, getSize());
		matrixP_.backButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.clearBack();
				NewFrompFrame.this.showAnalyseOptions();
			}
		});
		this.back_.add(matrixP_);
		invalidate();
		validate();
		repaint();
	}

	private void searchPathway() {// Opens the search panel
		clearBack();
		this.control_.computeSampleScores();
		PwSearchPane pwSearch = new PwSearchPane(Controller.project_,
				Project.samples_, Project.overall_, getSize());
		this.back_.add(pwSearch);
		invalidate();
		validate();
		repaint();
	}

	private void newProjectName() {// Opens the new project panel
		this.back_.removeAll();

		JButton button = new JButton();
		final JTextField txField = new JTextField("Enter Project Name: ");

		JPanel newProjectPanel = new JPanel();
		newProjectPanel.setLayout(null);
		newProjectPanel.setPreferredSize(new Dimension(400, 100));
		newProjectPanel.setVisible(true);
		newProjectPanel.setBackground(this.back_.getBackground());

		this.back_.add("Center", newProjectPanel);

		txField.setBounds(100, 200, 200, 25);
		txField.setVisible(true);
		newProjectPanel.add(txField);

		button.setBounds(325, 200, 75, 25);
		button.setVisible(true);
		button.setText("Ok");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tmp = txField.getText();
				System.out.println(tmp);
				NewFrompFrame.this.control_.newProject(tmp);
				NewFrompFrame.this.clearBack();
				NewFrompFrame.this.newEditSamples();
			}
		});
		newProjectPanel.add(button);
		invalidate();
		validate();
		repaint();
	}

	private void showAnalyseOptions() {// Opens the analysis options panel
		int xcol1 = 100;
		int yOff = 100;
		int ySize = 30;
		int xsize = 400;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Project.getBackColor_());
		panel.setBounds(0, 0, 400, 800);

		JButton s = new JButton("< Back to PathwaySelction");
		s.setBounds(xcol1, yOff - ySize, xsize, ySize);
		s.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewFrompFrame.this.selectPws();
			}
		});
		panel.add(s);

		JButton save = new JButton("Save Project");
		save.setBounds(xcol1, yOff + 2, 199, ySize);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewFrompFrame.this.clearBack();
				Controller.loadPathways(true);
				String path = Controller.saveProject();
				System.out.println("Save");
				NewFrompFrame.addRecentPath(path);
				NewFrompFrame.this.saveRecentProj();
				// NewFrompFrame.this.clearBack();
				NewFrompFrame.this.showAnalyseOptions();
			}
		});
		panel.add(save);

		JButton saveAs = new JButton("Save Project As");
		saveAs.setBounds(xcol1 + 202, yOff + 2, 199, ySize);
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewFrompFrame.this.saveAs();
				NewFrompFrame.this.showAnalyseOptions();
			}
		});
		panel.add(saveAs);

		JLabel label = new JLabel("Analysis Options");
		label.setBounds(xcol1, yOff + (ySize + 2) * 2, xsize, ySize);
		panel.add(label);

		s = new JButton("Pathway Completeness >");
		s.setBounds(xcol1, yOff + (ySize + 2) * 3, xsize, ySize);
		s.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (NewFrompFrame.this.control_.gotSamples()) {
					Controller.loadPathways(true);
					NewFrompFrame.this.showPathwayScorePane();
				} else {
					warningFrame();
				}
			}
		});
		panel.add(s);

		s = new JButton("Pathway Activity >");
		s.setBounds(xcol1, yOff + (ySize + 2) * 4, xsize, ySize);
		s.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (NewFrompFrame.this.control_.gotSamples()) {
					Controller.loadPathways(true);
					NewFrompFrame.this.showPathwayActMatrix();
				} else {
					warningFrame();
				}
			}
		});
		panel.add(s);

		s = new JButton("EC Activity >");
		s.setBounds(xcol1, yOff + (ySize + 2) * 5, xsize, ySize);
		s.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (NewFrompFrame.this.control_.gotSamples()) {
					Controller.loadPathways(true);
					//uncommented
					NewFrompFrame.this.showEcActPanes();
				} else {
					warningFrame();
				}
			}
		});
		panel.add(s);
		
		s = new JButton("Lowest Common Ancestor >");
		s.setBounds(xcol1, yOff + (ySize + 2) * 6, xsize, ySize);
		s.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (NewFrompFrame.this.control_.gotSamples()) {
					Controller.loadPathways(true);
					NewFrompFrame.this.showLCAPanes();
				} else {
					warningFrame();
				}
			}
		});
		panel.add(s);

		this.back_.add(panel);

		JButton backButt = new JButton("Back");
		backButt.setBounds(xcol1, yOff + (ySize + 2) * 7, 150, 25);
		backButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewFrompFrame.this.newEditSamples();
			}
		});
		invalidate();
		validate();
		repaint();
	}

	private void showNewProjPanel() {
		int xcol1 = 100;
		int yOff = 100;
		int ySize = 30;
		int xsize = 400;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Project.getBackColor_());
		panel.setBounds(0, 0, 400, 800);

		JLabel label = new JLabel("Project Options:");
		label.setBounds(xcol1, yOff - 30, xsize, ySize);
		panel.add(label);

		JButton s = new JButton("New Project");
		s.setBounds(xcol1, yOff, xsize, ySize);
		s.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewFrompFrame.this.newProjectName();
			}
		});
		panel.add(s);

		s = new JButton("Open Project");
		s.setBounds(xcol1, yOff + ySize + 2, xsize, ySize);
		s.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewFrompFrame.this.openProj();
			}
		});
		panel.add(s);

		label = new JLabel("Recent Projects");
		label.setBounds(xcol1, yOff + (ySize + 2) * 2, xsize, ySize);
		panel.add(label);

		String path = "";
		for (int i = 0; i < recentProj_.size(); i++) {
			final int index = i;
			path = (String) recentProj_.get(i);
			path = path.substring(path.lastIndexOf(File.separator));
			s = new JButton(path);
			s.setBounds(xcol1, yOff + (ySize + 2) * (i + 3), xsize, ySize);
			s.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Project.userPathways = new ArrayList();

					NewFrompFrame.this.control_
							.loadProjFile((String) NewFrompFrame.recentProj_
									.get(index));
					NewFrompFrame.this.control_.openProject();
					DataProcessor.newBaseData = true;
					Controller.dataChanged = true;

					NewFrompFrame.this.clearBack();
					NewFrompFrame.this.newEditSamples();
				}
			});
			panel.add(s);
		}
		this.back_.add(panel);
		invalidate();
		validate();
		repaint();
	}

	private void removeRecentDoubles() {
		if (recentProj_ == null) {
			recentProj_ = new ArrayList();
		}
		for (int i = 0; i < recentProj_.size(); i++) {
			String comp = (String) recentProj_.get(i);
			for (int j = 0; j < recentProj_.size(); j++) {
				if (i != j) {
					if (((String) recentProj_.get(j)).contentEquals(comp)) {
						recentProj_.remove(j);
						j--;
					}
				}
			}
		}
	}

	private void loadRecentProj() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"recentProj.txt"));
			recentProj_ = new ArrayList();
			String line = "";
			while ((line = in.readLine()) != null) {
				if (!line.isEmpty()) {
					recentProj_.add(line);
					System.out.println("load " + line);
				}
			}
			in.close();
		} catch (Exception e) {
			saveRecentProj();
			e.printStackTrace();
		}
	}

	private void saveRecentProj() {
		removeRecentDoubles();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"recentProj.txt"));
			for (int i = 0; i < recentProj_.size(); i++) {
				out.write((String) recentProj_.get(i));
				System.out.println("Save " + (String) recentProj_.get(i));
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addRecentPath(String path) {
		if (recentProj_.size() < 10) {
			recentProj_.add(0, path);
		} else {
			recentProj_.remove(recentProj_.size() - 1);
			recentProj_.add(0, path);
		}
	}

	private void openProj() {
		// clearBack();
		JFileChooser fChoose_ = new JFileChooser(this.path_ + File.separator
				+ "projects");
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);
		fChoose_.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if ((f.isDirectory())
						|| (f.getName().toLowerCase().endsWith(".prj"))
						|| (f.getName().toLowerCase().endsWith(".frp"))) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return ".prj / .frp";
			}
		});
		if (fChoose_.showOpenDialog(getParent()) == 0) {
			try {
				int erg = this.control_.loadProjFile(fChoose_.getSelectedFile()
						.getCanonicalPath());
				addRecentPath(fChoose_.getSelectedFile().getCanonicalPath());
				if (erg == 1) {
					Project.userPathways = new ArrayList();

					System.out.println("OPEN PROJECT 1");

					NewFrompFrame.this.control_.loadProjFile(fChoose_
							.getSelectedFile().getCanonicalPath());
					NewFrompFrame.this.control_.openProject();

					DataProcessor.newBaseData = true;
					Controller.dataChanged = true;
					NewFrompFrame.this.clearBack();
					NewFrompFrame.this.newEditSamples();
				} else {
					if (erg == 2) {
						Project.userPathways = new ArrayList();

						System.out.println("OPEN PROJECT 2");

						NewFrompFrame.this.control_.loadProjFile(fChoose_
								.getSelectedFile().getCanonicalPath());
						NewFrompFrame.this.control_.openProject();

						DataProcessor.newBaseData = true;
						Controller.dataChanged = true;
						NewFrompFrame.this.clearBack();
						NewFrompFrame.this.newEditSamples();
					}
					if (erg == -1) {
						System.out.println("OPEN PROJECT -1");
						System.out.println("-1");
						return;
					}
					if (erg == 0) {
						System.out.println("OPEN PROJECT O");
						return;
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();

				saveRecentProj();
				clearBack();
				newEditSamples();
			}
		}
		System.out.println("Open");
		// clearBack();
	}

	public void saveAs() {
		// clearBack();
		Controller.loadPathways(true);
		JFileChooser fChoose_ = new JFileChooser(this.path_ + File.separator
				+ "projects");
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);
		File file = new File(this.path_ + File.separator + "projects");
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
		if (fChoose_.showSaveDialog(getParent()) == 0) {
			try {
				String path = fChoose_.getSelectedFile().getCanonicalPath();
				if (!path.endsWith(".frp")) {
					path = path + ".frp";
					System.out.println(".frp");
				}
				Controller.project_.exportProj(path);
				addRecentPath(path);
				saveRecentProj();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// clearBack();
			invalidate();
			validate();
			repaint();
		}
		System.out.println("Save");
		// clearBack();
	}

	public void openPDf(String path) {
		try {
			File pdfFile = new File(path);
			if (pdfFile.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(pdfFile);
				} else {
					System.out.println("Awt Desktop is not supported!");
				}
			} else {
				System.out.println("File does not exist!");
			}
			System.out.println("Done");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void openClosingFrame() {
		if (Project.samples_.size() == 0) {
			System.exit(0);
		} else {
			final JFrame frame = new JFrame("Warning!");
			frame.setBounds(200, 200, 350, 300);
			frame.setLayout(null);
			frame.setVisible(true);

			JPanel backP = new JPanel();
			backP.setBounds(0, 0, 350, 300);
			backP.setLayout(null);
			frame.add(backP);

			JLabel label = new JLabel(
					"Warning all unsaved progress will be lost!");
			label.setBounds(25, 100, 300, 25);
			backP.add(label);

			label = new JLabel("Exit anyway?!");
			label.setBounds(125, 125, 300, 25);
			backP.add(label);

			JButton exit = new JButton("Exit");
			exit.setBounds(55, 200, 100, 30);
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			backP.add(exit);

			JButton cancel = new JButton("Cancel");
			cancel.setBounds(180, 200, 100, 30);
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.removeAll();
					frame.dispose();
				}
			});
			backP.add(cancel);
		}
	}
}
