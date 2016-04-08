package Objects;

import java.util.ArrayList;

//This is an extension of the EcNr class. This class includes all of the pathways associated with a particular EC as an  attribute.

public class EcWithPathway extends EcNr {
	public ArrayList<Pathway> pathways_;
	public boolean weightsSet;
	public boolean isSelected_ = true;

	public EcWithPathway(EcNr ecNr) {
		super(ecNr);
		this.maxChainLength_ = ecNr.maxChainLength_;

		this.pathways_ = new ArrayList<Pathway>();
		this.userEC = ecNr.userEC;
		this.weightsSet = false;
	}

	public EcWithPathway(EcWithPathway ecWp) {
		super(ecWp);
		this.maxChainLength_ = ecWp.maxChainLength_;
		this.pathways_ = new ArrayList<Pathway>();
		this.userEC = ecWp.userEC;
		if (ecWp.pathways_ != null) {
			for (int i = 0; i < ecWp.pathways_.size(); i++) {
				this.pathways_.add((Pathway) ecWp.pathways_.get(i));
			}
		}
	}

	public EcWithPathway(EcWithPathway ecWp, EcNr ecnr) {
		super(ecWp);
		this.maxChainLength_ = ecnr.maxChainLength_;
		this.pathways_ = new ArrayList<Pathway>();
		this.amount_ = ecnr.amount_;
		this.userEC = ecWp.userEC;
		if (ecWp.pathways_ != null) {
			for (int i = 0; i < ecWp.pathways_.size(); i++) {
				this.pathways_.add((Pathway) ecWp.pathways_.get(i));
			}
		}
		if (this.repseqs_.size() == 0) {
			this.repseqs_ = new ArrayList<Repseqs>();
			for (int repCnt = 0; repCnt < ecnr.repseqs_.size(); repCnt++) {
				this.repseqs_.add((Repseqs) ecnr.repseqs_.get(repCnt));
			}
		}
	}

	public void addPathway(Pathway pathway) {
		this.pathways_.add(new Pathway(pathway));
		if (this.pathways_.size() > 1) {
			this.unique_ = false;
		}
	}

	public void addStats() {
		if (this.pathways_.size() == 1) {
			if (((Pathway) this.pathways_.get(0)).id_ != "-1") {
				this.unique_ = true;
				this.unmapped = false;
				this.incomplete = false;
			} else {
				this.unique_ = false;
				this.unmapped = true;
			}
		}
		if (this.pathways_.size() > 1) {
			this.unique_ = false;
			this.unmapped = false;
			this.incomplete = false;
		}
		if (this.userEC) {
			System.out.println("userEC");
			return;
		}
		if (this.pathways_.size() == 0) {
			this.unique_ = false;
			this.unmapped = true;
		}
		if (!isCompleteEc()) {
			this.unique_ = false;
			this.unmapped = false;
			this.incomplete = true;
		}
	}

	public void printEC() {
		System.out.println("-------------PrintEc-----------------");
		System.out.println("Name: " + this.name_ + " Id: " + this.bioName_);
		System.out.println("Amount: " + this.amount_);
		System.out.println("PwAmount " + this.pathways_.size());
		for (int i = 0; i < this.pathways_.size(); i++) {
			System.out.println("pw Nr: " + i + " Id:"
					+ ((Pathway) this.pathways_.get(i)).name_);
		}
		System.out
				.println("///////-------------PrintEc----------------/////////");
	}
}
