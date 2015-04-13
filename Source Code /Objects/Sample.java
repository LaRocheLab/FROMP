/*   1:    */ package Objects;
/*   2:    */ 
/*   3:    */ import Prog.StringReader;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.io.BufferedReader;
/*   6:    */ import java.io.BufferedWriter;
/*   7:    */ import java.io.FileWriter;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ 
			  //This contains all of the pertinant data in each sample, including all of the ECs and the pathways they map to, all of the pathways and the ECs who map to them 
			  //as well as many other important pieces of data like the sample name, etc.

/*  12:    */ public class Sample
/*  13:    */ {
/*  14:    */   StringReader reader_;							// String reader used to load new samples 
/*  15:    */   public String name_;							// Name of the sample
/*  16:    */   public String fullPath_;						// The file path to the sample
/*  17:    */   public Color sampleCol_;						// the sample colour
/*  18:    */   public BufferedReader sample_;					// Buffered reader used to load new samples 
/*  19:    */   public ArrayList<PathwayWithEc> pathways_;		// Array list of pathways with ecs mapped to them in the sample
/*  20:    */   public ArrayList<EcWithPathway> ecs_;			// Array list of ecs with pathways in the sample
/*  21:    */   public ArrayList<PathwayWithEc> rnPathways_;	// 
/*  22:    */   public ArrayList<EcWithPathway> rns_;			// 
/*  23:    */   public ArrayList<ConvertStat> conversions_;		// the conversion statistrics for ecs in this sample
/*  24:    */   public boolean imported;						// 
/*  25: 28 */   public boolean matrixSample = false;			// 
/*  26:    */   public boolean inUse;							// 
/*  27:    */   public boolean valuesSet;						// 
/*  28: 31 */   public boolean singleSample_ = true;			// 
/*  29: 33 */   public boolean legitSample = false;				// 
/*  30: 35 */   public int indexNr = 0;							// 
				public boolean onoff = true;					// Used for display purposes in the GUI
/*  31:    */   
/*  32:    */   public Sample()
/*  33:    */   {
/*  34: 40 */     this.sample_ = null;
/*  35: 41 */     this.fullPath_ = "";
/*  36: 42 */     this.sampleCol_ = Color.white;
/*  37: 43 */     this.reader_ = new StringReader();
/*  38: 44 */     this.ecs_ = new ArrayList();
/*  39: 45 */     this.pathways_ = new ArrayList();
/*  40: 46 */     this.rns_ = new ArrayList();
/*  41: 47 */     this.rnPathways_ = new ArrayList();
/*  42: 48 */     this.conversions_ = new ArrayList();
/*  43: 49 */     this.inUse = false;
/*  44: 50 */     this.valuesSet = false;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Sample(String name, String fullpath)
/*  48:    */   {
/*  49: 53 */     this.name_ = name;
/*  50: 54 */     this.fullPath_ = fullpath;
/*  51: 55 */     this.sampleCol_ = Color.white;
/*  52: 56 */     this.reader_ = new StringReader();
/*  53: 57 */     this.ecs_ = new ArrayList();
/*  54: 58 */     this.pathways_ = new ArrayList();
/*  55: 59 */     this.rns_ = new ArrayList();
/*  56: 60 */     this.rnPathways_ = new ArrayList();
/*  57: 61 */     this.conversions_ = new ArrayList();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Sample(String name, String fullpath, Color col)
/*  61:    */   {
/*  62: 64 */     this.name_ = name;
/*  63: 65 */     this.fullPath_ = fullpath;
/*  64: 66 */     this.sampleCol_ = col;
/*  65: 67 */     this.reader_ = new StringReader();
/*  66: 68 */     this.ecs_ = new ArrayList();
/*  67: 69 */     this.pathways_ = new ArrayList();
/*  68: 70 */     this.rns_ = new ArrayList();
/*  69: 71 */     this.rnPathways_ = new ArrayList();
/*  70: 72 */     this.conversions_ = new ArrayList();
/*  71: 73 */     this.inUse = true;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Sample(String name, String fullpath, Color col, boolean using)
/*  75:    */   {
/*  76: 76 */     this.name_ = name;
/*  77: 77 */     this.fullPath_ = fullpath;
/*  78: 78 */     this.sampleCol_ = col;
/*  79: 79 */     this.inUse = using;
/*  80: 80 */     this.reader_ = new StringReader();
/*  81: 81 */     this.ecs_ = new ArrayList();
/*  82: 82 */     this.pathways_ = new ArrayList();
/*  83: 83 */     this.rns_ = new ArrayList();
/*  84: 84 */     this.rnPathways_ = new ArrayList();
/*  85: 85 */     this.conversions_ = new ArrayList();
/*  86:    */   }
/*  87:    */   
/*  88:    */   public Sample(Sample sample)
/*  89:    */   {
/*  90: 88 */     this.name_ = sample.name_;
/*  91: 89 */     this.fullPath_ = sample.fullPath_;
/*  92: 90 */     this.sampleCol_ = sample.sampleCol_;
/*  93: 91 */     this.reader_ = new StringReader();
/*  94:    */     
/*  95: 93 */     this.ecs_ = new ArrayList();
/*  96: 94 */     this.pathways_ = new ArrayList();
/*  97: 95 */     this.rns_ = new ArrayList();
/*  98: 96 */     this.rnPathways_ = new ArrayList();
/*  99: 97 */     this.conversions_ = new ArrayList();
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setColor(Color col)
/* 103:    */   {
/* 104:103 */     this.sampleCol_ = col;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void loadSample()
/* 108:    */   {
/* 109:110 */     this.sample_ = this.reader_.readTxt(this.fullPath_);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void clearPaths()
/* 113:    */   {
/* 114:116 */     this.valuesSet = false;
/* 115:117 */     this.pathways_ = new ArrayList();
/* 116:118 */     this.ecs_ = new ArrayList();
/* 117:119 */     this.rns_ = new ArrayList();
/* 118:120 */     this.rnPathways_ = new ArrayList();
/* 119:121 */     this.conversions_ = new ArrayList();
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void addPaths(PathwayWithEc path)
/* 123:    */   {
/* 124:124 */     if (this.pathways_ == null) {
/* 125:125 */       this.pathways_ = new ArrayList();
/* 126:    */     }
/* 127:127 */     this.pathways_.add(path);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void addRnPaths(PathwayWithEc path)
/* 131:    */   {
/* 132:130 */     this.rnPathways_.add(path);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void toggleUse()
/* 136:    */   {
/* 137:137 */     if (this.inUse) {
/* 138:138 */       this.inUse = false;
/* 139:    */     } else {
/* 140:141 */       this.inUse = true;
/* 141:    */     }
/* 142:    */   }
				public void toggleonoff()
/* 136:    */   {
/* 137:137 */     if (this.onoff) {
/* 138:138 */       this.onoff = false;
/* 139:    */     } else {
/* 140:141 */       this.onoff = true;
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   public void addConvStats(ConvertStat stat)
/* 145:    */   {
/* 146:148 */     if (this.conversions_ == null) {
/* 147:149 */       this.conversions_ = new ArrayList();
/* 148:    */     }
/* 149:151 */     for (int statCnt = 0; statCnt < this.conversions_.size(); statCnt++) {
/* 150:152 */       if (((ConvertStat)this.conversions_.get(statCnt)).desc_.contentEquals(stat.desc_))
/* 151:    */       {
/* 152:153 */         ((ConvertStat)this.conversions_.get(statCnt)).addStatsCnt(stat);
/* 153:    */         
/* 154:155 */         return;
/* 155:    */       }
/* 156:    */     }
/* 157:158 */     this.conversions_.add(stat);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void writeConvStats(String path)
/* 161:    */   {
/* 162:    */     try
/* 163:    */     {
/* 164:166 */       String desc = "";
/* 165:167 */       int ecAm = 0;
/* 166:168 */       int pfamToEcAm = 0;
/* 167:169 */       int pfamToRnAm = 0;
/* 168:170 */       int ecAmSum = 0;
/* 169:171 */       int pfamToEcAmSum = 0;
/* 170:172 */       int pfamToRnAmSum = 0;
/* 171:173 */       BufferedWriter out = new BufferedWriter(new FileWriter(path + this.name_ + ".txt"));
/* 172:174 */       out.write("! " + this.name_);
/* 173:175 */       out.newLine();
/* 174:176 */       for (int statCnt = 0; statCnt < this.conversions_.size(); statCnt++)
/* 175:    */       {
/* 176:177 */         desc = ((ConvertStat)this.conversions_.get(statCnt)).desc_;
/* 177:178 */         ecAm = ((ConvertStat)this.conversions_.get(statCnt)).ecAmount_;
/* 178:179 */         ecAmSum += ecAm;
/* 179:180 */         pfamToEcAm = ((ConvertStat)this.conversions_.get(statCnt)).pfamToEcAmount_;
/* 180:181 */         pfamToEcAmSum += pfamToEcAm;
/* 181:182 */         pfamToRnAm = ((ConvertStat)this.conversions_.get(statCnt)).pfamToRnAmount_;
/* 182:183 */         pfamToRnAmSum += pfamToRnAm;
/* 183:184 */         out.write(desc + "," + ecAm + "," + pfamToEcAm + "," + pfamToRnAm);
/* 184:185 */         out.newLine();
/* 185:    */       }
/* 186:187 */       out.newLine();
/* 187:188 */       out.write("----SumUp----:," + ecAmSum + "," + pfamToEcAmSum + "," + pfamToRnAmSum);
/* 188:189 */       out.close();
/* 189:    */     }
/* 190:    */     catch (IOException e)
/* 191:    */     {
/* 192:192 */       e.printStackTrace();
/* 193:    */     }
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void printConvStats()
/* 197:    */   {
/* 198:202 */     String desc = "";
/* 199:203 */     int ecAm = 0;
/* 200:204 */     int pfamToEcAm = 0;
/* 201:205 */     int pfamToRnAm = 0;
/* 202:206 */     for (int statCnt = 0; statCnt < this.conversions_.size(); statCnt++)
/* 203:    */     {
/* 204:207 */       desc = ((ConvertStat)this.conversions_.get(statCnt)).desc_;
/* 205:208 */       ecAm = ((ConvertStat)this.conversions_.get(statCnt)).ecAmount_;
/* 206:209 */       pfamToEcAm = ((ConvertStat)this.conversions_.get(statCnt)).pfamToEcAmount_;
/* 207:210 */       pfamToRnAm = ((ConvertStat)this.conversions_.get(statCnt)).pfamToRnAmount_;
/* 208:211 */       System.out.println(desc + "," + ecAm + "," + pfamToEcAm + "," + pfamToRnAm);
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void addEc(EcWithPathway ecNr)
/* 213:    */   {
/* 214:220 */     for (int ecCnt = 0; ecCnt < this.ecs_.size(); ecCnt++) {
/* 215:221 */       if (ecNr.isSameEc((EcNr)this.ecs_.get(ecCnt)))
/* 216:    */       {
/* 217:222 */         ((EcWithPathway)this.ecs_.get(ecCnt)).amount_ += ecNr.amount_;
/* 218:224 */         for (int statCnt = 0; statCnt < ecNr.stats_.size(); statCnt++) {
/* 219:225 */           ((EcWithPathway)this.ecs_.get(ecCnt)).stats_.add((EcSampleStats)ecNr.stats_.get(statCnt));
/* 220:    */         }
/* 221:228 */         return;
/* 222:    */       }
/* 223:    */     }
/* 224:232 */     this.ecs_.add(ecNr);
/* 225:    */   }
/* 226:    */   
/* 227:    */   public PathwayWithEc getPath(String pathId)
/* 228:    */   {
/* 229:235 */     for (int pathCnt = 0; pathCnt < this.pathways_.size(); pathCnt++) {
/* 230:236 */       if (((PathwayWithEc)this.pathways_.get(pathCnt)).id_.contentEquals(pathId)) {
/* 231:237 */         return (PathwayWithEc)this.pathways_.get(pathCnt);
/* 232:    */       }
/* 233:    */     }
/* 234:240 */     return null;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public EcWithPathway getEc(String ecId)
/* 238:    */   {
/* 239:243 */     for (int ecCnt = 0; ecCnt < this.ecs_.size(); ecCnt++) {
/* 240:244 */       if (((EcWithPathway)this.ecs_.get(ecCnt)).name_.contentEquals(ecId)) {
/* 241:245 */         return (EcWithPathway)this.ecs_.get(ecCnt);
/* 242:    */       }
/* 243:    */     }
/* 244:248 */     return null;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public void integratePathway(PathwayWithEc pathway)
/* 248:    */   {
/* 249:251 */     PathwayWithEc newPath = new PathwayWithEc(pathway);
/* 250:254 */     for (int ecPwCnt = 0; ecPwCnt < pathway.ecNrs_.size(); ecPwCnt++)
/* 251:    */     {
/* 252:255 */       EcNr pwEc = (EcNr)pathway.ecNrs_.get(ecPwCnt);
/* 253:256 */       for (int ecCnt = 0; ecCnt < this.ecs_.size(); ecCnt++)
/* 254:    */       {
/* 255:257 */         EcWithPathway smpEc = (EcWithPathway)this.ecs_.get(ecCnt);
/* 256:258 */         if (pwEc.isSameEc(smpEc))
/* 257:    */         {
/* 258:259 */           newPath.addEc(smpEc);
/* 259:260 */           smpEc.addPathway(pathway);
/* 260:261 */           break;
/* 261:    */         }
/* 262:    */       }
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void removeUserPath(String userPath)
/* 267:    */   {
/* 268:269 */     for (int ecCnt = 0; ecCnt < this.ecs_.size(); ecCnt++)
/* 269:    */     {
/* 270:270 */       EcWithPathway ecWpL = (EcWithPathway)this.ecs_.get(ecCnt);
/* 271:271 */       for (int pwCnt = 0; pwCnt < ecWpL.pathways_.size(); pwCnt++)
/* 272:    */       {
/* 273:272 */         Pathway pw = (Pathway)ecWpL.pathways_.get(pwCnt);
/* 274:273 */         if (pw.name_.contentEquals(userPath))
/* 275:    */         {
/* 276:274 */           ecWpL.pathways_.remove(pwCnt);
/* 277:275 */           break;
/* 278:    */         }
/* 279:    */       }
/* 280:    */     }
/* 281:279 */     for (int pwCnt = 0; pwCnt < this.pathways_.size(); pwCnt++)
/* 282:    */     {
/* 283:280 */       Pathway pw = (Pathway)this.pathways_.get(pwCnt);
/* 284:281 */       if (pw.name_.contentEquals(userPath))
/* 285:    */       {
/* 286:282 */         this.pathways_.remove(pwCnt);
/* 287:283 */         break;
/* 288:    */       }
/* 289:    */     }
/* 290:    */   }
/* 291:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.Sample

 * JD-Core Version:    0.7.0.1

 */