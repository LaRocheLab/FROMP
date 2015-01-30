/*  1:   */ package Objects;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ 
/*  6:   */ public class EcWithPathway
/*  7:   */   extends EcNr
/*  8:   */ {
/*  9:   */   public ArrayList<Pathway> pathways_;
/* 10:   */   public boolean weightsSet;
/* 11:10 */   public boolean isSelected_ = true;
/* 12:   */   
/* 13:   */   public EcWithPathway(EcNr ecNr)
/* 14:   */   {
/* 15:15 */     super(ecNr);
/* 16:16 */     this.maxChainLength_ = ecNr.maxChainLength_;
/* 17:   */     
/* 18:18 */     this.pathways_ = new ArrayList();
/* 19:19 */     this.userEC = ecNr.userEC;
/* 20:20 */     this.weightsSet = false;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public EcWithPathway(EcWithPathway ecWp)
/* 24:   */   {
/* 25:23 */     super(ecWp);
/* 26:24 */     this.maxChainLength_ = ecWp.maxChainLength_;
/* 27:25 */     this.pathways_ = new ArrayList();
/* 28:26 */     this.userEC = ecWp.userEC;
/* 29:27 */     if (ecWp.pathways_ != null) {
/* 30:28 */       for (int i = 0; i < ecWp.pathways_.size(); i++) {
/* 31:29 */         this.pathways_.add((Pathway)ecWp.pathways_.get(i));
/* 32:   */       }
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public EcWithPathway(EcWithPathway ecWp, EcNr ecnr)
/* 37:   */   {
/* 38:33 */     super(ecWp);
/* 39:34 */     this.maxChainLength_ = ecnr.maxChainLength_;
/* 40:35 */     this.pathways_ = new ArrayList();
/* 41:36 */     this.amount_ = ecnr.amount_;
/* 42:37 */     this.userEC = ecWp.userEC;
/* 43:38 */     if (ecWp.pathways_ != null) {
/* 44:39 */       for (int i = 0; i < ecWp.pathways_.size(); i++) {
/* 45:40 */         this.pathways_.add((Pathway)ecWp.pathways_.get(i));
/* 46:   */       }
/* 47:   */     }
/* 48:43 */     if (this.repseqs_.size() == 0)
/* 49:   */     {
/* 50:44 */       this.repseqs_ = new ArrayList();
/* 51:45 */       for (int repCnt = 0; repCnt < ecnr.repseqs_.size(); repCnt++) {
/* 52:47 */         this.repseqs_.add((Repseqs)ecnr.repseqs_.get(repCnt));
/* 53:   */       }
/* 54:   */     }
/* 55:   */   }
/* 56:   */   
/* 57:   */   public void addPathway(Pathway pathway)
/* 58:   */   {
/* 59:52 */     this.pathways_.add(new Pathway(pathway));
/* 60:53 */     if (this.pathways_.size() > 1) {
/* 61:54 */       this.unique_ = false;
/* 62:   */     }
/* 63:   */   }
/* 64:   */   
/* 65:   */   public void addStats()
/* 66:   */   {
/* 67:59 */     if (this.pathways_.size() == 1) {
/* 68:60 */       if (((Pathway)this.pathways_.get(0)).id_ != "-1")
/* 69:   */       {
/* 70:61 */         this.unique_ = true;
/* 71:62 */         this.unmapped = false;
/* 72:63 */         this.incomplete = false;
/* 73:   */       }
/* 74:   */       else
/* 75:   */       {
/* 76:66 */         this.unique_ = false;
/* 77:67 */         this.unmapped = true;
/* 78:   */       }
/* 79:   */     }
/* 80:70 */     if (this.pathways_.size() > 1)
/* 81:   */     {
/* 82:71 */       this.unique_ = false;
/* 83:72 */       this.unmapped = false;
/* 84:73 */       this.incomplete = false;
/* 85:   */     }
/* 86:75 */     if (this.userEC)
/* 87:   */     {
/* 88:76 */       System.out.println("userEC");
/* 89:77 */       return;
/* 90:   */     }
/* 91:79 */     if (this.pathways_.size() == 0)
/* 92:   */     {
/* 93:80 */       this.unique_ = false;
/* 94:81 */       this.unmapped = true;
/* 95:   */     }
/* 96:83 */     if (!isCompleteEc())
/* 97:   */     {
/* 98:84 */       this.unique_ = false;
/* 99:85 */       this.unmapped = false;
/* :0:86 */       this.incomplete = true;
/* :1:   */     }
/* :2:   */   }
/* :3:   */   
/* :4:   */   public void printEC()
/* :5:   */   {
/* :6:90 */     System.out.println("-------------PrintEc-----------------");
/* :7:91 */     System.out.println("Name: " + this.name_ + " Id: " + this.bioName_);
/* :8:92 */     System.out.println("Amount: " + this.amount_);
/* :9:93 */     System.out.println("PwAmount " + this.pathways_.size());
/* ;0:94 */     for (int i = 0; i < this.pathways_.size(); i++) {
/* ;1:95 */       System.out.println("pw Nr: " + i + " Id:" + ((Pathway)this.pathways_.get(i)).name_);
/* ;2:   */     }
/* ;3:97 */     System.out.println("///////-------------PrintEc----------------/////////");
/* ;4:   */   }
/* ;5:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.EcWithPathway

 * JD-Core Version:    0.7.0.1

 */