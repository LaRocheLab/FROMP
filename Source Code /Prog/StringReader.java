/*  1:   */ package Prog;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ import java.io.BufferedReader;
/*  5:   */ import java.io.BufferedWriter;
/*  6:   */ import java.io.FileReader;
/*  7:   */ import java.io.FileWriter;
/*  8:   */ import java.io.IOException;
/*  9:   */ import java.io.PrintStream;
/* 10:   */ import javax.swing.JFrame;
/* 11:   */ import javax.swing.JLabel;
/* 12:   */ import javax.swing.JPanel;
/* 13:   */ 
			// Class file used to supplement the buffered reader class to be used to parse through input files and conversion files for the data processor	

/* 14:   */ public class StringReader
/* 15:   */ {
/* 16:   */   public BufferedReader readTxt(String path)
/* 17:   */   {
/* 18:20 */     BufferedReader in = null;
/* 19:   */     try
/* 20:   */     {
/* 21:22 */       in = new BufferedReader(new FileReader(path));
/* 22:   */     }
/* 23:   */     catch (IOException e)
/* 24:   */     {
/* 25:24 */       openWarning("Error", "File: pathway" + path + ".chn" + " not found");
/* 26:25 */       e.printStackTrace();
/* 27:   */     }
/* 28:27 */     return in;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void printTxt(String path)
/* 32:   */   {
/* 33:31 */     BufferedReader in = null;
/* 34:   */     try
/* 35:   */     {
/* 36:33 */       in = new BufferedReader(new FileReader(path));
/* 37:34 */       String zeile = null;
/* 38:35 */       while ((zeile = in.readLine()) != null) {
/* 39:36 */         System.out.println("Gelesene Zeile: " + zeile);
/* 40:   */       }
/* 41:   */     }
/* 42:   */     catch (IOException e)
/* 43:   */     {
/* 44:39 */       openWarning("Error", "File: pathway" + path + ".chn" + " not found");
/* 45:40 */       e.printStackTrace();
/* 46:   */     }
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void printTxt(BufferedReader txt)
/* 50:   */   {
/* 51:   */     try
/* 52:   */     {
/* 53:45 */       String zeile = null;
/* 54:46 */       while ((zeile = txt.readLine()) != null) {
/* 55:47 */         System.out.println("Gelesene Zeile: " + zeile);
/* 56:   */       }
/* 57:   */     }
/* 58:   */     catch (IOException e)
/* 59:   */     {
/* 60:50 */       e.printStackTrace();
/* 61:   */     }
/* 62:   */   }
/* 63:   */   
/* 64:   */   public void copyTxtFile(String inPath, String outPath)
/* 65:   */   {
/* 66:54 */     BufferedReader in = readTxt(inPath);
/* 67:55 */     BufferedWriter out = null;
/* 68:56 */     String line = "";
/* 69:   */     try
/* 70:   */     {
/* 71:58 */       out = new BufferedWriter(new FileWriter(outPath));
/* 72:59 */       while ((line = in.readLine()) != null)
/* 73:   */       {
/* 74:60 */         out.write(line);
/* 75:61 */         out.newLine();
/* 76:   */       }
/* 77:63 */       out.close();
/* 78:   */     }
/* 79:   */     catch (IOException e)
/* 80:   */     {
/* 81:66 */       e.printStackTrace();
/* 82:   */     }
/* 83:   */   }
/* 84:   */   
/* 85:   */   private void openWarning(String title, String string)
/* 86:   */   {
/* 87:72 */     JFrame frame = new JFrame(title);
/* 88:73 */     frame.setVisible(true);
/* 89:74 */     frame.setBounds(200, 200, 350, 150);
/* 90:75 */     frame.setLayout(null);
/* 91:76 */     frame.setResizable(false);
/* 92:   */     
/* 93:78 */     JPanel panel = new JPanel();
/* 94:79 */     panel.setBounds(0, 0, 350, 150);
/* 95:80 */     panel.setBackground(Color.lightGray);
/* 96:81 */     panel.setVisible(true);
/* 97:82 */     panel.setLayout(null);
/* 98:83 */     frame.add(panel);
/* 99:   */     
/* :0:85 */     JLabel label = new JLabel(string);
/* :1:   */     
/* :2:87 */     label.setVisible(true);
/* :3:88 */     label.setForeground(Color.black);
/* :4:89 */     label.setBounds(0, 0, 350, 150);
/* :5:90 */     label.setLayout(null);
/* :6:91 */     panel.add(label);
/* :7:   */     
/* :8:93 */     frame.repaint();
/* :9:   */   }
/* ;0:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Prog.StringReader

 * JD-Core Version:    0.7.0.1

 */