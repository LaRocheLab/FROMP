/*   1:    */ package Prog;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.event.ActionEvent;
/*   5:    */ import java.awt.event.ActionListener;
/*   6:    */ import java.io.BufferedWriter;
/*   7:    */ import java.io.File;
/*   8:    */ import java.io.FileWriter;
/*   9:    */ import java.io.IOException;
/*  10:    */ import java.io.PrintStream;
/*  11:    */ import javax.swing.JButton;
/*  12:    */ import javax.swing.JFrame;
/*  13:    */ import javax.swing.JLabel;
/*  14:    */ import javax.swing.JPanel;
/*  15:    */ import pathwayLayout.PathLayoutGrid;
/*  16:    */ 
/*  17:    */ public class StartFromp
/*  18:    */ {
/*  19:    */   static NewFrompFrame newFrame;
/*  20:    */   static JFrame frame2;
				static String arg1="";
/*  21:    */   
/*  22:    */   public static void main(String[] args)
/*  23:    */   {
/*  24: 30 */     if (args != null)
/*  25:    */     {
/*  26: 31 */       if (args.length != 0)
/*  27:    */       {
/*  28: 32 */         String[] arrayOfString = args;int j = args.length;
/*  29: 32 */         for (int i = 0; i < j; i++)
/*  30:    */         {
/*  31: 32 */           String s = arrayOfString[i];
/*  32: 33 */           System.out.println(s);
/*  33:    */         }
/*  34: 35 */         if (args.length == 1)
/*  35:    */         {
/*  36: 36 */           if (args[0].contentEquals("h"))
/*  37:    */           {
						  arg1="h";
/*  38: 37 */             printOptions();
/*  39:    */           }
/*  40:    */           else
/*  41:    */           {
/*  42:    */             PathLayoutGrid Grid;
/*  43: 39 */             if (args[0].contentEquals("d")) {
/*  44: 41 */               Grid = new PathLayoutGrid(10, 10, true);
/*  45:    */             } else {
/*  46: 44 */               printOptions();
/*  47:    */             }
/*  48:    */           }
/*  49:    */         }
/*  50: 47 */         else if (args.length == 3)
/*  51:    */         {
/*  52: 49 */           if ((checkPath(args[0])) && (checkPath(args[1])))
/*  53:    */           {
/*  54:    */             CmdController cmd;
/*  55: 50 */             if (checkOptions(args[2]))
/*  56:    */             {
/*  57: 52 */               cmd = new CmdController(args);
/*  58:    */             }
/*  59:    */             else
/*  60:    */             {
/*  61: 55 */               System.out.println("Wrong option input");
/*  62: 56 */               System.out.println("option: " + args[2]);
/*  63: 57 */               printOptions();
/*  64:    */             }
/*  65:    */           }
						else if((checkPath(args[0])) && (!checkPath(args[1]))){
							if(args[1].endsWith("/")){
								File dir=new File(args[1]);
								dir.mkdir();
								CmdController cmd;
/*  55: 50 */           	 	if (checkOptions(args[2]) && (checkPath(args[1])))
/*  56:    */             		{
/*  57: 52 */               		cmd = new CmdController(args);
/*  58:    */             		}
/*  59:    */             		else
/*  60:    */             		{
/*  61: 55 */              	 		System.out.println("Wrong option input");
/*  62: 56 */               		System.out.println("option: " + args[2]);
/*  63: 57 */               		printOptions();
/*  64:    */           		}
							}
							else{
								System.out.println("1");
								System.out.println("Wrong input");
/*  69: 62 */             		System.out.println("check input/output-path");
/*  70: 63 */             		System.out.println("inputPath: " + args[0]);
/*  71: 64 */             		System.out.println("outputPath: " + args[1]);
/*  72: 65 */             		printOptions();
							}						
						}
/*  66:    */           else
/*  67:    */           {
						  System.out.println("2");
/*  68: 61 */             System.out.println("Wrong input");
/*  69: 62 */             System.out.println("check input/output-path");
/*  70: 63 */             System.out.println("inputPath: " + args[0]);
/*  71: 64 */             System.out.println("outputPath: " + args[1]);
/*  72: 65 */             printOptions();
/*  73:    */           }
/*  74:    */         }
					  else if(args.length == 4){
					  	if ((checkPath(args[0])) && (checkPath(args[1])))
/*  53:    */           {
/*  54:    */             CmdController cmd;
/*  55: 50 */             if (checkOptions(args[2])&&checkEC(args[3]))
/*  56:    */             {
/*  57: 52 */               cmd = new CmdController(args);
/*  58:    */             }
/*  59:    */             else
/*  60:    */             {
/*  61: 55 */               System.out.println("Wrong option or ec input");
/*  62: 56 */               System.out.println("option: " + args[2]);
							System.out.println("ec: " + args[3]);
/*  63: 57 */               printOptions();
/*  64:    */             }
/*  65:    */           }
						else if((checkPath(args[0])) && (!checkPath(args[1]))){
							if(args[1].endsWith("/")){
								File dir=new File(args[1]);
								dir.mkdir();
								CmdController cmd;
/*  55: 50 */           	 	if (checkOptions(args[2]) && (checkPath(args[1])))
/*  56:    */             		{
/*  57: 52 */               		cmd = new CmdController(args);
/*  58:    */             		}
/*  59:    */             		else
/*  60:    */             		{
/*  61: 55 */              	 		System.out.println("Wrong option input");
/*  62: 56 */               		System.out.println("option: " + args[2]);
/*  63: 57 */               		printOptions();
/*  64:    */           		}
							}
							else{
								System.out.println("1");
								System.out.println("Wrong input");
/*  69: 62 */             		System.out.println("check input/output-path");
/*  70: 63 */             		System.out.println("inputPath: " + args[0]);
/*  71: 64 */             		System.out.println("outputPath: " + args[1]);
/*  72: 65 */             		printOptions();
							}						
						}
/*  66:    */           else
/*  67:    */           {
						  System.out.println("2");
/*  68: 61 */             System.out.println("Wrong input");
/*  69: 62 */             System.out.println("check input/output-path");
/*  70: 63 */             System.out.println("inputPath: " + args[0]);
/*  71: 64 */             System.out.println("outputPath: " + args[1]);
/*  72: 65 */             printOptions();
/*  73:    */           }
					  }
/*  75:    */         else {
/*  76: 69 */           printOptions();
/*  77:    */         }
/*  78:    */       }
/*  79:    */       else
/*  80:    */       {
/*  81: 73 */         System.out.println("no args");
/*  82: 74 */         run();
/*  83:    */       }
/*  84:    */     }
/*  85:    */     else
/*  86:    */     {
/*  87: 78 */       System.out.println("null args");
/*  88: 79 */       run();
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static void runBat()
/*  93:    */   {
/*  94:    */     try
/*  95:    */     {
/*  96: 85 */       BufferedWriter out = new BufferedWriter(new FileWriter("FROMP.bat"));
/*  97: 86 */       out.write("java -Xms128m -Xmx256m -jar FROMP.jar start");
/*  98: 87 */       out.close();
/*  99: 88 */       System.out.println();
/* 100: 89 */       frame2 = new JFrame("Warning");
/* 101: 90 */       frame2.setLayout(null);
/* 102: 91 */       frame2.setBounds(100, 100, 300, 300);
/* 103: 92 */       frame2.setResizable(false);
/* 104: 93 */       frame2.setDefaultCloseOperation(3);
/* 105: 94 */       frame2.setVisible(true);
/* 106:    */       
/* 107: 96 */       JPanel back = new JPanel();
/* 108: 97 */       back.setLayout(null);
/* 109: 98 */       back.setBounds(0, 0, frame2.getWidth(), frame2.getHeight());
/* 110: 99 */       back.setBackground(new Color(233, 233, 233));
/* 111:    */       
/* 112:101 */       JLabel label = new JLabel("<html>FROMP uses quite a lot of memory. <br>To run properly,<br> please restart the Program with FROMP.bat.<br>If you choose to continue it may crash</html>");
/* 113:102 */       label.setLayout(null);
/* 114:103 */       label.setForeground(Color.black);
/* 115:104 */       label.setBounds(5, -10, frame2.getWidth(), 100);
/* 116:105 */       label.setVisible(true);
/* 117:106 */       back.add(label);
/* 118:    */       
/* 119:108 */       JButton button1 = new JButton("I want to start anyways");
/* 120:    */       
/* 121:110 */       button1.setBounds(0, 80, frame2.getWidth(), 100);
/* 122:111 */       button1.setLayout(null);
/* 123:112 */       button1.setVisible(true);
/* 124:113 */       button1.addActionListener(new ActionListener()
/* 125:    */       {
/* 126:    */         public void actionPerformed(ActionEvent arg0)
/* 127:    */         {
/* 128:118 */           StartFromp.run();
/* 129:119 */           StartFromp.remove();
/* 130:    */         }
/* 131:122 */       });
/* 132:123 */       back.add(button1);
/* 133:124 */       JButton button2 = new JButton("Ok, I'll use FROMP.bat");
/* 134:    */       
/* 135:126 */       button2.setBounds(0, 180, frame2.getWidth(), 100);
/* 136:127 */       button2.setLayout(null);
/* 137:128 */       button2.setVisible(true);
/* 138:129 */       button2.addActionListener(new ActionListener()
/* 139:    */       {
/* 140:    */         public void actionPerformed(ActionEvent arg0)
/* 141:    */         {
/* 142:134 */           System.exit(0);
/* 143:    */         }
/* 144:137 */       });
/* 145:138 */       back.add(button2);
/* 146:139 */       frame2.add(back);
/* 147:140 */       frame2.repaint();
/* 148:    */     }
/* 149:    */     catch (IOException e)
/* 150:    */     {
/* 151:144 */       System.out.println("Exception ");
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public static void remove()
/* 156:    */   {
/* 157:149 */     frame2.removeAll();
/* 158:150 */     frame2.dispose();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static void run()
/* 162:    */   {
/* 163:155 */     newFrame = new NewFrompFrame();
/* 164:    */   }
/* 165:    */   
/* 166:    */   private static boolean checkPath(String path)
/* 167:    */   {
/* 168:159 */     File f = new File(path);
/* 169:160 */     return f.exists();
/* 170:    */   }
/* 171:    */   
/* 172:    */   private static boolean checkOptions(String options)
/* 173:    */   {
/* 174:163 */     boolean ret = false;
/* 175:164 */     if (options.contentEquals("p")) {
/* 176:165 */       ret = true;
/* 177:    */     }
/* 178:167 */     if (options.contentEquals("s")) {
/* 179:168 */       ret = true;
/* 180:    */     }
/* 181:170 */     if (options.contentEquals("m")) {
/* 182:171 */       ret = true;
/* 183:    */     }
/* 184:173 */     if (options.contentEquals("e")) {
/* 185:174 */       ret = true;
/* 186:    */     }
/* 187:176 */     if (options.contentEquals("a")) {
/* 188:177 */       ret = true;
/* 189:    */     }
				  if (options.contentEquals("am")) {
                    ret = true;
			      }
/* 190:179 */     if (options.contentEquals("op")) {
/* 191:180 */       ret = true;
/* 192:    */     }
/* 193:182 */     if (options.contentEquals("up")) {
/* 194:183 */       ret = true;
/* 195:    */     }
/* 196:185 */     return ret;
/* 197:    */   }

				private static boolean checkEC(String options)
/* 173:    */   {
/* 174:163 */     boolean ret = false;
				  String testStr1;
				  String testStr2;
				  String testStr3;

/* 175:164 */     if (options.matches("[0-9].*")) {
/* 194:183 */       if(options.contains(".")){
						testStr1=options.substring(options.indexOf(".")+1);
						if(testStr1.matches("[0-9].*")){
							if(testStr1.contains(".")){
								testStr2=testStr1.substring(options.indexOf(".")+1);
								if(testStr2.matches("[0-9].*")){
									if(testStr2.contains(".")){
										testStr3=testStr2.substring(options.indexOf(".")+1);
										if(testStr3.matches("[0-9].*")){
											ret=true;
										}
									}
								}
							}
						}
					}
/* 195:    */     }
/* 196:185 */     return ret;
/* 197:    */   }

/* 198:    */   
/* 199:    */   private static void printOptions()
/* 200:    */   {
				  if(arg1!="h"){
/* 201:188 */     	System.out.println("The arguements used are invalid!!");
/* 202:189 */     	System.out.println("The correct arguements are:");
				  }
/* 203:190 */     System.out.println("../java -jar FROMP.jar h for help");
/* 204:191 */     System.out.println("../java -jar FROMP.jar d for the pathwaydesigner");
/* 205:192 */     System.out.println("");
/* 206:193 */     System.out.println("../java -jar FROMP.jar 'inputPath' 'outputPath' 'option'");
/* 207:194 */     System.out.println("Options are:");
/* 208:195 */     System.out.println("'P' for the pathway pictures");
/* 209:196 */     System.out.println("'s' for the pathway-score-matrix");
/* 210:197 */     System.out.println("'m' for the pathway-activity-matrix");
/* 211:198 */     System.out.println("'e' for the EC-activity-matrix");
/* 212:199 */     System.out.println("'a' all options");
				  System.out.println("'am' for pathway-score-matrix, pathway-activity-matrix, and EC-activity-matrix");
/* 213:200 */     System.out.println("'op' only multisample pictures");
/* 214:201 */     System.out.println("'up' only userpathway multisample pictures");
				  System.out.println("");
				  System.out.println("To include sequence IDs after your option add the ec number of that pathway who's ");
				  System.out.println("of that pathway who's sequence ID you are interested in");
				  System.out.println("");
/* 215:202 */     System.out.println("");
/* 216:203 */     System.out.println("blanks in the input-path or output-path will lead to errors");
/* 217:204 */     System.out.println("to solve use braces: (\"inputPath\")");
/* 218:    */   }
/* 219:    */ }
