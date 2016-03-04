package Objects;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;

//An extension of the pathway class. Adds all of the ECs which can be mapped to a particular path in a convenient ArrayList.

public class PathwayWithEc extends Pathway {
	public ArrayList<EcNr> ecNrs_; // Arraylist of ecs which map to this pathway
	public double weight_; // the weight of this pathway
	public double score_; // the pathway score
	public boolean weightSet;
	public int sumOfEC_; // the sum of the number of ecs
	//select for exporting
	public boolean selected = false;
	
	public PathwayWithEc(Pathway pathway) {
		super(pathway);
		this.ecNrs_ = new ArrayList();
		this.sumOfEC_ = 0;
		this.pathwayInfoPAth = pathway.pathwayInfoPAth;
	}

	public PathwayWithEc(PathwayWithEc pathway, boolean newPw) {
		super(pathway);
		this.ecNrs_ = new ArrayList();
		this.sumOfEC_ = pathway.sumOfEC_;
		if ((pathway.ecNrs_ != null) && (!newPw)) {
			for (int i = 0; i < pathway.ecNrs_.size(); i++) {
				addEc((EcNr) pathway.ecNrs_.get(i));
			}
			this.score_ = pathway.score_;
			this.weight_ = pathway.weight_;
			this.weightSet = pathway.weightSet;
			this.sumOfEC_ = pathway.sumOfEC_;
			this.pathwayInfoPAth = pathway.pathwayInfoPAth;
		}
		this.userPathway = pathway.userPathway;
	}

	public void addEc(EcNr ecNr) {
		if (this.ecNrs_.size() > 0) {
			for (int i = 0; i < this.ecNrs_.size(); i++) {
				if (((EcNr) this.ecNrs_.get(i)).name_.contentEquals(ecNr.name_)) {
					((EcNr) this.ecNrs_.get(i)).increaseAmount(ecNr.amount_);
					for (int repCnt = 0; repCnt < ecNr.repseqs_.size(); repCnt++) {
						((EcNr) this.ecNrs_.get(i)).repseqs_
								.add((Repseqs) ecNr.repseqs_.get(repCnt));
					}
					this.sumOfEC_ += 1;
					return;
				}
			}
		}
		this.ecNrs_.add(new EcNr(ecNr));
		this.sumOfEC_ += 1;
	}

	public void addEcNewly(EcNr ecNr) {
		if (this.ecNrs_.size() > 0) {
			for (int i = 0; i < this.ecNrs_.size(); i++) {
				if (((EcNr) this.ecNrs_.get(i)).name_.contentEquals(ecNr.name_)) {
					((EcNr) this.ecNrs_.get(i)).amount_ = 0;
					((EcNr) this.ecNrs_.get(i)).stats_ = new ArrayList();
					this.ecNrs_.remove(i);
					this.sumOfEC_ -= 1;
					break;
				}
			}
		}
		this.ecNrs_.add(new EcNr(ecNr));
		this.sumOfEC_ += 1;
	}

	public void addEc(EcNr ecNr, Sample samp) {
		if (this.ecNrs_.size() > 0) {
			for (int i = 0; i < this.ecNrs_.size(); i++) {
				if (((EcNr) this.ecNrs_.get(i)).name_.compareTo(ecNr.name_) == 0) {
					EcSampleStats tmpStats = new EcSampleStats(ecNr);
					((EcNr) this.ecNrs_.get(i)).stats_.add(tmpStats);

					((EcNr) this.ecNrs_.get(i)).increaseAmount(ecNr.amount_);
					for (int repCnt = 0; repCnt < ecNr.repseqs_.size(); repCnt++) {
						((EcNr) this.ecNrs_.get(i)).repseqs_
								.add((Repseqs) ecNr.repseqs_.get(repCnt));
					}
					return;
				}
			}
		}
		EcNr tmpec = new EcNr(ecNr);
		if (tmpec.stats_.isEmpty()) {
			tmpec.stats_.add(new EcSampleStats(ecNr));
		}
		this.ecNrs_.add(tmpec);
	}

	public String getEc(int index) {
		return ((EcNr) this.ecNrs_.get(index)).name_;
	}
	
	public double getScore(){
		return score_;
	}

	public void removeRandomEc() {
		double rand = Math.random();
		System.out.println(rand);
		int pick = (int) (rand * this.ecNrs_.size() - 1.0D);
		if (((EcNr) this.ecNrs_.get(pick)).amount_ > 1) {
			((EcNr) this.ecNrs_.get(pick)).amount_ -= 1;
		} else {
			this.ecNrs_.remove(pick);
		}
		this.sumOfEC_ -= 1;
	}

	public double getWeight(int mode) {
		double ret = 0.0D;
		this.weight_ = 0.0D;
		switch (mode) {
		case 0:
			for (int i = 0; i < this.ecNrs_.size(); i++) {
				ret += ((EcNr) this.ecNrs_.get(i)).weight_;
			}
			break;
		case 1:
			for (int i = 0; i < this.ecNrs_.size(); i++) {
				ret += ((EcNr) this.ecNrs_.get(i)).weight_
						* ((EcNr) this.ecNrs_.get(i)).chainMultiply1_;
			}
			break;
		case 2:
			for (int i = 0; i < this.ecNrs_.size(); i++) {
				ret += ((EcNr) this.ecNrs_.get(i)).weight_
						* ((EcNr) this.ecNrs_.get(i)).chainMultiply2_;
			}
		}
		this.weight_ = ret;

		return ret;
	}

	public void sortEcs() {
		boolean changed = true;
		int ecCnt1 = 0;
		// for (; changed; ecCnt1 < this.ecNrs_.size())
		while (changed && (ecCnt1 < this.ecNrs_.size())) {
			changed = false;
			// ecCnt1 = 0; continue;
			for (int ecCnt2 = ecCnt1 + 1; ecCnt2 < this.ecNrs_.size(); ecCnt2++) {
				if (!ec1BiggerEc2((EcNr) this.ecNrs_.get(ecCnt1),
						(EcNr) this.ecNrs_.get(ecCnt2))) {
					break;
				}
				switchEcs(ecCnt1, ecCnt2);
				ecCnt1++;
				ecCnt2++;
				changed = true;
			}
			ecCnt1++;
		}
	}

	public void switchEcs(int index1, int index2) {
		this.ecNrs_.add(index1, (EcNr) this.ecNrs_.get(index2));
		this.ecNrs_.remove(index2 + 1);
	}

	public boolean ec1SmallerEc2(String ec1, String ec2) {
		for (int i = 0; (i < ec1.length()) && (i < ec2.length()); i++) {
			if (ec1.charAt(i) < ec2.charAt(i)) {
				return true;
			}
			if (ec1.charAt(i) > ec2.charAt(i)) {
				return false;
			}
		}
		return false;
	}

	public boolean ec1BiggerEc2(EcNr ec1, EcNr ec2) {
		for (int i = 0; (i < ec1.nr_.length) && (i < ec2.nr_.length); i++) {
			if (ec1.nr_[i] > ec2.nr_[i]) {
				return true;
			}
			if (ec1.nr_[i] < ec2.nr_[i]) {
				return false;
			}
		}
		return false;
	}

	public void clearList() {
		while (this.ecNrs_.size() > 0) {
			this.ecNrs_.remove(0);
		}
	}

	public EcNr geteEcNr(String name) {
		for (int ecCnt = 0; ecCnt < this.ecNrs_.size(); ecCnt++) {
			if (((EcNr) this.ecNrs_.get(ecCnt)).name_.contentEquals(name)) {
				return (EcNr) this.ecNrs_.get(ecCnt);
			}
		}
		return null;
	}
	
	public String toString(){
		return "Name: " + name_ + " Score: " + score_;
	}

	public void printPath() {
		System.out.println("-------------PrintPAth-----------------");
		System.out.println("Name: " + this.name_ + " Id: " + this.id_);
		System.out.println("EcAmount " + this.ecNrs_.size());
		for (int i = 0; i < this.ecNrs_.size(); i++) {
			System.out.println("Ec Nr: " + i + " Id:"
					+ ((EcNr) this.ecNrs_.get(i)).name_);
		}
		System.out
				.println("///////-------------PrintPAth----------------/////////");
	}

//	@Override
//	public int compareTo(Object arg0) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

}
