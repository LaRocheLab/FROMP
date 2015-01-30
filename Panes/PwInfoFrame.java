/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import Objects.EcWithPathway;
/*  4:   */ import Objects.Pathway;
/*  5:   */ import Objects.PathwayWithEc;
/*  6:   */ import Objects.Project;
/*  7:   */ import Objects.Sample;
/*  8:   */ import Prog.PathButt;
/*  9:   */ import java.awt.Color;
/* 10:   */ import java.io.PrintStream;
/* 11:   */ import java.util.ArrayList;
/* 12:   */ import javax.swing.JFrame;
/* 13:   */ import javax.swing.JPanel;
/* 14:   */ 
/* 15:   */ public class PwInfoFrame
/* 16:   */   extends JFrame
/* 17:   */ {
/* 18:   */   private static final long serialVersionUID = 1L;
/* 19:   */   
/* 20:   */   public PwInfoFrame(EcWithPathway ec, Project actProj, Sample samp)
/* 21:   */   {
/* 22:20 */     int lineDis = 40;
/* 23:21 */     Sample samp_ = samp;
/* 24:22 */     setBounds(100, 100, 400, 50 + lineDis * ec.pathways_.size());
/* 25:23 */     setVisible(true);
/* 26:24 */     setLayout(null);
/* 27:25 */     if ((ec.amount_ <= 1) && (samp != null)) {
/* 28:26 */       ec.amount_ = samp.getEc(ec.name_).amount_;
/* 29:   */     }
/* 30:28 */     setTitle(ec.name_ + "(" + ec.bioName_ + ")" + " * " + ec.amount_);
/* 31:   */     
/* 32:30 */     JPanel back = new JPanel();
/* 33:31 */     back.setBackground(Color.white);
/* 34:32 */     back.setBounds(0, 0, getWidth(), getHeight());
/* 35:33 */     back.setVisible(true);
/* 36:34 */     back.setLayout(null);
/* 37:35 */     add(back);
/* 38:39 */     for (int pwCnt = 0; pwCnt < ec.pathways_.size(); pwCnt++) {
/* 39:40 */       for (int pwCnt2 = 0; pwCnt2 < samp_.pathways_.size(); pwCnt2++) {
/* 40:41 */         if (((Pathway)ec.pathways_.get(pwCnt)).id_.contentEquals(((PathwayWithEc)samp_.pathways_.get(pwCnt2)).id_))
/* 41:   */         {
/* 42:42 */           if (actProj == null) {
/* 43:43 */             System.out.println("actProj");
/* 44:   */           }
/* 45:45 */           if (samp_.pathways_.get(pwCnt2) == null) {
/* 46:46 */             System.out.println("samp_.pathways_.get(pwCnt2)");
/* 47:   */           }
/* 48:48 */           PathButt label = new PathButt(Project.samples_, samp_, (PathwayWithEc)samp_.pathways_.get(pwCnt2), Color.orange, "", 0);
/* 49:49 */           label.setBounds(0, lineDis * pwCnt, 400, lineDis);
/* 50:50 */           label.setVisible(true);
/* 51:51 */           label.setLayout(null);
/* 52:52 */           back.add(label);
/* 53:   */         }
/* 54:   */       }
/* 55:   */     }
/* 56:56 */     repaint();
/* 57:   */   }
/* 58:   */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Panes.PwInfoFrame
 * JD-Core Version:    0.7.0.1
 */