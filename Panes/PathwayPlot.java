/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.Pathway;
/*   4:    */ import Objects.PathwayWithEc;
/*   5:    */ import Objects.Project;
/*   6:    */ import Objects.Sample;
/*   7:    */ import Prog.DataProcessor;
/*   8:    */ import java.awt.BasicStroke;
/*   9:    */ import java.awt.BorderLayout;
/*  10:    */ import java.awt.Color;
/*  11:    */ import java.awt.Dimension;
/*  12:    */ import java.awt.Graphics;
/*  13:    */ import java.awt.Graphics2D;
/*  14:    */ import java.awt.event.ActionEvent;
/*  15:    */ import java.awt.event.ActionListener;
/*  16:    */ import java.awt.image.BufferedImage;
/*  17:    */ import java.beans.PropertyChangeEvent;
/*  18:    */ import java.beans.PropertyVetoException;
/*  19:    */ import java.beans.VetoableChangeListener;
/*  20:    */ import java.io.File;
/*  21:    */ import java.io.IOException;
/*  22:    */ import java.io.PrintStream;
/*  23:    */ import java.util.ArrayList;
/*  24:    */ import javax.imageio.ImageIO;
/*  25:    */ import javax.swing.ImageIcon;
/*  26:    */ import javax.swing.JButton;
/*  27:    */ import javax.swing.JCheckBox;
/*  28:    */ import javax.swing.JFileChooser;
/*  29:    */ import javax.swing.JLabel;
/*  30:    */ import javax.swing.JPanel;
/*  31:    */ import javax.swing.JScrollPane;
/*  32:    */ import javax.swing.filechooser.FileFilter;
/*  33:    */ 
/*  34:    */ public class PathwayPlot
/*  35:    */   extends JPanel
/*  36:    */ {
/*  37:    */   private static final long serialVersionUID = 1L;
/*  38:    */   Project proj_;
/*  39:    */   BufferedImage canvas_;
/*  40:    */   ImageIcon image_;
/*  41:    */   JLabel imageLabel_;
/*  42:    */   JPanel toolbar_;
/*  43:    */   JPanel showPanel_;
/*  44:    */   JScrollPane scrollPane_;
/*  45:    */   JCheckBox pointsBox_;
/*  46:    */   JCheckBox vertLineBox_;
/*  47:    */   JCheckBox linesBox_;
/*  48: 52 */   float scale_ = 1.0F;
/*  49:    */   JButton scaleDown_;
/*  50:    */   JButton scaleUp_;
/*  51:    */   JButton export_;
/*  52:    */   DataProcessor proc_;
/*  53:    */   
/*  54:    */   public PathwayPlot(Project proj, DataProcessor proc)
/*  55:    */   {
/*  56: 64 */     this.proj_ = proj;
/*  57: 65 */     this.proc_ = proc;
/*  58:    */     
/*  59: 67 */     setBackground(Project.getBackColor_());
/*  60: 68 */     this.canvas_ = new BufferedImage(2000, 2000, 2);
/*  61:    */     
/*  62: 70 */     setLayout(new BorderLayout());
/*  63:    */     
/*  64: 72 */     initCheckBoxes();
/*  65:    */     
/*  66: 74 */     setVisible(true);
/*  67:    */     
/*  68:    */ 
/*  69: 77 */     prePaint();
/*  70:    */   }
/*  71:    */   
/*  72:    */   private void initPanels()
/*  73:    */   {
/*  74: 83 */     this.toolbar_ = new JPanel();
/*  75:    */     
/*  76: 85 */     this.toolbar_.setPreferredSize(new Dimension(getWidth() - 20, 100));
/*  77: 86 */     this.toolbar_.setLocation(0, 0);
/*  78: 87 */     this.toolbar_.setBackground(Project.standard);
/*  79: 88 */     this.toolbar_.setVisible(true);
/*  80: 89 */     this.toolbar_.setLayout(null);
/*  81: 90 */     add("First", this.toolbar_);
/*  82:    */     
/*  83:    */ 
/*  84:    */ 
/*  85: 94 */     this.imageLabel_ = new JLabel(this.image_);
/*  86:    */     
/*  87:    */ 
/*  88: 97 */     this.imageLabel_.setVisible(true);
/*  89:    */   }
/*  90:    */   
/*  91:    */   private void addScrollPane()
/*  92:    */   {
/*  93:103 */     this.scrollPane_ = new JScrollPane(this.imageLabel_);
/*  94:104 */     this.scrollPane_.setVisible(true);
/*  95:105 */     this.scrollPane_.setVerticalScrollBarPolicy(20);
/*  96:106 */     this.scrollPane_.setHorizontalScrollBarPolicy(30);
/*  97:    */     
/*  98:108 */     add("Center", this.scrollPane_);
/*  99:    */   }
/* 100:    */   
/* 101:    */   private void addTools()
/* 102:    */   {
/* 103:112 */     JLabel label = new JLabel("Plot points");
/* 104:113 */     label.setBounds(20, 0, 100, 20);
/* 105:114 */     label.setLayout(null);
/* 106:115 */     label.setVisible(true);
/* 107:116 */     this.toolbar_.add(label);
/* 108:    */     
/* 109:    */ 
/* 110:    */ 
/* 111:120 */     this.toolbar_.add(this.pointsBox_);
/* 112:    */     
/* 113:122 */     label = new JLabel("Plot lines");
/* 114:123 */     label.setBounds(120, 0, 100, 20);
/* 115:124 */     label.setLayout(null);
/* 116:125 */     label.setVisible(true);
/* 117:126 */     this.toolbar_.add(label);
/* 118:    */     
/* 119:128 */     this.toolbar_.add(this.linesBox_);
/* 120:    */     
/* 121:130 */     label = new JLabel("Plot vertical");
/* 122:131 */     label.setBounds(220, 0, 100, 20);
/* 123:132 */     label.setLayout(null);
/* 124:133 */     label.setVisible(true);
/* 125:134 */     this.toolbar_.add(label);
/* 126:135 */     this.toolbar_.add(this.vertLineBox_);
/* 127:    */     
/* 128:137 */     label = new JLabel("Scale " + this.scale_ * 100.0F);
/* 129:138 */     label.setBounds(320, 30, 100, 20);
/* 130:139 */     label.setLayout(null);
/* 131:140 */     label.setVisible(true);
/* 132:141 */     this.toolbar_.add(label);
/* 133:142 */     this.toolbar_.add(this.scaleUp_);
/* 134:143 */     this.toolbar_.add(this.scaleDown_);
/* 135:144 */     this.toolbar_.add(this.export_);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void initCheckBoxes()
/* 139:    */   {
/* 140:152 */     this.linesBox_ = new JCheckBox();
/* 141:153 */     this.linesBox_.setBackground(Project.standard);
/* 142:154 */     this.linesBox_.setBounds(120, 20, 17, 16);
/* 143:155 */     this.linesBox_.setLayout(null);
/* 144:156 */     this.linesBox_.setVisible(true);
/* 145:157 */     this.linesBox_.setSelected(true);
/* 146:158 */     this.linesBox_.addActionListener(new ActionListener()
/* 147:    */     {
/* 148:    */       public void actionPerformed(ActionEvent e)
/* 149:    */       {
/* 150:163 */         PathwayPlot.this.prePaint();
/* 151:    */       }
/* 152:166 */     });
/* 153:167 */     this.linesBox_.addVetoableChangeListener(new VetoableChangeListener()
/* 154:    */     {
/* 155:    */       public void vetoableChange(PropertyChangeEvent evt)
/* 156:    */         throws PropertyVetoException
/* 157:    */       {
/* 158:173 */         PathwayPlot.this.prePaint();
/* 159:    */       }
/* 160:176 */     });
/* 161:177 */     this.pointsBox_ = new JCheckBox();
/* 162:178 */     this.pointsBox_.setBackground(Project.standard);
/* 163:179 */     this.pointsBox_.setBounds(20, 20, 17, 16);
/* 164:180 */     this.pointsBox_.setLayout(null);
/* 165:181 */     this.pointsBox_.setVisible(true);
/* 166:182 */     this.pointsBox_.addActionListener(new ActionListener()
/* 167:    */     {
/* 168:    */       public void actionPerformed(ActionEvent e)
/* 169:    */       {
/* 170:187 */         PathwayPlot.this.prePaint();
/* 171:    */       }
/* 172:190 */     });
/* 173:191 */     this.pointsBox_.addVetoableChangeListener(new VetoableChangeListener()
/* 174:    */     {
/* 175:    */       public void vetoableChange(PropertyChangeEvent evt)
/* 176:    */         throws PropertyVetoException
/* 177:    */       {
/* 178:197 */         PathwayPlot.this.prePaint();
/* 179:    */       }
/* 180:201 */     });
/* 181:202 */     this.vertLineBox_ = new JCheckBox();
/* 182:203 */     this.vertLineBox_.setBackground(Project.standard);
/* 183:204 */     this.vertLineBox_.setBounds(220, 20, 17, 16);
/* 184:205 */     this.vertLineBox_.setLayout(null);
/* 185:206 */     this.vertLineBox_.setVisible(true);
/* 186:207 */     this.vertLineBox_.addActionListener(new ActionListener()
/* 187:    */     {
/* 188:    */       public void actionPerformed(ActionEvent e)
/* 189:    */       {
/* 190:212 */         PathwayPlot.this.prePaint();
/* 191:    */       }
/* 192:215 */     });
/* 193:216 */     this.vertLineBox_.addVetoableChangeListener(new VetoableChangeListener()
/* 194:    */     {
/* 195:    */       public void vetoableChange(PropertyChangeEvent evt)
/* 196:    */         throws PropertyVetoException
/* 197:    */       {
/* 198:222 */         PathwayPlot.this.prePaint();
/* 199:    */       }
/* 200:226 */     });
/* 201:227 */     this.scaleUp_ = new JButton("Scale up");
/* 202:228 */     this.scaleUp_.setBounds(320, 10, 100, 20);
/* 203:229 */     this.scaleUp_.setVisible(true);
/* 204:230 */     this.scaleUp_.addActionListener(new ActionListener()
/* 205:    */     {
/* 206:    */       public void actionPerformed(ActionEvent e)
/* 207:    */       {
/* 208:235 */         if (PathwayPlot.this.scale_ < 5.0F)
/* 209:    */         {
/* 210:236 */           PathwayPlot.this.scale_ += 0.25F;
/* 211:237 */           PathwayPlot.this.prePaint();
/* 212:    */         }
/* 213:    */       }
/* 214:240 */     });
/* 215:241 */     this.scaleDown_ = new JButton("Scale down");
/* 216:242 */     this.scaleDown_.setBounds(320, 50, 100, 20);
/* 217:243 */     this.scaleDown_.setVisible(true);
/* 218:244 */     this.scaleDown_.addActionListener(new ActionListener()
/* 219:    */     {
/* 220:    */       public void actionPerformed(ActionEvent e)
/* 221:    */       {
/* 222:249 */         if (PathwayPlot.this.scale_ > 0.25F)
/* 223:    */         {
/* 224:250 */           PathwayPlot.this.scale_ -= 0.25F;
/* 225:251 */           PathwayPlot.this.prePaint();
/* 226:    */         }
/* 227:    */       }
/* 228:255 */     });
/* 229:256 */     this.export_ = new JButton("Export");
/* 230:257 */     this.export_.setBounds(220, 50, 100, 20);
/* 231:258 */     this.export_.setVisible(true);
/* 232:259 */     this.export_.addActionListener(new ActionListener()
/* 233:    */     {
/* 234:    */       public void actionPerformed(ActionEvent e)
/* 235:    */       {
/* 236:264 */         PathwayPlot.this.export();
/* 237:    */       }
/* 238:    */     });
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void plot()
/* 242:    */   {
/* 243:270 */     int size = (int)(10.0F * this.scale_);
/* 244:271 */     int xStep = (int)(10.0F * this.scale_);
/* 245:272 */     int yStep = (int)(10.0F * this.scale_);
/* 246:273 */     int yOffset = 20;
/* 247:274 */     int lastX = 30;
/* 248:275 */     int lastY = yOffset + yStep * 100;
/* 249:276 */     int x = 30;
/* 250:277 */     int y = yOffset + yStep * 100;
/* 251:    */     
/* 252:279 */     this.canvas_ = null;
/* 253:    */     
/* 254:281 */     System.gc();
/* 255:282 */     this.canvas_ = new BufferedImage((int)(1600.0F * this.scale_), (int)(1200.0F * this.scale_), 2);
/* 256:283 */     this.canvas_.createGraphics();
/* 257:284 */     Sample tmpSamp = (Sample)Project.samples_.get(0);
/* 258:285 */     PathwayWithEc tmpPath = null;
/* 259:286 */     Graphics g = this.canvas_.getGraphics();
/* 260:287 */     g.setColor(Project.getBackColor_());
/* 261:288 */     g.fillRect(0, 0, (int)(2100.0F * this.scale_), (int)(2100.0F * this.scale_));
/* 262:    */     
/* 263:290 */     g.setColor(Color.black);
/* 264:291 */     g.drawString("Score", 0, 20);
/* 265:293 */     for (int i = 0; i < 10; i++) {
/* 266:294 */       g.drawString(String.valueOf(100 - i * 10), 10, i * yStep * 10);
/* 267:    */     }
/* 268:297 */     g.drawLine(30, yOffset, 30, yOffset + yStep * 100);
/* 269:    */     
/* 270:299 */     g.drawLine(20, yOffset + yStep * 100, xStep * tmpSamp.pathways_.size(), yOffset + yStep * 100);
/* 271:    */     
/* 272:301 */     g.drawString("Pathway", xStep * tmpSamp.pathways_.size(), yOffset + yStep * 101);
/* 273:303 */     for (int i = 0; i < tmpSamp.pathways_.size(); i++) {
/* 274:304 */       g.drawString(((PathwayWithEc)tmpSamp.pathways_.get(i)).id_, (i + 1) * xStep + 30, yOffset + (yStep * 101 + 10 * (i % 10)) + 20);
/* 275:    */     }
/* 276:307 */     for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++)
/* 277:    */     {
/* 278:308 */       tmpSamp = (Sample)Project.samples_.get(smpCnt);
/* 279:309 */       lastX = 30;
/* 280:310 */       lastY = yOffset + yStep * 100;
/* 281:    */       
/* 282:312 */       g.setColor(tmpSamp.sampleCol_);
/* 283:313 */       for (int pwCnt = 0; pwCnt < tmpSamp.pathways_.size(); pwCnt++)
/* 284:    */       {
/* 285:314 */         tmpPath = (PathwayWithEc)tmpSamp.pathways_.get(pwCnt);
/* 286:315 */         x = 30 + (pwCnt + 1) * xStep;
/* 287:316 */         if (tmpPath.score_ > Project.minVisScore_) {
/* 288:317 */           y = yOffset + (yStep * 100 - (int)(tmpPath.score_ * yStep));
/* 289:    */         } else {
/* 290:320 */           y = yOffset + yStep * 100;
/* 291:    */         }
/* 292:322 */         if ((!this.proc_.getPathway(tmpPath.id_).isSelected()) || (tmpPath.score_ < Project.minVisScore_)) {
/* 293:323 */           y = yOffset + yStep * 101;
/* 294:    */         }
/* 295:325 */         Graphics2D g2 = (Graphics2D)g;
/* 296:326 */         if (this.linesBox_.isSelected())
/* 297:    */         {
/* 298:327 */           g2.setStroke(new BasicStroke(2.0F));
/* 299:328 */           g2.drawLine(lastX, lastY, x, y);
/* 300:    */         }
/* 301:330 */         if (this.vertLineBox_.isSelected())
/* 302:    */         {
/* 303:331 */           g2.setStroke(new BasicStroke(2.0F));
/* 304:332 */           g2.drawLine(x, yOffset + yStep * 100, x, y);
/* 305:    */         }
/* 306:334 */         if (this.pointsBox_.isSelected())
/* 307:    */         {
/* 308:335 */           g2.setStroke(new BasicStroke(2.0F));
/* 309:336 */           g2.fillOval(x - size / 2, y - size / 2, size, size);
/* 310:    */         }
/* 311:338 */         lastX = x;
/* 312:339 */         lastY = y;
/* 313:    */       }
/* 314:    */     }
/* 315:342 */     this.image_ = new ImageIcon(this.canvas_);
/* 316:    */     
/* 317:344 */     this.imageLabel_ = new JLabel(this.image_);
/* 318:    */   }
/* 319:    */   
/* 320:    */   private void export()
/* 321:    */   {
/* 322:350 */     JFileChooser fChoose_ = new JFileChooser("");
/* 323:351 */     fChoose_.setFileSelectionMode(0);
/* 324:352 */     fChoose_.setBounds(100, 100, 200, 20);
/* 325:353 */     fChoose_.setVisible(true);
/* 326:354 */     File file = new File("");
/* 327:355 */     fChoose_.setSelectedFile(file);
/* 328:356 */     fChoose_.setFileFilter(new FileFilter()
/* 329:    */     {
/* 330:    */       public boolean accept(File f)
/* 331:    */       {
/* 332:360 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".png"))) {
/* 333:361 */           return true;
/* 334:    */         }
/* 335:363 */         return false;
/* 336:    */       }
/* 337:    */       
/* 338:    */       public String getDescription()
/* 339:    */       {
/* 340:369 */         return ".png";
/* 341:    */       }
/* 342:    */     });
/* 343:373 */     if (fChoose_.showSaveDialog(getParent()) == 0)
/* 344:    */     {
/* 345:    */       try
/* 346:    */       {
/* 347:375 */         String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 348:377 */         if (!path.endsWith(".png")) {
/* 349:378 */           path = path + ".png";
/* 350:    */         }
/* 351:383 */         ImageIO.write(this.canvas_, "png", new File(path));
/* 352:    */       }
/* 353:    */       catch (IOException e1)
/* 354:    */       {
/* 355:387 */         e1.printStackTrace();
/* 356:    */       }
/* 357:390 */       repaint();
/* 358:    */     }
/* 359:392 */     System.out.println("Save");
/* 360:    */   }
/* 361:    */   
/* 362:    */   public void prePaint()
/* 363:    */   {
/* 364:395 */     removeAll();
/* 365:396 */     initPanels();
/* 366:397 */     addTools();
/* 367:398 */     plot();
/* 368:399 */     addScrollPane();
/* 369:400 */     invalidate();
/* 370:401 */     validate();
/* 371:402 */     repaint();
/* 372:    */   }
/* 373:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.PathwayPlot

 * JD-Core Version:    0.7.0.1

 */