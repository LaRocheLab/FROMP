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
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.util.ArrayList;
/*  15:    */ 
/*  16:    */ public class CmdController
/*  17:    */ {
/*  18:    */   public static String[] args_;
/*  19:    */   String inputPath_;
/*  20:    */   String outPutPath_;
/*  21:    */   String optionsCmd_;
/*  22:    */   static Controller controller;
/*  23:    */   static PathwayMatrix pwMAtrix;
				final String basePath_ = new File(".").getAbsolutePath() + File.separator;
/*  24:    */   
/*  25:    */   public CmdController(String[] args)
/*  26:    */   {
/*  27: 30 */     System.out.println("Starting cmdFromp");
/*  28:    */     
/*  29: 32 */     args_ = args;
/*  30: 33 */     this.inputPath_ = getInputPath();
/*  31: 34 */     System.out.println("input: " + this.inputPath_);
/*  32: 35 */     this.outPutPath_ = getOutputPath();
/*  33: 36 */     System.out.println("output: " + this.outPutPath_);
/*  34: 37 */     this.optionsCmd_ = getOptionCmd();
/*  35: 38 */     System.out.println("option: " + this.optionsCmd_);
/*  36: 39 */     controller = new Controller(Color.black);
/*  37:    */     
/*  38: 41 */     Panes.Loadingframe.showLoading = false;
/*  39: 42 */     Panes.HelpFrame.showSummary = false;
/*  40: 44 */     if (this.inputPath_.endsWith(".frp"))
/*  41:    */     {
/*  42: 45 */       controller.loadProjFile(this.inputPath_);
/*  43:    */     }
/*  44: 47 */     else if (this.inputPath_.endsWith(".out"))
/*  45:    */     {
/*  46: 49 */       String name = this.inputPath_.substring(this.inputPath_.lastIndexOf(File.separator));
/*  47: 50 */       Sample sample = new Sample(name, this.inputPath_, Color.red);
/*  48:    */       
/*  49: 52 */       Project.samples_.add(sample);
/*  50:    */     }
/*  51: 54 */     else if (this.inputPath_.endsWith(".lst"))
/*  52:    */     {
/*  53:    */       try
/*  54:    */       {
/*  55: 56 */         BufferedReader in = new BufferedReader(new FileReader(this.inputPath_));
/*  56:    */         
/*  57: 58 */         String comp = "<userP>";
/*  58:    */         String line=in.readLine();
					  System.out.println("Entering while loop");
/*  59: 60 */         while ((line) != null)
/*  60:    */         {
						System.out.println("Looping");
						try{
							line=in.readLine();
							//String line;
/*  62: 61 */           	if (line.startsWith(comp))
/*  63:    */          		{
/*  64: 62 */           	  String userP = line.substring(comp.length());
							  System.out.println("user pathway made");
/*  65: 63 */           	  Project.addUserP(userP);
							  System.out.println("pathway added");
/*  66:    */           	}
/*  67:    */           	else
/*  68:    */           	{
							  File f=new File (line);
							  if (f.exists() && !f.isDirectory()){
/*  69: 66 */           	  	String name = line.substring(line.lastIndexOf(File.separator));
							  	System.out.println("substring");
/*  70: 67 */           	  	Color col = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
/*  71: 68 */           	  	Sample sample = new Sample(name, line, col);
/*  72:    */           	  	
/*  73: 70 */           	  	Project.samples_.add(sample);
							  	System.out.println("sample added");
							  }
							  else{System.out.println("file does not exist");}

/*  74:    */           	}
						}
						catch (Exception e)
/*  78:    */       	{
/*  79: 74 */       	  e.printStackTrace();
						  continue;
/*  80:    */       	}
/*  61:    */           
/*  75:    */         }
/*  76:    */       }
/*  77:    */       catch (Exception e)
/*  78:    */       {
/*  79: 74 */         e.printStackTrace();
/*  80:    */       }
/*  81:    */     }
				  else{
				  	System.out.println("Incorrect File Format");
				  	System.exit(1);
				  }
/*  82: 77 */     Controller.loadPathways(true);
/*  83: 78 */     if ((this.optionsCmd_.contentEquals("p")) || (this.optionsCmd_.contentEquals("op")) || (this.optionsCmd_.contentEquals("up")) || (this.optionsCmd_.contentEquals("a")))
/*  84:    */     {
/*  85: 79 */       System.out.println("Pathway-score-matrix");
/*  86: 80 */       pwMAtrix = new PathwayMatrix(Project.samples_, Project.overall_, DataProcessor.pathwayList_, Controller.project_);
/*  87: 81 */       if (this.optionsCmd_.contentEquals("a"))
/*  88:    */       {
/*  89: 82 */         String tmpPAth = this.outPutPath_ + File.separator + "pathwayPics";
/*  90: 83 */         System.out.println("PathwayPics will be saved at: " + tmpPAth);
/*  91: 84 */         pwMAtrix.exportPics(tmpPAth, false, false);
/*  92:    */       }
/*  93: 87 */       else if (this.optionsCmd_.contentEquals("p"))
/*  94:    */       {
/*  95: 88 */         pwMAtrix.exportPics(this.outPutPath_, false, false);
/*  96: 89 */         System.exit(0);
/*  97:    */       }
/*  98: 91 */       else if (this.optionsCmd_.contentEquals("op"))
/*  99:    */       {
/* 100: 92 */         pwMAtrix.exportPics(this.outPutPath_, true, false);
/* 101: 93 */         System.exit(0);
/* 102:    */       }
/* 103: 95 */       else if (this.optionsCmd_.contentEquals("up"))
/* 104:    */       {
/* 105: 96 */         System.out.println("onlyUserPaths");
/* 106: 97 */         pwMAtrix.exportPics(this.outPutPath_, true, true);
/* 107: 98 */         System.exit(0);
/* 108:    */       }
/* 109:101 */       if (this.optionsCmd_.contentEquals("p")) {
/* 110:102 */         System.exit(0);
/* 111:    */       }
/* 112:    */     }
/* 113:105 */     if ((this.optionsCmd_.contentEquals("s")) || (this.optionsCmd_.contentEquals("a")) || (this.optionsCmd_.contentEquals("am")))
/* 114:    */     {
/* 115:106 */       System.out.println("Pathway-score-matrix");
/* 116:107 */       pwMAtrix = new PathwayMatrix(Project.samples_, Project.overall_, DataProcessor.pathwayList_, Controller.project_);
/* 117:108 */       pwMAtrix.exportMat(this.outPutPath_, true);
/* 118:109 */       if (this.optionsCmd_.contentEquals("s")) {
/* 119:110 */         System.exit(0);
/* 120:    */       }
/* 121:    */     }
/* 122:113 */     if ((this.optionsCmd_.contentEquals("m")) || (this.optionsCmd_.contentEquals("a")) || (this.optionsCmd_.contentEquals("am")))
/* 123:    */     {
/* 124:114 */       System.out.println("Pathway-activity-matrix");
/* 125:115 */       PathwayActivitymatrixPane pane = new PathwayActivitymatrixPane(Controller.processor_, Controller.project_, new Dimension(23, 23));
/* 126:116 */       pane.exportMat(false, this.outPutPath_);
/* 127:117 */       if (this.optionsCmd_.contentEquals("m")) {
/* 128:118 */         System.exit(0);
/* 129:    */       }
/* 130:    */     }
/* 131:121 */     if ((this.optionsCmd_.contentEquals("e")) || (this.optionsCmd_.contentEquals("a"))  || (this.optionsCmd_.contentEquals("am")))
/* 132:    */     {
/* 133:122 */       System.out.println("EC-activity-matrix");
/* 134:123 */       ActMatrixPane pane = new ActMatrixPane(Controller.project_, DataProcessor.ecList_, Controller.processor_, new Dimension(12, 12));
/* 135:124 */       pane.exportMat(this.outPutPath_, true);
/* 136:125 */       if (this.optionsCmd_.contentEquals("e")) {
/* 137:126 */         System.exit(0);
/* 138:    */       }
/* 139:    */     }
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
/* 157:    */ }