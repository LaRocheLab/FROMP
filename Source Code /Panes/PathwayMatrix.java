package Panes;

import Objects.EcNr;
import Objects.EcPosAndSize;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import Prog.PathButt;
import Prog.PngBuilder;
import Prog.StartFromp1;
import Prog.StringReader;
import Prog.XmlParser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import pathwayLayout.PathLayoutGrid;

// The "Show score-matrix" tab of the pathway completeness analysis

public class PathwayMatrix extends JPanel {
	private static final long serialVersionUID = 1L; 
	private ArrayList<Sample> samples_; // The arraylist of samples
	private Sample overSample_; 
	private ArrayList<PathwayWithEc> pathways_; // Arraylist of pathways
	private ArrayList<PathwayWithEc> tmpPathways_; // Arraylist of ecs mapped to pathways
	private double[][] scoreMat; 
	private double[][] rnScoreMat; 
	Project actProj_; // the active project
	int linDis = 40; 
	int colDis = 275; 
	int xDist = 250; 
	int offset = 100; 
	JLabel name; 
	JCheckBox showRn_; 
	JLabel mouseOverDisp; 
	JPanel mouseOverP; 
	JLabel mouseOverFrDisp; 
	JPanel optionsPanel_; // the options panel upon which the options, ie the buttons and checkboxes, will sit
	JPanel displayP_; // The display panel upon which this pathway matrix will be displayed
	JScrollPane showJPanel_; 
	private JCheckBox useCsf_; 
	MouseOverFrame infoFrame; 
	int xWidth; 

	public PathwayMatrix(ArrayList<Sample> samples, Sample overallSample,
			ArrayList<PathwayWithEc> pathways, Project actProj) {
		this.samples_ = new ArrayList();
		this.mouseOverDisp = new JLabel("Additional Pathway-information");
		for (int smpCnt = 0; smpCnt < samples.size(); smpCnt++) {
			this.samples_.add((Sample) samples.get(smpCnt));
		}
		this.pathways_ = pathways;
		this.overSample_ = overallSample;
		this.actProj_ = actProj;

		this.xWidth = ((Project.samples_.size() + 2) * this.colDis);
		setSize(this.xWidth, 2100);
		setLayout(new BorderLayout());
		setVisible(true);
		setBackground(Project.getBackColor_());

		initMainPanels();

		addOptions();
		writeMatrix();
	}

	private void initMainPanels() {// innitiates the options, display, and show panel
		this.optionsPanel_ = new JPanel();
		this.optionsPanel_.setPreferredSize(new Dimension(this.xWidth, 100));
		this.optionsPanel_.setLocation(0, 0);
		this.optionsPanel_.setBackground(Project.standard);
		this.optionsPanel_.setVisible(true);
		this.optionsPanel_.setLayout(null);
		add(this.optionsPanel_, "First");

		this.displayP_ = new JPanel();
		this.displayP_.setLocation(0, 0);
		this.displayP_.setPreferredSize(new Dimension(this.xWidth, 8000));
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

	private void addOptions() {// adds the buttons, etc. to the options panel
		if (this.useCsf_ == null) {
			this.useCsf_ = new JCheckBox("CSF");
		}
		this.useCsf_.setVisible(true);
		this.useCsf_.setLayout(null);
		this.useCsf_.setBackground(this.optionsPanel_.getBackground());
		this.useCsf_.setForeground(Project.getFontColor_());
		this.useCsf_.setBounds(10, 44, 100, 15);
		this.optionsPanel_.add(this.useCsf_);

		JButton writeButt = new JButton("Write matrix");
		writeButt.setBounds(10, 10, 150, 25);
		writeButt.setVisible(true);
		writeButt.setLayout(null);
		writeButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayMatrix.this.prepMat();

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
						PathwayMatrix.this.exportMat(path,
								PathwayMatrix.this.useCsf_.isSelected());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		this.optionsPanel_.add(writeButt);

		JButton openDetailFr = new JButton("Pathway-Info-Window");
		openDetailFr.setBounds(420, 10, 200, 25);
		openDetailFr.setVisible(true);
		openDetailFr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayMatrix.this.addMouseOverFrame();
			}
		});
		this.optionsPanel_.add(openDetailFr);

		this.mouseOverP = new JPanel();
		this.mouseOverP.setBackground(Project.getBackColor_());
		this.mouseOverP.setBounds(660, 10, 500, 60);
		this.optionsPanel_.add(this.mouseOverP);

		this.mouseOverDisp.setBounds(0, 0, 500, 60);
		this.mouseOverP.add(this.mouseOverDisp);
	}

	public void writeMatrix() {// writes the matrix to the display panel to be seen by the user
		int counter = 0;
		for (int i = 0; i < this.pathways_.size(); i++) {
			final PathwayWithEc fPath = new PathwayWithEc((PathwayWithEc) this.overSample_.pathways_.get(i), false);
			if (pwIsSelected(fPath)) {
				if (fPath.score_ >= Project.minVisScore_) {
					PathButt pathNames = new PathButt(this.samples_,
							this.overSample_,
							(PathwayWithEc) this.overSample_.pathways_.get(i),
							getBackground(), "", 1);
					pathNames.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent e) {
						}

						public void mousePressed(MouseEvent e) {
						}

						public void mouseExited(MouseEvent e) {
							PathwayMatrix.this.mouseOverDisp
									.setText("Additional Pathway-information");
							PathwayMatrix.this.repaint();
						}

						public void mouseEntered(MouseEvent e) {
							PathwayMatrix.this.setAdditionalInfo(fPath);
							PathwayMatrix.this.repaint();
						}

						public void mouseClicked(MouseEvent e) {
						}
					});
					pathNames.setBounds(this.xDist - this.colDis, this.linDis
							+ this.linDis * counter + this.offset, this.colDis,
							this.linDis);
					this.displayP_.add(pathNames);
					counter++;
				}
			}
		}
		int sampleNr = Project.samples_.size();
		int pathNr = this.pathways_.size();
		for (int sampleCnt = 0; sampleCnt < sampleNr; sampleCnt++) {
			Sample tmpSample = (Sample) Project.samples_.get(sampleCnt);

			final JLabel fullName = new JLabel(tmpSample.name_);
			int x = this.colDis * (sampleCnt + 1);
			int y = this.linDis;
			fullName.setBounds(x, 0 + this.offset, 500, 20);
			fullName.setForeground(tmpSample.sampleCol_);
			fullName.setVisible(false);
			this.displayP_.add(fullName);

			final JLabel smpName = new JLabel(tmpSample.name_);
			smpName.setBounds(0, 0, this.colDis, 20);
			smpName.setForeground(tmpSample.sampleCol_);

			JPanel mouseOver = new JPanel();
			mouseOver.setBounds(x, 20 + this.offset, this.colDis, 20);
			mouseOver.setBackground(Project.getBackColor_());
			mouseOver.setLayout(null);
			mouseOver.addMouseListener(new MouseListener() {
				public void mouseReleased(MouseEvent e) {
				}

				public void mousePressed(MouseEvent e) {
				}

				public void mouseExited(MouseEvent e) {
					smpName.setVisible(true);
					fullName.setVisible(false);
				}

				public void mouseEntered(MouseEvent e) {
					smpName.setVisible(false);
					fullName.setVisible(true);
				}

				public void mouseClicked(MouseEvent e) {
				}
			});
			mouseOver.add(smpName);
			this.displayP_.add(mouseOver);

			this.tmpPathways_ = tmpSample.pathways_;

			counter = 0;
			for (int pathCnt = 0; (pathCnt < pathNr)
					|| (pathCnt < this.tmpPathways_.size()); pathCnt++) {
				final PathwayWithEc fPath = new PathwayWithEc(
						(PathwayWithEc) this.tmpPathways_.get(pathCnt), false);
				if (pwIsSelected(fPath)) {
					if(fPath.score_ >= Project.minVisScore_ ) {
						PathButt scores = new PathButt(this.samples_,
								tmpSample, fPath, getBackground(), "", 1);

						scores.addMouseListener(new MouseListener() {
							public void mouseReleased(MouseEvent e) {
							}

							public void mousePressed(MouseEvent e) {
							}

							public void mouseExited(MouseEvent e) {
								PathwayMatrix.this.mouseOverDisp
										.setText("Additional Pathway-information");
								PathwayMatrix.this.repaint();
							}

							public void mouseEntered(MouseEvent e) {
								PathwayMatrix.this.setAdditionalInfo(fPath);
								PathwayMatrix.this.repaint();
							}

							public void mouseClicked(MouseEvent e) {
							}
						});
						x = this.colDis * (sampleCnt + 1);
						y = this.linDis + this.linDis * counter;
						scores.setBounds(x, y + this.offset, this.colDis,
								this.linDis);
						this.displayP_.add(scores);
						counter++;
					}
				}
			}
		}
	}

	public void prepMat() {// builds the 2d array of doubles: scoreMat
		this.scoreMat = new double[this.samples_.size() + 1][this.overSample_.pathways_
				.size()];
		for (int pwCnt = 0; pwCnt < this.overSample_.pathways_.size(); pwCnt++) {
			this.scoreMat[0][pwCnt] = ((PathwayWithEc) this.overSample_.pathways_
					.get(pwCnt)).score_;
			for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++) {
				this.scoreMat[(1 + smpCnt)][pwCnt] = ((PathwayWithEc) ((Sample) this.samples_
						.get(smpCnt)).pathways_.get(pwCnt)).score_;
			}
		}
	}

	public void prepRnMat() {// builds the 2d array of doubles: rnScoreMat
		this.rnScoreMat = new double[this.samples_.size() + 1][this.overSample_.rnPathways_
				.size()];
		for (int pwCnt = 0; pwCnt < this.overSample_.rnPathways_.size(); pwCnt++) {
			this.rnScoreMat[0][pwCnt] = ((PathwayWithEc) this.overSample_.rnPathways_
					.get(pwCnt)).score_;
			for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++) {
				this.rnScoreMat[(1 + smpCnt)][pwCnt] = ((PathwayWithEc) ((Sample) this.samples_
						.get(smpCnt)).rnPathways_.get(pwCnt)).score_;
			}
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

	private void addMouseOverFrame() {
		if (MouseOverFrame.getFrameCount() <= 0) {
			this.infoFrame = new MouseOverFrame();
		}
	}

	private boolean pwIsSelected(PathwayWithEc pathway) {
		for (int i = 0; i < this.pathways_.size(); i++) {
			if (pathway.id_ == ((PathwayWithEc) this.pathways_.get(i)).id_) {
				if (!((PathwayWithEc) this.pathways_.get(i)).isSelected()) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	public void exportMat(String path, boolean inCsf) {// exports the pathway matrix to a file
		String seperator = "\t";
		if (inCsf) {
			seperator = "\t";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
//			out.write("**** n.s. = 'not selected' ******** n.a. = not above minScore matrix is resorted");
//			out.newLine();
//			out.write(Project.projectPath_ + "PathayScoreMatrix");
//			out.newLine();
//			out.newLine();
			out.write("Pathway");
			for (int i = 0; i < this.samples_.size(); i++) {
				if (((Sample) this.samples_.get(i)).inUse) {
					out.write(seperator + ((Sample) this.samples_.get(i)).name_);
				}
			}
			out.newLine();
			for (int pathCnt1 = 0; pathCnt1 < this.overSample_.pathways_.size(); pathCnt1++) {
				PathwayWithEc pathway1 = (PathwayWithEc) this.overSample_.pathways_
						.get(pathCnt1);
				if (!pathway1.isSelected()) {
					out.write(pathway1.id_ +"-"+ pathway1.name_
							+ seperator + "n.s.");
					for (int smpCnt = 1; smpCnt < this.samples_.size(); smpCnt++) {
						out.write(seperator + "n.s.");
					}
				} else {
					out.write(pathway1.id_ +"-"+ pathway1.name_
							+ seperator + pathway1.score_);
					for (int smpCnt = 1; smpCnt < this.samples_.size(); smpCnt++) {
						Sample tmpSample = (Sample) this.samples_.get(smpCnt);
						if (tmpSample.inUse) {
							PathwayWithEc pathway2 = tmpSample
									.getPath(pathway1.id_);
							if (pathway2.score_ > Project.minVisScore_) {
								out.write(seperator + pathway2.score_);
							} else {
								out.write(seperator + "n.a.");
							}
						}
					}
					out.newLine();
				}
			}
			out.close();
		} catch (IOException e) {
			File f = new File(path);
			f.mkdirs();
			if (!path.endsWith(".txt")) {
				Calendar cal = Calendar.getInstance();

				int day = cal.get(5);
				int month = cal.get(2) + 1;
				int year = cal.get(1);

				String date = day + "." + month + "." + year;
				path = path + File.separator + "pwScore" + "."
						+ Project.workpath_ + "." + date;
			}
			String tmpPath = path + ".txt";
			File file1 = new File(tmpPath);
			if (file1.exists() && !file1.isDirectory()) {
				int i = 1;
				while ("Pigs" != "Fly") {// loop forever
					tmpPath = path + "(" + i + ")" + ".txt";
					File file2 = new File(tmpPath);
					if (file2.exists() && !file2.isDirectory()) {
						i++;
						continue;
					} else {
						path = path + "(" + i + ")";
						break;
					}
				}
			}
			path = path + ".txt";
			exportMat(path, inCsf);
		}
		System.out.println("outputfile succesfully written. Path: " + path);
	}

	public void exportPics(String path, boolean onlyOverall, boolean onlyUserP) {// exports the sample pictures
		if (!onlyOverall) {
			for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++) {
				Sample sample = (Sample) this.samples_.get(smpCnt);
				ArrayList<PathwayWithEc> pathways = ((Sample) this.samples_
						.get(smpCnt)).pathways_;

				System.out.println("Exporting picture from sample: "
						+ sample.name_);
				for (int pwCnt = 0; pwCnt < pathways.size(); pwCnt++) {
					PathwayWithEc pathway = (PathwayWithEc) pathways.get(pwCnt);
					if ((!onlyUserP) || (pathway.userPathway)) {
						BufferedImage image = buildImage(pathway, sample);

						System.out.println("Pathway: : " + pathway.id_ + " "
								+ pathway.name_);
						if (!pathway.id_.contentEquals("-1")) {
							PathwayMapFrame mFrame = new PathwayMapFrame(
									Project.samples_, sample, pathway);
							image = mFrame.drawImage(image);
						}
						exportPic(image, path, pathway, sample);
					}
				}
			}
		}
		Sample sample = Project.overall_;
		ArrayList<PathwayWithEc> pathways = sample.pathways_;
		System.out.println("Exporting picture from sample: " + sample.name_);
		for (int pwCnt = 0; pwCnt < pathways.size(); pwCnt++) {
			PathwayWithEc pathway = (PathwayWithEc) pathways.get(pwCnt);
			if ((!onlyUserP) || (pathway.userPathway)) {
				BufferedImage image = buildImage(pathway, sample);
				System.out.println("Pathway: : " + pathway.id_ + " "
						+ pathway.name_);
				if (!pathway.id_.contentEquals("-1")) {
					PathwayMapFrame mFrame = new PathwayMapFrame(
							Project.samples_, sample, pathway);
					image = mFrame.drawImage(image);
				}
				exportPic(image, path, pathway, sample);
			}
		}
	}

	private void exportPic(BufferedImage image, String path,
			PathwayWithEc pathway, Sample sample) {
		try {
			File f = new File(path);
			f.mkdirs();
			if (!path.endsWith(".png")) {
				Calendar cal = Calendar.getInstance();

				int day = cal.get(5);
				int month = cal.get(2) + 1;
				int year = cal.get(1);

				String date = day + "." + month + "." + year;
				path = path + File.separator + sample.name_ + "_" + pathway.id_
						+ "_" + Project.workpath_ + "_" + date + ".png";
			}
			if (image == null) {
				System.err.println("Image could not be generated "
						+ pathway.id_);
				return;
			}
			ImageIO.write(image, "png", new File(path));
		} catch (IOException e) {
			File f = new File(path);
			f.mkdirs();
			if (!path.endsWith(".png")) {
				Calendar cal = Calendar.getInstance();

				int day = cal.get(5);
				int month = cal.get(2) + 1;
				int year = cal.get(1);

				String date = day + "." + month + "." + year;
				path = path + File.separator + sample.name_ + "_" + pathway.id_
						+ "_" + Project.workpath_ + "_" + date + ".png";
			}
			exportPic(image, path, pathway, sample);
		}
	}

	private BufferedImage buildImage(PathwayWithEc path, Sample samp) {
		if (path.userPathway) {
			BufferedImage tmpImage = buildUserPath(path.pathwayInfoPAth, path,
					samp);
			return tmpImage;
		}
		if (path.score_ > 0.0D) {
			BufferedImage tmpImage = showPathwayMap(samp, path);
			return tmpImage;
		}
		try {
			if (path.id_ != "-1") {
				return ImageIO.read(new File(StartFromp1.FolderPath+"pics" + File.separator + path.id_
						+ ".png"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private BufferedImage buildUserPath(String loadPath, PathwayWithEc pathway,
			Sample samp) {
		PathLayoutGrid grid = new PathLayoutGrid(10, 10, false);
		grid.openPathWay(loadPath);
		BufferedImage image = grid.buildPicture(pathway, samp);
		return image;
	}

	public BufferedImage showPathwayMap(Sample sample, PathwayWithEc path) {
		return alterPathway(sample, path);
	}

	public BufferedImage alterPathway(Sample sample, PathwayWithEc pathway) {
		boolean found = false;

		PngBuilder builder_ = new PngBuilder();
		StringReader reader = new StringReader();
		XmlParser parser = new XmlParser();

		BufferedReader xmlPath = reader.readTxt(StartFromp1.FolderPath+"pathway" + File.separator
				+ pathway.id_ + ".xml");
		ArrayList<EcPosAndSize> tmppos = new ArrayList();
		for (int ecCount = 0; ecCount < pathway.ecNrs_.size(); ecCount++) {
			xmlPath = reader.readTxt(StartFromp1.FolderPath+"pathway" + File.separator + pathway.id_
					+ ".xml");
			tmppos = parser.findEcPosAndSize(
					((EcNr) pathway.ecNrs_.get(ecCount)).name_, xmlPath);
			((EcNr) pathway.ecNrs_.get(ecCount)).addPos(tmppos);
			if (tmppos != null) {
				found = true;
			}
		}
		if (found) {
			return builder_.getAlteredPathway(pathway, sample);
		}
		try {
			return ImageIO.read(new File(StartFromp1.FolderPath+"pics" + File.separator + pathway.id_
					+ ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
