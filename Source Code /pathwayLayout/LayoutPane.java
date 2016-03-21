package pathwayLayout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Prog.CmdController1;

public class LayoutPane extends JPanel {
	private static final long serialVersionUID = 1L;
	PathLayoutGrid grid;
	int xStepSize;
	int yStepSize;
	JPanel back;
	ArrayList<Node> nodes_;
	Node activeNode = null;
	boolean drawGrid = true;
	double buttonfac = 0.8D;
	boolean fieldOpen = false;
	boolean removeNode = false;
	boolean setComment = false;
	public static JTextField nameField;

	public LayoutPane(int xSize, int ySize, PathLayoutGrid Grid,
			ArrayList<Node> nodes) {
		nameField = new JTextField("Enter Pathname");

		this.grid = Grid;
		this.nodes_ = nodes;

		setVisible(true);
		setLayout(null);
		setLocation(0, 0);
		setBounds(0, 0, xSize, ySize);
		setBackground(Color.lightGray);

		addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseClicked(MouseEvent arg0) {
				final int x = arg0.getX();
				final int y = arg0.getY();
				if (LayoutPane.this.activeNode != null) {
					LayoutPane.this.activeNode.xPos = (x
							/ LayoutPane.this.xStepSize - 1);
					LayoutPane.this.activeNode.yPos = (y
							/ LayoutPane.this.yStepSize - 1);
					if (LayoutPane.this.activeNode.xPos < 0) {
						LayoutPane.this.activeNode.xPos = 0;
					}
					if (LayoutPane.this.activeNode.yPos < 0) {
						LayoutPane.this.activeNode.yPos = 0;
					}
					LayoutPane.this.adaptChildren(LayoutPane.this.activeNode);
					LayoutPane.this.activeNode = null;
					LayoutPane.this.reDo();
				} else if (!LayoutPane.this.fieldOpen) {
					LayoutPane.this.fieldOpen = true;
					final JTextField field = new JTextField("Name");
					field.setBounds(x, y, 100, 20);
					field.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							System.out.println("actionPerformed");
							LayoutPane.this.fieldOpen = false;
							if (!field.getText().isEmpty()) {
								Node node = new Node(field.getText());
								node.xPos = (x / LayoutPane.this.xStepSize - 1);
								node.yPos = (y / LayoutPane.this.yStepSize - 1);
								if (node.xPos < 0) {
									node.xPos = 0;
								}
								if (node.yPos < 0) {
									node.yPos = 0;
								}
								if (LayoutPane.this.setComment) {
									node.comment = LayoutPane.this.setComment;
									LayoutPane.this.setComment = false;
								}
								LayoutPane.this.grid.addNode(node);
								LayoutPane.this.remove(field);
								LayoutPane.this.reDo();
							} else {
								LayoutPane.this.remove(field);
								LayoutPane.this.invalidate();
								LayoutPane.this.validate();
								LayoutPane.this.repaint();
							}
						}
					});
					LayoutPane.this.add(field);
					LayoutPane.this.invalidate();
					LayoutPane.this.validate();
					LayoutPane.this.repaint();
				}
			}
		});
	}

	private void adaptChildren(Node node) {
		for (int i = 0; i < this.nodes_.size(); i++) {
			((Node) this.nodes_.get(i)).adaptConnections(node);
		}
	}

	public void reDo() {
		removeAll();
		if (getParent() != null) {
			setSize(getParent().getWidth(), getParent().getHeight());
		}
		setStepSize();
		setBackground(Color.lightGray);

		addButtons();
		addOptions();
		invalidate();
		validate();
		repaint();
	}

	private void addOptions() {
		JButton decGrid = new JButton("-");
		decGrid.setBounds(10, 45, 45, 45);
		decGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LayoutPane.this.grid.decGridSize();
				if (LayoutPane.this.setComment) {
					LayoutPane.this.setComment = false;
				}
				if (LayoutPane.this.activeNode != null) {
					LayoutPane.this.activeNode = null;
				}
				LayoutPane.this.fieldOpen = false;
				LayoutPane.this.reDo();
			}
		});
		add(decGrid);
		JButton incGrid = new JButton("+");
		incGrid.setBounds(10, 85, 45, 45);
		incGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LayoutPane.this.grid.incGridSize();
				if (LayoutPane.this.setComment) {
					LayoutPane.this.setComment = false;
				}
				if (LayoutPane.this.activeNode != null) {
					LayoutPane.this.activeNode = null;
				}
				LayoutPane.this.fieldOpen = false;
				LayoutPane.this.reDo();
			}
		});
		add(incGrid);
		System.err.println("addoptions  " + PathLayoutGrid.pathWayName);
		if (PathLayoutGrid.pathWayName != null) {
			if (!PathLayoutGrid.pathWayName.isEmpty()) {
				nameField.setText(PathLayoutGrid.pathWayName);
				System.out.println("!empty " + PathLayoutGrid.pathWayName);
			} else {
				nameField.setText("Enter Pathname");
				System.out.println("empty " + PathLayoutGrid.pathWayName);
			}
		} else {
			nameField.setText("Enter Pathname");
			System.out.println("null " + PathLayoutGrid.pathWayName);
		}
		nameField.setBounds(10, 10, 200, 30);
		add(nameField);

		JButton preview = new JButton("Preview");
		preview.setBounds(215, 10, 150, 30);
		preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (LayoutPane.this.setComment) {
					LayoutPane.this.setComment = false;
				}
				if (LayoutPane.this.activeNode != null) {
					LayoutPane.this.activeNode = null;
				}
				LayoutPane.this.fieldOpen = false;
				LayoutPane.this.grid.previewPathway();
			}
		});
		add(preview);

		JButton addComment = new JButton("Add comment node");
		addComment.setBounds(370, 10, 150, 30);
		if (this.setComment) {
			addComment.setBackground(Color.yellow);
		}
		addComment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LayoutPane.this.setComment = (!LayoutPane.this.setComment);
				if (LayoutPane.this.activeNode != null) {
					LayoutPane.this.activeNode = null;
				}
				LayoutPane.this.fieldOpen = false;
				LayoutPane.this.reDo();
			}
		});
		add(addComment);

		JButton forceMode = new JButton("forcelayout");
		forceMode.setBounds(525, 10, 150, 30);
		forceMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LayoutPane.this.grid.doForceLayout();
				LayoutPane.this.reDo();
			}
		});
		add(forceMode);

		JButton random = new JButton("randomlayout");
		random.setBounds(680, 10, 150, 30);
		random.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LayoutPane.this.grid
						.setNodesRandomly(LayoutPane.this.grid.nodes);
				LayoutPane.this.reDo();
			}
		});
		add(random);
	}

	private void setStepSize() {
		this.xStepSize = (getWidth() / (this.grid.xGridSize + 2));
		this.yStepSize = (getHeight() / (this.grid.yGridSize + 2));
	}

	private void addButtons() {
		this.nodes_ = this.grid.nodes;
		for (int i = 0; i < this.nodes_.size(); i++) {
			Node node = (Node) this.nodes_.get(i);
			addButton(node, i);
		}
	}

	private void addButton(Node node, int index) {
		JButton button = new JButton(node.getName());
		int charSpace = 8;
		int length = node.getName().length() * charSpace;
		if (length < 75) {
			length = 75;
		}
		int xPos = (int) (this.xStepSize + this.xStepSize * node.xPos - this.xStepSize
				* this.buttonfac / 2.0D);
		int yPos = (int) (this.yStepSize + this.yStepSize * node.yPos - this.yStepSize
				* this.buttonfac / 2.0D);
		button.setBounds(xPos, yPos, length,
				(int) (this.yStepSize * this.buttonfac));
		if (node.comment) {
			button.setBackground(Color.orange);
		}
		button.setVisible(true);
		if ((this.activeNode != null)
				&& (node.ownIndex == this.activeNode.ownIndex)) {
			if (node.comment) {
				button.setBackground(Color.green.darker());
			} else {
				button.setBackground(Color.green);
			}
		}
		final int indexF = index;
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LayoutPane.this.selectNode(indexF);
				LayoutPane.this.reDo();
			}
		});
		add(button);
		if ((this.activeNode != null) && (this.activeNode.ownIndex == index)) {
			addActiveNodeParts(node, index, xPos, yPos, length);
		}
	}

	private void addActiveNodeParts(Node node, int index, int xPos, int yPos,
			int length) {
		final int indexF = index;
		ImageIcon icon = null;
		try{
			icon = new ImageIcon(CmdController1.outPutPath_+"images"+File.separator+"DoNotDelThis.gif", "del Button gif");
		}
		catch (Exception e){
			System.out.println("Garbage can is gone!");
			System.exit(0);
		}
		int size = (int) (this.yStepSize * this.buttonfac / 2.0D);
		Image image = icon.getImage();
		Image newimg = image.getScaledInstance(size, size, 4);
		icon = new ImageIcon(newimg);
		JButton remButton = new JButton(icon);
		remButton.setBorder(null);
		remButton.setBounds(xPos + length, yPos + size, size, size);
		remButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LayoutPane.this.activeNode = null;
				LayoutPane.this.removeNode = true;
				LayoutPane.this.selectNode(indexF);
				LayoutPane.this.removeNode = false;
				LayoutPane.this.reDo();
			}
		});
		add(remButton);

		final JTextField field = new JTextField("Name");
		int ySize = (int) (this.yStepSize * this.buttonfac);
		field.setBounds(xPos, yPos + ySize, length, ySize);
		field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Node) LayoutPane.this.nodes_.get(indexF)).name = field
						.getText();
				LayoutPane.this.reDo();
				field.setVisible(false);
			}
		});
		field.setVisible(false);
		add(field);
		JButton editButt = new JButton("...");
		editButt.setBorder(null);
		editButt.setBounds(xPos + length, yPos, size, size);
		editButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				field.setVisible(!field.isVisible());
			}
		});
		add(editButt);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		int size = 10;
		int offset = size / 2;
		for (int i = 0; i < this.grid.xGridSize + 1; i++) {
			for (int j = 0; j < this.grid.yGridSize + 1; j++) {
				g2.setColor(Color.gray);
				g2.drawLine(this.xStepSize,
						this.yStepSize + this.yStepSize * j, this.xStepSize
								+ this.xStepSize * this.grid.xGridSize,
						this.yStepSize + this.yStepSize * j);
				g2.drawLine(this.xStepSize + this.xStepSize * i,
						this.yStepSize, this.xStepSize + this.xStepSize * i,
						this.yStepSize + this.yStepSize * this.grid.yGridSize);
			}
		}
		g2.setColor(Color.black);
		for (int i = 0; i < this.nodes_.size(); i++) {
			Node node = (Node) this.nodes_.get(i);
			for (int j = 0; j < node.conections.size(); j++) {
				if (node.comment) {
					g2.setColor(Color.blue);
				} else {
					g2.setColor(Color.black);
				}
				Node node2 = (Node) node.conections.get(j);
				int xPos = this.xStepSize + this.xStepSize * node.xPos;
				int yPos = this.yStepSize + this.yStepSize * node.yPos;
				int xPos2 = this.xStepSize + this.xStepSize * node2.xPos;
				int yPos2 = this.yStepSize + this.yStepSize * node2.yPos;
				g2.setStroke(new BasicStroke(3.0F));
				g2.drawLine(xPos, yPos, xPos2, yPos2);
				int dX = xPos - xPos2;
				int dy = yPos - yPos2;
				float length = (int) Math.sqrt(dX * dX + dy * dy);
				int vecX = (int) ((xPos - xPos2) / length * (0.3D * length));
				int vecY = (int) ((yPos - yPos2) / length * (0.3D * length));
				g2.setColor(Color.red);
				g2.fillOval(xPos2 + vecX - offset, yPos2 + vecY - offset, size,
						size);
				g2.setColor(Color.black);
			}
		}
	}

	private void selectNode(int index) {
		System.out.println("selectNode " + index);
		if (this.activeNode == null) {
			if (this.removeNode) {
				for (int j = 0; j < this.nodes_.size(); j++) {
					Node node = (Node) this.nodes_.get(j);
					for (int i = 0; i < node.conections.size(); i++) {
						Node conN = (Node) node.conections.get(i);
						if (index == conN.ownIndex) {
							node.conections.remove(conN);
							break;
						}
					}
				}
				this.grid.removeNode(index);
				this.removeNode = false;
				this.activeNode = null;
			} else {
				this.activeNode = ((Node) this.nodes_.get(index));
			}
		} else {
			System.out.println("selectNode " + this.activeNode.ownIndex + " "
					+ index);
			if (this.activeNode.ownIndex == index) {
				this.activeNode = null;
			} else {
				for (int i = 0; i < this.activeNode.conections.size(); i++) {
					Node conN = (Node) this.activeNode.conections.get(i);
					if (index == conN.ownIndex) {
						this.activeNode.conections.remove(conN);
						this.activeNode = null;
						return;
					}
				}
				this.activeNode.addConnection((Node) this.nodes_.get(index));
				this.activeNode = null;
			}
		}
		reDo();
	}
	//Not used
	protected ImageIcon createImageIcon(String path, String description) {
		ClassLoader cl = getClass().getClassLoader();
		URL imgURL = cl.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		}
		System.err.println("Couldn't find file: " + path);
		return null;
		
	}
}
