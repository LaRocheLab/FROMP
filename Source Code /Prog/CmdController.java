/*   1:    */ package Prog;
/*   2:    */ 
/*   3:    */ import Objects.Project;
/*   4:    */ import Objects.Sample;
/*   5:    */ import Panes.ActMatrixPane;
/*   6:    */ import Panes.PathwayActivitymatrixPane;
/*   7:    */ import Panes.PathwayMatrix;
/*   8:    */ import java.awt.Color;
/*   9:    */ import java.awt.Dimension;
/*  10:    */ import java.io.BufferedReader;
/*  11:    */ import java.io.File;
/*  12:    */ import java.io.FileReader;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.util.ArrayList;
/*  21:    */ import java.io.IOException;
/*  15:    */ 
			//this controls the cmdFromp functions 

/*  16:    */ public class CmdController
/*  17:    */ {
/*  18:    */   public static String[] args_;												// An array of String arguments taken in from the command line
/*  19:    */   String inputPath_;															// The inputpath given by the user  
/*  20:    */   String outPutPath_;															// The output path given in by the user
/*  21:    */   String optionsCmd_;															// The option denoted by the user. ie h for help, etc
				ArrayList<String> ec_;														// If an EC number was denoted by the user to output sequence IDs, this is the variable it is saved to
/*  22:    */   static Controller controller;												// The controller. Allows user to save, load etc.
/*  23:    */   static PathwayMatrix pwMAtrix;												// the Pathway Matrix
				final String basePath_ = new File(".").getAbsolutePath() + File.separator;	// The base path of the Fromp software. Necessary for all relative paths to function
				BufferedReader ecList;														// Buffered reader used to read in the 
				StringReader reader;														// String reader to assist in the reading of the 
/*  24:    */   
/*  25:    */   public CmdController(String[] args)
/*  26:    */   {
/*  27: 30 */     System.out.println("Starting cmdFromp");
/*  28:    */     
/*  29: 32 */     args_ = args;
				  this.ec_ = new ArrayList<String>();
				  if(args_.length == 2){
				  	if(checkEC(args_[1])){
				  		this.ec_.add(args_[1]);
				  	} else if (args_[1].contentEquals("seq")) {
				  		this.optionsCmd_=args_[1];
				  	}
					this.inputPath_ = getInputPath();						
/*  31: 34 */     	System.out.println("input: " + this.inputPath_);
				  }
				  else if(args_.length == 3){
				  	if(checkEC(args_[1])){
				  		this.inputPath_ = getInputPath();						
/*  31: 34 */     		System.out.println("input: " + this.inputPath_);		
						this.ec_.add(args_[1]);
						this.ec_.add(args_[2]);
				  	}
				  	else if (args_[1].contentEquals("seq")){
				  		this.inputPath_ = getInputPath();
				  		this.optionsCmd_=args_[1];
				  		this.ec_.add(args_[2]);
				  	}
				  	else{
/*  30: 33 */     		this.inputPath_ = getInputPath();					
/*  31: 34 */     		System.out.println("input: " + this.inputPath_);	
/*  32: 35 */     		this.outPutPath_ = getOutputPath();					
/*  33: 36 */     		System.out.println("output: " + this.outPutPath_);	
/*  34: 37 */     		this.optionsCmd_ = getOptionCmd();					
/*  35: 38 */     		System.out.println("option: " + this.optionsCmd_);	

				  	}
				  }
				  else if(args.length >= 4){
				  	if(checkEC(args_[1])){
				  		this.inputPath_ = getInputPath();						
/*  31: 34 */     		System.out.println("input: " + this.inputPath_);
						System.out.println("ECs");	
				  		for(int i=1;i<args.length;i++){
				  			this.ec_.add(args_[i]);
				  			System.out.println(args_[i]);
				  		}
				  	}
				  	else if(args_[1].contentEquals("seq")){
				  		this.inputPath_ = getInputPath();
				  		this.optionsCmd_=args_[1];
				  		System.out.println("input: " + this.inputPath_);
						System.out.println("Seqs");	
				  		for(int i=1;i<args.length;i++){
				  			this.ec_.add(args_[i]);
				  			System.out.println(args_[i]);
				  		}
				  	}
				  	else{
				  		this.inputPath_ = getInputPath();						
/*  31: 34 */     		System.out.println("input: " + this.inputPath_);	
/*  32: 35 */     		this.outPutPath_ = getOutputPath();					
/*  33: 36 */     		System.out.println("output: " + this.outPutPath_);	
/*  34: 37 */     		this.optionsCmd_ = getOptionCmd();					
/*  35: 38 */     		System.out.println("option: " + this.optionsCmd_);	
						System.out.println("ECs");
						for(int i=3;i<args.length;i++){
				  			this.ec_.add(args_[i]);
				  			System.out.println(args_[i]);
				  		}
				  	}
				  }



/*  36: 39 */     controller = new Controller(Color.black);
/*  37:    */     
/*  38: 41 */     Panes.Loadingframe.showLoading = false;
/*  39: 42 */     Panes.HelpFrame.showSummary = false;
/*  40: 44 */     if (this.inputPath_.endsWith(".frp"))					// If the input file is of the the project file type, load the project
/*  41:    */     {
/*  42: 45 */       controller.loadProjFile(this.inputPath_);
/*  43:    */     }
/*  51: 54 */     else if (this.inputPath_.endsWith(".lst"))			// If the input file is of the .lst type, itterate through the file and build samples for all of the file paths in the file, if the line starts with <userP> a new userpath is added. They are all added to a new project
/*  52:    */     {
/*  53:    */       try
/*  54:    */       {
/*  55: 56 */         BufferedReader in = new BufferedReader(new FileReader(this.inputPath_));
/*  56:    */         
/*  57: 58 */         String comp = "<userP>";
/*  58:    */         String line=in.readLine();
//					  System.out.println("Entering while loop");
/*  59: 60 */         while ((line) != null)
/*  60:    */         {
//						System.out.println("Looping");
						try{
							//String line;
/*  62: 61 */           	if (((line) != null) && line.startsWith(comp))
/*  63:    */          		{
/*  64: 62 */           	  String userP = line.substring(comp.length());
							  System.out.println("user pathway made");
/*  65: 63 */           	  Project.addUserP(userP);
							  System.out.println("pathway added");
/*  66:    */           	}
/*  67:    */           	else if ((line) != null) 
/*  68:    */           	{
							  if(!line.contains("\t")){ // If the line doesnt contain a tab then assume that is is just the path to the project or sample file
							  	File f=new File (line);
							  	if (f.exists() && !f.isDirectory() && line.endsWith(".frp")){
							  		controller.loadAnotherProjFile(line);
							  		System.out.println("Project file added");
							  		Project.workpath_=inputPath_.substring(inputPath_.lastIndexOf(File.separator),inputPath_.lastIndexOf("."));
				  					if(Project.workpath_.contains(File.separator)){
				  						Project.workpath_=Project.workpath_.replace(File.separator,"");
				  					}
								  }
								  else if (f.exists() && !f.isDirectory()){
/*  69: 66 */           		  	String name = line.substring(line.lastIndexOf(File.separator));
//								  	System.out.println("substring");
/*  70: 67 */           		  	Color col = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
/*  71: 68 */           		  	Sample sample = new Sample(name, line, col);
/*  72:    */           		  	
/*  73: 70 */           		  	Project.samples_.add(sample);
								  	System.out.println("sample added");
								  }
								  else{System.out.println("file does not exist");}
								}
								else{ // If the line does contain a tab then split the line into the the sample file and the sequence file
									String sampleString=line.substring(0,line.indexOf("\t"));
									String sequenceString=line.substring(line.indexOf("\t"));
									String tmpName="";
									Boolean newSample = false;
									File f=new File (sampleString);
							  		if (f.exists() && !f.isDirectory() && sampleString.endsWith(".frp")){
							  			controller.loadAnotherProjFile(sampleString);
							  			System.out.println("Project file added");
							  			Project.workpath_=inputPath_.substring(inputPath_.lastIndexOf(File.separator),inputPath_.lastIndexOf("."));
				  						if(Project.workpath_.contains(File.separator)){
				  							Project.workpath_=Project.workpath_.replace(File.separator,"");
				  						}
									}
									else if (f.exists() && !f.isDirectory()){
/*  69: 66 */           			  	String name = sampleString.substring(sampleString.lastIndexOf(File.separator));
//									  	System.out.println("substring");
/*  70: 67 */           			  	Color col = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
/*  71: 68 */           			  	Sample sample = new Sample(name, sampleString, col);
/*  72:    */           			  	newSample=true;
										tmpName=name;
/*  73: 70 */           			  	Project.samples_.add(sample);
									  	System.out.println("sample added");
									}
									else{System.out.println("file does not exist");}

									File f1=new File (sequenceString);
									if (f.exists() && !f.isDirectory() && newSample && tmpName!=null && tmpName!=""){
										for(int i=0;i<Project.samples_.size();i++){
											if(Project.samples_.get(i).name_.contentEquals(tmpName)){

											}
										}
									}
								}
/*  74:    */           	}

						}
						catch (Exception e)
/*  78:    */       	{
/*  79: 74 */       	  e.printStackTrace();
						  continue;
/*  80:    */       	}
/*  61:    */           line=in.readLine();
/*  75:    */         }
/*  76:    */       }
/*  77:    */       catch (Exception e)
/*  78:    */       {
/*  79: 74 */         e.printStackTrace();
/*  80:    */       }
/*  81:    */     }
				  
/*  44: 47 */     else
/*  45:    */     {
/*  46: 49 */       String name = this.inputPath_.substring(this.inputPath_.lastIndexOf(File.separator));
/*  47: 50 */       Sample sample = new Sample(name, this.inputPath_, Color.red);
/*  48:    */       
/*  49: 52 */       Project.samples_.add(sample);
/*  50:    */     }

/*  82: 77 */     Controller.loadPathways(true);
				  Project.workpath_=inputPath_.substring(inputPath_.lastIndexOf(File.separator),inputPath_.lastIndexOf("."));
				  if(Project.workpath_.contains(File.separator)){
				  	Project.workpath_=Project.workpath_.replace(File.separator,"");
				  }

				  if(!ec_.isEmpty()){
				  	if(this.optionsCmd_!=null){
				  		if(!this.optionsCmd_.contentEquals("seq")){
				  			ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
				  			System.out.println("Repseqs will be saved at: "+basePath_+"RepSeqIDs/");
				  			for(int i=0;i<ec_.size();i++){
								pane.cmdExportRepseqs(this.ec_.get(i));				  		
							}
				  		}
				  	}
				  	else{
				  		ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
				  		System.out.println("Repseqs will be saved at: "+basePath_+"RepSeqIDs/");
				  		for(int i=0;i<ec_.size();i++){
							pane.cmdExportRepseqs(this.ec_.get(i));				  		
						}
				  	}
				  }
				  if(this.optionsCmd_!=null){
				  	  if(this.optionsCmd_.contentEquals("ec")){
				  	  	this.reader = new StringReader();
				  	  	this.ecList=this.reader.readTxt(outPutPath_);
				  	  	ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
				  		System.out.println("Repseqs will be saved at: "+basePath_+"RepSeqIDs/");
				  	  	String line ="";
				  	  	try{
				  	  		while((line = this.ecList.readLine()) != null){
				  	  			if(checkEC(line)){
				  	  				pane.cmdExportRepseqs(line);	
				  	  			}
				  	  		}
				  	  	} catch(IOException e){
							e.printStackTrace();
						}
				  	  	System.exit(0);
				  	  }
				  	  if((this.optionsCmd_.contentEquals("seq"))&& !ec_.isEmpty()){
				  	  	ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
				  		System.out.println("Sequences will be saved at: "+basePath_+"Sequences/");
				  		for(int i=0;i<ec_.size();i++){
							pane.cmdExportSequences(this.ec_.get(i));				  		
						}
						if ((this.optionsCmd_.contentEquals("seq"))){
							System.exit(0);
						}	
				  	  } else if((this.optionsCmd_.contentEquals("seq"))){
				  	  	this.reader = new StringReader();
				  	  	this.ecList=this.reader.readTxt(outPutPath_);
				  	  	ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
				  		System.out.println("Sequences will be saved at: "+basePath_+"Sequences/");
				  	  	String line ="";
				  	  	try{
				  	  		while((line = this.ecList.readLine()) != null){
				  	  			if(checkEC(line)){
				  	  				pane.cmdExportSequences(line);	
				  	  			}
				  	  		}
				  	  	} catch(IOException e){
							e.printStackTrace();
						}
				  	  	System.exit(0);
				  	  }
/*  83: 78 */   	  if ((this.optionsCmd_.contentEquals("p")) || (this.optionsCmd_.contentEquals("op")) || (this.optionsCmd_.contentEquals("up")) || (this.optionsCmd_.contentEquals("a")))
/*  84:    */   	  {//this if statment contains al of the picture export commands
/*  85: 79 */   	    System.out.println("Pathway-score-matrix");
/*  86: 80 */   	    pwMAtrix = new PathwayMatrix(Project.samples_, Project.overall_, DataProcessor.pathwayList_, Controller.project_);//builds a pathway matrix object whigh will be used to generate the pathway pictures
/*  87: 81 */   	    if (this.optionsCmd_.contentEquals("a"))
/*  88:    */   	    {//if the a command is selected export all of the pathway pictures
/*  89: 82 */   	      String tmpPAth = this.outPutPath_ + File.separator + "pathwayPics";
/*  90: 83 */   	      System.out.println("PathwayPics will be saved at: " + tmpPAth);
/*  91: 84 */   	      pwMAtrix.exportPics(tmpPAth, false, false);
/*  92:    */   	    }
/*  93: 87 */   	    else if (this.optionsCmd_.contentEquals("p"))
/*  94:    */   	    {//if the p command is selected export all of the pathway pictures, then exit
						  String tmpPAth = this.outPutPath_ + File.separator + "pathwayPics";
/*  90: 83 */   	      System.out.println("PathwayPics will be saved at: " + tmpPAth);
/*  95: 88 */   	      pwMAtrix.exportPics(tmpPAth, false, false);
/*  96: 89 */   	      System.exit(0);
/*  97:    */   	    }
/*  98: 91 */   	    else if (this.optionsCmd_.contentEquals("op"))
/*  99:    */   	    {// if the op command is selected export all the multi pathway pictures, then exit
						  String tmpPAth = this.outPutPath_ + File.separator + "multiPathwayPics";
/*  90: 83 */   	      System.out.println("PathwayPics will be saved at: " + tmpPAth);
/* 100: 92 */   	      pwMAtrix.exportPics(tmpPAth, true, false);//note the true in the exportPics call. There was no false in the previous calls. This is how it differs
/* 101: 93 */   	      System.exit(0);
/* 102:    */   	    }
/* 103: 95 */   	    else if (this.optionsCmd_.contentEquals("up"))
/* 104:    */   	    {//if the up command is selected then export only the user pathway pictures, then exit 
						  String tmpPAth = this.outPutPath_ + File.separator + "userPathwayPics";
/*  90: 83 */     	    System.out.println("PathwayPics will be saved at: " + tmpPAth);
/* 105: 96 */     	    System.out.println("onlyUserPaths");
/* 106: 97 */     	    pwMAtrix.exportPics(tmpPAth, true, true);//both are true here
/* 107: 98 */     	    System.exit(0);
/* 108:    */     	  }
/* 109:101 */     	  if (this.optionsCmd_.contentEquals("p")) {
/* 110:102 */     	    System.exit(0);
/* 111:    */     	  }
/* 112:    */     	}
/* 113:105 */   	  if ((this.optionsCmd_.contentEquals("s")) || (this.optionsCmd_.contentEquals("a")) || (this.optionsCmd_.contentEquals("am")))
/* 114:    */   	  {//exports the pathway score matrix for the corresponding commands. if s is selected it exits afterwards
/* 115:106 */   	    System.out.println("Pathway-score-matrix");
/* 116:107 */   	    pwMAtrix = new PathwayMatrix(Project.samples_, Project.overall_, DataProcessor.pathwayList_, Controller.project_);
/* 117:108 */   	    pwMAtrix.exportMat(this.outPutPath_, true);
/* 118:109 */   	    if (this.optionsCmd_.contentEquals("s")) {
/* 119:110 */   	      System.exit(0);
/* 120:    */   	    }
/* 121:    */   	  }
/* 122:113 */   	  if ((this.optionsCmd_.contentEquals("m")) || (this.optionsCmd_.contentEquals("a")) || (this.optionsCmd_.contentEquals("am")))
/* 123:    */   	  {//exports the pathway activity matrix for the corresponding commands. if m is selected it exits afterwards
/* 124:114 */   	    System.out.println("Pathway-activity-matrix");
/* 125:115 */   	    PathwayActivitymatrixPane pane = new PathwayActivitymatrixPane(Controller.processor_, Controller.project_, new Dimension(23, 23));
/* 126:116 */   	    pane.exportMat(false, this.outPutPath_);
/* 127:117 */   	    if (this.optionsCmd_.contentEquals("m")) {
/* 128:118 */   	      System.exit(0);
/* 129:    */   	    }
/* 130:    */   	  }
/* 131:121 */   	  if ((this.optionsCmd_.contentEquals("e")) || (this.optionsCmd_.contentEquals("a"))  || (this.optionsCmd_.contentEquals("am")))
/* 132:    */   	  {//exports the EC-activity matrix for the corresponding commands. if e is selected it exits afterwards
/* 133:122 */   	    System.out.println("EC-activity-matrix");
/* 134:123 */   	    ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
/* 135:124 */   	    pane.exportMat(this.outPutPath_, true);
//						if((args_.length==4)&&(this.args_[3]!=null)){
//							System.out.println("Repseqs will be saved at: "+basePath_+"RepSeqIDs/");
//							pane.cmdExportRepseqs(this.ec_);
//						}
/* 136:125 */   	    if (this.optionsCmd_.contentEquals("e")) {
/* 137:126 */   	      System.exit(0);
/* 138:    */   	    }
/* 139:    */   	  }
					  if ((this.optionsCmd_.contentEquals("f")) || (this.optionsCmd_.contentEquals("a")) || (this.optionsCmd_.contentEquals("am")))
/* 132:    */   	  {//exports the samples as a .frp file so that it cam be used in the gui later. if f is selected it exits afterwards
/* 133:122 */   	    System.out.println("Export as .frp file");
						
            	   		String projPath= "";
						if (Project.projectPath_.contains("projects")){
							projPath=Project.projectPath_.substring(0,Project.projectPath_.indexOf("projects")-1);
							} else {
							projPath=Project.projectPath_;
						}
						String tmpPath= projPath + File.separator + "projects" + File.separator + inputPath_.substring(inputPath_.lastIndexOf(File.separator),inputPath_.lastIndexOf(".")) + ".frp";
/* 135:124 */   	    System.out.println(tmpPath);
						controller.saveProject(tmpPath);
/* 136:125 */     	  if (this.optionsCmd_.contentEquals("f")) {
/* 137:126 */     	    System.exit(0);
/* 138:    */     	  }
/* 139:    */     	}
				  }
				  
/* 140:129 */     System.exit(0);
/* 141:    */   }
/* 142:    */   
/* 143:    */   private String getInputPath()
/* 144:    */   {
/* 145:134 */     return args_[0];
/* 146:    */   }
/* 147:    */   
/* 148:    */   private String getOutputPath()
/* 149:    */   {
/* 150:137 */     return args_[1];
/* 151:    */   }
/* 152:    */   
/* 153:    */   private String getOptionCmd()
/* 154:    */   {
/* 155:140 */     return args_[2];
/* 156:    */   }
				private static boolean checkEC(String options)
/* 173:    */   {//checks that the EC is complete
/* 174:163 */     boolean ret = false;
				  String testStr1;
				  String testStr2;
				  String testStr3;

/* 175:164 */     if (options.matches("[0-9].*")) {
/* 194:183 */       if(options.contains(".")){
						testStr1=options.substring(options.indexOf(".")+1);
						if(testStr1.matches("[0-9].*")){
							if(testStr1.contains(".")){
								testStr2=testStr1.substring(testStr1.indexOf(".")+1);
								if(testStr2.matches("[0-9].*")){
									if(testStr2.contains(".")){
										testStr3=testStr2.substring(testStr2.indexOf(".")+1);
										if(testStr3.matches("[0-9]*")){
											ret=true;
										}
									}
								}
							}
						}
					}
/* 195:    */     }
/* 196:185 */     return ret;
/* 197:    */   }
/* 157:    */ }
