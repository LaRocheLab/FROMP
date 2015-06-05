package Panes;

import Objects.Project;
import java.util.Timer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//The cute little loading window with the swimming fish

public class Loadingframe extends Thread {
	private static final long serialVersionUID = 1L;
	public JFrame frame_;
	JPanel backGround_;
	JLabel mover_;
	JLabel outputer_;
	JLabel chapOut_;
	JLabel counter_;
	Timer timer;
	int step_;
	double laststep = 0.0D;
	static boolean running_;
	public static boolean showLoading = true;

	public Loadingframe() {
		if (!showLoading) {
			return;
		}
		running_ = true;
		init();
		start();
	}

	private void init() {
		if (!showLoading) {
			return;
		}
		this.frame_ = new JFrame();
		this.frame_.setBounds(200, 100, 400, 150);
		this.frame_.setResizable(false);
		this.frame_.setLayout(null);
		this.frame_.setVisible(true);

		this.backGround_ = new JPanel();
		this.backGround_.setBounds(0, 0, this.frame_.getWidth() + 50,
				this.frame_.getHeight() + 50);
		this.backGround_.setBackground(Project.getBackColor_());
		this.backGround_.setVisible(true);
		this.backGround_.setLayout(null);
		this.frame_.add(this.backGround_);

		this.step_ = 0;

		this.chapOut_ = new JLabel("");
		this.chapOut_.setBounds(10, 10, 400, 20);
		this.chapOut_.setLayout(null);
		this.chapOut_.setVisible(true);
		this.backGround_.add(this.chapOut_);

		this.mover_ = new JLabel("><((((°>");// this is the fish
		this.mover_.setBounds(-60 + this.step_ * 10, 40, 60, 20);

		this.mover_.setLayout(null);
		this.mover_.setVisible(true);
		this.backGround_.add(this.mover_);

		this.outputer_ = new JLabel("");
		this.outputer_.setBounds(10, 60, 400, 20);
		this.outputer_.setLayout(null);
		this.outputer_.setVisible(true);
		this.backGround_.add(this.outputer_);
		this.frame_.repaint();

		this.counter_ = new JLabel("");
		this.counter_.setBounds(10, 80, 400, 20);
		this.counter_.setLayout(null);
		this.counter_.setVisible(true);
		this.backGround_.add(this.counter_);
		this.frame_.repaint();
	}

	public void step() {
		if (!showLoading) {
			return;
		}
		if (this.frame_ == null) {
			init();
		}
		this.laststep = System.currentTimeMillis();
		this.frame_.repaint();
	}

	public void step(String mesg) {
		if (!showLoading) {
			return;
		}
		if (this.frame_ == null) {
			init();
		}
		this.laststep = System.currentTimeMillis();
		this.outputer_.setText(mesg);
		this.frame_.repaint();
	}

	public void bigStep(String mesg) {
		if (!showLoading) {
			return;
		}
		if (this.frame_ == null) {
			init();
		}
		this.laststep = System.currentTimeMillis();
		this.chapOut_.setText(mesg);
		this.frame_.repaint();
	}

	public void updateCounter(int count) {
		if (!showLoading) {
			return;
		}
		this.counter_.setText("nr.: " + count);
	}

	public void run() {
		if (!showLoading) {
			return;
		}
		int hopper = -1;
		while (running_) {
			this.step_ = ((this.step_ + 1) % 25);
			hopper *= -1;
			this.mover_.setLocation(-60 + this.step_ * 10, this.mover_.getY()
					+ hopper * 2);
			this.frame_.repaint();
			this.frame_.paintComponents(this.frame_.getGraphics());
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.frame_.setVisible(false);
		this.frame_.dispose();
		this.frame_ = null;
	}

	public static void close() {
		if (!showLoading) {
			return;
		}
		running_ = false;
		System.gc();
	}

	public boolean isRunning() {
		return running_;
	}

	public static long getSerialversionuid() {
		return 1L;
	}
}
