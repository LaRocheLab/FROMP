package Objects;

/**
 * This class holds information about the data (ie. which ec's were converted from pfams).
 * 
 *
 */
public class ConvertStat {
	String desc_;//seq id
	String ecNr_;
	String unusedEc= "No_unused_Ec";
	int ecAmount_;
	int pfamToEcAmount_;
	int pfamToRnAmount_;

	public ConvertStat(String desc, String ecName, int ecAm, int pfamToEcAm,int pfamToRnAm,String unusedEc) {
		this.desc_ = desc;
		this.ecAmount_ = ecAm;
		this.pfamToEcAmount_ = pfamToEcAm;
		this.pfamToRnAmount_ = pfamToRnAm;
		this.ecNr_ = ecName;
		if (unusedEc != ""){
			this.unusedEc = unusedEc;
		}
			
	}

	public void addStatsCnt(ConvertStat stat) {
		this.ecAmount_ += stat.ecAmount_;
		this.pfamToEcAmount_ += stat.pfamToEcAmount_;
		this.pfamToRnAmount_ += stat.pfamToRnAmount_;
	}

	public int getEcAmount_() {
		return this.ecAmount_;
	}

	public void setEcAmount_(int ecAmount_) {
		this.ecAmount_ = ecAmount_;
	}

	public String getDesc_() {
		return this.desc_;
	}

	public int getPfamToEcAmount_() {
		return this.pfamToEcAmount_;
	}

	public String getEcNr_() {
		return this.ecNr_;
	}
}
