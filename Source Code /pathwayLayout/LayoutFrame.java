package pathwayLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import Prog.StartFromp1;

public class LayoutFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	PathLayoutGrid grid;
	JPanel back_;
	ArrayList<Node> nodes_;
	LayoutPane pane;
	JMenu menu_;
	JMenuBar menuBar_;
	final String basePath_ = StartFromp1.FolderPath;

	public LayoutFrame(int xSize, int ySize, PathLayoutGrid Grid,
			ArrayList<Node> nodes) {
		super("LayoutTest");
		this.nodes_ = nodes;
		setSize(new Dimension(xSize, ySize));
		setVisible(true);
		setLayout(null);
		File filet = new File("userPaths");
		if (!filet.exists()) {
			filet.mkdir();
		}
		this.grid = Grid;

		addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent arg0) {
			}

			public void componentResized(ComponentEvent arg0) {
				if (LayoutFrame.this.pane != null) {
					LayoutFrame.this.pane.reDo();
				}
			}

			public void componentMoved(ComponentEvent arg0) {
			}

			public void componentHidden(ComponentEvent arg0) {
			}
		});
		buildPicture();
	}

	private void addMenu() {
		this.menu_ = new JMenu("File");
		this.menuBar_ = new JMenuBar();

		this.menu_.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		this.menuBar_.add(this.menu_);

		new JButton();
		new JTextField("Enter Pathway name");

		JMenuItem miItem = new JMenuItem("New Pathway", 78);
		miItem.setAccelerator(KeyStroke.getKeyStroke(78, 8));
		miItem.getAccessibleContext().setAccessibleDescription(
				"Design a new Pathway");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Open Pathway", 79);
		miItem.setAccelerator(KeyStroke.getKeyStroke(79, 8));
		miItem.getAccessibleContext()
				.setAccessibleDescription("Open a Pathway");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("open");
				System.out.println("basepath " + LayoutFrame.this.basePath_);
				JFileChooser fChoose_ = new JFileChooser(
						LayoutFrame.this.basePath_ + "userPaths");
				fChoose_.setFileSelectionMode(0);
				fChoose_.setBounds(100, 100, 200, 20);
				fChoose_.setVisible(true);
				File file = new File(LayoutFrame.this.basePath_ + "userPaths"
						+ File.separator + ".");
				fChoose_.setSelectedFile(file);
				fChoose_.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if ((f.isDirectory())
								|| (f.getName().toLowerCase().endsWith(".pwm"))) {
							return true;
						}
						return false;
					}

					public String getDescription() {
						return ".pwm";
					}
				});
				if (fChoose_.showOpenDialog(LayoutFrame.this.getParent()) == 0) {
					try {
						String path = fChoose_.getSelectedFile()
								.getCanonicalPath();
						if (!path.endsWith(".pwm")) {
							path = path + ".pwm";
							System.out.println(".pwm");
						}
						LayoutFrame.this.grid.openPathWay(path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				LayoutFrame.this.buildPicture();
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Save Pathway", 83);
		miItem.setAccelerator(KeyStroke.getKeyStroke(83, 8));
		miItem.getAccessibleContext().setAccessibleDescription(
				"Save your Pathway");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (LayoutPane.nameField.getText().contentEquals(
						"Enter Pathname")) {
					LayoutPane.nameField.setForeground(Color.red);
					LayoutFrame.this.pane.reDo();
				} else if (LayoutFrame.this.grid.loadPath != null) {
					if (!LayoutFrame.this.grid.loadPath.isEmpty()) {
						PathLayoutGrid.pathWayName = LayoutPane.nameField
								.getText();
						LayoutFrame.this.grid
								.savePathway(LayoutFrame.this.grid.loadPath);
					} else {
						LayoutFrame.this.saveAs();
					}
				} else {
					LayoutFrame.this.saveAs();
				}
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Save Pathway as", 65);
		miItem.setAccelerator(KeyStroke.getKeyStroke(65, 8));
		miItem.getAccessibleContext().setAccessibleDescription(
				"Save Pathway as");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (LayoutPane.nameField.getText().contentEquals(
						"Enter Pathname")) {
					LayoutPane.nameField.setForeground(Color.red);
					LayoutFrame.this.pane.reDo();
				} else {
					LayoutFrame.this.saveAs();
					System.out.println("Save");
				}
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Preview", 80);
		miItem.setAccelerator(KeyStroke.getKeyStroke(80, 8));
		miItem.getAccessibleContext().setAccessibleDescription(
				"Preview pathway");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LayoutFrame.this.grid.previewPathway();
			}
		});
		this.menu_.add(miItem);

		miItem = new JMenuItem("Quit", 81);
		miItem.setAccelerator(KeyStroke.getKeyStroke(81, 8));
		miItem.getAccessibleContext().setAccessibleDescription("Quit designer");
		miItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.menu_.add(miItem);
		setJMenuBar(this.menuBar_);
	}

	private void saveAs() {
		JFileChooser fChoose_ = new JFileChooser(StartFromp1.FolderPath+ "userPaths");
		fChoose_.setFileSelectionMode(0);
		fChoose_.setBounds(100, 100, 200, 20);
		fChoose_.setVisible(true);
		File file = new File(this.basePath_ + "userPaths" + File.separator
				+ ".");
		fChoose_.setSelectedFile(file);
		fChoose_.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if ((f.isDirectory())
						|| (f.getName().toLowerCase().endsWith(".pwm"))) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return ".pwm";
			}
		});
		if (fChoose_.showSaveDialog(getParent()) == 0) {
			try {
				String path = fChoose_.getSelectedFile().getCanonicalPath();
				if (!path.endsWith(".pwm")) {
					path = path + ".pwm";
					System.out.println(".pwm");
				}
				PathLayoutGrid.pathWayName = LayoutPane.nameField.getText();
				this.grid.savePathway(path);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			invalidate();
			validate();
			repaint();
		}
	}

	public void buildPicture() {
		addMenu();
		addBackPanel();

		invalidate();
		validate();
		repaint();
	}

	private void addBackPanel() {
		if (this.pane != null) {
			remove(this.pane);
		} else {
			this.pane = new LayoutPane(getWidth(), getHeight(), this.grid,
					this.nodes_);
		}
		add(this.pane);
		this.pane.reDo();
		invalidate();
		validate();
		repaint();
	}
}
