package Panes;

import Objects.ConvertStatGo;
import Objects.GONum;
import Objects.Line;
import Objects.Project;
import Objects.Sample;
import Prog.CmdController1;
import Prog.DataProcessor;
import Prog.MetaProteomicAnalysis;
import Prog.StartFromp1;
import Prog.seqWithFileName;
import Prog.tableAndChartData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.*;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;

/**
 * This is the activity GO Matrix Pane. GO oriented part of the GO activity section. From here you can generate the Go activity matrix
 * @author song
 *
 */
public class GoActMatixPane extends JPanel {
	private static final long serialVersionUID = 1L; //
	private JButton exportMat_; // Button to export the matrix to a file
	private JButton exportGos_; //button to export just the go numbers from the matrix into a file
	private JButton names_; // Tmp variable for building a a list of GO buttons
	private JLabel label_; 
	private JCheckBox bySumcheck_; // Check box used to sort matrix by sum
	private JCheckBox useOddsrat_; // Checkbox to include the odds-ratio in the calculation of the matrix
	private JCheckBox sort_odds_by_lowest;
	private JCheckBox useGeoDist_;
	private JCheckBox sort_by_lowest_Geo;
	private JCheckBox moveUnmappedToEnd; // Checkbox to moved unmapped gos to the end of the list. Default is checked
	public JCheckBox includeRepseq_; // Checkbox to include the ability to click on gos from samples and see the sequence ids which are associated with that go
	private JCheckBox useCsf_; // Checkbox to use CSF
	private JTextField maxVisField_; 
	private ArrayList<JLabel> nameLabels_; // ArrayList of labels
	private float maxVisVal_ = 0.0F; 
	private Project actProj_; // The active project
	private boolean sortedGo = false; 
	private boolean drawChart = false; 
	private int sumIndexSmp; 
	private int numChart = 0;
	private int NoDataChart = 0;
	private ArrayList<Sample> smpList_; // ArrayList of samples to be used to generate the matrix
	private JButton resort_; // JBotton to resort the array
	public ArrayList<Line> goMatrix_ = new ArrayList<Line>(); // Arraylist of line objects used to build the matrix
	private Loadingframe lframe; // Loading frame
	private Line unmappedSum; 
	private Line mappedSum; 
	private Line incompleteSum; 
	private Line sums; 
	private tableAndChartData returnData;
	JPanel optionsPanel_; // Options panel
	JPanel displayP_; // Panel which displays the go matrix
	JScrollPane showJPanel_; // Scroll pane which allows the user to scroll through the matrix if it is bigger than the allotted space
	DataProcessor proc_; // Data Processor which allows the input files to be parsed and for relevant data to be computed from them
	int selectedSampIndex_ = -1; 
	JLabel selectedSampText; 
	final String basePath_ = new File(".").getAbsolutePath() + File.separator; //File(".") = current directory
	JPopupMenu menuPopup;// Popup menu used for right clicking and exporting sequence ids
	static JPopupMenu ecMenuPopup;
	int popupIndexY; // Coordinates to facilitate the exporting of sequence ids
	int popupIndexX; 
	int yIndex1 = 0;
	int yIndex2 = 0;
	int scrollChartPos = 0;
	int num_exportMat_go = 0;
	String buttonName;
	public boolean exportAll = false;
	boolean findLca = false;
	boolean batchCommand = false;
	ArrayList<String> go_list= new ArrayList<String>();
	ArrayList<String> go_batch = new ArrayList<String>(); ;
	String fileName = "";
	String sampleName = "";
	String chosen_go = "";
	String chosen_sample = "";

	
	public GoActMatixPane(Project actProj,DataProcessor proc, Dimension dim) {
		// opens the loading frame
		this.lframe = new Loadingframe(); 
		this.lframe.bigStep("Preparing Panel"); 
		this.lframe.step("Init"); 
									
		this.actProj_ = actProj; // sets this active project
		this.proc_ = proc; // sets this data processor
		go_list = new ArrayList<String>();
		
		setLayout(new BorderLayout()); 
		setVisible(true); 
		setBackground(Project.getBackColor_()); 
		setSize(dim); 
		
		this.smpList_ = Project.samples_; 														
		this.sumIndexSmp = 0; 
		prepGoMatrix(); // Builds the go matrixLoadingframe.close();
		initMainPanels(); // Instantiates the options, display and scroll panels
		prepaint(); // Removes everything from the back panel adds the options panel, draws the sample names, shows the go matrix, then repaints the back pane
		Loadingframe.close(); // closes the loading frame
	}

	public GoActMatixPane(Project actProj,DataProcessor proc, Dimension dim,String lca) {
		// opens the loading frame
		this.lframe = new Loadingframe(); 
		this.lframe.bigStep("Preparing Panel"); 
		this.lframe.step("Init"); 
									
		this.actProj_ = actProj; // sets this active project
		this.proc_ = proc; // sets this data processor
		go_list = new ArrayList<String>();
		
		setLayout(new BorderLayout()); 
		setVisible(true); 
		setBackground(Project.getBackColor_()); 
		setSize(dim); 
		
		this.smpList_ = Project.samples_; 													
		this.sumIndexSmp = 0; 
		prepGoMatrix(); // Builds the go matrixLoadingframe.close();
		initMainPanels(); // Instantiates the options, display and scroll panels
		prepaint(); // Removes everything from the back panel adds the options panel, draws the sample names, shows the go matrix, then repaints the back pane
		prepaintLCA(); 
		Loadingframe.close(); // closes the loading frame
	}
	
	private void prepaintLCA() {// This method rebuids the back panel of the window
		removeAll(); // Removes everything on the backpanel
		initMainPanels(); // instanciates the options, display, and scroll panels
		prepGoMatrix();
		addOptionsLCA(); // adds the buttons, labels, checkboxes etc to the options panel
		if(drawChart==true){
			drawChart();
		}
		invalidate(); 
		validate(); 
		repaint(); 
		
	}

	private void addOptionsLCA() {
		
		int goCnt;
		GONum go_Path = null;
		go_list = new ArrayList<String>();
		//adding all the go numbers found within the ecMatrix to the LCA GO number combobox
		for (goCnt = 0; goCnt < this.goMatrix_.size(); goCnt++) {
			Line goNr = (Line) this.goMatrix_.get(goCnt);
			
			if (goNr.getGoNr_() != null) {
				go_Path = goNr.getGoNr_();
			}
			if (go_Path != null && go_Path.getGoNumber() != null) {
				go_list.add(go_Path.getGoNumber());
			}
			
		}
		Collections.sort(go_list);
		
		//Combo box of the various go numbers to find their lowest common ancestor
		final JComboBox<String> goList = new JComboBox<String>();
			System.out.println("go list size: " + go_list.size());
			goList.addItem("No GO Selected");
			for (int i = 0; i < go_list.size(); i++) {
				if (go_list.size() > 0) {
					goList.addItem(go_list.get(i));
				}
			}
		goList.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e) {
			 System.out.println(goList.getSelectedItem().toString());
			 chosen_go = goList.getSelectedItem().toString();
		    }
		  });
		goList.setSelectedItem(0);
		goList.setBounds(20,35,150,40);
		goList.setVisible(true);
		
		//combo box of all the samples present within the project, can choose to find lca of specific go and sample
		final JComboBox<String> sampleList = new JComboBox<String>();
			//can find lca of all samples given just the go number
			sampleList.addItem("All Samples");
			for (int j = 0; j < Project.samples_.size(); j++) {
				sampleList.addItem(Project.samples_.get(j).name_);
			}
			sampleList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					 System.out.println(sampleList.getSelectedItem().toString());
					chosen_sample = sampleList.getSelectedItem().toString();
				}
			});
		sampleList.setSelectedItem(0);
		sampleList.setBounds(210, 35, 350, 40);
		sampleList.setVisible(true);
		
		this.exportMat_ = new JButton("Find Lowest Common Ancestor");
		this.exportMat_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean oneFile = false;
				if (!chosen_go.equals("No GO Selected")&&!chosen_go.equals("")) {
				lframe = new Loadingframe(); // opens the loading frame
				lframe.bigStep("Calculating LCA..");
				lframe.step(chosen_go);

				LinkedHashMap<String,String> seq_for_lca;
				//If the user did not choose a sample, export all the samples into one file
				if(chosen_sample.equals("")||chosen_sample.equals("All Samples")){
					oneFile = true;
					exportAll = true;
				}
				else{
					oneFile = false;
					exportAll = false;
				}
				seq_for_lca = cmdExportSequencesGo(chosen_go,sampleName, oneFile, findLca);
				oneFile = false;
				exportAll = false;
				
				if(((chosen_sample.equals("All Samples")||(chosen_sample.equals("")))&&!chosen_go.equals("No GO Selected"))){
					fileName = chosen_go+"-";
					
				}
				else if(((!chosen_sample.equals("All Samples"))||(!chosen_sample.equals(""))&&!chosen_go.equals("No GO Selected"))){
					fileName = chosen_sample + "-" + chosen_go + "-";
					lframe.step(chosen_sample + "-" + chosen_go + "-");
				}
				if(!chosen_go.equals("No GO Selected")&&!chosen_go.equals("")){
				//cannot find the lowest common ancestor of a unselected go number and sample
				System.out.println("Lowest Common Section");
				MetaProteomicAnalysis meta = new MetaProteomicAnalysis();
				returnData = meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca, fileName), oneFile, batchCommand);
				}

				drawChart = true;
				if (numChart < 1) {
					prepaintLCA();
				} else {
					drawChart();
				}

				Loadingframe.close();
				}
				else{
					warningFrame("Please Select an GO");
				}
				
			}
				
		});
	this.exportMat_.setBounds(600, 35, 300, 40);
	this.exportMat_.setVisible(true);
	this.exportMat_.setLayout(null);
	this.exportMat_.setForeground(Project.getFontColor_());
	
	//Button used to clear the screen of all graphs & charts
	JButton clear = new JButton("Clear Screen");

	clear.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			displayP_.removeAll();
			displayP_.updateUI(); 
			numChart = 0;
			NoDataChart = 0;
			yIndex1 = 520;
			yIndex2 = 0;
		}
	});
	clear.setBounds(1000, 10, 170, 20);
	clear.setVisible(true);
	clear.setLayout(null);
	clear.setForeground(Color.red);
	
	JButton load_Seq_Batch = new JButton("Load Seq Batch file");
	load_Seq_Batch.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			System.out.println("Load Seq Batch file");
			String path_ = "";
			//path_ = new File("").getCanonicalPath();
			path_ = StartFromp1.FolderPath+"bin";
			JFileChooser fChoose_ = new JFileChooser(path_);
			fChoose_.setFileSelectionMode(0);
			fChoose_.setBounds(100, 100, 200, 20);
			fChoose_.setVisible(true);
			File file = new File(path_);
			fChoose_.setSelectedFile(file);;
			fChoose_.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if ((f.isDirectory())|| (f.getName().toLowerCase().endsWith(".txt"))
							|| (f.getName().toLowerCase().endsWith(".lst"))
							|| (f.getName().toLowerCase().endsWith(".faa"))){
						return true;
					}
					return false;
				}

				public String getDescription() {
					return ".txt/.lst/.faa";
				}
			});
			//using SeqFileReader to read a  batch of seq file.
			if (fChoose_.showSaveDialog(GoActMatixPane.this.getParent()) == 0) {
				try {
					String path = fChoose_.getSelectedFile().getCanonicalPath();
					
					StartFromp1.goSet = new ArrayList<String>();
					StartFromp1.FileSetofSeq = new ArrayList<seqWithFileName>();
					StartFromp1.SeqFileReader(path);
					
					if(!StartFromp1.FileSetofSeq.isEmpty()){
						System.out.println("Is Seq batch");
						runLcaSeq(StartFromp1.FileSetofSeq);
					}
					else{
						warningFrame("This is not a Seq batch file!");
					}	
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}
		
			}
		}	
	});
	
	load_Seq_Batch.setBounds(1000, 40, 170, 20);
	load_Seq_Batch.setVisible(true);
	load_Seq_Batch.setLayout(null);

	
	JButton load_Ec_Batch = new JButton("Load GO Batch file");
	load_Ec_Batch.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
			System.out.println("Load GO Batch file");
			String path_ = "";
			//path_ = new File("").getCanonicalPath();
			path_ = StartFromp1.FolderPath+"bin";
			JFileChooser fChoose_ = new JFileChooser(path_);
			fChoose_.setFileSelectionMode(0);
			fChoose_.setBounds(100, 100, 200, 20);
			fChoose_.setVisible(true);
			File file = new File(path_);
			fChoose_.setSelectedFile(file);;
			fChoose_.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if ((f.isDirectory())|| (f.getName().toLowerCase().endsWith(".txt"))){
						return true;
					}
					return false;
				}

				public String getDescription() {
					return ".txt";
				}
			});
			//using EcFileReader to read a  batch of go file.
			if (fChoose_.showSaveDialog(GoActMatixPane.this.getParent()) == 0) {
				try {
					String path = fChoose_.getSelectedFile().getCanonicalPath();
					//File filename = new File(path);
					
					StartFromp1.goSet =new ArrayList<String>();
					StartFromp1.EcFileReader(path);
					
					//boolean isBatch = checkBatchFile(filename);
					if(StartFromp1.goSet!=null){
						System.out.println("Is go batch");
						runLcaBatchFile(StartFromp1.goSet);
					}
					else{
						warningFrame("This is not a GO number batch file!");
					}
					
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}
		
			}
		}

		
	});
	load_Ec_Batch.setBounds(1000, 70, 170, 20);
	load_Ec_Batch.setVisible(true);
	

	
	JLabel warning = new JLabel("Note: Sequence files that contain a large amount of sequences may take awhile to process");
	warning.setBounds(20, 73, 700, 20);
	warning.setVisible(true);
	warning.setLayout(null);
	warning.setForeground(Project.getFontColor_());
	
	JLabel ec_combo = new JLabel("Choose GO:");
	ec_combo.setBounds(20,15,100,20);
	ec_combo.setVisible(true);
	
	JLabel smp_combo = new JLabel("Choose Sample:");
	smp_combo.setBounds(210,15,350,20);
	smp_combo.setVisible(true);


	this.optionsPanel_.add(this.exportMat_);
	this.optionsPanel_.add(clear);
	this.optionsPanel_.add(warning);
	this.optionsPanel_.add(goList);
	this.optionsPanel_.add(sampleList);
	this.optionsPanel_.add(ec_combo);
	this.optionsPanel_.add(smp_combo);
	this.optionsPanel_.add(load_Ec_Batch);
	this.optionsPanel_.add(load_Seq_Batch);
	
		
	}
	//load go#'s for lca function
	private void runLcaBatchFile(ArrayList<String> ecset){
		LinkedHashMap<String,String> seq_for_lca;
		MetaProteomicAnalysis meta = new MetaProteomicAnalysis();
		try{
			
			
			lframe = new Loadingframe(); // opens the loading frame
			lframe.bigStep("Calculating LCA..");
			
			for(int i = 0; i < ecset.size();i++){
				String line = ecset.get(i);
				lframe.step(line);
				exportAll = true;
				//equal to cmd seqall.
				seq_for_lca = cmdExportSequencesGo(line,sampleName, true, false);
				fileName = line +  "-";
				meta = new MetaProteomicAnalysis();
				returnData = meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca, fileName), false, batchCommand);
				
				drawChart = true;
				if (numChart < 1) {
					prepaintLCA();
				} 
				else {
					drawChart();
				}	
				
			}	
			Loadingframe.close();
		}
		
		catch (Exception e) {
		
			e.printStackTrace();
		}
			
	}
		
	private void runLcaSeq(ArrayList<seqWithFileName> fileSetofSeq) {
		LinkedHashMap<String,String> seq_for_lca;
		MetaProteomicAnalysis meta = new MetaProteomicAnalysis();
		try{
			
			
			lframe = new Loadingframe(); // opens the loading frame
			lframe.bigStep("Calculating LCA..");
			
			for(int i = 0; i < fileSetofSeq.size();i++){
				String line = fileSetofSeq.get(i).getFileName();
				lframe.step(line);
				exportAll = true;
				//equal to cmd seqall.
				seq_for_lca = fileSetofSeq.get(i).getIdSeq();
				fileName = line +  "-";
				meta = new MetaProteomicAnalysis();
				returnData = meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca, fileName), false, batchCommand);
				
				drawChart = true;
				if (numChart < 1) {
					prepaintLCA();
				} 
				else {
					drawChart();
				}	
				
			}	
			Loadingframe.close();
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	

	private void prepaint() { // This method rebuids the back panel of the window
		removeAll(); // Removes everything on the backpanel
		initMainPanels(); // instanciates the options, display, and scroll panels
		addOptions(); // adds the buttons, labels, checkboxes etc to the options panel
		drawSampleNames();
		showGoValues(); // paints the go matrix showing the go values, calls showValues, or show odds.
		invalidate(); 
		validate(); 
		repaint(); 
	}
	
	private void drawSampleNames() {

		this.lframe.bigStep("drawSampleNames");

		this.nameLabels_ = new ArrayList<JLabel>();

		int x = 0;
		int xREAL = 0;
		for (x = 0; x < Project.samples_.size(); x++) {
			if ((((Sample) Project.samples_.get(x)).inUse)
					&& (((Sample) Project.samples_.get(x)).onoff)) {
				this.lframe.step(((Sample) this.smpList_.get(x)).name_);
				this.label_ = new JLabel(((Sample) this.smpList_.get(x)).name_);

				this.label_
						.setForeground(((Sample) this.smpList_.get(x)).sampleCol_);
				this.label_.setBounds(50 + (xREAL + 1) * 130, 20, 130, 15);
				if (this.selectedSampIndex_ == x) {
					this.label_.setBounds(this.label_.getX(),
							this.label_.getY() - 20, 400,
							this.label_.getHeight());
				}
				this.label_.setVisible(true);
				this.label_.setLayout(null);
				final int nameIndex = xREAL;

				this.nameLabels_.add(this.label_);
				this.displayP_.add(this.label_);

				final JPanel mousOverP = new JPanel();
				mousOverP.setBounds(50 + (xREAL + 1) * 130, 0, 130, 40);

				mousOverP.setBackground(Project.getBackColor_());

				mousOverP.setVisible(true);
				mousOverP.setLayout(null);
				final int index = x;
				mousOverP.addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent e) {
						Project.samples_.get(index).onoff = false;
					}

					public void mouseEntered(MouseEvent e) {
						if (GoActMatixPane.this.selectedSampIndex_ != index) {
							((JLabel) GoActMatixPane.this.nameLabels_
									.get(nameIndex)).setBounds(
									((JLabel) GoActMatixPane.this.nameLabels_
											.get(nameIndex)).getX(),
									((JLabel) GoActMatixPane.this.nameLabels_
											.get(nameIndex)).getY() - 20, 400,
									GoActMatixPane.this.label_.getHeight());
							if (GoActMatixPane.this.selectedSampIndex_ != -1) {
								((JLabel) GoActMatixPane.this.nameLabels_
										.get(GoActMatixPane.this.selectedSampIndex_))
										.setBounds(
												((JLabel) GoActMatixPane.this.nameLabels_
														.get(GoActMatixPane.this.selectedSampIndex_))
														.getX(),
												((JLabel) GoActMatixPane.this.nameLabels_
														.get(GoActMatixPane.this.selectedSampIndex_))
														.getY() + 20, 130,
												GoActMatixPane.this.label_
														.getHeight());
							}
						}
						GoActMatixPane.this.repaint();
					}

					public void mouseExited(MouseEvent e) {
						if (GoActMatixPane.this.selectedSampIndex_ != index) {
							((JLabel) GoActMatixPane.this.nameLabels_
									.get(nameIndex)).setBounds(
									((JLabel) GoActMatixPane.this.nameLabels_
											.get(nameIndex)).getX(),
									((JLabel) GoActMatixPane.this.nameLabels_
											.get(nameIndex)).getY() + 20, 130,
									GoActMatixPane.this.label_.getHeight());
							if (GoActMatixPane.this.selectedSampIndex_ != -1) {
								((JLabel) GoActMatixPane.this.nameLabels_
										.get(GoActMatixPane.this.selectedSampIndex_))
										.setBounds(
												((JLabel) GoActMatixPane.this.nameLabels_
														.get(GoActMatixPane.this.selectedSampIndex_))
														.getX(),
												((JLabel) GoActMatixPane.this.nameLabels_
														.get(GoActMatixPane.this.selectedSampIndex_))
														.getY() - 20, 400,
												GoActMatixPane.this.label_
														.getHeight());
							}
						}
						GoActMatixPane.this.repaint();
					}
					
					public void mousePressed(MouseEvent e) {
					
					}

					public void mouseReleased(MouseEvent e) {
					}
				});
				this.displayP_.add(mousOverP);
				xREAL++;

			}
		}
		this.label_ = new JLabel(" Sum");

		this.label_.setForeground(Color.black);
		this.label_.setBounds(50 + (xREAL + 1) * 130, 20, 130, 15);
		this.label_.setVisible(true);
		this.label_.setLayout(null);

		this.displayP_.add(this.label_);
	
		
	}

	private void initMainPanels() {// Instantiates the options, display, and scroll panels
		this.optionsPanel_ = new JPanel();
		this.optionsPanel_.setPreferredSize(new Dimension(getWidth(), 100));
		this.optionsPanel_.setLocation(0, 0);
		this.optionsPanel_.setBackground(Project.standard);
		this.optionsPanel_.setVisible(true);
		this.optionsPanel_.setLayout(null);
		add(this.optionsPanel_, "First");
		this.displayP_ = new JPanel();
		this.displayP_.setLocation(0, 0);
		this.displayP_.setPreferredSize(new Dimension((Project.samples_.size() + 2) * 130,(this.goMatrix_.size() + 2) * 15 + 100));
		this.displayP_.setSize(getPreferredSize());
		this.displayP_.setBackground(Project.getBackColor_());
		this.displayP_.setVisible(true);
		this.displayP_.setLayout(null);
		this.showJPanel_ = new JScrollPane(this.displayP_);
		this.showJPanel_.setVisible(true);
		this.showJPanel_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.showJPanel_.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add("Center", this.showJPanel_);
	}
	
	/**
	 * Draws the tables and pie chart for the determine Lowest Common Ancestor for a given GO number
	 * or sample file.
	 * 
	 * @author Jennifer Terpstra.
	 */
	protected void drawChart(){
		final MetaProteomicAnalysis meta = new MetaProteomicAnalysis();
		PiePlot pie1=null;
		final JFreeChart chart;
		System.out.println(returnData.getFileName());
		if(returnData.getFileName()!=null&&returnData.getDataset()!=null&&returnData.getTable1()!=null&&returnData.getTable2()!=null){
			chart = ChartFactory.createPieChart("", returnData.getDataset(), true, true, false);
			pie1 = (PiePlot) chart.getPlot();
		}
		else{
			System.out.println("No data");
			chart=null;
			JPanel NoDatapanel = new JPanel(new BorderLayout());
			NoDatapanel.setBorder (BorderFactory.createTitledBorder 
					(BorderFactory.createEtchedBorder (), returnData.getFileName()+"No_Data ",TitledBorder.CENTER,TitledBorder.TOP));
			NoDatapanel.setVisible(true);
			NoDatapanel.setBounds(0, yIndex2 + (yIndex1 + 530)*numChart + 80*NoDataChart, 500,50);
			displayP_.add(NoDatapanel, BorderLayout.LINE_START);
			NoDataChart++;
			return;  
		}
		
		pie1.setNoDataMessage("No data available");
		pie1.setCircular(false);
		pie1.setLabelGenerator(null);
		
		JPanel panel = new ChartPanel(chart);
		JPanel panel2 = new JPanel(new BorderLayout());
		JPanel panel4 = new JPanel(new BorderLayout());
		JPanel panel3 = new JPanel(new BorderLayout());
		
		
		JButton exportPie = new JButton("Export");
		exportPie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String path = StartFromp1.FolderPath+ "PieChart" + File.separator + returnData.getFileName()
							+ " Total Taxonomy" + ".png";
					ChartUtilities.saveChartAsPNG(new File(path), chart, 1000, 1000);
					infoFrame(StartFromp1.FolderPath+ "PieChart" + File.separator + returnData.getFileName(), "PieChart");
				} catch (IOException e1) {
					warningFrame("PieChart folder does not exist!");
				}
			}
		});
		JButton exportTable1 = new JButton("Export");
		exportTable1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				meta.exportExcel(returnData.getTable1(),"TotalTaxa", returnData.getFileName());
				System.out.println("Exported Total Taxa");
			}
		});
		JButton exportTable2 = new JButton("Export");
		exportTable2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				meta.exportExcel(returnData.getTable2(),"Summary", returnData.getFileName());
				System.out.println("Exported Summary");
			}
		});
		
		panel.setVisible(true);
		panel.setLayout(null);
		
		panel3.add(exportPie, BorderLayout.NORTH);		
		panel3.setVisible(true);		
		panel3.setBounds(1020,yIndex2 + (yIndex1 + 530)*numChart + 80*NoDataChart , 500, 500);
		panel3.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (), returnData.getFileName()+
				"Pie Chart",TitledBorder.CENTER,TitledBorder.TOP));
		panel3.add(panel);
	
		JScrollPane tableContainer = new JScrollPane(returnData.getTable1());
		panel2.add(tableContainer, BorderLayout.CENTER);
		panel2.add(exportTable1, BorderLayout.NORTH);
		panel2.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (), returnData.getFileName()+
				"Total Taxonomy",TitledBorder.CENTER,TitledBorder.TOP));
		panel2.setVisible(true);
		panel2.setBounds(0, yIndex2 + (yIndex1 + 530)*numChart + 80*NoDataChart, 500,500);
		
		JScrollPane tableContainer2 = new JScrollPane(returnData.getTable2());
		panel4.add(tableContainer2, BorderLayout.CENTER);
		panel4.add(exportTable2, BorderLayout.NORTH);
		panel4.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (), returnData.getFileName()+
				" Summary",TitledBorder.CENTER,TitledBorder.TOP));
		panel4.setVisible(true);
		panel4.setBounds(510, yIndex2 + (yIndex1 + 530)*numChart + 80*NoDataChart, 500,500);
		numChart++;
		
		//extending the scroll bar in the find lowest common ancestor pane as more charts are added
		scrollChartPos = yIndex1 + (yIndex1 + 530)*numChart + 80*NoDataChart;
		displayP_.setPreferredSize(new Dimension(1520,scrollChartPos));
		
		
		displayP_.add(panel3, BorderLayout.LINE_START);
		displayP_.add(panel2, BorderLayout.LINE_START);
		displayP_.add(panel4, BorderLayout.LINE_START);
		
		this.showJPanel_.setViewportView(displayP_);
		this.showJPanel_.setVisible(true);
		this.showJPanel_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.showJPanel_.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	}

	private void prepGoMatrix() {// This preps the onscreen matrix using the arrayline_[] variable in the line object.
		
		this.goMatrix_ = new ArrayList<Line>();
		this.lframe = new Loadingframe();
		this.lframe.bigStep("Preparing GO Matrix");
		this.lframe.step("Init");
		this.sumIndexSmp = 0;
		for (int x = 0; x < Project.samples_.size(); x++) {
			if ((((Sample) Project.samples_.get(x)).inUse)
					&& (((Sample) Project.samples_.get(x)).onoff)) {
				this.sumIndexSmp += 1;
			}
		}
		int indexSmp = -1;

		this.mappedSum = new Line(new double[this.sumIndexSmp], true, false,
				false);
		this.mappedSum.fillWithZeros();
		this.unmappedSum = new Line(new double[this.sumIndexSmp], false, true,
				false);
		this.unmappedSum.fillWithZeros();
		this.incompleteSum = new Line(new double[this.sumIndexSmp], false,
				false, true);
		this.incompleteSum.fillWithZeros();
		this.sums = new Line(new double[this.sumIndexSmp], false, false, false);
		this.sums.fillWithZeros();
		for (int x = 0; x < Project.samples_.size(); x++) {
			if ((((Sample) Project.samples_.get(x)).inUse)
					&& (((Sample) Project.samples_.get(x)).onoff)) {
				indexSmp++;
				ArrayList<GONum> actSample = ((Sample) Project.samples_.get(x)).gos_;
				for (int goCnt = 0; goCnt < actSample.size(); goCnt++) {
					GONum actGo = actSample.get(goCnt);
				
					boolean found = false;
					this.lframe.step(actGo.GoNumber);
					for (int arrCnt = 0; arrCnt < this.goMatrix_.size(); arrCnt++) {
						if (actGo.GoNumber.contentEquals(((Line) this.goMatrix_.get(arrCnt)).getGoNr_().GoNumber)) {
							found = true;
							((Line) this.goMatrix_.get(arrCnt)).arrayLine_[indexSmp] = actGo.amount_;
							this.sums.arrayLine_[indexSmp] += actGo.amount_;
							this.incompleteSum.arrayLine_[indexSmp] += actGo.amount_;

							break;
						}
					}
					if (!found) {
						Line line = new Line(actGo,new double[this.sumIndexSmp]);
						line.fillWithZeros();
						line.arrayLine_[indexSmp] = actGo.amount_;
						this.sums.arrayLine_[indexSmp] += actGo.amount_;
						this.incompleteSum.arrayLine_[indexSmp] += actGo.amount_;
						this.goMatrix_.add(line);
					}		
				}
			}
		}
		for (int arrCnt = 0; arrCnt < this.goMatrix_.size(); arrCnt++) {
			goMatrix_.get(arrCnt).setSum();
		}
		this.mappedSum.setSum();
		this.unmappedSum.setSum();
		this.incompleteSum.setSum();
		this.sums.setSum();
		Loadingframe.close();	
		
	}

	private void addOptions() {// adds the buttons, labels, check boxes etc to the options panel
		this.lframe.bigStep("Adding options");
		this.lframe.step("Buttons");
		this.optionsPanel_.removeAll();
		
		JLabel geo_dist_label = new JLabel("Enrichment Options:");
		geo_dist_label.setBounds(200,1,150,15);
		geo_dist_label.setVisible(true);
		this.optionsPanel_.add(geo_dist_label);
		
		JLabel general_label = new JLabel("General Options:");
		general_label.setBounds(500,1,200,15);
		general_label.setVisible(true);
		this.optionsPanel_.add(general_label);
		
		if (this.bySumcheck_ == null) {
			this.bySumcheck_ = new JCheckBox("Sort by GO sum");
		}
		this.bySumcheck_.setVisible(true);
		this.bySumcheck_.setLayout(null);
		this.bySumcheck_.setBackground(this.optionsPanel_.getBackground());
		this.bySumcheck_.setForeground(Project.getFontColor_());
		this.bySumcheck_.setBounds(500, 20, 200, 15);
		this.optionsPanel_.add(this.bySumcheck_);
		
		if (this.useOddsrat_ == null) {
			this.useOddsrat_ = new JCheckBox("Odds-ratio");
		}
		this.useOddsrat_.setVisible(true);
		this.useOddsrat_.setLayout(null);
		this.useOddsrat_.setBackground(this.optionsPanel_.getBackground());
		this.useOddsrat_.setForeground(Project.getFontColor_());
		this.useOddsrat_.setBounds(200, 20, 230, 15);
		this.optionsPanel_.add(this.useOddsrat_);
		
		if (this.sort_odds_by_lowest == null) {
			this.sort_odds_by_lowest = new JCheckBox("Sort by Lowest Odds");
		}
		this.sort_odds_by_lowest.setVisible(true);
		this.sort_odds_by_lowest.setLayout(null);
		this.sort_odds_by_lowest.setBackground(this.optionsPanel_.getBackground());
		this.sort_odds_by_lowest.setForeground(Project.getFontColor_());
		this.sort_odds_by_lowest.setBounds(200, 37, 230, 15);
		this.optionsPanel_.add(this.sort_odds_by_lowest);
		
		if (this.useGeoDist_ == null) {
			this.useGeoDist_ = new JCheckBox("Hypergeometric P-Value");
		}
		this.useGeoDist_.setVisible(true);
		this.useGeoDist_.setLayout(null);
		this.useGeoDist_.setBackground(this.optionsPanel_.getBackground());
		this.useGeoDist_.setForeground(Project.getFontColor_());
		this.useGeoDist_.setBounds(200, 61, 250, 15);
		this.optionsPanel_.add(this.useGeoDist_);
		
		if (this.sort_by_lowest_Geo == null) {
			this.sort_by_lowest_Geo = new JCheckBox("Sort by Lowest P-Value");
		}
		this.sort_by_lowest_Geo.setVisible(true);
		this.sort_by_lowest_Geo.setLayout(null);
		this.sort_by_lowest_Geo.setBackground(this.optionsPanel_.getBackground());
		this.sort_by_lowest_Geo.setForeground(Project.getFontColor_());
		this.sort_by_lowest_Geo.setBounds(200, 78, 250, 15);
		this.optionsPanel_.add(sort_by_lowest_Geo);
		
		if (this.useCsf_ == null) {
			this.useCsf_ = new JCheckBox("CSF");
		}
		this.useCsf_.setVisible(true);
		this.useCsf_.setLayout(null);
		this.useCsf_.setBackground(this.optionsPanel_.getBackground());
		this.useCsf_.setForeground(Project.getFontColor_());
		this.useCsf_.setBounds(10, 61, 100, 15);
		this.optionsPanel_.add(this.useCsf_);
		
		/*
		 * More powerful than the "Apply Options" button, doesn't just resort
		 * the matrix but instead rebuilds the entire matrix along with the back panel.
		 * Shouldn't be used unless the user is looking to remove samples from their
		 * view as it will take more time than the apply options button. This will be especally
		 * noticable with large samples, but much less noticable with small samples.
		 */
		this.resort_ = new JButton("Rebuild"); 
		this.resort_.setBounds(700, 50, 200, 30);
		this.resort_.setVisible(true);
		this.resort_.setLayout(null);
		this.resort_.setForeground(Project.getFontColor_());
		this.resort_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int test = 0;// A temp variable to ensure that at least on of the samples.onoff=true
				for (int i = 0; i < Project.samples_.size(); i++) {
					if (Project.samples_.get(i).onoff == true) {
						test++;
					}
				}
				/*
				 * You can't view less than one sample at a time or the program will crash,
				 * this stops the user from being able to do that and makes a window that
				 * warns the user that they will be unable to do so.
				 */
				if (test < 1) {
					final JFrame frame = new JFrame("Warning!");
					frame.setBounds(200, 200, 300, 110);
					frame.setLayout(null);
					frame.setVisible(true);

					JPanel backP = new JPanel();
					backP.setBounds(0, 0, 300, 110);
					backP.setLayout(null);
					frame.add(backP);

					JLabel label = new JLabel("Warning! You must view at least");
					label.setBounds(10, 10, 280, 15);
					backP.add(label);
					JLabel label3 = new JLabel("One sample at a time.");
					label3.setBounds(10, 25, 280, 15);
					backP.add(label3);

					JLabel label2 = new JLabel("Samples have been reset.");
					label2.setBounds(10, 50, 280, 25);
					backP.add(label2);

					for (int i = 0; i < Project.samples_.size(); i++) {
						Project.samples_.get(i).onoff = true;
					}
				}
				prepGoMatrix();
				initMainPanels();
				prepaint();
				Loadingframe.close();
				
				/*
				 * After this button is pressed all sample.onoff are reset to true so that
				 * when the matrix is rebuilt again all samples will be there again. This way the user
				 * doesn't 'lose' any of their samples
				 */
				for (int i = 0; i < Project.samples_.size(); i++) {
					Project.samples_.get(i).onoff = true;
				}
			}		
		});
		this.optionsPanel_.add(this.resort_);

		if (this.exportGos_ == null) {
			this.exportGos_ = new JButton("Write GOs to file");
			this.exportGos_.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//JFrame frame =  new JFrame("Write Ecs to file");
					//frame.setVisible(true);
					//JOptionPane.showMessageDialog(null,strIN, "Warning", JOptionPane.WARNING_MESSAGE);
					String numExport = JOptionPane.showInputDialog(null,"Enter Number of Ecs to export","All");
					if(checkValidExportNum(numExport)){
						if(numExport.equalsIgnoreCase("all")){
							num_exportMat_go = 0;
						}
						else{
							num_exportMat_go = Integer.parseInt(numExport);
						}
					
					JFileChooser fChoose_ = new JFileChooser(Project.workpath_);
					fChoose_.setFileSelectionMode(0);
					fChoose_.setBounds(100, 100, 200, 20);
					fChoose_.setVisible(true);
					File file = new File(Project.workpath_);
					fChoose_.setSelectedFile(file);
					fChoose_.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							if ((f.isDirectory())
									|| (f.getName().toLowerCase()
											.endsWith(".txt"))) {
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
							GoActMatixPane.this.exportGoNums(path, num_exportMat_go);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				}
			});
		}
		this.exportGos_.setBounds(10, 38, 170, 20);
		this.exportGos_.setVisible(true);
		this.exportGos_.setLayout(null);
		this.exportGos_.setForeground(Project.getFontColor_());
		this.optionsPanel_.add(this.exportGos_);
		
		if (this.exportMat_ == null) {
			this.exportMat_ = new JButton("Write Matrix to file");
			this.exportMat_.addActionListener(new ActionListener() {
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
							String path = fChoose_.getSelectedFile().getCanonicalPath();
							if (!path.endsWith(".txt")) {
								path = path + ".txt";
								System.out.println(".txt");
							}
							GoActMatixPane.this.exportMatGo(path,	GoActMatixPane.this.useCsf_.isSelected());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				
			});
		}
		this.exportMat_.setBounds(10, 10, 170, 20);
		this.exportMat_.setVisible(true);
		this.exportMat_.setLayout(null);
		this.exportMat_.setForeground(Project.getFontColor_());
		this.optionsPanel_.add(this.exportMat_);
		
		
		if (this.moveUnmappedToEnd == null) {
			this.moveUnmappedToEnd = new JCheckBox("Unmapped at end of list");
			this.moveUnmappedToEnd.setSelected(true);
		}
		this.moveUnmappedToEnd.setVisible(true);
		this.moveUnmappedToEnd.setLayout(null);
		this.moveUnmappedToEnd.setBackground(this.optionsPanel_.getBackground());
		this.moveUnmappedToEnd.setForeground(Project.getFontColor_());
		this.moveUnmappedToEnd.setBounds(500, 78, 200, 15);
		this.optionsPanel_.add(this.moveUnmappedToEnd);
		
		if (this.includeRepseq_ == null) {
			this.includeRepseq_ = new JCheckBox("Include Sequence-Ids");
			this.includeRepseq_.setSelected(false);
		}
		this.includeRepseq_.setVisible(true);
		this.includeRepseq_.setLayout(null);
		this.includeRepseq_.setForeground(Project.getFontColor_());
		this.includeRepseq_.setBackground(this.optionsPanel_.getBackground());
		this.includeRepseq_.setBounds(500, 44, 200, 15);
		this.optionsPanel_.add(this.includeRepseq_);
		
		if (this.maxVisField_ == null) {
			this.maxVisField_ = new JTextField((int) Math.round(this.maxVisVal_));
		}
		this.maxVisField_.setBounds(600, 10, 100, 20);
		this.maxVisField_.setLayout(null);
		this.maxVisField_.setVisible(true);

		this.resort_ = new JButton("Apply options");
		this.resort_.setBounds(700, 10, 200, 30);
		this.resort_.setVisible(true);
		this.resort_.setLayout(null);
		this.resort_.setForeground(Project.getFontColor_());
		this.resort_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GoActMatixPane.this.resort();
			}
		});
		this.optionsPanel_.add(this.resort_);
		
		if (this.bySumcheck_ != null||this.useGeoDist_!=null||this.useOddsrat_!=null) {
			if (this.bySumcheck_.isSelected()) {
				this.sort_by_lowest_Geo.setSelected(false);
				this.sort_odds_by_lowest.setSelected(false);
				System.out.println("\nquicksort\n");
				quicksortSumGo();
				this.goMatrix_ = removeDuplicatesGo();
				System.out.println("\nquicksort done\n");
				System.out.println("bySum");
			}
			else {
				System.out.println("one");
				sortGOsbyNameQuickSort();
				this.goMatrix_ = removeDuplicatesGo();
				System.out.println("byName");
			}
			if(this.useGeoDist_.isSelected()){
				this.useOddsrat_.setSelected(false);
				this.sort_odds_by_lowest.setSelected(false);
				if(this.sort_by_lowest_Geo.isSelected()){
					this.bySumcheck_.setSelected(false);
					System.out.println("by Dist");
					quicksortGeoDistGo();
					this.goMatrix_ = removeDuplicatesGo();
				}
				else if(this.bySumcheck_.isSelected()){
					quicksortSumGo();
					this.goMatrix_ = removeDuplicatesGo();
					System.out.println("\nquicksort done\n");
					System.out.println("bySum");
				}
				else{
					System.out.println("two");
					sortGOsbyNameQuickSort();
					this.goMatrix_ = removeDuplicatesGo();
					System.out.println("byName");
				}
			}
			if(this.useOddsrat_.isSelected()){
				this.useGeoDist_.setSelected(false);
				this.sort_by_lowest_Geo.setSelected(false);
				if(this.sort_odds_by_lowest.isSelected()){
					System.out.println("eight");
					this.bySumcheck_.setSelected(false);
					System.out.println("sort by Odds");
					quicksortOdds();
					this.goMatrix_ = removeDuplicatesGo();
				}
				else if(this.bySumcheck_.isSelected()){
					quicksortSumGo();
					this.goMatrix_ = removeDuplicatesGo();
					System.out.println("\nquicksort done\n");
					System.out.println("bySum");
				}
				else{
					System.out.println("three");
					sortGOsbyNameQuickSort();
					this.goMatrix_ = removeDuplicatesGo();
					System.out.println("byName");
				}
			}
		} 
		else {
			System.out.println("four");
			sortGOsbyNameQuickSort();
			this.goMatrix_ = removeDuplicatesGo();
			System.out.println("byName");
		}
	}

	private void resort() {
		this.lframe = new Loadingframe();
		this.lframe.bigStep("resorting");
		this.sortedGo = false;

		if (this.bySumcheck_.isSelected()) {
			quicksortSumGo();
			this.goMatrix_ = removeDuplicatesGo();
		} else {
			sortGOsbyNameQuickSort();
		}

		prepaint();
		Loadingframe.close();
	}

	private void showGoValues() {// paints the go matrix showing the go values, calls showValues, or show odds
		this.lframe.bigStep("showGoValues");
		int goCnt = 0;
		for (goCnt = 0; goCnt < this.goMatrix_.size(); goCnt++) {
			Line goNr = (Line) this.goMatrix_.get(goCnt);
			this.lframe.step(goNr.getGoNr_().GoNumber);
//			if (!sumLineDrawn) {
//				addGoButton(this.mappedSum, goCnt + 1);
//				showValues(this.mappedSum, goCnt + 1);
//				addGoButton(this.unmappedSum, goCnt + 2);
//				showValues(this.unmappedSum, goCnt + 2);
//
//				sumLineDrawn = true;
//			}
			addGoButton(goNr, goCnt);
			
			if(this.useOddsrat_.isSelected()){
				this.useGeoDist_.setSelected(false);
			}
			else if(this.useGeoDist_.isSelected()){
				this.useOddsrat_.setSelected(false);
			}
			
			if (this.useOddsrat_.isSelected()&&!this.useGeoDist_.isSelected()) {
				//System.out.println("Odds ratio");
				showOdds(goNr, goCnt);
			} 
			else if(!this.useOddsrat_.isSelected()&&!this.useGeoDist_.isSelected()){
				//System.out.println("not odds");
				showValues(goNr, goCnt);
			}
			
			if (this.useGeoDist_.isSelected()&&!this.useOddsrat_.isSelected()) {
				showHypergeometricDistributionGo(goNr, goCnt);
				//System.out.println("Hyper");
			}
			else if(!this.useGeoDist_.isSelected()&&!this.useOddsrat_.isSelected()){
				//System.out.println("not hyper");
				showValues(goNr, goCnt);
			}
		}
		addGoButton(this.sums, goCnt);
		showValues(this.sums, goCnt);
	}
	//prints the go matrix, but in place of the gos the odds ratios are used
	private void showOdds(Line goNr, int index) {
		
		if (goNr.isSumline_()) {
			addSumLineVals(goNr, index);
			return;
		}
		int uncompleteOffset = 0;
		ArrayList<Double> odds_list = new ArrayList<Double>();
		ArrayList<JLabel> odd_labels = new ArrayList<JLabel>();
		
		for (int smpCnt = 0; smpCnt < goNr.arrayLine_.length; smpCnt++) {
			if (goNr.arrayLine_[smpCnt] != 0.0D) {
				float a = (float) goNr.arrayLine_[smpCnt];
				float b = (float) (this.sums.arrayLine_[smpCnt] - a);
				float c = goNr.sum_ - a;
				float d = this.sums.sum_ - a - b - c;
				
				odds_list.add(Double.parseDouble(String.valueOf(odds(a, b, c, d))));
				this.label_ = new JLabel(String.valueOf(odds(a, b, c, d)));
				odd_labels.add(this.label_);
				
				if(this.includeRepseq_.isSelected()){
					this.label_.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				
				this.lframe.step("adding1 " + goNr.arrayLine_[smpCnt]);
				if (this.includeRepseq_.isSelected()) {
					final int indexY = index;
					final int indexX = smpCnt;
					JMenuItem mItem = new JMenuItem("Export");
					menuPopup = new JPopupMenu();

					menuPopup.add(mItem);
					GoActMatixPane.this.setComponentPopupMenu(menuPopup);
					/*
					 * Popup menu that comes up when you right click any of the ECs when
					 * the "include sequence id" option in selected
					 */
					mItem.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							GONum goTmp = new GONum(((Line) GoActMatixPane.this.goMatrix_
											.get(popupIndexY)).getGoNr_());
							goTmp.amount_ = ((int) ((Line) GoActMatixPane.this.goMatrix_
									.get(popupIndexY)).arrayLine_[indexX]);
							ArrayList<ConvertStatGo> reps = new ArrayList<ConvertStatGo>();
							for (int statsCnt = 0; statsCnt < ((Sample) Project.samples_
									.get(popupIndexX)).conversionsGo_.size(); statsCnt++) {
								String test = (((ConvertStatGo) ((Sample) Project.samples_
										.get(popupIndexX)).conversionsGo_
										.get(statsCnt)).getDesc_());
								if ((goTmp.GoNumber
										.contentEquals(((ConvertStatGo) ((Sample) Project.samples_
												.get(popupIndexX)).conversionsGo_
												.get(statsCnt)).getGoNr_()))
										&& !test.contains("\t")) {
									reps.add((ConvertStatGo) ((Sample) Project.samples_
											.get(popupIndexX)).conversionsGo_
											.get(statsCnt));
								}
							}

							String test = "";
							String test2 = "";
							for (int i = reps.size() - 1; i >= 0; i--) {
								if ((reps.get(i) == null)) {
								} else {
									test = ((ConvertStatGo) reps.get(i))
											.getDesc_();
									if (test.contains("\t")) {
										reps.set(i, null);
									} else {
										for (int j = i - 1; j >= 0; j--) {
											if ((reps.get(j) == null)) {
											} else {
												test2 = ((ConvertStatGo) reps
														.get(j)).getDesc_();
												if (test.contains(test2)) {
													reps.set(j, null);
												}
											}
										}
									}
								}
							}
							for (int i = reps.size() - 1; i >= 0; i--) {
								if (reps.get(i) == null) {
									reps.remove(i);
								}
							}

							String sampName = ((Sample) Project.samples_
									.get(popupIndexX)).name_;
							ExportRepsGo(reps, goTmp, sampName);
						}
					});
					/*
					 * Opens up the window when you left click the ECs with "include sequence ids"
					 * selected
					 */
					this.label_.addMouseListener(new MouseListener()
							{
								public void mouseClicked(MouseEvent e) {
									if (SwingUtilities.isRightMouseButton(e)
											|| e.isControlDown()) {
										GoActMatixPane.this.menuPopup.show(
												e.getComponent(), e.getX(),
												e.getY());
										popupIndexY = indexY;
										popupIndexX = indexX;
									} else {
										GONum goTmp = new GONum(
												(GoActMatixPane.this.goMatrix_
														.get(indexY)).getGoNr_());
										goTmp.amount_ = ((int) ((Line) GoActMatixPane.this.goMatrix_
												.get(indexY)).arrayLine_[indexX]);
										ArrayList<ConvertStatGo> reps = new ArrayList<ConvertStatGo>();
										for (int statsCnt = 0; statsCnt < ((Sample) Project.samples_
												.get(indexX)).conversionsGo_
												.size(); statsCnt++) {
											String test = (((ConvertStatGo) ((Sample) Project.samples_
													.get(indexX)).conversionsGo_
													.get(statsCnt)).getDesc_());
											if ((goTmp.GoNumber
													.contentEquals(((ConvertStatGo) ((Sample) Project.samples_
															.get(indexX)).conversionsGo_
															.get(statsCnt))
															.getGoNr_()))
													&& !test.contains("\t")) {
												reps.add((ConvertStatGo) ((Sample) Project.samples_
														.get(indexX)).conversionsGo_
														.get(statsCnt));
											}
										}

										String test = "";
										String test2 = "";
										for (int i = reps.size() - 1; i >= 0; i--) {
											if ((reps.get(i) == null)) {
											} else {
												test = ((ConvertStatGo) reps
														.get(i)).getDesc_();
												if (test.contains("\t")) {
													reps.set(i, null);
												} else {
													for (int j = i - 1; j >= 0; j--) {
														if ((reps.get(j) == null)) {
														} else {
															test2 = ((ConvertStatGo) reps
																	.get(j))
																	.getDesc_();
															if (test.contains(test2)) {
																reps.set(j,
																		null);
															}
														}
													}
												}
											}
										}
										for (int i = reps.size() - 1; i >= 0; i--) {
											if (reps.get(i) == null) {
												reps.remove(i);
											}
										}

										String sampName = ((Sample) Project.samples_
												.get(indexX)).name_;
										new RepseqFrameGo(reps, goTmp, sampName);
									}
								}

								public void mouseEntered(MouseEvent e) {
								}

								public void mouseExited(MouseEvent e) {
								}

								public void mousePressed(MouseEvent e) {
								}

								public void mouseReleased(MouseEvent e) {
								}
							});
				}
			} else {
				this.label_ = new JLabel("0");
			}
			this.label_.setBounds(50 + (smpCnt + 1) * 130, uncompleteOffset
					+ 50 + index * 15, 130, 15);
			this.label_.setVisible(true);
			this.label_.setLayout(null);
			this.displayP_.add(this.label_);
		}
		goNr.setOdd_nums(odds_list);
		goNr.setOdds_labels(odd_labels);
		add_Odd_Colour(goNr);
		
		if (goNr.sum_ != 0) {
			this.label_ = new JLabel(String.valueOf(goNr.sum_));
			this.lframe.step("adding2 " + goNr.sum_);
		} else {
			this.label_ = new JLabel("0");
		}
		this.label_.setBounds(50 + (goNr.arrayLine_.length + 1) * 130,
				uncompleteOffset + 50 + index * 15, 130, 15);
		this.label_.setVisible(true);
		this.label_.setLayout(null);
		this.displayP_.add(this.label_);
		
	}
	
	/**
	 * Colours the lowest found odd ratios in each Ec number between the samples yellow
	 * 
	 * @param goNr Ec Number being considered
	 * @author Jennifer Terpstra
	 */
	private void add_Odd_Colour(Line goNr){
		for(int i = 0;i<goNr.getOdds_labels().size();i++){
			if (!goNr.getOdds_labels().get(i).getText().equals("Infinity")) {
				double odd_Num = Double.parseDouble(goNr.getOdds_labels().get(i).getText());
				if (odd_Num == goNr.getLowest_odds()) {
					goNr.getOdds_labels().get(i).setBackground(Color.YELLOW);
					goNr.getOdds_labels().get(i).setOpaque(true);
				}
			}
		}
	}
	
	/**
	 * Displays all calculated hypergeometric distribution p values for each
	 * each per sample. 
	 * @param ecNr Ec number
	 * @param index
	 * 
	 * @author Jennifer Terpstra
	 */
	
	public void showHypergeometricDistributionGo(Line goNr, int index){
		int total_go = 0;
		ArrayList<Double> hype_dist = new ArrayList<Double>(); 
		ArrayList<JLabel> geo_labels = new ArrayList<JLabel>();
		if (goNr.isSumline_()) {
			addSumLineVals(goNr, index);
			return;
		}
		int uncompleteOffset = 0;
		//finding the total amount of go's found in all the samples
		for(int i = 0; i<goNr.arrayLine_.length; i++){
			total_go += (float) (this.sums.arrayLine_[i]);
		}
		for (int smpCnt = 0; smpCnt < goNr.arrayLine_.length; smpCnt++) {
				float x = (float) goNr.arrayLine_[smpCnt];
				float n = (float) (this.sums.arrayLine_[smpCnt]);
				float K = goNr.sum_;
				float M = total_go;
				
				HypergeometricDistribution tmp_dist = new HypergeometricDistribution((int)M,(int)K,(int)n);
				hype_dist.add(tmp_dist.probability((int)x));
				this.label_ = new JLabel(String.valueOf((tmp_dist.probability((int)x))));
				geo_labels.add(this.label_);
				if(includeRepseq_!=null && includeRepseq_.isSelected()){
					this.label_.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				this.label_.setToolTipText(String.valueOf((tmp_dist.probability((int)x))));
				this.lframe.step("adding3 " + goNr.arrayLine_[smpCnt]);
				if (includeRepseq_!=null && includeRepseq_.isSelected()) {
					final int indexY = index;
					final int indexX = smpCnt;
					JMenuItem mItem = new JMenuItem("Export");
					menuPopup = new JPopupMenu();

					menuPopup.add(mItem);
					GoActMatixPane.this.setComponentPopupMenu(menuPopup);
					/*
					 * Popup menu that comes up when you right click any of the ECs when
					 * the "include sequence id" option in selected
					 */
					mItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e) {
							GONum goTmp = new GONum(((Line) GoActMatixPane.this.goMatrix_.get(popupIndexY)).getGoNr_());
							goTmp.amount_ = ((int) ((Line) GoActMatixPane.this.goMatrix_.get(popupIndexY)).arrayLine_[indexX]);
							ArrayList<ConvertStatGo> reps = new ArrayList<ConvertStatGo>();
							for (int statsCnt = 0; statsCnt < ((Sample) Project.samples_
									.get(popupIndexX)).conversionsGo_.size(); statsCnt++) {
								String test = ((((Sample) Project.samples_.get(popupIndexX)).conversionsGo_.get(statsCnt)).getDesc_());
								if (goTmp.GoNumber.contentEquals(Project.samples_.get(popupIndexX).conversionsGo_.get(statsCnt).getGoNr_()) 
										&& !test.contains("\t")) {
									reps.add(Project.samples_.get(popupIndexX).conversionsGo_.get(statsCnt));
								}
							}

							String test = "";
							String test2 = "";
							for (int i = reps.size() - 1; i >= 0; i--) {
								if ((reps.get(i) == null)) {
								} else {
									test = reps.get(i).getDesc_();
									if (test.contains("\t")) {
										reps.set(i, null);
									} else {
										for (int j = i - 1; j >= 0; j--) {
											if ((reps.get(j) == null)) {
											} else {
												test2 = reps.get(j).getDesc_();
												if (test.contains(test2)) {
													reps.set(j, null);
												}
											}
										}
									}
								}
							}
							for (int i = reps.size() - 1; i >= 0; i--) {
								if (reps.get(i) == null) {
									reps.remove(i);
								}
							}

							String sampName = ((Sample) Project.samples_
									.get(popupIndexX)).name_;
							ExportRepsGo(reps, goTmp, sampName);
						}
					});
					/*
					 * Opens up the window when you left click the ECs with "include sequence ids"
					 * selected
					 */
					this.label_.addMouseListener(new MouseListener()
							{
								public void mouseClicked(MouseEvent e) {
									if (SwingUtilities.isRightMouseButton(e)
											|| e.isControlDown()) {
										System.out.println("Right Button Pressed");
										GoActMatixPane.this.menuPopup.show(e.getComponent(), e.getX(),e.getY());
										popupIndexY = indexY;
										popupIndexX = indexX;
									} 
									else {
										GONum goTmp = new GONum(GoActMatixPane.this.goMatrix_.get(indexY).getGoNr_());
										goTmp.amount_ = ((int) ((Line) GoActMatixPane.this.goMatrix_.get(indexY)).arrayLine_[indexX]);
										ArrayList<ConvertStatGo> reps = new ArrayList<ConvertStatGo>();
										for (int statsCnt = 0; statsCnt < Project.samples_.get(indexX).conversionsGo_.size(); statsCnt++) {
											String test = (((ConvertStatGo) ((Sample) Project.samples_.get(indexX)).conversionsGo_.get(statsCnt)).getDesc_());
											if ((goTmp.GoNumber.contentEquals(Project.samples_.get(indexX).conversionsGo_.get(statsCnt).getGoNr_()))
													&& !test.contains("\t")) {
												reps.add(Project.samples_.get(indexX).conversionsGo_.get(statsCnt));
											}
										}

										String test = "";
										String test2 = "";
										for (int i = reps.size() - 1; i >= 0; i--) {
											if ((reps.get(i) == null)) {
											} 
											else {
												test = reps.get(i).getDesc_();
												if (test.contains("\t")) {
													reps.set(i, null);
												} else {
													for (int j = i - 1; j >= 0; j--) {
														if ((reps.get(j) == null)) {
														} else {
															test2 = reps.get(j).getDesc_();
															if (test.contains(test2)) {
																reps.set(j,null);
															}
														}
													}
												}
											}
										}
										for (int i = reps.size() - 1; i >= 0; i--) {
											if (reps.get(i) == null) {
												reps.remove(i);
											}
										}

										String sampName = ((Sample) Project.samples_
												.get(indexX)).name_;
										new RepseqFrameGo(reps, goTmp, sampName);
									}
								}

								public void mouseEntered(MouseEvent e) {
								}

								public void mouseExited(MouseEvent e) {
								}

								public void mousePressed(MouseEvent e) {
								}

								public void mouseReleased(MouseEvent e) {
								}
							});
				}
			this.label_.setBounds(50 + (smpCnt + 1) * 130, uncompleteOffset
					+ 50 + index * 15, 130, 15);
			this.label_.setVisible(true);
			this.label_.setLayout(null);
			this.displayP_.add(this.label_);
		}
		
		goNr.setDist_nums(hype_dist);
		goNr.setGeo_labels(geo_labels);
		add_Geo_Dist_Colour(goNr);
		
		if (goNr.sum_ != 0) {
			this.label_ = new JLabel(String.valueOf(goNr.sum_));
			this.lframe.step("adding4 " + goNr.sum_);
		} 
		else {
			this.label_ = new JLabel("0");
		}
		this.label_.setBounds(50 + (goNr.arrayLine_.length + 1) * 130,
				uncompleteOffset + 50 + index * 15, 130, 15);
		this.label_.setVisible(true);
		this.label_.setLayout(null);
		this.displayP_.add(this.label_);
		
	}
	
	/**
	 * Colours the lowest found p values in each Ec number between the samples yellow
	 * 
	 * @param ecNr Ec Number being considered
	 * @author Jennifer Terpstra
	 */
	private void add_Geo_Dist_Colour(Line ecNr){
		for(int i = 0;i<ecNr.getGeo_labels().size();i++){
			double geo_Num = Double.parseDouble(ecNr.getGeo_labels().get(i).getText());
			if(geo_Num==ecNr.getLowest_dist_num()){
				ecNr.getGeo_labels().get(i).setBackground(Color.YELLOW);
				ecNr.getGeo_labels().get(i).setOpaque(true);
			}
		}
	}
	
	private void showValues(Line goNr, int index) {// Prints the go matrix
		if (goNr.isSumline_()) {
			addSumLineVals(goNr, index);
			return;
		}
		int uncompleteOffset = 0;

		for (int smpCnt = 0; smpCnt < goNr.arrayLine_.length; smpCnt++) {
			if (goNr.arrayLine_[smpCnt] != 0.0D) {
				this.label_ = new JLabel(String.valueOf((int) goNr.arrayLine_[smpCnt]));
				if(this.includeRepseq_.isSelected()){
					this.label_.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				this.lframe.step("adding5 " + goNr.arrayLine_[smpCnt]);
				if (this.includeRepseq_.isSelected()) {
					final int indexY = index;
					final int indexX = smpCnt;

					JMenuItem mItem = new JMenuItem("Export");
					menuPopup = new JPopupMenu();

					menuPopup.add(mItem);
					GoActMatixPane.this.setComponentPopupMenu(menuPopup);
					// this is the little pop up menu that comes up when you right click 
					// any of the ECs when the "include repseqs" option is selected
					mItem.addActionListener(new ActionListener(){
						
						public void actionPerformed(ActionEvent e) {
							GONum goTmp = new GONum(GoActMatixPane.this.goMatrix_.get(popupIndexY).getGoNr_());
							goTmp.amount_ = ((int)(GoActMatixPane.this.goMatrix_.get(popupIndexY)).arrayLine_[indexX]);
							ArrayList<ConvertStatGo> reps = new ArrayList<ConvertStatGo>();
							for (int statsCnt = 0; statsCnt < ((Sample) Project.samples_
									.get(popupIndexX)).conversionsGo_.size(); statsCnt++) {
								String test = (((ConvertStatGo) ((Sample) Project.samples_
										.get(popupIndexX)).conversionsGo_
										.get(statsCnt)).getDesc_());
								if ((goTmp.GoNumber.contentEquals(((ConvertStatGo) ((Sample) Project.samples_
										.get(popupIndexX)).conversionsGo_.get(statsCnt)).getGoNr_()))
										&& !test.contains("\t")) {
									reps.add((ConvertStatGo) ((Sample) Project.samples_
											.get(popupIndexX)).conversionsGo_.get(statsCnt));
								}
							}

							String test = "";
							String test2 = "";
							for (int i = reps.size() - 1; i >= 0; i--) {
								if ((reps.get(i) == null)) {
								} else {
									test = ((ConvertStatGo) reps.get(i))
											.getDesc_();
									if (test.contains("\t")) {
										reps.set(i, null);
									} else {
										for (int j = i - 1; j >= 0; j--) {
											if ((reps.get(j) == null)) {
											} else {
												test2 = ((ConvertStatGo) reps
														.get(j)).getDesc_();
												if (test.contains(test2)) {
													reps.set(j, null);
												}
											}
										}
									}
								}
							}
							for (int i = reps.size() - 1; i >= 0; i--) {
								if (reps.get(i) == null) {
									reps.remove(i);
								}
							}

							String sampName = ((Sample) Project.samples_
									.get(popupIndexX)).name_;
							ExportRepsGo(reps, goTmp, sampName);
						}
					});
					/*
					 * Opens up the window when you left click the ECs with the
					 * "include sequence ids" selected
					 */
					this.label_.addMouseListener(new MouseListener()
							{
								public void mouseClicked(MouseEvent e) {
									if (SwingUtilities.isRightMouseButton(e)
											|| e.isControlDown()) {
										System.out
												.println("Right Button Pressed");
										GoActMatixPane.this.menuPopup.show(
												e.getComponent(), e.getX(),
												e.getY());
										popupIndexY = indexY;
										popupIndexX = indexX;
									} else {
										GONum goTmp = new GONum(
												((Line) GoActMatixPane.this.goMatrix_
														.get(indexY)).getGoNr_());
										goTmp.amount_ = ((int) ((Line) GoActMatixPane.this.goMatrix_
												.get(indexY)).arrayLine_[indexX]);
										ArrayList<ConvertStatGo> reps = new ArrayList<ConvertStatGo>();
										for (int statsCnt = 0; statsCnt < ((Sample) Project.samples_
												.get(indexX)).conversionsGo_
												.size(); statsCnt++) {
											if (goTmp.GoNumber
													.contentEquals(((ConvertStatGo) ((Sample) Project.samples_
															.get(indexX)).conversionsGo_
															.get(statsCnt))
															.getGoNr_())) {
												reps.add((ConvertStatGo) ((Sample) Project.samples_
														.get(indexX)).conversionsGo_
														.get(statsCnt));
											}
										}

										String test = "";
										String test2 = "";
										for (int i = reps.size() - 1; i >= 0; i--) {
											if ((reps.get(i) == null)) {
											} else {
												test = ((ConvertStatGo) reps
														.get(i)).getDesc_();
												if (test.contains("\t")) {
													reps.set(i, null);
												} else {
													for (int j = i - 1; j >= 0; j--) {
														if ((reps.get(j) == null)) {
														} else {
															test2 = ((ConvertStatGo) reps
																	.get(j))
																	.getDesc_();
															if (test.contains(test2)) {
																reps.set(j,
																		null);
															}
														}
													}
												}
											}
										}
										for (int i = reps.size() - 1; i >= 0; i--) {
											if (reps.get(i) == null) {
												reps.remove(i);
											}
										}

										String sampName = ((Sample) Project.samples_
												.get(indexX)).name_;
										new RepseqFrameGo(reps, goTmp, sampName);
									}
								}

								public void mouseEntered(MouseEvent e) {
								}

								public void mouseExited(MouseEvent e) {
								}

								public void mousePressed(MouseEvent e) {
								}

								public void mouseReleased(MouseEvent e) {
								}
							});
				}
			} else {
				this.label_ = new JLabel("0");
			}
			this.label_.setBounds(50 + (smpCnt + 1) * 130, uncompleteOffset
					+ 50 + index * 15, 130, 15);
			this.label_.setVisible(true);
			this.label_.setLayout(null);
			this.displayP_.add(this.label_);
		
		}
		if (goNr.sum_ != 0) {
			this.label_ = new JLabel(String.valueOf(goNr.sum_));
			this.lframe.step("adding6 " + goNr.sum_);
		} else {
			this.label_ = new JLabel("0");
		}
		this.label_.setBounds(50 + (goNr.arrayLine_.length + 1) * 130,
				uncompleteOffset + 50 + index * 15, 130, 15);
		this.label_.setVisible(true);
		this.label_.setLayout(null);
		this.displayP_.add(this.label_);
	}
	//exports the sequence ids of a particular GO to RepSeqIDs
	public void ExportRepsGo(ArrayList<ConvertStatGo> reps, GONum goNr,String sampName) {
		String text = "";
		System.out.println("Reps:" + reps.size());
		for (int repCnt = 0; repCnt < reps.size(); repCnt++) {
			int amount = (reps.get(repCnt)).getGoAmount_();
			if ((reps.get(repCnt)).getPfamToGoAmount_() > amount) {
				amount = ( reps.get(repCnt)).getPfamToGoAmount_();
			}
			text = text + (reps.get(repCnt)).getDesc_();
			text = text + "\n";
		}
		try {
			String sampleName;
			if (sampName.contains(".out")) {
				sampleName = sampName.replace(".out", "");
			} else {
				sampleName = sampName;
			}
			File f = new File(StartFromp1.FolderPath+ "Sequences");
			if (!f.exists()) {
		            f.mkdirs();
		    }
			File file = new File(StartFromp1.FolderPath + File.separator+"Sequences"+File.separator+sampleName + sampleName + "-GO-" +goNr.GoNumber + ".txt");
			PrintWriter printWriter = new PrintWriter(file);
			printWriter.println("" + text);
			printWriter.close();
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//adds the sum of a line in order to be able to print the line sum
	private void addSumLineVals(Line goNr, int index) {
		int uncompleteOffset = 0;

		for (int smpCnt = 0; smpCnt < goNr.arrayLine_.length; smpCnt++) {
			if (goNr.arrayLine_[smpCnt] != 0.0D) {
				this.label_ = new JLabel(String.valueOf((int) goNr.arrayLine_[smpCnt]));
				this.lframe.step("adding7 " + goNr.arrayLine_[smpCnt]);
			} else {
				this.label_ = new JLabel("0");
			}
			this.label_.setBounds(50 + (smpCnt + 1) * 130, uncompleteOffset
					+ 50 + index * 15, 130, 15);
			this.label_.setVisible(true);
			this.label_.setLayout(null);
			this.displayP_.add(this.label_);
		}
		if (goNr.sum_ != 0) {
			this.label_ = new JLabel(String.valueOf(goNr.sum_));
			this.lframe.step("adding8 " + goNr.sum_);

		} else {
			this.label_ = new JLabel("0");
		}
		this.label_.setBounds(50 + (goNr.arrayLine_.length + 1) * 130,
				uncompleteOffset + 50 + index * 15, 130, 15);
		this.label_.setVisible(true);
		this.label_.setLayout(null);
		this.displayP_.add(this.label_);
		
	}
	//adds all of the GO name buttons on the left hand side of the GO matrix
	private void addGoButton(Line goNr, int index) {
		this.lframe.bigStep("Adding ecButtons");
		if (!goNr.isSumline_()) {

			this.names_ = new JButton(goNr.getGoNr_().GoNumber);
			this.names_.setName(goNr.getGoNr_().GoNumber);
			go_list.add(goNr.getGoNr_().GoNumber);
			int uncompleteOffset = 0;
	
			this.names_.setBounds(50, uncompleteOffset + 50 + index * 15, 130,15);
			this.names_.setVisible(true);
			this.names_.setLayout(null);
			this.names_.setForeground(Project.getFontColor_());
			this.lframe.step(this.names_.getText());
			final int i = index;
			goNr.getGoNr_().amount_ = goNr.sum_;
			
			GoActMatixPane.this.setComponentPopupMenu(menuPopup);
			this.names_.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new PwInfoFrameGo(GoActMatixPane.this.goMatrix_.get(i).getGoNr_(), GoActMatixPane.this.actProj_,Project.overall_);
					
				}
			});
			if (this.includeRepseq_.isSelected()) {
				ecMenuPopup = new JPopupMenu();
				JMenuItem exportMat_one = new JMenuItem("Export all Sequences to one file");
				JMenuItem lca_one = new JMenuItem("Find Lowest Common Ancestor of all Sequences to one file");
			    JMenuItem exportMat_individual = new JMenuItem("Export all Sequences to individual files");
			    JMenuItem lca_individual = new JMenuItem("Find Lowest Common Ancestor of all Sequences to individual files");
			  
			    ecMenuPopup.add(exportMat_one);
			    ecMenuPopup.add(exportMat_individual);
			    ecMenuPopup.add(lca_one);
			    ecMenuPopup.add(lca_individual);
			  
				exportMat_one.addActionListener(new ActionListener(){
					//If the user clicks on the "Export all Sequences to one file" in the popup menu, sets the exportAll boolean to true
					//sends the buttons GO number into cmdExportSequencesGo to be handled like a command line option
					@Override
					public void actionPerformed(ActionEvent e) {
						exportAll = true;
						lframe = new Loadingframe(); // opens the loading frame
						lframe.bigStep("Exporting Sequences..");
						lframe.step(buttonName); 
						cmdExportSequencesGo(buttonName,sampleName, exportAll, findLca);
						exportAll = false;
						Loadingframe.close();
						
					}
					
				});
				exportMat_individual.addActionListener(new ActionListener(){
					//If the user clicks on the "Export all Sequences to individual files" in the popup menu, sets the exportAll boolean to false
					//sends the buttons GO number into cmdExportSequencesGo to be handled like a command line option
					@Override
					public void actionPerformed(ActionEvent e) {
						exportAll = false;
						lframe = new Loadingframe(); // opens the loading frame
						lframe.bigStep("Exporting Sequences..");
						lframe.step(buttonName); 
						cmdExportSequencesGo(buttonName,sampleName, exportAll, findLca);
						Loadingframe.close();
						
					}
					
				});
				lca_one.addActionListener(new ActionListener(){
					//If the user clicks on the "Find Lowest Common Ancestor of all Sequences to one file" in the popup menu, sets the exportAll boolean to true
					//sends the buttons GO number into cmdExportSequencesGo to be handled like a command line option
					@Override
					public void actionPerformed(ActionEvent e) {
						exportAll = true;
						findLca = true;
						lframe = new Loadingframe(); // opens the loading frame
						lframe.bigStep("Exporting Sequences/Finding Lca..");
						lframe.step(buttonName); 
						LinkedHashMap<String,String> seq_for_lca;
						seq_for_lca = cmdExportSequencesGo(buttonName,sampleName, exportAll, findLca);
						String file_name = buttonName+"-";
						MetaProteomicAnalysis meta = new MetaProteomicAnalysis();
						meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca,file_name), true, batchCommand);
						exportAll = false;
						findLca = false;
						Loadingframe.close();
					}
					
				});
				lca_individual.addActionListener(new ActionListener(){
					//If the user clicks on the "Find Lowest Common Ancestor of all Sequences to individual files" in the popup menu, sets the exportAll boolean to false
					//sends the buttons GO number into cmdExportSequencesGo to be handled like a command line option
					@Override
					public void actionPerformed(ActionEvent e) {
						exportAll = false;
						findLca = true;
						lframe = new Loadingframe(); // opens the loading frame
						lframe.bigStep("Exporting Sequences/Finding Lca.."); 
						lframe.step(buttonName);
						cmdExportSequencesGo(buttonName,sampleName, exportAll,findLca);
						findLca = false;
						Loadingframe.close();
					}
					
				});
				//When someone left clicks on the go number it opens a pop up menu containing the option to
				//export all sequences for that go number
				this.names_.addMouseListener(new MouseListener() {
					@SuppressWarnings("static-access")
					public void mouseClicked(MouseEvent e) {
						if (SwingUtilities.isRightMouseButton(e)|| e.isControlDown()) {
							GoActMatixPane.this.ecMenuPopup.show(e.getComponent(),e.getX(), e.getY());
							buttonName = e.getComponent().getName();
					
						}
					}
					@Override
					public void mouseEntered(MouseEvent e) {
					}
					@Override
					public void mouseExited(MouseEvent e) {
					}
					@Override
					public void mousePressed(MouseEvent e) {
					}
					@Override
					public void mouseReleased(MouseEvent e) {
					}
				});
			}
		} 
		else {
			int uncompleteOffset = 0;
			if (goNr.isMappedSums_()) {
				this.names_ = new JButton("MappedGOs");
			} 
			else if (goNr.isUnMappedSums_()) {
				this.names_ = new JButton("UnMappedGOs");
			} 
			else {
				this.names_ = new JButton("All GOs");
//				uncompleteOffset = 50;
			}
			this.names_.setBounds(50, uncompleteOffset + 50 + index * 15, 130,
					15);
			this.names_.setVisible(true);
			this.names_.setLayout(null);
			this.names_.setForeground(Project.getFontColor_());
			this.lframe.step(this.names_.getText());
		}
		
		this.displayP_.add(this.names_);
		
	}

	private void sortGOsbyNameQuickSort() {
		if (this.sortedGo) {
			return;
		}
		this.lframe.bigStep("Sorting GOs");
		if (this.goMatrix_.size() > 0) {
			quicksortNames(0, this.goMatrix_.size() - 1);
		}
		this.goMatrix_ = removeDuplicatesGo();
		System.out.println("Done Sorting");
		if (this.moveUnmappedToEnd != null) {
			if (this.moveUnmappedToEnd.isSelected()) {
				unmappedMover();
			}
		} else {
			unmappedMover();
		}
		Loadingframe.close();
	}

	private void quicksortNames(int low, int high) {
		int i = low, j = high;
		String pivot = this.goMatrix_.get(low + (high - low) / 2).getGoNr_().GoNumber;
		while (i <= j) {
			while (this.goMatrix_.get(i).getGoNr_().GoNumber.compareTo(pivot) < 0) {
				i++;
			}
			while (this.goMatrix_.get(j).getGoNr_().GoNumber.compareTo(pivot) > 0) {
				j--;
			}
			if (i <= j) {
				switchGos(i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quicksortNames(low, j);
		if (i < high)
			quicksortNames(i, high);
	}

	public ArrayList<Line> removeDuplicatesGo() {
		ArrayList<Line> tempLine = new ArrayList<Line>();
		ArrayList<String> tempName = new ArrayList<String>();
		for (int i = 0; i < this.goMatrix_.size(); i++) {
			if (!tempName.contains(this.goMatrix_.get(i).getGoNr_().GoNumber)) {
				tempLine.add(this.goMatrix_.get(i));
				tempName.add(this.goMatrix_.get(i).getGoNr_().GoNumber);
			}
		}
		return tempLine;
	}

	private void unmappedMover() {
		int unmappedCnt = 0;
		for (int goCnt1 = 0; goCnt1 < this.goMatrix_.size(); goCnt1++) {
			if ((((Line) this.goMatrix_.get(goCnt1)).getGoNr_().unmapped)
					&& (goCnt1 < this.goMatrix_.size() - unmappedCnt)) {
				moveToEnd(goCnt1);
				unmappedCnt++;
				goCnt1--;
			}
		}
	}

	private void moveToEnd(int index) {
		int tmp = index;
		while (tmp < this.goMatrix_.size() - 1) {
			switchGos(tmp, tmp + 1);
			tmp++;
		}
	}
	
	//exports the whole go matrix to the input path
	public void exportMatGo(String path, boolean inCsf) {
		String separator = "\t";
		String go= "";
		if (inCsf) {
			separator = "\t";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write("GO"+separator);
			for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
				if (((Sample) Project.samples_.get(smpCnt)).inUse) {
					out.write(((Sample) Project.samples_.get(smpCnt)).name_
							+ separator);
				}
			}
			out.newLine();
			for (int y = 0; y < this.goMatrix_.size(); y++) {
				Line line = (Line) this.goMatrix_.get(y);
			
				go = line.getGoNr_().GoNumber;
				
				out.write(go + separator);

				for (int x = 0; x < line.getArrayLine_().length; x++) {
					
					out.write(line.getArrayLine_()[x] + separator);
					
				}
				out.newLine();
			}
			out.close();
		} 
		catch (IOException e) {
			File f = new File(path);
			f.mkdirs();
			if (!path.endsWith(".txt")) {
				Calendar cal = Calendar.getInstance();

				int day = cal.get(5);
				int month = cal.get(2) + 1;
				int year = cal.get(1);

				String date = day + "." + month + "." + year;
				path = path + File.separator + "GoActMat" + "."
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
			exportMatGo(path, inCsf);
		}
	}
	/**
	 * Method is used to export all the go numbers from the go matrix into a
	 * .txt file. The number of gos to be exported can be specified.
	 * @param path Filepath to where the .txt will be saved
	 * @param numEc Number of GOs to export from the go matrix
	 * 
	 * @author Jennifer Terpstra , Song
	 */
	
	public void exportGoNums(String path, int numGo){
		try {
			
			System.out.println("export GO Nums");
			
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
				
			out.write("Go Activity GO Numbers");
				
			int exportNum = 0;
			/*Used for command line to check if the input entered for the number of
			 * go to be exported is a valid input.
			 */
			if(numGo == 0){
				exportNum = this.goMatrix_.size();
				System.out.println("exportNum="+exportNum);
			}
			else if(numGo > 0 && numGo <= this.goMatrix_.size()){
				exportNum = numGo;
			}
			else{
				//exits the program if the input is found to be a invalid input
				System.out.println("Number of exported GO's cannot be less than 0 or greater than total number of GO's");
				System.exit(0);
			}
			
			if(CmdController1.elistSortSum == true){
				quicksortSumGo();
			}
			
			for (int y = 0; y < exportNum; y++) {
				Line line = goMatrix_.get(y);
				out.newLine();
				out.write(line.getGoNr_().GoNumber);
				
			}
			out.close();
		} 
		catch (IOException e) {
			File f = new File(path);
			f.mkdirs();
			if (!path.endsWith(".txt")) {
				Calendar cal = Calendar.getInstance();

				int day = cal.get(5);
				int month = cal.get(2) + 1;
				int year = cal.get(1);

				String date = day + "." + month + "." + year;
				path = path + File.separator + "GoActMat" + "."
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
						path = path + "-" + i ;
						break;
					}
				}
			}
			path = path + ".txt";
			exportGoNums(path, numGo);
		}
		
	}
	
	private void quicksortSumGo() {
		this.lframe.bigStep("Sorting GOs");
		if (this.goMatrix_.size() > 0) {
			quicksortGo(0, this.goMatrix_.size() - 1);
		}
		reverseMatrixGo();
		this.goMatrix_ = removeDuplicatesGo();
		Loadingframe.close();
	}
	
	public void quicksortGeoDistGo() {
		this.lframe.bigStep("Sorting GOs");
		if(this.goMatrix_.size()>0){
			quicksortDistGo(0, this.goMatrix_.size() - 1);
		}
		this.goMatrix_ = removeDuplicatesGo();
		Loadingframe.close();
	}
	
	private void quicksortOdds(){
		this.lframe.bigStep("Sorting GOs");
		if(this.goMatrix_.size()>0){
			quicksortOdds(0, this.goMatrix_.size() - 1);
		}
		this.goMatrix_ = removeDuplicatesGo();
		if (this.moveUnmappedToEnd != null) {
			if (this.moveUnmappedToEnd.isSelected()) {
				unmappedMover();
			}
		} else {
			unmappedMover();
		}

		Loadingframe.close();
	}

	private void quicksortGo(int low, int high) {
		int i = low, j = high;
		int pivot = this.goMatrix_.get(high - 1).sum_;
		while (i <= j) {
			while (this.goMatrix_.get(i).sum_ < pivot) {
				i++;
			}
			while (this.goMatrix_.get(j).sum_ > pivot) {
				j--;
			}
			if (i <= j) {
				switchGos(i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quicksortGo(low, j);
		if (i < high)
			quicksortGo(i, high);
	}
	
	private void quicksortDistGo(int low, int high) {
		int i = low, j = high;
		double pivot = this.goMatrix_.get(high - 1).lowest_dist_num;
		while (i <= j) {
			while (this.goMatrix_.get(i).lowest_dist_num < pivot) {
				i++;
			}
			while (this.goMatrix_.get(j).lowest_dist_num > pivot) {
				j--;
			}
			if (i <= j) {
				switchGos(i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quicksortDistGo(low, j);
		if (i < high)
			quicksortDistGo(i, high);
	}
	
	
	private void quicksortOdds(int low, int high) {
		int i = low, j = high;
		double pivot = this.goMatrix_.get(high - 1).lowest_odds;
		while (i <= j) {
			while (this.goMatrix_.get(i).lowest_odds < pivot) {
				i++;
			}
			while (this.goMatrix_.get(j).lowest_odds > pivot) {
				j--;
			}
			if (i <= j) {
				switchGos(i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quicksortOdds(low, j);
		if (i < high)
			quicksortOdds(i, high);
	}
	//for go
	private void reverseMatrixGo() {
		int j = 0;
		for (int i = goMatrix_.size() - 1; i > j; i--) {
			switchGos(i, j);
			j++;
		}
	}
	
	private void switchGos(int index1, int index2) {
		Line line1 = (Line) this.goMatrix_.get(index1);
		Line line2 = (Line) this.goMatrix_.get(index2);

		this.goMatrix_.set(index1, line2);
		this.goMatrix_.set(index2, line1);
	}
	
	
	
	
	

	private float odds(float a, float b, float c, float d) {
		if (a == 0.0F) {
			return -1.0F;
		}
		if (b == 0.0F) {
			c = 0.001F;
		}
		if (c == 0.0F) {
			b = 0.001F;
		}
		if (d == 0.0F) {
			return -2.0F;
		}
		float ret = a / b / (c / d);

		return ret;
	}

	//exports all request sequence IDs for all samples given an GO name via cmdPrompt
	public void cmdExportRepseqsGo(String goName,String path) throws IOException {
		
		BufferedWriter out;
		for (int i = 0; i < Project.samples_.size();i++){
			
			Sample tmp = Project.samples_.get(i);
			out = new BufferedWriter (new FileWriter(path+tmp.name_+"-SeqID-GO-"+goName+".txt"));
			for (int goCnt = 0; goCnt < tmp.conversionsGo_.size(); goCnt++){
				if(tmp.conversionsGo_.get(goCnt).getGoNr_().contentEquals(goName)){
					System.out.println(tmp.conversionsGo_.get(goCnt).getDesc_());
					out.write(tmp.conversionsGo_.get(goCnt).getDesc_()+"\n");

				}	
			}
			out.close();
		}
	}
	
	/**
	 * Takes in command line calls to export sequences along with whether or not to print all sequences samples into each GO number file
	 */
	public LinkedHashMap<String,String> cmdExportSequencesGo(String goName,String sampleName, boolean oneFile, boolean findLca) {
		System.out.println(goName+ " cmdExport");
		GONum goTmp;
		ArrayList<ConvertStatGo> reps;
		String sampName;
		LinkedHashMap<String,String> seq_for_lca = null;

		for (int i = 0; i < goMatrix_.size(); i++) {
			//changed contains to equals!!
			if (goName.equals(this.goMatrix_.get(i).getGoNr_().GoNumber)) {
				goTmp = new GONum(goMatrix_.get(i).getGoNr_());
				for (int smpCnt = 0; smpCnt < goMatrix_.get(i).arrayLine_.length; smpCnt++) {
					goTmp.amount_ = ((int) (goMatrix_.get(i)).arrayLine_[smpCnt]);
					reps = new ArrayList<ConvertStatGo>();
					for (int statsCnt = 0; statsCnt < (Project.samples_.get(smpCnt)).conversionsGo_.size(); statsCnt++) {
						String test = (Project.samples_.get(smpCnt).conversionsGo_.get(statsCnt)).getDesc_();
						if (goTmp.GoNumber.contentEquals(Project.samples_.get(smpCnt).conversionsGo_.get(statsCnt).getGoNr_()) && !test.contains("\t")) {
							reps.add(Project.samples_.get(smpCnt).conversionsGo_.get(statsCnt));
						}
					}
					String test = "";
					String test2 = "";
					for (int j = reps.size() - 1; j >= 0; j--) {
						if (reps.get(j) == null) {
						} 
						else {
							test = (reps.get(j)).getDesc_();
							if (test.contains("\t")) {
								reps.set(j, null);
							} 
							else {
								for (int k = j - 1; k >= 0; k--) {
									if ((reps.get(k) == null)) {
									} 
									else {
										test2 = (reps.get(k)).getDesc_();
										if (test.contains(test2)) {
											reps.set(k, null);
										}
									}
								}
							}
						}
					}
					for (int j = reps.size() - 1; j >= 0; j--) {
						if (reps.get(j) == null) {
							reps.remove(j);
						}
					}
					sampName = Project.samples_.get(smpCnt).name_;
					
					if(reps.size()>0&&((chosen_sample.equals("All Samples"))||(chosen_sample.equals("")))){
						//print all sequences per sample GO number 
						System.out.print("Working1....");
						seq_for_lca = ExportSequencesGo(reps, goTmp, sampName, oneFile, findLca, seq_for_lca);
					}
					//used if trying to find the lca from the LCA page instead of the GO Matrix page
					else if(reps.size()>0 && chosen_sample.equals(sampName)){
						System.out.print("Working2....");
						seq_for_lca = ExportSequencesGo(reps, goTmp, sampName, oneFile, findLca, seq_for_lca);
					}
						
				}
			}
		}
		return seq_for_lca;
	}
	
	/**
	 * exports sequences from GO numbers from a sample to their own named file.
	 */
	public LinkedHashMap<String,String> ExportSequencesGo(ArrayList<ConvertStatGo> reps_, GONum goNr_,
		String sampName_, boolean oneFile, boolean findLca, LinkedHashMap<String,String> seqTmp) {
		String seqFilePath = "";
		String desc, protien, file_name;
		LinkedHashMap<String, String> seq_for_lca;
		//seq_for_lca will be reset,if is null or do not put all in file
		if(seqTmp==null||oneFile==false||exportAll==false){
			seq_for_lca = new LinkedHashMap<String,String>();
		}
		else{
			seq_for_lca = seqTmp;
		}
		for (int i = 0; i < Project.samples_.size(); i++) {
			if (sampName_.equals(Project.samples_.get(i).name_)) {
				if (Project.samples_.get(i).getSequenceFile() != null
						&& !Project.samples_.get(i).getSequenceFile().equals("")) {
					seqFilePath = Project.samples_.get(i).getSequenceFile();
				}
			}
		}
		if (seqFilePath != null && !seqFilePath.equals("")) {
			File seqFile = new File(seqFilePath);
			if (seqFile.exists() && !seqFile.isDirectory()) {
				LinkedHashMap<String, ProteinSequence> sequenceHash;
				try {
					sequenceHash = FastaReaderHelper.readFastaProteinSequence(seqFile);
					if (sequenceHash != null) {
						String text = "";
						System.out.println("repCnt: " + reps_.size()+" Sample name: "+sampName_);
						for (int repCnt = 0; repCnt < reps_.size(); repCnt++) {
							if ((sequenceHash.get(((ConvertStatGo) reps_.get(repCnt)).getDesc_())) != null) {
								desc = ((ConvertStatGo) reps_.get(repCnt)).getDesc_();
								protien = (sequenceHash.get(((ConvertStatGo) reps_.get(repCnt)).getDesc_())).toString();
								if(oneFile){
									text += ">"+ desc + " " + sampName_ + "\n" + protien+"\n";
									desc = desc + " " + sampName_;
								}
								else{
									text += ">"+ desc + "\n" + protien+"\n";
									desc = desc + " " + sampName_;
								}
								//used to determine if a sample was picked in the "FindLca" page
								if(chosen_sample.equals("All Samples")||chosen_sample.equals("")){
									seq_for_lca.put(desc, protien);
								}
								else if(chosen_sample.equals(sampName_)){
									seq_for_lca.put(desc, protien);
								}
//								// ensures that there is a ">" character in front of every new sample
//								if (repCnt < reps_.size() - 1) {
//									text = text + "\n>";
//								}
//								// Don't want the ">" character on the last newline with no sample
//								else if (repCnt == reps_.size()) {
//									text = text + "\n";
//								}
							}
						}
						reps_ = null;
						if(!CmdController1.optionsCmd_.equals("")&&!CmdController1.optionsCmd_.equals("lcamatgo")){
							//seqgo
							if(findLca == false && oneFile == false){
								try {
									String sampleName;
									if (sampName_.contains(".out")) {
										sampleName = sampName_.replace(".out", "");
									} else {
										sampleName = sampName_;
									}
									File f = new File(CmdController1.tmpPath+"Sequences"+File.separator);
									if (!f.exists()) {
								            f.mkdirs();
								    }
									File file = new File(CmdController1.tmpPath+"Sequences"+File.separator+
											sampleName +"-GO-"+ goNr_.GoNumber + "-Sequences" + ".txt");
									PrintWriter printWriter = new PrintWriter(file);
									if (text != null && text != "") {
										printWriter.println("" + text);
									} else {
										printWriter.println("No matching sequences in the file provided. ("+ sampName_ + ")");
									}
									printWriter.close();
								} 
								catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							//seqallgo
							else if(findLca == false && oneFile == true){
								try {
									if (sampName_.contains(".out")) {
										sampName_.replace(".out", "");
									} 
									else {
									}
									
									File f = new File(CmdController1.tmpPath+"Sequences"+File.separator);
									if (!f.exists()) {
								            f.mkdirs();
								    }
					
									File file = new File(CmdController1.tmpPath+"Sequences"+File.separator+
											Project.workpath_+"-GO-"+ goNr_.GoNumber + "-Sequences-" +CmdController1.sdf.format(CmdController1.d)+ ".txt");
									//This allows writing to the file of the same name to append to the file if created, creates file if not
									PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
									if (text != null && text != "") {
										printWriter.println("" + text);
									} else {
										printWriter.println("No matching sequences in the file provided. ("+ sampName_ + ")");
									}
									printWriter.close();
									
								} 
								catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							text=null;
						}
							
					} 
					else {
						System.out.println("The sequence file is not in the fasta format. ("+ sampName_ + ")");
					}
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}

			} 
			else {
				System.out.println("The sequence file associated with this sample ("+ sampName_ + ") does not exist");
			}
		} 
		else {
			System.out.println("There is no sequence file associated with this sample ("+ sampName_ + ")");
		}
		//used to find the lowest common ancestor of all the samples for a particular go in one table
		if(findLca == true && oneFile == false){
			if(chosen_sample.equals("All Samples")||chosen_sample.equals("")){
				file_name = sampName_ + "-" + goNr_.GoNumber;
			}
			else{
				file_name = chosen_sample + "-" + goNr_.GoNumber;
			}
			MetaProteomicAnalysis meta = new MetaProteomicAnalysis();
			meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca,file_name), true, batchCommand);
		}
		fileName = sampName_;
		return seq_for_lca;
	}

	public boolean isDrawChart() {
		return drawChart;
	}
	
	public void setDrawChart(boolean drawChart) {
		this.drawChart = drawChart;
	}
	
	/**
	 * Pops up whenever an exception occurs in a try/catch
	 * @param strIN Error message to be displayed
	 * 
	 * @author Jennifer Terpstra
	 */
	private void warningFrame(String strIN) { 
	
		JOptionPane.showMessageDialog(null,strIN, 
			    "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Checks the user input for how many gos to export to see if it is a valid number
	 * or if they wish to export all gos. 
	 * 
	 * @param strIN User input into the input message
	 * @return If the string is a valid input for exporting gos
	 * 
	 * @author Jennifer Terpstra
	 */
	private boolean checkValidExportNum(String strIN){
		if(strIN==null){
			return false;
		}
		if(strIN.equalsIgnoreCase("all")){
			return true;
		}
		try{
			int num = Integer.parseInt(strIN);
			if(num < 0){
				warningFrame("Number cannot be less than 0");
				return false;
			}
			else if(num > this.goMatrix_.size()){
				warningFrame("Number cannot be greater than the number of Ecs in the matrix");
				return false;
			}
			else{
				return true;
			}
		}
		catch (NumberFormatException e){
			warningFrame("Input must be a number");
			return false;
		}
	}
	
	/**
	 * Displays after a user exports tables or piechart with the file location, the location
	 * can then be displayed to the user.
	 * 
	 * @param strIN Filepath towards the exported file
	 * @param location Foler which the exported file is located
	 * 
	 * @author Jennifer Terpstra
	 * @return 
	 */
	public void infoFrame(String strIN, final String location) {
		JFrame infoFrame = new JFrame();
		infoFrame.setBounds(200, 200, 800, 200);
		infoFrame.setLayout(null);
		infoFrame.setVisible(true);
	
		JPanel backP = new JPanel();
		backP.setBounds(0, 0, 800, 80);
		backP.setLayout(null);
		infoFrame.add(backP);
	
		JLabel label = new JLabel("File was saved at " + strIN);
		label.setBounds(20, 20, 800, 50);
		backP.add(label);
	}
}