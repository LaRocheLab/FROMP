package Prog;

/**
 * This class stores the results of parsing the JSON formatted response from
 * sending tryptic peptide sequences to http://unipept.ugent.be/search/single
 * in order to determine the sequences lowest common ancestor.
 * 
 * @author Jennifer Terpstra
 *
 */

public class LowestCommonAncestor {
	String peptide;
	int taxon_id;
	String taxon_name;
	String taxon_rank;
	
	public LowestCommonAncestor(){	
	}
	
	public LowestCommonAncestor(String peptide,int taxon_id,String taxon_name,String taxon_rank){
		this.peptide=peptide;
		this.taxon_id=taxon_id;
		this.taxon_name=taxon_name;
		this.taxon_rank=taxon_rank;
	}

	public String getPeptide() {
		return peptide;
	}

	public void setPeptide(String peptide) {
		this.peptide = peptide;
	}

	public int getTaxon_id() {
		return taxon_id;
	}

	public void setTaxon_id(int taxon_id) {
		this.taxon_id = taxon_id;
	}

	public String getTaxon_name() {
		return taxon_name;
	}

	public void setTaxon_name(String taxon_name) {
		this.taxon_name = taxon_name;
	}

	public String getTaxon_rank() {
		return taxon_rank;
	}

	public void setTaxon_rank(String taxon_rank) {
		this.taxon_rank = taxon_rank;
	}
	public String toString() {
		return "peptide=" + peptide + ", taxon_id="
				+ taxon_id + ", taxon_name=" + taxon_name + ", taxon_rank="
				+ taxon_rank + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((taxon_name == null) ? 0 : taxon_name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LowestCommonAncestor other = (LowestCommonAncestor) obj;
		if (taxon_name == null) {
			if (other.taxon_name != null)
				return false;
		} else if (!taxon_name.equals(other.taxon_name))
			return false;
		return true;
	}

	
	
	
	

}
