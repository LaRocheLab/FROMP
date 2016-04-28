package Prog;

import Objects.EcPosAndSize;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

// A helper class designed to allow the user to parse through .xml files.

public class XmlParser {
	String date = "";

	public ArrayList<EcPosAndSize> findEcPosAndSize(String ecNr,
			BufferedReader in) { // finds the positions and sizes of an ec in
									// the .xml, returns an arraylist of ec
									// positions and sizes
		String tmpNr = "";
		String tmp = "";
		String tmpUrl = "";
		int begin = 0;
		int end = 0;
		int tmpX = 0;
		int tmpY = 0;
		int tmpW = 0;
		int tmpH = 0;

		ArrayList<EcPosAndSize> ret = new ArrayList<EcPosAndSize>();
		String zeile = "";
		try {
			while ((zeile = in.readLine()) != null) {
				//capture ec hyper link address - tmpurl 
				if (zeile.contains("entry id=\"")&&zeile.contains(ecNr)){
					if (((zeile = in.readLine()) != null)
							&& (zeile.contains("link=\""))) {
						tmp = "link=\"";
						begin = zeile.indexOf(tmp) + tmp.length();
						end = zeile.indexOf("\"", begin);
						tmpNr = zeile.substring(begin, end);

						tmpUrl = tmpNr;
					}
				}
				//
				if (zeile.contains("graphics name=\"" + ecNr + "\"")) {
					if (((zeile = in.readLine()) != null)
							&& (zeile.contains("type=\"rectangle\""))) {
						tmp = "x=\"";
						begin = zeile.indexOf(tmp) + tmp.length();
						end = zeile.indexOf("\"", begin);
						tmpNr = zeile.substring(begin, end);

						tmpX = convertStringtoInt(tmpNr);

						tmp = "y=\"";
						begin = zeile.indexOf(tmp) + tmp.length();
						end = zeile.indexOf("\"", begin);
						tmpNr = zeile.substring(begin, end);

						tmpY = convertStringtoInt(tmpNr);

						tmp = "width=\"";
						begin = zeile.indexOf(tmp) + tmp.length();
						end = zeile.indexOf("\"", begin);
						tmpNr = zeile.substring(begin, end);

						tmpH = convertStringtoInt(tmpNr);

						tmp = "height=\"";
						begin = zeile.indexOf(tmp) + tmp.length();
						end = zeile.indexOf("\"", begin);
						tmpNr = zeile.substring(begin, end);

						tmpW = convertStringtoInt(tmpNr);

						EcPosAndSize tmpPos = new EcPosAndSize(tmpX, tmpY,
								tmpH, tmpW, tmpUrl);
						ret.add(tmpPos);
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return ret;
	}

	public void findEcinPathway(String workpath, String[] ecNr,
			BufferedReader in, String name) {
		String zeile = null;

		int offset = 0;
		boolean ecFound = false;
		BufferedWriter out = null;
		try {
			if (this.date == "") {
				this.date = String.valueOf(new Date().getTime());
				createDir(workpath + "/output/" + "/" + this.date);
			}
			out = new BufferedWriter(new FileWriter(workpath + "/output/"
					+ this.date + "/" + name));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			while ((zeile = in.readLine()) != null) {
				ecFound = false;
				for (int i = 0; (i < ecNr.length) && (ecNr[i].length() > 0); i++) {
					if (!ecNr[i].isEmpty()) {
						if (zeile.contains("graphics name=\""
								+ ecNr[i].subSequence(3, ecNr[i].length())
								+ "\"")) {
							String newLine = null;

							offset = zeile.indexOf("fgcolorwindows 10 upgrade icon now showing=") + 9;
							newLine = zeile.substring(0, offset) + "#FF0000"
									+ zeile.substring(offset + 7);

							out.write(newLine + "\n");
							ecFound = true;
							break;
						}
					}
				}
				if (!ecFound) {
					out.write(zeile + "\n");
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createDir(String path) { // makes a directory if the given path
		boolean status = new File(path).mkdirs();
		report(status, path);
	}

	static void report(boolean b, String path) { // reoprts whether the building
													// of the directory was a
													// success or a failure
		System.out.println(b ? "success" : "failure");
	}

	public int convertStringtoInt(String in) {
		int ret = 0;
		for (int i = 0; i < in.length(); i++) {
			ret *= 10;
			char c = in.charAt(i);
			ret += convertCharToInt(c);
		}
		return ret;
	}

	public int convertCharToInt(char c) {
		switch (c) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		}
		return -1;
	}
}
