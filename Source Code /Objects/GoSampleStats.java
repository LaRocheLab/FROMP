package Objects;
import java.awt.Color;

/**
 * GoSampleStats include 3 parts: GO amount , the sample's index , and sample's color 
 * @author zs
 *
 */
public class GoSampleStats {
	public int amount_;
	public int sampleNr_;
	public Color col_;

	public GoSampleStats(GONum Go) {
		this.amount_ = Go.amount_;
		this.sampleNr_ = Go.sampleNr_;
		this.col_ = Go.samColor_;
	}
}
