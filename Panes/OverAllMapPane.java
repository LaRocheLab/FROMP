/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ import java.awt.image.BufferedImage;
/*  5:   */ import java.io.File;
/*  6:   */ import java.io.IOException;
/*  7:   */ import javax.imageio.ImageIO;
/*  8:   */ import javax.swing.ImageIcon;
/*  9:   */ import javax.swing.JLabel;
/* 10:   */ import javax.swing.JPanel;
/* 11:   */ 
/* 12:   */ public class OverAllMapPane
/* 13:   */   extends JPanel
/* 14:   */ {
/* 15:   */   private static final long serialVersionUID = 1L;
/* 16:   */   private JLabel image_;
/* 17:   */   final String picPath_;
/* 18:   */   private BufferedImage pathMap_;
/* 19:   */   
/* 20:   */   public OverAllMapPane()
/* 21:   */   {
/* 22:26 */     this.picPath_ = ("pics" + File.separator + "ec01100-50pro.png");
/* 23:   */     try
/* 24:   */     {
/* 25:29 */       this.pathMap_ = ImageIO.read(new File(this.picPath_));
/* 26:   */     }
/* 27:   */     catch (IOException e)
/* 28:   */     {
/* 29:32 */       e.printStackTrace();
/* 30:   */     }
/* 31:34 */     setBounds(0, 0, this.pathMap_.getWidth() + 300, this.pathMap_.getHeight() - 50);
/* 32:35 */     setLayout(null);
/* 33:36 */     setBackground(Color.white);
/* 34:   */     
/* 35:38 */     ImageIcon icon = new ImageIcon(this.pathMap_);
/* 36:39 */     this.image_ = new JLabel(icon);
/* 37:40 */     this.image_.setLayout(null);
/* 38:41 */     this.image_.setBounds(0, 0, this.pathMap_.getWidth(), this.pathMap_.getHeight());
/* 39:42 */     this.image_.setVisible(true);
/* 40:43 */     add(this.image_);
/* 41:   */   }
/* 42:   */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Panes.OverAllMapPane
 * JD-Core Version:    0.7.0.1
 */