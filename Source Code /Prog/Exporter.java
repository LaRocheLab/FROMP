package Prog;

import Objects.EcNr;
import Objects.EcWithPathway;
import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Sample;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

// The class which facilitates exporting of matricies (ec, pathway activity, etc) 

public class Exporter {
	JFileChooser fChoose_;
	public String path_ = "";
	JPanel parent_;

	public Exporter(JPanel par) {
		this.parent_ = par;
	}

	public void exportDoubleMatrix(ArrayList<String> xDesc,
			ArrayList<String> yDesc, double[][] matr, boolean inCsf) {
		String seperator = "\t";
		if (inCsf) {
			seperator = ",";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(this.path_));
			out.write("Pathway"+seperator);
			for (int x = 0; x < xDesc.size(); x++) {
				out.write((String) xDesc.get(x) + seperator);
			}
			out.newLine();
			for (int y = 0; y < yDesc.size(); y++) {
				out.write((String) yDesc.get(y) + seperator);
				for (int x = 0; x < xDesc.size(); x++) {
					out.write(matr[x][y] + seperator);
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportDoubleMatrix(ArrayList<String> xDesc,
			ArrayList<String> yDesc, float[][] matr, boolean inCsf) {
		String seperator = "\t";
		if (inCsf) {
			seperator = ",";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(this.path_));
			out.write(seperator);
			for (int x = 0; x < xDesc.size(); x++) {
				out.write((String) xDesc.get(x) + seperator);
			}
			out.newLine();
			for (int y = 0; y < yDesc.size(); y++) {
				out.write((String) yDesc.get(y) + seperator);
				for (int x = 0; x < xDesc.size(); x++) {
					out.write(matr[x][y] + seperator);
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportIntMatrix(ArrayList<String> xDesc,
			ArrayList<String> yDesc, int[][] matr, boolean inCsf) {
		String seperator = "\t";
		if (inCsf) {
			seperator = ",";
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(this.path_));
			out.write(seperator);
			for (int x = 0; x < xDesc.size(); x++) {
				out.write((String) xDesc.get(x) + seperator);
				System.out.println((String) xDesc.get(x));
			}
			out.newLine();
			for (int y = 0; y < yDesc.size(); y++) {
				out.write((String) yDesc.get(y) + seperator);
				for (int x = 0; x < xDesc.size(); x++) {
					if (matr[x][y] == 0) {
						out.write("0" + seperator);
					} else {
						out.write(matr[x][y] + seperator);
					}
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean saveDialog() {
		this.fChoose_ = new JFileChooser(this.path_);

		this.fChoose_.setFileSelectionMode(0);
		this.fChoose_.setBounds(100, 100, 200, 20);
		this.fChoose_.setVisible(true);

		this.fChoose_.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if ((f.isDirectory())
						|| (f.getName().toLowerCase().endsWith(".txt"))) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return ".txt";
			}
		});
		if (this.fChoose_.showSaveDialog(this.fChoose_.getParent()) == 0) {
			try {
				this.path_ = this.fChoose_.getSelectedFile().getCanonicalPath();
				if (!this.path_.endsWith(".txt")) {
					this.path_ += ".txt";
					System.out.println(".txt");
				}
				return true;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

	public ArrayList<String> getNameListFromPathwaysWEc(
			ArrayList<PathwayWithEc> list) {
		ArrayList<String> ret = new ArrayList();
		String name = "";
		for (int pwCnt = 0; pwCnt < list.size(); pwCnt++) {
			name = ((PathwayWithEc) list.get(pwCnt)).id_ + "/"
					+ ((PathwayWithEc) list.get(pwCnt)).name_;
			ret.add(name);
		}
		return ret;
	}

	public ArrayList<String> getNameListFromEcwp(ArrayList<EcWithPathway> list) {
		ArrayList<String> ret = new ArrayList();
		String name = "";
		for (int pwCnt = 0; pwCnt < list.size(); pwCnt++) {
			name = ((EcWithPathway) list.get(pwCnt)).name_;
			ret.add(name);
		}
		return ret;
	}

	public ArrayList<String> getNameListFromEcs(ArrayList<EcNr> list) {
		ArrayList<String> ret = new ArrayList();
		String name = "";
		for (int pwCnt = 0; pwCnt < list.size(); pwCnt++) {
			name = ((EcNr) list.get(pwCnt)).name_;
			ret.add(name);
		}
		return ret;
	}

	public ArrayList<String> getNameListFromPw(ArrayList<Pathway> list) {
		ArrayList<String> ret = new ArrayList();
		String name = "";
		for (int pwCnt = 0; pwCnt < list.size(); pwCnt++) {
			name = ((Pathway) list.get(pwCnt)).id_ + "/"
					+ ((Pathway) list.get(pwCnt)).name_;
			ret.add(name);
		}
		return ret;
	}

	public ArrayList<String> getNameListFromSample(ArrayList<Sample> list) {
		ArrayList<String> ret = new ArrayList();
		String name = "";
		for (int pwCnt = 0; pwCnt < list.size(); pwCnt++) {
			name = ((Sample) list.get(pwCnt)).name_;
			ret.add(name);
		}
		return ret;
	}

	public String getPath_() {
		return this.path_;
	}
}
