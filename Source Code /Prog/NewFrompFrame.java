/*    1:     */ package Prog;
/*    2:     */ 
/*    3:     */ import Objects.Project;
/*    4:     */ import Panes.AboutFrame;
/*    5:     */ import Panes.EcActPanes;
/*    6:     */ import Panes.EditsamplesPane;
/*    7:     */ import Panes.PathwayActPanes;
/*    8:     */ import Panes.PathwaySelectP;
/*    9:     */ import Panes.PathwaysPane;
/*   10:     */ import Panes.PwSearchPane;
/*   11:     */ import java.awt.BorderLayout;
/*   12:     */ import java.awt.Color;
/*   13:     */ import java.awt.Desktop;
/*   14:     */ import java.awt.Dimension;
/*   15:     */ import java.awt.event.ActionEvent;
/*   16:     */ import java.awt.event.ActionListener;
/*   17:     */ import java.awt.event.WindowEvent;
/*   18:     */ import java.awt.event.WindowListener;
/*   19:     */ import java.io.BufferedReader;
/*   20:     */ import java.io.BufferedWriter;
/*   21:     */ import java.io.File;
/*   22:     */ import java.io.FileReader;
/*   23:     */ import java.io.FileWriter;
/*   24:     */ import java.io.IOException;
/*   25:     */ import java.io.PrintStream;
/*   26:     */ import java.util.ArrayList;
/*   27:     */ import javax.accessibility.AccessibleContext;
/*   28:     */ import javax.swing.JButton;
/*   29:     */ import javax.swing.JColorChooser;
/*   30:     */ import javax.swing.JFileChooser;
/*   31:     */ import javax.swing.JFrame;
/*   32:     */ import javax.swing.JLabel;
/*   33:     */ import javax.swing.JMenu;
/*   34:     */ import javax.swing.JMenuBar;
/*   35:     */ import javax.swing.JMenuItem;
/*   36:     */ import javax.swing.JPanel;
/*   37:     */ import javax.swing.JTextField;
/*   38:     */ import javax.swing.KeyStroke;
/*   39:     */ import javax.swing.filechooser.FileFilter;
/*   40:     */ import pathwayLayout.PathLayoutGrid;
				import javax.swing.JScrollPane;
				import javax.swing.ScrollPaneConstants;
/*   41:     */ 
			/**
			 * Main GUI frame for FROMP. Every part of the GUI FROMP is run through this frame.
			 * 
			 * @author Jennifer Terpstra, Kevan Lynch
			 */
/*   42:     */ public class NewFrompFrame
/*   43:     */   extends JFrame
/*   44:     */ {
/*   45:     */   private static final long serialVersionUID = 7855981267252684730L;			//
/*   46:     */   private String separator_;													// The file seperator used by your OS. ie '/' for Linux/Mac and '\' for Windows
/*   47:     */   private String path_;															// The canonical path for this folder
/*   48:     */   private Color backCol_;														// Backgound colour
/*   49:     */   private Color sysColor_;														// System colour
/*   50:     */   private Color overallSampCol;													//
/*   51:     */   private JMenuBar menuBar_;													// Menu bar at the top of Fromp. ie 'File' , 'Project' , 'Analyze' etc.
/*   52:     */   private JMenu menu_;															//
/*   53:     */   private JPanel back_;															// The back panel on this JFrame
/*   54:     */   public static ArrayList<String> recentProj_;									//	
/*   55:  69 */   final String basePath_ = new File(".").getAbsolutePath() + File.separator;	// The base path of the Fromp software. Nessesairy for all relative paths to function 
/*   56:  70 */   final String recentProjPath_ = "recentProj.txt";								// Path to the file which contains the recently opened projects 
/*   57:     */   private Controller control_;													// The controller. Allows user to save, load etc.
				  private JScrollPane showJPanel_;												// Used only to be able to scroll through samples in the EditSamplesPane
/*   58:     */   
/*   59:     */   public NewFrompFrame()
/*   60:     */   {
/*   61:  78 */     this.separator_ = File.separator;
/*   62:     */     
/*   63:  80 */     setVisible(true);
/*   64:  81 */     setLayout(new BorderLayout());
/*   65:  82 */     setBounds(20, 20, 1200, 800);
/*   66:     */     
/*   67:  84 */     setDefaultCloseOperation(0);
					//Adds an action listner to the close button of the the JFrame which prints "Window Closing" when it is closed and 
					//opens a warning that you will lose all unsaved data by closing before you close.
/*   68:  85 */     addWindowListener(new WindowListener() 
/*   69:     */     {
/*   70:     */       public void windowOpened(WindowEvent arg0) {}
/*   71:     */       
/*   72:     */       public void windowIconified(WindowEvent arg0) {}
/*   73:     */       
/*   74:     */       public void windowDeiconified(WindowEvent arg0) {}
/*   75:     */       
/*   76:     */       public void windowDeactivated(WindowEvent arg0) {}
/*   77:     */       
/*   78:     */       public void windowClosing(WindowEvent arg0)
/*   79:     */       {
/*   80: 114 */         NewFrompFrame.this.openClosingFrame();
/*   82:     */       }
/*   83:     */       
/*   84:     */       public void windowClosed(WindowEvent arg0) {
                        System.out.println("Window Closing");
                      }
/*   85:     */       
/*   86:     */       public void windowActivated(WindowEvent arg0) {}
/*   87: 130 */     });
/*   88: 131 */     setTitle("FROMP-Alpha - Fragment Recruitment on Metabolic Pathways");
/*   89:     */     
/*   90: 133 */     this.backCol_ = new Color(233, 233, 233);
/*   91: 134 */     this.sysColor_ = Color.black;
/*   92: 135 */     this.overallSampCol = Color.red;
/*   93:     */     
/*   94: 137 */     this.control_ = new Controller(this.sysColor_);
/*   95:     */     
/*   96: 139 */     addBackPanel();
/*   97:     */     try
/*   98:     */     {
/*   99: 142 */       this.path_ = new File("").getCanonicalPath();
/*  100:     */     }
/*  101:     */     catch (IOException e1)
/*  102:     */     {
/*  103: 145 */       e1.printStackTrace();
/*  104:     */     }
/*  105: 147 */     addMenu();				// Adds the drop down menu
/*  106: 148 */     loadRecentProj();		// Adds the list of "Recent Projects"
/*  107: 149 */     showNewProjPanel();		// Shows the statring panel of Fromp. Includes 'Project Options' , and 'Recent Projects'
/*  108: 150 */     invalidate();			// Next three commands in combination reload the JFrame 
/*  109: 151 */     validate();				//
/*  110: 152 */     repaint();				//
/*  111:     */   }
/*  112:     */   
/*  113:     */   private void addMenu()
/*  114:     */   {//adds the drop down menu bar and all of its elements
/*  115: 155 */     this.menuBar_ = new JMenuBar();
/*  116:     */     	
/*  117: 157 */     addFileMenu();		// Adds the "File" drop down menu option
/*  118: 158 */     addProjectMenu();	// Adds the "Project" drop down menu option
/*  119: 159 */     addAnalyseMenu();	// I think you can piece it together from here
/*  120: 160 */     addSettingsMenu();	//
/*  121: 161 */     addDesignMenu();	//
/*  122: 162 */     addHelpMenu();		//
/*  123:     */     
/*  124: 164 */     setJMenuBar(this.menuBar_);
/*  125:     */   }
/*  126:     */   
/*  127:     */   private void addFileMenu()
/*  128:     */   {
/*  129: 169 */     this.menu_ = new JMenu("File");
/*  130:     */     
/*  131: 171 */     this.menu_.getAccessibleContext().setAccessibleDescription(
/*  132: 172 */       "The only menu in this program that has menu items");
/*  133: 173 */     this.menuBar_.add(this.menu_);
/*  134:     */     
/*  135:     */ 
/*  136: 176 */     new JButton();
/*  137: 177 */     new JTextField("Enter project name");
/*  138:     */     
/*  139: 179 */     JMenuItem miItem = new JMenuItem("New Project", 
/*  140: 180 */       78);
/*  141: 181 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/*  142: 182 */       78, 8));
/*  143: 183 */     miItem.getAccessibleContext().setAccessibleDescription(
/*  144: 184 */       "Start a new project");
/*  145: 185 */     miItem.addActionListener(new ActionListener()
/*  146:     */     {
/*  147:     */       public void actionPerformed(ActionEvent e)
/*  148:     */       {
/*  149: 190 */         NewFrompFrame.this.newProjectName();
/*  150:     */       }
/*  151: 193 */     });
/*  152: 194 */     this.menu_.add(miItem);
/*  153:     */     
/*  154: 196 */     miItem = new JMenuItem("Open Project", 79);
/*  155: 197 */     miItem.setAccelerator(KeyStroke.getKeyStroke(79, 8));
/*  156: 198 */     miItem.getAccessibleContext().setAccessibleDescription("Open a project");
/*  157: 199 */     miItem.addActionListener(new ActionListener()
/*  158:     */     {
/*  159:     */       public void actionPerformed(ActionEvent e)
/*  160:     */       {
/*  161: 204 */         NewFrompFrame.this.openProj();
/*  162:     */       }
/*  163: 207 */     });
/*  164: 208 */     this.menu_.add(miItem);
/*  165:     */     
/*  166:     */ 
/*  167: 211 */     miItem = new JMenuItem("Save Project", 
/*  168: 212 */       83);
/*  169: 213 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/*  170: 214 */       83, 8));
/*  171: 215 */     miItem.getAccessibleContext().setAccessibleDescription(
/*  172: 216 */       "Save your project");
/*  173: 217 */     miItem.addActionListener(new ActionListener()
/*  174:     */     {
/*  175:     */       public void actionPerformed(ActionEvent e)
/*  176:     */       {
//*  177: 221 */         NewFrompFrame.this.clearBack();
/*  178: 222 */         Controller.loadPathways(true);
/*  179: 223 */         String path = Controller.saveProject();
/*  180: 224 */         System.out.println("Save");
/*  181: 225 */         NewFrompFrame.addRecentPath(path);
/*  182: 226 */         NewFrompFrame.this.saveRecentProj();
//*  183: 227 */         NewFrompFrame.this.clearBack();
/*  184:     */       }
/*  185: 230 */     });
/*  186: 231 */     this.menu_.add(miItem);
/*  187:     */     
/*  188: 233 */     miItem = new JMenuItem("Save Project as", 
/*  189: 234 */       65);
/*  190: 235 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/*  191: 236 */       65, 8));
/*  192: 237 */     miItem.getAccessibleContext().setAccessibleDescription(
/*  193: 238 */       "Export Project as");
/*  194: 239 */     miItem.addActionListener(new ActionListener()
/*  195:     */     {
/*  196:     */       public void actionPerformed(ActionEvent e)
/*  197:     */       {
/*  198: 244 */         NewFrompFrame.this.saveAs();
/*  199:     */       }
/*  200: 247 */     });
/*  201: 248 */     this.menu_.add(miItem);
					
					miItem = new JMenuItem("Home", 
/*  204: 251 */       86);
/*  205: 252 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/*  206: 253 */       86, 8));
/*  207: 254 */     miItem.getAccessibleContext().setAccessibleDescription(
/*  208: 255 */       "Home");
/*  209: 256 */     miItem.addActionListener(new ActionListener()
/*  210:     */     {
/*  211:     */       public void actionPerformed(ActionEvent e)
/*  212:     */       {
						NewFrompFrame.this.control_ = new Controller(NewFrompFrame.this.sysColor_);
						clearBack();
/*  213: 261 */         addMenu();				
/*  106: 148 */     	loadRecentProj();		
/*  107: 149 */    		showNewProjPanel();		
/*  108: 150 */     	invalidate();			
/*  109: 151 */     	validate();				
/*  110: 152 */     	repaint();	
/*  214:     */       }
/*  215: 264 */     });
/*  216: 265 */     this.menu_.add(miItem);
/*  202:     */     
/*  203: 250 */     miItem = new JMenuItem("Quit", 
/*  204: 251 */       81);
/*  205: 252 */     miItem.setAccelerator(KeyStroke.getKeyStroke(
/*  206: 253 */       81, 8));
/*  207: 254 */     miItem.getAccessibleContext().setAccessibleDescription(
/*  208: 255 */       "Quit");
/*  209: 256 */     miItem.addActionListener(new ActionListener()
/*  210:     */     {
/*  211:     */       public void actionPerformed(ActionEvent e)
/*  212:     */       {
/*  213: 261 */         openClosingFrame();
/*  214:     */       }
/*  215: 264 */     });
/*  216: 265 */     this.menu_.add(miItem);
/*  217:     */     
/*  218: 267 */     invalidate();
/*  219: 268 */     validate();
/*  220: 269 */     repaint();
/*  221:     */   }
/*  222:     */   
/*  223:     */   private void addProjectMenu()
/*  224:     */   {
/*  225: 274 */     this.menu_ = new JMenu("Project");
/*  226:     */     
/*  227: 276 */     this.menu_.getAccessibleContext().setAccessibleDescription(
/*  228: 277 */       "Manage your Project");
/*  229: 278 */     this.menuBar_.add(this.menu_);
/*  230:     */     
/*  231:     */ 
/*  232: 281 */     JMenuItem mItem = new JMenuItem("Edit Samples", 
/*  233: 282 */       68);
/*  234: 283 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  235: 284 */       68, 8));
/*  236: 285 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  237: 286 */       "Add Samples to your project");
/*  238: 287 */     mItem.addActionListener(new ActionListener()
/*  239:     */     {
/*  240:     */       public void actionPerformed(ActionEvent e)
/*  241:     */       {
/*  242: 291 */         NewFrompFrame.this.newEditSamples();
/*  243:     */       }
/*  244: 299 */     });
/*  245: 300 */     this.menu_.add(mItem);
/*  246:     */     
/*  247:     */ 
/*  248:     */ 
/*  249: 304 */     mItem = new JMenuItem("Select Pathways", 
/*  250: 305 */       87);
/*  251: 306 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  252: 307 */       87, 8));
/*  253: 308 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  254: 309 */       "Select wanted Pathways");
/*  255: 310 */     mItem.addActionListener(new ActionListener()
/*  256:     */     {
/*  257:     */       public void actionPerformed(ActionEvent e)
/*  258:     */       {
						if (NewFrompFrame.this.control_.gotSamples()){
/*  259: 315 */         	NewFrompFrame.this.selectPws();
						} else {
							warningFrame();
						}
/*  260:     */       }
/*  261: 318 */     });
/*  262: 319 */     this.menu_.add(mItem);
/*  263:     */     
/*  264: 321 */     mItem = new JMenuItem("Search", 
/*  265: 322 */       88);
/*  266: 323 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  267: 324 */       88, 8));
/*  268: 325 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  269: 326 */       "Search");
/*  270: 327 */     mItem.addActionListener(new ActionListener()
/*  271:     */     {
/*  272:     */       public void actionPerformed(ActionEvent e)
/*  273:     */       {
/*  274: 332 */         System.out.println("Search Pathway");
/*  275: 333 */         if (NewFrompFrame.this.control_.gotSamples())
/*  276:     */         {
/*  277: 334 */           Controller.loadPathways(true);
/*  278: 335 */           NewFrompFrame.this.searchPathway();
/*  279:     */         }
/*  280:     */       }
/*  281: 343 */     });
/*  282: 344 */     this.menu_.add(mItem);
/*  283:     */   }
/*  284:     */   
/*  285:     */   private void addAnalyseMenu()
/*  286:     */   {
/*  287: 350 */     this.menu_ = new JMenu("Analyze");
/*  288:     */     
/*  289: 352 */     this.menu_.getAccessibleContext().setAccessibleDescription(
/*  290: 353 */       "Analyze");
/*  291: 354 */     this.menuBar_.add(this.menu_);
/*  292:     */     
/*  293:     */ 
/*  294:     */ 
/*  295:     */ 
/*  296: 359 */     JMenuItem mItem = new JMenuItem("Pathway Completeness", 
/*  297: 360 */       67);
/*  298: 361 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  299: 362 */       67, 8));
/*  300: 363 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  301: 364 */       "Pathway completeness score");
/*  302: 365 */     mItem.addActionListener(new ActionListener()
/*  303:     */     {
/*  304:     */       public void actionPerformed(ActionEvent e)
/*  305:     */       {
/*  329: 402 */         if (NewFrompFrame.this.control_.gotSamples())
/*  308:     */         {
						  NewFrompFrame.this.clearBack();
/*  309: 374 */           Controller.loadPathways(true);
						  if(!NewFrompFrame.this.control_.processor_.selectedPathways()){
							warningFrame("No pathways selected!");
							selectPws();
						  } else {
/*  310: 375 */           	NewFrompFrame.this.showPathwayScorePane();
						  }
/*  311:     */         } else {
							warningFrame();
						}
/*  312: 382 */         System.out.println("Pathway orientated");
						
/*  313:     */       }
/*  314: 385 */     });
/*  315: 386 */     this.menu_.add(mItem);
/*  316:     */     
/*  317: 388 */     mItem = new JMenuItem("Pathway Activity", 
/*  318: 389 */       80);
/*  319: 390 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  320: 391 */       80, 8));
/*  321: 392 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  322: 393 */       "Pathway Activity Analysis");
/*  323: 394 */     mItem.addActionListener(new ActionListener()
/*  324:     */     {
/*  325:     */       public void actionPerformed(ActionEvent e)
/*  326:     */       {
/*  327: 399 */         System.out.println("Pathway Activity");
						if (NewFrompFrame.this.control_.gotSamples())
/*  330:     */         {
						  NewFrompFrame.this.clearBack();
/*  331: 403 */           Controller.loadPathways(true);
						  if(!NewFrompFrame.this.control_.processor_.selectedPathways()){
							warningFrame("No pathways selected!");
							selectPws();
						  } else {
/*  332: 404 */           	NewFrompFrame.this.showPathwayActMatrix();
						  }
/*  333:     */         } else {
							warningFrame();
						}
/*  334:     */       }
/*  335: 414 */     });
/*  336: 415 */     this.menu_.add(mItem);
/*  337:     */     
/*  338:     */ 
/*  339: 418 */     mItem = new JMenuItem("EC Activity", 
/*  340: 419 */       69);
/*  341: 420 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  342: 421 */       69, 8));
/*  343: 422 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  344: 423 */       "EC Activity Matrix");
/*  345: 424 */     mItem.addActionListener(new ActionListener()
/*  346:     */     {
/*  347:     */       public void actionPerformed(ActionEvent e)
/*  348:     */       {
/*  350: 430 */         if (NewFrompFrame.this.control_.gotSamples())
/*  351:     */         {
						  NewFrompFrame.this.clearBack();
/*  352: 431 */           Controller.loadPathways(true);
						  if(!NewFrompFrame.this.control_.processor_.selectedPathways()){
							warningFrame("No pathways selected!");
							selectPws();
						  } else {
/*  353: 432 */           	NewFrompFrame.this.showEcActPanes();
/*  354:     */           }
						} else {
							warningFrame();
						}
/*  355: 438 */         System.out.println("Compare Samples");
/*  356:     */       }
/*  357: 441 */     });
/*  358: 442 */     this.menu_.add(mItem);
/*  359:     */   }

				  private void warningFrame(){
				  	JFrame wrngFrame=new JFrame();
/* 1108:1203 */     wrngFrame.setBounds(200, 200, 350, 100);
/* 1109:1204 */     wrngFrame.setLayout(null);
/* 1110:1205 */     wrngFrame.setVisible(true);
/* 1111:     */     
/* 1112:1207 */     JPanel backP = new JPanel();
/* 1113:1208 */     backP.setBounds(0, 0, 350, 100);
/* 1114:1209 */     backP.setLayout(null);
/* 1115:1210 */     wrngFrame.add(backP);
/* 1116:     */     
/* 1117:1212 */     JLabel label = new JLabel("Warning! No samples have been selected!");
/* 1118:1213 */     label.setBounds(25, 25, 300, 25);
/* 1119:1214 */     backP.add(label);
				  }
				  private void warningFrame(String strIN){
				  	JFrame wrngFrame=new JFrame();
/* 1108:1203 */     wrngFrame.setBounds(200, 200, 350, 100);
/* 1109:1204 */     wrngFrame.setLayout(null);
/* 1110:1205 */     wrngFrame.setVisible(true);
/* 1111:     */     
/* 1112:1207 */     JPanel backP = new JPanel();
/* 1113:1208 */     backP.setBounds(0, 0, 350, 100);
/* 1114:1209 */     backP.setLayout(null);
/* 1115:1210 */     wrngFrame.add(backP);
/* 1116:     */     
/* 1117:1212 */     JLabel label = new JLabel("Warning! "+strIN);
/* 1118:1213 */     label.setBounds(25, 25, 300, 25);
/* 1119:1214 */     backP.add(label);
				  }
/*  360:     */   
/*  361:     */   private void addSettingsMenu()
/*  362:     */   {
/*  363: 448 */     this.menu_ = new JMenu("Settings");
/*  364:     */     
/*  365: 450 */     this.menu_.getAccessibleContext().setAccessibleDescription(
/*  366: 451 */       "Settings");
/*  367: 452 */     this.menuBar_.add(this.menu_);
/*  368:     */     
/*  369:     */ 
/*  370:     */ 
/*  371: 456 */     JMenuItem mItem = new JMenuItem("Background Color", 
/*  372: 457 */       66);
/*  373: 458 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  374: 459 */       66, 8));
/*  375: 460 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  376: 461 */       "Set Background Color");
/*  377: 462 */     mItem.addActionListener(new ActionListener()
/*  378:     */     {
/*  379:     */       public void actionPerformed(ActionEvent e)
/*  380:     */       {
/*  382: 467 */         NewFrompFrame.this.backCol_ = JColorChooser.showDialog(NewFrompFrame.this.getParent(), "Choose BackgroundColor", NewFrompFrame.this.backCol_);
/*  383: 468 */         Project.setBackColor_(NewFrompFrame.this.backCol_);
/*  384: 469 */         NewFrompFrame.this.back_.setBackground(NewFrompFrame.this.backCol_);
//						NewFrompFrame.this.clearBack();
//						NewFrompFrame.this.addBackPanel();
//*  385: 470 */        NewFrompFrame.this.back_.invalidate();
//*  386: 471 */        NewFrompFrame.this.back_.validate();
//*  387: 472 */        NewFrompFrame.this.back_.repaint();
						NewFrompFrame.this.invalidate();
/*  386: 471 */         NewFrompFrame.this.validate();
/*  387: 472 */         NewFrompFrame.this.repaint();
/*  388:     */       }
/*  389: 474 */     });
/*  390: 475 */     this.menu_.add(mItem);
/*  391:     */     
/*  392: 477 */     mItem = new JMenuItem("System Color", 
/*  393: 478 */       70);
/*  394: 479 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  395: 480 */       70, 8));
/*  396: 481 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  397: 482 */       "Set System Color");
/*  398: 483 */     mItem.addActionListener(new ActionListener()
/*  399:     */     {
/*  400:     */       public void actionPerformed(ActionEvent e)
/*  401:     */       {
/*  402: 489 */         NewFrompFrame.this.sysColor_ = JColorChooser.showDialog(NewFrompFrame.this.getParent(), "Choose System Color", NewFrompFrame.this.sysColor_);
/*  403: 490 */         Project.setFontColor_(NewFrompFrame.this.sysColor_);
/*  404:     */       }
/*  405: 493 */     });
/*  406: 494 */     this.menu_.add(mItem);
/*  407:     */     
/*  408:     */ 
/*  409: 497 */     mItem = new JMenuItem("Overall Sample Color", 
/*  410: 498 */       67);
/*  411: 499 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  412: 500 */       67, 8));
/*  413: 501 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  414: 502 */       "Set Overall Sample Color");
/*  415: 503 */     mItem.addActionListener(new ActionListener()
/*  416:     */     {
/*  417:     */       public void actionPerformed(ActionEvent e)
/*  418:     */       {
/*  420: 509 */         NewFrompFrame.this.overallSampCol = JColorChooser.showDialog(NewFrompFrame.this.getParent(), "Choose Overall Color", NewFrompFrame.this.overallSampCol);
/*  421: 510 */         Project.setOverAllColor_(NewFrompFrame.this.overallSampCol);
/*  422:     */       }
/*  423: 513 */     });
/*  424: 514 */     this.menu_.add(mItem);
/*  425:     */   }
/*  426:     */   
/*  427:     */   private void addDesignMenu()
/*  428:     */   {
/*  429: 517 */     this.menu_ = new JMenu("Designer");
/*  430:     */     
/*  431: 519 */     this.menu_.getAccessibleContext().setAccessibleDescription(
/*  432: 520 */       "Designer");
/*  433: 521 */     this.menuBar_.add(this.menu_);
/*  434:     */     
/*  435: 523 */     JMenuItem mItem = new JMenuItem("Open Designer", 
/*  436: 524 */       71);
/*  437: 525 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  438: 526 */       71, 8));
/*  439: 527 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  440: 528 */       "About Fromp");
/*  441: 529 */     mItem.addActionListener(new ActionListener()
/*  442:     */     {
/*  443:     */       public void actionPerformed(ActionEvent e)
/*  444:     */       {
/*  445: 535 */         PathLayoutGrid Grid = new PathLayoutGrid(10, 10, true);
/*  446:     */       }
/*  447: 538 */     });
/*  448: 539 */     this.menu_.add(mItem);
/*  449:     */   }
/*  450:     */   
/*  451:     */   private void addHelpMenu()
/*  452:     */   {
/*  453: 544 */     this.menu_ = new JMenu("Help");
/*  454:     */     
/*  455: 546 */     this.menu_.getAccessibleContext().setAccessibleDescription(
/*  456: 547 */       "Help");
/*  457: 548 */     this.menuBar_.add(this.menu_);
/*  458:     */     
/*  459: 550 */     JMenuItem mItem = new JMenuItem("Help", 
/*  460: 551 */       72);
/*  461: 552 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  462: 553 */       72, 8));
/*  463: 554 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  464: 555 */       "About Fromp");
/*  465: 556 */     mItem.addActionListener(new ActionListener()
/*  466:     */     {
/*  467:     */       public void actionPerformed(ActionEvent e)
/*  468:     */       {
/*  469: 561 */         String file = NewFrompFrame.this.basePath_.substring(0, NewFrompFrame.this.basePath_.length() - 2) + "fromp-users-guide.pdf";
/*  470: 562 */         System.out.println(file);
/*  471: 563 */         NewFrompFrame.this.openPDf(file);
/*  472:     */       }
/*  473: 572 */     });
/*  474: 573 */     this.menu_.add(mItem);
/*  475:     */     
/*  476:     */ 
/*  477: 576 */     mItem = new JMenuItem("About", 
/*  478: 577 */       84);
/*  479: 578 */     mItem.setAccelerator(KeyStroke.getKeyStroke(
/*  480: 579 */       84, 8));
/*  481: 580 */     mItem.getAccessibleContext().setAccessibleDescription(
/*  482: 581 */       "About Fromp");
/*  483: 582 */     mItem.addActionListener(new ActionListener()
/*  484:     */     {
/*  485:     */       public void actionPerformed(ActionEvent e)
/*  486:     */       {
/*  487: 588 */         AboutFrame about = new AboutFrame();
/*  488: 589 */         System.out.println("About");
/*  489:     */       }
/*  490: 592 */     });
/*  491: 593 */     this.menu_.add(mItem);
/*  492:     */   }
/*  493:     */   
/*  494:     */   private void clearBack()
/*  495:     */   {//updates the FROMP frame
//*  496: 596 */     System.out.println("ClearBack");
/*  497: 597 */     this.back_.removeAll();
/*  498: 598 */     if (Project.workpath_ != null) {
/*  499: 599 */       setTitle("Project: " + Project.workpath_ + " - FROMP-Alpha - Fragment Recruitment on Metabolic Pathways");
/*  500:     */     } else {
/*  501: 602 */       setTitle("FROMP-Alpha - Fragment Recruitment on Metabolic Pathways");
/*  502:     */     }
/*  503: 604 */     invalidate();
/*  504: 605 */     validate();
/*  505: 606 */     repaint();
/*  506:     */   }
/*  507:     */   
/*  508:     */   private void addBackPanel()
/*  509:     */   {
/*  510: 609 */     this.back_ = new JPanel();
/*  511: 610 */     this.back_.setVisible(true);
/*  512: 611 */     this.back_.setLayout(new BorderLayout());
/*  513: 612 */     this.back_.setLocation(0, 0);
/*  514: 613 */     add(this.back_, "Center");
/*  515: 614 */     this.back_.setSize(getMaximumSize());
/*  516: 615 */     this.back_.setBackground(this.backCol_);
/*  517:     */   }
/*  518:     */   
/*  519:     */   private void newEditSamples()
/*  520:     */   {
/*  521: 621 */     clearBack();
/*  522: 622 */     this.control_.clearProcessor();
/*  523: 623 */     Controller.dataChanged = true;
/*  524: 624 */     Project.dataChanged = true;
/*  525: 625 */     EditsamplesPane samplesP_ = new EditsamplesPane(Controller.project_);
/*  526: 626 */     samplesP_.backButton_.addActionListener(new ActionListener()
/*  527:     */     {
/*  528:     */       public void actionPerformed(ActionEvent e)
/*  529:     */       {
/*  530: 631 */         NewFrompFrame.this.clearBack();
/*  531: 632 */         NewFrompFrame.this.showNewProjPanel();
/*  532:     */       }
/*  533: 634 */     });
/*  534: 635 */     samplesP_.nextButton_.addActionListener(new ActionListener()
/*  535:     */     {
/*  536:     */       public void actionPerformed(ActionEvent e)
/*  537:     */       {
						if(!NewFrompFrame.this.control_.gotSamples()) {
							warningFrame();
						} else {
/*  538: 640 */         	NewFrompFrame.this.clearBack();
/*  539:     */         
/*  540: 642 */         	NewFrompFrame.this.selectPws();
						}
/*  541:     */       }
/*  542: 644 */     });

					this.showJPanel_ = new JScrollPane(samplesP_);
					this.showJPanel_.setVisible(true);
					this.showJPanel_.setVerticalScrollBarPolicy(20);

					this.back_.add("Center", this.showJPanel_);

/*  544: 646 */     invalidate();
/*  545: 647 */     validate();
/*  546: 648 */     repaint();
/*  547:     */   }
/*  548:     */   
/*  549:     */   private void selectPws()
/*  550:     */   {// Opens the pathway selection panel
/*  551: 654 */     if (Controller.processor_ == null) {
/*  552: 655 */       Controller.loadPathways(false);
/*  553:     */     }
/*  554: 657 */     clearBack();
/*  555: 658 */     final PathwaySelectP selectP = new PathwaySelectP(Controller.processor_.getPathwayList_());
/*  556: 659 */     selectP.back_.addActionListener(new ActionListener()
/*  557:     */     {
/*  558:     */       public void actionPerformed(ActionEvent e)
/*  559:     */       {
/*  560: 664 */         NewFrompFrame.this.clearBack();
/*  561: 665 */         NewFrompFrame.this.newEditSamples();
/*  562:     */       }
/*  563: 667 */     });
/*  564: 668 */     selectP.next_.addActionListener(new ActionListener()
/*  565:     */     {
/*  566:     */       public void actionPerformed(ActionEvent e)
/*  567:     */       {
						Boolean noPathways=true;
						if(selectP.pathSelected()){
							noPathways=false;
						}
						if(!noPathways){
/*  568: 673 */         	NewFrompFrame.this.clearBack();
/*  569: 674 */         	NewFrompFrame.this.showAnalyseOptions();
						} else {
							warningFrame("No pathways selected!");
						}
/*  570:     */       }
/*  571: 676 */     });
/*  572: 677 */     this.back_.add(selectP);
/*  573: 678 */     invalidate();
/*  574: 679 */     validate();
/*  575: 680 */     repaint();
/*  576:     */   }
/*  577:     */   
/*  578:     */   private void showPathwayScorePane()
/*  579:     */   {// Opens the pathway completeness analysis
/*  580: 686 */     clearBack();
/*  581: 687 */     PathwaysPane pwPanes = new PathwaysPane(Controller.project_, Controller.processor_, getSize());
/*  582: 688 */     pwPanes.backButton_.addActionListener(new ActionListener()
/*  583:     */     {
/*  584:     */       public void actionPerformed(ActionEvent e)
/*  585:     */       {
/*  586: 693 */         NewFrompFrame.this.clearBack();
/*  587: 694 */         NewFrompFrame.this.showAnalyseOptions();
/*  588:     */       }
/*  589: 696 */     });
/*  590: 697 */     this.back_.add(pwPanes);
/*  591: 698 */     invalidate();
/*  592: 699 */     validate();
/*  593: 700 */     repaint();
/*  594:     */   }
/*  595:     */   
/*  596:     */   private void showPathwayActMatrix()
/*  597:     */   {// Opens the pathway activity analysis
/*  598: 706 */     clearBack();
/*  599: 707 */     PathwayActPanes actP = new PathwayActPanes(Controller.project_, Controller.processor_, getSize());
/*  600: 708 */     actP.backButton_.addActionListener(new ActionListener()
/*  601:     */     {
/*  602:     */       public void actionPerformed(ActionEvent e)
/*  603:     */       {
/*  604: 713 */         NewFrompFrame.this.clearBack();
/*  605: 714 */         NewFrompFrame.this.showAnalyseOptions();
/*  606:     */       }
/*  607: 716 */     });
/*  608: 717 */     this.back_.add(actP);
/*  609: 718 */     invalidate();
/*  610: 719 */     validate();
/*  611: 720 */     repaint();
/*  612:     */   }
/*  613:     */   
/*  614:     */   private void showEcActPanes()
/*  615:     */   {// Opens the ec activity analysis
/*  616: 726 */     clearBack();
/*  617: 727 */     EcActPanes matrixP_ = new EcActPanes(Controller.project_, Controller.processor_, getSize());
/*  618: 728 */     matrixP_.backButton_.addActionListener(new ActionListener()
/*  619:     */     {
/*  620:     */       public void actionPerformed(ActionEvent e)
/*  621:     */       {
/*  622: 733 */         NewFrompFrame.this.clearBack();
/*  623: 734 */         NewFrompFrame.this.showAnalyseOptions();
/*  624:     */       }
/*  625: 736 */     });
/*  626: 737 */     this.back_.add(matrixP_);
/*  627: 738 */     invalidate();
/*  628: 739 */     validate();
/*  629: 740 */     repaint();
/*  630:     */   }
/*  631:     */   
/*  632:     */   private void searchPathway()
/*  633:     */   {// Opens the search panel 
/*  634: 743 */     clearBack();
/*  635: 744 */     this.control_.computeSampleScores();
/*  636: 745 */     PwSearchPane pwSearch = new PwSearchPane(Controller.project_, Project.samples_, Project.overall_, getSize());
/*  637: 746 */     this.back_.add(pwSearch);
/*  638: 747 */     invalidate();
/*  639: 748 */     validate();
/*  640: 749 */     repaint();
/*  641:     */   }
/*  642:     */   
/*  643:     */   private void newProjectName()
/*  644:     */   {// Opens the new project panel
/*  645: 753 */     this.back_.removeAll();
/*  646:     */     
/*  647: 755 */     JButton button = new JButton();
/*  648: 756 */     final JTextField txField = new JTextField("Enter Project Name: ");
/*  649:     */     
/*  650: 758 */     JPanel newProjectPanel = new JPanel();
/*  651: 759 */     newProjectPanel.setLayout(null);
/*  652: 760 */     newProjectPanel.setPreferredSize(new Dimension(400, 100));
/*  653: 761 */     newProjectPanel.setVisible(true);
/*  654: 762 */     newProjectPanel.setBackground(this.back_.getBackground());
/*  655:     */     
/*  656: 764 */     this.back_.add("Center", newProjectPanel);
/*  657:     */     
/*  658: 766 */     txField.setBounds(100, 200, 200, 25);
/*  659: 767 */     txField.setVisible(true);
/*  660: 768 */     newProjectPanel.add(txField);
/*  661:     */     
/*  662: 770 */     button.setBounds(325, 200, 75, 25);
/*  663: 771 */     button.setVisible(true);
/*  664: 772 */     button.setText("Ok");
/*  665: 773 */     button.addActionListener(new ActionListener()
/*  666:     */     {
/*  667:     */       public void actionPerformed(ActionEvent e)
/*  668:     */       {
/*  669: 778 */         String tmp = txField.getText();
/*  670: 779 */         System.out.println(tmp);
/*  671: 780 */         NewFrompFrame.this.control_.newProject(tmp);
/*  672: 781 */         NewFrompFrame.this.clearBack();
/*  673: 782 */         NewFrompFrame.this.newEditSamples();
/*  674:     */       }
/*  675: 784 */     });
/*  676: 785 */     newProjectPanel.add(button);
/*  677: 786 */     invalidate();
/*  678: 787 */     validate();
/*  679: 788 */     repaint();
/*  680:     */   }
/*  681:     */   
/*  682:     */   private void showAnalyseOptions()
/*  683:     */   {// Opens the analysis options panel
/*  684: 793 */     int xcol1 = 100;
/*  685: 794 */     int yOff = 100;
/*  686: 795 */     int ySize = 30;
/*  687: 796 */     int xsize = 400;
/*  688:     */     
/*  689: 798 */     JPanel panel = new JPanel();
/*  690: 799 */     panel.setLayout(null);
/*  691: 800 */     panel.setBackground(Project.getBackColor_());
/*  692: 801 */     panel.setBounds(0, 0, 400, 800);
/*  693:     */     
/*  697:     */ 
/*  698: 807 */     JButton s = new JButton("< Back to PathwaySelction");
/*  699: 808 */     s.setBounds(xcol1, yOff - ySize, xsize, ySize);
/*  700: 809 */     s.addActionListener(new ActionListener()
/*  701:     */     {
/*  702:     */       public void actionPerformed(ActionEvent arg0)
/*  703:     */       {
/*  704: 814 */         NewFrompFrame.this.selectPws();
/*  705:     */       }
/*  706: 816 */     });
/*  707: 817 */     panel.add(s);
/*  708:     */     
/*  709: 819 */     JButton save = new JButton("Save Project");
/*  710: 820 */     save.setBounds(xcol1, yOff + 2, 199, ySize);
/*  711: 821 */     save.addActionListener(new ActionListener()
/*  712:     */     {
/*  713:     */       public void actionPerformed(ActionEvent arg0)
/*  714:     */       {
/*  715: 826 */         NewFrompFrame.this.clearBack();
/*  716: 827 */         Controller.loadPathways(true);
/*  717: 828 */         String path = Controller.saveProject();
/*  718: 829 */         System.out.println("Save");
/*  719: 830 */         NewFrompFrame.addRecentPath(path);
/*  720: 831 */         NewFrompFrame.this.saveRecentProj();
//*  721: 832 */         NewFrompFrame.this.clearBack();
/*  722: 833 */         NewFrompFrame.this.showAnalyseOptions();
/*  723:     */       }
/*  724: 835 */     });
/*  725: 836 */     panel.add(save);
/*  726:     */     
/*  727: 838 */     JButton saveAs = new JButton("Save Project As");
/*  728: 839 */     saveAs.setBounds(xcol1 + 202, yOff + 2, 199, ySize);
/*  729: 840 */     saveAs.addActionListener(new ActionListener()
/*  730:     */     {
/*  731:     */       public void actionPerformed(ActionEvent e)
/*  732:     */       {
/*  733: 845 */         NewFrompFrame.this.saveAs();
/*  734: 846 */         NewFrompFrame.this.showAnalyseOptions();
/*  735:     */       }
/*  736: 848 */     });
/*  737: 849 */     panel.add(saveAs);
/*  738:     */     
/*  739:     */ 
/*  740:     */ 
/*  741: 853 */     JLabel label = new JLabel("Analysis Options");
/*  742: 854 */     label.setBounds(xcol1, yOff + (ySize + 2) * 2, xsize, ySize);
/*  743: 855 */     panel.add(label);
/*  744:     */     
/*  745: 857 */     s = new JButton("Pathway Completeness >");
/*  746: 858 */     s.setBounds(xcol1, yOff + (ySize + 2) * 3, xsize, ySize);
/*  747: 859 */     s.addActionListener(new ActionListener()
/*  748:     */     {
/*  749:     */       public void actionPerformed(ActionEvent arg0)
/*  750:     */       {
/*  751: 864 */         if (NewFrompFrame.this.control_.gotSamples())
/*  752:     */         {
/*  753: 865 */           Controller.loadPathways(true);
/*  754: 866 */           NewFrompFrame.this.showPathwayScorePane();
/*  755:     */         } else {
							warningFrame();
						}
/*  756:     */       }
/*  757: 869 */     });
/*  758: 870 */     panel.add(s);
/*  759:     */     
/*  760: 872 */     s = new JButton("Pathway Activity >");
/*  761: 873 */     s.setBounds(xcol1, yOff + (ySize + 2) * 4, xsize, ySize);
/*  762: 874 */     s.addActionListener(new ActionListener()
/*  763:     */     {
/*  764:     */       public void actionPerformed(ActionEvent arg0)
/*  765:     */       {
/*  766: 879 */         if (NewFrompFrame.this.control_.gotSamples())
/*  767:     */         {
/*  768: 880 */           Controller.loadPathways(true);
/*  769: 881 */           NewFrompFrame.this.showPathwayActMatrix();
/*  770:     */         } else {
							warningFrame();
						}
/*  771:     */       }
/*  772: 884 */     });
/*  773: 885 */     panel.add(s);
/*  774:     */     
/*  775: 887 */     s = new JButton("EC Activity >");
/*  776: 888 */     s.setBounds(xcol1, yOff + (ySize + 2) * 5, xsize, ySize);
/*  777: 889 */     s.addActionListener(new ActionListener()
/*  778:     */     {
/*  779:     */       public void actionPerformed(ActionEvent arg0)
/*  780:     */       {
/*  781: 894 */         if (NewFrompFrame.this.control_.gotSamples())
/*  782:     */         {
/*  783: 895 */           Controller.loadPathways(true);
/*  784: 896 */           NewFrompFrame.this.showEcActPanes();
/*  785:     */         } else {
							warningFrame();
						}
/*  786:     */       }
/*  787: 899 */     });
/*  788: 900 */     panel.add(s);
/*  789:     */     
/*  790: 902 */     this.back_.add(panel);
/*  791:     */     
/*  792: 904 */     JButton backButt = new JButton("Back");
/*  793: 905 */     backButt.setBounds(xcol1, yOff + (ySize + 2) * 7, 150, 25);
/*  794: 906 */     backButt.addActionListener(new ActionListener()
/*  795:     */     {
/*  796:     */       public void actionPerformed(ActionEvent arg0)
/*  797:     */       {
/*  798: 912 */         NewFrompFrame.this.newEditSamples();
/*  799:     */       }
/*  800: 916 */     });
/*  801: 917 */     invalidate();
/*  802: 918 */     validate();
/*  803: 919 */     repaint();
/*  804:     */   }
/*  805:     */   
/*  806:     */   private void showNewProjPanel()
/*  807:     */   {
/*  808: 923 */     int xcol1 = 100;
/*  809: 924 */     int yOff = 100;
/*  810: 925 */     int ySize = 30;
/*  811: 926 */     int xsize = 400;
/*  812:     */     
/*  813:     */ 
/*  814:     */ 
/*  815: 930 */     JPanel panel = new JPanel();
/*  816: 931 */     panel.setLayout(null);
/*  817: 932 */     panel.setBackground(Project.getBackColor_());
/*  818: 933 */     panel.setBounds(0, 0, 400, 800);
/*  819:     */     
/*  820: 935 */     JLabel label = new JLabel("Project Options:");
/*  821: 936 */     label.setBounds(xcol1, yOff - 30, xsize, ySize);
/*  822: 937 */     panel.add(label);
/*  823:     */     
/*  824: 939 */     JButton s = new JButton("New Project");
/*  825: 940 */     s.setBounds(xcol1, yOff, xsize, ySize);
/*  826: 941 */     s.addActionListener(new ActionListener()
/*  827:     */     {
/*  828:     */       public void actionPerformed(ActionEvent arg0)
/*  829:     */       {
/*  830: 946 */         NewFrompFrame.this.newProjectName();
/*  831:     */       }
/*  832: 948 */     });
/*  833: 949 */     panel.add(s);
/*  834:     */     
/*  835: 951 */     s = new JButton("Open Project");
/*  836: 952 */     s.setBounds(xcol1, yOff + ySize + 2, xsize, ySize);
/*  837: 953 */     s.addActionListener(new ActionListener()
/*  838:     */     {
/*  839:     */       public void actionPerformed(ActionEvent arg0)
/*  840:     */       {
/*  841: 958 */         NewFrompFrame.this.openProj();
/*  842:     */       }
/*  843: 960 */     });
/*  844: 961 */     panel.add(s);
/*  845:     */     
/*  846:     */ 
/*  847: 964 */     label = new JLabel("Recent Projects");
/*  848: 965 */     label.setBounds(xcol1, yOff + (ySize + 2) * 2, xsize, ySize);
/*  849: 966 */     panel.add(label);
/*  850:     */     
/*  851: 968 */     String path = "";
/*  852: 969 */     for (int i = 0; i < recentProj_.size(); i++)
/*  853:     */     {
/*  854: 970 */       final int index = i;
/*  855: 971 */       path = (String)recentProj_.get(i);
/*  856: 972 */       path = path.substring(path.lastIndexOf(File.separator));
/*  857: 973 */       s = new JButton(path);
/*  858: 974 */       s.setBounds(xcol1, yOff + (ySize + 2) * (i + 3), xsize, ySize);
/*  859: 975 */       s.addActionListener(new ActionListener()
/*  860:     */       {
/*  861:     */         public void actionPerformed(ActionEvent arg0)
/*  862:     */         {
/*  863: 981 */           Project.userPathways = new ArrayList();
/*  864:     */           
/*  865: 983 */           NewFrompFrame.this.control_.loadProjFile((String)NewFrompFrame.recentProj_.get(index));
/*  866: 984 */           NewFrompFrame.this.control_.openProject();
/*  867: 985 */           DataProcessor.newBaseData = true;
/*  868: 986 */           Controller.dataChanged = true;
/*  869:     */           
/*  870: 988 */           NewFrompFrame.this.clearBack();
/*  871: 989 */           NewFrompFrame.this.newEditSamples();
/*  872:     */         }
/*  873: 991 */       });
/*  874: 992 */       panel.add(s);
/*  875:     */     }
/*  876: 994 */     this.back_.add(panel);
/*  877: 995 */     invalidate();
/*  878: 996 */     validate();
/*  879: 997 */     repaint();
/*  880:     */   }
/*  881:     */   
/*  882:     */   private void removeRecentDoubles()
/*  883:     */   {
/*  884:1001 */     if (recentProj_ == null) {
/*  885:1002 */       recentProj_ = new ArrayList();
/*  886:     */     }
/*  887:1004 */     for (int i = 0; i < recentProj_.size(); i++)
/*  888:     */     {
/*  889:1005 */       String comp = (String)recentProj_.get(i);
/*  890:1006 */       for (int j = 0; j < recentProj_.size(); j++) {
/*  891:1007 */         if (i != j) {
/*  892:1011 */           if (((String)recentProj_.get(j)).contentEquals(comp))
/*  893:     */           {
/*  894:1012 */             recentProj_.remove(j);
/*  895:1013 */             j--;
/*  896:     */           }
/*  897:     */         }
/*  898:     */       }
/*  899:     */     }
/*  900:     */   }
/*  901:     */   
/*  902:     */   private void loadRecentProj()
/*  903:     */   {
/*  904:     */     try
/*  905:     */     {
/*  906:1021 */       BufferedReader in = new BufferedReader(new FileReader("recentProj.txt"));
/*  907:1022 */       recentProj_ = new ArrayList();
/*  908:1023 */       String line = "";
/*  909:1024 */       while ((line = in.readLine()) != null) {
/*  910:1025 */         if (!line.isEmpty())
/*  911:     */         {
/*  912:1027 */           recentProj_.add(line);
/*  913:1028 */           System.out.println("load " + line);
/*  914:     */         }
/*  915:     */       }
/*  916:1030 */       in.close();
/*  917:     */     }
/*  918:     */     catch (Exception e)
/*  919:     */     {
/*  920:1033 */       saveRecentProj();
/*  921:1034 */       e.printStackTrace();
/*  922:     */     }
/*  923:     */   }
/*  924:     */   
/*  925:     */   private void saveRecentProj()
/*  926:     */   {
/*  927:1038 */     removeRecentDoubles();
/*  928:     */     try
/*  929:     */     {
/*  930:1040 */       BufferedWriter out = new BufferedWriter(new FileWriter("recentProj.txt"));
/*  931:1041 */       for (int i = 0; i < recentProj_.size(); i++)
/*  932:     */       {
/*  933:1042 */         out.write((String)recentProj_.get(i));
/*  934:1043 */         System.out.println("Save " + (String)recentProj_.get(i));
/*  935:1044 */         out.newLine();
/*  936:     */       }
/*  937:1046 */       out.close();
/*  938:     */     }
/*  939:     */     catch (IOException e)
/*  940:     */     {
/*  941:1050 */       e.printStackTrace();
/*  942:     */     }
/*  943:     */   }
/*  944:     */   
/*  945:     */   public static void addRecentPath(String path)
/*  946:     */   {
/*  947:1054 */     if (recentProj_.size() < 10)
/*  948:     */     {
/*  949:1055 */       recentProj_.add(0, path);
/*  950:     */     }
/*  951:     */     else
/*  952:     */     {
/*  953:1058 */       recentProj_.remove(recentProj_.size() - 1);
/*  954:1059 */       recentProj_.add(0, path);
/*  955:     */     }
/*  956:     */   }
/*  957:     */   
/*  958:     */   private void openProj()
/*  959:     */   {
//*  960:1063 */     clearBack();
/*  961:1064 */     JFileChooser fChoose_ = new JFileChooser(this.path_ + File.separator + "projects");
/*  962:1065 */     fChoose_.setFileSelectionMode(0);
/*  963:1066 */     fChoose_.setBounds(100, 100, 200, 20);
/*  964:1067 */     fChoose_.setVisible(true);
/*  965:1068 */     fChoose_.setFileFilter(new FileFilter()
/*  966:     */     {
/*  967:     */       public boolean accept(File f)
/*  968:     */       {
/*  969:1072 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".prj")) || (f.getName().toLowerCase().endsWith(".frp"))) {
/*  970:1073 */           return true;
/*  971:     */         }
/*  972:1075 */         return false;
/*  973:     */       }
/*  974:     */       
/*  975:     */       public String getDescription()
/*  976:     */       {
/*  977:1081 */         return ".prj / .frp";
/*  978:     */       }
/*  979:     */     });
/*  980:1085 */     if (fChoose_.showOpenDialog(getParent()) == 0) {
/*  981:     */       try
/*  982:     */       {
/*  983:1087 */         int erg = this.control_.loadProjFile(fChoose_.getSelectedFile().getCanonicalPath());
/*  984:1088 */         addRecentPath(fChoose_.getSelectedFile().getCanonicalPath());
/*  985:1089 */         if (erg == 1)
/*  986:     */         {
						        Project.userPathways = new ArrayList();

                          System.out.println("OPEN PROJECT 1");

						        NewFrompFrame.this.control_.loadProjFile(fChoose_.getSelectedFile().getCanonicalPath());
/*  987:1091 */           NewFrompFrame.this.control_.openProject();
/*  988:1092 */           
/*  989:1093 */           DataProcessor.newBaseData = true;
/*  990:1094 */           Controller.dataChanged = true;
                          NewFrompFrame.this.clearBack();
                          NewFrompFrame.this.newEditSamples();
/*  991:     */         }
/*  992:     */         else
/*  993:     */         {
/*  994:1098 */           if (erg == 2)
/*  995:     */           {
						        Project.userPathways = new ArrayList();

                          System.out.println("OPEN PROJECT 2");

						        NewFrompFrame.this.control_.loadProjFile(fChoose_.getSelectedFile().getCanonicalPath());
/*  987:1091 */           NewFrompFrame.this.control_.openProject();
/*  988:1092 */           
/*  989:1093 */           DataProcessor.newBaseData = true;
/*  990:1094 */           Controller.dataChanged = true;
                          NewFrompFrame.this.clearBack();
                          NewFrompFrame.this.newEditSamples();
/*  991:     */           }
/* 1001:1104 */           if (erg == -1)
/* 1002:     */           {
                            System.out.println("OPEN PROJECT -1");
/* 1003:1106 */             System.out.println("-1");
/* 1004:1107 */             return;
/* 1005:     */           }
/* 1006:1109 */           if (erg == 0) {
                            System.out.println("OPEN PROJECT O");
/* 1007:1110 */             return;
/* 1008:     */           }
/* 1009:     */         }
/* 1010:     */       }
/* 1011:     */       catch (IOException e1)
/* 1012:     */       {
/* 1013:1115 */         e1.printStackTrace();
/* 1014:     */         
/* 1015:1117 */         saveRecentProj();
/* 1016:1118 */         clearBack();
/* 1017:1119 */         newEditSamples();
/* 1018:     */       }
/* 1019:     */     }
/* 1020:1122 */     System.out.println("Open");
//**/                  clearBack();
/* 1021:     */   }
/* 1022:     */   
/* 1023:     */   public void saveAs()
/* 1024:     */   {
//* 1025:1125 */     clearBack();
/* 1026:1126 */     Controller.loadPathways(true);
/* 1027:1127 */     JFileChooser fChoose_ = new JFileChooser(this.path_ + File.separator + "projects");
/* 1028:1128 */     fChoose_.setFileSelectionMode(0);
/* 1029:1129 */     fChoose_.setBounds(100, 100, 200, 20);
/* 1030:1130 */     fChoose_.setVisible(true);
/* 1031:1131 */     File file = new File(this.path_ + File.separator + "projects");
/* 1032:1132 */     fChoose_.setSelectedFile(file);
/* 1033:1133 */     fChoose_.setFileFilter(new FileFilter()
/* 1034:     */     {
/* 1035:     */       public boolean accept(File f)
/* 1036:     */       {
/* 1037:1137 */         if ((f.isDirectory()) || (f.getName().toLowerCase().endsWith(".frp"))) {
/* 1038:1138 */           return true;
/* 1039:     */         }
/* 1040:1140 */         return false;
/* 1041:     */       }
/* 1042:     */       
/* 1043:     */       public String getDescription()
/* 1044:     */       {
/* 1045:1146 */         return ".frp";
/* 1046:     */       }
/* 1047:     */     });
/* 1048:1150 */     if (fChoose_.showSaveDialog(getParent()) == 0)
/* 1049:     */     {
/* 1050:     */       try
/* 1051:     */       {
/* 1052:1152 */         String path = fChoose_.getSelectedFile().getCanonicalPath();
/* 1053:1154 */         if (!path.endsWith(".frp"))
/* 1054:     */         {
/* 1055:1155 */           path = path + ".frp";
/* 1056:1156 */           System.out.println(".frp");
/* 1057:     */         }
/* 1058:1158 */         Controller.project_.exportProj(path);
/* 1059:1159 */         addRecentPath(path);
/* 1060:1160 */         saveRecentProj();
/* 1061:     */       }
/* 1062:     */       catch (IOException e1)
/* 1063:     */       {
/* 1064:1164 */         e1.printStackTrace();
/* 1065:     */       }
//* 1066:1167 */       clearBack();
/* 1067:1168 */       invalidate();
/* 1068:1169 */       validate();
/* 1069:1170 */       repaint();
/* 1070:     */     }
/* 1071:1172 */     System.out.println("Save");
//* 1072:1173 */     clearBack();
/* 1073:     */   }
/* 1074:     */   
/* 1075:     */   public void openPDf(String path)
/* 1076:     */   {
/* 1077:     */     try
/* 1078:     */     {
/* 1079:1178 */       File pdfFile = new File(path);
/* 1080:1179 */       if (pdfFile.exists())
/* 1081:     */       {
/* 1082:1181 */         if (Desktop.isDesktopSupported()) {
/* 1083:1182 */           Desktop.getDesktop().open(pdfFile);
/* 1084:     */         } else {
/* 1085:1184 */           System.out.println("Awt Desktop is not supported!");
/* 1086:     */         }
/* 1087:     */       }
/* 1088:     */       else {
/* 1089:1188 */         System.out.println("File does not exist!");
/* 1090:     */       }
/* 1091:1191 */       System.out.println("Done");
/* 1092:     */     }
/* 1093:     */     catch (Exception ex)
/* 1094:     */     {
/* 1095:1194 */       ex.printStackTrace();
/* 1096:     */     }
/* 1097:     */   }
/* 1098:     */   
/* 1099:     */   private void openClosingFrame()
/* 1100:     */   {
/* 1101:1198 */     if (Project.samples_.size() == 0)
/* 1102:     */     {
/* 1103:1199 */       System.exit(0);
/* 1104:     */     }
/* 1105:     */     else
/* 1106:     */     {
/* 1107:1202 */       final JFrame frame = new JFrame("Warning!");
/* 1108:1203 */       frame.setBounds(200, 200, 350, 300);
/* 1109:1204 */       frame.setLayout(null);
/* 1110:1205 */       frame.setVisible(true);
/* 1111:     */       
/* 1112:1207 */       JPanel backP = new JPanel();
/* 1113:1208 */       backP.setBounds(0, 0, 350, 300);
/* 1114:1209 */       backP.setLayout(null);
/* 1115:1210 */       frame.add(backP);
/* 1116:     */       
/* 1117:1212 */       JLabel label = new JLabel("Warning all unsaved progress will be lost!");
/* 1118:1213 */       label.setBounds(25, 100, 300, 25);
/* 1119:1214 */       backP.add(label);
/* 1120:     */       
/* 1121:1216 */       label = new JLabel("Exit anyway?!");
/* 1122:1217 */       label.setBounds(125, 125, 300, 25);
/* 1123:1218 */       backP.add(label);
/* 1124:     */       
/* 1125:1220 */       JButton exit = new JButton("Exit");
/* 1126:1221 */       exit.setBounds(55, 200, 100, 30);
/* 1127:1222 */       exit.addActionListener(new ActionListener()
/* 1128:     */       {
/* 1129:     */         public void actionPerformed(ActionEvent arg0)
/* 1130:     */         {
/* 1131:1227 */           System.exit(0);
/* 1132:     */         }
/* 1133:1229 */       });
/* 1134:1230 */       backP.add(exit);
/* 1135:     */       
/* 1136:1232 */       JButton cancel = new JButton("Cancel");
/* 1137:1233 */       cancel.setBounds(180, 200, 100, 30);
/* 1138:1234 */       cancel.addActionListener(new ActionListener()
/* 1139:     */       {
/* 1140:     */         public void actionPerformed(ActionEvent e)
/* 1141:     */         {
/* 1142:1239 */           frame.removeAll();
/* 1143:1240 */           frame.dispose();
/* 1144:     */         }
/* 1145:1242 */       });
/* 1146:1243 */       backP.add(cancel);
/* 1147:     */     }
/* 1148:     */   }
/* 1149:     */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Prog.NewFrompFrame

 * JD-Core Version:    0.7.0.1

 */