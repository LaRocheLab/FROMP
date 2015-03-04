/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.EcNr;
/*   4:    */ import Objects.EcSampleStats;
/*   5:    */ import Objects.PathwayWithEc;
/*   6:    */ import Objects.Project;
/*   7:    */ import Objects.Sample;
/*   8:    */ import java.awt.BorderLayout;
/*   9:    */ import java.awt.Color;
/*  10:    */ import java.awt.Component;
/*  11:    */ import java.awt.Dimension;
/*  12:    */ import java.awt.Graphics;
/*  13:    */ import java.awt.event.ActionEvent;
/*  14:    */ import java.awt.event.ActionListener;
/*  15:    */ import java.awt.image.BufferedImage;
/*  16:    */ import java.io.BufferedWriter;
/*  17:    */ import java.io.File;
/*  18:    */ import java.io.FileWriter;
/*  19:    */ import java.io.IOException;
/*  20:    */ import java.io.PrintStream;
/*  21:    */ import java.util.ArrayList;
/*  22:    */ import javax.imageio.ImageIO;
/*  23:    */ import javax.swing.ImageIcon;
/*  24:    */ import javax.swing.JButton;
/*  25:    */ import javax.swing.JCheckBox;
/*  26:    */ import javax.swing.JFileChooser;
/*  27:    */ import javax.swing.JFrame;
/*  28:    */ import javax.swing.JLabel;
/*  29:    */ import javax.swing.JPanel;
/*  30:    */ import javax.swing.JScrollPane;
/*  31:    */ import javax.swing.filechooser.FileFilter;
/*  32:    */ 
/*  33:    */ public class PathwayMapFrame
/*  34:    */   extends JFrame
/*  35:    */ {
/*  36:    */   private static final long serialVersionUID = 1L;
/*  37:    */   final Sample samp_;
/*  38:    */   final PathwayWithEc path_;
/*  39:    */   BufferedImage pathMap_;
/*  40:    */   BufferedImage origPathMap_;
/*  41:    */   JLabel image_;
/*  42:    */   JLabel label_;
/*  43:    */   final String workpath_;
/*  44: 51 */   boolean resized = false;
/*  45:    */   JButton butt;
/*  46:    */   int oldwidth;
/*  47:    */   int oldheight;
/*  48:    */   JPanel ecAmounts;
/*  49:    */   JLabel amount_;
/*  50:    */   JButton export_;
/*  51:    */   JButton exportValues_;
/*  52:    */   int xWidth;
/*  53:    */   int yWidth;
/*  54:    */   ArrayList<Sample> samples_;
/*  55:    */   JCheckBox showBars_;
/*  56:    */   JCheckBox showNumbers_;
/*  57:    */   Graphics g_;
/*  58:    */   int maxEcAmount;
/*  59:    */   private JScrollPane showJPanel_;
/*  60:    */   private JPanel displayP_;
/*  61:    */   
/*  62:    */   public PathwayMapFrame(Sample samp, PathwayWithEc path, String workpath)
/*  63:    */   {
/*  64: 80 */     System.out.println("unmatched pathmap");
/*  65: 81 */     int colDist = 20;
/*  66: 82 */     this.workpath_ = workpath;
/*  67: 83 */     this.samp_ = samp;
/*  68: 84 */     this.path_ = path;
/*  69:    */     
/*  70: 86 */     this.maxEcAmount = maxAmount(this.path_);
/*  71:    */     
/*  72: 88 */     setTitle(this.samp_.name_ + "   |   " + this.path_.id_ + "   |   " + this.path_.name_);
/*  73: 89 */     this.xWidth = (this.maxEcAmount + 200);
/*  74: 90 */     this.yWidth = (path.ecNrs_.size() * colDist);
/*  75: 91 */     setBounds(50, 50, 800, 600);
/*  76: 92 */     this.oldwidth = (getWidth() - 50);
/*  77: 93 */     this.oldheight = getHeight();
/*  78: 94 */     setLayout(new BorderLayout());
/*  79: 95 */     setVisible(true);
/*  80:    */     
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:    */ 
/*  86:102 */     this.displayP_ = new JPanel();
/*  87:103 */     this.displayP_.setLocation(0, 0);
/*  88:104 */     this.displayP_.setPreferredSize(new Dimension(this.xWidth, this.yWidth));
/*  89:105 */     this.displayP_.setSize(getPreferredSize());
/*  90:106 */     this.displayP_.setBackground(Project.getBackColor_());
/*  91:107 */     this.displayP_.setVisible(true);
/*  92:108 */     this.displayP_.setLayout(null);
/*  93:    */     
/*  94:110 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/*  95:111 */     this.showJPanel_.setVisible(true);
/*  96:112 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/*  97:113 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/*  98:    */     
/*  99:115 */     add("Center", this.showJPanel_);
/* 100:    */     
/* 101:    */ 
/* 102:118 */     this.exportValues_ = new JButton("Export Values");
/* 103:119 */     this.exportValues_.setBounds(this.displayP_.getWidth() - 150, colDist * this.path_.ecNrs_.size() + 80, 150, 30);
/* 104:120 */     this.exportValues_.setLayout(null);
/* 105:121 */     this.exportValues_.addActionListener(new ActionListener()
/* 106:    */     {
/* 107:    */       public void actionPerformed(ActionEvent e)
/* 108:    */       {
/* 109:126 */         PathwayMapFrame.this.exportVals();
/* 110:    */       }
/* 111:129 */     });
/* 112:130 */     this.displayP_.add(this.exportValues_);
/* 113:    */     
/* 114:132 */     this.ecAmounts = new JPanel();
/* 115:133 */     this.ecAmounts.setLayout(null);
/* 116:134 */     this.ecAmounts.setVisible(true);
/* 117:135 */     this.ecAmounts.setBounds(0, 0, this.xWidth, this.yWidth);
/* 118:136 */     this.ecAmounts.setBackground(Color.gray);
/* 119:    */     
/* 120:138 */     this.path_.sortEcs();
/* 121:    */     
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:143 */     int numEcs = 0;
/* 126:145 */     for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 127:    */     {
/* 128:146 */       if (((EcNr)this.path_.ecNrs_.get(ecCnt)).unique_) {
/* 129:147 */         this.label_ = new JLabel(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + " *");
/* 130:    */       } else {
/* 131:150 */         this.label_ = new JLabel(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_);
/* 132:    */       }
/* 133:152 */       this.label_.setLayout(null);
/* 134:153 */       this.label_.setVisible(true);
/* 135:154 */       this.label_.setBounds(10, colDist * ecCnt, 100, colDist);
/* 136:155 */       this.ecAmounts.add(this.label_);
/* 137:157 */       if (this.samp_.singleSample_)
/* 138:    */       {
/* 139:158 */         this.amount_ = new JLabel(String.valueOf(((EcNr)this.path_.ecNrs_.get(ecCnt)).amount_));
/* 140:159 */         this.amount_.setLayout(null);
/* 141:160 */         this.amount_.setVisible(true);
/* 142:161 */         this.amount_.setOpaque(true);
/* 143:162 */         this.amount_.setBounds(120, colDist * ecCnt + 1, ((EcNr)this.path_.ecNrs_.get(ecCnt)).amount_, colDist - 2);
/* 144:163 */         this.amount_.setBackground(this.samp_.sampleCol_);
/* 145:164 */         this.ecAmounts.add(this.amount_);
/* 146:    */       }
/* 147:    */       else
/* 148:    */       {
/* 149:167 */         int step = 0;
/* 150:168 */         ArrayList<EcSampleStats> tmpStats = ((EcNr)this.path_.ecNrs_.get(ecCnt)).stats_;
/* 151:169 */         for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++)
/* 152:    */         {
/* 153:170 */           Color col = ((EcSampleStats)tmpStats.get(stsCnt)).col_;
/* 154:    */           
/* 155:172 */           this.amount_ = new JLabel(String.valueOf(((EcSampleStats)tmpStats.get(stsCnt)).amount_));
/* 156:173 */           this.amount_.setLayout(null);
/* 157:174 */           this.amount_.setVisible(true);
/* 158:175 */           this.amount_.setOpaque(true);
/* 159:176 */           this.amount_.setBounds(120 + step, colDist * ecCnt + 1, ((EcSampleStats)tmpStats.get(stsCnt)).amount_, colDist - 2);
/* 160:177 */           this.amount_.setBackground(col);
/* 161:178 */           this.ecAmounts.add(this.amount_);
/* 162:179 */           step += ((EcSampleStats)tmpStats.get(stsCnt)).amount_;
/* 163:    */         }
/* 164:    */       }
/* 165:184 */       numEcs = ecCnt;
/* 166:    */     }
/* 167:186 */     for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 168:    */     {
/* 169:187 */       if (((EcNr)this.path_.ecNrs_.get(ecCnt)).unique_) {
/* 170:188 */         this.label_ = new JLabel(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + " *");
/* 171:    */       } else {
/* 172:191 */         this.label_ = new JLabel(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_);
/* 173:    */       }
/* 174:193 */       this.label_.setLayout(null);
/* 175:194 */       this.label_.setVisible(true);
/* 176:195 */       this.label_.setBounds(10, colDist * (ecCnt + numEcs + 2), 100, colDist);
/* 177:196 */       this.ecAmounts.add(this.label_);
/* 178:198 */       if (this.samp_.singleSample_)
/* 179:    */       {
/* 180:199 */         this.amount_ = new JLabel(String.valueOf(((EcNr)this.path_.ecNrs_.get(ecCnt)).amount_));
/* 181:200 */         this.amount_.setLayout(null);
/* 182:201 */         this.amount_.setVisible(true);
/* 183:202 */         this.amount_.setOpaque(true);
/* 184:203 */         this.amount_.setBounds(120, colDist * (ecCnt + numEcs + 2), 20, colDist - 2);
/* 185:204 */         this.amount_.setForeground(this.samp_.sampleCol_);
/* 186:205 */         this.amount_.setBackground(Color.white);
/* 187:206 */         this.ecAmounts.add(this.amount_);
/* 188:    */       }
/* 189:    */       else
/* 190:    */       {
/* 191:209 */         int step = 0;
/* 192:210 */         ArrayList<EcSampleStats> tmpStats = ((EcNr)this.path_.ecNrs_.get(ecCnt)).stats_;
/* 193:211 */         for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++)
/* 194:    */         {
/* 195:212 */           Color col = ((EcSampleStats)tmpStats.get(stsCnt)).col_;
/* 196:213 */           this.amount_ = new JLabel(String.valueOf(((EcSampleStats)tmpStats.get(stsCnt)).amount_));
/* 197:214 */           this.amount_.setLayout(null);
/* 198:215 */           this.amount_.setVisible(true);
/* 199:216 */           this.amount_.setOpaque(true);
/* 200:217 */           this.amount_.setBounds(120 + 20 * step, colDist * (ecCnt + numEcs + 2), 20, colDist - 2);
/* 201:218 */           this.amount_.setBackground(Color.white);
/* 202:219 */           this.amount_.setForeground(col);
/* 203:220 */           this.ecAmounts.add(this.amount_);
/* 204:221 */           step++;
/* 205:    */         }
/* 206:    */       }
/* 207:    */     }
/* 208:225 */     this.displayP_.add(this.ecAmounts);
/* 209:226 */     invalidate();
/* 210:227 */     validate();
/* 211:228 */     repaint();
/* 212:    */   }
/* 213:    */   
/* 214:    */   public PathwayMapFrame(ArrayList<Sample> samples, Sample samp, PathwayWithEc path, BufferedImage pathMap, String workpath)
/* 215:    */   {
/* 216:232 */     System.out.println(path.id_ + " " + path.ecNrs_.size());
/* 217:233 */     this.samples_ = samples;
/* 218:234 */     this.workpath_ = workpath;
/* 219:235 */     this.samp_ = samp;
/* 220:236 */     this.path_ = path;
/* 221:237 */     this.maxEcAmount = maxAmount(this.path_);
/* 222:    */     
/* 223:239 */     this.pathMap_ = new BufferedImage(pathMap.getWidth() + 1000, pathMap.getHeight() + 600, 1);
/* 224:240 */     this.origPathMap_ = pathMap;
/* 225:241 */     setTitle(this.samp_.name_ + "   |   " + this.path_.id_ + "   |   " + this.path_.name_);
/* 226:242 */     this.xWidth = 1000;
/* 227:243 */     this.yWidth = (path.ecNrs_.size() * 10);
/* 228:244 */     setBounds(50, 50, 800, 600);
/* 229:245 */     this.oldwidth = (getWidth() - 50);
/* 230:246 */     this.oldheight = getHeight();
/* 231:247 */     setLayout(new BorderLayout());
/* 232:248 */     setVisible(true);
/* 233:    */     
/* 234:    */ 
/* 235:    */ 
/* 236:    */ 
/* 237:    */ 
/* 238:    */ 
/* 239:255 */     this.displayP_ = new JPanel();
/* 240:256 */     this.displayP_.setLocation(0, 0);
/* 241:257 */     this.displayP_.setPreferredSize(new Dimension(this.xWidth, this.yWidth));
/* 242:258 */     this.displayP_.setSize(getPreferredSize());
/* 243:259 */     this.displayP_.setBackground(Project.getBackColor_());
/* 244:260 */     this.displayP_.setVisible(true);
/* 245:261 */     this.displayP_.setLayout(null);
/* 246:    */     
/* 247:263 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/* 248:264 */     this.showJPanel_.setVisible(true);
/* 249:265 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/* 250:266 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/* 251:    */     
/* 252:268 */     add("Center", this.showJPanel_);
/* 253:    */     
/* 254:270 */     this.showBars_ = new JCheckBox("show barcharts");
/* 255:271 */     this.showBars_.setBounds(320, 0, 200, 12);
/* 256:272 */     this.showBars_.setSelected(true);
/* 257:273 */     this.showBars_.setBackground(Project.getBackColor_());
/* 258:274 */     this.showBars_.addActionListener(new ActionListener()
/* 259:    */     {
/* 260:    */       public void actionPerformed(ActionEvent e)
/* 261:    */       {
/* 262:279 */         PathwayMapFrame.this.drawImage();
/* 263:    */       }
/* 264:282 */     });
/* 265:283 */     this.displayP_.add(this.showBars_);
/* 266:    */     
/* 267:285 */     this.showNumbers_ = new JCheckBox("show matrix");
/* 268:286 */     this.showNumbers_.setBounds(320, 13, 200, 12);
/* 269:287 */     this.showNumbers_.setSelected(true);
/* 270:288 */     this.showNumbers_.setBackground(Project.getBackColor_());
/* 271:289 */     this.showNumbers_.addActionListener(new ActionListener()
/* 272:    */     {
/* 273:    */       public void actionPerformed(ActionEvent e)
/* 274:    */       {
/* 275:294 */         PathwayMapFrame.this.drawImage();
/* 276:    */       }
/* 277:297 */     });
/* 278:298 */     this.displayP_.add(this.showNumbers_);
/* 279:    */     
/* 280:    */ 
/* 281:301 */     this.ecAmounts = new JPanel();
/* 282:302 */     this.ecAmounts.setLayout(null);
/* 283:303 */     this.ecAmounts.setVisible(true);
/* 284:304 */     this.ecAmounts.setBounds(this.pathMap_.getWidth(), 0, this.maxEcAmount + 200, this.yWidth);
/* 285:305 */     this.ecAmounts.setBackground(Color.gray);
/* 286:    */     
/* 287:307 */     this.path_.sortEcs();
/* 288:308 */     this.export_ = new JButton("Export Picture");
/* 289:309 */     this.export_.setBounds(10, 0, 150, 25);
/* 290:310 */     this.export_.setLayout(null);
/* 291:311 */     this.export_.addActionListener(new ActionListener()
/* 292:    */     {
/* 293:    */       public void actionPerformed(ActionEvent e)
/* 294:    */       {
/* 295:316 */         PathwayMapFrame.this.export();
/* 296:    */       }
/* 297:319 */     });
/* 298:320 */     this.displayP_.add(this.export_);
/* 299:    */     
/* 300:    */ 
/* 301:323 */     this.exportValues_ = new JButton("Export Values");
/* 302:324 */     this.exportValues_.setBounds(160, 0, 150, 25);
/* 303:325 */     this.exportValues_.setLayout(null);
/* 304:326 */     this.exportValues_.addActionListener(new ActionListener()
/* 305:    */     {
/* 306:    */       public void actionPerformed(ActionEvent e)
/* 307:    */       {
/* 308:331 */         PathwayMapFrame.this.exportVals();
/* 309:    */       }
/* 310:334 */     });
/* 311:335 */     this.displayP_.add(this.exportValues_);
/* 312:    */     
/* 313:337 */     drawImage();
/* 314:    */   }
/* 315:    */   
/* 316:    */   public PathwayMapFrame(ArrayList<Sample> samples, Sample samp, PathwayWithEc path)
/* 317:    */   {
/* 318:340 */     this.workpath_ = "";
/* 319:341 */     this.samp_ = samp;
/* 320:342 */     this.path_ = path;
/* 321:343 */     this.maxEcAmount = maxAmount(this.path_);
/* 322:344 */     this.xWidth = 1000;
/* 323:345 */     this.yWidth = (path.ecNrs_.size() * 10);
/* 324:    */   }
/* 325:    */   
/* 326:    */   private void drawImage()
/* 327:    */   {
/* 328:348 */     System.out.println("draw Image");
/* 329:349 */     Color col = Color.black;
/* 330:350 */     this.xWidth = maxAmount(this.path_);
/* 331:351 */     int lineHeight = 25;
/* 332:352 */     int height = 100 + (this.path_.ecNrs_.size() * lineHeight * 2 + Project.samples_.size() * lineHeight);
/* 333:353 */     if (height < this.origPathMap_.getHeight() + 200) {
/* 334:354 */       height = this.origPathMap_.getHeight() + 200;
/* 335:    */     }
/* 336:356 */     System.out.println(this.xWidth);
/* 337:357 */     if ((this.showBars_.isSelected()) || (this.showNumbers_.isSelected()))
/* 338:    */     {
/* 339:358 */       System.out.println("showbars");
/* 340:359 */       this.pathMap_ = new BufferedImage(this.origPathMap_.getWidth() + this.xWidth + 1000, height, 1);
/* 341:    */     }
/* 342:    */     else
/* 343:    */     {
/* 344:362 */       this.pathMap_ = new BufferedImage(this.origPathMap_.getWidth(), this.origPathMap_.getHeight(), 1);
/* 345:    */     }
/* 346:364 */     if (this.yWidth < this.pathMap_.getHeight()) {
/* 347:365 */       this.yWidth = this.pathMap_.getHeight();
/* 348:    */     }
/* 349:367 */     this.xWidth = this.pathMap_.getWidth();
/* 350:368 */     this.displayP_.setPreferredSize(new Dimension(this.xWidth, this.yWidth));
/* 351:369 */     this.pathMap_.createGraphics();
/* 352:370 */     this.g_ = this.pathMap_.getGraphics();
/* 353:371 */     this.g_.setColor(Color.white);
/* 354:372 */     this.g_.fillRect(0, 0, this.pathMap_.getWidth(), this.pathMap_.getHeight());
/* 355:373 */     this.g_.drawImage(this.origPathMap_, 0, 0, null);
/* 356:375 */     for (int i = 0; i < this.displayP_.getComponentCount(); i++) {
/* 357:376 */       if (this.displayP_.getComponent(i).getName() != null) {
/* 358:379 */         if (this.displayP_.getComponent(i).getName().contentEquals("Image")) {
/* 359:380 */           this.displayP_.remove(i);
/* 360:    */         }
/* 361:    */       }
/* 362:    */     }
/* 363:384 */     ImageIcon icon = new ImageIcon(this.pathMap_);
/* 364:385 */     this.image_ = new JLabel(icon);
/* 365:386 */     this.image_.setLayout(null);
/* 366:387 */     this.image_.setName("Image");
/* 367:388 */     this.image_.setBounds(0, 0, this.pathMap_.getWidth(), this.pathMap_.getHeight() + 100);
/* 368:    */     
/* 369:390 */     this.image_.setVisible(true);
/* 370:    */     
/* 371:392 */     int colDist = 20;
/* 372:    */     
/* 373:394 */     int numEcs = 1;
/* 374:396 */     if (this.samp_.singleSample_)
/* 375:    */     {
/* 376:397 */       this.g_.setColor(this.samp_.sampleCol_);
/* 377:398 */       this.g_.drawString("Sample: " + this.samp_.name_, this.origPathMap_.getWidth() + 20, 15 + colDist * numEcs);
/* 378:399 */       numEcs++;
/* 379:    */     }
/* 380:    */     else
/* 381:    */     {
/* 382:402 */       for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++)
/* 383:    */       {
/* 384:403 */         this.g_.setColor(((Sample)this.samples_.get(smpCnt)).sampleCol_);
/* 385:404 */         this.g_.drawString("Sample " + (smpCnt + 1) + ": " + ((Sample)this.samples_.get(smpCnt)).name_, this.origPathMap_.getWidth() + 20, 15 + colDist * numEcs);
/* 386:405 */         numEcs++;
/* 387:    */       }
/* 388:    */     }
/* 389:408 */     if (this.showBars_.isSelected()) {
/* 390:409 */       for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 391:    */       {
/* 392:410 */         numEcs++;
/* 393:411 */         this.g_.setColor(Color.black);
/* 394:412 */         if (((EcNr)this.path_.ecNrs_.get(ecCnt)).unique_) {
/* 395:413 */           this.g_.drawString(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + "*:", this.origPathMap_.getWidth() + 20, 15 + (colDist * numEcs + 1));
/* 396:    */         } else {
/* 397:416 */           this.g_.drawString(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + ":", this.origPathMap_.getWidth() + 20, 15 + (colDist * numEcs + 1));
/* 398:    */         }
/* 399:419 */         if (this.samp_.singleSample_)
/* 400:    */         {
/* 401:420 */           this.g_.setColor(this.samp_.sampleCol_);
/* 402:421 */           this.g_.fillRect(this.origPathMap_.getWidth() + 120, colDist * numEcs + 1, ((EcNr)this.path_.ecNrs_.get(ecCnt)).amount_, colDist - 2);
/* 403:    */         }
/* 404:    */         else
/* 405:    */         {
/* 406:424 */           int step = 0;
/* 407:425 */           ArrayList<EcSampleStats> tmpStats = ((EcNr)this.path_.ecNrs_.get(ecCnt)).stats_;
/* 408:426 */           for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++)
/* 409:    */           {
/* 410:427 */             col = ((EcSampleStats)tmpStats.get(stsCnt)).col_;
/* 411:    */             
/* 412:429 */             this.g_.setColor(col);
/* 413:430 */             this.g_.fillRect(this.origPathMap_.getWidth() + 120 + step, colDist * numEcs + 1, ((EcSampleStats)tmpStats.get(stsCnt)).amount_, colDist - 2);
/* 414:    */             
/* 415:432 */             step += ((EcSampleStats)tmpStats.get(stsCnt)).amount_;
/* 416:    */           }
/* 417:    */         }
/* 418:    */       }
/* 419:    */     }
/* 420:438 */     if (this.showNumbers_.isSelected()) {
/* 421:440 */       for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 422:    */       {
/* 423:441 */         this.g_.setColor(Color.black);
/* 424:442 */         numEcs++;
/* 425:443 */         if (((EcNr)this.path_.ecNrs_.get(ecCnt)).unique_) {
/* 426:444 */           this.g_.drawString(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + "*:", this.origPathMap_.getWidth() + 20, 15 + colDist * (numEcs + 2));
/* 427:    */         } else {
/* 428:447 */           this.g_.drawString(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + ":", this.origPathMap_.getWidth() + 20, 15 + colDist * (numEcs + 2));
/* 429:    */         }
/* 430:450 */         if (this.samp_.singleSample_)
/* 431:    */         {
/* 432:452 */           this.g_.drawString(String.valueOf(((EcNr)this.path_.ecNrs_.get(ecCnt)).amount_), this.origPathMap_.getWidth() + 80, 15 + colDist * (numEcs + 2));
/* 433:    */         }
/* 434:    */         else
/* 435:    */         {
/* 436:456 */           ArrayList<EcSampleStats> tmpStats = ((EcNr)this.path_.ecNrs_.get(ecCnt)).stats_;
/* 437:457 */           for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++)
/* 438:    */           {
/* 439:458 */             col = ((EcSampleStats)tmpStats.get(stsCnt)).col_;
/* 440:459 */             this.g_.setColor(col);
/* 441:460 */             this.g_.drawString(String.valueOf(((EcSampleStats)tmpStats.get(stsCnt)).amount_), this.origPathMap_.getWidth() + 20 + 120 + 30 * ((EcSampleStats)tmpStats.get(stsCnt)).sampleNr_, 15 + colDist * (numEcs + 2));
/* 442:    */           }
/* 443:    */         }
/* 444:    */       }
/* 445:    */     }
/* 446:466 */     this.displayP_.add(this.ecAmounts);
/* 447:467 */     this.image_.setLocation(0, 25);
/* 448:468 */     this.displayP_.add(this.image_);
/* 449:    */     
/* 450:470 */     invalidate();
/* 451:471 */     validate();
/* 452:472 */     repaint();
/* 453:    */   }
/* 454:    */   
/* 455:    */   public BufferedImage drawImage(BufferedImage image)
/* 456:    */   {
/* 457:475 */     Color col = Color.black;
/* 458:476 */     this.xWidth = maxAmount(this.path_);
/* 459:477 */     int lineHeight = 25;
/* 460:478 */     int height = 100 + (this.path_.ecNrs_.size() * lineHeight * 2 + Project.samples_.size() * lineHeight);
/* 461:479 */     if (height < image.getHeight() + 200) {
/* 462:480 */       height = image.getHeight() + 200;
/* 463:    */     }
/* 464:483 */     this.pathMap_ = new BufferedImage(image.getWidth() + this.xWidth + 1000, height, 1);
/* 465:485 */     if (this.yWidth < this.pathMap_.getHeight()) {
/* 466:486 */       this.yWidth = this.pathMap_.getHeight();
/* 467:    */     }
/* 468:488 */     this.xWidth = this.pathMap_.getWidth();
/* 469:489 */     this.pathMap_.createGraphics();
/* 470:490 */     this.g_ = this.pathMap_.getGraphics();
/* 471:491 */     this.g_.setColor(Color.white);
/* 472:492 */     this.g_.fillRect(0, 0, this.pathMap_.getWidth(), this.pathMap_.getHeight());
/* 473:493 */     this.g_.drawImage(image, 0, 0, null);
/* 474:    */     
/* 475:495 */     int colDist = 20;
/* 476:    */     
/* 477:497 */     int numEcs = 1;
/* 478:500 */     if (this.samp_.singleSample_)
/* 479:    */     {
/* 480:501 */       this.g_.setColor(this.samp_.sampleCol_);
/* 481:502 */       this.g_.drawString("Sample: " + this.samp_.name_, image.getWidth() + 20, 15 + colDist * numEcs);
/* 482:503 */       numEcs++;
/* 483:    */     }
/* 484:    */     else
/* 485:    */     {
/* 486:506 */       for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++)
/* 487:    */       {
/* 488:507 */         this.g_.setColor(((Sample)Project.samples_.get(smpCnt)).sampleCol_);
/* 489:508 */         this.g_.drawString("Sample " + (smpCnt + 1) + ": " + ((Sample)Project.samples_.get(smpCnt)).name_, image.getWidth() + 20, 15 + colDist * numEcs);
/* 490:509 */         numEcs++;
/* 491:    */       }
/* 492:    */     }
/* 493:513 */     for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 494:    */     {
/* 495:514 */       this.g_.setColor(Color.black);
/* 496:515 */       numEcs++;
/* 497:516 */       if (((EcNr)this.path_.ecNrs_.get(ecCnt)).unique_) {
/* 498:517 */         this.g_.drawString(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + "*:", image.getWidth() + 20, 15 + (colDist * numEcs + 1));
/* 499:    */       } else {
/* 500:521 */         this.g_.drawString(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + ":", image.getWidth() + 20, 15 + (colDist * numEcs + 1));
/* 501:    */       }
/* 502:524 */       if (this.samp_.singleSample_)
/* 503:    */       {
/* 504:525 */         this.g_.setColor(this.samp_.sampleCol_);
/* 505:526 */         this.g_.fillRect(image.getWidth() + 120, colDist * numEcs + 1, ((EcNr)this.path_.ecNrs_.get(ecCnt)).amount_, colDist - 2);
/* 506:    */       }
/* 507:    */       else
/* 508:    */       {
/* 509:529 */         int step = 0;
/* 510:530 */         ArrayList<EcSampleStats> tmpStats = ((EcNr)this.path_.ecNrs_.get(ecCnt)).stats_;
/* 511:531 */         for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++)
/* 512:    */         {
/* 513:532 */           col = ((EcSampleStats)tmpStats.get(stsCnt)).col_;
/* 514:    */           
/* 515:534 */           this.g_.setColor(col);
/* 516:535 */           this.g_.fillRect(image.getWidth() + 120 + step, colDist * numEcs + 1, ((EcSampleStats)tmpStats.get(stsCnt)).amount_, colDist - 2);
/* 517:    */           
/* 518:537 */           step += ((EcSampleStats)tmpStats.get(stsCnt)).amount_;
/* 519:    */         }
/* 520:    */       }
/* 521:    */     }
/* 522:544 */     for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 523:    */     {
/* 524:545 */       numEcs++;
/* 525:546 */       this.g_.setColor(Color.black);
/* 526:547 */       if (((EcNr)this.path_.ecNrs_.get(ecCnt)).unique_) {
/* 527:548 */         this.g_.drawString(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + "*:", image.getWidth() + 20, 15 + colDist * (numEcs + 2));
/* 528:    */       } else {
/* 529:552 */         this.g_.drawString(((EcNr)this.path_.ecNrs_.get(ecCnt)).name_ + ":", image.getWidth() + 20, 15 + colDist * (numEcs + 2));
/* 530:    */       }
/* 531:556 */       if (this.samp_.singleSample_)
/* 532:    */       {
/* 533:557 */         this.g_.drawString(String.valueOf(((EcNr)this.path_.ecNrs_.get(ecCnt)).amount_), image.getWidth() + 80, 15 + colDist * (numEcs + 2));
/* 534:    */       }
/* 535:    */       else
/* 536:    */       {
/* 537:560 */         ArrayList<EcSampleStats> tmpStats = ((EcNr)this.path_.ecNrs_.get(ecCnt)).stats_;
/* 538:561 */         for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++)
/* 539:    */         {
/* 540:562 */           col = ((EcSampleStats)tmpStats.get(stsCnt)).col_;
/* 541:563 */           this.g_.setColor(col);
/* 542:564 */           this.g_.drawString(String.valueOf(((EcSampleStats)tmpStats.get(stsCnt)).amount_), image.getWidth() + 20 + 120 + 30 * ((EcSampleStats)tmpStats.get(stsCnt)).sampleNr_, 15 + colDist * (numEcs + 2));
/* 543:    */         }
/* 544:    */       }
/* 545:    */     }
/* 546:568 */     return this.pathMap_;
/* 547:    */   }
/* 548:    */   
/* 549:    */   private void export()
/* 550:    */   {
/* 551:571 */     JFileChooser fChoose_ = new JFileChooser(this.path_.id_ + ".png");
/* 552:572 */     fChoose_.setFileSelectionMode(0);
/* 553:573 */     fChoose_.setBounds(100, 100, 200, 20);
/* 554:574 */     fChoose_.setVisible(true);
/* 555:575 */     File file = new File(this.path_.id_ + ".png");
/* 556:576 */     fChoose_.setSelectedFile(file);
/* 557:577 */     fChoose_.setFileFilter(new FileFilter()
/* 558:    */     {
/* 559:    */       public boolean accept(File f)
/* 560:    */       {
/* 561:581 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".png"))) {
/* 562:582 */           return true;
/* 563:    */         }
/* 564:584 */         return false;
/* 565:    */       }
/* 566:    */       
/* 567:    */       public String getDescription()
/* 568:    */       {
/* 569:590 */         return ".png";
/* 570:    */       }
/* 571:    */     });
/* 572:594 */     if (fChoose_.showSaveDialog(getParent()) == 0)
/* 573:    */     {
/* 574:    */       try
/* 575:    */       {
/* 576:597 */         String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 577:598 */         exportPicTo(path);
/* 578:    */       }
/* 579:    */       catch (IOException e)
/* 580:    */       {
/* 581:601 */         e.printStackTrace();
/* 582:    */       }
/* 583:605 */       invalidate();
/* 584:606 */       validate();
/* 585:607 */       repaint();
/* 586:    */     }
/* 587:609 */     System.out.println("Save");
/* 588:    */   }
/* 589:    */   
/* 590:    */   public void exportPicTo(String path)
/* 591:    */   {
/* 592:    */     try
/* 593:    */     {
/* 594:613 */       if (!path.endsWith(".png")) {
/* 595:614 */         path = path + ".png";
/* 596:    */       }
/* 597:617 */       BufferedImage outimage = new BufferedImage(this.displayP_.getWidth(), this.displayP_.getHeight(), 1);
/* 598:618 */       this.displayP_.paint(outimage.createGraphics());
/* 599:619 */       ImageIO.write(this.pathMap_, "png", new File(path));
/* 600:    */     }
/* 601:    */     catch (IOException e1)
/* 602:    */     {
/* 603:623 */       e1.printStackTrace();
/* 604:    */     }
/* 605:    */   }
/* 606:    */   
/* 607:    */   public BufferedImage getPicture()
/* 608:    */   {
/* 609:627 */     return new BufferedImage(this.displayP_.getWidth(), this.displayP_.getHeight(), 1);
/* 610:    */   }
/* 611:    */   
/* 612:    */   private void exportVals()
/* 613:    */   {
/* 614:630 */     JFileChooser fChoose_ = new JFileChooser(this.path_.id_ + ".txt");
/* 615:631 */     fChoose_.setFileSelectionMode(0);
/* 616:632 */     fChoose_.setBounds(100, 100, 200, 20);
/* 617:633 */     fChoose_.setVisible(true);
/* 618:634 */     File file = new File(this.path_.id_ + ".txt");
/* 619:635 */     fChoose_.setSelectedFile(file);
/* 620:636 */     fChoose_.setFileFilter(new FileFilter()
/* 621:    */     {
/* 622:    */       public boolean accept(File f)
/* 623:    */       {
/* 624:640 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".txt"))) {
/* 625:641 */           return true;
/* 626:    */         }
/* 627:643 */         return false;
/* 628:    */       }
/* 629:    */       
/* 630:    */       public String getDescription()
/* 631:    */       {
/* 632:649 */         return ".txt";
/* 633:    */       }
/* 634:    */     });
/* 635:653 */     if (fChoose_.showSaveDialog(getParent()) == 0)
/* 636:    */     {
/* 637:    */       try
/* 638:    */       {
/* 639:655 */         String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 640:657 */         if (!path.endsWith(".txt")) {
/* 641:658 */           path = path + ".txt";
/* 642:    */         }
/* 643:661 */         BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 644:663 */         if (this.samp_.singleSample_)
/* 645:    */         {
/* 646:664 */           out.write("!! Sample:" + this.samp_.name_ + " Pathway:" + this.path_.id_ + "," + this.path_.name_);
/* 647:665 */           out.newLine();
/* 648:666 */           for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 649:    */           {
/* 650:667 */             EcNr ecTmp = (EcNr)this.path_.ecNrs_.get(ecCnt);
/* 651:668 */             out.write(ecTmp.name_ + "/t" + ecTmp.amount_);
/* 652:669 */             out.newLine();
/* 653:    */           }
/* 654:    */         }
/* 655:    */         else
/* 656:    */         {
/* 657:673 */           int maxStats = 0;
/* 658:674 */           int maxIndex = 0;
/* 659:675 */           ArrayList<Integer> smpNrs = new ArrayList<Integer>();
/* 660:676 */           out.write("!! Sample:" + this.samp_.name_ + " Pathway:" + this.path_.id_ + "," + this.path_.name_);
/* 661:677 */           out.newLine();
/* 662:679 */           for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 663:    */           {
/* 664:680 */             EcNr ecTmp = (EcNr)this.path_.ecNrs_.get(ecCnt);
/* 665:681 */             if (ecTmp.stats_.size() > maxStats)
/* 666:    */             {
/* 667:682 */               maxStats = ecTmp.stats_.size();
/* 668:683 */               maxIndex = ecCnt;
/* 669:    */             }
/* 670:    */           }
/* 671:686 */           for (int stsCnt = 0; stsCnt < maxStats; stsCnt++) {
/* 672:687 */             smpNrs.add(Integer.valueOf(((EcSampleStats)((EcNr)this.path_.ecNrs_.get(maxIndex)).stats_.get(stsCnt)).sampleNr_));
/* 673:    */           }
/* 674:689 */           out.write("EcNr");
/* 675:690 */           for (int nrCnt = 0; nrCnt < smpNrs.size(); nrCnt++) {
/* 676:691 */             out.write("," + smpNrs.get(nrCnt));
/* 677:    */           }
/* 678:693 */           for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++)
/* 679:    */           {
/* 680:694 */             EcNr ecTmp = (EcNr)this.path_.ecNrs_.get(ecCnt);
/* 681:695 */             out.write(ecTmp.name_);
/* 682:696 */             for (int stsCnt = 0; stsCnt < ecTmp.stats_.size()/*maxStats*/; stsCnt++) {
/* 683:697 */               for (int nrCnt = 0; nrCnt < smpNrs.size(); nrCnt++) {
/* 684:698 */                 if (((EcSampleStats)ecTmp.stats_.get(stsCnt)).sampleNr_ == ((Integer)smpNrs.get(nrCnt)).intValue()) {
/* 685:699 */                   out.write("," + ((EcSampleStats)ecTmp.stats_.get(stsCnt)).amount_);
/* 686:    */                 } else {
/* 687:702 */                   out.write(",0");
/* 688:    */                 }
/* 689:    */               }
/* 690:    */             }
/* 691:706 */             out.newLine();
/* 692:    */           }
/* 693:    */         }
/* 694:710 */         out.close();
/* 695:    */       }
/* 696:    */       catch (IOException e1)
/* 697:    */       {
/* 698:714 */         e1.printStackTrace();
/* 699:    */       }
/* 700:716 */       invalidate();
/* 701:717 */       validate();
/* 702:718 */       repaint();
/* 703:    */     }
/* 704:    */   }
/* 705:    */   
/* 706:    */   private int maxAmount(PathwayWithEc pw)
/* 707:    */   {
/* 708:723 */     int maxEcCnt = 0;
/* 709:724 */     for (int ecCnt = 0; ecCnt < pw.ecNrs_.size(); ecCnt++) {
/* 710:725 */       if (maxEcCnt < ((EcNr)pw.ecNrs_.get(ecCnt)).amount_) {
/* 711:726 */         maxEcCnt = ((EcNr)pw.ecNrs_.get(ecCnt)).amount_;
/* 712:    */       }
/* 713:    */     }
/* 714:729 */     return maxEcCnt;
/* 715:    */   }
/* 716:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.PathwayMapFrame

 * JD-Core Version:    0.7.0.1

 */