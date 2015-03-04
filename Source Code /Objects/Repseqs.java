/*  1:   */ package Objects;
/*  2:   */ 
			//Holds the data for the sequence IDs.

/*  3:   */ public class Repseqs
/*  4:   */ {
/*  5:   */   String repseqDesc_;
/*  6:   */   int amount_;
/*  7:   */   
/*  8:   */   public Repseqs(String repseq, int am)
/*  9:   */   {
/* 10: 9 */     this.repseqDesc_ = repseq;
/* 11:10 */     this.amount_ = am;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void addAmount(int amount)
/* 15:   */   {
/* 16:14 */     this.amount_ += amount;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean isSameReps(String desc)
/* 20:   */   {
/* 21:18 */     if (this.repseqDesc_.contentEquals(desc)) {
/* 22:19 */       return true;
/* 23:   */     }
/* 24:22 */     return false;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getRepseqDesc_()
/* 28:   */   {
/* 29:27 */     return this.repseqDesc_;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setRepseqDesc_(String repseqDesc_)
/* 33:   */   {
/* 34:31 */     this.repseqDesc_ = repseqDesc_;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public int getAmount_()
/* 38:   */   {
/* 39:35 */     return this.amount_;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void setAmount_(int amount_)
/* 43:   */   {
/* 44:39 */     this.amount_ = amount_;
/* 45:   */   }
/* 46:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.Repseqs

 * JD-Core Version:    0.7.0.1

 */