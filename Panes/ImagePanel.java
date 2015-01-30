/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import java.awt.Graphics;
/*  4:   */ import java.awt.image.BufferedImage;
/*  5:   */ import javax.swing.JFrame;
/*  6:   */ import javax.swing.JPanel;
/*  7:   */ 
/*  8:   */ public class ImagePanel
/*  9:   */   extends JPanel
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = 1L;
/* 12:   */   public static BufferedImage image_;
/* 13:   */   
/* 14:   */   public ImagePanel(BufferedImage image)
/* 15:   */   {
/* 16:18 */     image_ = image;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void paintComponent(Graphics g)
/* 20:   */   {
/* 21:23 */     g.drawImage(image_, 0, 0, null);
/* 22:24 */     repaint();
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static void showImage(BufferedImage image, String title)
/* 26:   */   {
/* 27:29 */     JFrame f = new JFrame(title);
/* 28:30 */     f.add(new ImagePanel(image));
/* 29:31 */     f.setLocation(100, 100);
/* 30:32 */     f.setSize(image.getWidth(), image.getHeight() + 30);
/* 31:33 */     f.setVisible(true);
/* 32:   */   }
/* 33:   */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Panes.ImagePanel
 * JD-Core Version:    0.7.0.1
 */