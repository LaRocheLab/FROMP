/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ import javax.swing.JFrame;
/*  5:   */ import javax.swing.JLabel;
/*  6:   */ import javax.swing.JPanel;
	//		import javax.swing.JTextArea;
	//		import java.awt.Font;
/*  7:   */ 
			//The Help frame. Not super exciting maybe, but it is also the project summary window (when it is called by the data processor)

/*  8:   */ public class HelpFrame
/*  9:   */   extends JFrame
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = 1L;
/* 12:16 */   public static boolean showSummary = true;
/* 13:   */   
/* 14:   */   public HelpFrame(String Text)
/* 15:   */   {
/* 16:19 */     if (!showSummary) {
/* 17:20 */       return;
/* 18:   */     }
/* 19:22 */     setTitle("Project summary");
/* 20:23 */     setBounds(500, 400, 300, 450);
/* 21:24 */     setVisible(true);
/* 22:25 */     setResizable(false);
/* 23:26 */     setLayout(null);
/* 24:   */     
/* 25:28 */     JPanel back = new JPanel();
/* 26:29 */     back.setBounds(0, 0, 300, 400);
/* 27:30 */     back.setVisible(true);
/* 28:31 */     back.setLayout(null);
/* 29:32 */     back.setBackground(Color.white);
/* 30:33 */     add(back);
/* 31:   */
/* 32:35 */     JLabel fromp = new JLabel(Text);
/* 33:36 */     fromp.setBounds(0, 0, 300, 400);
/* 34:37 */     fromp.setVisible(true);
/* 35:38 */     back.add(fromp);
/* 36:   */   }
/* 37:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.HelpFrame

 * JD-Core Version:    0.7.0.1

 */