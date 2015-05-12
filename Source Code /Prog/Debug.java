package Prog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Debug {
	public static ArrayList<String> noEnzymeLines = new ArrayList();
	public static ArrayList<String> noRepseqLine = new ArrayList();
	public static ArrayList<String> EcList = new ArrayList();
	public static ArrayList<String> PfList = new ArrayList();

	public static void addnoEnzymeLine(String line) {
		noEnzymeLines.add(line);
	}

	public static void addnoRepseqLine(String line) {
		noRepseqLine.add(line);
	}

	public static void addEc(String line) {
		EcList.add(line);
	}

	public static void addPf(String line) {
		PfList.add(line);
	}

	public static void printAll() {
		printNoenz();
		printNoRep();
	}

	public static void writeOutAll(String path) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write("noEnzList");
			out.newLine();
			for (int i = 0; i < noEnzymeLines.size(); i++) {
				out.write((String) noEnzymeLines.get(i));
				out.newLine();
			}
			out.newLine();
			out.write("noRepseqList");
			out.newLine();
			for (int i = 0; i < noRepseqLine.size(); i++) {
				out.write((String) noRepseqLine.get(i));
				out.newLine();
			}
			out.newLine();
			out.write("ecList");
			out.newLine();
			for (int i = 0; i < EcList.size(); i++) {
				out.write((String) EcList.get(i));
				out.newLine();
			}
			out.newLine();
			out.write("PFList");
			out.newLine();
			for (int i = 0; i < PfList.size(); i++) {
				out.write((String) PfList.get(i));
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printNoenz() {
		System.out
				.println("-----------------NoEnzymeList:------------------------");
		for (int i = 0; i < noEnzymeLines.size(); i++) {
			System.out.println((String) noEnzymeLines.get(i));
		}
		System.out
				.println("-----------------NoEnzymeList End:------------------------");
	}

	public static void printNoRep() {
		System.out
				.println("-----------------noRepseqList:------------------------");
		for (int i = 0; i < noRepseqLine.size(); i++) {
			System.out.println((String) noRepseqLine.get(i));
		}
		System.out
				.println("-----------------NoRepymeList End:------------------------");
	}
}
