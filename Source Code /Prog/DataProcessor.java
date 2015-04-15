/*    1:     */ package Prog;
/*    2:     */ 
/*    3:     */ import Objects.ConvertStat;
/*    4:     */ import Objects.EcNr;
/*    5:     */ import Objects.EcPosAndSize;
/*    6:     */ import Objects.EcSampleStats;
/*    7:     */ import Objects.EcWithPathway;
/*    8:     */ import Objects.Pathway;
/*    9:     */ import Objects.PathwayWithEc;
/*   10:     */ import Objects.Project;
/*   11:     */ import Objects.Sample;
/*   12:     */ import Panes.HelpFrame;
/*   13:     */ import Panes.Loadingframe;
/*   14:     */ import java.awt.Color;
/*   15:     */ import java.awt.image.BufferedImage;
/*   16:     */ import java.io.BufferedReader;
/*   17:     */ import java.io.BufferedWriter;
/*   18:     */ import java.io.File;
/*   19:     */ import java.io.FileReader;
/*   20:     */ import java.io.FileWriter;
/*   21:     */ import java.io.IOException;
/*   22:     */ import java.io.PrintStream;
/*   23:     */ import java.util.ArrayList;
/*   24:     */ import java.util.Random;
/*   25:     */ import javax.imageio.ImageIO;
/*   26:     */ import javax.swing.JFrame;
/*   27:     */ import javax.swing.JLabel;
/*   28:     */ import javax.swing.JPanel;
				import java.util.Arrays;
				import java.util.*;
/*   29:     */ 

				//*THIS IS THE MOST IMPORTANT PART OF FROMP*
				// Now that I have you attention, let me explain. The data processor is what basically does all of the parsing of the raw input files the converting of pfams,
				// and everything important for the data. Everything else is a helper class for this: storing, and displaying data, to the users. This class does all of the 
				// computation that actually makes the data usable from the raw input. 
			

/*   30:     */ public class DataProcessor
/*   31:     */ {
/*   32:     */   Project activeProj_;
/*   33:     */   StringReader reader;
/*   34:     */   String workpath_;
/*   35:     */   String separator_;
/*   36:  31 */   static final String listPath = "list" + File.separator + "ec.list";					 		// These lines are the conversion charts found in "/list"
/*   37:  32 */   static final String ecNamesPath = "list" + File.separator + "ec2go.txt";				 		//
/*   38:  33 */   static final String rnListPath = "list" + File.separator + "rn.list";					 		//
/*   39:  34 */   static final String mapTitleList = "list" + File.separator + "map_title.tab";			 		//
/*   40:  35 */   static final String pfamToRnToEcPath_ = "list" + File.separator + "pfam2Ec2Rn.txt";	 		//
				  static final String interproToGOPath_ = "list" + File.separator + "interpro2GO.txt";	 		//
				  static final String interproToECPath_ = "list" + File.separator + "interPro_kegg.tsv"; 		//
/*   41:     */   static final String EC = "EC";																// Variables to store the starting Strings of Pfams, ECs, Rns, and InterPros
/*   42:     */   static final String PF = "Pf";																//
/*   43:     */   static final String RN = "Rn";																//
				  static final String IPR = "IPR";																//
/*   44:     */   XmlParser parser;																				// An XML parser, Prog.XMLParser
/*   45:  40 */   int offCounter = 0;																			//
/*   46:  41 */   int counter = 0;																				//
/*   47:  42 */   int nrOfP = 0;																				//
/*   48:  46 */   public static boolean newBaseData = true;														//		
/*   49:  47 */   public static boolean newUserData = true;														//
/*   50:     */   Loadingframe lFrame_;																			// Loading Frame to let the user know when things are happening
/*   51:  51 */   boolean chaining = true;																		// Boolean to indicate whether or not in chaining mode
/*   52:  53 */   int numOfConvertedPfam = 0;																	// Conversion statistics for the Pfam, EC, and InterPro conversions	
/*   53:  54 */   int numOfMissedpfams = 0;																		// 
/*   54:  55 */   int numOfPFams = 0;																			//
/*   55:  56 */   int numOfPfamsToGo = 0;																		//
/*   56:  57 */   int numOfGOToRn = 0;																			//
/*   57:  58 */   int numOfGoToEc = 0;																			//
/*   58:     */   int unmatchedIndex;																			//
/*   59:  62 */   int maxEcInP = 0;																				//
//				  int numIPR = 0;																				//	
//				  int numConvertedIPR = 0;																		//
//				  int numCompleteIPR = 0;																		//
//				  int numMappedIPR = 0;																			//
/*   60:     */   BufferedReader ecList;																		// Buffered Reader for reading the conversion files
/*   61:     */   BufferedReader rnList;																		//
/*   62:     */   BufferedReader nameList;																		//
/*   63:     */   BufferedReader ecToGoTxt_;																	//
/*   64:     */   BufferedReader pfamToRnToEc_;																	//
				  BufferedReader interproToGOTxt_;																//
				  BufferedReader interproToECTxt_;																//
/*   65:     */   PngBuilder build;																				// PngBuilder to draw the graphics
/*   66:     */   Color sysCol_;																				// Array list containing the pathways
/*   67:     */   public static ArrayList<PathwayWithEc> pathwayList_;											// Array list containing the user pathways
/*   68:     */   private static ArrayList<PathwayWithEc> newUserPathList_;										// Array list containg the ECs
/*   69:     */   public static ArrayList<EcWithPathway> ecList_;												//
/*   70:  75 */   boolean reduce = false;																		//
																												//
				  ArrayList<String> numEcs=new ArrayList<String>();												// Arraylists to facilitate conversion statistics
				  ArrayList<String> numPfams=new ArrayList<String>();											//	
				  ArrayList<String> totalnumPfams=new ArrayList<String>();										//
				  																								//
				  Hashtable<String, ArrayList<String>> IPRToECHash=new Hashtable<String, ArrayList<String>>();	// Hash of IPR -> EC conversion
				  Hashtable<String, ArrayList<String>> IPRToGOHash=new Hashtable<String, ArrayList<String>>();	// Hash of IPR -> GO conversion
				  Hashtable<String, ArrayList<String>> GOToECHash=new Hashtable<String, ArrayList<String>>();	// Hash of GO -> EC conversion

/*   71:     */   
/*   72:     */   public DataProcessor(Project actProj)
/*   73:     */   {// Builds the data processor object for the active project
/*   74:  78 */     this.activeProj_ = actProj;				 
															
					newUserData = true;						
					newBaseData = true;						
/*   75:     */     										
/*   76:  80 */     this.reader = new StringReader();		
/*   77:  81 */     this.separator_ = File.separator;		
/*   78:  82 */     this.parser = new XmlParser();			
/*   79:  83 */     this.lFrame_ = new Loadingframe();		
/*   80:     */     										
/*   81:  85 */     newUserPathList_ = new ArrayList();		
                    pathwayList_= null;						
/*   82:     */     										
/*   83:  87 */     prepData();	 // Prepares everything for the data processor 
/*   84:     */   }
/*   85:     */   
/*   86:     */   public DataProcessor()
/*   87:     */   {// Builds a shell of a data processor, not connected to 
					newUserData = true;
					newBaseData = true;
/*   88:  90 */     this.reader = new StringReader();
/*   89:  91 */     this.separator_ = File.separator;
/*   90:  92 */     this.parser = new XmlParser();
/*   91:     */   }
/*   92:     */   
/*   93:     */   public void prepData()
/*   94:     */   {// Prepares the data processor, if the base data or the user data is new, ie DataProcessor was just called, it prepares the pathlist and the ec list 
/*   95:  95 */     if (newBaseData)
/*   96:     */     {
/*   97:  96 */       System.err.println("new BaseData");
/*   98:  97 */       prepPathList();
/*   99:  98 */       prepEcList();
/*  100:  99 */       newBaseData = false;
/*  101:     */     }
/*  102: 101 */     if (newUserData)
/*  103:     */     {
/*  104: 102 */       System.err.println("new UserData");
/*  105: 103 */       prepUserPathList();
/*  106: 104 */       prepUserEc();
/*  107: 105 */       newUserData = false;
/*  108:     */     }
/*  109: 107 */     computeWeights();		// 
/*  110: 108 */     transferWeightToPwl();	// Transfers the weights to a pathway list
/*  111: 109 */     calcChainsForProg();	// Calculates the longest chain between EcNrs
/*  112: 111 */     if (this.lFrame_ != null) {
/*  113: 112 */       Loadingframe.close();
/*  114:     */     }
/*  115:     */   }
/*  116:     */   
/*  117:     */   public void processProject()
/*  118:     */   {
/*  119: 117 */     this.lFrame_ = new Loadingframe();
/*  120: 118 */     if ((newBaseData) || (newUserData)) {
/*  121: 119 */       prepData();
/*  122:     */     }
/*  123: 121 */     this.lFrame_ = new Loadingframe();
/*  124: 122 */     allEcVsPathway();
/*  125: 123 */     this.lFrame_ = new Loadingframe();
/*  126: 124 */     removeEcPfamDoubles();
/*  127: 125 */     this.lFrame_ = new Loadingframe();
/*  128: 126 */     if (Project.randMode_) {
/*  129: 127 */       removeRandomEc();
/*  130:     */     }
/*  131: 129 */     this.lFrame_ = new Loadingframe();
/*  132: 130 */     transferEcToPath();
/*  133:     */     
/*  134: 132 */     this.lFrame_ = new Loadingframe();
/*  135: 133 */     prepOverall();
/*  136:     */     
/*  137: 135 */     this.lFrame_ = new Loadingframe();
/*  138: 136 */     calcAllChainsforallSamples();
/*  139:     */     
/*  140: 138 */     this.lFrame_ = new Loadingframe();
/*  141: 139 */     getAllscores(0);
/*  142: 141 */     if (this.lFrame_ != null) {
/*  143: 142 */       Loadingframe.close();
/*  144:     */     }
/*  145:     */   }
/*  146:     */   
/*  147:     */   public String[] getEnzFromRawSample(String line)
/*  148:     */   {// Retrieves ec/pfam and sequence IDs from HMMER output files.
					if(line.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")){
						return getEnzFromInterPro(line);
					}
//					System.out.println("Raw Sample");
/*  149: 146 */     String[] ret = new String[4];
/*  150:     */     
/*  155: 152 */     ret[0] = "X";	// Ec name
/*  156:     */     				//
/*  157: 154 */     ret[1] = "1";	// Number of this ec with this sequence id
/*  158:     */     				//
/*  159: 156 */     ret[2] = "X";	// Whether or not it is a pf or if it is an ec 
/*  160:     */     				//
/*  161: 158 */     ret[3] = "X";	// Sequence ID
/*  162: 160 */     if (line.contains("_"))
/*  163:     */     {
/*  164: 161 */       String tmp = line.substring(0, line.indexOf("_"));
/*  165: 162 */       if (isEc(tmp))
/*  166:     */       {
/*  167: 163 */         String repSeq = line.substring(line.indexOf(" -") + 1);
/*  168: 164 */         repSeq = repSeq.substring(0, repSeq.indexOf(" -"));
/*  169: 165 */         if (repSeq.contains("/")) {
/*  170: 166 */           repSeq = repSeq.substring(0, repSeq.indexOf("/"));
/*  171:     */         }
/*  172: 168 */         while ((repSeq.startsWith(" ")) || (repSeq.startsWith("-"))) {
/*  173: 169 */           repSeq = repSeq.substring(1);
/*  174:     */         }
/*  175: 171 */         ret[0] = tmp;
/*  176: 172 */         ret[2] = "EC";
/*  177: 173 */         ret[3] = repSeq;
						if(!numEcs.contains(ret[0])){//adds to the count of total ecs for statistics
							numEcs.add(ret[0]);
						}
/*  178:     */       }
/*  179:     */       else
/*  180:     */       {
/*  181: 176 */         tmp = findPfamAndRepSeqInRaw(line);
/*  182: 177 */         if (!tmp.isEmpty())
/*  183:     */         {
/*  184: 178 */           ret[0] = tmp.substring(0, tmp.indexOf("-"));
/*  185: 179 */           ret[2] = "Pf";
/*  186: 180 */           String repSeq = tmp.substring(tmp.indexOf("-") + 1);
/*  187: 181 */           if (repSeq.contains("/")) {
/*  188: 182 */             repSeq = repSeq.substring(0, repSeq.indexOf("/"));
/*  189:     */           }
/*  190: 184 */           ret[3] = repSeq;

						  if(!totalnumPfams.contains(ret[0])){//adds to the count of total pfams 
							totalnumPfams.add(ret[0]);
						  }
						  String[] nump=ret.clone();
						  ArrayList<String[]> nump2;
						  nump2=convertPfam(nump);
						  for(int i=0;i<nump2.size();i++){
						  	if(!numPfams.contains(nump2.get(i)[0])){//adds to the count of total converted pfams
								numPfams.add(nump2.get(i)[0]);
//								System.out.println(nump2.get(i)[0]);
						  	}
						  }
/*  191:     */         }
			//			else{
			//				tmp=findInterProAndRepSeqInRaw(line);
			//				if(!tmp.isEmpty()){
			//					if(tmp.contains("-")){
			//						ret[0] = tmp.substring(0, tmp.indexOf("-"));
			//						String repSeq = tmp.substring(tmp.indexOf("-") + 1);
			//						if(repSeq.contains("/")){
			//							repSeq = repSeq.substring(0, repSeq.indexOf("/"));
			//						}
			//						ret[3] = repSeq;
			//					}else{
			//						ret[0]=tmp;
			//					} 
			//					ret[2] = "IPR";
			//				}
			//			}
/*  192:     */       }
/*  193:     */     }
/*  194: 189 */     if ((ret[0] == "X") && 
/*  195: 190 */       (line.startsWith("PF")))
/*  196:     */     {
/*  197: 191 */       ret[0] = line.substring(0, line.indexOf("."));
/*  198: 193 */       if (isPfambool(ret[0]))
/*  199:     */       {
/*  200: 194 */         ret[2] = "Pf";
/*  201: 195 */         String tmp = line.substring(line.indexOf(" ") + 1);
/*  202: 196 */         tmp = tmp.substring(0, tmp.indexOf(" -"));
/*  203: 197 */         ret[3] = tmp;

						if(!totalnumPfams.contains(ret[0])){		// Adds to the count of total pfams 
							totalnumPfams.add(ret[0]);
						}
						String[] nump=ret.clone();
						ArrayList<String[]> nump2;
						nump2=convertPfam(nump);
						for(int i=0;i<nump2.size();i++){
						  if(!numPfams.contains(nump2.get(i)[0])){	// Adds to the count of total converted pfams
							numPfams.add(nump2.get(i)[0]);
//							System.out.println(nump2.get(i)[0]);
						  }
						}
					  }
					  else if ( (ret[0] == "X") && (line.startsWith("IPR")) ) {
					  	ret[0] = line.substring(0, line.indexOf("."));
					  	if (isInterProBool(ret[0])){
					  		ret[2] = "IPR";
					  		String tmp = line.substring(line.indexOf(" "));
					  		tmp = tmp.substring(line.indexOf(" -"));
					  		ret[3] = tmp;
					  	}
					  }
/*  205:     */       else
/*  206:     */       {
/*  207: 200 */         ret[0] = "X";
/*  208:     */       }
/*  209:     */     }
/*  210: 204 */     if (ret[0] == "X")
/*  211:     */     {
					  String tmp;
/*  212:     */       try
/*  213:     */       {
/*  214: 206 */         tmp = line.substring(line.indexOf(" "));
/*  223: 212 */         while (tmp.startsWith(" ")) {
/*  224: 213 */            tmp = tmp.substring(1);
/*  215:     */         } 
/*  227: 218 */         ret = getEnzFromRawSample(tmp);
                      }
/*  216:     */       catch (StringIndexOutOfBoundsException e)
/*  217:     */       {
/*  219: 209 */         return ret;
                        
/*  220:     */       }
//*  221:     */       String tmp;
//*  222: 211 */       boolean empty=false;
					  if(tmp!=null){
/*  223: 212 */         while (tmp.startsWith(" ")) {
/*  224: 213 */           tmp = tmp.substring(1);
						}
/*  225:     */       }
/*  226: 216 */       if (tmp!=null) {
/*  227: 218 */         ret = getEnzFromRawSample(tmp);
/*  228:     */       }
/*  229:     */     }
/*  230: 223 */     return ret;
/*  231:     */   }

				  public String[] getEnzFromInterPro(String line)
				  { // Retrieves the interpro and sequence ids from InterPro output files

				  	if(!line.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")){
						return null;
					}
					String seperator=",";
//				  	System.out.println("Inter Pro");
					String[] ret = new String[4];

					ret[0] = "X";	// IPR name
					ret[1] = "1";	// Number of this ipr with this sequence id
					ret[2] = "X";	// Whether or not it is an ipr
					ret[3] = "X";	// Sequence id
					if(line.contains(seperator)){
						String interpro=findInterProInRaw(line);
						if(interpro!=null){
							Project.amountOfIPRs+=1;
							ret[0]=interpro;
							ret[2]="IPR";
						}
						String tmp=line.substring(line.indexOf(seperator)+1);
						if(tmp.contains(seperator)){
							ret[1]=tmp.substring(0,tmp.indexOf(seperator));
							ret[3]=tmp.substring(line.indexOf(seperator)+1);
						}
						else if(tmp!=null){
							ret[1]=tmp;
						}
					}
					else{
						if(line.contains("\t")){
							String repSeq = line.substring(0,line.indexOf("\t"));
							ret[3] = repSeq;
						}
						String interpro=findInterProInRaw(line);
						if(interpro!=null){
							Project.amountOfIPRs+=1;
//							System.out.println("IPR sucessfully saved");
							ret[0]=interpro;
							ret[2]="IPR";
						} else{
//							System.out.println("IPR save was unsuccessful");
						}
					}
					return ret;
				  }
/*  232:     */   
/*  233:     */   private String findPfamAndRepSeqInRaw(String input)
/*  234:     */   {// Finds the PFam and sequence ID in a line of raw data
/*  235: 226 */     String Pfam = "";
/*  236: 227 */     String tmp = input;
/*  237: 230 */     while (tmp.contains("PF"))
/*  238:     */     {
/*  239: 231 */       Pfam = tmp.substring(tmp.indexOf("PF"), tmp.indexOf("PF") + 7);
/*  240: 232 */       String repSeq = tmp.substring(tmp.indexOf("PF") + 7);
/*  241: 233 */       char firstChar = repSeq.charAt(0);
/*  242: 234 */       while ((isNumber(firstChar)) || (firstChar == '.') || (firstChar == ' '))
/*  243:     */       {
/*  244: 236 */         repSeq = repSeq.substring(1);
/*  245: 237 */         firstChar = repSeq.charAt(0);
/*  246:     */       }
/*  247: 239 */       if (isPfambool(Pfam)) {
/*  248: 240 */         return Pfam + "-" + repSeq.substring(0, repSeq.indexOf("-"));
/*  249:     */       }
/*  250: 243 */       tmp = tmp.substring(tmp.indexOf("PF") + 7);
/*  251:     */     }
/*  252: 246 */     return "";
/*  253:     */   }
				  private String findInterProInRaw(String input){
				  	String interpro="";
				  	String tmp = input;
				  	while (tmp.contains("IPR"))
				  	{
				  		interpro=tmp.substring(tmp.indexOf("IPR"), tmp.indexOf("IPR") + 9);
				  		if (interpro.matches("IPR[0-9][0-9][0-9][0-9][0-9][0-9]")){
//				  			System.out.println("IPR Found Successfully");
				  			return interpro;
				  		} else{
				  			tmp=tmp.substring(tmp.indexOf("IPR")+3);
				  		}
				  	}
//				  	System.out.println("IPR not found");
				  	return null;
				  }

/*  254:     */   
/*  255:     */   public String[] getEnzFromSample(String input)
/*  256:     */   {//retrieves ec/pfam and sequence ids from the three column, two column, and matrix data files.
					if(input.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")){
						return getEnzFromInterPro(input);
					}
//					System.out.println("Sample");
/*  257: 253 */     String seperator = "";
/*  258: 254 */     String tmp = input;
/*  259: 256 */     if (input.contains(",")) {
/*  260: 258 */       seperator = ",";
/*  261:     */     }
/*  262: 260 */     if ((seperator.isEmpty()) && (input.contains("_"))) {
/*  263: 262 */       seperator = "_";
/*  264:     */     }
/*  265: 264 */     if ((seperator.isEmpty()) && (input.contains("\t"))) {
/*  266: 266 */       seperator = "\t";
/*  267:     */     }
					String[] ret = new String[4];
					ret[0] = "X";//ec name
/*  156:     */     ret[1] = "1";//number of this ec with this sequence id
/*  158:     */     ret[2] = "X";//whether or not it is a pf, ipr or if it is an ec 
/*  160:     */     ret[3] = "X";//sequence id
/*  277: 277 */     if (input.isEmpty()) {
/*  278: 278 */       return ret;
/*  279:     */     }
					if(!seperator.isEmpty()){
						if (input.contains(seperator)){
						  if((input.length()-input.replace(seperator, "").length())==2||(input.length()-input.replace(seperator, "").length())==1)//this determines that the input is of the two or three column format
/*  280: 281 */     	  {
/*  282: 282 */     		  ret[0] = input.substring(0, input.indexOf(seperator));
/*  283: 283 */     		  tmp = input.substring(input.indexOf(seperator) + 1);
/*  284: 285 */     		  if (isEc(ret[0])) {
/*  285: 286 */     		    ret[2] = "EC";
								if(!numEcs.contains(ret[0])){//adds to the total number of ecs
									numEcs.add(ret[0]);
								}
/*  286: 289 */     		  } else if (ret[0].contains(".")) {
/*  287: 290 */     		    ret[0] = ret[0].substring(0, ret[0].indexOf("."));
/*  288:     */     		  }
/*  289: 293 */     		  String pfam = isPfam(ret[0]);
/*  290: 295 */     		  if (pfam != null)
/*  291:     */     		  {
/*  292: 296 */     		    ret[0] = pfam;
/*  293: 297 */     		    ret[2] = "Pf";
								if(!totalnumPfams.contains(ret[0])){//adds to the total number of pfams
									totalnumPfams.add(ret[0]);
								}
								String[] nump=ret.clone();
								ArrayList<String[]> nump2;
								nump2=convertPfam(nump);
								for(int i=0;i<nump2.size();i++){//adds to the total number of converted pfams
								  if(!numPfams.contains(nump2.get(i)[0])){
									numPfams.add(nump2.get(i)[0]);
//									System.out.println(nump2.get(i)[0]);
								  }
								}
/*  294:     */     		  }
							  String interpro = isInterPro(ret[0]);
							  if (interpro != null){
							  	ret[0] = interpro;
							  	ret[2] = "IPR";
							  }
/*  295: 299 */     		  if (tmp.contains(seperator))
/*  296:     */     		  {
/*  297: 300 */     		    ret[1] = tmp.substring(0, tmp.indexOf(seperator));
/*  298: 301 */     		    tmp = tmp.substring(tmp.indexOf(seperator) + 1);
/*  299: 302 */     		    if (!tmp.isEmpty()) {
/*  300: 303 */     		      ret[3] = tmp;
/*  301:     */     		    } else {
/*  302: 306 */     		      return ret;
/*  303:     */     		    }
/*  304:     */     		  }
/*  305:     */     		  else
/*  306:     */     		  {
/*  307: 310 */     		    ret[1] = tmp;
/*  308:     */     		    
/*  309: 312 */     		    return ret;
/*  310:     */     		  }
							  
/*  311:     */     	  }
						  
						  else if((input.length()-input.replace(seperator, "").length())>3){//Matrix format. Still nothing here to input the matrix format. Use the Load Ec-matrix button.
						  	
						  }
						}
					}
					else if(isPfambool(input)||isEc(input)||isInterProBool(input)){//one column format
//						System.out.println("One column");
						ret[0] = input;
/*  284: 285 */     	if (isEc(ret[0])) {
/*  285: 286 */     	  ret[2] = "EC";
						  if(!numEcs.contains(ret[0])){//adds to the total number of ecs
					  		numEcs.add(ret[0]);
					  	  }
/*  286: 289 */     	}
/*  289: 293 */     	String pfam = isPfam(ret[0]);
/*  290: 295 */     	if (pfam != null)
/*  291:     */     	{
/*  292: 296 */     	  ret[0] = pfam;
/*  293: 297 */     	  ret[2] = "Pf";
						  if(!totalnumPfams.contains(ret[0])){//adds to the total number of pfams
							totalnumPfams.add(ret[0]);
						  }
						  String[] nump=ret.clone();
						  ArrayList<String[]> nump2;
						  nump2=convertPfam(nump);
						  for(int i=0;i<nump2.size();i++){//adds to the total number of converted pfams
						    if(!numPfams.contains(nump2.get(i)[0])){
						  	  numPfams.add(nump2.get(i)[0]);
//							  System.out.println(nump2.get(i)[0]);
						    }
						  }
/*  294:     */     	}
						String interpro = isInterPro(ret[0]);
						if (interpro != null){
//						  System.out.println("One column IPR");
						  ret[0] = interpro;
						  ret[1] = "1";
						  ret[2] = "IPR";
						}
					}
					else
/*  313:     */     {
/*  314: 316 */       ret[0] = input;
/*  315: 317 */       return ret;
/*  316:     */     }
/*  317: 319 */     return ret;
/*  318:     */   }
/*  319:     */   
/*  320:     */   public String isPfam(String pfam)
/*  321:     */   {//if the input string is determined to be a pfam then the method outputs the pfam
/*  322: 323 */     String tmp = pfam;
/*  323: 324 */     if (tmp.contains("PF"))
/*  324:     */     {
/*  325: 326 */       tmp = tmp.substring(tmp.indexOf("PF"));
/*  326: 328 */       if (tmp.length() == 7)
/*  327:     */       {
/*  328: 329 */         if (isNumber(tmp.substring(2))) {
/*  329: 330 */           return tmp;
/*  330:     */         }
/*  331:     */       }
/*  332: 334 */       else if (tmp.length() >= 7)
/*  333:     */       {
/*  334: 335 */         tmp = tmp.substring(2);
/*  335: 336 */         isPfam(tmp);
/*  336:     */       }
/*  337:     */     }
/*  338: 341 */     return null;
/*  339:     */   }

				  public String isInterPro(String interpro)
				  { //if the input string is determined to be an interPro the the method outputs the interPro. Else it returns null.
				  	String tmp = interpro;
				  	if(tmp.contains("IPR")){
				  		tmp = tmp.substring(tmp.indexOf("IPR"));
				  		if(tmp.length() == 9 ){
				  			if(isNumber(tmp.substring(3))){
				  				return tmp;
				  			}
				  		}
				  		else if(tmp.length() >= 9 ){
				  			tmp = tmp.substring(3);
				  			isInterPro(tmp);
				  		}
				  	} 
				  	return null;
				  }
/*  340:     */   
/*  341:     */   public boolean isPfambool(String pfam)
/*  342:     */   {// returns a boolean variable which is the awnser to whether or not the string is a pfam
/*  343: 344 */     String tmp = pfam;
/*  344: 345 */     if ((tmp.startsWith("PF")) && 
/*  345: 346 */       (tmp.length() == 7) && 
/*  346: 347 */       (isNumber(tmp.substring(2)))) {
/*  347: 348 */       return true;
/*  348:     */     }
/*  349: 352 */     return false;
/*  350:     */   }
				  public boolean isInterProBool(String interpro){//returns a boolean variable which is the awnser to whether or not the string is an interPro
				  	String tmp = interpro;
				  	if( (tmp.startsWith("IPR")) && (tmp.length() == 9) && (isNumber(tmp.substring(3))) ){
				  		return true;
				  	}
				  	return false;
				  }
/*  351:     */   
/*  352:     */   public boolean isEc(String ec)
/*  353:     */   {//method returns a boolean variable stating whether or not the input string is a complete ec
/*  354: 355 */     if (ec == null) {
/*  355: 355 */       return false;
/*  356:     */     }
/*  357: 356 */     if (!ec.contains(".")) {
/*  358: 356 */       return false;
/*  359:     */     }
/*  360: 358 */     String ecPart = "";
/*  361: 359 */     String ecRest = ec;
/*  362: 360 */     ecPart = ecRest.substring(0, ecRest.indexOf("."));
/*  363: 361 */     ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
/*  364: 362 */     if (!isNumber(ecPart)) {
/*  365: 363 */       return false;
/*  366:     */     }
/*  367: 366 */     if (!ecRest.contains(".")) {
/*  368: 367 */       return false;
/*  369:     */     }
/*  370: 370 */     ecPart = ecRest.substring(0, ecRest.indexOf("."));
/*  371: 371 */     ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
/*  372: 372 */     if (!isNumber(ecPart)) {
/*  373: 373 */       return false;
/*  374:     */     }
/*  375: 376 */     if (!ecRest.contains(".")) {
/*  376: 377 */       return false;
/*  377:     */     }
/*  378: 379 */     ecPart = ecRest.substring(0, ecRest.indexOf("."));
/*  379: 380 */     ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
/*  380: 381 */     if (!isNumber(ecPart))
/*  381:     */     {
/*  382: 382 */       System.out.println("5 " + ec);
/*  383: 383 */       return false;
/*  384:     */     }
/*  385: 386 */     if (isNumber(ecRest)) {
/*  386: 386 */       return true;
/*  387:     */     }
/*  388: 388 */     System.out.println("1" + ec);
/*  389: 389 */     return false;
/*  390:     */   }
/*  391:     */   
/*  392:     */   public boolean isNumber(String in)
/*  393:     */   {// Returns whether or not a string is a number
/*  394:     */     try
/*  395:     */     {
/*  396: 396 */       int num = Integer.valueOf(in).intValue();
/*  397:     */     }
/*  398:     */     catch (Exception e)
/*  399:     */     {
/*  400:     */       int num;
/*  401: 399 */       return false;
/*  402:     */     }
/*  403:     */     int num;
/*  404: 401 */     return true;
/*  405:     */   }
/*  406:     */   
/*  407:     */   public boolean isNumber(char c)
/*  408:     */   {// Returns whether or not a char is a number
/*  409: 404 */     if ((c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6') || (c == '7') || (c == '8') || (c == '9')) {
/*  410: 405 */       return true;
/*  411:     */     }
/*  412: 408 */     return false;
/*  413:     */   }
/*  414:     */   
/*  415:     */   public boolean isDot(char c)
/*  416:     */   {
/*  417: 412 */     if (c == '.') {
/*  418: 413 */       return true;
/*  419:     */     }
/*  420: 416 */     return false;
/*  421:     */   }
/*  422:     */   
/*  423:     */   public String getEcNrFromList(String input)
/*  424:     */   {
/*  425: 424 */     String output = "";
/*  426: 425 */     if (input.contains("ec:")) {
/*  427: 426 */       output = input.substring(input.lastIndexOf("ec:") + 3, input
/*  428: 427 */         .length() - 1);
/*  429:     */     }
/*  430: 430 */     return output;
/*  431:     */   }
/*  432:     */   
/*  433:     */   public String getPathwayFromList(String input)
/*  434:     */   {
/*  435: 438 */     String output = "";
/*  436: 439 */     if (input.contains("path:")) {
/*  437: 440 */       output = input.substring(input.indexOf(":") + 1, 
/*  438: 441 */         input.indexOf(":") + 8);
/*  439:     */     }
/*  440: 444 */     return output;
/*  441:     */   }
/*  442:     */   
/*  443:     */   private void addUserNewPathsToSample(Sample sample)
/*  444:     */   {
/*  445: 447 */     if (newUserPathList_.isEmpty()) {
/*  446: 448 */       return;
/*  447:     */     }
/*  448: 451 */     for (int pathCnt = 0; pathCnt < newUserPathList_.size(); pathCnt++) {
/*  449: 452 */       sample.integratePathway((PathwayWithEc)newUserPathList_.get(pathCnt));
/*  450:     */     }
/*  451:     */   }
/*  452:     */   
				  //
/*  453:     */   public void allEcVsPathway()
/*  454:     */   {// Does the parsing of the input files As well as the printing out of statistics at the end. each file is parsed line by line and checked for formating at teach line in order to accomidate for mixed format files. If the file is found to be of the interpro format, however, then this method will call another method to parse the file so that efficiency isnt lost checking the file type at each line. This method then continues
/*  455: 459 */     String zeile = "";
/*  456: 460 */     EcNr ecNr = null;
/*  457:     */     
/*  464: 468 */     int counter = 0;
/*  465: 469 */     this.lFrame_.bigStep("all EC Vs Pathway");
					final long startTime = System.currentTimeMillis();
					outerloop:
/*  466: 474 */     for (int i = 0; i < Project.samples_.size(); i++)// For each of the samples in the project
/*  467:     */     {
/*  468: 475 */       this.lFrame_.bigStep(((Sample)Project.samples_.get(i)).name_);
					  
/*  469: 476 */       Sample sample = (Sample)Project.samples_.get(i);
					  ArrayList<Sample> sampleArray= new ArrayList();	// Needed for the matrix input format

/*  470: 477 */       if (sample.valuesSet) // If the sample has already had its vlaues set then simply add paths to the samples 
/*  471:     */       {
/*  472: 478 */         System.out.println("ValueSet");
/*  473: 479 */         addUserNewPathsToSample(sample);
/*  474:     */       }
/*  475: 483 */       else if (sample.imported) // if the  sample was imported then fill the samples ECs
/*  476:     */       {
/*  477: 484 */         fillSampleEcs((Sample)Project.samples_.get(i), i);
/*  478:     */       }
/*  479: 488 */       else if (!sample.name_.isEmpty()) // If the sample name is empty than the sample hasn't been built yet, so build the sample from the sample file
/*  480:     */       {
/*  481: 489 */         String tmp = ((Sample)Project.samples_.get(i)).fullPath_;
/*  482: 490 */         sample.sample_ = this.reader.readTxt(tmp);
/*  483: 491 */         sample.clearPaths();
/*  484: 492 */         Project.legitSamples.add(Boolean.valueOf(false));
/*  485:     */         try
/*  486:     */         {
//						  System.out.println("Parsing");
/*  487: 494 */           while ((zeile = sample.sample_.readLine()) != null)//This is what does the parsing through the files to build the "Sample attributes"
/*  488:     */           {
							if(zeile.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")||zeile.startsWith(">")){
								i=ParseInterpro(i);

								break;
							}
							if (zeile.startsWith(">")){//Important for interpro input formats where several samples are in the same file each starting off with a line containing the sample name starting with ">"
								if(Project.samples_.get(i).ecs_.isEmpty()){
									Project.samples_.get(i).name_=zeile.substring(zeile.indexOf(">")+1);
									continue;
								}else{
									Color tmpColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
									Sample tmpSample = new Sample(zeile.substring(zeile.indexOf(">")+1), sample.fullPath_, tmpColor);
									tmpSample.legitSample=true;
									tmpSample.inUse = true;
									Project.samples_.add(i+1,tmpSample);
									Project.legitSamples.add(i+1, true);
									i++;
									continue;
								}
							}
/*  489: 497 */             String[] newEnz = getEnzFromSample(zeile);
/*  490: 500 */             if (!enzReadCorrectly(newEnz)) {
/*  491: 501 */               newEnz = getEnzFromRawSample(zeile);
/*  492:     */             }
							if (!enzReadCorrectly(newEnz))
/*  498:     */             {
/*  499: 513 */               Debug.addnoEnzymeLine(sample.name_ + " " + zeile);
/*  500:     */             }
				            else
				            {
/*  503: 516 */               if (newEnz[2] == "EC") {//if the sequence was already an EC
/*  504: 517 */                 Debug.addEc(sample.name_ + " id: " + newEnz[0] + " repseq: " + newEnz[3] + " amount: " + newEnz[1]);
/*  505:     */               } else {
/*  506: 520 */                 Debug.addPf(sample.name_ + " id: " + newEnz[0] + " repseq: " + newEnz[3] + " amount: " + newEnz[1]);
/*  507:     */               }
								
/*  508: 522 */               counter++;
/*  509: 523 */               this.lFrame_.step(newEnz[0] + ": " + newEnz[1]);
/*  510: 524 */               this.lFrame_.updateCounter(counter);
/*  511: 525 */               if (newEnz[2].equalsIgnoreCase("PF"))// If the sequence was taken in as a pfam.
/*  512:     */               {
/*  513: 526 */                 if (!newEnz[1].isEmpty()) {
/*  514: 527 */                   Project.amountOfPfs += Integer.valueOf(newEnz[1]).intValue();
/*  515:     */                 }
/*  516: 529 */                 ArrayList<String[]> enzL = convertPfam(newEnz);
/*  517: 531 */                 for (int cnt = 0; cnt < enzL.size(); cnt++)
/*  518:     */                 {
/*  519: 532 */                   newEnz = (String[])enzL.get(cnt);
/*  520: 534 */                   if (!newEnz[0].isEmpty())
/*  521:     */                   {
/*  522: 535 */                     ecNr = new EcNr(newEnz);
/*  523:     */                     
/*  524: 537 */                     EcWithPathway ecWP = null;
/*  525: 538 */                     if (!ecNr.type_.contentEquals("X"))
/*  526:     */                     {
/*  527: 541 */                       if ((ecNr.type_.contentEquals("EC")) && (isEc(ecNr.name_)))
/*  528:     */                       {
/*  529: 542 */                         Project.samples_.get(i).addConvStats(new ConvertStat(newEnz[3], ecNr.name_, 0, ecNr.amount_, 0));
/*  530: 543 */                         ecWP = findEcWPath(ecNr);
/*  531: 544 */                         this.lFrame_.step("converted" + newEnz[0]);
//										if(!numOfConvertedPFs.contains(ecNr.name_)){
//											numOfConvertedPFs.add(ecNr.name_);
//										}
//*  532: 545 */                         Project.numOfConvertedPFs += ecNr.amount_;
/*  533:     */                       }
/*  534: 547 */                       if (ecWP != null)
/*  535:     */                       {
/*  536: 549 */                         if (!ecNr.isCompleteEc()) {
/*  537: 550 */                           ecNr.incomplete = true;
/*  538:     */                         }
/*  539: 552 */                         if (isEc(ecNr.name_))
/*  540:     */                         {
/*  541: 553 */                           Project.samples_.get(i).addEc(new EcWithPathway(ecWP, ecNr));
//										  if(!numOfConvPfsUsable.contains(ecNr.name_)){
//											numOfConvPfsUsable.add(ecNr.name_);
//										  }
//*  542: 554 */                           Project.numOfConvPfsUsable += ecNr.amount_;
/*  543: 555 */                           Project.legitSamples.remove(i);
/*  544: 556 */                           Project.legitSamples.add(i, Boolean.valueOf(true));
/*  545:     */                         }
/*  546:     */                       }
/*  547:     */                       else
/*  548:     */                       {
/*  549: 561 */                         if (!ecNr.isCompleteEc()) {
/*  550: 562 */                           ecNr.incomplete = true;
/*  551:     */                         }
/*  552: 564 */                         ecNr.unmapped = true;
/*  553: 565 */                         EcWithPathway unmatched = new EcWithPathway(ecNr);
/*  554: 566 */                         unmatched.addPathway((Pathway)getPathwayList_().get(this.unmatchedIndex));
/*  555: 567 */                         Project.samples_.get(i).addEc(unmatched);
/*  556:     */                       }
/*  557:     */                     }
/*  558:     */                   }
/*  559:     */                 }
/*  560:     */               }
//							  else if (newEnz[2].equalsIgnoreCase("IPR")){//if the sequence was taken in as an Interpro.
//							  	System.out.println("newEnz[2].equalsIgnoreCase(\"IPR\")");
//							  	System.out.println("IPR name: "+newEnz[0]);
//							  	ArrayList<String[]> enzL = convertInterpro(newEnz);//If you chance this to "enzL = convertInterproOld(newEnz)" Then it will change from direct to indirect mapping of Interpro reads
//							  	System.out.println("enzL size: "+enzL.size());
//							  	for(int cnt=0;cnt<enzL.size();cnt++){
//							  	  newEnz = (String[])enzL.get(cnt);
//							  	  System.out.println("Looping for IPR conversions to be set into samples");
//							  	  if (!newEnz[0].isEmpty())
//*  521:     */                   {
//									System.out.println("IPR not empty");
//*  522: 535 */                     ecNr = new EcNr(newEnz);
//*  523:     */                     
//*  524: 537 */                     EcWithPathway ecWP = null;
//*  525: 538 */                     if (!ecNr.type_.contentEquals("X"))
//*  526:     */                     {
//*  527: 541 */                       if ((ecNr.type_.contentEquals("EC")) && (isEc(ecNr.name_)))
//*  528:     */                       {
//										System.out.println("Conversion has given good results");
//*  529: 542 */                         Project.samples_.get(i).addConvStats(new ConvertStat(newEnz[3], ecNr.name_, 0, ecNr.amount_, 0));
//*  530: 543 */                         ecWP = findEcWPath(ecNr);
//*  531: 544 */                         this.lFrame_.step("converted" + newEnz[0]);
//*  533:     */                       }
//*  534: 547 */                       if (ecWP != null)
//*  535:     */                       {
//										System.out.println("EC Has Pathway");
//*  536: 549 */                         if (!ecNr.isCompleteEc()) {
//*  537: 550 */                           ecNr.incomplete = true;
//*  538:     */                         }
//*  539: 552 */                         if (isEc(ecNr.name_))
//*  540:     */                         {
//										  System.out.println("EC added");
//*  541: 553 */                           Project.samples_.get(i).addEc(new EcWithPathway(ecWP, ecNr));
//*  543: 555 */                           Project.legitSamples.remove(i);
//*  544: 556 */                           Project.legitSamples.add(i, Boolean.valueOf(true));
//*  545:     */                         }
//*  546:     */                       }
//*  547:     */                       else
//*  548:     */                       {
//*  549: 561 */                         if (!ecNr.isCompleteEc()) {
//*  550: 562 */                           ecNr.incomplete = true;
//*  551:     */                         }
//*  552: 564 */                         ecNr.unmapped = true;
//*  553: 565 */                         EcWithPathway unmatched = new EcWithPathway(ecNr);
//*  554: 566 */                         unmatched.addPathway((Pathway)getPathwayList_().get(this.unmatchedIndex));
//*  555: 567 */                         Project.samples_.get(i).addEc(unmatched);
//										System.out.println("Unmatched EC added");

//*  556:     */                       }
//*  557:     */                     }
									
//*  558:     */                   }
//								  System.out.println("IPR empty");
//
//							  	}
//							  }
/*  561: 574 */               else if (!newEnz[0].isEmpty())
/*  562:     */               {
/*  563: 575 */                 ecNr = new EcNr(newEnz);
/*  564: 577 */                 if (ecNr.couldBeEc())
/*  565:     */                 {
//								  if(!amountOfEcs.contains(ecNr.name_)){
//									amountOfEcs.add(ecNr.name_);
//								  }
//*  566: 578 */                   Project.amountOfEcs += ecNr.amount_;
/*  567: 580 */                   if (!ecNr.isCompleteEc()) {
/*  568: 581 */                     ecNr.incomplete = true;
/*  569:     */                   }
/*  570: 584 */                   Project.samples_.get(i).addConvStats(new ConvertStat(newEnz[3], ecNr.name_, ecNr.amount_, 0, 0));
/*  571: 585 */                   EcWithPathway ecWP = findEcWPath(ecNr);
/*  572: 587 */                   if (ecWP != null)
/*  573:     */                   {
/*  574: 588 */                     Project.samples_.get(i).addEc(new EcWithPathway(ecWP, ecNr));
//									if(!numOfUsableEcs.contains(ecNr.name_)){
//										numOfUsableEcs.add(ecNr.name_);
//								  	}
//*  575: 589 */                     Project.numOfUsableEcs += ecNr.amount_;
/*  576: 590 */                     Project.legitSamples.remove(i);
/*  577: 591 */                     Project.legitSamples.add(i, Boolean.valueOf(true));
/*  578:     */                   }
/*  579:     */                   else
/*  580:     */                   {
/*  581: 595 */                     if (!ecNr.isCompleteEc()) {
/*  582: 596 */                       ecNr.incomplete = true;
/*  583:     */                     }
/*  584: 599 */                     ecNr.unmapped = true;
/*  585: 600 */                     EcWithPathway unmatched = new EcWithPathway(ecNr);
/*  586: 601 */                     unmatched.addPathway((Pathway)getPathwayList_().get(this.unmatchedIndex));
/*  587: 602 */                     Project.samples_.get(i).addEc(unmatched);
/*  588:     */                   }
/*  589:     */                 }
/*  590:     */               }
				            }
/*  592:     */           }
/*  593: 608 */           sample.sample_.close();
/*  594:     */         }
/*  595:     */         catch (IOException e)
/*  596:     */         {
/*  597: 614 */           openWarning("Error", "File: " + tmp + " not found");
/*  598: 615 */           e.printStackTrace();
/*  599:     */         }
/*  600: 617 */         Debug.writeOutAll("DebugLists.txt");
/*  601: 618 */         System.out.println("finished allecvsp");
/*  602:     */       }
/*  603:     */     }
					final long endTime = System.currentTimeMillis();
					System.out.println("Total execution time(milliseconds): " + (endTime - startTime) );

			   		ArrayList<String> comptotecs=new ArrayList<String>();
			   		ArrayList<String> allecs=new ArrayList<String>();
			   		ArrayList<String> ecmapped=new ArrayList<String>();
			   		ArrayList<String> pfammapped=new ArrayList<String>();
			   		String testStr1;
			   		String testStr2;
			   		String testStr3;
			   		for (int i=0;i<Project.samples_.size();i++){//adds to the number of total complete ecs
			   			for(int k=0;k<Project.samples_.get(i).ecs_.size();k++){
			   				//System.out.println(Project.samples_.get(i).ecs_.get(k).name_);
			   				if(!comptotecs.contains(Project.samples_.get(i).ecs_.get(k).name_)){
			   					if (Project.samples_.get(i).ecs_.get(k).name_.matches("[0-9].*")) {
								    if(Project.samples_.get(i).ecs_.get(k).name_.contains(".")){
										testStr1=Project.samples_.get(i).ecs_.get(k).name_.substring(Project.samples_.get(i).ecs_.get(k).name_.indexOf(".")+1);
										if(testStr1.matches("[0-9].*")){
											if(testStr1.contains(".")){
												testStr2=testStr1.substring(testStr1.indexOf(".")+1);
												if(testStr2.matches("[0-9].*")){
													if(testStr2.contains(".")){
														testStr3=testStr2.substring(testStr2.indexOf(".")+1);
														if(testStr3.matches("[0-9]*")){
															comptotecs.add(Project.samples_.get(i).ecs_.get(k).name_);
														}
													}
												}
											}
										}
									}
				  				}
			   				}
			   				if(!allecs.contains(Project.samples_.get(i).ecs_.get(k).name_)){//adds to the total number of ecs so that is can later subtract the number of complete ecs to find the number of incomplete ecs
			   					allecs.add(Project.samples_.get(i).ecs_.get(k).name_);
			   				}
			   				if(!ecmapped.contains(Project.samples_.get(i).ecs_.get(k).name_)&&numEcs.contains(Project.samples_.get(i).ecs_.get(k).name_)&&!Project.samples_.get(i).ecs_.get(k).unmapped){
			   					ecmapped.add(Project.samples_.get(i).ecs_.get(k).name_); //adds to the number of mapped ecs
			   				}
			   				if(!pfammapped.contains(Project.samples_.get(i).ecs_.get(k).name_)&&numPfams.contains(Project.samples_.get(i).ecs_.get(k).name_)&&!Project.samples_.get(i).ecs_.get(k).unmapped){
			   					pfammapped.add(Project.samples_.get(i).ecs_.get(k).name_);//adds to the number of mapped pfams
			   				}
			   	 		}
			   		}
			   		int completepfams=0;
			   		int completeecs=0;
			   		for(int i=0;i<numPfams.size();i++){
			   			if (numPfams.get(i).matches("[0-9].*")) {
						    if(numPfams.get(i).contains(".")){
								testStr1=numPfams.get(i).substring(numPfams.get(i).indexOf(".")+1);
								if(testStr1.matches("[0-9].*")){
									if(testStr1.contains(".")){
										testStr2=testStr1.substring(testStr1.indexOf(".")+1);
										if(testStr2.matches("[0-9].*")){
											if(testStr2.contains(".")){
												testStr3=testStr2.substring(testStr2.indexOf(".")+1);
												if(testStr3.matches("[0-9]*")){
													completepfams++;//adds to the number of complete pfams
												}
											}
										}
									}
								}
							}
/* 195:    */     		}
			   		}
			   		for(int i=0;i<numEcs.size();i++){
						if (numEcs.get(i).matches("[0-9].*")) {
						    if(numEcs.get(i).contains(".")){
								testStr1=numEcs.get(i).substring(numEcs.get(i).indexOf(".")+1);
								if(testStr1.matches("[0-9].*")){
									if(testStr1.contains(".")){
										testStr2=testStr1.substring(testStr1.indexOf(".")+1);
										if(testStr2.matches("[0-9].*")){
											if(testStr2.contains(".")){
												testStr3=testStr2.substring(testStr2.indexOf(".")+1);
												if(testStr3.matches("[0-9]*")){
													completeecs++;//adds to the number of complete ecs
												}
											}
										}
									}
								}
							}
/* 195:    */     		}
			   		}

					int incomptotecs=allecs.size()-comptotecs.size();

					if(!Project.loaded){//adds a failsafe for whether of not the data was loaded from a project file, as the statistics are already saved there
						Project.amountOfEcs=numEcs.size();
						Project.numOfCompleteEcs=completeecs;
						Project.numOfMappedEcs=ecmapped.size();
						Project.amountOfPfs=totalnumPfams.size();
						Project.numOfConvertedPFs=numPfams.size();
						Project.numOfConvPfsComplete=completepfams;
						Project.numOfConvPfsMapped=pfammapped.size();
					}
//					calls the Help Frame to make the Project Summary window
/*  604: 621 */     String Text = "<html><body>Finished processing the samples"+
/*  617: 634 */       "<br>" + 
/*  617: 634 */       "<br>" + 
					  "Total complete ECs (including converted pfams):\t"+comptotecs.size()+
					  "<br>"+
					  "Total incomplete ECs (including converted pfams):\t"+incomptotecs+
					  "<br>"+
					  "ECs:\t"+Project.amountOfEcs+
					  "<br>"+
					  "Complete ECs:\t"+Project.numOfCompleteEcs+
					  "<br>"+
					  "Mapped ECs:\t"+Project.numOfMappedEcs+
					  "<br>"+
					  "Pfams:\t"+Project.amountOfPfs+
					  "<br>"+
					  "Converted Pfams:\t"+Project.numOfConvertedPFs+
			   		  "<br>"+
			   		  "Complete converted Pfams:\t"+Project.numOfConvPfsComplete+
			   		  "<br>"+
			   		  "Mapped converted Pfams:\t"+Project.numOfConvPfsMapped+
			   		  "<br>"+
					  "Interpros:\t"+Project.amountOfIPRs+
					  "<br>"+
					  "Converted Interpros:\t"+Project.numOfConvertedIPRs+
			   		  "<br>"+
			   		  "Complete converted Interpros:\t"+Project.numOfConvIPRsComplete+
			   		  "<br>"+
			   		  "Mapped converted Interpros:\t"+Project.numOfConvIPRsMapped+
			   		  "<br><br>"+
			   		   "<br>"+
/*  619: 636 */       "Sample that seem to be valid: " + "<br>";
/*  620: 637 */     for (int i = 0; i < Project.samples_.size(); i++)
/*  621:     */     {
/*  622: 638 */       Text = Text + (i + 1) + ":" + Project.samples_.get(i).legitSample + " ";
/*  623: 639 */       if ((i % 5 == 0) && (i != 0)) {
/*  624: 640 */         Text = Text + "<br>";
/*  625:     */       }
/*  626:     */     }
/*  627: 643 */     Text = Text + "<br></body></html>";
/*  628:     */     
/*  629:     */ 
/*  630: 646 */     HelpFrame helpF = new HelpFrame(Text);
/*  631: 647 */     System.out.println("---------------------------------------------------------------------------------------------");
/*  632: 648 */     System.out.println("Finished processing the samples");
					System.out.println("Total icomplete ECs (including converted pfams): "+comptotecs.size());
					System.out.println("Total incomplete ECs (including converted pfams): "+incomptotecs);
					System.out.println("ECs: "+Project.amountOfEcs);
					System.out.println("Complete ECs: "+Project.numOfCompleteEcs);
					System.out.println("Mapped ECs: "+Project.numOfMappedEcs);
					System.out.println("Pfams: "+Project.amountOfPfs);
					System.out.println("Converted Pfams: "+Project.numOfConvertedPFs);
					System.out.println("Complete converted Pfams: "+Project.numOfConvPfsComplete);
					System.out.println("Mapped converted Pfams: "+Project.numOfConvPfsMapped);
	
/*  637: 653 */     System.out.println("Sample that seem to be valid:");
/*  638: 654 */     for (int i = 0; i < Project.samples_.size(); i++) {
/*  639: 655 */       System.out.println("Sample: " + (i + 1) + ":" + ((Sample)Project.samples_.get(i)).name_ + " " + ((Sample)Project.samples_.get(i)).legitSample);
/*  640:     */     }
/*  641: 657 */     newUserPathList_ = new ArrayList();
/*  642:     */   }
/*  643:     */   

				  public int ParseInterpro(int count) 
				  {// Parses a file of the InterPro format and pulls out the samples, IPRs(which get converted) and sequence IDs 
//				  	System.out.println("Parse Interpro");
				  	String zeile="";
				  	int i=count;
				  	for (i = count; i < Project.samples_.size(); i++){
				  		Sample sample = Project.samples_.get(i);
				  		String tmp = ((Sample)Project.samples_.get(i)).fullPath_;
				  		sample.sample_ = this.reader.readTxt(tmp);
				  		try{
				  			while((zeile = sample.sample_.readLine()) != null){
				  				if (zeile.startsWith(">")){//Important for interpro input formats where several samples are in the same file each starting off with a line containing the sample name starting with ">"
									if(Project.samples_.get(i).ecs_.isEmpty()){
										Project.samples_.get(i).name_=zeile.substring(zeile.indexOf(">")+1);
										continue;
									}else{
										Color tmpColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
										Sample tmpSample = new Sample(zeile.substring(zeile.indexOf(">")+1), sample.fullPath_, tmpColor);
										tmpSample.legitSample=true;
										tmpSample.inUse = true;
										Project.samples_.add(i+1,tmpSample);
										Project.legitSamples.add(i+1, true);
										i++;
//										System.out.println("New Sample Added");
										continue;
									}
								}
								
								else if(zeile.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")){
									
									String[] newEnz = getEnzFromInterPro(zeile);
									if (newEnz[2].equalsIgnoreCase("IPR")){
										ArrayList<String[]> enzL = convertInterpro(newEnz);//If you chance this to "enzL = convertInterproOld(newEnz)" Then it will change from direct to indirect mapping of Interpro reads
										for(int cnt=0;cnt<enzL.size();cnt++){
											newEnz = (String[])enzL.get(cnt);
											if (!newEnz[0].isEmpty()){
				        				        EcNr ecNr = new EcNr(newEnz);
				        				            
				        				        EcWithPathway ecWP = null;
				        				        if (!ecNr.type_.contentEquals("X")){
				        				        	if ((ecNr.type_.contentEquals("EC")) && (isEc(ecNr.name_))){
				        				                Project.samples_.get(i).addConvStats(new ConvertStat(newEnz[3], ecNr.name_, 0, ecNr.amount_, 0));
				        				                ecWP = findEcWPath(ecNr);
				        				                this.lFrame_.step("converted" + newEnz[0]);
				        				                
				        		    		        }
				        		    		        if (ecWP != null){
//														System.out.println("EC Has Pathway");
				        		    		        	Project.numOfConvIPRsMapped+=1;
				        		   		 	            if (!ecNr.isCompleteEc()) {
				        		    		              ecNr.incomplete = true;
				        		    		            }
				        		    	            	if (isEc(ecNr.name_)){
//													  		System.out.println("EC added");
				        		    	            		Project.numOfConvIPRsComplete+=1;
				        		    	              		Project.samples_.get(i).addEc(new EcWithPathway(ecWP, ecNr));
				        		    	              		Project.legitSamples.remove(i);
				        		    	              		Project.legitSamples.add(i, Boolean.valueOf(true));
				        		    	            	}
				        		    		        	} else{
				        		        	        	if (!ecNr.isCompleteEc()) {
				        		        	        	  ecNr.incomplete = true;
				        		            	    	}
				        		        	        	ecNr.unmapped = true;
				        		            		    EcWithPathway unmatched = new EcWithPathway(ecNr);
				        		        		        unmatched.addPathway((Pathway)getPathwayList_().get(this.unmatchedIndex));
				        		   		    	        Project.samples_.get(i).addEc(unmatched);
//														System.out.println("Unmatched EC added");

					        			            }
					        			        }
				        			    	}
//				        			    	else{
//				        			    		System.out.println("newEnz is empty");
//				        			    	}
										}
									}
								
								}
							}
						}
				  		catch (IOException e)
/*  596:     */         {
/*  597: 614 */           openWarning("Error", "File: " + tmp + " not found");
/*  598: 615 */           e.printStackTrace();
/*  599:     */         }
				  	}
				  	return i;
				  }



/*  644:     */   public boolean enzReadCorrectly(String[] newEnz)
/*  645:     */   {// returns true if element 0 of the array is either ec, ipr, or pfam and element 1 is an int
					if(newEnz==null){
						return false;
					}
/*  646: 660 */     boolean ret = false;
/*  647: 662 */     if ((isEc(newEnz[0])) || (isPfambool(newEnz[0])) || (isInterProBool(newEnz[0]))) {
/*  648: 663 */       ret = true;
/*  649:     */     } else {
					  System.out.println("enz read incorrectly");
/*  650: 666 */       return false;
/*  651:     */     }
/*  652:     */     try
/*  653:     */     {
/*  654: 669 */       int num = Integer.valueOf(newEnz[1]).intValue();
/*  655:     */     }
/*  656:     */     catch (Exception e)
/*  657:     */     {
/*  658:     */       int num;
/*  659: 672 */       ret = false;
/*  660:     */     }
					if(ret){
						System.out.println("enz read correctly");
					} else{
						System.out.println("enz read incorrectly");
					}
/*  661: 675 */     return ret;
/*  662:     */   }
/*  663:     */   
/*  664:     */   public ArrayList<String[]> convertPfam(String[] pfam)
/*  665:     */   {//actually does the computation of converting a pfam into an ec

/*  666: 690 */     ArrayList<String[]> retList = new ArrayList();
/*  667: 691 */     this.pfamToRnToEc_ = this.reader.readTxt(pfamToRnToEcPath_);//this is the conversion file 
/*  668:     */     
/*  669: 693 */     String zeile = "";
/*  670: 694 */     String[] tmpNr = new String[4];
/*  671: 695 */     tmpNr[3] = pfam[3];
/*  672: 696 */     int pfamNr = Integer.valueOf(pfam[0].substring(2)).intValue();
/*  673:     */     
/*  674:     */ 
/*  675:     */ 	if(!totalnumPfams.contains(pfam[0])&&pfam[2].equalsIgnoreCase("PF")){
						totalnumPfams.add(pfam[0]);
					}
/*  676:     */ 
/*  677:     */ 
/*  678:     */ 
/*  679: 703 */     this.numOfPFams += 1;
/*  680:     */     try
/*  681:     */     {
/*  682: 710 */       while ((zeile = this.pfamToRnToEc_.readLine()) != null) {
/*  683: 711 */         if (!zeile.startsWith("!"))
/*  684:     */         {
/*  685: 714 */           if (pfamNr == Integer.valueOf(zeile.substring(zeile.indexOf(":PF") + 3, zeile.indexOf(":PF") + 8)).intValue())
/*  686:     */           {
/*  687: 715 */             tmpNr = new String[4];
/*  688: 716 */             tmpNr[0] = zeile.substring(zeile.indexOf(";") + 1);
/*  689: 717 */             tmpNr[1] = pfam[1];
/*  690: 718 */             if (tmpNr[0].startsWith("R")) {
/*  691: 719 */               tmpNr[2] = "Rn";
/*  692:     */             } else {
/*  693: 722 */               tmpNr[2] = "EC";
/*  694:     */             }
/*  695: 724 */             tmpNr[3] = pfam[3];
/*  696: 725 */             this.numOfPfamsToGo += 1;
/*  697: 726 */             if (tmpNr[2].contentEquals("EC")) {
/*  698: 727 */               retList.add(tmpNr);
							  if(!numPfams.contains(tmpNr[0])){
								numPfams.add(tmpNr[0]);
								System.out.println(tmpNr[0]);
						  	  }
/*  699:     */             }
/*  700:     */           }
/*  701: 730 */           if (pfamNr < Integer.valueOf(zeile.substring(zeile.indexOf(":PF") + 3, zeile.indexOf(":PF") + 8)).intValue()) {
/*  702:     */             break;
/*  703:     */           }
/*  704:     */         }
/*  705:     */       }
/*  706: 734 */       if (retList.size() == 0) {
/*  707: 735 */         this.numOfMissedpfams += 1;
/*  708:     */       }
/*  709: 737 */       this.pfamToRnToEc_.close();
/*  710:     */     }
/*  711:     */     catch (IOException e)
/*  712:     */     {
/*  713: 741 */       openWarning("Error", "File: " + pfamToRnToEcPath_ + " not found");
/*  714: 742 */       e.printStackTrace();
/*  715:     */     }
/*  716: 745 */     return retList;
/*  717:     */   }

				  private ArrayList<String[]> convertInterpro(String[] interpro){//this is the conversion step using ipr->kegg.
				  	ArrayList<String[]> retList = new ArrayList(); 
				    if(this.IPRToECHash.isEmpty()){
				    	DigitizeConversionFiles();
				    }
				    
				    String zeile = "";
				    String[] tmpNr = new String[4];
				    tmpNr[3] = interpro[3];
				    String interproNr = interpro[0];

				    if(this.IPRToECHash.containsKey(interproNr)){
				    	for(int i=0;i<IPRToECHash.get(interproNr).size();i++){
				    		tmpNr[0]=IPRToECHash.get(interproNr).get(i);
				    		tmpNr[1]=interpro[1];
				    		tmpNr[2]="EC";
				    		tmpNr[3]=interpro[3];
				    		Project.numOfConvertedIPRs+=1;
				    		retList.add(tmpNr);
				    	}
				    }
				    
//				    if(!retList.isEmpty()){
//				    	System.out.println("Conversion Successful");
//				    	for(int i=0;i<retList.size();i++){
//				    		String[] strings=retList.get(i);
//				    		System.out.println("tmpNr[0],[1],[2],[3]: "+strings[0]+","+strings[1]+","+strings[2]+","+strings[3] );
//				    	}
//				    } else{
//				    	System.out.println("Conversion unsuccessful");
//				    }
				    return retList;
				  }

				  private ArrayList<String[]> convertInterproOld(String[] interpro){//this is the ipr conversion step using, ipr->go, go->ec. I
					if(IPRToGOHash.isEmpty() || GOToECHash.isEmpty()){
						DigitizeConversionFilesOld();
					}
					ArrayList<String[]> retList = new ArrayList();
					ArrayList<String> tmpList = new ArrayList();

					String zeile = "";
					String[] tmpNr = new String[4];
					tmpNr[3] = interpro[3];
					String interproNr = interpro[0];
					
					if(this.IPRToGOHash.containsKey(interproNr)){
						for(int i=0;i<this.IPRToGOHash.get(interproNr).size();i++){
				    		tmpList.add(this.IPRToGOHash.get(interproNr).get(i));
				    	}
					}

//					if(!tmpList.isEmpty()){
//				    	System.out.println("IPR->GO Conversion Successful");
//				    	for(int i=0;i<tmpList.size();i++){
//				    		System.out.println("IPR->GO: "+interproNr+"->"+tmpList.get(i));
//				    	}
//				    }else{
//				    	System.out.println("IPR->GO Conversion unsuccessful");
//				    }
//					System.out.println("tmplist.size(): "+tmpList.size());

					for(int i=0;i<tmpList.size();i++){
						String key = tmpList.get(i);
						if(this.GOToECHash.get(key)!=null&&this.GOToECHash.containsKey(key)){
//							System.out.println("GOToECHash.containsKey: "+key);
							for(int j=0;j<this.GOToECHash.get(key).size();j++){
								tmpNr[0]=this.GOToECHash.get(key).get(j);
				    			tmpNr[1]=interpro[1];
				    			tmpNr[2]="EC";
				    			tmpNr[3]=interpro[3];

//				    			System.out.println("Added to ret list[0],[1],[2],[3]: "+tmpNr[0]+","+tmpNr[1]+","+tmpNr[2]+","+tmpNr[3] );

				    			retList.add(tmpNr);	
							}
						}
					}

//					if(!retList.isEmpty()){
//				    	System.out.println("GO->EC Conversion Successful");
//				    	for(int i=0;i<retList.size();i++){
//				    		String[] strings=retList.get(i);
//				    		System.out.println("GO->EC[0],[1],[2],[3]: "+strings[0]+","+strings[1]+","+strings[2]+","+strings[3] );
//				    	}
//				    } else{
//				    	System.out.println("GO->EC Conversion unsuccessful");
//				    }

					return retList;
				  }	


				  private void DigitizeConversionFiles(){ // Takes the IPR->EC conversion file into memory as a hashtable
				  	this.interproToECTxt_ = this.reader.readTxt(interproToECPath_);
				  	Hashtable<String, ArrayList<String>> tmpIPRToEC = new Hashtable<String, ArrayList<String>>();
				  	String line= "";
				  	try{
				  		while((line = this.interproToECTxt_.readLine()) != null){
				  			if(!line.startsWith("!")){
				  				if(line.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")&&line.contains("+")){
				  					String tmpIPR = line.substring(line.indexOf("IPR"),line.indexOf("IPR")+9);
				  					ArrayList<String> tmpECS= new ArrayList<String>();
				  					String temp= line;
				  					while(temp.contains("+")){
				  						temp= temp.substring(temp.indexOf("+")+1);
				  						String tmpEC = temp;
				  						if(tmpEC.contains("+")){
				  							tmpEC=tmpEC.substring(0,tmpEC.indexOf("+"));
				  						}
				  						tmpECS.add(tmpEC);
				  					}
//				  					System.out.println("Digitized: "+tmpIPR+"Maps to: "+tmpECS.toString());
				  					tmpIPRToEC.put(tmpIPR,tmpECS);
				  				}
				  			}
				  		}
				  	} catch(IOException e){
						openWarning("Error", "File" + interproToECPath_ +" not found");
						e.printStackTrace();
					}
					this.IPRToECHash = tmpIPRToEC;
				  }



				  private void DigitizeConversionFilesOld(){// Takes the IPR->GO and GO->EC conversion files into memory as hashtables
				  	this.interproToGOTxt_ = this.reader.readTxt(interproToGOPath_);
				  	this.ecToGoTxt_=this.reader.readTxt(ecNamesPath);
				  	Hashtable<String, ArrayList<String>> tmpIPRToGO = new Hashtable<String, ArrayList<String>>();
				  	Hashtable<String, ArrayList<String>> tmpECToGO = new Hashtable<String, ArrayList<String>>();

				  	String zeile="";
				  	try{
						while((zeile = this.interproToGOTxt_.readLine()) != null){
							if (!zeile.startsWith("!")){
								if(zeile.contains("InterPro:IPR")&&zeile.contains("; GO:")){
									String tmpIPR = zeile.substring(zeile.indexOf("InterPro:IPR") + 9, zeile.indexOf("InterPro:IPR") + 18);
									String tmpGO = zeile.substring(zeile.indexOf("; GO:")+2, zeile.indexOf("; GO:") + 12);

									if(tmpIPR!=null&&tmpIPRToGO.get(tmpIPR)!=null&&tmpGO!=null){
										ArrayList<String> tmpValue = tmpIPRToGO.get(tmpIPR);
										tmpValue.add(tmpGO);
										tmpIPRToGO.remove(tmpIPR);
										tmpIPRToGO.put(tmpIPR,tmpValue);
									} else if(tmpIPR!=null&&tmpGO!=null){
										ArrayList<String> tmpValue = new ArrayList<String>();
										tmpValue.add(tmpGO);
										tmpIPRToGO.put(tmpIPR,tmpValue);
									}

								}
							}
						}
						this.interproToGOTxt_.close();
					}catch(IOException e){
						openWarning("Error", "File" + interproToGOPath_ +" not found");
						e.printStackTrace();
					}

					try{
						while((zeile = this.ecToGoTxt_.readLine()) != null){
							if (!zeile.startsWith("!")){
								if(zeile.contains("EC:")&&zeile.contains("; GO:")){
									String tmpEC = zeile.substring(zeile.indexOf("EC:")+3, zeile.indexOf(" "));
									String tmpGO = zeile.substring(zeile.indexOf("; GO:")+2, zeile.indexOf("; GO:") + 12);

									if(tmpGO!=null&&tmpECToGO.get(tmpGO)!=null&&tmpEC!=null){
										ArrayList<String> tmpValue = tmpECToGO.get(tmpGO);
										tmpValue.add(tmpEC);
										tmpECToGO.remove(tmpGO);
										tmpECToGO.put(tmpGO,tmpValue);
									} else if(tmpEC!=null&&tmpGO!=null){
										ArrayList<String> tmpValue = new ArrayList<String>();
										tmpValue.add(tmpEC);
										tmpECToGO.put(tmpGO,tmpValue);
									}

								}
							}
						}
						this.ecToGoTxt_.close();
					}catch(IOException e){
						openWarning("Error", "File" + ecNamesPath +" not found");
						e.printStackTrace();
					}
					//System.out.println("IPRToGOHash: "+tmpIPRToGO.toString());
					//System.out.println("GOToECHash: "+tmpECToGO.toString());
					this.IPRToGOHash = tmpIPRToGO;
					this.GOToECHash = tmpECToGO;

				  }


/*  719:     */   private void fillSampleEcs(Sample sample, int sampleIndex)
/*  720:     */   {//
/*  721: 753 */     this.lFrame_.bigStep("Filling " + sample.name_);
/*  722: 754 */     for (int ecCnt = 0; ecCnt < sample.ecs_.size(); ecCnt++)
/*  723:     */     {
/*  724: 755 */       EcWithPathway smpEc = (EcWithPathway)sample.ecs_.get(ecCnt);
/*  725:     */       
/*  726: 757 */       this.lFrame_.step(smpEc.name_);
/*  727: 758 */       if (smpEc.pathways_.size() > 0) {
/*  728: 759 */         smpEc.pathways_ = new ArrayList();
/*  729:     */       }
/*  730: 761 */       EcWithPathway origEc = findEcWPath(smpEc);
/*  731: 762 */       if (origEc != null)
/*  732:     */       {
/*  733: 764 */         for (int pwcnt = 0; pwcnt < origEc.pathways_.size(); pwcnt++) {
/*  734: 765 */           smpEc.pathways_.add(new Pathway((Pathway)origEc.pathways_.get(pwcnt)));
/*  735:     */         }
/*  736: 771 */         smpEc.weight_ = origEc.weight_;
/*  737: 772 */         smpEc.unique_ = origEc.unique_;
/*  738: 773 */         smpEc.bioName_ = origEc.bioName_;
/*  739:     */       }
/*  740:     */       else
/*  741:     */       {
/*  742: 777 */         smpEc.addPathway((Pathway)getPathwayList_().get(this.unmatchedIndex));
/*  743:     */       }
/*  744:     */     }
/*  745:     */   }
/*  746:     */   
/*  747:     */   public void removeEcPfamDoubles()
/*  748:     */   {// 
/*  749: 788 */     String ecNr = "";
/*  750: 789 */     ConvertStat stat = null;
/*  751: 790 */     int amount = 0;
/*  752: 791 */     for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++)
/*  753:     */     {
/*  754: 793 */       Sample samp = (Sample)Project.samples_.get(smpCnt);
/*  755: 794 */       if (!samp.imported) {
/*  756: 797 */         for (int convCnt = 0; convCnt < samp.conversions_.size(); convCnt++)
/*  757:     */         {
/*  758: 798 */           stat = (ConvertStat)samp.conversions_.get(convCnt);
/*  759: 799 */           ecNr = "";
/*  760: 800 */           amount = 0;
/*  761: 801 */           if ((stat.getPfamToEcAmount_() > 0) && (stat.getEcAmount_() > 0))
/*  762:     */           {
/*  763: 802 */             ecNr = stat.getEcNr_();
/*  764: 803 */             if (stat.getPfamToEcAmount_() >= stat.getEcAmount_()) {
/*  765: 804 */               amount = stat.getEcAmount_();
/*  766:     */             } else {
/*  767: 808 */               amount = stat.getEcAmount_() - stat.getPfamToEcAmount_();
/*  768:     */             }
/*  769: 811 */             removeEcByEcnR(samp, ecNr, amount);
/*  770:     */           }
/*  771:     */         }
/*  772:     */       }
/*  773:     */     }
/*  774:     */   }
/*  775:     */   
/*  776:     */   public void removeEcByEcnR(Sample samp, String ecNr, int amount)
/*  777:     */   {// removes an ec by its name from a provided sample
/*  778: 824 */     if (ecNr == null) {
/*  779: 826 */       return;
/*  780:     */     }
/*  781: 828 */     for (int ecCnt = 0; ecCnt < samp.ecs_.size(); ecCnt++)
/*  782:     */     {
/*  783: 829 */       EcNr tmpEcNr = (EcNr)samp.ecs_.get(ecCnt);
/*  784: 830 */       if (tmpEcNr.name_.contentEquals(ecNr))
/*  785:     */       {
/*  786: 831 */         tmpEcNr.amount_ -= amount;
/*  787: 832 */         for (int statCnt = 0; statCnt < tmpEcNr.stats_.size(); statCnt++) {
/*  788: 833 */           ((EcSampleStats)tmpEcNr.stats_.get(statCnt)).amount_ -= amount;
/*  789:     */         }
/*  790: 837 */         if (tmpEcNr.amount_ <= 0) {
/*  791: 838 */           samp.ecs_.remove(ecCnt);
/*  792:     */         }
/*  793: 840 */         this.lFrame_.step("removeEcByEcnR" + ecNr + ":" + amount);
/*  794: 841 */         return;
/*  795:     */       }
/*  796:     */     }
/*  797:     */   }
/*  798:     */   
				  //this will only happen if random sampling is selected
/*  799:     */   private void removeRandomEc()
/*  800:     */   {// removes randome ecs for prepping during random sampling
/*  801: 853 */     Random generator = new Random();
/*  802:     */     
/*  803: 855 */     System.out.println("removeRandomEc");
/*  804:     */     
/*  805: 857 */     int sumOfEcs = 0;
/*  806: 858 */     int[] sumsOfEcsArr = new int[Project.samples_.size()];
/*  807: 859 */     if (Project.samples_.get(0) != null)
/*  808:     */     {
/*  809: 860 */       int minEcs = -1;
/*  810: 861 */       for (int i = 0; i < Project.samples_.size(); i++)
/*  811:     */       {
/*  812: 862 */         sumOfEcs = 0;
/*  813: 864 */         for (int ecCnt = 0; ecCnt < ((Sample)Project.samples_.get(i)).ecs_.size(); ecCnt++) {
/*  814: 865 */           if (((EcWithPathway)((Sample)Project.samples_.get(i)).ecs_.get(ecCnt)).pathways_.size() > 0) {
/*  815: 866 */             sumOfEcs += ((EcWithPathway)((Sample)Project.samples_.get(i)).ecs_.get(ecCnt)).amount_;
/*  816:     */           }
/*  817:     */         }
/*  818: 869 */         System.out.println("sums " + i + " : " + sumOfEcs);
/*  819: 870 */         sumsOfEcsArr[i] = sumOfEcs;
/*  820: 871 */         if ((sumOfEcs < minEcs) || (minEcs == -1)) {
/*  821: 872 */           minEcs = sumOfEcs;
/*  822:     */         }
/*  823:     */       }
/*  824: 875 */       for (int i = 0; i < Project.samples_.size(); i++) {
/*  825: 876 */         while (sumsOfEcsArr[i] > minEcs)
/*  826:     */         {
/*  827: 879 */           int pick = generator.nextInt(sumsOfEcsArr[i]);
/*  828: 880 */           int index = 0;
/*  829: 881 */           while (pick > 0)
/*  830:     */           {
/*  831: 882 */             pick -= ((EcWithPathway)((Sample)Project.samples_.get(i)).ecs_.get(index)).amount_;
/*  832: 883 */             if (pick <= 0) {
/*  833:     */               break;
/*  834:     */             }
/*  835: 887 */             index++;
/*  836:     */           }
/*  837: 890 */           ((EcWithPathway)((Sample)Project.samples_.get(i)).ecs_.get(index)).amount_ -= 1;
/*  838: 891 */           sumsOfEcsArr[i] -= 1;
/*  839: 893 */           if (((EcWithPathway)((Sample)Project.samples_.get(i)).ecs_.get(index)).amount_ <= 0) {
/*  840: 894 */             ((Sample)Project.samples_.get(i)).ecs_.remove(index);
/*  841:     */           }
/*  842:     */         }
/*  843:     */       }
/*  844: 900 */       for (int i = 0; i < Project.samples_.size(); i++)
/*  845:     */       {
/*  846: 901 */         int sum = 0;
/*  847: 902 */         for (int j = 0; j < ((Sample)Project.samples_.get(i)).ecs_.size(); j++) {
/*  848: 903 */           sum += ((EcWithPathway)((Sample)Project.samples_.get(i)).ecs_.get(j)).amount_;
/*  849:     */         }
/*  850: 905 */         System.out.println("newsize(" + i + ") =  counted " + sum + " predicted " + sumsOfEcsArr[i]);
/*  851:     */       }
/*  852:     */     }
/*  853:     */   }
/*  854:     */   
/*  855:     */   public EcWithPathway findEcWPath(EcNr ecNr)
/*  856:     */   {// loops through the ec list, which contains the ecs with pathways, and if ny ecs have the same name as the provided ecnr, returns the ec with pathway
/*  857: 916 */     for (int i = 0; i < ecList_.size(); i++) {
/*  858: 918 */       if (((EcWithPathway)ecList_.get(i)).name_.compareTo(ecNr.name_) == 0) {
/*  859: 919 */         return (EcWithPathway)ecList_.get(i);
/*  860:     */       }
/*  861:     */     }
/*  862: 922 */     return null;
/*  863:     */   }
/*  864:     */   
/*  865:     */   public void transferEcToPath()
/*  866:     */   {
/*  867: 930 */     EcWithPathway ecWP = null;
/*  868: 931 */     PathwayWithEc pwEc = null;
/*  869: 934 */     for (int sampleCount = 0; sampleCount < Project.samples_.size(); sampleCount++) {
/*  870: 936 */       if (!((Sample)Project.samples_.get(sampleCount)).valuesSet) {
/*  871: 939 */         for (int PwCount = 0; PwCount < getPathwayList_().size(); PwCount++)
/*  872:     */         {
/*  873: 940 */           pwEc = new PathwayWithEc((Pathway)getPathwayList_().get(PwCount));
/*  874: 941 */           pwEc.clearList();
/*  875: 942 */           ((Sample)Project.samples_.get(sampleCount)).addPaths(pwEc);
/*  876: 943 */           ((PathwayWithEc)((Sample)Project.samples_.get(sampleCount)).pathways_.get(((Sample)Project.samples_.get(sampleCount)).pathways_.size() - 1)).clearList();
/*  877:     */         }
/*  878:     */       }
/*  879:     */     }
/*  880: 946 */     if (Project.overall_.pathways_.size() == 0) {
/*  881: 947 */       for (int PwCount = 0; PwCount < getPathwayList_().size(); PwCount++) {
/*  882: 948 */         Project.overall_.addPaths(new PathwayWithEc((Pathway)getPathwayList_().get(PwCount)));
/*  883:     */       }
/*  884:     */     }
/*  885: 952 */     for (int sampleCount = 0; sampleCount < Project.samples_.size(); sampleCount++)
/*  886:     */     {
/*  887: 954 */       Sample tmpSample = (Sample)Project.samples_.get(sampleCount);
/*  888: 955 */       if (!tmpSample.valuesSet)
/*  889:     */       {
/*  890: 958 */         for (int pwCount = 0; pwCount < tmpSample.pathways_.size(); pwCount++) {
/*  891: 959 */           ((PathwayWithEc)tmpSample.pathways_.get(pwCount)).ecNrs_ = new ArrayList();
/*  892:     */         }
/*  893: 964 */         for (int ecsCount = 0; ecsCount < tmpSample.ecs_.size(); ecsCount++)
/*  894:     */         {
/*  895: 965 */           ecWP = null;
/*  896: 966 */           ecWP = (EcWithPathway)tmpSample.ecs_.get(ecsCount);
/*  897: 968 */           if (ecWP.pathways_ != null) {
/*  898: 969 */             for (int ecWPCount = 0; ecWPCount < ecWP.pathways_.size(); ecWPCount++) {
/*  899: 971 */               for (int pwCount = 0; pwCount < tmpSample.pathways_.size(); pwCount++) {
/*  900: 973 */                 if (((Pathway)ecWP.pathways_.get(ecWPCount)).id_.compareTo(((PathwayWithEc)((Sample)Project.samples_.get(sampleCount)).pathways_.get(pwCount)).id_) == 0)
/*  901:     */                 {
/*  902: 974 */                   if (((PathwayWithEc)tmpSample.pathways_.get(pwCount)).userPathway) {
/*  903: 975 */                     ((PathwayWithEc)tmpSample.pathways_.get(pwCount)).addEcNewly(ecWP);
/*  904:     */                   } else {
/*  905: 978 */                     ((PathwayWithEc)tmpSample.pathways_.get(pwCount)).addEc(ecWP);
/*  906:     */                   }
/*  907: 980 */                   this.lFrame_.step(String.valueOf(ecWP.amount_));
/*  908:     */                 }
/*  909:     */               }
/*  910:     */             }
/*  911:     */           }
/*  912:     */         }
/*  913: 987 */         tmpSample.valuesSet = true;
/*  914:     */       }
/*  915:     */     }
/*  916:     */   }
/*  917:     */   
/*  918:     */   public void prepOverall()
/*  919:     */   {
/*  920: 999 */     this.lFrame_.bigStep("Overall");
/*  921:     */     
/*  922:1001 */     Sample tmpSmp = null;
/*  923:1002 */     PathwayWithEc tmpPwS = null;
/*  924:1003 */     PathwayWithEc tmpPwD = null;
/*  925:1004 */     EcNr tmpEc = null;
/*  926:     */     
/*  927:1006 */     Project.overall_.ecs_ = new ArrayList();
/*  928:1007 */     for (int pwCnt = 0; pwCnt < Project.overall_.pathways_.size(); pwCnt++)
/*  929:     */     {
/*  930:1008 */       tmpPwD = (PathwayWithEc)Project.overall_.pathways_.get(pwCnt);
/*  931:1009 */       tmpPwD.ecNrs_ = new ArrayList();
/*  932:     */     }
/*  933:1012 */     for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++)
/*  934:     */     {
/*  935:1013 */       tmpSmp = (Sample)Project.samples_.get(smpCnt);
/*  936:1014 */       if (!tmpSmp.inUse)
/*  937:     */       {
/*  938:1015 */         System.out.println("not in use");
/*  939:     */       }
/*  940:     */       else
/*  941:     */       {
/*  942:1018 */         for (int pwCnt = 0; pwCnt < tmpSmp.pathways_.size(); pwCnt++)
/*  943:     */         {
/*  944:1019 */           tmpPwS = (PathwayWithEc)tmpSmp.pathways_.get(pwCnt);
/*  945:1020 */           tmpPwD = Project.overall_.getPath(tmpPwS.id_);
/*  946:1021 */           if (tmpPwD == null)
/*  947:     */           {
/*  948:1022 */             System.out.println("tmpwd is null" + tmpPwS.id_);
/*  949:1023 */             Project.overall_.pathways_.add(new PathwayWithEc(tmpPwS));
/*  950:1024 */             tmpPwD = Project.overall_.getPath(tmpPwS.id_);
/*  951:     */           }
/*  952:1026 */           for (int ecCnt = 0; ecCnt < tmpPwS.ecNrs_.size(); ecCnt++)
/*  953:     */           {
/*  954:1027 */             tmpEc = new EcNr((EcNr)tmpPwS.ecNrs_.get(ecCnt));
/*  955:1028 */             tmpEc.samColor_ = tmpSmp.sampleCol_;
/*  956:1029 */             tmpEc.sampleNr_ = smpCnt;
/*  957:1030 */             tmpPwD.addEc(tmpEc, tmpSmp);
/*  958:     */           }
/*  959:     */         }
/*  960:1033 */         for (int ecCnt = 0; ecCnt < tmpSmp.ecs_.size(); ecCnt++) {
/*  961:1035 */           Project.overall_.addEc(new EcWithPathway((EcWithPathway)tmpSmp.ecs_.get(ecCnt)));
/*  962:     */         }
/*  963:     */       }
/*  964:     */     }
/*  965:     */   }
/*  966:     */   
/*  967:     */   public boolean ecNrInList(String ecNr)
/*  968:     */   {
/*  969:1045 */     this.ecList = this.reader.readTxt(listPath);
/*  970:1046 */     String zeile = null;
/*  971:     */     try
/*  972:     */     {
/*  973:1048 */       while ((zeile = this.ecList.readLine()) != null) {
/*  974:1049 */         if (zeile.contains(ecNr)) {
/*  975:1051 */           return true;
/*  976:     */         }
/*  977:     */       }
/*  978:     */     }
/*  979:     */     catch (IOException e)
/*  980:     */     {
/*  981:1056 */       openWarning("Error", "File: " + listPath + " not found");
/*  982:1057 */       e.printStackTrace();
/*  983:     */     }
/*  984:1059 */     return false;
/*  985:     */   }
/*  986:     */   
/*  987:     */   private void prepEcList()
/*  988:     */   {// preps the ec list
/*  989:1063 */     this.lFrame_.bigStep("EC List");
/*  990:1064 */     String zeile = "";
/*  991:1065 */     String tmp = "";
/*  992:1066 */     String id = "";
/*  993:     */     
/*  994:1068 */     Boolean newEc = Boolean.valueOf(true);
/*  995:1069 */     int index = 0;
/*  996:1070 */     if (ecList_ == null)
/*  997:     */     {
/*  998:1071 */       ecList_ = new ArrayList();
/*  999:     */       try
/* 1000:     */       {
/* 1001:1073 */         this.ecList = this.reader.readTxt(listPath);
/* 1002:1074 */         while ((zeile = this.ecList.readLine()) != null)
/* 1003:     */         {
/* 1004:1076 */           tmp = getEcNrFromList(zeile);
/* 1005:1077 */           newEc = Boolean.valueOf(true);
/* 1006:     */           
/* 1007:1079 */           id = getPathwayFromList(zeile);
/* 1008:1081 */           if ((!id.equalsIgnoreCase("ec01120")) && (!id.equalsIgnoreCase("ec01110")) && (!id.equalsIgnoreCase("ec01100")))
/* 1009:     */           {
/* 1010:1085 */             Pathway tmpPathway = new Pathway(getPathway(id));
/* 1011:1086 */             if (!tmp.isEmpty())
/* 1012:     */             {
/* 1013:1087 */               for (int i = 0; (i < ecList_.size()) && (newEc.booleanValue()); i++)
/* 1014:     */               {
/* 1015:1088 */                 if (((EcWithPathway)ecList_.get(i)).name_.contentEquals(tmp))
/* 1016:     */                 {
/* 1017:1089 */                   newEc = Boolean.valueOf(false);
/* 1018:1090 */                   index = i;
/* 1019:     */                   
/* 1020:     */ 
/* 1021:1093 */                   break;
/* 1022:     */                 }
/* 1023:1095 */                 index = i;
/* 1024:     */               }
/* 1025:1101 */               if (newEc.booleanValue())
/* 1026:     */               {
/* 1027:1105 */                 ecList_.add(new EcWithPathway(new EcNr(tmp)));
/* 1028:1106 */                 ((EcWithPathway)ecList_.get(ecList_.size() - 1)).addPathway(tmpPathway);
/* 1029:1107 */                 setBioNameFromList((EcWithPathway)ecList_.get(ecList_.size() - 1));
/* 1030:1108 */                 this.lFrame_.step(((EcWithPathway)ecList_.get(ecList_.size() - 1)).name_);
/* 1031:     */               }
/* 1032:     */               else
/* 1033:     */               {
/* 1034:1112 */                 ((EcWithPathway)ecList_.get(index)).addPathway(tmpPathway);
/* 1035:     */               }
/* 1036:     */             }
/* 1037:     */           }
/* 1038:     */         }
/* 1039:1117 */         this.ecList.close();
/* 1040:     */       }
/* 1041:     */       catch (IOException e)
/* 1042:     */       {
/* 1043:1120 */         openWarning("Error", "File: " + listPath + " not found");
/* 1044:1121 */         e.printStackTrace();
/* 1045:     */       }
/* 1046:     */     }
/* 1047:     */   }
/* 1048:     */   
/* 1049:     */   private void setBioNameFromList(EcWithPathway ec)
/* 1050:     */   {
/* 1051:1126 */     String ecNr = ec.name_;
/* 1052:1127 */     String bioName = "";
/* 1053:1128 */     String zeile = "";
/* 1054:     */     try
/* 1055:     */     {
/* 1056:1131 */       BufferedReader ec2go = this.reader.readTxt(ecNamesPath);
/* 1057:1132 */       while ((zeile = ec2go.readLine()) != null) {
/* 1058:1134 */         if (zeile.startsWith("EC:" + ecNr))
/* 1059:     */         {
/* 1060:1135 */           bioName = zeile.substring(zeile.indexOf(" > GO:") + 6, zeile.indexOf(" ; GO:"));
/* 1061:1136 */           ec.bioName_ = bioName;
/* 1062:     */         }
/* 1063:     */       }
/* 1064:1139 */       ec2go.close();
/* 1065:     */     }
/* 1066:     */     catch (IOException e)
/* 1067:     */     {
/* 1068:1143 */       openWarning("Error", "File: " + ecNamesPath + " not found");
/* 1069:1144 */       e.printStackTrace();
/* 1070:     */     }
/* 1071:     */   }
/* 1072:     */   
/* 1073:     */   public Pathway getPathway(String id)
/* 1074:     */   {
/* 1075:1149 */     Pathway ret = null;
/* 1076:1151 */     for (int i = 0; i < pathwayList_.size(); i++) {
/* 1077:1153 */       if (id.compareTo(((PathwayWithEc)getPathwayList_().get(i)).id_) == 0)
/* 1078:     */       {
/* 1079:1155 */         ret = new Pathway((Pathway)getPathwayList_().get(i));
/* 1080:1156 */         return ret;
/* 1081:     */       }
/* 1082:     */     }
/* 1083:1159 */     return ret;
/* 1084:     */   }
/* 1085:     */   
/* 1086:     */   private void prepPathList()
/* 1087:     */   {// Preps the list of pathways
/* 1088:1168 */     this.lFrame_.bigStep("Pathlist");
/* 1089:1169 */     String zeile = "";
/* 1090:1170 */     String ecNr = "";
/* 1091:1171 */     String tmpid = "";
/* 1092:1172 */     String tmpName = "";
/* 1093:1173 */     String path = "";
/* 1094:1174 */     int index = -1;
/* 1095:1175 */     if (getPathwayList_() == null)
/* 1096:     */     {
/* 1097:1176 */       setPathwayList_(new ArrayList());
/* 1098:1177 */       this.ecList = this.reader.readTxt(listPath);
/* 1099:     */       try
/* 1100:     */       {
/* 1101:1180 */         while ((zeile = this.ecList.readLine()) != null)
/* 1102:     */         {
/* 1103:1181 */           tmpid = getPathwayFromList(zeile);
/* 1104:     */           
/* 1105:1183 */           ecNr = getEcNrFromList(zeile);
/* 1106:1185 */           if ((!tmpid.equalsIgnoreCase("ec01120")) && (!tmpid.equalsIgnoreCase("ec01110")) && (!tmpid.equalsIgnoreCase("ec01100"))) {
/* 1107:1188 */             if ((tmpid.equalsIgnoreCase(path)) && (!ecNr.isEmpty()))
/* 1108:     */             {
/* 1109:1190 */               ((PathwayWithEc)getPathwayList_().get(index)).addEc(new EcNr(ecNr));
/* 1110:     */             }
/* 1111:1193 */             else if (!ecNr.isEmpty())
/* 1112:     */             {
/* 1113:1194 */               path = tmpid;
/* 1114:1195 */               tmpName = getPathwayName(tmpid);
/* 1115:     */               
/* 1116:1197 */               index++;
/* 1117:     */               
/* 1118:1199 */               getPathwayList_().add(new PathwayWithEc(new Pathway(tmpid, tmpName)));
/* 1119:     */               
/* 1120:1201 */               ((PathwayWithEc)getPathwayList_().get(index)).addEc(new EcNr(ecNr));
/* 1121:     */               
/* 1122:1203 */               this.lFrame_.step(((PathwayWithEc)getPathwayList_().get(index)).name_);
/* 1123:     */             }
/* 1124:     */           }
/* 1125:     */         }
/* 1126:1209 */         this.ecList.close();
/* 1127:     */       }
/* 1128:     */       catch (IOException e)
/* 1129:     */       {
/* 1130:1212 */         openWarning("Error", "File: " + listPath + " not found");
/* 1131:1213 */         e.printStackTrace();
/* 1132:     */       }
/* 1133:1216 */       getPathwayList_().add(new PathwayWithEc(new Pathway("-1", "Unmatched")));
/* 1134:1217 */       this.unmatchedIndex = (getPathwayList_().size() - 1);
/* 1135:     */     }
/* 1136:     */   }
/* 1137:     */   
/* 1138:     */   private void prepUserEc()
/* 1139:     */   {
/* 1140:1221 */     System.out.println("prepUserEc");
/* 1141:     */     
/* 1142:     */ 
/* 1143:     */ 
/* 1144:1225 */     boolean ecFound = false;
/* 1145:1226 */     for (int i = 0; i < getPathwayList_().size(); i++)
/* 1146:     */     {
/* 1147:1227 */       PathwayWithEc pathway = (PathwayWithEc)getPathwayList_().get(i);
/* 1148:1228 */       if (pathway.userPathway)
/* 1149:     */       {
/* 1150:1231 */         System.out.println("userPathwayToec");
/* 1151:1232 */         ecFound = false;
/* 1152:1233 */         for (int j = 0; j < pathway.ecNrs_.size(); j++)
/* 1153:     */         {
/* 1154:1234 */           EcNr ecNr = (EcNr)pathway.ecNrs_.get(j);
/* 1155:1235 */           EcWithPathway ecWp = findEcWPath(ecNr);
/* 1156:1236 */           if (ecWp == null)
/* 1157:     */           {
/* 1158:1237 */             ecWp = new EcWithPathway(ecNr);
/* 1159:1238 */             ecWp.userEC = true;
/* 1160:     */             
/* 1161:1240 */             ecWp.addPathway(new Pathway(pathway));
/* 1162:1241 */             ecList_.add(ecWp);
/* 1163:     */           }
/* 1164:     */           else
/* 1165:     */           {
/* 1166:1244 */             ecWp.addPathway(pathway);
/* 1167:     */           }
/* 1168:     */         }
/* 1169:     */       }
/* 1170:     */     }
/* 1171:1248 */     System.out.println("end prepUserEc");
/* 1172:     */   }
/* 1173:     */   
/* 1174:     */   private void prepUserPathList()
/* 1175:     */   {
/* 1176:1251 */     System.out.println("prepUserPathList");
/* 1177:1252 */     if (this.activeProj_ == null) {
/* 1178:1253 */       return;
/* 1179:     */     }
/* 1180:1255 */     if (Project.userPathways == null) {
/* 1181:1256 */       return;
/* 1182:     */     }
/* 1183:1259 */     for (int i = 0; i < Project.userPathways.size(); i++)
/* 1184:     */     {
/* 1185:1260 */       System.out.println("userpath " + i);
/* 1186:1261 */       String path = (String)Project.userPathways.get(i);
/* 1187:1262 */       readUserPathway(path);
/* 1188:     */     }
/* 1189:1264 */     System.out.println("End prepUserPathList");
/* 1190:     */   }
/* 1191:     */   
/* 1192:     */   private void readUserPathway(String path)
/* 1193:     */   {
/* 1194:1267 */     System.out.println("reading userPaths");
/* 1195:     */     
/* 1196:1269 */     Pathway actPathway = new Pathway("", "");
/* 1197:1270 */     PathwayWithEc finPAth = new PathwayWithEc(actPathway);
/* 1198:1271 */     int userPathCnt = 0;
/* 1199:     */     try
/* 1200:     */     {
/* 1201:1273 */       BufferedReader in = new BufferedReader(new FileReader(path));
/* 1202:1274 */       String comp = "<pathName>";
/* 1203:     */       String zeile;
/* 1204:1275 */       while ((zeile = in.readLine()) != null)
/* 1205:     */       {
/* 1206:     */         //String zeile;
/* 1207:1276 */         comp = "<pathName>";
/* 1208:1277 */         if (zeile.startsWith(comp))
/* 1209:     */         {
/* 1210:1278 */           zeile = zeile.substring(zeile.indexOf(">") + 1);
/* 1211:1279 */           finPAth.name_ = zeile;
/* 1212:1280 */           System.out.println("userpathName " + zeile);
/* 1213:1281 */           if (finPAth.name_ == null)
/* 1214:     */           {
/* 1215:1282 */             finPAth.name_ = ("userPath" + userPathCnt);
/* 1216:1283 */             userPathCnt++;
/* 1217:     */           }
/* 1218:1285 */           if (finPAth.name_.contentEquals("null"))
/* 1219:     */           {
/* 1220:1286 */             finPAth.name_ = ("userPath" + userPathCnt);
/* 1221:1287 */             userPathCnt++;
/* 1222:     */           }
/* 1223:     */         }
/* 1224:1290 */         comp = "<id>";
/* 1225:1291 */         if (zeile.startsWith(comp))
/* 1226:     */         {
/* 1227:1292 */           zeile = zeile.substring(zeile.indexOf(">") + 1);
/* 1228:1293 */           finPAth.id_ = zeile;
/* 1229:1294 */           if (finPAth.id_.isEmpty()) {
/* 1230:1295 */             finPAth.id_ = finPAth.name_;
/* 1231:     */           }
/* 1232:     */         }
/* 1233:1298 */         comp = "<nodes>";
/* 1234:1299 */         if (zeile.startsWith(comp)) {
/* 1235:1300 */           finPAth = new PathwayWithEc(addNodes(finPAth, in), false);
/* 1236:     */         }
/* 1237:1302 */         comp = "<connections>";
/* 1238:1303 */         if (zeile.startsWith(comp)) {
/* 1239:     */           break;
/* 1240:     */         }
/* 1241:     */       }
/* 1242:1308 */       in.close();
/* 1243:1309 */       finPAth.userPathway = true;
/* 1244:1310 */       finPAth.pathwayInfoPAth = path;
/* 1245:1311 */       finPAth.id_ = finPAth.name_;
/* 1246:1312 */       finPAth.printPath();
/* 1247:1313 */       if (getPathway(finPAth.id_) == null)
/* 1248:     */       {
/* 1249:1314 */         getPathwayList_().add(finPAth);
/* 1250:1315 */         newUserPathList_.add(finPAth);
/* 1251:     */       }
/* 1252:     */     }
/* 1253:     */     catch (Exception e)
/* 1254:     */     {
/* 1255:1320 */       openWarning("Error", "File pathway: " + path + ".chn" + " not found");
/* 1256:1321 */       System.out.println("Userfilepath not found");
/* 1257:     */     }
/* 1258:1323 */     System.out.println("end reading userPaths");
/* 1259:     */   }
/* 1260:     */   
/* 1261:     */   private PathwayWithEc addNodes(PathwayWithEc path, BufferedReader in)
/* 1262:     */     throws IOException
/* 1263:     */   {
/* 1264:1326 */     System.out.println("addNode");
/* 1265:     */     
/* 1266:1328 */     PathwayWithEc finPAth = new PathwayWithEc(path);
/* 1267:1329 */     EcNr ec = null;
/* 1268:     */     
/* 1269:     */ 
/* 1270:1332 */     boolean comment = false;
/* 1271:     */     String zeile;
/* 1272:1333 */     while ((zeile = in.readLine()) != null)
/* 1273:     */     {
/* 1274:     */       //String zeile;
/* 1275:1334 */       String comp = "<name>";
/* 1276:1335 */       if (zeile.startsWith(comp))
/* 1277:     */       {
/* 1278:1337 */         String ecName = zeile.substring(zeile.indexOf(">") + 1);
/* 1279:1338 */         ec = new EcNr(ecName);
/* 1280:     */       }
/* 1281:1340 */       comp = "<comment>";
/* 1282:1341 */       if (zeile.startsWith(comp))
/* 1283:     */       {
/* 1284:1343 */         String ecName = zeile.substring(zeile.indexOf(">") + 1);
/* 1285:1344 */         if (ecName.contentEquals("true")) {
/* 1286:1345 */           comment = true;
/* 1287:     */         }
/* 1288:     */       }
/* 1289:1348 */       comp = "</node>";
/* 1290:1349 */       if (zeile.startsWith(comp)) {
/* 1291:1350 */         if (comment) {
/* 1292:1351 */           comment = false;
/* 1293:     */         } else {
/* 1294:1354 */           finPAth.addEc(ec);
/* 1295:     */         }
/* 1296:     */       }
/* 1297:1357 */       comp = "</nodes>";
/* 1298:1358 */       if (zeile.startsWith(comp)) {
/* 1299:     */         break;
/* 1300:     */       }
/* 1301:     */     }
/* 1302:1364 */     return finPAth;
/* 1303:     */   }
/* 1304:     */   
/* 1305:     */   public String getPathwayName(String id)
/* 1306:     */   {
/* 1307:1373 */     this.nameList = this.reader.readTxt(mapTitleList);
/* 1308:1374 */     String zeile = "";
/* 1309:1375 */     int beginIndex = 6;
/* 1310:     */     try
/* 1311:     */     {
/* 1312:1377 */       while ((zeile = this.nameList.readLine()) != null) {
/* 1313:1379 */         if (zeile.startsWith(id.substring(2))) {
/* 1314:1381 */           return zeile.substring(beginIndex);
/* 1315:     */         }
/* 1316:     */       }
/* 1317:     */     }
/* 1318:     */     catch (IOException e)
/* 1319:     */     {
/* 1320:1387 */       openWarning("Error", "File: pathway" + mapTitleList + ".chn" + " not found");
/* 1321:1388 */       e.printStackTrace();
/* 1322:     */     }
/* 1323:1390 */     return "";
/* 1324:     */   }
/* 1325:     */   
/* 1326:     */   private void computeWeights()
/* 1327:     */   {
/* 1328:1397 */     this.lFrame_.bigStep("Weights");
/* 1329:1398 */     float totaluniqueEc = ecList_.size();
/* 1330:1399 */     float totalNrEc = 0.0F;
/* 1331:1400 */     float nrOfPaths = 0.0F;
/* 1332:1401 */     float weight = 0.0F;
/* 1333:1402 */     String zeile = "";
/* 1334:     */     try
/* 1335:     */     {
/* 1336:1405 */       this.ecList = this.reader.readTxt(listPath);
/* 1337:1406 */       while ((zeile = this.ecList.readLine()) != null) {
/* 1338:1407 */         if ((zeile.contains("ec:")) && (!zeile.contains("ec01100")) && (!zeile.contains("ec01110")) && (!zeile.contains("ec01120"))) {
/* 1339:1408 */           totalNrEc += 1.0F;
/* 1340:     */         }
/* 1341:     */       }
/* 1342:     */     }
/* 1343:     */     catch (IOException e)
/* 1344:     */     {
/* 1345:1415 */       openWarning("Error", "File: " + listPath + " not found");
/* 1346:1416 */       e.printStackTrace();
/* 1347:     */     }
/* 1348:1418 */     System.out.println("computeWeights " + totaluniqueEc + " " + ecList_.size());
/* 1349:1419 */     for (int i = 0; i < totaluniqueEc; i++)
/* 1350:     */     {
/* 1351:1421 */       if (!((EcWithPathway)ecList_.get(i)).weightsSet)
/* 1352:     */       {
/* 1353:1422 */         nrOfPaths = 0.0F;
/* 1354:1424 */         for (int pathCount = 0; pathCount < ((EcWithPathway)ecList_.get(i)).pathways_.size(); pathCount++) {
/* 1355:1425 */           if (isNoOverAll(((Pathway)((EcWithPathway)ecList_.get(i)).pathways_.get(pathCount)).id_)) {
/* 1356:1426 */             nrOfPaths += 1.0F;
/* 1357:     */           }
/* 1358:     */         }
/* 1359:1430 */         if (nrOfPaths == 0.0F) {
/* 1360:1431 */           nrOfPaths = ((EcWithPathway)ecList_.get(i)).pathways_.size();
/* 1361:     */         }
/* 1362:1435 */         weight = totalNrEc / totaluniqueEc / nrOfPaths;
/* 1363:1436 */         ((EcWithPathway)ecList_.get(i)).weight_ = weight;
/* 1364:1437 */         this.lFrame_.step(String.valueOf(weight));
/* 1365:1438 */         ((EcWithPathway)ecList_.get(i)).weightsSet = true;
/* 1366:     */       }
/* 1367:1441 */       if (((EcWithPathway)ecList_.get(i)).weight_ > 100000.0F)
/* 1368:     */       {
/* 1369:1442 */         System.out.println("computeWeights userec");
/* 1370:1443 */         ((EcWithPathway)ecList_.get(i)).weight_ = 1.0F;
/* 1371:     */       }
/* 1372:     */     }
/* 1373:     */   }
/* 1374:     */   
/* 1375:     */   private void calcAllChainsforallSamples()
/* 1376:     */   {
/* 1377:1450 */     for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
/* 1378:1451 */       if (((Sample)Project.samples_.get(smpCnt)).inUse) {
/* 1379:1455 */         calcChainsForSample((Sample)Project.samples_.get(smpCnt));
/* 1380:     */       }
/* 1381:     */     }
/* 1382:1458 */     calcChainsForSample(Project.overall_);
/* 1383:     */   }
/* 1384:     */   
/* 1385:     */   private void calcChainsForSample(Sample samp)
/* 1386:     */   {
/* 1387:1464 */     PathwayWithEc actpath = null;
/* 1388:1465 */     EcNr actEcnr = null;
/* 1389:1466 */     for (int pathCnt = 0; pathCnt < samp.pathways_.size(); pathCnt++)
/* 1390:     */     {
/* 1391:1467 */       actpath = (PathwayWithEc)samp.pathways_.get(pathCnt);
/* 1392:1468 */       this.lFrame_.bigStep("Chaining");
/* 1393:1469 */       for (int ecCnt = 0; ecCnt < actpath.ecNrs_.size(); ecCnt++)
/* 1394:     */       {
/* 1395:1471 */         actEcnr = (EcNr)actpath.ecNrs_.get(ecCnt);
/* 1396:     */         
/* 1397:     */ 
/* 1398:1474 */         this.lFrame_.step(actEcnr.name_);
/* 1399:1475 */         findLongestChain(actpath, actEcnr, false);
/* 1400:     */         
/* 1401:     */ 
/* 1402:1478 */         actEcnr.adaptWeightToChain();
/* 1403:     */       }
/* 1404:     */     }
/* 1405:     */   }
/* 1406:     */   
/* 1407:     */   private void calcChainsForProg()
/* 1408:     */   {
/* 1409:1486 */     PathwayWithEc actpath = null;
/* 1410:1487 */     EcNr actEcnr = null;
/* 1411:1488 */     for (int pathCnt = 0; pathCnt < getPathwayList_().size(); pathCnt++)
/* 1412:     */     {
/* 1413:1489 */       actpath = (PathwayWithEc)getPathwayList_().get(pathCnt);
/* 1414:1490 */       this.lFrame_.bigStep("Chaining");
/* 1415:1491 */       for (int ecCnt = 0; ecCnt < actpath.ecNrs_.size(); ecCnt++)
/* 1416:     */       {
/* 1417:1492 */         actEcnr = (EcNr)actpath.ecNrs_.get(ecCnt);
/* 1418:1493 */         this.lFrame_.step(actEcnr.name_);
/* 1419:1494 */         findLongestChain(actpath, actEcnr, true);
/* 1420:     */         
/* 1421:     */ 
/* 1422:     */ 
/* 1423:1498 */         actEcnr.adaptWeightToChain();
/* 1424:     */       }
/* 1425:     */     }
/* 1426:     */   }
/* 1427:     */   
/* 1428:     */   private void findLongestChain(PathwayWithEc path, EcNr ecNr, boolean forProg)
/* 1429:     */   {
/* 1430:1509 */     int longestChain = 1;
/* 1431:1510 */     if (path.userPathway) {
/* 1432:1511 */       return;
/* 1433:     */     }
/* 1434:1513 */     if (path.id_.contentEquals("-1")) {
/* 1435:1514 */       return;
/* 1436:     */     }
/* 1437:1516 */     BufferedReader chainList = this.reader.readTxt("pathway" + File.separator + path.id_ + ".chn");
/* 1438:1517 */     String zeile = "";
/* 1439:1518 */     String subLine = "";
/* 1440:1519 */     String next = " --> ";
/* 1441:1520 */     String chainEc = "";
/* 1442:1521 */     String ecName = ecNr.name_;
/* 1443:1522 */     ArrayList<EcNr> chain = new ArrayList();
/* 1444:1523 */     boolean lastHit = false;
/* 1445:1524 */     int tempChainLenght = 1;
/* 1446:     */     try
/* 1447:     */     {
/* 1448:1526 */       while ((zeile = chainList.readLine()) != null) {
/* 1449:1527 */         if (zeile.startsWith(ecName + next))
/* 1450:     */         {
/* 1451:1528 */           lastHit = false;
/* 1452:1529 */           tempChainLenght = 1;
/* 1453:1530 */           subLine = zeile;
/* 1454:1531 */           while (!lastHit) {
/* 1455:1532 */             if (subLine.contains(next))
/* 1456:     */             {
/* 1457:1533 */               subLine = subLine.substring(zeile.indexOf(next) + 5);
/* 1458:1534 */               if (subLine.contains(next))
/* 1459:     */               {
/* 1460:1535 */                 chainEc = subLine.substring(0, subLine.indexOf(next));
/* 1461:     */               }
/* 1462:     */               else
/* 1463:     */               {
/* 1464:1538 */                 chainEc = subLine;
/* 1465:1539 */                 lastHit = true;
/* 1466:     */               }
/* 1467:1541 */               for (int ecCnt = 0; ecCnt < path.ecNrs_.size(); ecCnt++)
/* 1468:     */               {
/* 1469:1542 */                 if (chainEc.contentEquals(((EcNr)path.ecNrs_.get(ecCnt)).name_))
/* 1470:     */                 {
/* 1471:1543 */                   tempChainLenght++;
/* 1472:1544 */                   chain.add((EcNr)path.ecNrs_.get(ecCnt));
/* 1473:1545 */                   break;
/* 1474:     */                 }
/* 1475:1548 */                 lastHit = true;
/* 1476:     */               }
/* 1477:     */             }
/* 1478:     */             else
/* 1479:     */             {
/* 1480:1554 */               lastHit = true;
/* 1481:     */             }
/* 1482:     */           }
/* 1483:1557 */           if (tempChainLenght > longestChain) {
/* 1484:1558 */             longestChain = tempChainLenght;
/* 1485:     */           }
/* 1486:     */         }
/* 1487:     */       }
/* 1488:1566 */       chainList.close();
/* 1489:1567 */       if (ecNr.longestChain_ < longestChain)
/* 1490:     */       {
/* 1491:1568 */         ecNr.longestChain_ = longestChain;
/* 1492:1569 */         if (forProg) {
/* 1493:1570 */           ecNr.maxChainLength_ = longestChain;
/* 1494:     */         }
/* 1495:     */       }
/* 1496:1574 */       for (int chCnt = 0; chCnt < chain.size(); chCnt++) {
/* 1497:1575 */         if (((EcNr)chain.get(chCnt)).longestChain_ < longestChain)
/* 1498:     */         {
/* 1499:1576 */           ((EcNr)chain.get(chCnt)).longestChain_ = longestChain;
/* 1500:1577 */           if (forProg) {
/* 1501:1578 */             ((EcNr)chain.get(chCnt)).maxChainLength_ = longestChain;
/* 1502:     */           }
/* 1503:     */         }
/* 1504:     */       }
/* 1505:     */     }
/* 1506:     */     catch (IOException e)
/* 1507:     */     {
/* 1508:1587 */       openWarning("Error", "File: pathway" + File.separator + path.id_ + ".chn" + " not found");
/* 1509:1588 */       e.printStackTrace();
/* 1510:     */     }
/* 1511:     */   }
/* 1512:     */   
/* 1513:     */   private boolean isNoOverAll(String pathName)
/* 1514:     */   {
/* 1515:1601 */     if ((pathName.contentEquals("ec01100")) || (pathName.contentEquals("ec01110")) || (pathName.contentEquals("ec01120")) || 
/* 1516:1602 */       (pathName.contentEquals("rn01100")) || (pathName.contentEquals("rn01110")) || (pathName.contentEquals("rn01120"))) {
/* 1517:1604 */       return false;
/* 1518:     */     }
/* 1519:1607 */     return true;
/* 1520:     */   }
/* 1521:     */   
/* 1522:     */   public float getWeight(String ecNr)
/* 1523:     */   {
/* 1524:1619 */     for (int i = 0; i < ecList_.size(); i++) {
/* 1525:1620 */       if (((EcWithPathway)ecList_.get(i)).name_.contentEquals(ecNr)) {
/* 1526:1621 */         return ((EcWithPathway)ecList_.get(i)).weight_;
/* 1527:     */       }
/* 1528:     */     }
/* 1529:1624 */     return 0.0F;
/* 1530:     */   }
/* 1531:     */   
/* 1532:     */   public void transferWeightToPwl()
/* 1533:     */   {
/* 1534:1630 */     this.lFrame_.bigStep("transferWeights");
/* 1535:     */     
/* 1536:1632 */     float pathw = 0.0F;
/* 1537:1633 */     float tmpWeight = 0.0F;
/* 1538:1635 */     for (int i = 0; i < getPathwayList_().size(); i++) {
/* 1539:1636 */       if (!((PathwayWithEc)getPathwayList_().get(i)).weightSet)
/* 1540:     */       {
/* 1541:1637 */         pathw = 0.0F;
/* 1542:1638 */         for (int j = 0; j < ((PathwayWithEc)getPathwayList_().get(i)).ecNrs_.size(); j++)
/* 1543:     */         {
/* 1544:1639 */           String tmpEc = ((EcNr)((PathwayWithEc)getPathwayList_().get(i)).ecNrs_.get(j)).name_;
/* 1545:1640 */           tmpWeight = getWeight(tmpEc);
/* 1546:1641 */           ((EcNr)((PathwayWithEc)getPathwayList_().get(i)).ecNrs_.get(j)).weight_ = tmpWeight;
/* 1547:1642 */           pathw += tmpWeight;
/* 1548:     */         }
/* 1549:1644 */         ((PathwayWithEc)getPathwayList_().get(i)).weight_ = pathw;
/* 1550:     */         
/* 1551:1646 */         ((PathwayWithEc)getPathwayList_().get(i)).weightSet = true;
/* 1552:1647 */         if (pathw == 0.0D) {
/* 1553:1648 */           System.out.println("transfer we: " + ((PathwayWithEc)getPathwayList_().get(i)).id_ + "w " + pathw);
/* 1554:     */         }
/* 1555:1650 */         if (((PathwayWithEc)getPathwayList_().get(i)).userPathway) {
/* 1556:1651 */           System.out.println("userPath " + ((PathwayWithEc)getPathwayList_().get(i)).ecNrs_.size());
/* 1557:     */         }
/* 1558:     */       }
/* 1559:     */     }
/* 1560:     */   }
/* 1561:     */   
/* 1562:     */   public void getAllscores(int mode)
/* 1563:     */   {
/* 1564:1661 */     for (int i = 0; i < Project.samples_.size(); i++) {
/* 1565:1665 */       getScore((Sample)Project.samples_.get(i), mode);
/* 1566:     */     }
/* 1567:1667 */     getScore(Project.overall_, mode);
/* 1568:     */   }
/* 1569:     */   
/* 1570:     */   public void getScore(Sample sample, int mode)
/* 1571:     */   {
/* 1572:1674 */     double erg = 0.0D;
/* 1573:1675 */     PathwayWithEc tmp = null;
/* 1574:1677 */     for (int i = 0; i < sample.pathways_.size(); i++)
/* 1575:     */     {
/* 1576:1679 */       for (int pwCnt = 0; pwCnt < getPathwayList_().size(); pwCnt++) {
/* 1577:1680 */         if (((PathwayWithEc)sample.pathways_.get(i)).id_.contentEquals(((PathwayWithEc)getPathwayList_().get(pwCnt)).id_)) {
/* 1578:1681 */           tmp = (PathwayWithEc)getPathwayList_().get(pwCnt);
/* 1579:     */         }
/* 1580:     */       }
/* 1581:1684 */       erg = ((PathwayWithEc)sample.pathways_.get(i)).getWeight(mode) / tmp.getWeight(mode);
/* 1582:1685 */       ((PathwayWithEc)sample.pathways_.get(i)).score_ = (erg * 100.0D);
/* 1583:     */     }
/* 1584:1688 */     sample.valuesSet = true;
/* 1585:     */   }
/* 1586:     */   
/* 1587:     */   public void setWorkPath(String path)
/* 1588:     */   {
/* 1589:1695 */     this.workpath_ = path;
/* 1590:     */   }
/* 1591:     */   
/* 1592:     */   public void writeScore(Sample sample, String path, int mode)
/* 1593:     */   {
/* 1594:1704 */     String divide = "\t";
/* 1595:1705 */     if (mode == 1) {
/* 1596:1705 */       divide = ",";
/* 1597:     */     }
/* 1598:1706 */     double erg = 0.0D;
/* 1599:     */     try
/* 1600:     */     {
/* 1601:1708 */       if (path.isEmpty()) {
/* 1602:1709 */         path = this.workpath_ + "/output/weigth " + sample.name_;
/* 1603:1712 */       } else if (!path.endsWith(".txt")) {
/* 1604:1712 */         path = path + ".txt";
/* 1605:     */       }
/* 1606:1715 */       BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 1607:1716 */       out.write(sample.name_);
/* 1608:1717 */       out.newLine();
/* 1609:1718 */       for (int i = 0; i < sample.pathways_.size(); i++)
/* 1610:     */       {
/* 1611:1719 */         erg = ((PathwayWithEc)sample.pathways_.get(i)).score_;
/* 1612:1720 */         out.write(((PathwayWithEc)sample.pathways_.get(i)).id_ + divide + ((PathwayWithEc)sample.pathways_.get(i)).name_ + divide + erg);
/* 1613:1721 */         out.newLine();
/* 1614:     */       }
/* 1615:1723 */       out.close();
/* 1616:     */     }
/* 1617:     */     catch (IOException e)
/* 1618:     */     {
/* 1619:1726 */       e.printStackTrace();
/* 1620:     */     }
/* 1621:     */   }
/* 1622:     */   
/* 1623:     */   public BufferedImage alterPathway(Sample sample, PathwayWithEc pathway)
/* 1624:     */   {
/* 1625:1736 */     boolean found = false;
/* 1626:1737 */     if (this.build == null) {
/* 1627:1738 */       this.build = new PngBuilder();
/* 1628:     */     }
/* 1629:1740 */     BufferedReader xmlPath = this.reader.readTxt("pathway" + this.separator_ + pathway.id_ + ".xml");
/* 1630:1741 */     ArrayList<EcPosAndSize> tmppos = new ArrayList();
/* 1631:1743 */     for (int ecCount = 0; ecCount < pathway.ecNrs_.size(); ecCount++)
/* 1632:     */     {
/* 1633:1744 */       xmlPath = this.reader.readTxt("pathway" + this.separator_ + pathway.id_ + ".xml");
/* 1634:1745 */       tmppos = this.parser.findEcPosAndSize(((EcNr)pathway.ecNrs_.get(ecCount)).name_, xmlPath);
/* 1635:1746 */       ((EcNr)pathway.ecNrs_.get(ecCount)).addPos(tmppos);
/* 1636:     */       
/* 1637:1748 */       System.out.println(((EcNr)pathway.ecNrs_.get(ecCount)).stats_.size());
/* 1638:1749 */       if (tmppos != null) {
/* 1639:1750 */         found = true;
/* 1640:     */       }
/* 1641:     */     }
/* 1642:1753 */     if (found) {
/* 1643:1754 */       return this.build.getAlteredPathway(pathway, sample);
/* 1644:     */     }
/* 1645:     */     try
/* 1646:     */     {
/* 1647:1758 */       return ImageIO.read(new File("pics" + this.separator_ + pathway.id_ + ".png"));
/* 1648:     */     }
/* 1649:     */     catch (IOException e)
/* 1650:     */     {
/* 1651:1761 */       openWarning("Error", "File: pics" + this.separator_ + pathway.id_ + ".png" + " not found");
/* 1652:1762 */       e.printStackTrace();
/* 1653:     */     }
/* 1654:1766 */     return null;
/* 1655:     */   }
/* 1656:     */   
/* 1657:     */   public void alterPng(Sample sample)
/* 1658:     */   {
/* 1659:1773 */     if (this.build == null) {
/* 1660:1774 */       this.build = new PngBuilder();
/* 1661:     */     }
/* 1662:1776 */     PathwayWithEc tmpPath = null;
/* 1663:1777 */     BufferedReader xmlPath = null;
/* 1664:1778 */     ArrayList<EcPosAndSize> tmppos = new ArrayList();
/* 1665:1779 */     ArrayList<EcPosAndSize> pos = new ArrayList();
/* 1666:1780 */     for (int pathCount = 0; pathCount < sample.pathways_.size(); pathCount++)
/* 1667:     */     {
/* 1668:1781 */       tmpPath = (PathwayWithEc)sample.pathways_.get(pathCount);
/* 1669:1782 */       xmlPath = this.reader.readTxt("pathway" + this.separator_ + tmpPath.id_ + ".xml");
/* 1670:1784 */       while (pos.size() > 0) {
/* 1671:1785 */         pos.remove(0);
/* 1672:     */       }
/* 1673:1787 */       for (int ecCount = 0; ecCount < tmpPath.ecNrs_.size(); ecCount++)
/* 1674:     */       {
/* 1675:1788 */         xmlPath = this.reader.readTxt("pathway" + this.separator_ + tmpPath.id_ + ".xml");
/* 1676:1789 */         tmppos = this.parser.findEcPosAndSize(((EcNr)tmpPath.ecNrs_.get(ecCount)).name_, xmlPath);
/* 1677:1790 */         ((EcNr)tmpPath.ecNrs_.get(ecCount)).addPos(tmppos);
/* 1678:     */       }
/* 1679:1792 */       this.build.alterPathway(tmpPath, sample);
/* 1680:     */     }
/* 1681:     */   }
/* 1682:     */   
/* 1683:     */   public void sortPathwaysByScore(ArrayList<PathwayWithEc> pathways)
/* 1684:     */   {
/* 1685:1799 */     int tmpCnt = 0;
/* 1686:1800 */     for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++)
/* 1687:     */     {
/* 1688:1801 */       tmpCnt = pathCnt;
/* 1689:1802 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
/* 1690:1803 */         if (((PathwayWithEc)pathways.get(tmpCnt)).score_ < ((PathwayWithEc)pathways.get(pathCnt2)).score_) {
/* 1691:1804 */           tmpCnt = pathCnt2;
/* 1692:     */         }
/* 1693:     */       }
/* 1694:1807 */       pathways.add(pathCnt, (PathwayWithEc)pathways.get(tmpCnt));
/* 1695:1808 */       pathways.remove(tmpCnt + 1);
/* 1696:     */     }
/* 1697:     */   }
/* 1698:     */   
/* 1699:     */   public void sortPathwaysByName(ArrayList<PathwayWithEc> pathways)
/* 1700:     */   {
/* 1701:1816 */     int tmpCnt = 0;
/* 1702:1817 */     for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++)
/* 1703:     */     {
/* 1704:1818 */       tmpCnt = pathCnt;
/* 1705:1819 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
/* 1706:1820 */         for (int i = 0; i < ((PathwayWithEc)pathways.get(tmpCnt)).id_.length(); i++) {
/* 1707:1821 */           if (((PathwayWithEc)pathways.get(tmpCnt)).id_.charAt(i) < ((PathwayWithEc)pathways.get(tmpCnt)).id_.charAt(i)) {
/* 1708:1822 */             tmpCnt = pathCnt2;
/* 1709:     */           }
/* 1710:     */         }
/* 1711:     */       }
/* 1712:1826 */       pathways.add(pathCnt, (PathwayWithEc)pathways.get(tmpCnt));
/* 1713:1827 */       pathways.remove(tmpCnt + 1);
/* 1714:     */     }
/* 1715:     */   }
/* 1716:     */   
/* 1717:     */   public void sortAllPathwaysByName()
/* 1718:     */   {
/* 1719:1835 */     for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
/* 1720:1836 */       sortPathwaysByName(((Sample)Project.samples_.get(smpCnt)).pathways_);
/* 1721:     */     }
/* 1722:     */   }
/* 1723:     */   
/* 1724:     */   public void convertSelected()
/* 1725:     */   {
/* 1726:1842 */     for (int pathCnt = 0; pathCnt < getPathwayList_().size(); pathCnt++)
/* 1727:     */     {
/* 1728:1843 */       for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++)
/* 1729:     */       {
/* 1730:1844 */         Sample tmpSamp = (Sample)Project.samples_.get(smpCnt);
/* 1731:1845 */         for (int pathCnt2 = 0; pathCnt2 < tmpSamp.pathways_.size(); pathCnt2++) {
/* 1732:1846 */           if (((PathwayWithEc)getPathwayList_().get(pathCnt)).id_.contentEquals(((PathwayWithEc)tmpSamp.pathways_.get(pathCnt2)).id_)) {
/* 1733:1847 */             ((PathwayWithEc)tmpSamp.pathways_.get(pathCnt2)).setSelected(((PathwayWithEc)getPathwayList_().get(pathCnt)).isSelected());
/* 1734:     */           }
/* 1735:     */         }
/* 1736:     */       }
/* 1737:1851 */       for (int pathCnt2 = 0; pathCnt2 < Project.overall_.pathways_.size(); pathCnt2++) {
/* 1738:1852 */         ((PathwayWithEc)Project.overall_.pathways_.get(pathCnt2)).setSelected(((PathwayWithEc)getPathwayList_().get(pathCnt)).isSelected());
/* 1739:     */       }
/* 1740:     */     }
/* 1741:     */   }
/* 1742:     */   
/* 1743:     */   public void setPathwayList_(ArrayList<PathwayWithEc> pathwayList_)
/* 1744:     */   {
/* 1745:1859 */     this.pathwayList_ = pathwayList_;
/* 1746:     */   }
/* 1747:     */   
/* 1748:     */   public ArrayList<PathwayWithEc> getPathwayList_()
/* 1749:     */   {
/* 1750:1863 */     return pathwayList_;
/* 1751:     */   }

				  public boolean selectedPathways(){
				  	boolean ret=false;
				  	for (int i=0;i<pathwayList_.size();i++){
				  		if(pathwayList_.get(i).isSelected()){
				  			ret=true;
				  			break;
				  		}
				  	}
				  	return ret;
				  }
/* 1752:     */   
/* 1753:     */   public EcWithPathway getEc(String ecId)
/* 1754:     */   {
/* 1755:1866 */     for (int ecCnt = 0; ecCnt < ecList_.size(); ecCnt++) {
/* 1756:1867 */       if (((EcWithPathway)ecList_.get(ecCnt)).name_.contentEquals(ecId)) {
/* 1757:1868 */         return (EcWithPathway)ecList_.get(ecCnt);
/* 1758:     */       }
/* 1759:     */     }
/* 1760:1871 */     return null;
/* 1761:     */   }
/* 1762:     */   
/* 1763:     */   private void openWarning(String title, String string)
/* 1764:     */   {// open the warning frame which takes in its title and warning message
/* 1765:1875 */     JFrame frame = new JFrame(title);
/* 1766:1876 */     frame.setVisible(true);
/* 1767:1877 */     frame.setBounds(200, 200, 350, 150);
/* 1768:1878 */     frame.setLayout(null);
/* 1769:1879 */     frame.setResizable(false);
/* 1770:     */     
/* 1771:1881 */     JPanel panel = new JPanel();
/* 1772:1882 */     panel.setBounds(0, 0, 350, 150);
/* 1773:1883 */     panel.setBackground(Color.lightGray);
/* 1774:1884 */     panel.setVisible(true);
/* 1775:1885 */     panel.setLayout(null);
/* 1776:1886 */     frame.add(panel);
/* 1777:     */     
/* 1778:1888 */     JLabel label = new JLabel(string);
/* 1779:     */     
/* 1780:1890 */     label.setVisible(true);
/* 1781:1891 */     label.setForeground(Color.black);
/* 1782:1892 */     label.setBounds(0, 0, 350, 150);
/* 1783:1893 */     label.setLayout(null);
/* 1784:1894 */     panel.add(label);
/* 1785:     */     
/* 1786:1896 */     frame.repaint();
/* 1787:     */   }
/* 1788:     */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Prog.DataProcessor

 * JD-Core Version:    0.7.0.1

 */