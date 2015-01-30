/*   1:    */ package pathwayLayout;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Container;
/*   6:    */ import java.awt.Graphics;
/*   7:    */ import java.awt.Graphics2D;
/*   8:    */ import java.awt.Image;
/*   9:    */ import java.awt.event.ActionEvent;
/*  10:    */ import java.awt.event.ActionListener;
/*  11:    */ import java.awt.event.MouseEvent;
/*  12:    */ import java.awt.event.MouseListener;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.net.URL;
/*  15:    */ import java.util.ArrayList;
/*  16:    */ import javax.swing.ImageIcon;
/*  17:    */ import javax.swing.JButton;
/*  18:    */ import javax.swing.JPanel;
/*  19:    */ import javax.swing.JTextField;
/*  20:    */ 
/*  21:    */ public class LayoutPane
/*  22:    */   extends JPanel
/*  23:    */ {
/*  24:    */   private static final long serialVersionUID = 1L;
/*  25:    */   PathLayoutGrid grid;
/*  26:    */   int xStepSize;
/*  27:    */   int yStepSize;
/*  28:    */   JPanel back;
/*  29:    */   ArrayList<Node> nodes_;
/*  30: 37 */   Node activeNode = null;
/*  31: 39 */   boolean drawGrid = true;
/*  32: 40 */   double buttonfac = 0.8D;
/*  33: 42 */   boolean fieldOpen = false;
/*  34: 43 */   boolean removeNode = false;
/*  35: 44 */   boolean setComment = false;
/*  36:    */   public static JTextField nameField;
/*  37:    */   
/*  38:    */   public LayoutPane(int xSize, int ySize, PathLayoutGrid Grid, ArrayList<Node> nodes)
/*  39:    */   {
/*  40: 52 */     nameField = new JTextField("Enter Pathname");
/*  41:    */     
/*  42: 54 */     this.grid = Grid;
/*  43: 55 */     this.nodes_ = nodes;
/*  44:    */     
/*  45: 57 */     setVisible(true);
/*  46: 58 */     setLayout(null);
/*  47: 59 */     setLocation(0, 0);
/*  48: 60 */     setBounds(0, 0, xSize, ySize);
/*  49: 61 */     setBackground(Color.lightGray);
/*  50:    */     
/*  51: 63 */     addMouseListener(new MouseListener()
/*  52:    */     {
/*  53:    */       public void mouseReleased(MouseEvent arg0) {}
/*  54:    */       
/*  55:    */       public void mousePressed(MouseEvent arg0) {}
/*  56:    */       
/*  57:    */       public void mouseExited(MouseEvent arg0) {}
/*  58:    */       
/*  59:    */       public void mouseEntered(MouseEvent arg0) {}
/*  60:    */       
/*  61:    */       public void mouseClicked(MouseEvent arg0)
/*  62:    */       {
/*  63: 92 */         final int x = arg0.getX();
/*  64: 93 */         final int y = arg0.getY();
/*  65: 94 */         if (LayoutPane.this.activeNode != null)
/*  66:    */         {
/*  67: 96 */           LayoutPane.this.activeNode.xPos = (x / LayoutPane.this.xStepSize - 1);
/*  68: 97 */           LayoutPane.this.activeNode.yPos = (y / LayoutPane.this.yStepSize - 1);
/*  69: 99 */           if (LayoutPane.this.activeNode.xPos < 0) {
/*  70:100 */             LayoutPane.this.activeNode.xPos = 0;
/*  71:    */           }
/*  72:102 */           if (LayoutPane.this.activeNode.yPos < 0) {
/*  73:103 */             LayoutPane.this.activeNode.yPos = 0;
/*  74:    */           }
/*  75:106 */           LayoutPane.this.adaptChildren(LayoutPane.this.activeNode);
/*  76:107 */           LayoutPane.this.activeNode = null;
/*  77:108 */           LayoutPane.this.reDo();
/*  78:    */         }
/*  79:111 */         else if (!LayoutPane.this.fieldOpen)
/*  80:    */         {
/*  81:112 */           LayoutPane.this.fieldOpen = true;
/*  82:113 */           final JTextField field = new JTextField("Name");
/*  83:114 */           field.setBounds(x, y, 100, 20);
/*  84:115 */           field.addActionListener(new ActionListener()
/*  85:    */           {
/*  86:    */             public void actionPerformed(ActionEvent arg0)
/*  87:    */             {
/*  88:120 */               System.out.println("actionPerformed");
/*  89:121 */               LayoutPane.this.fieldOpen = false;
/*  90:122 */               if (!field.getText().isEmpty())
/*  91:    */               {
/*  92:123 */                 Node node = new Node(field.getText());
/*  93:124 */                 node.xPos = (x / LayoutPane.this.xStepSize - 1);
/*  94:125 */                 node.yPos = (y / LayoutPane.this.yStepSize - 1);
/*  95:126 */                 if (node.xPos < 0) {
/*  96:127 */                   node.xPos = 0;
/*  97:    */                 }
/*  98:129 */                 if (node.yPos < 0) {
/*  99:130 */                   node.yPos = 0;
/* 100:    */                 }
/* 101:132 */                 if (LayoutPane.this.setComment)
/* 102:    */                 {
/* 103:133 */                   node.comment = LayoutPane.this.setComment;
/* 104:134 */                   LayoutPane.this.setComment = false;
/* 105:    */                 }
/* 106:136 */                 LayoutPane.this.grid.addNode(node);
/* 107:137 */                 LayoutPane.this.remove(field);
/* 108:138 */                 LayoutPane.this.reDo();
/* 109:    */               }
/* 110:    */               else
/* 111:    */               {
/* 112:141 */                 LayoutPane.this.remove(field);
/* 113:142 */                 LayoutPane.this.invalidate();
/* 114:143 */                 LayoutPane.this.validate();
/* 115:144 */                 LayoutPane.this.repaint();
/* 116:    */               }
/* 117:    */             }
/* 118:147 */           });
/* 119:148 */           LayoutPane.this.add(field);
/* 120:149 */           LayoutPane.this.invalidate();
/* 121:150 */           LayoutPane.this.validate();
/* 122:151 */           LayoutPane.this.repaint();
/* 123:    */         }
/* 124:    */       }
/* 125:    */     });
/* 126:    */   }
/* 127:    */   
/* 128:    */   private void adaptChildren(Node node)
/* 129:    */   {
/* 130:160 */     for (int i = 0; i < this.nodes_.size(); i++) {
/* 131:161 */       ((Node)this.nodes_.get(i)).adaptConnections(node);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void reDo()
/* 136:    */   {
/* 137:165 */     removeAll();
/* 138:166 */     if (getParent() != null) {
/* 139:167 */       setSize(getParent().getWidth(), getParent().getHeight());
/* 140:    */     }
/* 141:170 */     setStepSize();
/* 142:171 */     setBackground(Color.lightGray);
/* 143:    */     
/* 144:173 */     addButtons();
/* 145:174 */     addOptions();
/* 146:175 */     invalidate();
/* 147:176 */     validate();
/* 148:177 */     repaint();
/* 149:    */   }
/* 150:    */   
/* 151:    */   private void addOptions()
/* 152:    */   {
/* 153:205 */     JButton decGrid = new JButton("-");
/* 154:206 */     decGrid.setBounds(10, 45, 45, 45);
/* 155:207 */     decGrid.addActionListener(new ActionListener()
/* 156:    */     {
/* 157:    */       public void actionPerformed(ActionEvent arg0)
/* 158:    */       {
/* 159:212 */         LayoutPane.this.grid.decGridSize();
/* 160:213 */         if (LayoutPane.this.setComment) {
/* 161:214 */           LayoutPane.this.setComment = false;
/* 162:    */         }
/* 163:216 */         if (LayoutPane.this.activeNode != null) {
/* 164:217 */           LayoutPane.this.activeNode = null;
/* 165:    */         }
/* 166:219 */         LayoutPane.this.fieldOpen = false;
/* 167:220 */         LayoutPane.this.reDo();
/* 168:    */       }
/* 169:222 */     });
/* 170:223 */     add(decGrid);
/* 171:224 */     JButton incGrid = new JButton("+");
/* 172:225 */     incGrid.setBounds(10, 85, 45, 45);
/* 173:226 */     incGrid.addActionListener(new ActionListener()
/* 174:    */     {
/* 175:    */       public void actionPerformed(ActionEvent arg0)
/* 176:    */       {
/* 177:231 */         LayoutPane.this.grid.incGridSize();
/* 178:232 */         if (LayoutPane.this.setComment) {
/* 179:233 */           LayoutPane.this.setComment = false;
/* 180:    */         }
/* 181:235 */         if (LayoutPane.this.activeNode != null) {
/* 182:236 */           LayoutPane.this.activeNode = null;
/* 183:    */         }
/* 184:238 */         LayoutPane.this.fieldOpen = false;
/* 185:239 */         LayoutPane.this.reDo();
/* 186:    */       }
/* 187:241 */     });
/* 188:242 */     add(incGrid);
/* 189:243 */     System.err.println("addoptions  " + PathLayoutGrid.pathWayName);
/* 190:244 */     if (PathLayoutGrid.pathWayName != null)
/* 191:    */     {
/* 192:245 */       if (!PathLayoutGrid.pathWayName.isEmpty())
/* 193:    */       {
/* 194:246 */         nameField.setText(PathLayoutGrid.pathWayName);
/* 195:247 */         System.out.println("!empty " + PathLayoutGrid.pathWayName);
/* 196:    */       }
/* 197:    */       else
/* 198:    */       {
/* 199:250 */         nameField.setText("Enter Pathname");
/* 200:251 */         System.out.println("empty " + PathLayoutGrid.pathWayName);
/* 201:    */       }
/* 202:    */     }
/* 203:    */     else
/* 204:    */     {
/* 205:255 */       nameField.setText("Enter Pathname");
/* 206:256 */       System.out.println("null " + PathLayoutGrid.pathWayName);
/* 207:    */     }
/* 208:258 */     nameField.setBounds(10, 10, 200, 30);
/* 209:259 */     add(nameField);
/* 210:    */     
/* 211:261 */     JButton preview = new JButton("Preview");
/* 212:262 */     preview.setBounds(215, 10, 150, 30);
/* 213:263 */     preview.addActionListener(new ActionListener()
/* 214:    */     {
/* 215:    */       public void actionPerformed(ActionEvent arg0)
/* 216:    */       {
/* 217:267 */         if (LayoutPane.this.setComment) {
/* 218:268 */           LayoutPane.this.setComment = false;
/* 219:    */         }
/* 220:270 */         if (LayoutPane.this.activeNode != null) {
/* 221:271 */           LayoutPane.this.activeNode = null;
/* 222:    */         }
/* 223:273 */         LayoutPane.this.fieldOpen = false;
/* 224:274 */         LayoutPane.this.grid.previewPathway();
/* 225:    */       }
/* 226:276 */     });
/* 227:277 */     add(preview);
/* 228:    */     
/* 229:279 */     JButton addComment = new JButton("Add comment node");
/* 230:280 */     addComment.setBounds(370, 10, 150, 30);
/* 231:281 */     if (this.setComment) {
/* 232:282 */       addComment.setBackground(Color.yellow);
/* 233:    */     }
/* 234:284 */     addComment.addActionListener(new ActionListener()
/* 235:    */     {
/* 236:    */       public void actionPerformed(ActionEvent arg0)
/* 237:    */       {
/* 238:288 */         LayoutPane.this.setComment = (!LayoutPane.this.setComment);
/* 239:289 */         if (LayoutPane.this.activeNode != null) {
/* 240:290 */           LayoutPane.this.activeNode = null;
/* 241:    */         }
/* 242:292 */         LayoutPane.this.fieldOpen = false;
/* 243:293 */         LayoutPane.this.reDo();
/* 244:    */       }
/* 245:295 */     });
/* 246:296 */     add(addComment);
/* 247:    */     
/* 248:298 */     JButton forceMode = new JButton("forcelayout");
/* 249:299 */     forceMode.setBounds(525, 10, 150, 30);
/* 250:300 */     forceMode.addActionListener(new ActionListener()
/* 251:    */     {
/* 252:    */       public void actionPerformed(ActionEvent arg0)
/* 253:    */       {
/* 254:305 */         LayoutPane.this.grid.doForceLayout();
/* 255:306 */         LayoutPane.this.reDo();
/* 256:    */       }
/* 257:308 */     });
/* 258:309 */     add(forceMode);
/* 259:    */     
/* 260:311 */     JButton random = new JButton("randomlayout");
/* 261:312 */     random.setBounds(680, 10, 150, 30);
/* 262:313 */     random.addActionListener(new ActionListener()
/* 263:    */     {
/* 264:    */       public void actionPerformed(ActionEvent arg0)
/* 265:    */       {
/* 266:318 */         LayoutPane.this.grid.setNodesRandomly(LayoutPane.this.grid.nodes);
/* 267:319 */         LayoutPane.this.reDo();
/* 268:    */       }
/* 269:321 */     });
/* 270:322 */     add(random);
/* 271:    */   }
/* 272:    */   
/* 273:    */   private void setStepSize()
/* 274:    */   {
/* 275:325 */     this.xStepSize = (getWidth() / (this.grid.xGridSize + 2));
/* 276:326 */     this.yStepSize = (getHeight() / (this.grid.yGridSize + 2));
/* 277:    */   }
/* 278:    */   
/* 279:    */   private void addButtons()
/* 280:    */   {
/* 281:330 */     this.nodes_ = this.grid.nodes;
/* 282:331 */     for (int i = 0; i < this.nodes_.size(); i++)
/* 283:    */     {
/* 284:332 */       Node node = (Node)this.nodes_.get(i);
/* 285:333 */       addButton(node, i);
/* 286:    */     }
/* 287:    */   }
/* 288:    */   
/* 289:    */   private void addButton(Node node, int index)
/* 290:    */   {
/* 291:337 */     JButton button = new JButton(node.getName());
/* 292:338 */     int charSpace = 8;
/* 293:339 */     int length = node.getName().length() * charSpace;
/* 294:340 */     if (length < 75) {
/* 295:341 */       length = 75;
/* 296:    */     }
/* 297:343 */     int xPos = (int)(this.xStepSize + this.xStepSize * node.xPos - this.xStepSize * this.buttonfac / 2.0D);
/* 298:344 */     int yPos = (int)(this.yStepSize + this.yStepSize * node.yPos - this.yStepSize * this.buttonfac / 2.0D);
/* 299:345 */     button.setBounds(xPos, yPos, length, (int)(this.yStepSize * this.buttonfac));
/* 300:346 */     if (node.comment) {
/* 301:347 */       button.setBackground(Color.orange);
/* 302:    */     }
/* 303:349 */     button.setVisible(true);
/* 304:350 */     if ((this.activeNode != null) && 
/* 305:351 */       (node.ownIndex == this.activeNode.ownIndex)) {
/* 306:352 */       if (node.comment) {
/* 307:353 */         button.setBackground(Color.green.darker());
/* 308:    */       } else {
/* 309:356 */         button.setBackground(Color.green);
/* 310:    */       }
/* 311:    */     }
/* 312:360 */     final int indexF = index;
/* 313:361 */     button.addActionListener(new ActionListener()
/* 314:    */     {
/* 315:    */       public void actionPerformed(ActionEvent arg0)
/* 316:    */       {
/* 317:366 */         LayoutPane.this.selectNode(indexF);
/* 318:367 */         LayoutPane.this.reDo();
/* 319:    */       }
/* 320:369 */     });
/* 321:370 */     add(button);
/* 322:371 */     if ((this.activeNode != null) && 
/* 323:372 */       (this.activeNode.ownIndex == index)) {
/* 324:373 */       addActiveNodeParts(node, index, xPos, yPos, length);
/* 325:    */     }
/* 326:    */   }
/* 327:    */   
/* 328:    */   private void addActiveNodeParts(Node node, int index, int xPos, int yPos, int length)
/* 329:    */   {
/* 330:378 */     final int indexF = index;
/* 331:379 */     ImageIcon icon = createImageIcon("images/del2.gif", "del Button gif");
/* 332:380 */     int size = (int)(this.yStepSize * this.buttonfac / 2.0D);
/* 333:381 */     Image image = icon.getImage();
/* 334:382 */     Image newimg = image.getScaledInstance(size, size, 4);
/* 335:383 */     icon = new ImageIcon(newimg);
/* 336:384 */     JButton remButton = new JButton(icon);
/* 337:385 */     remButton.setBorder(null);
/* 338:386 */     remButton.setBounds(xPos + length, yPos + size, size, size);
/* 339:387 */     remButton.addActionListener(new ActionListener()
/* 340:    */     {
/* 341:    */       public void actionPerformed(ActionEvent e)
/* 342:    */       {
/* 343:392 */         LayoutPane.this.activeNode = null;
/* 344:393 */         LayoutPane.this.removeNode = true;
/* 345:394 */         LayoutPane.this.selectNode(indexF);
/* 346:395 */         LayoutPane.this.removeNode = false;
/* 347:396 */         LayoutPane.this.reDo();
/* 348:    */       }
/* 349:398 */     });
/* 350:399 */     add(remButton);
/* 351:    */     
/* 352:401 */     final JTextField field = new JTextField("Name");
/* 353:402 */     int ySize = (int)(this.yStepSize * this.buttonfac);
/* 354:403 */     field.setBounds(xPos, yPos + ySize, length, ySize);
/* 355:404 */     field.addActionListener(new ActionListener()
/* 356:    */     {
/* 357:    */       public void actionPerformed(ActionEvent e)
/* 358:    */       {
/* 359:409 */         ((Node)LayoutPane.this.nodes_.get(indexF)).name = field.getText();
/* 360:410 */         LayoutPane.this.reDo();
/* 361:411 */         field.setVisible(false);
/* 362:    */       }
/* 363:413 */     });
/* 364:414 */     field.setVisible(false);
/* 365:415 */     add(field);
/* 366:416 */     JButton editButt = new JButton("...");
/* 367:417 */     editButt.setBorder(null);
/* 368:418 */     editButt.setBounds(xPos + length, yPos, size, size);
/* 369:419 */     editButt.addActionListener(new ActionListener()
/* 370:    */     {
/* 371:    */       public void actionPerformed(ActionEvent e)
/* 372:    */       {
/* 373:424 */         field.setVisible(!field.isVisible());
/* 374:    */       }
/* 375:428 */     });
/* 376:429 */     add(editButt);
/* 377:    */   }
/* 378:    */   
/* 379:    */   public void paintComponent(Graphics g)
/* 380:    */   {
/* 381:433 */     super.paintComponent(g);
/* 382:434 */     Graphics2D g2 = (Graphics2D)g;
/* 383:    */     
/* 384:    */ 
/* 385:437 */     int size = 10;
/* 386:438 */     int offset = size / 2;
/* 387:440 */     for (int i = 0; i < this.grid.xGridSize + 1; i++) {
/* 388:441 */       for (int j = 0; j < this.grid.yGridSize + 1; j++)
/* 389:    */       {
/* 390:442 */         g2.setColor(Color.gray);
/* 391:443 */         g2.drawLine(this.xStepSize, this.yStepSize + this.yStepSize * j, this.xStepSize + this.xStepSize * this.grid.xGridSize, this.yStepSize + this.yStepSize * j);
/* 392:444 */         g2.drawLine(this.xStepSize + this.xStepSize * i, this.yStepSize, this.xStepSize + this.xStepSize * i, this.yStepSize + this.yStepSize * this.grid.yGridSize);
/* 393:    */       }
/* 394:    */     }
/* 395:447 */     g2.setColor(Color.black);
/* 396:448 */     for (int i = 0; i < this.nodes_.size(); i++)
/* 397:    */     {
/* 398:449 */       Node node = (Node)this.nodes_.get(i);
/* 399:450 */       for (int j = 0; j < node.conections.size(); j++)
/* 400:    */       {
/* 401:451 */         if (node.comment) {
/* 402:452 */           g2.setColor(Color.blue);
/* 403:    */         } else {
/* 404:455 */           g2.setColor(Color.black);
/* 405:    */         }
/* 406:457 */         Node node2 = (Node)node.conections.get(j);
/* 407:458 */         int xPos = this.xStepSize + this.xStepSize * node.xPos;
/* 408:459 */         int yPos = this.yStepSize + this.yStepSize * node.yPos;
/* 409:460 */         int xPos2 = this.xStepSize + this.xStepSize * node2.xPos;
/* 410:461 */         int yPos2 = this.yStepSize + this.yStepSize * node2.yPos;
/* 411:462 */         g2.setStroke(new BasicStroke(3.0F));
/* 412:463 */         g2.drawLine(xPos, yPos, xPos2, yPos2);
/* 413:464 */         int dX = xPos - xPos2;
/* 414:465 */         int dy = yPos - yPos2;
/* 415:466 */         float length = (int)Math.sqrt(dX * dX + dy * dy);
/* 416:467 */         int vecX = (int)((xPos - xPos2) / length * (0.3D * length));
/* 417:468 */         int vecY = (int)((yPos - yPos2) / length * (0.3D * length));
/* 418:469 */         g2.setColor(Color.red);
/* 419:470 */         g2.fillOval(xPos2 + vecX - offset, yPos2 + vecY - offset, size, size);
/* 420:471 */         g2.setColor(Color.black);
/* 421:    */       }
/* 422:    */     }
/* 423:    */   }
/* 424:    */   
/* 425:    */   private void selectNode(int index)
/* 426:    */   {
/* 427:476 */     System.out.println("selectNode " + index);
/* 428:477 */     if (this.activeNode == null)
/* 429:    */     {
/* 430:478 */       if (this.removeNode)
/* 431:    */       {
/* 432:482 */         for (int j = 0; j < this.nodes_.size(); j++)
/* 433:    */         {
/* 434:483 */           Node node = (Node)this.nodes_.get(j);
/* 435:484 */           for (int i = 0; i < node.conections.size(); i++)
/* 436:    */           {
/* 437:485 */             Node conN = (Node)node.conections.get(i);
/* 438:486 */             if (index == conN.ownIndex)
/* 439:    */             {
/* 440:487 */               node.conections.remove(conN);
/* 441:488 */               break;
/* 442:    */             }
/* 443:    */           }
/* 444:    */         }
/* 445:492 */         this.grid.removeNode(index);
/* 446:493 */         this.removeNode = false;
/* 447:494 */         this.activeNode = null;
/* 448:    */       }
/* 449:    */       else
/* 450:    */       {
/* 451:497 */         this.activeNode = ((Node)this.nodes_.get(index));
/* 452:    */       }
/* 453:    */     }
/* 454:    */     else
/* 455:    */     {
/* 456:501 */       System.out.println("selectNode " + this.activeNode.ownIndex + " " + index);
/* 457:502 */       if (this.activeNode.ownIndex == index)
/* 458:    */       {
/* 459:503 */         this.activeNode = null;
/* 460:    */       }
/* 461:    */       else
/* 462:    */       {
/* 463:508 */         for (int i = 0; i < this.activeNode.conections.size(); i++)
/* 464:    */         {
/* 465:509 */           Node conN = (Node)this.activeNode.conections.get(i);
/* 466:510 */           if (index == conN.ownIndex)
/* 467:    */           {
/* 468:511 */             this.activeNode.conections.remove(conN);
/* 469:512 */             this.activeNode = null;
/* 470:513 */             return;
/* 471:    */           }
/* 472:    */         }
/* 473:516 */         this.activeNode.addConnection((Node)this.nodes_.get(index));
/* 474:517 */         this.activeNode = null;
/* 475:    */       }
/* 476:    */     }
/* 477:520 */     reDo();
/* 478:    */   }
/* 479:    */   
/* 480:    */   protected ImageIcon createImageIcon(String path, String description)
/* 481:    */   {
/* 482:528 */     ClassLoader cl = getClass().getClassLoader();
/* 483:529 */     URL imgURL = cl.getResource(path);
/* 484:530 */     if (imgURL != null) {
/* 485:531 */       return new ImageIcon(imgURL, description);
/* 486:    */     }
/* 487:533 */     System.err.println("Couldn't find file: " + path);
/* 488:534 */     return null;
/* 489:    */   }
/* 490:    */ }


/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar
 * Qualified Name:     pathwayLayout.LayoutPane
 * JD-Core Version:    0.7.0.1
 */