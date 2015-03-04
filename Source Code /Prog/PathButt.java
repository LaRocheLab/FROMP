/*   1:    */ package Prog;
/*   2:    */ 
/*   3:    */ import Objects.EcNr;
/*   4:    */ import Objects.EcPosAndSize;
/*   5:    */ import Objects.PathwayWithEc;
/*   6:    */ import Objects.Sample;
/*   7:    */ import Panes.PathwayMapFrame;
/*   8:    */ import java.awt.Color;
/*   9:    */ import java.awt.event.ActionEvent;
/*  10:    */ import java.awt.event.ActionListener;
/*  11:    */ import java.awt.image.BufferedImage;
/*  12:    */ import java.io.BufferedReader;
/*  13:    */ import java.io.File;
/*  14:    */ import java.io.IOException;
/*  15:    */ import java.io.PrintStream;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import javax.imageio.ImageIO;
/*  18:    */ import javax.swing.JButton;
/*  19:    */ import pathwayLayout.PathLayoutGrid;
/*  20:    */ 
/*  21:    */ public class PathButt
/*  22:    */   extends JButton
/*  23:    */ {
/*  24:    */   private static final long serialVersionUID = 1L;
/*  25:    */   final Sample samp_;
/*  26:    */   final PathwayWithEc path_;
/*  27:    */   final ArrayList<Sample> samples_;
/*  28:    */   final String workpath_;
/*  29: 32 */   String tmpWeight = "";
/*  30: 33 */   String tmpScore = "";
/*  31: 34 */   String tmpName = "";
/*  32:    */   PathwayMapFrame pathMap;
/*  33:    */   private PngBuilder builder_;
/*  34:    */   StringReader reader;
/*  35:    */   XmlParser parser;
/*  36: 39 */   String separator_ = File.separator;
/*  37: 40 */   boolean chaining = true;
/*  38:    */   
/*  39:    */   public PathButt(ArrayList<Sample> samples, Sample samp, PathwayWithEc path, Color backCol, String workpath, int mode)
/*  40:    */   {
/*  41: 43 */     this.workpath_ = workpath;
/*  42: 44 */     this.samp_ = samp;
/*  43: 45 */     this.samples_ = samples;
/*  44: 46 */     this.path_ = path;
/*  45: 47 */     this.tmpWeight = String.valueOf(this.path_.weight_);
/*  46: 48 */     if (this.tmpWeight.length() > 5) {
/*  47: 49 */       this.tmpWeight = this.tmpWeight.substring(0, 5);
/*  48:    */     }
/*  49: 51 */     this.tmpScore = String.valueOf(this.path_.score_);
/*  50: 52 */     if (this.tmpScore.length() > 5) {
/*  51: 53 */       this.tmpScore = this.tmpScore.substring(0, 5);
/*  52:    */     }
/*  53: 55 */     setBackground(backCol);
/*  54: 56 */     setOpaque(false);
/*  55: 57 */     setForeground(this.samp_.sampleCol_);
/*  56: 58 */     this.tmpName = this.path_.name_;
/*  57: 59 */     if (this.tmpName.contentEquals("testPath")) {
/*  58: 60 */       System.err.println("PathButt");
/*  59:    */     }
/*  60: 62 */     if (this.tmpName.length() > 30) {
/*  61: 63 */       this.tmpName = this.tmpName.substring(0, 30);
/*  62:    */     }
/*  63: 65 */     if (mode == 0) {
/*  64: 66 */       setText("<html>" + this.tmpName + "<br>ID:" + this.path_.id_ + "| Wgt:" + this.tmpWeight + "| Scr:" + this.tmpScore + "</html>");
/*  65: 68 */     } else if (mode == 1) {
/*  66: 69 */       setText("<html>" + this.tmpName + "<br>score: " + this.tmpScore + "</html>");
/*  67:    */     }
/*  68: 72 */     addActionListener(new ActionListener()
/*  69:    */     {
/*  70:    */       public void actionPerformed(ActionEvent e)
/*  71:    */       {
/*  72: 76 */         System.out.println(PathButt.this.samp_.singleSample_);
/*  73: 78 */         if (PathButt.this.path_.userPathway)
/*  74:    */         {
                        System.out.println("This 1");
/*  75: 79 */           BufferedImage tmpImage = PathButt.this.buildUserPath(PathButt.this.path_.pathwayInfoPAth, PathButt.this.path_, PathButt.this.samp_);
/*  76: 80 */           PathButt.this.pathMap = new PathwayMapFrame(PathButt.this.samples_, PathButt.this.samp_, PathButt.this.path_, tmpImage, PathButt.this.workpath_);
/*  77: 81 */           PathButt.this.pathMap.invalidate();
/*  78: 82 */           PathButt.this.pathMap.validate();
/*  79: 83 */           PathButt.this.pathMap.repaint();
/*  80:    */         }
/*  81: 86 */         else if (PathButt.this.path_.score_ > 0.0D)
/*  82:    */         {
                        System.out.println("or this 2");
/*  83: 87 */           BufferedImage tmpImage = PathButt.this.showPathwayMap(PathButt.this.samp_, PathButt.this.path_);
/*  84: 88 */           PathButt.this.pathMap = new PathwayMapFrame(PathButt.this.samples_, PathButt.this.samp_, PathButt.this.path_, tmpImage, PathButt.this.workpath_);
/*  85: 89 */           PathButt.this.pathMap.invalidate();
/*  86: 90 */           PathButt.this.pathMap.validate();
/*  87: 91 */           PathButt.this.pathMap.repaint();
/*  88:    */         }
/*  89:    */         else
/*  90:    */         {
/*  91:    */           try
/*  92:    */           {
/*  93: 95 */             if (PathButt.this.path_.id_ != "-1")
/*  94:    */             {
                            System.out.println("else if this 3");
/*  95: 96 */               BufferedImage tmpImage = ImageIO.read(new File("pics" + File.separator + PathButt.this.path_.id_ + ".png"));
/*  96: 97 */               PathButt.this.pathMap = new PathwayMapFrame(PathButt.this.samples_, PathButt.this.samp_, PathButt.this.path_, tmpImage, PathButt.this.workpath_);
/*  97:    */             }
/*  98:    */             else
/*  99:    */             {
                            System.out.println("else this 4");
/* 100:100 */               PathButt.this.pathMap = new PathwayMapFrame(PathButt.this.samp_, PathButt.this.path_, PathButt.this.workpath_);
/* 101:    */             }
/* 102:102 */             PathButt.this.pathMap.invalidate();
/* 103:103 */             PathButt.this.pathMap.validate();
/* 104:104 */             PathButt.this.pathMap.repaint();
/* 105:    */           }
/* 106:    */           catch (IOException e1)
/* 107:    */           {
/* 108:108 */             e1.printStackTrace();
/* 109:    */           }
/* 110:    */         }
/* 111:    */       }
/* 112:    */     });
/* 113:    */   }
/* 114:    */   
/* 115:    */   private BufferedImage buildUserPath(String loadPath, PathwayWithEc pathway, Sample samp)
/* 116:    */   {
/* 117:117 */     PathLayoutGrid grid = new PathLayoutGrid(10, 10, false);
/* 118:118 */     grid.openPathWay(loadPath);
/* 119:119 */     BufferedImage image = grid.buildPicture(pathway, samp);
/* 120:120 */     return image;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public BufferedImage showPathwayMap(Sample sample, PathwayWithEc path)
/* 124:    */   {
/* 125:123 */     return alterPathway(sample, path);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public BufferedImage alterPathway(Sample sample, PathwayWithEc pathway)
/* 129:    */   {
/* 130:126 */     boolean found = false;
/* 131:127 */     if (this.builder_ == null) {
/* 132:128 */       this.builder_ = new PngBuilder();
/* 133:    */     }
/* 134:130 */     if (this.reader == null) {
/* 135:131 */       this.reader = new StringReader();
/* 136:    */     }
/* 137:133 */     if (this.parser == null) {
/* 138:134 */       this.parser = new XmlParser();
/* 139:    */     }
/* 140:136 */     BufferedReader xmlPath = this.reader.readTxt("pathway" + this.separator_ + pathway.id_ + ".xml");
/* 141:137 */     ArrayList<EcPosAndSize> tmppos = new ArrayList();
/* 142:139 */     for (int ecCount = 0; ecCount < pathway.ecNrs_.size(); ecCount++)
/* 143:    */     {
/* 144:140 */       xmlPath = this.reader.readTxt("pathway" + this.separator_ + pathway.id_ + ".xml");
/* 145:141 */       tmppos = this.parser.findEcPosAndSize(((EcNr)pathway.ecNrs_.get(ecCount)).name_, xmlPath);
/* 146:142 */       ((EcNr)pathway.ecNrs_.get(ecCount)).addPos(tmppos);
/* 147:143 */       if (tmppos != null) {
/* 148:144 */         found = true;
/* 149:    */       }
/* 150:    */     }
/* 151:147 */     if (found) {
/* 152:148 */       return this.builder_.getAlteredPathway(pathway, sample);
/* 153:    */     }
/* 154:    */     try
/* 155:    */     {
/* 156:152 */       return ImageIO.read(new File("pics" + this.separator_ + pathway.id_ + ".png"));
/* 157:    */     }
/* 158:    */     catch (IOException e)
/* 159:    */     {
/* 160:155 */       e.printStackTrace();
/* 161:    */     }
/* 162:159 */     return null;
/* 163:    */   }
/* 164:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Prog.PathButt

 * JD-Core Version:    0.7.0.1

 */