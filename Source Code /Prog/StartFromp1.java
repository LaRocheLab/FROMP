package Prog;

import java.awt.Color;
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
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pathwayLayout.PathLayoutGrid;
import java.util.Date;

/**
 * This is the main method for FROMP. Its is what starts FROMP, and what decides
 * whether or not FROMP will be working in a gui or on the command line
 * depending upon the arguments the user includes.
 * 
 * @author Jennifer Terpstra, Kevan Lynch
 */
public class StartFromp1 {
	static NewFrompFrame newFrame;
	static CmdController1 cmd = new CmdController1();
	//static String arg1 = "";
	 //the totally number of  ecs need pass to cmdController for processing.
	static ArrayList<String> ecSet =new ArrayList<String>();
	static String in;
	static String out;
	//static BufferedReader fileIn;
	 //cmd code.
	static String cmdCode;
	String data="";
	
	/**
	 * Takes in a string array of arguments from the user which determines what
	 * the program will do. If there are no arguments, GUI Fromp starts. If
	 * there is one argument and that argument is h then the print options
	 * command is called. If there are 3 arguments and the third is a known
	 * command then the first argument is checked then taken as the input path,
	 * the second as the output path, and the third as the command to be done,
	 * cmdController is called. If there are four arguments, the program ensures
	 * the fourth is a proper EC then calls cmdController the same as before,
	 * but this time with an extra argument. otherwise the print options command
	 * is called.
	 * 
	 * @param args
	 *            User input which depending on the input changes how FROMP
	 *            works
	 * @author Jennifer Terpstra, Kevan Lynch
	 */
	public static void main(String[] args) {
		
		String[] arg1= args;
		//args = 0. start GUI		
		if(args.length  == 0){
			System.out.println("Welcome to  Fromp.\nGUI is starting...\nFor using cmdFROMP, please check FROMP-Maual.\n\n\n");
			newFrame = new NewFrompFrame();
			
		}
		//args = 1 , only h or d
		else if(args.length == 1){		
			//arg1 = args[0];
			//checkOptions(args[0]);
			if(args[0].contentEquals("h")){
				printOptions();	
			}
			else if(args[0].contentEquals("d")){
				PathLayoutGrid Grid = new PathLayoutGrid(10, 10, true);
			}
			else{
				argsError();	
			}		
		}
		/*
		//args =2, only inputPath + one ec #
		else if (args.length == 2){
			//check input path & ec#
			if(!checkPath(args[0],1) || !checkEC(args[1])){
				argsError();
			}
			
			System.out.println("cmd pass");
			//set cmd code(more detail of cmd code in /FROMP/cmd-info) and put ec # into set
			cmdCode=24;
			if(!ecSet.contains(line)){
				ecSet.add(args[1]);
			}
			cmd = new CmdController1(cmdCode,ecSet,in,out);
			
		}
		*/
		
		//args = 3. it may p,s,m,e,f,a,am,op,up,eclist(all),pvalue(all)
		else if (args.length == 3){
			
			if (checkPath(args[0],1) && checkPath(args[1],2)){
				if(args[2].contentEquals("p") || args[2].contentEquals("s") ||
						args[2].contentEquals("m") ||args[2].contentEquals("e") ||
						args[2].contentEquals("f") ||args[2].contentEquals("a") ||
						args[2].contentEquals("am") ||args[2].contentEquals("op") ||
						args[2].contentEquals("up") ||args[2].contentEquals("eclist") ||
						args[2].contentEquals("pvalue") ){
					
					cmdCode=args[2];
					in = args[0];
					out = args[1];
					//push to CmdController1
					cmd = new CmdController1(cmdCode,ecSet,in,out);			
					//System.out.println("cmd pass");
				}
				//wrong cmd.
				else{
					argsError();
				}		
			}
			//wrong input or output
			else{
				argsError();
			}	
		}
		//args = 4 or more it may ec,seq,seqall,eclist(#),pvalue(#),lca
		else if (args.length >3){
			if (checkPath(args[0],1) && checkPath(args[1],2)){
				in = args[0];
				out = args[1];
				//arg = eclist or pvalue
				if(args[2].contentEquals("eclist") || args[2].contentEquals("pvalue")){
					// if is not 4 args or 4th args not a #
					if (args.length !=4 || !checkNum(args[3])){
						argsError();
					}
					else{
						cmdCode=args[2]+args[3];
						//System.out.println(cmdCode);
						cmd = new CmdController1(cmdCode,ecSet,in,out);		
					}
					
				}
				//arg = ec,seq,seqall,lca)
				else if (args[2].contentEquals("ec") ||args[2].contentEquals("seq") ||
						args[2].contentEquals("seqall") ||args[2].contentEquals("lca")  ){
					//push all of input ec into ecset;
					for(int i=3; i<args.length;i++){
						EcFileReader(arg1[i]);
					}
					//sorting
					System.out.println("Sorting...");
					Collections.sort(ecSet);		
					//summary
					System.out.println(ecSet+"\nTotally "+ecSet.size()+" ec#s in the list.\nDone...");
					//push to cmdcontroller1
					cmdCode=args[2];
					cmd = new CmdController1(cmdCode,ecSet,in,out);			
				}
				//arg = other,error
				else{
					argsError();
				}
			}
			//input or output path is wrong.
			else{
				argsError();
			}
		}
		//args length problem, hardly to use.
		else{
			argsError();
		}
	}//main method finished.
	
	//store ec# from Multiple sources.
	public static  void EcFileReader(String EcOrList){
			// if it is a ec#
			if (checkEC(EcOrList)){
				if( !ecSet.contains(EcOrList)){
					ecSet.add(EcOrList);
					System.out.println(EcOrList+"  is added.continue...");
				}
				else
					System.out.println(EcOrList+"  has added already.pass...");
				
			}
			// if is a file path
			else if(checkPath(EcOrList,1)){
				try{
					BufferedReader fileIn1 = new BufferedReader(new FileReader(EcOrList));
					String line = fileIn1.readLine();
					
					while((line) != null){		
						EcFileReader(line);
						 line = fileIn1.readLine();					
					}
					fileIn1.close();
				}
				catch(Exception e){
					System.out.println("Can not read file  '"+EcOrList+"'   continue...");			
				}	
			}
			
			else{
				System.out.println(EcOrList+"  is not a valid ec number.pass...");
			}				
	}

	public static void run() { // runs gui fromp
		newFrame = new NewFrompFrame();
	}
	 // checks that the file given exists
	public static boolean checkPath(String path , int i) {
		
		//input file i=1
		if (i==1){	
			File f = new File(path);
			//no file or wrong file
			if(path.endsWith(File.separator) ||  !f.exists()){
				System.out.println("wrong inputfile:  " +path );
				return false;
			}
				
			else
				return true;		
		}
		//output file i=2
		else if(i==2)	{
			//default path
			if(path.contentEquals("def"))
				return true;
			//only check path, do not check file name.
			String bPath = path.substring(0, path.lastIndexOf(File.separator)+1);	
			//System.out.println(bPath);
			//System.out.println(path);
			File f = new File(bPath);
			return f.exists();	
		}				
		else{
			System.out.println("wrong outputfile:  " +path );
			return false;
		}
	}
	
	public static boolean checkEC(String options) {// checks that the EC is complete, ie is an ec number
		boolean ret = false;
		String testStr1;
		String testStr2;
		String testStr3;

		if (options.matches("[1-9].*")) {
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
	
	private static boolean checkNum(String options){
		try{
			Integer.parseInt(options);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	private static void argsError(){
		
		System.out.println("The arguements used are invalid.Plase check Manual or  type Help cmd blew.\njava -jar FROMP.jar h");
		System.exit(0);
	}
	private static void printOptions() { // Prints out the options for this program to the cmdline
		/*
		if (args[0] != "h") {
			System.out.println("The arguements used are invalid");
			System.out.println();
			System.out.println("The correct arguements are:");
		}
		*/		
		System.out.println("../java -jar FROMP.jar h for help");
		System.out.println("../java -jar FROMP.jar d for the pathwaydesigner");
		System.out.println();
		System.out.println("../java -jar FROMP.jar 'inputPath' 'outputPath' 'option'");
		System.out.println("Options are:");
		System.out.println("'p' for the pathway pictures");
		System.out.println("'s' for the pathway-score-matrix");
		System.out.println("'m' for the pathway-activity-matrix");
		System.out.println("'e' for the EC-activity-matrix");
		System.out.println("'f' to export the project as a .frp file");
		System.out.println("Exported .frp files will be saved in ~/projects");
		System.out.println("'a' all options");
		System.out.println("'am' for pathway-score-matrix, pathway-activity-matrix, EC-activity-matrix, and to export as a .frp file");
		System.out.println("'op' only multisample pictures");
		System.out.println("'up' only userpathway multisample pictures");
		System.out.println();
		System.out.println("'ec' to export a list of sequence IDs from a file of ec numbers from your input file");
		System.out.println("To include sequence IDs without a file of ec numbers, add the ec numbers");
		System.out.println("who's sequence ID you are interested in to your command");
		System.out.println("Sequence ID's will be stored in ~/RepSeqIDs");
		System.out.println();
		System.out.println("'seq' to export a file of sequences from a .frp file which has the sequence files related to the samples");
		System.out.println("Sequences will be stored in ~/Sequences");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' seq");
		System.out.println("'seqall' to export a file of sequences from a .frp file which has the sequence files related to the samples into one file per EC number");
		System.out.println("Sequences will be stored in ~/Sequences");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' seqall");
		System.out.println("'eclist' to export all ec numbers from the ec matrix");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' eclist");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' eclist 'number of ecs'");
		System.out.println("'pvalue' to export all ec numbers from the ec matrix that have been sorted according to lowest hypergeometric distribution pvalue");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' pvalue");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' pvalue 'number of ecs'");
		System.out.println("'lca' to find the lowest common ancestor for a given sequence file (or list of sequence file paths)."
				+ "Once the lowest common ancestor is determined the results are exported as both an excel file and a pie chart.");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'sequencePath' lca");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'sequenceListPath' lca");
		System.out.println("Excel files will be stored in ~/Excel");
		System.out.println("Piecharts will be stores in ~/PieChart");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'ecNumberListPath' lca");
		System.out.println("Total Taxon results will be stores in ~/Tables");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' lca 'ec number'");
		
		System.out.println();
		
		System.out.println();
		System.out.println();
		System.out.println("blanks in the input-path or output-path will lead to errors");
		System.out.println("to solve use braces: (\"inputPath\")");
	}
}
