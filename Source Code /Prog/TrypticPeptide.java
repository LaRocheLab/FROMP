package Prog;

import java.util.ArrayList;

/**
 * This class stores each sequence present within the sequence files that were
 * specified to be read. The sequence undergoes a tryptic digest which segments
 * the sequence at K or R into a tryptic peptide. The resulted tryptic peptides 
 * from this digest are stored in an arraylist per unique identifer. Finally, the
 * tryptic peptides are sent to http://unipept.ugent.be/ to determine their lowest
 * common ancestor. Those results are also stored in an arraylist per unqie identifer. 
 * 
 * @author Jennifer Terpstra
 *
 */
public class TrypticPeptide {
	String uniqueIdentifier;
	LowestCommonAncestor identifiedTaxa;
	ArrayList<String> peptides;
	ArrayList<LowestCommonAncestor> lca;
	LowestCommonAncestor lowestClass;
	
	public TrypticPeptide(){
		
	}
	public TrypticPeptide(String uniqueIdentifier,ArrayList<String> peptides){
		this.uniqueIdentifier=uniqueIdentifier;
		this.peptides=peptides;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public ArrayList<String> getPeptides() {
		return peptides;
	}

	public void setPeptides(ArrayList<String> peptides) {
		this.peptides = peptides;
	}
	
	public ArrayList<LowestCommonAncestor> getLca() {
		return lca;
	}
	public void setLca(ArrayList<LowestCommonAncestor> lca) {
		this.lca = lca;
	}
	
	public LowestCommonAncestor getIdentifiedTaxa() {
		return identifiedTaxa;
	}
	public void setIdentifiedTaxa(LowestCommonAncestor identifiedTaxa) {
		this.identifiedTaxa = identifiedTaxa;
	}
	public LowestCommonAncestor getLowestClass() {
		return lowestClass;
	}
	public void setLowestClass(LowestCommonAncestor lowestClass) {
		this.lowestClass = lowestClass;
	}
	
	

}
