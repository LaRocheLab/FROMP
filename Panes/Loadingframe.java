/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.Project;
/*   4:    */ import java.util.Timer;
/*   5:    */ import javax.swing.JFrame;
/*   6:    */ import javax.swing.JLabel;
/*   7:    */ import javax.swing.JPanel;
/*   8:    */ 
/*   9:    */ public class Loadingframe
/*  10:    */   extends Thread
/*  11:    */ {
/*  12:    */   private static final long serialVersionUID = 1L;
/*  13:    */   public JFrame frame_;
/*  14:    */   JPanel backGround_;
/*  15:    */   JLabel mover_;
/*  16:    */   JLabel outputer_;
/*  17:    */   JLabel chapOut_;
/*  18:    */   JLabel counter_;
/*  19:    */   Timer timer;
/*  20:    */   int step_;
/*  21: 29 */   double laststep = 0.0D;
/*  22:    */   static boolean running_;
/*  23: 32 */   public static boolean showLoading = true;
/*  24:    */   
/*  25:    */   public Loadingframe()
/*  26:    */   {
/*  27: 35 */     if (!showLoading) {
/*  28: 36 */       return;
/*  29:    */     }
/*  30: 38 */     running_ = true;
/*  31: 39 */     init();
/*  32: 40 */     start();
/*  33:    */   }
/*  34:    */   
/*  35:    */   private void init()
/*  36:    */   {
/*  37: 43 */     if (!showLoading) {
/*  38: 44 */       return;
/*  39:    */     }
/*  40: 46 */     this.frame_ = new JFrame();
/*  41: 47 */     this.frame_.setBounds(200, 100, 150, 150);
/*  42: 48 */     this.frame_.setResizable(false);
/*  43: 49 */     this.frame_.setLayout(null);
/*  44: 50 */     this.frame_.setVisible(true);
/*  45:    */     
/*  46: 52 */     this.backGround_ = new JPanel();
/*  47: 53 */     this.backGround_.setBounds(0, 0, this.frame_.getWidth() + 50, this.frame_.getHeight() + 50);
/*  48: 54 */     this.backGround_.setBackground(Project.getBackColor_());
/*  49: 55 */     this.backGround_.setVisible(true);
/*  50: 56 */     this.backGround_.setLayout(null);
/*  51: 57 */     this.frame_.add(this.backGround_);
/*  52:    */     
/*  53: 59 */     this.step_ = 0;
/*  54:    */     
/*  55: 61 */     this.chapOut_ = new JLabel("");
/*  56: 62 */     this.chapOut_.setBounds(10, 10, 130, 20);
/*  57: 63 */     this.chapOut_.setLayout(null);
/*  58: 64 */     this.chapOut_.setVisible(true);
/*  59: 65 */     this.backGround_.add(this.chapOut_);
/*  60:    */     
/*  61: 67 */     this.mover_ = new JLabel("><((((°>");
/*  62: 68 */     this.mover_.setBounds(-60 + this.step_ * 10, 40, 60, 20);
/*  63:    */     
/*  64: 70 */     this.mover_.setLayout(null);
/*  65: 71 */     this.mover_.setVisible(true);
/*  66: 72 */     this.backGround_.add(this.mover_);
/*  67:    */     
/*  68: 74 */     this.outputer_ = new JLabel("");
/*  69: 75 */     this.outputer_.setBounds(10, 60, 130, 20);
/*  70: 76 */     this.outputer_.setLayout(null);
/*  71: 77 */     this.outputer_.setVisible(true);
/*  72: 78 */     this.backGround_.add(this.outputer_);
/*  73: 79 */     this.frame_.repaint();
/*  74:    */     
/*  75: 81 */     this.counter_ = new JLabel("");
/*  76: 82 */     this.counter_.setBounds(10, 80, 130, 20);
/*  77: 83 */     this.counter_.setLayout(null);
/*  78: 84 */     this.counter_.setVisible(true);
/*  79: 85 */     this.backGround_.add(this.counter_);
/*  80: 86 */     this.frame_.repaint();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void step()
/*  84:    */   {
/*  85: 90 */     if (!showLoading) {
/*  86: 91 */       return;
/*  87:    */     }
/*  88: 93 */     if (this.frame_ == null) {
/*  89: 94 */       init();
/*  90:    */     }
/*  91: 96 */     this.laststep = System.currentTimeMillis();
/*  92: 97 */     this.frame_.repaint();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void step(String mesg)
/*  96:    */   {
/*  97:103 */     if (!showLoading) {
/*  98:104 */       return;
/*  99:    */     }
/* 100:106 */     if (this.frame_ == null) {
/* 101:107 */       init();
/* 102:    */     }
/* 103:109 */     this.laststep = System.currentTimeMillis();
/* 104:110 */     this.outputer_.setText(mesg);
/* 105:111 */     this.frame_.repaint();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void bigStep(String mesg)
/* 109:    */   {
/* 110:117 */     if (!showLoading) {
/* 111:118 */       return;
/* 112:    */     }
/* 113:120 */     if (this.frame_ == null) {
/* 114:121 */       init();
/* 115:    */     }
/* 116:123 */     this.laststep = System.currentTimeMillis();
/* 117:124 */     this.chapOut_.setText(mesg);
/* 118:125 */     this.frame_.repaint();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void updateCounter(int count)
/* 122:    */   {
/* 123:128 */     if (!showLoading) {
/* 124:129 */       return;
/* 125:    */     }
/* 126:131 */     this.counter_.setText("nr.: " + count);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void run()
/* 130:    */   {
/* 131:134 */     if (!showLoading) {
/* 132:135 */       return;
/* 133:    */     }
/* 134:137 */     int hopper = -1;
/* 135:138 */     while (running_)
/* 136:    */     {
/* 137:139 */       this.step_ = ((this.step_ + 1) % 25);
/* 138:140 */       hopper *= -1;
/* 139:141 */       this.mover_.setLocation(-60 + this.step_ * 10, this.mover_.getY() + hopper * 2);
/* 140:142 */       this.frame_.repaint();
/* 141:143 */       this.frame_.paintComponents(this.frame_.getGraphics());
/* 142:    */       try
/* 143:    */       {
/* 144:145 */         Thread.sleep(100L);
/* 145:    */       }
/* 146:    */       catch (InterruptedException e)
/* 147:    */       {
/* 148:148 */         e.printStackTrace();
/* 149:    */       }
/* 150:    */     }
/* 151:151 */     this.frame_.setVisible(false);
/* 152:152 */     this.frame_.dispose();
/* 153:153 */     this.frame_ = null;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static void close()
/* 157:    */   {
/* 158:156 */     if (!showLoading) {
/* 159:157 */       return;
/* 160:    */     }
/* 161:159 */     running_ = false;
/* 162:160 */     System.gc();
/* 163:    */   }
/* 164:    */   
/* 165:    */   public boolean isRunning()
/* 166:    */   {
/* 167:163 */     return running_;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public static long getSerialversionuid()
/* 171:    */   {
/* 172:166 */     return 1L;
/* 173:    */   }
/* 174:    */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     Panes.Loadingframe
 * JD-Core Version:    0.7.0.1
 */