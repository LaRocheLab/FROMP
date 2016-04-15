package Objects;

/**
 * Holds the data for the sequence IDs.
 */

public class Repseqs {
	String repseqDesc_;
	int amount_;

	public Repseqs(String repseq, int am) {
		this.repseqDesc_ = repseq;
		this.amount_ = am;
	}

	public void addAmount(int amount) {
		this.amount_ += amount;
	}

	public boolean isSameReps(String desc) {
		if (this.repseqDesc_.contentEquals(desc)) {
			return true;
		}
		return false;
	}

	public String getRepseqDesc_() {
		return this.repseqDesc_;
	}

	public void setRepseqDesc_(String repseqDesc_) {
		this.repseqDesc_ = repseqDesc_;
	}

	public int getAmount_() {
		return this.amount_;
	}

	public void setAmount_(int amount_) {
		this.amount_ = amount_;
	}
}
