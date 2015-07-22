package Objects;

// gives the position and size of an ec.

public class EcPosAndSize {
	public int x_;
	public int y_;
	public int width_;
	public int height_;
	public String ecURL_;

	public EcPosAndSize(int x, int y, int width, int height, String ecURL_) {
		this.x_ = x;
		this.y_ = y;
		this.width_ = width;
		this.height_ = height;
		this.ecURL_ = ecURL_;
	}
}
