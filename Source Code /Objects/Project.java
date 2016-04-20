package Objects;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.*;
import Prog.StartFromp1;
import Prog.StringReader;


/**
 *  Obviously this is where project files are stored. Serves mostly to store the data from the samples so it can be used elsewhere.
 *	One notable feature of this class however is the fact that this is where the matrix format files are processed... I don't know  
 *	why either but it works and it would take a lot of configuring to move it to Prog.DataProcessor. If you would like to take the  
 *	time to do so you are welcome
 */
public class Project {
	public static String projectPath_; // Path to the project file in question
	public static String workpath_; // The name of the project
	public static ArrayList<String> userPathways; // User created pathways being used for this project
	static final String VERS = "$$ver:"; 
	public static double minVisScore_; 
	public static boolean loaded = false; 
	public static boolean listMode_ = false; 
	public static boolean randMode_ = false; 
	public static boolean chaining = true; 
	public static boolean imported = false; 
	public static boolean dataChanged = true; 
	public static boolean dataChanged2 = true; 
	public static boolean iprNameChange = false;
	public static ArrayList<Sample> samples_=new ArrayList<Sample>(); // ArrayList of sample objects which stores the samples associated with this project
	public static ArrayList<String> removedSamples; 
	public static Sample overall_; 
	private static Color backColor_; 
	private static Color fontColor_; 
	private static Color overAllColor_; 
	public static Color standard = new Color(90, 125, 206); 
	public static int amountOfEcs = 0; 
	public static int numOfCompleteEcs = 0; 
	public static int numOfMappedEcs = 0; 
	public static int amountOfPfs = 0; 
	public static int numOfConvertedPFs = 0; 
	public static int numOfConvPfsComplete = 0; 
	public static int numOfConvPfsMapped = 0; 
	public static int amountOfIPRs = 0; 
	public static int numOfConvertedIPRs = 0; 
	public static int numOfConvIPRsComplete = 0; 
	public static int numOfConvIPRsMapped = 0;
	public static int amountOfUNIs = 0;
	public static int numOfConvertedUNIs = 0;
	public static int numOfConvUNIsComplete = 0;
	public static int numofConvUNIsMapped = 0;
	
	/**
	 * ArrayList of booleans which correlates to the samples_array and states whether or not each sample is valid
	 */
	public static ArrayList<Boolean> legitSamples = new ArrayList<Boolean>(); 
//	final static String basePath_ = new File(".").getAbsolutePath()
//			+ File.separator; // The base path of the Fromp software. Nessesairy for all relative paths to function
	final static String basePath_ = StartFromp1.FolderPath; // The base path of the Fromp software. Nessesairy for all relative paths to function
	//Hash which stores the IPR->EC conversion file
	Hashtable<String, ArrayList<String>> IPRToECHash = new Hashtable<String, ArrayList<String>>(); 
	Hashtable<String, String> PFamToECHash = new Hashtable<String, String>(); 
	BufferedReader interproToECTxt_; // Reader for IPR -> EC conversion file
	BufferedReader pfamToRnToEcTxt_; // Reader for PFam -> EC conversion file
	static final String interproToECPath_ = basePath_ + "list" + File.separator
			+ "interPro_kegg.tsv"; // Path to the file for the IPR -> EC conversion file
	static final String pfamToRnToEcPath_ = basePath_ + "list" + File.separator
			+ "pfam2Ec2Rn.txt"; // Path to the file for the PFam -> EC conversion file
	StringReader reader; // Prog.StringReader, not native Java string reader

	public Project(String workPath) {
		File file = new File("");
		try {
			projectPath_ = file.getCanonicalPath(); // Sets the path to the project file
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		imported = false;
		minVisScore_ = 0;
		workpath_ = workPath;

		samples_ = new ArrayList<Sample>();
		overall_ = new Sample("overAll", "");
		overall_.sampleCol_ = Color.black;
		overall_.singleSample_ = false;
		backColor_ = new Color(233, 233, 233);
		fontColor_ = Color.black;
		overAllColor_ = Color.red;
	}
	
	/**
	 * Method to load a project file. Calls loadProjectv0 with an integer 'mode'
	 * depending of whether or not the first line contains '$$ver:1". Version 1
	 * has denotations for whether or not each sample in the project file is 
	 * "in use"
	 * @param projectFile
	 * @return
	 */
	public int loadProject(BufferedReader projectFile) {
		String zeile = "";
		try {
			if ((zeile = projectFile.readLine()) != null) {
				if (zeile.contains("$$ver:1")) {
					return loadProjectv0(projectFile, zeile, 1);
				}
				return loadProjectv0(projectFile, zeile, 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int loadProjectv0(BufferedReader projectFile, String firstLine,
			int mode) {
		File file = new File("");
		minVisScore_ = 0;
		int ret = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		boolean inUse = false;
		try {
			projectPath_ = file.getCanonicalPath();
		} catch (IOException e) {
			openWarning("Error", "File: " + projectPath_ + " not found");
			e.printStackTrace();
		}
		samples_ = new ArrayList<Sample>();
		overall_ = new Sample("overAll", "");//overall ec and go amount of all samples
		overall_.sampleCol_ = Color.black;
		overall_.singleSample_ = false;
		try {
			if (mode == 1) {
				String zeile;
				if ((zeile = projectFile.readLine()) != null) {
					projectPath_ = zeile;
					ret = 1;
				} else {
					return -1;
				}
			} else {
				projectPath_ = firstLine;
			}
			String zeile;
			if ((zeile = projectFile.readLine()) != null) {
				workpath_ = zeile;
			}
			while ((zeile = projectFile.readLine()) != null) {
				inUse = false;
				String zeile2 = projectFile.readLine();
				red = convertStringtoInt(projectFile.readLine());
				green = convertStringtoInt(projectFile.readLine());
				blue = convertStringtoInt(projectFile.readLine());
				if ((mode == 1)
						&& (projectFile.readLine().contentEquals("inUse"))) {
					inUse = true;
					System.out.println(inUse);
				}
				samples_.add(new Sample(zeile, zeile2, new Color(red, green,
						blue), inUse));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (backColor_ == null) {
			backColor_ = Color.white;
		}
		return ret;
	}
//	// Writes out a project file. To my knowledge this method is never used as the exportProject method is preferred. Writes to a .prj file
//	public void writeProject() {
//		try {
//			BufferedWriter out = null;
//			if (projectPath_.endsWith(".prj")) {
//				out = new BufferedWriter(new FileWriter(projectPath_));
//			} else {
//				out = new BufferedWriter(new FileWriter(projectPath_
//						+ File.separator + "projects" + File.separator
//						+ workpath_ + ".prj"));
//			}
//			if (projectPath_ == null) {
//				System.out.println("no path");
//			}
//			System.out.println(projectPath_ + File.separator + "projects"
//					+ File.separator + workpath_ + ".prj");
//			out.write("$$ver:1");
//			out.newLine();
//			out.write(projectPath_ + "\n");
//			out.write(workpath_ + "\n");
//			for (int i = 0; i < samples_.size(); i++) {
//				out.write(((Sample) samples_.get(i)).name_ + "\n");
//				out.write(((Sample) samples_.get(i)).fullPath_ + "\n");
//				out.write(((Sample) samples_.get(i)).sampleCol_.getRed() + "\n");
//				out.write(((Sample) samples_.get(i)).sampleCol_.getGreen()
//						+ "\n");
//				out.write(((Sample) samples_.get(i)).sampleCol_.getBlue()
//						+ "\n");
//				if (((Sample) samples_.get(i)).inUse) {
//					out.write("inUse\n");
//					System.out.println("inUse");
//				} else {
//					out.write("notInUse\n");
//					System.out.println("notinUse");
//				}
//				System.out.println(((Sample) samples_.get(i)).name_);
//				System.out.println(((Sample) samples_.get(i)).fullPath_);
//			}
//			out.close();
//		} catch (IOException e) {
//			System.out.println("Exception ");
//		}
//	}

	public String exportProj(String path) {// The actual method to write the project out to a file. Builds a .frp file
		if (path == null) {
			path = projectPath_;
			if (!path.endsWith(".frp")) {
				path = projectPath_ + File.separator + "projects"+ File.separator + workpath_;
				String tmpPath = path + ".frp"; // Temporary string built to test if a file of that name already exists
				File file1 = new File(tmpPath); 
				//If the file of the name does exist than loop until appending some int i to the 
				//file name will generate a file name for a file that does not exist
				if (file1.exists() && !file1.isDirectory()) { 
					int i = 1; 
					//This is my favorite infinite loop condition (next to "Hell" != "Frozen over")
					while ("Pigs" != "Fly") { 
						tmpPath = path + "-" + i + ".frp"; //
						File file2 = new File(tmpPath); //
						//If the file with this name exists increment the int i and keep looping
						if (file2.exists() && !file2.isDirectory()) { 
							i++; 
							continue; 
						//If there is no file with this name that exists than this name is okay for the generated file.
						} 
						else { 
							path = path + "-" + i; 
							break; 
						} 
					} 
				} 
				path = path + ".frp"; // Appends the file extension
			}
			System.out.println(path);
		} 
		else if (path.isEmpty()) {
			System.out.println(projectPath_);
			System.out.println(workpath_);
			path = projectPath_ + File.separator + "projects" + File.separator
					+ workpath_;
			String tmpPath = path + ".frp";
			File file1 = new File(tmpPath);
			if (file1.exists() && !file1.isDirectory()) {
				int i = 1;
				while ("Pigs" != "Fly") {// loop forever
					tmpPath = path + "-" + i+ ".frp";
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
			path = path + ".frp";
			System.out.println(path);
		}
		BufferedWriter out = null;
		try {
			try {
				out = new BufferedWriter(new FileWriter(path));
			} catch (FileNotFoundException e) {
				//If an error is caught when trying to write to the file we are generating a new JFrame displaying the error message is built
				JFrame frame = new JFrame("Error"); 
				frame.setBounds(100, 100, 500, 100);
				frame.setVisible(true);
				frame.setLayout(null);

				JPanel back = new JPanel();
				back.setBounds(0, 0, 500, 100);
				back.setVisible(true);
				back.setLayout(null);
				back.setBackground(getBackColor_());
				frame.add(back);

				JLabel label = new JLabel(e.getMessage());
				label.setBounds(0, 0, 500, 100);
				label.setVisible(true);
				label.setLayout(null);
				label.setBackground(getBackColor_());
				back.add(label);

				System.out.println(e.getMessage());
				return "";
			}
			out.write("$$ver:$EXP1$");
			out.newLine();

			Date now = new Date();
			out.write("!! " + now.toString() + " !!");
			out.newLine();

			out.write("$PROJ$" + workpath_);
			out.newLine();
			if (backColor_ != null) {
				out.write("$back$" + backColor_.getRed() + ":"+ backColor_.getGreen() + ":" + backColor_.getBlue());
				out.newLine();
			}
			if (fontColor_ != null) {
				out.write("$font$" + fontColor_.getRed() + ":"+ fontColor_.getGreen() + ":" + fontColor_.getBlue());
				out.newLine();
			}
			if (overAllColor_ != null) {
				out.write("$oAll$" + overAllColor_.getRed() + ":"+ overAllColor_.getGreen() + ":"+ overAllColor_.getBlue());
				out.newLine();
			}
			//all samples write to file.
			for (int smpCnt = 0; smpCnt < samples_.size(); smpCnt++) {
				Sample tmpSamp = samples_.get(smpCnt);
				out.write("SMP*:" + tmpSamp.name_);
				out.newLine();
				if (tmpSamp.sampleCol_ != null) {
					out.write("smpCol*" + tmpSamp.sampleCol_.getRed() + ":"+ tmpSamp.sampleCol_.getGreen() + ":"+ tmpSamp.sampleCol_.getBlue());
					out.newLine();
				}
				//Write out EC and following data
				for (int ecCnt = 0; ecCnt < tmpSamp.ecs_.size(); ecCnt++) {
					EcWithPathway tmpEc = tmpSamp.ecs_.get(ecCnt);
					out.write("EC*:" + tmpEc.name_ + ":" + tmpEc.amount_);
					out.newLine();

					for (int repCnt = 0; repCnt < tmpSamp.conversions_.size(); repCnt++) {
						//seq id empty problem. need to fix.
						if (tmpSamp.conversions_.get(repCnt).ecNr_.contentEquals(tmpEc.name_)) {
							//desc_ = seq ID
							out.write(tmpSamp.conversions_.get(repCnt).desc_+"\t"
									 +tmpSamp.conversions_.get(repCnt).ecAmount_+"\t"
									 +tmpSamp.conversions_.get(repCnt).pfamToEcAmount_ +"\t"
									 +tmpSamp.conversions_.get(repCnt).unusedEc +"\n");
						}
					}
				}
				//Write out GO and following data
				for (int ecCnt = 0; ecCnt < tmpSamp.gos_.size(); ecCnt++) {
					GONum tmpGo = tmpSamp.gos_.get(ecCnt);
					out.write("GO*:" + tmpGo.GoNumber + ":" + tmpGo.amount_);
					out.newLine();

					for (int repCnt = 0; repCnt < tmpSamp.conversionsGo_.size(); repCnt++) {
						//seq id empty problem. need to fix.
						if (tmpSamp.conversionsGo_.get(repCnt).GoNr_.contentEquals(tmpGo.GoNumber)) {
							//desc_ = seq ID
							out.write(tmpSamp.conversionsGo_.get(repCnt).desc_+"\t"
								 	 +tmpSamp.conversionsGo_.get(repCnt).goAmount_+"\t"
								 	 +tmpSamp.conversionsGo_.get(repCnt).pfamToGoAmount_ +"\t"
									 +tmpSamp.conversionsGo_.get(repCnt).unusedGo +"\n");
						}
					}
				}
				
				
			}
			saveUserPAths(out); // Writes the user paths (if any) to the end of the file
			saveConvStats(out); // Writes the conversion statistics (if any) to the end of the file
			//Writes the sequence files to the end of the file. If a particular sample
			//dosn't have a sequence file associated with it then it is replace with "none"
			saveSeqFiles(out); 
			out.close(); // Closes the file
			return path;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	//Saves the conversion statistics to the project file
	private void saveConvStats(BufferedWriter out) throws IOException { 
		out.write("<ConvStats>");
		out.newLine();
		out.write("<amountOfEcs>" + amountOfEcs);
		out.newLine();
		out.write("<numOfCompleteEcs>" + numOfCompleteEcs);
		out.newLine();
		out.write("<numOfMappedEcs>" + numOfMappedEcs);
		out.newLine();
		out.write("<amountOfPfs>" + amountOfPfs);
		out.newLine();
		out.write("<numOfConvertedPFs>" + numOfConvertedPFs);
		out.newLine();
		out.write("<numOfConvPfsComplete>" + numOfConvPfsComplete);
		out.newLine();
		out.write("<numOfConvPfsMapped>" + numOfConvPfsMapped);
		out.newLine();
		for (int i = 0; i < legitSamples.size(); i++) {
			out.write("<legit>" + legitSamples.get(i));
			out.newLine();
		}
		out.write("</ConvStats>");
		out.newLine();
	}
	//Loads conversion statistics from an input project file
	private void loadConvStats(BufferedReader in) throws IOException {
		String zeile;
		loaded = true;
		while ((zeile = in.readLine()) != null) {
			// String zeile;
			String comp = "</ConvStats>";
			if (zeile.contentEquals(comp)) {
				break;
			}
			comp = "<amountOfEcs>";
			if (zeile.startsWith(comp)) {
				String tmp = zeile.substring(comp.length());
				try {
					int value = Integer.valueOf(tmp).intValue();
					amountOfEcs = value;
				} catch (Exception e) {
					System.out.println("couldn't convert to Integer: " + zeile);
				}
			} else {
				comp = "<numOfCompleteEcs>";
				if (zeile.startsWith(comp)) {
					String tmp = zeile.substring(comp.length());
					try {
						int value = Integer.valueOf(tmp).intValue();
						numOfCompleteEcs = value;
					} catch (Exception e) {
						System.out.println("couldn't convert to Integer: "
								+ zeile);
					}
				} else {
					comp = "<numOfMappedEcs>";
					if (zeile.startsWith(comp)) {
						String tmp = zeile.substring(comp.length());
						try {
							int value = Integer.valueOf(tmp).intValue();
							numOfMappedEcs = value;
						} catch (Exception e) {
							System.out.println("couldn't convert to Integer: "
									+ zeile);
						}
					} else {
						comp = "<amountOfPfs>";
						if (zeile.startsWith(comp)) {
							String tmp = zeile.substring(comp.length());
							try {
								int value = Integer.valueOf(tmp).intValue();
								amountOfPfs = value;
							} catch (Exception e) {
								System.out
										.println("couldn't convert to Integer: "
												+ zeile);
							}
						} else {
							comp = "<numOfConvertedPFs>";
							if (zeile.startsWith(comp)) {
								String tmp = zeile.substring(comp.length());
								try {
									int value = Integer.valueOf(tmp).intValue();
									numOfConvertedPFs = value;
								} catch (Exception e) {
									System.out
											.println("couldn't convert to Integer: "
													+ zeile);
								}
							} else {
								comp = "<numOfConvPfsComplete>";
								if (zeile.startsWith(comp)) {
									String tmp = zeile.substring(comp.length());
									try {
										int value = Integer.valueOf(tmp)
												.intValue();
										numOfConvPfsComplete = value;
									} catch (Exception e) {
										System.out
												.println("couldn't convert to Integer: "
														+ zeile);
									}
								} else {
									comp = "<numOfConvPfsMapped>";
									if (zeile.startsWith(comp)) {
										String tmp = zeile.substring(comp
												.length());
										try {
											int value = Integer.valueOf(tmp)
													.intValue();
											numOfConvPfsMapped = value;
										} catch (Exception e) {
											System.out
													.println("couldn't convert to Integer: "
															+ zeile);
										}
									} else {
										comp = "<legit>";
										if (zeile.startsWith(comp)) {
											String tmp = zeile.substring(comp
													.length());
											if (tmp.contentEquals("true")) {
												legitSamples.add(Boolean
														.valueOf(true));
											} else {
												legitSamples.add(Boolean
														.valueOf(false));
											}
										}
									}
								}

							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Saves the user paths to the project file
	 * @param out
	 * @throws IOException
	 */
	public void saveUserPAths(BufferedWriter out) throws IOException { 
		out.write("<userPathways>");
		out.newLine();
		if (userPathways == null) {
			out.write("</userPathways>");
			out.newLine();
			return;
		}
		for (int i = 0; i < userPathways.size(); i++) {
			String zeile = userPathways.get(i);
			String zeile2;

			String shortBasePath_;
			String seperate = File.separator;

			if (seperate.compareTo("/") == 0) {
				shortBasePath_ = basePath_.replace("./", "");
			} else {
				shortBasePath_ = basePath_.replace(".\\", "");
			}
			if (zeile.contains(basePath_)) {
				zeile2 = zeile.replace(basePath_, "");
			} else if (zeile.contains(shortBasePath_)) {
				zeile2 = zeile.replace(shortBasePath_, "");
			} else {
				zeile2 = zeile;
			}

			out.write(zeile2);
			out.newLine();
		}
		out.write("</userPathways>");
		out.newLine();
	}

	public void loadUserPAths(BufferedReader in, boolean newList)
			throws IOException {// loads the user paths from the project file
		if (userPathways == null) {
			userPathways = new ArrayList<String>();
		}
		String zeile;
		while ((zeile = in.readLine()) != null) {
			// String zeile;
			if (zeile.contentEquals("</userPathways>")) {
				break;
			}
			String zeile2;
			String seperate = File.separator;
			//System.out.println("File separator:" + seperate);
			if (seperate.compareTo("/") == 0) {
				//System.out.println("File separate 1");
				zeile2 = zeile.replace("\\", "/");
			} else if (seperate.compareTo("\\") == 0) {
				//System.out.println("File separate 2");
				zeile2 = zeile.replace("/", "\\");
			} else {
				//System.out.println("File separate 3");
				zeile2 = zeile;
			}

			String zeile3 = "" + basePath_ + zeile2;
			userPathways.remove(zeile3);
			userPathways.add(zeile3);
		}
	}
	/*
	 * Saves the sequence files to the project file in separate lines for all of the samples,
	 * if there is no sequence for the sample the method instead prints "none"
	 */
	private void saveSeqFiles(BufferedWriter out) throws IOException {
		out.write("<SeqFiles>");
		out.newLine();
		for (int i = 0; i < samples_.size(); i++) {
			if (samples_.get(i).getSequenceFile() != null
					&& !samples_.get(i).getSequenceFile().equals("")) {
				out.write(samples_.get(i).getSequenceFile());
			} else {
				out.write("none");
			}
			out.newLine();
		}
		out.write("</SeqFiles>");
		out.newLine();
	}
	/*
	 * Load the sequence files from the project files, if line says
	 * "none" then no sequence file is added to the sample.
	 */
	private void loadSeqFiles(BufferedReader in) throws IOException { 
		if (samples_.isEmpty()) {
			return;
		}
		String zeile;
		int i = 0;
		while ((zeile = in.readLine()) != null) {
			if (zeile.contentEquals("</SeqFiles>")) {
				break;
			}
			if (!zeile.contentEquals("none")) {
				File tmp = new File(zeile);
				
				if(System.getProperty("os.name").contains("Linux")){
					if(zeile.contains(File.separator)){
						if(tmp.exists()){
							samples_.get(i).setSequenceFile(zeile);
						}
					}
				}
				else if(System.getProperty("os.name").contains("Windows")){
					if(zeile.contains(File.separator)){
						if(tmp.exists()){
							samples_.get(i).setSequenceFile(zeile);
						}
					}
					
				}
				
				
			}
			i++;
		}
	}
	/*
	 * When you press the "Load EC-Matrix" button in the EditSamplesPane and load a matrix file
	 * type this is where it is parsed. Takes in ECs, or Pfams/IPRs to be converted, as well as the
	 * counts for each sample in a matrix format. This function parses that and builds
	 * samples from the file to add to this project.
	 */
	public void loadMat(String path, String name) {
		String line = "";
		String cutOff = "";
		int origSmpSize = samples_.size();
		int smpIndex = origSmpSize;
		Sample tmpSample = null;
		boolean firstLine = true;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			while ((line = in.readLine()) != null) {
				if (line.contains(",")) {
					smpIndex = origSmpSize;

					cutOff = line.substring(0, line.indexOf(","));
					line = line.substring(line.indexOf(",") + 1);
					System.out.println("cutoff " + cutOff);
					if (cutOff.startsWith("IPR")) {
						this.reader = new StringReader();
						ArrayList<String> convertedCutoff = convertInterpro(cutOff);
						for (int i = 0; i < convertedCutoff.size(); i++) {
							cutOff = convertedCutoff.get(i);
							System.out.println("cutoff " + cutOff);
							EcNr tmpEc = new EcNr(cutOff);
							tmpEc.sampleNr_ = smpIndex;
							while (!line.isEmpty()) {
								if (firstLine) {
									int addedSmpNr = smpIndex - origSmpSize;
									tmpSample = new Sample();
									tmpSample.name_ = (name + addedSmpNr);
									tmpSample.imported = true;
									tmpSample.inUse = true;
									tmpSample.matrixSample = true;
									tmpSample.sampleCol_ = new Color(
											(float) Math.random(),
											(float) Math.random(),
											(float) Math.random());
									samples_.add(tmpSample);
								} else {
									tmpSample = (Sample) samples_.get(smpIndex);
								}
								if (line.contains(",")) {
									cutOff = line.substring(0,
											line.indexOf(","));
									line = line
											.substring(line.indexOf(",") + 1);
									System.out.println(cutOff);
								} else {
									cutOff = line;
									System.err.println(cutOff);
									line = "";
								}
								tmpEc.amount_ = Integer.valueOf(cutOff)
										.intValue();
								tmpEc.sampleNr_ = smpIndex;
								tmpEc.samColor_ = ((Sample) samples_
										.get(samples_.size() - 1)).sampleCol_;
								tmpEc.stats_.add(new EcSampleStats(tmpEc));
								boolean alreadyThere = false;
								for (int x = 0; x < tmpSample.ecs_.size(); x++) {
									if (tmpEc.name_
											.equalsIgnoreCase(tmpSample.ecs_
													.get(x).name_)) {
										tmpSample.ecs_.get(x).amount_ += Integer
												.valueOf(cutOff).intValue();
										alreadyThere = true;
										break;
									}
								}
								if (!alreadyThere) {
									tmpSample.ecs_
											.add(new EcWithPathway(tmpEc));
								}
								System.err.println("tmpsSample ecSize "
										+ tmpSample.ecs_.size());
								smpIndex++;
							}
							if (firstLine) {
								firstLine = false;
							}
						}
						continue;

					} else if (cutOff.startsWith("PF")) {
						this.reader = new StringReader();
						cutOff = convertPFam(cutOff);
						System.out.println("cutoff " + cutOff);
					}
					EcNr tmpEc = new EcNr(cutOff);
					tmpEc.sampleNr_ = smpIndex;
					while (!line.isEmpty()) {
						if (firstLine) {
							int addedSmpNr = smpIndex - origSmpSize;
							tmpSample = new Sample();
							tmpSample.name_ = (name + addedSmpNr);
							tmpSample.imported = true;
							tmpSample.inUse = true;
							tmpSample.matrixSample = true;
							tmpSample.sampleCol_ = new Color(
									(float) Math.random(),
									(float) Math.random(),
									(float) Math.random());
							samples_.add(tmpSample);
						} else {
							tmpSample = (Sample) samples_.get(smpIndex);
						}
						if (line.contains(",")) {
							cutOff = line.substring(0, line.indexOf(","));
							line = line.substring(line.indexOf(",") + 1);
							System.out.println(cutOff);
						} else {
							cutOff = line;
							System.err.println(cutOff);
							line = "";
						}
						tmpEc.amount_ = Integer.valueOf(cutOff).intValue();
						tmpEc.sampleNr_ = smpIndex;
						tmpEc.samColor_ = ((Sample) samples_.get(samples_
								.size() - 1)).sampleCol_;
						tmpEc.stats_.add(new EcSampleStats(tmpEc));
						tmpSample.ecs_.add(new EcWithPathway(tmpEc));
						System.err.println("tmpsSample ecSize "
								+ tmpSample.ecs_.size());
						smpIndex++;
					}
					if (firstLine) {
						firstLine = false;
					}
				}
			}
			in.close();
		} catch (Exception e) {
			openWarning("Error", "File: " + path + " not found");
			e.printStackTrace();
		}
	}

	private String convertPFam(String pfam) {// Conversion step for Pfams using Pfam -> Ec conversion files
		String ret = "";
		if (this.PFamToECHash.isEmpty()) {
			DigitizeConversionFiles();
		}

		String pfamNr = pfam;

		if (this.PFamToECHash.containsKey(pfamNr)) {
			ret = PFamToECHash.get(pfamNr);
		}

		return ret;
	}

	private ArrayList<String> convertInterpro(String interpro) {// This is the conversion step using ipr->kegg.
		ArrayList<String> retList = new ArrayList<String>();
		if (this.IPRToECHash.isEmpty()) {
			DigitizeConversionFiles();
		}

		String tmpNr = "";
		String interproNr = interpro;

		if (this.IPRToECHash.containsKey(interproNr)) {
			for (int i = 0; i < IPRToECHash.get(interproNr).size(); i++) {
				tmpNr = IPRToECHash.get(interproNr).get(i);
				Project.numOfConvertedIPRs += 1;
				retList.add(tmpNr);
			}
		}
		return retList;
	}
	/*
	 * Parses through both the IPR->Kegg and PFam->EC conversion files and takes them
	 * into memory as HashTables to facilitate the conversion from PFam or IPR to EC
	 */
	private void DigitizeConversionFiles() { 
		this.interproToECTxt_ = this.reader.readTxt(interproToECPath_);
		this.pfamToRnToEcTxt_ = this.reader.readTxt(pfamToRnToEcPath_);
		Hashtable<String, ArrayList<String>> tmpIPRToEC = new Hashtable<String, ArrayList<String>>();
		Hashtable<String, String> tmpPFamToEC = new Hashtable<String, String>();
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
						// System.out.println("Digitized: "+tmpIPR+"Maps to: "+tmpECS.toString());
						tmpIPRToEC.put(tmpIPR, tmpECS);
					}
				}
			}
		} catch (IOException e) {
			openWarning("Error", "File" + interproToECPath_ + " not found");
			e.printStackTrace();
		}
		try {
			while ((line = this.pfamToRnToEcTxt_.readLine()) != null) {
				if (!line.startsWith("!")) {
					if (line.matches(".*PF[0-9][0-9][0-9][0-9][0-9].*")
							&& line.contains(";")
							&& !line.matches(".*R[0-9][0-9][0-9][0-9][0-9].*")) {
						String tmpPfam = line.substring(line.indexOf("PF"),
								line.indexOf("PF") + 7);
						String tmpEC = line.substring(line.indexOf(";") + 1);

						tmpPFamToEC.put(tmpPfam, tmpEC);
					}
				}
			}
		} catch (IOException e) {
			openWarning("Error", "File" + pfamToRnToEcPath_ + " not found");
			e.printStackTrace();
		}
		this.IPRToECHash = tmpIPRToEC;
		this.PFamToECHash = tmpPFamToEC;
	}

	public void refreshProj() {
		for (int i = 0; i < samples_.size(); i++) {
			Sample samp = (Sample) samples_.get(i);
			if (!samp.matrixSample) {
				samp.clearPaths();
			}
		}
	}
	/**
	 * Imports the project file at a given file path
	 * Format of .frp file : head part + (sample name part + read ec part + read go part)*samples amount + tail part 
	 * @param path
	 */
	public void importProj(String path) { 
		legitSamples = new ArrayList<Boolean>();
		BufferedReader in = null;
		projectPath_ = path;	
		int sampNr = -1;
		try {
			in = new BufferedReader(new FileReader(path));
			if (!in.readLine().contentEquals("$$ver:$EXP1$")) {
				in.close();
				System.out.println("Wrong format of the project file");
				return;
			}
			String tmpLine = "";
			tmpLine = in.readLine();
			//load head part
			while (!tmpLine.startsWith("SMP*:")) {
				//start to load head part
				if (tmpLine.startsWith("$PROJ$")) {
					workpath_ = tmpLine.substring("$PROJ$".length());
				}
				
				else if (tmpLine.startsWith("$back$")) {
					
					tmpLine = tmpLine.substring("$back$".length());
					int red = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
					tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
					int green = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
					tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
					int blue = Integer.valueOf(tmpLine).intValue();
					backColor_ = new Color(red, green, blue);
				}
				else if (tmpLine.startsWith("$font$")) {
					
					tmpLine = tmpLine.substring("$font$".length());
					int red = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
					tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
					int green = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
					tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
					int blue = Integer.valueOf(tmpLine).intValue();
					fontColor_ = new Color(red, green, blue);
				}
				else if (tmpLine.startsWith("$oAll$")) {
					
					tmpLine = tmpLine.substring("$oAll$".length());
					int red = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
					tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
					int green = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
					tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
					int blue = Integer.valueOf(tmpLine).intValue();
					overAllColor_ = new Color(red, green, blue);
					
				}
				tmpLine = in.readLine();
			}//end to load head part
			//start to load sample name part + read ec part + read go part
			while (!tmpLine.startsWith("<userPathways>")){
				//load sample name
				if (tmpLine.startsWith("SMP*:")) {
					if (samples_ == null) {
						samples_ = new ArrayList<Sample>();
					}	
					tmpLine = tmpLine.substring("SMP*:".length());
					String name = tmpLine;
					Sample tmpSamp = new Sample(name, "");
					sampNr++;
					tmpLine = in.readLine();
					if (!tmpLine.isEmpty()) {
						
						tmpLine = tmpLine.substring("smpCol*".length());
						int red = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
						tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
						int green = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
						tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
						int blue = Integer.valueOf(tmpLine).intValue();
						tmpSamp.sampleCol_ = new Color(red, green, blue);
						tmpSamp.inUse = true;
						tmpSamp.imported = true;
					} 
					else {
						tmpSamp.sampleCol_ = Color.BLUE;
					}
					tmpSamp.legitSample = true;
					samples_.add(tmpSamp);		
				}	
				
				//read EC part.	
				if (tmpLine.startsWith("EC*:") && StartFromp1.doEC) {
					String [] ecNumAmount = tmpLine.split(":");
					EcNr tmpEc = new EcNr(ecNumAmount[1]);
					tmpEc.amount_ = Integer.valueOf(ecNumAmount[2]).intValue();
					tmpEc.sampleNr_ = sampNr;
					tmpEc.samColor_ = ((Sample) samples_.get(samples_.size() - 1)).sampleCol_;
					tmpEc.stats_.add(new EcSampleStats(tmpEc));
					tmpLine = in.readLine();
					//read all(Format: seq id + ec amount + pf amount + unused ec num) of this Ec num.
					while(!tmpLine.startsWith("EC*:") && !tmpLine.startsWith("GO*:") && !tmpLine.startsWith("<userPathways>") && !tmpLine.startsWith("SMP*:")){
						try{
							ArrayList<String> line = new ArrayList<String>(Arrays.asList(tmpLine.split("\t")));
							
							String desc = line.get(0);
							int ecAm = Integer.valueOf(line.get(1));
							int pfAm = Integer.valueOf(line.get(2));
							String unUsedEc = line.get(3);
							
							ConvertStat convertStat = new ConvertStat(desc,tmpEc.name_, ecAm, pfAm, 0,unUsedEc);
							tmpEc.repseqs_.add(new Repseqs(desc, ecAm + pfAm));
							samples_.get(samples_.size() - 1).conversions_.add(convertStat);
						}
						catch(Exception e){
							e.printStackTrace();
						}
						tmpLine = in.readLine();
					}
					
					samples_.get(samples_.size() - 1).ecs_.add(new EcWithPathway(tmpEc));
					continue;
					
				}	
				
				//read GO part.
				if (tmpLine.startsWith("GO*:")&& StartFromp1.doGo) {
					
					String [] goNumAmount = tmpLine.split(":");
					GONum tmpGo = new GONum(goNumAmount[1]);
					tmpGo.amount_ = Integer.valueOf(goNumAmount[2]).intValue();
					tmpGo.sampleNr_ = sampNr;
					tmpGo.samColor_ = ((Sample) samples_.get(samples_.size() - 1)).sampleCol_;
					tmpGo.stats_.add(new GoSampleStats(tmpGo));
					tmpLine = in.readLine();
					//read all(Format: seq id + go amount + pf amount + unused go num) of this Go num.
					while(!tmpLine.startsWith("EC*:") && !tmpLine.startsWith("GO*:") && !tmpLine.startsWith("<userPathways>") && !tmpLine.startsWith("SMP*:")){
						try{
							ArrayList<String> line = new ArrayList<String>(Arrays.asList(tmpLine.split("\t")));
							
							String desc = line.get(0);
							// need to refactor later.
							int goAm = Integer.valueOf(line.get(1));
							int pfAm = Integer.valueOf(line.get(2));
							String unUsedGo = line.get(3);
							
							ConvertStatGo convertStatGo = new ConvertStatGo(desc,tmpGo.GoNumber, goAm, pfAm, 0,unUsedGo);
							tmpGo.repseqs_.add(new Repseqs(desc, goAm + pfAm));
							samples_.get(samples_.size() - 1).conversionsGo_.add(convertStatGo);
						}
						catch(Exception e){
							e.printStackTrace();
						}
						tmpLine = in.readLine();
					}
					samples_.get(samples_.size() - 1).gos_.add(new GONum(tmpGo));
					continue;
				}
				tmpLine = in.readLine();	
			}//end to load sample name part + read ec part + read go part	
							
			//start to read the tail part
			while (tmpLine != null){
				if (tmpLine.contentEquals("<userPathways>")) {
					loadUserPAths(in, true);
				}
				else if (tmpLine.contentEquals("<ConvStats>")) {
					loadConvStats(in);
				}
				else if (tmpLine.contentEquals("<SeqFiles>")) {
					loadSeqFiles(in);
				}
	
				tmpLine = in.readLine();
			}
			in.close();
			imported = true;
		}//end of try		
		catch (IOException e) {
			openWarning("Error", "File: " + path + " not found");
			e.printStackTrace();
		}
	}

	public BufferedReader readTxt(String path) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(path));
		} catch (IOException e) {
			openWarning("Error", "File: " + path + " not found");
			e.printStackTrace();
		}
		return in;
	}

	public void clearSamples() {// removes all the samples stored by this project object
		samples_ = new ArrayList<Sample>();
	}

	public int convertStringtoInt(String in) {
		int ret = 0;
		for (int i = 0; i < in.length(); i++) {
			ret *= 10;
			char c = in.charAt(i);
			ret += convertCharToInt(c);
		}
		return ret;
	}

	public int convertCharToInt(char c) {
		switch (c) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		}
		return -1;
	}

	public static Color getBackColor_() {
		if (backColor_ == null) {
			backColor_ = Color.orange;
		}
		return backColor_;
	}

	public static void setBackColor_(Color backColor_) {
		Project.backColor_ = backColor_;
	}

	public static Color getFontColor_() {
		return fontColor_;
	}

	public static void setFontColor_(Color fontColor_) {
		Project.fontColor_ = fontColor_;
	}

	public static Color getOverAllColor_() {
		return overAllColor_;
	}

	public static void setOverAllColor_(Color overAllColor_) {
		Project.overAllColor_ = overAllColor_;
	}

	public static void removeSample(int index) {
		/*
		 * if (removedSamples == null) { removedSamples = new ArrayList(); }
		 * removedSamples.add(((Sample)samples_.get(index)).name_);
		 */
		samples_.remove(index);
	}

	public static void removeUserPath(String path) {
		String pathName = getUserPathName(path);
		for (int i = 0; i < samples_.size(); i++) {
			Sample samp = (Sample) samples_.get(i);
			samp.removeUserPath(pathName);
			overall_.removeUserPath(pathName);
		}
	}

	private static String getUserPathName(String path)
   {
     try
     {
       @SuppressWarnings("resource")
	BufferedReader in = new BufferedReader(new FileReader(path));
       String comp = "<pathName>";
       String zeile;
       while ((zeile = in.readLine()) != null)
       {
         //String zeile;
         comp = "<pathName>";
         if (zeile.startsWith(comp)) {
           return zeile.substring(zeile.indexOf(">") + 1);
         }
       }
       in.close();
     }
     catch (Exception e)
     {
       openWarning("Error", "File: " + path + " not found");
       return "";
     }
     return "";
   }

	private static void openWarning(String title, String string) {
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

	public static void addUserP(String userP) {
		if (userPathways == null) {
			userPathways = new ArrayList<String>();
		}
		userPathways.add(userP);
	}
	public static boolean isIprNameChange() {
		return iprNameChange;
	}
	public static void setIprNameChange(boolean iprNameChange) {
		Project.iprNameChange = iprNameChange;
	}
	
	
}
