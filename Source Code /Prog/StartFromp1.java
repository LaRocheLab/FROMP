package Prog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import pathwayLayout.PathLayoutGrid;

/**
 * This is the main method for FROMP. Its is what starts FROMP, and what decides
 * whether or not FROMP will be working in a gui or on the command line
 * depending upon the arguments the user includes.
 * 
 * @author Song Zhao, Jennifer Terpstra, Kevan Lynch
 */
public class StartFromp1 {
    static NewFrompFrame newFrame;
    static CmdController1 cmd = new CmdController1();
    // the totally number of ecs need pass to cmdController for processing.
    public static ArrayList<String> ecSet = new ArrayList<String>();
    // the totally number of gos need pass to cmdController for processing.
    public static ArrayList<String> goSet = new ArrayList<String>();
    public static ArrayList<seqWithFileName> FileSetofSeq = new ArrayList<seqWithFileName>();
    static String in;
    static String out;
    static String cmdCode;

    String data = "";
    public static String FolderPath = "";
    public static boolean isSeq = false;
    // controller for loading ec or/and go
    public static boolean doEC = true;
    public static boolean doGo = true;

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
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
	File test = new File(StartFromp1.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	String test1 = test.toString();
	String folderPath = "";
	if (test1.endsWith("bin")) {
	    folderPath = test1.substring(0, test1.lastIndexOf("bin"));
	} else {
	    folderPath = test1.substring(0, test1.lastIndexOf("FROMP.jar"));
	}

	FolderPath = folderPath;
	cmd = new CmdController1();
	// args = 0. start GUI
	if (args.length == 0) {
	    System.out.println(
		    "Welcome to  FROMP.\nGUI is starting...\nFor using cmdFROMP, please check FROMP-Maual.\n\n\n");
	    newFrame = new NewFrompFrame();

	}
	// args = 1 , only h or d
	else if (args.length == 1) {

	    if (args[0].contentEquals("h")) {
		printOptions();
	    } else if (args[0].contentEquals("d")) {
		@SuppressWarnings("unused") // open a new window
		PathLayoutGrid Grid = new PathLayoutGrid(10, 10, true);
	    } else {
		argsError();
	    }
	}

	// args = 3. it may
	// p,s,m,e,g,f,a,am,op,up,eclist(all),pvalue(all),golist(all),pvaluego(all)
	else if (args.length == 3) {

	    if (checkPath(args[0], 1) && checkPath(args[1], 2)) {
		if (args[2].contentEquals("p") || args[2].contentEquals("s") || args[2].contentEquals("m")
			|| args[2].contentEquals("e") || args[2].contentEquals("f") || args[2].contentEquals("a")
			|| args[2].contentEquals("am") || args[2].contentEquals("op") || args[2].contentEquals("up")
			|| args[2].contentEquals("eclist") || args[2].contentEquals("pvalue")
			|| args[2].contentEquals("g") || args[2].contentEquals("golist")
			|| args[2].contentEquals("pvaluego")) {

		    cmdCode = args[2];
		    in = args[0];
		    // out = args[1];
		    // push to CmdController1
		    cmd = new CmdController1(cmdCode, ecSet, in, out);
		    // System.out.println("cmd pass");
		}
		// wrong cmd.
		else {
		    argsError();
		}
	    }
	    // wrong input or output
	    else {
		argsError();
	    }
	}
	// args = 4 or more it may
	// ec,go,seq,seqgo,seqall,seqallgo,eclist(#)/s,golist(#)/s,pvalue(#),pvaluego(#),lca,lcago,lcamat,lcamatgo.
	else if (args.length > 3) {
	    if (checkPath(args[0], 1) && checkPath(args[1], 2)) {
		in = args[0];
		// out = args[1];
		// arg = eclist or pvalue
		if (args[2].contentEquals("eclist") || args[2].contentEquals("pvalue")
			|| args[2].contentEquals("golist") || args[2].contentEquals("pvaluego")) {
		    // if is not 4 args or 4th args not a #
		    // if (args.length !=4 || !checkNum(args[3])){
		    if (args.length != 4) {
			argsError();
		    } else {
			cmdCode = args[2] + args[3];
			// System.out.println(cmdCode);
			cmd = new CmdController1(cmdCode, ecSet, in, out);
		    }

		}
		// arg = ec,seq,seqall)
		else if (args[2].contentEquals("ec") || args[2].contentEquals("seq") || args[2].contentEquals("seqall")
			|| args[2].contentEquals("go") || args[2].contentEquals("seqgo")
			|| args[2].contentEquals("seqallgo")) {
		    // push all of input ec into ecset;
		    for (int i = 3; i < args.length; i++) {
			EcFileReader(args[i]);
		    }
		    // sorting
		    System.out.println("Sorting...");
		    Collections.sort(ecSet);
		    // summary
		    System.out.println(ecSet + "\nTotally " + ecSet.size() + " ec#s in the list.\nDone...");
		    System.out.println(goSet + "\nTotally " + goSet.size() + " go#s in the list.\nDone...");
		    // push to cmdcontroller1
		    cmdCode = args[2];
		    cmd = new CmdController1(cmdCode, ecSet, in, out);
		}
		// arg = lca , lcamat. collection ec or seq
		else if (args[2].contentEquals("lca") || args[2].contentEquals("lcamat")
			|| args[2].contentEquals("lcago") || args[2].contentEquals("lcamatgo")) {

		    // push all of input ec into ecset;
		    for (int i = 3; i < args.length; i++) {
			EcFileReader(args[i]);
			if (isSeq == true) {
			    SeqFileReader(args[i]);
			    isSeq = false;

			}
		    }
		    // sorting
		    System.out.println("Sorting...");
		    Collections.sort(ecSet);
		    // summary
		    System.out.println(ecSet + "\nTotally " + ecSet.size() + " ec#s in the list.\nDone...");
		    System.out.println(goSet + "\nTotally " + goSet.size() + " go#s in the list.\nDone...");
		    // push to cmdcontroller1
		    cmdCode = args[2];
		    cmd = new CmdController1(cmdCode, ecSet, in, out);

		}
		// arg = other,error
		else {
		    argsError();
		}
	    }
	    // input or output path is wrong.
	    else {
		argsError();
	    }
	}
	// args length problem, hardly to use.
	else {
	    argsError();
	}
    }// main method finished.
     // add all seq for lca

    public static void SeqFileReader(String SeqOrList) {

	// if is a file path
	if (checkPath(SeqOrList, 1)) {
	    try {
		BufferedReader fileIn = new BufferedReader(new FileReader(SeqOrList));
		String line = fileIn.readLine();
		while ((line) != null) {
		    if (line.startsWith(">")) {
			seqWithFileName seqFile = new seqWithFileName();
			String name = SeqOrList.substring(SeqOrList.lastIndexOf(File.separator) + 1);
			seqFile.setFileName(name);
			while ((line) != null) {

			    String ID = line;
			    line = fileIn.readLine();
			    String Seq = "";
			    while (!line.startsWith(">")) {
				Seq += line;
				line = fileIn.readLine();
			    }
			    seqFile.addtoMap(ID, Seq);
			}

			FileSetofSeq.add(seqFile);
		    }

		    else {

			SeqFileReader(line);
		    }
		    line = fileIn.readLine();
		}
		fileIn.close();
	    } catch (Exception e) {
		System.out.println("Can not read file  '" + SeqOrList + "'   continue...");
	    }
	} else {
	    System.out.println("wrong sequence file : " + SeqOrList);
	}

    }

    // store ec# from Multiple sources.
    public static void EcFileReader(String EcOrList) {
	// check is it a seq file.
	if (EcOrList.startsWith(">")) {
	    isSeq = true;
	    return;
	}
	// if it is a ec#
	String ec = checkEC(EcOrList);
	if (!ec.contentEquals("-1")) {
	    if (!ecSet.contains(ec)) {
		ecSet.add(ec);
		System.out.print("\r" + ec + "  is added.continue...");
	    } else
		System.out.print("\r" + ec + "  has added already.pass...");

	}
	// if it is a go#
	else if (EcOrList.matches("\\d{7}")) {
	    if (!goSet.contains(EcOrList)) {
		goSet.add(EcOrList);
		System.out.print("\r" + EcOrList + "  is added.continue...");
	    } else
		System.out.print("\r" + EcOrList + "  has added already.pass...");

	}
	// if is a file path
	else if (checkPath(EcOrList, 1)) {
	    try {
		@SuppressWarnings("resource")
		BufferedReader fileIn1 = new BufferedReader(new FileReader(EcOrList));
		String line = fileIn1.readLine();

		while ((line) != null) {
		    if (line.startsWith(">")) {
			isSeq = true;
			return;
		    }
		    EcFileReader(line);
		    line = fileIn1.readLine();
		}
		fileIn1.close();

	    } catch (Exception e) {
		System.out.println("Can not read file  '" + EcOrList + "'   continue...");
	    }

	}

	else {
	    System.out.println(EcOrList + "  is not a valid ec/go number.pass...");
	}
    }

    public static void run() { // runs gui fromp
	newFrame = new NewFrompFrame();
    }

    // checks that the file given exists
    public static boolean checkPath(String path, int i) {

	// input file i=1
	if (i == 1) {
	    File f = new File(path);
	    // no file or wrong file
	    if (path.endsWith(File.separator) || !f.exists()) {
		if (!(path.matches(".*IPR.*") || path.matches(".*UniRef90.*") || path.matches(".*Pfam.*"))) {
		    System.out.println("This is not a vaid file path:" + path);
		}

		return false;
	    }

	    else
		return true;
	}
	// output file i=2
	else if (i == 2) {
	    // default path
	    if (path.contentEquals("def")) {
		out = "def";
		return true;
	    }

	    // check path, do not allow to assign a file name.
	    // if out path without "/",add a "/"
	    if (!path.endsWith(File.separator)) {
		path += File.separator;
	    }
	    File f = new File(path);
	    if (f.exists()) {
		out = path;
		return true;
	    } else {
		System.out.println("The Path of Output file was WRONG or NOT EXISTS. Issue at:" + path);
		return false;
	    }

	}
	return false;
    }

    public static String checkEC(String options) {// checks that the EC is
	// complete, ie is an ec
	// number

	String testStr1;
	String testStr2;
	String testStr3;

	if (options.matches("[1-9].*")) {
	    if (options.contains(".")) {
		testStr1 = options.substring(options.indexOf(".") + 1);
		if (testStr1.matches("[0-9].*")) {
		    if (testStr1.contains(".")) {
			testStr2 = testStr1.substring(testStr1.indexOf(".") + 1);
			if (testStr2.matches("[0-9].*")) {
			    if (testStr2.contains(".")) {
				testStr3 = testStr2.substring(testStr2.indexOf(".") + 1);
				if (testStr3.matches("[0-9]*")) {
				    return options;
				} else if (testStr3.matches("[0-9](.*)")) {

				    while (testStr3.matches("[0-9](.*)")) {
					testStr3 = testStr3.substring(1);

				    }

				    options = options.substring(0, options.indexOf(testStr3));

				    return options;
				}
			    }
			}
		    }
		}
	    }
	}

	return "" + -1;
    }

    public static String getCmdCode() {
	return cmdCode;
    }

    public static void setCmdCode(String cmdCode) {
	StartFromp1.cmdCode = cmdCode;
    }

    private static void argsError() {

	System.out.println(
		"\nThe arguements used are invalid.Plase check Manual or input Help command line blew.\njava -jar FROMP.jar h");
	System.exit(0);
    }

    private static void printOptions() { // Prints out the options for this
	// program to the cmd line

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
	System.out.println(
		"'am' for pathway-score-matrix, pathway-activity-matrix, EC-activity-matrix, and to export as a .frp file");
	System.out.println("'op' only multisample pictures");
	System.out.println("'up' only userpathway multisample pictures");
	System.out.println();
	System.out.println("'ec' to export a list of sequence IDs from a file of ec numbers from your input file");
	System.out.println("To include sequence IDs without a file of ec numbers, add the ec numbers");
	System.out.println("who's sequence ID you are interested in to your command");
	System.out.println("Sequence ID's will be stored in ~/RepSeqIDs");
	System.out.println();
	System.out.println(
		"'seq' to export a file of sequences from a .frp file which has the sequence files related to the samples");
	System.out.println("Sequences will be stored in ~/Sequences");
	System.out.println("Syntax: java -jar FROMP.jar 'inputPath' seq");
	System.out.println(
		"'seqall' to export a file of sequences from a .frp file which has the sequence files related to the samples into one file per EC number");
	System.out.println("Sequences will be stored in ~/Sequences");
	System.out.println("Syntax: java -jar FROMP.jar 'inputPath' seqall");
	System.out.println("'eclist' to export all ec numbers from the ec matrix");
	System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' eclist");
	System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' eclist 'number of ecs'");
	System.out.println(
		"'pvalue' to export all ec numbers from the ec matrix that have been sorted according to lowest hypergeometric distribution pvalue");
	System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' pvalue");
	System.out.println("Syntax: java -jar FROMP.jar 'inputPath' 'outputPath' pvalue 'number of ecs'");
	System.out.println(
		"'lca' to find the lowest common ancestor for a given sequence file (or list of sequence file paths)."
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
