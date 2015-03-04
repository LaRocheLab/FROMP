/*  1:   */ package Objects;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ 
/*  5:   */ public class EcSampleStats
/*  6:   */ {
/*  7:   */   public int amount_;
/*  8:   */   public int sampleNr_;
/*  9:   */   public Color col_;
/* 10:   */   
/* 11:   */   public EcSampleStats(EcNr ecNr)
/* 12:   */   {
/* 13:13 */     this.amount_ = ecNr.amount_;
/* 14:14 */     this.sampleNr_ = ecNr.sampleNr_;
/* 15:15 */     this.col_ = ecNr.samColor_;
/* 16:   */   }
/* 17:   */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Objects.EcSampleStats
 * JD-Core Version:    0.7.0.1
 */