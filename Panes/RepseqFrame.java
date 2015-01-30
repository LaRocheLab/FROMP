/*   1:    */ package Panes;
/*   2:    */ 
/*   3:    */ import Objects.ConvertStat;
/*   4:    */ import Objects.EcNr;
/*   5:    */ import Objects.Project;
/*   6:    */ import java.awt.Color;
/*   7:    */ import java.awt.event.MouseWheelEvent;
/*   8:    */ import java.awt.event.MouseWheelListener;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import javax.swing.JFrame;
/*  12:    */ import javax.swing.JLabel;
/*  13:    */ import javax.swing.JPanel;
/*  14:    */ import javax.swing.JTextArea;
/**/		  import java.io.PrintWriter;
/**/		  import java.io.File;
/*   33:   */ import javax.swing.JMenu;
/*   34:   */ import javax.swing.JMenuBar;
/*   35:   */ import javax.swing.JMenuItem;
/*   38:   */ import javax.swing.KeyStroke;
/*   15:   */ import java.awt.event.ActionEvent;
/*   16:   */ import java.awt.event.ActionListener;
/*   24:   */ import java.io.IOException;


/*  16:    */ public class RepseqFrame
/*  17:    */   extends JFrame
/*  18:    */ {
/*  19:    */   private static final long serialVersionUID = 1L;
				private JMenuBar menuBar_;
				private JMenu menu_;
				final String basePath_ = new File(".").getAbsolutePath() + File.separator;
/*  20:    */   EcNr ecNr_;
/*  21:    */   JLabel label_;
/*  22:    */   JPanel back_;
/*  23:    */   ArrayList<ConvertStat> reps_;
/*  24: 27 */   int xSize = 800;
/*  25:    */   int ySize;
				String sampName_;
/*  26:    */   
/*  27:    */   public RepseqFrame(ArrayList<ConvertStat> reps, EcNr ecNr, String sampName)
/*  28:    */   {
/*  29: 31 */     super(ecNr.name_ + " * " + reps.size());
/*  30:    */     this.sampName_=sampName;
/*  31: 33 */     this.reps_ = reps;

/*				  String test="";
				  String test2="";
				  for(int i=this.reps_.size()-1;i>=0;i--){
				  	test=((ConvertStat)this.reps_.get(i)).getDesc_();
				  	if(test.contains("\t")){
				  		this.reps_.set(i,null);
				  	}
				  	else{
//				  		innerloop:
				  		for(int j=this.reps_.size()-1;j>=0;j--){
				  			test2=((ConvertStat)this.reps_.get(j)).getDesc_();
				  			if(test2.contains(test)&&(i!=j)){
				  				this.reps_.set(i,null);
				  			}
				  		}
				  	}
				  }
				  for(int i=this.reps_.size()-1;i>=0;i--){
				  	if(this.reps_.get(i)==null){
				  		this.reps_.remove(i);
				  	}
				  }
*/
				 

/*  32: 34 */     this.ecNr_ = ecNr;
/*  33: 36 */     if (this.reps_ != null)
/*  34:    */     {
/*  35: 37 */       System.out.println("RepseqFrame " + this.reps_.size());
					if(this.reps_.size()<50){
/*  36: 38 */       	setBounds(100, 100, this.xSize, 100 + 25 * this.reps_.size());
					}
					else{setBounds(100, 100, this.xSize, 100 + 25);}
/*  37:    */     }
/*  38:    */     else
/*  39:    */     {
/*  40: 41 */       setBounds(100, 100, this.xSize, 100);
/*  41:    */     }
/*  42: 43 */     setResizable(false);
/*  43: 44 */     setVisible(true);
/*  44: 45 */     setLayout(null);
/*  45:    */     
/*  46: 47 */     this.back_ = new JPanel();
/*  47: 48 */     this.back_.setBackground(Project.getBackColor_());
/*  48: 49 */     if(this.reps_.size()<50)
				  {
					this.back_.setBounds(0, 0, this.xSize, 100 + 25 * this.reps_.size());
				  }
				  else
				  {
				  	this.back_.setBounds(100, 100, this.xSize, 100 + 25);
				  }
/*  49: 50 */     this.back_.setLayout(null);
/*  50: 51 */     this.back_.setVisible(true);
/*  51: 52 */     addMouseWheelListener(new MouseWheelListener()
/*  52:    */     {
/*  53:    */       public void mouseWheelMoved(MouseWheelEvent e)
/*  54:    */       {
/*  55: 57 */         System.out.print("move");
/*  56: 58 */         int count = e.getWheelRotation();
/*  57: 59 */         if (Math.abs(count) > 0)
/*  58:    */         {
/*  59: 60 */           int value = RepseqFrame.this.back_.getY() - count * 50;
/*  60: 61 */           RepseqFrame.this.back_.setLocation(RepseqFrame.this.back_.getX(), value);
/*  61: 62 */           RepseqFrame.this.repaint();
/*  62:    */         }
/*  63:    */       }
/*  64: 66 */     });
				  
/*  65: 67 */     add(this.back_);
/*  66: 68 */     if (this.reps_ != null) {
/*  67: 69 */       addrepseqs();
					addMenu();
/*  68:    */     }
				  
/*  69:    */   }
   
/*  71:    */   public RepseqFrame(ArrayList<ConvertStat> reps, String ecNr, int amount)
/*  72:    */   {
/*  73: 73 */     super(ecNr + " * " + amount);
/*  74:    */     this.sampName_="";
/*  75: 75 */     this.reps_ = reps;
/*  76: 77 */     if (this.reps_ != null)
/*  77:    */     {
/*  78: 78 */       System.out.println("RepseqFrame" + this.reps_.size());
/*  79: 79 */       if(this.reps_.size()<50)
					{
						setBounds(100, 100, this.xSize, 100 + 25 * this.reps_.size() + 5);
					}
					else
					{
						setBounds(100, 100, this.xSize, 130);
					}
/*  80:    */     }
/*  81:    */     else
/*  82:    */     {
/*  83: 82 */       setBounds(100, 100, this.xSize, 100);
/*  84:    */     }

/*  85: 84 */     setResizable(false);
/*  86: 85 */     setVisible(true);
/*  87: 86 */     setLayout(null);
/*  88:    */     
/*  89: 88 */     this.back_ = new JPanel();
/*  90: 89 */     this.back_.setBackground(Color.orange);
/*  91: 90 */     if(this.reps_.size()<50){this.back_.setBounds(0, 0, this.xSize, 50 + 25 * this.reps_.size());}
				  else{this.back_.setBounds(0, 0, this.xSize, 75 );}
/*  92: 91 */     this.back_.setLayout(null);
/*  93: 92 */     this.back_.setVisible(true);
/*  94: 93 */     addMouseWheelListener(new MouseWheelListener()
/*  95:    */     {
/*  96:    */       public void mouseWheelMoved(MouseWheelEvent e)
/*  97:    */       {
/*  98: 98 */         System.out.print("move");
/*  99: 99 */         if (RepseqFrame.this.back_.getY() <= 0)
/* 100:    */         {
/* 101:100 */           int count = e.getWheelRotation();
/* 102:101 */           int value = RepseqFrame.this.back_.getY() - count * 20;
/* 103:102 */           RepseqFrame.this.back_.setLocation(RepseqFrame.this.back_.getX(), value);
/* 104:103 */           RepseqFrame.this.repaint();
/* 105:    */         }
/* 106:    */       }
/* 107:108 */     });
				  
/* 108:109 */     add(this.back_);
/* 109:110 */     if (this.reps_ != null) {
/* 110:111 */       addrepseqs();
					addMenu();
/* 111:    */     }

/* 112:    */   }

				private void addMenu(){
					this.menuBar_ = new JMenuBar();

					this.menu_ = new JMenu("File");				

					this.menuBar_.add(this.menu_);
					JMenuItem miItem = new JMenuItem("Export",83);
					miItem.setAccelerator(KeyStroke.getKeyStroke(83, 8));

					miItem.addActionListener(new ActionListener()
				    {
				      public void actionPerformed(ActionEvent e)
				      {
				      	RepseqFrame.this.ExportReps();
				      }
				    });

				    this.menu_.add(miItem);
					setJMenuBar(this.menuBar_);
				}
		
				public void ExportReps()
				{
					String text="";
//               		String test="";
				    System.out.println("Reps:"+RepseqFrame.this.reps_.size());
				    for (int repCnt = 0; repCnt < RepseqFrame.this.reps_.size(); repCnt++)
					{
					  int amount = ((ConvertStat)RepseqFrame.this.reps_.get(repCnt)).getEcAmount_();
					  if (((ConvertStat)RepseqFrame.this.reps_.get(repCnt)).getPfamToEcAmount_() > amount) {
					    amount = ((ConvertStat)RepseqFrame.this.reps_.get(repCnt)).getPfamToEcAmount_();
					  }
//					  test=((ConvertStat)this.reps_.get(repCnt)).getDesc_() + "," + ((ConvertStat)this.reps_.get(repCnt)).getEcAmount_() + "," + ((ConvertStat)this.reps_.get(repCnt)).getPfamToEcAmount_() + "," + amount;
//					  if(!test.contains("\t")){
/* 140:148 */       	text = text + ((ConvertStat)this.reps_.get(repCnt)).getDesc_() + "," + ((ConvertStat)this.reps_.get(repCnt)).getEcAmount_() + "," + ((ConvertStat)this.reps_.get(repCnt)).getPfamToEcAmount_() + "," + amount;
/* 141:149 */       	text = text + "\n";
//					  }
					}
//					System.out.println("Text:\n"+text);
				    try
				    {
				      String sampleName;
				      if(sampName_.contains(".out")){
				      	sampleName=sampName_.replace(".out","");
				      }
				      else{
				      	sampleName=sampName_;
				      }
				      File file = new File(basePath_+"RepSeqIDs"+File.separator+sampleName+"-"+ecNr_.name_+".txt");
//				      file.getParentFile().mkdirs();
				      PrintWriter printWriter=new PrintWriter(file);
					  printWriter.println(""+text);
//					  System.out.println("Text:\n"+text);
					  printWriter.close (); 
					}
					catch (IOException e1)
					{
					  e1.printStackTrace();
					}
				}
  

/* 114:    */   private void addrepseqs()
/* 115:    */   {
/* 116:115 */     if(this.reps_.size()>50)
					{
					this.label_ = new JLabel("RepSeq , fromEc , fromPf , used Val.");
					this.label_.setBounds(5, 0, this.xSize, 20);
					this.label_.setLayout(null);
					this.back_.add(this.label_);

					JPanel line = new JPanel();
					line.setBounds(0, 20, this.xSize, 2);
					line.setBackground(Color.black);
					line.setLayout(null);
				    this.back_.add(line);
				    return;
				  }
				  this.label_ = new JLabel("RepSeq , fromEc , fromPf , used Val.");
/* 117:116 */     this.label_.setBounds(5, 0, this.xSize, 20);
/* 118:117 */     this.label_.setLayout(null);
/* 119:118 */     this.back_.add(this.label_);
/* 120:    */     
/* 121:120 */     JPanel line = new JPanel();
/* 122:121 */     line.setBounds(0, 20, this.xSize, 2);
/* 123:122 */     line.setBackground(Color.black);
/* 124:123 */     line.setLayout(null);
/* 125:124 */     this.back_.add(line);
/* 126:    */     
/* 127:126 */     JTextArea tArea = new JTextArea();
/* 128:127 */     tArea.setBounds(5, 22, this.xSize, 25 * this.reps_.size());
/* 129:128 */     tArea.setLayout(null);
/* 130:129 */     tArea.setEditable(false);
/* 131:130 */     this.back_.add(tArea);
/* 132:    */     
/* 133:132 */     String text = "";
				  String test="";
/* 134:134 */     for (int repCnt = 0; repCnt < this.reps_.size(); repCnt++)
/* 135:    */     {
/* 136:136 */       int amount = ((ConvertStat)this.reps_.get(repCnt)).getEcAmount_();
/* 137:137 */       if (((ConvertStat)this.reps_.get(repCnt)).getPfamToEcAmount_() > amount) {
/* 138:138 */         amount = ((ConvertStat)this.reps_.get(repCnt)).getPfamToEcAmount_();
/* 139:    */       }
//					test=((ConvertStat)this.reps_.get(repCnt)).getDesc_() + "," + ((ConvertStat)this.reps_.get(repCnt)).getEcAmount_() + "," + ((ConvertStat)this.reps_.get(repCnt)).getPfamToEcAmount_() + "," + amount;
//					if(!test.contains("\t")){
/* 140:148 */       	text = text + ((ConvertStat)this.reps_.get(repCnt)).getDesc_() + "," + ((ConvertStat)this.reps_.get(repCnt)).getEcAmount_() + "," + ((ConvertStat)this.reps_.get(repCnt)).getPfamToEcAmount_() + "," + amount;
/* 141:149 */       	text = text + "\n";
//					}
/* 142:    */     }
/* 143:152 */     tArea.setText(text);
/* 144:    */   }
/* 145:    */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.RepseqFrame

 * JD-Core Version:    0.7.0.1

 */