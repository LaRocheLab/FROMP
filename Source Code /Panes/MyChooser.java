package Panes;

import java.awt.Component;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

//an extension of JFileChooser. Not really that interesting

public class MyChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;

	public MyChooser(String lastPath_) {
		super(lastPath_);
	}

	protected JDialog createDialog(Component parent) throws HeadlessException {
		JDialog dlg = super.createDialog(parent);
		dlg.setLocation(20, 20);
		return dlg;
	}
}
