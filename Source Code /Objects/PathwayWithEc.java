/*   1:    */ package Objects;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ 
			//An extension of the pathway class. Adds all of the ECs which can be mapped to a particular path in a convieniant ArrayList.

/*   6:    */ public class PathwayWithEc
/*   7:    */   extends Pathway
/*   8:    */ {
/*   9:    */   public ArrayList<EcNr> ecNrs_;	// Arraylist of ecs which map to this pathway
/*  10:    */   public double weight_;			// the weight of this pathway
/*  11:    */   public double score_;			// the pathway score
/*  12:    */   public boolean weightSet;		// 
/*  13:    */   public int sumOfEC_;			// the sum of the number of ecs
/*  14:    */   
/*  15:    */   public PathwayWithEc(Pathway pathway){
/*  17: 15 */     super(pathway);
/*  18: 16 */     this.ecNrs_ = new ArrayList();
/*  19: 17 */     this.sumOfEC_ = 0;
/*  20: 18 */     this.pathwayInfoPAth = pathway.pathwayInfoPAth;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public PathwayWithEc(PathwayWithEc pathway, boolean newPw)
/*  24:    */   {
/*  25: 21 */     super(pathway);
/*  26: 22 */     this.ecNrs_ = new ArrayList();
/*  27: 23 */     this.sumOfEC_ = pathway.sumOfEC_;
/*  28: 24 */     if ((pathway.ecNrs_ != null) && (!newPw))
/*  29:    */     {
/*  30: 25 */       for (int i = 0; i < pathway.ecNrs_.size(); i++) {
/*  31: 26 */         addEc((EcNr)pathway.ecNrs_.get(i));
/*  32:    */       }
/*  33: 28 */       this.score_ = pathway.score_;
/*  34: 29 */       this.weight_ = pathway.weight_;
/*  35: 30 */       this.weightSet = pathway.weightSet;
/*  36: 31 */       this.sumOfEC_ = pathway.sumOfEC_;
/*  37: 32 */       this.pathwayInfoPAth = pathway.pathwayInfoPAth;
/*  38:    */     }
/*  39: 34 */     this.userPathway = pathway.userPathway;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void addEc(EcNr ecNr)
/*  43:    */   {
/*  44: 38 */     if (this.ecNrs_.size() > 0) {
/*  45: 39 */       for (int i = 0; i < this.ecNrs_.size(); i++) {
/*  46: 40 */         if (((EcNr)this.ecNrs_.get(i)).name_.contentEquals(ecNr.name_))
/*  47:    */         {
/*  48: 42 */           ((EcNr)this.ecNrs_.get(i)).increaseAmount(ecNr.amount_);
/*  49: 43 */           for (int repCnt = 0; repCnt < ecNr.repseqs_.size(); repCnt++) {
/*  50: 44 */             ((EcNr)this.ecNrs_.get(i)).repseqs_.add((Repseqs)ecNr.repseqs_.get(repCnt));
/*  51:    */           }
/*  52: 48 */           this.sumOfEC_ += 1;
/*  53: 49 */           return;
/*  54:    */         }
/*  55:    */       }
/*  56:    */     }
/*  57: 53 */     this.ecNrs_.add(new EcNr(ecNr));
/*  58: 54 */     this.sumOfEC_ += 1;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void addEcNewly(EcNr ecNr)
/*  62:    */   {
/*  63: 57 */     if (this.ecNrs_.size() > 0) {
/*  64: 58 */       for (int i = 0; i < this.ecNrs_.size(); i++) {
/*  65: 59 */         if (((EcNr)this.ecNrs_.get(i)).name_.contentEquals(ecNr.name_))
/*  66:    */         {
/*  67: 60 */           ((EcNr)this.ecNrs_.get(i)).amount_ = 0;
/*  68: 61 */           ((EcNr)this.ecNrs_.get(i)).stats_ = new ArrayList();
/*  69: 62 */           this.ecNrs_.remove(i);
/*  70: 63 */           this.sumOfEC_ -= 1;
/*  71: 64 */           break;
/*  72:    */         }
/*  73:    */       }
/*  74:    */     }
/*  75: 68 */     this.ecNrs_.add(new EcNr(ecNr));
/*  76: 69 */     this.sumOfEC_ += 1;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void addEc(EcNr ecNr, Sample samp)
/*  80:    */   {
/*  81: 73 */     if (this.ecNrs_.size() > 0) {
/*  82: 74 */       for (int i = 0; i < this.ecNrs_.size(); i++) {
/*  83: 75 */         if (((EcNr)this.ecNrs_.get(i)).name_.compareTo(ecNr.name_) == 0)
/*  84:    */         {
/*  85: 76 */           EcSampleStats tmpStats = new EcSampleStats(ecNr);
/*  86: 77 */           ((EcNr)this.ecNrs_.get(i)).stats_.add(tmpStats);
/*  87:    */           
/*  88: 79 */           ((EcNr)this.ecNrs_.get(i)).increaseAmount(ecNr.amount_);
/*  89: 80 */           for (int repCnt = 0; repCnt < ecNr.repseqs_.size(); repCnt++) {
/*  90: 81 */             ((EcNr)this.ecNrs_.get(i)).repseqs_.add((Repseqs)ecNr.repseqs_.get(repCnt));
/*  91:    */           }
/*  92: 83 */           return;
/*  93:    */         }
/*  94:    */       }
/*  95:    */     }
/*  96: 88 */     EcNr tmpec = new EcNr(ecNr);
/*  97: 89 */     if (tmpec.stats_.isEmpty()) {
/*  98: 90 */       tmpec.stats_.add(new EcSampleStats(ecNr));
/*  99:    */     }
/* 100: 93 */     this.ecNrs_.add(tmpec);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String getEc(int index)
/* 104:    */   {
/* 105: 97 */     return ((EcNr)this.ecNrs_.get(index)).name_;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void removeRandomEc()
/* 109:    */   {
/* 110:100 */     double rand = Math.random();
/* 111:101 */     System.out.println(rand);
/* 112:102 */     int pick = (int)(rand * this.ecNrs_.size() - 1.0D);
/* 113:103 */     if (((EcNr)this.ecNrs_.get(pick)).amount_ > 1) {
/* 114:104 */       ((EcNr)this.ecNrs_.get(pick)).amount_ -= 1;
/* 115:    */     } else {
/* 116:107 */       this.ecNrs_.remove(pick);
/* 117:    */     }
/* 118:109 */     this.sumOfEC_ -= 1;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public double getWeight(int mode)
/* 122:    */   {
/* 123:112 */     double ret = 0.0D;
/* 124:113 */     this.weight_ = 0.0D;
/* 125:115 */     switch (mode)
/* 126:    */     {
/* 127:    */     case 0: 
/* 128:117 */       for (int i = 0; i < this.ecNrs_.size(); i++) {
/* 129:118 */         ret += ((EcNr)this.ecNrs_.get(i)).weight_;
/* 130:    */       }
/* 131:120 */       break;
/* 132:    */     case 1: 
/* 133:122 */       for (int i = 0; i < this.ecNrs_.size(); i++) {
/* 134:123 */         ret += ((EcNr)this.ecNrs_.get(i)).weight_ * ((EcNr)this.ecNrs_.get(i)).chainMultiply1_;
/* 135:    */       }
/* 136:125 */       break;
/* 137:    */     case 2: 
/* 138:127 */       for (int i = 0; i < this.ecNrs_.size(); i++) {
/* 139:128 */         ret += ((EcNr)this.ecNrs_.get(i)).weight_ * ((EcNr)this.ecNrs_.get(i)).chainMultiply2_;
/* 140:    */       }
/* 141:    */     }
/* 142:132 */     this.weight_ = ret;
/* 143:    */     
/* 144:    */ 
/* 145:    */ 
/* 146:136 */     return ret;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void sortEcs()
/* 150:    */   {
/* 151:140 */     boolean changed = true;
/* 152:    */     int ecCnt1=0;
//* 153:142 */     for (; changed; ecCnt1 < this.ecNrs_.size())
                  while(changed && (ecCnt1<this.ecNrs_.size()))
/* 154:    */     {
/* 155:143 */       changed = false;
//* 156:144 */       ecCnt1 = 0; continue;
/* 157:145 */       for (int ecCnt2 = ecCnt1 + 1; ecCnt2 < this.ecNrs_.size(); ecCnt2++)
/* 158:    */       {
/* 159:146 */         if (!ec1BiggerEc2((EcNr)this.ecNrs_.get(ecCnt1), (EcNr)this.ecNrs_.get(ecCnt2))) {
/* 160:    */           break;
/* 161:    */         }
/* 162:147 */         switchEcs(ecCnt1, ecCnt2);
/* 163:148 */         ecCnt1++;
/* 164:149 */         ecCnt2++;
/* 165:150 */         changed = true;
/* 166:    */       }
/* 167:144 */       ecCnt1++;
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void switchEcs(int index1, int index2)
/* 172:    */   {
/* 173:161 */     this.ecNrs_.add(index1, (EcNr)this.ecNrs_.get(index2));
/* 174:162 */     this.ecNrs_.remove(index2 + 1);
/* 175:    */   }
/* 176:    */   
/* 177:    */   public boolean ec1SmallerEc2(String ec1, String ec2)
/* 178:    */   {
/* 179:166 */     for (int i = 0; (i < ec1.length()) && (i < ec2.length()); i++)
/* 180:    */     {
/* 181:167 */       if (ec1.charAt(i) < ec2.charAt(i)) {
/* 182:169 */         return true;
/* 183:    */       }
/* 184:171 */       if (ec1.charAt(i) > ec2.charAt(i)) {
/* 185:172 */         return false;
/* 186:    */       }
/* 187:    */     }
/* 188:175 */     return false;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public boolean ec1BiggerEc2(EcNr ec1, EcNr ec2)
/* 192:    */   {
/* 193:181 */     for (int i = 0; (i < ec1.nr_.length) && (i < ec2.nr_.length); i++)
/* 194:    */     {
/* 195:182 */       if (ec1.nr_[i] > ec2.nr_[i]) {
/* 196:183 */         return true;
/* 197:    */       }
/* 198:185 */       if (ec1.nr_[i] < ec2.nr_[i]) {
/* 199:186 */         return false;
/* 200:    */       }
/* 201:    */     }
/* 202:189 */     return false;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void clearList()
/* 206:    */   {
/* 207:192 */     while (this.ecNrs_.size() > 0) {
/* 208:193 */       this.ecNrs_.remove(0);
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   public EcNr geteEcNr(String name)
/* 213:    */   {
/* 214:197 */     for (int ecCnt = 0; ecCnt < this.ecNrs_.size(); ecCnt++) {
/* 215:198 */       if (((EcNr)this.ecNrs_.get(ecCnt)).name_.contentEquals(name)) {
/* 216:199 */         return (EcNr)this.ecNrs_.get(ecCnt);
/* 217:    */       }
/* 218:    */     }
/* 219:202 */     return null;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public void printPath()
/* 223:    */   {
/* 224:205 */     System.out.println("-------------PrintPAth-----------------");
/* 225:206 */     System.out.println("Name: " + this.name_ + " Id: " + this.id_);
/* 226:207 */     System.out.println("EcAmount " + this.ecNrs_.size());
/* 227:208 */     for (int i = 0; i < this.ecNrs_.size(); i++) {
/* 228:209 */       System.out.println("Ec Nr: " + i + " Id:" + ((EcNr)this.ecNrs_.get(i)).name_);
/* 229:    */     }
/* 230:211 */     System.out.println("///////-------------PrintPAth----------------/////////");
/* 231:    */   }
/* 232:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.PathwayWithEc

 * JD-Core Version:    0.7.0.1

 */