package Objects;

import Prog.StringReader;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//This contains all of the pertinent data in each sample, including all of the ECs and the pathways they map to, all of the pathways and the ECs who map to them 
//as well as many other important pieces of data like the sample name, etc.

public class Sample {
	StringReader reader_; // String reader used to load new samples
	public String name_; // Name of the sample
	public String fullPath_; // The file path to the sample
	public Color sampleCol_; // the sample color
	public BufferedReader sample_; // Buffered reader used to load new samples
	public ArrayList<PathwayWithEc> pathways_; // Array list of pathways with ecs mapped to them in the sample
	public ArrayList<EcWithPathway> ecs_; // Array list of ecs with pathways in the sample
	public ArrayList<PathwayWithEc> rnPathways_; 
	public ArrayList<EcWithPathway> rns_; 
	public ArrayList<ConvertStat> conversions_; // the conversion statistics for ecs in this sample
	public boolean imported; 
	public boolean matrixSample = false; 
	public boolean inUse; 
	public boolean valuesSet; 
	public boolean singleSample_ = true; 
	public boolean legitSample = false; 
	public int indexNr = 0; 
	public boolean onoff = true; // Used for display purposes in the GUI
	//seq file path
	private String sequenceFile; 

	public Sample() {
		this.sample_ = null;
		this.fullPath_ = "";
		this.sequenceFile = "";
		this.sampleCol_ = Color.white;
		this.reader_ = new StringReader();
		this.ecs_ = new ArrayList<EcWithPathway>();
		this.pathways_ = new ArrayList<PathwayWithEc>();
		this.rns_ = new ArrayList<EcWithPathway>();
		this.rnPathways_ = new ArrayList<PathwayWithEc>();
		this.conversions_ = new ArrayList<ConvertStat>();
		this.inUse = false;
		this.valuesSet = false;
	}

	public Sample(String name, String fullpath) {
		this.name_ = name;
		this.fullPath_ = fullpath;
		this.sequenceFile = "";
		this.sampleCol_ = Color.white;
		this.reader_ = new StringReader();
		this.ecs_ = new ArrayList<EcWithPathway>();
		this.pathways_ = new ArrayList<PathwayWithEc>();
		this.rns_ = new ArrayList<EcWithPathway>();
		this.rnPathways_ = new ArrayList<PathwayWithEc>();
		this.conversions_ = new ArrayList<ConvertStat>();
	}
	
	public Sample(String name, String fullpath, Color col) {
		this.name_ = name;
		this.fullPath_ = fullpath;
		this.sequenceFile = "";
		this.sampleCol_ = col;
		this.reader_ = new StringReader();
		this.ecs_ = new ArrayList<EcWithPathway>();
		this.pathways_ = new ArrayList<PathwayWithEc>();
		this.rns_ = new ArrayList<EcWithPathway>();
		this.rnPathways_ = new ArrayList<PathwayWithEc>();
		this.conversions_ = new ArrayList<ConvertStat>();
		this.inUse = true;
	}

	public Sample(String name, String fullpath, Color col, boolean using) {
		this.name_ = name;
		this.fullPath_ = fullpath;
		this.sequenceFile = "";
		this.sampleCol_ = col;
		this.inUse = using;
		this.reader_ = new StringReader();
		this.ecs_ = new ArrayList<EcWithPathway>();
		this.pathways_ = new ArrayList<PathwayWithEc>();
		this.rns_ = new ArrayList<EcWithPathway>();
		this.rnPathways_ = new ArrayList<PathwayWithEc>();
		this.conversions_ = new ArrayList<ConvertStat>();
	}

	public Sample(Sample sample) {
		this.name_ = sample.name_;
		this.fullPath_ = sample.fullPath_;
		this.sequenceFile = sample.sequenceFile;
		this.sampleCol_ = sample.sampleCol_;
		this.reader_ = new StringReader();

		this.ecs_ = new ArrayList<EcWithPathway>();
		this.pathways_ = new ArrayList<PathwayWithEc>();
		this.rns_ = new ArrayList<EcWithPathway>();
		this.rnPathways_ = new ArrayList<PathwayWithEc>();
		this.conversions_ = new ArrayList<ConvertStat>();
	}

	public void setSequenceFile(String seq) {
		this.sequenceFile = seq;
	}

	public String getSequenceFile() {
		return this.sequenceFile;
	}

	public void setColor(Color col) {
		this.sampleCol_ = col;
	}

	public void loadSample() {
		this.sample_ = this.reader_.readTxt(this.fullPath_);
	}

	public ArrayList<PathwayWithEc> getPathways_() {
		return pathways_;
	}

	public void setPathways_(ArrayList<PathwayWithEc> pathways_) {
		this.pathways_ = pathways_;
	}

	public void clearPaths() {
		this.valuesSet = false;
		this.pathways_ = new ArrayList<PathwayWithEc>();
		this.ecs_ = new ArrayList<EcWithPathway>();
		this.rns_ = new ArrayList<EcWithPathway>();
		this.rnPathways_ = new ArrayList<PathwayWithEc>();
		this.conversions_ = new ArrayList<ConvertStat>();
	}

	public void addPaths(PathwayWithEc path) {
		if (this.pathways_ == null) {
			this.pathways_ = new ArrayList<PathwayWithEc>();
		}
		this.pathways_.add(path);
	}

	public void addRnPaths(PathwayWithEc path) {
		this.rnPathways_.add(path);
	}

	public void toggleUse() {
		if (this.inUse) {
			this.inUse = false;
		} else {
			this.inUse = true;
		}
	}

	public void toggleonoff() {
		if (this.onoff) {
			this.onoff = false;
		} else {
			this.onoff = true;
		}
	}

	public void addConvStats(ConvertStat stat) {
		if (this.conversions_ == null) {
			this.conversions_ = new ArrayList<ConvertStat>();
		}
		for (int statCnt = 0; statCnt < this.conversions_.size(); statCnt++) {
			if (((ConvertStat) this.conversions_.get(statCnt)).desc_
					.contentEquals(stat.desc_)) {
				((ConvertStat) this.conversions_.get(statCnt))
						.addStatsCnt(stat);

				return;
			}
		}
		this.conversions_.add(stat);
	}

	public void writeConvStats(String path) {
		try {
			String desc = "";
			int ecAm = 0;
			int pfamToEcAm = 0;
			int pfamToRnAm = 0;
			int ecAmSum = 0;
			int pfamToEcAmSum = 0;
			int pfamToRnAmSum = 0;
			BufferedWriter out = new BufferedWriter(new FileWriter(path
					+ this.name_ + ".txt"));
			out.write("! " + this.name_);
			out.newLine();
			for (int statCnt = 0; statCnt < this.conversions_.size(); statCnt++) {
				desc = ((ConvertStat) this.conversions_.get(statCnt)).desc_;
				ecAm = ((ConvertStat) this.conversions_.get(statCnt)).ecAmount_;
				ecAmSum += ecAm;
				pfamToEcAm = ((ConvertStat) this.conversions_.get(statCnt)).pfamToEcAmount_;
				pfamToEcAmSum += pfamToEcAm;
				pfamToRnAm = ((ConvertStat) this.conversions_.get(statCnt)).pfamToRnAmount_;
				pfamToRnAmSum += pfamToRnAm;
				out.write(desc + "," + ecAm + "," + pfamToEcAm + ","
						+ pfamToRnAm);
				out.newLine();
			}
			out.newLine();
			out.write("----SumUp----:," + ecAmSum + "," + pfamToEcAmSum + ","
					+ pfamToRnAmSum);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printConvStats() {
		String desc = "";
		int ecAm = 0;
		int pfamToEcAm = 0;
		int pfamToRnAm = 0;
		for (int statCnt = 0; statCnt < this.conversions_.size(); statCnt++) {
			desc = ((ConvertStat) this.conversions_.get(statCnt)).desc_;
			ecAm = ((ConvertStat) this.conversions_.get(statCnt)).ecAmount_;
			pfamToEcAm = ((ConvertStat) this.conversions_.get(statCnt)).pfamToEcAmount_;
			pfamToRnAm = ((ConvertStat) this.conversions_.get(statCnt)).pfamToRnAmount_;
			System.out.println(desc + "," + ecAm + "," + pfamToEcAm + ","
					+ pfamToRnAm);
		}
	}

	public void addEc(EcWithPathway ecNr) {
		for (int ecCnt = 0; ecCnt < this.ecs_.size(); ecCnt++) {
			if (ecNr.isSameEc((EcNr) this.ecs_.get(ecCnt))) {
				((EcWithPathway) this.ecs_.get(ecCnt)).amount_ += ecNr.amount_;
				for (int statCnt = 0; statCnt < ecNr.stats_.size(); statCnt++) {
					((EcWithPathway) this.ecs_.get(ecCnt)).stats_
							.add((EcSampleStats) ecNr.stats_.get(statCnt));
				}
				return;
			}
		}
		this.ecs_.add(ecNr);
	}

	public PathwayWithEc getPath(String pathId) {
		for (int pathCnt = 0; pathCnt < this.pathways_.size(); pathCnt++) {
			if (((PathwayWithEc) this.pathways_.get(pathCnt)).id_
					.contentEquals(pathId)) {
				return (PathwayWithEc) this.pathways_.get(pathCnt);
			}
		}
		return null;
	}

	public EcWithPathway getEc(String ecId) {
		for (int ecCnt = 0; ecCnt < this.ecs_.size(); ecCnt++) {
			if (((EcWithPathway) this.ecs_.get(ecCnt)).name_
					.contentEquals(ecId)) {
				return (EcWithPathway) this.ecs_.get(ecCnt);
			}
		}
		return null;
	}

	public void integratePathway(PathwayWithEc pathway) {
		PathwayWithEc newPath = new PathwayWithEc(pathway);
		for (int ecPwCnt = 0; ecPwCnt < pathway.ecNrs_.size(); ecPwCnt++) {
			EcNr pwEc = (EcNr) pathway.ecNrs_.get(ecPwCnt);
			for (int ecCnt = 0; ecCnt < this.ecs_.size(); ecCnt++) {
				EcWithPathway smpEc = (EcWithPathway) this.ecs_.get(ecCnt);
				if (pwEc.isSameEc(smpEc)) {
					newPath.addEc(smpEc);
					smpEc.addPathway(pathway);
					break;
				}
			}
		}
	}

	public void removeUserPath(String userPath) {
		for (int ecCnt = 0; ecCnt < this.ecs_.size(); ecCnt++) {
			EcWithPathway ecWpL = (EcWithPathway) this.ecs_.get(ecCnt);
			for (int pwCnt = 0; pwCnt < ecWpL.pathways_.size(); pwCnt++) {
				Pathway pw = (Pathway) ecWpL.pathways_.get(pwCnt);
				if (pw.name_.contentEquals(userPath)) {
					ecWpL.pathways_.remove(pwCnt);
					break;
				}
			}
		}
		for (int pwCnt = 0; pwCnt < this.pathways_.size(); pwCnt++) {
			Pathway pw = (Pathway) this.pathways_.get(pwCnt);
			if (pw.name_.contentEquals(userPath)) {
				this.pathways_.remove(pwCnt);
				break;
			}
		}
	}
}
