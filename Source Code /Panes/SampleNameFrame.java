package Panes;

import Objects.Project;
import Objects.Sample;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// Frame for the express purpose of editing the name of a sample in the edit samples pane.

public class SampleNameFrame extends JFrame {
	String sampleName; // The current name of the sample
	final int sampleIndex; // As the name will be changing, the samples index in the Project.samples_ arraylist is nessesairy to keep track of the sample
	JTextField txField; // A text field that the user will overwrite to change the name of the sample
	private static final long serialVersionUID = 1L; 

	public SampleNameFrame(int index, String name) {
		this.sampleIndex = index;
		this.sampleName = name;

		setBounds(200, 200, 250, 200);
		setVisible(true);
		setDefaultCloseOperation(2);
		setLayout(null);

		addParts();
	}

	private void addParts() {// Adds components to the JFrame
		JPanel back = new JPanel();
		back.setBounds(0, 0, 250, 200);
		back.setBackground(Project.getBackColor_());
		back.setLayout(null);
		add(back);

		JLabel info = new JLabel("Change Sample Name");
		info.setBounds(10, 10, 220, 30);
		back.add(info);

		this.txField = new JTextField(this.sampleName);
		this.txField.setBounds(10, 50, 220, 30);
		this.txField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SampleNameFrame.this.sampleName = ((JTextField) arg0
						.getSource()).getText();
				SampleNameFrame.this.changeSampleName();
				SampleNameFrame.this.close();
			}
		});
		back.add(this.txField);

		JButton cancel = new JButton("Cancel");
		cancel.setBounds(10, 85, 100, 30);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SampleNameFrame.this.close();
			}
		});
		back.add(cancel);

		JButton save = new JButton("Save");
		save.setBounds(130, 85, 100, 30);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SampleNameFrame.this.sampleName = SampleNameFrame.this.txField
						.getText();
				SampleNameFrame.this.changeSampleName();
				SampleNameFrame.this.close();
			}
		});
		back.add(save);
	}

	private void changeSampleName() {// Changes the name of the sample
		((Sample) Project.samples_.get(this.sampleIndex)).name_ = this.sampleName;
		Project.dataChanged = true;
	}

	private void close() {
		setVisible(false);
		dispose();
	}
}
