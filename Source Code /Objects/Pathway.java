/*  1:   */ package Objects;
/*  2:   */ 
			//Pathways that ECs can be mapped to. Holds its name, id and whether or not it was a pathway designed by the user or if it was premade. 

/*  3:   */ public class Pathway
/*  4:   */ {
/*  5:   */   public String id_;					// the id of the pathway
/*  6:   */   public String name_;					// pathway name 
/*  7: 6 */   private boolean selected = true;		// whether or not this project will use this pathway
/*  8: 8 */   public boolean userPathway = false;	// whether or not this was a user built pathway
/*  9:   */   public String pathwayInfoPAth;		// info about the pathway
/* 10:   */   
/* 11:   */   public boolean isSelected()
/* 12:   */   {
/* 13:15 */     return this.selected;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void setSelected(boolean selected)
/* 17:   */   {
/* 18:18 */     this.selected = selected;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Pathway(String id, String name)
/* 22:   */   {
/* 23:21 */     this.id_ = id;
/* 24:22 */     this.name_ = name;
/* 25:23 */     this.selected = true;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public Pathway(Pathway path)
/* 29:   */   {
/* 30:26 */     this.id_ = path.id_;
/* 31:27 */     this.name_ = path.name_;
/* 32:28 */     this.selected = path.selected;
/* 33:29 */     this.userPathway = path.userPathway;
/* 34:30 */     if (this.userPathway) {
/* 35:31 */       this.id_ = this.name_;
/* 36:   */     }
/* 37:34 */     this.pathwayInfoPAth = path.pathwayInfoPAth;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean idBiggerId2(Pathway path2)
/* 41:   */   {
/* 42:37 */     if (this.id_.contentEquals("-1")) {
/* 43:38 */       return true;
/* 44:   */     }
/* 45:40 */     if (path2.id_.contentEquals("-1")) {
/* 46:41 */       return false;
/* 47:   */     }
/* 48:43 */     for (int charCnt = 0; (charCnt < this.id_.length()) && (charCnt < path2.id_.length()); charCnt++)
/* 49:   */     {
/* 50:44 */       if (this.id_.charAt(charCnt) > path2.id_.charAt(charCnt)) {
/* 51:45 */         return true;
/* 52:   */       }
/* 53:47 */       if (this.id_.charAt(charCnt) < path2.id_.charAt(charCnt)) {
/* 54:48 */         return false;
/* 55:   */       }
/* 56:   */     }
/* 57:51 */     return false;
/* 58:   */   }
/* 59:   */   
/* 60:   */   public boolean idSmallerId2(Pathway path2)
/* 61:   */   {
/* 62:55 */     for (int charCnt = 0; (charCnt < this.id_.length()) && (charCnt < path2.id_.length()); charCnt++)
/* 63:   */     {
/* 64:56 */       if (this.id_.charAt(charCnt) < path2.id_.charAt(charCnt)) {
/* 65:57 */         return true;
/* 66:   */       }
/* 67:59 */       if (this.id_.charAt(charCnt) > path2.id_.charAt(charCnt)) {
/* 68:60 */         return true;
/* 69:   */       }
/* 70:   */     }
/* 71:63 */     return false;
/* 72:   */   }
/* 73:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Objects.Pathway

 * JD-Core Version:    0.7.0.1

 */