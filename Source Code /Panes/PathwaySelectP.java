/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.Pathway;
/*   4:    */ import Objects.PathwayWithEc;
/*   5:    */ import Objects.Project;
/*   6:    */ import java.awt.BorderLayout;
/*   7:    */ import java.awt.Color;
/*   8:    */ import java.awt.Container;
/*   9:    */ import java.awt.Dimension;
/*  10:    */ import java.awt.event.ActionEvent;
/*  11:    */ import java.awt.event.ActionListener;
/*  12:    */ import java.io.BufferedReader;
/*  13:    */ import java.io.BufferedWriter;
/*  14:    */ import java.io.File;
/*  15:    */ import java.io.FileReader;
/*  16:    */ import java.io.FileWriter;
/*  17:    */ import java.io.IOException;
/*  18:    */ import java.io.PrintStream;
/*  19:    */ import java.util.ArrayList;
/*  20:    */ import javax.swing.JButton;
/*  21:    */ import javax.swing.JCheckBox;
/*  22:    */ import javax.swing.JFileChooser;
/*  23:    */ import javax.swing.JLabel;
/*  24:    */ import javax.swing.JPanel;
/*  25:    */ import javax.swing.JScrollPane;
/*  26:    */ import javax.swing.filechooser.FileFilter;
/*  27:    */ import pathwayLayout.PathLayoutGrid;
/*  28:    */ 
/*  29:    */ public class PathwaySelectP
/*  30:    */   extends JPanel
/*  31:    */ {
/*  32:    */   private static final long serialVersionUID = 1L;
/*  33:    */   ArrayList<PathwayWithEc> paths_;
/*  34: 45 */   final String cfgName = "pwcfg:";
/*  35: 46 */   final String selected = "TRUE";
/*  36: 47 */   final String notSelected = "FALSE";
/*  37: 48 */   final String fileEnding = ".pcg";
/*  38: 49 */   final String userPathfileEnding = ".pwm";
/*  39: 50 */   final String basePath_ = new File(".").getAbsolutePath() + File.separator;
/*  40:    */   boolean selectAll;
/*  41: 53 */   int xLine2 = 500;
/*  42:    */   JPanel optionsPanel_;
/*  43:    */   JPanel displayP_;
/*  44:    */   JScrollPane showJPanel_;
/*  45:    */   public JButton next_;
/*  46:    */   public JButton back_;
/*  47:    */   
/*  48:    */   public PathwaySelectP(ArrayList<PathwayWithEc> pws)
/*  49:    */   {
/*  50: 66 */     setBounds(0, 0, 1000, 3500);
/*  51: 67 */     setVisible(true);
/*  52: 68 */     setLayout(new BorderLayout());
/*  53: 69 */     setBackground(Project.getBackColor_());
/*  54: 70 */     this.selectAll = true;
/*  55:    */     
/*  56: 72 */     this.paths_ = pws;
/*  57: 73 */     this.back_ = new JButton("< Back to Sample Selection");
/*  58: 74 */     this.next_ = new JButton("Go to Analysis >");
/*  59: 75 */     initMainPanels();
/*  60: 76 */     showComponents();
/*  61:    */   }
/*  62:    */   
/*  63:    */   private void redo()
/*  64:    */   {
/*  65: 79 */     removeAll();
/*  66: 80 */     initMainPanels();
/*  67: 81 */     showComponents();
/*  68: 82 */     invalidate();
/*  69: 83 */     validate();
/*  70: 84 */     repaint();
/*  71:    */   }
/*  72:    */   
/*  73:    */   private void initMainPanels()
/*  74:    */   {
/*  75: 87 */     this.optionsPanel_ = new JPanel();
/*  76: 88 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth(), 100));
/*  77: 89 */     this.optionsPanel_.setLocation(0, 0);
/*  78: 90 */     this.optionsPanel_.setBackground(Project.getBackColor_().darker());
/*  79: 91 */     this.optionsPanel_.setVisible(true);
/*  80: 92 */     this.optionsPanel_.setLayout(null);
/*  81: 93 */     add(this.optionsPanel_, "First");
/*  82:    */     
/*  83: 95 */     this.displayP_ = new JPanel();
/*  84: 96 */     this.displayP_.setLocation(0, 0);
/*  85: 97 */     this.displayP_.setPreferredSize(new Dimension(1000, 4000));
/*  86: 98 */     this.displayP_.setSize(getPreferredSize());
/*  87: 99 */     this.displayP_.setBackground(Project.getBackColor_());
/*  88:    */     
/*  89:101 */     this.displayP_.setVisible(true);
/*  90:102 */     this.displayP_.setLayout(null);
/*  91:    */     
/*  92:104 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/*  93:105 */     this.showJPanel_.setVisible(true);
/*  94:106 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/*  95:107 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/*  96:    */     
/*  97:109 */     add("Center", this.showJPanel_);
/*  98:    */     
/*  99:111 */     addNextBackButt();
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void showComponents()
/* 103:    */   {
/* 104:118 */     addButtons();
/* 105:119 */     addPathways();
/* 106:    */   }
/* 107:    */   
/* 108:    */   private void addNextBackButt()
/* 109:    */   {
/* 110:123 */     this.back_.setBounds(20, 75, 250, 20);
/* 111:124 */     this.back_.setVisible(true);
/* 112:125 */     this.back_.setLayout(null);
/* 113:126 */     this.optionsPanel_.add(this.back_);
/* 114:    */     
/* 115:    */ 
/* 116:129 */     this.next_.setBounds(this.xLine2 + 150, 75, 250, 20);
/* 117:130 */     this.next_.setVisible(true);
/* 118:131 */     this.next_.setLayout(null);
/* 119:132 */     this.optionsPanel_.add(this.next_);
/* 120:    */   }
/* 121:    */   
/* 122:    */   private void addPathways()
/* 123:    */   {
/* 124:136 */     for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++)
/* 125:    */     {
/* 126:137 */       final int pwInt = pwCnt;
/* 127:138 */       Pathway tmpPath = (Pathway)this.paths_.get(pwCnt);
/* 128:139 */       final JCheckBox tmpBox = new JCheckBox(tmpPath.id_ + " / " + tmpPath.name_);
/* 129:140 */       tmpBox.setSelected(tmpPath.isSelected());
/* 130:141 */       tmpBox.setBounds(20, 90 + 20 * pwCnt, 400, 17);
/* 131:    */       
/* 132:143 */       tmpBox.addActionListener(new ActionListener()
/* 133:    */       {
/* 134:    */         public void actionPerformed(ActionEvent e)
/* 135:    */         {
/* 136:148 */           ((PathwayWithEc)PathwaySelectP.this.paths_.get(pwInt)).setSelected(tmpBox.isSelected());
/* 137:149 */           Prog.DataProcessor.newBaseData = true;
/* 138:150 */           System.out.println(pwInt + ((PathwayWithEc)PathwaySelectP.this.paths_.get(pwInt)).name_ + " " + ((PathwayWithEc)PathwaySelectP.this.paths_.get(pwInt)).isSelected());
/* 139:    */         }
/* 140:153 */       });
/* 141:154 */       this.displayP_.add(tmpBox);
/* 142:    */     }
/* 143:157 */     ArrayList<String> paths = Project.userPathways;
				  
/* 144:159 */     if (paths != null) {
					System.out.println(paths.toString());
/* 145:160 */       for (int uPathCnt = 0; uPathCnt < Project.userPathways.size(); uPathCnt++)
/* 146:    */       {
/* 147:161 */         final int pwInt = uPathCnt;
/* 148:162 */         String path = (String)paths.get(uPathCnt);
/* 149:163 */         JCheckBox tmpBox = new JCheckBox(path);
/* 150:164 */         tmpBox.setSelected(true);
/* 151:165 */         tmpBox.setBounds(this.xLine2, 90 + 20 * uPathCnt, 400, 17);
/* 152:    */         
/* 153:167 */         tmpBox.addActionListener(new ActionListener()
/* 154:    */         {
/* 155:    */           public void actionPerformed(ActionEvent e)
/* 156:    */           {
/* 157:173 */             Project.removeUserPath((String)Project.userPathways.get(pwInt));
/* 158:174 */             Project.userPathways.remove(pwInt);
/* 159:    */             
/* 160:176 */             Prog.DataProcessor.newUserData = true;
/* 161:177 */             PathwaySelectP.this.redo();
/* 162:    */           }
/* 163:180 */         });
/* 164:181 */         this.displayP_.add(tmpBox);
/* 165:    */       }
/* 166:    */     }
/* 167:184 */     final JCheckBox tmpBox2 = new JCheckBox("de/select all");
/* 168:185 */     final JCheckBox tmpBox = new JCheckBox("de/select all");
/* 169:186 */     tmpBox.setSelected(this.selectAll);
/* 170:187 */     tmpBox.setBounds(20, 90 + (20 * this.paths_.size() + 1), 400, 17);
/* 171:    */     
/* 172:189 */     tmpBox.addActionListener(new ActionListener()
/* 173:    */     {
/* 174:    */       public void actionPerformed(ActionEvent e)
/* 175:    */       {
/* 176:194 */         PathwaySelectP.this.selectAll(tmpBox.isSelected());
/* 177:195 */         tmpBox2.setSelected(tmpBox.isSelected());
/* 178:    */       }
/* 179:199 */     });
/* 180:200 */     this.displayP_.add(tmpBox);
/* 181:201 */     tmpBox2.setSelected(this.selectAll);
/* 182:202 */     tmpBox2.setBounds(20, 70, 400, 17);
/* 183:    */     
/* 184:204 */     tmpBox2.addActionListener(new ActionListener()
/* 185:    */     {
/* 186:    */       public void actionPerformed(ActionEvent e)
/* 187:    */       {
/* 188:209 */         PathwaySelectP.this.selectAll(tmpBox2.isSelected());
/* 189:210 */         tmpBox.setSelected(tmpBox2.isSelected());
/* 190:    */       }
/* 191:213 */     });
/* 192:214 */     this.displayP_.add(tmpBox2);
/* 193:    */   }
/* 194:    */   
/* 195:    */   private void addButtons()
/* 196:    */   {
/* 197:217 */     JLabel descr = new JLabel("Select your wanted Pathways");
/* 198:218 */     descr.setBounds(20, 10, 400, 17);
/* 199:219 */     this.displayP_.add(descr);
/* 200:    */     
/* 201:221 */     descr = new JLabel("Custom-made Pathways");
/* 202:222 */     descr.setBounds(this.xLine2, 10, 400, 17);
/* 203:223 */     this.displayP_.add(descr);
/* 204:    */     
/* 205:225 */     JButton save = new JButton("Save pathway selection");
/* 206:226 */     save.setBounds(20, 27, 400, 20);
/* 207:227 */     save.setVisible(true);
/* 208:228 */     save.setLayout(null);
/* 209:229 */     save.addActionListener(new ActionListener()
/* 210:    */     {
/* 211:    */       public void actionPerformed(ActionEvent e)
/* 212:    */       {
/* 213:234 */         PathwaySelectP.this.saveDialog("");
/* 214:    */       }
/* 215:237 */     });
/* 216:238 */     this.optionsPanel_.add(save);
/* 217:    */     
/* 218:240 */     JButton load = new JButton("Load pathway selection");
/* 219:241 */     load.setBounds(20, 47, 400, 20);
/* 220:242 */     load.setVisible(true);
/* 221:243 */     load.setLayout(null);
/* 222:244 */     load.addActionListener(new ActionListener()
/* 223:    */     {
/* 224:    */       public void actionPerformed(ActionEvent e)
/* 225:    */       {
/* 226:249 */         PathwaySelectP.this.openLoadDialog("");
/* 227:    */       }
/* 228:252 */     });
/* 229:253 */     this.optionsPanel_.add(load);
/* 230:    */     
/* 231:255 */     JButton addUserP = new JButton("Add custom-made pathway");
/* 232:256 */     addUserP.setBounds(this.xLine2, 27, 400, 20);
/* 233:257 */     addUserP.setVisible(true);
/* 234:258 */     addUserP.setLayout(null);
/* 235:259 */     addUserP.addActionListener(new ActionListener()
/* 236:    */     {
/* 237:    */       public void actionPerformed(ActionEvent e)
/* 238:    */       {
/* 239:265 */         PathwaySelectP.this.addUserPath();
/* 240:    */       }
/* 241:269 */     });
/* 242:270 */     this.optionsPanel_.add(addUserP);
/* 243:    */     
/* 244:272 */     JButton openDesigner = new JButton("Open pathway designer");
/* 245:273 */     openDesigner.setBounds(this.xLine2, 47, 400, 20);
/* 246:274 */     openDesigner.setVisible(true);
/* 247:275 */     openDesigner.setLayout(null);
/* 248:276 */     openDesigner.addActionListener(new ActionListener()
/* 249:    */     {
/* 250:    */       public void actionPerformed(ActionEvent e)
/* 251:    */       {
/* 252:281 */         PathLayoutGrid Grid = new PathLayoutGrid(10, 10, true);
/* 253:    */       }
/* 254:284 */     });
/* 255:285 */     this.optionsPanel_.add(openDesigner);
/* 256:    */   }
/* 257:    */   
/* 258:    */   private void selectAll(boolean selected)
/* 259:    */   {
/* 260:293 */     for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++) {
/* 261:294 */       if (!((PathwayWithEc)this.paths_.get(pwCnt)).userPathway) {
/* 262:295 */         ((PathwayWithEc)this.paths_.get(pwCnt)).setSelected(selected);
/* 263:    */       }
/* 264:    */     }
/* 265:298 */     this.selectAll = selected;
/* 266:    */     
/* 267:300 */     redo();
/* 268:    */   }
/* 269:    */   
/* 270:    */   private void saveConfig(String path)
/* 271:    */   {
/* 272:307 */     if (!path.endsWith(".pcg")) {
/* 273:308 */       path = path + ".pcg";
/* 274:    */     }
/* 275:    */     try
/* 276:    */     {
/* 277:312 */       BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 278:    */       
/* 279:314 */       out.write("pwcfg:1");
/* 280:315 */       out.newLine();
/* 281:317 */       for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++)
/* 282:    */       {
/* 283:318 */         if (((PathwayWithEc)this.paths_.get(pwCnt)).isSelected()) {
/* 284:319 */           out.write(((PathwayWithEc)this.paths_.get(pwCnt)).id_ + ":" + "TRUE");
/* 285:    */         } else {
/* 286:322 */           out.write(((PathwayWithEc)this.paths_.get(pwCnt)).id_ + ":" + "FALSE");
/* 287:    */         }
/* 288:324 */         out.newLine();
/* 289:    */       }
/* 290:326 */       out.close();
/* 291:    */     }
/* 292:    */     catch (IOException e)
/* 293:    */     {
/* 294:329 */       e.printStackTrace();
/* 295:    */     }
/* 296:    */   }
/* 297:    */   
/* 298:    */   private void loadConfig(BufferedReader inTxt)
/* 299:    */   {
/* 300:336 */     String zeile = "";
/* 301:    */     try
/* 302:    */     {
/* 303:338 */       zeile = inTxt.readLine();
/* 304:    */       
/* 305:    */ 
/* 306:341 */       int lineCounter = 0;
/* 307:342 */       while ((zeile = inTxt.readLine()) != null)
/* 308:    */       {
/* 309:343 */         convertLine(zeile, lineCounter);
/* 310:344 */         lineCounter++;
/* 311:    */       }
/* 312:    */     }
/* 313:    */     catch (IOException e)
/* 314:    */     {
/* 315:349 */       e.printStackTrace();
/* 316:    */     }
/* 317:351 */     removeAll();
/* 318:352 */     showComponents();
/* 319:    */   }
/* 320:    */   
/* 321:    */   private void convertLine(String line, int round)
/* 322:    */   {
/* 323:360 */     String id = "";
/* 324:361 */     String value = "";
/* 325:363 */     if (line.contains(":"))
/* 326:    */     {
/* 327:364 */       id = line.substring(0, line.indexOf(":"));
/* 328:365 */       value = line.substring(line.indexOf(":") + 1);
/* 329:366 */       System.out.println(id + " " + value);
/* 330:367 */       if (((PathwayWithEc)this.paths_.get(round)).id_.contentEquals(id))
/* 331:    */       {
/* 332:368 */         if (value.contentEquals("TRUE"))
/* 333:    */         {
/* 334:369 */           ((PathwayWithEc)this.paths_.get(round)).setSelected(true);
/* 335:370 */           return;
/* 336:    */         }
/* 337:373 */         ((PathwayWithEc)this.paths_.get(round)).setSelected(false);
/* 338:374 */         return;
/* 339:    */       }
/* 340:378 */       for (int pwCnt = 0; pwCnt < this.paths_.size(); pwCnt++) {
/* 341:379 */         if (((PathwayWithEc)this.paths_.get(pwCnt)).id_.contentEquals(id))
/* 342:    */         {
/* 343:380 */           if (value.contentEquals("TRUE"))
/* 344:    */           {
/* 345:381 */             ((PathwayWithEc)this.paths_.get(pwCnt)).setSelected(true);
/* 346:382 */             return;
/* 347:    */           }
/* 348:385 */           ((PathwayWithEc)this.paths_.get(pwCnt)).setSelected(false);
/* 349:386 */           return;
/* 350:    */         }
/* 351:    */       }
/* 352:    */     }
/* 353:    */     else {}
/* 354:    */   }
/* 355:    */   
/* 356:    */   private boolean saveDialog(String path)
/* 357:    */   {
/* 358:401 */     JFileChooser fChoose_ = new JFileChooser(path);
/* 359:402 */     getParent().add(fChoose_);
/* 360:403 */     fChoose_.setFileSelectionMode(0);
/* 361:404 */     fChoose_.setBounds(100, 100, 200, 20);
/* 362:405 */     fChoose_.setVisible(true);
/* 363:    */     
/* 364:    */ 
/* 365:408 */     fChoose_.setFileFilter(new FileFilter()
/* 366:    */     {
/* 367:    */       public boolean accept(File f)
/* 368:    */       {
/* 369:412 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".pcg"))) {
/* 370:413 */           return true;
/* 371:    */         }
/* 372:415 */         return false;
/* 373:    */       }
/* 374:    */       
/* 375:    */       public String getDescription()
/* 376:    */       {
/* 377:421 */         return ".pcg";
/* 378:    */       }
/* 379:    */     });
/* 380:425 */     if (fChoose_.showSaveDialog(fChoose_.getParent()) == 0) {
/* 381:    */       try
/* 382:    */       {
/* 383:427 */         path = fChoose_.getSelectedFile().getCanonicalPath();
/* 384:429 */         if (!path.endsWith(".pcg"))
/* 385:    */         {
/* 386:430 */           path = path + ".pcg";
/* 387:    */           
/* 388:432 */           System.out.println(".pcg");
/* 389:    */         }
/* 390:434 */         saveConfig(path);
/* 391:435 */         return true;
/* 392:    */       }
/* 393:    */       catch (IOException e1)
/* 394:    */       {
/* 395:438 */         e1.printStackTrace();
/* 396:    */       }
/* 397:    */     } else {
/* 398:442 */       return false;
/* 399:    */     }
/* 400:444 */     return false;
/* 401:    */   }
/* 402:    */   
/* 403:    */   private void openLoadDialog(String path)
/* 404:    */   {
/* 405:451 */     JFileChooser fChoose_ = new JFileChooser(path);
/* 406:452 */     fChoose_.setFileSelectionMode(0);
/* 407:453 */     fChoose_.setBounds(100, 100, 200, 20);
/* 408:454 */     fChoose_.setVisible(true);
/* 409:455 */     fChoose_.setFileFilter(new FileFilter()
/* 410:    */     {
/* 411:    */       public boolean accept(File f)
/* 412:    */       {
/* 413:459 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".pcg"))) {
/* 414:460 */           return true;
/* 415:    */         }
/* 416:462 */         return false;
/* 417:    */       }
/* 418:    */       
/* 419:    */       public String getDescription()
/* 420:    */       {
/* 421:468 */         return ".pcg";
/* 422:    */       }
/* 423:    */     });
/* 424:472 */     if (fChoose_.showOpenDialog(getParent()) == 0) {
/* 425:    */       try
/* 426:    */       {
/* 427:474 */         BufferedReader reader = readTxt(fChoose_.getSelectedFile().getCanonicalPath());
/* 428:475 */         loadConfig(reader);
/* 429:    */       }
/* 430:    */       catch (IOException e1)
/* 431:    */       {
/* 432:479 */         e1.printStackTrace();
/* 433:    */       }
/* 434:    */     }
/* 435:482 */     redo();
/* 436:    */   }
/* 437:    */   
/* 438:    */   private void addUserPath()
/* 439:    */   {
/* 440:485 */     JFileChooser fChoose_ = new JFileChooser(this.basePath_ + File.separator + "userPaths");
/* 441:486 */     fChoose_.setFileSelectionMode(0);
/* 442:487 */     fChoose_.setBounds(100, 100, 200, 20);
/* 443:488 */     fChoose_.setVisible(true);
/* 444:489 */     fChoose_.setFileFilter(new FileFilter()
/* 445:    */     {
/* 446:    */       public boolean accept(File f)
/* 447:    */       {
/* 448:493 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".pwm"))) {
/* 449:494 */           return true;
/* 450:    */         }
/* 451:496 */         return false;
/* 452:    */       }
/* 453:    */       
/* 454:    */       public String getDescription()
/* 455:    */       {
/* 456:502 */         return ".pwm";
/* 457:    */       }
/* 458:    */     });
/* 459:506 */     if (fChoose_.showOpenDialog(getParent()) == 0)
/* 460:    */     {
/* 461:507 */       if (Project.userPathways == null) {
/* 462:508 */         Project.userPathways = new ArrayList();
/* 463:    */       }
/* 464:    */       try
/* 465:    */       {
/* 466:511 */         Project.userPathways.add(fChoose_.getSelectedFile().getCanonicalPath());
/* 467:512 */         Prog.DataProcessor.newUserData = true;
/* 468:513 */         Project.dataChanged = true;
/* 469:    */       }
/* 470:    */       catch (IOException e)
/* 471:    */       {
/* 472:516 */         e.printStackTrace();
/* 473:    */       }
/* 474:    */     }
/* 475:521 */     redo();
/* 476:    */   }
/* 477:    */   
/* 478:    */   private BufferedReader readTxt(String path)
/* 479:    */   {
/* 480:528 */     BufferedReader in = null;
/* 481:    */     try
/* 482:    */     {
/* 483:530 */       in = new BufferedReader(new FileReader(path));
/* 484:    */     }
/* 485:    */     catch (IOException e)
/* 486:    */     {
/* 487:532 */       e.printStackTrace();
/* 488:    */     }
/* 489:534 */     return in;
/* 490:    */   }
/* 491:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.PathwaySelectP

 * JD-Core Version:    0.7.0.1

 */