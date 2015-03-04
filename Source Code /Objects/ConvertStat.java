/*  1:   */ package Objects;
/*  2:   */ 

			//This class holds information about the data (ie. which ec's were converted from pfams). Not particularily interesting.

/*  3:   */ public class ConvertStat
/*  4:   */ {
/*  5:   */   String desc_;
/*  6:   */   String ecNr_;
/*  7:   */   int ecAmount_;
/*  8:   */   int pfamToEcAmount_;
/*  9:   */   int pfamToRnAmount_;
/* 10:   */   
/* 11:   */   public ConvertStat(String desc, String ecName, int ecAm, int pfamToEcAm, int pfamToRnAm)
/* 12:   */   {
/* 13:13 */     this.desc_ = desc;
/* 14:14 */     this.ecAmount_ = ecAm;
/* 15:15 */     this.pfamToEcAmount_ = pfamToEcAm;
/* 16:16 */     this.pfamToRnAmount_ = pfamToRnAm;
/* 17:17 */     this.ecNr_ = ecName;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void addStatsCnt(ConvertStat stat)
/* 21:   */   {
/* 22:21 */     this.ecAmount_ += stat.ecAmount_;
/* 23:22 */     this.pfamToEcAmount_ += stat.pfamToEcAmount_;
/* 24:23 */     this.pfamToRnAmount_ += stat.pfamToRnAmount_;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public int getEcAmount_()
/* 28:   */   {
/* 29:26 */     return this.ecAmount_;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setEcAmount_(int ecAmount_)
/* 33:   */   {
/* 34:29 */     this.ecAmount_ = ecAmount_;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getDesc_()
/* 38:   */   {
/* 39:32 */     return this.desc_;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public int getPfamToEcAmount_()
/* 43:   */   {
/* 44:35 */     return this.pfamToEcAmount_;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public String getEcNr_()
/* 48:   */   {
/* 49:38 */     return this.ecNr_;
/* 50:   */   }
/* 51:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.ConvertStat

 * JD-Core Version:    0.7.0.1

 */