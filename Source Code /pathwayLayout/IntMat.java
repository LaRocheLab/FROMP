/*  1:   */ package pathwayLayout;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ 
/*  6:   */ public class IntMat
/*  7:   */ {
/*  8:   */   public int numOfRows;
/*  9:   */   public Integer[][] mat;
/* 10:   */   
/* 11:   */   public IntMat(ArrayList<Node> nodes)
/* 12:   */   {
/* 13:14 */     this.mat = new Integer[nodes.size()][nodes.size()];
/* 14:15 */     this.numOfRows = nodes.size();
/* 15:16 */     for (int i = 0; i < nodes.size(); i++) {
/* 16:17 */       for (int j = 0; j < nodes.size(); j++) {
/* 17:18 */         this.mat[i][j] = Integer.valueOf(0);
/* 18:   */       }
/* 19:   */     }
/* 20:21 */     Node node = null;
/* 21:22 */     Node node2 = null;
/* 22:23 */     for (int i = 0; i < nodes.size(); i++)
/* 23:   */     {
/* 24:24 */       node = (Node)nodes.get(i);
/* 25:25 */       for (int j = 0; j < node.conections.size(); j++)
/* 26:   */       {
/* 27:26 */         node2 = (Node)node.conections.get(j);
/* 28:   */         
/* 29:28 */         this.mat[node.ownIndex][node2.ownIndex] = Integer.valueOf(1);
/* 30:   */       }
/* 31:   */     }
/* 32:   */   }
/* 33:   */   
/* 34:   */   public IntMat(int size, boolean toIdentity)
/* 35:   */   {
/* 36:33 */     this.mat = new Integer[size][size];
/* 37:34 */     this.numOfRows = size;
/* 38:35 */     for (int i = 0; i < size; i++) {
/* 39:36 */       for (int j = 0; j < size; j++) {
/* 40:37 */         if ((toIdentity) && (i == j)) {
/* 41:38 */           this.mat[i][j] = Integer.valueOf(1);
/* 42:   */         } else {
/* 43:41 */           this.mat[i][j] = Integer.valueOf(0);
/* 44:   */         }
/* 45:   */       }
/* 46:   */     }
/* 47:   */   }
/* 48:   */   
/* 49:   */   public IntMat add(IntMat addMat)
/* 50:   */   {
/* 51:47 */     IntMat C = new IntMat(this.numOfRows, false);
/* 52:48 */     if (this.numOfRows != addMat.numOfRows) {
/* 53:49 */       throw new RuntimeException("Illegal matrix dimensions.");
/* 54:   */     }
/* 55:51 */     for (int i = 0; i < this.numOfRows; i++) {
/* 56:52 */       for (int j = 0; j < this.numOfRows; j++) {
/* 57:53 */         C.mat[i][j] = Integer.valueOf(this.mat[i][j].intValue() + addMat.mat[i][j].intValue());
/* 58:   */       }
/* 59:   */     }
/* 60:56 */     return C;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public IntMat times(IntMat B)
/* 64:   */   {
/* 65:59 */     IntMat A = this;
/* 66:60 */     if (A.numOfRows != B.numOfRows) {
/* 67:60 */       throw new RuntimeException("Illegal matrix dimensions.");
/* 68:   */     }
/* 69:61 */     IntMat C = new IntMat(A.numOfRows, false);
/* 70:62 */     for (int i = 0; i < C.numOfRows; i++) {
/* 71:63 */       for (int j = 0; j < C.numOfRows; j++) {
/* 72:64 */         for (int k = 0; k < A.numOfRows; k++)
/* 73:   */         {
/* 74:65 */           int tmp63_61 = j; Integer[] tmp63_60 = C.mat[i];tmp63_60[tmp63_61] = Integer.valueOf(tmp63_60[tmp63_61].intValue() + A.mat[i][k].intValue() * B.mat[k][j].intValue());
/* 75:   */         }
/* 76:   */       }
/* 77:   */     }
/* 78:67 */     return C;
/* 79:   */   }
/* 80:   */   
/* 81:   */   public IntMat clone()
/* 82:   */   {
/* 83:70 */     IntMat C = new IntMat(this.numOfRows, false);
/* 84:71 */     for (int i = 0; i < this.numOfRows; i++) {
/* 85:72 */       for (int j = 0; j < this.numOfRows; j++) {
/* 86:73 */         C.mat[i][j] = this.mat[i][j];
/* 87:   */       }
/* 88:   */     }
/* 89:76 */     return C;
/* 90:   */   }
/* 91:   */   
/* 92:   */   public void printMat()
/* 93:   */   {
/* 94:79 */     for (int i = 0; i < this.numOfRows; i++)
/* 95:   */     {
/* 96:80 */       for (int j = 0; j < this.numOfRows; j++) {
/* 97:81 */         System.out.print(" " + this.mat[i][j] + " ");
/* 98:   */       }
/* 99:83 */       System.out.println("");
/* :0:   */     }
/* :1:   */   }
/* :2:   */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     pathwayLayout.IntMat
 * JD-Core Version:    0.7.0.1
 */