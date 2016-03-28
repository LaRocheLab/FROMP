package Panes;

import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Prog.StartFromp1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import pathwayLayout.PathLayoutGrid;

// The pathway selction panel

public class PathwaySelectP extends JPanel {
	private static final long serialVersionUID = 1L; 
	ArrayList<PathwayWithEc> paths_; // An arraylist of Pathways
	final String cfgName = "pwcfg:"; 
	final String selected = "TRUE"; 
	final String notSelected = "FALSE"; 
	final String fileEnding = ".pcg"; 
	final String userPathfileEnding = ".pwm"; 
	final String basePath_ = StartFromp1.FolderPath; // The path to the working directory
																				
	boolean selectAll; 
	int xLine2 = 500; 
	JPanel optionsPanel_; // The options panel
	JPanel displayP_; // The panel which all the pathways are displayed on
	JScrollPane showJPanel_; // The scroll pane which allows the user to scroll through the samples if the exceed their allotted space
	public JButton next_; // Button to Go to Analysis
	public JButton back_; // Button to go Back to Sample Selection

	public PathwaySelectP(ArrayList<PathwayWithEc> pws) {
		setBounds(0, 0, 1000, 3500);
		setVisible(true);
		setLayout(new BorderLayout());
		setBackground(Project.getBackColor_());
		this.selectAll = true;

		this.paths_ = pws;
		this.back_ = new JButton("< Back to Sample Selection");
		this.next_ = new JButton("Go to Analysis >");
		initMainPanels();
		showComponents();
	}

	private void redo() {// rebuilds the back panel
		removeAll();
		initMainPanels();
		showComponents();
		invalidate();
		validate();
		repaint();
	}

	private void initMainPanels() {
		this.optionsPanel_ = new JPanel();
		this.optionsPanel_.setPreferredSize(new Dimension(getWidth(), 100));
		this.optionsPanel_.setLocation(0, 0);
		this.optionsPanel_.setBackground(Project.getBackColor_().darker());
		this.optionsPanel_.setVisible(true);
		this.optionsPanel_.setLayout(null);
		add(this.optionsPanel_, "First");

		this.displayP_ = new JPanel();
		this.displayP_.setLocation(0, 0);
		this.displayP_.setPreferredSize(new Dimension(1000, 4000));
		this.displayP_.setSize(getPreferredSize());
		this.displayP_.setBackground(Project.getBackColor_());

		this.displayP_.setVisible(true);
		this.displayP_.setLayout(null);

		this.showJPanel_ = new JScrollPane(this.displayP_);
		this.showJPanel_.setVisible(true);
		this.showJPanel_.setVerticalScrollBarPolicy(20);
		this.showJPanel_.setHorizontalScrollBarPolicy(30);

		add("Center", this.showJPanel_);

		addNextBackButt();
	}

	private void showComponents() {// adds the buttons and the pathways to the display panel
		addButtons();
		addPathways();
	}

	private void addNextBackButt() {
		this.back_.setBounds(20, 75, 250, 20);
		this.back_.setVisible(true);
		this.back_.setLayout(null);
		this.optionsPanel_.add(this.back_);

		this.next_.setBounds(this.xLine2 + 150, 75, 250, 20);
		this.next_.setVisible(true);
		this.next_.setLayout(null);
		this.optionsPanel_.add(this.next_);
	}

	private void addPathways() {// add the pathways to the display panel
		for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++) {
			final int pwInt = pwCnt;
			Pathway tmpPath = (Pathway) this.paths_.get(pwCnt);
			final JCheckBox tmpBox = new JCheckBox(tmpPath.id_ + " / "
					+ tmpPath.name_);
			tmpBox.setSelected(tmpPath.isSelected());
			tmpBox.setBounds(20, 90 + 20 * pwCnt, 400, 17);

			tmpBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((PathwayWithEc) PathwaySelectP.this.paths_.get(pwInt))
							.setSelected(tmpBox.isSelected());
					Prog.DataProcessor.newBaseData = true;
					System.out.println(pwInt
							+ ((PathwayWithEc) PathwaySelectP.this.paths_
									.get(pwInt)).name_
							+ " "
							+ ((PathwayWithEc) PathwaySelectP.this.paths_
									.get(pwInt)).isSelected());
				}
			});
			this.displayP_.add(tmpBox);
		}
		ArrayList<String> paths = Project.userPathways;

		if (paths != null) {
			// System.out.println(paths.toString());
			for (int uPathCnt = 0; uPathCnt < Project.userPathways.size(); uPathCnt++) {
				final int pwInt = uPathCnt;
				String path = (String) paths.get(uPathCnt);
				JCheckBox tmpBox = new JCheckBox(path);
				tmpBox.setSelected(true);
				tmpBox.setBounds(this.xLine2, 90 + 20 * uPathCnt, 400, 17);

				tmpBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Project.removeUserPath((String) Project.userPathways
								.get(pwInt));
						Project.userPathways.remove(pwInt);

						Prog.DataProcessor.newUserData = true;
						PathwaySelectP.this.redo();
					}
				});
				this.displayP_.add(tmpBox);
			}
		}
		final JCheckBox tmpBox2 = new JCheckBox("de/select all");
		final JCheckBox tmpBox = new JCheckBox("de/select all");
		tmpBox.setSelected(this.selectAll);
		tmpBox.setBounds(20, 90 + (20 * this.paths_.size() + 1), 400, 17);

		tmpBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaySelectP.this.selectAll(tmpBox.isSelected());
				tmpBox2.setSelected(tmpBox.isSelected());
			}
		});
		this.displayP_.add(tmpBox);
		tmpBox2.setSelected(this.selectAll);
		tmpBox2.setBounds(20, 70, 400, 17);

		tmpBox2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaySelectP.this.selectAll(tmpBox2.isSelected());
				tmpBox.setSelected(tmpBox2.isSelected());
			}
		});
		this.displayP_.add(tmpBox2);
	}

	private void addButtons() {// adds buttons to the display panel
		JLabel descr = new JLabel("Select your wanted Pathways");
		descr.setBounds(20, 10, 400, 17);
		this.displayP_.add(descr);

		descr = new JLabel("Custom-made Pathways");
		descr.setBounds(this.xLine2, 10, 400, 17);
		this.displayP_.add(descr);

		JButton save = new JButton("Save pathway selection");
		save.setBounds(20, 27, 400, 20);
		save.setVisible(true);
		save.setLayout(null);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaySelectP.this.saveDialog("");
			}
		});
		this.optionsPanel_.add(save);

		JButton load = new JButton("Load pathway selection");
		load.setBounds(20, 47, 400, 20);
		load.setVisible(true);
		load.setLayout(null);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaySelectP.this.openLoadDialog("");
			}
		});
		this.optionsPanel_.add(load);

		JButton addUserP = new JButton("Add custom-made pathway");
		addUserP.setBounds(this.xLine2, 27, 400, 20);
		addUserP.setVisible(true);
		addUserP.setLayout(null);
		addUserP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwaySelectP.this.addUserPath();
			}
		});
		this.optionsPanel_.add(addUserP);

		JButton openDesigner = new JButton("Open pathway designer");
		openDesigner.setBounds(this.xLine2, 47, 400, 20);
		openDesigner.setVisible(true);
		openDesigner.setLayout(null);
		openDesigner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathLayoutGrid Grid = new PathLayoutGrid(10, 10, true);
			}
		});
		this.optionsPanel_.add(openDesigner);
	}

	private void selectAll(boolean selected) {
		for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++) {
			if (!((PathwayWithEc) this.paths_.get(pwCnt)).userPathway) {
				((PathwayWithEc) this.paths_.get(pwCnt)).setSelected(selected);
			}
		}
		this.selectAll = selected;

		redo();
	}

	public boolean pathSelected() {
		boolean ret = false;
		outerloop: for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++) {
			if (((PathwayWithEc) this.paths_.get(pwCnt)).isSelected()) {
				ret = true;
				break outerloop;
			}
		}
		if (Project.userPathways != null && !Project.userPathways.isEmpty()) {
			ret = true;
		}
		return ret;
	}

	private void saveConfig(String path) {
		if (!path.endsWith(".pcg")) {
			path = path + ".pcg";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));

			out.write("pwcfg:1");
			out.newLine();
			for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++) {
				if (((PathwayWithEc) this.paths_.get(pwCnt)).isSelected()) {
					out.write(((PathwayWithEc) this.paths_.get(pwCnt)).id_
							+ ":" + "TRUE");
				} else {
					out.write(((PathwayWithEc) this.paths_.get(pwCnt)).id_
							+ ":" + "FALSE");
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadConfig(BufferedReader inTxt) {
		String zeile = "";
		try {
			zeile = inTxt.readLine();

			int lineCounter = 0;
			while ((zeile = inTxt.readLine()) != null) {
				convertLine(zeile, lineCounter);
				lineCounter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		removeAll();
		showComponents();
	}

	private void convertLine(String line, int round) {
		String id = "";
		String value = "";
		if (line.contains(":")) {
			id = line.substring(0, line.indexOf(":"));
			value = line.substring(line.indexOf(":") + 1);
			System.out.println(id + " " + value);
			if (((PathwayWithEc) this.paths_.get(round)).id_.contentEquals(id)) {
				if (value.contentEquals("TRUE")) {
					((PathwayWithEc) this.paths_.get(round)).setSelected(true);
					return;
				}
				((PathwayWithEc) this.paths_.get(round)).setSelected(false);
				return;
			}
			for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++) {
				if (((PathwayWithEc) this.paths_.get(pwCnt)).id_
						.contentEquals(id)) {
					if (value.contentEquals("TRUE")) {
						((PathwayWithEc) this.paths_.get(pwCnt))
								.setSelected(true);
						return;
					}
					((PathwayWithEc) this.paths_.get(pwCnt)).setSelected(false);
					return;
				}
			}
		} else {
		}
	}

	private boolean saveDialog(String path) {
		JFileChooser fChoose_ = new JFileChooser(path);
		getParent().add(fChoose_);
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);

		fChoose_.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if ((f.isDirectory())
						|| (f.getName().toLowerCase().endsWith(".pcg"))) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return ".pcg";
			}
		});
		if (fChoose_.showSaveDialog(fChoose_.getParent()) == 0) {
			try {
				path = fChoose_.getSelectedFile().getCanonicalPath();
				if (!path.endsWith(".pcg")) {
					path = path + ".pcg";

					System.out.println(".pcg");
				}
				saveConfig(path);
				return true;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

	private void openLoadDialog(String path) {
		JFileChooser fChoose_ = new JFileChooser(path);
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);
		fChoose_.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if ((f.isDirectory())
						|| (f.getName().toLowerCase().endsWith(".pcg"))) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return ".pcg";
			}
		});
		if (fChoose_.showOpenDialog(getParent()) == 0) {
			try {
				BufferedReader reader = readTxt(fChoose_.getSelectedFile()
						.getCanonicalPath());
				loadConfig(reader);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		redo();
	}

	private void addUserPath() {
		JFileChooser fChoose_ = new JFileChooser(this.basePath_+ "userPaths");
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);
		fChoose_.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if ((f.isDirectory())
						|| (f.getName().toLowerCase().endsWith(".pwm"))) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return ".pwm";
			}
		});
		if (fChoose_.showOpenDialog(getParent()) == 0) {
			if (Project.userPathways == null) {
				Project.userPathways = new ArrayList();
			}
			try {
				Project.userPathways.add(fChoose_.getSelectedFile()
						.getCanonicalPath());
				Prog.DataProcessor.newUserData = true;
				Project.dataChanged = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		redo();
	}

	private BufferedReader readTxt(String path) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
}
