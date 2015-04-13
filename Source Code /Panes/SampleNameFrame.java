/*  1:   */ package Panes;
/*  2:   */ 
/*  3:   */ import Objects.Project;
/*  4:   */ import Objects.Sample;
/*  5:   */ import java.awt.event.ActionEvent;
/*  6:   */ import java.awt.event.ActionListener;
/*  7:   */ import java.util.ArrayList;
/*  8:   */ import javax.swing.JButton;
/*  9:   */ import javax.swing.JFrame;
/* 10:   */ import javax.swing.JLabel;
/* 11:   */ import javax.swing.JPanel;
/* 12:   */ import javax.swing.JTextField;

			// Frame for the express purpose of editing the name of a sample in the edit samples pane.
/* 13:   */ 
/* 14:   */ public class SampleNameFrame
/* 15:   */   extends JFrame
/* 16:   */ {
/* 17:   */   String sampleName;								// The current name of the sample
/* 18:   */   final int sampleIndex;							// As the name will be changing, the samples index in the Project.samples_ arraylist is nessesairy to keep track of the sample
/* 19:   */   JTextField txField;								// A text field that the user will overwrite to change the name of the sample
/* 20:   */   private static final long serialVersionUID = 1L;	// 
/* 21:   */   
/* 22:   */   public SampleNameFrame(int index, String name)
/* 23:   */   {
/* 24:27 */     this.sampleIndex = index;
/* 25:28 */     this.sampleName = name;
/* 26:   */     
/* 27:30 */     setBounds(200, 200, 250, 200);
/* 28:31 */     setVisible(true);
/* 29:32 */     setDefaultCloseOperation(2);
/* 30:33 */     setLayout(null);
/* 31:   */     
/* 32:35 */     addParts();
/* 33:   */   }
/* 34:   */   
/* 35:   */   private void addParts()
/* 36:   */   {// Adds components to the JFrame
/* 37:38 */     JPanel back = new JPanel();
/* 38:39 */     back.setBounds(0, 0, 250, 200);
/* 39:40 */     back.setBackground(Project.getBackColor_());
/* 40:41 */     back.setLayout(null);
/* 41:42 */     add(back);
/* 42:   */     
/* 43:44 */     JLabel info = new JLabel("change samplename");
/* 44:45 */     info.setBounds(10, 10, 220, 30);
/* 45:46 */     back.add(info);
/* 46:   */     
/* 47:48 */     this.txField = new JTextField(this.sampleName);
/* 48:49 */     this.txField.setBounds(10, 50, 220, 30);
/* 49:50 */     this.txField.addActionListener(new ActionListener()
/* 50:   */     {
/* 51:   */       public void actionPerformed(ActionEvent arg0)
/* 52:   */       {
/* 53:55 */         SampleNameFrame.this.sampleName = ((JTextField)arg0.getSource()).getText();
/* 54:56 */         SampleNameFrame.this.changeSampleName();
/* 55:57 */         SampleNameFrame.this.close();
/* 56:   */       }
/* 57:59 */     });
/* 58:60 */     back.add(this.txField);
/* 59:   */     
/* 60:62 */     JButton cancel = new JButton("Cancel");
/* 61:63 */     cancel.setBounds(10, 85, 100, 30);
/* 62:64 */     cancel.addActionListener(new ActionListener()
/* 63:   */     {
/* 64:   */       public void actionPerformed(ActionEvent arg0)
/* 65:   */       {
/* 66:69 */         SampleNameFrame.this.close();
/* 67:   */       }
/* 68:71 */     });
/* 69:72 */     back.add(cancel);
/* 70:   */     
/* 71:74 */     JButton save = new JButton("Save");
/* 72:75 */     save.setBounds(130, 85, 100, 30);
/* 73:76 */     save.addActionListener(new ActionListener()
/* 74:   */     {
/* 75:   */       public void actionPerformed(ActionEvent arg0)
/* 76:   */       {
/* 77:81 */         SampleNameFrame.this.sampleName = SampleNameFrame.this.txField.getText();
/* 78:82 */         SampleNameFrame.this.changeSampleName();
/* 79:83 */         SampleNameFrame.this.close();
/* 80:   */       }
/* 81:85 */     });
/* 82:86 */     back.add(save);
/* 83:   */   }
/* 84:   */   
/* 85:   */   private void changeSampleName()
/* 86:   */   {// Changes the name of the sample
/* 87:89 */     ((Sample)Project.samples_.get(this.sampleIndex)).name_ = this.sampleName;
/* 88:90 */     Project.dataChanged = true;
/* 89:   */   }
/* 90:   */   
/* 91:   */   private void close()
/* 92:   */   {
/* 93:93 */     setVisible(false);
/* 94:94 */     dispose();
/* 95:   */   }
/* 96:   */ }



/* Location:           C:\Users\Kevan\Fromp-v1.0\FROMP.jar

 * Qualified Name:     Panes.SampleNameFrame

 * JD-Core Version:    0.7.0.1

 */