/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.EcNr;
/*   4:    */ import Objects.EcPosAndSize;
/*   5:    */ import Objects.PathwayWithEc;
/*   6:    */ import Objects.Project;
/*   7:    */ import Objects.Sample;
/*   8:    */ import Prog.PathButt;
/*   9:    */ import Prog.PngBuilder;
/*  10:    */ import Prog.StringReader;
/*  11:    */ import Prog.XmlParser;
/*  12:    */ import java.awt.BorderLayout;
/*  13:    */ import java.awt.Dimension;
/*  14:    */ import java.awt.event.ActionEvent;
/*  15:    */ import java.awt.event.ActionListener;
/*  16:    */ import java.awt.event.MouseEvent;
/*  17:    */ import java.awt.event.MouseListener;
/*  18:    */ import java.awt.image.BufferedImage;
/*  19:    */ import java.io.BufferedReader;
/*  20:    */ import java.io.BufferedWriter;
/*  21:    */ import java.io.File;
/*  22:    */ import java.io.FileWriter;
/*  23:    */ import java.io.IOException;
/*  24:    */ import java.io.PrintStream;
/*  25:    */ import java.util.ArrayList;
/*  26:    */ import java.util.Calendar;
/*  27:    */ import javax.imageio.ImageIO;
/*  28:    */ import javax.swing.JButton;
/*  29:    */ import javax.swing.JCheckBox;
/*  30:    */ import javax.swing.JFileChooser;
/*  31:    */ import javax.swing.JLabel;
/*  32:    */ import javax.swing.JPanel;
/*  33:    */ import javax.swing.JScrollPane;
/*  34:    */ import javax.swing.filechooser.FileFilter;
/*  35:    */ import pathwayLayout.PathLayoutGrid;

				// The "Show score-matrix" tab of the pathway completeness analysis
/*  36:    */ 
/*  37:    */ public class PathwayMatrix
/*  38:    */   extends JPanel
/*  39:    */ {
/*  40:    */   private static final long serialVersionUID = 1L;	// 
/*  41:    */   private ArrayList<Sample> samples_;					// The arraylist of samples 
/*  42:    */   private Sample overSample_;							// 
/*  43:    */   private ArrayList<PathwayWithEc> pathways_;			// Arraylist of pathways
/*  44:    */   private ArrayList<PathwayWithEc> tmpPathways_;		// Arraylist of ecs mapped to pathways
/*  45:    */   private double[][] scoreMat;						// 
/*  46:    */   private double[][] rnScoreMat;						// 
/*  47:    */   Project actProj_;									// the active project
/*  48: 57 */   int linDis = 40;									// 
/*  49: 58 */   int colDis = 275;									// 
/*  50: 59 */   int xDist = 250;									// 
/*  51: 60 */   int offset = 100;									// 
/*  52:    */   JLabel name;										// 
/*  53:    */   JCheckBox showRn_;									// 
/*  54:    */   JLabel mouseOverDisp;								// 
/*  55:    */   JPanel mouseOverP;									// 
/*  56:    */   JLabel mouseOverFrDisp;								// 
/*  57:    */   JPanel optionsPanel_;								// the options panel upon which the options, ie the buttons and checkboxes, will sit
/*  58:    */   JPanel displayP_;									// The display panel upon which this pathway matrix will be displayed 
/*  59:    */   JScrollPane showJPanel_;							// 
/*  60:    */   private JCheckBox useCsf_;							// 
/*  61:    */   MouseOverFrame infoFrame;							// 
/*  62:    */   int xWidth;											// 
/*  63:    */   				
/*  64:    */   public PathwayMatrix(ArrayList<Sample> samples, Sample overallSample, ArrayList<PathwayWithEc> pathways, Project actProj)
/*  65:    */   {
/*  66: 82 */     this.samples_ = new ArrayList();
/*  67: 83 */     this.mouseOverDisp = new JLabel("Additional Pathway-information");
/*  68: 86 */     for (int smpCnt = 0; smpCnt < samples.size(); smpCnt++) {
/*  69: 87 */       this.samples_.add((Sample)samples.get(smpCnt));
/*  70:    */     }
/*  71: 89 */     this.pathways_ = pathways;
/*  72: 90 */     this.overSample_ = overallSample;
/*  73: 91 */     this.actProj_ = actProj;
/*  74:    */     
/*  75: 93 */     this.xWidth = ((Project.samples_.size() + 2) * this.colDis);
/*  76: 94 */     setSize(this.xWidth, 2100);
/*  77: 95 */     setLayout(new BorderLayout());
/*  78: 96 */     setVisible(true);
/*  79: 97 */     setBackground(Project.getBackColor_());
/*  80:    */     
/*  81: 99 */     initMainPanels();
/*  82:    */     
/*  83:101 */     addOptions();
/*  84:102 */     writeMatrix();
/*  85:    */   }
/*  86:    */   
/*  87:    */   private void initMainPanels()
/*  88:    */   {// innitiates the options, display, and show panel
/*  89:106 */     this.optionsPanel_ = new JPanel();
/*  90:107 */     this.optionsPanel_.setPreferredSize(new Dimension(this.xWidth, 100));
/*  91:108 */     this.optionsPanel_.setLocation(0, 0);
/*  92:109 */     this.optionsPanel_.setBackground(Project.standard);
/*  93:110 */     this.optionsPanel_.setVisible(true);
/*  94:111 */     this.optionsPanel_.setLayout(null);
/*  95:112 */     add(this.optionsPanel_, "First");
/*  96:    */     
/*  97:114 */     this.displayP_ = new JPanel();
/*  98:115 */     this.displayP_.setLocation(0, 0);
/*  99:116 */     this.displayP_.setPreferredSize(new Dimension(this.xWidth, 8000));
/* 100:117 */     this.displayP_.setSize(getPreferredSize());
/* 101:118 */     this.displayP_.setBackground(Project.getBackColor_());
/* 102:119 */     this.displayP_.setVisible(true);
/* 103:120 */     this.displayP_.setLayout(null);
/* 104:    */     
/* 105:122 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/* 106:123 */     this.showJPanel_.setVisible(true);
/* 107:124 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/* 108:125 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/* 109:    */     
/* 110:127 */     add("Center", this.showJPanel_);
/* 111:    */   }
/* 112:    */   
/* 113:    */   private void addOptions()
/* 114:    */   {// adds the buttons, etc. to the options panel
/* 115:135 */     if (this.useCsf_ == null) {
/* 116:136 */       this.useCsf_ = new JCheckBox("CSF");
/* 117:    */     }
/* 118:138 */     this.useCsf_.setVisible(true);
/* 119:139 */     this.useCsf_.setLayout(null);
/* 120:140 */     this.useCsf_.setBackground(this.optionsPanel_.getBackground());
/* 121:141 */     this.useCsf_.setForeground(Project.getFontColor_());
/* 122:142 */     this.useCsf_.setBounds(10, 44, 100, 15);
/* 123:143 */     this.optionsPanel_.add(this.useCsf_);
/* 124:    */     
/* 125:145 */     JButton writeButt = new JButton("Write matrix");
/* 126:146 */     writeButt.setBounds(10, 10, 150, 25);
/* 127:147 */     writeButt.setVisible(true);
/* 128:148 */     writeButt.setLayout(null);
/* 129:149 */     writeButt.addActionListener(new ActionListener()
/* 130:    */     {
/* 131:    */       public void actionPerformed(ActionEvent e)
/* 132:    */       {
/* 133:154 */         PathwayMatrix.this.prepMat();
/* 134:    */         
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:160 */         JFileChooser fChoose_ = new JFileChooser(Project.workpath_);
/* 140:161 */         fChoose_.setFileSelectionMode(0);
/* 141:162 */         fChoose_.setBounds(100, 100, 200, 20);
/* 142:163 */         fChoose_.setVisible(true);
/* 143:164 */         File file = new File(Project.workpath_);
/* 144:165 */         fChoose_.setSelectedFile(file);
/* 145:166 */         fChoose_.setFileFilter(new FileFilter()
/* 146:    */         {
/* 147:    */           public boolean accept(File f)
/* 148:    */           {
/* 149:170 */             if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".txt"))) {
/* 150:171 */               return true;
/* 151:    */             }
/* 152:173 */             return false;
/* 153:    */           }
/* 154:    */           
/* 155:    */           public String getDescription()
/* 156:    */           {
/* 157:179 */             return ".txt";
/* 158:    */           }
/* 159:    */         });
/* 160:183 */         if (fChoose_.showSaveDialog(null) == 0) {
/* 161:    */           try
/* 162:    */           {
/* 163:185 */             String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 164:187 */             if (!path.endsWith(".txt"))
/* 165:    */             {
/* 166:188 */               path = path + ".txt";
/* 167:189 */               System.out.println(".txt");
/* 168:    */             }
/* 169:191 */             PathwayMatrix.this.exportMat(path, PathwayMatrix.this.useCsf_.isSelected());
/* 170:    */           }
/* 171:    */           catch (IOException e1)
/* 172:    */           {
/* 173:194 */             e1.printStackTrace();
/* 174:    */           }
/* 175:    */         }
/* 176:    */       }
/* 177:199 */     });
/* 178:200 */     this.optionsPanel_.add(writeButt);
/* 179:    */     
/* 180:    */ 
/* 181:203 */     JButton openDetailFr = new JButton("Pathway-Info-Window");
/* 182:204 */     openDetailFr.setBounds(420, 10, 200, 25);
/* 183:205 */     openDetailFr.setVisible(true);
/* 184:206 */     openDetailFr.addActionListener(new ActionListener()
/* 185:    */     {
/* 186:    */       public void actionPerformed(ActionEvent e)
/* 187:    */       {
/* 188:211 */         PathwayMatrix.this.addMouseOverFrame();
/* 189:    */       }
/* 190:213 */     });
/* 191:214 */     this.optionsPanel_.add(openDetailFr);
/* 192:    */     
/* 193:216 */     this.mouseOverP = new JPanel();
/* 194:217 */     this.mouseOverP.setBackground(Project.getBackColor_());
/* 195:218 */     this.mouseOverP.setBounds(660, 10, 500, 60);
/* 196:219 */     this.optionsPanel_.add(this.mouseOverP);
/* 197:    */     
/* 198:    */ 
/* 199:222 */     this.mouseOverDisp.setBounds(0, 0, 500, 60);
/* 200:223 */     this.mouseOverP.add(this.mouseOverDisp);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void writeMatrix()
/* 204:    */   {// writes the matrix to the display panel to be seen by the user
/* 205:233 */     int counter = 0;
/* 206:234 */     for (int i = 0; i < this.pathways_.size(); i++)
/* 207:    */     {
/* 208:235 */       final PathwayWithEc fPath = new PathwayWithEc((PathwayWithEc)this.overSample_.pathways_.get(i), false);
/* 209:236 */       if (pwIsSelected(fPath)) {
/* 210:239 */         if (fPath.score_ >= Project.minVisScore_)
/* 211:    */         {
/* 212:242 */           PathButt pathNames = new PathButt(this.samples_, this.overSample_, (PathwayWithEc)this.overSample_.pathways_.get(i), getBackground(), "", 1);
/* 213:243 */           pathNames.addMouseListener(new MouseListener()
/* 214:    */           {
/* 215:    */             public void mouseReleased(MouseEvent e) {}
/* 216:    */             
/* 217:    */             public void mousePressed(MouseEvent e) {}
/* 218:    */             
/* 219:    */             public void mouseExited(MouseEvent e)
/* 220:    */             {
/* 221:261 */               PathwayMatrix.this.mouseOverDisp.setText("Additional Pathway-information");
/* 222:262 */               PathwayMatrix.this.repaint();
/* 223:    */             }
/* 224:    */             
/* 225:    */             public void mouseEntered(MouseEvent e)
/* 226:    */             {
/* 227:269 */               PathwayMatrix.this.setAdditionalInfo(fPath);
/* 228:270 */               PathwayMatrix.this.repaint();
/* 229:    */             }
/* 230:    */             
/* 231:    */             public void mouseClicked(MouseEvent e) {}
/* 232:278 */           });
/* 233:279 */           pathNames.setBounds(this.xDist - this.colDis, this.linDis + this.linDis * counter + this.offset, this.colDis, this.linDis);
/* 234:280 */           this.displayP_.add(pathNames);
/* 235:281 */           counter++;
/* 236:    */         }
/* 237:    */       }
/* 238:    */     }
/* 239:283 */     int sampleNr = Project.samples_.size();
/* 240:284 */     int pathNr = this.pathways_.size();
/* 241:288 */     for (int sampleCnt = 0; sampleCnt < sampleNr; sampleCnt++)
/* 242:    */     {
/* 243:289 */       Sample tmpSample = (Sample)Project.samples_.get(sampleCnt);
/* 244:    */       
/* 245:291 */       final JLabel fullName = new JLabel(tmpSample.name_);
/* 246:292 */       int x = this.colDis * (sampleCnt + 1);
/* 247:293 */       int y = this.linDis;
/* 248:294 */       fullName.setBounds(x, 0 + this.offset, 500, 20);
/* 249:295 */       fullName.setForeground(tmpSample.sampleCol_);
/* 250:296 */       fullName.setVisible(false);
/* 251:297 */       this.displayP_.add(fullName);
/* 252:    */       
/* 253:299 */       final JLabel smpName = new JLabel(tmpSample.name_);
/* 254:300 */       smpName.setBounds(0, 0, this.colDis, 20);
/* 255:301 */       smpName.setForeground(tmpSample.sampleCol_);
/* 256:    */       
/* 257:303 */       JPanel mouseOver = new JPanel();
/* 258:304 */       mouseOver.setBounds(x, 20 + this.offset, this.colDis, 20);
/* 259:305 */       mouseOver.setBackground(Project.getBackColor_());
/* 260:306 */       mouseOver.setLayout(null);
/* 261:307 */       mouseOver.addMouseListener(new MouseListener()
/* 262:    */       {
/* 263:    */         public void mouseReleased(MouseEvent e) {}
/* 264:    */         
/* 265:    */         public void mousePressed(MouseEvent e) {}
/* 266:    */         
/* 267:    */         public void mouseExited(MouseEvent e)
/* 268:    */         {
/* 269:324 */           smpName.setVisible(true);
/* 270:325 */           fullName.setVisible(false);
/* 271:    */         }
/* 272:    */         
/* 273:    */         public void mouseEntered(MouseEvent e)
/* 274:    */         {
/* 275:331 */           smpName.setVisible(false);
/* 276:332 */           fullName.setVisible(true);
/* 277:    */         }
/* 278:    */         
/* 279:    */         public void mouseClicked(MouseEvent e) {}
/* 280:340 */       });
/* 281:341 */       mouseOver.add(smpName);
/* 282:342 */       this.displayP_.add(mouseOver);
/* 283:    */       
/* 284:    */ 
/* 285:345 */       this.tmpPathways_ = tmpSample.pathways_;
/* 286:    */       
/* 287:347 */       counter = 0;
/* 288:348 */       for (int pathCnt = 0; (pathCnt < pathNr) || (pathCnt < this.tmpPathways_.size()); pathCnt++)
/* 289:    */       {
/* 290:349 */         final PathwayWithEc fPath = new PathwayWithEc((PathwayWithEc)this.tmpPathways_.get(pathCnt), false);
/* 291:350 */         if (pwIsSelected(fPath)) {
/* 292:353 */           if (fPath.score_ >= Project.minVisScore_)
/* 293:    */           {
/* 294:357 */             PathButt scores = new PathButt(this.samples_, tmpSample, fPath, getBackground(), "", 1);
/* 295:    */             
/* 296:    */ 
/* 297:360 */             scores.addMouseListener(new MouseListener()
/* 298:    */             {
/* 299:    */               public void mouseReleased(MouseEvent e) {}
/* 300:    */               
/* 301:    */               public void mousePressed(MouseEvent e) {}
/* 302:    */               
/* 303:    */               public void mouseExited(MouseEvent e)
/* 304:    */               {
/* 305:378 */                 PathwayMatrix.this.mouseOverDisp.setText("Additional Pathway-information");
/* 306:379 */                 PathwayMatrix.this.repaint();
/* 307:    */               }
/* 308:    */               
/* 309:    */               public void mouseEntered(MouseEvent e)
/* 310:    */               {
/* 311:386 */                 PathwayMatrix.this.setAdditionalInfo(fPath);
/* 312:387 */                 PathwayMatrix.this.repaint();
/* 313:    */               }
/* 314:    */               
/* 315:    */               public void mouseClicked(MouseEvent e) {}
/* 316:395 */             });
/* 317:396 */             x = this.colDis * (sampleCnt + 1);
/* 318:397 */             y = this.linDis + this.linDis * counter;
/* 319:398 */             scores.setBounds(x, y + this.offset, this.colDis, this.linDis);
/* 320:399 */             this.displayP_.add(scores);
/* 321:400 */             counter++;
/* 322:    */           }
/* 323:    */         }
/* 324:    */       }
/* 325:    */     }
/* 326:    */   }
/* 327:    */   
/* 328:    */   public void prepMat()
/* 329:    */   {// builds the 2d array of doubles: scoreMat
/* 330:405 */     this.scoreMat = new double[this.samples_.size() + 1][this.overSample_.pathways_.size()];
/* 331:406 */     for (int pwCnt = 0; pwCnt < this.overSample_.pathways_.size(); pwCnt++)
/* 332:    */     {
/* 333:407 */       this.scoreMat[0][pwCnt] = ((PathwayWithEc)this.overSample_.pathways_.get(pwCnt)).score_;
/* 334:408 */       for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++) {
/* 335:409 */         this.scoreMat[(1 + smpCnt)][pwCnt] = ((PathwayWithEc)((Sample)this.samples_.get(smpCnt)).pathways_.get(pwCnt)).score_;
/* 336:    */       }
/* 337:    */     }
/* 338:    */   }
/* 339:    */   
/* 340:    */   public void prepRnMat()
/* 341:    */   {// builds the 2d array of doubles: rnScoreMat
/* 342:414 */     this.rnScoreMat = new double[this.samples_.size() + 1][this.overSample_.rnPathways_.size()];
/* 343:415 */     for (int pwCnt = 0; pwCnt < this.overSample_.rnPathways_.size(); pwCnt++)
/* 344:    */     {
/* 345:416 */       this.rnScoreMat[0][pwCnt] = ((PathwayWithEc)this.overSample_.rnPathways_.get(pwCnt)).score_;
/* 346:417 */       for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++) {
/* 347:418 */         this.rnScoreMat[(1 + smpCnt)][pwCnt] = ((PathwayWithEc)((Sample)this.samples_.get(smpCnt)).rnPathways_.get(pwCnt)).score_;
/* 348:    */       }
/* 349:    */     }
/* 350:    */   }
/* 351:    */   
/* 352:    */   private void setAdditionalInfo(PathwayWithEc path)
/* 353:    */   {
/* 354:423 */     this.mouseOverDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_ + "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_ + "</html>");
/* 355:425 */     if (this.infoFrame != null) {
/* 356:426 */       this.infoFrame.setAdditionalInfo(path);
/* 357:    */     }
/* 358:    */   }
/* 359:    */   
/* 360:    */   private void addMouseOverFrame()
/* 361:    */   {
/* 362:430 */     if (MouseOverFrame.getFrameCount() <= 0) {
/* 363:431 */       this.infoFrame = new MouseOverFrame();
/* 364:    */     }
/* 365:    */   }
/* 366:    */   
/* 367:    */   private boolean pwIsSelected(PathwayWithEc pathway)
/* 368:    */   {
/* 369:436 */     for (int i = 0; i < this.pathways_.size(); i++) {
/* 370:437 */       if (pathway.id_ == ((PathwayWithEc)this.pathways_.get(i)).id_)
/* 371:    */       {
/* 372:438 */         if (!((PathwayWithEc)this.pathways_.get(i)).isSelected()) {
/* 373:439 */           return false;
/* 374:    */         }
/* 375:442 */         return true;
/* 376:    */       }
/* 377:    */     }
/* 378:446 */     return false;
/* 379:    */   }
/* 380:    */   
/* 381:    */   public void exportMat(String path, boolean inCsf)
/* 382:    */   {// exports the pathway matrix to a file
/* 383:451 */     String seperator = "\t";
/* 384:452 */     if (inCsf) {
/* 385:453 */       seperator = ",";
/* 386:    */     }
/* 387:    */     try
/* 388:    */     {
/* 389:456 */       BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 390:    */       
/* 391:    */ 
/* 392:    */ 
/* 393:    */ 
/* 394:    */ 
/* 395:462 */       out.write("**** n.s. = 'not selected' ******** n.a. = not above minScore matrix is resorted");
/* 396:463 */       out.newLine();
/* 397:    */       
/* 398:465 */       out.write(Project.projectPath_ + "PathayScoreMatrix");
/* 399:    */       
/* 400:467 */       out.newLine();
/* 401:468 */       out.newLine();
/* 402:469 */       out.write(" " + seperator + "OverAll");
/* 403:470 */       for (int i = 0; i < this.samples_.size(); i++) {
/* 404:471 */         if (((Sample)this.samples_.get(i)).inUse) {
/* 405:474 */           out.write(((Sample)this.samples_.get(i)).name_ + seperator);
/* 406:    */         }
/* 407:    */       }
/* 408:476 */       out.newLine();
/* 409:477 */       for (int pathCnt1 = 0; pathCnt1 < this.overSample_.pathways_.size(); pathCnt1++)
/* 410:    */       {
/* 411:478 */         PathwayWithEc pathway1 = (PathwayWithEc)this.overSample_.pathways_.get(pathCnt1);
/* 412:479 */         if (!pathway1.isSelected())
/* 413:    */         {
/* 414:480 */           out.write(pathway1.id_ + seperator + pathway1.name_ + seperator + "n.s.");
/* 415:481 */           for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++) {
/* 416:482 */             out.write(seperator + "n.s.");
/* 417:    */           }
/* 418:    */         }
/* 419:    */         else
/* 420:    */         {
/* 421:486 */           out.write(pathway1.id_ + seperator + pathway1.name_ + seperator + pathway1.score_);
/* 422:487 */           for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++)
/* 423:    */           {
/* 424:488 */             Sample tmpSample = (Sample)this.samples_.get(smpCnt);
/* 425:489 */             if (tmpSample.inUse)
/* 426:    */             {
/* 427:492 */               PathwayWithEc pathway2 = tmpSample.getPath(pathway1.id_);
/* 428:493 */               if (pathway2.score_ > Project.minVisScore_) {
/* 429:494 */                 out.write(seperator + pathway2.score_);
/* 430:    */               } else {
/* 431:497 */                 out.write(seperator + "n.a.");
/* 432:    */               }
/* 433:    */             }
/* 434:    */           }
/* 435:500 */           out.newLine();
/* 436:    */         }
/* 437:    */       }
/* 438:503 */       out.close();
/* 439:    */     }
/* 440:    */     catch (IOException e)
/* 441:    */     {
/* 442:508 */       File f = new File(path);
/* 443:509 */       f.mkdirs();
/* 444:510 */       if (!path.endsWith(".txt"))
/* 445:    */       {
/* 446:511 */         Calendar cal = Calendar.getInstance();
/* 447:    */         
/* 448:513 */         int day = cal.get(5);
/* 449:514 */         int month = cal.get(2) + 1;
/* 450:515 */         int year = cal.get(1);
/* 451:    */         
/* 452:    */ 
/* 453:    */ 
/* 454:519 */         String date = day + "." + month + "." + year;
/* 455:520 */         path = path + File.separator + "pwScore" +"."+Project.workpath_+"."+ date;
/* 456:    */       }
					String tmpPath=path+".txt";
					File file1 = new File(tmpPath);
					if(file1.exists() && !file1.isDirectory()) { 
					  int i=1;
					  while("Pigs"!="Fly"){// loop forever
					  	tmpPath=path+"("+i+")"+".txt";
					  	File file2=new File(tmpPath);
					    if(file2.exists() && !file2.isDirectory()) { 
					  		i++;
					  		continue;
					  	} else{
					  		path=path+"("+i+")";
					  		break;
					  	}
					  }
					}
					path=path+".txt";
/* 457:522 */       exportMat(path, inCsf);
/* 458:    */     }
/* 459:524 */     System.out.println("outputfile succesfully written. Path: " + path);
/* 460:    */   }
/* 461:    */   
/* 462:    */   public void exportPics(String path, boolean onlyOverall, boolean onlyUserP)
/* 463:    */   {// exports the sample ppictures
/* 464:531 */     if (!onlyOverall) {
/* 465:532 */       for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++)
/* 466:    */       {
/* 467:533 */         Sample sample = (Sample)this.samples_.get(smpCnt);
/* 468:534 */         ArrayList<PathwayWithEc> pathways = ((Sample)this.samples_.get(smpCnt)).pathways_;
/* 469:    */         
/* 470:536 */         System.out.println("Exporting picture from sample: " + sample.name_);
/* 471:538 */         for (int pwCnt = 0; pwCnt < pathways.size(); pwCnt++)
/* 472:    */         {
/* 473:540 */           PathwayWithEc pathway = (PathwayWithEc)pathways.get(pwCnt);
/* 474:541 */           if ((!onlyUserP) || (pathway.userPathway))
/* 475:    */           {
/* 476:544 */             BufferedImage image = buildImage(pathway, sample);
/* 477:    */             
/* 478:546 */             System.out.println("Pathway: : " + pathway.id_ + " " + pathway.name_);
/* 479:547 */             if (!pathway.id_.contentEquals("-1"))
/* 480:    */             {
/* 481:548 */               PathwayMapFrame mFrame = new PathwayMapFrame(Project.samples_, sample, pathway);
/* 482:549 */               image = mFrame.drawImage(image);
/* 483:    */             }
/* 484:551 */             exportPic(image, path, pathway, sample);
/* 485:    */           }
/* 486:    */         }
/* 487:    */       }
/* 488:    */     }
/* 489:555 */     Sample sample = Project.overall_;
/* 490:556 */     ArrayList<PathwayWithEc> pathways = sample.pathways_;
/* 491:557 */     System.out.println("Exporting picture from sample: " + sample.name_);
/* 492:558 */     for (int pwCnt = 0; pwCnt < pathways.size(); pwCnt++)
/* 493:    */     {
/* 494:559 */       PathwayWithEc pathway = (PathwayWithEc)pathways.get(pwCnt);
/* 495:560 */       if ((!onlyUserP) || (pathway.userPathway))
/* 496:    */       {
/* 497:563 */         BufferedImage image = buildImage(pathway, sample);
/* 498:564 */         System.out.println("Pathway: : " + pathway.id_ + " " + pathway.name_);
/* 499:565 */         if (!pathway.id_.contentEquals("-1"))
/* 500:    */         {
/* 501:566 */           PathwayMapFrame mFrame = new PathwayMapFrame(Project.samples_, sample, pathway);
/* 502:567 */           image = mFrame.drawImage(image);
/* 503:    */         }
/* 504:569 */         exportPic(image, path, pathway, sample);
/* 505:    */       }
/* 506:    */     }
/* 507:    */   }
/* 508:    */   
/* 509:    */   private void exportPic(BufferedImage image, String path, PathwayWithEc pathway, Sample sample)
/* 510:    */   {
/* 511:    */     try
/* 512:    */     {
/* 513:574 */       File f = new File(path);
/* 514:575 */       f.mkdirs();
/* 515:576 */       if (!path.endsWith(".png"))
/* 516:    */       {
/* 517:577 */         Calendar cal = Calendar.getInstance();
/* 518:    */         
/* 519:579 */         int day = cal.get(5);
/* 520:580 */         int month = cal.get(2) + 1;
/* 521:581 */         int year = cal.get(1);
/* 522:    */         
/* 523:    */ 
/* 524:    */ 
/* 525:585 */         String date = day + "." + month + "." + year;
/* 526:586 */         path = path + File.separator + sample.name_ + "_" + pathway.id_ +"_"+Project.workpath_ +"_" + date + ".png";
/* 527:    */       }
/* 528:588 */       if (image == null)
/* 529:    */       {
/* 530:589 */         System.err.println("Image could not be generated " + pathway.id_);
/* 531:590 */         return;
/* 532:    */       }
/* 533:592 */       ImageIO.write(image, "png", new File(path));
/* 534:    */     }
/* 535:    */     catch (IOException e)
/* 536:    */     {
/* 537:595 */       File f = new File(path);
/* 538:596 */       f.mkdirs();
/* 539:597 */       if (!path.endsWith(".png"))
/* 540:    */       {
/* 541:598 */         Calendar cal = Calendar.getInstance();
/* 542:    */         
/* 543:600 */         int day = cal.get(5);
/* 544:601 */         int month = cal.get(2) + 1;
/* 545:602 */         int year = cal.get(1);
/* 546:    */         
/* 547:    */ 
/* 548:    */ 
/* 549:606 */         String date = day + "." + month + "." + year;
/* 550:607 */         path = path + File.separator + sample.name_ + "_" + pathway.id_ + "_"+Project.workpath_ +"_" + date + ".png";
/* 551:    */       }
/* 552:609 */       exportPic(image, path, pathway, sample);
/* 553:    */     }
/* 554:    */   }
/* 555:    */   
/* 556:    */   private BufferedImage buildImage(PathwayWithEc path, Sample samp)
/* 557:    */   {
/* 558:613 */     if (path.userPathway)
/* 559:    */     {
/* 560:614 */       BufferedImage tmpImage = buildUserPath(path.pathwayInfoPAth, path, samp);
/* 561:615 */       return tmpImage;
/* 562:    */     }
/* 563:618 */     if (path.score_ > 0.0D)
/* 564:    */     {
/* 565:619 */       BufferedImage tmpImage = showPathwayMap(samp, path);
/* 566:620 */       return tmpImage;
/* 567:    */     }
/* 568:    */     try
/* 569:    */     {
/* 570:625 */       if (path.id_ != "-1") {
/* 571:626 */         return ImageIO.read(new File("pics" + File.separator + path.id_ + ".png"));
/* 572:    */       }
/* 573:    */     }
/* 574:    */     catch (IOException e1)
/* 575:    */     {
/* 576:632 */       e1.printStackTrace();
/* 577:    */     }
/* 578:636 */     return null;
/* 579:    */   }
/* 580:    */   
/* 581:    */   private BufferedImage buildUserPath(String loadPath, PathwayWithEc pathway, Sample samp)
/* 582:    */   {
/* 583:639 */     PathLayoutGrid grid = new PathLayoutGrid(10, 10, false);
/* 584:640 */     grid.openPathWay(loadPath);
/* 585:641 */     BufferedImage image = grid.buildPicture(pathway, samp);
/* 586:642 */     return image;
/* 587:    */   }
/* 588:    */   
/* 589:    */   public BufferedImage showPathwayMap(Sample sample, PathwayWithEc path)
/* 590:    */   {
/* 591:645 */     return alterPathway(sample, path);
/* 592:    */   }
/* 593:    */   
/* 594:    */   public BufferedImage alterPathway(Sample sample, PathwayWithEc pathway)
/* 595:    */   {
/* 596:648 */     boolean found = false;
/* 597:    */     
/* 598:    */ 
/* 599:    */ 
/* 600:    */ 
/* 601:    */ 
/* 602:654 */     PngBuilder builder_ = new PngBuilder();
/* 603:655 */     StringReader reader = new StringReader();
/* 604:656 */     XmlParser parser = new XmlParser();
/* 605:    */     
/* 606:658 */     BufferedReader xmlPath = reader.readTxt("pathway" + File.separator + pathway.id_ + ".xml");
/* 607:659 */     ArrayList<EcPosAndSize> tmppos = new ArrayList();
/* 608:661 */     for (int ecCount = 0; ecCount < pathway.ecNrs_.size(); ecCount++)
/* 609:    */     {
/* 610:662 */       xmlPath = reader.readTxt("pathway" + File.separator + pathway.id_ + ".xml");
/* 611:663 */       tmppos = parser.findEcPosAndSize(((EcNr)pathway.ecNrs_.get(ecCount)).name_, xmlPath);
/* 612:664 */       ((EcNr)pathway.ecNrs_.get(ecCount)).addPos(tmppos);
/* 613:665 */       if (tmppos != null) {
/* 614:666 */         found = true;
/* 615:    */       }
/* 616:    */     }
/* 617:669 */     if (found) {
/* 618:670 */       return builder_.getAlteredPathway(pathway, sample);
/* 619:    */     }
/* 620:    */     try
/* 621:    */     {
/* 622:674 */       return ImageIO.read(new File("pics" + File.separator + pathway.id_ + ".png"));
/* 623:    */     }
/* 624:    */     catch (IOException e)
/* 625:    */     {
/* 626:677 */       e.printStackTrace();
/* 627:    */     }
/* 628:681 */     return null;
/* 629:    */   }
/* 630:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.PathwayMatrix

 * JD-Core Version:    0.7.0.1

 */