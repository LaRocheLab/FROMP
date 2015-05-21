package Prog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.org.mozilla.javascript.json.JsonParser;

/**
 * This class takes the sequence files of a specific EC number or Sample EC number and
 * extracts the peptide sequences. Once extracted, the sequences undergo a tryptic digest
 * (cutting the sequence everytime a K or R is met). This digested sequence is then stored
 * in an array for that specific unique sample. The digested sequences are then queryied into
 * the Tryptic Peptide Analysis found within http://unipept.ugent.be/search/single in order to
 * obtain their  taxonomic lowest common ancestor for a given tryptic peptide.
 * 
 * @author Jennifer Terpstra
 *
 */
public class MetaProteomicAnalysis {
	StringReader reader = new StringReader();
	//Used to extract the specific filename of the sequence file for naming the new .sh file
	String fileName = "";
	
	/**
	 * Performs the extraction of the protien sequences from the provided sequence files
	 * storing the digested results in TrypticPeptide class.
	 * 
	 * @param path The filepath of the Sequence file to be read
	 * @return ArrayList of the tryptic digested sequence
	 */
	public ArrayList<TrypticPeptide> readFasta(String path) {
		ArrayList<TrypticPeptide> retList = new ArrayList<TrypticPeptide>();
		TrypticPeptide trypticPeptide;
		//Stores each digested piece of the peptide for each unqiue identifer
		ArrayList<String> peptideList;
		BufferedReader rfile = reader.readTxt(path);
		//used to parse the long peptide into the tryptic digest
		String line = "";
		String fasta = "";
		String subfasta = "";
		//Extracts the filename of each sequence file
		fileName = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf(".txt"));
		try {
			trypticPeptide = new TrypticPeptide();
			while ((line = rfile.readLine()) != null) {
				//Save the unique identifier into tryptic peptide
				if(line.startsWith(">")){
					trypticPeptide.setUniqueIdentifier(line);
				}
				//the actual peptide sequence that needs to be digested
				else if (!line.startsWith(">")) {
					peptideList = new ArrayList<String>();
					fasta = line;
					for(int i = 0;i<fasta.length();i++){
						//Tryptic digest cuts the sequence up whenever it encounters an r or k
						if(fasta.charAt(i)=='k'||fasta.charAt(i)=='K'||fasta.charAt(i)=='r'||fasta.charAt(i)=='R'){
							subfasta +=fasta.charAt(i);
							//Tryptic peptide must not be any smaller than 5 residues long
							if(subfasta.length()>=5){
								peptideList.add(subfasta);
							}
							subfasta = "";
						}
						else{
							subfasta +=fasta.charAt(i);
						}
						//Case that you are at the end of the sequence and the letter is not k or r, subfasta must be reset
						if(fasta.length()-1==i){
							subfasta = "";
						}
					}
					trypticPeptide.setPeptides(peptideList);
					retList.add(trypticPeptide);
					trypticPeptide = new TrypticPeptide();
				}
			}
			
		} catch (IOException e) {
			System.out.println("File not found!\n");
			e.printStackTrace();
		}
		return retList;
	}
	/**
	 * Returns the taxonomic lowest common ancestor for a given tryptic peptide with a query string (GET) format
	 * 
	 * @param peptide Arraylist of tryptic peptides to be anazyed for their lowest common ancestor
	 */
	public void getTrypticPeptideAnaysis(ArrayList<TrypticPeptide> peptide) {
		// Saves the results of the lowest common ancestor search in a file within the /GetPost folder
		File file = new File(new File(".").getAbsolutePath() + File.separator
				+ "GetPost" + File.separator + fileName + ".sh");
		// Main Get query line
		String query = "GET http://api.unipept.ugent.be/api/v1/pept2lca.json?input[]=";
		// Peptide sequences to query
		String qPep = "";
		Boolean reset = false;
		int num = -1;
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for (int i = 0; i < peptide.size(); i++) {
				for (int j = 0; j < peptide.get(i).getPeptides().size(); j++) {
					try {
						// Format nessary for the first peptide to be inserted
						// into the query
						if (peptide.get(i).getPeptides().size() > 0 && j == 0) {
							qPep += peptide.get(i).getPeptides().get(j);
							// Format nessary for every subsequent peptide in
							// the query
						} else if (peptide.get(i).getPeptides().size() > 0) {
							if (reset == true) {
								qPep += peptide.get(i).getPeptides().get(j);
								reset = false;
							} else {
								qPep += "&input[]="
										+ peptide.get(i).getPeptides().get(j);
							}
						}
						if (j == peptide.get(i).getPeptides().size() - 1) {
							// combines the query format and the peptide sequences
							query += qPep;
							// Allows java to perform console calls
							Process p1 = Runtime.getRuntime().exec(query);
							BufferedReader input1 = new BufferedReader(
									new InputStreamReader(p1.getInputStream()));
							String line1 = input1.readLine();
							// If the response isnt empty, write the response to a file
							if (!line1.equals("[]")) {
								if (num != i) {
									printWriter.println(peptide.get(i)
											.getUniqueIdentifier() + "\n");
									num = i;
								}
								//used to parse the JSON format response from http://unipept.ugent.be/
								JSONArray jsonarray = new JSONArray(line1);
								JSONObject obj;
								LowestCommonAncestor lca;
								ArrayList<LowestCommonAncestor> lcaList = new ArrayList<LowestCommonAncestor>();
								//Parses out the peptide, taxon id, name and rank from the response JSON file. 
								//Stores the results in the LowestCommonAncestor class, which is then added to an arraylist.
								for (int k = 0; k < jsonarray.length(); k++) {
									obj = jsonarray.getJSONObject(k);
									lca = new LowestCommonAncestor(
											obj.getString("peptide"),
											Integer.parseInt(obj
													.getString("taxon_id")),
											obj.getString("taxon_name"),
											obj.getString("taxon_rank"));
									lcaList.add(lca);
								}
								//adding the arraylist of lowest common ancestors for that unique identifer
								peptide.get(i).setLca(lcaList);
								System.out.println("Working....\n");
								//Printing out the contents of the lowest common ancestor search into a file
								for (int p = 0; p < peptide.get(i).getLca()
										.size(); p++) {
									printWriter.println(peptide.get(i).getLca()
											.get(p).toString());
								}
								//If on the last peptide array, notfy the user that results are finished
								if (i == peptide.size() - 1) {
									System.out.println("Done\n");
								}
							}
							query = "GET http://api.unipept.ugent.be/api/v1/pept2lca.json?input[]=";
							qPep = "";
							p1.destroy();

						}
					} catch (IOException | JSONException e) {
						e.printStackTrace();
					}
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Returns the taxonomic lowest common ancestor for a given tryptic peptide with a request body (POST) format
	 */
	//curl -X POST -H \'Accept: application/json\' api.unipept.ugent.be/api/v1/pept2lca \\-d 'input[]=
	//CURRENTLY NOT WORKING, MIGHT REMOVE LATER
//	public void postTrypticPeptideAnaysis(ArrayList<TrypticPeptide> peptide){
//		//Saves the results of the lowest commoin ancestor search in a file within the /GetPost folder
//				File file = new File(new File(".").getAbsolutePath() + File.separator
//						+ "GetPost" + File.separator + fileName + ".sh");
//				//Main POST query line
//				String query = "curl -X POST -H \'Accept: application/json\' api.unipept.ugent.be/api/v1/pept2lca \\-d 'input[]=";
//				//Peptide sequences to query
//				String qPep = "";
//				Boolean reset = false;
//				int num = -1;
//				try {
//					PrintWriter printWriter = new PrintWriter(file);
//					for (int i = 0; i < peptide.size(); i++) {
//						for (int j = 0; j < peptide.get(i).getPeptides().size(); j++) {
//							try {
//								//Format nessary for the first peptide to be inserted into the query
//								if (peptide.get(i).getPeptides().size() > 0 && j == 0) {
//									qPep += peptide.get(i).getPeptides().get(j)+"\'";
//								//Format nessary for every subsequent peptide in the query
//								} else if (peptide.get(i).getPeptides().size() > 0
//										&& (qPep.length() + peptide.get(i)
//												.getPeptides().get(j).length()) < 100) {
//									//If the peptide length was over 100, query needs to be reset to not contain &input[]= first
//									if (reset == true) {
//										qPep += peptide.get(i).getPeptides().get(j)+"\'";
//										reset = false;
//									} else {
//										qPep += " \\-d \'input[]="
//												+ peptide.get(i).getPeptides().get(j)+"\'";
//									}
//								}
//								/*If on the last peptide within a TrypticPeptide or if the peptide sequence length if greater than 100
//								 * calls the get command. Query cannot exceed over 100 peptides as it may timeout.
//								 */
//								if (j == peptide.get(i).getPeptides().size() - 1
//										|| qPep.length()
//												+ peptide.get(i).getPeptides().get(j)
//														.length() > 100) {
//									if (qPep.length()
//											+ peptide.get(i).getPeptides().get(j)
//													.length() > 100) {
//										reset = true;
//									}
//									//combines the query format and the peptide sequences
//									query += qPep;
//									//System.out.println(query);
//									//Allows java to perform console calls 
//									Process p1 = Runtime.getRuntime().exec(query);
//									BufferedReader input1 = new BufferedReader(
//											new InputStreamReader(p1.getInputStream()));
//									String line1 = input1.readLine();
//									//If the response isnt empty, write the response to a file
//									if (!line1.equals("[]")) {
//										if (num != i) {
//											printWriter.println(peptide.get(i)
//													.getUniqueIdentifier() + "\n");
//											num = i;
//										}
//										printWriter.println(line1 + "\n");
//									}
//									query = "curl -X POST -H \'Accept: application/json\' api.unipept.ugent.be/api/v1/pept2lca \\-d 'input[]=";
//									qPep = "";
//									p1.destroy();
//
//								}
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//					printWriter.close();
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//	}

}
