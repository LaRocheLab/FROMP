package Objects;

import java.awt.Color;
import java.util.ArrayList;

/**
 * GONum Object stored GO Number, seqID mapped to this GO number, 
 * and unused GO Number.
 * (Sometimes, IPR key or Uniref key could map to multiple GO number.we only use first one,meanwhile,store all unused GO Number in this object);
 * @author zs 
 *
 */
public class GONum {
	
	public String GoNumber = "";
	private String GoTerm = "";
	public int amount_ =0; //amount of go number in a sample
	public int sampleNr_ = 0;
	public Color samColor_;
	public ArrayList<GoSampleStats> stats_= new ArrayList<GoSampleStats>();;
	public ArrayList<Repseqs> repseqs_ = new ArrayList<Repseqs>();
	public String type_; 
	public boolean isPfam_ = false; 
	public boolean unmapped = false; 
	public boolean unique_ =false;
	
	public GONum(String[] Go){
		this.repseqs_ = new ArrayList<Repseqs>();
		
		this.GoNumber = Go[0] ;
		if (isNumber(Go[1])) {
			this.amount_ = Integer.valueOf(Go[1]).intValue();
			if (this.amount_ == 0) {
				this.amount_ = 0;
			}
		} else {
			this.amount_ = 1;
		}
		this.type_ = Go[2];
		if (this.type_.contentEquals("PF")) {
			this.isPfam_ = true;
		}
		
		addRepseq(Go[3], this.amount_);
		
		
	}

	public String nameSuppl() {
		if (this.unmapped) {
			return " #";
		}
		if (this.unique_) {
			return " *";
		}
		
		return "";
	}
	
	public GONum(String GoNumber){
		this.GoNumber = GoNumber;
	}

	public GONum(String GoNumber, String GoTerm){
		this.GoNumber = GoNumber;
		this.GoTerm = GoTerm;
	}

	public String getGoNumber() {
		return GoNumber;
	}



	public void setGoNumber(String goNumber) {
		GoNumber = goNumber;
	}



	public String getGoTerm() {
		return GoTerm;
	}



	public void setGoTerm(String goName) {
		GoTerm = goName;
	}



	public int getAmount_() {
		return amount_;
	}



	public void setAmount_(int amount_) {
		this.amount_ = amount_;
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


	public boolean isSameGo(GONum goNr) {
		
		return this.GoNumber.contentEquals(goNr.GoNumber);
	}
	
	public GONum(GONum goNr) {
		
		this.repseqs_ = new ArrayList<Repseqs>();
		
		this.stats_ = new ArrayList<GoSampleStats>();
		
		this.GoNumber = goNr.GoNumber;
		
		this.amount_ = goNr.amount_;
		
		this.GoTerm = goNr.GoTerm;
	
		this.unmapped = goNr.unmapped;
		this.isPfam_ = goNr.isPfam_;
		
		for (int repCnt = 0; repCnt < goNr.repseqs_.size(); repCnt++) {
			this.repseqs_.add((Repseqs) goNr.repseqs_.get(repCnt));
		}
		for (int statcnt = 0; statcnt < goNr.stats_.size(); statcnt++) {
			this.stats_.add((GoSampleStats) goNr.stats_.get(statcnt));
		}
	}
	
	public void addSample(GONum goNr) {
		GoSampleStats tmpstats = new GoSampleStats(goNr);

		this.stats_.add(tmpstats);
	}
}
