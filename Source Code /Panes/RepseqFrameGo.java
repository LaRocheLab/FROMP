package Panes;

import Objects.ConvertStatGo;
import Objects.GONum;
import Objects.Project;
import Prog.StartFromp1;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.io.PrintWriter;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Font;
import java.util.LinkedHashMap;

import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.*;

// The window which displays the sequence IDs when an ec is clicked on in the Activity Matric Pane 	
// From this window the user can export the sequence IDs or sequences mapping to the particular ec

public class RepseqFrameGo extends JFrame {
	private static final long serialVersionUID = 1L; 
	private JMenuBar menuBar_; // Drop down menu bar for the repseq frame
	private JMenu menu_; // Menu item for the dropdown menu
	final String basePath_ = StartFromp1.FolderPath; // The current workpath
	
	GONum goNr_; // EC for which we are viewing the associated squence IDs
	JLabel label_; 
	JPanel back_; // Backpanel on the frame
	ArrayList<ConvertStatGo> reps_; // Arraylist of conversion statistics for the ECs
	int xSize = 800; 
	int ySize; 
	String sampName_; // Name of the sample in question

	public RepseqFrameGo(ArrayList<ConvertStatGo> reps, GONum goNr, String sampName) {
		//sets the title of the JFrame
		super(goNr.GoNumber + " * " + reps.size() + " Unique Sequence IDs"); 
		this.sampName_ = sampName;
		this.reps_ = reps;
		this.goNr_ = goNr;
		if (this.reps_ != null) // If the Arraylist which contains the sequence ids isnt null
		{
			System.out.println("RepseqFrameGo " + this.reps_.size());
			/*
			 * If there are more than 50 sequence IDs than we arn't going to display them
			 * and instead will display a warning message. The user will still be able to export
			 * them to a file
			 */
			if (this.reps_.size() < 50) { 
				setBounds(100, 100, this.xSize, 100 + 25 * this.reps_.size());
			} else {
				setBounds(100, 100, this.xSize, 100 + 100);
			}
		} else {
			setBounds(100, 100, this.xSize, 100);
		}
		setResizable(false);
		setVisible(true);
		setLayout(null);

		this.back_ = new JPanel();
		this.back_.setBackground(Project.getBackColor_());
		if (this.reps_.size() < 50) {
			this.back_
					.setBounds(0, 0, this.xSize, 100 + 25 * this.reps_.size());

		} else {
			this.back_.setBounds(5, 5, this.xSize, 100 + 100);
			JTextArea tArea = new JTextArea();
			tArea.setBounds(0, 0, this.xSize, 25 * this.reps_.size());
			tArea.setLayout(null);
			tArea.setEditable(false);
			Font font = new Font("Verdana", Font.BOLD, 12);
			tArea.setFont(font);
			this.back_.add(tArea);
			//warning if there are to many sequence id's to display
			tArea.setText("*WARNING*\nThere are too many sequence IDs to be viewed.\nYou may still export them using the drop-down File menu in the top left corner."); 
		}
		this.back_.setLayout(null);
		this.back_.setVisible(true);
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				System.out.print("move");
				int count = e.getWheelRotation();
				if (Math.abs(count) > 0) {
					int value = RepseqFrameGo.this.back_.getY() - count * 50;
					RepseqFrameGo.this.back_.setLocation(
							RepseqFrameGo.this.back_.getX(), value);
					RepseqFrameGo.this.repaint();
				}
			}
		});

		add(this.back_);
		if (this.reps_ != null) {
			addrepseqs();
			addMenu();
		}
	}

	public RepseqFrameGo(ArrayList<ConvertStatGo> reps, String goNr, int amount) {
		super(goNr + " * " + amount + " Unique Sequence IDs");
		this.sampName_ = "";
		this.reps_ = reps;
		if (this.reps_ != null) {
			System.out.println("RepseqFrameGo" + this.reps_.size());
			if (this.reps_.size() < 50) {
				setBounds(100, 100, this.xSize,
						100 + 25 * this.reps_.size() + 5);
			} else {
				setBounds(100, 100, this.xSize, 130);
			}
		} else {
			setBounds(100, 100, this.xSize, 100);
		}

		setResizable(false);
		setVisible(true);
		setLayout(null);

		this.back_ = new JPanel();
		this.back_.setBackground(Color.orange);
		if (this.reps_.size() < 50) {
			this.back_.setBounds(0, 0, this.xSize, 50 + 25 * this.reps_.size());
		} else {
			this.back_.setBounds(0, 0, this.xSize, 75);
		}
		this.back_.setLayout(null);
		this.back_.setVisible(true);
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
//				System.out.print("move");
				if (RepseqFrameGo.this.back_.getY() <= 0) {
					int count = e.getWheelRotation();
					int value = RepseqFrameGo.this.back_.getY() - count * 20;
					RepseqFrameGo.this.back_.setLocation(
							RepseqFrameGo.this.back_.getX(), value);
					RepseqFrameGo.this.repaint();
				}
			}
		});

		add(this.back_);
		if (this.reps_ != null) {
			addrepseqs();
			addMenu();
		}

	}

	private void addMenu() {// adds the dropdown File menu which allows the user to export the sequence ids and the sequences
		this.menuBar_ = new JMenuBar();

		this.menu_ = new JMenu("File");

		this.menuBar_.add(this.menu_);
		JMenuItem miItem = new JMenuItem("Export Reps", 83);
		miItem.setAccelerator(KeyStroke.getKeyStroke(83, 8));

		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RepseqFrameGo.this.ExportReps(); // exports the sequence ids which map to this ec in this sample
			}
		});

		this.menu_.add(miItem);

		miItem = new JMenuItem("Export Sequences", 86);
		miItem.setAccelerator(KeyStroke.getKeyStroke(86, 8));

		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//MODIFIED CHANGE IF BROKEN
				RepseqFrameGo.this.ExportSequences(); // exports the sequences which map to this ec in this sample for each samples EC
			}
			
		});

		this.menu_.add(miItem);

		setJMenuBar(this.menuBar_);
	}

	public void ExportReps() {// exports the sequence ids to a file in the directory ~/RepSeqIDs
		String text = "";
		// String test="";
		System.out.println("Reps:" + RepseqFrameGo.this.reps_.size());
		for (int repCnt = 0; repCnt < RepseqFrameGo.this.reps_.size(); repCnt++) {
			int amount = ((ConvertStatGo) RepseqFrameGo.this.reps_.get(repCnt))
					.getGoAmount_();
			if (((ConvertStatGo) RepseqFrameGo.this.reps_.get(repCnt))
					.getPfamToGoAmount_() > amount) {
				amount = ((ConvertStatGo) RepseqFrameGo.this.reps_.get(repCnt))
						.getPfamToGoAmount_();
			}
			// test=((ConvertStatGo)this.reps_.get(repCnt)).getDesc_() + "," +
			// ((ConvertStatGo)this.reps_.get(repCnt)).getGoAmount_() + "," +
			// ((ConvertStatGo)this.reps_.get(repCnt)).getPfamToGoAmount_() + ","
			// + amount;
			// if(!test.contains("\t")){
			text = text
					+ ((ConvertStatGo) this.reps_.get(repCnt)).getDesc_()
					+ ","
					+ ((ConvertStatGo) this.reps_.get(repCnt)).getGoAmount_()
					+ ","
					+ ((ConvertStatGo) this.reps_.get(repCnt))
							.getPfamToGoAmount_() + "," + amount;
			text = text + "\n";
			// }
		}
		// System.out.println("Text:\n"+text);
		try {
			String sampleName;
			if (sampName_.contains(".out")) {
				sampleName = sampName_.replace(".out", "");
			} else {
				sampleName = sampName_;
			}
			File file = new File(basePath_ + "RepSeqIDs" + File.separator
					+ sampleName + "-" + goNr_.GoNumber + ".txt");
			// file.getParentFile().mkdirs();
			PrintWriter printWriter = new PrintWriter(file);
			printWriter.println("" + text);
			// System.out.println("Text:\n"+text);
			printWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void ExportSequences() { // Exports the sequences to to a file in the ~/Sequences directory
		String seqFilePath = "";
		for (int i = 0; i < Project.samples_.size(); i++) {
			if (this.sampName_.equals(Project.samples_.get(i).name_)) {
				if (Project.samples_.get(i).getSequenceFile() != null
						&& !Project.samples_.get(i).getSequenceFile().equals("")) {
					seqFilePath = Project.samples_.get(i).getSequenceFile();
				}
			}
		}
		//ensure the path isn't null and the file exists
		if (seqFilePath != null && !seqFilePath.equals("none")) { 
			File seqFile = new File(seqFilePath); 
			if (seqFile.exists() && !seqFile.isDirectory()) { 
				//instanciates the sequence hash built by the biojava core
				LinkedHashMap<String, ProteinSequence> sequenceHash; 
				try {
					//calls biojava to build the sequence hash
					sequenceHash = FastaReaderHelper.readFastaProteinSequence(seqFile); 
					if (sequenceHash != null) {
						/*
						 * System.out.println("Seq File: "+seqFile); for
						 * (Map.Entry<String, ProteinSequence> entry :
						 * sequenceHash.entrySet()) { String key =
						 * entry.getKey(); ProteinSequence value =
						 * entry.getValue(); System.out.println(key+": "+value);
						 * }
						 */
						String text = ">";
						System.out.println("repCnt: "+ RepseqFrameGo.this.reps_.size());
						for (int repCnt = 0; repCnt < RepseqFrameGo.this.reps_.size(); repCnt++) {
							if ((sequenceHash.get(((ConvertStatGo) this.reps_.get(repCnt)).getDesc_())) != null) {
								text = text
										+ ((ConvertStatGo) this.reps_.get(repCnt))
												.getDesc_()
										+ "\n"
										+ (sequenceHash
												.get(((ConvertStatGo) this.reps_
														.get(repCnt))
														.getDesc_()))
												.toString();
								// ensures that there is a ">" character in front of every new sample
								if (repCnt < RepseqFrameGo.this.reps_.size() - 1) {
									text = text + "\n>";
								}
								// Don't want the ">" character on the last newline with no sample
								else if (repCnt == RepseqFrameGo.this.reps_
										.size()) {
									text = text + "\n";
								}

							}
						}
						try {
							String sampleName;
							if (sampName_.contains(".out")) {
								sampleName = sampName_.replace(".out", "");
							} else {
								sampleName = sampName_;
							}
							File file = new File(basePath_ + "Sequences"
									+ File.separator + sampleName + "-"
									+ goNr_.GoNumber + "-Sequences" + ".txt");
							PrintWriter printWriter = new PrintWriter(file);
							if (text != null && text != "") {
								printWriter.println("" + text);
								System.out
										.println("Done Writing Sequence Files\n");
							} else {
								printWriter
										.println("No matching sequences in the file provided.");
								warningFrame("No matching sequences in the file provided.");
							}
							printWriter.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						warningFrame("The sequence file is not in the fasta format");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else {
				warningFrame("The sequence file associated with this sample does not exist");
			}
		} else {
			warningFrame("There is no sequence file associated with this sample");
		}
	}

	public void warningFrame(String str) { // The popup window produced if there is a problem with exporting the sequences
		final JFrame frame = new JFrame("Warning!");
		frame.setBounds(200, 200, 500, 100);
		frame.setLayout(null);
		frame.setVisible(true);

		JPanel backP = new JPanel();
		backP.setBounds(0, 0, 500, 75);
		backP.setLayout(null);
		frame.add(backP);

		JLabel label = new JLabel(str);
		label.setBounds(25, 25, 450, 25);
		backP.add(label);
	}

	private void addrepseqs() {// adds the sequence ids to the viewing panel
		if (this.reps_.size() > 50) {
			this.label_ = new JLabel("RepSeq , fromEc , fromPf , used Val.");
			this.label_.setBounds(5, 0, this.xSize, 20);
			this.label_.setLayout(null);
			this.back_.add(this.label_);

			JPanel line = new JPanel();
			line.setBounds(0, 20, this.xSize, 2);
			line.setBackground(Color.black);
			line.setLayout(null);
			this.back_.add(line);
			return;
		}
		this.label_ = new JLabel("RepSeq , fromEc , fromPf , used Val.");
		this.label_.setBounds(5, 0, this.xSize, 20);
		this.label_.setLayout(null);
		this.back_.add(this.label_);

		JPanel line = new JPanel();
		line.setBounds(0, 20, this.xSize, 2);
		line.setBackground(Color.black);
		line.setLayout(null);
		this.back_.add(line);

		JTextArea tArea = new JTextArea();
		tArea.setBounds(5, 22, this.xSize, 25 * this.reps_.size());
		tArea.setLayout(null);
		tArea.setEditable(false);
		this.back_.add(tArea);

		String text = "";
		for (int repCnt = 0; repCnt < this.reps_.size(); repCnt++) {
			int amount = ((ConvertStatGo) this.reps_.get(repCnt)).getGoAmount_();
			if (((ConvertStatGo) this.reps_.get(repCnt)).getPfamToGoAmount_() > amount) {
				amount = ((ConvertStatGo) this.reps_.get(repCnt))
						.getPfamToGoAmount_();
			}
			// test=((ConvertStatGo)this.reps_.get(repCnt)).getDesc_() + "," +
			// ((ConvertStatGo)this.reps_.get(repCnt)).getGoAmount_() + "," +
			// ((ConvertStatGo)this.reps_.get(repCnt)).getPfamToGoAmount_() + ","
			// + amount;
			// if(!test.contains("\t")){
			text = text
					+ ((ConvertStatGo) this.reps_.get(repCnt)).getDesc_()
					+ ","
					+ ((ConvertStatGo) this.reps_.get(repCnt)).getGoAmount_()
					+ ","
					+ ((ConvertStatGo) this.reps_.get(repCnt))
							.getPfamToGoAmount_() + "," + amount;
			text = text + "\n";
			
		}
		tArea.setText(text);
	}
}
