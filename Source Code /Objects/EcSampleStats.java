package Objects;

import java.awt.Color;

public class EcSampleStats {
	public int amount_;
	public int sampleNr_;
	public Color col_;

	public EcSampleStats(EcNr ecNr) {
		this.amount_ = ecNr.amount_;
		this.sampleNr_ = ecNr.sampleNr_;
		this.col_ = ecNr.samColor_;
	}
}
