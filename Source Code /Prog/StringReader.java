package Prog;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Class file used to supplement the buffered reader class to be used to parse through input files and conversion files for the data processor
// pretty well all file reading is done using this class. It is more of a helper class than an object.

public class StringReader {
	public BufferedReader readTxt(String path) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(path));
		} catch (IOException e) {
			openWarning("Error", "File: pathway" + path + ".chn" + " not found");
			System.out.println("File: pathway" + path + ".chn" + " not found");
		}
		return in;
	}

	public void printTxt(String path) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(path));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				System.out.println("Gelesene Zeile: " + zeile);
			}
		} catch (IOException e) {
			openWarning("Error", "File: pathway" + path + ".chn" + " not found");
			e.printStackTrace();
		}
	}

	public void printTxt(BufferedReader txt) {
		try {
			String zeile = null;
			while ((zeile = txt.readLine()) != null) {
				System.out.println("Gelesene Zeile: " + zeile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void copyTxtFile(String inPath, String outPath) {
		BufferedReader in = readTxt(inPath);
		BufferedWriter out = null;
		String line = "";
		try {
			out = new BufferedWriter(new FileWriter(outPath));
			while ((line = in.readLine()) != null) {
				out.write(line);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openWarning(String title, String string) {
		JFrame frame = new JFrame(title);
		frame.setVisible(true);
		frame.setBounds(200, 200, 350, 150);
		frame.setLayout(null);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 350, 150);
		panel.setBackground(Color.lightGray);
		panel.setVisible(true);
		panel.setLayout(null);
		frame.add(panel);

		JLabel label = new JLabel(string);

		label.setVisible(true);
		label.setForeground(Color.black);
		label.setBounds(0, 0, 350, 150);
		label.setLayout(null);
		panel.add(label);

		frame.repaint();
	}
}
