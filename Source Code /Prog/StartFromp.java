package Prog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pathwayLayout.PathLayoutGrid;

/**
 * This is the main method for FROMP. Its is what starts FROMP, and what decides
 * whether or not FROMP will be working in a gui or on the command line
 * depending upon the arguments the user includes.
 * 
 * @author Jennifer Terpstra, Kevan Lynch
 */
public class StartFromp {
	static NewFrompFrame newFrame;
	static String arg1 = "";

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
		if (args != null) {
			if (args.length != 0) {
				String[] arrayOfString = args;
				int j = args.length;
				for (int i = 0; i < j; i++) {
					String s = arrayOfString[i];
					System.out.println(s);
				}
				if (args.length == 1) {
					if (args[0].contentEquals("h")) {
						arg1 = "h";
						printOptions();
					} else if (args[0].contentEquals("d")) {
						PathLayoutGrid Grid = new PathLayoutGrid(10, 10, true);

					} else {
						printOptions();
					}
				} else if (args.length == 2) {
					if (checkPath(args[0])) {
						if (checkEC(args[1])) {
							CmdController cmd = new CmdController(args);
						} else {
							System.out.println("1");
							System.out.println("Wrong input");
							System.out.println("check input/output-path");
							System.out.println("inputPath: " + args[0]);
							System.out.println("outputPath: " + args[1]);
							printOptions();
						}
					}
				} else if (args.length == 3) {
					if ((checkPath(args[0])) && (checkPath(args[1]))) {
						CmdController cmd;
						if (checkOptions(args[2])) {
							cmd = new CmdController(args);
						} else {
							System.out.println("Wrong option input");
							System.out.println("option: " + args[2]);
							printOptions();
						}
					} else if ((checkPath(args[0]))
							&& (checkEC(args[1]) && checkEC(args[2]))) {
						CmdController cmd = new CmdController(args);
					} else if ((checkPath(args[0])) && (!checkPath(args[1]))
							&& (!checkEC(args[1]))) {
						if (args[1].endsWith("/")) {
							File dir = new File(args[1]);
							dir.mkdir();
							CmdController cmd;
							if (checkOptions(args[2]) && (checkPath(args[1]))) {
								cmd = new CmdController(args);
							} else {
								System.out.println("Wrong option input");
								System.out.println("option: " + args[2]);
								printOptions();
							}
						} else if (args[1].contentEquals("seq")
								&& checkEC(args[2])) {
							CmdController cmd;
							cmd = new CmdController(args);
						} else if (args[1].contentEquals("seqall")
								&& checkEC(args[2])) {
							CmdController cmd;
							cmd = new CmdController(args);
						}else {
							//System.out.println("1");
							System.out.println("Wrong input");
							System.out.println("check input/output-path");
							System.out.println("inputPath: " + args[0]);
							System.out.println("outputPath: " + args[1]);
							printOptions();
						}
					} else {
						//System.out.println("2");
						System.out.println("Wrong input");
						System.out.println("check input/output-path");
						System.out.println("inputPath: " + args[0]);
						System.out.println("outputPath: " + args[1]);
						printOptions();
					}
				} else if (args.length == 4) {
					if ((checkPath(args[0])) && (checkPath(args[1]))) {
						CmdController cmd;
						if (checkOptions(args[2]) && (checkEC(args[3])||
								((args[2].contentEquals("pvalue")||args[2].contentEquals("eclist"))&&checkNum(args[3])))) {
							cmd = new CmdController(args);
						} else {
							System.out.println("Wrong option or ec input");
							System.out.println("option: " + args[2]);
							System.out.println("ec: " + args[3]);
							printOptions();
						}
					} else if ((checkPath(args[0])) && (!checkPath(args[1]))) {
						CmdController cmd;
						if (args[1].endsWith("/")) {
							File dir = new File(args[1]);
							dir.mkdir();
							if (checkOptions(args[2]) && (checkPath(args[1]))) {
								cmd = new CmdController(args);
							} else {
								System.out.println("Wrong option input");
								System.out.println("option: " + args[2]);
								printOptions();
							}
						} else if (checkEC(args[1]) && checkEC(args[2])
								&& checkEC(args[3])) {
							cmd = new CmdController(args);
						} else if (args[1].contentEquals("seq")
								&& checkEC(args[2]) && checkEC(args[3])) {
							cmd = new CmdController(args);
						} else {
							//System.out.println("1");
							System.out.println("Wrong input");
							System.out.println("check input/output-path");
							System.out.println("inputPath: " + args[0]);
							System.out.println("outputPath: " + args[1]);
							printOptions();
						}
					} else {
						//System.out.println("2");
						System.out.println("Wrong input");
						System.out.println("check input/output-path");
						System.out.println("inputPath: " + args[0]);
						System.out.println("outputPath: " + args[1]);
						printOptions();
					}
				} else if (args.length > 4) {
					if ((checkPath(args[0])) && (checkPath(args[1]))) {
						CmdController cmd;
						if (checkOptions(args[2])) {
							boolean allECs = true;
							for (int i = 3; i < args.length; i++) {
								if (!checkEC(args[i])) {
									allECs = false;
									break;
								}
							}
							if (allECs) {
								cmd = new CmdController(args);
							} else {
								System.out.println("Wrong option or ec input");
								System.out.println("option: " + args[2]);
								System.out.println("ec: " + args[3]);
								printOptions();
							}
						} else {
							System.out.println("Wrong option or ec input");
							System.out.println("option: " + args[2]);
							System.out.println("ec: " + args[3]);
							printOptions();
						}
					} else if ((checkPath(args[0])) && (!checkPath(args[1]))) {
						if (checkEC(args[1])) {
							CmdController cmd;
							boolean allECs = true;
							for (int i = 3; i < args.length; i++) {
								if (!checkEC(args[i])) {
									allECs = false;
								}
							}
							if (allECs) {
								cmd = new CmdController(args);
							} else {
								System.out.println("Wrong input");
								System.out.println("check input/output-path");
								System.out.println("inputPath: " + args[0]);
								System.out.println("outputPath: " + args[1]);
								printOptions();
							}
						} else if (args[1].contentEquals("seq")) {
							CmdController cmd;
							boolean allECs = true;
							for (int i = 2; i < args.length; i++) {
								if (!checkEC(args[i])) {
									allECs = false;
								}
							}
							if (allECs) {
								cmd = new CmdController(args);
							} else {
								System.out.println("Wrong input");
								System.out.println("check input/output-path");
								System.out.println("inputPath: " + args[0]);
								System.out.println("outputPath: " + args[1]);
								printOptions();
							}
						} else {
							//System.out.println("1");
							System.out.println("Wrong input");
							System.out.println("check input/output-path");
							System.out.println("inputPath: " + args[0]);
							System.out.println("outputPath: " + args[1]);
							printOptions();
						}
					} else {
						//System.out.println("2");
						System.out.println("Wrong input");
						System.out.println("check input/output-path");
						System.out.println("inputPath: " + args[0]);
						System.out.println("outputPath: " + args[1]);
						printOptions();
					}
				} else {
					printOptions();
				}
			} else {
				System.out.println("no args");
				run();
			}
		} else {
			System.out.println("null args");
			run();
		}
	}

	public static void run() { // runs gui fromp
		newFrame = new NewFrompFrame();
	}

	private static boolean checkPath(String path) { // checks that the file given exists
		File f = new File(path);
		return f.exists();
	}

	private static boolean checkOptions(String options) { // ensures that the option the user has selected is allowed
		boolean ret = false;
		if (options.contentEquals("p")) {
			ret = true;
		}
		if (options.contentEquals("s")) {
			ret = true;
		}
		if (options.contentEquals("m")) {
			ret = true;
		}
		if (options.contentEquals("e")) {
			ret = true;
		}
		if (options.contentEquals("f")) {
			ret = true;
		}
		if (options.contentEquals("a")) {
			ret = true;
		}
		if (options.contentEquals("am")) {
			ret = true;
		}
		if (options.contentEquals("op")) {
			ret = true;
		}
		if (options.contentEquals("up")) {
			ret = true;
		}
		if (options.contentEquals("ec")) {
			ret = true;
		}
		if (options.contentEquals("seq")) {
			ret = true;
		}
		if(options.contentEquals("seqall")){
			ret = true;
		}
		if(options.contentEquals("lca")){
			ret = true;
		}
		if(options.contentEquals("eclist")){
			ret = true;
		}
		if(options.contentEquals("pvalue")){
			ret = true;
		}
		return ret;
	}

	private static boolean checkEC(String options) {// checks that the EC is complete, ie is an ec number
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
	
	private static boolean checkNum(String options){
		try{
			Integer.parseInt(options);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	private static void printOptions() { // Prints out the options for this program to the cmdline
		if (arg1 != "h") {
			System.out.println("The arguements used are invalid!!");
			System.out.println("The correct arguements are:");
		}
		System.out.println("../java -jar FROMP.jar h for help");
		System.out.println("../java -jar FROMP.jar d for the pathwaydesigner");
		System.out.println();
		System.out
				.println("../java -jar FROMP.jar 'inputPath' 'outputPath' 'option'");
		System.out.println("Options are:");
		System.out.println("'P' for the pathway pictures");
		System.out.println("'s' for the pathway-score-matrix");
		System.out.println("'m' for the pathway-activity-matrix");
		System.out.println("'e' for the EC-activity-matrix");
		System.out.println("'f' to export the project as a .frp file");
		System.out.println("Exported .frp files will be saved in ~/projects");
		System.out.println();
		System.out
				.println("'ec' to export a list of sequence IDs from a file of ec numbers from your input file");
		System.out
				.println("To include sequence IDs without a file of ec numbers, add the ec numbers");
		System.out
				.println("who's sequence ID you are interested in to your command");
		System.out.println("Sequence ID's will be stored in ~/RepSeqIDs");
		System.out.println("");
		System.out
				.println("'seq' to export a file of sequences from a .frp file which has the sequence files related to the samples");
		System.out.println("Sequences will be stored in ~/Sequences");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' seq");
		System.out
				.println("'seqall' to export a file of sequences from a .frp file which has the sequence files related to the samples into one file per EC number");
		System.out.println("Sequences will be stored in ~/Sequences");
		System.out.println("Syntax: java -jar FROMP.jar 'inputPath' seqall");
		System.out.println("");
		System.out.println("'a' all options");
		System.out
				.println("'am' for pathway-score-matrix, pathway-activity-matrix, EC-activity-matrix, and to export as a .frp file");
		System.out.println("'op' only multisample pictures");
		System.out.println("'up' only userpathway multisample pictures");
		System.out.println("");
		System.out.println("");
		System.out
				.println("blanks in the input-path or output-path will lead to errors");
		System.out.println("to solve use braces: (\"inputPath\")");
	}
}
