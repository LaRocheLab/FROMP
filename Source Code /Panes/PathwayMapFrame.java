package Panes;

import Objects.EcNr;
import Objects.EcSampleStats;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

public class PathwayMapFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	final Sample samp_;
	final PathwayWithEc path_;
	BufferedImage pathMap_;
	BufferedImage origPathMap_;
	JLabel image_;
	JLabel label_;
	final String workpath_;
	boolean resized = false;
	JButton butt;
	int oldwidth;
	int oldheight;
	JPanel ecAmounts;
	JLabel amount_;
	JButton export_;
	JButton exportValues_;
	int xWidth;
	int yWidth;
	ArrayList<Sample> samples_;
	JCheckBox showBars_;
	JCheckBox showNumbers_;
	Graphics g_;
	int maxEcAmount;
	private JScrollPane showJPanel_;
	private JPanel displayP_;

	public PathwayMapFrame(Sample samp, PathwayWithEc path, String workpath) {
		System.out.println("unmatched pathmap");
		int colDist = 20;
		this.workpath_ = workpath;
		this.samp_ = samp;
		this.path_ = path;

		this.maxEcAmount = maxAmount(this.path_);

		setTitle(this.samp_.name_ + "   |   " + this.path_.id_ + "   |   "
				+ this.path_.name_);
		this.xWidth = (this.maxEcAmount + 200);
		this.yWidth = (path.ecNrs_.size() * colDist);
		setBounds(50, 50, 800, 600);
		this.oldwidth = (getWidth() - 50);
		this.oldheight = getHeight();
		setLayout(new BorderLayout());
		setVisible(true);

		this.displayP_ = new JPanel();
		this.displayP_.setLocation(0, 0);
		this.displayP_
				.setPreferredSize(new Dimension(this.xWidth, this.yWidth));
		this.displayP_.setSize(getPreferredSize());
		this.displayP_.setBackground(Project.getBackColor_());
		this.displayP_.setVisible(true);
		this.displayP_.setLayout(null);

		this.showJPanel_ = new JScrollPane(this.displayP_);
		this.showJPanel_.setVisible(true);
		this.showJPanel_.setVerticalScrollBarPolicy(20);
		this.showJPanel_.setHorizontalScrollBarPolicy(30);

		add("Center", this.showJPanel_);

		this.exportValues_ = new JButton("Export Values");
		this.exportValues_.setBounds(this.displayP_.getWidth() - 150, colDist
				* this.path_.ecNrs_.size() + 80, 150, 30);
		this.exportValues_.setLayout(null);
		this.exportValues_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayMapFrame.this.exportVals();
			}
		});
		this.displayP_.add(this.exportValues_);

		this.ecAmounts = new JPanel();
		this.ecAmounts.setLayout(null);
		this.ecAmounts.setVisible(true);
		this.ecAmounts.setBounds(0, 0, this.xWidth, this.yWidth);
		this.ecAmounts.setBackground(Color.gray);

		this.path_.sortEcs();

		int numEcs = 0;
		for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
			if (((EcNr) this.path_.ecNrs_.get(ecCnt)).unique_) {
				this.label_ = new JLabel(
						((EcNr) this.path_.ecNrs_.get(ecCnt)).name_ + " *");
			} else {
				this.label_ = new JLabel(
						((EcNr) this.path_.ecNrs_.get(ecCnt)).name_);
			}
			this.label_.setLayout(null);
			this.label_.setVisible(true);
			this.label_.setBounds(10, colDist * ecCnt, 100, colDist);
			this.ecAmounts.add(this.label_);
			if (this.samp_.singleSample_) {
				this.amount_ = new JLabel(
						String.valueOf(((EcNr) this.path_.ecNrs_.get(ecCnt)).amount_));
				this.amount_.setLayout(null);
				this.amount_.setVisible(true);
				this.amount_.setOpaque(true);
				this.amount_.setBounds(120, colDist * ecCnt + 1,
						((EcNr) this.path_.ecNrs_.get(ecCnt)).amount_,
						colDist - 2);
				this.amount_.setBackground(this.samp_.sampleCol_);
				this.ecAmounts.add(this.amount_);
			} else {
				int step = 0;
				ArrayList<EcSampleStats> tmpStats = ((EcNr) this.path_.ecNrs_
						.get(ecCnt)).stats_;
				for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++) {
					Color col = ((EcSampleStats) tmpStats.get(stsCnt)).col_;

					this.amount_ = new JLabel(
							String.valueOf(((EcSampleStats) tmpStats
									.get(stsCnt)).amount_));
					this.amount_.setLayout(null);
					this.amount_.setVisible(true);
					this.amount_.setOpaque(true);
					this.amount_.setBounds(120 + step, colDist * ecCnt + 1,
							((EcSampleStats) tmpStats.get(stsCnt)).amount_,
							colDist - 2);
					this.amount_.setBackground(col);
					this.ecAmounts.add(this.amount_);
					step += ((EcSampleStats) tmpStats.get(stsCnt)).amount_;
				}
			}
			numEcs = ecCnt;
		}
		for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
			if (((EcNr) this.path_.ecNrs_.get(ecCnt)).unique_) {
				this.label_ = new JLabel(
						((EcNr) this.path_.ecNrs_.get(ecCnt)).name_ + " *");
			} else {
				this.label_ = new JLabel(
						((EcNr) this.path_.ecNrs_.get(ecCnt)).name_);
			}
			this.label_.setLayout(null);
			this.label_.setVisible(true);
			this.label_.setBounds(10, colDist * (ecCnt + numEcs + 2), 100,
					colDist);
			this.ecAmounts.add(this.label_);
			if (this.samp_.singleSample_) {
				this.amount_ = new JLabel(
						String.valueOf(((EcNr) this.path_.ecNrs_.get(ecCnt)).amount_));
				this.amount_.setLayout(null);
				this.amount_.setVisible(true);
				this.amount_.setOpaque(true);
				this.amount_.setBounds(120, colDist * (ecCnt + numEcs + 2), 20,
						colDist - 2);
				this.amount_.setForeground(this.samp_.sampleCol_);
				this.amount_.setBackground(Color.white);
				this.ecAmounts.add(this.amount_);
			} else {
				int step = 0;
				ArrayList<EcSampleStats> tmpStats = ((EcNr) this.path_.ecNrs_
						.get(ecCnt)).stats_;
				for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++) {
					Color col = ((EcSampleStats) tmpStats.get(stsCnt)).col_;
					this.amount_ = new JLabel(
							String.valueOf(((EcSampleStats) tmpStats
									.get(stsCnt)).amount_));
					this.amount_.setLayout(null);
					this.amount_.setVisible(true);
					this.amount_.setOpaque(true);
					this.amount_.setBounds(120 + 20 * step, colDist
							* (ecCnt + numEcs + 2), 20, colDist - 2);
					this.amount_.setBackground(Color.white);
					this.amount_.setForeground(col);
					this.ecAmounts.add(this.amount_);
					step++;
				}
			}
		}
		this.displayP_.add(this.ecAmounts);
		invalidate();
		validate();
		repaint();
	}

	public PathwayMapFrame(ArrayList<Sample> samples, Sample samp,
			PathwayWithEc path, BufferedImage pathMap, String workpath) {
		System.out.println(path.id_ + " " + path.ecNrs_.size());
		this.samples_ = samples;
		this.workpath_ = workpath;
		this.samp_ = samp;
		this.path_ = path;
		this.maxEcAmount = maxAmount(this.path_);

		this.pathMap_ = new BufferedImage(pathMap.getWidth() + 1000,
				pathMap.getHeight() + 600, 1);
		this.origPathMap_ = pathMap;
		setTitle(this.samp_.name_ + "   |   " + this.path_.id_ + "   |   "
				+ this.path_.name_);
		this.xWidth = 1000;
		this.yWidth = (path.ecNrs_.size() * 10);
		setBounds(50, 50, 800, 600);
		this.oldwidth = (getWidth() - 50);
		this.oldheight = getHeight();
		setLayout(new BorderLayout());
		setVisible(true);

		this.displayP_ = new JPanel();
		this.displayP_.setLocation(0, 0);
		this.displayP_
				.setPreferredSize(new Dimension(this.xWidth, this.yWidth));
		this.displayP_.setSize(getPreferredSize());
		this.displayP_.setBackground(Project.getBackColor_());
		this.displayP_.setVisible(true);
		this.displayP_.setLayout(null);

		this.showJPanel_ = new JScrollPane(this.displayP_);
		this.showJPanel_.setVisible(true);
		this.showJPanel_.setVerticalScrollBarPolicy(20);
		this.showJPanel_.setHorizontalScrollBarPolicy(30);

		add("Center", this.showJPanel_);

		this.showBars_ = new JCheckBox("show barcharts");
		this.showBars_.setBounds(320, 0, 200, 12);
		this.showBars_.setSelected(true);
		this.showBars_.setBackground(Project.getBackColor_());
		this.showBars_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayMapFrame.this.drawImage();
			}
		});
		this.displayP_.add(this.showBars_);

		this.showNumbers_ = new JCheckBox("show matrix");
		this.showNumbers_.setBounds(320, 13, 200, 12);
		this.showNumbers_.setSelected(true);
		this.showNumbers_.setBackground(Project.getBackColor_());
		this.showNumbers_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayMapFrame.this.drawImage();
			}
		});
		this.displayP_.add(this.showNumbers_);

		this.ecAmounts = new JPanel();
		this.ecAmounts.setLayout(null);
		this.ecAmounts.setVisible(true);
		this.ecAmounts.setBounds(this.pathMap_.getWidth(), 0,
				this.maxEcAmount + 200, this.yWidth);
		this.ecAmounts.setBackground(Color.gray);

		this.path_.sortEcs();
		this.export_ = new JButton("Export Picture");
		this.export_.setBounds(10, 0, 150, 25);
		this.export_.setLayout(null);
		this.export_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayMapFrame.this.export();
			}
		});
		this.displayP_.add(this.export_);

		this.exportValues_ = new JButton("Export Values");
		this.exportValues_.setBounds(160, 0, 150, 25);
		this.exportValues_.setLayout(null);
		this.exportValues_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayMapFrame.this.exportVals();
			}
		});
		this.displayP_.add(this.exportValues_);

		drawImage();
	}

	public PathwayMapFrame(ArrayList<Sample> samples, Sample samp,
			PathwayWithEc path) {
		this.workpath_ = "";
		this.samp_ = samp;
		this.path_ = path;
		this.maxEcAmount = maxAmount(this.path_);
		this.xWidth = 1000;
		this.yWidth = (path.ecNrs_.size() * 10);
	}

	private void drawImage() {
		System.out.println("draw Image");
		Color col = Color.black;
		this.xWidth = maxAmount(this.path_);
		int lineHeight = 25;
		int height = 100 + (this.path_.ecNrs_.size() * lineHeight * 2 + Project.samples_
				.size() * lineHeight);
		if (height < this.origPathMap_.getHeight() + 200) {
			height = this.origPathMap_.getHeight() + 200;
		}
		System.out.println(this.xWidth);
		if ((this.showBars_.isSelected()) || (this.showNumbers_.isSelected())) {
			System.out.println("showbars");
			this.pathMap_ = new BufferedImage(this.origPathMap_.getWidth()
					+ this.xWidth + 1000, height, 1);
		} else {
			this.pathMap_ = new BufferedImage(this.origPathMap_.getWidth(),
					this.origPathMap_.getHeight(), 1);
		}
		if (this.yWidth < this.pathMap_.getHeight()) {
			this.yWidth = this.pathMap_.getHeight();
		}
		this.xWidth = this.pathMap_.getWidth();
		this.displayP_
				.setPreferredSize(new Dimension(this.xWidth, this.yWidth));
		this.pathMap_.createGraphics();
		this.g_ = this.pathMap_.getGraphics();
		this.g_.setColor(Color.white);
		this.g_.fillRect(0, 0, this.pathMap_.getWidth(),
				this.pathMap_.getHeight());
		this.g_.drawImage(this.origPathMap_, 0, 0, null);
		for (int i = 0; i < this.displayP_.getComponentCount(); i++) {
			if (this.displayP_.getComponent(i).getName() != null) {
				if (this.displayP_.getComponent(i).getName()
						.contentEquals("Image")) {
					this.displayP_.remove(i);
				}
			}
		}
		ImageIcon icon = new ImageIcon(this.pathMap_);
		this.image_ = new JLabel(icon);
		this.image_.setLayout(null);
		this.image_.setName("Image");
		this.image_.setBounds(0, 0, this.pathMap_.getWidth(),
				this.pathMap_.getHeight() + 100);
		

		this.image_.setVisible(true);

		int colDist = 20;

		int numEcs = 1;
		if (this.samp_.singleSample_) {
			this.g_.setColor(this.samp_.sampleCol_);
			this.g_.drawString("Sample: " + this.samp_.name_,
					this.origPathMap_.getWidth() + 20, 15 + colDist * numEcs);
			numEcs++;
		} else {
			for (int smpCnt = 0; smpCnt < this.samples_.size(); smpCnt++) {
				this.g_.setColor(((Sample) this.samples_.get(smpCnt)).sampleCol_);
				this.g_.drawString("Sample " + (smpCnt + 1) + ": "
						+ ((Sample) this.samples_.get(smpCnt)).name_,
						this.origPathMap_.getWidth() + 20, 15 + colDist
								* numEcs);
				numEcs++;
			}
		}
		if (this.showBars_.isSelected()) {
			for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
				numEcs++;
				this.g_.setColor(Color.black);
				if (((EcNr) this.path_.ecNrs_.get(ecCnt)).unique_) {
					this.g_.drawString(
							((EcNr) this.path_.ecNrs_.get(ecCnt)).name_ + "*:",
							this.origPathMap_.getWidth() + 20, 15 + (colDist
									* numEcs + 1));
				} else {
					this.g_.drawString(
							((EcNr) this.path_.ecNrs_.get(ecCnt)).name_ + ":",
							this.origPathMap_.getWidth() + 20, 15 + (colDist
									* numEcs + 1));
				}
				if (this.samp_.singleSample_) {
					this.g_.setColor(this.samp_.sampleCol_);
					this.g_.fillRect(this.origPathMap_.getWidth() + 120,
							colDist * numEcs + 1,
							((EcNr) this.path_.ecNrs_.get(ecCnt)).amount_,
							colDist - 2);
				} else {
					int step = 0;
					ArrayList<EcSampleStats> tmpStats = ((EcNr) this.path_.ecNrs_
							.get(ecCnt)).stats_;
					for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++) {
						col = ((EcSampleStats) tmpStats.get(stsCnt)).col_;

						this.g_.setColor(col);
						this.g_.fillRect(this.origPathMap_.getWidth() + 120
								+ step, colDist * numEcs + 1,
								((EcSampleStats) tmpStats.get(stsCnt)).amount_,
								colDist - 2);

						step += ((EcSampleStats) tmpStats.get(stsCnt)).amount_;
					}
				}
			}
		}
		if (this.showNumbers_.isSelected()) {
			for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
				this.g_.setColor(Color.black);
				numEcs++;
				if (((EcNr) this.path_.ecNrs_.get(ecCnt)).unique_) {
					this.g_.drawString(
							((EcNr) this.path_.ecNrs_.get(ecCnt)).name_ + "*:",
							this.origPathMap_.getWidth() + 20, 15 + colDist
									* (numEcs + 2));
				} else {
					this.g_.drawString(
							((EcNr) this.path_.ecNrs_.get(ecCnt)).name_ + ":",
							this.origPathMap_.getWidth() + 20, 15 + colDist
									* (numEcs + 2));
				}
				if (this.samp_.singleSample_) {
					this.g_.drawString(String.valueOf(((EcNr) this.path_.ecNrs_
							.get(ecCnt)).amount_),
							this.origPathMap_.getWidth() + 80, 15 + colDist
									* (numEcs + 2));
				} else {
					ArrayList<EcSampleStats> tmpStats = ((EcNr) this.path_.ecNrs_
							.get(ecCnt)).stats_;
					for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++) {
						col = ((EcSampleStats) tmpStats.get(stsCnt)).col_;
						this.g_.setColor(col);
						this.g_.drawString(
								String.valueOf(((EcSampleStats) tmpStats
										.get(stsCnt)).amount_),
								this.origPathMap_.getWidth()
										+ 20
										+ 120
										+ 30
										* ((EcSampleStats) tmpStats.get(stsCnt)).sampleNr_,
								15 + colDist * (numEcs + 2));
					}
				}
			}
		}
		this.displayP_.add(this.ecAmounts);
		this.image_.setLocation(0, 25);
		/*
		 * Adding url links for each mapped ec number in the Pathways
		 */
		
		
		for(int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++){
			if(this.path_.ecNrs_.get(ecCnt).getEcLabel()!=null){
			
				for(int i = 0; i < this.path_.ecNrs_.get(ecCnt).getEcLabel().size(); i++){
					JLabel tmpLabel = new JLabel();
					tmpLabel = this.path_.ecNrs_.get(ecCnt).getEcLabel().get(i);
					tmpLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
					this.image_.add(tmpLabel);
				}
			}
		}
		this.displayP_.add(this.image_);

		invalidate();
		validate();
		repaint();
	}

	public BufferedImage drawImage(BufferedImage image) {
		Color col = Color.black;
		this.xWidth = maxAmount(this.path_);
		int lineHeight = 25;
		int height = 100 + (this.path_.ecNrs_.size() * lineHeight * 2 + Project.samples_
				.size() * lineHeight);
		if (height < image.getHeight() + 200) {
			height = image.getHeight() + 200;
		}
		this.pathMap_ = new BufferedImage(
				image.getWidth() + this.xWidth + 1000, height, 1);
		if (this.yWidth < this.pathMap_.getHeight()) {
			this.yWidth = this.pathMap_.getHeight();
		}
		this.xWidth = this.pathMap_.getWidth();
		this.pathMap_.createGraphics();
		this.g_ = this.pathMap_.getGraphics();
		this.g_.setColor(Color.white);
		this.g_.fillRect(0, 0, this.pathMap_.getWidth(),
				this.pathMap_.getHeight());
		this.g_.drawImage(image, 0, 0, null);

		int colDist = 20;

		int numEcs = 1;
		//right side part1 sample name - PathWay Pic
		if (this.samp_.singleSample_) {
			this.g_.setColor(this.samp_.sampleCol_);
			this.g_.drawString("Sample: " + this.samp_.name_,
					image.getWidth() + 20, 15 + colDist * numEcs);
			numEcs++;
		} else {
			for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
				this.g_.setColor(((Sample) Project.samples_.get(smpCnt)).sampleCol_);
				this.g_.drawString("Sample " + (smpCnt + 1) + ": "
						+ ((Sample) Project.samples_.get(smpCnt)).name_,
						image.getWidth() + 20, 15 + colDist * numEcs);
				numEcs++;
			}
		}
		//right side part2 ec bar - PathWay Pic
		for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
			this.g_.setColor(Color.black);
			numEcs++;
			if (((EcNr) this.path_.ecNrs_.get(ecCnt)).unique_) {
				this.g_.drawString(((EcNr) this.path_.ecNrs_.get(ecCnt)).name_
						+ "*:", image.getWidth() + 20,
						15 + (colDist * numEcs + 1));
			} 
			else {
				this.g_.drawString(((EcNr) this.path_.ecNrs_.get(ecCnt)).name_
						+ ":", image.getWidth() + 20,
						15 + (colDist * numEcs + 1));
			}
			// one sample or multiple samples
			if (this.samp_.singleSample_) {
				this.g_.setColor(this.samp_.sampleCol_);
//				this.g_.fillRect(image.getWidth() + 120, colDist * numEcs + 1,
//						((EcNr) this.path_.ecNrs_.get(ecCnt)).amount_,
//						colDist - 2);
				this.g_.fillRect(image.getWidth() + 120, colDist * numEcs + 1,200,colDist - 2);
				
			} 
			else {
				int step = 0;
				ArrayList<EcSampleStats> tmpStats = ((EcNr) this.path_.ecNrs_
						.get(ecCnt)).stats_;
				// over all ec amount
				int allAmount = 0;
				for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++){
					allAmount += tmpStats.get(stsCnt).amount_;
					
				}
				
				
				for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++) {
					col = ((EcSampleStats) tmpStats.get(stsCnt)).col_;

					this.g_.setColor(col);
					double singleAmount = tmpStats.get(stsCnt).amount_;
					singleAmount = tmpStats.get(stsCnt).amount_;
					double weight = (singleAmount/allAmount)*100;
					double w1 =weight *2;
					this.g_.fillRect(image.getWidth() + 120 + step, colDist * numEcs + 1,(int)w1, colDist - 2);
//					int w2 = (int)weight;
//					this.g_.drawString(""+w2+"%", image.getWidth() + 120 + 200+stsCnt*30, colDist * numEcs + 1 + colDist - 4);

//					step += ((EcSampleStats) tmpStats.get(stsCnt)).amount_;
					step += w1;
				}
			}
		}
		
		//right side part3 ec with numbers - PathWay Pic
		for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
			numEcs++;
			this.g_.setColor(Color.black);
			if (((EcNr) this.path_.ecNrs_.get(ecCnt)).unique_) {
				this.g_.drawString(((EcNr) this.path_.ecNrs_.get(ecCnt)).name_
						+ "*:", image.getWidth() + 20, 15 + colDist
						* (numEcs + 2));
			} 
			else {
				this.g_.drawString(((EcNr) this.path_.ecNrs_.get(ecCnt)).name_
						+ ":", image.getWidth() + 20, 15 + colDist
						* (numEcs + 2));
			}
			if (this.samp_.singleSample_) {
				this.g_.drawString(String.valueOf(((EcNr) this.path_.ecNrs_
						.get(ecCnt)).amount_), image.getWidth() + 80, 15
						+ colDist * (numEcs + 2));
			} 
			else {
				ArrayList<EcSampleStats> tmpStats = ((EcNr) this.path_.ecNrs_
						.get(ecCnt)).stats_;
				for (int stsCnt = 0; stsCnt < tmpStats.size(); stsCnt++) {
					col = ((EcSampleStats) tmpStats.get(stsCnt)).col_;
					this.g_.setColor(col);
					this.g_.drawString(String.valueOf(((EcSampleStats) tmpStats
							.get(stsCnt)).amount_), image.getWidth() + 20 + 120
							+ 30
							* ((EcSampleStats) tmpStats.get(stsCnt)).sampleNr_,
							15 + colDist * (numEcs + 2));
				}
			}
		}
		return this.pathMap_;
	}

	private void export() {
		JFileChooser fChoose_ = new JFileChooser(this.path_.id_ + ".png");
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);
		File file = new File(this.path_.id_ + ".png");
		fChoose_.setSelectedFile(file);
		fChoose_.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if ((f.isDirectory())
						|| (f.getName().toLowerCase().endsWith(".png"))) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return ".png";
			}
		});
		if (fChoose_.showSaveDialog(getParent()) == 0) {
			try {
				String path = fChoose_.getSelectedFile().getCanonicalPath();
				exportPicTo(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			invalidate();
			validate();
			repaint();
		}
		System.out.println("Save");
	}

	public void exportPicTo(String path) {
		try {
			if (!path.endsWith(".png")) {
				path = path + ".png";
			}
			BufferedImage outimage = new BufferedImage(
					this.displayP_.getWidth(), this.displayP_.getHeight(), 1);
			this.displayP_.paint(outimage.createGraphics());
			ImageIO.write(this.pathMap_, "png", new File(path));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public BufferedImage getPicture() {
		return new BufferedImage(this.displayP_.getWidth(),
				this.displayP_.getHeight(), 1);
	}

	private void exportVals() {
		JFileChooser fChoose_ = new JFileChooser(this.path_.id_ + ".txt");
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);
		File file = new File(this.path_.id_ + ".txt");
		fChoose_.setSelectedFile(file);
		fChoose_.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if ((f.isDirectory())
						|| (f.getName().toLowerCase().endsWith(".txt"))) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return ".txt";
			}
		});
		if (fChoose_.showSaveDialog(getParent()) == 0) {
			try {
				String path = fChoose_.getSelectedFile().getCanonicalPath();
				if (!path.endsWith(".txt")) {
					path = path + ".txt";
				}
				BufferedWriter out = new BufferedWriter(new FileWriter(path));
				if (this.samp_.singleSample_) {
					out.write("!! Sample:" + this.samp_.name_ + " Pathway:"
							+ this.path_.id_ + "," + this.path_.name_);
					out.newLine();
					for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
						EcNr ecTmp = (EcNr) this.path_.ecNrs_.get(ecCnt);
						out.write(ecTmp.name_ + "/t" + ecTmp.amount_);
						out.newLine();
					}
				} else {
					int maxStats = 0;
					int maxIndex = 0;
					ArrayList<Integer> smpNrs = new ArrayList<Integer>();
					out.write("!! Sample:" + this.samp_.name_ + " Pathway:"
							+ this.path_.id_ + "," + this.path_.name_);
					out.newLine();
					for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
						EcNr ecTmp = (EcNr) this.path_.ecNrs_.get(ecCnt);
						if (ecTmp.stats_.size() > maxStats) {
							maxStats = ecTmp.stats_.size();
							maxIndex = ecCnt;
						}
					}
					for (int stsCnt = 0; stsCnt < maxStats; stsCnt++) {
						smpNrs.add(Integer
								.valueOf(((EcSampleStats) ((EcNr) this.path_.ecNrs_
										.get(maxIndex)).stats_.get(stsCnt)).sampleNr_));
					}
					out.write("EcNr");
					for (int nrCnt = 0; nrCnt < smpNrs.size(); nrCnt++) {
						out.write("," + smpNrs.get(nrCnt));
					}
					for (int ecCnt = 0; ecCnt < this.path_.ecNrs_.size(); ecCnt++) {
						EcNr ecTmp = (EcNr) this.path_.ecNrs_.get(ecCnt);
						out.write(ecTmp.name_);
						for (int stsCnt = 0; stsCnt < ecTmp.stats_.size()/* maxStats */; stsCnt++) {
							for (int nrCnt = 0; nrCnt < smpNrs.size(); nrCnt++) {
								if (((EcSampleStats) ecTmp.stats_.get(stsCnt)).sampleNr_ == ((Integer) smpNrs
										.get(nrCnt)).intValue()) {
									out.write(","
											+ ((EcSampleStats) ecTmp.stats_
													.get(stsCnt)).amount_);
								} else {
									out.write(",0");
								}
							}
						}
						out.newLine();
					}
				}
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			invalidate();
			validate();
			repaint();
		}
	}

	private int maxAmount(PathwayWithEc pw) {
		int maxEcCnt = 0;
		for (int ecCnt = 0; ecCnt < pw.ecNrs_.size(); ecCnt++) {
			if (maxEcCnt < ((EcNr) pw.ecNrs_.get(ecCnt)).amount_) {
				maxEcCnt = ((EcNr) pw.ecNrs_.get(ecCnt)).amount_;
			}
		}
		return maxEcCnt;
	}
}
