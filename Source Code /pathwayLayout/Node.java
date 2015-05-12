package pathwayLayout;

import java.util.ArrayList;

public class Node {
	public boolean comment = false;
	public String name;
	public int xPos;
	public int yPos;
	public float xForce = 0.0F;
	public float yForce = 0.0F;
	public ArrayList<Node> conections;
	static int nodeIndexCnt = 0;
	public int oldIndex;
	public int ownIndex;
	public int switchNodeId = -1;

	public Node(String name) {
		this.name = name;
		this.conections = new ArrayList();
		this.ownIndex = nodeIndexCnt;
		nodeIndexCnt += 1;
		this.xPos = -1;
		this.yPos = -1;
	}

	public Node() {
		this.name = "";
		this.conections = new ArrayList();
		this.ownIndex = nodeIndexCnt;
		nodeIndexCnt += 1;
		this.xPos = -1;
		this.yPos = -1;
	}

	public Node(Node node, boolean withConections) {
		this.name = node.name;
		this.conections = new ArrayList();
		if (withConections) {
			for (int i = 0; i < node.conections.size(); i++) {
				Node copy = new Node((Node) node.conections.get(i), false);
				this.conections.add(copy);
			}
		}
		this.ownIndex = node.ownIndex;
		this.xPos = node.xPos;
		this.yPos = node.yPos;
		this.xForce = node.xForce;
		this.yForce = node.yForce;
	}

	public boolean isConnectedWith(Node b) {
		Node compareNode = null;
		for (int i = 0; i < this.conections.size(); i++) {
			compareNode = (Node) this.conections.get(i);
			if (b == compareNode) {
				return true;
			}
		}
		return false;
	}

	public void addConnection(Node node) {
		this.conections.add(node);
	}

	public void adaptConnections(Node node) {
		for (int i = 0; i < this.conections.size(); i++) {
			Node coNode = (Node) this.conections.get(i);
			if (coNode.ownIndex == node.ownIndex) {
				coNode.xPos = node.xPos;
				coNode.yPos = node.yPos;
			}
		}
	}

	public String getName() {
		if (this.name == null) {
			return String.valueOf(this.ownIndex);
		}
		if (this.name.isEmpty()) {
			return String.valueOf(this.ownIndex);
		}
		return this.name;
	}

	public static void resetIndexCnt() {
		nodeIndexCnt = 0;
	}

	public float calcForceByConnections(ArrayList<Node> nodes) {
		float pullConst = 0.04F;
		for (int i = 0; i < this.conections.size(); i++) {
			Node conNode = (Node) this.conections.get(i);
			float vecX = conNode.xPos - this.xPos;
			float vecY = conNode.yPos - this.yPos;
			float length = getLength(vecX, vecY);
			vecX /= length;
			vecY /= length;

			vecX *= pullConst * length;
			vecY *= pullConst * length;
			this.xForce += vecX;
			this.yForce += vecY;
			conNode.xForce -= vecX;
			conNode.yForce -= vecY;
		}
		for (int i = 0; i < nodes.size(); i++) {
			Node conNode = (Node) nodes.get(i);
			if ((!isConnectedWith(conNode))
					&& (this.ownIndex != conNode.ownIndex)) {
				float vecX = conNode.xPos - this.xPos;
				float vecY = conNode.yPos - this.yPos;
				float length = getLength(vecX, vecY);
				vecX /= length;
				vecY /= length;

				vecX *= -1.0F / (length * length);
				vecY *= -1.0F / (length * length);
				this.xForce += vecX;
				this.yForce += vecY;
			}
		}
		return getLength(this.xForce, this.yForce);
	}

	private float getLength(float vecX, float vecY) {
		float ret = (float) Math.abs(Math.sqrt(vecX * vecX + vecY * vecY));
		return ret;
	}

	public void moveByForce(int xSize, int ySize) {
		if ((this.xPos + this.xForce >= 0.0F)
				&& (this.xPos + this.xForce <= xSize)) {
			this.xPos = ((int) (this.xPos + this.xForce));
		}
		if ((this.yPos + this.yForce >= 0.0F)
				&& (this.yPos + this.yForce <= ySize)) {
			this.yPos = ((int) (this.yPos + this.yForce));
		}
	}

	public boolean minDistToConKept(float minDist) {
		for (int i = 0; i < this.conections.size(); i++) {
			Node conNode = (Node) this.conections.get(i);
			float vecX = conNode.xPos - this.xPos;
			float vecY = conNode.yPos - this.yPos;
			float length = getLength(vecX, vecY);
			if (length < minDist) {
				return false;
			}
		}
		return true;
	}
}
