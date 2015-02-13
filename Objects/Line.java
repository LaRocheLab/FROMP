/*  1:   */ package Objects;
/*  2:   */ 

			//This class is used in the EC matrix pane and it contains the values of all of the matrix elements in the line (arrayline_[]) 
			//as well as the total sum of the line (sumline_).	

/*  3:   */ public class Line
/*  4:   */ {
/*  5:   */   PathwayWithEc path_;
/*  6:   */   EcWithPathway ec_;
/*  7:   */   EcNr ecNr_;
/*  8:   */   private final boolean sumline_;
/*  9:   */   private final boolean mappedSums_;
/* 10:   */   private final boolean unmappedSums_;
/* 11:   */   private final boolean incompleteSums_;
/* 12:   */   public double[] arrayLine_;
/* 13:   */   public int sum_;
/* 14:   */   
/* 15:   */   public Line(PathwayWithEc pw, double[] line)
/* 16:   */   {
/* 17:16 */     this.path_ = pw;
/* 18:17 */     this.arrayLine_ = line;
/* 19:18 */     this.sumline_ = false;
/* 20:19 */     this.mappedSums_ = false;
/* 21:20 */     this.unmappedSums_ = false;
/* 22:21 */     this.incompleteSums_ = false;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Line(EcWithPathway ecwp, double[] line)
/* 26:   */   {
/* 27:25 */     this.ec_ = new EcWithPathway(ecwp);
/* 28:26 */     this.arrayLine_ = line;
/* 29:27 */     this.sumline_ = false;
/* 30:28 */     this.mappedSums_ = false;
/* 31:29 */     this.unmappedSums_ = false;
/* 32:30 */     this.incompleteSums_ = false;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Line(EcNr ecNr, double[] line)
/* 36:   */   {
/* 37:33 */     this.ecNr_ = ecNr;
/* 38:34 */     this.arrayLine_ = line;
/* 39:35 */     this.sumline_ = false;
/* 40:36 */     this.mappedSums_ = false;
/* 41:37 */     this.unmappedSums_ = false;
/* 42:38 */     this.incompleteSums_ = false;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public Line(double[] line, boolean mappedSums, boolean unmappedSums, boolean incompletSums)
/* 46:   */   {
/* 47:41 */     this.arrayLine_ = line;
/* 48:42 */     this.sumline_ = true;
/* 49:43 */     this.mappedSums_ = mappedSums;
/* 50:44 */     this.unmappedSums_ = unmappedSums;
/* 51:45 */     this.incompleteSums_ = incompletSums;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public void setSum()
/* 55:   */   {
/* 56:48 */     this.sum_ = 0;
/* 57:49 */     for (int i = 0; i < this.arrayLine_.length; i++) {
/* 58:50 */       this.sum_ = ((int)(this.sum_ + this.arrayLine_[i]));
/* 59:   */     }
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void fillWithZeros()
/* 63:   */   {
/* 64:54 */     for (int i = 0; i < this.arrayLine_.length; i++) {
/* 65:55 */       this.arrayLine_[i] = 0.0D;
/* 66:   */     }
/* 67:   */   }
/* 68:   */   
/* 69:   */   public EcNr getEcNr_()
/* 70:   */   {
/* 71:60 */     return this.ecNr_;
/* 72:   */   }
/* 73:   */   
/* 74:   */   public double getSum_()
/* 75:   */   {
/* 76:64 */     return this.sum_;
/* 77:   */   }
/* 78:   */   
/* 79:   */   public double getEntry(int index)
/* 80:   */   {
/* 81:68 */     if (this.arrayLine_.length < index) {
/* 82:68 */       return -1.0D;
/* 83:   */     }
/* 84:70 */     return this.arrayLine_[index];
/* 85:   */   }
/* 86:   */   
/* 87:   */   public void setEntry(int index, double value)
/* 88:   */   {
/* 89:74 */     this.arrayLine_[index] = value;
/* 90:   */   }
/* 91:   */   
/* 92:   */   public PathwayWithEc getPath_()
/* 93:   */   {
/* 94:77 */     return this.path_;
/* 95:   */   }
/* 96:   */   
/* 97:   */   public EcWithPathway getEc_()
/* 98:   */   {
/* 99:80 */     return this.ec_;
/* :0:   */   }
/* :1:   */   
/* :2:   */   public double[] getArrayLine_()
/* :3:   */   {
/* :4:83 */     return this.arrayLine_;
/* :5:   */   }
/* :6:   */   
/* :7:   */   public boolean isSumline_()
/* :8:   */   {
/* :9:86 */     return this.sumline_;
/* ;0:   */   }
/* ;1:   */   
/* ;2:   */   public boolean isMappedSums_()
/* ;3:   */   {
/* ;4:89 */     return this.mappedSums_;
/* ;5:   */   }
/* ;6:   */   
/* ;7:   */   public boolean isUnMappedSums_()
/* ;8:   */   {
/* ;9:92 */     return this.unmappedSums_;
/* <0:   */   }
/* <1:   */   
/* <2:   */   public boolean isincompleteSums_()
/* <3:   */   {
/* <4:95 */     return this.incompleteSums_;
/* <5:   */   }
/* <6:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.Line

 * JD-Core Version:    0.7.0.1

 */