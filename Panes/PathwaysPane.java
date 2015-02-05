/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.EcWithPathway;
/*   4:    */ import Objects.Pathway;
/*   5:    */ import Objects.PathwayWithEc;
/*   6:    */ import Objects.Project;
/*   7:    */ import Objects.Sample;
/*   8:    */ import Prog.DataProcessor;
/*   9:    */ import java.awt.BorderLayout;
/*  10:    */ import java.awt.Color;
/*  11:    */ import java.awt.Dimension;
/*  12:    */ import java.awt.event.ActionEvent;
/*  13:    */ import java.awt.event.ActionListener;
/*  14:    */ import java.io.PrintStream;
/*  15:    */ import java.util.ArrayList;
/*  16:    */ import javax.swing.JButton;
/*  17:    */ import javax.swing.JCheckBox;
/*  18:    */ import javax.swing.JLabel;
/*  19:    */ import javax.swing.JPanel;
/*  20:    */ import javax.swing.JTextField;
/*  21:    */ 
/*  22:    */ public class PathwaysPane
/*  23:    */   extends JPanel
/*  24:    */ {
/*  25:    */   private static final long serialVersionUID = 1L;
/*  26:    */   SampleScorePane ssp;
/*  27:    */   PathwayMatrix pwMat;
/*  28:    */   Color buttonDefault_;
/*  29:    */   Project activeProj_;
/*  30:    */   ArrayList<PathwayWithEc> pwList_;
/*  31:    */   ArrayList<EcWithPathway> ecList_;
/*  32:    */   DataProcessor proc_;
/*  33:    */   JPanel showPanel_;
/*  34:    */   int yOffset_;
/*  35: 42 */   int minVisScore_ = 0;
/*  36: 43 */   int xCol2 = 300;
/*  37: 44 */   int xCol3 = 500;
/*  38: 45 */   int mode = 0;
/*  39: 47 */   int yLine1 = 30;
/*  40:    */   JPanel optionsPanel_;
/*  41:    */   JButton pwScores_;
/*  42:    */   JButton pwMatrix_;
/*  43:    */   JButton actMatrix_;
/*  44:    */   JButton pwPlot_;
/*  45:    */   JTextField maxVisfield;
/*  46:    */   JCheckBox chainCheck_;
/*  47:    */   JCheckBox chainCheck2_;
/*  48:    */   JCheckBox listCheck_;
/*  49: 60 */   int activeChainingMode = 0;
/*  50:    */   int xwidth;
				boolean checked;
/*  51:    */   public JButton backButton_;
/*  52:    */   
/*  53:    */   public PathwaysPane(Project activeProj, DataProcessor proc, Dimension dim)
/*  54:    */   {
/*  55: 66 */     this.activeProj_ = activeProj;
/*  56: 67 */     this.pwList_ = proc.getPathwayList_();
/*  57: 68 */     this.ecList_ = DataProcessor.ecList_;
/*  58: 69 */     this.proc_ = proc;
/*  59: 70 */     this.minVisScore_ = Project.minVisScore_;
/*  60: 71 */     this.xwidth = (4000 + Project.samples_.size() * 300);
/*  61: 72 */     setSize(dim);
/*  62: 73 */     setBackground(Project.getBackColor_().darker());
/*  63: 74 */     setLayout(new BorderLayout());
/*  64: 75 */     setVisible(true);

				  checked=false;
/*  65:    */     
/*  66: 77 */     this.backButton_ = new JButton("< Back to the analysisoptions");
/*  67: 78 */     initMainPanels();
/*  68: 79 */     initChainChecks();
/*  69: 80 */     addOptions();
/*  70: 81 */     showScores();
/*  71:    */   }
/*  72:    */   
/*  73:    */   private void initMainPanels()
/*  74:    */   {
/*  75: 84 */     this.optionsPanel_ = new JPanel();
/*  76: 85 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth() - 50, 100));
/*  77: 86 */     this.optionsPanel_.setBackground(Project.getBackColor_().darker());
/*  78: 87 */     this.optionsPanel_.setBackground(Project.getBackColor_());
/*  79: 88 */     this.optionsPanel_.setVisible(true);
/*  80: 89 */     this.optionsPanel_.setLayout(null);
/*  81: 90 */     add(this.optionsPanel_, "First");
/*  82:    */     
/*  83: 92 */     this.showPanel_ = new JPanel();
/*  84: 93 */     this.showPanel_.setPreferredSize(new Dimension(getWidth() - 70, getHeight() - 150));
/*  85: 94 */     this.showPanel_.setBackground(Project.getBackColor_().brighter());
/*  86: 95 */     this.showPanel_.setLayout(new BorderLayout());
/*  87: 96 */     this.showPanel_.setVisible(true);
/*  88: 97 */     add(this.showPanel_, "Center");
/*  89:    */   }
/*  90:    */   
/*  91:    */   private void addOptions()
/*  92:    */   {
/*  93:100 */     this.optionsPanel_.removeAll();
/*  94:101 */     addButtons();
/*  95:102 */     showMaxVis();
/*  96:103 */     showListMode();
/*  97:104 */     addChaincheck();
/*  98:105 */     invalidate();
/*  99:106 */     validate();
/* 100:107 */     repaint();
/* 101:    */   }
/* 102:    */   
/* 103:    */   private void initChainChecks()
/* 104:    */   {
/* 105:110 */     JLabel chain = new JLabel("Use chaining mode 1");
/* 106:111 */     if (this.chainCheck_ == null)
/* 107:    */     {
/* 108:112 */       this.chainCheck_ = new JCheckBox();
/* 109:113 */       this.chainCheck_.setSelected(false);
/* 110:    */     }
/* 111:115 */     this.chainCheck_.setBackground(this.optionsPanel_.getBackground());
/* 112:116 */     chain.setBounds(this.xCol3 + 20, this.yLine1, 230, 20);
/* 113:117 */     this.optionsPanel_.add(chain);
/* 114:    */     
/* 115:119 */     this.chainCheck_.setBounds(this.xCol3, this.yLine1, 20, 20);
/* 116:120 */     this.chainCheck_.addActionListener(new ActionListener()
/* 117:    */     {
/* 118:    */       public void actionPerformed(ActionEvent e)
/* 119:    */       {
/* 120:125 */         Project.chaining = PathwaysPane.this.chainCheck_.isSelected();
/* 121:126 */         if (PathwaysPane.this.chainCheck2_.isSelected()) {
/* 122:127 */           PathwaysPane.this.chainCheck2_.setSelected(false);
/* 123:    */         } else {
/* 124:131 */           PathwaysPane.this.activeChainingMode = 0;
/* 125:    */         }
/* 126:133 */         if (PathwaysPane.this.chainCheck_.isSelected()) {
/* 127:134 */           PathwaysPane.this.activeChainingMode = 1;
/* 128:    */         }
/* 129:136 */         PathwaysPane.this.proc_.getAllscores(PathwaysPane.this.activeChainingMode);
/* 130:137 */         PathwaysPane.this.redo();
/* 131:    */       }
/* 132:140 */     });
/* 133:141 */     this.optionsPanel_.add(this.chainCheck_);
/* 134:    */     
/* 135:143 */     JLabel chain2 = new JLabel("Use chaining mode 2");
/* 136:144 */     if (this.chainCheck2_ == null)
/* 137:    */     {
/* 138:145 */       this.chainCheck2_ = new JCheckBox();
/* 139:    */       
/* 140:147 */       this.chainCheck2_.setSelected(false);
/* 141:    */     }
/* 142:150 */     this.chainCheck2_.setSelected(false);
/* 143:151 */     chain2.setBounds(this.xCol3 + 220, this.yLine1, 230, 20);
/* 144:152 */     this.optionsPanel_.add(chain2);
/* 145:153 */     this.chainCheck2_.setBounds(this.xCol3 + 200, this.yLine1, 20, 20);
/* 146:154 */     this.chainCheck2_.setBackground(this.optionsPanel_.getBackground());
/* 147:155 */     this.chainCheck2_.addActionListener(new ActionListener()
/* 148:    */     {
/* 149:    */       public void actionPerformed(ActionEvent e)
/* 150:    */       {
/* 151:160 */         Project.chaining = PathwaysPane.this.chainCheck2_.isSelected();
/* 152:161 */         if (PathwaysPane.this.chainCheck_.isSelected()) {
/* 153:162 */           PathwaysPane.this.chainCheck_.setSelected(false);
/* 154:    */         } else {
/* 155:166 */           PathwaysPane.this.activeChainingMode = 0;
/* 156:    */         }
/* 157:168 */         if (PathwaysPane.this.chainCheck2_.isSelected()) {
/* 158:169 */           PathwaysPane.this.activeChainingMode = 2;
/* 159:    */         }
/* 160:171 */         PathwaysPane.this.proc_.getAllscores(PathwaysPane.this.activeChainingMode);
/* 161:172 */         PathwaysPane.this.redo();
/* 162:    */       }
/* 163:176 */     });
/* 164:177 */     this.optionsPanel_.add(this.chainCheck2_);
/* 165:    */   }
/* 166:    */   
/* 167:    */   private void addChaincheck()
/* 168:    */   {
/* 169:180 */     JLabel chain = new JLabel("Use chaining mode 1");
/* 170:181 */     chain.setBounds(this.xCol3 + 20, this.yLine1, 230, 20);
/* 171:182 */     this.optionsPanel_.add(chain);
/* 172:183 */     this.optionsPanel_.add(this.chainCheck_);
/* 173:    */     
/* 174:185 */     JLabel chain2 = new JLabel("Use chaining mode 2");
/* 175:186 */     chain2.setBounds(this.xCol3 + 220, this.yLine1, 230, 20);
/* 176:187 */     this.optionsPanel_.add(chain2);
/* 177:188 */     this.optionsPanel_.add(this.chainCheck2_);
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void showListMode()
/* 181:    */   {
/* 182:191 */     JLabel listl_ = new JLabel("Sort by Score:");
/* 183:    */     
/* 184:193 */     this.listCheck_ = new JCheckBox();
/* 185:    */     
/* 186:    */ 
/* 187:196 */     listl_.setBounds(this.xCol2 + 20, this.yLine1, 200, 20);
/* 188:197 */     this.optionsPanel_.add(listl_);
/* 189:198 */     this.listCheck_.setBounds(this.xCol2, this.yLine1, 20, 20);
//* 190:199 */     this.listCheck_.setBackground(this.optionsPanel_.getBackground());
//* 191:200 */     if (this.activeProj_ != null) {
//* 192:201 */       this.listCheck_.setSelected(Project.listMode_);
//* 193:    */     }
				  listCheck_.setSelected(checked);
/* 194:203 */     this.listCheck_.addActionListener(new ActionListener()
/* 195:    */     {
/* 196:    */       public void actionPerformed(ActionEvent e)
/* 197:    */       {
//* 198:208 */         Project.listMode_ = PathwaysPane.this.listCheck_.isSelected();
					   
					   if(checked){checked=false;}
					   else{checked=true;}
					  
/* 199:209 */         for (int i = 0; i < Project.samples_.size(); i++) {
/* 200:210 */           if (PathwaysPane.this.listCheck_.isSelected()) {
/* 201:212 */             PathwaysPane.this.sortPathwaysByScore(((Sample)Project.samples_.get(i)).pathways_);
/* 202:    */           } else {
/* 203:215 */             PathwaysPane.this.sortPathsById(((Sample)Project.samples_.get(i)).pathways_);
/* 204:    */           }
/* 205:    */         }
/* 206:218 */         if (PathwaysPane.this.listCheck_.isSelected()) {
/* 207:220 */           PathwaysPane.this.sortPathwaysByScore(Project.overall_.pathways_);
/* 208:    */         } else {
/* 209:224 */           PathwaysPane.this.sortPathsById(Project.overall_.pathways_);
/* 210:    */         }
					  
/* 211:227 */         PathwaysPane.this.redo();
/* 212:    */       }
/* 213:231 */     });
/* 214:232 */     this.optionsPanel_.add(this.listCheck_);
/* 215:    */   }
/* 216:    */   
/* 217:    */   private void showMaxVis()
/* 218:    */   {
/* 219:235 */     JLabel maxvisl_ = new JLabel("Min. shown score:");
/* 220:236 */     if (this.maxVisfield == null) {
/* 221:237 */       this.maxVisfield = new JTextField(this.minVisScore_);
/* 222:    */     }
/* 223:239 */     maxvisl_.setBounds(10, 10, 200, 20);
/* 224:240 */     this.optionsPanel_.add(maxvisl_);
/* 225:241 */     this.maxVisfield.setBounds(10, 30, 200, 20);
/* 226:242 */     this.maxVisfield.addActionListener(new ActionListener()
/* 227:    */     {
/* 228:    */       public void actionPerformed(ActionEvent e)
/* 229:    */       {
/* 230:247 */         PathwaysPane.this.getMaxVis();
/* 231:248 */         PathwaysPane.this.redo();
/* 232:    */       }
/* 233:251 */     });
/* 234:252 */     this.optionsPanel_.add(this.maxVisfield);
/* 235:    */   }
/* 236:    */   
/* 237:    */   private void redo()
/* 238:    */   {
/* 239:255 */     this.optionsPanel_.removeAll();
/* 240:256 */     addOptions();
/* 241:257 */     this.showPanel_.removeAll();
/* 242:258 */     System.out.println("redo");
/* 243:259 */     switch (this.mode)
/* 244:    */     {
/* 245:    */     case 0: 
/* 246:261 */       showScores();
/* 247:262 */       break;
/* 248:    */     case 1: 
/* 249:264 */       showMatrix();
/* 250:265 */       break;
/* 251:    */     case 3: 
/* 252:270 */       showPlot();
/* 253:    */     }
/* 254:273 */     invalidate();
/* 255:274 */     validate();
/* 256:275 */     repaint();
/* 257:    */   }
/* 258:    */   
/* 259:    */   private void getMaxVis()
/* 260:    */   {
/* 261:279 */     if (this.maxVisfield != null)
/* 262:    */     {
/* 263:281 */       int tmp = convertStringtoInt(this.maxVisfield.getText());
/* 264:282 */       if (this.minVisScore_ != tmp) {
/* 265:283 */         this.minVisScore_ = tmp;
/* 266:    */       }
/* 267:    */     }
/* 268:287 */     Project.minVisScore_ = this.minVisScore_;
/* 269:    */   }
/* 270:    */   
/* 271:    */   private void addButtons()
/* 272:    */   {
/* 273:293 */     this.pwScores_ = new JButton("Show pathway-scores");
/* 274:294 */     this.pwScores_.setBounds(0, 80, 200, 20);
/* 275:295 */     this.pwScores_.setVisible(true);
/* 276:296 */     if (this.mode == 0) {
/* 277:297 */       this.pwScores_.setBackground(Project.standard);
/* 278:    */     }
/* 279:302 */     this.pwScores_.addActionListener(new ActionListener()
/* 280:    */     {
/* 281:    */       public void actionPerformed(ActionEvent e)
/* 282:    */       {
/* 283:307 */         PathwaysPane.this.showScores();
/* 284:308 */         PathwaysPane.this.mode = 0;
/* 285:309 */         PathwaysPane.this.changeMode();
/* 286:    */       }
/* 287:312 */     });
/* 288:313 */     this.optionsPanel_.add(this.pwScores_);
/* 289:314 */     this.buttonDefault_ = this.optionsPanel_.getBackground();
/* 290:    */     
/* 291:316 */     this.pwMatrix_ = new JButton("Show score-matrix");
/* 292:317 */     this.pwMatrix_.setBounds(201, 80, 200, 20);
/* 293:318 */     this.pwMatrix_.setVisible(true);
/* 294:319 */     if (this.mode == 1) {
/* 295:320 */       this.pwMatrix_.setBackground(Project.standard);
/* 296:    */     }
/* 297:325 */     this.pwMatrix_.addActionListener(new ActionListener()
/* 298:    */     {
/* 299:    */       public void actionPerformed(ActionEvent e)
/* 300:    */       {
/* 301:330 */         PathwaysPane.this.showMatrix();
/* 302:331 */         PathwaysPane.this.mode = 1;
/* 303:332 */         PathwaysPane.this.changeMode();
/* 304:    */       }
/* 305:335 */     });
/* 306:336 */     this.optionsPanel_.add(this.pwMatrix_);
/* 307:    */     
/* 308:338 */     this.pwPlot_ = new JButton("Show score-plot");
/* 309:339 */     this.pwPlot_.setBounds(402, 80, 200, 20);
/* 310:340 */     this.pwPlot_.setVisible(true);
/* 311:341 */     if (this.mode == 3) {
/* 312:342 */       this.pwPlot_.setBackground(Project.standard);
/* 313:    */     }
/* 314:347 */     this.pwPlot_.addActionListener(new ActionListener()
/* 315:    */     {
/* 316:    */       public void actionPerformed(ActionEvent e)
/* 317:    */       {
/* 318:352 */         PathwaysPane.this.showPlot();
/* 319:353 */         PathwaysPane.this.mode = 3;
/* 320:354 */         PathwaysPane.this.changeMode();
/* 321:    */       }
/* 322:357 */     });
/* 323:358 */     this.optionsPanel_.add(this.pwPlot_);
/* 324:    */     
/* 325:    */ 
/* 326:361 */     this.backButton_.setBounds(10, 52, 200, 25);
/* 327:362 */     this.optionsPanel_.add(this.backButton_);
/* 328:    */   }
/* 329:    */   
/* 330:    */   private void showScores()
/* 331:    */   {
/* 332:366 */     clear();
/* 333:367 */     this.ssp = new SampleScorePane(Project.overall_, this.activeProj_, this.proc_, new Dimension(getWidth(), getHeight()));
/* 334:368 */     this.showPanel_.add("Center", this.ssp);
/* 335:369 */     invalidate();
/* 336:370 */     validate();
/* 337:371 */     repaint();
/* 338:    */   }
/* 339:    */   
/* 340:    */   private void showMatrix()
/* 341:    */   {
/* 342:375 */     clear();
/* 343:    */     
/* 344:377 */     this.pwMat = new PathwayMatrix(Project.samples_, Project.overall_, this.pwList_, this.activeProj_);
/* 345:    */     
/* 346:379 */     this.showPanel_.add("Center", this.pwMat);
/* 347:380 */     invalidate();
/* 348:381 */     validate();
/* 349:382 */     repaint();
/* 350:    */   }
/* 351:    */   
/* 352:    */   private void showPlot()
/* 353:    */   {
/* 354:385 */     clear();
/* 355:    */     
/* 356:387 */     PathwayPlot pwPlot = new PathwayPlot(this.activeProj_, this.proc_);
/* 357:    */     
/* 358:389 */     this.showPanel_.add("Center", pwPlot);
/* 359:390 */     invalidate();
/* 360:391 */     validate();
/* 361:392 */     repaint();
/* 362:    */   }
/* 363:    */   
/* 364:    */   private void clear()
/* 365:    */   {
/* 366:396 */     this.showPanel_.removeAll();
/* 367:    */   }
/* 368:    */   
/* 369:    */   private void sortPathwaysByScore(ArrayList<PathwayWithEc> pathways)
/* 370:    */   {
/* 371:399 */     int tmpCnt = 0;
/* 372:400 */     for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++)
/* 373:    */     {
/* 374:401 */       tmpCnt = pathCnt;
/* 375:402 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
/* 376:403 */         if (((PathwayWithEc)pathways.get(tmpCnt)).score_ < ((PathwayWithEc)pathways.get(pathCnt2)).score_) {
/* 377:404 */           tmpCnt = pathCnt2;
/* 378:    */         }
/* 379:    */       }
/* 380:407 */       pathways.add(pathCnt, (PathwayWithEc)pathways.get(tmpCnt));
/* 381:408 */       pathways.remove(tmpCnt + 1);
/* 382:    */     }
/* 383:    */   }
/* 384:    */   
/* 385:    */   public int getyOffset_()
/* 386:    */   {
/* 387:413 */     return this.yOffset_;
/* 388:    */   }
/* 389:    */   
/* 390:    */   public void setyOffset_(int yOffset_)
/* 391:    */   {
/* 392:416 */     this.yOffset_ = yOffset_;
/* 393:    */   }
/* 394:    */   
/* 395:    */   private void sortPathsById(ArrayList<PathwayWithEc> pathways)
/* 396:    */   {
		//		  Loadingframe lframe = new Loadingframe();	
		//		  lframe.bigStep("Sorting pathways");
				  //quicksortPathsById(pathways,0,pathways.size()-1);
		//		  Loadingframe.close();


				  int tmpCnt = 0;
/* 372:400 */     for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++)
/* 373:    */     {
/* 374:401 */       tmpCnt = pathCnt;
/* 375:402 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
/* 376:403 */         if (((PathwayWithEc)pathways.get(tmpCnt)).idBiggerId2(((PathwayWithEc)pathways.get(pathCnt2)))) {
/* 377:404 */           tmpCnt = pathCnt2;
/* 378:    */         }
/* 379:    */       }
/* 380:407 */       pathways.add(pathCnt, (PathwayWithEc)pathways.get(tmpCnt));
/* 381:408 */       pathways.remove(tmpCnt + 1);
/* 382:    */     }
				 
//* 397:419 */     boolean changed = true;
//* 398:420 */     Pathway path1 = null;
//* 399:421 */     Pathway path2 = null;
//* 400:    */     int pathCnt=0;
///* 401:422 */     for (; changed; pathCnt < pathways.size())
 //                 while(changed && (pathCnt < pathways.size()))
//* 402:    */     {
//* 403:423 */       changed = false;
//* 404:    */       
///* 405:425 */       pathCnt = 0; continue;
//* 406:426 */       path1 = (Pathway)pathways.get(pathCnt);
//* 407:427 */       path2 = null;
//* 408:428 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++)
//* 409:    */       {
//* 410:429 */         path2 = (Pathway)pathways.get(pathCnt2);
//* 411:430 */         if (!path1.idBiggerId2(path2)) {
//* 412:    */           break;
//* 413:    */         }
//* 414:432 */         PathwayWithEc origPaths1 = (PathwayWithEc)pathways.get(pathCnt);
//* 415:433 */         PathwayWithEc origPaths2 = (PathwayWithEc)pathways.get(pathCnt2);
//* 416:    */         
//* 417:435 */         pathways.remove(pathCnt2);
//* 418:436 */         pathways.remove(pathCnt);
//* 419:    */         
//* 420:438 */         pathways.add(pathCnt, origPaths2);
//* 421:439 */         pathways.add(pathCnt2, origPaths1);
//* 422:    */         
//* 423:441 */         pathCnt++;
//* 424:442 */         pathCnt2++;
//* 425:443 */         changed = true;
//* 426:    */       }
//* 427:425 */       pathCnt++;
//* 428:    */     }
/* 429:    */   }

				private void quicksortPathsById(ArrayList<PathwayWithEc> path, int low, int high)
				{
					int i=low, j=high;
//					if(i>=j){return;}
					PathwayWithEc pivot=path.get(low+(high-low)/2);
					while(i<=j)
					{
						while(path.get(i).idSmallerId2(pivot))
						{
							i++;
						}
						while(path.get(j).idBiggerId2(pivot))
						{
							j--;
						}
						if(i<=j)
						{
							switchPaths(path, i, j);
							i++;
							j--;
						}
					}
					if(low < j)
						quicksortPathsById(path, low, j);
					if(i < high)
						quicksortPathsById(path, i, high);
				}

				private void switchPaths(ArrayList<PathwayWithEc> path, int i, int j)
				{
					PathwayWithEc tmp=path.get(i);

					path.set(i, path.get(j));
					path.set(j, tmp);
				}


				
/* 430:    */   
/* 431:    */   public int convertStringtoInt(String in)
/* 432:    */   {
/* 433:453 */     int ret = 0;
/* 434:454 */     int ccti = 0;
/* 435:456 */     for (int i = 0; i < in.length(); i++)
/* 436:    */     {
/* 437:458 */       char c = in.charAt(i);
/* 438:459 */       ccti = convertCharToInt(c);
/* 439:460 */       if (ccti == -1) {
/* 440:461 */         return ret;
/* 441:    */       }
/* 442:464 */       ret *= 10;
/* 443:465 */       ret += ccti;
/* 444:    */     }
/* 445:468 */     return ret;
/* 446:    */   }
/* 447:    */   
/* 448:    */   public int convertCharToInt(char c)
/* 449:    */   {
/* 450:471 */     switch (c)
/* 451:    */     {
/* 452:    */     case '0': 
/* 453:473 */       return 0;
/* 454:    */     case '1': 
/* 455:476 */       return 1;
/* 456:    */     case '2': 
/* 457:479 */       return 2;
/* 458:    */     case '3': 
/* 459:482 */       return 3;
/* 460:    */     case '4': 
/* 461:485 */       return 4;
/* 462:    */     case '5': 
/* 463:488 */       return 5;
/* 464:    */     case '6': 
/* 465:491 */       return 6;
/* 466:    */     case '7': 
/* 467:494 */       return 7;
/* 468:    */     case '8': 
/* 469:497 */       return 8;
/* 470:    */     case '9': 
/* 471:500 */       return 9;
/* 472:    */     }
/* 473:503 */     return -1;
/* 474:    */   }
/* 475:    */   
/* 476:    */   private void changeMode()
/* 477:    */   {
/* 478:508 */     redo();
/* 479:    */   }
/* 480:    */   
/* 481:    */   public void newSize(Dimension dim) {}
/* 482:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.PathwaysPane

 * JD-Core Version:    0.7.0.1

 */