package Prog;

import javax.swing.JTable;

import org.jfree.data.general.DefaultPieDataset;

/**
 * Stores the tables and filename data from MetaProteomicAnaysis to be used by other classes.
 *  
 * @author Jennifer Terpstra
 *
 */
public class tableAndChartData {
	JTable table1;
	JTable table2;
	String fileName;
	DefaultPieDataset dataset;
	
	public tableAndChartData(){
		
	}

	public JTable getTable1() {
		return table1;
	}

	public void setTable1(JTable table1) {
		this.table1 = table1;
	}

	public JTable getTable2() {
		return table2;
	}

	public void setTable2(JTable table2) {
		this.table2 = table2;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public DefaultPieDataset getDataset() {
		return dataset;
	}

	public void setDataset(DefaultPieDataset dataset) {
		this.dataset = dataset;
	}
	

}
