package Objects;

import java.awt.Color;
import java.io.BufferedReader;
import java.util.ArrayList;
import javax.swing.JLabel;

//An Ec object. Contains most of the info you need to know about a particular EC.

public class EcNr {
	public String name_; // Name of the ec
	public String bioName_; 
	public float weight_; // It's computed weight
	public int amount_; // The number of this ec that there are
	public Color samColor_; // Sample colour
	public int sampleNr_; 
	public ArrayList<EcPosAndSize> posSize_; // arrayList of positions and sizes of ecs
	public ArrayList<EcSampleStats> stats_; // arrayList of the sampleNr amount and colour of the ecs
	public ArrayList<JLabel> ecLabel;
	public int[] nr_; 
	public boolean unique_ = true; 
	public boolean isPfam_ = false; 
	public boolean incomplete = false; 
	public boolean unmapped = false; 
	public String type_; 
	public String rnNr_; 
	public int maxChainLength_; 
	public int longestChain_; 
	public double chainMultiply1_; 
	public double chainMultiply2_; 
	public boolean userEC = false; 
	static BufferedReader pfam2Go; // reader for pfam -> ec conversion file
	static BufferedReader Kegg2Go; // reader for Kegg-> go conversion
	public ArrayList<Repseqs> repseqs_; // An arraylist of sequence ids

	public EcNr(String[] name) {
		this.maxChainLength_ = 1;
		this.longestChain_ = 1;
		this.repseqs_ = new ArrayList<Repseqs>();
		this.posSize_ = new ArrayList<EcPosAndSize>();
		this.stats_ = new ArrayList<EcSampleStats>();
		ecLabel = new ArrayList<JLabel>();
		this.name_ = name[0];
		if (isNumber(name[1])) {
			this.amount_ = Integer.valueOf(name[1]).intValue();
			if (this.amount_ == 0) {
				this.amount_ = 0;
			}
		} else {
			this.amount_ = 1;
		}
		this.type_ = name[2];
		if (this.type_.contentEquals("PF")) {
			this.isPfam_ = true;
		}
		this.nr_ = new int[4];
		if (this.type_.contentEquals("EC")) {
			ecToVec();
		}
		addRepseq(name[3], this.amount_);
	}

	public EcNr(String name) {
		this.longestChain_ = 1;
		this.maxChainLength_ = 1;
		this.repseqs_ = new ArrayList<Repseqs>();
		this.posSize_ = new ArrayList<EcPosAndSize>();
		this.stats_ = new ArrayList<EcSampleStats>();
		ecLabel = new ArrayList<JLabel>();
		this.name_ = name;
		this.amount_ = 1;
		this.nr_ = new int[4];
		if ((this.name_.startsWith("PF")) || (this.name_.startsWith("R"))) {
			this.isPfam_ = true;
		}
	}

	public EcNr(EcNr ecNr) {
		this.maxChainLength_ = ecNr.maxChainLength_;
		if (this.maxChainLength_ == 0) {
			this.maxChainLength_ = 1;
		}
		this.longestChain_ = 1;
		this.repseqs_ = new ArrayList<Repseqs>();
		this.posSize_ = new ArrayList<EcPosAndSize>();
		this.stats_ = new ArrayList<EcSampleStats>();
		ecLabel = new ArrayList<JLabel>();
		this.name_ = ecNr.name_;
		this.weight_ = ecNr.weight_;
		this.amount_ = ecNr.amount_;
		this.samColor_ = ecNr.samColor_;
		this.bioName_ = ecNr.bioName_;
		this.nr_ = new int[4];
		this.unique_ = ecNr.unique_;
		this.incomplete = ecNr.incomplete;
		this.unmapped = ecNr.unmapped;
		this.isPfam_ = ecNr.isPfam_;
		for (int i = 0; i < this.nr_.length; i++) {
			this.nr_[i] = ecNr.nr_[i];
		}
		for (int repCnt = 0; repCnt < ecNr.repseqs_.size(); repCnt++) {
			this.repseqs_.add((Repseqs) ecNr.repseqs_.get(repCnt));
		}
		for (int statcnt = 0; statcnt < ecNr.stats_.size(); statcnt++) {
			this.stats_.add((EcSampleStats) ecNr.stats_.get(statcnt));
		}
	}

	public int getStatsSum() {
		int ret = 0;
		for (int i = 0; i < this.stats_.size(); i++) {
			ret += ((EcSampleStats) this.stats_.get(i)).amount_;
		}
		return ret;
	}

	public void setWeight(float w) {
		this.weight_ = w;
	}
	
	

	public ArrayList<JLabel> getEcLabel() {
		return ecLabel;
	}

	public void setEcLabel(ArrayList<JLabel> ecLabel) {
		this.ecLabel = ecLabel;
	}

	public void adaptWeightToChain() {
		this.chainMultiply1_ = Math.sqrt(this.maxChainLength_);
		this.chainMultiply2_ = (Math.sqrt(this.longestChain_) * Math
				.sqrt(this.maxChainLength_));
	}

	public void increaseAmount(int amount) {
		this.amount_ += amount;
	}

	public void increaseAmount(EcNr ecnr) {
		this.amount_ += ecnr.amount_;
	}

	public void setSample(Color samcol, short SampleNr) {
		this.samColor_ = samcol;
		this.sampleNr_ = SampleNr;
	}

	public void addPos(ArrayList<EcPosAndSize> pos) {
		for (int i = 0; i < pos.size(); i++) {
			this.posSize_.add((EcPosAndSize) pos.get(i));
		}
	}

	public void addSample(EcNr ecNr) {
		EcSampleStats tmpstats = new EcSampleStats(ecNr);

		this.stats_.add(tmpstats);
	}

	public boolean isSameEc(EcNr ec) {
		return this.name_.contentEquals(ec.name_);
	}
	/**
	 * ec# from string to int[]. 
	 */
	public void ecToVec() {
		String tmpec = this.name_;
		if (this.name_.contains("°")) {
			return;
		}
		int index = 0;
		while (tmpec.length() > 0) {
			if (tmpec.charAt(0) == '-') {
				while (index < this.nr_.length) {
					this.nr_[index] = -1;
					index++;
				}
				break;
			}
			if (tmpec.indexOf(".") < 1) {
				this.nr_[index] = Integer.valueOf(tmpec).intValue();
				break;
			}
			String tmp = tmpec.substring(0, tmpec.indexOf("."));
			tmpec = tmpec.substring(tmpec.indexOf(".") + 1);

			this.nr_[index] = Integer.valueOf(tmp).intValue();
			index++;
		}
	}

	public boolean isNumber(String in) {
		if (in == null) {
			return false;
		}
		if (in.length() < 1) {
			return false;
		}
		for (int i = 0; i < in.length(); i++) {
			if (!isNumber(in.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean isNumber(char c) {
		if ((c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4')
				|| (c == '5') || (c == '6') || (c == '7') || (c == '8')
				|| (c == '9')) {
			return true;
		}
		return false;
	}

	private void addRepseq(String repseq, int amount) {
		for (int repCnt = 0; repCnt < this.repseqs_.size(); repCnt++) {
			if (((Repseqs) this.repseqs_.get(repCnt)).isSameReps(repseq)) {
				((Repseqs) this.repseqs_.get(repCnt)).addAmount(amount);
				System.out.println("repseq added");
				return;
			}
		}
		this.repseqs_.add(new Repseqs(repseq, amount));
	}

	public boolean isCompleteEc() {
		String ec = this.name_;
		if (ec == null) {
			return false;
		}
		if (!ec.contains(".")) {
			return false;
		}
		String ecPart = "";
		String ecRest = ec;
		ecPart = ecRest.substring(0, ecRest.indexOf("."));
		ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
		if (!isNumber(ecPart)) {
			return false;
		}
		if (!ecRest.contains(".")) {
			return false;
		}
		ecPart = ecRest.substring(0, ecRest.indexOf("."));
		ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
		if (!isNumber(ecPart)) {
			return false;
		}
		if (!ecRest.contains(".")) {
			return false;
		}
		ecPart = ecRest.substring(0, ecRest.indexOf("."));
		ecRest = ecRest.substring(ecRest.indexOf(".") + 1);
		if (!isNumber(ecPart)) {
			return false;
		}
		if (ecRest.contains("°")) {
			ecRest = ecRest.substring(0, ecRest.indexOf("°"));
		}
		if (isNumber(ecRest)) {
			return true;
		}
		return false;
	}

	public String nameSuppl() {
		if (this.unmapped) {
			return " #";
		}
		if (this.unique_) {
			return " *";
		}
		if (this.incomplete) {
			return " ^";
		}
		return "";
	}

	public String getFullName() {
		return this.name_ + nameSuppl();
	}
	
	

	public String getName_() {
		return name_;
	}

	public void setName_(String name_) {
		this.name_ = name_;
	}

	public boolean couldBeEc() {
		if (this.name_ == null) {
			return false;
		}
		if (this.name_.isEmpty()) {
			return false;
		}
		for (int charCtn = 0; charCtn < this.name_.length(); charCtn++) {
			char ch = this.name_.charAt(charCtn);
			if ((!isNumber(ch)) && (ch != '_') && (ch != '-') && (ch != '.')) {
				return false;
			}
		}
		return true;
	}

	public boolean isUnique_() {
		return this.unique_;
	}

	public boolean isIncomplete() {
		return this.incomplete;
	}

	public boolean isUnmapped() {
		return this.unmapped;
	}

	public void printEC() {
		System.out.println("-------------PrintEc-----------------");
		System.out.println("Name: " + this.name_ + " Id: " + this.bioName_);
		System.out.println("Amount: " + this.amount_);
		for (int i = 0; i < this.repseqs_.size(); i++) {
			System.out.println("repseq " + i + " "
					+ ((Repseqs) this.repseqs_.get(i)).repseqDesc_);
		}
		System.out
				.println("///////-------------PrintEc----------------/////////");
	}
}
