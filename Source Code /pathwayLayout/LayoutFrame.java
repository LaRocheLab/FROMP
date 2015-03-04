/*   1:    */ package pathwayLayout;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Dimension;
/*   5:    */ import java.awt.event.ActionEvent;
/*   6:    */ import java.awt.event.ActionListener;
/*   7:    */ import java.awt.event.ComponentEvent;
/*   8:    */ import java.awt.event.ComponentListener;
/*   9:    */ import java.io.File;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import javax.accessibility.AccessibleContext;
/*  14:    */ import javax.swing.JButton;
/*  15:    */ import javax.swing.JFileChooser;
/*  16:    */ import javax.swing.JFrame;
/*  17:    */ import javax.swing.JMenu;
/*  18:    */ import javax.swing.JMenuBar;
/*  19:    */ import javax.swing.JMenuItem;
/*  20:    */ import javax.swing.JPanel;
/*  21:    */ import javax.swing.JTextField;
/*  22:    */ import javax.swing.KeyStroke;
/*  23:    */ import javax.swing.filechooser.FileFilter;
/*  24:    */ 
/*  25:    */ public class LayoutFrame
/*  26:    */   extends JFrame
/*  27:    */ {
/*  28:    */   private static final long serialVersionUID = 1L;
/*  29:    */   PathLayoutGrid grid;
/*  30:    */   JPanel back_;
/*  31:    */   ArrayList<Node> nodes_;
/*  32:    */   LayoutPane pane;
/*  33:    */   JMenu menu_;
/*  34:    */   JMenuBar menuBar_;
/*  35: 45 */   final String basePath_ = new File("").getAbsolutePath() + File.separator;
/*  36:    */   
/*  37:    */   public LayoutFrame(int xSize, int ySize, PathLayoutGrid Grid, ArrayList<Node> nodes)
/*  38:    */   {
/*  39: 48 */     super("LayoutTest");
/*  40: 49 */     this.nodes_ = nodes;
/*  41: 50 */     setSize(new Dimension(xSize, ySize));
/*  42: 51 */     setVisible(true);
/*  43: 52 */     setLayout(null);
/*  44: 53 */     File filet = new File("userPaths");
/*  45: 54 */     if (!filet.exists()) {
/*  46: 55 */       filet.mkdir();
/*  47:    */     }
/*  48: 58 */     this.grid = Grid;
/*  49:    */     
/*  50: 60 */     addComponentListener(new ComponentListener()
/*  51:    */     {
/*  52:    */       public void componentShown(ComponentEvent arg0) {}
/*  53:    */       
/*  54:    */       public void componentResized(ComponentEvent arg0)
/*  55:    */       {
/*  56: 71 */         if (LayoutFrame.this.pane != null) {
/*  57: 72 */           LayoutFrame.this.pane.reDo();
/*  58:    */         }
/*  59:    */       }
/*  60:    */       
/*  61:    */       public void componentMoved(ComponentEvent arg0) {}
/*  62:    */       
/*  63:    */       public void componentHidden(ComponentEvent arg0) {}
/*  64: 88 */     });
/*  65: 89 */     buildPicture();
/*  66:    */   }
/*  67:    */   
/*  68:    */   private void addMenu()
/*  69:    */   {
/*  70: 92 */     this.menu_ = new JMenu("File");
/*  71: 93 */     this.menuBar_ = new JMenuBar();
/*  72:    */     
/*  73: 95 */     this.menu_.getAccessibleContext().setAccessibleDescription(
/*  74: 96 */       "The only menu in this program that has menu items");
/*  75: 97 */     this.menuBar_.add(this.menu_);
/*  76:    */     
/*  77:    */ 
/*  78:100 */     new JButton();
/*  79:101 */     new JTextField("Enter Pathway name");
/*  80:    */     
/*  81:103 */     JMenuItem miItem = new JMenuItem("New Pathway", 
/*  82:104 */       78);
/*  83:105 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/*  84:106 */       78, 8));
/*  85:107 */     miItem.getAccessibleContext().setAccessibleDescription(
/*  86:108 */       "Design a new Pathway");
/*  87:109 */     miItem.addActionListener(new ActionListener()
/*  88:    */     {
/*  89:    */       public void actionPerformed(ActionEvent e) {}
/*  90:116 */     });
/*  91:117 */     this.menu_.add(miItem);
/*  92:    */     
/*  93:119 */     miItem = new JMenuItem("Open Pathway", 79);
/*  94:120 */     miItem.setAccelerator(KeyStroke.getKeyStroke(79, 8));
/*  95:121 */     miItem.getAccessibleContext().setAccessibleDescription("Open a Pathway");
/*  96:122 */     miItem.addActionListener(new ActionListener()
/*  97:    */     {
/*  98:    */       public void actionPerformed(ActionEvent e)
/*  99:    */       {
/* 100:127 */         System.out.println("open");
/* 101:128 */         System.out.println("basepath " + LayoutFrame.this.basePath_);
/* 102:129 */         JFileChooser fChoose_ = new JFileChooser(LayoutFrame.this.basePath_ + File.separator + "userPaths");
/* 103:130 */         fChoose_.setFileSelectionMode(0);
/* 104:131 */         fChoose_.setBounds(100, 100, 200, 20);
/* 105:132 */         fChoose_.setVisible(true);
/* 106:133 */         File file = new File(LayoutFrame.this.basePath_ + "userPaths" + File.separator + ".");
/* 107:134 */         fChoose_.setSelectedFile(file);
/* 108:135 */         fChoose_.setFileFilter(new FileFilter()
/* 109:    */         {
/* 110:    */           public boolean accept(File f)
/* 111:    */           {
/* 112:139 */             if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".pwm"))) {
/* 113:140 */               return true;
/* 114:    */             }
/* 115:142 */             return false;
/* 116:    */           }
/* 117:    */           
/* 118:    */           public String getDescription()
/* 119:    */           {
/* 120:148 */             return ".pwm";
/* 121:    */           }
/* 122:    */         });
/* 123:152 */         if (fChoose_.showOpenDialog(LayoutFrame.this.getParent()) == 0) {
/* 124:    */           try
/* 125:    */           {
/* 126:154 */             String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 127:156 */             if (!path.endsWith(".pwm"))
/* 128:    */             {
/* 129:157 */               path = path + ".pwm";
/* 130:158 */               System.out.println(".pwm");
/* 131:    */             }
/* 132:160 */             LayoutFrame.this.grid.openPathWay(path);
/* 133:    */           }
/* 134:    */           catch (IOException e1)
/* 135:    */           {
/* 136:165 */             e1.printStackTrace();
/* 137:    */           }
/* 138:    */         }
/* 139:169 */         LayoutFrame.this.buildPicture();
/* 140:    */       }
/* 141:172 */     });
/* 142:173 */     this.menu_.add(miItem);
/* 143:    */     
/* 144:    */ 
/* 145:176 */     miItem = new JMenuItem("Save Pathway", 
/* 146:177 */       83);
/* 147:178 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/* 148:179 */       83, 8));
/* 149:180 */     miItem.getAccessibleContext().setAccessibleDescription(
/* 150:181 */       "Save your Pathway");
/* 151:182 */     miItem.addActionListener(new ActionListener()
/* 152:    */     {
/* 153:    */       public void actionPerformed(ActionEvent e)
/* 154:    */       {
/* 155:186 */         if (LayoutPane.nameField.getText().contentEquals("Enter Pathname"))
/* 156:    */         {
/* 157:187 */           LayoutPane.nameField.setForeground(Color.red);
/* 158:188 */           LayoutFrame.this.pane.reDo();
/* 159:    */         }
/* 160:191 */         else if (LayoutFrame.this.grid.loadPath != null)
/* 161:    */         {
/* 162:192 */           if (!LayoutFrame.this.grid.loadPath.isEmpty())
/* 163:    */           {
/* 164:193 */             PathLayoutGrid.pathWayName = LayoutPane.nameField.getText();
/* 165:194 */             LayoutFrame.this.grid.savePathway(LayoutFrame.this.grid.loadPath);
/* 166:    */           }
/* 167:    */           else
/* 168:    */           {
/* 169:197 */             LayoutFrame.this.saveAs();
/* 170:    */           }
/* 171:    */         }
/* 172:    */         else
/* 173:    */         {
/* 174:201 */           LayoutFrame.this.saveAs();
/* 175:    */         }
/* 176:    */       }
/* 177:206 */     });
/* 178:207 */     this.menu_.add(miItem);
/* 179:    */     
/* 180:209 */     miItem = new JMenuItem("Save Pathway as", 
/* 181:210 */       65);
/* 182:211 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/* 183:212 */       65, 8));
/* 184:213 */     miItem.getAccessibleContext().setAccessibleDescription(
/* 185:214 */       "Save Pathway as");
/* 186:215 */     miItem.addActionListener(new ActionListener()
/* 187:    */     {
/* 188:    */       public void actionPerformed(ActionEvent e)
/* 189:    */       {
/* 190:220 */         if (LayoutPane.nameField.getText().contentEquals("Enter Pathname"))
/* 191:    */         {
/* 192:221 */           LayoutPane.nameField.setForeground(Color.red);
/* 193:222 */           LayoutFrame.this.pane.reDo();
/* 194:    */         }
/* 195:    */         else
/* 196:    */         {
/* 197:225 */           LayoutFrame.this.saveAs();
/* 198:226 */           System.out.println("Save");
/* 199:    */         }
/* 200:    */       }
/* 201:230 */     });
/* 202:231 */     this.menu_.add(miItem);
/* 203:    */     
/* 204:233 */     miItem = new JMenuItem("Preview", 
/* 205:234 */       80);
/* 206:235 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/* 207:236 */       80, 8));
/* 208:237 */     miItem.getAccessibleContext().setAccessibleDescription(
/* 209:238 */       "Preview pathway");
/* 210:239 */     miItem.addActionListener(new ActionListener()
/* 211:    */     {
/* 212:    */       public void actionPerformed(ActionEvent e)
/* 213:    */       {
/* 214:244 */         LayoutFrame.this.grid.previewPathway();
/* 215:    */       }
/* 216:247 */     });
/* 217:248 */     this.menu_.add(miItem);
/* 218:    */     
/* 219:250 */     miItem = new JMenuItem("Quit", 
/* 220:251 */       81);
/* 221:252 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/* 222:253 */       81, 8));
/* 223:254 */     miItem.getAccessibleContext().setAccessibleDescription(
/* 224:255 */       "Quit designer");
/* 225:256 */     miItem.addActionListener(new ActionListener()
/* 226:    */     {
/* 227:    */       public void actionPerformed(ActionEvent e)
/* 228:    */       {
/* 229:261 */         System.exit(0);
/* 230:    */       }
/* 231:264 */     });
/* 232:265 */     this.menu_.add(miItem);
/* 233:266 */     setJMenuBar(this.menuBar_);
/* 234:    */   }
/* 235:    */   
/* 236:    */   private void saveAs()
/* 237:    */   {
/* 238:270 */     JFileChooser fChoose_ = new JFileChooser(File.separator + "userPaths");
/* 239:271 */     fChoose_.setFileSelectionMode(0);
/* 240:272 */     fChoose_.setBounds(100, 100, 200, 20);
/* 241:273 */     fChoose_.setVisible(true);
/* 242:274 */     File file = new File(this.basePath_ + "userPaths" + File.separator + ".");
/* 243:275 */     fChoose_.setSelectedFile(file);
/* 244:276 */     fChoose_.setFileFilter(new FileFilter()
/* 245:    */     {
/* 246:    */       public boolean accept(File f)
/* 247:    */       {
/* 248:280 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".pwm"))) {
/* 249:281 */           return true;
/* 250:    */         }
/* 251:283 */         return false;
/* 252:    */       }
/* 253:    */       
/* 254:    */       public String getDescription()
/* 255:    */       {
/* 256:289 */         return ".pwm";
/* 257:    */       }
/* 258:    */     });
/* 259:293 */     if (fChoose_.showSaveDialog(getParent()) == 0)
/* 260:    */     {
/* 261:    */       try
/* 262:    */       {
/* 263:295 */         String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 264:297 */         if (!path.endsWith(".pwm"))
/* 265:    */         {
/* 266:298 */           path = path + ".pwm";
/* 267:299 */           System.out.println(".pwm");
/* 268:    */         }
/* 269:301 */         PathLayoutGrid.pathWayName = LayoutPane.nameField.getText();
/* 270:302 */         this.grid.savePathway(path);
/* 271:    */       }
/* 272:    */       catch (IOException e1)
/* 273:    */       {
/* 274:306 */         e1.printStackTrace();
/* 275:    */       }
/* 276:309 */       invalidate();
/* 277:310 */       validate();
/* 278:311 */       repaint();
/* 279:    */     }
/* 280:    */   }
/* 281:    */   
/* 282:    */   public void buildPicture()
/* 283:    */   {
/* 284:317 */     addMenu();
/* 285:318 */     addBackPanel();
/* 286:    */     
/* 287:320 */     invalidate();
/* 288:321 */     validate();
/* 289:322 */     repaint();
/* 290:    */   }
/* 291:    */   
/* 292:    */   private void addBackPanel()
/* 293:    */   {
/* 294:325 */     if (this.pane != null) {
/* 295:326 */       remove(this.pane);
/* 296:    */     } else {
/* 297:329 */       this.pane = new LayoutPane(getWidth(), getHeight(), this.grid, this.nodes_);
/* 298:    */     }
/* 299:331 */     add(this.pane);
/* 300:332 */     this.pane.reDo();
/* 301:333 */     invalidate();
/* 302:334 */     validate();
/* 303:335 */     repaint();
/* 304:    */   }
/* 305:    */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     pathwayLayout.LayoutFrame
 * JD-Core Version:    0.7.0.1
 */