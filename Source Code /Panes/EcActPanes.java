/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.EcWithPathway;
/*   4:    */ import Objects.PathwayWithEc;
/*   5:    */ import Objects.Project;
/*   6:    */ import Prog.DataProcessor;
/*   7:    */ import java.awt.BorderLayout;
/*   8:    */ import java.awt.Color;
/*   9:    */ import java.awt.Dimension;
/*  10:    */ import java.awt.event.ActionEvent;
/*  11:    */ import java.awt.event.ActionListener;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import javax.swing.JButton;
/*  14:    */ import javax.swing.JLabel;
/*  15:    */ import javax.swing.JPanel;
/*  16:    */ import javax.swing.Timer;
/*  17:    */ 

/*  18:    */ public class EcActPanes
/*  19:    */   extends JPanel
/*  20:    */ {
/*  21:    */   private static final long serialVersionUID = 1L;	//
/*  22:    */   Project activeProj_;								// This is the active project
/*  23:    */   ArrayList<PathwayWithEc> pwList_;					// List of pathways to which something is mapped
/*  24:    */   ArrayList<EcWithPathway> ecList_;					// list of ec which map to some pathway
/*  25:    */   DataProcessor proc_;								// the data processor object which controlls the parsing of the input files allowing important information to be gleaned for analysis
/*  26:    */   JPanel showPanel_;									// 
/*  27:    */   JPanel optionsPanel_;								// The options panel
/*  28:    */   JButton pathwaySort;								// A button to choose to g to the PathwayEcMat panel
/*  29:    */   JButton ecSort_;									// A button to go to the ActMatrixPane panel
/*  30:    */   PathwayEcMat pwEcMat;								// 
/*  31:    */   ActMatrixPane actMat_;								// 
/*  32:    */   int mode_;											// 
/*  33:    */   int xsize;											// 
/*  34:    */   int yOffset_;										// 
/*  35:    */   public JButton backButton_;							// Button to go back to Analysis Options
/*  36:    */   Timer timer;										// 
/*  37:    */   
/*  38:    */   public EcActPanes(Project activeProj, DataProcessor proc, Dimension dim)
/*  39:    */   {
/*  40: 46 */     this.activeProj_ = activeProj;
/*  41: 47 */     this.pwList_ = proc.getPathwayList_();
/*  42: 48 */     this.ecList_ = DataProcessor.ecList_;
/*  43: 49 */     this.proc_ = proc;
/*  44: 50 */     this.xsize = (4000 + Project.samples_.size() * 300);
/*  45: 51 */     setSize(dim);
/*  46: 52 */     if (Project.getBackColor_() == null) {
/*  47: 53 */       Project.setBackColor_(Color.orange);
/*  48:    */     }
/*  49: 55 */     setBackground(Project.getBackColor_());
/*  50: 56 */     setLayout(new BorderLayout());
/*  51: 57 */     setVisible(true);
/*  52: 58 */     this.backButton_ = new JButton(" < Back to the Analysis Options");
/*  53: 59 */     initMainPanels();
/*  54:    */     
/*  55: 61 */     addOptions();
/*  56: 62 */     showActivity();
/*  57:    */   }
/*  58:    */   
/*  59:    */   private void initMainPanels()
/*  60:    */   {
/*  61: 65 */     this.optionsPanel_ = new JPanel();
/*  62:    */     
/*  63: 67 */     this.optionsPanel_.setPreferredSize(new Dimension(getWidth() - 50, 100));
/*  64: 68 */     this.optionsPanel_.setBackground(Project.getBackColor_());
/*  65: 69 */     this.optionsPanel_.setVisible(true);
/*  66: 70 */     this.optionsPanel_.setLayout(null);
/*  67: 71 */     add(this.optionsPanel_, "First");
/*  68:    */     
/*  69: 73 */     this.showPanel_ = new JPanel();
/*  70: 74 */     this.showPanel_.setPreferredSize(new Dimension(getWidth() - 70, getHeight() - 150));
/*  71: 75 */     this.showPanel_.setBackground(Project.getBackColor_());
/*  72: 76 */     this.showPanel_.setLayout(new BorderLayout());
/*  73: 77 */     this.showPanel_.setVisible(true);
/*  74: 78 */     add(this.showPanel_, "Center");
/*  75:    */   }
/*  76:    */   
/*  77:    */   private void addOptions()
/*  78:    */   {
/*  79: 81 */     this.optionsPanel_.removeAll();
/*  80:    */     
/*  81: 83 */     this.pathwaySort = new JButton("EC orientated");
/*  82: 84 */     this.pathwaySort.setBounds(0, 80, 200, 20);
/*  83: 85 */     this.pathwaySort.setSelected(false);
/*  84: 86 */     this.pathwaySort.setVisible(true);
/*  85: 87 */     if (this.mode_ == 0) {
/*  86: 88 */       this.pathwaySort.setBackground(Project.standard);
/*  87:    */     }
/*  88: 91 */     this.pathwaySort.addActionListener(new ActionListener()
/*  89:    */     {
/*  90:    */       public void actionPerformed(ActionEvent e)
/*  91:    */       {
/*  92: 96 */         EcActPanes.this.mode_ = 0;
/*  93: 97 */         EcActPanes.this.addOptions();
/*  94: 98 */         EcActPanes.this.showActivity();
/*  95:    */       }
/*  96:100 */     });
/*  97:101 */     this.optionsPanel_.add(this.pathwaySort);
/*  98:    */     
/*  99:103 */     this.ecSort_ = new JButton("Pathway orientated");
/* 100:104 */     this.ecSort_.setBounds(201, 80, 200, 20);
/* 101:105 */     this.ecSort_.setSelected(false);
/* 102:106 */     this.ecSort_.setVisible(true);
/* 103:107 */     if (this.mode_ == 1) {
/* 104:108 */       this.ecSort_.setBackground(Project.standard);
/* 105:    */     }
/* 106:111 */     this.ecSort_.addActionListener(new ActionListener()
/* 107:    */     {
/* 108:    */       public void actionPerformed(ActionEvent e)
/* 109:    */       {
/* 110:116 */         EcActPanes.this.mode_ = 1;
/* 111:117 */         EcActPanes.this.addOptions();
/* 112:118 */         EcActPanes.this.showActivity();
/* 113:    */       }
/* 114:120 */     });
/* 115:121 */     this.optionsPanel_.add(this.ecSort_);
/* 116:    */     
/* 117:123 */     JLabel legend = new JLabel("EC legend:");
/* 118:124 */     legend.setForeground(Project.getFontColor_());
/* 119:125 */     legend.setBounds(800, 10, 150, 17);
/* 120:126 */     this.optionsPanel_.add(legend);
/* 121:    */     
/* 122:128 */     legend = new JLabel("unique EC => '*'");
/* 123:129 */     legend.setForeground(Project.getFontColor_());
/* 124:130 */     legend.setBounds(800, 27, 150, 17);
/* 125:131 */     this.optionsPanel_.add(legend);
/* 126:    */     
/* 127:133 */     legend = new JLabel("unmapped EC => '#'");
/* 128:134 */     legend.setForeground(Project.getFontColor_());
/* 129:135 */     legend.setBounds(800, 44, 150, 17);
/* 130:136 */     this.optionsPanel_.add(legend);
/* 131:    */     
/* 132:138 */     legend = new JLabel("incomplete EC => '^'");
/* 133:139 */     legend.setForeground(Project.getFontColor_());
/* 134:140 */     legend.setBounds(800, 61, 150, 17);
/* 135:141 */     this.optionsPanel_.add(legend);
/* 136:    */     
/* 137:    */ 
/* 138:144 */     this.backButton_.setBounds(10, 10, 300, 25);
/* 139:145 */     this.optionsPanel_.add(this.backButton_);
/* 140:    */   }
/* 141:    */   
/* 142:    */   private void showActivity()
/* 143:    */   {
/* 144:149 */     this.showPanel_.removeAll();
/* 145:    */     
/* 146:151 */     switchmode();
/* 147:152 */     switch (this.mode_)
/* 148:    */     {
/* 149:    */     case 0: 
/* 150:154 */       if ((this.actMat_ == null) || (Project.dataChanged)) {
/* 151:155 */         this.actMat_ = new ActMatrixPane(this.activeProj_, this.ecList_, this.proc_, this.showPanel_.getSize());
/* 152:    */       }
/* 153:157 */       this.showPanel_.add(this.actMat_);
/* 154:158 */       invalidate();
/* 155:159 */       validate();
/* 156:160 */       repaint();
/* 157:161 */       break;
/* 158:    */     case 1: 
/* 159:164 */       if ((this.pwEcMat == null) || (Project.dataChanged)) {
/* 160:165 */         this.pwEcMat = new PathwayEcMat(this.pwList_, this.activeProj_, this.proc_, this.showPanel_.getSize());
/* 161:    */       }
/* 162:167 */       this.showPanel_.add(this.pwEcMat);
/* 163:168 */       invalidate();
/* 164:169 */       validate();
/* 165:170 */       repaint();
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   private void switchmode() {}
/* 170:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.EcActPanes

 * JD-Core Version:    0.7.0.1

 */