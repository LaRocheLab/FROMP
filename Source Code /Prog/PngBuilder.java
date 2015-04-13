/*   1:    */ package Prog;
/*   2:    */ 
/*   3:    */ import Objects.EcNr;
/*   4:    */ import Objects.EcPosAndSize;
/*   5:    */ import Objects.EcSampleStats;
/*   6:    */ import Objects.PathwayWithEc;
/*   7:    */ import Objects.Sample;
/*   8:    */ import java.awt.Color;
/*   9:    */ import java.awt.image.BufferedImage;
/*  10:    */ import java.io.File;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.util.ArrayList;
/*  14:    */ import javax.imageio.ImageIO;
/*  15:    */ 
			  //This class builds the pngs output by FROMP  

/*  16:    */ public class PngBuilder
/*  17:    */ {
/*  18:    */   BufferedImage image;					// The image being generated
/*  19: 14 */   String separator_ = File.separator;		// the file seperator used by this OS
/*  20:    */   
/*  21:    */   public BufferedImage getAlteredPathway(ArrayList<EcPosAndSize> posList, String pathwayId, Sample sample)
/*  22:    */   {
/*  23: 18 */     System.out.println("1");
/*  24: 19 */     Color col = sample.sampleCol_;
/*  25:    */     try
/*  26:    */     {
/*  27: 23 */       this.image = ImageIO.read(new File("pics" + this.separator_ + pathwayId + ".png"));
/*  28: 24 */       reColorAllEcs(this.image, Color.white);
/*  29: 25 */       for (int i = 0; i < posList.size(); i++)
/*  30:    */       {
/*  31: 26 */         EcPosAndSize tmpPos = (EcPosAndSize)posList.get(i);
/*  32: 27 */         int xStart = tmpPos.x_ - tmpPos.width_ / 2;
/*  33: 28 */         int yStart = tmpPos.y_ - tmpPos.height_ / 2;
/*  34: 29 */         for (int x = xStart; x < xStart + tmpPos.width_; x++) {
/*  35: 30 */           for (int y = yStart; y < yStart + tmpPos.height_; y++) {
/*  36: 31 */             if ((x > 0) && (x < this.image.getWidth()))
/*  37:    */             {
/*  38: 33 */               if ((y > 0) && (y < this.image.getHeight()))
/*  39:    */               {
/*  40: 34 */                 if (this.image.getRGB(x, y) != Color.BLACK.getRGB()) {
/*  41: 35 */                   this.image.setRGB(x, y, col.getRGB());
/*  42:    */                 }
/*  43:    */               }
/*  44:    */               else
/*  45:    */               {
/*  46: 39 */                 System.err.println("y-out");
/*  47: 40 */                 System.err.println(tmpPos.x_);
/*  48: 41 */                 System.err.println(tmpPos.y_);
/*  49: 42 */                 System.err.println(tmpPos.width_);
/*  50: 43 */                 System.err.println(tmpPos.height_);
/*  51: 44 */                 System.err.println();
/*  52:    */               }
/*  53:    */             }
/*  54:    */             else
/*  55:    */             {
/*  56: 48 */               System.err.println("x-out");
/*  57: 49 */               System.err.println(tmpPos.x_);
/*  58: 50 */               System.err.println(tmpPos.y_);
/*  59: 51 */               System.err.println(tmpPos.width_);
/*  60: 52 */               System.err.println(tmpPos.height_);
/*  61: 53 */               System.err.println();
/*  62:    */             }
/*  63:    */           }
/*  64:    */         }
/*  65:    */       }
/*  66:    */     }
/*  67:    */     catch (IOException e)
/*  68:    */     {
/*  69: 60 */       e.printStackTrace();
/*  70:    */     }
/*  71: 62 */     return this.image;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void alterPathway(ArrayList<EcPosAndSize> posList, String pathwayId, Sample sample)
/*  75:    */   {
/*  76: 66 */     System.out.println("2");
/*  77: 67 */     Color col = sample.sampleCol_;
/*  78:    */     try
/*  79:    */     {
/*  80: 72 */       this.image = ImageIO.read(new File("pics" + this.separator_ + pathwayId + ".png"));
/*  81: 73 */       reColorAllEcs(this.image, Color.white);
/*  82: 74 */       for (int i = 0; i < posList.size(); i++)
/*  83:    */       {
/*  84: 75 */         EcPosAndSize tmpPos = (EcPosAndSize)posList.get(i);
/*  85: 76 */         int xStart = tmpPos.x_ - tmpPos.width_ / 2;
/*  86: 77 */         int yStart = tmpPos.y_ - tmpPos.height_ / 2;
/*  87: 78 */         for (int x = xStart; x < xStart + tmpPos.width_; x++) {
/*  88: 79 */           for (int y = yStart; y < yStart + tmpPos.height_; y++) {
/*  89: 80 */             if ((x > 0) && (x < this.image.getWidth()))
/*  90:    */             {
/*  91: 82 */               if ((y > 0) && (y < this.image.getHeight()))
/*  92:    */               {
/*  93: 83 */                 if (this.image.getRGB(x, y) != Color.BLACK.getRGB()) {
/*  94: 84 */                   this.image.setRGB(x, y, col.getRGB());
/*  95:    */                 }
/*  96:    */               }
/*  97:    */               else
/*  98:    */               {
/*  99: 88 */                 System.err.println("y-out");
/* 100: 89 */                 System.err.println(tmpPos.x_);
/* 101: 90 */                 System.err.println(tmpPos.y_);
/* 102: 91 */                 System.err.println(tmpPos.width_);
/* 103: 92 */                 System.err.println(tmpPos.height_);
/* 104: 93 */                 System.err.println();
/* 105:    */               }
/* 106:    */             }
/* 107:    */             else
/* 108:    */             {
/* 109: 97 */               System.err.println("x-out");
/* 110: 98 */               System.err.println(tmpPos.x_);
/* 111: 99 */               System.err.println(tmpPos.y_);
/* 112:100 */               System.err.println(tmpPos.width_);
/* 113:101 */               System.err.println(tmpPos.height_);
/* 114:102 */               System.err.println();
/* 115:    */             }
/* 116:    */           }
/* 117:    */         }
/* 118:    */       }
/* 119:    */     }
/* 120:    */     catch (IOException e)
/* 121:    */     {
/* 122:109 */       e.printStackTrace();
/* 123:    */     }
/* 124:    */     try
/* 125:    */     {
/* 126:112 */       File outputfile = new File("outpics" + this.separator_ + pathwayId + ".png");
/* 127:113 */       ImageIO.write(this.image, "png", outputfile);
/* 128:    */     }
/* 129:    */     catch (IOException localIOException1) {}
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void alterPathway(PathwayWithEc tmpPath, Sample sample)
/* 133:    */   {
/* 134:120 */     System.out.println("3");
/* 135:121 */     Color col = sample.sampleCol_;
/* 136:    */     
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:126 */     int stepper = 0;
/* 141:127 */     int statsCnt = 0;
/* 142:128 */     EcSampleStats tmpStats = null;
/* 143:    */     try
/* 144:    */     {
/* 145:133 */       this.image = ImageIO.read(new File("pics" + this.separator_ + tmpPath.id_ + ".png"));
/* 146:134 */       reColorAllEcs(this.image, Color.white);
/* 147:135 */       for (int ecCnt = 0; ecCnt < tmpPath.ecNrs_.size(); ecCnt++)
/* 148:    */       {
/* 149:136 */         EcNr tmpEc = (EcNr)tmpPath.ecNrs_.get(ecCnt);
/* 150:137 */         System.out.println("possize " + tmpEc.posSize_.size());
/* 151:138 */         for (int i = 0; i < tmpEc.posSize_.size(); i++)
/* 152:    */         {
/* 153:139 */           statsCnt = 0;
/* 154:140 */           EcPosAndSize tmpPos = (EcPosAndSize)tmpEc.posSize_.get(i);
/* 155:141 */           int xStart = tmpPos.x_ - tmpPos.width_ / 2;
/* 156:142 */           int yStart = tmpPos.y_ - tmpPos.height_ / 2;
/* 157:143 */           int statsSum = 0;
/* 158:144 */           for (int statCnt = 0; statsCnt < tmpEc.stats_.size(); statCnt++) {
/* 159:145 */             statsSum += ((EcSampleStats)tmpEc.stats_.get(statCnt)).amount_;
/* 160:    */           }
/* 161:147 */           int sampSteps = tmpPos.width_ / statsSum;
/* 162:149 */           if (!sample.singleSample_)
/* 163:    */           {
/* 164:150 */             tmpStats = (EcSampleStats)tmpEc.stats_.get(statsCnt);
/* 165:151 */             col = tmpStats.col_;
/* 166:152 */             stepper = 0;
/* 167:    */           }
/* 168:154 */           statsCnt = 0;
/* 169:155 */           stepper = 0;
/* 170:156 */           for (int x = xStart; x < xStart + tmpPos.width_; x++)
/* 171:    */           {
/* 172:157 */             if ((!sample.singleSample_) && 
/* 173:158 */               (stepper > tmpStats.amount_ * sampSteps) && 
/* 174:159 */               (statsCnt < tmpEc.stats_.size()))
/* 175:    */             {
/* 176:160 */               statsCnt++;
/* 177:161 */               tmpStats = (EcSampleStats)tmpEc.stats_.get(statsCnt);
/* 178:162 */               col = tmpStats.col_;
/* 179:    */             }
/* 180:166 */             for (int y = yStart; y < yStart + tmpPos.height_; y++) {
/* 181:167 */               if ((x > 0) && (x < this.image.getWidth()))
/* 182:    */               {
/* 183:169 */                 if ((y > 0) && (y < this.image.getHeight()))
/* 184:    */                 {
/* 185:170 */                   if (this.image.getRGB(x, y) != Color.BLACK.getRGB()) {
/* 186:171 */                     this.image.setRGB(x, y, col.getRGB());
/* 187:    */                   }
/* 188:    */                 }
/* 189:    */                 else
/* 190:    */                 {
/* 191:175 */                   System.err.println("y-out");
/* 192:176 */                   System.err.println(tmpPos.x_);
/* 193:177 */                   System.err.println(tmpPos.y_);
/* 194:178 */                   System.err.println(tmpPos.width_);
/* 195:179 */                   System.err.println(tmpPos.height_);
/* 196:180 */                   System.err.println();
/* 197:    */                 }
/* 198:    */               }
/* 199:    */               else
/* 200:    */               {
/* 201:184 */                 System.err.println("x-out");
/* 202:185 */                 System.err.println(tmpPos.x_);
/* 203:186 */                 System.err.println(tmpPos.y_);
/* 204:187 */                 System.err.println(tmpPos.width_);
/* 205:188 */                 System.err.println(tmpPos.height_);
/* 206:189 */                 System.err.println();
/* 207:    */               }
/* 208:    */             }
/* 209:192 */             stepper++;
/* 210:    */           }
/* 211:    */         }
/* 212:    */       }
/* 213:    */     }
/* 214:    */     catch (IOException e)
/* 215:    */     {
/* 216:198 */       e.printStackTrace();
/* 217:    */     }
/* 218:    */     try
/* 219:    */     {
/* 220:201 */       File outputfile = new File("outpics" + this.separator_ + tmpPath.id_ + ".png");
/* 221:202 */       ImageIO.write(this.image, "png", outputfile);
/* 222:    */     }
/* 223:    */     catch (IOException localIOException1) {}
/* 224:    */   }
/* 225:    */   
/* 226:    */   public BufferedImage getAlteredPathway(PathwayWithEc tmpPath, Sample sample)
/* 227:    */   {
/* 228:210 */     Color col = sample.sampleCol_;
/* 229:    */     System.out.println("test 123");
/* 230:    */ 
/* 231:    */ 
/* 232:214 */     int stepper = 0;
/* 233:215 */     int statsCnt = 0;
/* 234:216 */     EcSampleStats tmpStats = null;
/* 235:217 */     double perc = 0.0D;
/* 236:    */     try
/* 237:    */     {
/* 238:220 */       this.image = ImageIO.read(new File("pics" + this.separator_ + tmpPath.id_ + ".png"));
/* 239:221 */       reColorAllEcs(this.image, Color.white);
/* 240:222 */       for (int ecCnt = 0; ecCnt < tmpPath.ecNrs_.size(); ecCnt++)
/* 241:    */       {
/* 242:224 */         EcNr tmpEc = (EcNr)tmpPath.ecNrs_.get(ecCnt);
/* 243:    */         
/* 244:226 */         statsCnt = 0;
/* 245:227 */         for (int i = 0; i < tmpEc.posSize_.size(); i++)
/* 246:    */         {
/* 247:228 */           statsCnt = 0;
/* 248:229 */           EcPosAndSize tmpPos = (EcPosAndSize)tmpEc.posSize_.get(i);
/* 249:230 */           int xStart = tmpPos.x_ - tmpPos.width_ / 2;
/* 250:231 */           int yStart = tmpPos.y_ - tmpPos.height_ / 2;
/* 251:232 */           int statsSum = 0;
/* 252:234 */           for (int statCnt = 0; statCnt < tmpEc.stats_.size(); statCnt++)
/* 253:    */           {
/* 254:235 */             statsSum += ((EcSampleStats)tmpEc.stats_.get(statCnt)).amount_;
/* 255:236 */             if (((EcSampleStats)tmpEc.stats_.get(statCnt)).amount_ == 0) {
/* 256:237 */               statsSum += tmpEc.amount_;
/* 257:    */             }
/* 258:    */           }
/* 259:242 */           if ((statsSum == 0) && (!sample.singleSample_)) {
/* 260:    */             break;
/* 261:    */           }
/* 262:243 */           if ((!sample.singleSample_) && (statsCnt < tmpEc.stats_.size()))
/* 263:    */           {
/* 264:244 */             tmpStats = (EcSampleStats)tmpEc.stats_.get(statsCnt);
/* 265:245 */             col = tmpStats.col_;
/* 266:246 */             stepper = 0;
/* 267:247 */             perc = (double)tmpStats.amount_ / (double)statsSum;
                          System.out.println("tmpstats=" + tmpStats.amount_);
                          System.out.println("statsSum=" + statsSum);
                          System.out.println("perc=" + perc);
/* 268:    */           }
/* 269:256 */           for (int x = xStart; x < xStart + tmpPos.width_; x++)
/* 270:    */           {
/* 271:257 */             if ((!sample.singleSample_) && (stepper > tmpPos.width_* perc))
/* 273:    */             {
//* 274:259 */               stepper = 0;
/* 275:260 */               if (statsCnt < tmpEc.stats_.size() - 1)
/* 276:    */               {
							  stepper = 0;
/* 277:261 */                 statsCnt++;
/* 278:262 */                 tmpStats = (EcSampleStats)tmpEc.stats_.get(statsCnt);
/* 279:263 */                 col = tmpStats.col_;
/* 280:264 */                 perc = (double)tmpStats.amount_ / (double)statsSum;
                              System.out.println("tmpstats=" + tmpStats.amount_);
                              System.out.println("statsSum=" + statsSum);
                              System.out.println("perc=" + perc);
/* 281:    */               }
/* 282:    */             }
/* 283:269 */             for (int y = yStart; y < yStart + tmpPos.height_; y++) {
/* 284:270 */               if ((x > 0) && (x < this.image.getWidth()))
/* 285:    */               {
/* 286:271 */                 if (col == null) {
/* 287:272 */                   col = Color.ORANGE;
/* 288:    */                 }
/* 289:274 */                 if ((y > 0) && (y < this.image.getHeight()))
/* 290:    */                 {
/* 291:275 */                   if (this.image.getRGB(x, y) != Color.BLACK.getRGB()) {
/* 292:276 */                     this.image.setRGB(x, y, col.getRGB());
/* 293:    */                   }
/* 294:    */                 }
/* 295:    */                 else
/* 296:    */                 {
/* 297:280 */                   System.err.println("y-out");
/* 298:281 */                   System.err.println(tmpPos.x_);
/* 299:282 */                   System.err.println(tmpPos.y_);
/* 300:283 */                   System.err.println(tmpPos.width_);
/* 301:284 */                   System.err.println(tmpPos.height_);
/* 302:285 */                   System.err.println();
/* 303:    */                 }
/* 304:    */               }
/* 305:    */               else
/* 306:    */               {
/* 307:289 */                 System.err.println("x-out");
/* 308:290 */                 System.err.println(tmpPos.x_);
/* 309:291 */                 System.err.println(tmpPos.y_);
/* 310:292 */                 System.err.println(tmpPos.width_);
/* 311:293 */                 System.err.println(tmpPos.height_);
/* 312:294 */                 System.err.println();
/* 313:    */               }
/* 314:    */             }
/* 315:297 */             stepper++;
/* 316:    */           }
/* 317:    */         }
/* 318:    */       }
/* 319:    */     }
/* 320:    */     catch (IOException e)
/* 321:    */     {
/* 322:303 */       e.printStackTrace();
/* 323:    */     }
/* 324:305 */     return this.image;
/* 325:    */   }
/* 326:    */   
/* 327:    */   private void reColorAllEcs(BufferedImage image, Color c)
/* 328:    */   {
/* 329:310 */     for (int x = 0; x < image.getWidth(); x++) {
/* 330:311 */       for (int y = 0; y < image.getHeight(); y++) {
/* 331:312 */         if ((image.getRGB(x, y) != Color.black.getRGB()) && (image.getRGB(x, y) != Color.white.getRGB())) {
/* 332:313 */           image.setRGB(x, y, c.getRGB());
/* 333:    */         }
/* 334:    */       }
/* 335:    */     }
/* 336:    */   }
/* 337:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Prog.PngBuilder

 * JD-Core Version:    0.7.0.1

 */