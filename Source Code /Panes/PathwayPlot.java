package Panes;

import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import Prog.DataProcessor;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;

// The "Show score-plot" tab of the pathway completeness analysis

public class PathwayPlot extends JPanel{
	private static final long serialVersionUID = 1L; 
	Project proj_; // the current project
	BufferedImage canvas_; 
	ImageIcon image_; 
	JLabel imageLabel_; // houses this.image_
	JPanel toolbar_; 
	JPanel showPanel_; 
	JScrollPane scrollPane_; // the scroll pane which houses the imageLabel_
	JCheckBox pointsBox_; 
	JCheckBox vertLineBox_; 
	JCheckBox linesBox_; 
	JCheckBox scoresBox_;
	JCheckBox showLegendScatter_;
	JCheckBox showLegendBar_;
	float scale_ = 0.5F; 
	JButton scaleDown_; 
	JButton scaleUp_; 
	JButton scatter_;
	JButton comboBar_;
	JButton export_; 
	JFreeChart chart;
	DataProcessor proc_; 
	String basePath_ = "";
	boolean hasLegendScatter = false;
	boolean hasLegendBar = false;
	boolean export = false;

	public PathwayPlot(Project proj, DataProcessor proc) {
		this.proj_ = proj;
		this.proc_ = proc;

		setBackground(Project.getBackColor_());
		this.canvas_ = new BufferedImage(2000, 2000, 2);

		setLayout(new BorderLayout());

		initCheckBoxes();

		setVisible(true);

		prePaint();
	}

	private void initPanels() {// initiates the toolbar_ and the imageLabel_
		this.toolbar_ = new JPanel();

		this.toolbar_.setPreferredSize(new Dimension(getWidth() - 20, 100));
		this.toolbar_.setLocation(0, 0);
		this.toolbar_.setBackground(Project.standard);
		this.toolbar_.setVisible(true);
		this.toolbar_.setLayout(null);
		add("First", this.toolbar_);

		this.imageLabel_ = new JLabel(this.image_);

		this.imageLabel_.setVisible(true);
	}

	private void addScrollPane() {// adds the scroll pane to this
		this.scrollPane_ = new JScrollPane(this.imageLabel_);
		this.scrollPane_.setVisible(true);
		this.scrollPane_.setVerticalScrollBarPolicy(20);
		this.scrollPane_.setHorizontalScrollBarPolicy(30);

		add("Center", this.scrollPane_);
	}

	private void addTools() {// adds the tools allowing the user to scale the image to their liking as well as choose the type of graph they would like to see
		JLabel label = new JLabel("Plot points");
		label.setBounds(20, 0, 100, 20);
		label.setLayout(null);
		label.setVisible(true);
		this.toolbar_.add(label);

		this.toolbar_.add(this.pointsBox_);

		label = new JLabel("Plot lines");
		label.setBounds(120, 0, 100, 20);
		label.setLayout(null);
		label.setVisible(true);
		this.toolbar_.add(label);

		this.toolbar_.add(this.linesBox_);

		label = new JLabel("Plot vertical");
		label.setBounds(220, 0, 100, 20);
		label.setLayout(null);
		label.setVisible(true);
		this.toolbar_.add(label);
		this.toolbar_.add(this.vertLineBox_);
		
		label = new JLabel("Plot scores");
		label.setBounds(20, 40, 100, 20);
		label.setLayout(null);
		label.setVisible(true);
		this.toolbar_.add(label);
		this.toolbar_.add(this.scoresBox_);

		label = new JLabel("Scale " + this.scale_ * 100.0F);
		label.setBounds(320, 30, 100, 20);
		label.setLayout(null);
		label.setVisible(true);
		this.toolbar_.add(label);
		this.toolbar_.add(this.scaleUp_);
		this.toolbar_.add(this.scaleDown_);
		this.toolbar_.add(this.export_);
		
		label = new JLabel("Show legend");
		label.setBounds(560, 30, 100, 20);
		label.setLayout(null);
		label.setVisible(true);
		this.toolbar_.add(label);
		this.toolbar_.add(showLegendScatter_);
		
		this.toolbar_.add(this.scatter_);
		
		label = new JLabel("Show legend");
		label.setBounds(800, 30, 100, 20);
		label.setLayout(null);
		label.setVisible(true);
		this.toolbar_.add(label);
		this.toolbar_.add(showLegendBar_);
		
		this.toolbar_.add(this.comboBar_);
	}

	public void initCheckBoxes() {// initiates all of the checkboxes and their actionlisteners, does not add them to the panel
		
		this.linesBox_ = new JCheckBox();
		this.linesBox_.setBackground(Project.standard);
		this.linesBox_.setBounds(120, 20, 17, 16);
		this.linesBox_.setLayout(null);
		this.linesBox_.setVisible(true);
		this.linesBox_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayPlot.this.prePaint();
			}
		});
		this.linesBox_.addVetoableChangeListener(new VetoableChangeListener() {
			public void vetoableChange(PropertyChangeEvent evt)
					throws PropertyVetoException {
				PathwayPlot.this.prePaint();
			}
		});
		this.pointsBox_ = new JCheckBox();
		this.pointsBox_.setBackground(Project.standard);
		this.pointsBox_.setBounds(20, 20, 17, 16);
		this.pointsBox_.setLayout(null);
		this.pointsBox_.setSelected(true);
		this.pointsBox_.setVisible(true);
		this.pointsBox_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayPlot.this.prePaint();
			}
		});
		this.pointsBox_.addVetoableChangeListener(new VetoableChangeListener() {
			public void vetoableChange(PropertyChangeEvent evt)
					throws PropertyVetoException {
				PathwayPlot.this.prePaint();
			}
		});
		//set scores box
		this.scoresBox_ = new JCheckBox();
		this.scoresBox_.setBackground(Project.standard);
		this.scoresBox_.setBounds(20, 60, 17, 16);
		this.scoresBox_.setLayout(null);
		this.scoresBox_.setSelected(false);
		this.scoresBox_.setVisible(true);
		this.scoresBox_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayPlot.this.prePaint();
			}
		});
		this.scoresBox_.addVetoableChangeListener(new VetoableChangeListener() {
			public void vetoableChange(PropertyChangeEvent evt)
					throws PropertyVetoException {
				PathwayPlot.this.prePaint();
			}
		});
		
		
		//plot scores button
		this.vertLineBox_ = new JCheckBox();
		this.vertLineBox_.setBackground(Project.standard);
		this.vertLineBox_.setBounds(220, 20, 17, 16);
		this.vertLineBox_.setLayout(null);
		this.vertLineBox_.setSelected(true);
		this.vertLineBox_.setVisible(true);
		this.vertLineBox_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayPlot.this.prePaint();
			}
		});
		this.vertLineBox_
				.addVetoableChangeListener(new VetoableChangeListener() {
					public void vetoableChange(PropertyChangeEvent evt)
							throws PropertyVetoException {
						PathwayPlot.this.prePaint();
					}
				});
		
		this.scaleUp_ = new JButton("Scale Up");
		this.scaleUp_.setBounds(320, 10, 150, 20);
		this.scaleUp_.setVisible(true);
		this.scaleUp_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (PathwayPlot.this.scale_ < 1.5F) {
					PathwayPlot.this.scale_ += 0.25F;
					PathwayPlot.this.prePaint();
				}
			}
		});
		this.scaleDown_ = new JButton("Scale Down");
		this.scaleDown_.setBounds(320, 50, 150, 20);
		this.scaleDown_.setVisible(true);
		this.scaleDown_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (PathwayPlot.this.scale_ > 0.25F) {
					PathwayPlot.this.scale_ -= 0.25F;
					PathwayPlot.this.prePaint();
				}
			}
		});
		this.export_ = new JButton("Export All");
		this.export_.setBounds(1050, 10, 100, 30);
		this.export_.setVisible(true);
		this.export_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayPlot.this.export();
				PathwayPlot.this.drawPlot(1, hasLegendScatter, true);
				PathwayPlot.this.drawPlot(2, hasLegendBar, true);
				
			}
		});
		this.scatter_ = new JButton("Show Scatter Plot");
		this.scatter_.setBounds(560, 10, 200, 20);
		this.scatter_.setVisible(true);
		this.scatter_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayPlot.this.drawPlot(1, hasLegendScatter, false);
			}
		});
		this.showLegendScatter_ = new JCheckBox();
		this.showLegendScatter_.setBackground(Project.standard);
		this.showLegendScatter_.setBounds(560, 50, 17, 16);
		this.showLegendScatter_.setLayout(null);
		this.showLegendScatter_.setVisible(true);
		this.showLegendScatter_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(showLegendScatter_.isSelected()){
					hasLegendScatter = true;
				}
				else{
					hasLegendScatter = false;
				}
				
			}
		});
		this.comboBar_ = new JButton("Show Bar Graph");
		this.comboBar_.setBounds(800, 10, 200, 20);
		this.comboBar_.setVisible(true);
		this.comboBar_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayPlot.this.drawPlot(2, hasLegendBar, false);
			}
		});
		this.showLegendBar_ = new JCheckBox();
		this.showLegendBar_.setBackground(Project.standard);
		this.showLegendBar_.setBounds(800, 50, 17, 16);
		this.showLegendBar_.setLayout(null);
		this.showLegendBar_.setVisible(true);
		this.showLegendBar_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(showLegendBar_.isSelected()){
					hasLegendBar = true;
				}
				else{
					hasLegendBar = false;
				}
				
			}
		});
	}

	public void plot() {// builds the plot
		int size = (int) (10.0F * this.scale_);
		int xStep = (int) (10.0F * this.scale_);
		int yStep = (int) (30.0F * this.scale_);
		int yOffset = 20;
		int xOffset = 20*3;
		
		int lastX = 0;
		int lastY = 30;
		
		int x = 0;
		int y = 30;

		this.canvas_ = null;

		System.gc();
		
		this.canvas_ = new BufferedImage((int) (1600.0F * this.scale_),
				(int) (Project.samples_.get(0).pathways_.size()*yStep*3 * this.scale_), 2);
	
		this.canvas_.createGraphics();
		Sample tmpSamp = (Sample) Project.samples_.get(0);
		PathwayWithEc tmpPath = null;
		Graphics g = this.canvas_.getGraphics();
		g.setColor(Project.getBackColor_());
//		if (scoresBox_.isSelected()){
//			g.setColor(Color.white);
//		}
		
//		g.fillRect(0, 0, (int) (2100.0F * this.scale_),
//				(int) (2100.0F * this.scale_));
		g.fillRect(0, 0, (int) (1600.0F * this.scale_),
				(int) (Project.samples_.get(0).pathways_.size()*yStep*3 * this.scale_));

		g.setColor(Color.black);
		//draw word "score" at x axis
		g.drawString("Score", xOffset+xStep*100, 20);
		//from top to bottom
		for (int i = 0; i < 10; i++) {
			//values of x axis(90 ,80,70......)
			g.drawString(String.valueOf(i * 10), xOffset+i * xStep * 10,20);
		}
		//draw line y axis. 0 to "score"
		g.drawLine(xOffset,30, xOffset + xStep * 100,30);
		//draw line x axis. 0 to pathway
		g.drawLine(xOffset, 30, xOffset,yStep * tmpSamp.pathways_.size()+90);
		//draw word "Pathway" at x axis
		g.drawString("Pathway", 0,yStep * tmpSamp.pathways_.size()+120);
		//draw pathway id (ec00010 ....)
		for (int i = 0; i < tmpSamp.pathways_.size(); i++) {
			g.drawString(((PathwayWithEc) tmpSamp.pathways_.get(i)).id_,0,(i + 1) * yStep+30);
		}
		//draw all samples
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			tmpSamp = (Sample) Project.samples_.get(smpCnt);
			//the position of last sample
			lastX = xOffset;
			lastY = 30;

			g.setColor(tmpSamp.sampleCol_);
			
			//all pathway score for each sample.
			for (int pwCnt = 0; pwCnt < tmpSamp.pathways_.size(); pwCnt++) {
				tmpPath = (PathwayWithEc) tmpSamp.pathways_.get(pwCnt);
				
				// pathway - line y position
				y = (int)((pwCnt + 1) * yStep + 25);
				
				//hides any pathways with a score below 0 or is 0
				if (tmpPath.score_ > Project.minVisScore_) {
					x = xOffset
							+  (int) (tmpPath.score_ * xStep);
				} 
				//pathway <= 0
				else {
					//y = yOffset + yStep * 100;
					x = xOffset;
				}
				//the project do not use this pathway or pathway score < minVisScore
				if ((!this.proc_.getPathway(tmpPath.id_).isSelected())
						|| (tmpPath.score_ < Project.minVisScore_ )) {
					//y = yOffset + yStep * 101;
					x = xOffset;
				}
				//draw picture
				Graphics2D g2 = (Graphics2D) g;
				if (this.linesBox_.isSelected()) {
					g2.setStroke(new BasicStroke(2.0F));
					g2.drawLine(lastX, lastY, x, y);
				}
				if (this.vertLineBox_.isSelected()) {
					g2.setStroke(new BasicStroke(2.0F));
					g2.drawLine(xOffset, y, x, y);
				}
				if (this.scoresBox_.isSelected()) {
					if(tmpPath.score_ > 0){
						
						g2.setStroke(new BasicStroke(2.0F));
						Font score = new Font ("Dialog", Font.PLAIN, 10);
						g2.setFont(score);
						g2.drawString(""+Math.round(tmpPath.score_), (x - size/2), (y + 2*size));
						
					}
					
				}
				if (this.pointsBox_.isSelected()) {
					g2.setStroke(new BasicStroke(2.0F));
					g2.fillOval(x - size / 2, y - size / 2, size, size);				
				}
				lastX = x;
				lastY = y;
			}
		}
		this.image_ = new ImageIcon(this.canvas_);

		this.imageLabel_ = new JLabel(this.image_);
		
		
       
		
	}

	private void export() {// exports the plot as a picture file
		JFileChooser fChoose_ = new JFileChooser("");
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);
		File file = new File("");
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
				basePath_ = path;
				if (!path.endsWith(".png")) {
					path = path + ".png";
				}
				ImageIO.write(this.canvas_, "png", new File(path));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			repaint();
		}
		System.out.println("Save");
	}

	public void prePaint() {// rebuilds the panel
		removeAll();
		initPanels();
		addTools();
		plot();
		addScrollPane();
		invalidate();
		validate();
		repaint();
	}
	
	/**
	 * Used to draw the Scatter plot and Stacked bar chart for the pathway scores. Uses Jfree chart which can be found at
	 * http://www.jfree.org/jfreechart/. 
	 * @param mode Tells the method with graph to draw. 1 for scatter and 2 for bar.
	 * @param showLegend Tells the method whether to draw a legend with the graphs
	 * 
	 * @author Jennifer Terpstra
	 */
	public void drawPlot(int mode, boolean showLegend, boolean export){
		//inputs all the pathway scores into the graph data set
		int totalSeries = Project.samples_.size();
		DefaultCategoryDataset tmpseries = new DefaultCategoryDataset();
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			Sample tmpSamp = Project.samples_.get(smpCnt);
			for (int pathCnt = 0; pathCnt < tmpSamp.getPathways_().size(); pathCnt++) {
				PathwayWithEc tmpPath = tmpSamp.getPathways_().get(pathCnt);
				//pathway score must be greater than min score and 0 in order to be put into graph dataset
				if (tmpPath.getScore() > Project.minVisScore_&& tmpPath.getScore() > 0) {
					tmpseries.addValue(tmpPath.getScore(), tmpSamp.name_, tmpPath.getId_());
				}
			}

		}
		
		CategoryAxis domainAxis;
		CategoryPlot categoryplot;
		
		//drawing the scatter plot graph for the pathway scores
		if (mode == 1) {
			chart = ChartFactory.createLineChart("Pathway Scores", "Pathway",
					"Scores", tmpseries, PlotOrientation.HORIZONTAL,
					showLegend, true, false);

			categoryplot = (CategoryPlot) chart.getPlot();

			categoryplot.setBackgroundPaint(Color.LIGHT_GRAY);
			categoryplot.setDomainGridlinePaint(Color.white);
			categoryplot.setDomainGridlinesVisible(true);
			categoryplot.setRangeGridlinesVisible(false);
			categoryplot.setDomainCrosshairVisible(true);
			categoryplot.setRangeCrosshairVisible(true);
			categoryplot.setRangeCrosshairLockedOnData(true);

			LineAndShapeRenderer renderer = (LineAndShapeRenderer) categoryplot
					.getRenderer();
			renderer.setBaseShapesVisible(true);
			renderer.setLinesVisible(false);
			//ensures that each series representing each sample contains the same colour as the original sample
			for (int i = 0; i < totalSeries; i++) {
				renderer.setSeriesFillPaint(i,(Project.samples_.get(i).sampleCol_));
				renderer.setUseFillPaint(true);
				renderer.setSeriesPaint(i, (Project.samples_.get(i).sampleCol_));
			}

			
		}
		//drawing the stacked bar graph for the pathway scores
		else {
			chart = ChartFactory.createStackedBarChart("Pathway Scores",
					"Pathway", "Scores", tmpseries, PlotOrientation.HORIZONTAL,
					showLegend, true, false);

			categoryplot = (CategoryPlot) chart.getPlot();

			categoryplot.setBackgroundPaint(Color.LIGHT_GRAY);
			categoryplot.setDomainGridlinePaint(Color.white);
			categoryplot.setDomainGridlinesVisible(true);
			categoryplot.setRangeGridlinesVisible(false);

			ValueAxis rangedAxis = categoryplot.getRangeAxis();
			rangedAxis.setVisible(false);
			rangedAxis.setVerticalTickLabels(false);

			BarRenderer renderer = (BarRenderer) categoryplot.getRenderer();
			renderer.setDrawBarOutline(false);
			renderer.setItemMargin(0.1);
			renderer.setMaximumBarWidth(0.1);
			
			//ensures that each series representing each sample contains the same colour as the original sample
			for (int i = 0; i < totalSeries; i++) {
				 final Paint gp0 = Project.samples_.get(i).sampleCol_;
				 renderer.setSeriesPaint(i, gp0);
			}
		}
		
		domainAxis = categoryplot.getDomainAxis();
		domainAxis.setLowerMargin(0.01);
		domainAxis.setCategoryMargin(0.2);
		
		//setting domain font type and size so that it is easily readable
		Font font = new Font("Dialog", Font.PLAIN, 7);
		domainAxis.setTickLabelFont(font);
		chart.setBackgroundPaint(Color.white);
		//Frame which the graph is drawn on
		
		if(export==false){
			JFrame showPlot = new JFrame();

			JPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(1000, 1000));
			chartPanel.setVisible(true);
			showPlot.getContentPane().setLayout(new FlowLayout());
			showPlot.setContentPane(chartPanel);
			showPlot.pack();
			showPlot.setVisible(true);
		}
		else{
			//used to export the charts when the export all button is pressed
			try {
				if(mode == 1){
					ChartUtilities.saveChartAsPNG(new File(basePath_ + " LineChart" + ".png"), chart, 1000, 1000);
				}
				else{
					ChartUtilities.saveChartAsPNG(new File(basePath_ + " BarChart" + ".png"), chart, 1000, 1000);
				}	
			} 
			catch (IOException e1) {
			}
		}
		 
	}

	
	
}
