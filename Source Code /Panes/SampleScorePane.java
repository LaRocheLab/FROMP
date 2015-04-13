/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.Pathway;
/*   4:    */ import Objects.PathwayWithEc;
/*   5:    */ import Objects.Project;
/*   6:    */ import Objects.Sample;
/*   7:    */ import Prog.DataProcessor;
/*   8:    */ import Prog.PathButt;
/*   9:    */ import java.awt.BorderLayout;
/*  10:    */ import java.awt.Dimension;
/*  11:    */ import java.awt.event.ActionEvent;
/*  12:    */ import java.awt.event.ActionListener;
/*  13:    */ import java.awt.event.MouseEvent;
/*  14:    */ import java.awt.event.MouseListener;
/*  15:    */ import java.awt.event.MouseWheelEvent;
/*  16:    */ import java.awt.event.MouseWheelListener;
/*  17:    */ import java.io.BufferedWriter;
/*  18:    */ import java.io.File;
/*  19:    */ import java.io.FileWriter;
/*  20:    */ import java.io.IOException;
/*  21:    */ import java.io.PrintStream;
/*  22:    */ import java.util.ArrayList;
/*  23:    */ import javax.swing.JButton;
/*  24:    */ import javax.swing.JCheckBox;
/*  25:    */ import javax.swing.JFileChooser;
/*  26:    */ import javax.swing.JLabel;
/*  27:    */ import javax.swing.JPanel;
/*  28:    */ import javax.swing.JScrollPane;
/*  29:    */ import javax.swing.filechooser.FileFilter;
/*  30:    */ 
				// The "Show pathway-scores" tab of the pathway completeness analysis

/*  31:    */ public class SampleScorePane
/*  32:    */   extends JPanel
/*  33:    */ {
/*  34:    */   private static final long serialVersionUID = 1L;	// 
/*  35:    */   private DataProcessor proc_;						// the data processor object used to parse through input files
/*  36: 43 */   private static int counter = 0;						// 
/*  37: 45 */   private int yoffset = 100;							// 
/*  38: 46 */   private int linCnt = 0;								// 
/*  39: 47 */   private int colCnt = 0;								// 
/*  40: 48 */   private int linDis = 40;							// 
/*  41: 49 */   private int colDis = 275;							// 
/*  42: 51 */   private int lines = 31;								// 
/*  43:    */   private double maxVisScore;							// 
/*  44:    */   private JLabel mouseOverDisp;						// 
/*  45:    */   private JPanel mouseOverP;							// 
/*  46:    */   private JPanel optionsPanel_;						// the options panel upon which the options, ie buttons and chectboxes, are displayed
/*  47:    */   private JPanel displayP_;							// the display panel upon which the pathway scores will be displayed 
/*  48:    */   private JScrollPane showJPanel_;					// 
/*  49:    */   private JCheckBox useCsf_;							// 
/*  50:    */   private static MouseOverFrame infoFrame;			// 
/*  51:    */   int xWidth;											// 
/*  52:    */   
/*  53:    */   public SampleScorePane(Sample overAll, Project actProj, DataProcessor proc, Dimension dim)
/*  54:    */   {
/*  55: 72 */     System.out.println("SampleScorePane");
/*  56: 73 */     this.maxVisScore = Project.minVisScore_;
/*  57: 74 */     this.proc_ = proc;
/*  58: 75 */     this.xWidth = ((Project.samples_.size() + 3) * this.colDis);
/*  59:    */     
/*  60: 77 */     setLayout(new BorderLayout());
/*  61: 78 */     setVisible(true);
/*  62: 79 */     setBackground(Project.getBackColor_());
/*  63: 80 */     setSize(dim);
/*  64: 81 */     initMainPanels();
/*  65:    */     
/*  66: 83 */     addscrollOptions();
/*  67:    */     
/*  68: 85 */     addOptions();
/*  69: 86 */     showMat();
/*  70:    */     
/*  71: 88 */     repaint();
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void initMainPanels()
/*  75:    */   {
/*  76: 92 */     this.optionsPanel_ = new JPanel();
/*  77:    */     
/*  78: 94 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth() - 20, 100));
/*  79: 95 */     this.optionsPanel_.setLocation(0, 0);
/*  80: 96 */     this.optionsPanel_.setBackground(Project.standard);
/*  81: 97 */     this.optionsPanel_.setVisible(true);
/*  82: 98 */     this.optionsPanel_.setLayout(null);
/*  83: 99 */     add("First", this.optionsPanel_);
/*  84:    */     
/*  85:101 */     this.displayP_ = new JPanel();
/*  86:102 */     this.displayP_.setLocation(0, 0);
/*  87:103 */     this.displayP_.setPreferredSize(new Dimension(2000, 2000));
/*  88:104 */     this.displayP_.setSize(new Dimension(2000, 2000));
/*  89:105 */     this.displayP_.setBackground(Project.getBackColor_());
/*  90:106 */     this.displayP_.setVisible(true);
/*  91:107 */     this.displayP_.setLayout(null);
/*  92:    */     
/*  93:109 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/*  94:110 */     this.showJPanel_.setVisible(true);
/*  95:111 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/*  96:112 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/*  97:    */     
/*  98:114 */     add("Center", this.showJPanel_);
/*  99:    */   }
/* 100:    */   
/* 101:    */   private void addscrollOptions()
/* 102:    */   {
/* 103:121 */     addMouseWheelListener(new MouseWheelListener()
/* 104:    */     {
/* 105:    */       public void mouseWheelMoved(MouseWheelEvent e)
/* 106:    */       {
/* 107:125 */         e.getWheelRotation();
/* 108:    */       }
/* 109:    */     });
/* 110:    */   }
/* 111:    */   
/* 112:    */   private void addOptions()
/* 113:    */   {
/* 114:133 */     JButton left = new JButton("Previous Sample");
/* 115:134 */     left.setBounds(10, 10, 200, 25);
/* 116:135 */     left.setVisible(true);
/* 117:136 */     left.addActionListener(new ActionListener()
/* 118:    */     {
/* 119:    */       public void actionPerformed(ActionEvent e)
/* 120:    */       {
/* 121:141 */         SampleScorePane.counter -= 1;
/* 122:142 */         if ((SampleScorePane.counter < 0) || (SampleScorePane.counter > Project.samples_.size())) {
/* 123:143 */           SampleScorePane.counter = Project.samples_.size();
/* 124:    */         }
/* 125:145 */         SampleScorePane.this.displayP_.removeAll();
/* 126:146 */         SampleScorePane.this.showMat();
/* 127:    */         
/* 128:148 */         SampleScorePane.this.optionsPanel_.removeAll();
/* 129:149 */         SampleScorePane.this.addOptions();
/* 130:    */         
/* 131:151 */         SampleScorePane.this.invalidate();
/* 132:152 */         SampleScorePane.this.validate();
/* 133:153 */         SampleScorePane.this.repaint();
/* 134:    */       }
/* 135:156 */     });
/* 136:157 */     this.optionsPanel_.add(left);
/* 137:    */     
/* 138:159 */     JButton right = new JButton("Next Sample");
/* 139:160 */     right.setBounds(10, 40, 200, 25);
/* 140:161 */     right.setVisible(true);
/* 141:162 */     right.addActionListener(new ActionListener()
/* 142:    */     {
/* 143:    */       public void actionPerformed(ActionEvent e)
/* 144:    */       {
/* 145:167 */         SampleScorePane.counter += 1;
/* 146:168 */         if (SampleScorePane.counter > Project.samples_.size()) {
/* 147:169 */           SampleScorePane.counter = 0;
/* 148:    */         }
/* 149:171 */         SampleScorePane.this.displayP_.removeAll();
/* 150:172 */         SampleScorePane.this.showMat();
/* 151:    */         
/* 152:174 */         SampleScorePane.this.optionsPanel_.removeAll();
/* 153:175 */         SampleScorePane.this.addOptions();
/* 154:    */         
/* 155:177 */         SampleScorePane.this.invalidate();
/* 156:178 */         SampleScorePane.this.validate();
/* 157:179 */         SampleScorePane.this.repaint();
/* 158:    */       }
/* 159:183 */     });
/* 160:184 */     this.optionsPanel_.add(right);
/* 161:186 */     if (this.useCsf_ == null) {
/* 162:187 */       this.useCsf_ = new JCheckBox("CSF");
/* 163:    */     }
/* 164:189 */     this.useCsf_.setVisible(true);
/* 165:190 */     this.useCsf_.setLayout(null);
/* 166:191 */     this.useCsf_.setBackground(this.optionsPanel_.getBackground());
/* 167:192 */     this.useCsf_.setForeground(Project.getFontColor_());
/* 168:193 */     this.useCsf_.setBounds(250, 50, 80, 15);
/* 169:194 */     this.optionsPanel_.add(this.useCsf_);
/* 170:    */     
/* 171:196 */     JButton export = new JButton("Write to file");
/* 172:197 */     export.setBounds(250, 10, 130, 25);
/* 173:198 */     export.setVisible(true);
/* 174:199 */     export.setLayout(null);
/* 175:200 */     export.setForeground(Project.getFontColor_());
/* 176:201 */     export.addActionListener(new ActionListener()
/* 177:    */     {
/* 178:    */       public void actionPerformed(ActionEvent e)
/* 179:    */       {
/* 180:205 */         JFileChooser fChoose_ = new JFileChooser(Project.workpath_);
/* 181:206 */         fChoose_.setFileSelectionMode(0);
/* 182:207 */         fChoose_.setBounds(100, 100, 200, 20);
/* 183:208 */         fChoose_.setVisible(true);
/* 184:209 */         File file = new File(Project.workpath_);
/* 185:210 */         fChoose_.setSelectedFile(file);
/* 186:211 */         fChoose_.setFileFilter(new FileFilter()
/* 187:    */         {
/* 188:    */           public boolean accept(File f)
/* 189:    */           {
/* 190:215 */             if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".txt"))) {
/* 191:216 */               return true;
/* 192:    */             }
/* 193:218 */             return false;
/* 194:    */           }
/* 195:    */           
/* 196:    */           public String getDescription()
/* 197:    */           {
/* 198:224 */             return ".txt";
/* 199:    */           }
/* 200:    */         });
/* 201:228 */         if (fChoose_.showSaveDialog(null) == 0) {
/* 202:    */           try
/* 203:    */           {
/* 204:230 */             String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 205:232 */             if (!path.endsWith(".txt"))
/* 206:    */             {
/* 207:233 */               path = path + ".txt";
/* 208:234 */               System.out.println(".txt");
/* 209:    */             }
/* 210:236 */             SampleScorePane.this.exportMat(path, SampleScorePane.this.useCsf_.isSelected());
/* 211:    */           }
/* 212:    */           catch (IOException e1)
/* 213:    */           {
/* 214:239 */             e1.printStackTrace();
/* 215:    */           }
/* 216:    */         }
/* 217:    */       }
/* 218:244 */     });
/* 219:245 */     this.optionsPanel_.add(export);
/* 220:    */     
/* 221:247 */     JButton openDetailFr = new JButton("Pathway-Info-Window");
/* 222:248 */     openDetailFr.setBounds(420, 10, 200, 25);
/* 223:249 */     openDetailFr.setVisible(true);
/* 224:250 */     openDetailFr.addActionListener(new ActionListener()
/* 225:    */     {
/* 226:    */       public void actionPerformed(ActionEvent e)
/* 227:    */       {
/* 228:255 */         if (SampleScorePane.infoFrame == null)
/* 229:    */         {
/* 230:256 */           SampleScorePane.this.addMouseOverFrame();
/* 231:    */         }
/* 232:    */         else
/* 233:    */         {
/* 234:259 */           SampleScorePane.infoFrame.closeFrame();
/* 235:260 */           SampleScorePane.infoFrame = null;
/* 236:    */         }
/* 237:    */       }
/* 238:263 */     });
/* 239:264 */     this.optionsPanel_.add(openDetailFr);
/* 240:    */     
/* 241:266 */     Sample tmpSample = null;
/* 242:    */     JLabel label;
//* 243:    */     JLabel label;
/* 244:270 */     if (counter == Project.samples_.size())
/* 245:    */     {
/* 246:271 */       tmpSample = Project.overall_;
/* 247:272 */       label = new JLabel("Sample 0: " + tmpSample.name_);
/* 248:    */     }
/* 249:    */     else
/* 250:    */     {
/* 251:275 */       tmpSample = (Sample)Project.samples_.get(counter);
/* 252:276 */       label = new JLabel("Sample " + (counter + 1) + ": " + tmpSample.name_);
/* 253:    */     }
/* 254:279 */     label.setBounds(10, 70, 500, 20);
/* 255:280 */     this.optionsPanel_.add(label);
/* 256:    */     
/* 257:282 */     this.mouseOverP = new JPanel();
/* 258:283 */     this.mouseOverP.setBackground(Project.getBackColor_());
/* 259:284 */     this.mouseOverP.setBounds(660, 10, 500, 60);
/* 260:285 */     this.optionsPanel_.add(this.mouseOverP);
/* 261:    */     
/* 262:287 */     this.mouseOverDisp = new JLabel("Additional Pathway-information");
/* 263:    */     
/* 264:289 */     this.mouseOverDisp.setBounds(0, 0, 500, 60);
/* 265:290 */     this.mouseOverP.add(this.mouseOverDisp);
/* 266:    */   }
/* 267:    */   
/* 268:    */   private void showMat()
/* 269:    */   {
/* 270:293 */     String tmpWeight = "";
/* 271:294 */     String tmpScore = "";
/* 272:    */     
/* 273:296 */     Sample tmpSample = null;
/* 274:297 */     PathwayWithEc tmpPath = null;
/* 275:    */     
/* 276:299 */     JButton pathButt = null;
/* 277:301 */     if (counter == Project.samples_.size()) {
/* 278:302 */       tmpSample = Project.overall_;
/* 279:    */     } else {
/* 280:305 */       tmpSample = (Sample)Project.samples_.get(counter);
/* 281:    */     }
/* 282:309 */     int listCount = 0;
/* 283:310 */     if (Project.listMode_) {
/* 284:311 */       sortPathwaysByScore(tmpSample.pathways_);
/* 285:    */     } else {
/* 286:314 */       sortPathsById(tmpSample.pathways_);
/* 287:    */     }
/* 288:316 */     for (int pathCnt = 0; pathCnt < tmpSample.pathways_.size(); pathCnt++) {
/* 289:317 */       if (this.proc_.getPathway(((PathwayWithEc)tmpSample.pathways_.get(pathCnt)).id_).isSelected())
/* 290:    */       {
/* 291:321 */         tmpPath = (PathwayWithEc)tmpSample.pathways_.get(pathCnt);
/* 292:322 */         if (tmpPath.name_.contentEquals("testPath")) {
/* 293:323 */           System.err.println("PathButt");
/* 294:    */         }
/* 295:325 */         tmpWeight = String.valueOf(tmpPath.weight_);
/* 296:327 */         if (((PathwayWithEc)tmpSample.pathways_.get(pathCnt)).isSelected()) {
/* 297:330 */           if (tmpPath.score_ >= this.maxVisScore)
/* 298:    */           {
/* 299:331 */             if (tmpWeight.length() > 3) {
/* 300:332 */               tmpWeight = tmpWeight.substring(0, 3);
/* 301:    */             }
/* 302:334 */             tmpScore = String.valueOf(tmpPath.score_);
/* 303:335 */             if (tmpScore.length() > 3) {
/* 304:336 */               tmpScore = tmpScore.substring(0, 3);
/* 305:    */             }
/* 306:338 */             pathButt = new PathButt(Project.samples_, tmpSample, tmpPath, Project.getBackColor_(), Project.workpath_, 1);
/* 307:339 */             final PathwayWithEc fpath = new PathwayWithEc(tmpPath, false);
/* 308:340 */             pathButt.addMouseListener(new MouseListener()
/* 309:    */             {
/* 310:    */               public void mouseReleased(MouseEvent e) {}
/* 311:    */               
/* 312:    */               public void mousePressed(MouseEvent e) {}
/* 313:    */               
/* 314:    */               public void mouseExited(MouseEvent e)
/* 315:    */               {
/* 316:357 */                 SampleScorePane.this.mouseOverDisp.setVisible(false);
/* 317:    */                 
/* 318:359 */                 SampleScorePane.this.mouseOverDisp.setText("Additional Pathway-information");
/* 319:    */               }
/* 320:    */               
/* 321:    */               public void mouseEntered(MouseEvent e)
/* 322:    */               {
/* 323:366 */                 SampleScorePane.this.mouseOverDisp.setVisible(true);
/* 324:    */                 
/* 325:368 */                 SampleScorePane.this.setAdditionalInfo(fpath);
/* 326:    */               }
/* 327:    */               
/* 328:    */               public void mouseClicked(MouseEvent e) {}
/* 329:    */             });
/* 330:378 */             if (Project.listMode_)
/* 331:    */             {
/* 332:379 */               this.linCnt = (listCount % this.lines);
/* 333:380 */               this.colCnt = (listCount / this.lines);
/* 334:    */             }
/* 335:    */             else
/* 336:    */             {
/* 337:383 */               this.linCnt = (pathCnt % this.lines);
/* 338:384 */               this.colCnt = (pathCnt / this.lines);
/* 339:    */             }
/* 340:386 */             pathButt.setBounds(this.colCnt * this.colDis, this.linCnt * this.linDis + this.yoffset, this.colDis, this.linDis);
/* 341:387 */             this.displayP_.add(pathButt);
/* 342:388 */             listCount++;
/* 343:    */           }
/* 344:    */         }
/* 345:    */       }
/* 346:    */     }
/* 347:    */   }
/* 348:    */   
/* 349:    */   private void sortPathwaysByScore(ArrayList<PathwayWithEc> pathways)
/* 350:    */   {
/* 351:393 */     int tmpCnt = 0;
/* 352:394 */     for (int pathCnt = 0; pathCnt < pathways.size() - 1; pathCnt++)
/* 353:    */     {
/* 354:395 */       tmpCnt = pathCnt;
/* 355:396 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++) {
/* 356:397 */         if (((PathwayWithEc)pathways.get(tmpCnt)).score_ < ((PathwayWithEc)pathways.get(pathCnt2)).score_) {
/* 357:398 */           tmpCnt = pathCnt2;
/* 358:    */         }
/* 359:    */       }
/* 360:401 */       pathways.add(pathCnt, (PathwayWithEc)pathways.get(tmpCnt));
/* 361:402 */       pathways.remove(tmpCnt + 1);
/* 362:    */     }
/* 363:    */   }
/* 364:    */   
/* 365:    */   private void sortPathsById(ArrayList<PathwayWithEc> pathways)
/* 366:    */   {
/* 367:406 */     boolean changed = true;
/* 368:407 */     Pathway path1 = null;
/* 369:408 */     Pathway path2 = null;
/* 370:    */     int pathCnt=0;
//* 371:409 */     for (; changed; pathCnt < pathways.size())
                  while(changed && (pathCnt < pathways.size()))
/* 372:    */     {
/* 373:410 */       changed = false;
/* 374:    */       
//* 375:412 */       pathCnt = 0; continue;
/* 376:413 */       path1 = (Pathway)pathways.get(pathCnt);
/* 377:414 */       path2 = null;
/* 378:415 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < pathways.size(); pathCnt2++)
/* 379:    */       {
/* 380:416 */         path2 = (Pathway)pathways.get(pathCnt2);
/* 381:417 */         if (!path1.idBiggerId2(path2)) {
/* 382:    */           break;
/* 383:    */         }
/* 384:419 */         PathwayWithEc origPaths1 = (PathwayWithEc)pathways.get(pathCnt);
/* 385:420 */         PathwayWithEc origPaths2 = (PathwayWithEc)pathways.get(pathCnt2);
/* 386:    */         
/* 387:422 */         pathways.remove(pathCnt2);
/* 388:423 */         pathways.remove(pathCnt);
/* 389:    */         
/* 390:425 */         pathways.add(pathCnt, origPaths2);
/* 391:426 */         pathways.add(pathCnt2, origPaths1);
/* 392:    */         
/* 393:428 */         pathCnt++;
/* 394:429 */         pathCnt2++;
/* 395:430 */         changed = true;
/* 396:    */       }
/* 397:412 */       pathCnt++;
/* 398:    */     }
/* 399:    */   }
/* 400:    */   
/* 401:    */   private void setAdditionalInfo(PathwayWithEc path)
/* 402:    */   {
/* 403:441 */     this.mouseOverDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_ + "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_ + "</html>");
/* 404:443 */     if (infoFrame != null) {
/* 405:444 */       infoFrame.setAdditionalInfo(path);
/* 406:    */     }
/* 407:    */   }
/* 408:    */   
/* 409:    */   private void addMouseOverFrame()
/* 410:    */   {
/* 411:448 */     if (MouseOverFrame.getFrameCount() <= 0) {
/* 412:449 */       infoFrame = new MouseOverFrame();
/* 413:    */     }
/* 414:    */   }
/* 415:    */   
/* 416:    */   private void exportMat(String path, boolean inCsf)
/* 417:    */   {
/* 418:456 */     String seperator = "\t";
/* 419:457 */     if (inCsf) {
/* 420:458 */       seperator = ",";
/* 421:    */     }
/* 422:    */     try
/* 423:    */     {
/* 424:461 */       BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 425:    */       Sample tmpSample;
//* 426:    */       Sample tmpSample;
/* 427:465 */       if (counter == Project.samples_.size()) {
/* 428:466 */         tmpSample = Project.overall_;
/* 429:    */       } else {
/* 430:469 */         tmpSample = (Sample)Project.samples_.get(counter);
/* 431:    */       }
/* 432:471 */       out.write("**** n.s. = 'not selected' ******** n.a. = not above minScore");
/* 433:472 */       out.newLine();
/* 434:473 */       out.newLine();
/* 435:474 */       out.write("Sample Scores " + seperator + tmpSample.name_);
/* 436:475 */       out.newLine();
/* 437:476 */       out.newLine();
/* 438:477 */       ArrayList<PathwayWithEc> pathways = tmpSample.pathways_;
/* 439:478 */       for (int pathCnt = 0; pathCnt < pathways.size(); pathCnt++)
/* 440:    */       {
/* 441:479 */         PathwayWithEc pathway = (PathwayWithEc)pathways.get(pathCnt);
/* 442:480 */         if ((pathway.isSelected()) && (pathway.score_ >= this.maxVisScore))
/* 443:    */         {
/* 444:481 */           out.write(pathway.id_ + seperator + pathway.name_ + seperator + pathway.score_);
/* 445:482 */           out.newLine();
/* 446:    */         }
/* 447:    */         else
/* 448:    */         {
/* 449:485 */           if (!pathway.isSelected()) {
/* 450:486 */             out.write(pathway.id_ + seperator + pathway.name_ + " n.s.");
/* 451:    */           } else {
/* 452:489 */             out.write(pathway.id_ + seperator + pathway.name_ + " n.a.");
/* 453:    */           }
/* 454:491 */           out.newLine();
/* 455:    */         }
/* 456:    */       }
/* 457:494 */       out.newLine();
/* 458:    */       
/* 459:    */ 
/* 460:    */ 
/* 461:498 */       out.close();
/* 462:    */     }
/* 463:    */     catch (IOException e)
/* 464:    */     {
/* 465:501 */       e.printStackTrace();
/* 466:    */     }
/* 467:    */   }
/* 468:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.SampleScorePane

 * JD-Core Version:    0.7.0.1

 */