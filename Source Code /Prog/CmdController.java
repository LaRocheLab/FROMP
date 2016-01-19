package Prog;

import Objects.Line;
import Objects.Project;
import Objects.Sample;
import Panes.ActMatrixPane;
import Panes.PathwayActivitymatrixPane;
import Panes.PathwayMatrix;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.PrintStream;
import java.util.ArrayList;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.org.mozilla.javascript.json.JsonParser;
import java.io.IOException;
/**
 * Used for command line FROMP functions. Processes all command line user input
 * into FROMP.
 * 
 * @author Jennifer Terpstra, Kevan Lynch
 */

public class CmdController {
	public static String[] args_; // An array of String arguments taken in from the command line
	String inputPath_; // The inputpath given by the user
	String outPutPath_; // The output path given in by the user
	String optionsCmd_; // The option denoted by the user. ie h for help, etc
	ArrayList<String> ec_; // If an EC number was denoted by the user to output sequence IDs, this is the variable it is saved to
	int num_ec_exported = 0; //number of ecs desired to be exported in the ec list
	static Controller controller; // The controller. Allows user to save, load etc.
	static PathwayMatrix pwMAtrix; // the Pathway Matrix
	//Basepath of the FROMP software. Necessary for all relative paths to function
	final String basePath_ = new File(".").getAbsolutePath() + File.separator; 
	BufferedReader ecList; // Buffered reader used to read in the ec numbers listed in a file
	BufferedReader sequenceList;// buffered reader user to read in the sequence filenames listed in a file
	StringReader reader; // String reader to assist in the reading of the
	boolean batchCommand = false;
	private String inputPath;
	private String optionsCmd;

	public CmdController(String[] args) {
		System.out.println("Starting cmdFromp");
		/*
		 * args length start from 2, 1arg cmd has executed at StartFromp.java.such like h,d.
		 */
		args_ = args;
		this.ec_ = new ArrayList<String>();
		//args = 2 ,  'inputPath'  &  'ec#/seq/seqall/lca' -- !!seq/seqall/lca not working!!
		if (args_.length == 2) {
			if (checkEC(args_[1])) {
				this.ec_.add(args_[1]);
			} else if (args_[1].contentEquals("seq")) {
				this.optionsCmd_ = args_[1];
			}
			else if (args_[1].contentEquals("seqall")) {
				this.optionsCmd_ = args_[1];
			}
			else if (args_[1].contentEquals("lca")) {
				this.optionsCmd_= args_[1];
			}
			this.inputPath_ = getInputPath();
			System.out.println("input: " + this.inputPath_);
		}
		//args = 3 , 'inputPath' & 'ec#/seq/seqall/lca' & 'ec#'
		else if (args_.length == 3) {	
			if (checkEC(args_[1])) {
				this.inputPath_ = getInputPath();
				System.out.println("input: " + this.inputPath_);
				this.ec_.add(args_[1]);
				this.ec_.add(args_[2]);
			} 	
			else if (args_[1].contentEquals("seq")) {			
				this.inputPath_ = getInputPath();
				this.optionsCmd_ = args_[1];
				this.ec_.add(args_[2]);	
			} 
			else if (args_[1].contentEquals("seqall")) {	
				this.inputPath_ = getInputPath();
				this.optionsCmd_ = args_[1];
				this.ec_.add(args_[2]);
			}
			else if (args_[1].contentEquals("lca")) {
				this.inputPath_ = getInputPath();
				this.optionsCmd_ = args_[1];
				this.ec_.add(args_[2]);
			}
			//if arg1   !=   ec#/seq/seqall/lca , just print inputPath
			else {
				this.inputPath_ = getInputPath();
				System.out.println("input: " + this.inputPath_);
				this.outPutPath_ = getOutputPath();
				System.out.println("output: " + this.outPutPath_);
				this.optionsCmd_ = getOptionCmd();
				System.out.println("option: " + this.optionsCmd_);

			}
			
			
		} 
		//args =4  or more.  
		else if (args.length >= 4) {
			
			//'inputPath' & 'ec#' & 'ec#'  & 'ec#' & 'ec#' & 'ec#'  & 'ec#'......
			if (checkEC(args_[1])) {
				this.inputPath_ = getInputPath();
				System.out.println("input: " + this.inputPath_);
				System.out.println("ECs");
				for (int i = 1; i < args.length; i++) {
					this.ec_.add(args_[i]);
					System.out.println(args_[i]);
				}
			} 
			// 'inputPath' & 'seq' & 'ec#'  & 'ec#' & 'ec#' & 'ec#'  & 'ec#'......
			else if (args_[1].contentEquals("seq")) {
				this.inputPath_ = getInputPath();
				this.optionsCmd_ = args_[1];
				System.out.println("input: " + this.inputPath_);
				System.out.println("Seqs");
				for (int i = 2; i < args.length; i++) {
					this.ec_.add(args_[i]);
					System.out.println("mount" +args_[i]);
				}
			} 
			// 'inputPath' & 'seqall' & 'ec#'  & 'ec#' & 'ec#' & 'ec#'  & 'ec#'......
			else if (args_[1].contentEquals("seqall")) {
				this.inputPath_ = getInputPath();
				this.optionsCmd_ = args_[1];
				System.out.println("input: " + this.inputPath_);
				System.out.println("Seqall");
				for (int i = 2; i < args.length; i++) {
					this.ec_.add(args_[i]);
					System.out.println(args_[i]);
				}
			} 
			//'inputPath' & 'lca' & 'ec#'  & 'ec#' & 'ec#' & 'ec#'  & 'ec#'......
			else if (args_[1].contentEquals("lca")) {
				this.inputPath_ = getInputPath();
				this.optionsCmd_ = args_[1];
				System.out.println("input: " + this.inputPath_);
				System.out.println("lca");
				for (int i = 2; i < args.length; i++) {
					this.ec_.add(args_[i]);
					System.out.println(args_[i]);
				}
			}
			//'inputPath' & 'eclistPath/outputPath' & 'eclist/pvalue'
			else if (args_[2].contentEquals("eclist")||args_[2].contentEquals("pvalue")){
				this.inputPath_ = getInputPath();
				this.outPutPath_ = getOutputPath();
				this.optionsCmd_ = args_[2];
				System.out.println("input: " + this.inputPath_);
				this.num_ec_exported = Integer.parseInt(args_[3]);
				System.out.println("Number " + num_ec_exported);
			}
			//'inputPath' & 'a path' & ‘a cmd’  &  'ec#' & 'ec#'  & 'ec#'......
			else {
				this.inputPath_ = getInputPath();
				System.out.println("input: " + this.inputPath_);
				this.outPutPath_ = getOutputPath();
				System.out.println("output: " + this.outPutPath_);
				this.optionsCmd_ = getOptionCmd();
				System.out.println("option: " + this.optionsCmd_);
				System.out.println("ECs");
				for (int i = 3; i < args.length; i++) {
					this.ec_.add(args_[i]);
					System.out.println(args_[i]);
				}
			}
		}

		controller = new Controller(Color.black);

		Panes.Loadingframe.showLoading = false;
		Panes.HelpFrame.showSummary = false;
		if (this.inputPath_.endsWith(".frp")) // If the input file is of the the project file type, load the project
		{
			//add try-catch for check input file.
			try{
				File f = new File(inputPath_);
				if (f.exists() && !f.isDirectory()) {
					controller.loadProjFile(this.inputPath_);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("wrong .frp file");
			}
			
		/*
		 * If the input file is of the .lst type, iterate through the file and build samples for
		 * all the file paths in the file, if the line starts with <userP> a new user path
		 * is added. They are all added to a new project!
		 */
		}
		
		else if (this.inputPath_.endsWith(".lst")){
			try {
				BufferedReader in = new BufferedReader(new FileReader(this.inputPath_));

				String comp = "<userP>";
				String line = in.readLine();
				while ((line) != null) {
					try {
						// String line;
						if (((line) != null) && line.startsWith(comp)) {
							String userP = line.substring(comp.length());
							System.out.println("user pathway made");
							Project.addUserP(userP);
							System.out.println("pathway added");
						}
						else if ((line) != null) {
							//if the line doesn't contain a tab then assume it is just the path to the project or sample files
							//no sequence file
							if (!line.contains("\t")) { 
								File f = new File(line);
								//the file ends with ".frp"
								if (f.exists() && !f.isDirectory()&& line.endsWith(".frp")) {
									controller.loadAnotherProjFile(line);
									System.out.println("Project file added");
									//get project name from a path. such like /xx/xx/abc.txt --> after last / and before last . --> it will get abc.need+1
									Project.workpath_ = inputPath_.substring(inputPath_.lastIndexOf(File.separator)+1,inputPath_.lastIndexOf("."));
									
									//check any"/" in project name.
									if (Project.workpath_.contains(File.separator)) {
										Project.workpath_ = Project.workpath_.replace(File.separator, "");
									}
									
								}
								//if the file file not ends with ".frp". such like .txt / .lst
								else if (f.exists() && !f.isDirectory()) {
									//get filename.filetype.  such like abc.txt, need +1
									String name = line.substring(line.lastIndexOf(File.separator)+1);
									Color col = new Color((float) Math.random(),(float) Math.random(),(float) Math.random());
									Sample sample = new Sample(name, line, col);

									Project.samples_.add(sample);
									System.out.println("sample added");
								} 
								//if file not exists or it is a folder
								else {
									System.out.println("file does not exist");
								}
							} 
							//line contains "\t" -- tab, means it include sample and sequence 
							else { // If the line does contain a tab then split the line into the the sample file and the sequence file
								String sampleString = line.substring(0,line.indexOf("\t"));
								//sequence file should not include "\t".so it need +1
								String sequenceString = line.substring(line.indexOf("\t")+1);
								String tmpName = "";
								Boolean newSample = false;
								File f = new File(sampleString);
								if (f.exists() && !f.isDirectory()&& sampleString.endsWith(".frp")) {
									controller.loadAnotherProjFile(sampleString);
									System.out.println("Project file added");
									Project.workpath_ = inputPath_.substring(inputPath_.lastIndexOf(File.separator),inputPath_.lastIndexOf("."));
									
									if (Project.workpath_.contains(File.separator)) {
										Project.workpath_ = Project.workpath_.replace(File.separator, "");
									}
								} 
								else if (f.exists() && !f.isDirectory()) {
									//need+1
									String name = sampleString.substring(sampleString.lastIndexOf(File.separator)+1);
									Color col = new Color(
											(float) Math.random(),
											(float) Math.random(),
											(float) Math.random());
									Sample sample = new Sample(name,
											sampleString, col);
									newSample = true;
									tmpName = name;
									Project.samples_.add(sample);
									System.out.println("sample added");
								} else {
									System.out.println("file does not exist");
								}

								File f1 = new File(sequenceString);
								if (f.exists() && !f.isDirectory() && newSample
										&& tmpName != null && tmpName != "") {
									for (int i = 0; i < Project.samples_.size(); i++) {
										if (Project.samples_.get(i).name_
												.contentEquals(tmpName)) {

										}
									}
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					line = in.readLine();
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		else {
			//need +1
			String name = this.inputPath_.substring(this.inputPath_.lastIndexOf(File.separator)+1);
			Sample sample = new Sample(name, this.inputPath_, Color.red);

			Project.samples_.add(sample);
		}
		Controller.loadPathways(true);
		Project.workpath_ = inputPath_.substring(inputPath_.lastIndexOf(File.separator),inputPath_.lastIndexOf("."));
		if (Project.workpath_.contains(File.separator)) {
			Project.workpath_ = Project.workpath_.replace(File.separator, "");
		}

		if (!ec_.isEmpty()) {
			if (this.optionsCmd_ != null) {
				if (!this.optionsCmd_.contentEquals("seq") && !this.optionsCmd_.contentEquals("seqall") &&
						!this.optionsCmd_.contentEquals("eclist")&& !this.optionsCmd_.contentEquals("pvalue")) {
					ActMatrixPane pane = new ActMatrixPane(Controller.project_,
							DataProcessor.ecList_, Controller.processor_,
							new Dimension(12, 12));
					System.out.println("Repseqs will be saved at: " + basePath_
							+ "RepSeqIDs/");
					for (int i = 0; i < ec_.size(); i++) {
						pane.cmdExportRepseqs(this.ec_.get(i));
					}
				}
			} else {
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,
						DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				System.out.println("Repseqs will be saved at: " + basePath_
						+ "RepSeqIDs/");
				for (int i = 0; i < ec_.size(); i++) {
					pane.cmdExportRepseqs(this.ec_.get(i));
				}
			}
		}
		if (this.optionsCmd_ != null) {
			if (this.optionsCmd_.contentEquals("ec")) {
				this.reader = new StringReader();
				this.ecList = this.reader.readTxt(outPutPath_);
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,
						DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				System.out.println("Repseqs will be saved at: " + basePath_
						+ "RepSeqIDs/");
				String line = "";
				try {
					while ((line = this.ecList.readLine()) != null) {
						if (checkEC(line)) {
							pane.cmdExportRepseqs(line);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			if ((this.optionsCmd_.contentEquals("seq")) && !ec_.isEmpty()) {
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,
						DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				System.out.println("Sequences will be saved at: " + basePath_
						+ "Sequences/");
				for (int i = 0; i < ec_.size(); i++) {
					pane.cmdExportSequences(this.ec_.get(i),"", false, false);
				}
				if ((this.optionsCmd_.contentEquals("seq"))) {
					System.exit(0);
				}
			} else if ((this.optionsCmd_.contentEquals("seq"))) {
				this.reader = new StringReader();
				this.ecList = this.reader.readTxt(outPutPath_);
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,
						DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				System.out.println("Sequences will be saved at: " + basePath_
						+ "Sequences/");
				String line = "";
				try {
					while ((line = this.ecList.readLine()) != null) {
						if (checkEC(line)) {
							pane.cmdExportSequences(line,"", false, false);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			else if((this.optionsCmd_.contentEquals("seqall")) && !ec_.isEmpty()) {
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,
						DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				System.out.println("Sequences will be saved at: " + basePath_
						+ "Sequences/");
				for (int i = 0; i < ec_.size(); i++) {
					pane.cmdExportSequences(this.ec_.get(i),"", true, false);
				}
				if ((this.optionsCmd_.contentEquals("seq"))) {
					System.exit(0);
				}
			}
			else if((this.optionsCmd_.contentEquals("seqall"))){
				this.reader = new StringReader();
				this.ecList = this.reader.readTxt(outPutPath_);
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,
						DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				System.out.println("Sequences will be saved at: " + basePath_
						+ "Sequences/");
				String line = "";
				try {
					while ((line = this.ecList.readLine()) != null) {
						if (checkEC(line)) {
							pane.cmdExportSequences(line,"", true, false);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
			else if((this.optionsCmd_.contentEquals("lca"))&&ec_.isEmpty()){
				this.reader = new StringReader();
				this.sequenceList = this.reader.readTxt(outPutPath_);
				MetaProteomicAnalysis metapro = new MetaProteomicAnalysis();
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				System.out.println("Warning: Process may take awhile if there are a large amount of sequences\n");
				String line = "";
				try {
					ArrayList<String> timedOut;
					long startTime = System.currentTimeMillis();
					while ((line = this.sequenceList.readLine()) != null) {
						//If the line contains a > it is not a file containing a list of sequence files
						if(line.contains(">")){
							metapro.getTrypticPeptideAnaysis(metapro.readFasta(outPutPath_), true, batchCommand);
							break;
						}
						else if(line.contains("Ec Activity EC Numbers")){
							
							batchCommand = true;
							line = sequenceList.readLine();
							String sampleName = "";
							LinkedHashMap<String,String> seq_for_lca;
							seq_for_lca = pane.cmdExportSequences(line,sampleName, true, false);
							String fileName = line +  "-";
							tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(metapro.readFasta(seq_for_lca, fileName), false, batchCommand);
							
							batchCommand = false;
						}
						else if(line.matches("[0-9]+.[0-9]+.[0-9]+.[0-9]+")){
							
							batchCommand = true;
							String sampleName = "";
							LinkedHashMap<String,String> seq_for_lca;
							seq_for_lca = pane.cmdExportSequences(line,sampleName, true, false);
							String fileName = line +  "-";
							tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(metapro.readFasta(seq_for_lca, fileName), false, batchCommand);
							
							batchCommand = false;
						}
						else{
							metapro.getTrypticPeptideAnaysis(metapro.readFasta(line), true, batchCommand);
						}
					}
					
					System.out.println("LCA Done");
					//checking for any timed out ec numbers
					checkTimedOut(metapro);
					
					long endTime   = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					timedOut = metapro.getTimedOut();
					//printing out the names of the files that timed out
					if(!timedOut.isEmpty()){
						System.out.println("Files that timed out:");
						for(int i = 0; i < timedOut.size(); i++){
							System.out.println(timedOut.get(i).substring(0, timedOut.get(i).indexOf("-")));
						}
					}
					else{
						System.out.println("All files executed successfully");
					}
					System.out.println("Time" + " " + totalTime);
				} catch (IOException e) {
					System.out.println("File does not exist");
				}
			}
			else if((this.optionsCmd_.contentEquals("lca"))&&!ec_.isEmpty()){
				MetaProteomicAnalysis metapro = new MetaProteomicAnalysis();
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				String sampleName = "";
				String line = "";
				for (int i = 0;i<this.ec_.size();i++) {
					line = ""+ec_.get(i);
					if (checkEC(line)) {
						LinkedHashMap<String,String> seq_for_lca;
						seq_for_lca = pane.cmdExportSequences(line,sampleName, true, false);
						String fileName = line +  "-";
						tableAndChartData returnData = metapro.
								getTrypticPeptideAnaysis(metapro.readFasta(seq_for_lca, fileName), true, batchCommand);
					}
				}
				System.out.println("Done LCA");
				checkTimedOut(metapro);
			}
			//export picture commands
			if ((this.optionsCmd_.contentEquals("p"))
					|| (this.optionsCmd_.contentEquals("op"))
					|| (this.optionsCmd_.contentEquals("up"))
					|| (this.optionsCmd_.contentEquals("a"))) {
				System.out.println("Pathway-score-matrix");
				//builds a pathway matrix object which will be used to generate pathway pictues
				pwMAtrix = new PathwayMatrix(Project.samples_,
						Project.overall_, DataProcessor.pathwayList_,
						Controller.project_);
				//if the a command is selected export all of the pathway pictures 
				if (this.optionsCmd_.contentEquals("a")) {
					String tmpPAth = this.outPutPath_ + File.separator
							+ "pathwayPics";
					System.out.println("PathwayPics will be saved at: "
							+ tmpPAth);
					pwMAtrix.exportPics(tmpPAth, false, false);
				//if the p command is selected export all of the pathway pictures, then exit
				} else if (this.optionsCmd_.contentEquals("p")) {
					String tmpPAth = this.outPutPath_ + File.separator
							+ "pathwayPics";
					System.out.println("PathwayPics will be saved at: "
							+ tmpPAth);
					pwMAtrix.exportPics(tmpPAth, false, false);
					System.exit(0);
				//if the op command is selected export all the multi-pathway pictures, then exit
				} else if (this.optionsCmd_.contentEquals("op")) {
					String tmpPAth = this.outPutPath_ + File.separator
							+ "multiPathwayPics";
					//true in the exportPics call. There was no false in the previous calls.
					System.out.println("PathwayPics will be saved at: "
							+ tmpPAth);
					pwMAtrix.exportPics(tmpPAth, true, false);
					System.exit(0);
				//if the up command is selected then export only the user pathway picture, then exit.
				} else if (this.optionsCmd_.contentEquals("up")) {
					String tmpPAth = this.outPutPath_ + File.separator
							+ "userPathwayPics";
					System.out.println("PathwayPics will be saved at: "
							+ tmpPAth);
					System.out.println("onlyUserPaths");
					pwMAtrix.exportPics(tmpPAth, true, true);
					System.exit(0);
				}
				if (this.optionsCmd_.contentEquals("p")) {
					System.exit(0);
				}
			}
			/*
			 * exports the pathway score matrix for the corresponding commands. If s is selected it
			 * exits afterwards.
			 */
			if ((this.optionsCmd_.contentEquals("s"))
					|| (this.optionsCmd_.contentEquals("a"))
					|| (this.optionsCmd_.contentEquals("am"))) {
				System.out.println("Pathway-score-matrix");
				pwMAtrix = new PathwayMatrix(Project.samples_,
						Project.overall_, DataProcessor.pathwayList_,
						Controller.project_);
				pwMAtrix.exportMat(this.outPutPath_, true);
				if (this.optionsCmd_.contentEquals("s")) {
					System.exit(0);
				}
			}
			/*exports the pathway activity for the corresponding commands. If m
			 * is selected it exits afterward
			 */
			if ((this.optionsCmd_.contentEquals("m"))
					|| (this.optionsCmd_.contentEquals("a"))
					|| (this.optionsCmd_.contentEquals("am"))) {
				System.out.println("Pathway-activity-matrix");
				PathwayActivitymatrixPane pane = new PathwayActivitymatrixPane(
						Controller.processor_, Controller.project_,
						new Dimension(23, 23));
				pane.exportMat(false, this.outPutPath_);
				if (this.optionsCmd_.contentEquals("m")) {
					System.exit(0);
				}
			}
			/*exports the EC-activity matrix for the corresponding commands.
			 * If e is selected it exits afterwards
			 */
			if ((this.optionsCmd_.contentEquals("e"))
					|| (this.optionsCmd_.contentEquals("a"))
					|| (this.optionsCmd_.contentEquals("am"))) {
				System.out.println("EC-activity-matrix");
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,
						DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				pane.exportMat(this.outPutPath_, true);
				if (this.optionsCmd_.contentEquals("e")) {
					System.exit(0);
				}
			}
			if ((this.optionsCmd_.contentEquals("eclist"))||this.optionsCmd_.contentEquals("pvalue")){
				System.out.println("EC list or Pvalue");
				
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,
						DataProcessor.ecList_, Controller.processor_,
						new Dimension(12, 12));
				if(optionsCmd_.contentEquals("pvalue")){
					//need to make the matrix have the geometric distribution p values before sorting
					int ecCnt = 0;
					for (ecCnt = 0; ecCnt < pane.ecMatrix_.size(); ecCnt++) {
						Line ecNr = (Line) pane.ecMatrix_.get(ecCnt);
						pane.showHypergeometricDistribution(ecNr, ecCnt);
					}
					//sort by lowest p value, lowest p value to highest p value throughout all samples
					pane.quicksortGeoDist();
					pane.ecMatrix_ = pane.removeDuplicates();
					outPutPath_ += File.separator + Project.workpath_+"-pvalue-eclist.txt";
				}
				else{
					//add number of  Ec, it won't overwrite origin-file. It will plus a number at the end of file's name.
					if(num_ec_exported == 0)
						outPutPath_ += File.separator + Project.workpath_+"-eclist-all.txt";
					else
						outPutPath_ += File.separator + Project.workpath_+"-eclist"+"-"+num_ec_exported+".txt";
				}
				pane.exportEcNums(outPutPath_, this.num_ec_exported);
				if (this.optionsCmd_.contentEquals("eclist")) {
					System.exit(0);
				}
			}
			/*exports the samples as a .frp file so that it can be used
			 * in the GUI later. If f is selected it exists afterwards
			 */
			if ((this.optionsCmd_.contentEquals("f"))
					|| (this.optionsCmd_.contentEquals("a"))
					|| (this.optionsCmd_.contentEquals("am"))) {
				System.out.println("Export as .frp file");

				String projPath = "";
				if (Project.projectPath_.contains("projects")) {
					projPath = Project.projectPath_.substring(0,
							Project.projectPath_.indexOf("projects") - 1);
				} else {
					projPath = Project.projectPath_;
				}
				String tmpPath = projPath + File.separator + "projects"
						+ File.separator + inputPath_.substring(inputPath_.lastIndexOf(File.separator),inputPath_.lastIndexOf(".")) + ".frp";
				controller.saveProject(tmpPath);
				if (this.optionsCmd_.contentEquals("f")) {
					System.exit(0);
				}
			}
		}

		System.exit(0);
	}
	
	/**
	 * This method performs the ability to try re-running timed out ec numbers from the find lowest
	 * common ancestor operation.
	 * 
	 * @param meta MetaProteomicAnalysis class used which holds the timed out ec numbers
	 * 
	 * @author Jennifer Terpstra
	 */
	private void checkTimedOut(MetaProteomicAnalysis meta){
		
		if(meta.getTimedOut()==null){
			System.out.println("All Ec's LCA Successful");
			return;
		}
		//no timeout occured
		if(meta.getTimedOut().size()==0){
			System.out.println("All Ec's LCA Successful");
			return;
		}
		//timeout has occured try to re-run timed out ecs
		else{
			System.out.println("Timeout has occured!");
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,
					new Dimension(12, 12));
			String sampleName = "";
			for (int ec = 0; ec < meta.getTimedOut().size(); ec++) {
				String ecNum = meta.getTimedOut().get(ec).substring(0, meta.getTimedOut().get(ec).indexOf("-"));
				System.out.println(ecNum);
				if (checkEC(ecNum)) {
					LinkedHashMap<String,String> seq_for_lca;
					seq_for_lca = pane.cmdExportSequences(ecNum,sampleName, true, false);
					String fileName = ecNum +  "-";
					tableAndChartData returnData = meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca, fileName), true, batchCommand);
				}
			}
		}
	}
	
	private String getInputPath() {
		return args_[0];
	}

	private String getOutputPath() {
		return args_[1];
	}

	private String getOptionCmd() {
		return args_[2];
	}

	private static boolean checkEC(String options) {// checks that the EC is complete
		System.out.println("Check Ec");
		boolean ret = false;
		String testStr1;
		String testStr2;
		String testStr3;

		if (options.matches("[0-9].*")) {
			if (options.contains(".")) {
				testStr1 = options.substring(options.indexOf(".") + 1);
				if (testStr1.matches("[0-9].*")) {
					if (testStr1.contains(".")) {
						testStr2 = testStr1
								.substring(testStr1.indexOf(".") + 1);
						if (testStr2.matches("[0-9].*")) {
							if (testStr2.contains(".")) {
								testStr3 = testStr2.substring(testStr2
										.indexOf(".") + 1);
								if (testStr3.matches("[0-9]*")) {
									ret = true;
								}
							}
						}
					}
				}
			}
		}
		return ret;
	}
}
