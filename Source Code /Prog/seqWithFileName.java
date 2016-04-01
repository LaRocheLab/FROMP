package Prog;

import java.util.LinkedHashMap;

public class seqWithFileName {
	
	private String FileName = "";
	private LinkedHashMap<String, String> IdSeq =new LinkedHashMap<String, String>();
	
	public seqWithFileName(){
		
		
	}
	
	public void setFileName(String newName){
		
		FileName = newName;
		
	}
	
	public String getFileName(){
		
		return FileName;
		
	}
	
	public void setIdSeq(LinkedHashMap<String, String> newIdseq){
		IdSeq = newIdseq;
	}
	public LinkedHashMap<String, String> getIdSeq(){
		return IdSeq;
	}
	public void addtoMap (String Id,String Seq){
		
		IdSeq.put(Id, Seq);
		
	}

}
