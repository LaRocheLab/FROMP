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
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.io.IOException;
/**
 * Used for command line FROMP functions. Processes all command line user input
 * into FROMP.
 * 
 * @author Song Zhao, Jennifer Terpstra, Kevan Lynch
 */

public class CmdController1 {
	public static String[] args_; // An array of String arguments taken in from the command line
	String inputPath_	; // The input path given by the user
	public static String outPutPath_=StartFromp1.FolderPath; // The output path given in by the user
	public static String optionsCmd_=""; // The option denoted by the user. ie h for help, etc
	ArrayList<String> ec_=new ArrayList<String>();; // If an EC number was denoted by the user to output sequence IDs, this is the variable it is saved to
	int num_ec_exported = 0; //number of ecs desired to be exported in the ec list
	static Controller controller; // The controller. Allows user to save, load etc.
	static PathwayMatrix pwMAtrix; // the Pathway Matrix
	//Base path of the FROMP software. Necessary for all relative paths to function
	//final String basePath_ = new File(".").getAbsolutePath() + File.separator; 
	BufferedReader ecList; // Buffered reader used to read in the ec numbers listed in a file
	BufferedReader sequenceList;// buffered reader user to read in the sequence filenames listed in a file
	StringReader reader; // String reader to assist in the reading of the
	boolean batchCommand = false;
	int cmdCode;
	public static String tmpPath = "";
	public static boolean elistSortSum=false;
	final String basePath_ = StartFromp1.FolderPath+"def"+File.separator;
	static ArrayList <String> unusedEc = new ArrayList<String>();
	public CmdController1(){
		
	}
	public CmdController1(String cmd,ArrayList<String> ecSet,String in,String out) throws IOException  {
		optionsCmd_ = cmd;
		ec_=ecSet;
		inputPath_=in;
		outPutPath_=out;
		
		System.out.println("Starting cmdFromp");
		
		controller = new Controller(Color.black);
		
		Panes.Loadingframe.showLoading = false;
		Panes.HelpFrame.showSummary = false;
		
		//check user's request ec or/and go. before load file. only load requested item for save time.
		checkDoEcOrAndGo();
		
		//add all samples(only loaded .frp data)
		readInputFile(inputPath_);
		
		//load data from input file
		Controller.loadPathways(true);
		//set out put file name.
		Project.workpath_ = inputPath_.substring(inputPath_.lastIndexOf(File.separator)+1,inputPath_.lastIndexOf("."));
		//process all samples
		processing();
		
		
		System.out.println("ALL Done.");
		System.exit(0);
		
	}
	/**
	 * For checking user request ec or/and go. before load file. only load requested item(ec/go) for saving time.
	 */
	private void checkDoEcOrAndGo() {
		String [] EcRequest = {"p","s","m","e","op","up","ec","seq","seqall","lca","lcamat"}; 
		String [] GoRequest = {"g","go","seqgo","seqallgo","lcago","lcamatgo"};
		String [] AllRequest = {"f","a","am"};
		//only EC
		if (Arrays.asList(EcRequest).contains(optionsCmd_)){
			StartFromp1.doEC = true;
			StartFromp1.doGo = false;
		}
		//only GO
		else if (Arrays.asList(GoRequest).contains(optionsCmd_)){
			StartFromp1.doEC = false;
			StartFromp1.doGo = true;
		}
		//EC and GO
		else if (Arrays.asList(AllRequest).contains(optionsCmd_)){
			StartFromp1.doEC = true;
			StartFromp1.doGo = true;
		}
		//for special options eclist/golist pvalue/pvaluego
		else if (optionsCmd_.startsWith("eclist")||optionsCmd_.startsWith("pvalue")){
			StartFromp1.doEC = true;
			StartFromp1.doGo = false;
			
		}
		else if  (optionsCmd_.startsWith("golist")||optionsCmd_.startsWith("pvaluego")){
			StartFromp1.doEC = false;
			StartFromp1.doGo = true;
			
		}
		//test option "lca-all". one line can read EC, GO, and 
		else if (optionsCmd_.contentEquals("lca-all")){
			
			if (StartFromp1.ecSet.isEmpty()){
				StartFromp1.doEC = false;
			}
			if (StartFromp1.goSet.isEmpty()){
				StartFromp1.doGo = false;
			}
		}
	}
	//read input file or path.  IP=input path
	private void readInputFile(String IP) {
		// for loading file.
		
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
		// if input is a (sample + sequence) path. add sample meanwhile add sequence
		else if(IP.contains("\t")){
			//get sample path and sequence path from the input path
			String samplePath = IP.substring(0,IP.indexOf("\t"));
			String seqPath = IP.substring(IP.lastIndexOf("\t")+1);
			//check sample path,if pass add it.
			if (StartFromp1.checkPath(samplePath, 1)){
				//get filename.filetype.  such like abc.txt, need +1
				String name = samplePath.substring(samplePath.lastIndexOf(File.separator)+1);
				//set random color for sample, set color is necessary, because we may need output picture, if all sample set(0,0,0) will hard to distinguish
				Color col = new Color((float) Math.random(),(float) Math.random(),(float) Math.random());
				//IP = sample's path
				try{
					Sample sample = new Sample(name, samplePath, col);
					//add sample into sample list in project class.
					
					System.out.println(name+"   sample added");
					// check sequence path, if pass,connect it with sample 
					if(StartFromp1.checkPath(seqPath, 1)){
						sample.setSequenceFile(seqPath);
				
					}
					else{
						seqPath = "No Sequence";
					}			
					Project.samples_.add(sample);		
					System.out.println("sample: "+samplePath+" \nsequence: "+seqPath+"\n");
				
				}
				catch (Exception e){
					System.out.println("wrong sample");		
				}	
			}
			else{
				System.out.println("wrong sample");
			}
		}
		//input is a list file or sample file.
		else {
			try{
				BufferedReader in = new BufferedReader(new FileReader(IP));
				String line = in.readLine();
				while(line != null){
					
					//sample path (list file )
					if (StartFromp1.checkPath(line, 1)) {
						readInputFile(line);
					}
					//if it is a sample + seq path or the first line of sample file.
					else if (line.contains("\t")) {
						String part1 = line.substring(0,line.indexOf("\t"));
						String part2 = line.substring(line.indexOf("\t")+1);
						//if it is a sample + seq path
						if (StartFromp1.checkPath(part1, 1) && StartFromp1.checkPath(part2, 1)){
							readInputFile(line);
						}
						//if it is wrong input path
//						else if (line.contains(File.separator)) {
//							System.out.println("Wong input file path: " + line);					
//						}
						//add sample.
						else {
							//get filename.filetype.  such like abc.txt, need +1
							String name = IP.substring(IP.lastIndexOf(File.separator)+1);
							//set random color for sample, set color is necessary, because we may need output picture, if all sample set(0,0,0) will hard to distinguish
							Color col = new Color((float) Math.random(),(float) Math.random(),(float) Math.random());
							//IP = sample's path
							try{
								Sample sample = new Sample(name, IP, col);
								//add sample into sample list in project class.
								Project.samples_.add(sample);
								System.out.println(IP+"   sample added");
							}
							catch (Exception e){
								System.out.println("wrong sample");
							}		
							System.out.println(Project.workpath_);	
							break;
						}			
					}
					line = in.readLine();
				}
				in.close();
			}
			catch(Exception e){
				System.out.println("wrong file:   "+IP );
			}
		}		
	}//finish sample input work.
		
	//main processing method.
	private void processing() throws IOException{	
		
		//1. p or a --checked out put path.
		if (optionsCmd_.contentEquals("p")|| optionsCmd_.contentEquals("a")){
			System.out.println("Pathway-score-matrix");
			//builds a pathway matrix object which will be used to generate pathway pictures
			pwMAtrix = new PathwayMatrix(Project.samples_,Project.overall_, DataProcessor.pathwayList_,Controller.project_);
			
			if (outPutPath_.contentEquals("def")){
				tmpPath = basePath_+"p";	
			}
			else{
				tmpPath = outPutPath_ + "pathwayPics";	
			}
			//System.out.println("PathwayPics will be saved at: "+ tmpPath);
			// export pictures
			pwMAtrix.exportPics(tmpPath, false, false);
			System.out.println("PathwayPics were saved at: "+ tmpPath);
			//System.exit(0);	
		}
		//2. s or a or am --checked output path
		if (optionsCmd_.contentEquals("s")||optionsCmd_.contentEquals("a")||optionsCmd_.contentEquals("am")){
			System.out.println("Pathway-score-matrix");
			pwMAtrix = new PathwayMatrix(Project.samples_,Project.overall_, DataProcessor.pathwayList_,Controller.project_);
			//export matrix. exportMat(path,ecf is selected)
			if (outPutPath_.contentEquals("def")){
				tmpPath = basePath_+"s";
							
			}
			else{
				tmpPath = outPutPath_.substring(0,outPutPath_.length()-1);

			}
			
			pwMAtrix.exportMat(tmpPath, true);	
			System.out.println("Output files were saved at: "+ tmpPath);
		}
		//3. m or a or am --checked output path
		if (optionsCmd_.contentEquals("m") || optionsCmd_.contentEquals("a") || optionsCmd_.contentEquals("am")){
			System.out.println("Pathway-activity-matrix");
			PathwayActivitymatrixPane pane = new PathwayActivitymatrixPane(Controller.processor_, Controller.project_,new Dimension(23, 23));
			if (outPutPath_.contentEquals("def")){
				tmpPath = basePath_+"m";
						
			}
			else{
				tmpPath= outPutPath_.substring(0,outPutPath_.length()-1);
				
			}
			pane.exportMat(false,tmpPath);
			System.out.println("Output files were saved at: "+ tmpPath);
			
		}
		//4. e or a or am --checked output path
		if  (optionsCmd_.contentEquals("e") || optionsCmd_.contentEquals("a") || optionsCmd_.contentEquals("am")){
			System.out.println("EC-activity-matrix");
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			
			if (outPutPath_.contentEquals("def")){
				tmpPath = basePath_+"e";
					
			}
			else{
				tmpPath= outPutPath_.substring(0,outPutPath_.length()-1);
						
			}
			pane.exportMat(tmpPath, true);
			System.out.println("Output files were saved at: "+ tmpPath);
		}
		
		//5. g or a or am --checked output path // need to change!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
		if  (optionsCmd_.contentEquals("g") || optionsCmd_.contentEquals("a") || optionsCmd_.contentEquals("am")){
			System.out.println("GO-activity-matrix");
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			
			if (outPutPath_.contentEquals("def")){
				tmpPath = basePath_+"g";
					
			}
			else{
				tmpPath= outPutPath_.substring(0,outPutPath_.length()-1);
						
			}
			pane.exportMat(tmpPath, true);
			System.out.println("Output files were saved at: "+ tmpPath);
		}

		//6. f or a - export all data as a project(.frp) file.
		if ( optionsCmd_.contentEquals("f") || optionsCmd_.contentEquals("a") ){
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy-HH_mm_ss");
			
			System.out.println("Export as .frp file");
			//project path
			//String projPath = "";
			String fileName = inputPath_.substring(inputPath_.lastIndexOf(File.separator)+1);
			if(outPutPath_.contentEquals("def")){
				// need to add , if no exist folder, will create a new folder.---------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				tmpPath = basePath_+"f"+File.separator+fileName+"-New-"+sdf.format(d)+".frp";

			}

			else {
				
				tmpPath = outPutPath_+fileName+"-New-"+sdf.format(d)+".frp";
			}
			checkSeqFile();
			Controller.saveProject(tmpPath);
			System.out.println("New Project File at : "+tmpPath);
		}
		//7. op --checked out path
		else if (optionsCmd_.contentEquals("op")){
			System.out.println("Pathway-score-matrix");
			//builds a pathway matrix object which will be used to generate pathway pictures
			pwMAtrix = new PathwayMatrix(Project.samples_,Project.overall_, DataProcessor.pathwayList_,Controller.project_);
			
			if (outPutPath_.contentEquals("def")){
				tmpPath = basePath_+"op";	
			}
			else{
				tmpPath = outPutPath_ + "pathwayPics";
				
			}
			
			// export pictures
			pwMAtrix.exportPics(tmpPath, true, false);
			System.out.println("PathwayPics were saved at: "+ tmpPath);
			
			
		}
		//8. up - user pathway --checked out put path
		else if (optionsCmd_.contentEquals("up")){
			System.out.println("Pathway-score-matrix");
			//builds a pathway matrix object which will be used to generate pathway pictures
			pwMAtrix = new PathwayMatrix(Project.samples_,Project.overall_, DataProcessor.pathwayList_,Controller.project_);
			
			if (outPutPath_.contentEquals("def")){
				tmpPath= basePath_+"up";	
			}
			else{
				tmpPath = outPutPath_ + "pathwayPics";
				
			}
			
			//System.out.println("PathwayPics will be saved at: "+ tmpPath);
			System.out.println("onlyUserPaths");
			// export pictures
			pwMAtrix.exportPics(tmpPath, true, true);
			System.out.println("PathwayPics were saved at: "+ tmpPath);
			//System.exit(0);	
		}
		
		//9. ec --checked out put path
		else if (optionsCmd_.contentEquals("ec")){
			if(outPutPath_.contentEquals("def")){		
				tmpPath = basePath_+"ec"+File.separator;
			}
			else{
				tmpPath = outPutPath_;
			}
			ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
			for (int i = 0; i < ec_.size(); i++) {
				
				pane.cmdExportRepseqs(this.ec_.get(i));
			}
			System.out.println("Repseqs saved at: "+ tmpPath );
		}
		
		//10. go --checked out put path
		else if (optionsCmd_.contentEquals("go")){
			if(outPutPath_.contentEquals("def")){		
				tmpPath = basePath_+"go"+File.separator;
			}
			else{
				tmpPath = outPutPath_;
			}
			ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
			for (int i = 0; i < StartFromp1.goSet.size(); i++) {
				
				pane.cmdExportRepseqsGo(StartFromp1.goSet.get(i),tmpPath);
			}
			System.out.println("Repseqs saved at: "+ tmpPath );
		}
		
		//11. seq -- checked out put path. --checking seq file
		else if (optionsCmd_.contentEquals("seq")){
			
			checkSeqFile();
			
			if(outPutPath_.contains("def")){		
				tmpPath = basePath_+"seq"+File.separator;
			}
			
			else {
				tmpPath = outPutPath_;
			}
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			
			for (int i = 0; i < ec_.size(); i++) {
				
				pane.cmdExportSequences(this.ec_.get(i),"", false, false);
			}
			
			System.out.println("Output files were saved at: "+ tmpPath+"Sequences"+File.separator);
		}
		
		//12. seqgo -- checked out put path. --checking seq file
		else if (optionsCmd_.contentEquals("seqgo")){
			
			checkSeqFile();
			
			if(outPutPath_.contains("def")){		
				tmpPath = basePath_+"seqgo"+File.separator;
			}
			
			else {
				tmpPath = outPutPath_;
			}
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			
			for (int i = 0; i < StartFromp1.goSet.size(); i++) {
				
				pane.cmdExportSequencesGo(StartFromp1.goSet.get(i),"", false, false);
			}
			
			System.out.println("Output files were saved at: "+ tmpPath+"Sequences"+File.separator);
		}
		
		//13. seqall --checked output path --checking seq file
		else if (optionsCmd_.contentEquals("seqall")){
			
			checkSeqFile();
			
			if(outPutPath_.contentEquals("def")){	
				
				tmpPath = basePath_+"seqall"+File.separator;
			}
			else {
				tmpPath = outPutPath_;
			}
			
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			
			for (int i = 0; i < ec_.size(); i++) {
				
				pane.cmdExportSequences(this.ec_.get(i),"", true, false);
			}
			System.out.println("Output files were saved at: "+ tmpPath+"Sequences"+File.separator);
		}
		// eclist(s) or eclist#(s) - checked out path
		else if (optionsCmd_.startsWith ("eclist")){
			//initialization
			System.out.println("EC list");
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			
			//output all
			if(optionsCmd_.contentEquals("eclist")){
				//num_ec_exported=0;
				if(outPutPath_.contentEquals("def")){
					tmpPath=basePath_+"eclist"+File.separator + Project.workpath_+"-eclist-all.txt";;	
				}
				//if it is a assigned path.
				else{
					tmpPath = outPutPath_+Project.workpath_+"-eclist-all.txt";
				}
			}
			//output all - sort by sum
			else if(optionsCmd_.contentEquals("eclists")){
				elistSortSum = true;
				//num_ec_exported=0;
				if(outPutPath_.contentEquals("def")){
					tmpPath=basePath_+"eclist"+File.separator + Project.workpath_+"-eclist-all-sortBySum.txt";;	
				}
				//if it is a assigned path.
				else{
					tmpPath = outPutPath_+Project.workpath_+"-eclist-all-sortBySum.txt";
				}
				//pane.exportEcNums(tmpPath, this.num_ec_exported);
			}
			//output by assigned # 
			else{
				String sortBySum ="";
				if (optionsCmd_.endsWith("s")){
					elistSortSum = true;
					sortBySum = "-sortBySum";
					try{
						num_ec_exported=Integer.parseInt(optionsCmd_.substring(6,optionsCmd_.length()-1));
					}
					
					catch (Exception e){
						System.out.println("Wrong eclist options");
						System.exit(0);	
					}		
				}
				// only number
				else{
					try{
						num_ec_exported = Integer.parseInt(optionsCmd_.substring(6));
						System.out.println("check #:"+num_ec_exported);
					}
					catch (Exception e){
						System.out.println("Wrong eclist options");
						System.exit(0);
					}		
				}
				if(outPutPath_.contentEquals("def")){
					tmpPath=basePath_+"eclist"+File.separator + Project.workpath_+"-eclist"+"-"+num_ec_exported+sortBySum+".txt";	
				}
				//if no assigned file name.
				else {
					tmpPath = outPutPath_+ Project.workpath_+"-eclist"+"-"+num_ec_exported+sortBySum+".txt";
				}
			}	
			pane.exportEcNums(tmpPath, this.num_ec_exported);		
		}
		// pvalue or pvalue#- checked out path
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
					tmpPath = basePath_+"pvalue"+File.separator + Project.workpath_+"-pvalue-eclist-all.txt";;	
				}
				//if no assigned file name.
				else {
					tmpPath = outPutPath_+Project.workpath_+"-pvalue-eclist-all.txt";
				}
				
			}
			//output by assgined # 
			else{
				try{
					num_ec_exported = Integer.parseInt(optionsCmd_.substring(6));
					System.out.println("check pvalue #:"+num_ec_exported);
				}
				catch(Exception e){
					System.out.println("Wrong pvalue options. Quit");
					System.exit(0);
				}
			
				
				if(outPutPath_.contentEquals("def")){
					tmpPath = basePath_+"pvalue"+File.separator + Project.workpath_+"-pvalue-eclist"+"-"+num_ec_exported+".txt";	
				}
				//if no assigned file name.
				else if (outPutPath_.endsWith(File.separator)){
					tmpPath = outPutPath_+Project.workpath_+"-pvalue-eclist"+"-"+num_ec_exported+".txt";
				}
				
			}
			pane.exportEcNums(tmpPath, this.num_ec_exported);
			
		}
		// lca.- checked out path--can read ec , ec file , seq file.
		else if (optionsCmd_.contentEquals("lca")){
			
			checkSeqFile();
			//set output path
			if(outPutPath_.contentEquals("def")){		
				tmpPath = basePath_+"lca"+File.separator;
			}
			else {
				tmpPath = outPutPath_;
			}

			MetaProteomicAnalysis metapro = new MetaProteomicAnalysis();
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			pane.exportAll = true;
			String sampleName = "";
			String line = "";
			
			if (!ec_.isEmpty()){
				
				for (int i = 0;i<this.ec_.size();i++) {
					
					line = ""+ec_.get(i);
					LinkedHashMap<String,String> seq_for_lca;
					seq_for_lca = pane.cmdExportSequences(line,sampleName, true, false);
					String fileName = line +  "-";
					metapro = new MetaProteomicAnalysis();
					batchCommand = true;
					metapro.getTrypticPeptideAnaysis(metapro.readFasta(seq_for_lca, fileName), true, batchCommand);
					
				}
			}
			
			if(!StartFromp1.FileSetofSeq.isEmpty()){
				
				for(int j=0;j< StartFromp1.FileSetofSeq.size();j++){
					seqWithFileName seqFile = StartFromp1.FileSetofSeq.get(j);
					metapro = new MetaProteomicAnalysis();
					batchCommand = true;
					metapro.getTrypticPeptideAnaysis(metapro.readFasta(seqFile.getIdSeq(), seqFile.getFileName()+"-"), true, batchCommand);
					
				}
				
			}
			if (!unusedEc.isEmpty()){
				System.out.println("No values found within sequence file for ec: "+unusedEc);	
			}
			System.out.println("Done LCA");
			checkTimedOut(metapro);

		}
		// lcamat - can read ec , ec file , seq file.
		else if (optionsCmd_.contentEquals("lcamat")){
			
			checkSeqFile();		
			//set output path
			if(outPutPath_.contentEquals("def")){		
				tmpPath = basePath_+"lcamat"+File.separator;
			}
			else {
				tmpPath = outPutPath_;
			}		
			ActMatrixPane pane = new ActMatrixPane(Controller.project_,DataProcessor.ecList_, Controller.processor_,new Dimension(12, 12));
			pane.exportAll = true;
			MetaProteomicAnalysis metapro = new MetaProteomicAnalysis();
			
			String sampleName = "";
			String line = "";
			
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy-HH_mm_ss");
			
			//process ec file.
			if (!ec_.isEmpty()){
				String path = tmpPath+Project.workpath_+"-EC-Taxa-Matrix-"+sdf.format(d)+".txt";
				
				File file = new File(path);
				//StringBuffer tableContent = new StringBuffer();
				String separator = "\t";
				try{
					FileWriter fileWriter = new FileWriter(file);
					String writerLine = "EC-Taxa";
					
					for (int t =0 ; t < Project.samples_.size();t++){
						
						writerLine += separator+ Project.samples_.get(t).name_;
					
					}
					//title finished.
					fileWriter.write(writerLine+"\n");
					
					//writer each ec to line.
					for (int i = 0;i<this.ec_.size();i++) {
						
						line = ""+ec_.get(i);
						LinkedHashMap<String,String> seq_for_lca;
						seq_for_lca = pane.cmdExportSequences(line,sampleName, true, false);
						String fileName = line +  "-";
						metapro = new MetaProteomicAnalysis(); 
						ArrayList<TrypticPeptide> peptide = metapro.readFasta(seq_for_lca, fileName); 
						batchCommand = true;
						metapro.getTrypticPeptideAnaysis(peptide, true, batchCommand);
						// Prepare printable  taxa table
						HashMap <String, int []> taxaTable = new HashMap<String, int[]>();
						
						
						//each peptide
						for (int j =0; j<peptide.size();j++){
							
							if (peptide.get(j).getIdentifiedTaxa() != null) {
								String ecTaxaName = fileName+peptide.get(j).getIdentifiedTaxa().getTaxon_name();
								String samName = peptide.get(j).getUniqueIdentifier().substring(peptide.get(j).getUniqueIdentifier().indexOf(" ")+1); 
								int samIndex=0;
								for (int s=0;s<Project.samples_.size();s++){
									if (Project.samples_.get(s).name_.contentEquals(samName)){
										samIndex = s;
										break;	
									}	
								}
								
//								System.out.println("ecTaxaName:"+ecTaxaName);
//								System.out.println("samName:"+samName);
//								System.out.println("samIndex:"+samIndex);	
								//no need root, Bacteria and Inconclusive
								if(!ecTaxaName.contentEquals(fileName+"root") && !ecTaxaName.contentEquals(fileName+"Bacteria") && !ecTaxaName.contentEquals(fileName+"Inconclusive")){
									
									if (taxaTable.containsKey(ecTaxaName)){
										int [] num = taxaTable.get(ecTaxaName);
										num [samIndex] += 1;
										taxaTable.put(ecTaxaName,num);		
									}
									else{
										int [] num = new int [Project.samples_.size()];
										num[samIndex]+=1;
										taxaTable.put(ecTaxaName,num);		
									}	
								}
							}	
						}						
						for (String key : taxaTable.keySet() ){
							String Line =key;
							int [] num = taxaTable.get(key);
							
							for (int sam = 0 ; sam < num.length;sam++){
								Line += "\t"+num[sam];	
							}
							System.out.println("Line:"+Line);
							fileWriter.write(Line+"\n");		
						}
						// one empty line for each ec.
						if(!taxaTable.keySet().isEmpty()){
							fileWriter.write("\t\n");
							
						}
					}
					fileWriter.close();					
				}
				catch(IOException e){
					e.printStackTrace();
				}	
				
			}
			// Process seq file.
			if (!StartFromp1.FileSetofSeq.isEmpty()){
				String path = tmpPath+Project.workpath_+"-Seq-Taxa-Matrix-"+sdf.format(d)+".txt";
				
				File file = new File(path);
				//StringBuffer tableContent = new StringBuffer();
				String separator = "\t";
				try{
					FileWriter fileWriter = new FileWriter(file);
					String writerLine = "Seq-Taxa";
					
					for (int t =0 ; t < Project.samples_.size();t++){
						
						writerLine += separator+ Project.samples_.get(t).name_;
					
					}
					//title finished.
					fileWriter.write(writerLine+"\n");
					
					//writer each ec to line.
					for (int i = 0;i< StartFromp1.FileSetofSeq.size();i++) {
						
						String fileName = StartFromp1.FileSetofSeq.get(i).getFileName() +  "-";
						metapro = new MetaProteomicAnalysis(); 
						ArrayList<TrypticPeptide> peptide = metapro.readFasta(StartFromp1.FileSetofSeq.get(i).getIdSeq(), fileName); 
						batchCommand = true;
						metapro.getTrypticPeptideAnaysis(peptide, true, batchCommand);
						// Prepare printable  taxa table
						HashMap <String, int []> taxaTable = new HashMap<String, int[]>();
						
						
						//each peptide
						for (int j =0; j<peptide.size();j++){	
							if (peptide.get(j).getIdentifiedTaxa() != null) {
								String ecTaxaName = fileName+peptide.get(j).getIdentifiedTaxa().getTaxon_name();
								String samName = peptide.get(j).getUniqueIdentifier().substring(peptide.get(j).getUniqueIdentifier().indexOf(" ")+1); 
								int samIndex=0;
								for (int s=0;s<Project.samples_.size();s++){
									if (Project.samples_.get(s).name_.contentEquals(samName)){
										samIndex = s;
										break;	
									}	
								}
								//no need root, Bacteria and Inconclusive
								if(!ecTaxaName.contentEquals(fileName+"root") && !ecTaxaName.contentEquals(fileName+"Bacteria") && !ecTaxaName.contentEquals(fileName+"Inconclusive")){
									
									if (taxaTable.containsKey(ecTaxaName)){
										int [] num = taxaTable.get(ecTaxaName);
										num [samIndex] += 1;
										taxaTable.put(ecTaxaName,num);		
									}
									else{
										int [] num = new int [Project.samples_.size()];
										num[samIndex]+=1;
										taxaTable.put(ecTaxaName,num);		
									}	
								}
							}
							
						}
						for (String key : taxaTable.keySet() ){
							String Line =key;
							int [] num = taxaTable.get(key);
							
							for (int sam = 0 ; sam < num.length;sam++){
								Line += "\t"+num[sam];	
							}
							System.out.println("Line:"+Line);
							fileWriter.write(Line+"\n");		
						}
						// one empty line for each ec.
						if(!taxaTable.keySet().isEmpty()){
							fileWriter.write("\t\n");
							
						}
					}
					fileWriter.close();					
				}
				catch(IOException e){
					e.printStackTrace();
				}					
			}
			if (!unusedEc.isEmpty()){
				System.out.println("No values found within sequence file for ec: "+unusedEc);	
			}
			System.out.println("Done Lca Matrix");
		}
	}
	
	public static boolean checkSeqFileFormat(String seqFilePath){
		
		try{
			BufferedReader in = new BufferedReader(new FileReader(seqFilePath));
			String line = in.readLine();
			//test 20 lines
			int count = 0 ;
			while (line != null && count<=20){
				
				if (!line.startsWith(">")){
					in.close();
					return false;
					
				}
				else {
					
					line = in.readLine();
					while (!line.startsWith(">")){
						if(!line.matches("[A-Za-z]*")){
							in.close();
							return false;
						}
						
						line = in.readLine();
					}
					
				}
//				line = in.readLine();
				count++;	
			}
			in.close();
			return true;			
		}
		catch(Exception e){	
			System.out.println("Wrong sequence file.");
			return false;
		}	
	}
	
	// checking is there a sequence file connect with sample file.
	private void checkSeqFile() {
		
		System.out.println("Checking sequence file...");
		ArrayList<String>sampleWithoutSeq = new ArrayList<String>();
		
		for (int i =0 ; i < Project.samples_.size();i++){	
			
			if(Project.samples_.get(i).getSequenceFile()== null || 
					Project.samples_.get(i).getSequenceFile().contentEquals("")){
				
				sampleWithoutSeq.add(i, Project.samples_.get(i).name_);
			}	
		}
		if (sampleWithoutSeq.size()!=0){
			System.err.println("\nNo sequence file found for the samples below:\n");
			
			for (int i = 0 ; i < sampleWithoutSeq.size();i++){
				if (sampleWithoutSeq.get(i)!=null){
					System.out.println(sampleWithoutSeq.get(i));
				}
			}
			System.out.println("\nSequence file is needed for cmd :'seq' 'seqall' and 'lca'. optional for cmd :'f'");
			System.out.print("Add sequence file for the samples?(y/n):");
			boolean pass = false;
			Scanner kb = new Scanner(System.in);
			String key = kb.nextLine().toLowerCase();
			System.out.println();
			while (!pass){
				//add seq file	
				if (key.contentEquals("y")){
					//add seq file in order to all samples without seq.
					for (int i = 0 ; i < sampleWithoutSeq.size();i++){
						if (sampleWithoutSeq.get(i)!=null){
							while(!pass){
								
								System.out.println("\nAdding sequence file for: "+sampleWithoutSeq.get(i));
								// pass until input correct.
								System.out.print("\nInput sequence file path('n' to skip current sample):");
								key = kb.nextLine();
								System.out.println();
								//if input is a path , not n or N;
								if(!key.contentEquals("n")&&!key.contentEquals("N")){
									//check path first
									if(StartFromp1.checkPath(key, 1)){
										if (checkSeqFileFormat(key)){
											Project.samples_.get(i).setSequenceFile(key);
											System.out.println("Sequence file has added");
											pass=true;
										}
										else{
											System.out.println("It is not a valid sequence file");
											pass=false;
										}
									}
									else {
										pass = false;
									}
								}
								//n to skip current sample
								else{
									System.out.println(sampleWithoutSeq.get(i)+" has skipped");
									pass = true;
								}
								
							}
							pass = false;
						
						}
					}
					key = "y";
					pass=true;
				}
				//do not add seq file- continue or quit
				else if (key.contains("n")){
					while(!pass){
						
						System.out.print("Continue without sequence file(y) or quit(n) (y/n):");
						key = kb.nextLine();
						key = key.toLowerCase();
						System.out.println();
						if (key.contentEquals("y")){
							pass = true;
						}
						else if (key.contentEquals("n")){
							System.exit(0);
						}
						else{
							pass =false;
						}				
					}
				}
				// input was not y or n
				else {
					
					pass = false;
					System.out.print("Add sequence file for the samples?(y/n):");
					key = kb.nextLine().toLowerCase();
					System.out.println();
				}		
			}//loop for adding seq or not. 
			kb.close();		
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
				if (!StartFromp1.checkEC(ecNum).contentEquals("-1")) {
					LinkedHashMap<String,String> seq_for_lca;
					seq_for_lca = pane.cmdExportSequences(ecNum,sampleName, true, false);
					String fileName = ecNum +  "-";
					meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca, fileName), true, batchCommand);
				}
			}
		}
	}
}
