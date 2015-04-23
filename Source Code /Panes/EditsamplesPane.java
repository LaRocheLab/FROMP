/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.Project;
/*   4:    */ import Objects.Sample;
/*   5:    */ import Prog.Controller;
/*   6:    */ import Prog.DataProcessor;
/*   7:    */ import Prog.IndexButton;
/*   8:    */ import Prog.StringReader;
/*   9:    */ import java.awt.Color;
/*  10:    */ import java.awt.event.ActionEvent;
/*  11:    */ import java.awt.event.ActionListener;
/*  12:    */ import java.awt.event.WindowEvent;
/*  13:    */ import java.awt.event.WindowListener;
/*  14:    */ import java.io.BufferedReader;
/*  15:    */ import java.io.File;
/*  16:    */ import java.io.IOException;
/*  17:    */ import java.io.PrintStream;
/*  18:    */ import java.util.ArrayList;
			  import java.util.*;
/*  19:    */ import javax.swing.JButton;
/*  20:    */ import javax.swing.JCheckBox;
/*  21:    */ import javax.swing.JColorChooser;
/*  22:    */ import javax.swing.JFileChooser;
/*  23:    */ import javax.swing.JFrame;
/*  24:    */ import javax.swing.JLabel;
/*  25:    */ import javax.swing.JPanel;
/*  26:    */ import javax.swing.JSlider;
/*  27:    */ import javax.swing.event.ChangeEvent;
/*  28:    */ import javax.swing.event.ChangeListener;
/*  29:    */ import javax.swing.filechooser.FileFilter;
/*   13:     */ import java.awt.Dimension;

				// This is the panel in between th estart screen and the pathway selection screen where you are able to select 
				// samples you want to work with and whether or not you want to do random sampling.

/*  31:    */ public class EditsamplesPane
/*  32:    */   extends JPanel
/*  33:    */ {
/*  34:    */   private static final long serialVersionUID = 1L;	//
/*  35:    */   Project activeProj_;								// Project object from which to add and remove samples
/*  36:    */   Sample tmpSamp_;									// A temporary sample object, to be used for adding samples to the project
/*  37:    */   IndexButton[] colButton_;							// Array of colour buttons for the samples
/*  38:    */   IndexButton[] delButt_;								// Array of buttons used to remove samples from the project
				IndexButton[] seqButt_;								// Array of buttons used to add a sequence file to a sample
/*  39:    */   JButton button_;									// Select sample button
/*  40:    */   JButton newColors_;									// Set new colours button 
/*  41:    */   String lastPath_;									// Temporary variable used to keep track of the last file path in the FileChooser
/*  42:    */   MyChooser fChoose_;									// The afforementioned FileChooser
/*  43:    */   File lastFile;										// The last file in the Project.samples_ arraylist 
/*  44:    */   JSlider slider;										// Sample Colour Difference Slider
/*  45:    */   int colChange;										// Difference in colour from sample to sample if not interfiered with
/*  46:    */   JLabel colDiff;										// 
/*  47:    */   ArrayList<JCheckBox> checks_;						// Checkboxes to the left of each sample. If not checked, sets sample.inuse to false
/*  48:    */   public JCheckBox listCheck_;						// 
/*  49:    */   JCheckBox chainCheck_;								// 
/*  50:    */   JLabel listl_;										// 
/*  51:    */   public JCheckBox randCheck_;						// CheckBox for random sampling
/*  52:    */   JLabel randl_;										// 
/*  53:    */   public boolean dataChanged_;						// 
/*  54:    */   public JButton advance_;							// 
/*  55:    */   private JButton save_;								// 
/*  56:    */   private JButton saveAs_;							// 
/*  57:    */   private JButton loadMat_;							// 
/*  58:    */   public JButton backButton_;							// 	
/*  59:    */   public JButton nextButton_;							// 
/*  60:    */   ArrayList<JButton> names_;							// ArrayList of buttons which contain the names of the samples
/*  61:    */   StringReader reader;								// 
/*  62: 81 */   int xCol2 = 800;									// 
/*  63:    */    
/*  64:    */   public EditsamplesPane(Project activeProj)
/*  65:    */   {
/*  66: 84 */     this.reader = new StringReader();
/*  67:    */     
/*  68: 86 */     this.activeProj_ = activeProj;
/*  69: 87 */     if (Project.samples_.size() > 0) {
/*  70: 88 */       this.lastFile = new File(((Sample)Project.samples_.get(Project.samples_.size() - 1)).fullPath_);
/*  71:    */     }
/*  72: 90 */     this.lastPath_ = "";
/*  73: 91 */     this.colChange = 15;
/*  74: 92 */     this.colButton_ = new IndexButton[Project.samples_.size()];
/*  75: 93 */     this.delButt_ = new IndexButton[Project.samples_.size()];
				  this.seqButt_ = new IndexButton[Project.samples_.size()];
/*  76: 94 */     this.fChoose_ = new MyChooser(this.lastPath_);
/*  77: 95 */     this.checks_ = new ArrayList();
/*  78:    */     
/*  79: 97 */     this.backButton_ = new JButton("< Back to Project Menu");
/*  80: 98 */     this.nextButton_ = new JButton("Go to Pathway Selection >");
/*  81:    */     
/*  82:100 */     setLayout(null);
/*  83:101 */     setVisible(true);
/*  84:102 */     setBackground(Project.getBackColor_());
				  setPreferredSize(new Dimension(getWidth(), (Project.samples_.size() + 2) * 50 + 100));
				  setSize(getPreferredSize());
/*  85:103 */     prepPaint();						// Sets everything up for the EditSamplesPane
/*  86:    */   }
/*  87:    */   
/*  88:    */   private void addSamples()
/*  89:    */   {// Adds all the samples on screen as buttons
/*  90:107 */     setBackground(Project.getBackColor_());
/*  91:    */     
/*  92:109 */     this.names_ = new ArrayList();
/*  93:    */     
/*  94:111 */     this.colButton_ = new IndexButton[Project.samples_.size()];
/*  95:112 */     this.delButt_ = new IndexButton[Project.samples_.size()];
				  this.seqButt_ = new IndexButton[Project.samples_.size()];
/*  96:113 */     int nameL = 400;
/*  97:114 */     int colH = 20;
/*  98:115 */     int colD = 25;
/*  99:    */     
/* 100:117 */     this.newColors_ = new JButton("Set new Colors");
/* 101:118 */     this.newColors_.setBounds(this.xCol2, 150, 150, 30);
/* 102:119 */     this.newColors_.addActionListener(new ActionListener()
/* 103:    */     {
/* 104:    */       public void actionPerformed(ActionEvent e)
/* 105:    */       {
/* 106:124 */         EditsamplesPane.this.convertChecks();
/* 107:125 */         EditsamplesPane.this.setNewColorSet();
/* 108:126 */         EditsamplesPane.this.prepPaint();
/* 109:127 */         Project.dataChanged = true;
/* 110:    */       }
/* 111:129 */     });
/* 112:130 */     add(this.newColors_);
/* 113:    */     
/* 114:132 */     loadSampleButton();
/* 115:134 */     if (Project.samples_.size() > 0)
/* 116:    */     {
/* 117:136 */       JLabel label = new JLabel("col");
/* 118:137 */       label.setBounds(31, 23, 40, 20);
/* 119:138 */       add(label);
/* 120:    */       
/* 121:140 */       label = new JLabel("name");
/* 122:141 */       label.setBounds(200, 23, 40, 20);
/* 123:142 */       add(label);
/* 124:    */       
/* 125:144 */       label = new JLabel("valid");
/* 126:145 */       label.setBounds(61 + nameL, 23, 40, 20);
/* 127:146 */       add(label);
/* 128:    */       
/* 129:148 */       label = new JLabel("del");
/* 130:149 */       label.setBounds(102 + nameL, 23, 40, 20);
/* 131:150 */       add(label);
					label = new JLabel("seq");
/* 130:149 */       label.setBounds(156 + nameL, 23, 40, 20);
/* 131:150 */       add(label);
/* 132:    */     }
/* 133:153 */     for (int sampCnt = 0; sampCnt < Project.samples_.size(); sampCnt++)
/* 134:    */     {
/* 135:154 */       final int cnt = sampCnt;
/* 136:155 */       this.tmpSamp_ = ((Sample)Project.samples_.get(sampCnt));
/* 137:    */       
/* 138:157 */       JButton sampName = new JButton(this.tmpSamp_.name_);
/* 139:158 */       sampName.setBounds(60, 50 + colD * sampCnt, nameL, colH);
/* 140:159 */       sampName.setVisible(true);
/* 141:160 */       sampName.setLayout(null);
/* 142:161 */       sampName.addActionListener(new ActionListener()
/* 143:    */       {
/* 144:    */         public void actionPerformed(ActionEvent e)
/* 145:    */         {
/* 146:168 */           SampleNameFrame nFrame = new SampleNameFrame(cnt, ((Sample)Project.samples_.get(cnt)).name_);
/* 147:169 */           nFrame.addWindowListener(new WindowListener()
/* 148:    */           {
/* 149:    */             public void windowOpened(WindowEvent arg0) {}
/* 150:    */             
/* 151:    */             public void windowIconified(WindowEvent arg0) {}
/* 152:    */             
/* 153:    */             public void windowDeiconified(WindowEvent arg0) {}
/* 154:    */             
/* 155:    */             public void windowDeactivated(WindowEvent arg0) {}
/* 156:    */             
/* 157:    */             public void windowClosing(WindowEvent arg0) {}
/* 158:    */             
/* 159:    */             public void windowClosed(WindowEvent arg0)
/* 160:    */             {
/* 161:204 */               EditsamplesPane.this.prepPaint();
/* 162:    */             }
/* 163:    */             
/* 164:    */             public void windowActivated(WindowEvent arg0) {}
/* 165:    */           });
/* 166:    */         }
/* 167:216 */       });
/* 168:217 */       add(sampName);
/* 169:218 */       this.names_.add(sampName);
/* 170:    */       
/* 171:220 */       JColorChooser colChoose_ = new JColorChooser();
/* 172:221 */       colChoose_.setBounds(0, 0, 400, 400);
/* 173:222 */       colChoose_.setVisible(true);
/* 174:    */       
/* 175:224 */       this.colButton_[sampCnt] = new IndexButton(sampCnt);
/* 176:225 */       this.colButton_[sampCnt].setBackground(this.tmpSamp_.sampleCol_);
/* 177:226 */       this.colButton_[sampCnt].setBounds(30, 50 + colD * sampCnt, 20, 20);
/* 178:227 */       this.colButton_[sampCnt].setLayout(null);
/* 179:228 */       this.colButton_[sampCnt].setVisible(true);
/* 180:229 */       this.colButton_[sampCnt].addActionListener(new ActionListener()
/* 181:    */       {
/* 182:    */         public void actionPerformed(ActionEvent e)
/* 183:    */         {
/* 184:234 */           ((Sample)Project.samples_.get(cnt)).sampleCol_ = JColorChooser.showDialog(null, "Choose SampleColor", EditsamplesPane.this.tmpSamp_.sampleCol_);
/* 185:235 */           EditsamplesPane.this.colButton_[cnt].setBackground(((Sample)Project.samples_.get(cnt)).sampleCol_);
/* 186:236 */           Project.dataChanged = true;
/* 187:237 */           EditsamplesPane.this.prepPaint();
/* 188:    */         }
/* 189:239 */       });
/* 190:240 */       add(this.colButton_[sampCnt]);
/* 191:    */       
					this.seqButt_[sampCnt] = new IndexButton(sampCnt);
					if(Project.samples_.get(sampCnt).getSequenceFile()!=null&&!Project.samples_.get(cnt).getSequenceFile().equals("")){
/* 196:246 */       	this.seqButt_[sampCnt].setText("Y");
					} else {
						this.seqButt_[sampCnt].setText("N");
					}
/* 194:244 */       this.seqButt_[sampCnt].setBounds(142 + nameL, 50 + colD * sampCnt, 50, 20);
/* 195:245 */       this.seqButt_[sampCnt].setVisible(true);
					if(Project.samples_.get(cnt).getSequenceFile()!=null&&!Project.samples_.get(cnt).getSequenceFile().equals("")){
/* 196:246 */       	this.seqButt_[sampCnt].setBackground(Color.green);
					} else {
						this.seqButt_[sampCnt].setBackground(Color.red);
					}
/* 197:247 */       this.seqButt_[sampCnt].addActionListener(new ActionListener()
/* 198:    */       {
/* 199:    */         public void actionPerformed(ActionEvent e)
/* 200:    */         {
//* 201:252 */           Project.dataChanged = true;
						Project.dataChanged = true;
/* 430:478 */         	String path_ = "";
/* 431:    */         	try
/* 432:    */         	{
/* 433:480 */         	  path_ = new File("").getCanonicalPath();
/* 434:    */         	}
/* 435:    */         	catch (IOException e1)
/* 436:    */         	{
/* 437:483 */         	  e1.printStackTrace();
/* 438:    */         	}
/* 439:485 */         	JFileChooser fChoose_ = new JFileChooser(path_);
/* 440:486 */         	fChoose_.setFileSelectionMode(0);
/* 441:487 */         	fChoose_.setBounds(100, 100, 200, 20);
/* 442:488 */         	fChoose_.setVisible(true);
/* 443:489 */         	File file = new File(path_);
/* 444:490 */         	fChoose_.setSelectedFile(file);
/* 445:491 */         	fChoose_.setFileFilter(new FileFilter()
/* 446:    */         	{
/* 447:    */         	  public boolean accept(File f)
/* 448:    */         	  {
							if((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".fasta")) || (f.getName().toLowerCase().endsWith(".fas")) || (f.getName().toLowerCase().endsWith(".faa"))) {
								return true;
							}
							return false;
/* 453:    */         	  }
/* 454:    */         	  
/* 455:    */         	  public String getDescription()
/* 456:    */         	  {
/* 457:504 */         	    return ".fasta/.fas/.faa";
/* 458:    */         	  }
/* 459:    */         	});
/* 460:508 */         	if (fChoose_.showSaveDialog(EditsamplesPane.this.getParent()) == 0)
/* 461:    */         	{
/* 462:    */         	  try
/* 463:    */         	  {
/* 464:510 */         	    String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 470:516 */         	    Project.samples_.get(cnt).setSequenceFile(path);
/* 471:    */         	  }
/* 472:    */         	  catch (IOException e1)
/* 473:    */         	  {
/* 474:521 */         	    e1.printStackTrace();
/* 475:    */         	  }
						  EditsamplesPane.this.prepPaint();
/* 479:    */         	}
/* 204:    */         }
/* 205:257 */       });
/* 206:258 */       add(this.seqButt_[sampCnt]);

/* 192:242 */       this.delButt_[sampCnt] = new IndexButton(sampCnt);
/* 193:243 */       this.delButt_[sampCnt].setText("x");
/* 194:244 */       this.delButt_[sampCnt].setBounds(86 + nameL, 50 + colD * sampCnt, 50, 20);
/* 195:245 */       this.delButt_[sampCnt].setVisible(true);
/* 196:246 */       this.delButt_[sampCnt].setBackground(Project.getBackColor_());
/* 197:247 */       this.delButt_[sampCnt].addActionListener(new ActionListener()
/* 198:    */       {
/* 199:    */         public void actionPerformed(ActionEvent e)
/* 200:    */         {
/* 201:252 */           Project.dataChanged = true;
/* 202:253 */           EditsamplesPane.this.removeSample(EditsamplesPane.this.delButt_[cnt].SampleIndex_);
						EditsamplesPane.this.prepPaint();
/* 204:    */         }
/* 205:257 */       });
/* 206:258 */       add(this.delButt_[sampCnt]);
/* 207:    */       
/* 208:260 */       JCheckBox check = new JCheckBox();
/* 209:261 */       check.setBounds(10, 50 + colD * sampCnt, 17, 17);
/* 210:262 */       check.setBackground(Project.getBackColor_());
/* 211:263 */       if (this.tmpSamp_.inUse) {
/* 212:264 */         check.setSelected(true);
/* 213:    */       }
/* 214:266 */       add(check);
/* 215:267 */       this.checks_.add(check);
/* 216:    */       
/* 217:269 */       JPanel legitP = new JPanel();
/* 218:270 */       if ((this.tmpSamp_.legitSample) || (this.tmpSamp_.imported)) {
/* 219:271 */         legitP.setBackground(Color.green);
/* 220:    */       } else {
/* 221:274 */         legitP.setBackground(Color.red);
/* 222:    */       }
/* 223:276 */       legitP.setBounds(63 + nameL, 50 + colD * sampCnt, 20, 20);
/* 224:277 */       add(legitP);
/* 225:    */     }
/* 226:    */   }
/* 227:    */   
/* 228:    */   private void loadSampleButton()
/* 229:    */   {// Builds the Select Sample button
/* 230:282 */     this.button_ = new JButton();
/* 231:283 */     this.button_.setBounds(this.xCol2, 50, 150, 30);
/* 232:284 */     this.button_.setText("Select Sample");
/* 233:285 */     add(this.button_);
/* 234:    */     
/* 235:287 */     this.button_.addActionListener(new ActionListener()
/* 236:    */     {
/* 237:    */       public void actionPerformed(ActionEvent e)
/* 238:    */       {
/* 239:293 */         if (EditsamplesPane.this.lastFile != null) {
/* 240:294 */           EditsamplesPane.this.fChoose_.setCurrentDirectory(EditsamplesPane.this.lastFile);
/* 241:    */         } else {
/* 242:297 */           EditsamplesPane.this.lastFile = new File("");
/* 243:    */         }
/* 244:    */         try
/* 245:    */         {
/* 246:301 */           EditsamplesPane.this.lastPath_ = EditsamplesPane.this.fChoose_.getCurrentDirectory().getCanonicalPath();
/* 247:    */         }
/* 248:    */         catch (IOException e2)
/* 249:    */         {
/* 250:304 */           e2.printStackTrace();
/* 251:    */         }
/* 252:306 */         EditsamplesPane.this.fChoose_.setFileSelectionMode(0);
/* 253:307 */         EditsamplesPane.this.fChoose_.setVisible(true);
/* 254:308 */         EditsamplesPane.this.fChoose_.setFileFilter(new FileFilter()
/* 255:    */         {
/* 256:    */           public boolean accept(File f)
/* 257:    */           {
/* 258:312 */             if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".txt")) || (f.getName().toLowerCase().endsWith(".out")) || (f.getName().toLowerCase().endsWith(".tsv"))) {
/* 259:313 */               return true;
/* 260:    */             }
/* 261:315 */             return false;
/* 262:    */           }
/* 263:    */           
/* 264:    */           public String getDescription()
/* 265:    */           {
/* 266:321 */             return ".txt";
/* 267:    */           }
/* 268:    */         });
/* 269:325 */         if (EditsamplesPane.this.fChoose_.showOpenDialog(EditsamplesPane.this.getParent()) == 0)
/* 270:    */         {
/* 271:    */           try
/* 272:    */           {
/* 273:327 */             EditsamplesPane.this.lastPath_ = EditsamplesPane.this.fChoose_.getCurrentDirectory().toString();
/* 274:328 */             EditsamplesPane.this.lastFile = EditsamplesPane.this.fChoose_.getCurrentDirectory();
/* 275:    */             
/* 276:330 */             boolean legit = EditsamplesPane.this.testSampleFile(EditsamplesPane.this.fChoose_.getSelectedFile().getCanonicalPath());
/* 277:331 */             Sample sample = new Sample(EditsamplesPane.this.fChoose_.getSelectedFile().getName(), EditsamplesPane.this.fChoose_.getSelectedFile().getCanonicalPath(), EditsamplesPane.this.getColor(Project.samples_.size()));
/* 278:332 */             sample.legitSample = legit;
/* 279:333 */             Project.samples_.add(sample);
/* 280:334 */             Project.dataChanged = true;
/* 281:    */           }
/* 282:    */           catch (IOException e1)
/* 283:    */           {
/* 284:337 */             e1.printStackTrace();
/* 285:    */           }
/* 286:339 */           EditsamplesPane.this.prepPaint();
/* 287:    */         }
/* 288:    */       }
/* 289:    */     });
/* 290:    */   }
/* 291:    */   
/* 292:    */   private void convertChecks()
/* 293:    */   {// "Converts" the check boxes next to the samples into data stored about the samples. If the boxes are checked than inUse for that sample equals true, else false
/* 294:346 */     System.out.println("convert");
/* 295:347 */     for (int i = 0; i < this.checks_.size(); i++)
/* 296:    */     {
/* 297:348 */       System.out.println(((JCheckBox)this.checks_.get(i)).isEnabled());
/* 298:349 */       if (((JCheckBox)this.checks_.get(i)).isSelected())
/* 299:    */       {
/* 300:350 */         if ((i < Project.samples_.size()) && 
/* 301:351 */           (!((Sample)Project.samples_.get(i)).inUse))
/* 302:    */         {
/* 303:352 */           this.dataChanged_ = true;
/* 304:353 */           ((Sample)Project.samples_.get(i)).inUse = true;
/* 305:    */         }
/* 306:    */       }
/* 307:358 */       else if ((i < Project.samples_.size()) && 
/* 308:359 */         (((Sample)Project.samples_.get(i)).inUse)) {
/* 309:360 */         ((Sample)Project.samples_.get(i)).inUse = false;
/* 310:    */       }
/* 311:    */     }
/* 312:365 */     while (this.checks_.size() > 0) {
/* 313:366 */       this.checks_.remove(0);
/* 314:    */     }
/* 315:368 */     Project.dataChanged = true;
/* 316:    */   }
/* 317:    */   
/* 318:    */   private void addMatrixButton()
/* 319:    */   {// Adds the Load EC-Matrix button. If pressed it opens a FileChooser. If a file is selected it trys Project.loadMat on the path of the file selected to add the samples
/* 320:371 */     this.loadMat_ = new JButton("Load EC-matrix");
/* 321:372 */     this.loadMat_.setBounds(this.xCol2, 85, 150, 30);
/* 322:373 */     this.loadMat_.setLayout(null);
/* 323:374 */     this.loadMat_.setVisible(true);
/* 324:375 */     this.loadMat_.addActionListener(new ActionListener()
/* 325:    */     {
/* 326:    */       public void actionPerformed(ActionEvent arg0)
/* 327:    */       {
/* 328:380 */         if (EditsamplesPane.this.lastFile != null) {
/* 329:381 */           EditsamplesPane.this.fChoose_.setCurrentDirectory(EditsamplesPane.this.lastFile);
/* 330:    */         } else {
/* 331:384 */           EditsamplesPane.this.lastFile = new File("");
/* 332:    */         }
/* 333:    */         try
/* 334:    */         {
/* 335:388 */           EditsamplesPane.this.lastPath_ = EditsamplesPane.this.fChoose_.getCurrentDirectory().getCanonicalPath();
/* 336:    */         }
/* 337:    */         catch (IOException e2)
/* 338:    */         {
/* 339:391 */           e2.printStackTrace();
/* 340:    */         }
/* 341:393 */         EditsamplesPane.this.fChoose_.setFileSelectionMode(0);
/* 342:394 */         EditsamplesPane.this.fChoose_.setVisible(true);
/* 343:395 */         EditsamplesPane.this.fChoose_.setFileFilter(new FileFilter()
/* 344:    */         {
/* 345:    */           public boolean accept(File f)
/* 346:    */           {
/* 347:399 */             if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".txt")) || (f.getName().toLowerCase().endsWith(".out"))) {
/* 348:400 */               return true;
/* 349:    */             }
/* 350:402 */             return false;
/* 351:    */           }
/* 352:    */           
/* 353:    */           public String getDescription()
/* 354:    */           {
/* 355:408 */             return ".txt";
/* 356:    */           }
/* 357:    */         });
/* 358:412 */         if (EditsamplesPane.this.fChoose_.showOpenDialog(EditsamplesPane.this.getParent()) == 0)
/* 359:    */         {
/* 360:413 */           EditsamplesPane.this.lastPath_ = EditsamplesPane.this.fChoose_.getCurrentDirectory().toString();
/* 361:414 */           EditsamplesPane.this.lastFile = EditsamplesPane.this.fChoose_.getCurrentDirectory();
/* 362:    */           try
/* 363:    */           {
/* 364:416 */             EditsamplesPane.this.activeProj_.loadMat(EditsamplesPane.this.fChoose_.getSelectedFile().getCanonicalPath(), EditsamplesPane.this.fChoose_.getSelectedFile().getName());
/* 365:    */           }
/* 366:    */           catch (IOException e)
/* 367:    */           {
/* 368:419 */             e.printStackTrace();
/* 369:    */           }
/* 370:421 */           Project.dataChanged = true;
/* 371:422 */           EditsamplesPane.this.prepPaint();
/* 372:    */         }
/* 373:    */       }
/* 374:425 */     });
/* 375:426 */     add(this.loadMat_);
/* 376:    */     
/* 377:428 */     JButton help = new JButton("?");
/* 378:429 */     help.setBounds(this.xCol2 + 150, 85, 50, 30);
/* 379:430 */     help.setLayout(null);
/* 380:431 */     help.setVisible(true);
/* 381:432 */     help.addActionListener(new ActionListener()
/* 382:    */     {
/* 383:    */       public void actionPerformed(ActionEvent arg0)
/* 384:    */       {
/* 385:437 */         String Text = "<html><body>Add Ec-Matrix to the programm<br>a line in the input file should be of this form<br>(1.2.1.12,29,15,0)<br>Warning: no empty fields and no blanks</body></html>";
/* 386:    */         
/* 387:    */ 
/* 388:    */ 
/* 389:    */ 
/* 390:    */ 
/* 391:    */ 
/* 392:    */ 
/* 393:    */ 
/* 394:    */ 
/* 395:447 */         HelpFrame helpF = new HelpFrame(Text);
/* 396:    */       }
/* 397:449 */     });
/* 398:450 */     add(help);
/* 399:    */   }
/* 400:    */   
/* 401:    */   private void addSaveButtons()
/* 402:    */   {
/* 403:453 */     this.save_ = new JButton("Save project");
/* 404:454 */     this.save_.setBounds(this.xCol2, 500, 150, 30);
/* 405:455 */     this.save_.setLayout(null);
/* 406:456 */     this.save_.setVisible(true);
/* 407:457 */     this.save_.addActionListener(new ActionListener()
/* 408:    */     {
/* 409:    */       public void actionPerformed(ActionEvent e)
/* 410:    */       {
/* 411:460 */         Project.dataChanged = true;
/* 412:461 */         EditsamplesPane.this.setValues();
/* 413:462 */         Controller.loadPathways(true);
/* 414:463 */         Controller.saveProject();
/* 415:    */       }
/* 416:465 */     });
/* 417:466 */     add(this.save_);
/* 418:    */     
/* 419:468 */     this.saveAs_ = new JButton("Save project as");
/* 420:469 */     this.saveAs_.setBounds(this.xCol2, 535, 150, 30);
/* 421:470 */     this.saveAs_.setLayout(null);
/* 422:471 */     this.saveAs_.setVisible(true);
/* 423:472 */     this.saveAs_.addActionListener(new ActionListener()
/* 424:    */     {
/* 425:    */       public void actionPerformed(ActionEvent e)
/* 426:    */       {
/* 427:475 */         Project.dataChanged = true;
/* 428:476 */         EditsamplesPane.this.setValues();
/* 429:477 */         Controller.loadPathways(true);
/* 430:478 */         String path_ = "";
/* 431:    */         try
/* 432:    */         {
/* 433:480 */           path_ = new File("").getCanonicalPath();
/* 434:    */         }
/* 435:    */         catch (IOException e1)
/* 436:    */         {
/* 437:483 */           e1.printStackTrace();
/* 438:    */         }
/* 439:485 */         JFileChooser fChoose_ = new JFileChooser(path_ + File.separator + "projects");
/* 440:486 */         fChoose_.setFileSelectionMode(0);
/* 441:487 */         fChoose_.setBounds(100, 100, 200, 20);
/* 442:488 */         fChoose_.setVisible(true);
/* 443:489 */         File file = new File(path_ + File.separator + "projects");
/* 444:490 */         fChoose_.setSelectedFile(file);
/* 445:491 */         fChoose_.setFileFilter(new FileFilter()
/* 446:    */         {
/* 447:    */           public boolean accept(File f)
/* 448:    */           {
/* 449:495 */             if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".frp"))) {
/* 450:496 */               return true;
/* 451:    */             }
/* 452:498 */             return false;
/* 453:    */           }
/* 454:    */           
/* 455:    */           public String getDescription()
/* 456:    */           {
/* 457:504 */             return ".frp";
/* 458:    */           }
/* 459:    */         });
/* 460:508 */         if (fChoose_.showSaveDialog(EditsamplesPane.this.getParent()) == 0)
/* 461:    */         {
/* 462:    */           try
/* 463:    */           {
/* 464:510 */             String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 465:512 */             if (!path.endsWith(".frp"))
/* 466:    */             {
/* 467:513 */               path = path + ".frp";
/* 468:514 */               System.out.println(".frp");
/* 469:    */             }
/* 470:516 */             Controller.project_.exportProj(path);
/* 471:    */           }
/* 472:    */           catch (IOException e1)
/* 473:    */           {
/* 474:521 */             e1.printStackTrace();
/* 475:    */           }
/* 476:524 */           EditsamplesPane.this.invalidate();
/* 477:525 */           EditsamplesPane.this.validate();
/* 478:526 */           EditsamplesPane.this.repaint();
/* 479:    */         }
/* 480:528 */         System.out.println("Save");
/* 481:    */       }
/* 482:530 */     });
/* 483:531 */     add(this.saveAs_);
/* 484:    */   }
/* 485:    */   
/* 486:    */   private void addBackNext()
/* 487:    */   {// Adds the "Back to project Menu" and "Go to pathway selection" buttons
/* 488:535 */     this.backButton_.setBounds(this.xCol2 - 135, 570, 250, 30);
/* 489:536 */     add(this.backButton_);
/* 490:    */     
/* 491:    */ 
/* 492:539 */     this.nextButton_.setBounds(this.xCol2 + 125, 570, 250, 30);
/* 493:540 */     add(this.nextButton_);
/* 494:    */   }
/* 495:    */   
/* 496:    */   private void removeSample(int sampIndex)
/* 497:    */   {
/* 498:543 */     Project.removeSample(sampIndex);
/* 499:    */   }
/* 500:    */   
/* 501:    */   private void setNewColorSet()
/* 502:    */   {
/* 503:546 */     for (int sampCnt = 0; sampCnt < Project.samples_.size(); sampCnt++)
/* 504:    */     {
/* 505:547 */       this.tmpSamp_ = ((Sample)Project.samples_.get(sampCnt));
/* 506:548 */       this.tmpSamp_.sampleCol_ = getColor(sampCnt);
/* 507:    */     }
/* 508:    */   }
/* 509:    */   
/* 510:    */   private void addColDiff()
/* 511:    */   {
/* 512:553 */     if (this.colDiff == null) {
/* 513:554 */       this.colDiff = new JLabel("Sample Color Difference");
/* 514:    */     }
/* 515:556 */     this.colDiff.setLayout(null);
/* 516:557 */     this.colDiff.setBounds(this.xCol2, 180, 200, 30);
/* 517:558 */     add(this.colDiff);
/* 518:560 */     if (this.slider == null) {
/* 519:561 */       this.slider = new JSlider(10, 100, this.colChange);
/* 520:    */     }
/* 521:563 */     this.slider.setLayout(null);
/* 522:564 */     this.slider.setBackground(Project.getBackColor_());
/* 523:565 */     this.slider.setBounds(this.xCol2, 210, 150, 30);
/* 524:566 */     this.slider.addChangeListener(new ChangeListener()
/* 525:    */     {
/* 526:    */       public void stateChanged(ChangeEvent arg0)
/* 527:    */       {
/* 528:571 */         EditsamplesPane.this.colChange = ((JSlider)arg0.getSource()).getValue();
/* 529:    */       }
/* 530:573 */     });
/* 531:574 */     add(this.slider);
/* 532:    */   }
/* 533:    */   
/* 534:    */   private Color getColor(int pos)
/* 535:    */   {
/* 536:578 */     Color col = new Color(255, 0, 0);
/* 537:580 */     for (int j = 0; j < pos; j++) {
/* 538:581 */       if (col.getBlue() <= 255 - this.colChange) {
/* 539:582 */         col = new Color(col.getRed() - this.colChange, col.getGreen(), col.getBlue() + this.colChange);
/* 540:585 */       } else if (col.getGreen() <= 255 - this.colChange) {
/* 541:586 */         col = new Color(col.getRed(), col.getGreen() + this.colChange, col.getBlue());
/* 542:    */       }
/* 543:    */     }
/* 544:590 */     return col;
/* 545:    */   }
/* 546:    */   
/* 547:    */   public void prepPaint()
/* 548:    */   {
/* 549:593 */     removeAll();				// Clears the back panel
/* 550:594 */     addSamples();				// Adds samples if there are any in the current project
/* 551:595 */     addColDiff();				// Sets up the colour slider
/* 552:596 */     showRandomSampling();		// Sets up the random sampling button and if it is selected puts the project inot random sampling mode
/* 553:597 */     addMatrixButton();		// Sets up the Add EC-Matrix button, as well as the help button next to it. If it is pressed the EC-matrix is parsed in this sample object
/* 554:    */     							// 
/* 555:599 */     addBackNext();			// Adds the back and next buttons
/* 556:600 */     invalidate();				// After this the method rebuilds the back panel with what has been done
/* 557:601 */     validate();				// 
/* 558:602 */     repaint();				// 
/* 559:    */   }
/* 560:    */   
/* 561:    */   private void setValues()
/* 562:    */   {
/* 563:606 */     convertChecks();
/* 564:    */     
/* 565:608 */     copyNames();
/* 566:609 */     Project.dataChanged = true;
/* 567:    */   }
/* 568:    */   
/* 569:    */   private void copyNames()
/* 570:    */   {
/* 571:612 */     for (int nCnt = 0; nCnt < this.names_.size(); nCnt++) {
/* 572:613 */       ((Sample)Project.samples_.get(nCnt)).name_ = ((JButton)this.names_.get(nCnt)).getText();
/* 573:    */     }
/* 574:    */   }
/* 575:    */   
/* 576:    */   private void showRandomSampling()
/* 577:    */   {// Shows the random sampling checkbox. If checked sets ranMode_ in Project object
/* 578:617 */     if (this.randCheck_ == null)
/* 579:    */     {
/* 580:618 */       this.randCheck_ = new JCheckBox();
/* 581:619 */       this.randl_ = new JLabel("Random Sampling:");
/* 582:620 */       this.randCheck_.addActionListener(new ActionListener()
/* 583:    */       {
/* 584:    */         public void actionPerformed(ActionEvent e)
/* 585:    */         {
/* 586:625 */           Project.randMode_ = EditsamplesPane.this.randCheck_.isSelected();
/* 587:626 */           EditsamplesPane.this.dataChanged_ = true;
/* 588:627 */           Project.dataChanged = true;
/* 589:628 */           if (EditsamplesPane.this.randCheck_.isSelected()) {
/* 590:629 */             EditsamplesPane.this.openWarning("Warning!", "<html><body>Be carefull with randomsampling and saving afterwards <br> only the reduced set of enzymes will be saved.</body></html>");
/* 591:    */           }
/* 592:    */         }
/* 593:    */       });
/* 594:    */     }
/* 595:636 */     this.randl_.setBounds(this.xCol2, 320, 200, 20);
/* 596:637 */     add(this.randl_);
/* 597:638 */     this.randCheck_.setBounds(this.xCol2, 350, 20, 20);
/* 598:639 */     this.randCheck_.setBackground(Project.getBackColor_());
/* 599:640 */     if (this.activeProj_ != null) {
/* 600:641 */       this.randCheck_.setSelected(Project.randMode_);
/* 601:    */     }
/* 602:643 */     add(this.randCheck_);
/* 603:    */   }
/* 604:    */   
/* 605:    */   private void openWarning(String title, String string)
/* 606:    */   {// Opens a warning Frame, with the input title as the title, and input string as the body
/* 607:647 */     JFrame frame = new JFrame(title);
/* 608:648 */     frame.setVisible(true);
/* 609:649 */     frame.setBounds(200, 200, 350, 150);
/* 610:650 */     frame.setLayout(null);
/* 611:651 */     frame.setResizable(false);
/* 612:    */     
/* 613:653 */     JPanel panel = new JPanel();
/* 614:654 */     panel.setBounds(0, 0, 350, 150);
/* 615:655 */     panel.setBackground(Color.lightGray);
/* 616:656 */     panel.setVisible(true);
/* 617:657 */     panel.setLayout(null);
/* 618:658 */     frame.add(panel);
/* 619:    */     
/* 620:660 */     JLabel label = new JLabel(string);
/* 621:    */     
/* 622:662 */     label.setVisible(true);
/* 623:663 */     label.setForeground(Color.black);
/* 624:664 */     label.setBounds(0, 0, 350, 150);
/* 625:665 */     label.setLayout(null);
/* 626:666 */     panel.add(label);
/* 627:    */     
/* 628:668 */     frame.repaint();
/* 629:    */   }
/* 630:    */   
/* 631:    */   private boolean testSampleFile(String samplePath)
/* 632:    */     throws IOException
/* 633:    */   {// Tests the first five lines of a file, if none of them can be read the file registers as invalid, saying"no enzyme in sample line". If all can be read, registers as valid. Anywhere in between, registers as valid, but offers a warning that only x of 5 were read
/* 634:672 */     int retries = 5;
/* 635:673 */     int goodLines = 0;
/* 636:    */     
/* 637:675 */     BufferedReader sample = this.reader.readTxt(samplePath);
/* 638:676 */     DataProcessor tmpProc = new DataProcessor();
/* 639:677 */     for (int i = 0; i < retries; i++)
/* 640:    */     {
/* 641:678 */       String zeile = sample.readLine();
/* 642:679 */       if (zeile == null) {
/* 643:    */         break;
/* 644:    */       }
/* 645:680 */       String[] newEnz = tmpProc.getEnzFromSample(zeile);
/* 646:681 */       if (!tmpProc.enzReadCorrectly(newEnz)) {
/* 647:682 */         newEnz = tmpProc.getEnzFromRawSample(zeile);
/* 648:    */       }
					if (!tmpProc.enzReadCorrectly(newEnz)) {
						newEnz = tmpProc.getEnzFromInterPro(zeile);
					}
/* 649:684 */       if (!tmpProc.enzReadCorrectly(newEnz))
/* 650:    */       {
/* 651:685 */         System.err.println("no enzyme in sample line");
/* 652:686 */         System.err.println(zeile);
/* 653:    */       }
/* 654:    */       else
/* 655:    */       {
/* 656:689 */         goodLines++;
/* 657:    */       }
/* 658:    */     }
/* 659:695 */     if (goodLines > 0)
/* 660:    */     {
/* 661:696 */       if (goodLines < retries) {
/* 662:697 */         openWarning("Warning!", "<html><body>The selected samplefile may not be correct.<br>Out of the first " + 
/* 663:698 */           retries + " lines " + (retries - goodLines) + " could not be read correctly.<br>" + 
/* 664:699 */           "Please check your file.</body></html>");
/* 665:    */       } else {
/* 666:702 */         openWarning("Sample valid", "<html><body>All good.</body></html>");
/* 667:    */       }
/* 668:704 */       return true;
/* 669:    */     }
/* 670:707 */     openWarning("Warning!", "<html><body>The selected samplefile could not be read correctly.<br>Please check your file.</body></html>");
/* 671:    */     
/* 672:709 */     return false;
/* 673:    */   }
/* 674:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.EditsamplesPane

 * JD-Core Version:    0.7.0.1

 */