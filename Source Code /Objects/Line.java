package Objects;

// This class is used in many of the classes which display matricies. It contains the values of all of the matrix elements in the line (arrayline_[]) 
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
	public int sum_;

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

	public void fillWithZeros() {
		for (int i = 0; i < this.arrayLine_.length; i++) {
			this.arrayLine_[i] = 0.0D;
		}
	}

	public EcNr getEcNr_() {
		return this.ecNr_;
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
