/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import Objects.EcWithPathway;
/*  4:   */ import Objects.PathwayWithEc;
/*  5:   */ import Objects.Project;
/*  6:   */ import Prog.DataProcessor;
/*  7:   */ import java.awt.BorderLayout;
/*  8:   */ import java.awt.Color;
/*  9:   */ import java.awt.Dimension;
/* 10:   */ import java.util.ArrayList;
/* 11:   */ import javax.swing.JButton;
/* 12:   */ import javax.swing.JCheckBox;
/* 13:   */ import javax.swing.JPanel;
/* 14:   */ 
/* 15:   */ public class PathwayActPanes
/* 16:   */   extends JPanel
/* 17:   */ {
/* 18:   */   private static final long serialVersionUID = 1L;
/* 19:   */   Project activeProj_;
/* 20:   */   ArrayList<PathwayWithEc> pwList_;
/* 21:   */   ArrayList<EcWithPathway> ecList_;
/* 22:   */   DataProcessor proc_;
/* 23:   */   JCheckBox includeWeights;
/* 24:   */   PathwayActivitymatrixPane actMat_;
/* 25:   */   JPanel showPanel_;
/* 26:   */   JPanel optionsPanel_;
/* 27:   */   public JButton backButton_;
/* 28:   */   int xwidth;
/* 29:   */   
/* 30:   */   public PathwayActPanes(Project activeProj, DataProcessor proc, Dimension dim)
/* 31:   */   {
/* 32:39 */     this.activeProj_ = activeProj;
/* 33:40 */     this.pwList_ = proc.getPathwayList_();
/* 34:41 */     this.ecList_ = DataProcessor.ecList_;
/* 35:42 */     this.proc_ = proc;
/* 36:43 */     setSize(dim);
/* 37:44 */     setBackground(Project.standard);
/* 38:45 */     setLayout(new BorderLayout());
/* 39:46 */     setVisible(true);
/* 40:47 */     this.backButton_ = new JButton("< Back to the analysisoptions");
/* 41:   */     
/* 42:49 */     prepaint();
/* 43:   */   }
/* 44:   */   
/* 45:   */   private void initMainPanels()
/* 46:   */   {
/* 47:52 */     this.optionsPanel_ = new JPanel();
/* 48:53 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth() - 50, 50));
/* 49:54 */     this.optionsPanel_.setBackground(Project.getBackColor_().darker());
/* 50:55 */     this.optionsPanel_.setBackground(Project.getBackColor_());
/* 51:56 */     this.optionsPanel_.setVisible(true);
/* 52:57 */     this.optionsPanel_.setLayout(null);
/* 53:58 */     add(this.optionsPanel_, "First");
/* 54:   */     
/* 55:   */ 
/* 56:61 */     this.backButton_.setBounds(20, 12, 200, 25);
/* 57:62 */     this.optionsPanel_.add(this.backButton_);
/* 58:   */     
/* 59:64 */     this.showPanel_ = new JPanel();
/* 60:65 */     this.showPanel_.setPreferredSize(new Dimension(getWidth() - 70, getHeight() - 150));
/* 61:66 */     this.showPanel_.setBackground(Project.getBackColor_().brighter());
/* 62:67 */     this.showPanel_.setLayout(new BorderLayout());
/* 63:68 */     this.showPanel_.setVisible(true);
/* 64:69 */     add(this.showPanel_, "Center");
/* 65:   */   }
/* 66:   */   
/* 67:   */   private void showActivity()
/* 68:   */   {
/* 69:74 */     if ((this.actMat_ == null) || (Project.dataChanged))
/* 70:   */     {
/* 71:75 */       this.actMat_ = new PathwayActivitymatrixPane(this.proc_, this.activeProj_, this.showPanel_.getSize());
/* 72:76 */       this.showPanel_.add(this.actMat_);
/* 73:   */     }
/* 74:79 */     else if (this.actMat_.changed_)
/* 75:   */     {
/* 76:80 */       this.actMat_.showMatrix();
/* 77:   */     }
/* 78:   */   }
/* 79:   */   
/* 80:   */   private void prepaint()
/* 81:   */   {
/* 82:85 */     initMainPanels();
/* 83:86 */     showActivity();
/* 84:87 */     invalidate();
/* 85:88 */     validate();
/* 86:89 */     repaint();
/* 87:   */   }
/* 88:   */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Panes.PathwayActPanes
 * JD-Core Version:    0.7.0.1
 */