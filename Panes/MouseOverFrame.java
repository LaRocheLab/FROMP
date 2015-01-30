/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import Objects.PathwayWithEc;
/*  4:   */ import java.awt.Color;
/*  5:   */ import java.awt.event.WindowAdapter;
/*  6:   */ import java.awt.event.WindowEvent;
/*  7:   */ import java.io.PrintStream;
/*  8:   */ import javax.swing.JFrame;
/*  9:   */ import javax.swing.JLabel;
/* 10:   */ import javax.swing.JPanel;
/* 11:   */ 
/* 12:   */ public class MouseOverFrame
/* 13:   */   extends JFrame
/* 14:   */ {
/* 15:   */   private static final long serialVersionUID = 1L;
/* 16:   */   private static int frameCount;
/* 17:   */   private JLabel mouseOverFrDisp;
/* 18:   */   
/* 19:   */   public MouseOverFrame()
/* 20:   */   {
/* 21:25 */     super("Additional Pathway-information");
/* 22:26 */     if (frameCount > 0)
/* 23:   */     {
/* 24:27 */       closeFrame();
/* 25:28 */       return;
/* 26:   */     }
/* 27:30 */     frameCount = 0;
/* 28:   */     
/* 29:32 */     setVisible(true);
/* 30:33 */     setLayout(null);
/* 31:34 */     setBounds(700, 10, 500, 100);
/* 32:35 */     addWindowListener(new WindowAdapter()
/* 33:   */     {
/* 34:   */       public void windowClosing(WindowEvent e)
/* 35:   */       {
/* 36:37 */         MouseOverFrame.frameCount -= 1;
/* 37:38 */         MouseOverFrame.this.closeFrame();
/* 38:   */       }
/* 39:42 */     });
/* 40:43 */     JPanel mouseOverP = new JPanel();
/* 41:44 */     mouseOverP.setBackground(Color.white);
/* 42:45 */     mouseOverP.setBounds(0, 0, 500, 60);
/* 43:46 */     add(mouseOverP);
/* 44:   */     
/* 45:   */ 
/* 46:49 */     this.mouseOverFrDisp = new JLabel("Additional Pathway-information");
/* 47:50 */     this.mouseOverFrDisp.setBounds(0, 0, 500, 60);
/* 48:51 */     mouseOverP.add(this.mouseOverFrDisp);
/* 49:   */     
/* 50:53 */     setText("Additional Pathway-information");
/* 51:   */     
/* 52:55 */     frameCount += 1;
/* 53:56 */     System.out.println("addMouseOverFrame frameCount class" + frameCount);
/* 54:   */   }
/* 55:   */   
/* 56:   */   public void setText(String text)
/* 57:   */   {
/* 58:60 */     this.mouseOverFrDisp.setText(text);
/* 59:61 */     repaint();
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void setAdditionalInfo(PathwayWithEc path)
/* 63:   */   {
/* 64:64 */     this.mouseOverFrDisp.setText("<html>" + path.name_ + "<br>ID:" + path.id_ + "<br> Wgt:" + path.weight_ + "| Scr:" + path.score_ + "</html>");
/* 65:65 */     repaint();
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void closeFrame()
/* 69:   */   {
/* 70:68 */     setVisible(false);
/* 71:69 */     dispose();
/* 72:70 */     frameCount -= 1;
/* 73:   */   }
/* 74:   */   
/* 75:   */   public static int getFrameCount()
/* 76:   */   {
/* 77:73 */     return frameCount;
/* 78:   */   }
/* 79:   */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Panes.MouseOverFrame
 * JD-Core Version:    0.7.0.1
 */