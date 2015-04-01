/*   1:    */ package Prog;
/*   2:    */ 
/*   3:    */ import Objects.PathwayWithEc;
/*   4:    */ import Objects.Project;
/*   5:    */ import Objects.Sample;
/*   6:    */ import java.awt.Color;
/*   7:    */ import java.awt.image.BufferedImage;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ 
			//This is pretty well always in the background while in Fromp. It allous the user to save, load, open, etc. projects.	

/*  11:    */ public class Controller
/*  12:    */ {
/*  13:    */   public static Project project_;
/*  14:    */   public static DataProcessor processor_;
/*  15:    */   StringReader reader_;
/*  16: 15 */   static boolean dataChanged = true;
/*  17:    */   Color sysCol_;
/*  18:    */   
/*  19:    */   public Controller(Color sysCol)
/*  20:    */   {
/*  21: 19 */     processor_ = null;
/*  22: 20 */     this.reader_ = new StringReader();
/*  23: 21 */     newProject("default");
/*  24: 22 */     this.sysCol_ = sysCol;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void newProject(String workPath)
/*  28:    */   {
/*  29: 26 */     clearProcessor();
/*  30: 27 */     project_ = new Project(workPath);
/*  31: 28 */     Project.imported = false;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public int loadProjFile(String path)
/*  35:    */   {
/*  36: 32 */     project_ = new Project("");
/*  37: 33 */     if (path.endsWith(".frp"))
/*  38:    */     {
/*  39: 34 */       project_.importProj(path);
/*  40: 35 */       System.out.println("import");
/*  41: 36 */       return 1;
/*  42:    */     }
/*  43: 39 */     return project_.loadProject(this.reader_.readTxt(path));
/*  44:    */   }

/*  34:    */   public int loadAnotherProjFile(String path)
/*  35:    */   {
/*  37: 33 */     if (path.endsWith(".frp"))
/*  38:    */     {
/*  39: 34 */       project_.importProj(path);
/*  40: 35 */       System.out.println("import");
/*  41: 36 */       return 1;
/*  42:    */     }
/*  43: 39 */     return project_.loadProject(this.reader_.readTxt(path));
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void openProject()
/*  47:    */   {
/*  48: 44 */     clearProcessor();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static String saveProject()
/*  52:    */   {
/*  53: 49 */     if (project_ != null)
/*  54:    */     {
/*  55: 50 */       String path = project_.exportProj(null);
//*  56: 51 */       NewFrompFrame.addRecentPath(path);
/*  57: 52 */       return path;
/*  58:    */     }
/*  59: 54 */     return "";
/*  60:    */   }

				public static String saveProject(String tmpPath)
/*  52:    */   {
/*  53: 49 */     if (project_ != null)
/*  54:    */     {
/*  55: 50 */       String path = project_.exportProj(tmpPath);
//*  56: 51 */       NewFrompFrame.addRecentPath(path);
/*  57: 52 */       return path;
/*  58:    */     }
/*  59: 54 */     return "";
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setProjColor(Color col)
/*  63:    */   {
/*  64: 57 */     Project.setBackColor_(col);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void computeSampleScores()
/*  68:    */   {
/*  69: 60 */     if (Project.dataChanged) {
/*  70: 61 */       loadPathways(true);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public BufferedImage showPathwayMap(Sample sample, PathwayWithEc path)
/*  75:    */   {
/*  76: 65 */     return processor_.alterPathway(sample, path);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void clearProcessor()
/*  80:    */   {
/*  81: 69 */     processor_ = null;
/*  82: 70 */     System.gc();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static void loadPathways(boolean fullLoad)
/*  86:    */   {
/*  87: 74 */     if (processor_ == null) {
/*  88: 75 */       processor_ = new DataProcessor(project_);
/*  89:    */     }
/*  90: 77 */     if (Project.dataChanged)
/*  91:    */     {
/*  92: 78 */       if (DataProcessor.newBaseData)
/*  93:    */       {
/*  94: 79 */         System.out.println("newBaseData");
/*  95: 80 */         if (processor_ == null) {
/*  96: 81 */           processor_ = new DataProcessor(project_);
/*  97:    */         } else {
/*  98: 84 */           processor_.prepData();
/*  99:    */         }
/* 100:    */       }
/* 101: 87 */       if (!fullLoad) {
/* 102: 88 */         return;
/* 103:    */       }
/* 104: 90 */       processor_.reduce = Project.randMode_;
/* 105: 91 */       if (!Project.imported) {
/* 106: 96 */         project_.refreshProj();
/* 107:    */       }
/* 108: 98 */       processor_.processProject();
/* 109: 99 */       dataChanged = false;
/* 110:100 */       Project.dataChanged = false;
/* 111:    */     }
/* 112:    */     else {}
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean gotSamples()
/* 116:    */   {
/* 117:107 */     if (project_ == null) {
/* 118:107 */       return false;
/* 119:    */     }
/* 120:108 */     if (Project.samples_.isEmpty()) {
/* 121:108 */       return false;
/* 122:    */     }
/* 123:110 */     return true;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void copyTxtFile(String inPath, String outPath)
/* 127:    */   {
/* 128:113 */     this.reader_.copyTxtFile(inPath, outPath);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void writeScore(Sample sample, String saveAs, int mode)
/* 132:    */   {
/* 133:117 */     processor_.setWorkPath(Project.workpath_);
/* 134:118 */     processor_.writeScore(sample, saveAs, mode);
/* 135:    */   }
/* 136:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Prog.Controller

 * JD-Core Version:    0.7.0.1

 */