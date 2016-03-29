package Prog;

import Objects.EcNr;
import Objects.EcPosAndSize;
import Objects.EcSampleStats;
import Objects.PathwayWithEc;
import Objects.Sample;
import Panes.ImagePanel;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JLabel;


/**
 * Builds the PNG files which are outputted by FROMP
 * 
 * @author Jennifer Terpstra, Kevan Lynch
 */
public class PngBuilder {
	BufferedImage image; // The image being generated
	String separator_ = File.separator; // the file seperator used by this OS
	URI url;

	public BufferedImage getAlteredPathway(ArrayList<EcPosAndSize> posList,
			String pathwayId, Sample sample) {
		System.out.println("1");
		Color col = sample.sampleCol_;
		try {
			this.image = ImageIO.read(new File(StartFromp1.FolderPath+"pics" + this.separator_
					+ pathwayId + ".png"));
			reColorAllEcs(this.image, Color.white);
			for (int i = 0; i < posList.size(); i++) {
				EcPosAndSize tmpPos = (EcPosAndSize) posList.get(i);
				int xStart = tmpPos.x_ - tmpPos.width_ / 2;
				int yStart = tmpPos.y_ - tmpPos.height_ / 2;
				for (int x = xStart; x < xStart + tmpPos.width_; x++) {
					for (int y = yStart; y < yStart + tmpPos.height_; y++) {
						if ((x > 0) && (x < this.image.getWidth())) {
							if ((y > 0) && (y < this.image.getHeight())) {
								if (this.image.getRGB(x, y) != Color.BLACK
										.getRGB()) {
									this.image.setRGB(x, y, col.getRGB());
								}
							} else {
								System.err.println("y-out");
								System.err.println(tmpPos.x_);
								System.err.println(tmpPos.y_);
								System.err.println(tmpPos.width_);
								System.err.println(tmpPos.height_);
								System.err.println();
							}
						} else {
							System.err.println("x-out");
							System.err.println(tmpPos.x_);
							System.err.println(tmpPos.y_);
							System.err.println(tmpPos.width_);
							System.err.println(tmpPos.height_);
							System.err.println();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.image;
	}

	public void alterPathway(ArrayList<EcPosAndSize> posList, String pathwayId,
			Sample sample) {
		Color col = sample.sampleCol_;
		try {
			this.image = ImageIO.read(new File(StartFromp1.FolderPath+"pics" + this.separator_
					+ pathwayId + ".png"));
			reColorAllEcs(this.image, Color.white);
			for (int i = 0; i < posList.size(); i++) {
				EcPosAndSize tmpPos = (EcPosAndSize) posList.get(i);
				int xStart = tmpPos.x_ - tmpPos.width_ / 2;
				int yStart = tmpPos.y_ - tmpPos.height_ / 2;
				for (int x = xStart; x < xStart + tmpPos.width_; x++) {
					for (int y = yStart; y < yStart + tmpPos.height_; y++) {
						if ((x > 0) && (x < this.image.getWidth())) {
							if ((y > 0) && (y < this.image.getHeight())) {
								if (this.image.getRGB(x, y) != Color.BLACK
										.getRGB()) {
									this.image.setRGB(x, y, col.getRGB());
								}
							} else {
								System.err.println("y-out");
								System.err.println(tmpPos.x_);
								System.err.println(tmpPos.y_);
								System.err.println(tmpPos.width_);
								System.err.println(tmpPos.height_);
								System.err.println();
							}
						} else {
							System.err.println("x-out");
							System.err.println(tmpPos.x_);
							System.err.println(tmpPos.y_);
							System.err.println(tmpPos.width_);
							System.err.println(tmpPos.height_);
							System.err.println();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			File outputfile = new File("outpics" + this.separator_ + pathwayId
					+ ".png");
			ImageIO.write(this.image, "png", outputfile);
		} catch (IOException localIOException1) {
		}
	}

	public void alterPathway(PathwayWithEc tmpPath, Sample sample) {
		Color col = sample.sampleCol_;

		int stepper = 0;
		int statsCnt = 0;
	
		EcSampleStats tmpStats = null;
		try {
			this.image = ImageIO.read(new File(StartFromp1.FolderPath+"pics" + this.separator_
					+ tmpPath.id_ + ".png"));
			reColorAllEcs(this.image, Color.white);
			for (int ecCnt = 0; ecCnt < tmpPath.ecNrs_.size(); ecCnt++) {
				EcNr tmpEc = (EcNr) tmpPath.ecNrs_.get(ecCnt);
				System.out.println("possize " + tmpEc.posSize_.size());
				for (int i = 0; i < tmpEc.posSize_.size(); i++) {
					statsCnt = 0;
					EcPosAndSize tmpPos = (EcPosAndSize) tmpEc.posSize_.get(i);
					int xStart = tmpPos.x_ - tmpPos.width_ / 2;
					int yStart = tmpPos.y_ - tmpPos.height_ / 2;
					int statsSum = 0;
					for (int statCnt = 0; statsCnt < tmpEc.stats_.size(); statCnt++) {
						statsSum += ((EcSampleStats) tmpEc.stats_.get(statCnt)).amount_;
					}
					int sampSteps = tmpPos.width_ / statsSum;
					if (!sample.singleSample_) {
						tmpStats = (EcSampleStats) tmpEc.stats_.get(statsCnt);
						col = tmpStats.col_;
						stepper = 0;
					}
					statsCnt = 0;
					stepper = 0;
					for (int x = xStart; x < xStart + tmpPos.width_; x++) {
						if ((!sample.singleSample_)
								&& (stepper > tmpStats.amount_ * sampSteps)
								&& (statsCnt < tmpEc.stats_.size())) {
							statsCnt++;
							tmpStats = (EcSampleStats) tmpEc.stats_
									.get(statsCnt);
							col = tmpStats.col_;
						}
						for (int y = yStart; y < yStart + tmpPos.height_; y++) {
							if ((x > 0) && (x < this.image.getWidth())) {
								if ((y > 0) && (y < this.image.getHeight())) {
									if (this.image.getRGB(x, y) != Color.BLACK
											.getRGB()) {
										this.image.setRGB(x, y, col.getRGB());
									}
								} else {
									System.err.println("y-out");
									System.err.println(tmpPos.x_);
									System.err.println(tmpPos.y_);
									System.err.println(tmpPos.width_);
									System.err.println(tmpPos.height_);
									System.err.println();
								}
							} else {
								System.err.println("x-out");
								System.err.println(tmpPos.x_);
								System.err.println(tmpPos.y_);
								System.err.println(tmpPos.width_);
								System.err.println(tmpPos.height_);
								System.err.println();
							}
						}
						stepper++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			File outputfile = new File("outpics" + this.separator_
					+ tmpPath.id_ + ".png");
			ImageIO.write(this.image, "png", outputfile);
		} catch (IOException localIOException1) {
		}
	}

	public BufferedImage getAlteredPathway(PathwayWithEc tmpPath, Sample sample) {
		ArrayList<JLabel> labelList = new ArrayList<JLabel>();
		
		final Map<String,URI> urlList = new HashMap<String,URI>();
		Color col = sample.sampleCol_;

		int stepper = 0;
		int statsCnt = 0;
		EcSampleStats tmpStats = null;
		double perc = 0.0D;
		try {
			this.image = ImageIO.read(new File(StartFromp1.FolderPath+"pics" + this.separator_+ tmpPath.id_ + ".png"));
			reColorAllEcs(this.image, Color.white);
			for (int ecCnt = 0; ecCnt < tmpPath.ecNrs_.size(); ecCnt++) {
				EcNr tmpEc = (EcNr) tmpPath.ecNrs_.get(ecCnt);
				//System.out.println(tmpEc.name_);

				statsCnt = 0;
				for (int i = 0; i < tmpEc.posSize_.size(); i++) {
					statsCnt = 0;
					EcPosAndSize tmpPos = (EcPosAndSize) tmpEc.posSize_.get(i);
					try {
						//System.out.println(tmpPos.ecURL_);
						URI uri = new URI(tmpPos.ecURL_);
						urlList.put(tmpEc.name_, uri);
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					int xStart = tmpPos.x_ - tmpPos.width_ / 2;
					int yStart = tmpPos.y_ - tmpPos.height_ / 2;
					
					//making the colored portions of the pathway picture click able in order to open the ec numbers web page
					JLabel label = new JLabel();
					label.setToolTipText(tmpEc.name_);
					
					MouseListener ml = new MouseListener() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							if (evt.getClickCount() > 0) {
								Desktop desktop = Desktop.getDesktop();
								JLabel label = (JLabel) evt.getSource();
								try {
									desktop.browse(urlList.get(label.getToolTipText()));
								} catch (IOException e) {
									e.printStackTrace();
								}

							}
						}

						@Override
						public void mouseEntered(MouseEvent e) {
					
						}

						@Override
						public void mouseExited(MouseEvent e) {
						}

						@Override
						public void mousePressed(MouseEvent e) {
						}
						
						@Override
						public void mouseReleased(MouseEvent e) {
						}
					};
					
					label.addMouseListener(ml);
					labelList.add(label);
					label.setBounds(xStart, yStart+50, tmpPos.width_, tmpPos.height_);
					//if the pathway contains multiple instances of the same EC must be added to an arrayList of labels
					if(!tmpEc.getEcLabel().contains(label)){
						tmpEc.getEcLabel().add(label);
					}
					
					int statsSum = 0;
					for (int statCnt = 0; statCnt < tmpEc.stats_.size(); statCnt++) {
						statsSum += ((EcSampleStats) tmpEc.stats_.get(statCnt)).amount_;
						if (((EcSampleStats) tmpEc.stats_.get(statCnt)).amount_ == 0) {
							statsSum += tmpEc.amount_;
						}
					}
					if ((statsSum == 0) && (!sample.singleSample_)) {
						break;
					}
					if ((!sample.singleSample_)
							&& (statsCnt < tmpEc.stats_.size())) {
						tmpStats = (EcSampleStats) tmpEc.stats_.get(statsCnt);
						col = tmpStats.col_;
						stepper = 0;
						perc = (double) tmpStats.amount_ / (double) statsSum;
					}
					for (int x = xStart; x < xStart + tmpPos.width_; x++) {
						if ((!sample.singleSample_)
								&& (stepper > tmpPos.width_ * perc)) {
							if (statsCnt < tmpEc.stats_.size() - 1) {
								stepper = 0;
								statsCnt++;
								tmpStats = (EcSampleStats) tmpEc.stats_
										.get(statsCnt);
								col = tmpStats.col_;
								perc = (double) tmpStats.amount_
										/ (double) statsSum;
							}
						}
						for (int y = yStart; y < yStart + tmpPos.height_; y++) {
							if ((x > 0) && (x < this.image.getWidth())) {
								if (col == null) {
									col = Color.ORANGE;
								}
								if ((y > 0) && (y < this.image.getHeight())) {
									if (this.image.getRGB(x, y) != Color.BLACK.getRGB()) {
										this.image.setRGB(x, y, col.getRGB());
										
									}
								} else {
									System.err.println("y-out");
									System.err.println(tmpPos.x_);
									System.err.println(tmpPos.y_);
									System.err.println(tmpPos.width_);
									System.err.println(tmpPos.height_);
									System.err.println();
								}
							} else {
								System.err.println("x-out");
								System.err.println(tmpPos.x_);
								System.err.println(tmpPos.y_);
								System.err.println(tmpPos.width_);
								System.err.println(tmpPos.height_);
								System.err.println();
							}
						}
						stepper++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.image;
	}

	private void reColorAllEcs(BufferedImage image, Color c) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if ((image.getRGB(x, y) != Color.black.getRGB())
						&& (image.getRGB(x, y) != Color.white.getRGB())) {
					image.setRGB(x, y, c.getRGB());
				}
			}
		}
	}
}
