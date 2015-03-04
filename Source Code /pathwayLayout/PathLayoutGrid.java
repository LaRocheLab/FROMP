/*   1:    */ package pathwayLayout;
/*   2:    */ 
/*   3:    */ import Objects.EcNr;
/*   4:    */ import Objects.EcSampleStats;
/*   5:    */ import Objects.Pathway;
/*   6:    */ import Objects.PathwayWithEc;
/*   7:    */ import Objects.Sample;
/*   8:    */ import Panes.ImagePanel;
/*   9:    */ import java.awt.BasicStroke;
/*  10:    */ import java.awt.Color;
/*  11:    */ import java.awt.Graphics2D;
/*  12:    */ import java.awt.image.BufferedImage;
/*  13:    */ import java.io.BufferedReader;
/*  14:    */ import java.io.BufferedWriter;
/*  15:    */ import java.io.FileReader;
/*  16:    */ import java.io.FileWriter;
/*  17:    */ import java.io.IOException;
/*  18:    */ import java.io.PrintStream;
/*  19:    */ import java.util.ArrayList;
/*  20:    */ import java.util.Random;
/*  21:    */ 
/*  22:    */ public class PathLayoutGrid
/*  23:    */ {
/*  24:    */   Node[][] layout;
/*  25:    */   ArrayList<Node> nodes;
/*  26:    */   ArrayList<Node> minGraph;
/*  27:    */   IntMat adjMat1;
/*  28:    */   IntMat adjMat2;
/*  29:    */   IntMat adjMat3;
/*  30:    */   IntMat adjMat4;
/*  31:    */   IntMat weightMat;
/*  32: 38 */   int maxDist = 10;
/*  33:    */   int xGridSize;
/*  34:    */   int yGridSize;
/*  35:    */   int xStepSize;
/*  36:    */   int yStepSize;
/*  37: 46 */   int charSpace = 8;
/*  38: 48 */   double buttonfac = 0.8D;
/*  39:    */   String loadPath;
/*  40:    */   public static String pathWayName;
/*  41: 54 */   private float minForce = 0.47F;
/*  42: 55 */   private float minCnt = 1000.0F;
/*  43:    */   
/*  44:    */   public PathLayoutGrid(PathLayoutGrid grid)
/*  45:    */   {
/*  46: 59 */     this.nodes = copyNodeList(grid.nodes);
/*  47:    */     
/*  48: 61 */     this.maxDist = grid.maxDist;
/*  49:    */     
/*  50: 63 */     this.xGridSize = grid.xGridSize;
/*  51: 64 */     this.yGridSize = grid.yGridSize;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public PathLayoutGrid(int xSize, int ySize, boolean designMode)
/*  55:    */   {
/*  56: 70 */     pathWayName = "Enter Pathname";
/*  57: 71 */     this.layout = new Node[xSize][ySize];
/*  58: 72 */     this.xGridSize = xSize;
/*  59: 73 */     this.yGridSize = ySize;
/*  60: 75 */     if (!designMode) {
/*  61: 76 */       return;
/*  62:    */     }
/*  63: 83 */     if (this.nodes == null) {
/*  64: 90 */       this.nodes = new ArrayList();
/*  65:    */     }
/*  66: 93 */     LayoutFrame frame = new LayoutFrame(800, 600, this, this.nodes);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void randomMode(int times)
/*  70:    */   {
/*  71:102 */     int minCost = 10000;
/*  72:103 */     int cost = 0;
/*  73:104 */     for (int i = 0; i < times; i++)
/*  74:    */     {
/*  75:106 */       this.nodes = setNodesRandomly(this.nodes);
/*  76:107 */       cost = evaluate(this.nodes);
/*  77:108 */       if (cost < minCost)
/*  78:    */       {
/*  79:109 */         minCost = cost;
/*  80:110 */         this.minGraph = copyNodeList(this.nodes);
/*  81:111 */         System.out.println("new minGraph:" + i + ", cost:" + cost);
/*  82:    */       }
/*  83:113 */       System.out.println("Round:" + i + ", cost:" + cost);
/*  84:    */     }
/*  85:116 */     LayoutFrame frame = new LayoutFrame(800, 600, this, this.minGraph);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void doForceLayout()
/*  89:    */   {
/*  90:119 */     if (this.nodes == null)
/*  91:    */     {
/*  92:120 */       System.out.println("Nodes == null");
/*  93:121 */       return;
/*  94:    */     }
/*  95:123 */     float webforce = 3000.0F;
/*  96:124 */     int rndCnt = 0;
/*  97:125 */     while (webforce > this.minForce)
/*  98:    */     {
/*  99:126 */       webforce = calcNodeForces();
/* 100:127 */       moveNodesByForce();
/* 101:    */       
/* 102:129 */       moveNodesToCenter();
/* 103:130 */       rndCnt++;
/* 104:131 */       if (rndCnt > 1000)
/* 105:    */       {
/* 106:132 */         setNodesRandomly(this.nodes);
/* 107:133 */         rndCnt = 0;
/* 108:    */       }
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   private float calcNodeForces()
/* 113:    */   {
/* 114:139 */     float webforce = 0.0F;
/* 115:140 */     for (int nodeCnt = 0; nodeCnt < this.nodes.size(); nodeCnt++)
/* 116:    */     {
/* 117:141 */       Node srcNode = (Node)this.nodes.get(nodeCnt);
/* 118:142 */       srcNode.xForce = 0.0F;
/* 119:143 */       srcNode.yForce = 0.0F;
/* 120:    */     }
/* 121:145 */     for (int nodeCnt = 0; nodeCnt < this.nodes.size(); nodeCnt++)
/* 122:    */     {
/* 123:146 */       Node srcNode = (Node)this.nodes.get(nodeCnt);
/* 124:147 */       webforce += srcNode.calcForceByConnections(copyNodeList(this.nodes));
/* 125:    */     }
/* 126:149 */     return webforce;
/* 127:    */   }
/* 128:    */   
/* 129:    */   private void moveNodesByForce()
/* 130:    */   {
/* 131:153 */     float minDist = 1.0F;
/* 132:154 */     for (int i = 0; i < this.nodes.size(); i++)
/* 133:    */     {
/* 134:155 */       Node node = new Node((Node)this.nodes.get(i), true);
/* 135:156 */       node.moveByForce(this.xGridSize, this.yGridSize);
/* 136:157 */       if (positionTaken(node)) {
/* 137:158 */         System.out.println("moveByForce Position taken " + i);
/* 138:161 */       } else if (!node.minDistToConKept(minDist)) {
/* 139:162 */         System.out.println("minDistReached " + i);
/* 140:    */       } else {
/* 141:166 */         ((Node)this.nodes.get(i)).moveByForce(this.xGridSize, this.yGridSize);
/* 142:    */       }
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void moveNodesToCenter()
/* 147:    */   {
/* 148:172 */     int xSum = 0;
/* 149:173 */     int ySum = 0;
/* 150:174 */     for (int i = 0; i < this.nodes.size(); i++)
/* 151:    */     {
/* 152:175 */       Node node = (Node)this.nodes.get(i);
/* 153:176 */       xSum += node.xPos;
/* 154:177 */       ySum += node.yPos;
/* 155:    */     }
/* 156:179 */     xSum /= this.nodes.size();
/* 157:180 */     ySum /= this.nodes.size();
/* 158:    */     
/* 159:182 */     xSum = this.xGridSize / 2 - xSum;
/* 160:183 */     ySum = this.yGridSize / 2 - ySum;
/* 161:185 */     for (int i = 0; i < this.nodes.size(); i++)
/* 162:    */     {
/* 163:186 */       Node node = (Node)this.nodes.get(i);
/* 164:187 */       if ((node.xPos + xSum >= 0) && (node.xPos + xSum <= this.xGridSize)) {
/* 165:188 */         node.xPos += xSum;
/* 166:    */       }
/* 167:190 */       if ((node.yPos + ySum >= 0) && (node.yPos + ySum <= this.yGridSize)) {
/* 168:191 */         node.yPos += ySum;
/* 169:    */       }
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   public ArrayList<Node> copyNodeList(ArrayList<Node> nodes)
/* 174:    */   {
/* 175:196 */     ArrayList<Node> retList = new ArrayList();
/* 176:197 */     for (int i = 0; i < nodes.size(); i++) {
/* 177:198 */       retList.add(new Node((Node)nodes.get(i), true));
/* 178:    */     }
/* 179:200 */     return retList;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void doGridLayout(float tMax, float tMin, ArrayList<Node> nodesList, float minCost, int repeatTimes, float reduceTemp, float pRate)
/* 183:    */   {
/* 184:204 */     float temperature = tMax;
/* 185:205 */     nodesList = setNodesRandomly(nodesList);
/* 186:206 */     float cost = evaluate(nodesList);
/* 187:    */     
/* 188:208 */     minCost = cost;
/* 189:    */     
/* 190:210 */     this.minGraph = copyNodeList(nodesList);
/* 191:211 */     ArrayList<Node> tempGraph = new ArrayList();
/* 192:212 */     while (temperature > tMin)
/* 193:    */     {
/* 194:213 */       for (int i = 0; i < repeatTimes; i++)
/* 195:    */       {
/* 196:214 */         tempGraph = copyNodeList(neighbor(nodesList, pRate));
/* 197:215 */         float tmpCost = localMin(tempGraph);
/* 198:216 */         float random = (float)Math.random();
/* 199:217 */         if (random < Math.exp((cost - tmpCost) / temperature))
/* 200:    */         {
/* 201:218 */           cost = tmpCost;
/* 202:219 */           nodesList = copyNodeList(tempGraph);
/* 203:220 */           if (cost < minCost)
/* 204:    */           {
/* 205:221 */             minCost = cost;
/* 206:222 */             this.minGraph = copyNodeList(nodesList);
/* 207:    */           }
/* 208:    */         }
/* 209:    */       }
/* 210:226 */       temperature *= reduceTemp;
/* 211:    */     }
/* 212:    */   }
/* 213:    */   
/* 214:    */   private ArrayList<Node> neighbor(ArrayList<Node> nodesL, float pRate)
/* 215:    */   {
/* 216:233 */     Random g = new Random();
/* 217:234 */     for (int i = 0; i < nodesL.size(); i++)
/* 218:    */     {
/* 219:235 */       float random = (float)Math.random();
/* 220:236 */       if (random < pRate)
/* 221:    */       {
/* 222:237 */         Node node = (Node)nodesL.get(i);
/* 223:238 */         while (positionTaken(node))
/* 224:    */         {
/* 225:239 */           node.xPos = g.nextInt(this.xGridSize);
/* 226:240 */           node.yPos = g.nextInt(this.yGridSize);
/* 227:    */         }
/* 228:    */       }
/* 229:    */     }
/* 230:244 */     return nodesL;
/* 231:    */   }
/* 232:    */   
/* 233:    */   private float localMin(ArrayList<Node> nodesL)
/* 234:    */   {
/* 235:247 */     float cost = evaluate(nodesL);
/* 236:    */     float minCost;
/* 237:    */     do
/* 238:    */     {
/* 239:254 */       minCost = cost;
/* 240:255 */       for (int i = 0; i < nodesL.size(); i++)
/* 241:    */       {
/* 242:256 */         Node node = (Node)nodesL.get(i);
/* 243:257 */         int origX = node.xPos;
/* 244:258 */         int origY = node.yPos;
/* 245:259 */         for (int x = 0; x < this.xGridSize; x++) {
/* 246:260 */           for (int y = 0; y < this.yGridSize; y++) {
/* 247:261 */             if ((x != origX) && (y != origY))
/* 248:    */             {
/* 249:262 */               node.xPos = x;
/* 250:263 */               node.yPos = y;
/* 251:    */               
/* 252:265 */               float trialCost = evaluate(nodesL);
/* 253:266 */               if (trialCost < minCost)
/* 254:    */               {
/* 255:267 */                 minCost = trialCost;
/* 256:    */               }
/* 257:    */               else
/* 258:    */               {
/* 259:270 */                 node.xPos = origX;
/* 260:271 */                 node.yPos = origY;
/* 261:    */               }
/* 262:    */             }
/* 263:    */           }
/* 264:    */         }
/* 265:    */       }
/* 266:277 */     } while (minCost < cost);
/* 267:278 */     return minCost;
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void testNodes()
/* 271:    */   {
/* 272:284 */     Node a = new Node("0");
/* 273:285 */     Node b = new Node("1");
/* 274:286 */     Node c = new Node("2");
/* 275:287 */     Node d = new Node("3");
/* 276:288 */     Node e = new Node("4");
/* 277:289 */     Node f = new Node("5");
/* 278:290 */     Node g = new Node("6");
/* 279:291 */     Node h = new Node("7");
/* 280:292 */     Node i = new Node("8");
/* 281:    */     
/* 282:    */ 
/* 283:295 */     a.addConnection(b);
/* 284:296 */     b.addConnection(c);
/* 285:297 */     c.addConnection(d);
/* 286:298 */     c.addConnection(e);
/* 287:299 */     d.addConnection(f);
/* 288:300 */     d.addConnection(g);
/* 289:301 */     a.addConnection(h);
/* 290:302 */     g.addConnection(i);
/* 291:    */     
/* 292:    */ 
/* 293:305 */     addNode(a);
/* 294:306 */     addNode(b);
/* 295:307 */     addNode(c);
/* 296:308 */     addNode(d);
/* 297:309 */     addNode(e);
/* 298:310 */     addNode(f);
/* 299:311 */     addNode(g);
/* 300:312 */     addNode(h);
/* 301:313 */     addNode(i);
/* 302:    */   }
/* 303:    */   
/* 304:    */   public void resetNodePos()
/* 305:    */   {
/* 306:317 */     for (int i = 0; i < this.nodes.size(); i++)
/* 307:    */     {
/* 308:318 */       Node node = (Node)this.nodes.get(i);
/* 309:319 */       node.xPos = -1;
/* 310:320 */       node.yPos = -1;
/* 311:    */     }
/* 312:    */   }
/* 313:    */   
/* 314:    */   public ArrayList<Node> setNodesRandomly(ArrayList<Node> nodes)
/* 315:    */   {
/* 316:324 */     Random g = new Random();
/* 317:    */     
/* 318:326 */     resetNodePos();
/* 319:327 */     for (int i = 0; i < nodes.size(); i++)
/* 320:    */     {
/* 321:328 */       Node node = (Node)nodes.get(i);
/* 322:329 */       while (positionTaken(node))
/* 323:    */       {
/* 324:330 */         node.xPos = g.nextInt(this.xGridSize);
/* 325:331 */         node.yPos = g.nextInt(this.yGridSize);
/* 326:    */       }
/* 327:333 */       System.out.println("NOde " + node.ownIndex + " Pos =(" + node.xPos + ", " + node.yPos + ")");
/* 328:    */     }
/* 329:335 */     return nodes;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public boolean positionTaken(Node node)
/* 333:    */   {
/* 334:338 */     if (node.xPos < 0) {
/* 335:339 */       return true;
/* 336:    */     }
/* 337:342 */     for (int i = 0; i < this.nodes.size(); i++)
/* 338:    */     {
/* 339:343 */       Node compare = (Node)this.nodes.get(i);
/* 340:344 */       if (node.ownIndex != compare.ownIndex) {
/* 341:347 */         if ((node.xPos == compare.xPos) && (node.yPos == compare.yPos))
/* 342:    */         {
/* 343:348 */           System.out.println("compare " + i);
/* 344:349 */           return true;
/* 345:    */         }
/* 346:    */       }
/* 347:    */     }
/* 348:352 */     return false;
/* 349:    */   }
/* 350:    */   
/* 351:    */   public void buildAdjMAt(ArrayList<Node> nodeL)
/* 352:    */   {
/* 353:355 */     IntMat adjMat = new IntMat(nodeL);
/* 354:356 */     IntMat ident = new IntMat(nodeL.size(), true);
/* 355:357 */     System.out.println("ident");
/* 356:358 */     ident.printMat();
/* 357:359 */     System.out.println("adj");
/* 358:360 */     adjMat.printMat();
/* 359:    */     
/* 360:362 */     this.adjMat1 = adjMat.add(ident);
/* 361:    */     
/* 362:364 */     System.out.println("adj");
/* 363:365 */     this.adjMat1.printMat();
/* 364:    */     
/* 365:367 */     IntMat b = adjMat.clone();
/* 366:    */     
/* 367:369 */     this.adjMat2 = this.adjMat1.times(b);
/* 368:370 */     System.out.println("adj");
/* 369:371 */     this.adjMat2.printMat();
/* 370:372 */     this.adjMat3 = this.adjMat2.times(b);
/* 371:373 */     System.out.println("adj");
/* 372:374 */     this.adjMat3.printMat();
/* 373:375 */     this.adjMat4 = this.adjMat3.times(b);
/* 374:376 */     System.out.println("adj");
/* 375:377 */     this.adjMat4.printMat();
/* 376:    */   }
/* 377:    */   
/* 378:    */   public int evaluate(ArrayList<Node> nodes)
/* 379:    */   {
/* 380:387 */     int sum = 0;
/* 381:388 */     for (int i = 0; i < nodes.size() - 1; i++) {
/* 382:389 */       for (int j = i + 1; j < nodes.size(); j++) {
/* 383:390 */         sum += getCost((Node)nodes.get(i), (Node)nodes.get(j));
/* 384:    */       }
/* 385:    */     }
/* 386:393 */     System.out.println("evaluate sum =" + sum);
/* 387:394 */     return sum;
/* 388:    */   }
/* 389:    */   
/* 390:    */   public int getCost(Node a, Node b)
/* 391:    */   {
/* 392:401 */     int Wij = this.weightMat.mat[a.ownIndex][b.ownIndex].intValue();
/* 393:402 */     int ret = 0;
/* 394:403 */     int dist = getDist(a, b);
/* 395:404 */     if (Wij >= 0) {
/* 396:405 */       ret = Wij * dist;
/* 397:    */     } else {
/* 398:408 */       ret = Wij * Math.min(dist, this.maxDist);
/* 399:    */     }
/* 400:410 */     return ret;
/* 401:    */   }
/* 402:    */   
/* 403:    */   public int getDist(Node a, Node b)
/* 404:    */   {
/* 405:417 */     int ret = Math.abs(a.xPos - b.xPos) + Math.abs(a.yPos - b.yPos);
/* 406:418 */     return ret;
/* 407:    */   }
/* 408:    */   
/* 409:    */   public void buildWeightMAt(ArrayList<Node> nodeL)
/* 410:    */   {
/* 411:429 */     this.weightMat = new IntMat(this.adjMat1.numOfRows, false);
/* 412:430 */     for (int i = 0; i < this.weightMat.numOfRows; i++) {
/* 413:431 */       for (int j = 0; j < this.weightMat.numOfRows; j++) {
/* 414:432 */         this.weightMat.mat[i][j] = Integer.valueOf(getWeight(i, j));
/* 415:    */       }
/* 416:    */     }
/* 417:435 */     System.out.println("weightMat");
/* 418:436 */     this.weightMat.printMat();
/* 419:    */   }
/* 420:    */   
/* 421:    */   public int getWeight(int i, int j)
/* 422:    */   {
/* 423:439 */     if (this.adjMat1.mat[i][j].intValue() > 0) {
/* 424:440 */       return 3;
/* 425:    */     }
/* 426:442 */     if ((this.adjMat1.mat[i][j].intValue() == 0) && (this.adjMat2.mat[i][j].intValue() > 0)) {
/* 427:443 */       return 1;
/* 428:    */     }
/* 429:445 */     if ((this.adjMat2.mat[i][j].intValue() == 0) && (this.adjMat3.mat[i][j].intValue() > 0)) {
/* 430:446 */       return 0;
/* 431:    */     }
/* 432:448 */     if ((this.adjMat3.mat[i][j].intValue() == 0) && (this.adjMat4.mat[i][j].intValue() > 0)) {
/* 433:449 */       return -1;
/* 434:    */     }
/* 435:452 */     return -2;
/* 436:    */   }
/* 437:    */   
/* 438:    */   private void updateIndex()
/* 439:    */   {
/* 440:459 */     for (int i = 0; i < this.nodes.size(); i++)
/* 441:    */     {
/* 442:460 */       Node node = (Node)this.nodes.get(i);
/* 443:461 */       if (node.ownIndex != i)
/* 444:    */       {
/* 445:465 */         for (int j = 0; j < this.nodes.size(); j++) {
/* 446:466 */           if (i != j)
/* 447:    */           {
/* 448:469 */             Node node2 = (Node)this.nodes.get(j);
/* 449:470 */             for (int h = 0; h < node2.conections.size(); h++)
/* 450:    */             {
/* 451:471 */               Node con = (Node)node2.conections.get(h);
/* 452:472 */               if (con.ownIndex == node.ownIndex) {
/* 453:473 */                 con.ownIndex = i;
/* 454:    */               }
/* 455:    */             }
/* 456:    */           }
/* 457:    */         }
/* 458:477 */         node.ownIndex = i;
/* 459:    */       }
/* 460:    */     }
/* 461:    */   }
/* 462:    */   
/* 463:    */   public void savePathway(String path)
/* 464:    */   {
/* 465:483 */     this.loadPath = path;
/* 466:484 */     updateIndex();
/* 467:    */     try
/* 468:    */     {
/* 469:486 */       BufferedWriter out = new BufferedWriter(new FileWriter(path));
/* 470:    */       
/* 471:488 */       out.write("<pathName>" + pathWayName);
/* 472:489 */       out.newLine();
/* 473:490 */       out.write("<xGridSize>" + this.xGridSize);
/* 474:491 */       out.newLine();
/* 475:492 */       out.write("<yGridSize>" + this.yGridSize);
/* 476:493 */       out.newLine();
/* 477:494 */       out.write("<nodes>");
/* 478:495 */       out.newLine();
/* 479:496 */       for (int i = 0; i < this.nodes.size(); i++)
/* 480:    */       {
/* 481:497 */         Node node = (Node)this.nodes.get(i);
/* 482:498 */         out.write("<name>" + node.name);
/* 483:499 */         out.newLine();
/* 484:500 */         out.write("<index>" + node.ownIndex);
/* 485:501 */         out.newLine();
/* 486:502 */         if ((node.xPos != -1) && (node.yPos != -1))
/* 487:    */         {
/* 488:503 */           out.write("<xPos>" + node.xPos);
/* 489:504 */           out.newLine();
/* 490:505 */           out.write("<yPos>" + node.yPos);
/* 491:506 */           out.newLine();
/* 492:    */         }
/* 493:508 */         out.write("<comment>" + node.comment);
/* 494:509 */         out.newLine();
/* 495:510 */         out.write("</node>");
/* 496:511 */         out.newLine();
/* 497:    */       }
/* 498:513 */       out.write("</nodes>");
/* 499:514 */       out.newLine();
/* 500:515 */       out.write("<connections>");
/* 501:516 */       out.newLine();
/* 502:517 */       for (int i = 0; i < this.nodes.size(); i++)
/* 503:    */       {
/* 504:518 */         Node node = (Node)this.nodes.get(i);
/* 505:519 */         out.write("<node>");
/* 506:520 */         out.newLine();
/* 507:521 */         out.write("<index>" + node.ownIndex);
/* 508:522 */         out.newLine();
/* 509:523 */         for (int j = 0; j < node.conections.size(); j++)
/* 510:    */         {
/* 511:524 */           Node con = (Node)node.conections.get(j);
/* 512:525 */           out.write("<conIndex>" + con.ownIndex);
/* 513:526 */           out.newLine();
/* 514:    */         }
/* 515:528 */         out.write("</node>");
/* 516:529 */         out.newLine();
/* 517:    */       }
/* 518:531 */       out.write("</connections>");
/* 519:532 */       out.newLine();
/* 520:533 */       out.close();
/* 521:    */     }
/* 522:    */     catch (IOException e)
/* 523:    */     {
/* 524:536 */       e.printStackTrace();
/* 525:    */     }
/* 526:    */   }
/* 527:    */   
/* 528:    */   public void openPathWay(String path)
/* 529:    */   {
/* 530:540 */     this.nodes = new ArrayList();
/* 531:541 */     this.loadPath = path;
/* 532:542 */     String name = "";
/* 533:    */     try
/* 534:    */     {
/* 535:544 */       BufferedReader in = new BufferedReader(new FileReader(path));
/* 536:    */       
/* 537:    */ 
/* 538:547 */       String comp = "<pathName>";
/* 539:    */       String line;
/* 540:548 */       while ((line = in.readLine()) != null)
/* 541:    */       {
//* 542:    */         String line;
/* 543:549 */         comp = "<pathName>";
/* 544:550 */         if (line.startsWith(comp))
/* 545:    */         {
/* 546:551 */           pathWayName = line.substring(line.indexOf(">") + 1);
/* 547:552 */           name = pathWayName;
/* 548:    */         }
/* 549:554 */         comp = "<xGridSize>";
/* 550:555 */         if (line.startsWith(comp))
/* 551:    */         {
/* 552:556 */           line = line.substring(line.indexOf(">") + 1);
/* 553:557 */           this.xGridSize = Integer.valueOf(line).intValue();
/* 554:    */         }
/* 555:560 */         comp = "<yGridSize>";
/* 556:561 */         if (line.startsWith(comp))
/* 557:    */         {
/* 558:562 */           line = line.substring(line.indexOf(">") + 1);
/* 559:563 */           this.yGridSize = Integer.valueOf(line).intValue();
/* 560:    */         }
/* 561:566 */         comp = "<nodes>";
/* 562:567 */         if (line.startsWith(comp)) {
/* 563:568 */           loadNodes(in);
/* 564:    */         }
/* 565:571 */         if (this.nodes != null)
/* 566:    */         {
/* 567:572 */           comp = "<connections>";
/* 568:573 */           if (line.startsWith(comp)) {
/* 569:574 */             loadConnections(in);
/* 570:    */           }
/* 571:    */         }
/* 572:    */       }
/* 573:578 */       in.close();
/* 574:    */     }
/* 575:    */     catch (Exception e)
/* 576:    */     {
/* 577:581 */       e.printStackTrace();
/* 578:    */     }
/* 579:583 */     if (pathWayName.isEmpty()) {
/* 580:584 */       pathWayName = name;
/* 581:    */     }
/* 582:    */   }
/* 583:    */   
/* 584:    */   private void loadConnections(BufferedReader in)
/* 585:    */   {
/* 586:591 */     Node node = null;
/* 587:592 */     Node connec = null;
/* 588:    */     try
/* 589:    */     {
/* 590:    */       String line;
/* 591:596 */       while ((line = in.readLine()) != null)
/* 592:    */       {
//* 593:    */         String line;
/* 594:597 */         String comp = "<index>";
/* 595:598 */         if (line.startsWith(comp))
/* 596:    */         {
/* 597:599 */           line = line.substring(line.indexOf(">") + 1);
/* 598:600 */           int index = Integer.valueOf(line).intValue();
/* 599:601 */           node = (Node)this.nodes.get(index);
/* 600:    */         }
/* 601:603 */         comp = "<conIndex>";
/* 602:604 */         if (line.startsWith(comp))
/* 603:    */         {
/* 604:605 */           line = line.substring(line.indexOf(">") + 1);
/* 605:606 */           int conIndex = Integer.valueOf(line).intValue();
/* 606:607 */           connec = (Node)this.nodes.get(conIndex);
/* 607:608 */           node.addConnection(connec);
/* 608:    */         }
/* 609:610 */         comp = "</connections>";
/* 610:611 */         if (line.startsWith(comp)) {
/* 611:612 */           return;
/* 612:    */         }
/* 613:    */       }
/* 614:    */     }
/* 615:    */     catch (IOException e)
/* 616:    */     {
/* 617:617 */       e.printStackTrace();
/* 618:    */     }
/* 619:    */   }
/* 620:    */   
/* 621:    */   private void loadNodes(BufferedReader in)
/* 622:    */   {
/* 623:622 */     System.out.println("loadNodes");
/* 624:    */     
/* 625:    */ 
/* 626:625 */     Node node = null;
/* 627:626 */     Node.resetIndexCnt();
/* 628:    */     try
/* 629:    */     {
/* 630:    */       String line;
/* 631:628 */       while ((line = in.readLine()) != null)
/* 632:    */       {
//* 633:    */         String line;
/* 634:629 */         String comp = "<name>";
/* 635:630 */         if (line.startsWith(comp))
/* 636:    */         {
/* 637:631 */           line = line.substring(line.indexOf(">") + 1);
/* 638:632 */           node = new Node(line);
/* 639:    */         }
/* 640:634 */         comp = "<index>";
/* 641:635 */         if (line.startsWith(comp))
/* 642:    */         {
/* 643:636 */           line = line.substring(line.indexOf(">") + 1);
/* 644:637 */           if (node != null) {
/* 645:638 */             node.ownIndex = Integer.valueOf(line).intValue();
/* 646:    */           }
/* 647:    */         }
/* 648:641 */         comp = "<xPos>";
/* 649:642 */         if (line.startsWith(comp))
/* 650:    */         {
/* 651:643 */           line = line.substring(line.indexOf(">") + 1);
/* 652:644 */           if (node != null) {
/* 653:645 */             node.xPos = Integer.valueOf(line).intValue();
/* 654:    */           }
/* 655:    */         }
/* 656:648 */         comp = "<yPos>";
/* 657:649 */         if (line.startsWith(comp))
/* 658:    */         {
/* 659:650 */           line = line.substring(line.indexOf(">") + 1);
/* 660:651 */           if (node != null) {
/* 661:652 */             node.yPos = Integer.valueOf(line).intValue();
/* 662:    */           }
/* 663:    */         }
/* 664:655 */         comp = "<comment>";
/* 665:656 */         if (line.startsWith(comp))
/* 666:    */         {
/* 667:657 */           line = line.substring(line.indexOf(">") + 1);
/* 668:658 */           if ((node != null) && 
/* 669:659 */             (line.contentEquals("true"))) {
/* 670:660 */             node.comment = true;
/* 671:    */           }
/* 672:    */         }
/* 673:664 */         comp = "</node>";
/* 674:665 */         if (line.startsWith(comp)) {
/* 675:666 */           addNode(node);
/* 676:    */         }
/* 677:668 */         comp = "</nodes>";
/* 678:669 */         if (line.startsWith(comp)) {
/* 679:670 */           return;
/* 680:    */         }
/* 681:    */       }
/* 682:    */     }
/* 683:    */     catch (IOException e)
/* 684:    */     {
/* 685:675 */       e.printStackTrace();
/* 686:    */     }
/* 687:    */   }
/* 688:    */   
/* 689:    */   public void previewPathway()
/* 690:    */   {
/* 691:679 */     Pathway path = new Pathway("", "");
/* 692:680 */     BufferedImage image = buildPicture(new PathwayWithEc(path), new Sample());
/* 693:681 */     ImagePanel.showImage(image, pathWayName);
/* 694:    */   }
/* 695:    */   
/* 696:    */   public BufferedImage buildPicture(PathwayWithEc path, Sample samp)
/* 697:    */   {
/* 698:684 */     int width = 800;
/* 699:685 */     int height = 600;
/* 700:686 */     BufferedImage image = new BufferedImage(width, height, 1);
/* 701:687 */     this.xStepSize = (width / (this.xGridSize + 2));
/* 702:688 */     this.yStepSize = (height / (this.yGridSize + 2));
/* 703:689 */     Graphics2D g = image.createGraphics();
/* 704:    */     
/* 705:    */ 
/* 706:692 */     int size = 10;
/* 707:693 */     int offset = size / 2;
/* 708:    */     
/* 709:    */ 
/* 710:696 */     g.setColor(Color.white);
/* 711:697 */     g.fillRect(0, 0, width, height);
/* 712:698 */     g.setColor(Color.black);
/* 713:699 */     if (path.name_ != null) {
/* 714:700 */       g.drawString("Pathname: " + path.name_, 5, 15);
/* 715:    */     } else {
/* 716:703 */       g.drawString("Pathname: no name", 5, 15);
/* 717:    */     }
/* 718:705 */     for (int i = 0; i < this.xGridSize + 1; i++) {
/* 719:706 */       for (int j = 0; j < this.yGridSize + 1; j++)
/* 720:    */       {
/* 721:707 */         g.setColor(Color.gray);
/* 722:708 */         g.drawLine(this.xStepSize, this.yStepSize + this.yStepSize * j, this.xStepSize + this.xStepSize * this.xGridSize, this.yStepSize + this.yStepSize * j);
/* 723:709 */         g.drawLine(this.xStepSize + this.xStepSize * i, this.yStepSize, this.xStepSize + this.xStepSize * i, this.yStepSize + this.yStepSize * this.yGridSize);
/* 724:    */       }
/* 725:    */     }
/* 726:712 */     g.setColor(Color.black);
/* 727:713 */     for (int i = 0; i < this.nodes.size(); i++)
/* 728:    */     {
/* 729:714 */       Node node = (Node)this.nodes.get(i);
/* 730:715 */       for (int j = 0; j < node.conections.size(); j++)
/* 731:    */       {
/* 732:716 */         if (node.comment) {
/* 733:717 */           g.setColor(Color.blue);
/* 734:    */         } else {
/* 735:720 */           g.setColor(Color.black);
/* 736:    */         }
/* 737:722 */         Node node2 = (Node)node.conections.get(j);
/* 738:723 */         int xPos = this.xStepSize + this.xStepSize * node.xPos;
/* 739:724 */         int yPos = this.yStepSize + this.yStepSize * node.yPos;
/* 740:725 */         int xPos2 = this.xStepSize + this.xStepSize * node2.xPos;
/* 741:726 */         int yPos2 = this.yStepSize + this.yStepSize * node2.yPos;
/* 742:727 */         g.setStroke(new BasicStroke(3.0F));
/* 743:728 */         g.drawLine(xPos, yPos, xPos2, yPos2);
/* 744:729 */         int dX = xPos - xPos2;
/* 745:730 */         int dy = yPos - yPos2;
/* 746:731 */         float length = (int)Math.sqrt(dX * dX + dy * dy);
/* 747:732 */         int vecX = (int)((xPos - xPos2) / length * (0.3D * length));
/* 748:733 */         int vecY = (int)((yPos - yPos2) / length * (0.3D * length));
/* 749:734 */         g.setStroke(new BasicStroke(1.0F));
/* 750:735 */         if (!node.comment) {
/* 751:736 */           g.fillOval(xPos2 + vecX - offset, yPos2 + vecY - offset, size, size);
/* 752:    */         }
/* 753:    */       }
/* 754:    */     }
/* 755:740 */     addNodesInPicture(g, path, samp);
/* 756:741 */     return image;
/* 757:    */   }
/* 758:    */   
/* 759:    */   private Graphics2D addNodesInPicture(Graphics2D g, PathwayWithEc path, Sample samp)
/* 760:    */   {
/* 761:745 */     EcNr actEcNr = null;
/* 762:    */     
/* 763:747 */     boolean ecInSample = false;
/* 764:748 */     int amount = 0;
/* 765:749 */     int boxLength = 0;
/* 766:750 */     for (int i = 0; i < this.nodes.size(); i++)
/* 767:    */     {
/* 768:751 */       Node node = (Node)this.nodes.get(i);
/* 769:752 */       boxLength = node.name.length() * this.charSpace;
/* 770:753 */       if (boxLength < 50) {
/* 771:754 */         boxLength = 50;
/* 772:    */       }
/* 773:756 */       int xPos = (int)(this.xStepSize + this.xStepSize * node.xPos - this.xStepSize * this.buttonfac / 2.0D);
/* 774:757 */       int yPos = (int)(this.yStepSize + this.yStepSize * node.yPos - this.yStepSize * this.buttonfac / 2.0D);
/* 775:758 */       int ySize = (int)(this.yStepSize * this.buttonfac);
/* 776:    */       
/* 777:760 */       ecInSample = false;
/* 778:761 */       for (int j = 0; j < path.ecNrs_.size(); j++) {
/* 779:762 */         if (((EcNr)path.ecNrs_.get(j)).name_.contentEquals(node.name))
/* 780:    */         {
/* 781:763 */           ecInSample = true;
/* 782:764 */           actEcNr = (EcNr)path.ecNrs_.get(j);
/* 783:    */         }
/* 784:    */       }
/* 785:767 */       if (samp.singleSample_)
/* 786:    */       {
/* 787:768 */         System.out.println("SingleSample" + node.comment);
/* 788:769 */         if (ecInSample) {
/* 789:770 */           g.setColor(samp.sampleCol_);
/* 790:    */         } else {
/* 791:773 */           g.setColor(Color.white);
/* 792:    */         }
/* 793:775 */         if (node.comment)
/* 794:    */         {
/* 795:776 */           System.out.println("SingleSample");
/* 796:777 */           g.setColor(Color.lightGray);
/* 797:778 */           g.fillRoundRect(xPos, yPos, boxLength, ySize, 5, 5);
/* 798:    */         }
/* 799:    */         else
/* 800:    */         {
/* 801:781 */           g.fillRect(xPos, yPos, boxLength, ySize);
/* 802:    */         }
/* 803:    */       }
/* 804:785 */       else if (ecInSample)
/* 805:    */       {
/* 806:786 */         ArrayList<EcSampleStats> stats = actEcNr.stats_;
/* 807:787 */         int step = 0;
/* 808:    */         
/* 809:    */ 
/* 810:790 */         amount = 0;
/* 811:791 */         for (int stsCnt = 0; stsCnt < stats.size(); stsCnt++) {
/* 812:792 */           amount += ((EcSampleStats)stats.get(stsCnt)).amount_;
/* 813:    */         }
/* 814:794 */         float stepSize = boxLength / amount;
/* 815:797 */         for (int stsCnt = 0; stsCnt < stats.size(); stsCnt++)
/* 816:    */         {
/* 817:798 */           Color col = ((EcSampleStats)stats.get(stsCnt)).col_;
/* 818:    */           
/* 819:800 */           g.setColor(col);
/* 820:    */           
/* 821:802 */           int length = (int)(((EcSampleStats)stats.get(stsCnt)).amount_ * stepSize);
/* 822:804 */           if (node.comment)
/* 823:    */           {
/* 824:805 */             System.out.println("SingleSample");
/* 825:806 */             g.setColor(Color.lightGray);
/* 826:807 */             g.fillRoundRect(xPos, yPos, length, ySize, 5, 5);
/* 827:    */           }
/* 828:    */           else
/* 829:    */           {
/* 830:810 */             g.fillRect(xPos + step, yPos, length, ySize);
/* 831:    */           }
/* 832:812 */           step += length;
/* 833:    */         }
/* 834:    */       }
/* 835:    */       else
/* 836:    */       {
/* 837:816 */         g.setColor(Color.white);
/* 838:817 */         if (node.comment)
/* 839:    */         {
/* 840:818 */           System.out.println("SingleSample");
/* 841:819 */           g.setColor(Color.lightGray);
/* 842:820 */           g.fillRoundRect(xPos, yPos, boxLength, ySize, 5, 5);
/* 843:    */         }
/* 844:    */         else
/* 845:    */         {
/* 846:823 */           g.fillRect(xPos, yPos, boxLength, ySize);
/* 847:    */         }
/* 848:825 */         g.fillRect(xPos, yPos, boxLength, ySize);
/* 849:    */       }
/* 850:828 */       g.setColor(Color.black);
/* 851:829 */       if (node.comment) {
/* 852:830 */         g.drawRoundRect(xPos, yPos, boxLength, (int)(this.yStepSize * this.buttonfac), 5, 5);
/* 853:    */       } else {
/* 854:833 */         g.drawRect(xPos, yPos, boxLength, (int)(this.yStepSize * this.buttonfac));
/* 855:    */       }
/* 856:835 */       g.drawString(node.name, xPos + 10, yPos + ySize / 2 + 2);
/* 857:    */     }
/* 858:838 */     return g;
/* 859:    */   }
/* 860:    */   
/* 861:    */   public void removeNode(int index)
/* 862:    */   {
/* 863:841 */     this.nodes.remove(index);
/* 864:    */     
/* 865:    */ 
/* 866:844 */     Node.nodeIndexCnt -= 1;
/* 867:845 */     for (int i = 0; i < this.nodes.size(); i++)
/* 868:    */     {
/* 869:846 */       Node node = (Node)this.nodes.get(i);
/* 870:847 */       if (node.ownIndex > index) {
/* 871:848 */         node.ownIndex -= 1;
/* 872:    */       }
/* 873:850 */       for (int j = 0; j < node.conections.size(); j++)
/* 874:    */       {
/* 875:851 */         Node node2 = (Node)node.conections.get(j);
/* 876:852 */         System.out.println(node2.name + " " + node2.ownIndex);
/* 877:    */       }
/* 878:    */     }
/* 879:    */   }
/* 880:    */   
/* 881:    */   public void addNode(Node node)
/* 882:    */   {
/* 883:857 */     if (this.nodes == null) {
/* 884:858 */       this.nodes = new ArrayList();
/* 885:    */     }
/* 886:860 */     this.nodes.add(node);
/* 887:    */   }
/* 888:    */   
/* 889:    */   public void incGridSize()
/* 890:    */   {
/* 891:863 */     this.xGridSize += 1;
/* 892:864 */     this.yGridSize += 1;
/* 893:    */   }
/* 894:    */   
/* 895:    */   public void decGridSize()
/* 896:    */   {
/* 897:867 */     if (this.xGridSize > 0) {
/* 898:868 */       this.xGridSize -= 1;
/* 899:    */     }
/* 900:870 */     if (this.yGridSize > 0) {
/* 901:871 */       this.yGridSize -= 1;
/* 902:    */     }
/* 903:    */   }
/* 904:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     pathwayLayout.PathLayoutGrid

 * JD-Core Version:    0.7.0.1

 */