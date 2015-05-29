package Prog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;

/**
 * This class takes the sequence files of a specific EC number or Sample EC number and
 * extracts the peptide sequences. Once extracted, the sequences undergo a tryptic digest.
 * This digested sequence is then stored in an array for that specific unique sample.
 * The digested sequences are then queryied into the Tryptic Peptide Analysis found within
 * http://unipept.ugent.be/search/single in order to obtain their  taxonomic lowest common
 * ancestor for a given tryptic peptide.
 * 
 * @author Jennifer Terpstra
 *
 */
public class MetaProteomicAnalysis {
	StringReader reader = new StringReader();
	//Used to extract the specific filename of the sequence file for naming the new .sh file
	String fileName = "";
	
	public MetaProteomicAnalysis(){
		
	}
	
	/**
	 * Performs the extraction of the protien sequences from the provided sequence files
	 * storing the digested results in TrypticPeptide class. Code for tryptic digest
	 * was found at:
	 * 
	 * https://github.com/statisticalbiotechnology/NonlinearGradientsUI/blob/master/src/nonlineargradientsui/Protein.java. 
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
					String peptide;
					int i = 0; 
					int subfasa;
					while (i < fasta.length()) {
						subfasa = i;
						while ((subfasa < fasta.length() - 1)
								&& (((fasta.charAt(subfasa) == 'K' || fasta.charAt(subfasa) == 'R') && fasta
										.charAt(subfasa + 1) == 'P') || (fasta.charAt(subfasa) != 'K' && fasta
										.charAt(subfasa) != 'R'))) {
							subfasa += 1;
						}
						peptide = fasta.substring(i, subfasa + 1);
						if ((peptide.length() >= 5)) {
							peptideList.add(peptide);
						}
						i = subfasa + 1;
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
		int highestTaxa = 0;
		LowestCommonAncestor lowestTaxa = new LowestCommonAncestor();
		boolean reset = false;
		String[] taxa_id = { "forma", "varietas", "subspecies", "species",
				"speciessubgroup", "speciesgroup", "subgenus", "genus",
				"subtribe", "tribe", "subfamily", "family", "superfamily",
				"parvorder", "infraorder", "suborder", "order", "superorder",
				"infraclass", "subclass", "class", "superclass", "subphylum",
				"phylum", "superphylum", "subkingdom", "kingdom",
				"superkingdom","no rank" };
		ArrayList<String> taxa_rank=new ArrayList<String>();
		for(int i =0;i<taxa_id.length;i++){
			taxa_rank.add(taxa_id[i]);
		}
		int num = -1;
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for (int i = 0; i < peptide.size(); i++) {
				for (int j = 0; j < peptide.get(i).getPeptides().size(); j++) {
					try {
						// Format nessary for the first peptide to be inserted into the query
						if (peptide.get(i).getPeptides().size() > 0 && j == 0) {
							qPep += peptide.get(i).getPeptides().get(j);
							// Format nessary for every subsequent peptide in
							// the query
						} else if (peptide.get(i).getPeptides().size() > 0) {
							if (reset == true) {
								qPep += peptide.get(i).getPeptides().get(j);
								//count = 1;
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
							//System.out.println(query);
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
									if(k==0){
										//keeps track of the most specific taxa, if another peptide sequence taxa is more specific it replaces
										highestTaxa = taxa_rank.indexOf(lca.getTaxon_rank());
										lowestTaxa=lca;
									}
									else{
										//System.out.println(lca.getTaxon_rank());
										//System.out.println(taxa_rank.indexOf(lca.getTaxon_rank()));
										if(taxa_rank.indexOf(lca.getTaxon_rank()) < highestTaxa){
											highestTaxa = taxa_rank.indexOf(lca.getTaxon_rank());
											lowestTaxa=lca;
										}
									}
									lcaList.add(lca);
								}
								//setting each peptides lowest Class used to determine its final Lowest Common Ancestor
								peptide.get(i).setLowestClass(lowestTaxa);
								//adding the arraylist of lowest common ancestors for that unique identifer
								peptide.get(i).setLca(lcaList);
								System.out.println(fileName + " Working....\n");
								//Printing out the contents of the lowest common ancestor search into a file
								for (int p = 0; p < peptide.get(i).getLca()
										.size(); p++) {
									printWriter.println(peptide.get(i).getLca()
											.get(p).toString());
								}
								//If on the last peptide array, notfy the user that results are finished
								if (i == peptide.size() - 1) {
									System.out.println(fileName + " Done\n");
									findCommonLCA(peptide);
								}
							}
							query = "GET http://api.unipept.ugent.be/api/v1/pept2lca.json?input[]=";
							qPep = "";
							//reset=true;
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
	
	/**
	 * Responsible for taking each Tryptic peptide and sending their determine lowest taxa id to
	 * http://api.unipept.ugent.be/api/v1/taxonomy in order to determine their taxonic information.
	 * This information is then compared with the remaining Lowest Common ancestors within the 
	 * Tryptic Peptide to see if they are valid towards to lowest taxa id taxonic information. If the
	 * remaining LCA pass the rest then the lowest taxa can then be set to the identified taxa of the 
	 * Tryptic Peptide. 
	 * 
	 * @param peptide Arraylist of Tryptic Peptides for a given sample
	 * @author Jennifer Terpstra
	 */
	public void findCommonLCA(ArrayList<TrypticPeptide> peptide) {
		//final taxa results get written to a file with a -LCA.txt postfix
		File file = new File(new File(".").getAbsolutePath() + File.separator
				+ "GetPost" + File.separator + fileName + "-LCA.txt");
		String query = "";
		Process p1 = null;
		boolean positive = false;
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(file);
			for (int i = 0; i < peptide.size(); i++) {
				if (peptide.get(i).getLca() != null) {
					//If the tryipic peptide only had one lowest common ancestor result, set it as its identified taxa
					if (peptide.get(i).getLca().size() == 1) {
						peptide.get(i).setIdentifiedTaxa(peptide.get(i).getLca().get(0));
						System.out.println(peptide.get(i).getUniqueIdentifier());
						printWriter.println(peptide.get(i).getUniqueIdentifier());
						System.out.println(peptide.get(i).getIdentifiedTaxa());
						printWriter.println(peptide.get(i).getIdentifiedTaxa());
					} else {
						/*If the tryipic peptide had multiple lowest common ancestor results, first the lowest taxa identifier
						 * id must be sent to http://api.unipept.ugent.be/api/v1/taxonomy.json to determine its taxon information.
						 */
						query = "GET http://api.unipept.ugent.be/api/v1/taxonomy.json?input[]="
								+ peptide.get(i).getLowestClass().getTaxon_id()+ "&extra=true&names=true";
						try {
							//sending the get request to the server
							p1 = Runtime.getRuntime().exec(query);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						BufferedReader input1 = new BufferedReader(
								new InputStreamReader(p1.getInputStream()));
						try {
							//reading the response from the server
							String line1 = input1.readLine();
							//parsing stringt response into a JSONArray so that it can be processed
							JSONArray jsonarray = new JSONArray(line1);
							JSONObject obj;
							//for each LCA within the arraylist
							for (int j = 0; j < peptide.get(i).getLca().size(); j++) {
								//for each element within the JSON array
								for (int k = 0; k < jsonarray.length(); k++) {
									obj = jsonarray.getJSONObject(k);
									//peptide LCA rank cannot be "no rank" or null
									if (!(peptide.get(i).getLca().get(j)
											.getTaxon_rank()).equals("no rank")) {
										if (((!(peptide.get(i).getLca().get(j)
												.getTaxon_rank().equals("null"))) && !(peptide.get(i).getLca().get(j).getTaxon_rank()).isEmpty())) {
											if (!(obj.getString(peptide.get(i).getLca().get(j).getTaxon_rank()+ "_id").equals("null"))
													&& (!(obj.getString(peptide.get(i).getLca().get(j).getTaxon_rank()+ "_id").isEmpty()))) {
												//obtaining taxonID from the server response according to the current tryptic peptide LCA taxon rank
												int taxonID = (Integer.parseInt(obj.getString(peptide.get(i).getLca().get(j).getTaxon_rank()+ "_id")));
												//obtaining taxon name from the server for the current tryptic pepetide LCA taxon rank
												String taxonName = obj.getString(peptide.get(i).getLca().get(j).getTaxon_rank()+ "_name");
												//obtaining current sample tryptic pepetide LCA taxon id 
												int sampleTaxonID = peptide.get(i).getLca().get(j).getTaxon_id();
												//obtaining current sample tryptic peptide LCA taxon name
												String sampleName = peptide.get(i).getLca().get(j).getTaxon_name();
												System.out.println("response "+ taxonID + ", " + taxonName);
												System.out.println("sample "+ sampleTaxonID + ", " + sampleName);
												/*if the response taxonID is equal to the sample taxonID than the sample LCA is valid for the
												 * tryptic peptides current lowest taxa 
												 */
												if (taxonID == sampleTaxonID
														&& positive != false) {
													// System.out.println("Positive!\n");
													positive = true;
												} else {
													positive = false;
												}

											}
										}
									}

								}
							}
							if (positive) {
								System.out.println(peptide.get(i).getUniqueIdentifier());
								printWriter.println(peptide.get(i).getUniqueIdentifier());
								peptide.get(i).setIdentifiedTaxa(peptide.get(i).getLowestClass());
							} else {
								/*If one of the LCA within the LCA arraylist fails the comparison with the lowest taxa taxonic
								 * information than that Tryptic pepetides determined taxa is set to Inconclusive.
								 */
								System.out.println(peptide.get(i).getUniqueIdentifier());
								printWriter.println(peptide.get(i).getUniqueIdentifier());
								peptide.get(i).setIdentifiedTaxa(new LowestCommonAncestor("",0,"Inconclusive",""));
								positive = true;
							}
							System.out.println(peptide.get(i).getIdentifiedTaxa());
							printWriter.println(peptide.get(i).getIdentifiedTaxa());
							// System.out.println(line1);
							query = "";
							line1 = "";
						} catch (IOException | JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// System.out.println(peptide.get(i).getUniqueIdentifier());
						// System.out.println(peptide.get(i).getLowestClass().getTaxon_rank());

					}
				}

			}
			printWriter.close();
			drawLCAGraph(peptide);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Calculates the total amount of determined taxa within a sample and stores the
	 * results within a hashmap. A Jtable of these results are then created to be displayed
	 * to the user. Another Jtable of the total taxa determined results is also created for
	 * the sample.
	 * 
	 * @param peptide Arraylist of Tryptic peptides for a given sample
	 * @author Jennifer Terpstra
	 */
	public void drawLCAGraph(ArrayList<TrypticPeptide> peptide){
		String sample;
		//Counts the number of indentified rows within the Tryptic peptide arrayList
		int countIdentRows = 0;
		Map<String, Integer> totalResult = new HashMap<String, Integer>();
		//Counting the results for identified taxa, stored in hashmap
		for (int i = 0; i < peptide.size(); i++) {
			if (peptide.get(i).getIdentifiedTaxa() != null) {
				countIdentRows++;
				String taxonName = peptide.get(i).getIdentifiedTaxa().getTaxon_name();
				if (totalResult.containsKey(taxonName)) {
					Integer count = totalResult.get(peptide.get(i)
							.getIdentifiedTaxa().getTaxon_name());
					totalResult.put(taxonName, count + 1);
				} else {
					totalResult.put(taxonName, 1);
				}
			}
		}
		//setting the Jtable rowData and colNames for the TotalTaxon table
		Object[][] rowData = new Object[totalResult.keySet().size()][2];
		Object[] colNames = { "Identified Taxon", "Number of occurances" };
		int index = 0, index2 = 0;
		for (String key : totalResult.keySet()) {
			rowData[index][0] = key;
			rowData[index][1] = totalResult.get(key);
			index++;
		}
		//setting the Jtable rowData and colNames for the Summary table
		Object[][] rowData2 = new Object[countIdentRows][3];
		Object[] colNames2 = { "Sequence ID", "Sample", "Taxon Name" };
		//If the file being read in is a sequence file per EC Sample, setting rowdata and colnames
		if (!Character.isDigit(fileName.charAt(0))) {
			//parsing out samplename from filename
			sample = fileName.substring(fileName.indexOf(0) + 1,fileName.indexOf('-'));
			for (int i = 0; i < peptide.size(); i++) {
				if (peptide.get(i).getIdentifiedTaxa() != null) {
					rowData2[index2][0] = peptide.get(i).getUniqueIdentifier();
					rowData2[index2][1] = sample;
					rowData2[index2][2] = peptide.get(i).getIdentifiedTaxa().getTaxon_name();
					index2++;
				}
			}
		} else {
			//if file being read in is a sequence file per EC (includes multiple samples)
			for (int i = 0; i < peptide.size(); i++) {
				if (peptide.get(i).getIdentifiedTaxa() != null) {
					//gets unique identifer of sequence in case that the identifier ends with '-'
					if (peptide.get(i).getUniqueIdentifier().lastIndexOf('-') > peptide
							.get(i).getUniqueIdentifier().lastIndexOf('+')) {
						rowData2[index2][0] = peptide.get(i).getUniqueIdentifier().subSequence(peptide.get(i)
							.getUniqueIdentifier().indexOf(peptide.get(i).getUniqueIdentifier()
							.charAt(0)),peptide.get(i).getUniqueIdentifier().lastIndexOf('-') + 1);
						rowData2[index2][1] = peptide.get(i).getUniqueIdentifier().substring(
							peptide.get(i).getUniqueIdentifier().lastIndexOf('-') + 1);
					} else {
						//gets unique identifer of sequence in case that identifer ends with '+'
						rowData2[index2][0] = peptide.get(i).getUniqueIdentifier().subSequence(
							peptide.get(i).getUniqueIdentifier().indexOf(peptide.get(i)
							.getUniqueIdentifier().charAt(0)),peptide.get(i).getUniqueIdentifier()
							.lastIndexOf('+') + 1);
						rowData2[index2][1] = peptide.get(i).getUniqueIdentifier().substring(
							peptide.get(i).getUniqueIdentifier().lastIndexOf('+') + 1);
					}
					rowData2[index2][2] = peptide.get(i).getIdentifiedTaxa().getTaxon_name();
					index2++;
				}
			}
		}
		//creating table for Total Taxa
		DefaultTableModel tableModel = new DefaultTableModel(rowData, colNames);
		JTable table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setFillsViewportHeight(true);
		table.getColumn("Identified Taxon").setMinWidth(400);
		//made into a button on a later date
		exportExcel(table, "TotalTaxon");
		//creating table for Summary of data
		DefaultTableModel tableModel2 = new DefaultTableModel(rowData2, colNames2);
		JTable table2 = new JTable(tableModel2) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table2.getColumn("Sequence ID").setMinWidth(400);
		table2.getColumn("Sample").setMinWidth(300);
		table2.setFillsViewportHeight(true);
		//made into a button on a later date
		exportExcel(table2, "Summary");
		
		//Temporary UI visualzation for tables, until placement in main UI is made
		JFrame frame = new JFrame(fileName);
		JFrame frame2 = new JFrame(fileName);

		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		panel.setLayout(new BorderLayout());
		panel2.setLayout(new BorderLayout());

		JScrollPane tableContainer = new JScrollPane(table);
		JScrollPane tableContainer2 = new JScrollPane(table2);

		panel.add(tableContainer, BorderLayout.CENTER);
		panel2.add(tableContainer2, BorderLayout.CENTER);
		frame.getContentPane().add(panel);
		frame2.getContentPane().add(panel2);

		frame.pack();
		frame.setVisible(true);
		frame2.pack();
		frame2.setVisible(true);
		
	}
	/**
	 * This function tables in a JTable and exports the results into an excel file.
	 * Majority of Code obtained at: http://niravjavadeveloper.blogspot.com/2011/05/java-swing-export-jtable-to-excel-file.html#ixzz3bWxHHaFQ
	 * 
	 * @param table JTable to be exported
	 * @param tableName Table type to be exported
	 * @author Jennifer Terpstra
	 * 
	 */
	public void exportExcel(JTable table, String tableName){
		 try {
			 //saves the new excel file within the Excel folder
			 	File file = new File(new File(".").getAbsolutePath() + File.separator
				+ "Excel" + File.separator + fileName + tableName + ".xls");
	            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
	            WritableSheet sheet1 = workbook1.createSheet("fileName", 0);
	            TableModel model = table.getModel();

	            for (int i = 0; i < model.getColumnCount(); i++) {
	                Label column = new Label(i, 0, model.getColumnName(i));
	                sheet1.addCell(column);
	            }
	            int j = 0;
	            for (int i = 0; i < model.getRowCount(); i++) {
	                for (j = 0; j < model.getColumnCount(); j++) {
	                	//determines if the cell value is a number or string. If the value is a number,
	                	//it is stored as an number within the excel file
	                	if(isNumeric(model.getValueAt(i, j).toString())){
	                		Number number = new Number(j, i+1, ((int) model.getValueAt(i, j))/1.0); 
	                		sheet1.addCell(number);
	                	}
	                	else{
	                		Label row = new Label(j, i + 1,model.getValueAt(i, j).toString());
	                		 sheet1.addCell(row);
	                	}
	                    
	                   
	                }
	            }
	            workbook1.write();
	            workbook1.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	
	/**
	 * Used to determine if a string is in fact a number or just
	 * a string.
	 * 
	 * @param str A string to be tested if its numerical
	 * @return returns true if the string is numerical
	 * @author Jennifer Terpstra
	 * 
	 */
	public static boolean isNumeric(String str) {
		try {
			int d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
