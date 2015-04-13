/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.EcNr;
/*   4:    */ import Objects.Line;
/*   5:    */ import Objects.Pathway;
/*   6:    */ import Objects.PathwayWithEc;
/*   7:    */ import Objects.Project;
/*   8:    */ import Objects.Sample;
/*   9:    */ import Prog.DataProcessor;
/*  10:    */ import Prog.Exporter;
/*  11:    */ import Prog.PathButt;
/*  12:    */ import java.awt.BorderLayout;
/*  13:    */ import java.awt.Color;
/*  14:    */ import java.awt.Dimension;
/*  15:    */ import java.awt.event.ActionEvent;
/*  16:    */ import java.awt.event.ActionListener;
/*  17:    */ import java.awt.event.MouseEvent;
/*  18:    */ import java.awt.event.MouseListener;
/*  19:    */ import java.io.File;
/*  20:    */ import java.io.PrintStream;
/*  21:    */ import java.util.ArrayList;
/*  22:    */ import java.util.Calendar;
/*  23:    */ import javax.swing.JButton;
/*  24:    */ import javax.swing.JCheckBox;
/*  25:    */ import javax.swing.JLabel;
/*  26:    */ import javax.swing.JPanel;
/*  27:    */ import javax.swing.JScrollPane;
/*  28:    */ 

			//The Pathway Activity Matrix Pane. All things you see happening in the Pathway Activity part of the gui are controlled here.

/*  29:    */ public class PathwayActivitymatrixPane
/*  30:    */   extends JPanel
/*  31:    */ {
/*  32:    */   private static final long serialVersionUID = 1L;	// 
/*  33:    */   DataProcessor proc_;								// DataProcessor object to do the parsing and computations nessesairy to build the activity matrix
/*  34:    */   Project actProj_;									// The active project
/*  35:    */   Exporter exp_;										// Used to export the matrix to a file 
/*  36:    */   boolean includeWeights_;							// Boolean to say whether or not to to include the weights
/*  37:    */   JButton export_;									// Button used in the gui to export the activity matrix
/*  38:    */   boolean changed_;									// 
/*  39:    */   JCheckBox includeWeights;							// Checkboxes for sorting, normalizing and including the weights of the matrix
/*  40:    */   JCheckBox normalizeMax_;							// 
/*  41:    */   JCheckBox normalizeSum_;							// 
/*  42:    */   JCheckBox sortByLine_;								// 
/*  43:    */   int numOfpaths;										// Number of paths
/*  44:    */   int numOfSamples;									// Number of samples
/*  45:    */   ArrayList<PathwayWithEc> paths_;					// ArrayList of the pathways which have ecs mapped
/*  46:    */   ArrayList<Line> lineMatrix_;						// An arraylist of line objects used to build the activity matrix
/*  47:    */   MouseOverFrame infoFrame;							// A mouse over frame used to display information about what you are mousing over of the main fromp frame
/*  48:    */   private JPanel optionsPanel_;						// The options panel at the top of the display for this frame
/*  49:    */   private JPanel displayP_;							// The main display panel where the matrix is displayed 
/*  50:    */   private JScrollPane showJPanel_;					// A scroll pane which houses the display panel such that if the display panel exceded the alloted space of the frame it is fit to the display size and the usedr is able to scroll through the content
/*  51:    */   private JLabel mouseOverDisp;						// A label which holds the same purpose as the MouseOverFrame
/*  52:    */   private JPanel mouseOverP;							// 
/*  53:    */   private JCheckBox useCsf_;							// 
/*  54:    */   
/*  55:    */   public PathwayActivitymatrixPane(DataProcessor proc, Project proj, Dimension dim)
/*  56:    */   {
/*  57: 64 */     System.out.println("Pathway Activity matrix");
/*  58: 65 */     this.proc_ = proc;
/*  59: 66 */     this.actProj_ = proj;
/*  60:    */     
/*  61: 68 */     setSize(dim);
/*  62: 69 */     setVisible(true);
/*  63: 70 */     setLayout(new BorderLayout());
/*  64: 71 */     setBackground(Project.getBackColor_());
/*  65: 72 */     setPreferredSize(dim);
/*  66: 73 */     this.includeWeights_ = false;	
/*  67:    */     
/*  68: 75 */     prepPaths();					// Prepares the Pathwaylist this.paths_
/*  69:    */     								// 
/*  70: 77 */     this.changed_ = true;			// Notes that the data has been changed
/*  71:    */     								// 
/*  72:    */ 									// 
/*  73: 80 */     initMainPanels();				// Initiates the options panel, the display panel, and the scroll panel
/*  74: 81 */     addOptions();					// Adds checkboxes, buttons, and labels to the options panel
/*  75: 82 */     createMatrix();				// Builds the activity matrix by generating the arraylist of line objects which will be used to display the matrix
/*  76: 83 */     showMatrix();					// Generates the onscreen activity matrix
/*  77:    */     								// 
/*  78:    */ 									// 
/*  79:    */ 									// 
/*  80: 87 */     invalidate();					// Rebuilds the backpanel to refresh what is seen on screen
/*  81: 88 */     validate();					// 
/*  82: 89 */     repaint();					// 
/*  83:    */   }
/*  84:    */   
/*  85:    */   private void initMainPanels()
/*  86:    */   {
/*  87: 92 */     this.optionsPanel_ = new JPanel();
/*  88: 93 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth(), 100));
/*  89: 94 */     this.optionsPanel_.setLocation(0, 0);
/*  90: 95 */     this.optionsPanel_.setBackground(Project.standard);
/*  91: 96 */     this.optionsPanel_.setVisible(true);
/*  92: 97 */     this.optionsPanel_.setLayout(null);
/*  93: 98 */     add(this.optionsPanel_, "First");
/*  94:    */     
/*  95:100 */     this.displayP_ = new JPanel();
/*  96:101 */     this.displayP_.setLocation(0, 0);
/*  97:102 */     this.displayP_.setPreferredSize(new Dimension(getWidth() + Project.samples_.size() * 300, 8000));
/*  98:103 */     this.displayP_.setSize(getPreferredSize());
/*  99:104 */     this.displayP_.setBackground(Project.getBackColor_());
/* 100:105 */     this.displayP_.setVisible(true);
/* 101:106 */     this.displayP_.setLayout(null);
/* 102:    */     
/* 103:108 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/* 104:109 */     this.showJPanel_.setVisible(true);
/* 105:110 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/* 106:111 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/* 107:    */     
/* 108:113 */     add("Center", this.showJPanel_);
/* 109:    */   }
/* 110:    */   
/* 111:    */   private void prepPaths()
/* 112:    */   {
/* 113:116 */     this.paths_ = new ArrayList();
/* 114:117 */     for (int pathCnt = 0; pathCnt < this.proc_.getPathwayList_().size(); pathCnt++) {
/* 115:118 */       if (((PathwayWithEc)this.proc_.getPathwayList_().get(pathCnt)).isSelected()) {
/* 116:119 */         this.paths_.add(new PathwayWithEc((Pathway)this.proc_.getPathwayList_().get(pathCnt)));
/* 117:    */       }
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   private void addOptions()
/* 122:    */   {
/* 123:124 */     this.includeWeights = new JCheckBox("Include Weights");
/* 124:125 */     this.includeWeights.setBounds(300, 5, 200, 20);
/* 125:126 */     this.includeWeights.setSelected(false);
/* 126:127 */     this.includeWeights.setBackground(this.optionsPanel_.getBackground());
/* 127:128 */     this.includeWeights.setVisible(true);
/* 128:129 */     this.includeWeights.addActionListener(new ActionListener()
/* 129:    */     {
/* 130:    */       public void actionPerformed(ActionEvent e)
/* 131:    */       {
/* 132:134 */         PathwayActivitymatrixPane.this.setIncludeWeights_(PathwayActivitymatrixPane.this.includeWeights.isSelected());
/* 133:135 */         PathwayActivitymatrixPane.this.changed_ = true;
/* 134:    */       }
/* 135:139 */     });
/* 136:140 */     this.optionsPanel_.add(this.includeWeights);
/* 137:    */     
/* 138:142 */     this.normalizeMax_ = new JCheckBox("Normalize by highest");
/* 139:143 */     this.normalizeMax_.setBounds(300, 25, 200, 20);
/* 140:144 */     this.normalizeMax_.setSelected(false);
/* 141:145 */     this.normalizeMax_.setBackground(this.optionsPanel_.getBackground());
/* 142:146 */     this.normalizeMax_.setVisible(true);
/* 143:147 */     this.normalizeMax_.addActionListener(new ActionListener()
/* 144:    */     {
/* 145:    */       public void actionPerformed(ActionEvent e)
/* 146:    */       {
/* 147:152 */         PathwayActivitymatrixPane.this.changed_ = true;
/* 148:153 */         if (PathwayActivitymatrixPane.this.normalizeSum_.isSelected()) {
/* 149:154 */           PathwayActivitymatrixPane.this.normalizeSum_.setSelected(false);
/* 150:    */         }
/* 151:    */       }
/* 152:159 */     });
/* 153:160 */     this.optionsPanel_.add(this.normalizeMax_);
/* 154:    */     
/* 155:162 */     this.normalizeSum_ = new JCheckBox("Normalize by Col-sum");
/* 156:163 */     this.normalizeSum_.setBounds(500, 25, 200, 20);
/* 157:164 */     this.normalizeSum_.setSelected(false);
/* 158:165 */     this.normalizeSum_.setBackground(this.optionsPanel_.getBackground());
/* 159:166 */     this.normalizeSum_.setVisible(true);
/* 160:167 */     this.normalizeSum_.addActionListener(new ActionListener()
/* 161:    */     {
/* 162:    */       public void actionPerformed(ActionEvent e)
/* 163:    */       {
/* 164:172 */         PathwayActivitymatrixPane.this.changed_ = true;
/* 165:173 */         if (PathwayActivitymatrixPane.this.normalizeMax_.isSelected()) {
/* 166:174 */           PathwayActivitymatrixPane.this.normalizeMax_.setSelected(false);
/* 167:    */         }
/* 168:    */       }
/* 169:180 */     });
/* 170:181 */     this.optionsPanel_.add(this.normalizeSum_);
/* 171:    */     
/* 172:183 */     this.sortByLine_ = new JCheckBox("Sort by Linesum");
/* 173:184 */     this.sortByLine_.setBounds(500, 5, 200, 20);
/* 174:185 */     this.sortByLine_.setSelected(false);
/* 175:186 */     this.sortByLine_.setBackground(this.optionsPanel_.getBackground());
/* 176:187 */     this.sortByLine_.setVisible(true);
/* 177:188 */     this.sortByLine_.addActionListener(new ActionListener()
/* 178:    */     {
/* 179:    */       public void actionPerformed(ActionEvent e)
/* 180:    */       {
/* 181:193 */         PathwayActivitymatrixPane.this.changed_ = true;
/* 182:    */       }
/* 183:197 */     });
/* 184:198 */     this.optionsPanel_.add(this.sortByLine_);
/* 186:201 */     this.useCsf_ = new JCheckBox("CSF");
/* 188:203 */     this.useCsf_.setVisible(true);
/* 189:204 */     this.useCsf_.setLayout(null);
/* 190:205 */     this.useCsf_.setBackground(this.optionsPanel_.getBackground());
/* 191:206 */     this.useCsf_.setForeground(Project.getFontColor_());
/* 192:207 */     this.useCsf_.setBounds(10, 44, 100, 15);
/* 193:208 */     this.optionsPanel_.add(this.useCsf_);

/* 194:210 */     this.export_ = new JButton("Write to file");
/* 197:213 */     this.export_.setBounds(10, 10, 150, 20);
/* 198:214 */     this.export_.setVisible(true);
/* 199:215 */     this.export_.setLayout(null);
/* 200:216 */     this.export_.addActionListener(new ActionListener()
/* 201:    */     {
/* 202:    */       public void actionPerformed(ActionEvent e)
/* 203:    */       {
/* 204:221 */         PathwayActivitymatrixPane.this.exportMat(true, "");
/* 205:    */       }
/* 206:224 */     });
/* 207:225 */     this.optionsPanel_.add(this.export_);
/* 208:    */     
/* 209:227 */     JButton applyOptions = new JButton("Apply");
/* 210:228 */     applyOptions.setBounds(450, 50, 100, 20);
/* 211:229 */     applyOptions.setVisible(true);
/* 212:230 */     applyOptions.setLayout(null);
/* 213:231 */     applyOptions.addActionListener(new ActionListener()
/* 214:    */     {
/* 215:    */       public void actionPerformed(ActionEvent e)
/* 216:    */       {
/* 217:236 */         PathwayActivitymatrixPane.this.showMatrix();
/* 218:237 */         PathwayActivitymatrixPane.this.repaint();
					  PathwayActivitymatrixPane.this.showMatrix();
/* 218:237 */         PathwayActivitymatrixPane.this.repaint();
/* 219:    */       }
/* 220:240 */     });
/* 221:241 */     this.optionsPanel_.add(applyOptions);
					
				  JButton rebuild=new JButton("Rebuild");
				  rebuild.setBounds(450, 75, 100, 20);
				  rebuild.setVisible(true);
/* 212:230 */     rebuild.setLayout(null);
/* 213:231 */     rebuild.addActionListener(new ActionListener()
/* 214:    */     {
/* 215:    */       public void actionPerformed(ActionEvent e)
/* 216:    */       {
					  PathwayActivitymatrixPane.this.changed_=true;
/*  75: 82 */     	  PathwayActivitymatrixPane.this.createMatrix();
/*  76: 83 */     	  PathwayActivitymatrixPane.this.showMatrix();
/*  80: 87 */     	  PathwayActivitymatrixPane.this.invalidate();
/*  81: 88 */     	  PathwayActivitymatrixPane.this.validate();
/*  82: 89 */     	  PathwayActivitymatrixPane.this.repaint();

					  PathwayActivitymatrixPane.this.showMatrix();
/* 218:237 */         PathwayActivitymatrixPane.this.repaint();
					  for(int i=0;i<Project.samples_.size();i++){
					  	Project.samples_.get(i).onoff=true;
					  }
/* 219:    */       }
/* 220:240 */     });
/* 221:241 */     this.optionsPanel_.add(rebuild);

/* 238:258 */     this.mouseOverP = new JPanel();
/* 239:259 */     this.mouseOverP.setBackground(Project.getBackColor_());
/* 240:260 */     this.mouseOverP.setBounds(700, 10, 500, 60);
/* 241:261 */     this.optionsPanel_.add(this.mouseOverP);
/* 242:    */     
/* 243:    */ 
/* 244:264 */     this.mouseOverDisp = new JLabel("Additional Pathway-information");
/* 245:    */     
/* 246:266 */     this.mouseOverDisp.setBounds(0, 0, 500, 60);
/* 247:267 */     this.mouseOverP.add(this.mouseOverDisp);
/* 248:    */   }
/* 249:    */   
/* 250:    */   private void sortByLineSum()
/* 251:    */   {
/* 252:271 */     Loadingframe lframe = new Loadingframe();
/* 253:272 */     lframe.bigStep("Sorting ecs");
				  quicksortByLineSum(0, this.lineMatrix_.size()-1);
				  reverseMatrix();
/* 284:303 */     Loadingframe.close();
/* 285:    */   }
 
				private void quicksortByLineSum(int low, int high)
				{
					int i=low, j=high;
//					if(i>=j){return;}
					double pivot=this.lineMatrix_.get(low+(high-low)/2).sum_;
					while(i<=j){
						while(this.lineMatrix_.get(i).sum_<pivot){
							i++;
						}
						while(this.lineMatrix_.get(j).sum_>pivot){
							j--;
						}
						if(i<=j){
							switchLines(i, j);
							i++;
							j--;
						}
					}
					if(low < j)
						quicksortByLineSum(low, j);
					if(i < high)
						quicksortByLineSum(i, high);
				}
/* 286:    */   
/* 287:    */   private void switchLines(int index1, int index2)
/* 288:    */   {
/* 289:307 */     Line line1 = (Line)this.lineMatrix_.get(index1);
/* 290:308 */     Line line2 = (Line)this.lineMatrix_.get(index2);
/* 294:    */     
/* 295:313 */     this.lineMatrix_.set(index1, line2);
/* 296:314 */     this.lineMatrix_.set(index2, line1);
/* 297:    */   }

				private void reverseMatrix() 
				  {
				  	int j=0;
				    for(int i=lineMatrix_.size()-1;i>j;i--){
				  	  switchLines(i, j);
				  	  j++;
				    }
				  }
/* 298:    */   
/* 299:    */   private void normalizeMax()
/* 300:    */   {
/* 301:318 */     for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
/* 302:319 */       normalizeRowByMaxEntry(smpCnt);
/* 303:    */     }
/* 304:    */   }
/* 305:    */   
/* 306:    */   private void normalizeRowsByAllEntries()
/* 307:    */   {
/* 308:323 */     for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
/* 309:324 */       normalizeRowByAllEntries(smpCnt);
/* 310:    */     }
/* 311:    */   }
/* 312:    */   
/* 313:    */   private void normalizeRowByMaxEntry(int index)
/* 314:    */   {
/* 315:328 */     double highestEntry = 0.0D;
/* 316:329 */     for (int pathCnt = 0; pathCnt < this.numOfpaths; pathCnt++) {
/* 317:330 */       if (highestEntry < ((Line)this.lineMatrix_.get(pathCnt)).getEntry(index)) {
/* 318:331 */         highestEntry = ((Line)this.lineMatrix_.get(pathCnt)).getEntry(index);
/* 319:    */       }
/* 320:    */     }
/* 321:334 */     double newVal = 0.0D;
/* 322:335 */     for (int pathCnt = 0; pathCnt < this.numOfpaths; pathCnt++)
/* 323:    */     {
/* 324:336 */       newVal = ((Line)this.lineMatrix_.get(pathCnt)).getEntry(index) / highestEntry * 100.0D;
/* 325:337 */       ((Line)this.lineMatrix_.get(pathCnt)).setEntry(index, newVal);
/* 326:    */     }
/* 327:    */   }
/* 328:    */   
/* 329:    */   private void normalizeRowByAllEntries(int index)
/* 330:    */   {
/* 331:341 */     double sum = 0.0D;
/* 332:342 */     for (int pathCnt = 0; pathCnt < this.numOfpaths; pathCnt++) {
/* 333:343 */       sum += ((Line)this.lineMatrix_.get(pathCnt)).getEntry(index);
/* 334:    */     }
/* 335:345 */     double newVal = 0.0D;
/* 336:346 */     for (int pathCnt = 0; pathCnt < this.numOfpaths; pathCnt++)
/* 337:    */     {
/* 338:347 */       newVal = ((Line)this.lineMatrix_.get(pathCnt)).getEntry(index) / sum * 100.0D;
/* 339:348 */       ((Line)this.lineMatrix_.get(pathCnt)).setEntry(index, newVal);
/* 340:    */     }
/* 341:    */   }
/* 342:    */   
/* 343:    */   private void createMatrix()
/* 344:    */   {
/* 345:353 */     this.numOfpaths = 0;
/* 346:354 */     this.lineMatrix_ = new ArrayList();
/* 347:355 */     for (int j = 0; j < this.proc_.getPathwayList_().size(); j++) 
				  {
/* 348:356 */       if (((PathwayWithEc)this.proc_.getPathwayList_().get(j)).isSelected()) {
/* 349:357 */         this.numOfpaths += 1;
/* 350:    */       }
/* 351:    */     }
/* 352:361 */     this.numOfSamples = Project.samples_.size();
/* 353:    */     
/* 354:363 */     int sampleNr = Project.samples_.size();
/* 355:    */     
/* 356:365 */     double[] tmpLine = new double[sampleNr];
/* 357:366 */     for (int pathCnt = 0; pathCnt < this.paths_.size(); pathCnt++)
/* 358:    */     {
/* 359:367 */       tmpLine = new double[sampleNr];
					int xREAL=0;
/* 360:368 */       if (this.proc_.getPathway(((PathwayWithEc)this.paths_.get(pathCnt)).id_).isSelected())
/* 361:    */       {
/* 362:371 */         for (int sampleCnt = 0; sampleCnt < sampleNr; sampleCnt++)
/* 363:    */         {
						if(Project.samples_.get(sampleCnt).onoff){
/* 364:375 */           	PathwayWithEc tmpPath = (PathwayWithEc)((Sample)Project.samples_.get(sampleCnt)).pathways_.get(pathCnt);
/* 365:376 */           	double activity = 0.0D;
/* 366:    */           	double tmpScore;
//* 367:    */          	 double tmpScore;
/* 368:377 */           	if (this.includeWeights_)
/* 369:    */           	{
/* 370:379 */           	  for (int ecCnt = 0; ecCnt < tmpPath.ecNrs_.size(); ecCnt++) {
/* 371:380 */           	    activity += ((EcNr)tmpPath.ecNrs_.get(ecCnt)).amount_ * ((EcNr)tmpPath.ecNrs_.get(ecCnt)).weight_;
/* 372:    */           	  }
/* 373:382 */           	  tmpScore = activity;
/* 374:    */           	}
/* 375:    */           	else
/* 376:    */           	{
/* 377:385 */           	  for (int ecCnt = 0; ecCnt < tmpPath.ecNrs_.size(); ecCnt++) {
/* 378:386 */           	    activity += ((EcNr)tmpPath.ecNrs_.get(ecCnt)).amount_;
/* 379:    */           	  }
/* 380:388 */           	  tmpScore = activity;
/* 381:    */           	}
/* 382:390 */           	tmpLine[sampleCnt] = tmpScore;
							xREAL++;
						}
/* 383:    */         }
/* 384:392 */         this.lineMatrix_.add(new Line((PathwayWithEc)this.paths_.get(pathCnt), tmpLine));
/* 385:    */       }
/* 386:    */     }
/* 387:394 */     System.out.println("endOfcreate");
/* 388:395 */     this.changed_ = false;
/* 389:    */   }
/* 390:    */   
/* 391:    */   public void showMatrix()
/* 392:    */   {
/* 393:399 */     if (this.changed_) {
/* 394:400 */       createMatrix();
/* 395:    */     }
/* 396:402 */     if (this.normalizeMax_.isSelected()) {
/* 397:403 */       normalizeMax();
/* 398:    */     }
/* 399:405 */     if (this.normalizeSum_.isSelected()) {
/* 400:406 */       normalizeRowsByAllEntries();
/* 401:    */     }
/* 402:408 */     if (this.sortByLine_.isSelected()) {
/* 403:409 */       sortByLineSum();
/* 404:    */     }
/* 405:414 */     Loadingframe lframe = new Loadingframe();
/* 406:415 */     lframe.bigStep("repainting");
/* 407:416 */     lframe.step("removeAll");
/* 408:417 */     this.displayP_.removeAll();
/* 409:    */     
/* 410:419 */     int col = 1;
/* 411:420 */     int coldist = 40;
/* 412:421 */     int xDist = 60;
/* 413:422 */     int namesWidth = 275;
/* 414:    */     
/* 415:424 */     int scoreWidth = 150;
/* 416:425 */     int scoreLength = 6;
/* 417:    */     
/* 418:    */ 
/* 419:    */ 
/* 420:    */ 
/* 421:    */ 
/* 422:    */ 
/* 423:432 */     int counter = 0;
/* 424:433 */     for (int i = 0; i < this.lineMatrix_.size(); i++) {
/* 425:434 */       if (((Line)this.lineMatrix_.get(i)).getPath_().isSelected())
/* 426:    */       {
/* 427:438 */         PathwayWithEc tmpPath = Project.overall_.getPath(((Line)this.lineMatrix_.get(i)).getPath_().id_);
/* 428:439 */         lframe.step(tmpPath.id_);
/* 429:440 */         PathButt pathNames = new PathButt(Project.samples_, Project.overall_, tmpPath, Project.getBackColor_(), Project.workpath_, 1);
/* 430:441 */         pathNames.setBounds(xDist - 5, col * coldist + coldist * counter, namesWidth, coldist);
/* 431:    */         
/* 432:443 */         final PathwayWithEc fpath = new PathwayWithEc(tmpPath, false);
/* 433:444 */         pathNames.addMouseListener(new MouseListener()
/* 434:    */         {
/* 435:    */           public void mouseReleased(MouseEvent e) {}
/* 436:    */           
/* 437:    */           public void mousePressed(MouseEvent e) {}
/* 438:    */           
/* 439:    */           public void mouseExited(MouseEvent e)
/* 440:    */           {
/* 441:461 */             PathwayActivitymatrixPane.this.mouseOverDisp.setVisible(false);
/* 442:    */             
/* 443:463 */             PathwayActivitymatrixPane.this.mouseOverDisp.setText("Additional Pathway-information");
/* 444:    */           }
/* 445:    */           
/* 446:    */           public void mouseEntered(MouseEvent e)
/* 447:    */           {
/* 448:470 */             PathwayActivitymatrixPane.this.mouseOverDisp.setVisible(true);
/* 449:    */             
/* 450:472 */             PathwayActivitymatrixPane.this.setAdditionalInfo(fpath);
/* 451:    */           }
/* 452:    */           
/* 453:    */           public void mouseClicked(MouseEvent e) {}
/* 454:481 */         });
/* 455:482 */         pathNames.addMouseListener(new MouseListener()
/* 456:    */         {
/* 457:    */           public void mouseReleased(MouseEvent e) {}
/* 458:    */           
/* 459:    */           public void mousePressed(MouseEvent e) {}
/* 460:    */           
/* 461:    */           public void mouseExited(MouseEvent e)
/* 462:    */           {
/* 463:499 */             if (PathwayActivitymatrixPane.this.infoFrame != null) {
/* 464:500 */               PathwayActivitymatrixPane.this.infoFrame.setText("Additional Pathway-information");
/* 465:    */             }
/* 466:502 */             PathwayActivitymatrixPane.this.repaint();
/* 467:    */           }
/* 468:    */           
/* 469:    */           public void mouseEntered(MouseEvent e)
/* 470:    */           {
/* 471:508 */             PathwayActivitymatrixPane.this.setAdditionalInfo(fpath);
/* 472:509 */             PathwayActivitymatrixPane.this.repaint();
/* 473:    */           }
/* 474:    */           
/* 475:    */           public void mouseClicked(MouseEvent e) {}
/* 476:517 */         });
/* 477:518 */         this.displayP_.add(pathNames);
/* 478:519 */         counter++;
/* 479:    */       }
/* 480:    */     }
/* 481:522 */     int sampleNr = Project.samples_.size();
/* 482:523 */     int pathNr = this.lineMatrix_.size();
				  int xREAL=0;
/* 483:528 */     for (int sampleCnt = 0; sampleCnt < sampleNr; sampleCnt++)
/* 484:    */     {
					if(Project.samples_.get(sampleCnt).onoff){
/* 485:530 */       	final JLabel fullName = new JLabel(((Sample)Project.samples_.get(sampleCnt)).name_);
/* 486:531 */       	int x = xDist + namesWidth + scoreWidth * (xREAL + 1);
/* 487:532 */       	fullName.setBounds(x, (col - 1) * coldist - 5, 500, coldist);
/* 488:533 */       	fullName.setForeground(((Sample)Project.samples_.get(sampleCnt)).sampleCol_);
/* 489:534 */       	fullName.setVisible(false);
/* 490:535 */       	this.displayP_.add(fullName);
/* 491:    */       
/* 492:537 */       	final JLabel smpName = new JLabel(((Sample)Project.samples_.get(sampleCnt)).name_);
/* 493:538 */       	smpName.setBounds(0, 0, scoreWidth, 20);
/* 494:539 */       	smpName.setForeground(((Sample)Project.samples_.get(sampleCnt)).sampleCol_);
/* 495:540 */       	smpName.setVisible(true);
					
						final int smpCnt=sampleCnt;
/* 496:    */       	
/* 497:542 */       	JPanel mouseOver = new JPanel();
/* 498:543 */       	mouseOver.setBounds(x, (col - 1) * coldist - 5 + 25, scoreWidth, 20);
/* 499:544 */       	mouseOver.setBackground(Project.getBackColor_());
/* 500:545 */       	mouseOver.setLayout(null);
/* 501:546 */       	mouseOver.addMouseListener(new MouseListener()
/* 502:    */       	{
/* 503:    */       	  public void mouseReleased(MouseEvent e) {}
/* 504:    */       	  
/* 505:    */       	  public void mousePressed(MouseEvent e) {}
/* 506:    */       	  
/* 507:    */       	  public void mouseExited(MouseEvent e)
/* 508:    */       	  {
/* 509:563 */       	    fullName.setVisible(false);
/* 510:564 */       	    smpName.setVisible(true);
/* 511:    */       	  }
/* 512:    */       	  
/* 513:    */       	  public void mouseEntered(MouseEvent e)
/* 514:    */       	  {
/* 515:570 */       	    fullName.setVisible(true);
/* 516:571 */       	    smpName.setVisible(false);
/* 517:    */       	  }
/* 518:    */       	  
/* 519:    */       	  public void mouseClicked(MouseEvent e) {
							Project.samples_.get(smpCnt).onoff=false;
						  }
/* 520:579 */       	});
/* 521:580 */       	this.displayP_.add(mouseOver);
/* 522:581 */       	mouseOver.add(smpName);
/* 523:    */       
/* 524:    */ 
/* 525:    */ 
/* 526:585 */       	counter = 0;
/* 527:586 */       	for (int pathCnt = 0; pathCnt < pathNr; pathCnt++) {
/* 528:587 */       	  if (((PathwayWithEc)((Sample)Project.samples_.get(sampleCnt)).pathways_.get(pathCnt)).isSelected())
/* 529:    */       	  {
/* 530:592 */       	    String tmpString = String.valueOf(((Line)this.lineMatrix_.get(pathCnt)).getEntry(sampleCnt));
/* 531:594 */       	    if (tmpString.length() > scoreLength)
/* 532:    */       	    {
/* 533:595 */       	      tmpString = tmpString.substring(0, scoreLength);
/* 534:596 */       	      lframe.step(tmpString);
/* 535:    */       	    }
/* 536:598 */       	    JLabel scores = new JLabel(tmpString);
/* 537:599 */       	    scores.setForeground(((Sample)Project.samples_.get(sampleCnt)).sampleCol_);
/* 538:600 */       	    x = xDist + namesWidth + scoreWidth * (xREAL + 1);
/* 539:601 */       	    int y = col * coldist + coldist * counter;
/* 540:602 */       	    scores.setBounds(x, y, scoreWidth, coldist);
/* 541:603 */       	    this.displayP_.add(scores);
/* 542:604 */       	    counter++;
/* 543:    */       	  }
/* 544:    */       	}
						xREAL++;
					}
/* 545:    */     }
/* 546:607 */     for (int pathCnt = 0; pathCnt < pathNr; pathCnt++)
/* 547:    */     {
/* 548:608 */       if (((Line)this.lineMatrix_.get(pathCnt)).sum_ == 0) {
/* 549:609 */         ((Line)this.lineMatrix_.get(pathCnt)).setSum();
/* 550:    */       }
/* 551:611 */       String tmpString = String.valueOf(((Line)this.lineMatrix_.get(pathCnt)).getSum_());
/* 552:613 */       if (tmpString.length() > scoreLength)
/* 553:    */       {
/* 554:614 */         tmpString = tmpString.substring(0, scoreLength);
/* 555:615 */         lframe.step(tmpString);
/* 556:    */       }
/* 557:617 */       JLabel scores = new JLabel(tmpString);
/* 558:618 */       scores.setForeground(Color.black);
/* 559:619 */       int x = xDist + namesWidth + scoreWidth * (xREAL + 1);
/* 560:620 */       int y = col * coldist + coldist * pathCnt;
/* 561:621 */       scores.setBounds(x, y, scoreWidth, coldist);
/* 562:622 */       this.displayP_.add(scores);
/* 563:    */     }
/* 564:624 */     invalidate();
/* 565:625 */     validate();
/* 566:626 */     repaint();
/* 567:627 */     Loadingframe.close();
/* 568:    */   }
/* 569:    */   
/* 570:    */   public void exportMat(boolean dialog, String path)
/* 571:    */   {
/* 572:630 */     this.exp_ = new Exporter(this);
/* 573:631 */     double[][] matrix = new double[this.numOfSamples][this.numOfpaths];
/* 574:    */     
/* 575:633 */     ArrayList<String> yDesc = new ArrayList();
/* 576:634 */     for (int pathCnt = 0; pathCnt < this.lineMatrix_.size(); pathCnt++) {
/* 577:635 */       yDesc.add(((Line)this.lineMatrix_.get(pathCnt)).getPath_().id_ + " / " + ((Line)this.lineMatrix_.get(pathCnt)).getPath_().name_);
/* 578:    */     }
/* 579:637 */     ArrayList<String> xDesc = this.exp_.getNameListFromSample(Project.samples_);
/* 580:639 */     for (int pathcnt = 0; pathcnt < this.lineMatrix_.size(); pathcnt++) {
/* 581:640 */       for (int sampleCnt = 0; sampleCnt < this.numOfSamples; sampleCnt++) {
/* 582:641 */         matrix[sampleCnt][pathcnt] = ((Line)this.lineMatrix_.get(pathcnt)).getEntry(sampleCnt);
/* 583:    */       }
/* 584:    */     }
/* 585:644 */     if (dialog)
/* 586:    */     {
/* 587:645 */       if (this.exp_.saveDialog()) {
/* 588:646 */         this.exp_.exportDoubleMatrix(xDesc, yDesc, matrix, this.useCsf_.isSelected());
/* 589:    */       }
/* 590:    */     }
/* 591:    */     else
/* 592:    */     {
/* 593:650 */       File f = new File(path);
/* 594:651 */       f.mkdirs();
/* 595:652 */       if (!path.endsWith(".txt"))
/* 596:    */       {
/* 597:653 */         Calendar cal = Calendar.getInstance();
/* 598:    */         
/* 599:655 */         int day = cal.get(5);
/* 600:656 */         int month = cal.get(2) + 1;
/* 601:657 */         int year = cal.get(1);
/* 602:    */         
/* 603:    */ 		
/* 604:    */ 
/* 605:661 */         String date = day + "." + month + "." + year;
/* 606:662 */         path = path + File.separator + "pwActivity" +"."+Project.workpath_+"."+ date;
/* 607:    */       }
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
/* 608:664 */       this.exp_.path_ = path;
/* 609:665 */       this.exp_.exportDoubleMatrix(xDesc, yDesc, matrix, this.useCsf_.isSelected());
/* 610:    */     }
/* 611:    */   }
/* 612:    */   
/* 613:    */   private void setAdditionalInfo(PathwayWithEc path)
/* 614:    */   {
/* 615:669 */     this.mouseOverDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_ + "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_ + "</html>");
/* 616:671 */     if (this.infoFrame != null) {
/* 617:672 */       this.infoFrame.setAdditionalInfo(path);
/* 618:    */     }
/* 619:    */   }
/* 620:    */   
/* 621:    */   public boolean isIncludeWeights_()
/* 622:    */   {
/* 623:676 */     return this.includeWeights_;
/* 624:    */   }
/* 625:    */   
/* 626:    */   public void setIncludeWeights_(boolean includeWeights_)
/* 627:    */   {
/* 628:681 */     this.includeWeights_ = includeWeights_;
/* 629:    */   }
/* 630:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.PathwayActivitymatrixPane

 * JD-Core Version:    0.7.0.1

 */