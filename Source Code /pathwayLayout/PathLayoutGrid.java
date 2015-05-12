package pathwayLayout;

import Objects.EcNr;
import Objects.EcSampleStats;
import Objects.Pathway;
import Objects.PathwayWithEc;
import Objects.Sample;
import Panes.ImagePanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class PathLayoutGrid {
	Node[][] layout;
	ArrayList<Node> nodes;
	ArrayList<Node> minGraph;
	IntMat adjMat1;
	IntMat adjMat2;
	IntMat adjMat3;
	IntMat adjMat4;
	IntMat weightMat;
	int maxDist = 10;
	int xGridSize;
	int yGridSize;
	int xStepSize;
	int yStepSize;
	int charSpace = 8;
	double buttonfac = 0.8D;
	String loadPath;
	public static String pathWayName;
	private float minForce = 0.47F;
	private float minCnt = 1000.0F;

	public PathLayoutGrid(PathLayoutGrid grid) {
		this.nodes = copyNodeList(grid.nodes);

		this.maxDist = grid.maxDist;

		this.xGridSize = grid.xGridSize;
		this.yGridSize = grid.yGridSize;
	}

	public PathLayoutGrid(int xSize, int ySize, boolean designMode) {
		pathWayName = "Enter Pathname";
		this.layout = new Node[xSize][ySize];
		this.xGridSize = xSize;
		this.yGridSize = ySize;
		if (!designMode) {
			return;
		}
		if (this.nodes == null) {
			this.nodes = new ArrayList();
		}
		LayoutFrame frame = new LayoutFrame(800, 600, this, this.nodes);
	}

	public void randomMode(int times) {
		int minCost = 10000;
		int cost = 0;
		for (int i = 0; i < times; i++) {
			this.nodes = setNodesRandomly(this.nodes);
			cost = evaluate(this.nodes);
			if (cost < minCost) {
				minCost = cost;
				this.minGraph = copyNodeList(this.nodes);
				System.out.println("new minGraph:" + i + ", cost:" + cost);
			}
			System.out.println("Round:" + i + ", cost:" + cost);
		}
		LayoutFrame frame = new LayoutFrame(800, 600, this, this.minGraph);
	}

	public void doForceLayout() {
		if (this.nodes == null) {
			System.out.println("Nodes == null");
			return;
		}
		float webforce = 3000.0F;
		int rndCnt = 0;
		while (webforce > this.minForce) {
			webforce = calcNodeForces();
			moveNodesByForce();

			moveNodesToCenter();
			rndCnt++;
			if (rndCnt > 1000) {
				setNodesRandomly(this.nodes);
				rndCnt = 0;
			}
		}
	}

	private float calcNodeForces() {
		float webforce = 0.0F;
		for (int nodeCnt = 0; nodeCnt < this.nodes.size(); nodeCnt++) {
			Node srcNode = (Node) this.nodes.get(nodeCnt);
			srcNode.xForce = 0.0F;
			srcNode.yForce = 0.0F;
		}
		for (int nodeCnt = 0; nodeCnt < this.nodes.size(); nodeCnt++) {
			Node srcNode = (Node) this.nodes.get(nodeCnt);
			webforce += srcNode
					.calcForceByConnections(copyNodeList(this.nodes));
		}
		return webforce;
	}

	private void moveNodesByForce() {
		float minDist = 1.0F;
		for (int i = 0; i < this.nodes.size(); i++) {
			Node node = new Node((Node) this.nodes.get(i), true);
			node.moveByForce(this.xGridSize, this.yGridSize);
			if (positionTaken(node)) {
				System.out.println("moveByForce Position taken " + i);
			} else if (!node.minDistToConKept(minDist)) {
				System.out.println("minDistReached " + i);
			} else {
				((Node) this.nodes.get(i)).moveByForce(this.xGridSize,
						this.yGridSize);
			}
		}
	}

	public void moveNodesToCenter() {
		int xSum = 0;
		int ySum = 0;
		for (int i = 0; i < this.nodes.size(); i++) {
			Node node = (Node) this.nodes.get(i);
			xSum += node.xPos;
			ySum += node.yPos;
		}
		xSum /= this.nodes.size();
		ySum /= this.nodes.size();

		xSum = this.xGridSize / 2 - xSum;
		ySum = this.yGridSize / 2 - ySum;
		for (int i = 0; i < this.nodes.size(); i++) {
			Node node = (Node) this.nodes.get(i);
			if ((node.xPos + xSum >= 0) && (node.xPos + xSum <= this.xGridSize)) {
				node.xPos += xSum;
			}
			if ((node.yPos + ySum >= 0) && (node.yPos + ySum <= this.yGridSize)) {
				node.yPos += ySum;
			}
		}
	}

	public ArrayList<Node> copyNodeList(ArrayList<Node> nodes) {
		ArrayList<Node> retList = new ArrayList();
		for (int i = 0; i < nodes.size(); i++) {
			retList.add(new Node((Node) nodes.get(i), true));
		}
		return retList;
	}

	public void doGridLayout(float tMax, float tMin, ArrayList<Node> nodesList,
			float minCost, int repeatTimes, float reduceTemp, float pRate) {
		float temperature = tMax;
		nodesList = setNodesRandomly(nodesList);
		float cost = evaluate(nodesList);

		minCost = cost;

		this.minGraph = copyNodeList(nodesList);
		ArrayList<Node> tempGraph = new ArrayList();
		while (temperature > tMin) {
			for (int i = 0; i < repeatTimes; i++) {
				tempGraph = copyNodeList(neighbor(nodesList, pRate));
				float tmpCost = localMin(tempGraph);
				float random = (float) Math.random();
				if (random < Math.exp((cost - tmpCost) / temperature)) {
					cost = tmpCost;
					nodesList = copyNodeList(tempGraph);
					if (cost < minCost) {
						minCost = cost;
						this.minGraph = copyNodeList(nodesList);
					}
				}
			}
			temperature *= reduceTemp;
		}
	}

	private ArrayList<Node> neighbor(ArrayList<Node> nodesL, float pRate) {
		Random g = new Random();
		for (int i = 0; i < nodesL.size(); i++) {
			float random = (float) Math.random();
			if (random < pRate) {
				Node node = (Node) nodesL.get(i);
				while (positionTaken(node)) {
					node.xPos = g.nextInt(this.xGridSize);
					node.yPos = g.nextInt(this.yGridSize);
				}
			}
		}
		return nodesL;
	}

	private float localMin(ArrayList<Node> nodesL) {
		float cost = evaluate(nodesL);
		float minCost;
		do {
			minCost = cost;
			for (int i = 0; i < nodesL.size(); i++) {
				Node node = (Node) nodesL.get(i);
				int origX = node.xPos;
				int origY = node.yPos;
				for (int x = 0; x < this.xGridSize; x++) {
					for (int y = 0; y < this.yGridSize; y++) {
						if ((x != origX) && (y != origY)) {
							node.xPos = x;
							node.yPos = y;

							float trialCost = evaluate(nodesL);
							if (trialCost < minCost) {
								minCost = trialCost;
							} else {
								node.xPos = origX;
								node.yPos = origY;
							}
						}
					}
				}
			}
		} while (minCost < cost);
		return minCost;
	}

	public void testNodes() {
		Node a = new Node("0");
		Node b = new Node("1");
		Node c = new Node("2");
		Node d = new Node("3");
		Node e = new Node("4");
		Node f = new Node("5");
		Node g = new Node("6");
		Node h = new Node("7");
		Node i = new Node("8");

		a.addConnection(b);
		b.addConnection(c);
		c.addConnection(d);
		c.addConnection(e);
		d.addConnection(f);
		d.addConnection(g);
		a.addConnection(h);
		g.addConnection(i);

		addNode(a);
		addNode(b);
		addNode(c);
		addNode(d);
		addNode(e);
		addNode(f);
		addNode(g);
		addNode(h);
		addNode(i);
	}

	public void resetNodePos() {
		for (int i = 0; i < this.nodes.size(); i++) {
			Node node = (Node) this.nodes.get(i);
			node.xPos = -1;
			node.yPos = -1;
		}
	}

	public ArrayList<Node> setNodesRandomly(ArrayList<Node> nodes) {
		Random g = new Random();

		resetNodePos();
		for (int i = 0; i < nodes.size(); i++) {
			Node node = (Node) nodes.get(i);
			while (positionTaken(node)) {
				node.xPos = g.nextInt(this.xGridSize);
				node.yPos = g.nextInt(this.yGridSize);
			}
			System.out.println("NOde " + node.ownIndex + " Pos =(" + node.xPos
					+ ", " + node.yPos + ")");
		}
		return nodes;
	}

	public boolean positionTaken(Node node) {
		if (node.xPos < 0) {
			return true;
		}
		for (int i = 0; i < this.nodes.size(); i++) {
			Node compare = (Node) this.nodes.get(i);
			if (node.ownIndex != compare.ownIndex) {
				if ((node.xPos == compare.xPos) && (node.yPos == compare.yPos)) {
					System.out.println("compare " + i);
					return true;
				}
			}
		}
		return false;
	}

	public void buildAdjMAt(ArrayList<Node> nodeL) {
		IntMat adjMat = new IntMat(nodeL);
		IntMat ident = new IntMat(nodeL.size(), true);
		System.out.println("ident");
		ident.printMat();
		System.out.println("adj");
		adjMat.printMat();

		this.adjMat1 = adjMat.add(ident);

		System.out.println("adj");
		this.adjMat1.printMat();

		IntMat b = adjMat.clone();

		this.adjMat2 = this.adjMat1.times(b);
		System.out.println("adj");
		this.adjMat2.printMat();
		this.adjMat3 = this.adjMat2.times(b);
		System.out.println("adj");
		this.adjMat3.printMat();
		this.adjMat4 = this.adjMat3.times(b);
		System.out.println("adj");
		this.adjMat4.printMat();
	}

	public int evaluate(ArrayList<Node> nodes) {
		int sum = 0;
		for (int i = 0; i < nodes.size() - 1; i++) {
			for (int j = i + 1; j < nodes.size(); j++) {
				sum += getCost((Node) nodes.get(i), (Node) nodes.get(j));
			}
		}
		System.out.println("evaluate sum =" + sum);
		return sum;
	}

	public int getCost(Node a, Node b) {
		int Wij = this.weightMat.mat[a.ownIndex][b.ownIndex].intValue();
		int ret = 0;
		int dist = getDist(a, b);
		if (Wij >= 0) {
			ret = Wij * dist;
		} else {
			ret = Wij * Math.min(dist, this.maxDist);
		}
		return ret;
	}

	public int getDist(Node a, Node b) {
		int ret = Math.abs(a.xPos - b.xPos) + Math.abs(a.yPos - b.yPos);
		return ret;
	}

	public void buildWeightMAt(ArrayList<Node> nodeL) {
		this.weightMat = new IntMat(this.adjMat1.numOfRows, false);
		for (int i = 0; i < this.weightMat.numOfRows; i++) {
			for (int j = 0; j < this.weightMat.numOfRows; j++) {
				this.weightMat.mat[i][j] = Integer.valueOf(getWeight(i, j));
			}
		}
		System.out.println("weightMat");
		this.weightMat.printMat();
	}

	public int getWeight(int i, int j) {
		if (this.adjMat1.mat[i][j].intValue() > 0) {
			return 3;
		}
		if ((this.adjMat1.mat[i][j].intValue() == 0)
				&& (this.adjMat2.mat[i][j].intValue() > 0)) {
			return 1;
		}
		if ((this.adjMat2.mat[i][j].intValue() == 0)
				&& (this.adjMat3.mat[i][j].intValue() > 0)) {
			return 0;
		}
		if ((this.adjMat3.mat[i][j].intValue() == 0)
				&& (this.adjMat4.mat[i][j].intValue() > 0)) {
			return -1;
		}
		return -2;
	}

	private void updateIndex() {
		for (int i = 0; i < this.nodes.size(); i++) {
			Node node = (Node) this.nodes.get(i);
			if (node.ownIndex != i) {
				for (int j = 0; j < this.nodes.size(); j++) {
					if (i != j) {
						Node node2 = (Node) this.nodes.get(j);
						for (int h = 0; h < node2.conections.size(); h++) {
							Node con = (Node) node2.conections.get(h);
							if (con.ownIndex == node.ownIndex) {
								con.ownIndex = i;
							}
						}
					}
				}
				node.ownIndex = i;
			}
		}
	}

	public void savePathway(String path) {
		this.loadPath = path;
		updateIndex();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));

			out.write("<pathName>" + pathWayName);
			out.newLine();
			out.write("<xGridSize>" + this.xGridSize);
			out.newLine();
			out.write("<yGridSize>" + this.yGridSize);
			out.newLine();
			out.write("<nodes>");
			out.newLine();
			for (int i = 0; i < this.nodes.size(); i++) {
				Node node = (Node) this.nodes.get(i);
				out.write("<name>" + node.name);
				out.newLine();
				out.write("<index>" + node.ownIndex);
				out.newLine();
				if ((node.xPos != -1) && (node.yPos != -1)) {
					out.write("<xPos>" + node.xPos);
					out.newLine();
					out.write("<yPos>" + node.yPos);
					out.newLine();
				}
				out.write("<comment>" + node.comment);
				out.newLine();
				out.write("</node>");
				out.newLine();
			}
			out.write("</nodes>");
			out.newLine();
			out.write("<connections>");
			out.newLine();
			for (int i = 0; i < this.nodes.size(); i++) {
				Node node = (Node) this.nodes.get(i);
				out.write("<node>");
				out.newLine();
				out.write("<index>" + node.ownIndex);
				out.newLine();
				for (int j = 0; j < node.conections.size(); j++) {
					Node con = (Node) node.conections.get(j);
					out.write("<conIndex>" + con.ownIndex);
					out.newLine();
				}
				out.write("</node>");
				out.newLine();
			}
			out.write("</connections>");
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openPathWay(String path) {
		this.nodes = new ArrayList();
		this.loadPath = path;
		String name = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));

			String comp = "<pathName>";
			String line;
			while ((line = in.readLine()) != null) {
				// String line;
				comp = "<pathName>";
				if (line.startsWith(comp)) {
					pathWayName = line.substring(line.indexOf(">") + 1);
					name = pathWayName;
				}
				comp = "<xGridSize>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					this.xGridSize = Integer.valueOf(line).intValue();
				}
				comp = "<yGridSize>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					this.yGridSize = Integer.valueOf(line).intValue();
				}
				comp = "<nodes>";
				if (line.startsWith(comp)) {
					loadNodes(in);
				}
				if (this.nodes != null) {
					comp = "<connections>";
					if (line.startsWith(comp)) {
						loadConnections(in);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (pathWayName.isEmpty()) {
			pathWayName = name;
		}
	}

	private void loadConnections(BufferedReader in) {
		Node node = null;
		Node connec = null;
		try {
			String line;
			while ((line = in.readLine()) != null) {
				// String line;
				String comp = "<index>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					int index = Integer.valueOf(line).intValue();
					node = (Node) this.nodes.get(index);
				}
				comp = "<conIndex>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					int conIndex = Integer.valueOf(line).intValue();
					connec = (Node) this.nodes.get(conIndex);
					node.addConnection(connec);
				}
				comp = "</connections>";
				if (line.startsWith(comp)) {
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadNodes(BufferedReader in) {
		System.out.println("loadNodes");

		Node node = null;
		Node.resetIndexCnt();
		try {
			String line;
			while ((line = in.readLine()) != null) {
				// String line;
				String comp = "<name>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					node = new Node(line);
				}
				comp = "<index>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					if (node != null) {
						node.ownIndex = Integer.valueOf(line).intValue();
					}
				}
				comp = "<xPos>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					if (node != null) {
						node.xPos = Integer.valueOf(line).intValue();
					}
				}
				comp = "<yPos>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					if (node != null) {
						node.yPos = Integer.valueOf(line).intValue();
					}
				}
				comp = "<comment>";
				if (line.startsWith(comp)) {
					line = line.substring(line.indexOf(">") + 1);
					if ((node != null) && (line.contentEquals("true"))) {
						node.comment = true;
					}
				}
				comp = "</node>";
				if (line.startsWith(comp)) {
					addNode(node);
				}
				comp = "</nodes>";
				if (line.startsWith(comp)) {
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void previewPathway() {
		Pathway path = new Pathway("", "");
		BufferedImage image = buildPicture(new PathwayWithEc(path),
				new Sample());
		ImagePanel.showImage(image, pathWayName);
	}

	public BufferedImage buildPicture(PathwayWithEc path, Sample samp) {
		int width = 800;
		int height = 600;
		BufferedImage image = new BufferedImage(width, height, 1);
		this.xStepSize = (width / (this.xGridSize + 2));
		this.yStepSize = (height / (this.yGridSize + 2));
		Graphics2D g = image.createGraphics();

		int size = 10;
		int offset = size / 2;

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.black);
		if (path.name_ != null) {
			g.drawString("Pathname: " + path.name_, 5, 15);
		} else {
			g.drawString("Pathname: no name", 5, 15);
		}
		for (int i = 0; i < this.xGridSize + 1; i++) {
			for (int j = 0; j < this.yGridSize + 1; j++) {
				g.setColor(Color.gray);
				g.drawLine(this.xStepSize, this.yStepSize + this.yStepSize * j,
						this.xStepSize + this.xStepSize * this.xGridSize,
						this.yStepSize + this.yStepSize * j);
				g.drawLine(this.xStepSize + this.xStepSize * i, this.yStepSize,
						this.xStepSize + this.xStepSize * i, this.yStepSize
								+ this.yStepSize * this.yGridSize);
			}
		}
		g.setColor(Color.black);
		for (int i = 0; i < this.nodes.size(); i++) {
			Node node = (Node) this.nodes.get(i);
			for (int j = 0; j < node.conections.size(); j++) {
				if (node.comment) {
					g.setColor(Color.blue);
				} else {
					g.setColor(Color.black);
				}
				Node node2 = (Node) node.conections.get(j);
				int xPos = this.xStepSize + this.xStepSize * node.xPos;
				int yPos = this.yStepSize + this.yStepSize * node.yPos;
				int xPos2 = this.xStepSize + this.xStepSize * node2.xPos;
				int yPos2 = this.yStepSize + this.yStepSize * node2.yPos;
				g.setStroke(new BasicStroke(3.0F));
				g.drawLine(xPos, yPos, xPos2, yPos2);
				int dX = xPos - xPos2;
				int dy = yPos - yPos2;
				float length = (int) Math.sqrt(dX * dX + dy * dy);
				int vecX = (int) ((xPos - xPos2) / length * (0.3D * length));
				int vecY = (int) ((yPos - yPos2) / length * (0.3D * length));
				g.setStroke(new BasicStroke(1.0F));
				if (!node.comment) {
					g.fillOval(xPos2 + vecX - offset, yPos2 + vecY - offset,
							size, size);
				}
			}
		}
		addNodesInPicture(g, path, samp);
		return image;
	}

	private Graphics2D addNodesInPicture(Graphics2D g, PathwayWithEc path,
			Sample samp) {
		EcNr actEcNr = null;

		boolean ecInSample = false;
		int amount = 0;
		int boxLength = 0;
		for (int i = 0; i < this.nodes.size(); i++) {
			Node node = (Node) this.nodes.get(i);
			boxLength = node.name.length() * this.charSpace;
			if (boxLength < 50) {
				boxLength = 50;
			}
			int xPos = (int) (this.xStepSize + this.xStepSize * node.xPos - this.xStepSize
					* this.buttonfac / 2.0D);
			int yPos = (int) (this.yStepSize + this.yStepSize * node.yPos - this.yStepSize
					* this.buttonfac / 2.0D);
			int ySize = (int) (this.yStepSize * this.buttonfac);

			ecInSample = false;
			for (int j = 0; j < path.ecNrs_.size(); j++) {
				if (((EcNr) path.ecNrs_.get(j)).name_.contentEquals(node.name)) {
					ecInSample = true;
					actEcNr = (EcNr) path.ecNrs_.get(j);
				}
			}
			if (samp.singleSample_) {
				System.out.println("SingleSample" + node.comment);
				if (ecInSample) {
					g.setColor(samp.sampleCol_);
				} else {
					g.setColor(Color.white);
				}
				if (node.comment) {
					System.out.println("SingleSample");
					g.setColor(Color.lightGray);
					g.fillRoundRect(xPos, yPos, boxLength, ySize, 5, 5);
				} else {
					g.fillRect(xPos, yPos, boxLength, ySize);
				}
			} else if (ecInSample) {
				ArrayList<EcSampleStats> stats = actEcNr.stats_;
				int step = 0;

				amount = 0;
				for (int stsCnt = 0; stsCnt < stats.size(); stsCnt++) {
					amount += ((EcSampleStats) stats.get(stsCnt)).amount_;
				}
				float stepSize = boxLength / amount;
				for (int stsCnt = 0; stsCnt < stats.size(); stsCnt++) {
					Color col = ((EcSampleStats) stats.get(stsCnt)).col_;

					g.setColor(col);

					int length = (int) (((EcSampleStats) stats.get(stsCnt)).amount_ * stepSize);
					if (node.comment) {
						System.out.println("SingleSample");
						g.setColor(Color.lightGray);
						g.fillRoundRect(xPos, yPos, length, ySize, 5, 5);
					} else {
						g.fillRect(xPos + step, yPos, length, ySize);
					}
					step += length;
				}
			} else {
				g.setColor(Color.white);
				if (node.comment) {
					System.out.println("SingleSample");
					g.setColor(Color.lightGray);
					g.fillRoundRect(xPos, yPos, boxLength, ySize, 5, 5);
				} else {
					g.fillRect(xPos, yPos, boxLength, ySize);
				}
				g.fillRect(xPos, yPos, boxLength, ySize);
			}
			g.setColor(Color.black);
			if (node.comment) {
				g.drawRoundRect(xPos, yPos, boxLength,
						(int) (this.yStepSize * this.buttonfac), 5, 5);
			} else {
				g.drawRect(xPos, yPos, boxLength,
						(int) (this.yStepSize * this.buttonfac));
			}
			g.drawString(node.name, xPos + 10, yPos + ySize / 2 + 2);
		}
		return g;
	}

	public void removeNode(int index) {
		this.nodes.remove(index);

		Node.nodeIndexCnt -= 1;
		for (int i = 0; i < this.nodes.size(); i++) {
			Node node = (Node) this.nodes.get(i);
			if (node.ownIndex > index) {
				node.ownIndex -= 1;
			}
			for (int j = 0; j < node.conections.size(); j++) {
				Node node2 = (Node) node.conections.get(j);
				System.out.println(node2.name + " " + node2.ownIndex);
			}
		}
	}

	public void addNode(Node node) {
		if (this.nodes == null) {
			this.nodes = new ArrayList();
		}
		this.nodes.add(node);
	}

	public void incGridSize() {
		this.xGridSize += 1;
		this.yGridSize += 1;
	}

	public void decGridSize() {
		if (this.xGridSize > 0) {
			this.xGridSize -= 1;
		}
		if (this.yGridSize > 0) {
			this.yGridSize -= 1;
		}
	}
}
