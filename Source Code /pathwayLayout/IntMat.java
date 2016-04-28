package pathwayLayout;

import java.util.ArrayList;

public class IntMat {
	public int numOfRows;
	public Integer[][] mat;

	public IntMat(ArrayList<Node> nodes) {
		this.mat = new Integer[nodes.size()][nodes.size()];
		this.numOfRows = nodes.size();
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {
				this.mat[i][j] = Integer.valueOf(0);
			}
		}
		Node node = null;
		Node node2 = null;
		for (int i = 0; i < nodes.size(); i++) {
			node = (Node) nodes.get(i);
			for (int j = 0; j < node.conections.size(); j++) {
				node2 = (Node) node.conections.get(j);

				this.mat[node.ownIndex][node2.ownIndex] = Integer.valueOf(1);
			}
		}
	}

	public IntMat(int size, boolean toIdentity) {
		this.mat = new Integer[size][size];
		this.numOfRows = size;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if ((toIdentity) && (i == j)) {
					this.mat[i][j] = Integer.valueOf(1);
				} else {
					this.mat[i][j] = Integer.valueOf(0);
				}
			}
		}
	}

	public IntMat add(IntMat addMat) {
		IntMat C = new IntMat(this.numOfRows, false);
		if (this.numOfRows != addMat.numOfRows) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}
		for (int i = 0; i < this.numOfRows; i++) {
			for (int j = 0; j < this.numOfRows; j++) {
				C.mat[i][j] = Integer.valueOf(this.mat[i][j].intValue()
						+ addMat.mat[i][j].intValue());
			}
		}
		return C;
	}

	public IntMat times(IntMat B) {
		IntMat A = this;
		if (A.numOfRows != B.numOfRows) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}
		IntMat C = new IntMat(A.numOfRows, false);
		for (int i = 0; i < C.numOfRows; i++) {
			for (int j = 0; j < C.numOfRows; j++) {
				for (int k = 0; k < A.numOfRows; k++) {
					int tmp63_61 = j;
					Integer[] tmp63_60 = C.mat[i];
					tmp63_60[tmp63_61] = Integer.valueOf(tmp63_60[tmp63_61]
							.intValue()
							+ A.mat[i][k].intValue()
							* B.mat[k][j].intValue());
				}
			}
		}
		return C;
	}

	public IntMat clone() {
		IntMat C = new IntMat(this.numOfRows, false);
		for (int i = 0; i < this.numOfRows; i++) {
			for (int j = 0; j < this.numOfRows; j++) {
				C.mat[i][j] = this.mat[i][j];
			}
		}
		return C;
	}

	public void printMat() {
		for (int i = 0; i < this.numOfRows; i++) {
			for (int j = 0; j < this.numOfRows; j++) {
				System.out.print(" " + this.mat[i][j] + " ");
			}
			System.out.println("");
		}
	}
}
