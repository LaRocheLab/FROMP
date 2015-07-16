package Objects;

//Pathways that ECs can be mapped to. Holds its name, id and whether or not it was a pathway designed by the user or if it was premade. 

public class Pathway {
	public String id_; // the id of the pathway
	public String name_; // pathway name
	private boolean selected = true; // whether or not this project will use this pathway
	public boolean userPathway = false; // whether or not this was a user built pathway
	public String pathwayInfoPAth; // info about the pathway

	public boolean isSelected() {
		return this.selected;
	}
	 
	public String getId_() {
		return id_;
	}

	public void setId_(String id_) {
		this.id_ = id_;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Pathway(String id, String name) {
		this.id_ = id;
		this.name_ = name;
		this.selected = true;
	}

	public Pathway(Pathway path) {
		this.id_ = path.id_;
		this.name_ = path.name_;
		this.selected = path.selected;
		this.userPathway = path.userPathway;
		if (this.userPathway) {
			this.id_ = this.name_;
		}
		this.pathwayInfoPAth = path.pathwayInfoPAth;
	}

	public boolean idBiggerId2(Pathway path2) {
		if (this.id_.contentEquals("-1")) {
			return true;
		}
		if (path2.id_.contentEquals("-1")) {
			return false;
		}
		for (int charCnt = 0; (charCnt < this.id_.length())
				&& (charCnt < path2.id_.length()); charCnt++) {
			if (this.id_.charAt(charCnt) > path2.id_.charAt(charCnt)) {
				return true;
			}
			if (this.id_.charAt(charCnt) < path2.id_.charAt(charCnt)) {
				return false;
			}
		}
		return false;
	}

	public boolean idSmallerId2(Pathway path2) {
		for (int charCnt = 0; (charCnt < this.id_.length())
				&& (charCnt < path2.id_.length()); charCnt++) {
			if (this.id_.charAt(charCnt) < path2.id_.charAt(charCnt)) {
				return true;
			}
			if (this.id_.charAt(charCnt) > path2.id_.charAt(charCnt)) {
				return true;
			}
		}
		return false;
	}
	
	public String getName(){
		return name_;
	}

}
