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
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStreamReader;

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
	String inputPath_	; // The inputpath given by the user
	public static String outPutPath_=StartFromp1.FolderPath; // The output path given in by the user
	public static String optionsCmd_=""; // The option denoted by the user. ie h for help, etc
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
	public static String tmpPath = "";
	final String basePath_ = StartFromp1.FolderPath+"Output"+File.separator;
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
		
		controller = new Controller(Color.black);
		
		Panes.Loadingframe.showLoading = false;
		Panes.HelpFrame.showSummary = false;
		//add all samples
		readInputFile(inputPath_);
		
		//check load finish
		Controller.loadPathways(true);
		//set out put file name.
		Project.workpath_ = inputPath_.substring(inputPath_.lastIndexOf(File.separator)+1,inputPath_.lastIndexOf("."));
		//process all samples
		processing();
		System.out.println("ALL Done.");
		System.exit(0);
		
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
		//input is a .lst/.list file . it may include all above type files and .lst. 
		else if(IP.endsWith(".lst")|| IP.endsWith(".list")){
			try{
				BufferedReader in = new BufferedReader(new FileReader(IP));
				String line = in.readLine();
				while(line != null){
					//if it is not a sample+seq file
					if (!line.contains("\t")){
						//need check input path, because that inside of .lst/.lst was not checked in startFromp.
						if( StartFromp1.checkPath(line, 1)){	
							readInputFile(line);
						}
						
					}
					//if it is a sample+seq file, will check input path by next step.
					else{
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
		//input is a *.* sample files.
		else{
			//System.out.println("111"+Project.workpath_);
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
		}
		
		
		//input is a normal sample (.txt or .out) with a sequence path.
		//1. (group samples) .frp file with  a sequence list file
		//2. (single sample) .txt or .out file with a single sequence file.
		
		
		
	}//finish sample input work.
		
	//main processing method.
	private void processing(){	
		
		// p or a --checked out put path.
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
		// s or a or am --checked output path
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
		// m or a or am --checked output path
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
		// e or a or am --checked output path
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
		// f export the project as a .frp file. (it was  .txt + .out + .frp = new one .frp file ) need add def function
		//f or a --checked out path
		if ( optionsCmd_.contentEquals("f") || optionsCmd_.contentEquals("a") ){
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy-HH_mm_ss");
			
			System.out.println("Export as .frp file");
			//project path
			String projPath = "";
			if(outPutPath_.contentEquals("def")){
				// need to add , if no exist folder, will create a new folder.---------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				projPath=basePath_+"f"+File.separator+"NewProject-"+sdf.format(d)+".frp";

			}

			else {
				
				projPath=outPutPath_+"NewProject-"+sdf.format(d)+".frp";
			}
			checkSeqFile();
			Controller.saveProject(projPath);
			System.out.println("New Project File at : "+projPath);
		}
		// op --checked out path
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
		// up - user pathway --checked out put path
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
		// ec --checked out put path
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
		// seq -- checked out put path. --checking seq file
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
		// seqall --checked output path --checking seq file
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
		// eclist or eclist# - checked out path
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
				//if it is a assgined path.
				else{
					tmpPath = outPutPath_+Project.workpath_+"-eclist-all.txt";
				}
				//pane.exportEcNums(tmpPath, this.num_ec_exported);
			}
			//output by assgined # 
			else{
				num_ec_exported = Integer.parseInt(optionsCmd_.substring(6));
				System.out.println("check #:"+num_ec_exported);
				
				if(outPutPath_.contentEquals("def")){
					tmpPath=basePath_+"eclist"+File.separator + Project.workpath_+"-eclist"+"-"+num_ec_exported+".txt";	
				}
				//if no assigned file name.
				else {
					tmpPath = outPutPath_+ Project.workpath_+"-eclist"+"-"+num_ec_exported+".txt";
				}
				//pane.exportEcNums(tmpPath, this.num_ec_exported);
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
				num_ec_exported = Integer.parseInt(optionsCmd_.substring(6));
				System.out.println("check pvalue #:"+num_ec_exported);
				
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
					tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(metapro.readFasta(seq_for_lca, fileName), true, batchCommand);
					
				}
			}
			
			if(!StartFromp1.FileSetofSeq.isEmpty()){
				
				for(int j=0;j< StartFromp1.FileSetofSeq.size();j++){
					seqWithFileName seqFile = StartFromp1.FileSetofSeq.get(j);
					metapro = new MetaProteomicAnalysis();
					batchCommand = true;
					tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(metapro.readFasta(seqFile.getIdSeq(), seqFile.getFileName()+"-"), true, batchCommand);
					
				}
				
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
						tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(peptide, true, batchCommand);
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
						tableAndChartData returnData = metapro.getTrypticPeptideAnaysis(peptide, true, batchCommand);
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
					if(!line.matches("[A-Za-z]*")){
						in.close();
						return false;
					}
				}
				line = in.readLine();
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
						
						System.out.print("\nContinue without sequence file(y) or quit(n) (y/n):");
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
					tableAndChartData returnData = meta.getTrypticPeptideAnaysis(meta.readFasta(seq_for_lca, fileName), true, batchCommand);
				}
			}
		}
	}
}
