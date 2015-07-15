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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

// The "Show score-plot" tab of the pathway completeness analysis

public class PathwayPlot extends JPanel {
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
	float scale_ = 0.5F; 
	JButton scaleDown_; 
	JButton scaleUp_; 
	JButton export_; 
	DataProcessor proc_; 

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

	private void initPanels() {// innitiates the toolbar_ and the imageLabel_
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

		label = new JLabel("Scale " + this.scale_ * 100.0F);
		label.setBounds(320, 30, 100, 20);
		label.setLayout(null);
		label.setVisible(true);
		this.toolbar_.add(label);
		this.toolbar_.add(this.scaleUp_);
		this.toolbar_.add(this.scaleDown_);
		this.toolbar_.add(this.export_);
	}

	public void initCheckBoxes() {// initiates all of the checkboxes and their actionlisteners, does not add them to the panel
		this.linesBox_ = new JCheckBox();
		this.linesBox_.setBackground(Project.standard);
		this.linesBox_.setBounds(120, 20, 17, 16);
		this.linesBox_.setLayout(null);
		this.linesBox_.setVisible(true);
		this.linesBox_.setSelected(true);
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
		this.vertLineBox_ = new JCheckBox();
		this.vertLineBox_.setBackground(Project.standard);
		this.vertLineBox_.setBounds(220, 20, 17, 16);
		this.vertLineBox_.setLayout(null);
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
				if (PathwayPlot.this.scale_ < 5.0F) {
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
		this.export_ = new JButton("Export");
		this.export_.setBounds(220, 50, 100, 20);
		this.export_.setVisible(true);
		this.export_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathwayPlot.this.export();
			}
		});
	}

	public void plot() {// builds the plot
		int size = (int) (10.0F * this.scale_);
		int xStep = (int) (10.0F * this.scale_);
		int yStep = (int) (10.0F * this.scale_);
		int yOffset = 20;
		int lastX = 30;
		int lastY = yOffset + yStep * 100;
		int x = 30;
		int y = yOffset + yStep * 100;

		this.canvas_ = null;

		System.gc();
		this.canvas_ = new BufferedImage((int) (1600.0F * this.scale_),
				(int) (1200.0F * this.scale_), 2);
		this.canvas_.createGraphics();
		Sample tmpSamp = (Sample) Project.samples_.get(0);
		PathwayWithEc tmpPath = null;
		Graphics g = this.canvas_.getGraphics();
		g.setColor(Project.getBackColor_());
		g.fillRect(0, 0, (int) (2100.0F * this.scale_),
				(int) (2100.0F * this.scale_));

		g.setColor(Color.black);
		g.drawString("Score", 0, 20);
		for (int i = 0; i < 10; i++) {
			g.drawString(String.valueOf(100 - i * 10), 10, i * yStep * 10);
		}
		g.drawLine(30, yOffset, 30, yOffset + yStep * 100);

		g.drawLine(20, yOffset + yStep * 100, xStep * tmpSamp.pathways_.size(),
				yOffset + yStep * 100);

		g.drawString("Pathway", xStep * tmpSamp.pathways_.size(), yOffset
				+ yStep * 101);
		for (int i = 0; i < tmpSamp.pathways_.size(); i++) {
			g.drawString(((PathwayWithEc) tmpSamp.pathways_.get(i)).id_,
					(i + 1) * xStep + 30, yOffset
							+ (yStep * 101 + 10 * (i % 10)) + 20);
		}
		for (int smpCnt = 0; smpCnt < Project.samples_.size(); smpCnt++) {
			tmpSamp = (Sample) Project.samples_.get(smpCnt);
			lastX = 30;
			lastY = yOffset + yStep * 100;

			g.setColor(tmpSamp.sampleCol_);
			for (int pwCnt = 0; pwCnt < tmpSamp.pathways_.size(); pwCnt++) {
				tmpPath = (PathwayWithEc) tmpSamp.pathways_.get(pwCnt);
				x = 30 + (pwCnt + 1) * xStep;
				if (tmpPath.score_ > Project.minVisScore_ && tmpPath.score_ > 0) {
					y = yOffset
							+ (yStep * 100 - (int) (tmpPath.score_ * yStep));
				} else {
					y = yOffset + yStep * 100;
				}
				if ((!this.proc_.getPathway(tmpPath.id_).isSelected())
						|| (tmpPath.score_ < Project.minVisScore_)) {
					y = yOffset + yStep * 101;
				}
				Graphics2D g2 = (Graphics2D) g;
				if (this.linesBox_.isSelected()) {
					g2.setStroke(new BasicStroke(2.0F));
					g2.drawLine(lastX, lastY, x, y);
				}
				if (this.vertLineBox_.isSelected()) {
					g2.setStroke(new BasicStroke(2.0F));
					g2.drawLine(x, yOffset + yStep * 100, x, y);
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
}
