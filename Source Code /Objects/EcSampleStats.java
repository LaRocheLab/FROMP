package Objects;
import java.awt.Color;

/**
 * EcSampleStats include 3 parts: ec amount , the sample's index , and sample's color 
 *
 *
 */
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
