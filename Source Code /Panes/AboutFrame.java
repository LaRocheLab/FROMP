/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ import javax.swing.JFrame;
/*  5:   */ import javax.swing.JLabel;
/*  6:   */ import javax.swing.JPanel;
/*  7:   */ 
			//An inFROMPmational (...see what I did there...) JFrame (ie. popup window) which gives details about 
			//the version and developers of the FROMP software. 

/*  8:   */ public class AboutFrame
/*  9:   */   extends JFrame
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = 1L;
/* 12:   */   
/* 13:   */   public AboutFrame()
/* 14:   */   {
/* 15:17 */     setTitle("About Fromp");
/* 16:18 */     setBounds(500, 400, 300, 300);
/* 17:19 */     setVisible(true);
/* 18:20 */     setResizable(false);
/* 19:21 */     setLayout(null);
/* 20:   */     
/* 21:23 */     JPanel back = new JPanel();
/* 22:24 */     back.setBounds(0, 0, 300, 300);
/* 23:25 */     back.setVisible(true);
/* 24:26 */     back.setLayout(null);
/* 25:27 */     back.setBackground(Color.white);
/* 26:28 */     add(back);
/* 27:   */     
/* 28:30 */     JLabel fromp = new JLabel("Fromp Ver. 1.0: 05.2012");
/* 29:31 */     fromp.setBounds(10, 10, 200, 20);
/* 30:32 */     fromp.setVisible(true);
/* 31:33 */     back.add(fromp);
/* 32:   */     
/* 33:35 */     JLabel idea = new JLabel("Developers:");
/* 34:36 */     idea.setBounds(10, 50, 200, 20);
/* 35:37 */     idea.setVisible(true);
/* 36:38 */     back.add(idea);
/* 37:   */     
/* 38:40 */     idea = new JLabel("Dhwani Desai");
/* 39:41 */     idea.setBounds(30, 80, 200, 20);
/* 40:42 */     idea.setVisible(true);
/* 41:43 */     back.add(idea);
/* 42:   */     
/* 43:45 */     idea = new JLabel("Harald Schunck");
/* 44:46 */     idea.setBounds(30, 110, 200, 20);
/* 45:47 */     idea.setVisible(true);
/* 46:48 */     back.add(idea);
/* 47:   */     
/* 48:50 */     idea = new JLabel("Johannes Löser");
/* 49:51 */     idea.setBounds(30, 140, 200, 20);
/* 50:52 */     idea.setVisible(true);
/* 51:53 */     back.add(idea);
/* 52:   */     
/* 53:55 */     idea = new JLabel("Julie LaRoche");
/* 54:56 */     idea.setBounds(30, 170, 200, 20);
/* 55:57 */     idea.setVisible(true);
/* 56:58 */     back.add(idea);
/* 57:   */   }
/* 58:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.AboutFrame

 * JD-Core Version:    0.7.0.1

 */