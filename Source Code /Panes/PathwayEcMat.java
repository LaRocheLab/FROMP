/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.EcNr;
/*   4:    */ import Objects.EcWithPathway;
/*   5:    */ import Objects.Line;
/*   6:    */ import Objects.PathwayWithEc;
/*   7:    */ import Objects.Project;
/*   8:    */ import Objects.Sample;
/*   9:    */ import Prog.DataProcessor;
/*  10:    */ import Prog.PathButt;
/*  11:    */ import java.awt.BorderLayout;
/*  12:    */ import java.awt.Color;
/*  13:    */ import java.awt.Dimension;
/*  14:    */ import java.awt.event.ActionEvent;
/*  15:    */ import java.awt.event.ActionListener;
/*  16:    */ import java.awt.event.MouseEvent;
/*  17:    */ import java.awt.event.MouseListener;
/*  18:    */ import java.io.BufferedWriter;
/*  19:    */ import java.io.File;
/*  20:    */ import java.io.FileWriter;
/*  21:    */ import java.io.IOException;
/*  22:    */ import java.io.PrintStream;
/*  23:    */ import java.util.ArrayList;
/*  24:    */ import javax.swing.JButton;
/*  25:    */ import javax.swing.JCheckBox;
/*  26:    */ import javax.swing.JFileChooser;
/*  27:    */ import javax.swing.JLabel;
/*  28:    */ import javax.swing.JPanel;
/*  29:    */ import javax.swing.JScrollPane;
/*  30:    */ import javax.swing.filechooser.FileFilter;
/*  31:    */ 
/*  32:    */ public class PathwayEcMat
/*  33:    */   extends JPanel
/*  34:    */ {
/*  35:    */   private static final long serialVersionUID = 1L;
/*  36:    */   private ArrayList<ArrayList<Line>> arrays_;
/*  37:    */   private double[] pwSums_;
/*  38:    */   ArrayList<PathwayWithEc> origPaths_;
/*  39:    */   Project actProj_;
/*  40:    */   JCheckBox sortPathesBySum_;
/*  41:    */   JCheckBox sortEcsBySum_;
/*  42:    */   boolean dataChanged_;
/*  43:    */   int activesamps_;
/*  44:    */   DataProcessor proc_;
/*  45: 50 */   boolean allset = true;
/*  46:    */   int xWidth;
/*  47:    */   int yOffset_;
/*  48: 53 */   int linecnt_ = 0;
/*  49:    */   private JPanel optionsPanel_;
/*  50:    */   private JPanel displayP_;
/*  51:    */   private JScrollPane showJPanel_;
/*  52:    */   private JLabel mouseOverDisp;
/*  53:    */   private JPanel mouseOverP;
/*  54:    */   private JCheckBox useCsf_;
/*  55: 62 */   boolean firstTime = true;
				private JButton rebuild;
/*  56:    */   
/*  57:    */   public PathwayEcMat(ArrayList<PathwayWithEc> origPaths, Project actProj, DataProcessor proc, Dimension dim)
/*  58:    */   {
/*  59: 67 */     System.out.println("pathwayecmat");
/*  60: 68 */     this.dataChanged_ = false;
/*  61:    */     
/*  62: 70 */     this.origPaths_ = origPaths;
/*  63: 71 */     this.actProj_ = actProj;
/*  64: 72 */     this.proc_ = proc;
/*  65:    */     
/*  66: 74 */     this.xWidth = (4000 + Project.samples_.size() * 300);
/*  67:    */     
/*  68:    */ 
/*  69:    */ 
/*  70: 78 */     createArrays();
/*  71: 79 */     fillArrays();
/*  72:    */     
/*  73:    */ 
/*  74: 82 */     setSize(dim);
/*  75: 83 */     setVisible(true);
/*  76: 84 */     setLayout(new BorderLayout());
/*  77: 85 */     setBackground(Project.getBackColor_());
/*  78:    */     
/*  79: 87 */     this.sortPathesBySum_ = new JCheckBox("Sort paths by the sum of ECs");
/*  80: 88 */     this.sortEcsBySum_ = new JCheckBox("Sort ECs by sum");
/*  81: 89 */     this.sortPathesBySum_.setSelected(false);
/*  82: 90 */     this.sortEcsBySum_.setSelected(false);
/*  83:    */     
/*  84: 92 */     prepaint();
/*  85:    */     
/*  86: 94 */     System.out.println("finished 0");
/*  87: 95 */     this.allset = true;
/*  88:    */   }
/*  89:    */   
/*  90:    */   private void prepaint()
/*  91:    */   {
/*  92: 99 */     removeAll();
/*  93:    */     
/*  94:101 */     initMainPanels();
/*  95:102 */     addOptions();
/*  96:    */     
/*  97:104 */     drawArr();
/*  98:    */     
/*  99:106 */     invalidate();
/* 100:107 */     validate();
/* 101:108 */     repaint();
/* 102:    */   }
/* 103:    */   
/* 104:    */   private void initMainPanels()
/* 105:    */   {
/* 106:111 */     this.optionsPanel_ = new JPanel();
/* 107:112 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth(), 100));
/* 108:113 */     this.optionsPanel_.setLocation(0, 0);
/* 109:114 */     this.optionsPanel_.setBackground(Project.standard);
/* 110:115 */     this.optionsPanel_.setVisible(true);
/* 111:116 */     this.optionsPanel_.setLayout(null);
/* 112:117 */     add(this.optionsPanel_, "First");
/* 113:    */     
/* 114:119 */     this.displayP_ = new JPanel();
/* 115:120 */     this.displayP_.setLocation(0, 0);
/* 116:121 */     this.displayP_.setPreferredSize(new Dimension(getWidth() + Project.samples_.size() * 300, 4000 + 12 * this.linecnt_));
/* 117:122 */     this.displayP_.setSize(getPreferredSize());
/* 118:123 */     this.displayP_.setBackground(Project.getBackColor_());
/* 119:124 */     this.displayP_.setVisible(true);
/* 120:125 */     this.displayP_.setLayout(null);
/* 121:    */     
/* 122:127 */     this.showJPanel_ = new JScrollPane(this.displayP_);
/* 123:128 */     this.showJPanel_.setVisible(true);
/* 124:129 */     this.showJPanel_.setVerticalScrollBarPolicy(20);
/* 125:130 */     this.showJPanel_.setHorizontalScrollBarPolicy(30);
/* 126:    */     
/* 127:132 */     add("Center", this.showJPanel_);
/* 128:    */   }
/* 129:    */   
/* 130:    */   private void createArrays()
/* 131:    */   {
/* 132:136 */     Loadingframe lframe = new Loadingframe();
/* 133:137 */     lframe.bigStep("creating Arrays");
/* 134:138 */     this.activesamps_ = 0;
/* 135:139 */     for (int x = 0; x < Project.samples_.size(); x++) {
/* 136:140 */       if ((((Sample)Project.samples_.get(x)).inUse)&&(Project.samples_.get(x).onoff)) {
/* 137:141 */         this.activesamps_ += 1;
/* 138:    */       }
/* 139:    */     }
/* 140:145 */     this.arrays_ = new ArrayList();
/* 141:146 */     ArrayList<Line> array = new ArrayList();
/* 142:147 */     for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++)
/* 143:    */     {
/* 144:148 */       array = new ArrayList();
/* 145:149 */       this.arrays_.add(array);
/* 146:    */     }
/* 147:151 */     Loadingframe.close();
/* 148:    */   }
/* 149:    */   
/* 150:    */   private void fillArrays()
/* 151:    */   {
/* 152:155 */     this.linecnt_ = 0;
/* 153:156 */     Loadingframe lframe = new Loadingframe();
/* 154:157 */     lframe.bigStep("creating Arrays");
/* 155:    */     
/* 156:159 */     this.pwSums_ = new double[this.arrays_.size()];
/* 157:162 */     for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++)
/* 158:    */     {
/* 159:163 */       int activCnt = 0;
/* 160:164 */       for (int ecCnt = 0; ecCnt < ((PathwayWithEc)this.origPaths_.get(origCnt)).ecNrs_.size(); ecCnt++)
/* 161:    */       {
/* 162:165 */         EcNr ecNr = (EcNr)((PathwayWithEc)this.origPaths_.get(origCnt)).ecNrs_.get(ecCnt);
/* 163:166 */         double[] arr = new double[this.activesamps_];
					  int xREAL=0;
/* 164:167 */         for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
/* 165:168 */           if ((((Sample)Project.samples_.get(smpCnt)).inUse)&&(Project.samples_.get(smpCnt).onoff))
/* 166:    */           {
/* 167:171 */             EcWithPathway ecwp = ((Sample)Project.samples_.get(smpCnt)).getEc(ecNr.name_);
/* 168:172 */             if (ecwp != null)
/* 169:    */             {
/* 170:173 */               arr[xREAL] = ecwp.amount_;
/* 171:174 */               activCnt += ecwp.amount_;
/* 172:    */             }
/* 173:176 */             ecwp = null;
						  xREAL++;
/* 174:    */           }
/* 175:    */         }
/* 176:178 */         Line line = new Line(ecNr, arr);
/* 177:179 */         ((ArrayList)this.arrays_.get(origCnt)).add(line);
/* 178:180 */         this.linecnt_ += 1;
/* 179:181 */         line.setSum();
/* 180:182 */         ecNr = null;
/* 181:    */       }
/* 182:184 */       this.pwSums_[origCnt] = activCnt;
/* 183:    */     }
/* 184:186 */     Loadingframe.close();
/* 185:    */   }
/* 186:    */   
/* 187:    */   private void sortEcsBySum()
/* 188:    */   {
//* 189:191 */     boolean changed = true;
/* 190:    */     
/* 191:193 */     Loadingframe lframe = new Loadingframe();
/* 192:    */     
/* 193:    */ 
/* 194:196 */     lframe.bigStep("sorting ecs");
/* 195:197 */     for (int pthCnt = 0; pthCnt < this.arrays_.size(); pthCnt++)
/* 196:    */     {
/* 197:198 */       ArrayList<Line> arr1 = (ArrayList)this.arrays_.get(pthCnt);

//* 198:199 */       changed = true;
/* 199:    */       
/* 200:201 */       lframe.step("Sorting path:" + ((PathwayWithEc)this.origPaths_.get(pthCnt)).id_);
//* 201:    */       int ecCnt=0;
//* 202:202 */       for (; changed; ecCnt < arr1.size())
//                     while(changed && (ecCnt < arr1.size()))
//* 203:    */       {
//* 204:203 */         changed = false;
///* 205:204 */         ecCnt = 0; continue;
//* 206:205 */         if (((Line)arr1.get(ecCnt)).sum_ == 0) {
//* 207:206 */           ((Line)arr1.get(ecCnt)).setSum();
//* 208:    */         }
//* 209:208 */         double sum1 = ((Line)arr1.get(ecCnt)).sum_;
//* 210:209 */         double sum2 = 0.0D;
//* 211:210 */         for (int pathCnt2 = ecCnt + 1; pathCnt2 < arr1.size(); pathCnt2++)merge sort java
//* 212:    */         {
//* 213:211 */           if (((Line)arr1.get(pathCnt2)).sum_ == 0) {
//* 214:212 */             ((Line)arr1.get(pathCnt2)).setSum();
//* 215:    */           }
//* 216:214 */           sum2 = ((Line)arr1.get(pathCnt2)).sum_;
//* 217:215 */           if (sum1 >= sum2) {
//* 218:    */             break;
//* 219:    */           }
///* 220:216 */           switchLines(arr1, ecCnt, pathCnt2);
//* 221:217 */           ecCnt++;
//* 222:218 */           pathCnt2++;
//* 223:219 */           changed = true;
//* 224:220 */           lframe.step("Switch");
//* 225:    */         }
//* 226:204 */         ecCnt++;
//* 227:    */       }
					quicksortEcsBySum(arr1, 0, arr1.size()-1);
					int j=0;
					for(int i=arr1.size()-1;i>j;i--){
						switchLines(arr1, i, j);
						j++;
					}
/* 228:    */     }

/* 229:229 */     Loadingframe.close();
/* 230:230 */     System.out.println("sortedbysum");
/* 231:    */   }
/* 232:    */   
				private void quicksortEcsBySum(ArrayList<Line> arr, int low, int high)
				{
					int i = low, j = high;
					if(i>=j){return;}
					double pivot=((Line)arr.get(low+(high-low)/2)).sum_;
					while(i <= j){
						while(((Line)arr.get(i)).sum_ < pivot){
							i++;
						}
						while(((Line)arr.get(j)).sum_ > pivot){
							j--;
						}
						if(i<=j){
//							lframe.step("Swaping: "+arr.get(i).sum_+" and "+arr.get(j).sum_);
							switchLines(arr, i, j);
							i++;
							j--;
						}
//						lframe.step();
					}
					if(low < j)
						quicksortEcsBySum(arr, low, j);
					if(i < high)
						quicksortEcsBySum(arr, i, high);
				}

/* 233:    */   private void sortEcsByName()
/* 234:    */   {
/* 235:233 */     boolean changed = true;
/* 236:    */     
/* 237:235 */     Loadingframe lframe = new Loadingframe();
/* 238:    */     
/* 239:    */ 
/* 240:238 */     lframe.bigStep("sorting ecs");
/* 241:239 */     for (int pthCnt = 0; pthCnt < this.arrays_.size(); pthCnt++)
/* 242:    */     {
/* 243:240 */       ArrayList<Line> arr1 = (ArrayList)this.arrays_.get(pthCnt);
//* 244:241 */       changed = true;
/* 245:242 */       lframe.step("Sorting path:" + ((PathwayWithEc)this.origPaths_.get(pthCnt)).id_);
					quicksortEcsByName(arr1, 0, arr1.size()-1);
//* 246:    */       int ecCnt1=0;
///* 247:244 */       for (; changed; ecCnt1 < arr1.size())
//                     while(changed && (ecCnt1 < arr1.size()))
//* 248:    */       {
//* 249:245 */         changed = false;
///* 250:246 */         ecCnt1 = 0; continue;
//* 251:247 */         Line line1 = (Line)arr1.get(ecCnt1);
//* 252:248 */         EcNr sum1 = line1.getEcNr_();
//* 253:249 */         for (int ecCnt2 = ecCnt1 + 1; ecCnt2 < arr1.size(); ecCnt2++)
//* 254:    */         {
//* 255:250 */           lframe.step("comparing");
//* 256:251 */           Line line2 = (Line)arr1.get(ecCnt2);
//* 257:252 */           EcNr sum2 = line2.getEcNr_();
//* 258:253 */           if (sum1.name_.compareTo(sum2.name_) <= 0) {
//* 259:    */             break;
//* 260:    */           }
//* 261:254 */           switchLines(arr1, ecCnt1, ecCnt2);
//* 262:255 */           ecCnt1++;
//* 263:256 */           ecCnt2++;
//* 264:257 */           changed = true;
//* 265:    */         }
//* 266:246 */         ecCnt1++;
//* 267:    */       }
/* 268:    */     }
/* 269:266 */     Loadingframe.close();
/* 270:267 */     System.out.println("sortbyName");
/* 271:    */   }

				private void quicksortEcsByName(ArrayList<Line> arr, int low, int high)
				{
					int i = low, j = high;
					if(i>=j){return;}
					String pivot=arr.get(high-1).getEcNr_().name_;
					while(i <= j){
						while(arr.get(i).getEcNr_().name_.compareTo(pivot) < 0){
							i++;
						}
						while(arr.get(j).getEcNr_().name_.compareTo(pivot) > 0){
							j--;
						}
						if(i<=j){
//							lframe.step("Swaping: "+arr.get(i).sum_+" and "+arr.get(j).sum_);
							switchLines(arr, i, j);
							i++;
							j--;
						}
//						lframe.step();
					}
					if(low < j)
						quicksortEcsByName(arr, low, j);
					if(i < high)
						quicksortEcsByName(arr, i, high);
				}
/* 272:    */   
/* 273:    */   private void sortPathsBySum()
/* 274:    */   {
/* 275:270 */     Loadingframe lframe = new Loadingframe();
/* 276:271 */     boolean changed = true;
/* 277:272 */     double sum1 = 0.0D;
/* 278:273 */     double sum2 = 0.0D;
/* 279:274 */     lframe.bigStep("sorting paths");
/* 280:275 */     setsums();
				  
				  QuickSortPathsBySum(0, this.pwSums_.length - 1);
//* 281:    */     int pathCnt=0;
///* 282:276 */     for (; changed; pathCnt < this.pwSums_.length)
//                   while(changed && (pathCnt < this.pwSums_.length))
//* 283:    */     {
//* 284:277 */       changed = false;
///* 285:278 */       pathCnt = 0; continue;
//* 286:279 */       lframe.step("Sorting path:" + ((PathwayWithEc)this.origPaths_.get(pathCnt)).id_);
//* 287:280 */       sum1 = this.pwSums_[pathCnt];
//* 288:281 */       sum2 = 0.0D;
//* 289:282 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < this.pwSums_.length; pathCnt2++)
//* 290:    */       {
//* 291:284 */         sum2 = this.pwSums_[pathCnt2];
//* 292:285 */         if (sum1 >= sum2) {
//* 293:    */           break;
//* 294:    */         }
//* 295:286 */         switchPaths(pathCnt, pathCnt2);
//* 296:287 */         pathCnt++;
//* 297:288 */         pathCnt2++;
//* 298:289 */         changed = true;
//* 299:    */       }
//* 300:278 */       pathCnt++;
//* 301:    */     }
				  int j=0;
				  for(int i=pwSums_.length-1;i>j;i--){
				  	switchPaths(i, j);
				  	j++;
				  }
/* 302:297 */     Loadingframe.close();
/* 303:    */   }
				
				private void QuickSortPathsBySum(int low, int high)
				{
					int i=low, j=high;
					double pivot=pwSums_[high-1];

					while(i<=j){
						while(pwSums_[i]<pivot){
							i++;
						}
						while(pwSums_[j]>pivot){
							j--;
						}
						if(i<=j){
							switchPaths(i, j);
							i++;
							j--;
						}
					}
					if(low<j){
						QuickSortPathsBySum(low, j);
					}
					if(i<high){
						QuickSortPathsBySum(i, high);
					}
				}
/* 304:    */   
/* 305:    */   private void setsums()
/* 306:    */   {
/* 307:300 */     double sum = 0.0D;
/* 308:301 */     for (int pathCnt = 0; pathCnt < this.arrays_.size(); pathCnt++)
/* 309:    */     {
/* 310:302 */       sum = 0.0D;
/* 311:303 */       for (int ecCnt = 0; ecCnt < ((ArrayList)this.arrays_.get(pathCnt)).size(); ecCnt++) {
/* 312:304 */         sum += ((Line)((ArrayList)this.arrays_.get(pathCnt)).get(ecCnt)).sum_;
/* 313:    */       }
/* 314:306 */       this.pwSums_[pathCnt] = sum;
/* 315:    */     }
/* 316:    */   }
/* 317:    */   
/* 318:    */   private void sortPathsByName()
/* 319:    */   {
				  quicksortPathsByName(0,origPaths_.size()-1);
//* 320:310 */     boolean changed = true;
//* 321:311 */     String sum1 = "";
//* 322:312 */     String sum2 = "";
//* 323:    */     int pathCnt=0;
///* 324:313 */     for (; changed; pathCnt < this.arrays_.size())
 //                 while(changed && (pathCnt < this.arrays_.size()))
//* 325:    */     {
//* 326:314 */       changed = false;
//* 327:    */       
///* 328:316 */       pathCnt = 0; continue;
//* 329:317 */       sum1 = ((PathwayWithEc)this.origPaths_.get(pathCnt)).id_;
//* 330:318 */       sum2 = "";
//* 331:319 */       for (int pathCnt2 = pathCnt + 1; pathCnt2 < this.arrays_.size(); pathCnt2++)
//* 332:    */       {
//* 333:320 */         sum2 = ((PathwayWithEc)this.origPaths_.get(pathCnt2)).id_;
//* 334:321 */         if (!id1Bigger(sum1, sum2)) {
//* 335:    */           break;
//* 336:    */         }
//* 337:322 */         switchPaths(pathCnt, pathCnt2);
///* 338:323 */         pathCnt++;
//* 339:324 */         pathCnt2++;
//* 340:325 */         changed = true;
//* 341:    */       }
//* 342:316 */       pathCnt++;
//* 343:    */     }
/* 344:    */   }

				private void quicksortPathsByName(int low, int high)
				{
					int i=low, j=high;
					if(i>=j){return;}
					String pivot=this.origPaths_.get(low+(high-low)/2).id_;
					while(i<=j){
						while(this.origPaths_.get(i).id_.compareTo(pivot)<0){
							i++;
						}
						while(this.origPaths_.get(j).id_.compareTo(pivot)>0){
							j--;
						}
						if(i<=j){
							switchPaths(i, j);
							i++;
							j--;
						}
					}
					if(low<j){
						quicksortPathsByName(low,j);
					}
					if(i<high){
						quicksortPathsByName(i,high);
					}
				}
/* 345:    */   
/* 346:    */   private void switchPaths(int index1, int index2)
/* 347:    */   {
/* 348:337 */     double tmp = this.pwSums_[index1];
/* 349:338 */     this.pwSums_[index1] = this.pwSums_[index2];
/* 350:339 */     this.pwSums_[index2] = tmp;
/* 351:    */     
/* 352:341 */     PathwayWithEc origPaths1 = (PathwayWithEc)this.origPaths_.get(index1);
/* 353:342 */     PathwayWithEc origPaths2 = (PathwayWithEc)this.origPaths_.get(index2);
/* 357:    */     
/* 358:347 */     this.origPaths_.set(index1, origPaths2);
/* 359:348 */     this.origPaths_.set(index2, origPaths1);
/* 360:    */     
/* 361:350 */     ArrayList<Line> line1 = (ArrayList)this.arrays_.get(index1);
/* 362:351 */     ArrayList<Line> line2 = (ArrayList)this.arrays_.get(index2);
/* 366:    */     
/* 367:356 */     this.arrays_.set(index1, line2);
/* 368:357 */     this.arrays_.set(index2, line1);
/* 369:    */   }
/* 370:    */   
/* 371:    */   private void writeOutAllArr(String path, boolean inCsf)
/* 372:    */   {
/* 373:360 */     String seperator = "\t";
/* 374:361 */     if (inCsf) {
/* 375:362 */       seperator = ",";
/* 376:    */     }
/* 377:    */     try
/* 378:    */     {
/* 379:365 */       BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 380:366 */       for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++)
/* 381:    */       {
/* 382:367 */         out.write(((PathwayWithEc)this.origPaths_.get(origCnt)).id_ + seperator);
/* 383:368 */         out.newLine();
/* 384:369 */         for (int oriEcCnt = 0; oriEcCnt < ((PathwayWithEc)this.origPaths_.get(origCnt)).ecNrs_.size(); oriEcCnt++)
/* 385:    */         {
/* 386:370 */           out.write(((EcNr)((PathwayWithEc)this.origPaths_.get(origCnt)).ecNrs_.get(oriEcCnt)).name_ + ((EcNr)((PathwayWithEc)this.origPaths_.get(origCnt)).ecNrs_.get(oriEcCnt)).nameSuppl() + seperator);
/* 387:371 */           for (int smpCnt = 0; smpCnt < this.activesamps_; smpCnt++) {
/* 388:372 */             if (((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).getEntry(smpCnt) == 0.0D) {
/* 389:373 */               out.write("0" + seperator);
/* 390:    */             } else {
/* 391:376 */               out.write(((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).getEntry(smpCnt) + seperator);
/* 392:    */             }
/* 393:    */           }
/* 394:379 */           out.newLine();
/* 395:380 */           out.newLine();
/* 396:    */         }
/* 397:    */       }
/* 398:    */     }
/* 399:    */     catch (IOException e)
/* 400:    */     {
/* 401:385 */       e.printStackTrace();
/* 402:    */     }
/* 403:    */   }
/* 404:    */   
/* 405:    */   private void addOptions()
/* 406:    */   {
/* 407:391 */     if (this.useCsf_ == null) {
/* 408:392 */       this.useCsf_ = new JCheckBox("CSF");
/* 409:    */     }
/* 410:394 */     this.useCsf_.setVisible(true);
/* 411:395 */     this.useCsf_.setLayout(null);
/* 412:396 */     this.useCsf_.setBackground(this.optionsPanel_.getBackground());
/* 413:397 */     this.useCsf_.setForeground(Project.getFontColor_());
/* 414:398 */     this.useCsf_.setBounds(10, 44, 100, 15);
/* 415:399 */     this.optionsPanel_.add(this.useCsf_);
/* 416:    */     
/* 417:401 */     JButton export = new JButton("Export values");
/* 418:402 */     export.setBounds(10, 10, 150, 20);
/* 419:403 */     export.setVisible(true);
/* 420:404 */     export.addActionListener(new ActionListener()
/* 421:    */     {
/* 422:    */       public void actionPerformed(ActionEvent e)
/* 423:    */       {
/* 424:409 */         JFileChooser fChoose_ = new JFileChooser(Project.workpath_);
/* 425:410 */         fChoose_.setFileSelectionMode(0);
/* 426:411 */         fChoose_.setBounds(100, 100, 200, 20);
/* 427:412 */         fChoose_.setVisible(true);
/* 428:413 */         File file = new File(Project.workpath_);
/* 429:414 */         fChoose_.setSelectedFile(file);
/* 430:415 */         fChoose_.setFileFilter(new FileFilter()
/* 431:    */         {
/* 432:    */           public boolean accept(File f)
/* 433:    */           {
/* 434:419 */             if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".txt"))) {
/* 435:420 */               return true;
/* 436:    */             }
/* 437:422 */             return false;
/* 438:    */           }
/* 439:    */           
/* 440:    */           public String getDescription()
/* 441:    */           {
/* 442:428 */             return ".txt";
/* 443:    */           }
/* 444:    */         });
/* 445:432 */         if (fChoose_.showSaveDialog(null) == 0) {
/* 446:    */           try
/* 447:    */           {
/* 448:434 */             String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 449:436 */             if (!path.endsWith(".txt"))
/* 450:    */             {
/* 451:437 */               path = path + ".txt";
/* 452:438 */               System.out.println(".txt");
/* 453:    */             }
/* 454:440 */             PathwayEcMat.this.writeOutAllArr(path, PathwayEcMat.this.useCsf_.isSelected());
/* 455:    */           }
/* 456:    */           catch (IOException e1)
/* 457:    */           {
/* 458:443 */             e1.printStackTrace();
/* 459:    */           }
/* 460:    */         }
/* 461:    */       }
/* 462:448 */     });
/* 463:449 */     this.optionsPanel_.add(export);
/* 464:    */     
/* 465:    */ 
/* 466:452 */     this.sortPathesBySum_.setBounds(200, 10, 250, 20);
/* 467:453 */     this.sortPathesBySum_.setBackground(this.optionsPanel_.getBackground());
/* 468:    */     
/* 469:455 */     this.optionsPanel_.add(this.sortPathesBySum_);
/* 470:    */     
/* 471:    */ 
/* 472:458 */     this.sortEcsBySum_.setBounds(200, 30, 200, 20);
/* 473:459 */     this.sortEcsBySum_.setBackground(this.optionsPanel_.getBackground());
/* 474:    */     
/* 475:461 */     this.optionsPanel_.add(this.sortEcsBySum_);
/* 476:    */     
/* 477:463 */     JButton resort = new JButton("Resort");
/* 478:464 */     resort.setBounds(550, 10, 100, 25);
/* 479:465 */     resort.addActionListener(new ActionListener()
/* 480:    */     {
/* 481:    */       public void actionPerformed(ActionEvent e)
/* 482:    */       {
/* 483:470 */         PathwayEcMat.this.prepaint();
/* 484:    */       }
/* 485:473 */     });
/* 486:474 */     this.optionsPanel_.add(resort);
/* 487:    */     
/* 488:476 */     this.mouseOverP = new JPanel();
/* 489:477 */     this.mouseOverP.setBackground(Project.getBackColor_());
/* 490:478 */     this.mouseOverP.setBounds(700, 10, 500, 60);
/* 491:479 */     this.optionsPanel_.add(this.mouseOverP);
/* 492:    */     
/* 493:481 */     this.mouseOverDisp = new JLabel("Additional Pathway-information");
/* 494:    */     
/* 495:483 */     this.mouseOverDisp.setBounds(0, 0, 500, 60);
/* 496:484 */     this.mouseOverP.add(this.mouseOverDisp);

				  this.rebuild=new JButton("Rebuild");
				  this.rebuild.setBounds(550,50,100,25);
				  this.rebuild.addActionListener(new ActionListener()
/* 480:    */     {
/* 481:    */       public void actionPerformed(ActionEvent e)
/* 482:    */       {
/* 483:470 */          	PathwayEcMat.this.createArrays();
/*  71: 79 */     		PathwayEcMat.this.fillArrays();
 						PathwayEcMat.this.prepaint();
 						for(int i=0;i<Project.samples_.size();i++){
 							Project.samples_.get(i).onoff=true;
 						}
/* 484:    */       }
/* 485:473 */     });
/* 486:474 */     this.optionsPanel_.add(this.rebuild);
/* 497:    */     
/* 498:486 */     System.out.println("finished 1");
/* 499:    */   }
/* 500:    */   
/* 501:    */   private void drawArr()
/* 502:    */   {
/* 503:490 */     double time = System.currentTimeMillis();
/* 504:491 */     System.out.println("drawArr");
/* 505:    */     
/* 506:493 */     int xdist = 100;
/* 507:496 */     if (this.sortPathesBySum_.isSelected())
/* 508:    */     {
/* 509:497 */       sortPathsBySum();
/* 510:498 */       this.dataChanged_ = true;
/* 511:    */     }
/* 512:    */     else
/* 513:    */     {
/* 514:502 */       sortPathsByName();
/* 515:    */     }
/* 516:505 */     this.allset = false;
/* 517:510 */     if (this.sortEcsBySum_.isSelected()) {
/* 518:511 */       sortEcsBySum();
/* 519:514 */     } else if (this.firstTime) {
/* 520:515 */       this.firstTime = false;
/* 521:    */     } else {
/* 522:518 */       sortEcsByName();
/* 523:    */     }
/* 524:521 */     Loadingframe lframe = new Loadingframe();
/* 525:522 */     lframe.bigStep("painting values");
/* 526:523 */     lframe.step("removing old vals");
/* 527:    */     
/* 528:525 */     int lineCounter = 0;
/* 529:526 */     int arrCounter = 0;
/* 530:527 */     int arrH = 50;
/* 531:528 */     int smpNameSpace = 25;
/* 532:529 */     int smpSpaceCnt = 0;
/* 533:530 */     int lineH = 12;
/* 534:    */     
/* 535:532 */     String line = "";
/* 536:    */     
/* 537:    */ 
/* 538:535 */     lineCounter = 0;
/* 539:    */     
/* 540:    */ 
/* 541:    */ 
/* 542:539 */     System.out.println("starting " + (System.currentTimeMillis() - time));
/* 543:540 */     for (int origCnt = 0; origCnt < this.origPaths_.size(); origCnt++)
/* 544:    */     {
/* 545:541 */       lframe.bigStep("drawing " + ((PathwayWithEc)this.origPaths_.get(origCnt)).id_);
/* 546:542 */       if (((PathwayWithEc)this.origPaths_.get(origCnt)).isSelected())
/* 547:    */       {
/* 548:546 */         final PathwayWithEc path = Project.overall_.getPath(((PathwayWithEc)this.origPaths_.get(origCnt)).id_);
/* 549:547 */         PathButt pathButt = new PathButt(Project.samples_, Project.overall_, path, Project.getBackColor_(), Project.workpath_, 0);
/* 550:548 */         pathButt.setBounds(50, 10 + lineH * lineCounter + arrH * arrCounter + smpNameSpace * smpSpaceCnt, 400, 50);
/* 551:549 */         pathButt.setVisible(true);
/* 552:550 */         pathButt.setLayout(null);
/* 553:551 */         pathButt.addMouseListener(new MouseListener()
/* 554:    */         {
/* 555:    */           public void mouseReleased(MouseEvent e) {}
/* 556:    */           
/* 557:    */           public void mousePressed(MouseEvent e) {}
/* 558:    */           
/* 559:    */           public void mouseExited(MouseEvent e)
/* 560:    */           {
/* 561:568 */             PathwayEcMat.this.mouseOverDisp.setVisible(false);
/* 562:    */             
/* 563:570 */             PathwayEcMat.this.mouseOverDisp.setText("Additional Pathway-information");
/* 564:    */           }
/* 565:    */           
/* 566:    */           public void mouseEntered(MouseEvent e)
/* 567:    */           {
/* 568:577 */             PathwayEcMat.this.mouseOverDisp.setVisible(true);
/* 569:    */             
/* 570:579 */             PathwayEcMat.this.setAdditionalInfo(path);
/* 571:    */           }
/* 572:    */           
/* 573:    */           public void mouseClicked(MouseEvent e) {

						}
/* 574:588 */         });
/* 575:589 */         this.displayP_.add(pathButt);
/* 576:590 */         lineCounter++;
/* 577:591 */         line = "";
/* 578:592 */         smpSpaceCnt++;
					  int smpNme=0;
/* 579:593 */         for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
/* 580:594 */           if ((((Sample)Project.samples_.get(smpCnt)).inUse)&&(Project.samples_.get(smpCnt).onoff))
/* 581:    */           {
						  final int sampleCount=smpCnt;
						  final int sampleName=smpNme;
/* 582:595 */             lframe.step("drawing " + ((Sample)Project.samples_.get(smpCnt)).name_);
/* 583:596 */             line = ((Sample)Project.samples_.get(smpCnt)).name_;
/* 584:597 */             final JLabel fullName = new JLabel(line);
/* 585:598 */             fullName.setBounds(20 + xdist + xdist * sampleName, 50 + lineH * lineCounter + arrH * arrCounter + smpNameSpace * smpSpaceCnt - lineH, 500, lineH);
/* 586:599 */             fullName.setForeground(((Sample)Project.samples_.get(smpCnt)).sampleCol_);
/* 587:600 */             fullName.setVisible(false);
/* 588:601 */             fullName.setLayout(null);
/* 589:602 */             this.displayP_.add(fullName);
/* 590:    */             
/* 591:604 */             final JLabel smpName = new JLabel(line);
/* 592:    */             
/* 593:606 */             smpName.setBounds(0, 0, xdist, lineH);
/* 594:607 */             smpName.setForeground(((Sample)Project.samples_.get(smpCnt)).sampleCol_);
/* 595:608 */             smpName.setVisible(true);
/* 596:609 */             smpName.setLayout(null);
/* 597:    */             
/* 598:    */ 			  
/* 599:612 */             JPanel mouseOver = new JPanel();
/* 600:613 */             mouseOver.setBounds(20 + xdist + xdist * sampleName, 50 + lineH * lineCounter + arrH * arrCounter + smpNameSpace * smpSpaceCnt, xdist, lineH);
/* 601:614 */             mouseOver.setBackground(Project.getBackColor_());
/* 602:615 */             mouseOver.setVisible(true);
/* 603:616 */             mouseOver.setLayout(null);
/* 604:617 */             mouseOver.addMouseListener(new MouseListener()
/* 605:    */             {
/* 606:    */               public void mouseReleased(MouseEvent e) {}
/* 607:    */               
/* 608:    */               public void mousePressed(MouseEvent e) {}
/* 609:    */               
/* 610:    */               public void mouseExited(MouseEvent e)
/* 611:    */               {
/* 612:634 */                 fullName.setVisible(false);
/* 613:635 */                 smpName.setVisible(true);
/* 614:    */               }
/* 615:    */               
/* 616:    */               public void mouseEntered(MouseEvent e)
/* 617:    */               {
/* 618:641 */                 fullName.setVisible(true);
/* 619:642 */                 smpName.setVisible(false);
/* 620:    */               }
/* 621:    */               
/* 622:    */               public void mouseClicked(MouseEvent e) {
								Project.samples_.get(sampleCount).onoff=false;
							}
/* 623:652 */             });
/* 624:653 */             this.displayP_.add(mouseOver);
/* 625:654 */             mouseOver.add(smpName);
						  smpNme++;
/* 626:    */           }
/* 627:    */         }
/* 628:662 */         lineCounter++;
/* 629:665 */         for (int oriEcCnt = 0; oriEcCnt < ((PathwayWithEc)this.origPaths_.get(origCnt)).ecNrs_.size(); oriEcCnt++)
/* 630:    */         {
/* 631:666 */           if (((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).getSum_() == 0.0D) {
/* 632:667 */             ((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).setSum();
/* 633:    */           }
/* 634:669 */           if (((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).sum_ > 0)
/* 635:    */           {
/* 636:672 */             final int ecIndex = oriEcCnt;
/* 637:673 */             final int pathIndex = origCnt;
/* 638:    */             
/* 639:    */ 
/* 640:    */ 
/* 641:677 */             line = this.proc_.findEcWPath(((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).getEcNr_()).getFullName();
/* 642:678 */             lframe.step("drawing " + line);
/* 643:679 */             JButton ecButt = new JButton(line);
/* 644:680 */             ecButt.setBounds(10, 50 + lineH * lineCounter + arrH * arrCounter + smpNameSpace * smpSpaceCnt, xdist, lineH);
/* 645:    */             
/* 646:682 */             ecButt.setVisible(true);
/* 647:683 */             ecButt.setLayout(null);
/* 648:684 */             ecButt.addActionListener(new ActionListener()
/* 649:    */             {
/* 650:    */               public void actionPerformed(ActionEvent e)
/* 651:    */               {
/* 652:690 */                 EcWithPathway ecWp = PathwayEcMat.this.proc_.getEc(((EcNr)((PathwayWithEc)PathwayEcMat.this.origPaths_.get(pathIndex)).ecNrs_.get(ecIndex)).name_);
/* 653:691 */                 if (ecWp != null) {
/* 654:692 */                   new PwInfoFrame(ecWp, PathwayEcMat.this.actProj_, Project.overall_);
/* 655:    */                 } else {
/* 656:695 */                   System.out.println("no paths");
/* 657:    */                 }
/* 658:    */               }
/* 659:699 */             });
/* 660:700 */             this.displayP_.add(ecButt);
/* 661:702 */             for (int smpCnt = 0; smpCnt < this.activesamps_; smpCnt++)
/* 662:    */             {
/* 663:703 */               if (((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).getEntry(smpCnt) == 0.0D) {
/* 664:704 */                 line = "0";
/* 665:    */               } else {
/* 666:707 */                 line = String.valueOf(((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).getEntry(smpCnt));
/* 667:    */               }
/* 668:710 */               JLabel value = new JLabel(line);
/* 669:711 */               value.setBounds(20 + xdist + xdist * smpCnt, 50 + lineH * lineCounter + arrH * arrCounter + smpNameSpace * smpSpaceCnt, xdist, lineH);
/* 670:    */               
/* 671:713 */               value.setForeground(Color.black);
/* 672:714 */               value.setVisible(true);
/* 673:715 */               value.setLayout(null);
/* 674:716 */               this.displayP_.add(value);
/* 675:    */             }
/* 676:719 */             line = String.valueOf(((Line)((ArrayList)this.arrays_.get(origCnt)).get(oriEcCnt)).getSum_());
/* 677:720 */             JLabel value = new JLabel(line);
/* 678:721 */             value.setBounds(20 + xdist + xdist * this.activesamps_, 50 + lineH * lineCounter + arrH * arrCounter + smpNameSpace * smpSpaceCnt, xdist, lineH);
/* 679:    */             
/* 680:723 */             value.setForeground(Color.black);
/* 681:724 */             value.setVisible(true);
/* 682:725 */             value.setLayout(null);
/* 683:726 */             this.displayP_.add(value);
/* 684:727 */             lineCounter++;
/* 685:    */           }
/* 686:    */         }
/* 687:730 */         arrCounter++;
/* 688:    */       }
/* 689:    */     }
/* 690:733 */     System.out.println("finished2 " + (System.currentTimeMillis() - time));
/* 691:734 */     Loadingframe.close();
/* 692:    */   }
/* 693:    */   
/* 694:    */   private void switchLines(ArrayList<Line> arr, int index1, int index2)
/* 695:    */   {
/* 696:737 */     if (index2 < index1)
/* 697:    */     {
/* 698:738 */       int tmp = index2;
/* 699:739 */       index2 = index1;
/* 700:740 */       index1 = tmp;
/* 701:    */     }
/* 702:742 */     Line line1 = (Line)arr.get(index1);
/* 703:743 */     Line line2 = (Line)arr.get(index2);
/* 707:    */     
/* 708:748 */     arr.set(index1, line2);
/* 709:749 */     arr.set(index2, line1);
/* 710:    */   }
/* 711:    */   
/* 712:    */   private boolean id1Bigger(String id1, String id2)
/* 713:    */   {
/* 714:753 */     if (id1.contentEquals("-1")) {
/* 715:754 */       return true;
/* 716:    */     }
/* 717:756 */     if (id2.contentEquals("-1")) {
/* 718:757 */       return false;
/* 719:    */     }
/* 720:759 */     for (int charCnt = 0; (charCnt < id1.length()) && (charCnt < id2.length()); charCnt++)
/* 721:    */     {
/* 722:760 */       if (id1.charAt(charCnt) > id2.charAt(charCnt)) {
/* 723:761 */         return true;
/* 724:    */       }
/* 725:763 */       if (id1.charAt(charCnt) < id2.charAt(charCnt)) {
/* 726:764 */         return false;
/* 727:    */       }
/* 728:    */     }
/* 729:767 */     return false;
/* 730:    */   }
/* 731:    */   
/* 732:    */   public void setyOffset_(int yOffset_)
/* 733:    */   {
/* 734:770 */     this.yOffset_ = yOffset_;
/* 735:    */   }
/* 736:    */   
/* 737:    */   private void setAdditionalInfo(PathwayWithEc path)
/* 738:    */   {
/* 739:773 */     this.mouseOverDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_ + "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_ + "</html>");
/* 740:    */   }
/* 741:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.PathwayEcMat

 * JD-Core Version:    0.7.0.1

 */