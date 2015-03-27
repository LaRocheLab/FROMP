/*    1:     */ package Panes;
/*    2:     */ 
/*    3:     */ import Objects.ConvertStat;
/*    4:     */ import Objects.EcNr;
/*    5:     */ import Objects.EcWithPathway;
/*    6:     */ import Objects.Line;
/*    7:     */ import Objects.Pathway;
/*    8:     */ import Objects.Project;
/*    9:     */ import Objects.Sample;
/*   10:     */ import Prog.DataProcessor;
/*   11:     */ import java.awt.BorderLayout;
/*   12:     */ import java.awt.Color;
/*   13:     */ import java.awt.Dimension;
/*   14:     */ import java.awt.event.ActionEvent;
/*   15:     */ import java.awt.event.ActionListener;
/*   16:     */ import java.awt.event.MouseEvent;
/*   17:     */ import java.awt.event.MouseListener;
/*   18:     */ import java.io.BufferedWriter;
/*   19:     */ import java.io.File;
/*   20:     */ import java.io.FileWriter;
/*   21:     */ import java.io.IOException;
/*   22:     */ import java.io.PrintStream;
/*   23:     */ import java.util.ArrayList;
/*   24:     */ import java.util.Calendar;
/*   25:     */ import javax.swing.JButton;
/*   26:     */ import javax.swing.JCheckBox;
/*   27:     */ import javax.swing.JFileChooser;
/*   28:     */ import javax.swing.JLabel;
/*   29:     */ import javax.swing.JPanel;
/*   30:     */ import javax.swing.JScrollPane;
/*   31:     */ import javax.swing.JTextField;
/*   32:     */ import javax.swing.filechooser.FileFilter;
/**/			import javax.swing.JPopupMenu;
/**/			import javax.swing.JMenuItem;
/*   38:     */ import javax.swing.KeyStroke;
				import javax.swing.*;
/**/		  import java.io.PrintWriter;
/**/		  import java.io.File;
/*   33:     */ 

				//This is the activity Matrix Pane. If you are looning for anything that goes on in the Activity Matrix part of the GUI, this is probably where it happens.
				//

/*   34:     */ public class ActMatrixPane
/*   35:     */   extends JPanel
/*   36:     */ {
/*   37:     */   private static final long serialVersionUID = 1L;
/*   38:     */   private ArrayList<Sample> smpList_;
/*   39:     */   private JButton export_;
/*   40:     */   private JButton names_;
/*   41:     */   private JLabel label_;
/*   42:     */   private JCheckBox bySumcheck_;
/*   43:     */   private JCheckBox useOddsrat_;
/*   44:     */   private JCheckBox moveUnmappedToEnd;
/*   45:     */   private JCheckBox includeRepseq_;
/*   46:     */   private JCheckBox showOptions_;
/*   47:     */   private JCheckBox dispIncomplete_;
/*   48:     */   private JCheckBox useCsf_;
/*   49:     */   private JTextField maxVisField_;
/*   50:  57 */   private float maxVisVal_ = 0.0F;
/*   51:     */   private ArrayList<JLabel> nameLabels_;
/*   52:     */   private Project actProj_;
/*   53:  65 */   private boolean sortedEc = false;
/*   54:  66 */   private final int xSize = 130;
/*   55:  67 */   private final int ySize = 15;
/*   56:     */   private int sumIndexSmp;
/*   57:     */   private JButton resort_;
/*   58:     */   private ArrayList<Line> ecMatrix_;
/*   59:     */   private Loadingframe lframe;
/*   60:     */   private Line unmappedSum;
/*   61:     */   private Line mappedSum;
/*   62:     */   private Line incompleteSum;
/*   63:     */   private Line sums;
/*   64:     */   JPanel optionsPanel_;
/*   65:     */   JPanel displayP_;
/*   66:     */   JScrollPane showJPanel_;
/*   67:     */   DataProcessor proc_;
/*   68:  88 */   int selectedSampIndex_ = -1;
/*   69:     */   JLabel selectedSampText;
				  final String basePath_ = new File(".").getAbsolutePath() + File.separator;
				  JPopupMenu menuPopup;
				  int popupIndexY;
				  int popupIndexX;
/*   70:     */   
/*   71:     */   public ActMatrixPane(Project actProj, ArrayList<EcWithPathway> ecList, DataProcessor proc, Dimension dim)
/*   72:     */   {
/*   73:  92 */     this.lframe = new Loadingframe();
/*   74:  93 */     this.lframe.bigStep("Preparing Panel");
/*   75:  94 */     this.lframe.step("Init");
/*   76:     */     
/*   77:  96 */     this.actProj_ = actProj;
/*   78:  97 */     this.proc_ = proc;
/*   79:     */     
/*   80:     */ 
/*   81: 100 */     setLayout(new BorderLayout());
/*   82: 101 */     setVisible(true);
/*   83: 102 */     setBackground(Project.getBackColor_());
/*   84: 103 */     setSize(dim);
/*   85:     */     
/*   86: 105 */     this.smpList_ = Project.samples_;
/*   87: 106 */     this.sumIndexSmp = 0;
/*   88:     */     
/*   89: 108 */     setSelectedEc();
/*   90: 109 */     prepMatrix();
/*   91: 110 */     initMainPanels();
/*   92:     */     
/*   93: 112 */     prepaint();
/*   94:     */     
/*   95: 114 */     Loadingframe.close();
/*   96:     */   }
/*   97:     */   
/*   98:     */   private void prepaint()
/*   99:     */   {
/*  100: 117 */     removeAll();
/*  101: 118 */     initMainPanels();
/*  102: 119 */     addOptions();
/*  103: 120 */     drawSampleNames();
/*  104: 121 */     showEcValues();
/*  105:     */     
/*  106: 123 */     invalidate();
/*  107: 124 */     validate();
/*  108: 125 */     repaint();
/*  109:     */   }
/*  110:     */   
/*  111:     */   private void initMainPanels()
/*  112:     */   {
/*  113: 128 */     this.optionsPanel_ = new JPanel();
/*  114: 129 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth(), 100));
/*  115: 130 */     this.optionsPanel_.setLocation(0, 0);
/*  116: 131 */     this.optionsPanel_.setBackground(Project.standard);
/*  117: 132 */     this.optionsPanel_.setVisible(true);
/*  118: 133 */     this.optionsPanel_.setLayout(null);
/*  119: 134 */     add(this.optionsPanel_, "First");
/*  120:     */     
/*  121: 136 */     this.displayP_ = new JPanel();
/*  122: 137 */     this.displayP_.setLocation(0, 0);
/*  123: 138 */     this.displayP_.setPreferredSize(new Dimension((Project.samples_.size() + 2) * 130, (this.ecMatrix_.size() + 2) * 15 + 100));
/*  124: 139 */     this.displayP_.setSize(getPreferredSize());
/*  125: 140 */     this.displayP_.setBackground(Project.getBackColor_());
/*  126: 141 */     this.displayP_.setVisible(true);
/*  127: 142 */     this.displayP_.setLayout(null);
/*  128:     */     
/*  129: 144 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/*  130: 145 */     this.showJPanel_.setVisible(true);
/*  131: 146 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/*  132: 147 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/*  133:     */     
/*  134: 149 */     add("Center", this.showJPanel_);
/*  135:     */   }
/*  136:     */   
				
				  //This preps the onscreen matrix using the arrayline_[] variable in the line object.	
/*  137:     */   private void prepMatrix()
				  {
/*  139: 152 */     this.ecMatrix_ = new ArrayList<Line>();
/*  140: 153 */     this.lframe.bigStep("Preparing Matrix");
/*  141: 154 */     this.lframe.step("Init");
/*  142: 155 */     this.sumIndexSmp = 0;
/*  143: 158 */     for (int x = 0; x < Project.samples_.size(); x++) {
/*  144: 159 */       if (((Sample)Project.samples_.get(x)).inUse) {
/*  145: 160 */         this.sumIndexSmp += 1;
/*  146:     */       }
/*  147:     */     }
/*  148: 163 */     int indexSmp = -1;
/*  149:     */     
/*  150:     */ 
/*  151:     */ 
/*  152: 167 */     this.mappedSum = new Line(new double[this.sumIndexSmp], true, false, false);
/*  153: 168 */     this.mappedSum.fillWithZeros();
/*  154: 169 */     this.unmappedSum = new Line(new double[this.sumIndexSmp], false, true, false);
/*  155: 170 */     this.unmappedSum.fillWithZeros();
/*  156: 171 */     this.incompleteSum = new Line(new double[this.sumIndexSmp], false, false, true);
/*  157: 172 */     this.incompleteSum.fillWithZeros();
/*  158: 173 */     this.sums = new Line(new double[this.sumIndexSmp], false, false, false);
/*  159: 174 */     this.sums.fillWithZeros();
/*  160: 176 */     for (int x = 0; x < Project.samples_.size(); x++) {
/*  161: 177 */       if (((Sample)Project.samples_.get(x)).inUse)
/*  162:     */       {
/*  163: 178 */         indexSmp++;
/*  164: 179 */         ArrayList<EcWithPathway> actSample = ((Sample)Project.samples_.get(x)).ecs_;
/*  165: 181 */         for (int ecCnt = 0; ecCnt < actSample.size(); ecCnt++)
/*  166:     */         {
/*  167: 182 */           EcWithPathway actEc = (EcWithPathway)actSample.get(ecCnt);
/*  168: 183 */           actEc.addStats();
/*  169: 185 */           if (actEc.isSelected_)
/*  170:     */           {
/*  171: 188 */             boolean found = false;
/*  172: 189 */             this.lframe.step(actEc.name_);
/*  173: 190 */             for (int arrCnt = 0; arrCnt < this.ecMatrix_.size(); arrCnt++) {
/*  174: 191 */               if (actEc.name_.contentEquals(((Line)this.ecMatrix_.get(arrCnt)).getEc_().name_))
/*  175:     */               {
/*  176: 192 */                 found = true;
/*  177: 193 */                 ((Line)this.ecMatrix_.get(arrCnt)).arrayLine_[indexSmp] = actEc.amount_;
/*  178: 194 */                 this.sums.arrayLine_[indexSmp] += actEc.amount_;
/*  179: 195 */                 if ((actEc.isCompleteEc()) || (actEc.userEC))
/*  180:     */                 {
/*  181: 196 */                   if (actEc.isUnmapped())
/*  182:     */                   {
/*  183: 197 */                     this.unmappedSum.arrayLine_[indexSmp] += actEc.amount_; break;
/*  184:     */                   }
/*  185: 200 */                   this.mappedSum.arrayLine_[indexSmp] += actEc.amount_; break;
/*  186:     */                 }
/*  187: 204 */                 this.incompleteSum.arrayLine_[indexSmp] += actEc.amount_;
/*  188:     */                 
/*  189: 206 */                 break;
/*  190:     */               }
/*  191:     */             }
/*  192: 209 */             if (!found)
/*  193:     */             {
/*  194: 210 */               Line line = new Line(new EcWithPathway(actEc), new double[this.sumIndexSmp]);
/*  195: 211 */               line.fillWithZeros();
/*  196: 212 */               line.arrayLine_[indexSmp] = actEc.amount_;
/*  197: 213 */               this.sums.arrayLine_[indexSmp] += actEc.amount_;
/*  198: 214 */               if ((actEc.isCompleteEc()) || (actEc.userEC))
/*  199:     */               {
/*  200: 215 */                 if (actEc.isUnmapped()) {
/*  201: 216 */                   this.unmappedSum.arrayLine_[indexSmp] += actEc.amount_;
/*  202:     */                 } else {
/*  203: 219 */                   this.mappedSum.arrayLine_[indexSmp] += actEc.amount_;
/*  204:     */                 }
/*  205:     */               }
/*  206:     */               else {
/*  207: 223 */                 this.incompleteSum.arrayLine_[indexSmp] += actEc.amount_;
/*  208:     */               }
/*  209: 226 */               this.ecMatrix_.add(line);
/*  210:     */             }
/*  211:     */           }
/*  212:     */         }
/*  213:     */       }
/*  214:     */     }
/*  215: 232 */     for (int arrCnt = 0; arrCnt < this.ecMatrix_.size(); arrCnt++) {
/*  216: 233 */       ((Line)this.ecMatrix_.get(arrCnt)).setSum();
/*  217:     */     }
/*  218: 236 */     this.mappedSum.setSum();
/*  219: 237 */     this.unmappedSum.setSum();
/*  220: 238 */     this.incompleteSum.setSum();
/*  221: 239 */     this.sums.setSum();
/*  222: 240 */     unCompleteMover();
/*  223:     */   }
/*  224:     */   
/*  225:     */   private void addOptions()
/*  226:     */   {
/*  227: 245 */     this.lframe.bigStep("Adding options");
/*  228: 246 */     this.lframe.step("Buttons");
/*  229: 247 */     this.optionsPanel_.removeAll();
/*  230: 248 */     if (this.bySumcheck_ != null)
/*  231:     */     {
/*  232: 249 */       if (this.bySumcheck_.isSelected())
/*  233:     */       {
//*  234: 250 */         sortEcsbySumBubble();
						System.out.println("\nquicksort\n");
				    	quicksortSum();
				      	this.ecMatrix_=removeDuplicates();
						System.out.println("\nquicksort done\n");
/*  235: 251 */         System.out.println("bySum");
/*  236:     */       }
/*  237:     */       else
/*  238:     */       {
						sortEcsbyNameBubble();//This method used to be implemented in bubble sort before I changed the method to use quicksort. I kept the name though... just in case 
						this.ecMatrix_=removeDuplicates();
/*  240: 255 */         System.out.println("byName");
/*  241:     */       }
/*  242:     */     }
/*  243:     */     else
/*  244:     */     {
/*  245: 259 */       sortEcsbyNameBubble();
					  this.ecMatrix_=removeDuplicates();
/*  246: 260 */       System.out.println("byName");
/*  247:     */     }
/*  248: 263 */     if (this.showOptions_ == null)
/*  249:     */     {
/*  250: 264 */       this.showOptions_ = new JCheckBox("Show options");
/*  251: 265 */       this.showOptions_.addActionListener(new ActionListener()
/*  252:     */       {
/*  253:     */         public void actionPerformed(ActionEvent e)
/*  254:     */         {
/*  255: 270 */           ActMatrixPane.this.switchOptionsMode();
/*  256:     */         }
/*  257:     */       });
/*  258:     */     }
/*  259: 275 */     this.showOptions_.setVisible(true);
/*  260: 276 */     this.showOptions_.setSelected(true);
/*  261: 277 */     this.showOptions_.setBackground(this.optionsPanel_.getBackground());
/*  262: 278 */     this.showOptions_.setForeground(Project.getFontColor_());
/*  263: 279 */     this.showOptions_.setLayout(null);
/*  264: 280 */     this.showOptions_.setBounds(130, 10, 120, 15);
/*  265:     */     
/*  266: 282 */     this.optionsPanel_.add(this.showOptions_);
/*  267: 284 */     if (this.bySumcheck_ == null) {
/*  268: 285 */       this.bySumcheck_ = new JCheckBox("Sort by sum");
/*  269:     */     }
/*  270: 287 */     this.bySumcheck_.setVisible(true);
/*  271: 288 */     this.bySumcheck_.setLayout(null);
/*  272: 289 */     this.bySumcheck_.setBackground(this.optionsPanel_.getBackground());
/*  273: 290 */     this.bySumcheck_.setForeground(Project.getFontColor_());
/*  274: 291 */     this.bySumcheck_.setBounds(250, 10, 100, 15);
/*  275: 292 */     this.optionsPanel_.add(this.bySumcheck_);
/*  276: 294 */     if (this.useOddsrat_ == null) {
/*  277: 295 */       this.useOddsrat_ = new JCheckBox("Odds-ratio");
/*  278:     */     }
/*  279: 297 */     this.useOddsrat_.setVisible(true);
/*  280: 298 */     this.useOddsrat_.setLayout(null);
/*  281: 299 */     this.useOddsrat_.setBackground(this.optionsPanel_.getBackground());
/*  282: 300 */     this.useOddsrat_.setForeground(Project.getFontColor_());
/*  283: 301 */     this.useOddsrat_.setBounds(250, 27, 100, 15);
/*  284: 302 */     this.optionsPanel_.add(this.useOddsrat_);
/*  285: 304 */     if (this.useCsf_ == null) {
/*  286: 305 */       this.useCsf_ = new JCheckBox("CSF");
/*  287:     */     }
/*  288: 307 */     this.useCsf_.setVisible(true);
/*  289: 308 */     this.useCsf_.setLayout(null);
/*  290: 309 */     this.useCsf_.setBackground(this.optionsPanel_.getBackground());
/*  291: 310 */     this.useCsf_.setForeground(Project.getFontColor_());
/*  292: 311 */     this.useCsf_.setBounds(10, 44, 100, 15);
/*  293: 312 */     this.optionsPanel_.add(this.useCsf_);
/*  294: 314 */     if (this.export_ == null)
/*  295:     */     {
/*  296: 315 */       this.export_ = new JButton("Write to file");
/*  297: 316 */       this.export_.addActionListener(new ActionListener()
/*  298:     */       {
/*  299:     */         public void actionPerformed(ActionEvent e)
/*  300:     */         {
/*  301: 320 */           JFileChooser fChoose_ = new JFileChooser(Project.workpath_);
/*  302: 321 */           fChoose_.setFileSelectionMode(0);
/*  303: 322 */           fChoose_.setBounds(100, 100, 200, 20);
/*  304: 323 */           fChoose_.setVisible(true);
/*  305: 324 */           File file = new File(Project.workpath_);
/*  306: 325 */           fChoose_.setSelectedFile(file);
/*  307: 326 */           fChoose_.setFileFilter(new FileFilter()
/*  308:     */           {
/*  309:     */             public boolean accept(File f)
/*  310:     */             {
/*  311: 330 */               if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".txt"))) {
/*  312: 331 */                 return true;
/*  313:     */               }
/*  314: 333 */               return false;
/*  315:     */             }
/*  316:     */             
/*  317:     */             public String getDescription()
/*  318:     */             {
/*  319: 339 */               return ".txt";
/*  320:     */             }
/*  321:     */           });
/*  322: 343 */           if (fChoose_.showSaveDialog(null) == 0) {
/*  323:     */             try
/*  324:     */             {
/*  325: 345 */               String path = fChoose_.getSelectedFile().getCanonicalPath();
/*  326: 347 */               if (!path.endsWith(".txt"))
/*  327:     */               {
/*  328: 348 */                 path = path + ".txt";
/*  329: 349 */                 System.out.println(".txt");
/*  330:     */               }
/*  331: 351 */               ActMatrixPane.this.exportMat(path, ActMatrixPane.this.useCsf_.isSelected());
/*  332:     */             }
/*  333:     */             catch (IOException e1)
/*  334:     */             {
/*  335: 354 */               e1.printStackTrace();
/*  336:     */             }
/*  337:     */           }
/*  338:     */         }
/*  339:     */       });
/*  340:     */     }
/*  341: 361 */     this.export_.setBounds(10, 10, 100, 20);
/*  342: 362 */     this.export_.setVisible(true);
/*  343: 363 */     this.export_.setLayout(null);
/*  344: 364 */     this.export_.setForeground(Project.getFontColor_());
/*  345:     */     
/*  346: 366 */     this.optionsPanel_.add(this.export_);
/*  347: 370 */     if (this.moveUnmappedToEnd == null)
/*  348:     */     {
/*  349: 371 */       this.moveUnmappedToEnd = new JCheckBox("Unmapped at end of list");
/*  350: 372 */       this.moveUnmappedToEnd.setSelected(true);
/*  351:     */     }
/*  352: 375 */     this.moveUnmappedToEnd.setVisible(true);
/*  353: 376 */     this.moveUnmappedToEnd.setLayout(null);
/*  354: 377 */     this.moveUnmappedToEnd.setBackground(this.optionsPanel_.getBackground());
/*  355: 378 */     this.moveUnmappedToEnd.setForeground(Project.getFontColor_());
/*  356: 379 */     this.moveUnmappedToEnd.setBounds(360, 10, 200, 15);
/*  357: 380 */     this.optionsPanel_.add(this.moveUnmappedToEnd);
/*  358: 382 */     if (this.includeRepseq_ == null)
/*  359:     */     {
/*  360: 383 */       this.includeRepseq_ = new JCheckBox("Include Repseq-Ids");
/*  361: 384 */       this.includeRepseq_.setSelected(false);
/*  362:     */     }
/*  363: 387 */     this.includeRepseq_.setVisible(true);
/*  364: 388 */     this.includeRepseq_.setLayout(null);
/*  365: 389 */     this.includeRepseq_.setForeground(Project.getFontColor_());
/*  366: 390 */     this.includeRepseq_.setBackground(this.optionsPanel_.getBackground());
/*  367: 391 */     this.includeRepseq_.setBounds(360, 27, 200, 15);
/*  368: 392 */     this.optionsPanel_.add(this.includeRepseq_);
/*  369: 394 */     if (this.dispIncomplete_ == null)
/*  370:     */     {
/*  371: 395 */       this.dispIncomplete_ = new JCheckBox("Display incomplete ECs");
/*  372: 396 */       this.dispIncomplete_.setSelected(false);
/*  373:     */     }
/*  374: 399 */     this.dispIncomplete_.setVisible(true);
/*  375: 400 */     this.dispIncomplete_.setLayout(null);
/*  376: 401 */     this.dispIncomplete_.setForeground(Project.getFontColor_());
/*  377: 402 */     this.dispIncomplete_.setBackground(this.optionsPanel_.getBackground());
/*  378: 403 */     this.dispIncomplete_.setBounds(360, 44, 200, 15);
/*  379: 404 */     this.optionsPanel_.add(this.dispIncomplete_);
/*  380: 406 */     if (this.maxVisField_ == null) {
/*  381: 407 */       this.maxVisField_ = new JTextField((int)Math.round(this.maxVisVal_));
/*  382:     */     }
/*  383: 409 */     this.maxVisField_.setBounds(600, 10, 100, 20);
/*  384: 410 */     this.maxVisField_.setLayout(null);
/*  385: 411 */     this.maxVisField_.setVisible(true);
/*  386:     */     
/*  387:     */ 
/*  388:     */ 
/*  389: 415 */     this.resort_ = new JButton("Apply options");
/*  390:     */     
/*  391: 417 */     this.resort_.setBounds(700, 10, 200, 30);
/*  392: 418 */     this.resort_.setVisible(true);
/*  393: 419 */     this.resort_.setLayout(null);
/*  394: 420 */     this.resort_.setForeground(Project.getFontColor_());
/*  395: 421 */     this.resort_.addActionListener(new ActionListener()
/*  396:     */     {
/*  397:     */       public void actionPerformed(ActionEvent e)
/*  398:     */       {
/*  399: 426 */         ActMatrixPane.this.resort();
/*  400:     */       }
/*  401: 429 */     });
/*  402: 430 */     this.optionsPanel_.add(this.resort_);
/*  403:     */     
/*  404: 432 */     JLabel sampleSelectHint = new JLabel("Click on samplename to sort matrix.");
/*  405: 433 */     sampleSelectHint.setForeground(Project.getFontColor_());
/*  406: 434 */     sampleSelectHint.setBounds(0, 70, 250, 20);
/*  407: 435 */     this.optionsPanel_.add(sampleSelectHint);
/*  408:     */   }
/*  409:     */   
/*  410:     */   private void resort()
/*  411:     */   {
/*  412: 438 */     this.lframe = new Loadingframe();
/*  413: 439 */     this.lframe.bigStep("resorting");
/*  414: 440 */     this.sortedEc = false;

/*  415: 441 */     if (this.bySumcheck_.isSelected()) {
/*  416: 443 */       quicksortSum();
				      this.ecMatrix_=removeDuplicates();
/*  417:     */     } 
					else {
/*  418: 446 */       sortEcsbyNameBubble();
/*  419:     */     }

/*  420: 449 */     prepaint();
/*  421: 450 */     Loadingframe.close();
/*  422:     */   }
/*  423:     */   
/*  424:     */   private void drawSampleNames()
/*  425:     */   {
/*  426: 453 */     this.lframe.bigStep("drawSampleNames");
/*  427:     */     
/*  428: 455 */     this.nameLabels_ = new ArrayList();
/*  429: 456 */     String usedSampleText = "";
/*  430: 457 */     String name = "";
/*  431: 458 */     if (this.selectedSampIndex_ < 0) {
/*  432: 459 */       name = "Overall";
/*  433:     */     } else {
/*  434: 462 */       name = ((Sample)Project.samples_.get(this.selectedSampIndex_)).name_;
/*  435:     */     }
/*  436: 467 */     usedSampleText = "Matrix now sorted by " + name;
/*  437: 468 */     this.selectedSampText = new JLabel(usedSampleText);
/*  438: 469 */     this.selectedSampText.setBounds(0, 83, 800, 20);
/*  439: 470 */     this.selectedSampText.setForeground(Project.getFontColor_());
/*  440: 471 */     this.optionsPanel_.add(this.selectedSampText);
/*  441: 472 */     int x = 0;
/*  442: 473 */     for (x = 0; x < Project.samples_.size(); x++) {
/*  443: 474 */       if (((Sample)Project.samples_.get(x)).inUse)
/*  444:     */       {
/*  445: 477 */         this.lframe.step(((Sample)this.smpList_.get(x)).name_);
/*  446: 478 */         this.label_ = new JLabel(((Sample)this.smpList_.get(x)).name_);
/*  447:     */         
/*  448:     */ 
/*  449:     */ 
/*  450: 482 */         this.label_.setForeground(((Sample)this.smpList_.get(x)).sampleCol_);
/*  451: 483 */         this.label_.setBounds(50 + (x + 1) * 130, 20, 130, 15);
/*  452: 484 */         if (this.selectedSampIndex_ == x) {
/*  453: 485 */           this.label_.setBounds(this.label_.getX(), this.label_.getY() - 20, 400, this.label_.getHeight());
/*  454:     */         }
/*  455: 487 */         this.label_.setVisible(true);
/*  456: 488 */         this.label_.setLayout(null);
/*  457: 489 */         final int nameIndex = x;
/*  458:     */         
/*  459: 491 */         this.nameLabels_.add(this.label_);
/*  460: 492 */         this.displayP_.add(this.label_);
/*  461:     */         
/*  462: 494 */         final JPanel mousOverP = new JPanel();
/*  463: 495 */         mousOverP.setBounds(50 + (x + 1) * 130, 0, 130, 40);
/*  464:     */         
/*  465: 497 */         mousOverP.setBackground(Project.getBackColor_());
/*  466:     */         
/*  467:     */ 
/*  468:     */ 
/*  469:     */ 
/*  470: 502 */         mousOverP.setVisible(true);
/*  471: 503 */         mousOverP.setLayout(null);
/*  472: 504 */         final int index = x;
/*  473: 505 */         mousOverP.addMouseListener(new MouseListener()
/*  474:     */         {
/*  475:     */           public void mouseClicked(MouseEvent e)
/*  476:     */           {
/*  477: 510 */             if (ActMatrixPane.this.selectedSampIndex_ != index)
/*  478:     */             {
/*  479: 511 */               ActMatrixPane.this.selectedSampIndex_ = index;
/*  480: 512 */               ActMatrixPane.this.selectedSampText = new JLabel("<html><body>using sampleNr.: " + ActMatrixPane.this.selectedSampIndex_ + "<br> as reference.</body></html>");
/*  481: 513 */               mousOverP.setBackground(Project.standard);
/*  482: 514 */               ((JLabel)ActMatrixPane.this.nameLabels_.get(nameIndex)).setBackground(Project.standard);
/*  483: 515 */               ActMatrixPane.this.bySumcheck_.setSelected(true);
/*  484:     */             }
/*  485:     */             else
/*  486:     */             {
/*  487: 518 */               ActMatrixPane.this.selectedSampIndex_ = -1;
/*  488: 519 */               ActMatrixPane.this.selectedSampText = new JLabel("<html><body>using sampleNr.: " + ActMatrixPane.this.selectedSampIndex_ + "<br> as reference.</body></html>");
/*  489: 520 */               mousOverP.setBackground(Project.getBackColor_());
/*  490:     */             }
/*  491: 522 */             ActMatrixPane.this.resort();
/*  492:     */           }
/*  493:     */           
/*  494:     */           public void mouseEntered(MouseEvent e)
/*  495:     */           {
/*  496: 528 */             if (ActMatrixPane.this.selectedSampIndex_ != index)
/*  497:     */             {
/*  498: 529 */               ((JLabel)ActMatrixPane.this.nameLabels_.get(nameIndex)).setBounds(((JLabel)ActMatrixPane.this.nameLabels_.get(nameIndex)).getX(), ((JLabel)ActMatrixPane.this.nameLabels_.get(nameIndex)).getY() - 20, 400, ActMatrixPane.this.label_.getHeight());
/*  499: 530 */               if (ActMatrixPane.this.selectedSampIndex_ != -1) {
/*  500: 531 */                 ((JLabel)ActMatrixPane.this.nameLabels_.get(ActMatrixPane.this.selectedSampIndex_)).setBounds(((JLabel)ActMatrixPane.this.nameLabels_.get(ActMatrixPane.this.selectedSampIndex_)).getX(), ((JLabel)ActMatrixPane.this.nameLabels_.get(ActMatrixPane.this.selectedSampIndex_)).getY() + 20, 130, ActMatrixPane.this.label_.getHeight());
/*  501:     */               }
/*  502:     */             }
/*  503: 534 */             ActMatrixPane.this.repaint();
/*  504:     */           }
/*  505:     */           
/*  506:     */           public void mouseExited(MouseEvent e)
/*  507:     */           {
/*  508: 540 */             if (ActMatrixPane.this.selectedSampIndex_ != index)
/*  509:     */             {
/*  510: 541 */               ((JLabel)ActMatrixPane.this.nameLabels_.get(nameIndex)).setBounds(((JLabel)ActMatrixPane.this.nameLabels_.get(nameIndex)).getX(), ((JLabel)ActMatrixPane.this.nameLabels_.get(nameIndex)).getY() + 20, 130, ActMatrixPane.this.label_.getHeight());
/*  511: 542 */               if (ActMatrixPane.this.selectedSampIndex_ != -1) {
/*  512: 543 */                 ((JLabel)ActMatrixPane.this.nameLabels_.get(ActMatrixPane.this.selectedSampIndex_)).setBounds(((JLabel)ActMatrixPane.this.nameLabels_.get(ActMatrixPane.this.selectedSampIndex_)).getX(), ((JLabel)ActMatrixPane.this.nameLabels_.get(ActMatrixPane.this.selectedSampIndex_)).getY() - 20, 400, ActMatrixPane.this.label_.getHeight());
/*  513:     */               }
/*  514:     */             }
/*  515: 546 */             ActMatrixPane.this.repaint();
/*  516:     */           }
/*  517:     */           
/*  518:     */           public void mousePressed(MouseEvent e) {}
/*  519:     */           
/*  520:     */           public void mouseReleased(MouseEvent e) {}
/*  521: 561 */         });
/*  522: 562 */         this.displayP_.add(mousOverP);
/*  523:     */       }
/*  524:     */     }
/*  525: 564 */     this.label_ = new JLabel(" sum");
/*  526:     */     
/*  527:     */ 
/*  528:     */ 
/*  529: 568 */     this.label_.setForeground(Color.black);
/*  530: 569 */     this.label_.setBounds(50 + (x + 1) * 130, 20, 130, 15);
/*  531: 570 */     this.label_.setVisible(true);
/*  532: 571 */     this.label_.setLayout(null);
/*  533:     */     
/*  534: 573 */     this.displayP_.add(this.label_);
/*  535:     */   }
/*  536:     */   
/*  537:     */   private void showEcValues()
/*  538:     */   {
/*  539: 577 */     boolean sumLineDrawn = false;
/*  540: 578 */     this.lframe.bigStep("showEcValues");
/*  541: 579 */     int ecCnt = 0;
/*  542: 580 */     for (ecCnt = 0; ecCnt < this.ecMatrix_.size(); ecCnt++)
/*  543:     */     {
/*  544: 581 */       Line ecNr = (Line)this.ecMatrix_.get(ecCnt);
//*  545: 582 */       System.out.println(ecNr.getEc_().name_);
/*  546: 583 */       this.lframe.step(ecNr.getEc_().name_);
/*  547: 584 */       if (((!ecNr.getEc_().isCompleteEc()) && (!sumLineDrawn)) || (ecNr.getEc_().userEC))
/*  548:     */       {
/*  549: 586 */         addEcButton(this.mappedSum, ecCnt + 1);
/*  550: 587 */         showValues(this.mappedSum, ecCnt + 1);
/*  551:     */         
/*  552: 589 */         addEcButton(this.unmappedSum, ecCnt + 2);
/*  553: 590 */         showValues(this.unmappedSum, ecCnt + 2);
/*  554:     */         
/*  555: 592 */         sumLineDrawn = true;
/*  556: 594 */         if (!this.dispIncomplete_.isSelected()) {
/*  557:     */           break;
/*  558:     */         }
/*  559:     */       }
/*  560: 599 */       addEcButton(ecNr, ecCnt);
/*  561: 600 */       if (this.useOddsrat_.isSelected()) {
/*  562: 601 */         showOdds(ecNr, ecCnt);
/*  563:     */       } else {
/*  564: 604 */         showValues(ecNr, ecCnt);
/*  565:     */       }
/*  566:     */     }
/*  567: 607 */     if (this.dispIncomplete_.isSelected())
/*  568:     */     {
/*  569: 608 */       addEcButton(this.incompleteSum, ecCnt);
/*  570: 609 */       showValues(this.incompleteSum, ecCnt);
/*  571: 610 */       addEcButton(this.sums, ecCnt + 1);
/*  572: 611 */       showValues(this.sums, ecCnt + 1);
/*  573:     */     }
/*  574:     */     else
/*  575:     */     {
/*  576: 614 */       addEcButton(this.sums, ecCnt);
/*  577: 615 */       showValues(this.sums, ecCnt);
/*  578:     */     }
/*  579:     */   }
/*  580:     */   
/*  581:     */   private void showOdds(Line ecNr, int index)
/*  582:     */   {
/*  583: 622 */     if (ecNr.isSumline_())
/*  584:     */     {
/*  585: 623 */       addSumLineVals(ecNr, index);
/*  586: 624 */       return;
/*  587:     */     }
/*  588: 627 */     int uncompleteOffset = 0;
/*  589: 629 */     if (!ecNr.getEc_().isCompleteEc()) {
/*  590: 630 */       uncompleteOffset = 50;
/*  591:     */     }
/*  592: 632 */     for (int smpCnt = 0; smpCnt < ecNr.arrayLine_.length; smpCnt++)
/*  593:     */     {
/*  594: 633 */       if (ecNr.arrayLine_[smpCnt] != 0.0D)
/*  595:     */       {
/*  596: 634 */         float a = (float)ecNr.arrayLine_[smpCnt];
/*  597: 635 */         float b = (float)(this.sums.arrayLine_[smpCnt] - a);
/*  598: 636 */         float c = ecNr.sum_ - a;
/*  599: 637 */         float d = this.sums.sum_ - a - b - c;
/*  600: 638 */         this.label_ = new JLabel(String.valueOf(odds(a, b, c, d)));
/*  601: 639 */         this.lframe.step("adding " + ecNr.arrayLine_[smpCnt]);
/*  602: 640 */         if (this.includeRepseq_.isSelected())
/*  603:     */         {
/*  604: 641 */           final int indexY = index;
/*  605: 642 */           final int indexX = smpCnt;
/*  606: 643 */           JMenuItem mItem = new JMenuItem("Export");
    					  menuPopup = new JPopupMenu();

/*  676: 729 */           menuPopup.add(mItem);
        				  ActMatrixPane.this.setComponentPopupMenu(menuPopup);
        				  
        				  mItem.addActionListener(new ActionListener()//this is the little popup menu that comes up when you right click any of the ECs when the "include repseqs" option is selected
				    	  {
				    	    public void actionPerformed(ActionEvent e)
				    	    {
				    	     	EcNr ecTmp = new EcNr(((Line)ActMatrixPane.this.ecMatrix_.get(popupIndexY)).getEc_());
/*  681: 735 */           	    ecTmp.amount_ = ((int)((Line)ActMatrixPane.this.ecMatrix_.get(popupIndexY)).arrayLine_[indexX]);
/*  682: 736 */             	ArrayList<ConvertStat> reps = new ArrayList();
/*  683: 737 */             	for (int statsCnt = 0; statsCnt < ((Sample)Project.samples_.get(popupIndexX)).conversions_.size(); statsCnt++) {
								  String test=(((ConvertStat)((Sample)Project.samples_.get(popupIndexX)).conversions_.get(statsCnt)).getDesc_());
/*  684: 738 */             	  if ((ecTmp.name_.contentEquals(((ConvertStat)((Sample)Project.samples_.get(popupIndexX)).conversions_.get(statsCnt)).getEcNr_())) &&
									!test.contains("\t") ){
/*  685: 739 */                     reps.add((ConvertStat)((Sample)Project.samples_.get(popupIndexX)).conversions_.get(statsCnt));
/*  686:     */                   }
/*  687:     */             	}

								String test="";
				  				String test2="";
				  				for(int i=reps.size()-1;i>=0;i--){
				  					if((reps.get(i)==null)){}
				  					else{
				  						test=((ConvertStat)reps.get(i)).getDesc_();
//				  						System.out.println("1  "+test);
				  						if(test.contains("\t")){
				  							reps.set(i,null);
				  						}
				  						else{
//				  						innerloop:
				  						for(int j=i-1;j>=0;j--){
				  								if((reps.get(j)==null)){}
				  								else{
				  									test2=((ConvertStat)reps.get(j)).getDesc_();
//				  									System.out.println("2  "+  test2);
				  									if(test.contains(test2)){
				  										reps.set(j,null);
				  									}
						  						}
				  							}
				  						}
				  					}
				  				}
				  				for(int i=reps.size()-1;i>=0;i--){
				  					if(reps.get(i)==null){
				  						reps.remove(i);
				  					}
				  				}

								String sampName=((Sample)Project.samples_.get(popupIndexX)).name_;
/*  688: 743 */             	ExportReps(reps, ecTmp, sampName);
				    		}
				    	  });

						  this.label_.addMouseListener(new MouseListener()//this opens up the window when you left click the ECs with "include repseqs" selected
/*  677:     */           {
/*  678:     */             public void mouseClicked(MouseEvent e)
/*  679:     */             {
								if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()){
                     				System.out.println("Right Button Pressed");
    								ActMatrixPane.this.menuPopup.show(e.getComponent(), e.getX(), e.getY());
    								popupIndexY=indexY;
    								popupIndexX=indexX;	
                  			  	}
                  			  	else{
/*  680: 734 */                 	EcNr ecTmp = new EcNr(((Line)ActMatrixPane.this.ecMatrix_.get(indexY)).getEc_());
/*  681: 735 */                 	ecTmp.amount_ = ((int)((Line)ActMatrixPane.this.ecMatrix_.get(indexY)).arrayLine_[indexX]);
/*  682: 736 */                 	ArrayList<ConvertStat> reps = new ArrayList();
/*  683: 737 */                 	for (int statsCnt = 0; statsCnt < ((Sample)Project.samples_.get(indexX)).conversions_.size(); statsCnt++) {
									  String test=(((ConvertStat)((Sample)Project.samples_.get(indexX)).conversions_.get(statsCnt)).getDesc_());
/*  684: 738 */                 	  if ((ecTmp.name_.contentEquals(((ConvertStat)((Sample)Project.samples_.get(indexX)).conversions_.get(statsCnt)).getEcNr_())) &&
										  !test.contains("\t") ){
/*  685: 739 */                 	    reps.add((ConvertStat)((Sample)Project.samples_.get(indexX)).conversions_.get(statsCnt));
/*  686:     */                 	  }
/*  687:     */                 	}

									String test="";
				  					String test2="";
				  					for(int i=reps.size()-1;i>=0;i--){
				  						if((reps.get(i)==null)){}
				  						else{
				  							test=((ConvertStat)reps.get(i)).getDesc_();
//				  							System.out.println("1  "+test);
				  							if(test.contains("\t")){
				  								reps.set(i,null);
				  							}
				  							else{
//				  								innerloop:
				  								for(int j=i-1;j>=0;j--){
				  									if((reps.get(j)==null)){}
				  									else{
				  										test2=((ConvertStat)reps.get(j)).getDesc_();
//				  										System.out.println("2  "+  test2);
				  										if(test.contains(test2)){
				  											reps.set(j,null);
				  										}
						  							}
				  								}
				  							}
				  						}
				  					}
				  					for(int i=reps.size()-1;i>=0;i--){
				  						if(reps.get(i)==null){
				  							reps.remove(i);
				  						}
				  					}

									String sampName=((Sample)Project.samples_.get(indexX)).name_;
/*  688: 743 */                 	RepseqFrame repFrame = new RepseqFrame(reps, ecTmp, sampName);
								}
/*  689:     */             }
/*  690:     */             
/*  691:     */             public void mouseEntered(MouseEvent e) {}
/*  692:     */             
/*  693:     */             public void mouseExited(MouseEvent e) {}
/*  694:     */             
/*  695:     */             public void mousePressed(MouseEvent e) {}
/*  696:     */             
/*  697:     */             public void mouseReleased(MouseEvent e) {}
/*  698:     */           });
/*  629:     */         }
/*  630:     */       }
/*  631:     */       else
/*  632:     */       {
/*  633: 688 */         this.label_ = new JLabel("0");
/*  634:     */       }
/*  635: 690 */       this.label_.setBounds(50 + (smpCnt + 1) * 130, uncompleteOffset + 50 + index * 15, 130, 15);
/*  636: 691 */       this.label_.setVisible(true);
/*  637: 692 */       this.label_.setLayout(null);
/*  638: 693 */       this.displayP_.add(this.label_);
/*  639:     */     }
/*  640: 696 */     if (ecNr.sum_ != 0)
/*  641:     */     {
/*  642: 697 */       this.label_ = new JLabel(String.valueOf(ecNr.sum_));
/*  643: 698 */       this.lframe.step("adding " + ecNr.sum_);
/*  644:     */     }
/*  645:     */     else
/*  646:     */     {
/*  647: 702 */       this.label_ = new JLabel("0");
/*  648:     */     }
/*  649: 704 */     this.label_.setBounds(50 + (ecNr.arrayLine_.length + 1) * 130, uncompleteOffset + 50 + index * 15, 130, 15);
/*  650: 705 */     this.label_.setVisible(true);
/*  651: 706 */     this.label_.setLayout(null);
/*  652: 707 */     this.displayP_.add(this.label_);
/*  653:     */   }
/*  654:     */   
/*  655:     */   private void showValues(Line ecNr, int index)
/*  656:     */   {
/*  657: 711 */     if (ecNr.isSumline_())
/*  658:     */     {
/*  659: 712 */       addSumLineVals(ecNr, index);
/*  660: 713 */       return;
/*  661:     */     }
/*  662: 716 */     int uncompleteOffset = 0;
/*  663: 718 */     if ((!ecNr.getEc_().isCompleteEc()) && (!ecNr.isMappedSums_()) && (!ecNr.isUnMappedSums_())) {
/*  664: 719 */       uncompleteOffset = 50;
/*  665:     */     }
/*  666: 721 */     for (int smpCnt = 0; smpCnt < ecNr.arrayLine_.length; smpCnt++)
/*  667:     */     {
/*  668: 722 */       if (ecNr.arrayLine_[smpCnt] != 0.0D)
/*  669:     */       {
/*  670: 723 */         this.label_ = new JLabel(String.valueOf((int)ecNr.arrayLine_[smpCnt]));
/*  671: 724 */         this.lframe.step("adding " + ecNr.arrayLine_[smpCnt]);
/*  672: 726 */         if (this.includeRepseq_.isSelected())
/*  673:     */         {
/*  674: 727 */           final int indexY = index;
/*  675: 728 */           final int indexX = smpCnt;

						  JMenuItem mItem = new JMenuItem("Export");
    					  menuPopup = new JPopupMenu();

/*  676: 729 */           menuPopup.add(mItem);
        				  ActMatrixPane.this.setComponentPopupMenu(menuPopup);
        				  
        				  mItem.addActionListener(new ActionListener()//this is the little popup menu that comes up when you right click any of the ECs when the "include repseqs" option is selected
				    	  {
				    	    public void actionPerformed(ActionEvent e)
				    	    {
				    	    	EcNr ecTmp = new EcNr(((Line)ActMatrixPane.this.ecMatrix_.get(popupIndexY)).getEc_());
/*  681: 735 */           		ecTmp.amount_ = ((int)((Line)ActMatrixPane.this.ecMatrix_.get(popupIndexY)).arrayLine_[indexX]);
/*  682: 736 */             	ArrayList<ConvertStat> reps = new ArrayList();
/*  683: 737 */             	for (int statsCnt = 0; statsCnt < ((Sample)Project.samples_.get(popupIndexX)).conversions_.size(); statsCnt++) {
								  String test=(((ConvertStat)((Sample)Project.samples_.get(popupIndexX)).conversions_.get(statsCnt)).getDesc_());
/*  684: 738 */             	  if ((ecTmp.name_.contentEquals(((ConvertStat)((Sample)Project.samples_.get(popupIndexX)).conversions_.get(statsCnt)).getEcNr_())) &&
									!test.contains("\t") ){
/*  685: 739 */                     reps.add((ConvertStat)((Sample)Project.samples_.get(popupIndexX)).conversions_.get(statsCnt));
/*  686:     */                   }
/*  687:     */             	}
								
								String test="";
				  				String test2="";
				  				for(int i=reps.size()-1;i>=0;i--){
				  					if((reps.get(i)==null)){}
				  					else{
				  						test=((ConvertStat)reps.get(i)).getDesc_();
//				  						System.out.println("1  "+test);
				  						if(test.contains("\t")){
				  							reps.set(i,null);
				  						}
				  						else{
//				  							innerloop:
				  							for(int j=i-1;j>=0;j--){
				  								if((reps.get(j)==null)){}
				  								else{
				  									test2=((ConvertStat)reps.get(j)).getDesc_();
//				  									System.out.println("2  "+  test2);
				  									if(test.contains(test2)){
				  										reps.set(j,null);
				  									}
						  						}
				  							}
				  						}
				  					}
				  				}
				  				for(int i=reps.size()-1;i>=0;i--){
				  					if(reps.get(i)==null){
				  						reps.remove(i);
				  					}
				  				}

								String sampName=((Sample)Project.samples_.get(popupIndexX)).name_;
/*  688: 743 */             	ExportReps(reps, ecTmp, sampName);
				    		}
				    	  });

						  this.label_.addMouseListener(new MouseListener()//this opens up the window when you left click the ECs with "include repseqs" selected
/*  677:     */           {
/*  678:     */             public void mouseClicked(MouseEvent e)
/*  679:     */             {
								if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()){
                     				System.out.println("Right Button Pressed");
    								ActMatrixPane.this.menuPopup.show(e.getComponent(), e.getX(), e.getY());
    								popupIndexY=indexY;
    								popupIndexX=indexX;
                  			  	}
                  			  	else{
/*  680: 734 */                 	EcNr ecTmp = new EcNr(((Line)ActMatrixPane.this.ecMatrix_.get(indexY)).getEc_());
/*  681: 735 */                 	ecTmp.amount_ = ((int)((Line)ActMatrixPane.this.ecMatrix_.get(indexY)).arrayLine_[indexX]);
/*  682: 736 */                 	ArrayList<ConvertStat> reps = new ArrayList();
/*  683: 737 */                 	for (int statsCnt = 0; statsCnt < ((Sample)Project.samples_.get(indexX)).conversions_.size(); statsCnt++) {
/*  684: 738 */                 	  if (ecTmp.name_.contentEquals(((ConvertStat)((Sample)Project.samples_.get(indexX)).conversions_.get(statsCnt)).getEcNr_())) {
/*  685: 739 */                 	    reps.add((ConvertStat)((Sample)Project.samples_.get(indexX)).conversions_.get(statsCnt));
/*  686:     */                 	  }
/*  687:     */                 	}

									String test="";
				  					String test2="";
				  					for(int i=reps.size()-1;i>=0;i--){
				  						if((reps.get(i)==null)){}
				  						else{
				  							test=((ConvertStat)reps.get(i)).getDesc_();
//				  							System.out.println("1  "+test);
				  							if(test.contains("\t")){
				  								reps.set(i,null);
				  							}
				  							else{
//				  								innerloop:
				  								for(int j=i-1;j>=0;j--){
				  									if((reps.get(j)==null)){}
				  									else{
				  										test2=((ConvertStat)reps.get(j)).getDesc_();
//				  										System.out.println("2  "+  test2);
				  										if(test.contains(test2)){
				  											reps.set(j,null);
				  										}
						  							}
				  								}
				  							}
				  						}
				  					}
				  					for(int i=reps.size()-1;i>=0;i--){
				  						if(reps.get(i)==null){
				  							reps.remove(i);
				  						}
				  					}

									String sampName=((Sample)Project.samples_.get(indexX)).name_;
/*  688: 743 */                 	RepseqFrame repFrame = new RepseqFrame(reps, ecTmp, sampName);
								}
/*  689:     */             }
/*  690:     */             
/*  691:     */             public void mouseEntered(MouseEvent e) {}
/*  692:     */             
/*  693:     */             public void mouseExited(MouseEvent e) {}
/*  694:     */             
/*  695:     */             public void mousePressed(MouseEvent e) {}
/*  696:     */             
/*  697:     */             public void mouseReleased(MouseEvent e) {}
/*  698:     */           });
/*  699:     */         }
/*  700:     */       }
/*  701:     */       else
/*  702:     */       {
/*  703: 774 */         this.label_ = new JLabel("0");
/*  704:     */       }
/*  705: 776 */       this.label_.setBounds(50 + (smpCnt + 1) * 130, uncompleteOffset + 50 + index * 15, 130, 15);
/*  706: 777 */       this.label_.setVisible(true);
/*  707: 778 */       this.label_.setLayout(null);
/*  708: 779 */       this.displayP_.add(this.label_);
/*  709:     */     }
/*  710: 782 */     if (ecNr.sum_ != 0)
/*  711:     */     {
/*  712: 783 */       this.label_ = new JLabel(String.valueOf(ecNr.sum_));
/*  713: 784 */       this.lframe.step("adding " + ecNr.sum_);
/*  714:     */     }
/*  715:     */     else
/*  716:     */     {
/*  717: 788 */       this.label_ = new JLabel("0");
/*  718:     */     }
/*  719: 790 */     this.label_.setBounds(50 + (ecNr.arrayLine_.length + 1) * 130, uncompleteOffset + 50 + index * 15, 130, 15);
/*  720: 791 */     this.label_.setVisible(true);
/*  721: 792 */     this.label_.setLayout(null);
/*  722: 793 */     this.displayP_.add(this.label_);
/*  723:     */   }
/*  724:     */   
				  public void ExportReps(ArrayList<ConvertStat> reps, EcNr ecNr, String sampName)//exports sequence ids of a particular EC to RepSeqIDs 
				  {
					String text="";
               		String test="";
				    System.out.println("Reps:"+reps.size());
				    for (int repCnt = 0; repCnt < reps.size(); repCnt++)
					{
					  int amount = ((ConvertStat)reps.get(repCnt)).getEcAmount_();
					  if (((ConvertStat)reps.get(repCnt)).getPfamToEcAmount_() > amount) {
					    amount = ((ConvertStat)reps.get(repCnt)).getPfamToEcAmount_();
					  }
					  test=((ConvertStat)reps.get(repCnt)).getDesc_();
					  if(!test.contains("\t")){
/* 140:148 */       	text = text + ((ConvertStat)reps.get(repCnt)).getDesc_();
/* 141:149 */       	text = text + "\n";
					  } 
					}
//					System.out.println("Text:\n"+text);
				    try
				    {
				      String sampleName;
				      if(sampName.contains(".out")){
				      	sampleName=sampName.replace(".out","");
				      }
				      else{
				      	sampleName=sampName;
				      }
				      File file = new File(basePath_+"RepSeqIDs"+File.separator+sampleName+"-"+ecNr.name_+".txt");
//				      file.getParentFile().mkdirs();
				      PrintWriter printWriter=new PrintWriter(file);
					  printWriter.println(""+text);
//					  System.out.println("Text:\n"+text);
					  printWriter.close (); 
					}
					catch (IOException e1)
					{
					  e1.printStackTrace();
					}
				  }

/*  725:     */   private void addSumLineVals(Line ecNr, int index)
/*  726:     */   {
/*  727: 797 */     int uncompleteOffset = 0;
/*  728: 798 */     if ((!ecNr.isMappedSums_()) && (!ecNr.isUnMappedSums_())) {
/*  729: 799 */       uncompleteOffset = 50;
/*  730:     */     }
/*  731: 801 */     for (int smpCnt = 0; smpCnt < ecNr.arrayLine_.length; smpCnt++)
/*  732:     */     {
/*  733: 802 */       if (ecNr.arrayLine_[smpCnt] != 0.0D)
/*  734:     */       {
/*  735: 803 */         this.label_ = new JLabel(String.valueOf((int)ecNr.arrayLine_[smpCnt]));
/*  736: 804 */         this.lframe.step("adding " + ecNr.arrayLine_[smpCnt]);
/*  737:     */       }
/*  738:     */       else
/*  739:     */       {
/*  740: 808 */         this.label_ = new JLabel("0");
/*  741:     */       }
/*  742: 810 */       this.label_.setBounds(50 + (smpCnt + 1) * 130, uncompleteOffset + 50 + index * 15, 130, 15);
/*  743: 811 */       this.label_.setVisible(true);
/*  744: 812 */       this.label_.setLayout(null);
/*  745: 813 */       this.displayP_.add(this.label_);
/*  746:     */     }
/*  747: 815 */     if (ecNr.sum_ != 0)
/*  748:     */     {
/*  749: 816 */       this.label_ = new JLabel(String.valueOf(ecNr.sum_));
/*  750: 817 */       this.lframe.step("adding " + ecNr.sum_);
/*  751:     */     }
/*  752:     */     else
/*  753:     */     {
/*  754: 821 */       this.label_ = new JLabel("0");
/*  755:     */     }
/*  756: 823 */     this.label_.setBounds(50 + (ecNr.arrayLine_.length + 1) * 130, uncompleteOffset + 50 + index * 15, 130, 15);
/*  757: 824 */     this.label_.setVisible(true);
/*  758: 825 */     this.label_.setLayout(null);
/*  759: 826 */     this.displayP_.add(this.label_);
/*  760:     */   }
/*  761:     */   
/*  762:     */   private void addEcButton(Line ecNr, int index)
/*  763:     */   {
/*  764: 831 */     this.lframe.bigStep("Adding ecButtons");
/*  765: 832 */     if (!ecNr.isSumline_())
/*  766:     */     {
/*  767: 833 */       ecNr.getEc_().addStats();
/*  768: 834 */       this.names_ = new JButton(ecNr.getEc_().name_ + ecNr.getEc_().nameSuppl());
/*  769: 835 */       int uncompleteOffset = 0;
/*  770: 837 */       if (!ecNr.getEc_().isCompleteEc()) {
/*  771: 838 */         uncompleteOffset = 50;
/*  772:     */       }
/*  773: 840 */       this.names_.setBounds(50, uncompleteOffset + 50 + index * 15, 130, 15);
/*  774: 841 */       this.names_.setVisible(true);
/*  775: 842 */       this.names_.setLayout(null);
/*  776: 843 */       this.names_.setForeground(Project.getFontColor_());
/*  777: 844 */       this.lframe.step(this.names_.getText());
/*  778: 845 */       final int i = index;
/*  779: 846 */       ecNr.getEc_().amount_ = ecNr.sum_;
/*  780: 847 */       this.names_.addActionListener(new ActionListener()
/*  781:     */       {
/*  782:     */         public void actionPerformed(ActionEvent e)
/*  783:     */         {
/*  784: 854 */           PwInfoFrame frame = new PwInfoFrame(((Line)ActMatrixPane.this.ecMatrix_.get(i)).getEc_(), ActMatrixPane.this.actProj_, Project.overall_);
/*  785:     */         }
/*  786:     */       });
/*  787:     */     }
/*  788:     */     else
/*  789:     */     {
/*  790: 861 */       int uncompleteOffset = 0;
/*  791: 862 */       if (ecNr.isMappedSums_())
/*  792:     */       {
/*  793: 863 */         this.names_ = new JButton("MappedECs");
/*  794:     */       }
/*  795: 865 */       else if (ecNr.isUnMappedSums_())
/*  796:     */       {
/*  797: 866 */         this.names_ = new JButton("UnMappedECs");
/*  798:     */       }
/*  799: 868 */       else if (ecNr.isincompleteSums_())
/*  800:     */       {
/*  801: 869 */         this.names_ = new JButton("InCompleteECs");
/*  802: 870 */         uncompleteOffset = 50;
/*  803:     */       }
/*  804:     */       else
/*  805:     */       {
/*  806: 873 */         this.names_ = new JButton("All ECs");
/*  807: 874 */         uncompleteOffset = 50;
/*  808:     */       }
/*  809: 877 */       this.names_.setBounds(50, uncompleteOffset + 50 + index * 15, 130, 15);
/*  810: 878 */       this.names_.setVisible(true);
/*  811: 879 */       this.names_.setLayout(null);
/*  812: 880 */       this.names_.setForeground(Project.getFontColor_());
/*  813: 881 */       this.lframe.step(this.names_.getText());
/*  814:     */     }
/*  815: 884 */     this.displayP_.add(this.names_);
/*  816:     */   }
/*  817:     */   
/*  818:     */   private void switchOptionsMode()
/*  819:     */   {
/*  820: 887 */     if (this.showOptions_.isSelected())
/*  821:     */     {
/*  822: 888 */       this.useOddsrat_.setVisible(true);
/*  823: 889 */       this.bySumcheck_.setVisible(true);
/*  824: 890 */       this.moveUnmappedToEnd.setVisible(true);
/*  825: 891 */       this.resort_.setVisible(true);
/*  826: 892 */       this.maxVisField_.setVisible(true);
/*  827: 893 */       this.includeRepseq_.setVisible(true);
/*  828:     */     }
/*  829:     */     else
/*  830:     */     {
/*  831: 896 */       this.useOddsrat_.setVisible(false);
/*  832: 897 */       this.bySumcheck_.setVisible(false);
/*  833: 898 */       this.moveUnmappedToEnd.setVisible(false);
/*  834: 899 */       this.resort_.setVisible(false);
/*  835: 900 */       this.maxVisField_.setVisible(false);
/*  836: 901 */       this.includeRepseq_.setVisible(false);
/*  837:     */     }
/*  838:     */   }
//*  839:     */  

//				  This method is no longer implemented in Bubble Sort, I just left the name incase unforeseen parts of the program were dependant upon it
/*  840:     */   private void sortEcsbyNameBubble()
/*  841:     */   {
/*  842: 907 */     if (this.sortedEc) {
/*  843: 907 */       return;
/*  844:     */     }
/*  845: 909 */     this.lframe.bigStep("Sorting ECs");
				    System.out.println("Sorting ECs");
				    quicksortNames(0, this.ecMatrix_.size()-1);
				   	this.ecMatrix_=removeDuplicates();
				    System.out.println("Done Sorting");
				    if (this.moveUnmappedToEnd != null)
/*  872:     */     {
/*  873: 937 */       if (this.moveUnmappedToEnd.isSelected()) {
/*  874: 938 */         unmappedMover();
/*  875:     */       }
/*  876:     */     }
/*  877:     */     else {
/*  878: 942 */       unmappedMover();
/*  879:     */     }
/*  880: 944 */     unCompleteMover();
/*  881: 945 */     Loadingframe.close();
/*  882:     */   }
				  
				  private void quicksortNames(int low, int high)
				  {
				  	int i=low, j=high;
				  	String pivot=this.ecMatrix_.get(low+(high-low)/2).getEc_().name_;
				  	while(i<=j){
				  		while(this.ecMatrix_.get(i).getEc_().name_.compareTo(pivot) < 0){
				  			i++;
				  		}
				  		while(this.ecMatrix_.get(j).getEc_().name_.compareTo(pivot) > 0){
				  			j--;
				  		}
				  		if(i<=j){
				  			switchEcs(i, j);
				  			i++;
				  			j--;
				  		}
				  	}
				  	if(low<j)
				  		quicksortNames(low, j);
				  	if(i<high)
				  		quicksortNames(i, high);
				  }

			  	  private ArrayList<Line> removeDuplicates(){
				  	ArrayList<Line> tempLine=new ArrayList<Line>();
				  	ArrayList<String> tempName=new ArrayList<String>();
				  	for(int i=0;i<this.ecMatrix_.size();i++){
				  		if(!tempName.contains(this.ecMatrix_.get(i).getEc_().name_)){
				  			tempLine.add(this.ecMatrix_.get(i));
				  			tempName.add(this.ecMatrix_.get(i).getEc_().name_);
				  		}
				  	}
				  	return tempLine;
				  }

/*  884:     */   private void unmappedMover()
/*  885:     */   {
/*  886: 950 */     int unmappedCnt = 0;
/*  887: 951 */     for (int ecCnt1 = 0; ecCnt1 < this.ecMatrix_.size(); ecCnt1++)
/*  888:     */     {
/*  889: 952 */       ((Line)this.ecMatrix_.get(ecCnt1)).getEc_().addStats();
/*  890: 953 */       if ((((Line)this.ecMatrix_.get(ecCnt1)).getEc_().unmapped) && 
/*  891: 954 */         (ecCnt1 < this.ecMatrix_.size() - unmappedCnt))
/*  892:     */       {
/*  893: 955 */         moveToEnd(ecCnt1);
/*  894: 956 */         unmappedCnt++;
/*  895: 957 */         ecCnt1--;
/*  896:     */       }
/*  897:     */     }
/*  898:     */   }
/*  899:     */   
/*  900:     */   private void unCompleteMover()
/*  901:     */   {
/*  902: 965 */     int unCompleteCnt = 0;
/*  903: 966 */     for (int ecCnt1 = 0; ecCnt1 < this.ecMatrix_.size(); ecCnt1++) {
/*  904: 967 */       if ((!((Line)this.ecMatrix_.get(ecCnt1)).getEc_().isCompleteEc()) && 
/*  905: 968 */         (ecCnt1 < this.ecMatrix_.size() - unCompleteCnt))
/*  906:     */       {
/*  907: 969 */         moveToEnd(ecCnt1);
/*  908: 970 */         unCompleteCnt++;
/*  909: 971 */         ecCnt1--;
/*  910:     */       }
/*  911:     */     }
/*  912:     */   }
/*  913:     */   
/*  914:     */   private void sortEcsbySumBubble()
/*  915:     */   {
					System.out.println("Sum Bubble");
/*  916: 980 */     if (this.sortedEc) {
/*  917: 980 */       return;
/*  918:     */     }
/*  919: 982 */     this.lframe.bigStep("Sorting ECs");
/*  920: 983 */     boolean changed = true;
/*  921: 984 */     double sum1 = 0.0D;
/*  922: 985 */     double sum2 = 0.0D;
/*  923: 986 */     if (!this.sortedEc)
/*  924:     */     {
/*  925:     */       int ecCnt1=0;
                      while(changed && (ecCnt1 < this.ecMatrix_.size()))
/*  927:     */       {
/*  928: 988 */         changed = false;
/*  930: 990 */         sum1 = 0.0D;
/*  931: 991 */         if (this.selectedSampIndex_ < 0) {
/*  932: 992 */           sum1 = ((Line)this.ecMatrix_.get(ecCnt1)).sum_;
/*  933:     */         } else {
/*  934: 995 */           sum1 = ((Line)this.ecMatrix_.get(ecCnt1)).getEntry(this.selectedSampIndex_);
/*  935:     */         }
/*  936: 998 */         sum2 = 0.0D;
/*  937: 999 */         for (int ecCnt2 = ecCnt1 + 1; ecCnt2 < this.ecMatrix_.size(); ecCnt2++)
/*  938:     */         {
/*  939:1000 */           sum2 = 0.0D;
/*  940:1001 */           this.lframe.step("comparing");
/*  941:1002 */           if (this.selectedSampIndex_ < 0) {
/*  942:1003 */             sum2 = ((Line)this.ecMatrix_.get(ecCnt2)).sum_;
/*  943:     */           } else {
/*  944:1006 */             sum2 = ((Line)this.ecMatrix_.get(ecCnt2)).getEntry(this.selectedSampIndex_);
/*  945:     */           }
/*  946:1008 */           if (sum1 >= sum2) {
/*  947:     */             break;
/*  948:     */           }
/*  949:1009 */           switchEcs(ecCnt1, ecCnt2);
/*  950:1010 */           ecCnt1++;
/*  951:1011 */           ecCnt2++;
/*  952:1012 */           changed = true;
/*  953:1013 */           this.lframe.step("switching");
/*  954:     */         }
/*  955: 989 */         ecCnt1++;
/*  956:     */       }
/*  957:1021 */       this.sortedEc = true;
/*  958:     */     }
/*  959:1023 */     if (this.moveUnmappedToEnd.isSelected()) {
/*  960:1024 */       unmappedMover();
/*  961:     */     }
/*  962:1026 */     unCompleteMover();
/*  963:     */   }
/*  964:     */   
/*  965:     */   
/*  982:     */   
/*  983:     */   private void moveToEnd(int index)
/*  984:     */   {
/*  985:1045 */     int tmp = index;
/*  986:1046 */     while (tmp < this.ecMatrix_.size() - 1)
/*  987:     */     {
/*  988:1047 */       switchEcs(tmp, tmp + 1);
/*  989:1048 */       tmp++;
/*  990:     */     }
/*  991:     */   }
/*  992:     */   
/*  993:     */   public void exportMat(String path, boolean inCsf)//Exports the whole matrix to the input path
/*  994:     */   {
/*  995:1054 */     String separator = "\t";
/*  996:1055 */     if (inCsf) {
/*  997:1056 */       separator = ",";
/*  998:     */     }
/*  999:     */     try
/* 1000:     */     {
/* 1001:1059 */       BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 1002:1060 */       out.write("EcActivity " + separator);
/* 1003:1061 */       out.newLine();
/* 1004:1062 */       out.newLine();
/* 1005:1064 */       for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
/* 1006:1065 */         if (((Sample)Project.samples_.get(smpCnt)).inUse)
/* 1007:     */         {
/* 1008:1066 */           out.write(separator);
/* 1009:1067 */           out.write(((Sample)Project.samples_.get(smpCnt)).name_ + separator);
/* 1010:     */         }
/* 1011:     */       }
/* 1012:1070 */       out.newLine();
/* 1013:1071 */       for (int y = 0; y < this.ecMatrix_.size(); y++)
/* 1014:     */       {
/* 1015:1072 */         Line line = (Line)this.ecMatrix_.get(y);
/* 1016:1073 */         if ((!line.getEc_().isCompleteEc()) && (!this.dispIncomplete_.isSelected())) {
/* 1017:     */           break;
/* 1018:     */         }
/* 1019:1076 */         out.write(line.getEc_().name_ + line.getEc_().nameSuppl() + separator);
/* 1020:1077 */         for (int x = 0; x < line.getArrayLine_().length; x++) {
/* 1021:1078 */           if (this.useOddsrat_.isSelected())
/* 1022:     */           {
/* 1023:1079 */             float a = (float)line.arrayLine_[x];
/* 1024:1080 */             float b = (float)(this.sums.arrayLine_[x] - a);
/* 1025:1081 */             float c = line.sum_ - a;
/* 1026:1082 */             float d = this.sums.sum_ - a - b - c;
/* 1027:1083 */             out.write(odds(a, b, c, d) + separator);
/* 1028:     */           }
/* 1029:     */           else
/* 1030:     */           {
/* 1031:1086 */             out.write(line.getArrayLine_()[x] + separator);
/* 1032:     */           }
/* 1033:     */         }
/* 1034:1089 */         out.newLine();
/* 1035:     */       }
/* 1036:1091 */       out.close();
/* 1037:     */     }
/* 1038:     */     catch (IOException e)
/* 1039:     */     {
/* 1040:1094 */       File f = new File(path);
/* 1041:1095 */       f.mkdirs();
/* 1042:1096 */       if (!path.endsWith(".txt"))
/* 1043:     */       {
/* 1044:1097 */         Calendar cal = Calendar.getInstance();
/* 1045:     */         
/* 1046:1099 */         int day = cal.get(5);
/* 1047:1100 */         int month = cal.get(2) + 1;
/* 1048:1101 */         int year = cal.get(1);
/* 1049:     */        
/* 1051:     */ 
/* 1052:1105 */         String date = day + "." + month + "." + year;
/* 1053:1106 */         path = path + File.separator + "EcActMat" +"."+Project.workpath_+"."+date;
/* 1054:     */       }
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
/* 1055:1108 */       exportMat(path, inCsf);
/* 1056:     */     }
/* 1057:     */   }


/* 1058:     */   private void quicksortSum()
				  {
				    this.lframe.bigStep("Sorting ECs");
				    System.out.println("Sorting ECs by sum");
				    quicksort(0, this.ecMatrix_.size()-1);
				    reverseMatrix();
				   	this.ecMatrix_=removeDuplicates();
				    System.out.println("Done Sorting");
				    if (this.moveUnmappedToEnd != null)
					{
					  if (this.moveUnmappedToEnd.isSelected()) {
				         unmappedMover();
					  }
					}
					else {
					  unmappedMover();
					}
										
					unCompleteMover();
					Loadingframe.close();
				  }

				  private void quicksort(int low, int high)
				  {
					int i=low, j=high;
					int pivot=this.ecMatrix_.get(high-1).sum_;
					while(i<=j){
						while(this.ecMatrix_.get(i).sum_<pivot){
							i++;
						}
						while(this.ecMatrix_.get(j).sum_>pivot){
							j--;
						}
						if(i<=j){
							switchEcs(i, j);
							i++;
							j--;
						}
					}
					if(low<j)
						quicksort(low,j);
					if(i<high)
						quicksort(i,high);
				  }

				  private void reverseMatrix() 
				  {
				  	int j=0;
				    for(int i=ecMatrix_.size()-1;i>j;i--){
				  	  switchEcs(i, j);
				  	  j++;
				    }
				  }

                  private void switchEcs(int index1, int index2)
/*  966:     */   {
/*  973:1035 */     Line line1= (Line)this.ecMatrix_.get(index1);
/*  974:1036 */     Line line2 = (Line)this.ecMatrix_.get(index2);

					this.ecMatrix_.set(index1, line2);
					this.ecMatrix_.set(index2, line1);
/*  981:     */   }
//* 1095:     */   
/* 1096:     */   private float odds(float a, float b, float c, float d)
/* 1097:     */   {
/* 1098:1148 */     if (a == 0.0F) {
/* 1099:1148 */       return -1.0F;
/* 1100:     */     }
/* 1101:1149 */     if (b == 0.0F) {
/* 1102:1151 */       c = 0.001F;
/* 1103:     */     }
/* 1104:1153 */     if (c == 0.0F) {
/* 1105:1155 */       b = 0.001F;
/* 1106:     */     }
/* 1107:1157 */     if (d == 0.0F) {
/* 1108:1157 */       return -2.0F;
/* 1109:     */     }
/* 1110:1159 */     float ret = a / b / (c / d);
/* 1111:     */     
/* 1112:     */ 
/* 1113:     */ 
/* 1114:1163 */     return ret;
/* 1115:     */   }
/* 1116:     */   
/* 1117:     */   private void setSelectedEc()
/* 1118:     */   {
/* 1119:1168 */     for (int x = 0; x < Project.samples_.size(); x++) {
/* 1120:1169 */       if (((Sample)Project.samples_.get(x)).inUse)
/* 1121:     */       {
/* 1122:1170 */         ArrayList<EcWithPathway> actSample = ((Sample)Project.samples_.get(x)).ecs_;
/* 1123:1171 */         for (int ecCnt = 0; ecCnt < actSample.size(); ecCnt++)
/* 1124:     */         {
/* 1125:1172 */           boolean isSelected = false;
/* 1126:1173 */           EcWithPathway actEc = (EcWithPathway)actSample.get(ecCnt);
/* 1127:1174 */           for (int pwCnt = 0; pwCnt < actEc.pathways_.size(); pwCnt++) {
/* 1128:1179 */             if (this.proc_.getPathway(((Pathway)actEc.pathways_.get(pwCnt)).id_).isSelected()) {
/* 1129:1180 */               isSelected = true;
/* 1130:     */             }
/* 1131:     */           }
/* 1132:1183 */           if (actEc.pathways_.size() == 0) {
/* 1133:1184 */             isSelected = true;
/* 1134:     */           }
/* 1135:1186 */           if (!isSelected) {
/* 1136:1188 */             actEc.isSelected_ = false;
/* 1137:     */           } else {
/* 1138:1191 */             actEc.isSelected_ = true;
/* 1139:     */           }
/* 1140:     */         }
/* 1141:     */       }
/* 1142:     */     }
/* 1143:     */   }
					
				  public void cmdExportRepseqs(String ecName){// allows for the exporting of sequence IDs for all samples given an EC name via the cmdPrompt
				  	int index;	
				  	EcNr ecTmp;
				  	
				  	for(int i=0;i<this.ecMatrix_.size();i++){
				  		//System.out.println(this.ecMatrix_.get(i).getEc_().name_);
				  		if(ecName.contains(this.ecMatrix_.get(i).getEc_().name_)){
							ecTmp=new EcNr(((Line)ActMatrixPane.this.ecMatrix_.get(i)).getEc_());
							for (int smpCnt = 0; smpCnt < ecMatrix_.get(i).arrayLine_.length; smpCnt++)
							{	
								ecTmp.amount_ = ((int)((Line)ActMatrixPane.this.ecMatrix_.get(i)).arrayLine_[smpCnt]);
						    	ArrayList<ConvertStat> reps = new ArrayList();
								for (int statsCnt = 0; statsCnt < ((Sample)Project.samples_.get(smpCnt)).conversions_.size(); statsCnt++) {
									String test=(((ConvertStat)((Sample)Project.samples_.get(smpCnt)).conversions_.get(statsCnt)).getDesc_());
									if ((ecTmp.name_.contentEquals(((ConvertStat)((Sample)Project.samples_.get(smpCnt)).conversions_.get(statsCnt)).getEcNr_())) &&
									!test.contains("\t") ){
										reps.add((ConvertStat)((Sample)Project.samples_.get(smpCnt)).conversions_.get(statsCnt));
									}
								}		
								String test="";
							  	String test2="";
							  	for(int j=reps.size()-1;j>=0;j--){
							  		if((reps.get(j)==null)){}
							  		else{
							  			test=((ConvertStat)reps.get(j)).getDesc_();
			//				  			System.out.println("1  "+test);
							  			if(test.contains("\t")){
							  				reps.set(j,null);
							  			}
							  			else{
			//				  				innerloop:
							  				for(int k=j-1;k>=0;k--){
							  					if((reps.get(k)==null)){}
							  					else{
								  					test2=((ConvertStat)reps.get(k)).getDesc_();
			//				  						System.out.println("2  "+  test2);
							  						if(test.contains(test2)){
							  							reps.set(k,null);
							  						}
												}
							  				}
							  			}
							  		}
							  	}
							  	for(int j=reps.size()-1;j>=0;j--){
							  		if(reps.get(j)==null){
							  			reps.remove(j);
							  		}
							  	}						
								String sampName=((Sample)Project.samples_.get(smpCnt)).name_;
								if(reps.size()>0){
						    		ExportReps(reps, ecTmp, sampName);
						    	}
							}
				  		}
				  	}
        	      }
        	    }