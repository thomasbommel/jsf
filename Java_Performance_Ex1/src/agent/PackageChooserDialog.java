package agent;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class PackageChooserDialog extends JDialog {
	
	private final List<PackagesChangedListener> listeners;
	
	private final JTextArea txtAreaPackages;

	public PackageChooserDialog() {
		super((JDialog) null, "Package Chooser");
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		listeners = new ArrayList<>();
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		txtAreaPackages = new JTextArea();
		txtAreaPackages.setPreferredSize(new Dimension(500, 300));
		
		JPanel buttonPanel = new JPanel();
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(ev -> {
			for (PackagesChangedListener l : listeners) {
				l.packagesChanged(new ArrayList<String>(Arrays.asList(txtAreaPackages.getText().split("\n"))));
			}
			this.dispose();
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(ev -> {
			this.dispose();
		});
		
		buttonPanel.add(btnOK);
		buttonPanel.add(btnCancel);
		
		mainPanel.add(new JLabel("Enter package names (one per line, no additional whitespaces):"), BorderLayout.NORTH);
		mainPanel.add(txtAreaPackages, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		this.add(mainPanel);
	}
	
	public PackageChooserDialog (PackagesChangedListener initialListener) {
		this();
		this.addListener(initialListener);
	}
	
	public void packAndShow() {
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void addListener(PackagesChangedListener l) {
		listeners.add(l);
	}
	
	public void removeListener(PackagesChangedListener l) {
		listeners.remove(l);
	}
}
