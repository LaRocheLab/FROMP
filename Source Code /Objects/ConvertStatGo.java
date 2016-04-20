package Objects;

/**
 * This class holds information about the data. IPR,Uniref --> GO
 * 
 *
 */
public class ConvertStatGo {
	String desc_;//seq id
	String GoNr_;
	String unusedGo= "No_unused_GO";
	int goAmount_;
	int pfamToGoAmount_;
	int pfamToRnAmount_;

	public ConvertStatGo(String desc, String goName, int goAm, int pfamToGoAm,int pfamToRnAm,String unusedGo) {
		this.desc_ = desc;
		this.goAmount_ = goAm;
		this.pfamToGoAmount_ = pfamToGoAm;
		this.pfamToRnAmount_ = pfamToRnAm;
		this.GoNr_ = goName;
		if (unusedGo != ""){
			this.unusedGo = unusedGo;
		}
			
	}
	public void addStatsCnt(ConvertStatGo stat) {
		this.goAmount_ += stat.goAmount_;
		this.pfamToGoAmount_ += stat.pfamToGoAmount_;
		this.pfamToRnAmount_ += stat.pfamToRnAmount_;
	}

	public int getGoAmount_() {
		return this.goAmount_;
	}

	public void setGoAmount_(int goAmount_) {
		this.goAmount_ = goAmount_;
	}

	public String getDesc_() {
		return this.desc_;
	}

	public int getPfamToGoAmount_() {
		return this.pfamToGoAmount_;
	}

	public String getGoNr_() {
		return this.GoNr_;
	}
}
