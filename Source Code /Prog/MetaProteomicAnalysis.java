package Prog;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.org.mozilla.javascript.JavaScriptException;
import Panes.ActMatrixPane;
import Panes.SampleNameFrame;
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
	tableAndChartData returnData = new tableAndChartData();
	boolean commandLineOn = false;
	boolean batchCommandOn = false;
	final String basePath_ = new File(".").getAbsolutePath() + File.separator;
	ArrayList<String> timedOut = new ArrayList<String>();
	
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
		//Extracts the filename of each sequence file
		fileName = path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf(".txt"));
		try {
			trypticPeptide = new TrypticPeptide();
			if (rfile != null) {
				while ((line = rfile.readLine()) != null) {
					// Save the unique identifier into tryptic peptide
					if (line.startsWith(">")) {
						trypticPeptide.setUniqueIdentifier(line);
					}
					// the actual peptide sequence that needs to be digested
					else if (!line.startsWith(">")) {
						peptideList = new ArrayList<String>();
						fasta = line;
						String peptide;
						int i = 0;
						int subfasa;
						while (i < fasta.length()) {
							subfasa = i;
							while ((subfasa < fasta.length() - 1)
									&& (((fasta.charAt(subfasa) == 'K' || fasta
											.charAt(subfasa) == 'R') && fasta
											.charAt(subfasa + 1) == 'P') || (fasta
											.charAt(subfasa) != 'K' && fasta
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
			}

		} catch (IOException e) {
			warningFrame("File " + path + " Not found!");
		}
		
		return retList;
	}
	
	/**
	 * Performs the extraction of the protien sequences from the provided sequence files
	 * storing the digested results in TrypticPeptide class. Code for tryptic digest
	 * was found at:
	 * 
	 * https://github.com/statisticalbiotechnology/NonlinearGradientsUI/blob/master/src/nonlineargradientsui/Protein.java. 
	 * 
	 */
	public ArrayList<TrypticPeptide> readFasta(LinkedHashMap seq, String sampleName) {
		ArrayList<TrypticPeptide> retList = new ArrayList<TrypticPeptide>();
		TrypticPeptide trypticPeptide = new TrypticPeptide();
		//Stores each digested piece of the peptide for each unqiue identifer
		ArrayList<String> peptideList = new ArrayList<String>();
		fileName = sampleName;
		//used to parse the long peptide into the tryptic digest
		String fasta = "";
		// Save the unique identifier into tryptic peptide
		Object[] keys = null;
		try {
			keys  = seq.keySet().toArray();
		} catch(NullPointerException e){
			System.out.println("No values found within sequence file for ec: " + sampleName);
			System.exit(0);
		}
		for(int j = 0; j < keys.length; j++){
			trypticPeptide.setUniqueIdentifier(keys[j].toString());
			peptideList = new ArrayList<String>();
			fasta = (String) seq.get(keys[j].toString());
			String peptide;
			int i = 0;
			int subfasa;
			while (i < fasta.length()) {
				subfasa = i;
				while ((subfasa < fasta.length() - 1)
						&& (((fasta.charAt(subfasa) == 'K' || fasta
								.charAt(subfasa) == 'R') && fasta
								.charAt(subfasa + 1) == 'P') || (fasta
								.charAt(subfasa) != 'K' && fasta
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
		returnData.setFileName(fileName);
		return retList;
	}
	
	/**
	 * Returns the taxonomic lowest common ancestor for a given tryptic peptide with a query string (GET) format
	 * 
	 * @param peptide Arraylist of tryptic peptides to be anazyed for their lowest common ancestor
	 */
	public tableAndChartData getTrypticPeptideAnaysis(ArrayList<TrypticPeptide> peptide, boolean commandline, boolean batchCommand) {
		System.out.println("getPeptide");
		// Saves the results of the lowest common ancestor search in a file within the /GetPost folder
		commandLineOn = commandline;
		batchCommandOn = batchCommand;
		// Main Get query line
		String query = "http://api.unipept.ugent.be/api/v1/pept2lca.json?input[]=";
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
		for (int i = 0; i < peptide.size(); i++) {
			for (int j = 0; j < peptide.get(i).getPeptides().size(); j++) {
				try {
					// Format nessary for the first peptide to be inserted into the query
					if (peptide.get(i).getPeptides().size() > 0 && j == 0) {
						qPep += peptide.get(i).getPeptides().get(j);
						// Format nessary for every subsequent peptide in the query
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
						
						//performing get request
				        URL url = new URL(query);
				        
				        HttpURLConnection con = (HttpURLConnection) url.openConnection();
				        BufferedReader input1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line1 = input1.readLine();
						con.disconnect();
						input1.close();
						
						// If the response isnt empty, write the response to a file
						if (!line1.equals("[]")) {
							if (num != i) {
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
										Integer.parseInt(obj.getString("taxon_id")),
										obj.getString("taxon_name"),
										obj.getString("taxon_rank"));
								if(k==0){
									//keeps track of the most specific taxa, if another peptide sequence taxa is more specific it replaces
									highestTaxa = taxa_rank.indexOf(lca.getTaxon_rank());
									lowestTaxa=lca;
								}
								else{
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
							for (int p = 0; p < peptide.get(i).getLca().size(); p++) {
							}
							//If on the last peptide array, notfy the user that results are finished
							if (i == peptide.size() - 1) {
								System.out.println(fileName + " Done\n");
								findCommonLCA(peptide);
							}
						}
						query = "http://api.unipept.ugent.be/api/v1/pept2lca.json?input[]=";
						qPep = "";

					}
				} catch (IOException| JSONException b) {
					System.out.println("Connection Timeout " + fileName);
					if(!timedOut.contains(fileName)){
						timedOut.add(fileName);
					}
				}
			}
		}
		return returnData;
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
		System.out.println("Find common LCA");
		//final taxa results get written to a file with a -LCA.txt postfix
		String query = "";
		Process p1 = null;
		boolean positive = false;
		PrintWriter printWriter;
		try {
			for (int i = 0; i < peptide.size(); i++) {
				if (peptide.get(i).getLca() != null) {
					//If the tryipic peptide only had one lowest common ancestor result, set it as its identified taxa
					if (peptide.get(i).getLca().size() == 1) {
						peptide.get(i).setIdentifiedTaxa(peptide.get(i).getLca().get(0));
					}
					else {
						/*If the tryipic peptide had multiple lowest common ancestor results, first the lowest taxa identifier
						 * id must be sent to http://api.unipept.ugent.be/api/v1/taxonomy.json to determine its taxon information.
						 */
						query = "http://api.unipept.ugent.be/api/v1/taxonomy.json?input[]="
								+ peptide.get(i).getLowestClass().getTaxon_id()+ "&extra=true&names=true";
						//get request
						System.out.println(query);
						URL url = new URL(query);
					    HttpURLConnection con = (HttpURLConnection) url.openConnection();
					    con.setRequestMethod("GET");
					    BufferedReader input1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
						
						try {
							//reading the response from the server
							String line1 = input1.readLine();
							con.disconnect();
							input1.close();
							//parsing stringt response into a JSONArray so that it can be processed
							JSONArray jsonarray = new JSONArray(line1);
							JSONObject obj;
							//for each LCA within the arraylist
							System.out.println("Finding Lca of " + fileName + "....\n");
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
												/*if the response taxonID is equal to the sample taxonID than the sample LCA is valid for the
												 * tryptic peptides current lowest taxa 
												 */
												if (taxonID == sampleTaxonID
														&& positive != false) {
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
								peptide.get(i).setIdentifiedTaxa(peptide.get(i).getLowestClass());
							} else {
								/*If one of the LCA within the LCA arraylist fails the comparison with the lowest taxa taxonic
								 * information than that Tryptic pepetides determined taxa is set to Inconclusive.
								 */
								peptide.get(i).setIdentifiedTaxa(new LowestCommonAncestor("",0,"Inconclusive",""));
								positive = true;
							}
							query = "";
							line1 = "";
						} catch (IOException | JSONException a) {
						}
						//added to try to fix timeout
						
					}
				}
				
			}
			drawLCAGraph(peptide, fileName);
		} catch (FileNotFoundException e1) {
			//warningFrame("File " + file.getAbsolutePath() + " not found");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("Connection Timeout for " + fileName);
			if(!timedOut.contains(fileName)){
				timedOut.add(fileName);
			}
		
		}
	}
	
	/**
	 * Calculates the total amount of determined taxa within a sample and stores the
	 * results within a hashmap. A Jtable of these results are then created to be displayed
	 * to the user. Another Jtable of the total taxa determined results is also created for
	 * the sample.
	 * 
	 * @param peptide Arraylist of Tryptic peptides for a given sample
	 * 
	 * @author Jennifer Terpstra
	 */
	public void drawLCAGraph(ArrayList<TrypticPeptide> peptide, String fileName){
		System.out.println("Draw Graph");
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
		DefaultPieDataset dataset = new DefaultPieDataset();
		int index = 0, index2 = 0, totalOther = 0;
		//will add button to allow sorting
		totalResult = sortHashMapByValuesD(totalResult);
		
		for (String key : totalResult.keySet()) {
			if(index < 14){
				rowData[index][0] = key;
				rowData[index][1] = totalResult.get(key);
				dataset.setValue(key, totalResult.get(key));
			}
			else{
				rowData[index][0] = key;
				rowData[index][1] = totalResult.get(key);
				totalOther += totalResult.get(key);
				key = "Other";
				if(index == totalResult.size()-1){
					dataset.setValue(key, totalOther);
				}
			}
			index++;
		}
		returnData.setDataset(dataset);
		if(commandLineOn){
			final JFreeChart chart = ChartFactory.createPieChart(fileName
					+ " Total Taxonomy", returnData.getDataset(), true, true, false);
			PiePlot pie1 = (PiePlot) chart.getPlot();
			pie1.setNoDataMessage("No data available");
			pie1.setCircular(false);
			pie1.setLabelGenerator(null);
			try {
				ChartUtilities.saveChartAsPNG(new File(basePath_
						+ "PieChart" + File.separator + fileName
						+ " Total Taxonomy" + ".png"), chart, 1000, 1000);
				System.out.println("Pie Chart exported to " + File.separator
						+ "PieChart" + File.separator + fileName
						+ " Total Taxonomy" + ".png");
			} catch (IOException e1) {
				warningFrame("PieChart folder does not exist!");
			}
		}
		//setting the Jtable rowData and colNames for the Summary table
		Object[][] rowData2 = new Object[countIdentRows][3];
		Object[] colNames2 = { "Sequence ID", "Sample", "Taxon Name" };
		//If the file being read in is a sequence file per EC Sample, setting rowdata and colnames
		if (!Character.isDigit(fileName.charAt(0))) {
			//parsing out samplename from filename
			for (int i = 0; i < peptide.size(); i++) {
				if (peptide.get(i).getIdentifiedTaxa() != null) {
					sample = peptide.get(i).getUniqueIdentifier().substring(peptide.get(i).getUniqueIdentifier().indexOf(" "));
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
			//displays a popup if the words within the cell are to large
			public String getToolTipText( MouseEvent e ){
		       try{
		        	int row = rowAtPoint( e.getPoint() );
			        int column = columnAtPoint( e.getPoint() );

			        Object value = getValueAt(row, column);
			        return value == null ? null : value.toString();
		       }
		       catch(ArrayIndexOutOfBoundsException exe){
		    	   return "";
		       }
				
		    }
		};
		table.setFillsViewportHeight(true);
		
		if(commandLineOn){
			exportExcel(table, "TotalTaxon", fileName);
			System.out.println("File exported to " +  File.separator
					+ "Excel" + File.separator + fileName + "TotalTaxon" + ".xls");
		}
		
		returnData.setTable1(table);
		returnData.setFileName(fileName);
		
		//creating table for Summary of data
		DefaultTableModel tableModel2 = new DefaultTableModel(rowData2, colNames2);
		JTable table2 = new JTable(tableModel2) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
			public String getToolTipText( MouseEvent e ){
				try{
		        	int row = rowAtPoint( e.getPoint() );
			        int column = columnAtPoint( e.getPoint() );

			        Object value = getValueAt(row, column);
			        return value == null ? null : value.toString();
		       }
		       catch(ArrayIndexOutOfBoundsException exe){
		    	   return "";
		       }
		    }
		};
		table2.setFillsViewportHeight(true);
		
		if(commandLineOn){
			exportExcel(table2, "Summary", fileName);
			System.out.println("File exported to " +  File.separator
				+ "Excel" + File.separator + fileName + "Summary" + ".xls");
		}
		else if(batchCommandOn){
			exportTableTxt(table2, "TotalTaxon", fileName);
			System.out.println("File exported to " +  File.separator
					+ "Tables" + File.separator + fileName + "Summary" + ".txt");
		}
		returnData.setTable2(table2);
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
	public void exportExcel(JTable table, String tableName, String fileName){
		 try {
			 //saves the new excel file within the Excel folder
			 	File file = new File(basePath_ + "Excel" + File.separator + fileName + tableName + ".xls");
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
						Number number = new Number(j, i + 1,
								((int) model.getValueAt(i, j)) / 1.0);
						sheet1.addCell(number);
					} else {
						Label row = new Label(j, i + 1, model.getValueAt(i, j)
								.toString());
						sheet1.addCell(row);
					}

				}
			}
			workbook1.write();
			workbook1.close();
		} catch (Exception ex) {
			warningFrame("Excel folder does not exist!");
		}
		if(!commandLineOn){
			ActMatrixPane pane = new ActMatrixPane();
			pane.infoFrame(File.separator + "Excel" + File.separator + fileName + tableName + ".xls", "Excel");
		}
	}
	
	public void exportTableTxt(JTable table, String tableName, String fileName){
		File file = new File(basePath_ + "Tables" + File.separator + fileName + tableName + ".txt");
		StringBuffer tableContent = new StringBuffer();
		TableModel model = table.getModel();
		String separator = "\t";
		try {
			FileWriter fileWriter = new FileWriter(file);
			String column = "";
			for (int i = 0; i < model.getColumnCount(); i++) {
                column += model.getColumnName(i) + separator;
            }
			fileWriter.write(column + "\n");
		    int j = 0;
		    String data = "";
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                	if(j!=model.getColumnCount()-1){
                		data += model.getValueAt(i, j).toString() + separator;
                	}
                	else{
                		data += model.getValueAt(i, j).toString() + "\n";
                	}
                	
                }
                fileWriter.write(data);
             }
            fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	/**
	 * Used to sort the hashmaps.
	 * http://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
	 * 
	 * @param totalResult hashmap to be sorted
	 * @return sorted hashmap
	 * @author Jennifer Terpstra
	 */
	public LinkedHashMap sortHashMapByValuesD(Map<String, Integer> totalResult) {
		List mapKeys = new ArrayList(totalResult.keySet());
		List mapValues = new ArrayList(totalResult.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);
		Collections.reverse(mapValues);
		Collections.reverse(mapKeys);

		LinkedHashMap sortedMap = new LinkedHashMap();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = totalResult.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					totalResult.remove(key);
					mapKeys.remove(key);
					sortedMap.put((String) key, (int) val);
					break;
				}

			}

		}
		return sortedMap;
	}
	
	/**
	 * Pops up whenever an exception occurs in a try/catch
	 * @param strIN Error message to be displayed
	 * 
	 * @author Jennifer Terpstra
	 */
	private void warningFrame(String strIN) {
		JFrame wrngFrame = new JFrame();
		wrngFrame.setBounds(200, 200, 1500, 100);
		wrngFrame.setLayout(null);
		wrngFrame.setVisible(true);

		JPanel backP = new JPanel();
		backP.setBounds(0, 0, 1500, 100);
		backP.setLayout(null);
		wrngFrame.add(backP);

		JLabel label = new JLabel("Warning! " + strIN);
		label.setBounds(25, 25, 1500, 25);
		backP.add(label);
	}
	
	public ArrayList<String> getTimedOut(){
		return timedOut;
	}

}
