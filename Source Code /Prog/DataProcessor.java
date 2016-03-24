package Prog;

import Objects.ConvertStat;
import Objects.EcNr;
import Objects.EcPosAndSize;
import Objects.EcSampleStats;
import Objects.EcWithPathway;
import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import Panes.HelpFrame;
import Panes.Loadingframe;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.*;

/**
 * THIS IS THE MOST IMPORTANT PART OF FROMP Now that I have you attention, let
 * me explain. The data processor is what basically does all of the parsing of
 * the raw input files the converting of pfams,and everything important for the
 * data. Everything else is a helper class for this: storing, and displaying
 * data, to the users. This class does all of the computation that actually
 * makes the data usable from the raw input.
 * 
 * @author Jennifer Terpstra, Kevan Lynch
 */
public class DataProcessor {
	Project activeProj_;
	StringReader reader;
	String workpath_;
	String separator_;
	//These lines are the conversion charts found in /list
	
	static final String listPath = StartFromp1.FolderPath+"list" + File.separator + "ec.list";
	static final String pathwayList = StartFromp1.FolderPath+"list" + File.separator + "pathway.list";
	static final String ecNamesPath = StartFromp1.FolderPath+"list" + File.separator + "ec2go.txt"; 
	static final String rnListPath = StartFromp1.FolderPath+"list" + File.separator + "rn.list"; 
	static final String mapTitleList = StartFromp1.FolderPath+"list" + File.separator + "map_title.tab"; //
	static final String pfamToRnToEcPath_ = StartFromp1.FolderPath+"list" + File.separator + "pfam2Ec2Rn.txt"; //
	static final String interproToGOPath_ = StartFromp1.FolderPath+"list" + File.separator + "interpro2GO.txt"; //
	static final String interproToECPath_ = StartFromp1.FolderPath+"list" + File.separator + "interPro_kegg.tsv";
	static final String uni2ECPath_ = StartFromp1.FolderPath+"list" + File.separator + "uniref2ec.txt";
	/* Variables to store the starting Strings of Pframs, ECs
	 * Rns and interpros
	 */
	static final String EC = "EC"; 
	static final String PF = "Pf"; 
	static final String RN = "Rn"; 
	static final String IPR = "IPR"; 
	
	XmlParser parser; // An XML parser, Prog.XMLParser
	int offCounter = 0; 
	int counter = 0; 
	int nrOfP = 0; 
	public static boolean newBaseData = true; 
	public static boolean newUserData = true; 
	Loadingframe lFrame_; // Loading Frame to let the user know when things are happening
	boolean chaining = true; // Boolean to indicate whether or not in chaining mode
	int numOfConvertedPfam = 0; // Conversion statistics for the Pfam, EC, and InterPro conversions
	int numOfMissedpfams = 0; 
	int numOfPFams = 0; 
	int numOfPfamsToGo = 0; 
	int numOfGOToRn = 0; 
	int numOfGoToEc = 0; 
	int unmatchedIndex; 
	int maxEcInP = 0; 
	int numIPR = 0; 
	int numConvertedIPR = 0; 
	int numCompleteIPR = 0; 
	int numMappedIPR = 0; 
	BufferedReader ecList; // Buffered Reader for reading the conversion files
	BufferedReader rnList; 
	BufferedReader nameList; 
	BufferedReader ecToGoTxt_; 
	BufferedReader pfamToRnToEc_; 
	BufferedReader interproToGOTxt_;
	BufferedReader interproToECTxt_; 
	BufferedReader uniToEcTxt_;
	PngBuilder build; // PngBuilder to draw the graphics
	Color sysCol_; 
	public static ArrayList<PathwayWithEc> pathwayList_; // Array list containing the pathways
	private static ArrayList<PathwayWithEc> newUserPathList_; // Array list containing the user pathways
	public static ArrayList<EcWithPathway> ecList_; // Array list containg the ECs
	boolean reduce = false; 
	ArrayList<String> numEcs = new ArrayList<String>(); // Arraylists to facilitate conversion statistics
	ArrayList<String> numPfams = new ArrayList<String>(); 
	ArrayList<String> totalnumPfams = new ArrayList<String>(); 
	//Hash of IPR -> EC conversion															
	Hashtable<String, ArrayList<String>> IPRToECHash = new Hashtable<String, ArrayList<String>>(); 
	//Hash of IPR -> GO conversion
	Hashtable<String, ArrayList<String>> IPRToGOHash = new Hashtable<String, ArrayList<String>>();
	//Hash of GO -> EC conversion
	Hashtable<String, ArrayList<String>> GOToECHash = new Hashtable<String, ArrayList<String>>();
	//Hash of UNI -> GO conversion
	Hashtable<String, ArrayList<String>> UniToECHash = new Hashtable<String, ArrayList<String>>();

	public DataProcessor(Project actProj) {// Builds the data processor object for the active project
		
		this.activeProj_ = actProj;

		newUserData = true;
		newBaseData = true;

		this.reader = new StringReader();
		this.separator_ = File.separator;
		this.parser = new XmlParser();
		this.lFrame_ = new Loadingframe();

		newUserPathList_ = new ArrayList<PathwayWithEc>();
		pathwayList_ = null;

		prepData(); // Prepares everything for the data processor
	}

	public DataProcessor() {// Builds a shell of a data processor, not connected to
		newUserData = true;
		newBaseData = true;
		this.reader = new StringReader();
		this.separator_ = File.separator;
		this.parser = new XmlParser();
	}

	/**
	 * Prepares the data processor, if the base data or the user data is new.
	 * For example, the DataProcessor was just called resulting in it preparing
	 * the pathlist and the ec list
	 *
	 * @author Jennifer Terpstra, Kevan Lynch
	 */
	public void prepData() {
		if (newBaseData) {
			System.err.println("new BaseData");
			prepPathList();
			prepEcList();
			newBaseData = false;
		}
		if (newUserData) {
			System.err.println("new UserData");
			prepUserPathList();
			prepUserEc();
			newUserData = false;
		}
		computeWeights(); 
		transferWeightToPwl(); // Transfers the weights to a pathway list
		calcChainsForProg(); // Calculates the longest chain between EcNrs
		if (this.lFrame_ != null) {
			Loadingframe.close();
		}
	}

	public void processProject() { // prepares the project object
		this.lFrame_ = new Loadingframe();
		if ((newBaseData) || (newUserData)) {
			prepData();
		}
		this.lFrame_ = new Loadingframe();
		allEcVsPathway();
		this.lFrame_ = new Loadingframe();
		removeEcPfamDoubles();
		this.lFrame_ = new Loadingframe();
		if (Project.randMode_) {
			removeRandomEc();
		}
		this.lFrame_ = new Loadingframe();
		transferEcToPath();

		this.lFrame_ = new Loadingframe();
		prepOverall();

		this.lFrame_ = new Loadingframe();
		calcAllChainsforallSamples();

		this.lFrame_ = new Loadingframe();
		getAllscores(0);
		if (this.lFrame_ != null) {
			Loadingframe.close();
		}
	}

	/**
	 * Retrieves ec/pfam and sequence ids from HMMER output files.
	 *
	 * @param line
	 *            Read in lines from the HMMER files
	 * @return String array of EC name, number of EC, pfram or ec and sequence
	 *         id
	 *
	 * @author Jennifer Terpstra, Kevan Lynch
	 */
	public String[] getEnzFromRawSample(String line) {
		if (line.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")) {
			return getEnzFromInterPro(line);
		}
		if (line.matches(".*UniRef90_.*")){
			System.out.println("Raw Uni");
			return getEnzFromUni(line);
		}
		String[] ret = new String[4];

		ret[0] = "X"; // Ec name
		ret[1] = "1"; // Number of this ec with this sequence id
		ret[2] = "X"; // Whether or not it is a pf or if it is an ec
		ret[3] = "X"; // Sequence ID
		if (line.contains("_")) {
			String tmp = line.substring(0, line.indexOf("_"));
			if (isEc(tmp)) {
				String repSeq = line.substring(line.indexOf(" -") + 1);
				repSeq = repSeq.substring(0, repSeq.indexOf(" -"));
				if (repSeq.contains("/")) {
					repSeq = repSeq.substring(0, repSeq.indexOf("/"));
				}
				while ((repSeq.startsWith(" ")) || (repSeq.startsWith("-"))) {
					repSeq = repSeq.substring(1);
				}
				ret[0] = tmp;
				ret[2] = "EC";
				ret[3] = repSeq;
				if (!numEcs.contains(ret[0])) {// adds to the count of total ecs for statistics
					numEcs.add(ret[0]);
				}
			} else {
				tmp = findPfamAndRepSeqInRaw(line);
				if (!tmp.isEmpty()) {
					ret[0] = tmp.substring(0, tmp.indexOf("-"));
					ret[2] = "Pf";
					String repSeq = tmp.substring(tmp.indexOf("-") + 1);
					if (repSeq.contains("/")) {
						repSeq = repSeq.substring(0, repSeq.indexOf("/"));
					}
					ret[3] = repSeq;

					if (!totalnumPfams.contains(ret[0])) {// adds to the count of total pfams
						totalnumPfams.add(ret[0]);
					}
					String[] nump = ret.clone();
					ArrayList<String[]> nump2;
					nump2 = convertPfam(nump);
					for (int i = 0; i < nump2.size(); i++) {
						//adds to the count of total converted pfams
						if (!numPfams.contains(nump2.get(i)[0])) {
							numPfams.add(nump2.get(i)[0]);
							System.out.println(nump2.get(i)[0]);
						}
					}
				}
			}
		}
		if ((ret[0] == "X") && (line.startsWith("PF"))) {
			ret[0] = line.substring(0, line.indexOf("."));
			if (isPfambool(ret[0])) {
				ret[2] = "Pf";
				String tmp = line.substring(line.indexOf(" ") + 1);
				tmp = tmp.substring(0, tmp.indexOf(" -"));
				ret[3] = tmp;

				if (!totalnumPfams.contains(ret[0])) { // Adds to the count of total pfams
					totalnumPfams.add(ret[0]);
				}
				String[] nump = ret.clone();
				ArrayList<String[]> nump2;
				nump2 = convertPfam(nump);
				for (int i = 0; i < nump2.size(); i++) {
					//Adds to the count of total converted pfams
					if (!numPfams.contains(nump2.get(i)[0])) { 
						numPfams.add(nump2.get(i)[0]);
						System.out.println(nump2.get(i)[0]);
					}
				}
			} else if ((ret[0] == "X") && (line.startsWith("IPR"))) {
				ret[0] = line.substring(0, line.indexOf("."));
				if (isInterProBool(ret[0])) {
					ret[2] = "IPR";
					String tmp = line.substring(line.indexOf(" "));
					tmp = tmp.substring(line.indexOf(" -"));
					ret[3] = tmp;
				}
			} else {
				ret[0] = "X";
			}
		}
		if (ret[0] == "X") {
			String tmp;
			try {
				tmp = line.substring(line.indexOf(" "));
				while (tmp.startsWith(" ")) {
					tmp = tmp.substring(1);
				}
				ret = getEnzFromRawSample(tmp);
			} 
			catch (StringIndexOutOfBoundsException e) {
				return ret;

			}
			if (tmp != null) {
				while (tmp.startsWith(" ")) {
					tmp = tmp.substring(1);
				}
			}
			if (tmp != null) {
				ret = getEnzFromRawSample(tmp);
			}
		}
		return ret;
	}
	/*Retrieves the interpro and sequence ids from Interpro output files*/
	public String[] getEnzFromInterPro(String line) { 

		if (!line.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")) {
			return null;
		}
		String seperator = ",";
		String[] ret = new String[4];

		ret[0] = "X"; // IPR name
		ret[1] = "1"; // Number of this ipr with this sequence id
		ret[2] = "X"; // Whether or not it is an ipr
		ret[3] = "X"; // Sequence id
		//added !line.contains("/t") for a strange index exception was occuring without
		if (line.contains(seperator) && !line.contains("\t")) {
			String interpro = findInterProInRaw(line);
			if (interpro != null) {
				Project.amountOfIPRs += 1;
				ret[0] = interpro;
				ret[2] = "IPR";
			}
			String tmp = line.substring(line.indexOf(seperator) + 1);
			if (tmp.contains(seperator)) {
				ret[1] = tmp.substring(0, tmp.indexOf(seperator));
				ret[3] = tmp.substring(line.indexOf(seperator) + 1);
			} else if (tmp != null) {
				ret[1] = tmp;
			}
		} else {
			if (line.contains("\t")) {
				String repSeq = line.substring(0, line.indexOf("\t"));
				ret[3] = repSeq;
			}
			String interpro = findInterProInRaw(line);
			if (interpro != null) {
				Project.amountOfIPRs += 1;
				ret[0] = interpro;
				ret[2] = "IPR";
			} else {
				System.out.println("IPR save was unsuccessful");
			}
		}
		return ret;
	}
	
	public String[] getEnzFromUni(String line){

		if (!line.matches(".*UniRef90_.*")) {
			System.out.println("returned null");
			return null;
		}
		String seperator = ",";
		String[] ret = new String[4];
		ret[0] = "X"; // UniRef name
		ret[1] = "1"; // Number of this UniRef with this sequence id
		ret[2] = "X"; // Whether or not it is an UniRef
		ret[3] = "X"; // Sequence id
		if (line.contains("\t")) {
			String[] repSeq = line.split("\\t");
			ret[3] = repSeq[0];
			String uni = findUNIInRaw(line);
			if (uni != null) {
				Project.amountOfUNIs += 1;
				ret[0] = uni;
				ret[2] = "UniRef";
			} else {
				System.out.println("UniRef save was unsuccessful");
			}
		}
		return ret;
	}

	/**
	 * Finds the PFam and sequence ID in a line of raw data.
	 * 
	 * @param input
	 *            Line of user input containing their raw data
	 * 
	 * @author Jennifer Terpstra, Kevan Lynch
	 */
	private String findPfamAndRepSeqInRaw(String input) {
		String Pfam = "";
		String tmp = input;
		while (tmp.contains("PF")) {
			Pfam = tmp.substring(tmp.indexOf("PF"), tmp.indexOf("PF") + 7);
			String repSeq = tmp.substring(tmp.indexOf("PF") + 7);
			char firstChar = repSeq.charAt(0);
			while ((isNumber(firstChar)) || (firstChar == '.')
					|| (firstChar == ' ')) {
				repSeq = repSeq.substring(1);
				firstChar = repSeq.charAt(0);
			}
			if (isPfambool(Pfam)) {
				return Pfam + "-" + repSeq.substring(0, repSeq.indexOf("-"));
			}
			tmp = tmp.substring(tmp.indexOf("PF") + 7);
		}
		return "";
	}

	private String findInterProInRaw(String input) { // method to parse the interpro format
		String interpro = "";
		String tmp = input;
		while (tmp.contains("IPR")) {
			interpro = tmp.substring(tmp.indexOf("IPR"), tmp.indexOf("IPR") + 9);
			if (interpro.matches("IPR[0-9][0-9][0-9][0-9][0-9][0-9]")) {
				return interpro;
			} else {
				tmp = tmp.substring(tmp.indexOf("IPR") + 3);
			}
		}
		return null;
	}
	
	private String findUNIInRaw(String input) {
		String uni = "";
		String[] uniref;
		String tmp = input;
		while (tmp.contains("UniRef")) {
			uniref = tmp.split("\\t");
			uni = uniref[1];
			if (uni.matches(".*UniRef90_.*")) {
				return uni;
			}
		}
		return null;
		
	}

	/**
	 * Retrieves ec/pfam and sequence ids from the three column, two column, and
	 * matrix data files.
	 *
	 * @author Jennifer Terpstra, Kevan Lynch
	 */
	public String[] getEnzFromSample(String input) {
		if (input.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")) {
			return getEnzFromInterPro(input);
		}
		if (input.matches(".*UniRef90_.*")){
			return getEnzFromUni(input);
		}
		String seperator = "";
		String tmp = input;
		//adding input.contains("\t"), might have to remove
		if (input.contains(",") && !input.contains("\t")) {
			seperator = ",";
		}
		if ((seperator.isEmpty()) && (input.contains("_"))) {
			seperator = "_";
		}
		if ((seperator.isEmpty()) && (input.contains("\t"))) {
			seperator = "\t";
		}
		String[] ret = new String[4];
		ret[0] = "X";// ec name
		ret[1] = "1";// number of this ec with this sequence id
		ret[2] = "X";// whether or not it is a pf, ipr or if it is an ec
		ret[3] = "X";// sequence id
		if (input.isEmpty()) {
			return ret;
		}
		if (!seperator.isEmpty()) {
			if (input.contains(seperator)) {
				if ((input.length() - input.replace(seperator, "").length()) == 2
						|| (input.length() - input.replace(seperator, "")
								.length()) == 1)// this determines that the input is of the two or three column format
				{
					ret[0] = input.substring(0, input.indexOf(seperator));
					tmp = input.substring(input.indexOf(seperator) + 1);
					if (isEc(ret[0])) {
						ret[2] = "EC";
						if (!numEcs.contains(ret[0])) {// adds to the total number of ecs
							numEcs.add(ret[0]);
						}
					} else if (ret[0].contains(".")) {
						ret[0] = ret[0].substring(0, ret[0].indexOf("."));
					}
					String pfam = isPfam(ret[0]);
					if (pfam != null) {
						ret[0] = pfam;
						ret[2] = "Pf";
						if (!totalnumPfams.contains(ret[0])) {// adds to the total number of pfams
							totalnumPfams.add(ret[0]);
						}
						String[] nump = ret.clone();
						ArrayList<String[]> nump2;
						nump2 = convertPfam(nump);
						//adds to the total number of converted pfams
						for (int i = 0; i < nump2.size(); i++) {
							if (!numPfams.contains(nump2.get(i)[0])) {
								numPfams.add(nump2.get(i)[0]);
							}
						}
					}
					String interpro = isInterPro(ret[0]);
					if (interpro != null) {
						ret[0] = interpro;
						ret[2] = "IPR";
					}
					if (tmp.contains(seperator)) {
						ret[1] = tmp.substring(0, tmp.indexOf(seperator));
						tmp = tmp.substring(tmp.indexOf(seperator) + 1);
						if (!tmp.isEmpty()) {
							ret[3] = tmp;
						} else {
							return ret;
						}
					} else {
						ret[1] = tmp;

						return ret;
					}

				}
				else if ((input.length() - input.replace(seperator, "")
						.length()) > 3) {// Matrix format. Still nothing here to input the matrix format. Use the Load Ec-matrix button.
				}
			}
		} else if (isPfambool(input) || isEc(input) || isInterProBool(input)) {// one column format
			ret[0] = input;
			if (isEc(ret[0])) {
				ret[2] = "EC";
				if (!numEcs.contains(ret[0])) {// adds to the total number of ecs
					numEcs.add(ret[0]);
				}
			}
			String pfam = isPfam(ret[0]);
			if (pfam != null) {
				ret[0] = pfam;
				ret[2] = "Pf";
				if (!totalnumPfams.contains(ret[0])) {// adds to the total number of pfams
					totalnumPfams.add(ret[0]);
				}
				String[] nump = ret.clone();
				ArrayList<String[]> nump2;
				nump2 = convertPfam(nump);
				for (int i = 0; i < nump2.size(); i++) {// adds to the total number of converted pfams
					if (!numPfams.contains(nump2.get(i)[0])) {
						numPfams.add(nump2.get(i)[0]);
					}
				}
			}
			String interpro = isInterPro(ret[0]);
			if (interpro != null) {
				ret[0] = interpro;
				ret[1] = "1";
				ret[2] = "IPR";
			}
		} else {
			ret[0] = input;
			return ret;
		}
		return ret;
	}

	/**
	 * Assuming the user input string is pfam format then the method outputs the
	 * pfam, only without the PF at the begining.
	 * 
	 * @returns pFam parsed to have no "PF" or NULL if not pFam
	 * 
	 * @author Jennifer Terpstra, Kevan Lynch
	 */
	public String isPfam(String pfam) {
		String tmp = pfam;
		if (tmp.contains("PF")) {
			tmp = tmp.substring(tmp.indexOf("PF"));
			if (tmp.length() == 7) {
				if (isNumber(tmp.substring(2))) {
					return tmp;
				}
			} else if (tmp.length() >= 7) {
				tmp = tmp.substring(2);
				isPfam(tmp);
			}
		}
		return null;
	}
	
	/*If the input string is determine to be an interPro the method outputs the interPro. Else if returns null*/
	public String isInterPro(String interpro) { 
		String tmp = interpro;
		if (tmp.contains("IPR")) {
			tmp = tmp.substring(tmp.indexOf("IPR"));
			if (tmp.length() == 9) {
				if (isNumber(tmp.substring(3))) {
					return tmp;
				}
			} else if (tmp.length() >= 9) {
				tmp = tmp.substring(3);
				isInterPro(tmp);
			}
		}
		return null;
	}
	
	/*If the input string is deteremined to be UNI the method outputs the uni. Else returns null */
	public String isUNI(String uni){
		String tmp = uni;
		String[] uniref;
		if (tmp.contains("UniRef90_")) {
			uniref = tmp.split("\\t");
			tmp = uniref[1];
			if(tmp.contains("UniRef90_")){
				return tmp;
			}
		}
		return null;
	}
	
	// returns a boolean variable which is the awnser to whether or not the string is a pfam
	public boolean isPfambool(String pfam) {
		String tmp = pfam;
		if ((tmp.startsWith("PF")) && (tmp.length() == 7)
				&& (isNumber(tmp.substring(2)))) {
			return true;
		}
		return false;
	}
	//returns a boolean variable which is the answer to whether or not the string is an interPro
	public boolean isInterProBool(String interpro) {
		String tmp = interpro;
		if ((tmp.startsWith("IPR")) && (tmp.length() == 9)
				&& (isNumber(tmp.substring(3)))) {
			return true;
		}
		return false;
	}
	//returns a boolean variable which is the answer to whether or not the string is UniRef
	public boolean isUNIBool(String uni){
		String tmp = uni;
		if ((tmp.matches(".*UniRef90_.*"))) {
			return true;
		}
		return false;
	}
	//returns a boolean stating whether or not the input string is a complete EC
	public boolean isEc(String ec) {
		if (ec == null) {
			return false;
		}
		if (!ec.contains(".")) {
			return false;
		}
		String ecPart = "";
		String ecRest = ec;
		ecPart = ecRest.substring(0, ecRest.indexOf("."));
		ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
		if (!isNumber(ecPart)) {
			return false;
		}
		if (!ecRest.contains(".")) {
			return false;
		}
		ecPart = ecRest.substring(0, ecRest.indexOf("."));
		ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
		if (!isNumber(ecPart)) {
			return false;
		}
		if (!ecRest.contains(".")) {
			return false;
		}
		ecPart = ecRest.substring(0, ecRest.indexOf("."));
		ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
		if (!isNumber(ecPart)) {
			System.out.println("5 " + ec);
			return false;
		}
		if (isNumber(ecRest)) {
			return true;
		}
		System.out.println("1" + ec);
		return false;
	}

	public boolean isNumber(String in) {// Returns whether or not a string is a number
		try {
			int num = Integer.valueOf(in).intValue();
		} catch (Exception e) {
			int num;
			return false;
		}
		int num;
		return true;
	}

	public boolean isNumber(char c) {// Returns whether or not a char is a number
		if ((c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4')
				|| (c == '5') || (c == '6') || (c == '7') || (c == '8')
				|| (c == '9')) {
			return true;
		}
		return false;
	}

	public boolean isDot(char c) {
		if (c == '.') {
			return true;
		}
		return false;
	}

	public String getEcNrFromList(String input) {
		String output = "";
		if (input.contains("ec:")) {
			output = input.substring(input.lastIndexOf("ec:") + 3,
					input.length() - 1);
		}
		return output;
	}

	public String getPathwayFromList(String input) {
		String output = "";
		if (input.contains("path:")) {
			output = input.substring(input.indexOf(":") + 1,
					input.indexOf(":") + 8);
		}
		return output;
	}

	private void addUserNewPathsToSample(Sample sample) {
		if (newUserPathList_.isEmpty()) {
			return;
		}
		for (int pathCnt = 0; pathCnt < newUserPathList_.size(); pathCnt++) {
			sample.integratePathway((PathwayWithEc) newUserPathList_
					.get(pathCnt));
		}
	}

	/**
	 * Does the parsing of the input files as well as the printing out of
	 * statistics at the end. Each file is parsed line by line and checked for
	 * formating at teach line in order to accommodate for mixed format files.
	 * If the file is found to be of the interpro format, however, then this
	 * method will call another method to parse the file so that efficiency
	 * isn't lost checking the file type at each line. This method then
	 * continues.
	 * 
	 * @see ParseInterpro
	 * @author Kevan Lynch, Jennifer Terpstra
	 */
	public void allEcVsPathway() {
		System.out.println("allEcVsPathway");
		String zeile = "";
		EcNr ecNr = null;

		int counter = 0;
		this.lFrame_.bigStep("all EC Vs Pathway");
		final long startTime = System.currentTimeMillis();
		outerloop: for (int i = 0; i < Project.samples_.size(); i++) {// For each of the samples in the project														
			this.lFrame_.bigStep(((Sample) Project.samples_.get(i)).name_);

			Sample sample = (Sample) Project.samples_.get(i);
			ArrayList<Sample> sampleArray = new ArrayList(); // Needed for the matrix input format

			if (sample.valuesSet) {// If the sample has already had its values set then simply add paths to the samples
				addUserNewPathsToSample(sample);
			}
			else if (sample.imported){ // if the sample was imported then fill the samples ECs
				fillSampleEcs((Sample) Project.samples_.get(i), i);
				// If the sample name is empty than the sample hasn't been build yet, so build the sample from the sample file
			} 
			else if (!sample.name_.isEmpty()) {
				String tmp = ((Sample) Project.samples_.get(i)).fullPath_;
				sample.sample_ = this.reader.readTxt(tmp);
				sample.clearPaths();
				Project.legitSamples.add(Boolean.valueOf(false));
				try {
					//Parses through the files to build the "Sample attributes"
					while ((zeile = sample.sample_.readLine()) != null)
					{
						
						if (zeile.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")|| zeile.startsWith(">")) {
							i = ParseInterpro(i);
							break;
						}
						/*important for interpro input formats where several samples are in the same file
						 * each starting off with a line containing the sample name starting with ">"
						 */
						if (zeile.startsWith(">")) {
							if (Project.samples_.get(i).ecs_.isEmpty()) {
								Project.samples_.get(i).name_ = zeile.substring(zeile.indexOf(">") + 1);
								continue;
							} else {
								Color tmpColor = new Color((float) Math.random(),(float) Math.random(),(float) Math.random());
								Sample tmpSample = new Sample(zeile.substring(zeile.indexOf(">") + 1),sample.fullPath_, tmpColor);
								tmpSample.legitSample = true;
								tmpSample.inUse = true;
								Project.samples_.add(i + 1, tmpSample);
								Project.legitSamples.add(i + 1, true);
								i++;
								continue;
							}
						}
						String[] newEnz = getEnzFromSample(zeile);
						if (!enzReadCorrectly(newEnz)) {
							newEnz = getEnzFromRawSample(zeile);
						}
						if (!enzReadCorrectly(newEnz)) {
							Debug.addnoEnzymeLine(sample.name_ + " " + zeile);
						} 
						else {
							if (newEnz[2] == "EC") {// if the sequence was already an EC
								Debug.addEc(sample.name_ + " id: " + newEnz[0] + " repseq: " + newEnz[3] + " amount: " + newEnz[1]);
							} else {
								Debug.addPf(sample.name_ + " id: " + newEnz[0] + " repseq: " + newEnz[3] + " amount: " + newEnz[1]);
							}
							counter++;
							this.lFrame_.step(newEnz[0] + ": " + newEnz[1]);
							this.lFrame_.updateCounter(counter);
							
							//If the sequence was taken in as a pfam
							if (newEnz[2].equalsIgnoreCase("PF")){
								if (!newEnz[1].isEmpty()) {
									Project.amountOfPfs += Integer.valueOf(newEnz[1]).intValue();
								}
								ArrayList<String[]> enzL = convertPfam(newEnz);
								for (int cnt = 0; cnt < enzL.size(); cnt++) {
									newEnz = (String[]) enzL.get(cnt);
									if (!newEnz[0].isEmpty()) {
										ecNr = new EcNr(newEnz);

										EcWithPathway ecWP = null;
										if (!ecNr.type_.contentEquals("X")) {
											if ((ecNr.type_.contentEquals("EC"))
													&& (isEc(ecNr.name_))) {
												Project.samples_.get(i).addConvStats(new ConvertStat(
														newEnz[3],ecNr.name_,0,ecNr.amount_,0));
												ecWP = findEcWPath(ecNr);
												this.lFrame_.step("converted " 
														+ newEnz[0]);
											}
											if (ecWP != null) {
												if (!ecNr.isCompleteEc()) {
													ecNr.incomplete = true;
												}
												if (isEc(ecNr.name_)) {
													Project.samples_.get(i).addEc(new EcWithPathway(ecWP, ecNr));
													Project.legitSamples.remove(i);
													Project.legitSamples.add(i,Boolean.valueOf(true));
												}
											} else {
												if (!ecNr.isCompleteEc()) {
													ecNr.incomplete = true;
												}
												ecNr.unmapped = true;
												EcWithPathway unmatched = new EcWithPathway(ecNr);
												unmatched.addPathway((Pathway) getPathwayList_().get(this.unmatchedIndex));
												Project.samples_.get(i).addEc(unmatched);
											}
										}
									}
								}
							}
							//testing if works!
							else if(newEnz[2].equalsIgnoreCase("uniref")){
								if (!newEnz[1].isEmpty()) {
									Project.amountOfUNIs += Integer.valueOf(newEnz[1]).intValue();
								}
								ArrayList<String[]> enzL = convertUni(newEnz);
								System.out.println(enzL);
								for (int cnt = 0; cnt < enzL.size(); cnt++) {
									newEnz = (String[]) enzL.get(cnt);
									if (!newEnz[0].isEmpty()) {
										ecNr = new EcNr(newEnz);

										EcWithPathway ecWP = null;
										if (!ecNr.type_.contentEquals("X")) {
											if ((ecNr.type_.contentEquals("EC")) && (isEc(ecNr.name_))) {
												Project.samples_.get(i).addConvStats(new ConvertStat(newEnz[3],ecNr.name_,0,ecNr.amount_,0));
												ecWP = findEcWPath(ecNr);
												this.lFrame_.step("converted " + newEnz[0]);
											}
											if (ecWP != null) {
												if (!ecNr.isCompleteEc()) {
													ecNr.incomplete = true;
												}
												if (isEc(ecNr.name_)) {
													Project.samples_.get(i).addEc(new EcWithPathway(ecWP, ecNr));
													Project.legitSamples.remove(i);
													Project.legitSamples.add(i,Boolean.valueOf(true));
												}
											} else {
												if (!ecNr.isCompleteEc()) {
													ecNr.incomplete = true;
												}
												ecNr.unmapped = true;
												EcWithPathway unmatched = new EcWithPathway(ecNr);
												unmatched.addPathway((Pathway) getPathwayList_().get(this.unmatchedIndex));
												Project.samples_.get(i).addEc(unmatched);
											}
										}
									}
								}
								
							}
							else if (!newEnz[0].isEmpty()) {
								ecNr = new EcNr(newEnz);
								if (ecNr.couldBeEc()) {
									if (!ecNr.isCompleteEc()) {
										ecNr.incomplete = true;
									}
									Project.samples_.get(i).addConvStats(new ConvertStat(newEnz[3],ecNr.name_, ecNr.amount_,0, 0));
									EcWithPathway ecWP = findEcWPath(ecNr);
									if (ecWP != null) {
										Project.samples_.get(i).addEc(new EcWithPathway(ecWP, ecNr));
										Project.legitSamples.remove(i);
										Project.legitSamples.add(i,Boolean.valueOf(true));
									} else {
										if (!ecNr.isCompleteEc()) {
											ecNr.incomplete = true;
										}
										ecNr.unmapped = true;
										EcWithPathway unmatched = new EcWithPathway(ecNr);
										unmatched.addPathway((Pathway) getPathwayList_().get(this.unmatchedIndex));
										Project.samples_.get(i).addEc(unmatched);
									}
								}
							}
						}
					}
					sample.sample_.close();
				} catch (IOException e) {
					openWarning("Error", "File: " + tmp + " not found");
					e.printStackTrace();
				}
				Debug.writeOutAll("DebugLists.txt");
				System.out.println("finished allecvsp");
			}
			//check status
			
		}
		final long endTime = System.currentTimeMillis();
		System.out.println("Total execution time(milliseconds): " + (endTime - startTime));

		ArrayList<String> comptotecs = new ArrayList<String>();
		ArrayList<String> allecs = new ArrayList<String>();
		ArrayList<String> ecmapped = new ArrayList<String>();
		ArrayList<String> pfammapped = new ArrayList<String>();
		String testStr1;
		String testStr2;
		String testStr3;
		for (int i = 0; i < Project.samples_.size(); i++) {// adds to the number of total complete ecs
			for (int k = 0; k < Project.samples_.get(i).ecs_.size(); k++) {
				if (!comptotecs.contains(Project.samples_.get(i).ecs_.get(k).name_)) {
					if (Project.samples_.get(i).ecs_.get(k).name_.matches("[0-9].*")) {
						if (Project.samples_.get(i).ecs_.get(k).name_.contains(".")) {
							testStr1 = Project.samples_.get(i).ecs_.get(k).name_.substring(Project.samples_.get(i).ecs_
											.get(k).name_.indexOf(".") + 1);
							if (testStr1.matches("[0-9].*")) {
								if (testStr1.contains(".")) {
									testStr2 = testStr1.substring(testStr1.indexOf(".") + 1);
									if (testStr2.matches("[0-9].*")) {
										if (testStr2.contains(".")) {
											testStr3 = testStr2.substring(testStr2.indexOf(".") + 1);
											if (testStr3.matches("[0-9]*")) {
												comptotecs.add(Project.samples_.get(i).ecs_.get(k).name_);
											}
										}
									}
								}
							}
						}
					}
				}
				/*adds to the total number of ecs so that it can later subtract the number of
				 * complete ecs to find the number of incomplete ecs
				 */
				if (!allecs.contains(Project.samples_.get(i).ecs_.get(k).name_)) {
					allecs.add(Project.samples_.get(i).ecs_.get(k).name_);
				}
				if (!ecmapped
						.contains(Project.samples_.get(i).ecs_.get(k).name_)
						&& numEcs.contains(Project.samples_.get(i).ecs_.get(k).name_)
						&& !Project.samples_.get(i).ecs_.get(k).unmapped) {
					ecmapped.add(Project.samples_.get(i).ecs_.get(k).name_); // adds to the number of mapped ecs
				}
				if (!pfammapped
						.contains(Project.samples_.get(i).ecs_.get(k).name_)
						&& numPfams.contains(Project.samples_.get(i).ecs_.get(k).name_)
						&& !Project.samples_.get(i).ecs_.get(k).unmapped) {
					pfammapped.add(Project.samples_.get(i).ecs_.get(k).name_);// adds to the number of mapped pfams							
				}
			}
		}
		int completepfams = 0;
		int completeecs = 0;
		for (int i = 0; i < numPfams.size(); i++) {
			if (numPfams.get(i).matches("[0-9].*")) {
				if (numPfams.get(i).contains(".")) {
					testStr1 = numPfams.get(i).substring(numPfams.get(i).indexOf(".") + 1);
					if (testStr1.matches("[0-9].*")) {
						if (testStr1.contains(".")) {
							testStr2 = testStr1.substring(testStr1.indexOf(".") + 1);
							if (testStr2.matches("[0-9].*")) {
								if (testStr2.contains(".")) {
									testStr3 = testStr2.substring(testStr2.indexOf(".") + 1);
									if (testStr3.matches("[0-9]*")) {
										completepfams++;// adds to the number of complete pfams
									}
								}
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < numEcs.size(); i++) {
			if (numEcs.get(i).matches("[0-9].*")) {
				if (numEcs.get(i).contains(".")) {
					testStr1 = numEcs.get(i).substring(numEcs.get(i).indexOf(".") + 1);
					if (testStr1.matches("[0-9].*")) {
						if (testStr1.contains(".")) {
							testStr2 = testStr1.substring(testStr1.indexOf(".") + 1);
							if (testStr2.matches("[0-9].*")) {
								if (testStr2.contains(".")) {
									testStr3 = testStr2.substring(testStr2.indexOf(".") + 1);
									if (testStr3.matches("[0-9]*")) {
										completeecs++;// adds to the number of complete ecs
									}
								}
							}
						}
					}
				}
			}
		}

		int incomptotecs = allecs.size() - comptotecs.size();
		/* adds a failsafe for whether or not the data was loaded from a project file, as
		 * the statistics are already saved there
		 */
		if (!Project.loaded) {
			Project.amountOfEcs = numEcs.size();
			Project.numOfCompleteEcs = completeecs;
			Project.numOfMappedEcs = ecmapped.size();
			Project.amountOfPfs = totalnumPfams.size();
			Project.numOfConvertedPFs = numPfams.size();
			Project.numOfConvPfsComplete = completepfams;
			Project.numOfConvPfsMapped = pfammapped.size();
		}
		// calls the Help Frame to make the Project Summary window
		String Text = "<html><body>Finished processing the samples" + "<br>"
				+ "<br>" + "Total complete ECs (including converted pfams):\t"
				+ comptotecs.size()
				+ "<br>"
				+ "Total incomplete ECs (including converted pfams):\t"
				+ incomptotecs
				+ "<br>"
				+ "ECs:\t"
				+ Project.amountOfEcs
				+ "<br>"
				+ "Complete ECs:\t"
				+ Project.numOfCompleteEcs
				+ "<br>"
				+ "Mapped ECs:\t"
				+ Project.numOfMappedEcs
				+ "<br>"
				+ "Pfams:\t"
				+ Project.amountOfPfs
				+ "<br>"
				+ "Converted Pfams:\t"
				+ Project.numOfConvertedPFs
				+ "<br>"
				+ "Complete converted Pfams:\t"
				+ Project.numOfConvPfsComplete
				+ "<br>"
				+ "Mapped converted Pfams:\t"
				+ Project.numOfConvPfsMapped
				+ "<br>"
				+ "Interpros:\t"
				+ Project.amountOfIPRs
				+ "<br>"
				+ "Converted Interpros:\t"
				+ Project.numOfConvertedIPRs
				+ "<br>"
				+ "Complete converted Interpros:\t"
				+ Project.numOfConvIPRsComplete
				+ "<br>"
				+ "Mapped converted Interpros:\t"
				+ Project.numOfConvIPRsMapped
				+ "<br><br>"
				+ "<br>"
				+ "Sample that seem to be valid: "
				+ "<br>";
		for (int i = 0; i < Project.samples_.size(); i++) {
			Text = Text + (i + 1) + ":" + Project.samples_.get(i).legitSample + " ";
			if ((i % 5 == 0) && (i != 0)) {
				Text = Text + "<br>";
			}
		}
		Text = Text + "<br></body></html>";
		//Commented out help frame as it is unnessary for general users. Useful for debugging
		//HelpFrame helpF = new HelpFrame(Text);
		System.out.println("---------------------------------------------------------------------------------------------");
		System.out.println("Finished processing the samples");
		System.out.println("Total icomplete ECs (including converted pfams): " + comptotecs.size());
		System.out.println("Total incomplete ECs (including converted pfams): " + incomptotecs);
		System.out.println("ECs: " + Project.amountOfEcs);
		System.out.println("Complete ECs: " + Project.numOfCompleteEcs);
		System.out.println("Mapped ECs: " + Project.numOfMappedEcs);
		System.out.println("Pfams: " + Project.amountOfPfs);
		System.out.println("Converted Pfams: " + Project.numOfConvertedPFs);
		System.out.println("Complete converted Pfams: " + Project.numOfConvPfsComplete);
		System.out.println("Mapped converted Pfams: " + Project.numOfConvPfsMapped);

		System.out.println("Sample that seem to be valid:");
		for (int i = 0; i < Project.samples_.size(); i++) {
			System.out.println("Sample: " + (i + 1) + ":"
					+ ((Sample) Project.samples_.get(i)).name_ + " "
					+ ((Sample) Project.samples_.get(i)).legitSample);
		}
		newUserPathList_ = new ArrayList();
	}

	/**
	 * Parses a file of the InterPro format and pulls out the samples,
	 * IPRs(which get converted) and sequence IDs.
	 * 
	 * @param count
	 *            Sample number
	 * @return converted Sample number
	 * @author Kevan Lynch, Jennifer Terpstra
	 */
	public int ParseInterpro(int count) {
		String zeile = "";
		String seqPath = "";
		System.out.println("Parse Interpro");
		boolean hasSeq = false;
		ArrayList<String> seqPaths = new ArrayList<String>();
		int i = count;
		for (i = count; i < Project.samples_.size(); i++) {
			Sample sample = Project.samples_.get(i);
			System.out.println(sample.name_+" is working");
			if(sample.getSequenceFile()!=""){
				hasSeq = true;
			}
			else{
				hasSeq = false;
			}
			StringReader reader2 = new StringReader();
			String tmp = ((Sample) Project.samples_.get(i)).fullPath_;
			if(hasSeq){
				String seqFilePath = ((Sample) Project.samples_.get(i)).getSequenceFile();
				BufferedReader seq = reader2.readTxt(seqFilePath);
				try {
					while((seqPath = seq.readLine()) !=null){
						seqPaths.add(seqPath);
					}
				} catch (IOException e1) {
					System.out.println("No sequence file exists");
				}
			}
			
			sample.sample_ = this.reader.readTxt(tmp);
			
			try {
				while ((zeile = sample.sample_.readLine()) != null) {
					/*Important for interpro input where several samples are in the same
					 * file each starting off with a line contaning the sample name
					 * starting with ">"
					 */
					if (zeile.startsWith(">")) {
						if (Project.samples_.get(i).ecs_.isEmpty()) {
							Project.samples_.get(i).name_ = zeile.substring(zeile.indexOf(">") + 1);
							if(hasSeq){
								Project.samples_.get(i).setSequenceFile(seqPaths.get(0));
							}
							continue;
						} else {
							Color tmpColor = new Color((float) Math.random(),
									(float) Math.random(),
									(float) Math.random());
							Sample tmpSample = new Sample(zeile.substring(zeile
									.indexOf(">") + 1), sample.fullPath_,
									tmpColor);
							if (hasSeq) {
								for (int k = 1; k < seqPaths.size(); k++) {
										tmpSample.setSequenceFile(seqPaths.get(k));
								}
							}
							tmpSample.legitSample = true;
							tmpSample.inUse = true;
							Project.samples_.add(i+1, tmpSample);
							Project.legitSamples.add(i+1, true);
							i++;
							continue;
						}
					}

					else if (zeile.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")) {

						String[] newEnz = getEnzFromInterPro(zeile);
						if (newEnz[2].equalsIgnoreCase("IPR")) {
							/*If you chance this to "enzL = convertInterproOld(newEnz) then it will
							 * change from direct to indirect mapping of Interpro reads
							 */
							ArrayList<String[]> enzL = convertInterpro(newEnz);
							for (int cnt = 0; cnt < enzL.size(); cnt++) {
								newEnz = (String[]) enzL.get(cnt);
								if (!newEnz[0].isEmpty()) {
									EcNr ecNr = new EcNr(newEnz);

									EcWithPathway ecWP = null;
									if (!ecNr.type_.contentEquals("X")) {
										if ((ecNr.type_.contentEquals("EC"))&& (isEc(ecNr.name_))) {
											Project.samples_.get(i).addConvStats(new ConvertStat(newEnz[3],ecNr.name_,0,ecNr.amount_,0));
											ecWP = findEcWPath(ecNr);
											this.lFrame_.step("converted " + newEnz[0]);

										}
										if (ecWP != null) {
											Project.numOfConvIPRsMapped += 1;
											if (!ecNr.isCompleteEc()) {
												ecNr.incomplete = true;
											}
											if (isEc(ecNr.name_)) {
												Project.numOfConvIPRsComplete += 1;
												Project.samples_.get(i).addEc(
														new EcWithPathway(ecWP,
																ecNr));
												//added to try to fix out of bounds exception!!
												if(Project.legitSamples.size()==1){
													Project.legitSamples.remove(0);
													Project.legitSamples.add(0,Boolean.valueOf(true));
												}
												//added -1 to attempt to fix error
												else if(Project.legitSamples.size()>1){
													Project.legitSamples.remove(i);
													Project.legitSamples.add(i,Boolean.valueOf(true));
												}
												else{
													Project.legitSamples.add(0,Boolean.valueOf(true));
												}
												
											}
										} else {
											if (!ecNr.isCompleteEc()) {
												ecNr.incomplete = true;
											}
											ecNr.unmapped = true;
											EcWithPathway unmatched = new EcWithPathway(
													ecNr);
											unmatched
													.addPathway((Pathway) getPathwayList_()
															.get(this.unmatchedIndex));
											Project.samples_.get(i).addEc(
													unmatched);

										}
									}
								}
							}
						}

					}
				}
			} catch (IOException e) {
				openWarning("Error", "File: " + tmp + " not found");
				e.printStackTrace();
			}
		}
		return i;
	}
	
	//returns true if element 0 of the array is either ec,ipr or pfam and the element 1 is an int
	public boolean enzReadCorrectly(String[] newEnz) {
		if (newEnz == null) {
			return false;
		}
		boolean ret = false;
		if ((isEc(newEnz[0])) || (isPfambool(newEnz[0]))|| (isInterProBool(newEnz[0])|| (isUNIBool(newEnz[0])))) {
			ret = true;
		} else {
			return false;
		}
		try {
			int num = Integer.valueOf(newEnz[1]).intValue();
		} catch (Exception e) {
			int num;
			ret = false;
		}
		return ret;
	}

	/**
	 * Does the computation of converting from pfam format into an EC
	 * 
	 * @param pfam
	 *            pfam sequence
	 * @return converted EC sequence
	 * 
	 * @author Jennifer Terpstra, Kevan Lynch
	 */
	public ArrayList<String[]> convertPfam(String[] pfam) {

		ArrayList<String[]> retList = new ArrayList();
		this.pfamToRnToEc_ = this.reader.readTxt(pfamToRnToEcPath_);//conversion file
		String zeile = "";
		String[] tmpNr = new String[4];
		tmpNr[3] = pfam[3];
		int pfamNr = Integer.valueOf(pfam[0].substring(2)).intValue();

		if (!totalnumPfams.contains(pfam[0]) && pfam[2].equalsIgnoreCase("PF")) {
			totalnumPfams.add(pfam[0]);
		}

		this.numOfPFams += 1;
		try {
			while ((zeile = this.pfamToRnToEc_.readLine()) != null) {
				if (!zeile.startsWith("!")) {
					if (pfamNr == Integer.valueOf(zeile.substring(zeile.indexOf(":PF") + 3,zeile.indexOf(":PF") + 8)).intValue()) {
						tmpNr = new String[4];
						tmpNr[0] = zeile.substring(zeile.indexOf(";") + 1);
						tmpNr[1] = pfam[1];
						if (tmpNr[0].startsWith("R")) {
							tmpNr[2] = "Rn";
						} else {
							tmpNr[2] = "EC";
						}
						tmpNr[3] = pfam[3];
						this.numOfPfamsToGo += 1;
						if (tmpNr[2].contentEquals("EC")) {
							retList.add(tmpNr);
							if (!numPfams.contains(tmpNr[0])) {
								numPfams.add(tmpNr[0]);
							}
						}
					}
					if (pfamNr < Integer.valueOf(zeile.substring(zeile.indexOf(":PF") + 3,zeile.indexOf(":PF") + 8)).intValue()) {
						break;
					}
				}
			}
			if (retList.size() == 0) {
				this.numOfMissedpfams += 1;
			}
			this.pfamToRnToEc_.close();
		} catch (IOException e) {
			openWarning("Error", "File: " + pfamToRnToEcPath_ + " not found");
			e.printStackTrace();
		}
		return retList;
	}

	private ArrayList<String[]> convertInterpro(String[] interpro) {// conversion step using ipr->kegg
																	
		ArrayList<String[]> retList = new ArrayList();
		if (this.IPRToECHash.isEmpty()) {
			DigitizeConversionFiles();
		}

		String zeile = "";
		String[] tmpNr = new String[4];
		tmpNr[3] = interpro[3];
		String interproNr = interpro[0];

		if (this.IPRToECHash.containsKey(interproNr)) {
			for (int i = 0; i < IPRToECHash.get(interproNr).size(); i++) {
				tmpNr[0] = IPRToECHash.get(interproNr).get(i);
				tmpNr[1] = interpro[1];
				tmpNr[2] = "EC";
				tmpNr[3] = interpro[3];
				Project.numOfConvertedIPRs += 1;
				retList.add(tmpNr);
			}
		}
		return retList;
	}

	private ArrayList<String[]> convertUni(String[] uni) {//conversion step using uniref -> EC
		
		ArrayList<String[]> retList = new ArrayList();
		if (UniToECHash.isEmpty()) {
			DigitizeConversionFilesUni();
		}

		String zeile = "";
		String[] tmpNr = new String[4];
		tmpNr[3] = uni[3];
		String uniNr = uni[0];
		System.out.println(uniNr);

		if (this.UniToECHash.containsKey(uniNr)) {
			System.out.println("Found UniRef");
			for (int i = 0; i < UniToECHash.get(uniNr).size(); i++) {
				tmpNr[0] = UniToECHash.get(uniNr).get(i);
				tmpNr[1] = uni[1];
				tmpNr[2] = "EC";
				tmpNr[3] = uni[3];
				Project.numOfConvertedUNIs += 1;
				retList.add(tmpNr);
			}
		}
		return retList;
	}

	private void DigitizeConversionFiles() { // Takes the IPR->EC conversion file into memory as a hashtable
		this.interproToECTxt_ = this.reader.readTxt(interproToECPath_);
		Hashtable<String, ArrayList<String>> tmpIPRToEC = new Hashtable<String, ArrayList<String>>();
		String line = "";
		try {
			while ((line = this.interproToECTxt_.readLine()) != null) {
				if (!line.startsWith("!")) {
					if (line.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")
							&& line.contains("+")) {
						String tmpIPR = line.substring(line.indexOf("IPR"),
								line.indexOf("IPR") + 9);
						ArrayList<String> tmpECS = new ArrayList<String>();
						String temp = line;
						while (temp.contains("+")) {
							temp = temp.substring(temp.indexOf("+") + 1);
							String tmpEC = temp;
							if (tmpEC.contains("+")) {
								tmpEC = tmpEC.substring(0, tmpEC.indexOf("+"));
							}
							tmpECS.add(tmpEC);
						}
						tmpIPRToEC.put(tmpIPR, tmpECS);
					}
				}
			}
		} catch (IOException e) {
			openWarning("Error", "File" + interproToECPath_ + " not found");
			e.printStackTrace();
		}
		this.IPRToECHash = tmpIPRToEC;
	}

	private void DigitizeConversionFilesUni() {// Takes the UniRef->GO and GO->EC conversion files into memory as hashtables
		this.uniToEcTxt_ = this.reader.readTxt(uni2ECPath_);
		Hashtable<String, ArrayList<String>> tmpUNIToEC = new Hashtable<String, ArrayList<String>>();
		String zeile = "";
		String[] uni_args = null;
		String[] ec_nums = null;
		try {
			while ((zeile = this.uniToEcTxt_.readLine()) != null) {
				if (!zeile.startsWith("!")) {
					if (zeile.contains("UniRef90")&& zeile.matches(".*[0-9]+.[0-9]+.[0-9]+.[0-9]+.*")) {
						uni_args = zeile.split("\\t");
						String tmpUni = uni_args[0];
						ArrayList<String> tmpECS = new ArrayList<String>();
						if(uni_args[1].contains(" ")){
							ec_nums = uni_args[1].split("\\s");
							for(String ec: ec_nums){
								if(ec != ""){
									tmpECS.add(ec);
								}
							}
							tmpUNIToEC.put(tmpUni, tmpECS);
						}
					}
				}
				uni_args = null;
				ec_nums = null;
			}
			this.uniToEcTxt_.close();
		} catch (IOException e) {
			openWarning("Error", "File" + uni2ECPath_ + " not found");
		}
		this.UniToECHash = tmpUNIToEC;
	}

	private void fillSampleEcs(Sample sample, int sampleIndex) {//
		this.lFrame_.bigStep("Filling " + sample.name_);
		for (int ecCnt = 0; ecCnt < sample.ecs_.size(); ecCnt++) {
			EcWithPathway smpEc = (EcWithPathway) sample.ecs_.get(ecCnt);

			this.lFrame_.step(smpEc.name_);
			if (smpEc.pathways_.size() > 0) {
				smpEc.pathways_ = new ArrayList();
			}
			EcWithPathway origEc = findEcWPath(smpEc);
			if (origEc != null) {
				for (int pwcnt = 0; pwcnt < origEc.pathways_.size(); pwcnt++) {
					smpEc.pathways_.add(new Pathway((Pathway) origEc.pathways_
							.get(pwcnt)));
				}
				smpEc.weight_ = origEc.weight_;
				smpEc.unique_ = origEc.unique_;
				smpEc.bioName_ = origEc.bioName_;
			} else {
				smpEc.addPathway((Pathway) getPathwayList_().get(
						this.unmatchedIndex));
			}
		}
	}

	public void removeEcPfamDoubles() {
		String ecNr = "";
		ConvertStat stat = null;
		int amount = 0;
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			Sample samp = (Sample) Project.samples_.get(smpCnt);
			if (!samp.imported) {
				for (int convCnt = 0; convCnt < samp.conversions_.size(); convCnt++) {
					stat = (ConvertStat) samp.conversions_.get(convCnt);
					ecNr = "";
					amount = 0;
					if ((stat.getPfamToEcAmount_() > 0)
							&& (stat.getEcAmount_() > 0)) {
						ecNr = stat.getEcNr_();
						if (stat.getPfamToEcAmount_() >= stat.getEcAmount_()) {
							amount = stat.getEcAmount_();
						} else {
							amount = stat.getEcAmount_()
									- stat.getPfamToEcAmount_();
						}
						removeEcByEcnR(samp, ecNr, amount);
					}
				}
			}
		}
	}

	public void removeEcByEcnR(Sample samp, String ecNr, int amount) {// removes an ec by its name from a provided sample
										
		if (ecNr == null) {
			return;
		}
		for (int ecCnt = 0; ecCnt < samp.ecs_.size(); ecCnt++) {
			EcNr tmpEcNr = (EcNr) samp.ecs_.get(ecCnt);
			if (tmpEcNr.name_.contentEquals(ecNr)) {
				tmpEcNr.amount_ -= amount;
				for (int statCnt = 0; statCnt < tmpEcNr.stats_.size(); statCnt++) {
					((EcSampleStats) tmpEcNr.stats_.get(statCnt)).amount_ -= amount;
				}
				if (tmpEcNr.amount_ <= 0) {
					samp.ecs_.remove(ecCnt);
				}
				this.lFrame_.step("removeEcByEcnR" + ecNr + ":" + amount);
				return;
			}
		}
	}

	// this will only happen if random sampling is selected
	private void removeRandomEc() {// removes randome ecs for prepping during random sampling
		Random generator = new Random();

		int sumOfEcs = 0;
		int[] sumsOfEcsArr = new int[Project.samples_.size()];
		if (Project.samples_.get(0) != null) {
			int minEcs = -1;
			for (int i = 0; i < Project.samples_.size(); i++) {
				sumOfEcs = 0;
				for (int ecCnt = 0; ecCnt < ((Sample) Project.samples_.get(i)).ecs_
						.size(); ecCnt++) {
					if (((EcWithPathway) ((Sample) Project.samples_.get(i)).ecs_
							.get(ecCnt)).pathways_.size() > 0) {
						sumOfEcs += ((EcWithPathway) ((Sample) Project.samples_
								.get(i)).ecs_.get(ecCnt)).amount_;
					}
				}
				System.out.println("sums " + i + " : " + sumOfEcs);
				sumsOfEcsArr[i] = sumOfEcs;
				if ((sumOfEcs < minEcs) || (minEcs == -1)) {
					minEcs = sumOfEcs;
				}
			}
			for (int i = 0; i < Project.samples_.size(); i++) {
				while (sumsOfEcsArr[i] > minEcs) {
					int pick = generator.nextInt(sumsOfEcsArr[i]);
					int index = 0;
					while (pick > 0) {
						pick -= ((EcWithPathway) ((Sample) Project.samples_
								.get(i)).ecs_.get(index)).amount_;
						if (pick <= 0) {
							break;
						}
						index++;
					}
					((EcWithPathway) ((Sample) Project.samples_.get(i)).ecs_
							.get(index)).amount_ -= 1;
					sumsOfEcsArr[i] -= 1;
					if (((EcWithPathway) ((Sample) Project.samples_.get(i)).ecs_
							.get(index)).amount_ <= 0) {
						((Sample) Project.samples_.get(i)).ecs_.remove(index);
					}
				}
			}
			for (int i = 0; i < Project.samples_.size(); i++) {
				int sum = 0;
				for (int j = 0; j < ((Sample) Project.samples_.get(i)).ecs_
						.size(); j++) {
					sum += ((EcWithPathway) ((Sample) Project.samples_.get(i)).ecs_
							.get(j)).amount_;
				}
				System.out.println("newsize(" + i + ") =  counted " + sum
						+ " predicted " + sumsOfEcsArr[i]);
			}
		}
	}
	/*loops through the ec list, which contains the ecs with pathways and if my ecs
	 * have the same name as the provided ecnr, returns the ec with pathway
	 */
	public EcWithPathway findEcWPath(EcNr ecNr) {
		for (int i = 0; i < ecList_.size(); i++) {
			if (((EcWithPathway) ecList_.get(i)).name_.compareTo(ecNr.name_) == 0) {
				return (EcWithPathway) ecList_.get(i);
			}
		}
		return null;
	}

	public void transferEcToPath() {
		EcWithPathway ecWP = null;
		PathwayWithEc pwEc = null;
		for (int sampleCount = 0; sampleCount < Project.samples_.size(); sampleCount++) {
			if (!((Sample) Project.samples_.get(sampleCount)).valuesSet) {
				for (int PwCount = 0; PwCount < getPathwayList_().size(); PwCount++) {
					pwEc = new PathwayWithEc((Pathway) getPathwayList_().get(
							PwCount));
					pwEc.clearList();
					((Sample) Project.samples_.get(sampleCount)).addPaths(pwEc);
					((PathwayWithEc) ((Sample) Project.samples_
							.get(sampleCount)).pathways_
							.get(((Sample) Project.samples_.get(sampleCount)).pathways_
									.size() - 1)).clearList();
				}
			}
		}
		if (Project.overall_.pathways_.size() == 0) {
			for (int PwCount = 0; PwCount < getPathwayList_().size(); PwCount++) {
				Project.overall_.addPaths(new PathwayWithEc(
						(Pathway) getPathwayList_().get(PwCount)));
			}
		}
		for (int sampleCount = 0; sampleCount < Project.samples_.size(); sampleCount++) {
			Sample tmpSample = (Sample) Project.samples_.get(sampleCount);
			if (!tmpSample.valuesSet) {
				for (int pwCount = 0; pwCount < tmpSample.pathways_.size(); pwCount++) {
					((PathwayWithEc) tmpSample.pathways_.get(pwCount)).ecNrs_ = new ArrayList();
				}
				for (int ecsCount = 0; ecsCount < tmpSample.ecs_.size(); ecsCount++) {
					ecWP = null;
					ecWP = (EcWithPathway) tmpSample.ecs_.get(ecsCount);
					if (ecWP.pathways_ != null) {
						for (int ecWPCount = 0; ecWPCount < ecWP.pathways_
								.size(); ecWPCount++) {
							for (int pwCount = 0; pwCount < tmpSample.pathways_
									.size(); pwCount++) {
								if (((Pathway) ecWP.pathways_.get(ecWPCount)).id_
										.compareTo(((PathwayWithEc) ((Sample) Project.samples_
												.get(sampleCount)).pathways_
												.get(pwCount)).id_) == 0) {
									if (((PathwayWithEc) tmpSample.pathways_
											.get(pwCount)).userPathway) {
										((PathwayWithEc) tmpSample.pathways_
												.get(pwCount)).addEcNewly(ecWP);
									} else {
										((PathwayWithEc) tmpSample.pathways_
												.get(pwCount)).addEc(ecWP);
									}
									this.lFrame_.step(String
											.valueOf(ecWP.amount_));
								}
							}
						}
					}
				}
				tmpSample.valuesSet = true;
			}
		}
	}

	public void prepOverall() {
		this.lFrame_.bigStep("Overall");

		Sample tmpSmp = null;
		PathwayWithEc tmpPwS = null;
		PathwayWithEc tmpPwD = null;
		EcNr tmpEc = null;

		Project.overall_.ecs_ = new ArrayList();
		for (int pwCnt = 0; pwCnt < Project.overall_.pathways_.size(); pwCnt++) {
			tmpPwD = (PathwayWithEc) Project.overall_.pathways_.get(pwCnt);
			tmpPwD.ecNrs_ = new ArrayList();
		}
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			tmpSmp = (Sample) Project.samples_.get(smpCnt);
			if (!tmpSmp.inUse) {
				System.out.println("not in use");
			} else {
				for (int pwCnt = 0; pwCnt < tmpSmp.pathways_.size(); pwCnt++) {
					tmpPwS = (PathwayWithEc) tmpSmp.pathways_.get(pwCnt);
					tmpPwD = Project.overall_.getPath(tmpPwS.id_);
					if (tmpPwD == null) {
						Project.overall_.pathways_
								.add(new PathwayWithEc(tmpPwS));
						tmpPwD = Project.overall_.getPath(tmpPwS.id_);
					}
					for (int ecCnt = 0; ecCnt < tmpPwS.ecNrs_.size(); ecCnt++) {
						tmpEc = new EcNr((EcNr) tmpPwS.ecNrs_.get(ecCnt));
						tmpEc.samColor_ = tmpSmp.sampleCol_;
						tmpEc.sampleNr_ = smpCnt;
						tmpPwD.addEc(tmpEc, tmpSmp);
					}
				}
				for (int ecCnt = 0; ecCnt < tmpSmp.ecs_.size(); ecCnt++) {
					Project.overall_.addEc(new EcWithPathway(
							(EcWithPathway) tmpSmp.ecs_.get(ecCnt)));
				}
			}
		}
	}

	public boolean ecNrInList(String ecNr) {
		this.ecList = this.reader.readTxt(listPath);
		String zeile = null;
		try {
			while ((zeile = this.ecList.readLine()) != null) {
				if (zeile.contains(ecNr)) {
					return true;
				}
			}
		} catch (IOException e) {
			openWarning("Error", "File: " + listPath + " not found");
			e.printStackTrace();
		}
		return false;
	}

	private void prepEcList() {// preps the ec list
		this.lFrame_.bigStep("EC List");
		String zeile = "";
		String tmp = "";
		String id = "";

		Boolean newEc = Boolean.valueOf(true);
		int index = 0;
		if (ecList_ == null) {
			ecList_ = new ArrayList();
			try {
				this.ecList = this.reader.readTxt(listPath);
				while ((zeile = this.ecList.readLine()) != null) {
					tmp = getEcNrFromList(zeile);
					newEc = Boolean.valueOf(true);

					id = getPathwayFromList(zeile);
					
					if ((!id.equalsIgnoreCase("ec01120"))
							&& (!id.equalsIgnoreCase("ec01110"))
							&& (!id.equalsIgnoreCase("ec01100"))) {
						Pathway tmpPathway = new Pathway(getPathway(id));
						if (!tmp.isEmpty()) {
							for (int i = 0; (i < ecList_.size())
									&& (newEc.booleanValue()); i++) {
								if (((EcWithPathway) ecList_.get(i)).name_
										.contentEquals(tmp)) {
									newEc = Boolean.valueOf(false);
									index = i;

									break;
								}
								index = i;
							}
							if (newEc.booleanValue()) {
								ecList_.add(new EcWithPathway(new EcNr(tmp)));
								((EcWithPathway) ecList_
										.get(ecList_.size() - 1))
										.addPathway(tmpPathway);
								setBioNameFromList((EcWithPathway) ecList_
										.get(ecList_.size() - 1));
								this.lFrame_.step(((EcWithPathway) ecList_
										.get(ecList_.size() - 1)).name_);
							} else {
								((EcWithPathway) ecList_.get(index))
										.addPathway(tmpPathway);
							}
						}
					}
				}
				this.ecList.close();
			} catch (IOException e) {
				openWarning("Error", "File: " + listPath + " not found");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Given an ec number, returns the pathway number in order to find the appropriate pathway
	 * 
	 * @param ecNum EC number
	 * @return Pathway number
	 * 
	 * @author Jennifer Terpstra
	 */
	public String getPathwayEc(String ecNum){
		String zeile = "";
		String tmp = "";
		String id = "";
		Map<String,String> eclist = new HashMap<String,String>();
		this.ecList = this.reader.readTxt(listPath);
		try {
			while ((zeile = this.ecList.readLine()) != null) {
				tmp = getEcNrFromList(zeile);
				id = getPathwayFromList(zeile);
				if(!id.equals("")&&!eclist.containsKey(tmp)){
					eclist.put(tmp, id);
				}
			}
			if(eclist.containsKey(ecNum)){
				return eclist.get(ecNum);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	private void setBioNameFromList(EcWithPathway ec) {
		String ecNr = ec.name_;
		String bioName = "";
		String zeile = "";
		try {
			BufferedReader ec2go = this.reader.readTxt(ecNamesPath);
			while ((zeile = ec2go.readLine()) != null) {
				if (zeile.startsWith("EC:" + ecNr)) {
					bioName = zeile.substring(zeile.indexOf(" > GO:") + 6,
							zeile.indexOf(" ; GO:"));
					ec.bioName_ = bioName;
				}
			}
			ec2go.close();
		} catch (IOException e) {
			openWarning("Error", "File: " + ecNamesPath + " not found");
			e.printStackTrace();
		}
	}

	public Pathway getPathway(String id) {
		Pathway ret = null;
		for (int i = 0; i < pathwayList_.size(); i++) {
			if (id.compareTo(((PathwayWithEc) getPathwayList_().get(i)).id_) == 0) {
				ret = new Pathway((Pathway) getPathwayList_().get(i));
				return ret;
			}
		}
		return ret;
	}

	private void prepPathList() {// Preps the list of pathways
		this.lFrame_.bigStep("Pathlist");
		String zeile = "";
		String ecNr = "";
		String tmpid = "";
		String tmpName = "";
		String path = "";
		int index = -1;
		if (getPathwayList_() == null) {
			setPathwayList_(new ArrayList());
			System.out.println(listPath);
			this.ecList = this.reader.readTxt(listPath);
			try {
				while ((zeile = this.ecList.readLine()) != null) {
					tmpid = getPathwayFromList(zeile);

					ecNr = getEcNrFromList(zeile);
					if ((!tmpid.equalsIgnoreCase("ec01120"))
							&& (!tmpid.equalsIgnoreCase("ec01110"))
							&& (!tmpid.equalsIgnoreCase("ec01100"))) {
						if ((tmpid.equalsIgnoreCase(path)) && (!ecNr.isEmpty())) {
							((PathwayWithEc) getPathwayList_().get(index))
									.addEc(new EcNr(ecNr));
						} else if (!ecNr.isEmpty()) {
							path = tmpid;
							tmpName = getPathwayName(tmpid);

							index++;

							getPathwayList_().add(
									new PathwayWithEc(new Pathway(tmpid,
											tmpName)));

							((PathwayWithEc) getPathwayList_().get(index))
									.addEc(new EcNr(ecNr));

							this.lFrame_
									.step(((PathwayWithEc) getPathwayList_()
											.get(index)).name_);
						}
					}
				}
				this.ecList.close();
			} catch (IOException e) {
				openWarning("Error", "File: " + listPath + " not found");
				e.printStackTrace();
			}
			getPathwayList_().add(
					new PathwayWithEc(new Pathway("-1", "Unmatched")));
			this.unmatchedIndex = (getPathwayList_().size() - 1);
		}
	}

	private void prepUserEc() {

		boolean ecFound = false;
		for (int i = 0; i < getPathwayList_().size(); i++) {
			PathwayWithEc pathway = (PathwayWithEc) getPathwayList_().get(i);
			if (pathway.userPathway) {
				ecFound = false;
				for (int j = 0; j < pathway.ecNrs_.size(); j++) {
					EcNr ecNr = (EcNr) pathway.ecNrs_.get(j);
					EcWithPathway ecWp = findEcWPath(ecNr);
					if (ecWp == null) {
						ecWp = new EcWithPathway(ecNr);
						ecWp.userEC = true;

						ecWp.addPathway(new Pathway(pathway));
						ecList_.add(ecWp);
					} else {
						ecWp.addPathway(pathway);
					}
				}
			}
		}
	}

	private void prepUserPathList() {
		
		if (this.activeProj_ == null) {
			return;
		}
		if (Project.userPathways == null) {
			return;
		}
		for (int i = 0; i < Project.userPathways.size(); i++) {
			System.out.println("userpath " + i);
			String path = (String) Project.userPathways.get(i);
			readUserPathway(path);
		}
	}

	private void readUserPathway(String path) {

		Pathway actPathway = new Pathway("", "");
		PathwayWithEc finPAth = new PathwayWithEc(actPathway);
		int userPathCnt = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String comp = "<pathName>";
			String zeile;
			while ((zeile = in.readLine()) != null) {
				// String zeile;
				comp = "<pathName>";
				if (zeile.startsWith(comp)) {
					zeile = zeile.substring(zeile.indexOf(">") + 1);
					finPAth.name_ = zeile;
					System.out.println("userpathName " + zeile);
					if (finPAth.name_ == null) {
						finPAth.name_ = ("userPath" + userPathCnt);
						userPathCnt++;
					}
					if (finPAth.name_.contentEquals("null")) {
						finPAth.name_ = ("userPath" + userPathCnt);
						userPathCnt++;
					}
				}
				comp = "<id>";
				if (zeile.startsWith(comp)) {
					zeile = zeile.substring(zeile.indexOf(">") + 1);
					finPAth.id_ = zeile;
					if (finPAth.id_.isEmpty()) {
						finPAth.id_ = finPAth.name_;
					}
				}
				comp = "<nodes>";
				if (zeile.startsWith(comp)) {
					finPAth = new PathwayWithEc(addNodes(finPAth, in), false);
				}
				comp = "<connections>";
				if (zeile.startsWith(comp)) {
					break;
				}
			}
			in.close();
			finPAth.userPathway = true;
			finPAth.pathwayInfoPAth = path;
			finPAth.id_ = finPAth.name_;
			finPAth.printPath();
			if (getPathway(finPAth.id_) == null) {
				getPathwayList_().add(finPAth);
				newUserPathList_.add(finPAth);
			}
		} catch (Exception e) {
			openWarning("Error", "File pathway: " + path + ".chn"
					+ " not found");
			System.out.println("Userfilepath not found");
		}
	}

	private PathwayWithEc addNodes(PathwayWithEc path, BufferedReader in) throws IOException {

		PathwayWithEc finPAth = new PathwayWithEc(path);
		EcNr ec = null;

		boolean comment = false;
		String zeile;
		while ((zeile = in.readLine()) != null) {
			String comp = "<name>";
			if (zeile.startsWith(comp)) {
				String ecName = zeile.substring(zeile.indexOf(">") + 1);
				ec = new EcNr(ecName);
			}
			comp = "<comment>";
			if (zeile.startsWith(comp)) {
				String ecName = zeile.substring(zeile.indexOf(">") + 1);
				if (ecName.contentEquals("true")) {
					comment = true;
				}
			}
			comp = "</node>";
			if (zeile.startsWith(comp)) {
				if (comment) {
					comment = false;
				} else {
					finPAth.addEc(ec);
				}
			}
			comp = "</nodes>";
			if (zeile.startsWith(comp)) {
				break;
			}
		}
		return finPAth;
	}

	public String getPathwayName(String id) {
		this.nameList = this.reader.readTxt(mapTitleList);
		String zeile = "";
		int beginIndex = 6;
		try {
			while ((zeile = this.nameList.readLine()) != null) {
				if (zeile.startsWith(id.substring(2))) {
					return zeile.substring(beginIndex);
				}
			}
		} catch (IOException e) {
			openWarning("Error", "File: pathway" + mapTitleList + ".chn"
					+ " not found");
			e.printStackTrace();
		}
		return "";
	}

	private void computeWeights() {
		this.lFrame_.bigStep("Weights");
		float totaluniqueEc = ecList_.size();
		float totalNrEc = 0.0F;
		float nrOfPaths = 0.0F;
		float weight = 0.0F;
		String zeile = "";
		try {
			this.ecList = this.reader.readTxt(listPath);
			while ((zeile = this.ecList.readLine()) != null) {
				if ((zeile.contains("ec:")) && (!zeile.contains("ec01100"))
						&& (!zeile.contains("ec01110"))
						&& (!zeile.contains("ec01120"))) {
					totalNrEc += 1.0F;
				}
			}
		} catch (IOException e) {
			openWarning("Error", "File: " + listPath + " not found");
			e.printStackTrace();
		}
		for (int i = 0; i < totaluniqueEc; i++) {
			if (!((EcWithPathway) ecList_.get(i)).weightsSet) {
				nrOfPaths = 0.0F;
				for (int pathCount = 0; pathCount < ((EcWithPathway) ecList_
						.get(i)).pathways_.size(); pathCount++) {
					if (isNoOverAll(((Pathway) ((EcWithPathway) ecList_.get(i)).pathways_
							.get(pathCount)).id_)) {
						nrOfPaths += 1.0F;
					}
				}
				if (nrOfPaths == 0.0F) {
					nrOfPaths = ((EcWithPathway) ecList_.get(i)).pathways_
							.size();
				}
				weight = totalNrEc / totaluniqueEc / nrOfPaths;
				((EcWithPathway) ecList_.get(i)).weight_ = weight;
				this.lFrame_.step(String.valueOf(weight));
				((EcWithPathway) ecList_.get(i)).weightsSet = true;
			}
			if (((EcWithPathway) ecList_.get(i)).weight_ > 100000.0F) {
				System.out.println("computeWeights userec");
				((EcWithPathway) ecList_.get(i)).weight_ = 1.0F;
			}
		}
	}

	private void calcAllChainsforallSamples() {
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			if (((Sample) Project.samples_.get(smpCnt)).inUse) {
				calcChainsForSample((Sample) Project.samples_.get(smpCnt));
			}
		}
		calcChainsForSample(Project.overall_);
	}

	private void calcChainsForSample(Sample samp) {
		PathwayWithEc actpath = null;
		EcNr actEcnr = null;
		for (int pathCnt = 0; pathCnt < samp.pathways_.size(); pathCnt++) {
			actpath = (PathwayWithEc) samp.pathways_.get(pathCnt);
			this.lFrame_.bigStep("Chaining");
			for (int ecCnt = 0; ecCnt < actpath.ecNrs_.size(); ecCnt++) {
				actEcnr = (EcNr) actpath.ecNrs_.get(ecCnt);

				this.lFrame_.step(actEcnr.name_);
				findLongestChain(actpath, actEcnr, false);

				actEcnr.adaptWeightToChain();
			}
		}
	}

	private void calcChainsForProg() {
		PathwayWithEc actpath = null;
		EcNr actEcnr = null;
		for (int pathCnt = 0; pathCnt < getPathwayList_().size(); pathCnt++) {
			actpath = (PathwayWithEc) getPathwayList_().get(pathCnt);
			this.lFrame_.bigStep("Chaining");
			for (int ecCnt = 0; ecCnt < actpath.ecNrs_.size(); ecCnt++) {
				actEcnr = (EcNr) actpath.ecNrs_.get(ecCnt);
				this.lFrame_.step(actEcnr.name_);
				findLongestChain(actpath, actEcnr, true);

				actEcnr.adaptWeightToChain();
			}
		}
	}

	private void findLongestChain(PathwayWithEc path, EcNr ecNr, boolean forProg) {
		int longestChain = 1;
		if (path.userPathway) {
			return;
		}
		if (path.id_.contentEquals("-1")) {
			return;
		}
		BufferedReader chainList = this.reader.readTxt(StartFromp1.FolderPath+"pathway"
				+ File.separator + path.id_ + ".chn");
		String zeile = "";
		String subLine = "";
		String next = " --> ";
		String chainEc = "";
		String ecName = ecNr.name_;
		ArrayList<EcNr> chain = new ArrayList<EcNr>();
		boolean lastHit = false;
		int tempChainLenght = 1;
		try {
			while ((zeile = chainList.readLine()) != null) {
				if (zeile.startsWith(ecName + next)) {
					lastHit = false;
					tempChainLenght = 1;
					subLine = zeile;
					while (!lastHit) {
						if (subLine.contains(next)) {
							subLine = subLine
									.substring(zeile.indexOf(next) + 5);
							if (subLine.contains(next)) {
								chainEc = subLine.substring(0,
										subLine.indexOf(next));
							} else {
								chainEc = subLine;
								lastHit = true;
							}
							for (int ecCnt = 0; ecCnt < path.ecNrs_.size(); ecCnt++) {
								if (chainEc.contentEquals(((EcNr) path.ecNrs_
										.get(ecCnt)).name_)) {
									tempChainLenght++;
									chain.add((EcNr) path.ecNrs_.get(ecCnt));
									break;
								}
								lastHit = true;
							}
						} else {
							lastHit = true;
						}
					}
					if (tempChainLenght > longestChain) {
						longestChain = tempChainLenght;
					}
				}
			}
			chainList.close();
			if (ecNr.longestChain_ < longestChain) {
				ecNr.longestChain_ = longestChain;
				if (forProg) {
					ecNr.maxChainLength_ = longestChain;
				}
			}
			for (int chCnt = 0; chCnt < chain.size(); chCnt++) {
				if (((EcNr) chain.get(chCnt)).longestChain_ < longestChain) {
					((EcNr) chain.get(chCnt)).longestChain_ = longestChain;
					if (forProg) {
						((EcNr) chain.get(chCnt)).maxChainLength_ = longestChain;
					}
				}
			}
		} 
		catch (IOException e) {
			openWarning("Error", "File: pathway" + File.separator + path.id_
					+ ".chn" + " not found");
			e.printStackTrace();
		}
	}

	private boolean isNoOverAll(String pathName) {
		if ((pathName.contentEquals("ec01100"))
				|| (pathName.contentEquals("ec01110"))
				|| (pathName.contentEquals("ec01120"))
				|| (pathName.contentEquals("rn01100"))
				|| (pathName.contentEquals("rn01110"))
				|| (pathName.contentEquals("rn01120"))) {
			return false;
		}
		return true;
	}

	public float getWeight(String ecNr) {
		for (int i = 0; i < ecList_.size(); i++) {
			if (((EcWithPathway) ecList_.get(i)).name_.contentEquals(ecNr)) {
				return ((EcWithPathway) ecList_.get(i)).weight_;
			}
		}
		return 0.0F;
	}

	public void transferWeightToPwl() {
		this.lFrame_.bigStep("transferWeights");

		float pathw = 0.0F;
		float tmpWeight = 0.0F;
		for (int i = 0; i < getPathwayList_().size(); i++) {
			if (!((PathwayWithEc) getPathwayList_().get(i)).weightSet) {
				pathw = 0.0F;
				for (int j = 0; j < ((PathwayWithEc) getPathwayList_().get(i)).ecNrs_
						.size(); j++) {
					String tmpEc = ((EcNr) ((PathwayWithEc) getPathwayList_()
							.get(i)).ecNrs_.get(j)).name_;
					tmpWeight = getWeight(tmpEc);
					((EcNr) ((PathwayWithEc) getPathwayList_().get(i)).ecNrs_
							.get(j)).weight_ = tmpWeight;
					pathw += tmpWeight;
				}
				((PathwayWithEc) getPathwayList_().get(i)).weight_ = pathw;

				((PathwayWithEc) getPathwayList_().get(i)).weightSet = true;
				if (pathw == 0.0D) {
					System.out.println("transfer we: "
							+ ((PathwayWithEc) getPathwayList_().get(i)).id_
							+ "w " + pathw);
				}
				if (((PathwayWithEc) getPathwayList_().get(i)).userPathway) {
					System.out.println("userPath "
							+ ((PathwayWithEc) getPathwayList_().get(i)).ecNrs_
									.size());
				}
			}
		}
	}

	public void getAllscores(int mode) {
		for (int i = 0; i < Project.samples_.size(); i++) {
			getScore((Sample) Project.samples_.get(i), mode);
		}
		getScore(Project.overall_, mode);
	}

	public void getScore(Sample sample, int mode) {
		double erg = 0.0D;
		PathwayWithEc tmp = null;
		for (int i = 0; i < sample.pathways_.size(); i++) {
			for (int pwCnt = 0; pwCnt < getPathwayList_().size(); pwCnt++) {
				if (((PathwayWithEc) sample.pathways_.get(i)).id_
						.contentEquals(((PathwayWithEc) getPathwayList_().get(
								pwCnt)).id_)) {
					tmp = (PathwayWithEc) getPathwayList_().get(pwCnt);
				}
			}
			erg = ((PathwayWithEc) sample.pathways_.get(i)).getWeight(mode)
					/ tmp.getWeight(mode);
			((PathwayWithEc) sample.pathways_.get(i)).score_ = (erg * 100.0D);
		}
		sample.valuesSet = true;
	}

	public void setWorkPath(String path) {
		this.workpath_ = path;
	}

	public void writeScore(Sample sample, String path, int mode) {
		String divide = "\t";
		if (mode == 1) {
			divide = ",";
		}
		double erg = 0.0D;
		try {
			if (path.isEmpty()) {
				path = this.workpath_ + "/output/weigth " + sample.name_;
			} else if (!path.endsWith(".txt")) {
				path = path + ".txt";
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write(sample.name_);
			out.newLine();
			for (int i = 0; i < sample.pathways_.size(); i++) {
				erg = ((PathwayWithEc) sample.pathways_.get(i)).score_;
				out.write(((PathwayWithEc) sample.pathways_.get(i)).id_
						+ divide
						+ ((PathwayWithEc) sample.pathways_.get(i)).name_
						+ divide + erg);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage alterPathway(Sample sample, PathwayWithEc pathway) {
		boolean found = false;
		if (this.build == null) {
			this.build = new PngBuilder();
		}
		BufferedReader xmlPath = this.reader.readTxt("pathway"
				+ this.separator_ + pathway.id_ + ".xml");
		ArrayList<EcPosAndSize> tmppos = new ArrayList();
		for (int ecCount = 0; ecCount < pathway.ecNrs_.size(); ecCount++) {
			xmlPath = this.reader.readTxt("pathway" + this.separator_
					+ pathway.id_ + ".xml");
			tmppos = this.parser.findEcPosAndSize(
					((EcNr) pathway.ecNrs_.get(ecCount)).name_, xmlPath);
			((EcNr) pathway.ecNrs_.get(ecCount)).addPos(tmppos);

			System.out.println(((EcNr) pathway.ecNrs_.get(ecCount)).stats_
					.size());
			if (tmppos != null) {
				found = true;
			}
		}
		if (found) {
			return this.build.getAlteredPathway(pathway, sample);
		}
		try {
			return ImageIO.read(new File("pics" + this.separator_ + pathway.id_
					+ ".png"));
		} catch (IOException e) {
			openWarning("Error", "File: pics" + this.separator_ + pathway.id_
					+ ".png" + " not found");
			e.printStackTrace();
		}
		return null;
	}

	public void alterPng(Sample sample) {
		if (this.build == null) {
			this.build = new PngBuilder();
		}
		PathwayWithEc tmpPath = null;
		BufferedReader xmlPath = null;
		ArrayList<EcPosAndSize> tmppos = new ArrayList();
		ArrayList<EcPosAndSize> pos = new ArrayList();
		for (int pathCount = 0; pathCount < sample.pathways_.size(); pathCount++) {
			tmpPath = (PathwayWithEc) sample.pathways_.get(pathCount);
			xmlPath = this.reader.readTxt("pathway" + this.separator_
					+ tmpPath.id_ + ".xml");
			while (pos.size() > 0) {
				pos.remove(0);
			}
			for (int ecCount = 0; ecCount < tmpPath.ecNrs_.size(); ecCount++) {
				xmlPath = this.reader.readTxt("pathway" + this.separator_
						+ tmpPath.id_ + ".xml");
				tmppos = this.parser.findEcPosAndSize(
						((EcNr) tmpPath.ecNrs_.get(ecCount)).name_, xmlPath);
				((EcNr) tmpPath.ecNrs_.get(ecCount)).addPos(tmppos);
			}
			this.build.alterPathway(tmpPath, sample);
		}
	}

	public void sortPathwaysByScore(ArrayList<PathwayWithEc> pathways) {
		int tmpCnt = 0;
		for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++) {
			tmpCnt = pathCnt;
			for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
				if (((PathwayWithEc) pathways.get(tmpCnt)).score_ < ((PathwayWithEc) pathways
						.get(pathCnt2)).score_) {
					tmpCnt = pathCnt2;
				}
			}
			pathways.add(pathCnt, (PathwayWithEc) pathways.get(tmpCnt));
			pathways.remove(tmpCnt + 1);
		}
	}

	public void sortPathwaysByName(ArrayList<PathwayWithEc> pathways) {
		int tmpCnt = 0;
		for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++) {
			tmpCnt = pathCnt;
			for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
				for (int i = 0; i < ((PathwayWithEc) pathways.get(tmpCnt)).id_
						.length(); i++) {
					if (((PathwayWithEc) pathways.get(tmpCnt)).id_.charAt(i) < ((PathwayWithEc) pathways
							.get(tmpCnt)).id_.charAt(i)) {
						tmpCnt = pathCnt2;
					}
				}
			}
			pathways.add(pathCnt, (PathwayWithEc) pathways.get(tmpCnt));
			pathways.remove(tmpCnt + 1);
		}
	}

	public void sortAllPathwaysByName() {
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			sortPathwaysByName(((Sample) Project.samples_.get(smpCnt)).pathways_);
		}
	}

	public void convertSelected() {
		for (int pathCnt = 0; pathCnt < getPathwayList_().size(); pathCnt++) {
			for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
				Sample tmpSamp = (Sample) Project.samples_.get(smpCnt);
				for (int pathCnt2 = 0; pathCnt2 < tmpSamp.pathways_.size(); pathCnt2++) {
					if (((PathwayWithEc) getPathwayList_().get(pathCnt)).id_
							.contentEquals(((PathwayWithEc) tmpSamp.pathways_
									.get(pathCnt2)).id_)) {
						((PathwayWithEc) tmpSamp.pathways_.get(pathCnt2))
								.setSelected(((PathwayWithEc) getPathwayList_()
										.get(pathCnt)).isSelected());
					}
				}
			}
			for (int pathCnt2 = 0; pathCnt2 < Project.overall_.pathways_.size(); pathCnt2++) {
				((PathwayWithEc) Project.overall_.pathways_.get(pathCnt2))
						.setSelected(((PathwayWithEc) getPathwayList_().get(
								pathCnt)).isSelected());
			}
		}
	}

	public void setPathwayList_(ArrayList<PathwayWithEc> pathwayList_) {
		this.pathwayList_ = pathwayList_;
	}

	public ArrayList<PathwayWithEc> getPathwayList_() {
		return pathwayList_;
	}

	public boolean selectedPathways() {
		boolean ret = false;
		for (int i = 0; i < pathwayList_.size(); i++) {
			if (pathwayList_.get(i).isSelected()) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public EcWithPathway getEc(String ecId) {
		for (int ecCnt = 0; ecCnt < ecList_.size(); ecCnt++) {
			if (((EcWithPathway) ecList_.get(ecCnt)).name_.contentEquals(ecId)) {
				return (EcWithPathway) ecList_.get(ecCnt);
			}
		}
		return null;
	}

	private void openWarning(String title, String string) {// open the warning which takes in its title and warning message
															
		JFrame frame = new JFrame(title);
		frame.setVisible(true);
		frame.setBounds(200, 200, 350, 150);
		frame.setLayout(null);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 350, 150);
		panel.setBackground(Color.lightGray);
		panel.setVisible(true);
		panel.setLayout(null);
		frame.add(panel);

		JLabel label = new JLabel(string);

		label.setVisible(true);
		label.setForeground(Color.black);
		label.setBounds(0, 0, 350, 150);
		label.setLayout(null);
		panel.add(label);

		frame.repaint();
	}
}
