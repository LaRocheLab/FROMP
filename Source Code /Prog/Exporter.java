/*   1:    */ package Prog;
/*   2:    */ 
/*   3:    */ import Objects.EcNr;
/*   4:    */ import Objects.EcWithPathway;
/*   5:    */ import Objects.Pathway;
/*   6:    */ import Objects.PathwayWithEc;
/*   7:    */ import Objects.Sample;
/*   8:    */ import java.io.BufferedWriter;
/*   9:    */ import java.io.File;
/*  10:    */ import java.io.FileWriter;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.PrintStream;
/*  13:    */ import java.util.ArrayList;
/*  14:    */ import javax.swing.JFileChooser;
/*  15:    */ import javax.swing.JPanel;
/*  16:    */ import javax.swing.filechooser.FileFilter;
/*  17:    */ 
/*  18:    */ public class Exporter
/*  19:    */ {
/*  20:    */   JFileChooser fChoose_;
/*  21: 22 */   public String path_ = "";
/*  22:    */   JPanel parent_;
/*  23:    */   
/*  24:    */   public Exporter(JPanel par)
/*  25:    */   {
/*  26: 26 */     this.parent_ = par;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void exportDoubleMatrix(ArrayList<String> xDesc, ArrayList<String> yDesc, double[][] matr, boolean inCsf)
/*  30:    */   {
/*  31: 30 */     String seperator = "\t";
/*  32: 31 */     if (inCsf) {
/*  33: 32 */       seperator = ",";
/*  34:    */     }
/*  35:    */     try
/*  36:    */     {
/*  37: 35 */       BufferedWriter out = new BufferedWriter(new FileWriter(this.path_));
/*  38: 36 */       out.write(seperator);
/*  39: 37 */       for (int x = 0; x < xDesc.size(); x++) {
/*  40: 38 */         out.write((String)xDesc.get(x) + seperator);
/*  41:    */       }
/*  42: 40 */       out.newLine();
/*  43: 41 */       for (int y = 0; y < yDesc.size(); y++)
/*  44:    */       {
/*  45: 42 */         out.write((String)yDesc.get(y) + seperator);
/*  46: 43 */         for (int x = 0; x < xDesc.size(); x++) {
/*  47: 44 */           out.write(matr[x][y] + seperator);
/*  48:    */         }
/*  49: 46 */         out.newLine();
/*  50:    */       }
/*  51: 48 */       out.close();
/*  52:    */     }
/*  53:    */     catch (IOException e)
/*  54:    */     {
/*  55: 51 */       e.printStackTrace();
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void exportDoubleMatrix(ArrayList<String> xDesc, ArrayList<String> yDesc, float[][] matr, boolean inCsf)
/*  60:    */   {
/*  61: 56 */     String seperator = "\t";
/*  62: 57 */     if (inCsf) {
/*  63: 58 */       seperator = ",";
/*  64:    */     }
/*  65:    */     try
/*  66:    */     {
/*  67: 61 */       BufferedWriter out = new BufferedWriter(new FileWriter(this.path_));
/*  68: 62 */       out.write(seperator);
/*  69: 63 */       for (int x = 0; x < xDesc.size(); x++) {
/*  70: 64 */         out.write((String)xDesc.get(x) + seperator);
/*  71:    */       }
/*  72: 66 */       out.newLine();
/*  73: 67 */       for (int y = 0; y < yDesc.size(); y++)
/*  74:    */       {
/*  75: 68 */         out.write((String)yDesc.get(y) + seperator);
/*  76: 69 */         for (int x = 0; x < xDesc.size(); x++) {
/*  77: 70 */           out.write(matr[x][y] + seperator);
/*  78:    */         }
/*  79: 72 */         out.newLine();
/*  80:    */       }
/*  81: 74 */       out.close();
/*  82:    */     }
/*  83:    */     catch (IOException e)
/*  84:    */     {
/*  85: 77 */       e.printStackTrace();
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void exportIntMatrix(ArrayList<String> xDesc, ArrayList<String> yDesc, int[][] matr, boolean inCsf)
/*  90:    */   {
/*  91: 82 */     String seperator = "\t";
/*  92: 83 */     if (inCsf) {
/*  93: 84 */       seperator = ",";
/*  94:    */     }
/*  95:    */     try
/*  96:    */     {
/*  97: 87 */       BufferedWriter out = new BufferedWriter(new FileWriter(this.path_));
/*  98: 88 */       out.write(seperator);
/*  99: 89 */       for (int x = 0; x < xDesc.size(); x++)
/* 100:    */       {
/* 101: 90 */         out.write((String)xDesc.get(x) + seperator);
/* 102: 91 */         System.out.println((String)xDesc.get(x));
/* 103:    */       }
/* 104: 93 */       out.newLine();
/* 105: 94 */       for (int y = 0; y < yDesc.size(); y++)
/* 106:    */       {
/* 107: 95 */         out.write((String)yDesc.get(y) + seperator);
/* 108: 96 */         for (int x = 0; x < xDesc.size(); x++) {
/* 109: 97 */           if (matr[x][y] == 0) {
/* 110: 98 */             out.write("0" + seperator);
/* 111:    */           } else {
/* 112:101 */             out.write(matr[x][y] + seperator);
/* 113:    */           }
/* 114:    */         }
/* 115:104 */         out.newLine();
/* 116:    */       }
/* 117:106 */       out.close();
/* 118:    */     }
/* 119:    */     catch (IOException e)
/* 120:    */     {
/* 121:109 */       e.printStackTrace();
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   public boolean saveDialog()
/* 126:    */   {
/* 127:114 */     this.fChoose_ = new JFileChooser(this.path_);
/* 128:    */     
/* 129:116 */     this.fChoose_.setFileSelectionMode(0);
/* 130:117 */     this.fChoose_.setBounds(100, 100, 200, 20);
/* 131:118 */     this.fChoose_.setVisible(true);
/* 132:    */     
/* 133:    */ 
/* 134:121 */     this.fChoose_.setFileFilter(new FileFilter()
/* 135:    */     {
/* 136:    */       public boolean accept(File f)
/* 137:    */       {
/* 138:125 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".txt"))) {
/* 139:126 */           return true;
/* 140:    */         }
/* 141:128 */         return false;
/* 142:    */       }
/* 143:    */       
/* 144:    */       public String getDescription()
/* 145:    */       {
/* 146:134 */         return ".txt";
/* 147:    */       }
/* 148:    */     });
/* 149:138 */     if (this.fChoose_.showSaveDialog(this.fChoose_.getParent()) == 0) {
/* 150:    */       try
/* 151:    */       {
/* 152:140 */         this.path_ = this.fChoose_.getSelectedFile().getCanonicalPath();
/* 153:142 */         if (!this.path_.endsWith(".txt"))
/* 154:    */         {
/* 155:143 */           this.path_ += ".txt";
/* 156:144 */           System.out.println(".txt");
/* 157:    */         }
/* 158:146 */         return true;
/* 159:    */       }
/* 160:    */       catch (IOException e1)
/* 161:    */       {
/* 162:149 */         e1.printStackTrace();
/* 163:    */       }
/* 164:    */     } else {
/* 165:153 */       return false;
/* 166:    */     }
/* 167:155 */     return false;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public ArrayList<String> getNameListFromPathwaysWEc(ArrayList<PathwayWithEc> list)
/* 171:    */   {
/* 172:158 */     ArrayList<String> ret = new ArrayList();
/* 173:159 */     String name = "";
/* 174:160 */     for (int pwCnt = 0; pwCnt < list.size(); pwCnt++)
/* 175:    */     {
/* 176:161 */       name = ((PathwayWithEc)list.get(pwCnt)).id_ + "/" + ((PathwayWithEc)list.get(pwCnt)).name_;
/* 177:162 */       ret.add(name);
/* 178:    */     }
/* 179:164 */     return ret;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public ArrayList<String> getNameListFromEcwp(ArrayList<EcWithPathway> list)
/* 183:    */   {
/* 184:168 */     ArrayList<String> ret = new ArrayList();
/* 185:169 */     String name = "";
/* 186:170 */     for (int pwCnt = 0; pwCnt < list.size(); pwCnt++)
/* 187:    */     {
/* 188:171 */       name = ((EcWithPathway)list.get(pwCnt)).name_;
/* 189:172 */       ret.add(name);
/* 190:    */     }
/* 191:174 */     return ret;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public ArrayList<String> getNameListFromEcs(ArrayList<EcNr> list)
/* 195:    */   {
/* 196:177 */     ArrayList<String> ret = new ArrayList();
/* 197:178 */     String name = "";
/* 198:179 */     for (int pwCnt = 0; pwCnt < list.size(); pwCnt++)
/* 199:    */     {
/* 200:180 */       name = ((EcNr)list.get(pwCnt)).name_;
/* 201:181 */       ret.add(name);
/* 202:    */     }
/* 203:183 */     return ret;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public ArrayList<String> getNameListFromPw(ArrayList<Pathway> list)
/* 207:    */   {
/* 208:186 */     ArrayList<String> ret = new ArrayList();
/* 209:187 */     String name = "";
/* 210:188 */     for (int pwCnt = 0; pwCnt < list.size(); pwCnt++)
/* 211:    */     {
/* 212:189 */       name = ((Pathway)list.get(pwCnt)).id_ + "/" + ((Pathway)list.get(pwCnt)).name_;
/* 213:190 */       ret.add(name);
/* 214:    */     }
/* 215:192 */     return ret;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public ArrayList<String> getNameListFromSample(ArrayList<Sample> list)
/* 219:    */   {
/* 220:195 */     ArrayList<String> ret = new ArrayList();
/* 221:196 */     String name = "";
/* 222:197 */     for (int pwCnt = 0; pwCnt < list.size(); pwCnt++)
/* 223:    */     {
/* 224:198 */       name = ((Sample)list.get(pwCnt)).name_;
/* 225:199 */       ret.add(name);
/* 226:    */     }
/* 227:201 */     return ret;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public String getPath_()
/* 231:    */   {
/* 232:205 */     return this.path_;
/* 233:    */   }
/* 234:    */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Prog.Exporter
 * JD-Core Version:    0.7.0.1
 */