package Objects;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;

// This class is used in many of the classes which display matrices. It contains the values of all of the matrix elements in the line (arrayline_[]) 
// as well as the total sum of the line (sumline_).	This object is a literal "Line" of the matrix.

public class Line {
	PathwayWithEc path_;
	EcWithPathway ec_;
	EcNr ecNr_;
	private final boolean sumline_;
	private final boolean mappedSums_;
	private final boolean unmappedSums_;
	private final boolean incompleteSums_;
	public double[] arrayLine_;
	public ArrayList<Double> dist_nums; 
	public ArrayList<Double> odd_nums;
	public int sum_;
	public double lowest_dist_num;
	public double lowest_odds;
	ArrayList<JLabel> geo_labels;
	ArrayList<JLabel> odds_labels;

	public Line(PathwayWithEc pw, double[] line) {
		this.path_ = pw;
		this.arrayLine_ = line;
		this.sumline_ = false;
		this.mappedSums_ = false;
		this.unmappedSums_ = false;
		this.incompleteSums_ = false;
	}

	public Line(EcWithPathway ecwp, double[] line) {
		this.ec_ = new EcWithPathway(ecwp);
		this.arrayLine_ = line;
		this.sumline_ = false;
		this.mappedSums_ = false;
		this.unmappedSums_ = false;
		this.incompleteSums_ = false;
	}

	public Line(EcNr ecNr, double[] line) {
		this.ecNr_ = ecNr;
		this.arrayLine_ = line;
		this.sumline_ = false;
		this.mappedSums_ = false;
		this.unmappedSums_ = false;
		this.incompleteSums_ = false;
	}

	public Line(double[] line, boolean mappedSums, boolean unmappedSums,
			boolean incompletSums) {
		this.arrayLine_ = line;
		this.sumline_ = true;
		this.mappedSums_ = mappedSums;
		this.unmappedSums_ = unmappedSums;
		this.incompleteSums_ = incompletSums;
	}

	public void setSum() {
		this.sum_ = 0;
		for (int i = 0; i < this.arrayLine_.length; i++) {
			this.sum_ = ((int) (this.sum_ + this.arrayLine_[i]));
		}
	}
	
	

	public ArrayList<Double> getDist_nums() {
		return dist_nums;
	}

	public void setDist_nums(ArrayList<Double> dist_nums) {
		this.dist_nums = dist_nums;
		setLowest_dist_num();
	}
	
	public ArrayList<Double> getOdd_nums() {
		return odd_nums;
	}

	public void setOdd_nums(ArrayList<Double> odd_nums) {
		this.odd_nums = odd_nums;
		setLowest_odds();
	}

	public double getLowest_odds() {
		return lowest_odds;
	}

	public void setLowest_odds() {
		if(odd_nums!=null||odd_nums.size()!=0){
			Collections.sort(odd_nums);
			this.lowest_odds = odd_nums.get(0);
			//System.out.println(this.lowest_odds);
		}
		
	}

	public ArrayList<JLabel> getOdds_labels() {
		return odds_labels;
	}

	public void setOdds_labels(ArrayList<JLabel> odds_labels) {
		this.odds_labels = odds_labels;
	}

	public ArrayList<JLabel> getGeo_labels() {
		return geo_labels;
	}

	public void setGeo_labels(ArrayList<JLabel> geo_labels) {
		this.geo_labels = geo_labels;
	}

	public double getLowest_dist_num() {
		return lowest_dist_num;
	}

	public void setLowest_dist_num() {
		if(dist_nums!=null||dist_nums.size()!=0){
			Collections.sort(dist_nums);
			this.lowest_dist_num = dist_nums.get(0);
		
			
		}
		
	}

	public void fillWithZeros() {
		for (int i = 0; i < this.arrayLine_.length; i++) {
			this.arrayLine_[i] = 0.0D;
		}
	}

	public EcNr getEcNr_() {
		return this.ecNr_;
	}
	
	

	public void setEc_(EcWithPathway ec_) {
		this.ec_ = ec_;
	}

	public double getSum_() {
		return this.sum_;
	}

	public double getEntry(int index) {
		if (this.arrayLine_.length < index) {
			return -1.0D;
		}
		return this.arrayLine_[index];
	}

	public void setEntry(int index, double value) {
		this.arrayLine_[index] = value;
	}

	public PathwayWithEc getPath_() {
		return this.path_;
	}

	public EcWithPathway getEc_() {
		return this.ec_;
	}

	public double[] getArrayLine_() {
		return this.arrayLine_;
	}

	public boolean isSumline_() {
		return this.sumline_;
	}

	public boolean isMappedSums_() {
		return this.mappedSums_;
	}

	public boolean isUnMappedSums_() {
		return this.unmappedSums_;
	}

	public boolean isincompleteSums_() {
		return this.incompleteSums_;
	}
}
