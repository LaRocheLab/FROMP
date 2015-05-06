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
			/**
			 * This is the main method for FROMP. Its is what starts FROMP, and what decides whether or not FROMP will
			 * be working in a gui or on the command line depending upon the arguments the user includes.
			 * 
			 * @author Jennifer Terpstra, Kevan Lynch
			 */
/*  17:    */ public class StartFromp
/*  18:    */ {
/*  19:    */   static NewFrompFrame newFrame;
				static String arg1="";
/*  21:    */  /**
 				* Takes in a string array of arguments from the user which determines what the program will do. 
 				* If there are no arguments, GUI Fromp starts. If there is one argument and that argument is h then the print options command is called. 
 				* If there are 3 arguments and the third is a known command then the first argument is checked then taken as the input path, 
 				* the second as the output path, and the third as the command to be done, cmdController is called. If there are four arguments, 
 				* the program ensures the fourth is a proper EC then calls cmdController the same as before, but this time with an extra argument.
 				* otherwise the print options command is called.
 				* 
 				* @param args User input which depending on the input changes how FROMP works
 				* @author Jennifer Terpstra, Kevan Lynch
 				*/
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
/*  40:    */           else if (args[0].contentEquals("d"))
/*  41:    */           {
/*  42:    */             PathLayoutGrid Grid = new PathLayoutGrid(10, 10, true);
/*  45:    */             
/*  48:    */           }
						else {
/*  46: 44 */             printOptions();
/*  47:    */           }
/*  49:    */         }
					  else if (args.length == 2){
					  	if (checkPath(args[0])){
							if(checkEC(args[1])){
								CmdController cmd = new CmdController(args);
							}  else{
					 			System.out.println("1");
								System.out.println("Wrong input");
/*  69: 62 */             		System.out.println("check input/output-path");
/*  70: 63 */             		System.out.println("inputPath: " + args[0]);
/*  71: 64 */             		System.out.println("outputPath: " + args[1]);
/*  72: 65 */             		printOptions();
					 		}
					 	}
					  }
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
						else if((checkPath(args[0])) && (checkEC(args[1]) && checkEC(args[2]))){
							CmdController cmd = new CmdController(args);
						}
						else if((checkPath(args[0])) && (!checkPath(args[1])) && (!checkEC(args[1]))){
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
							} else if (args[1].contentEquals("seq")&&checkEC(args[2])){
								CmdController cmd;
								cmd = new CmdController(args);
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
							CmdController cmd;
                     if(args[1].endsWith("/")){
								File dir=new File(args[1]);
								dir.mkdir();
								if (checkOptions(args[2]) && (checkPath(args[1])))
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
							else if(checkEC(args[1])&&checkEC(args[2])&&checkEC(args[3])){
								cmd = new CmdController(args);
							}
							else if(args[1].contentEquals("seq")&&checkEC(args[2])&&checkEC(args[3])){
								cmd = new CmdController(args);
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
					  else if(args.length > 4){
					  	if ((checkPath(args[0])) && (checkPath(args[1])))
/*  53:    */           {
/*  54:    */             CmdController cmd;
/*  55: 50 */             if (checkOptions(args[2]))
/*  56:    */             {
							boolean allECs=true;
							for(int i=3;i<args.length;i++){
								if(!checkEC(args[i])){
									allECs=false;
									break;
								}
							}
							if(allECs){
/*  57: 52 */               	cmd = new CmdController(args);
							}
							else{
								System.out.println("Wrong option or ec input");
/*  62: 56 */               	System.out.println("option: " + args[2]);
								System.out.println("ec: " + args[3]);
/*  63: 57 */               	printOptions();
							}
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
							if(checkEC(args[1])){
								CmdController cmd;
								boolean allECs=true;
								for(int i=3;i<args.length;i++){
									if(!checkEC(args[i])){
										allECs=false;
									}
								}
								if(allECs){
/*  57: 52 */               		cmd = new CmdController(args);
								}
								else{
/*  68: 61 */           		  System.out.println("Wrong input");
/*  69: 62 */           		  System.out.println("check input/output-path");
/*  70: 63 */           		  System.out.println("inputPath: " + args[0]);
/*  71: 64 */           		  System.out.println("outputPath: " + args[1]);
								  printOptions();
								}
							} else if(args[1].contentEquals("seq")){
								CmdController cmd;
								boolean allECs=true;
								for(int i=2;i<args.length;i++){
									if(!checkEC(args[i])){
										allECs=false;
									}
								}
								if(allECs){
/*  57: 52 */               		cmd = new CmdController(args);
								}
								else{
/*  68: 61 */           		  System.out.println("Wrong input");
/*  69: 62 */           		  System.out.println("check input/output-path");
/*  70: 63 */           		  System.out.println("inputPath: " + args[0]);
/*  71: 64 */           		  System.out.println("outputPath: " + args[1]);
								  printOptions();
								}
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
/* 160:    */   
/* 161:    */   public static void run()
/* 162:    */   { // runs gui fromp
/* 163:155 */     newFrame = new NewFrompFrame();
/* 164:    */   }
/* 165:    */   
/* 166:    */   private static boolean checkPath(String path)
/* 167:    */   { // checks that the file given exists
/* 168:159 */     File f = new File(path);
/* 169:160 */     return f.exists();
/* 170:    */   }
/* 171:    */   
/* 172:    */   private static boolean checkOptions(String options)
/* 173:    */   { // ensures that the option the user has selected is allowed
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
				  if (options.contentEquals("f")) {
/* 188:177 */       ret = true;
/* 189:    */     }
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
				  if (options.contentEquals("ec")){
				  	ret = true;	
				  }
				  if (options.contentEquals("seq")){
				  	ret = true;	
				  }
/* 196:185 */     return ret;
/* 197:    */   }

				private static boolean checkEC(String options)
/* 173:    */   {//checks that the EC is complete, ie is an ec number
/* 174:163 */     boolean ret = false;
				  String testStr1;
				  String testStr2;
				  String testStr3;

/* 175:164 */     if (options.matches("[0-9].*")) {
/* 194:183 */       if(options.contains(".")){
						testStr1=options.substring(options.indexOf(".")+1);
						if(testStr1.matches("[0-9].*")){
							if(testStr1.contains(".")){
								testStr2=testStr1.substring(testStr1.indexOf(".")+1);
								if(testStr2.matches("[0-9].*")){
									if(testStr2.contains(".")){
										testStr3=testStr2.substring(testStr2.indexOf(".")+1);
										if(testStr3.matches("[0-9]*")){
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
/* 200:    */   { // Prints out the options for this program to the cmdline
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
				  System.out.println("'f' to export the project as a .frp file");
				  System.out.println("Exported .frp files will be saved in ~/projects");
				  System.out.println("");
				  System.out.println("'ec' to export a list of sequence IDs from a file of ec numbers from your input file");
				  System.out.println("To include sequence IDs without a file of ec numbers, add the ec numbers");
				  System.out.println("who's sequence ID you are interested in to your command");
				  System.out.println("Sequence ID's will be stored in ~/RepSeqIDs");
				  System.out.println("");
				  System.out.println("'seq' to export a file of sequences from a .frp file which has the sequence files related to the samples");
				  System.out.println("Sequences will be stored in ~/Sequences");			
  				  System.out.println("Syntax: java -jar FROMP.jar 'inputPath' seq");
				  System.out.println("");
/* 212:199 */     System.out.println("'a' all options");
				  System.out.println("'am' for pathway-score-matrix, pathway-activity-matrix, EC-activity-matrix, and to export as a .frp file");
/* 213:200 */     System.out.println("'op' only multisample pictures");
/* 214:201 */     System.out.println("'up' only userpathway multisample pictures");
				  System.out.println("");
/* 215:202 */     System.out.println("");
/* 216:203 */     System.out.println("blanks in the input-path or output-path will lead to errors");
/* 217:204 */     System.out.println("to solve use braces: (\"inputPath\")");
/* 218:    */   }
/* 219:    */ }
