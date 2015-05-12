package Prog;

import Objects.EcNr;
import Objects.EcPosAndSize;
import Objects.PathwayWithEc;
import Objects.Sample;
import Panes.PathwayMapFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import pathwayLayout.PathLayoutGrid;

public class PathButt extends JButton {
	private static final long serialVersionUID = 1L;
	final Sample samp_;
	final PathwayWithEc path_;
	final ArrayList<Sample> samples_;
	final String workpath_;
	String tmpWeight = "";
	String tmpScore = "";
	String tmpName = "";
	PathwayMapFrame pathMap;
	private PngBuilder builder_;
	StringReader reader;
	XmlParser parser;
	String separator_ = File.separator;
	boolean chaining = true;

	public PathButt(ArrayList<Sample> samples, Sample samp, PathwayWithEc path,
			Color backCol, String workpath, int mode) {
		this.workpath_ = workpath;
		this.samp_ = samp;
		this.samples_ = samples;
		this.path_ = path;
		this.tmpWeight = String.valueOf(this.path_.weight_);
		if (this.tmpWeight.length() > 5) {
			this.tmpWeight = this.tmpWeight.substring(0, 5);
		}
		this.tmpScore = String.valueOf(this.path_.score_);
		if (this.tmpScore.length() > 5) {
			this.tmpScore = this.tmpScore.substring(0, 5);
		}
		setBackground(backCol);
		setOpaque(false);
		setForeground(this.samp_.sampleCol_);
		this.tmpName = this.path_.name_;
		if (this.tmpName.contentEquals("testPath")) {
			System.err.println("PathButt");
		}
		if (this.tmpName.length() > 30) {
			this.tmpName = this.tmpName.substring(0, 30);
		}
		if (mode == 0) {
			setText("<html>" + this.tmpName + "<br>ID:" + this.path_.id_
					+ "| Wgt:" + this.tmpWeight + "| Scr:" + this.tmpScore
					+ "</html>");
		} else if (mode == 1) {
			setText("<html>" + this.tmpName + "<br>score: " + this.tmpScore
					+ "</html>");
		}
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(PathButt.this.samp_.singleSample_);
				if (PathButt.this.path_.userPathway) {
					System.out.println("This 1");
					BufferedImage tmpImage = PathButt.this.buildUserPath(
							PathButt.this.path_.pathwayInfoPAth,
							PathButt.this.path_, PathButt.this.samp_);
					PathButt.this.pathMap = new PathwayMapFrame(
							PathButt.this.samples_, PathButt.this.samp_,
							PathButt.this.path_, tmpImage,
							PathButt.this.workpath_);
					PathButt.this.pathMap.invalidate();
					PathButt.this.pathMap.validate();
					PathButt.this.pathMap.repaint();
				} else if (PathButt.this.path_.score_ > 0.0D) {
					System.out.println("or this 2");
					BufferedImage tmpImage = PathButt.this.showPathwayMap(
							PathButt.this.samp_, PathButt.this.path_);
					PathButt.this.pathMap = new PathwayMapFrame(
							PathButt.this.samples_, PathButt.this.samp_,
							PathButt.this.path_, tmpImage,
							PathButt.this.workpath_);
					PathButt.this.pathMap.invalidate();
					PathButt.this.pathMap.validate();
					PathButt.this.pathMap.repaint();
				} else {
					try {
						if (PathButt.this.path_.id_ != "-1") {
							System.out.println("else if this 3");
							BufferedImage tmpImage = ImageIO
									.read(new File("pics" + File.separator
											+ PathButt.this.path_.id_ + ".png"));
							PathButt.this.pathMap = new PathwayMapFrame(
									PathButt.this.samples_,
									PathButt.this.samp_, PathButt.this.path_,
									tmpImage, PathButt.this.workpath_);
						} else {
							System.out.println("else this 4");
							PathButt.this.pathMap = new PathwayMapFrame(
									PathButt.this.samp_, PathButt.this.path_,
									PathButt.this.workpath_);
						}
						PathButt.this.pathMap.invalidate();
						PathButt.this.pathMap.validate();
						PathButt.this.pathMap.repaint();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	private BufferedImage buildUserPath(String loadPath, PathwayWithEc pathway,
			Sample samp) {
		PathLayoutGrid grid = new PathLayoutGrid(10, 10, false);
		grid.openPathWay(loadPath);
		BufferedImage image = grid.buildPicture(pathway, samp);
		return image;
	}

	public BufferedImage showPathwayMap(Sample sample, PathwayWithEc path) {
		return alterPathway(sample, path);
	}

	public BufferedImage alterPathway(Sample sample, PathwayWithEc pathway) {
		boolean found = false;
		if (this.builder_ == null) {
			this.builder_ = new PngBuilder();
		}
		if (this.reader == null) {
			this.reader = new StringReader();
		}
		if (this.parser == null) {
			this.parser = new XmlParser();
		}
		BufferedReader xmlPath = this.reader.readTxt("pathway"
				+ this.separator_ + pathway.id_ + ".xml");
		ArrayList<EcPosAndSize> tmppos = new ArrayList();
		for (int ecCount = 0; ecCount < pathway.ecNrs_.size(); ecCount++) {
			xmlPath = this.reader.readTxt("pathway" + this.separator_
					+ pathway.id_ + ".xml");
			tmppos = this.parser.findEcPosAndSize(
					((EcNr) pathway.ecNrs_.get(ecCount)).name_, xmlPath);
			((EcNr) pathway.ecNrs_.get(ecCount)).addPos(tmppos);
			if (tmppos != null) {
				found = true;
			}
		}
		if (found) {
			return this.builder_.getAlteredPathway(pathway, sample);
		}
		try {
			return ImageIO.read(new File("pics" + this.separator_ + pathway.id_
					+ ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
