/*  1:   */ package Prog;
/*  2:   */ 
/*  3:   */ import java.io.BufferedWriter;
/*  4:   */ import java.io.FileWriter;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.PrintStream;
/*  7:   */ import java.util.ArrayList;
/*  8:   */ 
/*  9:   */ public class Debug
/* 10:   */ {
/* 11:11 */   public static ArrayList<String> noEnzymeLines = new ArrayList();
/* 12:12 */   public static ArrayList<String> noRepseqLine = new ArrayList();
/* 13:13 */   public static ArrayList<String> EcList = new ArrayList();
/* 14:14 */   public static ArrayList<String> PfList = new ArrayList();
/* 15:   */   
/* 16:   */   public static void addnoEnzymeLine(String line)
/* 17:   */   {
/* 18:17 */     noEnzymeLines.add(line);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public static void addnoRepseqLine(String line)
/* 22:   */   {
/* 23:21 */     noRepseqLine.add(line);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static void addEc(String line)
/* 27:   */   {
/* 28:24 */     EcList.add(line);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static void addPf(String line)
/* 32:   */   {
/* 33:27 */     PfList.add(line);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public static void printAll()
/* 37:   */   {
/* 38:30 */     printNoenz();
/* 39:31 */     printNoRep();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public static void writeOutAll(String path)
/* 43:   */   {
/* 44:   */     try
/* 45:   */     {
/* 46:36 */       BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 47:37 */       out.write("noEnzList");
/* 48:38 */       out.newLine();
/* 49:39 */       for (int i = 0; i < noEnzymeLines.size(); i++)
/* 50:   */       {
/* 51:40 */         out.write((String)noEnzymeLines.get(i));
/* 52:41 */         out.newLine();
/* 53:   */       }
/* 54:43 */       out.newLine();
/* 55:44 */       out.write("noRepseqList");
/* 56:45 */       out.newLine();
/* 57:46 */       for (int i = 0; i < noRepseqLine.size(); i++)
/* 58:   */       {
/* 59:47 */         out.write((String)noRepseqLine.get(i));
/* 60:48 */         out.newLine();
/* 61:   */       }
/* 62:50 */       out.newLine();
/* 63:51 */       out.write("ecList");
/* 64:52 */       out.newLine();
/* 65:53 */       for (int i = 0; i < EcList.size(); i++)
/* 66:   */       {
/* 67:54 */         out.write((String)EcList.get(i));
/* 68:55 */         out.newLine();
/* 69:   */       }
/* 70:57 */       out.newLine();
/* 71:58 */       out.write("PFList");
/* 72:59 */       out.newLine();
/* 73:60 */       for (int i = 0; i < PfList.size(); i++)
/* 74:   */       {
/* 75:61 */         out.write((String)PfList.get(i));
/* 76:62 */         out.newLine();
/* 77:   */       }
/* 78:64 */       out.close();
/* 79:   */     }
/* 80:   */     catch (IOException e)
/* 81:   */     {
/* 82:67 */       e.printStackTrace();
/* 83:   */     }
/* 84:   */   }
/* 85:   */   
/* 86:   */   public static void printNoenz()
/* 87:   */   {
/* 88:72 */     System.out.println("-----------------NoEnzymeList:------------------------");
/* 89:73 */     for (int i = 0; i < noEnzymeLines.size(); i++) {
/* 90:74 */       System.out.println((String)noEnzymeLines.get(i));
/* 91:   */     }
/* 92:76 */     System.out.println("-----------------NoEnzymeList End:------------------------");
/* 93:   */   }
/* 94:   */   
/* 95:   */   public static void printNoRep()
/* 96:   */   {
/* 97:79 */     System.out.println("-----------------noRepseqList:------------------------");
/* 98:80 */     for (int i = 0; i < noRepseqLine.size(); i++) {
/* 99:81 */       System.out.println((String)noRepseqLine.get(i));
/* :0:   */     }
/* :1:83 */     System.out.println("-----------------NoRepymeList End:------------------------");
/* :2:   */   }
/* :3:   */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Prog.Debug
 * JD-Core Version:    0.7.0.1
 */