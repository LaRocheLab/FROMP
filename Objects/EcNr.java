/*   1:    */ package Objects;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.io.BufferedReader;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ 
/*   8:    */ public class EcNr
/*   9:    */ {
/*  10:    */   public String name_;
/*  11:    */   public String bioName_;
/*  12:    */   public float weight_;
/*  13:    */   public int amount_;
/*  14:    */   public Color samColor_;
/*  15:    */   public int sampleNr_;
/*  16:    */   public ArrayList<EcPosAndSize> posSize_;
/*  17:    */   public ArrayList<EcSampleStats> stats_;
/*  18:    */   public int[] nr_;
/*  19: 20 */   public boolean unique_ = true;
/*  20: 21 */   public boolean isPfam_ = false;
/*  21: 22 */   public boolean incomplete = false;
/*  22: 23 */   public boolean unmapped = false;
/*  23:    */   public String type_;
/*  24:    */   public String rnNr_;
/*  25:    */   public int maxChainLength_;
/*  26:    */   public int longestChain_;
/*  27:    */   public double chainMultiply1_;
/*  28:    */   public double chainMultiply2_;
/*  29: 34 */   public boolean userEC = false;
/*  30:    */   static BufferedReader pfam2Go;
/*  31:    */   static BufferedReader Kegg2Go;
/*  32:    */   public ArrayList<Repseqs> repseqs_;
/*  33:    */   
/*  34:    */   public EcNr(String[] name)
/*  35:    */   {
/*  36: 43 */     this.maxChainLength_ = 1;
/*  37: 44 */     this.longestChain_ = 1;
/*  38: 45 */     this.repseqs_ = new ArrayList();
/*  39:    */     
/*  40: 47 */     this.posSize_ = new ArrayList();
/*  41: 48 */     this.stats_ = new ArrayList();
/*  42: 49 */     this.name_ = name[0];
/*  43: 50 */     if (isNumber(name[1]))
/*  44:    */     {
/*  45: 52 */       this.amount_ = Integer.valueOf(name[1]).intValue();
/*  46: 53 */       if (this.amount_ == 0) {
/*  47: 54 */         this.amount_ = 0;
/*  48:    */       }
/*  49:    */     }
/*  50:    */     else
/*  51:    */     {
/*  52: 58 */       this.amount_ = 1;
/*  53:    */     }
/*  54: 60 */     this.type_ = name[2];
/*  55: 61 */     if (this.type_.contentEquals("PF")) {
/*  56: 62 */       this.isPfam_ = true;
/*  57:    */     }
/*  58: 64 */     this.nr_ = new int[4];
/*  59: 65 */     if (this.type_.contentEquals("EC")) {
/*  60: 66 */       ecToVec();
/*  61:    */     }
/*  62: 68 */     addRepseq(name[3], this.amount_);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public EcNr(String name)
/*  66:    */   {
/*  67: 71 */     this.longestChain_ = 1;
/*  68: 72 */     this.maxChainLength_ = 1;
/*  69: 73 */     this.repseqs_ = new ArrayList();
/*  70: 74 */     this.posSize_ = new ArrayList();
/*  71: 75 */     this.stats_ = new ArrayList();
/*  72: 76 */     this.name_ = name;
/*  73: 77 */     this.amount_ = 1;
/*  74: 78 */     this.nr_ = new int[4];
/*  75: 79 */     if ((this.name_.startsWith("PF")) || (this.name_.startsWith("R"))) {
/*  76: 83 */       this.isPfam_ = true;
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public EcNr(EcNr ecNr)
/*  81:    */   {
/*  82: 88 */     this.maxChainLength_ = ecNr.maxChainLength_;
/*  83: 89 */     if (this.maxChainLength_ == 0) {
/*  84: 90 */       this.maxChainLength_ = 1;
/*  85:    */     }
/*  86: 92 */     this.longestChain_ = 1;
/*  87: 93 */     this.repseqs_ = new ArrayList();
/*  88: 94 */     this.posSize_ = new ArrayList();
/*  89: 95 */     this.stats_ = new ArrayList();
/*  90: 96 */     this.name_ = ecNr.name_;
/*  91: 97 */     this.weight_ = ecNr.weight_;
/*  92: 98 */     this.amount_ = ecNr.amount_;
/*  93: 99 */     this.samColor_ = ecNr.samColor_;
/*  94:100 */     this.bioName_ = ecNr.bioName_;
/*  95:101 */     this.nr_ = new int[4];
/*  96:102 */     this.unique_ = ecNr.unique_;
/*  97:103 */     this.incomplete = ecNr.incomplete;
/*  98:104 */     this.unmapped = ecNr.unmapped;
/*  99:105 */     this.isPfam_ = ecNr.isPfam_;
/* 100:106 */     for (int i = 0; i < this.nr_.length; i++) {
/* 101:107 */       this.nr_[i] = ecNr.nr_[i];
/* 102:    */     }
/* 103:110 */     for (int repCnt = 0; repCnt < ecNr.repseqs_.size(); repCnt++) {
/* 104:112 */       this.repseqs_.add((Repseqs)ecNr.repseqs_.get(repCnt));
/* 105:    */     }
/* 106:114 */     for (int statcnt = 0; statcnt < ecNr.stats_.size(); statcnt++) {
/* 107:115 */       this.stats_.add((EcSampleStats)ecNr.stats_.get(statcnt));
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   public int getStatsSum()
/* 112:    */   {
/* 113:123 */     int ret = 0;
/* 114:124 */     for (int i = 0; i < this.stats_.size(); i++) {
/* 115:125 */       ret += ((EcSampleStats)this.stats_.get(i)).amount_;
/* 116:    */     }
/* 117:127 */     return ret;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setWeight(float w)
/* 121:    */   {
/* 122:134 */     this.weight_ = w;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void adaptWeightToChain()
/* 126:    */   {
/* 127:137 */     this.chainMultiply1_ = Math.sqrt(this.maxChainLength_);
/* 128:138 */     this.chainMultiply2_ = (Math.sqrt(this.longestChain_) * Math.sqrt(this.maxChainLength_));
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void increaseAmount(int amount)
/* 132:    */   {
/* 133:146 */     this.amount_ += amount;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void increaseAmount(EcNr ecnr)
/* 137:    */   {
/* 138:154 */     this.amount_ += ecnr.amount_;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setSample(Color samcol, short SampleNr)
/* 142:    */   {
/* 143:162 */     this.samColor_ = samcol;
/* 144:163 */     this.sampleNr_ = SampleNr;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void addPos(ArrayList<EcPosAndSize> pos)
/* 148:    */   {
/* 149:169 */     for (int i = 0; i < pos.size(); i++) {
/* 150:170 */       this.posSize_.add((EcPosAndSize)pos.get(i));
/* 151:    */     }
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void addSample(EcNr ecNr)
/* 155:    */   {
/* 156:178 */     EcSampleStats tmpstats = new EcSampleStats(ecNr);
/* 157:    */     
/* 158:180 */     this.stats_.add(tmpstats);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public boolean isSameEc(EcNr ec)
/* 162:    */   {
/* 163:185 */     return this.name_.contentEquals(ec.name_);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void ecToVec()
/* 167:    */   {
/* 168:194 */     String tmpec = this.name_;
/* 169:195 */     if (this.name_.contains("°")) {
/* 170:196 */       return;
/* 171:    */     }
/* 172:198 */     int index = 0;
/* 173:199 */     while (tmpec.length() > 0)
/* 174:    */     {
/* 175:200 */       if (tmpec.charAt(0) == '-')
/* 176:    */       {
/* 177:201 */         while (index < this.nr_.length)
/* 178:    */         {
/* 179:202 */           this.nr_[index] = -1;
/* 180:203 */           index++;
/* 181:    */         }
/* 182:205 */         break;
/* 183:    */       }
/* 184:207 */       if (tmpec.indexOf(".") < 1)
/* 185:    */       {
/* 186:208 */         this.nr_[index] = Integer.valueOf(tmpec).intValue();
/* 187:209 */         break;
/* 188:    */       }
/* 189:211 */       String tmp = tmpec.substring(0, tmpec.indexOf("."));
/* 190:212 */       tmpec = tmpec.substring(tmpec.indexOf(".") + 1);
/* 191:    */       
/* 192:214 */       this.nr_[index] = Integer.valueOf(tmp).intValue();
/* 193:215 */       index++;
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   public boolean isNumber(String in)
/* 198:    */   {
/* 199:226 */     if (in == null) {
/* 200:226 */       return false;
/* 201:    */     }
/* 202:227 */     if (in.length() < 1) {
/* 203:227 */       return false;
/* 204:    */     }
/* 205:228 */     for (int i = 0; i < in.length(); i++) {
/* 206:229 */       if (!isNumber(in.charAt(i))) {
/* 207:230 */         return false;
/* 208:    */       }
/* 209:    */     }
/* 210:233 */     return true;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public boolean isNumber(char c)
/* 214:    */   {
/* 215:241 */     if ((c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6') || (c == '7') || (c == '8') || (c == '9')) {
/* 216:242 */       return true;
/* 217:    */     }
/* 218:245 */     return false;
/* 219:    */   }
/* 220:    */   
/* 221:    */   private void addRepseq(String repseq, int amount)
/* 222:    */   {
/* 223:249 */     for (int repCnt = 0; repCnt < this.repseqs_.size(); repCnt++) {
/* 224:250 */       if (((Repseqs)this.repseqs_.get(repCnt)).isSameReps(repseq))
/* 225:    */       {
/* 226:251 */         ((Repseqs)this.repseqs_.get(repCnt)).addAmount(amount);
/* 227:252 */         System.out.println("repseq added");
/* 228:253 */         return;
/* 229:    */       }
/* 230:    */     }
/* 231:256 */     this.repseqs_.add(new Repseqs(repseq, amount));
/* 232:    */   }
/* 233:    */   
/* 234:    */   public boolean isCompleteEc()
/* 235:    */   {
/* 236:261 */     String ec = this.name_;
/* 237:262 */     if (ec == null) {
/* 238:262 */       return false;
/* 239:    */     }
/* 240:263 */     if (!ec.contains(".")) {
/* 241:263 */       return false;
/* 242:    */     }
/* 243:265 */     String ecPart = "";
/* 244:266 */     String ecRest = ec;
/* 245:267 */     ecPart = ecRest.substring(0, ecRest.indexOf("."));
/* 246:268 */     ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
/* 247:269 */     if (!isNumber(ecPart)) {
/* 248:270 */       return false;
/* 249:    */     }
/* 250:273 */     if (!ecRest.contains(".")) {
/* 251:274 */       return false;
/* 252:    */     }
/* 253:277 */     ecPart = ecRest.substring(0, ecRest.indexOf("."));
/* 254:278 */     ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
/* 255:279 */     if (!isNumber(ecPart)) {
/* 256:280 */       return false;
/* 257:    */     }
/* 258:283 */     if (!ecRest.contains(".")) {
/* 259:284 */       return false;
/* 260:    */     }
/* 261:286 */     ecPart = ecRest.substring(0, ecRest.indexOf("."));
/* 262:287 */     ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
/* 263:288 */     if (!isNumber(ecPart)) {
/* 264:289 */       return false;
/* 265:    */     }
/* 266:291 */     if (ecRest.contains("°")) {
/* 267:292 */       ecRest = ecRest.substring(0, ecRest.indexOf("°"));
/* 268:    */     }
/* 269:294 */     if (isNumber(ecRest)) {
/* 270:294 */       return true;
/* 271:    */     }
/* 272:296 */     return false;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public String nameSuppl()
/* 276:    */   {
/* 277:300 */     if (this.unmapped) {
/* 278:301 */       return " #";
/* 279:    */     }
/* 280:303 */     if (this.unique_) {
/* 281:304 */       return " *";
/* 282:    */     }
/* 283:306 */     if (this.incomplete) {
/* 284:307 */       return " ^";
/* 285:    */     }
/* 286:309 */     return "";
/* 287:    */   }
/* 288:    */   
/* 289:    */   public String getFullName()
/* 290:    */   {
/* 291:313 */     return this.name_ + nameSuppl();
/* 292:    */   }
/* 293:    */   
/* 294:    */   public boolean couldBeEc()
/* 295:    */   {
/* 296:317 */     if (this.name_ == null) {
/* 297:317 */       return false;
/* 298:    */     }
/* 299:318 */     if (this.name_.isEmpty()) {
/* 300:318 */       return false;
/* 301:    */     }
/* 302:320 */     for (int charCtn = 0; charCtn < this.name_.length(); charCtn++)
/* 303:    */     {
/* 304:321 */       char ch = this.name_.charAt(charCtn);
/* 305:322 */       if ((!isNumber(ch)) && 
/* 306:323 */         (ch != '_') && (ch != '-') && (ch != '.')) {
/* 307:324 */         return false;
/* 308:    */       }
/* 309:    */     }
/* 310:326 */     return true;
/* 311:    */   }
/* 312:    */   
/* 313:    */   public boolean isUnique_()
/* 314:    */   {
/* 315:329 */     return this.unique_;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public boolean isIncomplete()
/* 319:    */   {
/* 320:332 */     return this.incomplete;
/* 321:    */   }
/* 322:    */   
/* 323:    */   public boolean isUnmapped()
/* 324:    */   {
/* 325:335 */     return this.unmapped;
/* 326:    */   }
/* 327:    */   
/* 328:    */   public void printEC()
/* 329:    */   {
/* 330:338 */     System.out.println("-------------PrintEc-----------------");
/* 331:339 */     System.out.println("Name: " + this.name_ + " Id: " + this.bioName_);
/* 332:340 */     System.out.println("Amount: " + this.amount_);
/* 333:341 */     for (int i = 0; i < this.repseqs_.size(); i++) {
/* 334:342 */       System.out.println("repseq " + i + " " + ((Repseqs)this.repseqs_.get(i)).repseqDesc_);
/* 335:    */     }
/* 336:344 */     System.out.println("///////-------------PrintEc----------------/////////");
/* 337:    */   }
/* 338:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.EcNr

 * JD-Core Version:    0.7.0.1

 */