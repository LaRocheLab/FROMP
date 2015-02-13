/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import java.awt.Component;
/*  4:   */ import java.awt.HeadlessException;
/*  5:   */ import javax.swing.JDialog;
/*  6:   */ import javax.swing.JFileChooser;
/*  7:   */ 
			//an extension of JFileChooser. Not really that interesting

/*  8:   */ public class MyChooser
/*  9:   */   extends JFileChooser
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = 1L;
/* 12:   */   
/* 13:   */   public MyChooser(String lastPath_)
/* 14:   */   {
/* 15:17 */     super(lastPath_);
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected JDialog createDialog(Component parent)
/* 19:   */     throws HeadlessException
/* 20:   */   {
/* 21:23 */     JDialog dlg = super.createDialog(parent);
/* 22:24 */     dlg.setLocation(20, 20);
/* 23:25 */     return dlg;
/* 24:   */   }
/* 25:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.MyChooser

 * JD-Core Version:    0.7.0.1

 */