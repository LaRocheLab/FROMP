package Prog;

import Objects.PathwayWithEc;
import Objects.Project;
import Objects.Sample;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is pretty well always in the background while in Fromp. It allows the user to save, load, open, etc. projects.
 */
public class Controller {
	public static Project project_=new Project(""); // The current project
	//data processor which does all parsing and hard computation of raw input files
	public static DataProcessor processor_; 
	StringReader reader_; // Used for reading inputfiles. Not java's native method. Found at Prog.StringReader
	static boolean dataChanged = true; 
	Color sysCol_; 

	public Controller(Color sysCol) {
		processor_ = null;
		this.reader_ = new StringReader();
		newProject("default");
		this.sysCol_ = sysCol;
	}

	public void newProject(String workPath) {
		clearProcessor();
		project_ = new Project(workPath);
		Project.imported = false;
	}

	public int loadProjFile(String path) {
		//project_ = new Project("");
		if (path.endsWith(".frp")) {
			project_.importProj(path);
			System.out.println("import");
			return 1;
		}
		return project_.loadProject(this.reader_.readTxt(path));
	}
//no need
	public int loadAnotherProjFile(String path) {
		if (path.endsWith(".frp")) {
			project_.importProj(path);
			System.out.println("import");
			return 1;
		}
		return project_.loadProject(this.reader_.readTxt(path));
	}

	public void openProject() {
		clearProcessor();
	}

	public static String saveProject() {
		if (project_ != null) {
			String path = project_.exportProj(null);
			// NewFrompFrame.addRecentPath(path);
			return path;
		}
		return "";
	}

	public static String saveProject(String tmpPath) {
		if (project_ != null) {
			String path = project_.exportProj(tmpPath);
			// NewFrompFrame.addRecentPath(path);
			return path;
		}
		return "";
	}

	public void setProjColor(Color col) {
		Project.setBackColor_(col);
	}

	public void computeSampleScores() {
		if (Project.dataChanged) {
			loadPathways(true);
		}
	}

	public BufferedImage showPathwayMap(Sample sample, PathwayWithEc path) {
		return processor_.alterPathway(sample, path);
	}

	public void clearProcessor() {
		processor_ = null;
		System.gc();
	}
	/**
	 * Load data from input file
	 * @param fullLoad
	 */
	public static void loadPathways(boolean fullLoad) {
		if (processor_ == null) {
			System.out.println("new processor");
			processor_ = new DataProcessor(project_);
		}
		if (Project.dataChanged) {
			System.out.println("Data changed");
			if (DataProcessor.newBaseData) {
				System.out.println("newBaseData");
				if (processor_ == null) {
					processor_ = new DataProcessor(project_);
				} 
				else {
					processor_.prepData();
				}
			}
			if (!fullLoad) {
				return;
			}
			processor_.reduce = Project.randMode_;
			if (!Project.imported) {
				project_.refreshProj();
			}
			processor_.processProject();
			dataChanged = false;
			Project.dataChanged = false;
		} 
		else {
			return;
		}
	}

	public boolean gotSamples() {// if the project has samples this method will return true
		if (project_ == null) {
			return false;
		}
		if (Project.samples_.isEmpty()) {
			return false;
		}
		return true;
	}

	public void copyTxtFile(String inPath, String outPath) {
		this.reader_.copyTxtFile(inPath, outPath);
	}

	public void writeScore(Sample sample, String saveAs, int mode) {
		processor_.setWorkPath(Project.workpath_);
		processor_.writeScore(sample, saveAs, mode);
	}

	public void loadIprMat(String IP) throws IOException {
		
		project_.importIprMat(IP);
		

	}
}
