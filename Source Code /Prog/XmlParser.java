/*   1:    */ package Prog;
/*   2:    */ 
/*   3:    */ import Objects.EcPosAndSize;
/*   4:    */ import java.io.BufferedReader;
/*   5:    */ import java.io.BufferedWriter;
/*   6:    */ import java.io.File;
/*   7:    */ import java.io.FileWriter;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Date;
/*  12:    */ 
			// A helper class designed to allow the user to parse through .xml files.

/*  13:    */ public class XmlParser
/*  14:    */ {
/*  15: 12 */   String date = "";
/*  16:    */   
/*  17:    */   public ArrayList<EcPosAndSize> findEcPosAndSize(String ecNr, BufferedReader in)
/*  18:    */   { // finds the positions and sizes of an ec in the .xml, returns an arraylist of ec positions and sizes
/*  19: 16 */     String tmpNr = "";
/*  20: 17 */     String tmp = "";
/*  21: 18 */     int begin = 0;
/*  22: 19 */     int end = 0;
/*  23: 20 */     int tmpX = 0;
/*  24: 21 */     int tmpY = 0;
/*  25: 22 */     int tmpW = 0;
/*  26: 23 */     int tmpH = 0;
/*  27:    */     
/*  28: 25 */     ArrayList<EcPosAndSize> ret = new ArrayList();
/*  29: 26 */     String zeile = "";
/*  30:    */     try
/*  31:    */     {
/*  32: 28 */       while ((zeile = in.readLine()) != null) {
/*  33: 32 */         if (zeile.contains("graphics name=\"" + ecNr + "\"")) {
/*  34: 34 */           if (((zeile = in.readLine()) != null) && 
/*  35: 35 */             (zeile.contains("type=\"rectangle\"")))
/*  36:    */           {
/*  37: 36 */             tmp = "x=\"";
/*  38: 37 */             begin = zeile.indexOf(tmp) + tmp.length();
/*  39: 38 */             end = zeile.indexOf("\"", begin);
/*  40: 39 */             tmpNr = zeile.substring(begin, end);
/*  41:    */             
/*  42: 41 */             tmpX = convertStringtoInt(tmpNr);
/*  43:    */             
/*  44: 43 */             tmp = "y=\"";
/*  45: 44 */             begin = zeile.indexOf(tmp) + tmp.length();
/*  46: 45 */             end = zeile.indexOf("\"", begin);
/*  47: 46 */             tmpNr = zeile.substring(begin, end);
/*  48:    */             
/*  49: 48 */             tmpY = convertStringtoInt(tmpNr);
/*  50:    */             
/*  51: 50 */             tmp = "width=\"";
/*  52: 51 */             begin = zeile.indexOf(tmp) + tmp.length();
/*  53: 52 */             end = zeile.indexOf("\"", begin);
/*  54: 53 */             tmpNr = zeile.substring(begin, end);
/*  55:    */             
/*  56: 55 */             tmpH = convertStringtoInt(tmpNr);
/*  57:    */             
/*  58: 57 */             tmp = "height=\"";
/*  59: 58 */             begin = zeile.indexOf(tmp) + tmp.length();
/*  60: 59 */             end = zeile.indexOf("\"", begin);
/*  61: 60 */             tmpNr = zeile.substring(begin, end);
/*  62:    */             
/*  63: 62 */             tmpW = convertStringtoInt(tmpNr);
/*  64:    */             
/*  65: 64 */             EcPosAndSize tmpPos = new EcPosAndSize(tmpX, tmpY, tmpH, tmpW);
/*  66: 65 */             ret.add(tmpPos);
/*  67:    */           }
/*  68:    */         }
/*  69:    */       }
/*  70:    */     }
/*  71:    */     catch (IOException e1)
/*  72:    */     {
/*  73: 74 */       e1.printStackTrace();
/*  74:    */     }
/*  75: 77 */     return ret;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void findEcinPathway(String workpath, String[] ecNr, BufferedReader in, String name)
/*  79:    */   { 
/*  80: 81 */     String zeile = null;
/*  81:    */     
/*  82: 83 */     int offset = 0;
/*  83: 84 */     boolean ecFound = false;
/*  84: 85 */     BufferedWriter out = null;
/*  85:    */     try
/*  86:    */     {
/*  87: 87 */       if (this.date == "")
/*  88:    */       {
/*  89: 88 */         this.date = String.valueOf(new Date().getTime());
/*  90: 89 */         createDir(workpath + "/output/" + "/" + this.date);
/*  91:    */       }
/*  92: 91 */       out = new BufferedWriter(new FileWriter(workpath + "/output/" + this.date + "/" + name));
/*  93:    */     }
/*  94:    */     catch (IOException e1)
/*  95:    */     {
/*  96: 94 */       e1.printStackTrace();
/*  97:    */     }
/*  98:    */     try
/*  99:    */     {
/* 100: 99 */       while ((zeile = in.readLine()) != null)
/* 101:    */       {
/* 102:100 */         ecFound = false;
/* 103:101 */         for (int i = 0; (i < ecNr.length) && (ecNr[i].length() > 0); i++) {
/* 104:104 */           if (!ecNr[i].isEmpty()) {
/* 105:106 */             if (zeile.contains("graphics name=\"" + ecNr[i].subSequence(3, ecNr[i].length()) + "\""))
/* 106:    */             {
/* 107:107 */               String newLine = null;
/* 108:    */               
/* 109:109 */               offset = zeile.indexOf("fgcolor=") + 9;
/* 110:110 */               newLine = zeile.substring(0, offset) + "#FF0000" + zeile.substring(offset + 7);
/* 111:    */               
/* 112:112 */               out.write(newLine + "\n");
/* 113:113 */               ecFound = true;
/* 114:114 */               break;
/* 115:    */             }
/* 116:    */           }
/* 117:    */         }
/* 118:118 */         if (!ecFound) {
/* 119:120 */           out.write(zeile + "\n");
/* 120:    */         }
/* 121:    */       }
/* 122:    */     }
/* 123:    */     catch (IOException e1)
/* 124:    */     {
/* 125:125 */       e1.printStackTrace();
/* 126:    */     }
/* 127:    */     try
/* 128:    */     {
/* 129:128 */       out.close();
/* 130:    */     }
/* 131:    */     catch (IOException e)
/* 132:    */     {
/* 133:132 */       e.printStackTrace();
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void createDir(String path)
/* 138:    */   { // makes a directory if the given path
/* 139:137 */     boolean status = new File(path).mkdirs();
/* 140:138 */     report(status, path);
/* 141:    */   }
/* 142:    */   
/* 143:    */   static void report(boolean b, String path)
/* 144:    */   { // reoprts whether the building of the directory was a success or a failure
/* 145:141 */     System.out.println(b ? "success" : "failure");
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int convertStringtoInt(String in)
/* 149:    */   {
/* 150:144 */     int ret = 0;
/* 151:146 */     for (int i = 0; i < in.length(); i++)
/* 152:    */     {
/* 153:147 */       ret *= 10;
/* 154:148 */       char c = in.charAt(i);
/* 155:149 */       ret += convertCharToInt(c);
/* 156:    */     }
/* 157:151 */     return ret;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public int convertCharToInt(char c)
/* 161:    */   {
/* 162:154 */     switch (c)
/* 163:    */     {
/* 164:    */     case '0': 
/* 165:156 */       return 0;
/* 166:    */     case '1': 
/* 167:159 */       return 1;
/* 168:    */     case '2': 
/* 169:162 */       return 2;
/* 170:    */     case '3': 
/* 171:165 */       return 3;
/* 172:    */     case '4': 
/* 173:168 */       return 4;
/* 174:    */     case '5': 
/* 175:171 */       return 5;
/* 176:    */     case '6': 
/* 177:174 */       return 6;
/* 178:    */     case '7': 
/* 179:177 */       return 7;
/* 180:    */     case '8': 
/* 181:180 */       return 8;
/* 182:    */     case '9': 
/* 183:183 */       return 9;
/* 184:    */     }
/* 185:186 */     return -1;
/* 186:    */   }
/* 187:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Prog.XmlParser

 * JD-Core Version:    0.7.0.1

 */