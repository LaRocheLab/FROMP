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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import java.io.PrintStream;
import java.util.ArrayList;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import sun.org.mozilla.javascript.json.JsonParser;
import java.io.IOException;
import java.util.Date;
/**
 * Used for command line FROMP functions. Processes all command line user input
 * into FROMP.
 * 
 * @author Jennifer Terpstra, Kevan Lynch
 */

public class CmdController1 {
	public static String[] args_; // An array of String arguments taken in from the command line
	String inputPath_; // The inputpath given by the user
	public static String outPutPath_; // The output path given in by the user
	String optionsCmd_; // The option denoted by the user. ie h for help, etc
	ArrayList<String> ec_=new ArrayList<String>();; // If an EC number was denoted by the user to output sequence IDs, this is the variable it is saved to
	int num_ec_exported = 0; //number of ecs desired to be exported in the ec list
	static Controller controller; // The controller. Allows user to save, load etc.
	static PathwayMatrix pwMAtrix; // the Pathway Matrix
	//Basepath of the FROMP software. Necessary for all relative paths to function
	//final String basePath_ = new File(".").getAbsolutePath() + File.separator; 
	BufferedReader ecList; // Buffered reader used to read in the ec numbers listed in a file
	BufferedReader sequenceList;// buffered reader user to read in the sequence filenames listed in a file
	StringReader reader; // String reader to assist in the reading of the
	boolean batchCommand = false;
	private String inputPath;
	private String optionsCmd;
	int cmdCode;
	final String basePath_ = new File("").getAbsolutePath() + File.separator+"Output"+File.separator;
	//for inti
	public CmdController1(){
		
	}
	public CmdController1(String cmd,ArrayList<String> ecSet,String in,String out)  {
		this.optionsCmd_ = cmd;
		ec_=ecSet;
		inputPath_=in;
		outPutPath_=out;
		//processing();
		System.out.println("Starting cmdFromp");
		//add all samples
		readInputFile(inputPath_);
		//check load finish
		Controller.loadPathways(true);
		//process all samples
		processing();
		System.out.println("Done cmdFromp");
		
	}
	//read input file or  path.  IP=input path
	private void readInputFile(String IP) {
		// for loading file.
		controller = new Controller(Color.black);
		Panes.Loadingframe.showLoading = false;
		Panes.HelpFrame.showSummary = false;
		//input is a .frp file
		if (IP.endsWith(".frp")) {
			//add try-catch for check input file.
			try{			
				controller.loadProjFile(IP);
				System.out.println("Project file added");
				//get file name, for using as a part of output file name.
				Project.workpath_ = IP.substring(IP.lastIndexOf(File.separator)+1,IP.lastIndexOf("."));
			}
			catch (Exception e) {
				//e.printStackTrace();
				System.out.println("wrong .frp file: "+IP);
			}
		}
		//input is a .pwm file .user pathway
		else if(IP.endsWith(".pwm")){
			String userP = IP;
			//remove <userP> tag, if has
			if(userP.startsWith("<userP>")){
				userP=userP.substring(7);
			}
			//add user pathway to project
			try{			
				Project.addUserP(userP);
				System.out.println("user pathway made.\npathway addeds");
			}
			catch (Exception e) {
				//e.printStackTrace();
				System.out.println("wrong .pwm file");
			}
			
		}	
		//input is a normal sample. txt or .out
		else if(IP.endsWith(".txt")||IP.endsWith(".out")){
			//get filename.filetype.  such like abc.txt, need +1
			String name = IP.substring(IP.lastIndexOf(File.separator)+1);
			//set random color for sample, set color is necessary, because we may need output picture, if all sample set(0,0,0) will hard to distinguish
			Color col = new Color((float) Math.random(),(float) Math.random(),(float) Math.random());
			//IP = sample's path
			Sample sample = new Sample(name, IP, col);
			//samples_ is  static <Sample> Arraylist in project.java 
			Project.samples_.add(sample);
			System.out.println(IP+"   sample added");
			
		}
		//input is a normal sample (.txt or .out) with a sequence path.
		//1. (group samples) .frp file with  a sequence list file
		//2. (single sample) .txt or .out file with a single sequence file.
		
		
		//input is a .lst file . it may include all above type files and .lst. 
		else if(IP.endsWith(".lst")){
			try{
				BufferedReader in = new BufferedReader(new FileReader(IP));
				String line = in.readLine();
				while(line != null){
					//need check input path, because .lst in .lst not checked in startFromp.
					if( StartFromp1.checkPath(line, 1)){	
						readInputFile(line);
					}
					line = in.readLine();
				}
				in.close();
			}
			catch(Exception e){
				System.out.println("wrong .lst file:   "+IP );
			}
		}
	}//finish sample input work.
		
	//main processing method.
	private void processing(){	
		
		// p or a
		if (optionsCmd_.contentEquals("p")|| optionsCmd_.contentEquals("a")){
			System.out.println("Pathway-score-matrix");
			//builds a pathway matrix object which will be used to generate pathway pictures
			pwMAtrix = new PathwayMatrix(Project.samples_,Project.overall_, DataProcessor.pathwayList_,Controller.project_);
			String tmpPAth = "";
			if (outPutPath_.contentEquals("def")){
				tmpPAth= basePath_+"p";	
			}
			else{
				tmpPAth = outPutPath_ + File.separator+ "pathwayPics";	
			}
			System.out.println("PathwayPics will be saved at: "+ tmpPAth);
			// export pictures
			pwMAtrix.exportPics(tmpPAth, false, false);
			System.out.println("PathwayPics were saved at: "+ tmpPAth);
			//System.exit(0);	
		}
		// s or a or am
		if (optionsCmd_.contentEquals("s")||optionsCmd_.contentEquals("a")||optionsCmd_.contentEquals("am")){
			System.out.println("Pathway-score-matrix");
			pwMAtrix = new PathwayMatrix(Project.samples_,Project.overall_, DataProcessor.pathwayList_,Controller.project_);
			//export matrix. exportMat(path,ecf is selected)
			if (outPutPath_.contentEquals("def")){
				pwMAtrix.exportMat(basePath_+"s", true);				
			}
			else{
				pwMAtrix.exportMat(this.outPutPath_, true);
			}			
		}
		// m or a or am
		if (optionsCmd_.contentEquals("m") || optionsCmd_.contentEquals("a") || optionsCmd_.contentEquals("am")){
			System.out.println("Pathway-activity-matrix");
			PathwayActivitymatrixPane pane = new PathwayActivitymatrixPane(Controller.processor_, Controller.project_,new Dimension(23, 23));
			if (outPutPath_.contentEquals("def")){
				pane.exportMat(false,basePath_+"m");			
			}
			else{
				pane.exportMat(false, this.outPutPath_);
			}
		}
		// e or a or am
		if  (optionsCmd_.contentEquals("e") || optionsCmd_.contentEquals("a") || optionsCmd_.contentEquals("am")){
			System.out.println("EC-activity-matrix");
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			
			if (outPutPath_.contentEquals("def")){
				pane.exportMat(basePath_+"e", true);		
			}
			else{
				pane.exportMat(this.outPutPath_, true);		
			}
		}
		// f export the project as a .frp file. (it was  .txt + .out + .frp = new one .frp file ) need add def function
		//f or a 
		if ( optionsCmd_.contentEquals("f") || optionsCmd_.contentEquals("a") ){
			Date d = new Date();
			System.out.println("Export as .frp file");
			//project path
			String projPath = "";
			if(outPutPath_.contentEquals("def")){
				// need to add , if no exist folder, will create a new folder.---------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				projPath=basePath_+"f"+File.separator+"New Project - "+d.toString()+".frp";
			}
			//competed path. /xx/xx.frp
			else if (outPutPath_.endsWith(".frp")){
				projPath=outPutPath_;
			}
			//no name, creat one. /xx/ --> /xx/new project.frp
			else if (outPutPath_.endsWith(File.separator)){
				
				projPath=outPutPath_+"New Project - "+d.toString()+".frp";	
			}
			
			// not a .frp . /xx/out --> /xx/out.frp
			else{
				projPath=outPutPath_+".frp";
			}
			Controller.saveProject(projPath);
			System.out.println("New Project File at : "+projPath);
		}
		// op
		else if (optionsCmd_.contentEquals("op")){
			System.out.println("Pathway-score-matrix");
			//builds a pathway matrix object which will be used to generate pathway pictures
			pwMAtrix = new PathwayMatrix(Project.samples_,Project.overall_, DataProcessor.pathwayList_,Controller.project_);
			String tmpPAth = "";
			if (outPutPath_.contentEquals("def")){
				tmpPAth= basePath_+"op";	
			}
			else{
				tmpPAth = this.outPutPath_ + File.separator+ "pathwayPics";
				
			}
			
			System.out.println("PathwayPics will be saved at: "+ tmpPAth);
			// export pictures
			pwMAtrix.exportPics(tmpPAth, true, false);
			System.out.println("PathwayPics were saved at: "+ tmpPAth);
			//System.exit(0);
			
		}
		// up
		else if (optionsCmd_.contentEquals("up")){
			System.out.println("Pathway-score-matrix");
			//builds a pathway matrix object which will be used to generate pathway pictures
			pwMAtrix = new PathwayMatrix(Project.samples_,Project.overall_, DataProcessor.pathwayList_,Controller.project_);
			String tmpPAth = "";
			if (outPutPath_.contentEquals("def")){
				tmpPAth= basePath_+"op";	
			}
			else{
				tmpPAth = this.outPutPath_ + File.separator+ "pathwayPics";
				
			}
			
			System.out.println("PathwayPics will be saved at: "+ tmpPAth);
			System.out.println("onlyUserPaths");
			// export pictures
			pwMAtrix.exportPics(tmpPAth, true, true);
			System.out.println("PathwayPics were saved at: "+ tmpPAth);
			//System.exit(0);	
		}
		// ec ---------need add def !!!!!!!!!!!!!!
		else if (optionsCmd_.contentEquals("ec")){
			if(outPutPath_.contains("def")){		
				outPutPath_ = basePath_+"ec"+File.separator;
			}
			ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
			for (int i = 0; i < ec_.size(); i++) {
				
				pane.cmdExportRepseqs(this.ec_.get(i));
			}
			System.out.println("Repseqs saved at: "+ outPutPath_ );
		}
		// seq - checked out path. it can not assign file name, only can assign folder name, because it will generate many files.
		else if (optionsCmd_.contentEquals("seq")){
			if(outPutPath_.contains("def")){		
				outPutPath_ = basePath_+"seq"+File.separator;
			}
			//if it is not a folder.create a folder.
			else if (!outPutPath_.endsWith(File.separator)){
				outPutPath_ += File.separator;
			}
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			//System.out.println("Sequences will be saved at: " + basePath_+ "Sequences/");
			
			for (int i = 0; i < ec_.size(); i++) {
				
				pane.cmdExportSequences(this.ec_.get(i),"", false, false);
			}
			System.out.println("Files saved at: "+ outPutPath_ );
		}
		// seqall
		else if (optionsCmd_.contentEquals("seqall")){
			if(outPutPath_.contains("def")){		
				outPutPath_ = basePath_+"seqall"+File.separator;
			}
			//if no assigned file name.
			else if (outPutPath_.endsWith(File.separator)){
				outPutPath_ += Project.workpath_+"-seqall.txt";
			}
			
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			//System.out.println("Sequences will be saved at: " + basePath_+ "Sequences/");
			
			for (int i = 0; i < ec_.size(); i++) {
				
				pane.cmdExportSequences(this.ec_.get(i),"", true, false);
			}
			System.out.println("Files saved at: "+ outPutPath_ );
		}
		// eclist or eclist#such like.ecliset20 - checked out path
		else if (optionsCmd_.startsWith ("eclist")){
			//initialization
			System.out.println("EC list");
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			
			//output all
			if(optionsCmd_.contentEquals("eclist")){
				//num_ec_exported=0;
				if(outPutPath_.contentEquals("def")){
					outPutPath_=basePath_+"eclist"+File.separator + Project.workpath_+"-eclist-all.txt";;	
				}
				//if no assigned file name.
				else if (outPutPath_.endsWith(File.separator)){
					outPutPath_ += Project.workpath_+"-eclist-all.txt";
				}
				pane.exportEcNums(outPutPath_, this.num_ec_exported);
			}
			//output by assgined # 
			else{
				num_ec_exported = Integer.parseInt(optionsCmd_.substring(6));
				System.out.println("check #:"+num_ec_exported);
				
				if(outPutPath_.contentEquals("def")){
					outPutPath_=basePath_+"eclist"+File.separator + Project.workpath_+"-eclist"+"-"+num_ec_exported+".txt";	
				}
				//if no assigned file name.
				else if (outPutPath_.endsWith(File.separator)){
					outPutPath_ += Project.workpath_+"-eclist"+"-"+num_ec_exported+".txt";
				}
				pane.exportEcNums(outPutPath_, this.num_ec_exported);
			}
		}
		// pvalue - checked out path
		else if (optionsCmd_.startsWith("pvalue")){
			//initialization
			System.out.println("pvalue");
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			//need to make the matrix have the geometric distribution p values before sorting
			int ecCnt = 0;
			for (ecCnt = 0; ecCnt < pane.ecMatrix_.size(); ecCnt++) {
				Line ecNr = (Line) pane.ecMatrix_.get(ecCnt);
				pane.showHypergeometricDistribution(ecNr, ecCnt);
			}
			//sort by lowest p value, lowest p value to highest p value throughout all samples
			pane.quicksortGeoDist();
			pane.ecMatrix_ = pane.removeDuplicates();
			
			//output all
			if(optionsCmd_.contentEquals("pvalue")){
				//num_ec_exported=0;
				if(outPutPath_.contentEquals("def")){
					outPutPath_=basePath_+"pvalue"+File.separator + Project.workpath_+"-pvalue-eclist-all.txt";;	
				}
				//if no assigned file name.
				else if (outPutPath_.endsWith(File.separator)){
					outPutPath_ += Project.workpath_+"-pvalue-eclist-all.txt";
				}
				pane.exportEcNums(outPutPath_, this.num_ec_exported);
			}
			//output by assgined # 
			else{
				num_ec_exported = Integer.parseInt(optionsCmd_.substring(6));
				System.out.println("check pvalue #:"+num_ec_exported);
				
				if(outPutPath_.contentEquals("def")){
					outPutPath_=basePath_+"pvalue"+File.separator + Project.workpath_+"-pvalue-eclist"+"-"+num_ec_exported+".txt";	
				}
				//if no assigned file name.
				else if (outPutPath_.endsWith(File.separator)){
					outPutPath_ += Project.workpath_+"-pvalue-eclist"+"-"+num_ec_exported+".txt";
				}
				pane.exportEcNums(outPutPath_, this.num_ec_exported);
			}
			
			//outPutPath_ += File.separator + Project.workpath_+"-pvalue-eclist.txt";
			pane.exportEcNums(outPutPath_, this.num_ec_exported);
			
		}
		// lca.
		else if (optionsCmd_.contentEquals("lca")){
			
			//set output path
			if(outPutPath_.contains("def")){		
				outPutPath_ = basePath_+"lca"+File.separator;
			}
			
			
			//read sequence file.it means, connect sequence  file with project file.  ec# = null , it means use all ec# in project
			if(ec_.isEmpty()){

				this.reader = new StringReader();
				//bufferReader
				this.sequenceList = this.reader.readTxt(outPutPath_);
				MetaProteomicAnalysis metapro = new MetaProteomicAnalysis();
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
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
						//Top seq in eclist file. it means if read a eclist.txt file.
						else if(line.contains("Ec Activity EC Numbers")){
							//?
							batchCommand = true;
							line = sequenceList.readLine();
							String sampleName = "";
							LinkedHashMap<String,String> seq_for_lca;
							seq_for_lca = pane.cmdExportSequences(line,sampleName, true, false);
							String fileName = line +  "-";
							tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(metapro.readFasta(seq_for_lca, fileName), false, batchCommand);
							
							batchCommand = false;
						}
						//ec#. under "Ec Activity EC Numbers"
						else if(line.matches("[0-9]+.[0-9]+.[0-9]+.[0-9]+")){
							
							batchCommand = true;
							String sampleName = "";
							LinkedHashMap<String,String> seq_for_lca;
							seq_for_lca = pane.cmdExportSequences(line,sampleName, true, false);
							String fileName = line +  "-";
							tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(metapro.readFasta(seq_for_lca, fileName), false, batchCommand);
							
							batchCommand = false;
						}
						//not eclist, ec# or ">"
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
					// no time out.
					else{
						System.out.println("All files executed successfully");
					}
					System.out.println("Time" + " " + totalTime);
				} catch (IOException e) {
					System.out.println("File does not exist");
				}
			}
			//read ec# file , sequence = null
			else{

				MetaProteomicAnalysis metapro = new MetaProteomicAnalysis();
				ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
				String sampleName = "";
				String line = "";
				
				for (int i = 0;i<this.ec_.size();i++) {
					line = ""+ec_.get(i);
					//line =  a ec , then check is it ec
					
						LinkedHashMap<String,String> seq_for_lca;
						seq_for_lca = pane.cmdExportSequences(line,sampleName, true, false);
						String fileName = line +  "-";
						tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(metapro.readFasta(seq_for_lca, fileName), true, batchCommand);
					
				}
				System.out.println("Done LCA");
				checkTimedOut(metapro);
			}
		}
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
				if (StartFromp1.checkEC(ecNum)) {
					LinkedHashMap<String,String> seq_for_lca;
					seq_for_lca = pane.cmdExportSequences(ecNum,sampleName, true, false);
					String fileName = ecNum +  "-";
					tableAndChartData returnData = meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca, fileName), true, batchCommand);
				}
			}
		}
	}
	


	
}
