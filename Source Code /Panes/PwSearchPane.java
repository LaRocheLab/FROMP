/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.EcWithPathway;
/*   4:    */ import Objects.PathwayWithEc;
/*   5:    */ import Objects.Project;
/*   6:    */ import Objects.Sample;
/*   7:    */ import Prog.PathButt;
/*   8:    */ import java.awt.BorderLayout;
/*   9:    */ import java.awt.Color;
/*  10:    */ import java.awt.Dimension;
/*  11:    */ import java.awt.event.ActionEvent;
/*  12:    */ import java.awt.event.ActionListener;
/*  13:    */ import java.awt.event.MouseEvent;
/*  14:    */ import java.awt.event.MouseListener;
/*  15:    */ import java.io.PrintStream;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import javax.swing.JButton;
/*  18:    */ import javax.swing.JLabel;
/*  19:    */ import javax.swing.JPanel;
/*  20:    */ import javax.swing.JScrollPane;
/*  21:    */ import javax.swing.JTextField;
/*  22:    */ 
/*  23:    */ public class PwSearchPane
/*  24:    */   extends JPanel
/*  25:    */ {
/*  26:    */   private static final long serialVersionUID = 1L;
/*  27:    */   private ArrayList<Sample> samples_;
/*  28:    */   private Sample overSample_;
/*  29:    */   private ArrayList<PathwayWithEc> pathways_;
/*  30:    */   private JTextField searchfield;
/*  31:    */   private JTextField searchfield2;
/*  32:    */   private ArrayList<Integer> pathwIndexes_;
/*  33:    */   private ArrayList<Integer> ecIndexes_;
/*  34:    */   private Project proj_;
/*  35:    */   private int line_;
/*  36: 41 */   private int linDis = 40;
/*  37: 42 */   private int colDis = 250;
/*  38: 43 */   private int xDist = 250;
/*  39:    */   private JPanel optionsPanel_;
/*  40:    */   private JPanel displayP_;
/*  41:    */   private JScrollPane showJPanel_;
/*  42:    */   private JLabel mouseOverDisp;
/*  43:    */   private JPanel mouseOverP;
/*  44:    */   
/*  45:    */   public PwSearchPane(Project proj, ArrayList<Sample> samples, Sample overallSample, Dimension dim)
/*  46:    */   {
/*  47: 52 */     this.samples_ = new ArrayList();
/*  48: 53 */     this.proj_ = proj;
/*  49: 54 */     for (int smpCnt = 0; smpCnt < samples.size(); smpCnt++) {
/*  50: 55 */       this.samples_.add((Sample)samples.get(smpCnt));
/*  51:    */     }
/*  52: 57 */     this.overSample_ = overallSample;
/*  53: 58 */     setVisible(true);
/*  54: 59 */     setLayout(new BorderLayout());
/*  55: 60 */     setBackground(Color.orange);
/*  56: 61 */     setSize(dim);
/*  57:    */     
/*  58:    */ 
/*  59: 64 */     findPw();
/*  60:    */     
/*  61: 66 */     invalidate();
/*  62: 67 */     validate();
/*  63: 68 */     repaint();
/*  64:    */   }
/*  65:    */   
/*  66:    */   private void initMainPanels()
/*  67:    */   {
/*  68: 71 */     this.optionsPanel_ = new JPanel();
/*  69: 72 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth(), 100));
/*  70: 73 */     this.optionsPanel_.setLocation(0, 0);
/*  71: 74 */     this.optionsPanel_.setBackground(Project.getBackColor_().darker());
/*  72: 75 */     this.optionsPanel_.setVisible(true);
/*  73: 76 */     this.optionsPanel_.setLayout(null);
/*  74: 77 */     add(this.optionsPanel_, "First");
/*  75:    */     
/*  76: 79 */     this.displayP_ = new JPanel();
/*  77: 80 */     this.displayP_.setLocation(0, 0);
/*  78: 81 */     this.displayP_.setPreferredSize(new Dimension(getWidth() + Project.samples_.size() * 300, 12000));
/*  79: 82 */     this.displayP_.setSize(getPreferredSize());
/*  80: 83 */     this.displayP_.setBackground(Project.getBackColor_());
/*  81: 84 */     this.displayP_.setVisible(true);
/*  82: 85 */     this.displayP_.setLayout(null);
/*  83:    */     
/*  84: 87 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/*  85: 88 */     this.showJPanel_.setVisible(true);
/*  86: 89 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/*  87: 90 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/*  88:    */     
/*  89: 92 */     add("Center", this.showJPanel_);
/*  90:    */   }
/*  91:    */   
/*  92:    */   private void findPw()
/*  93:    */   {
/*  94: 95 */     removeAll();
/*  95: 96 */     initMainPanels();
/*  96: 97 */     this.pathwIndexes_ = new ArrayList();
/*  97: 98 */     this.line_ = 0;
/*  98:    */     
/*  99:100 */     this.searchfield = new JTextField("Enter Pathway-ID");
/* 100:101 */     this.searchfield.setBounds(100, 200, 200, 25);
/* 101:102 */     this.searchfield.setVisible(true);
/* 102:103 */     this.searchfield.addActionListener(new ActionListener()
/* 103:    */     {
/* 104:    */       public void actionPerformed(ActionEvent e)
/* 105:    */       {
/* 106:108 */         String tmp = PwSearchPane.this.searchfield.getText();
/* 107:109 */         PwSearchPane.this.searchPw(tmp);
/* 108:110 */         if (PwSearchPane.this.pathwIndexes_.size() > 0) {
/* 109:111 */           PwSearchPane.this.showAllPaths();
/* 110:    */         }
/* 111:113 */         PwSearchPane.this.invalidate();
/* 112:114 */         PwSearchPane.this.validate();
/* 113:115 */         PwSearchPane.this.repaint();
/* 114:    */       }
/* 115:118 */     });
/* 116:119 */     this.displayP_.add(this.searchfield);
/* 117:    */     
/* 118:121 */     JButton button_ = new JButton();
/* 119:122 */     button_.setBounds(325, 200, 100, 25);
/* 120:123 */     button_.setVisible(true);
/* 121:124 */     button_.setText("search");
/* 122:125 */     button_.addActionListener(new ActionListener()
/* 123:    */     {
/* 124:    */       public void actionPerformed(ActionEvent e)
/* 125:    */       {
/* 126:130 */         String tmp = PwSearchPane.this.searchfield.getText();
/* 127:131 */         PwSearchPane.this.searchPw(tmp);
/* 128:132 */         if (PwSearchPane.this.pathwIndexes_.size() > 0) {
/* 129:133 */           PwSearchPane.this.showAllPaths();
/* 130:    */         }
/* 131:135 */         PwSearchPane.this.invalidate();
/* 132:136 */         PwSearchPane.this.validate();
/* 133:137 */         PwSearchPane.this.repaint();
/* 134:    */       }
/* 135:140 */     });
/* 136:141 */     this.displayP_.add(button_);
/* 137:    */     
/* 138:143 */     this.pathwIndexes_ = new ArrayList();
/* 139:144 */     this.line_ = 0;
/* 140:    */     
/* 141:146 */     this.searchfield2 = new JTextField("Enter Ec-ID");
/* 142:147 */     this.searchfield2.setBounds(500, 200, 200, 25);
/* 143:148 */     this.searchfield2.setVisible(true);
/* 144:149 */     this.searchfield2.addActionListener(new ActionListener()
/* 145:    */     {
/* 146:    */       public void actionPerformed(ActionEvent e)
/* 147:    */       {
/* 148:154 */         String tmp = PwSearchPane.this.searchfield2.getText();
/* 149:155 */         PwSearchPane.this.searchEc(tmp);
/* 150:156 */         if (PwSearchPane.this.ecIndexes_.size() > 0) {
/* 151:157 */           PwSearchPane.this.showEcs();
/* 152:    */         }
/* 153:159 */         PwSearchPane.this.invalidate();
/* 154:160 */         PwSearchPane.this.validate();
/* 155:161 */         PwSearchPane.this.repaint();
/* 156:    */       }
/* 157:164 */     });
/* 158:165 */     this.displayP_.add(this.searchfield2);
/* 159:    */     
/* 160:167 */     JButton button2_ = new JButton();
/* 161:168 */     button2_.setBounds(725, 200, 100, 25);
/* 162:169 */     button2_.setVisible(true);
/* 163:170 */     button2_.setText("search");
/* 164:171 */     button2_.addActionListener(new ActionListener()
/* 165:    */     {
/* 166:    */       public void actionPerformed(ActionEvent e)
/* 167:    */       {
/* 168:176 */         String tmp = PwSearchPane.this.searchfield2.getText();
/* 169:    */         
/* 170:178 */         PwSearchPane.this.searchEc(tmp);
/* 171:180 */         if (PwSearchPane.this.ecIndexes_.size() > 0) {
/* 172:181 */           PwSearchPane.this.showEcs();
/* 173:    */         }
/* 174:184 */         PwSearchPane.this.invalidate();
/* 175:185 */         PwSearchPane.this.validate();
/* 176:186 */         PwSearchPane.this.repaint();
/* 177:    */       }
/* 178:189 */     });
/* 179:190 */     this.displayP_.add(button2_);
/* 180:    */   }
/* 181:    */   
/* 182:    */   private void searchPw(String in)
/* 183:    */   {
/* 184:194 */     String lowcase = in.replaceFirst(in.substring(0, 1), in.substring(0, 1).toUpperCase());
/* 185:195 */     String upCase = in.replaceFirst(in.substring(0, 1), in.substring(0, 1).toLowerCase());
/* 186:196 */     this.pathwIndexes_ = new ArrayList();
/* 187:197 */     for (int pwCnt = 0; pwCnt < this.overSample_.pathways_.size(); pwCnt++) {
/* 188:198 */       if ((((PathwayWithEc)this.overSample_.pathways_.get(pwCnt)).id_.contains(lowcase)) || (((PathwayWithEc)this.overSample_.pathways_.get(pwCnt)).id_.contains(upCase))) {
/* 189:199 */         this.pathwIndexes_.add(Integer.valueOf(pwCnt));
/* 190:    */       }
/* 191:    */     }
/* 192:202 */     for (int pwCnt = 0; pwCnt < this.overSample_.pathways_.size(); pwCnt++) {
/* 193:203 */       if ((((PathwayWithEc)this.overSample_.pathways_.get(pwCnt)).name_.contains(lowcase)) || (((PathwayWithEc)this.overSample_.pathways_.get(pwCnt)).name_.contains(upCase)))
/* 194:    */       {
/* 195:204 */         boolean notInList = true;
/* 196:205 */         for (int i = 0; i < this.pathwIndexes_.size(); i++) {
/* 197:206 */           if (((Integer)this.pathwIndexes_.get(i)).intValue() == pwCnt) {
/* 198:207 */             notInList = false;
/* 199:    */           }
/* 200:    */         }
/* 201:210 */         if (notInList) {
/* 202:211 */           this.pathwIndexes_.add(Integer.valueOf(pwCnt));
/* 203:    */         }
/* 204:    */       }
/* 205:    */     }
/* 206:    */   }
/* 207:    */   
/* 208:    */   private void searchEc(String in)
/* 209:    */   {
/* 210:217 */     this.ecIndexes_ = new ArrayList();
/* 211:218 */     for (int ecCnt = 0; ecCnt < this.overSample_.ecs_.size(); ecCnt++) {
/* 212:219 */       if (((EcWithPathway)this.overSample_.ecs_.get(ecCnt)).name_.equalsIgnoreCase(in)) {
/* 213:220 */         this.ecIndexes_.add(Integer.valueOf(ecCnt));
/* 214:    */       }
/* 215:    */     }
/* 216:223 */     for (int ecCnt = 0; ecCnt < this.overSample_.ecs_.size(); ecCnt++) {
/* 217:224 */       if (((EcWithPathway)this.overSample_.ecs_.get(ecCnt)).name_.startsWith(in))
/* 218:    */       {
/* 219:225 */         boolean notInList = true;
/* 220:226 */         for (int i = 0; i < this.ecIndexes_.size(); i++) {
/* 221:227 */           if (((Integer)this.ecIndexes_.get(i)).intValue() == ecCnt) {
/* 222:228 */             notInList = false;
/* 223:    */           }
/* 224:    */         }
/* 225:231 */         if (notInList) {
/* 226:232 */           this.ecIndexes_.add(Integer.valueOf(ecCnt));
/* 227:    */         }
/* 228:    */       }
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   private void showAllPaths()
/* 233:    */   {
/* 234:238 */     removeAll();
/* 235:239 */     initMainPanels();
/* 236:240 */     showaddInfoPanel();
/* 237:    */     
/* 238:242 */     this.line_ = 0;
/* 239:243 */     JButton newSearch = new JButton("New Search");
/* 240:244 */     newSearch.setBounds(40, 20, this.colDis, this.linDis);
/* 241:245 */     newSearch.setVisible(true);
/* 242:246 */     newSearch.addActionListener(new ActionListener()
/* 243:    */     {
/* 244:    */       public void actionPerformed(ActionEvent e)
/* 245:    */       {
/* 246:251 */         PwSearchPane.this.removeAll();
/* 247:252 */         PwSearchPane.this.findPw();
/* 248:253 */         PwSearchPane.this.invalidate();
/* 249:254 */         PwSearchPane.this.validate();
/* 250:255 */         PwSearchPane.this.repaint();
/* 251:    */       }
/* 252:258 */     });
/* 253:259 */     this.optionsPanel_.add(newSearch);
/* 254:260 */     for (int indCnt = 0; indCnt < this.pathwIndexes_.size(); indCnt++) {
/* 255:261 */       showPathway(((Integer)this.pathwIndexes_.get(indCnt)).intValue());
/* 256:    */     }
/* 257:    */   }
/* 258:    */   
/* 259:    */   private void showaddInfoPanel()
/* 260:    */   {
/* 261:265 */     this.mouseOverP = new JPanel();
/* 262:266 */     this.mouseOverP.setBackground(Project.getBackColor_());
/* 263:267 */     this.mouseOverP.setBounds(700, 10, 500, 60);
/* 264:268 */     this.optionsPanel_.add(this.mouseOverP);
/* 265:    */     
/* 266:270 */     this.mouseOverDisp = new JLabel("Additional Pathway-information");
/* 267:    */     
/* 268:272 */     this.mouseOverDisp.setBounds(0, 0, 500, 60);
/* 269:273 */     this.mouseOverP.add(this.mouseOverDisp);
/* 270:    */   }
/* 271:    */   
/* 272:    */   private void showPathway(int index)
/* 273:    */   {
/* 274:280 */     this.pathways_ = this.overSample_.pathways_;
/* 275:    */     
/* 276:282 */     this.displayP_.setSize(getWidth(), 100 + this.pathways_.size() * this.linDis);
/* 277:    */     
/* 278:284 */     final PathwayWithEc opath = (PathwayWithEc)this.pathways_.get(index);
/* 279:    */     
/* 280:286 */     PathButt pathNames = new PathButt(this.samples_, this.overSample_, (PathwayWithEc)this.pathways_.get(index), getBackground(), "", 0);
/* 281:287 */     pathNames.setBounds(this.xDist - this.colDis, this.linDis + this.linDis * this.line_, this.colDis, this.linDis);
/* 282:288 */     pathNames.addMouseListener(new MouseListener()
/* 283:    */     {
/* 284:    */       public void mouseReleased(MouseEvent e) {}
/* 285:    */       
/* 286:    */       public void mousePressed(MouseEvent e) {}
/* 287:    */       
/* 288:    */       public void mouseExited(MouseEvent e)
/* 289:    */       {
/* 290:305 */         PwSearchPane.this.mouseOverDisp.setVisible(false);
/* 291:    */         
/* 292:307 */         PwSearchPane.this.mouseOverDisp.setText("Additional Pathway-information");
/* 293:    */       }
/* 294:    */       
/* 295:    */       public void mouseEntered(MouseEvent e)
/* 296:    */       {
/* 297:314 */         PwSearchPane.this.mouseOverDisp.setVisible(true);
/* 298:    */         
/* 299:316 */         PwSearchPane.this.setAdditionalInfo(opath);
/* 300:    */       }
/* 301:    */       
/* 302:    */       public void mouseClicked(MouseEvent e) {}
/* 303:325 */     });
/* 304:326 */     this.displayP_.add(pathNames);
/* 305:    */     
/* 306:328 */     int sampleNr = this.samples_.size();
/* 307:    */     
/* 308:    */ 
/* 309:    */ 
/* 310:    */ 
/* 311:333 */     this.displayP_.setSize(getWidth(), 100 + sampleNr * this.linDis);
/* 312:335 */     for (int sampleCnt = 0; sampleCnt < sampleNr; sampleCnt++)
/* 313:    */     {
/* 314:336 */       Sample tmpSample = (Sample)this.samples_.get(sampleCnt);
/* 315:    */       
/* 316:338 */       JLabel name = new JLabel(tmpSample.name_);
/* 317:339 */       int x = this.colDis * (sampleCnt + 1);
/* 318:340 */       int y = this.linDis;
/* 319:341 */       name.setBounds(x + 5, 20, this.colDis - 10, 20);
/* 320:342 */       this.displayP_.add(name);
/* 321:343 */       final PathwayWithEc fpath = new PathwayWithEc(tmpSample.getPath(((PathwayWithEc)this.pathways_.get(index)).id_), false);
/* 322:    */       
/* 323:345 */       PathButt scores = new PathButt(this.samples_, tmpSample, fpath, getBackground(), "", 0);
/* 324:346 */       scores.addMouseListener(new MouseListener()
/* 325:    */       {
/* 326:    */         public void mouseReleased(MouseEvent e) {}
/* 327:    */         
/* 328:    */         public void mousePressed(MouseEvent e) {}
/* 329:    */         
/* 330:    */         public void mouseExited(MouseEvent e)
/* 331:    */         {
/* 332:363 */           PwSearchPane.this.mouseOverDisp.setVisible(false);
/* 333:    */           
/* 334:365 */           PwSearchPane.this.mouseOverDisp.setText("Additional Pathway-information");
/* 335:    */         }
/* 336:    */         
/* 337:    */         public void mouseEntered(MouseEvent e)
/* 338:    */         {
/* 339:372 */           PwSearchPane.this.mouseOverDisp.setVisible(true);
/* 340:    */           
/* 341:374 */           PwSearchPane.this.setAdditionalInfo(fpath);
/* 342:    */         }
/* 343:    */         
/* 344:    */         public void mouseClicked(MouseEvent e) {}
/* 345:383 */       });
/* 346:384 */       x = this.colDis * (sampleCnt + 1);
/* 347:385 */       y = this.linDis + this.linDis * this.line_;
/* 348:386 */       scores.setBounds(x, y, this.colDis, this.linDis);
/* 349:387 */       this.displayP_.add(scores);
/* 350:    */     }
/* 351:389 */     this.line_ += 1;
/* 352:    */   }
/* 353:    */   
/* 354:    */   private void showEcs()
/* 355:    */   {
/* 356:392 */     removeAll();
/* 357:393 */     initMainPanels();
/* 358:    */     
/* 359:395 */     this.line_ = 0;
/* 360:    */     
/* 361:    */ 
/* 362:398 */     JButton newSearch = new JButton("new search");
/* 363:399 */     newSearch.setBounds(40, 20, this.colDis, this.linDis);
/* 364:400 */     newSearch.setVisible(true);
/* 365:401 */     newSearch.addActionListener(new ActionListener()
/* 366:    */     {
/* 367:    */       public void actionPerformed(ActionEvent e)
/* 368:    */       {
/* 369:406 */         PwSearchPane.this.removeAll();
/* 370:407 */         PwSearchPane.this.findPw();
/* 371:408 */         PwSearchPane.this.invalidate();
/* 372:409 */         PwSearchPane.this.validate();
/* 373:410 */         PwSearchPane.this.repaint();
/* 374:    */       }
/* 375:413 */     });
/* 376:414 */     this.optionsPanel_.add(newSearch);
/* 377:415 */     this.displayP_.setSize(getWidth(), 100 + this.ecIndexes_.size() * this.linDis);
/* 378:416 */     for (int ecCnt = 0; ecCnt < this.ecIndexes_.size(); ecCnt++)
/* 379:    */     {
/* 380:417 */       final int index = ((Integer)this.ecIndexes_.get(ecCnt)).intValue();
/* 381:418 */       JButton ecButt = new JButton(((EcWithPathway)this.overSample_.ecs_.get(index)).name_ + " " + ((EcWithPathway)this.overSample_.ecs_.get(index)).amount_);
/* 382:419 */       ecButt.setBounds(20, 20 + this.linDis + this.linDis * this.line_, this.colDis, this.linDis);
/* 383:420 */       ecButt.setVisible(true);
/* 384:421 */       ecButt.addActionListener(new ActionListener()
/* 385:    */       {
/* 386:    */         public void actionPerformed(ActionEvent e)
/* 387:    */         {
/* 388:426 */           if (PwSearchPane.this.proj_ == null) {
/* 389:427 */             System.out.println("actProjsearch");
/* 390:    */           }
/* 391:430 */           PwInfoFrame frame = new PwInfoFrame((EcWithPathway)PwSearchPane.this.overSample_.ecs_.get(index), PwSearchPane.this.proj_, PwSearchPane.this.overSample_);
/* 392:    */         }
/* 393:433 */       });
/* 394:434 */       this.displayP_.add(ecButt);
/* 395:435 */       this.line_ += 1;
/* 396:    */     }
/* 397:    */   }
/* 398:    */   
/* 399:    */   private void setAdditionalInfo(PathwayWithEc path)
/* 400:    */   {
/* 401:440 */     this.mouseOverDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_ + "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_ + "</html>");
/* 402:    */   }
/* 403:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.PwSearchPane

 * JD-Core Version:    0.7.0.1

 */