/*   1:    */ package Objects;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.io.BufferedReader;
/*   5:    */ import java.io.BufferedWriter;
/*   6:    */ import java.io.File;
/*   7:    */ import java.io.FileNotFoundException;
/*   8:    */ import java.io.FileReader;
/*   9:    */ import java.io.FileWriter;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.util.Date;
/*  14:    */ import javax.swing.JFrame;
/*  15:    */ import javax.swing.JLabel;
/*  16:    */ import javax.swing.JPanel;
			  import java.util.*;
			  import Prog.StringReader;

/*  17:    */ 
			//Obviously this is where project files are stored. Not much computation is done here beyond loading saved project files, 
			//and exporting project files. Serves mostly to store the data from the samples so it can be used elsewhere.


/*  18:    */ public class Project
/*  19:    */ {
/*  20:    */   public static String projectPath_;
/*  21:    */   public static String workpath_;
/*  22:    */   public static ArrayList<String> userPathways;
/*  23:    */   static final String VERS = "$$ver:";
/*  24:    */   public static int minVisScore_;
				public static boolean loaded=false;
/*  25: 28 */   public static boolean listMode_ = false;
/*  26: 29 */   public static boolean randMode_ = false;
/*  27: 30 */   public static boolean chaining = true;
/*  28: 31 */   public static boolean imported = false;
/*  29: 33 */   public static boolean dataChanged = true;
/*  30: 34 */   public static boolean dataChanged2 = true;
/*  31:    */   public static ArrayList<Sample> samples_;
/*  32:    */   public static ArrayList<String> removedSamples;
/*  33:    */   public static Sample overall_;
/*  34:    */   private static Color backColor_;
/*  35:    */   private static Color fontColor_;
/*  36:    */   private static Color overAllColor_;
/*  37: 47 */   public static Color standard = new Color(90, 125, 206);
/*  38: 49 */   public static int amountOfEcs=0;
				public static int numOfCompleteEcs=0;
				public static int numOfMappedEcs=0;
				public static int amountOfPfs=0;
				public static int numOfConvertedPFs=0;
				public static int numOfConvPfsComplete=0;
				public static int numOfConvPfsMapped=0;
				public static int amountOfIPRs=0;
				public static int numOfConvertedIPRs=0;
				public static int numOfConvIPRsComplete=0;
				public static int numOfConvIPRsMapped=0;
/*  43: 54 */   public static ArrayList<Boolean> legitSamples = new ArrayList();
				final static String basePath_ = new File(".").getAbsolutePath() + File.separator;
				Hashtable<String, ArrayList<String>> IPRToECHash=new Hashtable<String, ArrayList<String>>();
				Hashtable<String, String> PFamToECHash=new Hashtable<String, String>();
				BufferedReader interproToECTxt_;
				BufferedReader pfamToRnToEcTxt_;
				static final String interproToECPath_ = basePath_+"list" + File.separator + "interPro_kegg.tsv";
				static final String pfamToRnToEcPath_ = basePath_+"list" + File.separator + "pfam2Ec2Rn.txt";
				StringReader reader;
/*  44:    */   
/*  45:    */   public Project(String workPath)
/*  46:    */   {
/*  47: 57 */     File file = new File("");
/*  48:    */     try
/*  49:    */     {
/*  50: 60 */       projectPath_ = file.getCanonicalPath();
/*  51:    */     }
/*  52:    */     catch (IOException e)
/*  53:    */     {
/*  54: 63 */       e.printStackTrace();
/*  55:    */     }
/*  56: 65 */     imported = false;
/*  57: 66 */     minVisScore_ = 0;
/*  58: 67 */     workpath_ = workPath;
/*  59:    */     
/*  60: 69 */     samples_ = new ArrayList();
/*  61: 70 */     overall_ = new Sample("overAll", "");
/*  62: 71 */     overall_.sampleCol_ = Color.black;
/*  63: 72 */     overall_.singleSample_ = false;
/*  64: 73 */     backColor_ = new Color(233, 233, 233);
/*  65: 74 */     fontColor_ = Color.black;
/*  66: 75 */     overAllColor_ = Color.red;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int loadProject(BufferedReader projectFile)
/*  70:    */   {
/*  71: 84 */     String zeile = "";
/*  72:    */     try
/*  73:    */     {
/*  74: 86 */       if ((zeile = projectFile.readLine()) != null)
/*  75:    */       {
/*  76: 87 */         if (zeile.contains("$$ver:1"))
/*  77:    */         {
/*  78: 88 */           System.out.println("Vers1");
/*  79: 89 */           return loadProjectv0(projectFile, zeile, 1);
/*  80:    */         }
/*  81: 92 */         System.out.println("Vers0");
/*  82: 93 */         return loadProjectv0(projectFile, zeile, 0);
/*  83:    */       }
/*  84:    */     }
/*  85:    */     catch (IOException e)
/*  86:    */     {
/*  87: 98 */       e.printStackTrace();
/*  88:    */     }
/*  89:100 */     return -1;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public int loadProjectv0(BufferedReader projectFile, String firstLine, int mode)
/*  93:    */   {
/*  94:109 */     File file = new File("");
/*  95:110 */     minVisScore_ = 0;
/*  96:111 */     int ret = 0;
/*  97:112 */     int red = 0;
/*  98:113 */     int green = 0;
/*  99:114 */     int blue = 0;
/* 100:115 */     boolean inUse = false;
/* 101:    */     try
/* 102:    */     {
/* 103:117 */       projectPath_ = file.getCanonicalPath();
/* 104:    */     }
/* 105:    */     catch (IOException e)
/* 106:    */     {
/* 107:121 */       openWarning("Error", "File: " + projectPath_ + " not found");
/* 108:122 */       e.printStackTrace();
/* 109:    */     }
/* 110:124 */     samples_ = new ArrayList();
/* 111:125 */     overall_ = new Sample("overAll", "");
/* 112:126 */     overall_.sampleCol_ = Color.black;
/* 113:127 */     overall_.singleSample_ = false;
/* 114:    */     try
/* 115:    */     {
/* 116:131 */       if (mode == 1)
/* 117:    */       {
/* 118:    */         String zeile;
/* 119:132 */         if ((zeile = projectFile.readLine()) != null)
/* 120:    */         {
/* 121:135 */           projectPath_ = zeile;
/* 122:136 */           ret = 1;
/* 123:    */         }
/* 124:    */         else
/* 125:    */         {
/* 126:140 */           return -1;
/* 127:    */         }
/* 128:    */       }
/* 129:    */       else
/* 130:    */       {
/* 131:144 */         projectPath_ = firstLine;
/* 132:    */       }
/* 133:    */       String zeile;
/* 134:146 */       if ((zeile = projectFile.readLine()) != null) {
/* 135:148 */         workpath_ = zeile;
/* 136:    */       }
/* 137:153 */       while ((zeile = projectFile.readLine()) != null)
/* 138:    */       {
/* 139:154 */         inUse = false;
/* 140:155 */         String zeile2 = projectFile.readLine();
/* 141:156 */         red = convertStringtoInt(projectFile.readLine());
/* 142:157 */         green = convertStringtoInt(projectFile.readLine());
/* 143:158 */         blue = convertStringtoInt(projectFile.readLine());
/* 144:159 */         if ((mode == 1) && 
/* 145:160 */           (projectFile.readLine().contentEquals("inUse")))
/* 146:    */         {
/* 147:161 */           inUse = true;
/* 148:162 */           System.out.println(inUse);
/* 149:    */         }
/* 150:165 */         samples_.add(new Sample(zeile, zeile2, new Color(red, green, blue), inUse));
/* 151:    */       }
/* 152:    */     }
/* 153:    */     catch (IOException e)
/* 154:    */     {
/* 155:169 */       e.printStackTrace();
/* 156:    */     }
/* 157:171 */     if (backColor_ == null) {
/* 158:172 */       backColor_ = Color.white;
/* 159:    */     }
/* 160:174 */     return ret;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void writeProject()
/* 164:    */   {
/* 165:180 */     System.out.println(projectPath_);
/* 166:    */     try
/* 167:    */     {
/* 168:182 */       BufferedWriter out = null;
/* 169:183 */       if (projectPath_.endsWith(".prj")) {
/* 170:184 */         out = new BufferedWriter(new FileWriter(projectPath_));
/* 171:    */       } else {
/* 172:187 */         out = new BufferedWriter(new FileWriter(projectPath_ + File.separator + "projects" + File.separator + workpath_ + ".prj"));
/* 173:    */       }
/* 174:190 */       if (projectPath_ == null) {
/* 175:191 */         System.out.println("no path");
/* 176:    */       }
/* 177:193 */       System.out.println(projectPath_ + File.separator + "projects" + File.separator + workpath_ + ".prj");
/* 178:194 */       out.write("$$ver:1");
/* 179:195 */       out.newLine();
/* 180:196 */       out.write(projectPath_ + "\n");
/* 181:197 */       out.write(workpath_ + "\n");
/* 182:198 */       for (int i = 0; i < samples_.size(); i++)
/* 183:    */       {
/* 184:199 */         out.write(((Sample)samples_.get(i)).name_ + "\n");
/* 185:200 */         out.write(((Sample)samples_.get(i)).fullPath_ + "\n");
/* 186:201 */         out.write(((Sample)samples_.get(i)).sampleCol_.getRed() + "\n");
/* 187:202 */         out.write(((Sample)samples_.get(i)).sampleCol_.getGreen() + "\n");
/* 188:203 */         out.write(((Sample)samples_.get(i)).sampleCol_.getBlue() + "\n");
/* 189:204 */         if (((Sample)samples_.get(i)).inUse)
/* 190:    */         {
/* 191:205 */           out.write("inUse\n");
/* 192:206 */           System.out.println("inUse");
/* 193:    */         }
/* 194:    */         else
/* 195:    */         {
/* 196:209 */           out.write("notInUse\n");
/* 197:210 */           System.out.println("notinUse");
/* 198:    */         }
/* 199:212 */         System.out.println(((Sample)samples_.get(i)).name_);
/* 200:213 */         System.out.println(((Sample)samples_.get(i)).fullPath_);
/* 201:    */       }
/* 202:216 */       out.close();
/* 203:    */     }
/* 204:    */     catch (IOException e)
/* 205:    */     {
/* 206:219 */       System.out.println("Exception ");
/* 207:    */     }
/* 208:    */   }
/* 209:    */   
/* 210:    */   public String exportProj(String path)
/* 211:    */   {
				  System.out.println("exportProj");
/* 212:225 */     if (path == null)
/* 213:    */     {
/* 214:226 */       path = projectPath_;
/* 215:227 */       if (!path.endsWith(".frp")) {
/* 216:228 */         path = projectPath_ + File.separator + "projects" + File.separator + workpath_ ;
					  String tmpPath=path+ ".frp";
					  File file1 = new File(tmpPath);
					  if(file1.exists() && !file1.isDirectory()) { 
						int i=1;
					 	while("Pigs"!="Fly"){// loop forever
					  		tmpPath=path+"("+i+")"+ ".frp";
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
					  path=path+ ".frp";
/* 217:    */       }
/* 218:230 */       System.out.println(path);
/* 219:    */     }
/* 220:232 */     else if (path.isEmpty())
/* 221:    */     {
/* 222:233 */       System.out.println(projectPath_);
/* 223:234 */       System.out.println(workpath_);
/* 224:235 */       path = projectPath_ + File.separator + "projects" + File.separator + workpath_ ;
					String tmpPath=path+ ".frp";
					File file1 = new File(tmpPath);
					if(file1.exists() && !file1.isDirectory()) { 
						int i=1;
					 	while("Pigs"!="Fly"){// loop forever
					  		tmpPath=path+"("+i+")"+ ".frp";
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
					path=path+ ".frp";
/* 225:236 */       System.out.println(path);
/* 226:    */     }
/* 227:238 */     BufferedWriter out = null;
/* 228:    */     try
/* 229:    */     {
/* 230:    */       try
/* 231:    */       {
/* 232:242 */         out = new BufferedWriter(new FileWriter(path));
/* 233:    */       }
/* 234:    */       catch (FileNotFoundException e)
/* 235:    */       {
/* 236:245 */         JFrame frame = new JFrame("Error");
/* 237:246 */         frame.setBounds(100, 100, 500, 100);
/* 238:247 */         frame.setVisible(true);
/* 239:248 */         frame.setLayout(null);
/* 240:    */         
/* 241:250 */         JPanel back = new JPanel();
/* 242:251 */         back.setBounds(0, 0, 500, 100);
/* 243:252 */         back.setVisible(true);
/* 244:253 */         back.setLayout(null);
/* 245:254 */         back.setBackground(getBackColor_());
/* 246:255 */         frame.add(back);
/* 247:    */         
/* 248:257 */         JLabel label = new JLabel(e.getMessage());
/* 249:258 */         label.setBounds(0, 0, 500, 100);
/* 250:259 */         label.setVisible(true);
/* 251:260 */         label.setLayout(null);
/* 252:261 */         label.setBackground(getBackColor_());
/* 253:262 */         back.add(label);
/* 254:    */         
/* 255:264 */         System.out.println(e.getMessage());
/* 256:265 */         return "";
/* 257:    */       }
/* 258:268 */       out.write("$$ver:$EXP1$");
/* 259:269 */       out.newLine();
/* 260:    */       
/* 261:271 */       Date now = new Date();
/* 262:272 */       out.write("!! " + now.toString() + " !!");
/* 263:273 */       out.newLine();
/* 264:    */       
/* 265:275 */       out.write("$PROJ$" + workpath_);
/* 266:276 */       out.newLine();
/* 267:277 */       if (backColor_ != null)
/* 268:    */       {
/* 269:278 */         out.write("$back$" + backColor_.getRed() + ":" + backColor_.getGreen() + ":" + backColor_.getBlue());
/* 270:279 */         out.newLine();
/* 271:    */       }
/* 272:281 */       if (fontColor_ != null)
/* 273:    */       {
/* 274:282 */         out.write("$font$" + fontColor_.getRed() + ":" + fontColor_.getGreen() + ":" + fontColor_.getBlue());
/* 275:283 */         out.newLine();
/* 276:    */       }
/* 277:285 */       if (overAllColor_ != null)
/* 278:    */       {
/* 279:286 */         out.write("$oAll$" + overAllColor_.getRed() + ":" + overAllColor_.getGreen() + ":" + overAllColor_.getBlue());
/* 280:287 */         out.newLine();
/* 281:    */       }
/* 282:291 */       for (int smpCnt = 0; smpCnt < samples_.size(); smpCnt++)
/* 283:    */       {
/* 284:292 */         Sample tmpSamp = (Sample)samples_.get(smpCnt);
/* 285:293 */         out.write("SMP*:" + tmpSamp.name_);
/* 286:294 */         out.newLine();
/* 287:295 */         if (tmpSamp.sampleCol_ != null)
/* 288:    */         {
/* 289:296 */           out.write("smpCol*" + tmpSamp.sampleCol_.getRed() + ":" + tmpSamp.sampleCol_.getGreen() + ":" + tmpSamp.sampleCol_.getBlue());
/* 290:297 */           out.newLine();
/* 291:    */         }
/* 292:300 */         for (int ecCnt = 0; ecCnt < tmpSamp.ecs_.size(); ecCnt++)
/* 293:    */         {
/* 294:301 */           EcWithPathway tmpEc = (EcWithPathway)tmpSamp.ecs_.get(ecCnt);
/* 295:302 */           out.write("EC*:" + tmpEc.name_ + ":" + tmpEc.amount_);
/* 296:303 */           out.newLine();
/* 297:304 */           System.out.println(tmpEc.repseqs_.size());
/* 298:305 */           for (int repCnt = 0; repCnt < tmpSamp.conversions_.size(); repCnt++) {
/* 299:306 */             if (((ConvertStat)tmpSamp.conversions_.get(repCnt)).ecNr_.contentEquals(tmpEc.name_)) {
/* 300:307 */               out.write(((ConvertStat)tmpSamp.conversions_.get(repCnt)).desc_ + ":" + ((ConvertStat)tmpSamp.conversions_.get(repCnt)).ecAmount_ + ":" + ((ConvertStat)tmpSamp.conversions_.get(repCnt)).pfamToEcAmount_ + ";");
/* 301:    */             }
/* 302:    */           }
/* 303:310 */           out.newLine();
/* 304:    */         }
/* 305:    */       }
/* 306:313 */       saveUserPAths(out);
/* 307:314 */       saveConvStats(out);
/* 308:315 */       out.close();
/* 309:316 */       return path;
/* 310:    */     }
/* 311:    */     catch (IOException e)
/* 312:    */     {
/* 313:320 */       e.printStackTrace();
/* 314:    */     }
/* 315:321 */     return "";
/* 316:    */   }
/* 317:    */   
/* 318:    */   private void saveConvStats(BufferedWriter out)
/* 319:    */     throws IOException
/* 320:    */   {
/* 321:330 */     out.write("<ConvStats>");
/* 322:331 */     out.newLine();
/* 323:332 */     out.write("<amountOfEcs>" + amountOfEcs);
/* 324:333 */     out.newLine();
/* 325:334 */     out.write("<numOfCompleteEcs>" + numOfCompleteEcs);
/* 326:335 */     out.newLine();
				  out.write("<numOfMappedEcs>" + numOfMappedEcs);
/* 326:335 */     out.newLine();
/* 327:336 */     out.write("<amountOfPfs>" + amountOfPfs);
/* 328:337 */     out.newLine();
/* 329:338 */     out.write("<numOfConvertedPFs>" + numOfConvertedPFs);
/* 330:339 */     out.newLine();
/* 331:340 */     out.write("<numOfConvPfsComplete>" + numOfConvPfsComplete);
/* 332:341 */     out.newLine();
				  out.write("<numOfConvPfsMapped>" + numOfConvPfsMapped);
/* 332:341 */     out.newLine();
/* 333:342 */     for (int i = 0; i < legitSamples.size(); i++)
/* 334:    */     {
/* 335:343 */       out.write("<legit>" + legitSamples.get(i));
/* 336:344 */       out.newLine();
/* 337:    */     }
/* 338:346 */     out.write("</ConvStats>");
/* 339:347 */     out.newLine();
/* 340:    */   }
/* 341:    */   
/* 342:    */   private void loadConvStats(BufferedReader in)
/* 343:    */     throws IOException
/* 344:    */   {
/* 345:    */     String zeile;
				  loaded=true;
/* 346:354 */     while ((zeile = in.readLine()) != null)
/* 347:    */     {
//* 348:    */       String zeile;
/* 349:355 */       String comp = "</ConvStats>";
/* 350:356 */       if (zeile.contentEquals(comp)) {
/* 351:    */         break;
/* 352:    */       }
/* 353:359 */       comp = "<amountOfEcs>";
/* 354:360 */       if (zeile.startsWith(comp))
/* 355:    */       {
/* 356:361 */         String tmp = zeile.substring(comp.length());
/* 357:    */         try
/* 358:    */         {
/* 359:363 */           int value = Integer.valueOf(tmp).intValue();
/* 360:364 */           amountOfEcs = value;
/* 361:    */         }
/* 362:    */         catch (Exception e)
/* 363:    */         {
/* 364:367 */           System.out.println("couldn't convert to Integer: " + zeile);
/* 365:    */         }
/* 366:    */       }
/* 367:    */       else
/* 368:    */       {
/* 369:371 */         comp = "<numOfCompleteEcs>";
/* 370:372 */         if (zeile.startsWith(comp))
/* 371:    */         {
/* 372:373 */           String tmp = zeile.substring(comp.length());
/* 373:    */           try
/* 374:    */           {
/* 375:375 */             int value = Integer.valueOf(tmp).intValue();
/* 376:376 */             numOfCompleteEcs = value;
/* 377:    */           }
/* 378:    */           catch (Exception e)
/* 379:    */           {
/* 380:379 */             System.out.println("couldn't convert to Integer: " + zeile);
/* 381:    */           }
/* 382:    */         }
/* 383:    */         else
/* 384:    */         {
/* 385:383 */           comp = "<numOfMappedEcs>";
/* 386:384 */           if (zeile.startsWith(comp))
/* 387:    */           {
/* 388:385 */             String tmp = zeile.substring(comp.length());
/* 389:    */             try
/* 390:    */             {
/* 391:387 */               int value = Integer.valueOf(tmp).intValue();
/* 392:388 */               numOfMappedEcs = value;
/* 393:    */             }
/* 394:    */             catch (Exception e)
/* 395:    */             {
/* 396:391 */               System.out.println("couldn't convert to Integer: " + zeile);
/* 397:    */             }
/* 398:    */           }
/* 399:    */           else
/* 400:    */           {
/* 401:395 */             comp = "<amountOfPfs>";
/* 402:396 */             if (zeile.startsWith(comp))
/* 403:    */             {
/* 404:397 */               String tmp = zeile.substring(comp.length());
/* 405:    */               try
/* 406:    */               {
/* 407:399 */                 int value = Integer.valueOf(tmp).intValue();
/* 408:400 */                 amountOfPfs = value;
/* 409:    */               }
/* 410:    */               catch (Exception e)
/* 411:    */               {
/* 412:403 */                 System.out.println("couldn't convert to Integer: " + zeile);
/* 413:    */               }
/* 414:    */             }
/* 415:    */             else
/* 416:    */             {
/* 417:407 */               comp = "<numOfConvertedPFs>";
/* 418:408 */               if (zeile.startsWith(comp))
/* 419:    */               {
/* 420:409 */                 String tmp = zeile.substring(comp.length());
/* 421:    */                 try
/* 422:    */                 {
/* 423:411 */                   int value = Integer.valueOf(tmp).intValue();
/* 424:412 */                   numOfConvertedPFs = value;
/* 425:    */                 }
/* 426:    */                 catch (Exception e)
/* 427:    */                 {
/* 428:415 */                   System.out.println("couldn't convert to Integer: " + zeile);
/* 429:    */                 }
/* 430:    */               }
							else
/* 416:    */             	{	
/* 417:407 */                 comp = "<numOfConvPfsComplete>";
/* 418:408 */                 if (zeile.startsWith(comp))
/* 419:    */                 {
/* 420:409 */                   String tmp = zeile.substring(comp.length());
/* 421:    */                   try
/* 422:    */                   {
/* 423:411 */                     int value = Integer.valueOf(tmp).intValue();
/* 424:412 */                     numOfConvPfsComplete = value;
/* 425:    */                   }
/* 426:    */                   catch (Exception e)
/* 427:    */                   {
/* 428:415 */                     System.out.println("couldn't convert to Integer: " + zeile);
/* 429:    */                   }
/* 430:    */                 }
							  else
/* 416:    */             	  {	
/* 417:407 */                   comp = "<numOfConvPfsMapped>";
/* 418:408 */                   if (zeile.startsWith(comp))
/* 419:    */                   {
/* 420:409 */                     String tmp = zeile.substring(comp.length());
/* 421:    */                     try
/* 422:    */                     {
/* 423:411 */                       int value = Integer.valueOf(tmp).intValue();
/* 424:412 */                       numOfConvPfsMapped = value;
/* 425:    */                     }
/* 426:    */                     catch (Exception e)
/* 427:    */                     {
/* 428:415 */                       System.out.println("couldn't convert to Integer: " + zeile);
/* 429:    */                     }
/* 430:    */                   }
/* 431:    */                   else
/* 432:    */                   {
/* 433:419 */                     comp = "<legit>";
/* 434:420 */                     if (zeile.startsWith(comp))
/* 435:    */                     {
/* 436:421 */                       String tmp = zeile.substring(comp.length());
/* 437:422 */                       if (tmp.contentEquals("true")) {
/* 438:423 */                         legitSamples.add(Boolean.valueOf(true));
/* 439:    */                       } else {
/* 440:426 */                         legitSamples.add(Boolean.valueOf(false));
/* 441:    */                       }
/* 442:    */                     }
/* 443:    */                   }
/* 444:    */                 }
/* 445:    */                 
/* 446:    */               }
/* 447:    */             }
/* 448:    */           }
/* 449:    */         }
					}
				  }
				}
/* 450:    */   
/* 451:    */   public void saveUserPAths(BufferedWriter out)
/* 452:    */     throws IOException
/* 453:    */   {
/* 454:432 */     out.write("<userPathways>");
/* 455:433 */     out.newLine();
/* 456:434 */     if (userPathways == null)
/* 457:    */     {
/* 458:435 */       out.write("</userPathways>");
/* 459:436 */       out.newLine();
/* 460:437 */       return;
/* 461:    */     }
/* 462:439 */     for (int i = 0; i < userPathways.size(); i++)
/* 463:    */     {
					String zeile=(String)userPathways.get(i);
					String zeile2;

					String shortBasePath_;
					String seperate=File.separator;

					if(seperate.compareTo("/")==0){
						shortBasePath_=basePath_.replace("./","");
					}
					else{
						shortBasePath_=basePath_.replace(".\\","");
					}
					if(zeile.contains(basePath_)){
						zeile2=zeile.replace(basePath_,"");
					}
					else if(zeile.contains(shortBasePath_)){
						zeile2=zeile.replace(shortBasePath_,"");
					}
					else{
						zeile2=zeile;
					}

/* 464:440 */       out.write(zeile2);
/* 465:441 */       out.newLine();
/* 466:    */     }
/* 467:443 */     out.write("</userPathways>");
/* 468:444 */     out.newLine();
/* 469:    */   }
/* 470:    */   
/* 471:    */   public void loadUserPAths(BufferedReader in, boolean newList)
/* 472:    */     throws IOException
/* 473:    */   {
/* 474:451 */     if (userPathways == null) {
/* 475:452 */       userPathways = new ArrayList();
/* 476:    */     }
/* 477:    */     String zeile;
/* 478:454 */     while ((zeile = in.readLine()) != null)
/* 479:    */     {
//* 480:    */       String zeile;
/* 481:455 */       if (zeile.contentEquals("</userPathways>")) {
/* 482:    */         break;
/* 483:    */       }
					String zeile2;
					String seperate=File.separator;
					System.out.println("File separator:"+seperate);
					if(seperate.compareTo("/")==0){
						System.out.println("File separate 1");
						zeile2=zeile.replace("\\","/");
					}
					else if(seperate.compareTo("\\")==0){
						System.out.println("File separate 2");
						zeile2=zeile.replace("/","\\");
					}
					else{
						System.out.println("File separate 3");
						zeile2=zeile;
					}

					String zeile3=""+basePath_+zeile2;
/* 484:459 */       userPathways.remove(zeile3);
/* 485:460 */       userPathways.add(zeile3);
				  }
/* 487:    */   }
/* 488:    */   
/* 489:    */   public void loadMat(String path, String name)
/* 490:    */   {
/* 491:465 */     String line = "";
/* 492:466 */     String cutOff = "";
/* 493:467 */     int origSmpSize = samples_.size();
/* 494:468 */     int smpIndex = origSmpSize;
/* 495:469 */     Sample tmpSample = null;
/* 496:470 */     boolean firstLine = true;
/* 497:    */     try
/* 498:    */     {
/* 499:473 */       BufferedReader in = new BufferedReader(new FileReader(path));
/* 500:475 */       while ((line = in.readLine()) != null) {
/* 501:476 */         if (line.contains(","))
/* 502:    */         {
/* 503:480 */           smpIndex = origSmpSize;
/* 504:    */           
/* 505:482 */           cutOff = line.substring(0, line.indexOf(","));
/* 506:483 */           line = line.substring(line.indexOf(",") + 1);
/* 507:484 */           System.out.println("cutoff " + cutOff);
						if(cutOff.startsWith("IPR")){
							this.reader = new StringReader();
							ArrayList<String> convertedCutoff= convertInterpro(cutOff);
							for(int i=0;i<convertedCutoff.size();i++){
								cutOff=convertedCutoff.get(i);
								System.out.println("cutoff " + cutOff);
								EcNr tmpEc = new EcNr(cutOff);
								tmpEc.sampleNr_ = smpIndex;
								while (!line.isEmpty())
/* 511:    */           		{
/* 512:489 */           		  if (firstLine)
/* 513:    */           		  {
/* 514:490 */           		    int addedSmpNr = smpIndex - origSmpSize;
									tmpSample = new Sample();
/* 516:492 */           		    tmpSample.name_ = (name + addedSmpNr);
/* 517:493 */           		    tmpSample.imported = true;
/* 518:494 */           		    tmpSample.inUse = true;
/* 519:495 */           		    tmpSample.matrixSample = true;
/* 520:496 */           		    tmpSample.sampleCol_ = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
/* 521:497 */           		    samples_.add(tmpSample);
/* 522:    */           		  }
/* 523:    */           		  else
/* 524:    */           		  {
/* 525:500 */           		    tmpSample = (Sample)samples_.get(smpIndex);
/* 526:    */           		  }
/* 527:503 */           		  if (line.contains(","))
/* 528:    */           		  {
/* 529:504 */           		    cutOff = line.substring(0, line.indexOf(","));
/* 530:505 */           		    line = line.substring(line.indexOf(",") + 1);
/* 531:506 */           		    System.out.println(cutOff);
/* 532:    */           		  }
/* 533:    */           		  else
/* 534:    */           		  {
/* 535:509 */           		    cutOff = line;
/* 536:510 */           		    System.err.println(cutOff);
/* 537:511 */           		    line = "";
/* 538:    */           		  }
/* 539:513 */           		  tmpEc.amount_ = Integer.valueOf(cutOff).intValue();
/* 540:514 */           		  tmpEc.sampleNr_ = smpIndex;
/* 541:515 */           		  tmpEc.samColor_ = ((Sample)samples_.get(samples_.size() - 1)).sampleCol_;
/* 542:516 */           		  tmpEc.stats_.add(new EcSampleStats(tmpEc));
								  boolean alreadyThere=false;
								  for(int x=0;x<tmpSample.ecs_.size();x++){
								  	if(tmpEc.name_.equalsIgnoreCase(tmpSample.ecs_.get(x).name_)){
								  		tmpSample.ecs_.get(x).amount_ += Integer.valueOf(cutOff).intValue();
								  		alreadyThere=true;
								  		break;
								  	}
								  }
								  if(!alreadyThere){
/* 543:517 */           		  	tmpSample.ecs_.add(new EcWithPathway(tmpEc));
								  }
/* 544:518 */           		  System.err.println("tmpsSample ecSize " + tmpSample.ecs_.size());
/* 545:519 */           		  smpIndex++;
/* 546:    */           		}
								if (firstLine) {
/* 548:522 */            		 firstLine = false;
/* 549:    */           		}
							}
							continue;
							
						}
						else if(cutOff.startsWith("PF")){
							this.reader = new StringReader();
							cutOff=convertPFam(cutOff);
							System.out.println("cutoff " + cutOff);
						}
/* 508:485 */           EcNr tmpEc = new EcNr(cutOff);
/* 509:486 */           tmpEc.sampleNr_ = smpIndex;
/* 510:488 */           while (!line.isEmpty())
/* 511:    */           {
/* 512:489 */             if (firstLine)
/* 513:    */             {
/* 514:490 */               int addedSmpNr = smpIndex - origSmpSize;
/* 515:491 */               tmpSample = new Sample();
/* 516:492 */               tmpSample.name_ = (name + addedSmpNr);
/* 517:493 */               tmpSample.imported = true;
/* 518:494 */               tmpSample.inUse = true;
/* 519:495 */               tmpSample.matrixSample = true;
/* 520:496 */               tmpSample.sampleCol_ = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
/* 521:497 */               samples_.add(tmpSample);
/* 522:    */             }
/* 523:    */             else
/* 524:    */             {
/* 525:500 */               tmpSample = (Sample)samples_.get(smpIndex);
/* 526:    */             }
/* 527:503 */             if (line.contains(","))
/* 528:    */             {
/* 529:504 */               cutOff = line.substring(0, line.indexOf(","));
/* 530:505 */               line = line.substring(line.indexOf(",") + 1);
/* 531:506 */               System.out.println(cutOff);
/* 532:    */             }
/* 533:    */             else
/* 534:    */             {
/* 535:509 */               cutOff = line;
/* 536:510 */               System.err.println(cutOff);
/* 537:511 */               line = "";
/* 538:    */             }
/* 539:513 */             tmpEc.amount_ = Integer.valueOf(cutOff).intValue();
/* 540:514 */             tmpEc.sampleNr_ = smpIndex;
/* 541:515 */             tmpEc.samColor_ = ((Sample)samples_.get(samples_.size() - 1)).sampleCol_;
/* 542:516 */             tmpEc.stats_.add(new EcSampleStats(tmpEc));
/* 543:517 */             tmpSample.ecs_.add(new EcWithPathway(tmpEc));
/* 544:518 */             System.err.println("tmpsSample ecSize " + tmpSample.ecs_.size());
/* 545:519 */             smpIndex++;
/* 546:    */           }
/* 547:521 */           if (firstLine) {
/* 548:522 */             firstLine = false;
/* 549:    */           }
/* 550:    */         }
/* 551:    */       }
/* 552:527 */       in.close();
/* 553:    */     }
/* 554:    */     catch (Exception e)
/* 555:    */     {
/* 556:530 */       openWarning("Error", "File: " + path + " not found");
/* 557:531 */       e.printStackTrace();
/* 558:    */     }
/* 559:    */   }

				private String convertPFam(String pfam){
					String ret="";
					if(this.PFamToECHash.isEmpty()){
				    	DigitizeConversionFiles();
				    }

				    String zeile="";
				    String tmpNr="";
				    String pfamNr=pfam;

				    if(this.PFamToECHash.containsKey(pfamNr)){
				    	ret=PFamToECHash.get(pfamNr);
				    }

					return ret;
				}

				private ArrayList<String> convertInterpro(String interpro){//this is the conversion step using ipr->kegg.
				  	ArrayList<String> retList = new ArrayList(); 
				    if(this.IPRToECHash.isEmpty()){
				    	DigitizeConversionFiles();
				    }
				    
				    String zeile = "";
				    String tmpNr = "";
				    String interproNr = interpro;

				    if(this.IPRToECHash.containsKey(interproNr)){
				    	for(int i=0;i<IPRToECHash.get(interproNr).size();i++){
				    		tmpNr=IPRToECHash.get(interproNr).get(i);
				    		Project.numOfConvertedIPRs+=1;
				    		retList.add(tmpNr);
				    	}
				    }
				    return retList;
				}
				private void DigitizeConversionFiles(){
				  	this.interproToECTxt_ = this.reader.readTxt(interproToECPath_);
				  	this.pfamToRnToEcTxt_ = this.reader.readTxt(pfamToRnToEcPath_);
				  	Hashtable<String, ArrayList<String>> tmpIPRToEC = new Hashtable<String, ArrayList<String>>();
				  	Hashtable<String, String> tmpPFamToEC = new Hashtable<String, String>();
				  	String line= "";
				  	try{
				  		while((line = this.interproToECTxt_.readLine()) != null){
				  			if(!line.startsWith("!")){
				  				if(line.matches(".*IPR[0-9][0-9][0-9][0-9][0-9][0-9].*")&&line.contains("+")){
				  					String tmpIPR = line.substring(line.indexOf("IPR"),line.indexOf("IPR")+9);
				  					ArrayList<String> tmpECS= new ArrayList<String>();
				  					String temp= line;
				  					while(temp.contains("+")){
				  						temp= temp.substring(temp.indexOf("+")+1);
				  						String tmpEC = temp;
				  						if(tmpEC.contains("+")){
				  							tmpEC=tmpEC.substring(0,tmpEC.indexOf("+"));
				  						}
				  						tmpECS.add(tmpEC);
				  					}
//				  					System.out.println("Digitized: "+tmpIPR+"Maps to: "+tmpECS.toString());
				  					tmpIPRToEC.put(tmpIPR,tmpECS);
				  				}
				  			}
				  		}
				  	} catch(IOException e){
						openWarning("Error", "File" + interproToECPath_ +" not found");
						e.printStackTrace();
					}
					try{
				  		while((line = this.pfamToRnToEcTxt_.readLine()) != null){
				  			if(!line.startsWith("!")){
				  				if(line.matches(".*PF[0-9][0-9][0-9][0-9][0-9].*")&&line.contains(";")&&!line.matches(".*R[0-9][0-9][0-9][0-9][0-9].*")){
				  					String tmpPfam = line.substring(line.indexOf("PF"),line.indexOf("PF")+7);
				  					String tmpEC= line.substring(line.indexOf(";")+1);

				  					tmpPFamToEC.put(tmpPfam,tmpEC);
				  				}
				  			}
				  		}
				  	} catch(IOException e){
						openWarning("Error", "File" + pfamToRnToEcPath_ +" not found");
						e.printStackTrace();
					}
					this.IPRToECHash=tmpIPRToEC;
					this.PFamToECHash = tmpPFamToEC;
				}
/* 560:    */   
/* 561:    */   public void refreshProj()
/* 562:    */   {
/* 563:536 */     for (int i = 0; i < samples_.size(); i++)
/* 564:    */     {
/* 565:538 */       Sample samp = (Sample)samples_.get(i);
/* 566:539 */       if (!samp.matrixSample) {
/* 567:542 */         samp.clearPaths();
/* 568:    */       }
/* 569:    */     }
/* 570:    */   }
/* 571:    */   
/* 572:    */   public void importProj(String path)
/* 573:    */   {
/* 574:599 */     legitSamples = new ArrayList();
/* 575:600 */     BufferedReader in = null;
/* 576:601 */     projectPath_ = path;
/* 577:602 */     imported = true;
/* 578:603 */     boolean finishedStart = false;
/* 579:604 */     int sampNr = -1;
/* 580:    */     try
/* 581:    */     {
/* 582:606 */       in = new BufferedReader(new FileReader(path));
/* 583:607 */       if (!in.readLine().contentEquals("$$ver:$EXP1$")) {
/* 584:607 */         return;
/* 585:    */       }
/* 586:608 */       String tmpLine = "";
/* 587:609 */       while ((tmpLine = in.readLine()) != null)
/* 588:    */       {
/* 589:610 */         if (!finishedStart)
/* 590:    */         {
/* 591:611 */           if (tmpLine.startsWith("!!")) {
/* 592:    */             continue;
/* 593:    */           }
/* 594:614 */           if (tmpLine.startsWith("SMP*:"))
/* 595:    */           {
/* 596:616 */             finishedStart = true;
/* 597:    */           }
/* 598:    */           else
/* 599:    */           {
/* 600:619 */             if (tmpLine.startsWith("$PROJ$")) {
/* 601:620 */               workpath_ = tmpLine.substring("$PROJ$".length());
/* 602:    */             }
/* 603:622 */             if (tmpLine.startsWith("$back$"))
/* 604:    */             {
/* 605:623 */               tmpLine = tmpLine.substring("$back$".length());
/* 606:624 */               int red = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
/* 607:625 */               tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 608:626 */               int green = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
/* 609:627 */               tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 610:628 */               int blue = Integer.valueOf(tmpLine).intValue();
/* 611:629 */               backColor_ = new Color(red, green, blue);
/* 612:    */             }
/* 613:631 */             if (tmpLine.startsWith("$font$"))
/* 614:    */             {
/* 615:632 */               tmpLine = tmpLine.substring("$font$".length());
/* 616:633 */               int red = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
/* 617:634 */               tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 618:635 */               int green = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
/* 619:636 */               tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 620:637 */               int blue = Integer.valueOf(tmpLine).intValue();
/* 621:638 */               fontColor_ = new Color(red, green, blue);
/* 622:    */             }
/* 623:640 */             if (tmpLine.startsWith("$oAll$"))
/* 624:    */             {
/* 625:641 */               tmpLine = tmpLine.substring("$oAll$".length());
/* 626:642 */               int red = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
/* 627:643 */               tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 628:644 */               int green = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
/* 629:645 */               tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 630:646 */               int blue = Integer.valueOf(tmpLine).intValue();
/* 631:647 */               overAllColor_ = new Color(red, green, blue);
/* 632:    */             }
/* 633:    */           }
/* 634:    */         }
/* 635:652 */         if (finishedStart)
/* 636:    */         {
/* 637:653 */           if (samples_ == null) {
/* 638:654 */             samples_ = new ArrayList();
/* 639:    */           }
/* 640:658 */           if (tmpLine.startsWith("SMP*:"))
/* 641:    */           {
/* 642:659 */             tmpLine = tmpLine.substring("SMP*:".length());
/* 643:660 */             String name = tmpLine;
/* 644:661 */             Sample tmpSamp = new Sample(name, "");
/* 645:662 */             sampNr++;
/* 646:663 */             if (!(tmpLine = in.readLine()).isEmpty())
/* 647:    */             {
/* 648:664 */               tmpLine = tmpLine.substring("smpCol*".length());
/* 649:665 */               int red = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
/* 650:666 */               tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 651:667 */               int green = Integer.valueOf(tmpLine.substring(0, tmpLine.indexOf(":"))).intValue();
/* 652:668 */               tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 653:669 */               int blue = Integer.valueOf(tmpLine).intValue();
/* 654:670 */               tmpSamp.sampleCol_ = new Color(red, green, blue);
/* 655:671 */               tmpSamp.inUse = true;
/* 656:672 */               tmpSamp.imported = true;
/* 657:    */             }
/* 658:    */             else
/* 659:    */             {
/* 660:675 */               tmpSamp.sampleCol_ = Color.BLUE;
/* 661:    */             }
/* 662:677 */             tmpSamp.legitSample = true;
/* 663:678 */             samples_.add(tmpSamp);
/* 664:    */           }
/* 665:682 */           if (tmpLine.startsWith("EC*:"))
/* 666:    */           {
/* 667:683 */             tmpLine = tmpLine.substring("EC*:".length());
/* 668:684 */             String name = tmpLine.substring(0, tmpLine.indexOf(":"));
/* 669:685 */             tmpLine = tmpLine.substring(tmpLine.indexOf(":") + 1);
/* 670:686 */             EcNr tmpEc = new EcNr(name);
/* 671:    */             
/* 672:688 */             tmpEc.amount_ = Integer.valueOf(tmpLine).intValue();
/* 673:689 */             tmpEc.sampleNr_ = sampNr;
/* 674:690 */             tmpEc.samColor_ = ((Sample)samples_.get(samples_.size() - 1)).sampleCol_;
/* 675:691 */             tmpEc.stats_.add(new EcSampleStats(tmpEc));
/* 676:696 */             if (!(tmpLine = in.readLine()).isEmpty())
/* 677:    */             {
/* 678:697 */               String tmpRep = tmpLine;
/* 679:698 */               while (!tmpRep.isEmpty())
/* 680:    */               {
/* 681:699 */                 if (tmpLine.isEmpty()) {
/* 682:    */                   break;
/* 683:    */                 }
/* 684:700 */                 if (tmpLine.contains(";")) {
/* 685:701 */                   tmpRep = tmpLine.substring(0, tmpLine.indexOf(";"));
/* 686:    */                 } else {
/* 687:704 */                   tmpRep = tmpLine;
/* 688:    */                 }
/* 689:707 */                 int lastIndex = tmpRep.lastIndexOf(":");
/* 690:708 */                 String nums = tmpRep.substring(lastIndex);
/* 691:709 */                 String desc = tmpRep.substring(0, lastIndex);
/* 692:710 */                 lastIndex = desc.lastIndexOf(":");
/* 693:711 */                 nums = desc.substring(lastIndex + 1) + nums;
/* 694:712 */                 desc = desc.substring(0, lastIndex);
/* 695:    */                 
/* 696:714 */                 int ecAm = Integer.valueOf(nums.substring(0, nums.indexOf(":"))).intValue();
/* 697:715 */                 nums = nums.substring(nums.indexOf(":") + 1);
/* 698:716 */                 int pfAm = Integer.valueOf(nums).intValue();
/* 699:717 */                 ConvertStat convertStat = new ConvertStat(desc, tmpEc.name_, ecAm, pfAm, 0);
/* 700:718 */                 tmpEc.repseqs_.add(new Repseqs(desc, ecAm + pfAm));
/* 701:719 */                 ((Sample)samples_.get(samples_.size() - 1)).conversions_.add(convertStat);
/* 702:720 */                 tmpLine = tmpLine.substring(tmpLine.indexOf(";") + 1);
/* 703:    */               }
/* 704:    */             }
/* 705:723 */             ((Sample)samples_.get(samples_.size() - 1)).ecs_.add(new EcWithPathway(tmpEc));
/* 706:    */           }
/* 707:725 */           if (tmpLine.contentEquals("<userPathways>")) {
/* 708:726 */             loadUserPAths(in, true);
/* 709:    */           }
/* 710:728 */           if (tmpLine.contentEquals("<ConvStats>")) {
/* 711:729 */             loadConvStats(in);
/* 712:    */           }
/* 713:    */         }
/* 714:    */       }
/* 715:734 */       in.close();
/* 716:    */     }
/* 717:    */     catch (IOException e)
/* 718:    */     {
/* 719:736 */       openWarning("Error", "File: " + path + " not found");
/* 720:737 */       e.printStackTrace();
/* 721:    */     }
/* 722:    */   }
/* 723:    */   
/* 724:    */   public BufferedReader readTxt(String path)
/* 725:    */   {
/* 726:746 */     BufferedReader in = null;
/* 727:    */     try
/* 728:    */     {
/* 729:748 */       in = new BufferedReader(new FileReader(path));
/* 730:    */     }
/* 731:    */     catch (IOException e)
/* 732:    */     {
/* 733:750 */       openWarning("Error", "File: " + path + " not found");
/* 734:751 */       e.printStackTrace();
/* 735:    */     }
/* 736:753 */     return in;
/* 737:    */   }
/* 738:    */   
/* 739:    */   public void clearSamples()
/* 740:    */   {
/* 741:759 */     samples_ = new ArrayList();
/* 742:    */   }
/* 743:    */   
/* 744:    */   public int convertStringtoInt(String in)
/* 745:    */   {
/* 746:768 */     int ret = 0;
/* 747:770 */     for (int i = 0; i < in.length(); i++)
/* 748:    */     {
/* 749:771 */       ret *= 10;
/* 750:772 */       char c = in.charAt(i);
/* 751:773 */       ret += convertCharToInt(c);
/* 752:    */     }
/* 753:775 */     return ret;
/* 754:    */   }
/* 755:    */   
/* 756:    */   public int convertCharToInt(char c)
/* 757:    */   {
/* 758:782 */     switch (c)
/* 759:    */     {
/* 760:    */     case '0': 
/* 761:784 */       return 0;
/* 762:    */     case '1': 
/* 763:787 */       return 1;
/* 764:    */     case '2': 
/* 765:790 */       return 2;
/* 766:    */     case '3': 
/* 767:793 */       return 3;
/* 768:    */     case '4': 
/* 769:796 */       return 4;
/* 770:    */     case '5': 
/* 771:799 */       return 5;
/* 772:    */     case '6': 
/* 773:802 */       return 6;
/* 774:    */     case '7': 
/* 775:805 */       return 7;
/* 776:    */     case '8': 
/* 777:808 */       return 8;
/* 778:    */     case '9': 
/* 779:811 */       return 9;
/* 780:    */     }
/* 781:814 */     return -1;
/* 782:    */   }
/* 783:    */   
/* 784:    */   public static Color getBackColor_()
/* 785:    */   {
/* 786:819 */     if (backColor_ == null) {
/* 787:820 */       backColor_ = Color.orange;
/* 788:    */     }
/* 789:822 */     return backColor_;
/* 790:    */   }
/* 791:    */   
/* 792:    */   public static void setBackColor_(Color backColor_)
/* 793:    */   {
/* 794:825 */     backColor_ = backColor_;
/* 795:    */   }
/* 796:    */   
/* 797:    */   public static Color getFontColor_()
/* 798:    */   {
/* 799:828 */     return fontColor_;
/* 800:    */   }
/* 801:    */   
/* 802:    */   public static void setFontColor_(Color fontColor_)
/* 803:    */   {
/* 804:831 */     fontColor_ = fontColor_;
/* 805:    */   }
/* 806:    */   
/* 807:    */   public static Color getOverAllColor_()
/* 808:    */   {
/* 809:834 */     return overAllColor_;
/* 810:    */   }
/* 811:    */   
/* 812:    */   public static void setOverAllColor_(Color overAllColor_)
/* 813:    */   {
/* 814:837 */     overAllColor_ = overAllColor_;
/* 815:    */   }
/* 816:    */   
/* 817:    */   public static void removeSample(int index)
/* 818:    */   {
/* 819:840 */     if (removedSamples == null) {
/* 820:841 */       removedSamples = new ArrayList();
/* 821:    */     }
/* 822:843 */     removedSamples.add(((Sample)samples_.get(index)).name_);
/* 823:844 */     samples_.remove(index);
/* 824:    */   }
/* 825:    */   
/* 826:    */   public static void removeUserPath(String path)
/* 827:    */   {
/* 828:848 */     String pathName = getUserPathName(path);
/* 829:849 */     for (int i = 0; i < samples_.size(); i++)
/* 830:    */     {
/* 831:850 */       Sample samp = (Sample)samples_.get(i);
/* 832:851 */       samp.removeUserPath(pathName);
/* 833:852 */       overall_.removeUserPath(pathName);
/* 834:    */     }
/* 835:    */   }
/* 836:    */   
/* 837:    */   private static String getUserPathName(String path)
/* 838:    */   {
/* 839:    */     try
/* 840:    */     {
/* 841:858 */       BufferedReader in = new BufferedReader(new FileReader(path));
/* 842:859 */       String comp = "<pathName>";
/* 843:    */       String zeile;
/* 844:860 */       while ((zeile = in.readLine()) != null)
/* 845:    */       {
//* 846:    */         String zeile;
/* 847:861 */         comp = "<pathName>";
/* 848:862 */         if (zeile.startsWith(comp)) {
/* 849:863 */           return zeile.substring(zeile.indexOf(">") + 1);
/* 850:    */         }
/* 851:    */       }
/* 852:    */     }
/* 853:    */     catch (Exception e)
/* 854:    */     {
/* 855:870 */       openWarning("Error", "File: " + path + " not found");
/* 856:871 */       return "";
/* 857:    */     }
/* 858:    */     String zeile;
/* 859:873 */     return "";
/* 860:    */   }
/* 861:    */   
/* 862:    */   private static void openWarning(String title, String string)
/* 863:    */   {
/* 864:877 */     JFrame frame = new JFrame(title);
/* 865:878 */     frame.setVisible(true);
/* 866:879 */     frame.setBounds(200, 200, 350, 150);
/* 867:880 */     frame.setLayout(null);
/* 868:881 */     frame.setResizable(false);
/* 869:    */     
/* 870:883 */     JPanel panel = new JPanel();
/* 871:884 */     panel.setBounds(0, 0, 350, 150);
/* 872:885 */     panel.setBackground(Color.lightGray);
/* 873:886 */     panel.setVisible(true);
/* 874:887 */     panel.setLayout(null);
/* 875:888 */     frame.add(panel);
/* 876:    */     
/* 877:890 */     JLabel label = new JLabel(string);
/* 878:    */     
/* 879:892 */     label.setVisible(true);
/* 880:893 */     label.setForeground(Color.black);
/* 881:894 */     label.setBounds(0, 0, 350, 150);
/* 882:895 */     label.setLayout(null);
/* 883:896 */     panel.add(label);
/* 884:    */     
/* 885:898 */     frame.repaint();
/* 886:    */   }
/* 887:    */   
/* 888:    */   public static void addUserP(String userP)
/* 889:    */   {
/* 890:901 */     if (userPathways == null) {
/* 891:902 */       userPathways = new ArrayList();
/* 892:    */     }
/* 893:904 */     userPathways.add(userP);
/* 894:    */   }
/* 895:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.Project

 * JD-Core Version:    0.7.0.1

 */