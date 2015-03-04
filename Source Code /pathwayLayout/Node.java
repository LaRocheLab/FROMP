/*   1:    */ package pathwayLayout;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ 
/*   5:    */ public class Node
/*   6:    */ {
/*   7:  6 */   public boolean comment = false;
/*   8:    */   public String name;
/*   9:    */   public int xPos;
/*  10:    */   public int yPos;
/*  11: 10 */   public float xForce = 0.0F;
/*  12: 11 */   public float yForce = 0.0F;
/*  13:    */   public ArrayList<Node> conections;
/*  14: 14 */   static int nodeIndexCnt = 0;
/*  15:    */   public int oldIndex;
/*  16:    */   public int ownIndex;
/*  17: 18 */   public int switchNodeId = -1;
/*  18:    */   
/*  19:    */   public Node(String name)
/*  20:    */   {
/*  21: 21 */     this.name = name;
/*  22: 22 */     this.conections = new ArrayList();
/*  23: 23 */     this.ownIndex = nodeIndexCnt;
/*  24: 24 */     nodeIndexCnt += 1;
/*  25: 25 */     this.xPos = -1;
/*  26: 26 */     this.yPos = -1;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Node()
/*  30:    */   {
/*  31: 29 */     this.name = "";
/*  32: 30 */     this.conections = new ArrayList();
/*  33: 31 */     this.ownIndex = nodeIndexCnt;
/*  34: 32 */     nodeIndexCnt += 1;
/*  35: 33 */     this.xPos = -1;
/*  36: 34 */     this.yPos = -1;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Node(Node node, boolean withConections)
/*  40:    */   {
/*  41: 38 */     this.name = node.name;
/*  42: 39 */     this.conections = new ArrayList();
/*  43: 41 */     if (withConections) {
/*  44: 42 */       for (int i = 0; i < node.conections.size(); i++)
/*  45:    */       {
/*  46: 43 */         Node copy = new Node((Node)node.conections.get(i), false);
/*  47: 44 */         this.conections.add(copy);
/*  48:    */       }
/*  49:    */     }
/*  50: 47 */     this.ownIndex = node.ownIndex;
/*  51: 48 */     this.xPos = node.xPos;
/*  52: 49 */     this.yPos = node.yPos;
/*  53: 50 */     this.xForce = node.xForce;
/*  54: 51 */     this.yForce = node.yForce;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isConnectedWith(Node b)
/*  58:    */   {
/*  59: 54 */     Node compareNode = null;
/*  60: 55 */     for (int i = 0; i < this.conections.size(); i++)
/*  61:    */     {
/*  62: 56 */       compareNode = (Node)this.conections.get(i);
/*  63: 57 */       if (b == compareNode) {
/*  64: 58 */         return true;
/*  65:    */       }
/*  66:    */     }
/*  67: 61 */     return false;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void addConnection(Node node)
/*  71:    */   {
/*  72: 64 */     this.conections.add(node);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void adaptConnections(Node node)
/*  76:    */   {
/*  77: 68 */     for (int i = 0; i < this.conections.size(); i++)
/*  78:    */     {
/*  79: 69 */       Node coNode = (Node)this.conections.get(i);
/*  80: 70 */       if (coNode.ownIndex == node.ownIndex)
/*  81:    */       {
/*  82: 71 */         coNode.xPos = node.xPos;
/*  83: 72 */         coNode.yPos = node.yPos;
/*  84:    */       }
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public String getName()
/*  89:    */   {
/*  90: 77 */     if (this.name == null) {
/*  91: 78 */       return String.valueOf(this.ownIndex);
/*  92:    */     }
/*  93: 80 */     if (this.name.isEmpty()) {
/*  94: 81 */       return String.valueOf(this.ownIndex);
/*  95:    */     }
/*  96: 83 */     return this.name;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static void resetIndexCnt()
/* 100:    */   {
/* 101: 86 */     nodeIndexCnt = 0;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public float calcForceByConnections(ArrayList<Node> nodes)
/* 105:    */   {
/* 106: 90 */     float pullConst = 0.04F;
/* 107: 94 */     for (int i = 0; i < this.conections.size(); i++)
/* 108:    */     {
/* 109: 95 */       Node conNode = (Node)this.conections.get(i);
/* 110: 96 */       float vecX = conNode.xPos - this.xPos;
/* 111: 97 */       float vecY = conNode.yPos - this.yPos;
/* 112: 98 */       float length = getLength(vecX, vecY);
/* 113: 99 */       vecX /= length;
/* 114:100 */       vecY /= length;
/* 115:    */       
/* 116:102 */       vecX *= pullConst * length;
/* 117:103 */       vecY *= pullConst * length;
/* 118:104 */       this.xForce += vecX;
/* 119:105 */       this.yForce += vecY;
/* 120:106 */       conNode.xForce -= vecX;
/* 121:107 */       conNode.yForce -= vecY;
/* 122:    */     }
/* 123:109 */     for (int i = 0; i < nodes.size(); i++)
/* 124:    */     {
/* 125:110 */       Node conNode = (Node)nodes.get(i);
/* 126:111 */       if ((!isConnectedWith(conNode)) && (this.ownIndex != conNode.ownIndex))
/* 127:    */       {
/* 128:114 */         float vecX = conNode.xPos - this.xPos;
/* 129:115 */         float vecY = conNode.yPos - this.yPos;
/* 130:116 */         float length = getLength(vecX, vecY);
/* 131:117 */         vecX /= length;
/* 132:118 */         vecY /= length;
/* 133:    */         
/* 134:120 */         vecX *= -1.0F / (length * length);
/* 135:121 */         vecY *= -1.0F / (length * length);
/* 136:122 */         this.xForce += vecX;
/* 137:123 */         this.yForce += vecY;
/* 138:    */       }
/* 139:    */     }
/* 140:125 */     return getLength(this.xForce, this.yForce);
/* 141:    */   }
/* 142:    */   
/* 143:    */   private float getLength(float vecX, float vecY)
/* 144:    */   {
/* 145:128 */     float ret = (float)Math.abs(Math.sqrt(vecX * vecX + vecY * vecY));
/* 146:129 */     return ret;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void moveByForce(int xSize, int ySize)
/* 150:    */   {
/* 151:132 */     if ((this.xPos + this.xForce >= 0.0F) && (this.xPos + this.xForce <= xSize)) {
/* 152:133 */       this.xPos = ((int)(this.xPos + this.xForce));
/* 153:    */     }
/* 154:135 */     if ((this.yPos + this.yForce >= 0.0F) && (this.yPos + this.yForce <= ySize)) {
/* 155:136 */       this.yPos = ((int)(this.yPos + this.yForce));
/* 156:    */     }
/* 157:    */   }
/* 158:    */   
/* 159:    */   public boolean minDistToConKept(float minDist)
/* 160:    */   {
/* 161:144 */     for (int i = 0; i < this.conections.size(); i++)
/* 162:    */     {
/* 163:145 */       Node conNode = (Node)this.conections.get(i);
/* 164:146 */       float vecX = conNode.xPos - this.xPos;
/* 165:147 */       float vecY = conNode.yPos - this.yPos;
/* 166:148 */       float length = getLength(vecX, vecY);
/* 167:149 */       if (length < minDist) {
/* 168:150 */         return false;
/* 169:    */       }
/* 170:    */     }
/* 171:153 */     return true;
/* 172:    */   }
/* 173:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     pathwayLayout.Node

 * JD-Core Version:    0.7.0.1

 */